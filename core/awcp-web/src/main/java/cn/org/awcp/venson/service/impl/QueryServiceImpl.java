package cn.org.awcp.venson.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.service.QueryService;
import cn.org.awcp.venson.util.DateFormaterUtil;
import BP.WF.Port.AuthorWay;
import BP.WF.Template.PubLib.WFState;

@Service
@Transactional(readOnly = true)
public class QueryServiceImpl implements QueryService {

	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;
	@Resource(name = "namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	// 流程超时设置值：上线值设定为：DAY，测试值设定为：MINUTE
	private static final String FLOW_OVERTIME_VALUE = "DAY";

	/** 在办件 */
	private static final String UNTREATED = "0";
	/** 在办件 */
	private static final String HANDLED = "1";
	/** 在办件 */
	private static final String COMPILE = "2";
	/** 退回件 */
	private static final String RETURN = "3";

	public Map<String, Object> getUntreatedData(int limit, int offset, String FK_Flow, String workItemName,
			String userName, boolean hasReturn) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<>();
		String wfSql = "  a.WFState=" + WFState.Askfor.getValue() + " OR a.WFState=" + WFState.Runing.getValue()
				+ "  OR a.WFState=" + WFState.AskForReplay.getValue() + " OR a.WFState=" + WFState.Shift.getValue()
				+ "  OR a.WFState=" + WFState.Fix.getValue();
		if (hasReturn) {
			wfSql += " OR a.WFState=" + WFState.ReturnSta.getValue();
		}
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT a.FK_Flow,a.FlowName,a.StarterName,a.FK_Node,a.FID,(CASE a.WFState WHEN 5 THEN " + RETURN
				+ "  ELSE " + UNTREATED + " END) WFState ," + "a.WorkID,a.title,a.nodeName,a.RDT  FROM WF_EmpWorks a ");
		builder.append("  WHERE (" + wfSql + ") ");
		// 包含授权
		includeAuth(userName, builder, params);

		if (StringUtils.isNotBlank(FK_Flow)) {
			params.put("FK_Flow", FK_Flow);
			builder.append(" and a.FK_Flow=:FK_Flow ");
		}

		if (StringUtils.isNotBlank(workItemName)) {
			params.put("title", workItemName);
			builder.append(" and a.title like concat('%',:title,'%') ");
		}
		builder.append(" GROUP BY a.workid ORDER BY a.SDTOfNode DESC ");
		int count = jdbcTemplate.queryForObject("select count(1) from (" + builder.toString() + ") temp", params,
				Integer.class);
		result.put(SC.TOTAL, count);
		if (count == 0) {
			result.put(SC.DATA, Collections.EMPTY_LIST);
			return result;
		}
		builder.append(" limit " + offset + "," + limit);
		result.put(SC.DATA, jdbcTemplate.queryForList(builder.toString(), params));
		return result;
	}

	private void includeAuth(String userName, StringBuilder builder, Map<String, Object> params) {
		// 判断是否拥有授权信息并且是否超出授权日期.
		List<Map<String, Object>> authors = getAuthors(userName);
		if (!authors.isEmpty()) {
			Set<String> userNames = new HashSet<>();
			Set<String> authorFlows = new HashSet<>();
			for (Map<String, Object> author : authors) {
				String authorToDate = (String) author.get("authorToDate");
				if (new Date().before(DateFormaterUtil.stringToDate(authorToDate))) {
					String no = (String) author.get("no");
					// 录入授权人的信息
					Integer authorway = (Integer) author.get("AuthorWay");
					if (StringUtils.isNotBlank(no)) {
						userNames.add(no);
					}
					if (authorway == AuthorWay.SpecFlows.getValue()) {
						String authorFlow = (String) author.get("authorFlows");
						authorFlows.add(authorFlow);
					}
				}

			}
			if (!userNames.isEmpty()) {
				builder.append(" AND (a.FK_Emp=:FK_Emp  ");
				params.put("FK_Emp", userName);
				int i = 0;
				for (String name : userNames) {
					builder.append(" or a.FK_Emp=:FK_Emp" + i + " ");
					params.put("FK_Emp" + i, name);
					i++;
				}
				builder.append(") ");
				if (!authorFlows.isEmpty()) {
					String flow = "";
					for (String s : authorFlows) {
						flow += s;
					}
					builder.append(" and a.FK_Flow in(:flows) ");
					String flows = "";
					for (String s : flow.split(",")) {
						flows = "'" + s + "',";
					}
					flows = flows.substring(0, flows.length() - 1);
					params.put("flows", flows);
				}
			} else {
				if (StringUtils.isNotBlank(userName)) {
					builder.append(" AND a.FK_Emp=:FK_Emp ");
					params.put("FK_Emp", userName);
				}
			}
		} else {
			if (StringUtils.isNotBlank(userName)) {
				builder.append(" AND a.FK_Emp=:FK_Emp ");
				params.put("FK_Emp", userName);
			}
		}
	}

