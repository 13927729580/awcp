package org.szcloud.framework.venson.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.venson.service.QueryService;

import BP.Port.WebUser;
import BP.WF.Port.AuthorWay;
import BP.WF.Port.WFEmp;
import BP.WF.Template.PubLib.WFState;

@Service
@Transactional(readOnly = true)
public class QueryServiceImpl implements QueryService {

	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;
	@Resource(name = "namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public Map<String, Object> getUntreatedData(int limit, int offset, String FK_Flow, String workItemName,
			String userName, boolean hasReturn) {
		Map<String, Object> result = new HashMap<String, Object>();
		String wfSql = "  a.WFState=" + WFState.Askfor.getValue() + " OR a.WFState=" + WFState.Runing.getValue()
				+ "  OR a.WFState=" + WFState.AskForReplay.getValue() + " OR a.WFState=" + WFState.Shift.getValue()
				+ "  OR a.WFState=" + WFState.Fix.getValue();
		if (hasReturn) {
			wfSql += " OR a.WFState=" + WFState.ReturnSta.getValue();
		}
		StringBuilder builder = new StringBuilder();
		builder.append(
				"SELECT a.FK_Flow,a.StarterName,a.FK_Node,a.FID,(CASE a.WFState WHEN 5 THEN 3  ELSE 0 END) WFState ,a.WorkID,a.title,a.nodeName,a.RDT  FROM WF_EmpWorks a  WHERE ("
						+ wfSql + ")");
		if (WebUser.getIsAuthorize() == false) {
			// 不是授权状态
			if (BP.WF.Glo.getIsEnableTaskPool() == true) {
				builder.append(" AND TaskSta!=1 ");
			}
			if (StringUtils.isNotBlank(userName)) {
				builder.append(" and a.FK_Emp= '" + userName + "' ");
			}
		} else {
			// 如果是授权状态, 获取当前委托人的信息.
			WFEmp emp = new WFEmp(userName);
			if (StringUtils.isNotBlank(emp.getNo())) {
				builder.append(" AND a.FK_Emp='" + emp.getNo() + "' ");
			}
			if (BP.WF.Glo.getIsEnableTaskPool() == true) {
				builder.append(" AND TaskSta!=0 ");
			}
			if (emp.getHisAuthorWay().equals(AuthorWay.SpecFlows)) {
				builder.append("' AND  FK_Flow IN " + emp.getAuthorFlows());
			}
		}
		if (StringUtils.isNotBlank(FK_Flow)) {
			builder.append(" and a.FK_Flow='" + FK_Flow + "' ");
		}

		if (StringUtils.isNotBlank(workItemName)) {
			builder.append(" and a.title like '%" + workItemName + "%' ");
		}
		builder.append(" GROUP BY a.workid ORDER BY a.SDTOfNode DESC ");
		int count = meta.queryOne("select count(1) from (" + builder.toString() + ") temp");
		result.put("count", count);
		if (count == 0) {
			result.put("data", Collections.EMPTY_LIST);
			return result;
		}
		builder.append(" limit " + offset + "," + limit);
		result.put("data", meta.search(builder.toString()));
		return result;
	}

