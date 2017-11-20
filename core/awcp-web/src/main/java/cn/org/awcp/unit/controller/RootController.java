package cn.org.awcp.unit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.core.domain.design.context.data.DataDefine;
import cn.org.awcp.formdesigner.utils.ScriptEngineUtils;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.venson.controller.base.ControllerContext;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.util.CheckUtils;
import cn.org.awcp.venson.util.EmailUtil;
import cn.org.awcp.venson.util.SMSUtil;

@Controller
@RequestMapping("/")
public class RootController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired	
	private MetaModelOperateService metaModelOperateServiceImpl;

	@Autowired
	private FormdesignerService formdesignerServiceImpl;

	/**
	 * 获取部门树数据
	 * @return
	 */
	@RequestMapping(value = "queryDeptTreeData", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> queryDeptTreeData(String type) {
		String sql = "select Group_ID as ID,PARENT_GROUP_ID as PID,GROUP_CH_NAME as Name,NUMBER as NUMBER"
				+ " from p_un_group b order by case when NUMBER is null then 0 end , NUMBER asc";
		List<Map<String, Object>> documentList = metaModelOperateServiceImpl.search(sql);
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : documentList) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", map.get("ID"));
			// 为了不引起冲突的判断
			if ("manage".equals(type)) {
				if (!"".equals(map.get("NUMBER")) && map.get("NUMBER") != null) {
					m.put("name", map.get("Name") + "-" + map.get("NUMBER"));
				} else {
					m.put("name", map.get("Name"));
				}
			} else {
				m.put("name", map.get("Name"));
			}
			m.put("pId", map.get("PID"));
			m.put("url","punUserGroupController/getUsers.do?groupId=" + map.get("ID") + "&rootGroupId=" + map.get("PID"));
			m.put("target", "sysEditFrame");
			m.put("open", true);
			// 只有简单的选择功能
			if ("simple".equals(type)) {
				m.remove("url");
			}
			returnList.add(m);
		}
		return returnList;
	}

	/**
	 * 树性模板获取节点数
	 * 
	 * @param dynamicPageId
	 * @return
	 * @throws ScriptException
	 */
	@RequestMapping(value = "getTreeData", method = RequestMethod.POST)
	@ResponseBody
	public Object getTreeData(String dynamicPageId) throws ScriptException {
		DynamicPageVO page = formdesignerServiceImpl.findById(dynamicPageId);
		if (StringUtils.isNotBlank(page.getDataJson())) {
			DocumentVO docVo = getDocVo(dynamicPageId, page);
			JSONArray array = JSON.parseArray(StringEscapeUtils.unescapeHtml4(page.getDataJson()));
			DataDefine dd = JSON.parseObject(array.getString(0), DataDefine.class);
			ScriptEngine engine = getEngine(page, docVo);
			String script = dd.getSqlScript();
			String sql = null;
			if (StringUtils.isNotBlank(script)) {
				sql = (String) engine.eval(script);
			}
			List<Map<String, Object>> data = metaModelOperateServiceImpl.search(sql);
			// 处理url路径
			for (Map<String, Object> map : data) {
				String url = (String) map.get("url");
				if (StringUtils.isNotBlank(url)) {
					url = ControllerHelper.getBasePath() + url;
					map.put("url", url);
				}

			}
			return data;
		}

		return null;
	}

	private ScriptEngine getEngine(DynamicPageVO pageVO, DocumentVO docVo) {
		ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
		Map<String, Object> root = new HashMap<String, Object>();
		HttpServletRequest request = ControllerContext.getRequest();
		engine.put("request", request);
		engine.put("session", request.getSession());
		engine.put("root", root);
		return engine;
	}

	@RequestMapping(value = "getListData", method = RequestMethod.POST)
	@ResponseBody
	public ReturnResult getListData(HttpServletRequest request, String dynamicPageId) {
		ReturnResult result = ReturnResult.get();
		result.setStatus(StatusCode.SUCCESS);
		try {
			if (dynamicPageId == null) {
				result.setStatus(StatusCode.FAIL.setMessage("动态页面Id不能为空！"));
			}
			DynamicPageVO page = formdesignerServiceImpl.findById(dynamicPageId);

			if (page == null) {
				result.setStatus(StatusCode.FAIL.setMessage("动态页面没找到！"));
			}
			if (StringUtils.isBlank(page.getDataJson())) {
				result.setStatus(StatusCode.FAIL.setMessage("动态页面数据源脚本为空！"));
			}
			DocumentVO docVo = getDocVo(dynamicPageId, page);
			JSONArray array = JSON.parseArray(StringEscapeUtils.unescapeHtml4(page.getDataJson()));
			DataDefine dd = JSON.parseObject(array.getString(0), DataDefine.class);
			ScriptEngine engine = getEngine(page, docVo);
			String script = dd.getSqlScript();
			String sql = null;
			if (StringUtils.isNotBlank(script)) {

				sql = (String) engine.eval(script);

			}
			String ps = request.getParameter("pageSize");
			String cp = request.getParameter("currentPage");
			int pageSize = Integer.parseInt(StringUtils.isNumeric(ps) ? ps : "10");
			int currentPage = Integer.parseInt(StringUtils.isNumeric(cp) ? cp : "1");
			List<Map<String, Object>> data = metaModelOperateServiceImpl
					.search(sql + " limit " + (currentPage - 1) * pageSize + "," + pageSize);
			StringBuilder countSql = new StringBuilder();
			countSql.append("select count(*) from (").append(sql).append(") temp");
			result.setData(data).setTotal(countSql);
		} catch (Exception e) {
			result.setStatus(StatusCode.FAIL.setMessage("执行脚本出错！"));
			logger.info("ERROR", e);
		}
		return result;
	}

	private DocumentVO getDocVo(String dynamicPageId, DynamicPageVO page) {
		DocumentVO docVo = new DocumentVO();
		docVo.setDynamicPageId(page.getId().toString());
		docVo.setUpdate(true);
		docVo.setRecordId(dynamicPageId + "");
		docVo.setDynamicPageName(page.getName());
		return docVo;
	}

	/**
	 * 发送验证码
	 * 
	 * @param type
	 *            0：短信，1：邮箱
	 * @param to
	 *            发送对象(手机号或QQ邮箱)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "sendCode", method = RequestMethod.GET)
	public ReturnResult sendCode(@RequestParam(value = "type", required = false, defaultValue = "0") int type,
			@RequestParam("to") String to) {
		ReturnResult result = ReturnResult.get();
		String code;
		// 0是发送短信验证码
		if (type == 0) {
			if (!CheckUtils.isChinaPhoneLegal(to)) {
				return result.setStatus(StatusCode.FAIL.setMessage("手机号有误"));
			}
			code = SMSUtil.send(to);
			SessionUtils.addObjectToSession(SessionContants.SMS_VERIFY_CODE + to, code);
		} else {
			if (!CheckUtils.isLegalEmail(to)) {
				return result.setStatus(StatusCode.FAIL.setMessage("邮箱格式有误"));
			}
			code = EmailUtil.sendVerificationEmail(to);
			SessionUtils.addObjectToSession(SessionContants.SMS_VERIFY_CODE + to, code);
		}
		logger.debug(SessionContants.SMS_VERIFY_CODE + to + "--------------" + code);
		result.setStatus(StatusCode.SUCCESS);
		return result;
	}

}