	public List<Map<String, Object>> getAuthors(String userName) {
		List<Map<String, Object>> authors = meta
				.search("select no,authorToDate,authorFlows,AuthorWay from wf_emp where author=?", userName);
		return authors;
	}

	@Override
	public Map<String, Object> getReturnData(int limit, int offset, String FK_Flow, String workItemName,
			String userName) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<>();
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT a.FK_Flow,a.FlowName,a.StarterName,a.FK_Node,a.FID," + RETURN
				+ " WFState ,a.WorkID,a.title,a.nodeName,a.RDT  FROM WF_EmpWorks a  WHERE a.WFState=5 ");
		// 包含授权
		includeAuth(userName, builder, params);
		if (StringUtils.isNotBlank(FK_Flow)) {
			params.put("FK_Flow", FK_Flow);
			builder.append(" and a.FK_Flow=:FK_Flow ");
		}

		if (StringUtils.isNotBlank(workItemName)) {
			params.put("title", workItemName);
			builder.append(" and a.title like concat('%',:title,'%') ");
		}
		builder.append(" GROUP BY a.workid ORDER BY a.SDTOfNode DESC ");
		int count = jdbcTemplate.queryForObject("select count(1) from (" + builder.toString() + ") temp", params,
				Integer.class);
		result.put(SC.TOTAL, count);
		if (count == 0) {
			result.put(SC.DATA, Collections.EMPTY_LIST);
			return result;
		}
		builder.append(" limit " + offset + "," + limit);
		result.put(SC.DATA, jdbcTemplate.queryForList(builder.toString(), params));
		return result;
	}

	@Override
	public Map<String, Object> getHandledData(int limit, int offset, String FK_Flow, String workItemName,
			String userName) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<>();
		StringBuilder builder = new StringBuilder();

		builder.append("select * , COUNT(DISTINCT d.workid) from ( ");

		builder.append("(SELECT a.FK_Flow,a.FlowName,a.StarterName,a.FK_Node,a.FID,a.WorkID," + HANDLED
				+ " as WFState,a.title,a.nodeName,a.RDT,concat(a.todoemps,B.FK_Emp) as userName"
				+ ",( CASE WHEN NOW()>DATE_ADD(a.RDT, INTERVAL ( SELECT DeductDays FROM wf_node WHERE nodeid=a.FK_Node) "
				+ FLOW_OVERTIME_VALUE + ") THEN 1 ELSE 0 END) isovertime "
				+ " FROM WF_GenerWorkFlow A left join WF_GenerWorkerlist B on A.WorkID=B.WorkID  where  B.IsEnable=1 AND B.IsPass=1 ) ");

		builder.append(" union ");

		// 加入抄送的已处理件
		builder.append("(SELECT b.FK_Flow,b.FlowName,b.StarterName,b.FK_Node,b.FID,b.WorkID," + HANDLED
				+ " as WFState,b.title,b.nodeName,b.RDT,a.CCTo as userName,"
				+ "( CASE WHEN NOW()>DATE_ADD(b.RDT, INTERVAL ( SELECT DeductDays FROM wf_node WHERE nodeid=b.FK_Node) "
				+ FLOW_OVERTIME_VALUE + ") THEN 1 ELSE 0 END)  isovertime"
				+ "   FROM wf_cclist a left join wf_generworkflow b on a.WorkID=b.WorkID where a.Sta!=0 AND b.WFSta=0 )");
		builder.append("  ) d  where WFState<>3   ");
		if (StringUtils.isNotBlank(FK_Flow)) {
			params.put("FK_Flow", FK_Flow);
			builder.append(" and d.FK_Flow=:FK_Flow ");
		}

		if (StringUtils.isNotBlank(workItemName)) {
			params.put("title", workItemName);
			builder.append(" and d.title like concat('%',:title,'%') ");
		}
		if (StringUtils.isNotBlank(userName)) {
			builder.append(" AND d.userName like concat('%',:userName,'%') ");
			params.put("userName", userName);
		}
		builder.append(" GROUP BY d.workid order by d.rdt desc ");
		int count = jdbcTemplate.queryForObject("select count(1) from (" + builder.toString() + ") temp", params,
				Integer.class);
		result.put(SC.TOTAL, count);
		if (count == 0) {
			result.put(SC.DATA, Collections.EMPTY_LIST);
			return result;
		}
		builder.append(" limit " + offset + "," + limit);
		result.put(SC.DATA, jdbcTemplate.queryForList(builder.toString(), params));
		return result;
	}

	@Override
	public Map<String, Object> getCompileData(int limit, int offset, String FK_Flow, String workItemName,
			String userName) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<>();
		StringBuilder builder = new StringBuilder();

		builder.append("select * , COUNT(DISTINCT d.workid) from ( ");
		builder.append("(select a.FK_Flow,a.FlowName,a.StarterName,a.FK_Node,a.FID,a.WorkID," + COMPILE
				+ " as WFState,a.title,a.nodeName,a.RDT,concat(a.EMPS,a.TodoEmps) as userName,a.WFSta as state from wf_generworkflow a  where a.WFSta=1 )  ");

		builder.append(" union ");

		// 加入抄送的办结件
		builder.append("(SELECT b.FK_Flow,b.FlowName,b.StarterName,b.FK_Node,a.FID,b.WorkID," + COMPILE
				+ " as WFState,b.title,b.nodeName,b.RDT,"
				+ "a.CCTo as userName,a.Sta as state FROM wf_cclist a left join wf_generworkflow b on a.WorkID=b.WorkID  "
				+ " where a.Sta!=0 AND b.WFSta=1 )");

		builder.append(" ) d  where 1=1 ");
		if (StringUtils.isNotBlank(FK_Flow)) {
			params.put("FK_Flow", FK_Flow);
			builder.append(" and d.FK_Flow=:FK_Flow ");
		}

		if (StringUtils.isNotBlank(workItemName)) {
			params.put("title", workItemName);
			builder.append(" and d.title like concat('%',:title,'%') ");
		}
		if (StringUtils.isNotBlank(userName)) {
			builder.append(" AND d.userName like concat('%',:userName,'%') ");
			params.put("userName", userName);
		}
		builder.append("GROUP BY d.workid order by d.rdt desc");
		int count = jdbcTemplate.queryForObject("select count(1) from (" + builder.toString() + ") temp", params,
				Integer.class);
		result.put(SC.TOTAL, count);
		if (count == 0) {
			result.put(SC.DATA, Collections.EMPTY_LIST);
			return result;
		}
		builder.append(" limit " + offset + "," + limit);
		result.put(SC.DATA, jdbcTemplate.queryForList(builder.toString(), params));
		return result;
	}

	@Override
	public Map<String, Object> getAllData(int limit, int offset, String FK_Flow, String workItemName, String userName) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<>();
		StringBuilder builder = new StringBuilder();
		// WFState 0:待处理1:办理中2:已完成3:被驳回
		builder.append("select d.* , COUNT(DISTINCT d.workid) from ( ");
		String wfSql = "  a.WFState=" + WFState.Askfor.getValue() + " OR a.WFState=" + WFState.Runing.getValue()
				+ "  OR a.WFState=" + WFState.AskForReplay.getValue() + " OR a.WFState=" + WFState.Shift.getValue()
				+ "  OR a.WFState=" + WFState.Fix.getValue() + " OR a.WFState=" + WFState.ReturnSta.getValue();
		// 在办件
		builder.append(
				" (SELECT a.FK_Flow,a.FlowName,a.StarterName,a.FK_Node,a.FID,a.WorkID,(CASE a.WFState WHEN 5 THEN "
						+ RETURN + "  ELSE " + UNTREATED + " END) WFState ,"
						+ "a.title,a.nodeName,a.RDT,a.FK_Emp as userName,'0' isovertime  FROM WF_EmpWorks a  WHERE ("
						+ wfSql + "))");
		builder.append(" union ");
		// 办结件
		builder.append("(select a.FK_Flow,a.FlowName ,a.StarterName,a.FK_Node,a.FID,a.WorkID," + COMPILE
				+ " as WFState,a.title,a.nodeName,a.RDT,"
				+ "concat(a.EMPS,a.TodoEmps) as userName,'0' isovertime from wf_generworkflow a where a.WFSta=1 ) ");

		builder.append(" union ");

		// 抄送件
		builder.append(
				"(SELECT b.FK_Flow,b.FlowName,b.StarterName,0 as FK_Node,a.FID,b.WorkID,(CASE  WHEN a.sta=2 and b.WFState=3 THEN "
						+ COMPILE + " WHEN a.sta=2 THEN " + HANDLED + " ELSE " + UNTREATED
						+ " END) as WFState,b.title,b.nodeName,b.RDT,"
						+ "a.CCTo as userName,'0' isovertime FROM wf_cclist a left join wf_generworkflow b on a.WorkID=b.WorkID )");

		builder.append(" union ");

		// 已办件
		builder.append("(SELECT a.FK_Flow,a.FlowName ,a.StarterName,a.FK_Node,a.FID,a.WorkID," + HANDLED
				+ " as WFState,a.title,a.nodeName,a.RDT,b.FK_Emp as userName,"
				+ "( CASE WHEN NOW()>DATE_ADD(a.RDT, INTERVAL ( SELECT DeductDays FROM wf_node WHERE nodeid=a.FK_Node) "
				+ FLOW_OVERTIME_VALUE + ") THEN 1 ELSE 0 END) isovertime "
				+ "FROM WF_GenerWorkerlist B LEFT JOIN WF_GenerWorkFlow a ON A.WorkID=B.WorkID where  B.IsEnable=1 AND B.IsPass=1 and a.wfsta=0 )  ");
		builder.append(" ) d  where 1=1 ");
		if (StringUtils.isNotBlank(FK_Flow)) {
			params.put("FK_Flow", FK_Flow);
			builder.append(" and d.FK_Flow=:FK_Flow ");
		}

		if (StringUtils.isNotBlank(workItemName)) {
			params.put("title", workItemName);
			builder.append(" and d.title like concat('%',:title,'%') ");
		}
		if (StringUtils.isNotBlank(userName)) {
			builder.append(" AND d.userName like concat('%',:userName,'%') ");
			params.put("userName", userName);
		}
		int count = jdbcTemplate.queryForObject("select count(1) from (" + builder.toString() + ") temp", params,
				Integer.class);
		builder.append(" GROUP BY d.workid order by d.WFState ASC ,d.rdt DESC ");
		result.put(SC.TOTAL, count);
		if (count == 0) {
			result.put(SC.DATA, Collections.EMPTY_LIST);
			return result;
		}
		builder.append(" limit " + offset + "," + limit);
		result.put(SC.DATA, jdbcTemplate.queryForList(builder.toString(), params));
		return result;
	}

}