	@Override
	public Map<String, Object> getReturnData(int limit, int offset, String FK_Flow, String workItemName,
			String userName) {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder builder = new StringBuilder();
		builder.append(
				"SELECT a.FK_Flow,a.StarterName,a.FK_Node,a.FID,3 WFState ,a.WorkID,a.title,a.nodeName,a.RDT  FROM WF_EmpWorks a  WHERE a.WFState=5 ");
		if (WebUser.getIsAuthorize() == false) {
			// 不是授权状态
			if (BP.WF.Glo.getIsEnableTaskPool() == true) {
				builder.append(" AND TaskSta!=1 ");
			}
			if (StringUtils.isNotBlank(userName)) {
				builder.append(" and a.FK_Emp= '" + userName + "' ");
			}
		} else {
			// 如果是授权状态, 获取当前委托人的信息.
			WFEmp emp = new WFEmp(userName);
			if (StringUtils.isNotBlank(emp.getNo())) {
				builder.append(" AND a.FK_Emp='" + emp.getNo() + "' ");
			}
			if (BP.WF.Glo.getIsEnableTaskPool() == true) {
				builder.append(" AND TaskSta!=0 ");
			}
			if (emp.getHisAuthorWay().equals(AuthorWay.SpecFlows)) {
				builder.append("' AND  FK_Flow IN " + emp.getAuthorFlows());
			}
		}
		if (StringUtils.isNotBlank(FK_Flow)) {
			builder.append(" and a.FK_Flow='" + FK_Flow + "' ");
		}

		if (StringUtils.isNotBlank(workItemName)) {
			builder.append(" and a.title like '%" + workItemName + "%' ");
		}
		builder.append(" GROUP BY a.workid ORDER BY a.SDTOfNode DESC ");
		int count = meta.queryOne("select count(1) from (" + builder.toString() + ") temp");
		result.put("count", count);
		if (count == 0) {
			result.put("data", Collections.EMPTY_LIST);
			return result;
		}
		builder.append(" limit " + offset + "," + limit);
		result.put("data", meta.search(builder.toString()));
		return result;
	}

	@Override
	public Map<String, Object> getHandledData(int limit, int offset, String FK_Flow, String workItemName,
			String userName) {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder builder = new StringBuilder();

		builder.append("select * , COUNT(DISTINCT workid) from ( ");

		builder.append(
				"(SELECT a.FK_Flow,a.StarterName,a.FK_Node,a.FID,a.WorkID,1 as WFState,a.title,a.nodeName,a.RDT,concat(a.EMPS,B.FK_Emp) as userName"
						+ ",( CASE WHEN NOW()>DATE_ADD(a.RDT, INTERVAL ( SELECT DeductDays FROM wf_node WHERE nodeid=a.FK_Node) MINUTE) THEN 1 ELSE 0 END) isovertime "
						+ " FROM WF_GenerWorkFlow A left join WF_GenerWorkerlist B on A.WorkID=B.WorkID  where  B.IsEnable=1 AND B.IsPass=1 ) ");

		builder.append(" union ");

		// 加入抄送的已处理件
		builder.append(
				"(SELECT b.FK_Flow,b.StarterName,b.FK_Node,b.FID,b.WorkID,1 as WFState,b.title,b.nodeName,b.RDT,a.CCTo as userName,"
						+ "( CASE WHEN NOW()>DATE_ADD(b.RDT, INTERVAL ( SELECT DeductDays FROM wf_node WHERE nodeid=b.FK_Node) MINUTE) THEN 1 ELSE 0 END)  isovertime"
						+ "   FROM wf_cclist a left join wf_generworkflow b on a.WorkID=b.WorkID where a.Sta!=0 AND b.WFSta=0 )");
		builder.append("  ) d  where WFState<>3   ");
		if (StringUtils.isNotBlank(FK_Flow)) {
			builder.append(" and d.FK_Flow='" + FK_Flow + "' ");
		}
		if (StringUtils.isNotBlank(workItemName)) {
			builder.append(" and d.title like '%" + workItemName + "%' ");
		}
		if (StringUtils.isNotBlank(userName)) {
			builder.append(" and d.userName like '%" + userName + "%' ");
		}
		builder.append(" GROUP BY d.workid order by d.rdt desc ");
		int count = meta.queryOne("select count(1) from (" + builder.toString() + ") temp");
		result.put("count", count);
		if (count == 0) {
			result.put("data", Collections.EMPTY_LIST);
			return result;
		}
		builder.append(" limit " + offset + "," + limit);
		result.put("data", meta.search(builder.toString()));
		return result;
	}

	@Override
	public Map<String, Object> getCompileData(int limit, int offset, String FK_Flow, String workItemName,
			String userName) {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder builder = new StringBuilder();

		builder.append("select * , COUNT(DISTINCT workid) from ( ");
		builder.append(
				"(select a.FK_Flow,a.StarterName,a.FK_Node,a.FID,a.WorkID,2 as WFState,a.title,a.nodeName,a.RDT,concat(a.EMPS,a.TodoEmps) as userName,a.WFSta as state from wf_generworkflow a  where a.WFSta=1 )  ");

		builder.append(" union ");

		// 加入抄送的办结件
		builder.append("(SELECT b.FK_Flow,b.StarterName,b.FK_Node,a.FID,b.WorkID,2 as WFState,b.title,b.nodeName,b.RDT,"
				+ "a.CCTo as userName,a.Sta as state FROM wf_cclist a left join wf_generworkflow b on a.WorkID=b.WorkID  "
				+ " where a.Sta!=0 AND b.WFSta=1 )");

		builder.append(" ) d  where 1=1 ");
		if (StringUtils.isNotBlank(FK_Flow)) {
			builder.append(" and d.FK_Flow='" + FK_Flow + "' ");
		}
		if (StringUtils.isNotBlank(workItemName)) {
			builder.append(" and d.title like '%" + workItemName + "%' ");
		}
		if (StringUtils.isNotBlank(userName)) {
			builder.append(" and d.userName like '%" + userName + "%' ");
		}
		builder.append("GROUP BY d.workid order by d.rdt desc");
		int count = meta.queryOne("select count(1) from (" + builder.toString() + ") temp");
		result.put("count", count);
		if (count == 0) {
			result.put("data", Collections.EMPTY_LIST);
			return result;
		}
		builder.append(" limit " + offset + "," + limit);
		result.put("data", meta.search(builder.toString()));
		return result;
	}

	@Override
	public Map<String, Object> getAllData(int limit, int offset, String FK_Flow, String workItemName, String userName) {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder builder = new StringBuilder();
		builder.append("select * , COUNT(DISTINCT workid) from ( ");
		builder.append(
				"(select a.FK_Flow ,a.StarterName,a.FK_Node,a.FID,a.WorkID,(CASE a.WFState WHEN 5 THEN 3 WHEN 3 THEN 2 ELSE 0 END) as WFState,a.title,a.nodeName,a.RDT,"
						+ "a.EMPS as userName,'0' isovertime from wf_generworkflow a where a.wfstate in(3,5) or todoemps like '%"
						+ userName + "%')");

		builder.append(" union ");

		builder.append(
				"(SELECT b.FK_Flow,b.StarterName,b.FK_Node,a.FID,b.WorkID,(CASE b.WFState WHEN 5 THEN 3 WHEN 3 THEN 2 ELSE 0 END) as WFState,b.title,b.nodeName,b.RDT,"
						+ "a.CCTo as userName,'0' isovertime FROM wf_cclist a left join wf_generworkflow b on a.WorkID=b.WorkID )");

		builder.append(" union ");

		builder.append(
				"(SELECT a.FK_Flow ,a.StarterName,a.FK_Node,a.FID,a.WorkID,1 as WFState,a.title,a.nodeName,a.RDT,b.FK_Emp as userName,"
						+ "( CASE WHEN NOW()>DATE_ADD(a.RDT, INTERVAL ( SELECT DeductDays FROM wf_node WHERE nodeid=a.FK_Node) MINUTE) THEN 1 ELSE 0 END) isovertime "
						+ "FROM WF_GenerWorkerlist B LEFT JOIN WF_GenerWorkFlow a ON A.WorkID=B.WorkID where  B.IsEnable=1 AND B.IsPass=1 and a.wfsta=0 )  ");
		builder.append(" ) d where 1=1 ");
		if (StringUtils.isNotBlank(FK_Flow)) {
			builder.append(" and d.FK_Flow='" + FK_Flow + "' ");
		}
		if (StringUtils.isNotBlank(workItemName)) {
			builder.append(" and d.title like '%" + workItemName + "%' ");
		}
		if (StringUtils.isNotBlank(userName)) {
			builder.append(" and d.userName like '%" + userName + "%' ");
		}
		builder.append(" GROUP BY d.workid ORDER BY d.WFState ASC ,d.rdt DESC ");

		int count = meta.queryOne("select count(1) from (" + builder.toString() + ") temp");
		result.put("count", count);
		if (count == 0) {
			result.put("data", Collections.EMPTY_LIST);
			return result;
		}
		builder.append(" limit " + offset + "," + limit);
		result.put("data", meta.search(builder.toString()));
		return result;
	}

}
