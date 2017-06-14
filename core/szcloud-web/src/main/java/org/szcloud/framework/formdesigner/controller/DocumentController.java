package org.szcloud.framework.formdesigner.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.base.BaseController;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.utils.BeanUtils;
import org.szcloud.framework.core.utils.DateUtils;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.Springfactory;
import org.szcloud.framework.core.utils.Tools;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.core.utils.mongodb.MongoDBUtils;
import org.szcloud.framework.formdesigner.application.service.DocumentService;
import org.szcloud.framework.formdesigner.application.service.FormdesignerService;
import org.szcloud.framework.formdesigner.application.service.PrintService;
import org.szcloud.framework.formdesigner.application.service.StoreService;
import org.szcloud.framework.formdesigner.application.vo.DocumentVO;
import org.szcloud.framework.formdesigner.application.vo.DynamicPageVO;
import org.szcloud.framework.formdesigner.application.vo.PageActVO;
import org.szcloud.framework.formdesigner.application.vo.StoreVO;
import org.szcloud.framework.formdesigner.core.domain.DynamicPage;
import org.szcloud.framework.formdesigner.core.domain.design.context.act.PageAct;
import org.szcloud.framework.formdesigner.core.domain.design.context.data.DataDefine;
import org.szcloud.framework.formdesigner.core.engine.FreeMarkers;
import org.szcloud.framework.formdesigner.core.parse.bean.PageDataBeanWorker;
import org.szcloud.framework.formdesigner.engine.util.VirtualRequest;
import org.szcloud.framework.formdesigner.utils.DocUtils;
import org.szcloud.framework.formdesigner.utils.DocumentUtils;
import org.szcloud.framework.formdesigner.utils.ScriptEngineUtils;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.venson.controller.base.ControllerHelper;
import org.szcloud.framework.venson.service.QueryService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.itextpdf.text.DocumentException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

import jxl.Cell;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Controller
@RequestMapping("/document")
public class DocumentController extends BaseController {
	/**
	 * 日志对象
	 */
	protected static final Log logger = LogFactory.getLog(DocumentController.class);
	private static final int MAX_EXCEL_PAGE_SIZE = 64000;
	@Autowired
	private FormdesignerService formdesignerServiceImpl;
	@Autowired
	private DocumentService documentServiceImpl;

	@Autowired
	private StoreService storeServiceImpl;

	@Autowired
	private PrintService printServiceImpl;

	// @Autowired
	// private WorkflowProcessService workflowProcessService;
	@Resource(name = "queryServiceImpl")
	private QueryService query;
	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;

	/**
	 *
	 * 读取参数，如dynamicPageId、recordId等（SpringMVC已经帮忙做了） 校验参数 收集额外参数到Map中
	 * 根据参数Map和脚本环境，解析出sql语句，根据sql语句查询数据 根据数据初始化各组件的状态(各个脚本包括选项脚本) 读取模版 模版+数据+状态
	 * = 输出 只针对单数据源的？
	 *
	 * @param id
	 * @param dynamicPageId
	 * @param recordId
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/view")
	public String view(String id, String dynamicPageId, String docId, HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		// 校验必要参数

		// 查询模版
		// 渲染模版
		logger.debug(" view.do start");
		// 新增：id为空时，为新增，获取dp，解析成HTML返回
		if (StringUtils.isBlank(id) && StringUtils.isBlank(dynamicPageId)) {
			// TODO ERROR
		} else {
			DocumentVO docVo = null;
			DynamicPageVO pageVO = null;
			logger.debug("start init document and dynamicpage ");
			// initDocAndPage(docVo, pageVO, id, docId, dynamicPageId);
			if (StringUtils.isBlank(docId)) {
				pageVO = formdesignerServiceImpl.findById(Long.parseLong(dynamicPageId));
				docVo = new DocumentVO();
				docVo.setFlowTempleteId(request.getParameter("FK_Flow"));
				docVo.setWorkItemId(request.getParameter("WorkID"));
				docVo.setEntryId(request.getParameter("FK_Node"));
				docVo.setUpdate(false);
				docVo.setDynamicPageId(pageVO.getId().toString());
				if (StringUtils.isNotBlank(id)) {
					docVo.setUpdate(true);
					docVo.setRecordId(id);
				}
				if (StringUtils.isNotEmpty(request.getParameter("selectId"))) {
					docVo.setSelectId(request.getParameter("selectId"));
				}
				docVo.setDynamicPageName(pageVO.getName());
				// 把列表页面配置的属性加到docVo中，在模板解析的时候使用
				docVo.setShowTotalCount(pageVO.getShowTotalCount());
				docVo.setIsLimitPage(pageVO.getIsLimitPage());
				docVo.setPageSize(pageVO.getPageSize());
				docVo.setShowReverseNum(pageVO.getShowReverseNum());
				docVo.setReverseNumMode(pageVO.getReverseNumMode());
				docVo.setReverseSortord(pageVO.getReverseSortord());
				// TODO 设置当前用户
			} else {
				docVo = documentServiceImpl.findById(docId);
				docVo.setUpdate(true);
				if (StringUtils.isNotBlank(dynamicPageId)) {
					pageVO = formdesignerServiceImpl.findById(Long.parseLong(dynamicPageId));
				} else {
					pageVO = formdesignerServiceImpl.findById(Long.parseLong(docVo.getDynamicPageId()));
				}
				docVo.setDynamicPageId(pageVO.getId().toString());
				docVo.setDynamicPageName(pageVO.getName());
				// 把列表页面配置的属性加到docVo中，在模板解析的时候使用
				docVo.setShowTotalCount(pageVO.getShowTotalCount());
				docVo.setIsLimitPage(pageVO.getIsLimitPage());
				docVo.setPageSize(pageVO.getPageSize());
				docVo.setShowReverseNum(pageVO.getShowReverseNum());
				docVo.setReverseNumMode(pageVO.getReverseNumMode());
				docVo.setReverseSortord(pageVO.getReverseSortord());
			}
			logger.debug("end init document and dynamicpage");
			if (docVo == null || pageVO == null) {
				// TODO
			}
			logger.debug("start init request parameters ");
			Map<String, String> map = new HashMap<String, String>();
			Enumeration enumeration = request.getParameterNames();
			for (; enumeration.hasMoreElements();) {
				Object o = enumeration.nextElement();
				String name = o.toString();
				String[] values = request.getParameterValues(name);
				map.put(o.toString(), StringUtils.join(values, ";"));
			}
			logger.debug("end init request parameters ");
			docVo.setRequestParams(map);

			Map<String, Object> root = new HashMap<String, Object>();
			logger.debug("start init engine ");
			// 拿脚本执行引擎
			ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
			engine.put("request", request);
			engine.put("session", request.getSession());
			engine.put("root", root);
			logger.debug("end init engine ");
			// 分页
			Integer currentPage = 1;
			Integer pageSize = 50;
			if (StringUtils.isNotBlank(request.getParameter("currentPage"))) {
				currentPage = Integer.parseInt(request.getParameter("currentPage"));
			}
			/*
			 * if(StringUtils.isNotBlank(request.getParameter("pageSize"))){
			 * pageSize =Integer.parseInt(request.getParameter("pageSize")); }
			 */
			// 对用户配置的是否分页，以及每页记录条数进行解析
			if (StringUtils.isNotBlank(request.getParameter("pageSize"))) {
				// 如果request中有pageSize则用该pageSize
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			} else if (docVo.getPageSize() != null && !docVo.getPageSize().equals("")) {
				// 否则用用户配置的默认pageSize
				if (docVo.getIsLimitPage() != null && docVo.getIsLimitPage() == 1) {
					pageSize = Integer.parseInt(docVo.getPageSize().toString());
				} else {
					// 不分页处理,则限制在1000条
					pageSize = 1000;
				}
			}
			String orderBy = docVo.getRequestParams().get("orderBy");
			String allowOrderBy = docVo.getRequestParams().get("allowOrderBy");
			docVo.setAllowOrderBy(allowOrderBy);
			docVo.setOrderBy(orderBy);

			// 查找是否存在app页面绑定
			Object applist = meta.queryObject("select PAGEID_APP from p_un_page_binding where PAGEID_APP_LIST=?",
					pageVO.getId());
			docVo.setAppListPageId(applist.toString());

			logger.debug("start init dataMap ");
			Map<String, List<Map<String, String>>> dataMap = documentServiceImpl.initDocumentData(currentPage, pageSize,
					docVo, engine, pageVO);
			logger.debug("end init engine ");
			docVo.setListParams(dataMap);
			engine.put("DocumentUtils", new DocumentUtils(docVo, pageVO));

			/**
			 * key: componentId value: component 相关信息
			 */
			Map<String, String> others = new HashMap<String, String>();
			Map<String, JSONObject> status = new HashMap<String, JSONObject>();
			logger.debug("start find components ");

			JSONObject jcon = new JSONObject();
			jcon.put("relatePageId", pageVO.getId());
			jcon.put("componentType", "");
			List<JSONObject> components = formdesignerServiceImpl.getComponentByContainerWithColumn(jcon);
			logger.debug("end find components the size is " + components.size());
			logger.debug("start init components status");
			logger.debug("components : " + JSON.toJSONString(components, true));
			if (components != null && components.size() > 0) {
				for (int i = 0; i < components.size(); i++) {
					JSONObject component = components.get(i);
					logger.debug("component :  " + JSON.toJSONString(component));
					if (component != null) {

						JSONObject o = new JSONObject();

						String value = DocUtils.getComponentValue(component, dataMap, engine);
						int type = component.getIntValue("componentType");

						String optionsText = null;
						switch (type) {
						case 1010: // 计算值、禁用
							o.put("disabled",
									String.valueOf(computeStatus(component.getString("disabledScript"), engine)));
							break;
						case 1008:// 列框
							// if(pageVO.getPageType() == 1003) {
							String showScript = component.getString("showScript");
							if (StringUtils.isNotBlank(showScript)) {
								engine.eval(showScript);
							}
							// }
							// 计算列框的disabled值
							o.put("disabled",
									String.valueOf(computeStatus(component.getString("disabledScript"), engine)));
							// 下面三个 计算值、隐藏
						case 1014:
						case 1015:
						case 1009:
							o.put("hidden", String.valueOf(computeStatus(component.getString("hiddenScript"), engine)));
							o.put("isRequired", component.getString("isRequired"));
							break;
						case 1017:
							o.put("hidden", String.valueOf(computeStatus(component.getString("hiddenScript"), engine)));
							o.put("disabled",
									String.valueOf(computeStatus(component.getString("disabledScript"), engine)));
							break;
						case 1006:
							o.put("hidden", String.valueOf(computeStatus(component.getString("hiddenScript"), engine)));
							o.put("readonly",
									String.valueOf(computeStatus(component.getString("readonlyScript"), engine)));
							o.put("disabled",
									String.valueOf(computeStatus(component.getString("disabledScript"), engine)));
							String script = component.getString("optionScript");
							String ret = "";
							String markerText = "";
							if (StringUtils.isNotBlank(script)) {
								ret = (String) engine.eval(script);
							}
							markerText = "<option value=\"{0}\" {1}>{2}</option>";
							optionsText = getOptionText(ret, value, markerText);
							// o.put("optionText", optionsText);
							others.put(component.getString("name"), optionsText);
							break;
						case 1003:
							ret = "";
							o.put("hidden", String.valueOf(computeStatus(component.getString("hiddenScript"), engine)));
							o.put("readonly",
									String.valueOf(computeStatus(component.getString("readonlyScript"), engine)));
							o.put("disabled",
									String.valueOf(computeStatus(component.getString("disabledScript"), engine)));
							script = component.getString("optionScript");
							if (StringUtils.isNotBlank(script)) {
								ret = (String) engine.eval(script);
							}
							markerText = "<label class='radio-inline'><input type='checkbox' name='"
									+ component.getString("name") + "' value=\"{0}\" {1}>{2}</label>";
							optionsText = getOptionText(ret, value, markerText);
							// o.put("optionText", optionsText);
							others.put(component.getString("name"), optionsText);
							break;
						case 1004:
							ret = "";
							o.put("hidden", String.valueOf(computeStatus(component.getString("hiddenScript"), engine)));
							o.put("readonly",
									String.valueOf(computeStatus(component.getString("readonlyScript"), engine)));
							o.put("disabled",
									String.valueOf(computeStatus(component.getString("disabledScript"), engine)));
							script = component.getString("optionScript");
							if (StringUtils.isNotBlank(script)) {
								ret = (String) engine.eval(script);
							}
							markerText = "<label class='radio-inline'><input type='radio' name='"
									+ component.getString("name") + "' value=\"{0}\" {1}>{2}</label>";
							optionsText = getOptionText(ret, value, markerText);
							// o.put("optionText", optionsText);
							others.put(component.getString("name"), optionsText);
							break;
						case 1034:
							o.put("hidden", String.valueOf(computeStatus(component.getString("hiddenScript"), engine)));
							o.put("readonly",
									String.valueOf(computeStatus(component.getString("readonlyScript"), engine)));
							o.put("disabled",
									String.valueOf(computeStatus(component.getString("disabledScript"), engine)));
							script = component.getString("tab_url");
							Object val = (String) engine.eval(script);
							others.put(component.getString("name"), val + "");
							break;
						// 下面几种，只需执行同一段代码，所以没有break;计算值、隐藏、只读、禁用
						case 1001:
						case 1002:
						case 1005:
						case 1007:
						case 1011:
						case 1012:
						case 1013:
						case 1016:
						case 1020:
						case 1019:
						case 1029:
						case 1030:
						case 1031:
						case 1032:
						case 1033:
						case 1035:
						case 1036:
						case 1037:
							o.put("hidden", String.valueOf(computeStatus(component.getString("hiddenScript"), engine)));
							o.put("readonly",
									String.valueOf(computeStatus(component.getString("readonlyScript"), engine)));
							o.put("disabled",
									String.valueOf(computeStatus(component.getString("disabledScript"), engine)));
						default:
							break;
						}

						status.put(component.getString("name"), o);
						logger.debug(" status " + JSON.toJSONString(status.get(component.getString("name")))
								+ "\n others " + others.get(component.getString("name")));

					}
				}
			}
			logger.debug("end init components status");
			logger.debug("start init pageacts status");
			BaseExample actExample = new BaseExample();
			actExample.createCriteria().andEqualTo("dynamicPage_id", pageVO.getId()).andLike("code",
					StoreService.PAGEACT_CODE + "%");
			List<StoreVO> stores = storeServiceImpl.selectPagedByExample(actExample, 1, Integer.MAX_VALUE, null);
			Map<String, Map<String, Object>> pageActStatus = new HashMap<String, Map<String, Object>>();
			if (stores != null && stores.size() > 0) {
				for (StoreVO store : stores) {
					Map<String, Object> actState = new HashMap<String, Object>();
					PageAct act = JSON.parseObject(store.getContent(), PageAct.class);
					String hiddenScript = StringEscapeUtils.unescapeHtml4(act.getHiddenScript());
					Boolean chooseValidate = act.isChooseValidate();
					String chooseScript = StringEscapeUtils.unescapeHtml4(act.getChooseScript());
					if (act.getActType() == 2002) {// 初始化返回按钮的返回页面ID
						actState.put("backId", map.get("backId"));
					}
					// 执行隐藏脚本
					Boolean hiddenStatus = (Boolean) engine.eval(hiddenScript);
					actState.put("chooseValidate", chooseValidate);
					if (chooseValidate != null && !chooseValidate.equals("")) {
						Double chooseNum = (Double) engine.eval(chooseScript);
						actState.put("chooseNum", chooseNum);
					}

					actState.put("hidden", hiddenStatus);
					pageActStatus.put(act.getPageId(), actState);
				}
			}
			logger.debug("end init pageacts status");

			root.put("pageActStatus", pageActStatus);

			logger.debug("start excute preloadscript");
			// 执行页面加载前脚本
			String preLoadScript = StringEscapeUtils.unescapeHtml4(pageVO.getPreLoadScript());
			if (StringUtils.isNotBlank(preLoadScript)) {
				engine.eval(preLoadScript);
			}
			logger.debug("end excute preloadscript");

			String dataJson = pageVO.getDataJson();

			if (StringUtils.isNotBlank(dataJson)) {
				for (Iterator<String> it = dataMap.keySet().iterator(); it.hasNext();) {
					String key = it.next();
					Object values = dataMap.get(key);
					if (values != null) {
						Paginator page = (((PageList) values).getPaginator());
						root.put(key + "_paginator", page);
					}
				}
			}
			DocumentUtils docUtils = (DocumentUtils) engine.get("DocumentUtils");
			docVo = docUtils.getCurrentDocument();
			logger.debug("start find template");
			String templateString = documentServiceImpl.getTemplateString(pageVO);
			logger.debug("end find template");
			root.putAll(docVo.getListParams());
			root.put("others", others);
			root.put("status", status);
			root.put("request", request);
			logger.debug(
					"--------------------此处超强分割线-----------------view.do-----------------提交到后台的数据：-----------------------------------");
			logger.debug("提交到后台的数据：");
			logger.debug(docVo.getRequestParams().toString());
			logger.debug(
					"--------------------此处超强分割线----------------------------------查询回页面的数据：-----------------------------------");
			logger.debug("查询回页面的数据：");
			logger.debug(docVo.getListParams().toString());
			logger.debug(
					"----------------------------------------------此处超强分割线end---------------------------------------------------");
			Map<String, Object> sessionMap = new HashMap<String, Object>();
			Enumeration sessionEnumeration = request.getSession().getAttributeNames();
			for (; sessionEnumeration.hasMoreElements();) {
				Object o = sessionEnumeration.nextElement();
				String sessionName = o.toString();
				Object values = request.getSession().getAttribute(sessionName);
				sessionMap.put(o.toString(), values);
			}
			root.put("session", sessionMap);
			root.put("vo", docVo);
			root.put("currentPage", currentPage);

			logger.debug("start render template");
			String s = FreeMarkers.renderString(templateString, root);
			root.clear();
			templateString = null;
			logger.debug("end render template");
			logger.debug(" view.do end");
			response.setContentType("text/html;");
			response.getWriter().write(s);
		}
		return null;
	}

	/**
	 * 计算状态脚本
	 * 
	 * @param script
	 * @param engine
	 * @return
	 */
	private boolean computeStatus(String script, ScriptEngine engine) {
		if (StringUtils.isNotBlank(script)) {
			script = StringEscapeUtils.unescapeHtml4(script);
			Boolean status = null;
			try {
				status = (Boolean) engine.eval(script);
			} catch (ScriptException e) {
				logger.info("ERROR", e);
			}
			if (status != null)
				return status;
		}
		return false;
	}

	/**
	 * 根据选项脚本和值，得到选项的html代码
	 *
	 * @param ret
	 * @param value
	 * @param markerText
	 * @return
	 */
	private String getOptionText(String ret, String value, String markerText) {
		StringBuilder sb = new StringBuilder("");
		if (markerText != null && markerText.contains("<option")) {
			sb.append(MessageFormat.format(markerText, "", "", ""));
		}

		if (StringUtils.isBlank(value)) {
			value = "";
		}
		String[] optionStr = null;
		if (StringUtils.isNotBlank(ret)) {
			optionStr = ret.split("\\;");
		}
		String[] values = value.split(";");
		if (optionStr != null && optionStr.length > 0) {
			for (String str : optionStr) {
				String[] entry = str.split("\\=");
				if (entry != null && entry.length == 2) {
					if (Arrays.binarySearch(values, entry[0]) > -1) {
						sb.append(MessageFormat.format(markerText, entry[0],
								"checked=\"checked\" selected=\"selected\"", entry[1]));
					} else {
						logger.debug(str);
						sb.append(MessageFormat.format(markerText, entry[0], "", entry[1]));
					}
				}
			}
			return sb.toString();
		} else {
			return sb.toString();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/excute")
	public ModelAndView excuteAct(HttpServletRequest request, HttpServletResponse response) throws DocumentException {
		org.szcloud.framework.core.domain.SzcloudJdbcTemplate jdbcTemplate1 = Springfactory.getBean("jdbcTemplate");
		ModelAndView mv = new ModelAndView();
		String actId = request.getParameter("actId");
		String pageId = request.getParameter("dynamicPageId");
		String docId = request.getParameter("docId");
		String update = request.getParameter("update");
		String[] _selects = request.getParameterValues("_selects");// 列表页面才能使用
		if (StringUtils.isBlank(actId)) {
			mv.addObject("dynamicPageId", pageId);
			mv.addObject("docId", docId);
			mv.setViewName("redirect:/document/view.do");
		}
		DynamicPageVO pageVO = null;
		if (StringUtils.isNotBlank(pageId)) {
			pageVO = formdesignerServiceImpl.findById(Long.parseLong(pageId));
		}
		boolean isUpdate = false;
		if (StringUtils.isNotBlank(update)) {
			isUpdate = update.equalsIgnoreCase("true");
		}

		DocumentVO docVo = new DocumentVO();
		if (pageVO.getPageType() == 1002) { // 表单页面
			// 初始化表单数据
			docVo = documentServiceImpl.findById(docId);
			docVo.setUpdate(isUpdate);
			docVo.setDynamicPageId(pageId);
			docVo.setDynamicPageName(pageVO.getName());
		} else if (1003 == pageVO.getPageType()) { // 列表页面
			// 要返回的页面ID
			DocumentUtils.putThingIntoSession("backId", pageVO.getId());
		}

		Map<String, String> map = new HashMap<String, String>();
		Enumeration enumeration = request.getParameterNames();
		for (; enumeration.hasMoreElements();) {
			Object o = enumeration.nextElement();
			String name = o.toString();
			String[] values = request.getParameterValues(name);
			map.put(o.toString(), StringUtils.join(values, ";"));
		}
		logger.debug("--------此处超强分割线----------将执行按钮脚本,提交到后台的参数--------提交到后台的数据：----------------------");
		logger.debug(map.toString());
		logger.debug("---------此处超强分割线--------------END------------------");

		docVo.setRequestParams(map);
		docVo = documentServiceImpl.processParams(docVo);
		StoreVO store = storeServiceImpl.findById(actId);
		PageActVO act = JSON.parseObject(store.getContent(), PageActVO.class);
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) Tools.getObjectFromSession(SessionContants.CURRENT_USER);
		// 初始化脚本解释执行器,加载全局工具类
		ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
		engine.put("request", request);
		engine.put("session", request.getSession());

		// 如跳转、返回之类的
		if (StringUtils.isNotBlank(act.getServerScript())) {
			String ret = null;
			try {
				jdbcTemplate1.beginTranstaion();
				String script = StringEscapeUtils.unescapeHtml4(act.getServerScript());
				ret = (String) engine.eval(script);
				jdbcTemplate1.commit();
				if (ret != null && (ret.startsWith("redirect:") || ret.startsWith("forward:"))) {
					mv.setViewName(ret);
					return mv;
				}
			} catch (Exception e) {
				logger.info("ERROR", e);
				try {
					jdbcTemplate1.rollback();
				} catch (Exception e1) {
					logger.info("ERROR", e);
				}
			}
		} else {
			// 校验文档
			DocumentUtils utils = new DocumentUtils(docVo, pageVO);
			Integer actType = act.getActType();
			// 根据actType 执行其默认操作
			switch (actType) {
			case 2000:
				break;
			case 2001:// 保存--不带流程，
				if (user.getUserId() != null) {
					docVo.setLastmodifier(String.valueOf(user.getUserId()));
					docVo.setAuditUser(String.valueOf(user.getUserId()));
				}
				// 设置doc 记录为草稿状态
				docVo.setState("草稿");
				if (docVo.isUpdate()) {
					// 更新数据
					for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
						String o = it.next();
						utils.updateData(o);
					}
					// 更新document 记录
					// 最后修改时间
					docVo.setLastmodified(new Date(System.currentTimeMillis()));
					utils.saveDocument();

				} else {
					docVo.setCreated(new Date(System.currentTimeMillis())); // 创建时间
					docVo.setAuthorId(user.getUserId());// 作者
					docVo.setLastmodified(docVo.getCreated()); // 最后修改时间
					// 保存数据 向document插入数据
					for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
						docVo.setId("");
						String o = it.next();
						utils.saveData(o);
						utils.saveDocument();
					}
					utils.saveDocument();
				}
				break;
			case 2002:
				// TODO 返回按钮实现
				Long backId = null;
				Object obt = DocumentUtils.getThingFromSession("backId");
				if (obt != null) {
					backId = (Long) obt;
				} else {
					backId = Long.parseLong(act.getExtbute().get("target"));
				}
				mv.setViewName("redirect:/document/view.do?dynamicPageId=" + backId);
				return mv;
			case 2003:
				// TODO 删除动作实现
				if (_selects != null && _selects.length >= 1) {
					for (String select : _selects) {
						utils.deleteData(pageVO, select);
						BaseExample base = new BaseExample();
						base.createCriteria().andEqualTo("RECORD_ID", select);
						documentServiceImpl.deleteByExample(base);
					}
					String viewName = "redirect:/document/view.do?dynamicPageId=" + pageId;
					mv.setViewName(viewName);
					return mv;
				} else {
					// TODO 返回并提示没有选择记录
				}
				break;
			case 2008:// 保存带流程
				break;
			case 2009:// 新增
				mv.addObject("dynamicPageId", act.getExtbute().get("target"));
				mv.setViewName("redirect:/document/view.do");
				return mv;
			case 2010:// 打开或审批
				if (_selects != null && _selects.length == 1) {
					BaseExample baseExample = new BaseExample();
					baseExample.createCriteria().andEqualTo("RECORD_ID", _selects[0]);
					List<DocumentVO> documents = documentServiceImpl.findPageByExample(baseExample, 1,
							Integer.MAX_VALUE, null);
					List<DocumentVO> workflowDoc = new ArrayList<DocumentVO>();
					List<DocumentVO> noneProcessDoc = new ArrayList<DocumentVO>();
					if (documents != null && documents.size() > 0) {
						for (DocumentVO document : documents) {
							if (StringUtils.isNotBlank(document.getInstanceId())) {
								workflowDoc.add(document);
							} else {
								noneProcessDoc.add(document);
							}
						}
					}

					if (workflowDoc.size() > 0) {// 流程相关

					} else if (noneProcessDoc.size() > 0) {// 流程无关
						// 判断用户权限 有权限直接打开
						mv.setViewName("redirect:/document/view.do?recordId=" + _selects[0] + "&dynamicPageId="
								+ act.getExtbute().get("target"));
						return mv;
					}
				}
				break;
			case 2011:// 流程流转
				break;
			case 2012:// 打开
				if (_selects != null && _selects.length == 1) {
					BaseExample baseExample = new BaseExample();
					baseExample.createCriteria().andEqualTo("RECORD_ID", _selects[0]);
					List<DocumentVO> documents = documentServiceImpl.findPageByExample(baseExample, 1,
							Integer.MAX_VALUE, null);
					if (documents != null && documents.size() > 0) {
						// 只读方式打开
						mv.setViewName("redirect:/document/view.do?id=" + documents.get(0).getRecordId()
								+ "&dynamicPageId=" + documents.get(0).getDynamicPageId());
						return mv;
					} else {

					}
				}
				break;
			case 2013:// 审批
				if (_selects != null && _selects.length == 1) {
					BaseExample baseExample = new BaseExample();
					baseExample.createCriteria().andEqualTo("RECORD_ID", _selects[0]);
					List<DocumentVO> documents = documentServiceImpl.findPageByExample(baseExample, 1,
							Integer.MAX_VALUE, null);
					List<DocumentVO> workflowDoc = new ArrayList<DocumentVO>();
					List<DocumentVO> noneProcessDoc = new ArrayList<DocumentVO>();
					if (documents != null && documents.size() > 0) {
						for (DocumentVO document : documents) {
							if (StringUtils.isNotBlank(document.getWorkflowId())) {
								workflowDoc.add(document);
							} else {
								noneProcessDoc.add(document);
							}
						}
					}
					if (workflowDoc.size() > 0) {// 流程相关

					} else if (noneProcessDoc.size() > 0) {// 流程无关
						// 判断用户权限 有权限直接打开
						mv.setViewName("redirect:/document/view.do?recordId=" + _selects[0] + "&dynamicPageId="
								+ noneProcessDoc.get(0).getDynamicPageId());
						return mv;
					}
				} else {
					// 返回并提示选择只能选择一条记录
				}

				break;
			case 2014:// pdf打印
				// 1、准备参数
				String script = act.getExtbute().get("script");
				Map<String, String> params = new HashMap<String, String>();
				if (StringUtils.isNotBlank(script)) {
					try {
						params = (Map<String, String>) engine.eval(StringEscapeUtils.unescapeHtml4(script));
					} catch (ScriptException e2) {
						logger.info("ERROR", e2);
					}
				}
				List listPages = formdesignerServiceImpl.getChildListPages(pageVO.getId());
				StringBuilder sb = new StringBuilder();
				if (listPages != null && listPages.size() > 0) {
					for (int k = 0; k < listPages.size(); k++) {
						sb.append(listPages.get(k) + ",");
					}
				}

				Map totalMap = new HashMap();
				totalMap.put("pageVOId", pageVO.getId().toString());
				totalMap.put("listPages", sb.toString());
				totalMap.putAll(map);
				totalMap.putAll(params);

				VirtualRequest virtualRequest = new VirtualRequest(totalMap); // request的getParament无法重置，所有自己模拟一个virtualRequest用于存跳转参数
				try {
					print(virtualRequest, request, response);
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					logger.info("ERROR", e);
				}
				return null;

			case 2015:// 保存--不带流程，
				docVo.setLastmodifier(String.valueOf(user.getUserId()));
				docVo.setAuditUser(String.valueOf(user.getUserId()));
				// 设置doc 记录为草稿状态
				docVo.setState("草稿");
				if (docVo.isUpdate()) {
					// 更新数据
					for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
						String o = it.next();
						utils.updateData(o);
					}
					// 更新document 记录
					// 最后修改时间
					docVo.setLastmodified(new Date(System.currentTimeMillis()));
					utils.saveDocument();
				} else {
					// 创建时间
					docVo.setCreated(new Date(System.currentTimeMillis()));
					docVo.setAuthorId(user.getUserId());
					// 最后修改时间
					docVo.setLastmodified(docVo.getCreated());
					// 保存数据
					// 向document插入数据
					for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
						docVo.setId("");
						String o = it.next();
						utils.saveData(o);
						utils.saveDocument();
					}
					utils.saveDocument();
				}
				break;
			case 2016:// Excel导出
				// 1、准备参数
				String excelScript = act.getExtbute().get("script");
				Map<String, String> excelParams = new HashMap<String, String>();
				if (StringUtils.isNotBlank(excelScript)) {
					try {
						excelParams = (Map<String, String>) engine.eval(StringEscapeUtils.unescapeHtml4(excelScript));
					} catch (ScriptException e2) {
						logger.info("ERROR", e2);
					}
				}
				// excel模板的Id
				String templateFileId = act.getExtbute().get("templateFileId");
				// 导出数据的sql
				String sqlScript = act.getExtbute().get("sqlScript");
				String actSql = new String();
				if (StringUtils.isNotBlank(sqlScript)) {
					try {
						actSql = (String) engine.eval(StringEscapeUtils.unescapeHtml4(sqlScript));
					} catch (ScriptException e2) {
						logger.info("ERROR", e2);
					}
				}

				// 此处改为直接调用函数，参考打印pdf同样的调用方式
				Map paramMap = new HashMap();
				paramMap.put("actSql", actSql);
				paramMap.put("templateFileId", templateFileId);
				paramMap.put("excelParams", excelParams);
				paramMap.putAll(map);
				VirtualRequest vRequest = new VirtualRequest(paramMap); // request的getParament无法重置，所有自己模拟一个virtualRequest用于存跳转参数
				try {
					excelListPage(vRequest, request, response);
				} catch (ScriptException e) {
					logger.info("ERROR", e);
				}
				return null;
			case 2017:// 保存并返回
				if (user.getUserId() != null) {
					docVo.setLastmodifier(String.valueOf(user.getUserId()));
					docVo.setAuditUser(String.valueOf(user.getUserId()));
				}
				if (docVo.isUpdate()) {
					// 更新数据
					for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
						String o = it.next();
						utils.updateData(o);
					}
					// 更新document 记录
					// 最后修改时间
					docVo.setLastmodified(new Date(System.currentTimeMillis()));
					utils.saveDocument();

				} else {
					// 创建时间
					docVo.setCreated(new Date(System.currentTimeMillis()));
					docVo.setAuthorId(user.getUserId());
					// 最后修改时间
					docVo.setLastmodified(docVo.getCreated());
					// 保存数据
					// 向document插入数据
					for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
						docVo.setId("");
						String o = it.next();
						utils.saveData(o);
						utils.saveDocument();
					}
					utils.saveDocument();
				}
				backId = null;
				obt = DocumentUtils.getThingFromSession("backId");
				if (obt != null) {
					backId = (Long) obt;
				} else {
					backId = Long.parseLong(act.getExtbute().get("target"));
				}
				mv.setViewName("redirect:/document/view.do?dynamicPageId=" + backId);
				return mv;

			default:
				break;
			}
			// 调用流程接口

		}

		// 数据校验
		// 根据Map和模型，组装Map（documentVo.params），key为模型item的code
		// 读取actId对应的act中的actScript，获得jsrunner，执行脚本，该跳转跳转，该返回返回，根据actType来确定
		// 默认跳转到document/view，即当前页面。跳转或者返回只能
		mv.addObject("dynamicPageId", pageId);
		mv.addObject("id", docVo.getId());
		mv.setViewName("redirect:/document/view.do");
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/refreshState", method = RequestMethod.POST)
	public String refrehState(HttpServletRequest request) {
		String componentId = request.getParameter("componentId");
		StoreVO store = null;
		if (StringUtils.isNotBlank(componentId)) {
			componentId = componentId.substring(componentId.length() - 36);
			store = storeServiceImpl.findById(componentId);
		}
		JSONObject component = null;
		if (store != null) {
			component = JSON.parseObject(store.getContent());
			int type = Integer.parseInt((String) component.get("componentType"));
			switch (type) {
			case 1006:
				String script = (String) component.get("optionScript");
				if (StringUtils.isNotBlank(script)) {
					ScriptEngine engine = ScriptEngineUtils.getScriptEngine();
					engine.put("request", request);
					engine.put("DocumentUtils", new DocumentUtils());
					String ret = null;
					try {
						ret = (String) engine.eval(script);
					} catch (ScriptException e) {
						logger.info("ERROR", e);
					}
					StringBuilder sb = new StringBuilder();
					if (StringUtils.isNotBlank(ret)) {
						String[] optionStr = ret.split("\\;");
						if (optionStr.length > 0) {
							for (String str : optionStr) {
								String[] entry = str.split("\\=");
								if (entry != null && entry.length > 1) {
									sb.append("<option value=\"" + entry[0] + "\">" + entry[1] + "</option>");
								} else {
									sb.append("<option value=\"" + entry[0] + "\">" + entry[0] + "</option>");
								}
							}
							return sb.toString();
						} else {
							return ret;
						}
					} else {
						sb.append("<option value=' '></option>");
					}
				}
				break;
			default:
				break;
			}
		}
		return null;
	}

	@ResponseBody
	@RequestMapping("/getListJson")
	public List<Map<String, String>> getListJson(HttpServletRequest request) throws ScriptException {

		Map<String, String> paramMap = new HashMap<String, String>();
		Enumeration enumeration = request.getParameterNames();
		for (; enumeration.hasMoreElements();) {
			Object o = enumeration.nextElement();
			paramMap.put(o.toString(), request.getParameter(o.toString()));
		}

		String dynamicPageId = paramMap.get("dynamicPageId");
		DynamicPageVO pageVO = formdesignerServiceImpl.findById(Long.parseLong(dynamicPageId));
		DocumentVO docVo = new DocumentVO();
		docVo.setDynamicPageId(String.valueOf(pageVO.getId()));

		ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
		engine.put("request", request);
		engine.put("session", request.getSession());
		Map<String, List<Map<String, String>>> dataMap = documentServiceImpl.initDocumentData(1, 25, docVo, engine,
				pageVO);

		if (dataMap != null && !dataMap.isEmpty()) {
			BaseExample baseExample = new BaseExample();
			baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId).andLike("CODE",
					StoreService.COMPONENT_CODE + "%");
			List<StoreVO> columnComs = storeServiceImpl.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE,
					"T_ORDER");
			for (int k = 0; k < columnComs.size(); k++) {
				JSONObject o = JSON.parseObject(columnComs.get(k).getContent());
				if ("1008".equals(o.getString("componentType"))) { // 列组件
					String showScript = o.getString("showScript");
					if (StringUtils.isNotBlank(showScript)) {
						Boolean ret = (Boolean) engine.eval(showScript);
						if (ret) {
							break;
						} else {
							// TODO 报错
						}
					}
				}
			}

			if (dataMap.values().iterator().hasNext()) {
				List<Map<String, String>> resultList = dataMap.values().iterator().next(); // 取到dataMap第一个value
				List<Map<String, String>> finalResultList = new ArrayList<Map<String, String>>();
				if (resultList != null) {
					for (int i = 0; i < resultList.size(); i++) { // 取出的map中带有多列，过滤，剩列组件对应的列以及组件id或ID
						Map<String, String> map = resultList.get(i);
						Map<String, String> finalMap = new LinkedHashMap<String, String>();
						if (map.get("ID") != null) {
							finalMap.put("ID", map.get("ID"));
						} else if (map.get("id") != null) {
							finalMap.put("id", map.get("id"));
						}
						for (int k = 0; k < columnComs.size(); k++) {
							JSONObject o = JSON.parseObject(columnComs.get(k).getContent());
							if ("1008".equals(o.getString("componentType"))) { // 列组件
								String columnDataName = o.getString("dataItemCode")
										.substring(o.getString("dataItemCode").lastIndexOf(".") + 1);
								finalMap.put(columnDataName, map.get(columnDataName));
							}
						}
						finalResultList.add(finalMap);
					}
					return finalResultList;
				}
			}

		}
		return null;

	}

	@RequestMapping("/getList")
	@ResponseBody
	public String getList(String code, String level, String value) {
		String ret = DocumentUtils.getOptions(code, level, value);
		return ret;
	}

	// 对于内嵌列表页面，处理ajax请求
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "/excuteOnly")
	public String excuteActNoDirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String result = "1";
		String docId = request.getParameter("docId");
		String pageId = request.getParameter("dynamicPageId");
		String actId = request.getParameter("actId");
		String update = request.getParameter("update");
		boolean isUpdate = false;
		if (StringUtils.isNotBlank(update)) {
			isUpdate = update.equalsIgnoreCase("true");
		}
		String[] _selects = request.getParameterValues("_selects");// 列表页面才能使用
		// 根据pageId，获取组件，然后进行request遍历，组装成Map（documentVo.requestParams），key为组件name
		ModelAndView mv = new ModelAndView();
		DocumentVO docVo = new DocumentVO();
		DynamicPageVO pageVO = null;
		// 查找模版
		if (StringUtils.isNotBlank(pageId)) {
			pageVO = formdesignerServiceImpl.findById(Long.parseLong(pageId));
		} else {
			return null;
		}

		// 表单页面
		if (pageVO.getPageType() == 1002) {// 初始化表单数据
			docVo.setId(docId);
			docVo.setUpdate(isUpdate);
			docVo.setDynamicPageId(pageId);
			docVo.setDynamicPageName(pageVO.getName());
			Map<String, String> map = new HashMap<String, String>();
			Enumeration enumeration = request.getParameterNames();
			for (; enumeration.hasMoreElements();) {
				Object o = enumeration.nextElement();
				String name = o.toString();
				String[] values = request.getParameterValues(name);
				map.put(o.toString(), StringUtils.join(values, ";"));
			}
			docVo.setRequestParams(map);
			docVo = documentServiceImpl.processParams(docVo);
			logger.debug("---------此处超强分割线-------执行按钮脚本，提交到后台的参数-------提交到后台的数据：-----------");
			logger.debug(map.toString());
			logger.debug("--------此处超强分割线----------END----------------");
		} else if (1003 == pageVO.getPageType()) {// 列表页面

		}

		StoreVO store = storeServiceImpl.findById(actId);
		PageActVO act = JSON.parseObject(store.getContent(), PageActVO.class);
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) Tools.getObjectFromSession(SessionContants.CURRENT_USER);
		// 初始化脚本解释执行器,加载全局工具类
		ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
		// TODO put engine 工具类库
		engine.put("request", request);

		// 如跳转、返回之类的
		if (StringUtils.isNotBlank(act.getServerScript())) {
			String ret = null;
			try {
				String script = StringEscapeUtils.unescapeHtml4(act.getServerScript());
				Object o = engine.eval(script);
				ret = (String) o;
				if (ret != null && (ret.startsWith("redirect:") || ret.startsWith("forward:"))) {
					mv.setViewName(ret);
					response.setContentType("text/html;");
					response.getWriter().write(result);
					response.sendRedirect(ret.substring(ret.indexOf("redirect:") + 9));
					return null;
				} else {
					if (StringUtils.isNotBlank(ret)) {
						response.setContentType("text/html;");
						response.getWriter().write(ret);
						return null;
					}
				}
			} catch (ScriptException e) {
				logger.info("ERROR", e);
			}
		} else {
			// 校验文档
			DocumentUtils utils = new DocumentUtils(docVo, pageVO);
			Integer actType = act.getActType();
			// 根据actType 执行其默认操作
			switch (actType) {
			case 2001:// 保存--不带流程，
				if (user.getUserId() != null) {
					docVo.setLastmodifier(String.valueOf(user.getUserId()));
					docVo.setAuditUser(String.valueOf(user.getUserId()));
				}
				// 设置doc 记录为草稿状态
				docVo.setState("草稿");
				if (docVo.isUpdate()) {
					// 更新数据
					for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
						String o = it.next();
						utils.updateData(o);
					}
					// 更新document 记录
					// 最后修改时间
					docVo.setLastmodified(new Date(System.currentTimeMillis()));
					utils.saveDocument();

				} else {
					// 创建时间
					docVo.setCreated(new Date(System.currentTimeMillis()));
					docVo.setAuthorId(user.getUserId());
					// 最后修改时间
					docVo.setLastmodified(docVo.getCreated());
					// 保存数据
					// 向document插入数据
					for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
						docVo.setId("");
						String o = it.next();
						utils.saveData(o);
						utils.saveDocument();
					}
					utils.saveDocument();
				}
				break;

			case 2003:
				// TODO 删除动作实现
				if (_selects != null && _selects.length >= 1) {
					for (String select : _selects) {
						utils.deleteData(pageVO, select);
						BaseExample base = new BaseExample();
						base.createCriteria().andEqualTo("RECORD_ID", select);
						documentServiceImpl.deleteByExample(base);
					}
					mv.setViewName("redirect:/document/view.do?dynamicPageId=" + pageId);
					return "1";
				} else {
					// TODO 返回并提示没有选择记录
				}
				break;

			case 2009:
				mv.addObject("dynamicPageId", act.getExtbute().get("target"));
				mv.setViewName("redirect:/document/view.do");
				response.setContentType("text/html;");
				response.getWriter().write(result);
				return null;
			default:
				break;
			}
			// 调用流程接口

		}

		// 数据校验
		// 根据Map和模型，组装Map（documentVo.params），key为模型item的code
		// 读取actId对应的act中的actScript，获得jsrunner，执行脚本，该跳转跳转，该返回返回，根据actType来确定
		// 默认跳转到document/view，即当前页面。跳转或者返回只能

		response.setContentType("text/html;");
		response.getWriter().write(result);
		return null;
	}

	public String appendURLParam(String key, Object value, String url) {
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		if (url.endsWith(".do")) {
			sb.append("?");
		} else {
			sb.append("&");
		}
		sb.append(key).append("=").append(value);
		return sb.toString();
	}

	/**
	 * 打印
	 * 
	 * @param paramRequest
	 *            所有参数条件都放到paramRequest中
	 * @param request
	 * @param response
	 * @throws ScriptException
	 * @throws DocumentException
	 */
	@RequestMapping(value = "/print")
	public void print(VirtualRequest paramRequest, HttpServletRequest request, HttpServletResponse response)
			throws ScriptException, DocumentException {

		String docId = paramRequest.getParameter("docId");
		String pageId = paramRequest.getParameter("dynamicPageId");
		String workflowId = paramRequest.getParameter("workflowId");

		// logger.debug(paramRequest.getParameter("pageVOId"));

		String actId = paramRequest.getParameter("actId");
		StoreVO store = storeServiceImpl.findById(actId);
		PageActVO act = JSON.parseObject(store.getContent(), PageActVO.class);

		// 根据pageId，获取组件，然后进行request遍历，组装成Map（documentVo.requestParams），key为组件name

		DocumentVO docVo = new DocumentVO();

		DynamicPageVO pageVO = null;
		// 查找模版
		if (StringUtils.isNotBlank(pageId)) {
			pageVO = formdesignerServiceImpl.findById(Long.parseLong(pageId));
		} else {
			// FIXME 报错
			// return null;
		}

		// 表单页面
		// if (pageVO.getPageType() == 1002) {// 初始化表单数据
		// 查找document
		// if (docVo.isUpdate()) {
		// DocumentVO tmp = documentServiceImpl.findById(docId);
		// }
		docVo.setId(docId);

		docVo.setDynamicPageId(pageId);
		docVo.setDynamicPageName(pageVO.getName());
		docVo.setWorkflowId(workflowId);

		Integer currentPage = 1;
		Integer pageSize = 10;
		if (StringUtils.isNotBlank(paramRequest.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(paramRequest.getParameter("currentPage"));
		}
		if (StringUtils.isNotBlank(paramRequest.getParameter("pageSize"))) {
			pageSize = Integer.parseInt(paramRequest.getParameter("pageSize"));
		}

		// 初始化脚本解释执行器,加载全局工具类
		ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
		// TODO put engine 工具类库
		engine.put("request", paramRequest);
		engine.put("session", request.getSession());

		Map<String, List<Map<String, String>>> dataMap = documentServiceImpl.initDocumentData(currentPage, pageSize,
				docVo, engine, pageVO);
		docVo.setListParams(dataMap);
		engine.put("DocumentUtils", new DocumentUtils(docVo, pageVO));

		JSONObject jcon = new JSONObject();
		jcon.put("relatePageId", pageVO.getId());
		jcon.put("componentType", "");
		List<JSONObject> components = formdesignerServiceImpl.getComponentByContainerWithColumn(jcon);

		// 对表格组件进行处理，将真实值转换为显示值；比如一些 日期格式化、code转对应value等；
		if (dataMap != null && !dataMap.isEmpty()) {
			BaseExample baseExample = new BaseExample();
			baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", pageVO.getId()).andLike("CODE",
					StoreService.COMPONENT_CODE + "%");
			List<StoreVO> columnComs = storeServiceImpl.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, null);
			for (int k = 0; k < columnComs.size(); k++) {
				JSONObject o = JSON.parseObject(columnComs.get(k).getContent());
				if ("1017".equals(o.getString("componentType"))) { // 列组件或者表格组件
					String showScript = o.getString("showScript");
					if (StringUtils.isNotBlank(showScript)) {
						Boolean ret = (Boolean) engine.eval(showScript);
						if (ret) {
							break;
						} else {
							// TODO 报错
						}
					}
				}
			}
		}

		// 对查出的表单数据进行处理 转化为<Map<String,String>>形式；如
		// prmi.id="aaaa",方便打印直接根据dataItemCode查询对应的值
		Map finalMap = new HashMap();
		if (dataMap != null && !dataMap.isEmpty()) {
			Iterator iter = dataMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				String alais = key.substring(0, key.indexOf("_list"));
				List val = (List) entry.getValue();
				if (val != null && val.size() > 0) {
					Map asignMap = (Map) val.get(0);
					if ("epai".equals(alais)) {
						logger.debug(asignMap + "");
					}
					Iterator asignIter = asignMap.entrySet().iterator();
					while (asignIter.hasNext()) {
						Map.Entry asignEntry = (Map.Entry) asignIter.next();
						String asignKey = (String) asignEntry.getKey();
						String asignVal = (String) asignEntry.getValue();
						finalMap.put(alais + "." + asignKey, asignVal);
					}
				}
			}
		}
		dataMap.putAll(finalMap);

		// 如果有包含列表页面，或者总页面是列表页面；
		// 则读取列表页面中的数据，存到dataMap中；格式：Map<String,List<Map<String,String>>> eg:
		// Map<pmi,list<Map<pmi.name,aaaa>>>
		Map listPageMap = getListPageData(paramRequest, request);
		dataMap.putAll(listPageMap);

		// 打印时需要打印checkBox中所有选项（不止选择的选项）,所以需查找出option所以选项，放到map中。
		Map optionMap = getOptionTextMapByPage(engine, pageVO);
		dataMap.putAll(optionMap);

		String templetId = act.getExtbute().get("templet");
		StoreVO printManageVO = storeServiceImpl.findById(templetId);
		JSONObject printJson = JSON.parseObject(printManageVO.getContent());
		String fileName = "申报书.pdf";
		if (StringUtils.isNoneBlank(printJson.getString("pdfName"))) {
			java.util.Date now = new java.util.Date();
			String dateStr = DateUtils.format(now, "ddMMyyyy_HHmmss");
			fileName = printJson.getString("pdfName") + dateStr + ".pdf";
		}
		// 设置在线预览方式打开Pdf文件
		response.setContentType("application/pdf;charset=UTF-8");
		try {
			response.setHeader("Content-disposition",
					"inline; filename=" + encodeChineseDownloadFileName(request, fileName));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			logger.info("ERROR", e1);
		}

		OutputStream out = null;
		try {
			// 判断是否需要合并pdf
			boolean isMerge = StringUtils.isNotBlank(request.getParameter("isMerge")) ? true : false;
			// 创建临时合并文件
			String root = ControllerHelper.getUploadPath(ControllerHelper.ATTACHMENT_UPLOAD_PATH);
			String temp1 = root + "temp1.pdf";
			if (isMerge) {
				out = new FileOutputStream(temp1);
			} else {
				out = response.getOutputStream();
			}
			if (StringUtils.isNotBlank(printJson.getString("templateFileId"))) { // 有配置自定义模板
				printServiceImpl.printPDFByTemplate(printManageVO, dataMap, out, components);
			} else {
				printServiceImpl.printPDF(printManageVO, dataMap, out);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("ERROR", e);
		}
		// }
	}

	@ResponseBody
	@RequestMapping(value = "/excelListPage")
	public void excelListPage(VirtualRequest paramRequest, HttpServletRequest request, HttpServletResponse response)
			throws ScriptException {
		String docId = paramRequest.getParameter("docId");
		String pageId = paramRequest.getParameter("dynamicPageId");
		String templateFileId = paramRequest.getParameter("templateFileId");
		String actSql = paramRequest.getParameter("actSql");
		// 根据pageId，获取组件，然后进行request遍历，组装成Map（documentVo.requestParams），key为组件name
		ModelAndView mv = new ModelAndView();
		DocumentVO docVo = new DocumentVO();
		DynamicPageVO pageVO = null;
		// 查找模版
		if (StringUtils.isNotBlank(pageId)) {
			pageVO = formdesignerServiceImpl.findById(Long.parseLong(pageId));
		} else {
			// FIXME 报错
			// return null;
		}
		docVo.setId(docId);
		docVo.setDynamicPageId(pageId);
		docVo.setDynamicPageName(pageVO.getName());
		// 初始化脚本解释执行器,加载全局工具类
		ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
		// TODO put engine 工具类库
		engine.put("request", paramRequest);
		engine.put("session", request.getSession());
		// 分页
		String orderBy = docVo.getRequestParams().get("orderBy");
		String allowOrderBy = docVo.getRequestParams().get("allowOrderBy");
		docVo.setAllowOrderBy(allowOrderBy);
		docVo.setOrderBy(orderBy);
		Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
		if (StringUtils.isNotBlank(actSql)) {// 当excel导出动作的sql脚本不为空，则根据该sql查找数据进行导出

			// 此处由于documentService的excuteQueryForList查找出来的类型是List<Map<String,Object>>
			// 需要转换为List<Map<String,String>>类型的数据
			List<Map<String, Object>> sqlData = documentServiceImpl.excuteQueryForList(actSql);
			List<Map<String, String>> stringData = new ArrayList<Map<String, String>>();
			for (Map<String, Object> tem1 : sqlData) {
				Map<String, String> tem2 = new HashMap<String, String>();
				for (Map.Entry<String, Object> entry : tem1.entrySet()) {
					if (entry.getValue() != null) {
						tem2.put(entry.getKey(), entry.getValue().toString());
					} else {
						tem2.put(entry.getKey(), "");
					}
				}
				stringData.add(tem2);
			}
			dataMap.put("sqlData", stringData);
		} else {// 导出excel动作中的sql脚本为空，则根据列表页面的数据源来导出
			dataMap = documentServiceImpl.initDocumentData(1, MAX_EXCEL_PAGE_SIZE, docVo, engine, pageVO);
		}

		engine.put("DocumentUtils", new DocumentUtils(docVo, pageVO));

		List<JSONObject> columns = new ArrayList<JSONObject>();
		if (!StringUtils.isNotBlank(templateFileId)) {// 如果没有模板，则根据执行下面代找到所有列框，然后根据列框来导出数据
			// 查找列框
			BaseExample baseExample = new BaseExample();
			baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", pageId).andLike("code",
					StoreService.COMPONENT_CODE + "%");
			PageList<StoreVO> stores = storeServiceImpl.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, null);

			for (StoreVO store : stores) {
				JSONObject json = JSON.parseObject(StringEscapeUtils.unescapeHtml4(store.getContent()));
				Integer type = json.getInteger("componentType");
				if (type == 1008) {
					columns.add(json);
				}
			}
			Collections.sort(columns, new Comparator<JSONObject>() {
				@Override
				public int compare(JSONObject o1, JSONObject o2) {
					return o1.getIntValue("order") - o2.getIntValue("order");
				}
			});
			// 真实值替换成显示值 TODO
			for (JSONObject column : columns) {
				String showScript = column.getString("showScript");
				if (StringUtils.isNotBlank(showScript)) {
					engine.eval(StringEscapeUtils.unescapeHtml4(showScript));
				}
			}
		}

		response.setContentType("text/html;charset=UTF-8");
		response.setContentType("application/x-msdownload;");
		try {
			response.setHeader("Content-disposition",
					"attachment; filename=" + encodeChineseDownloadFileName(request, pageVO.getName() + "-列表.xls"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			logger.info("ERROR", e1);
		}
		try {
			// 此处多传了模板ID和页面两个参数pageVO，pageVO是为了获取序号的模式和序号是降序还是升序
			printExcel(columns, dataMap, response.getOutputStream(), templateFileId, pageVO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("ERROR", e);
		}
		// }
	}

	public Map getListPageData(VirtualRequest paramRequest, HttpServletRequest request) throws ScriptException {

		Map<String, List> returnMap = new HashMap<String, List>();

		DynamicPage dp = DynamicPage.get(DynamicPage.class, paramRequest.getParameter("pageVOId"));
		DynamicPageVO vo = BeanUtils.getNewInstance(dp, DynamicPageVO.class);

		String listPagesStr = null;
		if (vo.getPageType() == 1003 && StringUtils.isNotEmpty(paramRequest.getParameter("dynamicPageId")))// 列表页面，则返回自己
			listPagesStr = paramRequest.getParameter("dynamicPageId");
		else
			listPagesStr = paramRequest.getParameter("listPages");
		String[] listPages = null;
		if (StringUtils.isNotBlank(listPagesStr)) {
			listPages = listPagesStr.split(",");
		}
		if (listPages != null && listPages.length > 0) {
			for (int index = 0; index < listPages.length; index++) {
				Long dynamicPageId = Long.parseLong(listPages[index]);
				DynamicPageVO pageVO = formdesignerServiceImpl.findById(dynamicPageId);
				DocumentVO docVo = new DocumentVO();
				docVo.setDynamicPageId(String.valueOf(pageVO.getId()));

				ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
				engine.put("request", paramRequest);
				engine.put("session", request.getSession());
				Map<String, List<Map<String, String>>> dataMap = documentServiceImpl.initDocumentData(1,
						Integer.MAX_VALUE, docVo, engine, pageVO);

				if (dataMap != null && !dataMap.isEmpty()) {

					BaseExample baseExample = new BaseExample();
					baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId).andLike("CODE",
							StoreService.COMPONENT_CODE + "%");
					List<StoreVO> columnComs = storeServiceImpl.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE,
							null);
					for (int k = 0; k < columnComs.size(); k++) {
						JSONObject o = JSON.parseObject(columnComs.get(k).getContent());
						if ("1008".equals(o.getString("componentType"))) { // 列组件或者表格组件
							String showScript = o.getString("showScript");
							if (StringUtils.isNotBlank(showScript)) {
								Boolean ret = (Boolean) engine.eval(showScript);
								if (ret) {
									break;
								} else {
									// TODO 报错
								}
							}
						}
					}

					if (dataMap.values().iterator().hasNext()) {
						List<Map<String, String>> resultList = dataMap.values().iterator().next(); // 取到dataMap第一个value
						String key = dataMap.keySet().iterator().next();
						String alias = key.substring(0, key.lastIndexOf("_list"));
						List<Map<String, String>> finalResultList = new ArrayList<Map<String, String>>();
						if (resultList != null) {
							for (int i = 0; i < resultList.size(); i++) { // 取出的map中带有多列，过滤，剩列组件对应的列
								Map<String, String> map = resultList.get(i);
								Map<String, String> finalMap = new LinkedHashMap<String, String>();

								Iterator iter = map.entrySet().iterator();
								while (iter.hasNext()) {
									Map.Entry entry = (Map.Entry) iter.next();
									String itemCode = (String) entry.getKey();
									String val = (String) entry.getValue();
									finalMap.put(alias + "." + itemCode, val);
								}

								finalResultList.add(finalMap);
							}

						}
						returnMap.put(key.substring(0, key.lastIndexOf("_list")), finalResultList);
					}

				}
			}
		}
		return returnMap;
	}

	/**
	 * 对文件流输出下载的中文文件名进行编码 屏蔽各种浏览器版本的差异性
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName)
			throws UnsupportedEncodingException {

		String filename = null;
		String agent = request.getHeader("USER-AGENT");
		if (null != agent) {
			if (-1 != agent.indexOf("Firefox")) {// Firefox
				filename = "=?UTF-8?B?"
						+ (new String(org.apache.commons.codec.binary.Base64.encodeBase64(pFileName.getBytes("UTF-8"))))
						+ "?=";
			} else if (-1 != agent.indexOf("Chrome")) {// Chrome
				filename = new String(pFileName.getBytes(), "ISO8859-1");
			} else {// IE7+
				filename = java.net.URLEncoder.encode(pFileName, "UTF-8");
				filename = StringUtils.replace(filename, "+", "%20");// 替换空格
			}
		} else {
			filename = pFileName;
		}
		return filename;
	}

	/**
	 * ajax刷新表格数据，如果传入的method为delete，则执行删除，返回查询列表数据
	 * 
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "/refreshDataGrid")
	public Map refreshDataGrid(HttpServletRequest request) {

		String componentId = request.getParameter("componentId");

		StoreVO s = storeServiceImpl.findById(componentId);

		String method = request.getParameter("method");

		if ("delete".equals(method)) { // 删除
			String selects = request.getParameter("_selects");
			String[] _selects = null;
			if (selects != null) {
				_selects = selects.split(",");
			}
			Long pageId = s.getDynamicPageId();
			DynamicPageVO pageVO = formdesignerServiceImpl.findById(pageId);
			DocumentUtils utils = new DocumentUtils(null, pageVO);
			if (_selects != null && _selects.length >= 1) {
				for (String select : _selects) {
					utils.deleteData(pageVO, select);
				}
			}
		}

		Map dataMap = getDataGridItems(request, s);
		return dataMap;
	}

	/**
	 * 获取对应的表格数据
	 * 
	 * @param request
	 * @param s
	 *            表格组件
	 * @return
	 */
	public Map getDataGridItems(HttpServletRequest request, StoreVO s) {

		int currentPage = 1;
		int pageSize = 10;
		if (request.getParameter("currentPage") != null) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		if (request.getParameter("pageSize") != null) {
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}
		if (s != null) {
			JSONObject oo = JSON.parseObject(s.getContent());
			String dataAlias = "";
			if (request.getParameter("dataFile") != null) {
				dataAlias = oo.getString(request.getParameter("dataFile"));
			} else {
				dataAlias = oo.getString("dataAlias");
			}

			Long pageId = s.getDynamicPageId();
			DynamicPageVO pageVo = formdesignerServiceImpl.findById(pageId);
			Map<String, DataDefine> map = PageDataBeanWorker
					.convertConfToDataDefines(StringEscapeUtils.unescapeHtml4(pageVo.getDataJson()));
			DataDefine dd = map.get(dataAlias);
			ScriptEngine engine = ScriptEngineUtils.getScriptEngine();
			DocumentVO docVo = new DocumentVO();
			engine.put("request", request);
			engine.put("DocumentUtils", new DocumentUtils(docVo, pageVo));
			try {
				PageList<Map<String, String>> pageList = documentServiceImpl.getDataListByDataDefine(dd, engine,
						currentPage, pageSize, null);

				if (pageList == null)
					return null;

				// 操作显示脚本
				Map<String, List<Map<String, String>>> dataMap = new HashMap<String, List<Map<String, String>>>();
				dataMap.put(dataAlias + "_list", pageList);
				docVo.setListParams(dataMap);

				String showScript = oo.getString("showScript");
				if (StringUtils.isNoneBlank(showScript)) {
					if (StringUtils.isNotBlank(showScript)) {
						engine.eval(StringEscapeUtils.unescapeHtml4(showScript));
					}
				}

				Map returnMap = new HashMap();
				returnMap.put("currentPage", pageList.getPaginator().getPage());
				returnMap.put("total", pageList.getPaginator().getTotalCount());
				returnMap.put("totalPage", pageList.getPaginator().getTotalPages());
				returnMap.put("data", pageList);
				// 通用选择框需要的数据
				if (request.getParameter("dataFile") != null) {
					returnMap.put("zTreeId", oo.getString("zTreeId"));
					returnMap.put("pId", oo.getString("pId"));
					returnMap.put("zTreeName", oo.getString("zTreeName"));
					if ("showDataItemCode".equals(request.getParameter("dataFile"))) {
						returnMap.put("showName", oo.getString("showName"));
						returnMap.put("showWhere", oo.getString("showWhere"));
						returnMap.put("returnVal", oo.getString("returnVal"));
					}
				}

				return returnMap;
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				logger.info("ERROR", e);
			}
		}
		return null;
	}

	@ResponseBody
	@RequestMapping("/excuteByAjax")
	public String excuteActByAjax(HttpServletRequest request, HttpServletResponse response) {
		String docId = request.getParameter("docId");
		String id = request.getParameter("id");
		String pageId = request.getParameter("dynamicPageId");
		String workflowId = request.getParameter("workflowId");
		String instanceId = request.getParameter("instanceId");
		String taskId = request.getParameter("taskId");
		String actId = request.getParameter("actId");
		String update = request.getParameter("update");
		String[] _selects = request.getParameterValues("_selects");// 列表页面才能使用
		String staticHtmlId = request.getParameter("staticHtmlId");// 用于页面静态化
		if (StringUtils.isBlank(actId)) {

		}
		DynamicPageVO pageVO = null;
		if (StringUtils.isNotBlank(pageId)) {
			pageVO = formdesignerServiceImpl.findById(Long.parseLong(pageId));
		} else {

		}

		return null;
	}

	private void printExcel(List<JSONObject> columns, Map<String, List<Map<String, String>>> dataMap, OutputStream os,
			String templateFileId, DynamicPageVO pageVO) {
		WritableWorkbook workbook = null;
		Workbook template = null;// 定义模板
		if (templateFileId != null && StringUtils.isNoneBlank(templateFileId)) {// 当模板ID不为空，找到模板
			MongoClient client = MongoDBUtils.getMongoClient();
			DB db = client.getDB("myFiles");
			GridFS myFS = new GridFS(db);
			DBObject query = new BasicDBObject("id", templateFileId);
			GridFSDBFile file = myFS.findOne(query);
			try {
				template = Workbook.getWorkbook(file.getInputStream());// 初始化模板
			} catch (BiffException e) {
				logger.info("ERROR", e);
			} catch (IOException e) {
				logger.info("ERROR", e);
			}
		}

		try {
			// 序号模式
			String numFormat = pageVO.getReverseNumMode() != null ? pageVO.getReverseNumMode() : "0000";
			// 序号升序或降序
			String sortType = pageVO.getReverseSortord();

			if (template != null) {// 如果模板不为空，则根据模板导出
				workbook = Workbook.createWorkbook(os, template);// 根据模板创建一个workbook
				WritableSheet sheet = workbook.getSheet(0);// 新建一个工作簿
				Sheet templateSheet = template.getSheet(0);// 获取模板的工作簿

				// 下面是获取模板中的列头，模板中的的列头格式为
				// ：”数据源名.属性名_列名“，或者当根据sql查找导出数据时格式为：“sql属性名_列名”
				List<String> templateColumns = new ArrayList<String>();
				int colNum = templateSheet.getColumns();
				for (int i = 0; i < colNum; i++) {
					Cell cell = templateSheet.getCell(i, 0);
					if (cell == null)
						continue;
					if (StringUtils.isNotBlank(cell.getContents())) {
						// 如果列头不为空则增加到templateColumns中进行后面数据填充
						templateColumns.add(cell.getContents());
					}
				}
				// 遍历模板中的列头名称，把数据一一对应填并根据模板填写到工作簿sheet中
				for (int i = 0; i < templateColumns.size(); i++) {
					String templateColumn = templateColumns.get(i);
					String[] colums = templateColumn.split("_");
					String[] codes = colums[0].split("\\.");
					if (codes != null && codes.length > 0) {
						// 填写列头
						// 获取表头的CellFormat
						CellFormat headFormat = sheet.getWritableCell(i, 0).getCellFormat();
						// 定义列头的内容和格式
						Label headLabel = new Label(i, 0, colums[1], headFormat);
						// 增加列头
						sheet.addCell(headLabel);
						// 获取模板中定义的数据源名和属性名
						String contentCodes = "";
						if (codes.length == 1) {
							// 当定义为根据sql导出数据，格式为“sql属性名_列名”
							contentCodes = codes[0];
						} else {
							// 当根据数据源导出数据时，格式为”数据源名.属性名_列名“
							contentCodes = codes[1];
						}
						// 获取数据
						List<Map<String, String>> list = new ArrayList<Map<String, String>>();
						if (dataMap.get("sqlData") != null) {
							// 当根据sql导出时，dataMap中数据只有一个List
							list = dataMap.get("sqlData");
						} else {
							// 当根据数据源导出时，根据数据源名称查找对应的list
							list = dataMap.get(codes[0] + "_list");
						}
						// 对序号进行独立的处理
						if (contentCodes.equalsIgnoreCase("no") && i == 0) {
							// 序号的格式定义为“no_序号名”
							if (dataMap.get("sqlData") != null) {
								// 根据sql导出时，对序号的处理不能使用Paginator
								int size = list.size();
								for (int j = 0; j < size; j++) {
									int number = size - j;
									DecimalFormat df2 = new DecimalFormat("0000");// 根据sql导出其序号模式为“0000”
									CellFormat contentFormat = sheet.getWritableCell(0, 1).getCellFormat();
									Label la = new Label(0, j + 1, df2.format(number), contentFormat);
									sheet.addCell(la);
								}
							} else {
								Paginator page = ((PageList) list).getPaginator();
								for (int j = 0; j < list.size(); j++) {
									int number = page.getTotalCount() - page.getPage() * page.getLimit()
											+ page.getLimit() - j;
									DecimalFormat df2 = new DecimalFormat("0000");// 根据sql导出其序号模式为“0000”
									CellFormat contentFormat = sheet.getWritableCell(0, 1).getCellFormat();
									Label la = new Label(0, j + 1, df2.format(number), contentFormat);
									sheet.addCell(la);
								}
							}

						} else {
							// 内容处理，根据模板中定义的属性名对号入座
							CellFormat contentFormat = sheet.getWritableCell(i, 1).getCellFormat();
							for (int j = 0; j < list.size(); j++) {
								Map<String, String> data = list.get(j);
								String content = data.get(contentCodes);
								Label la = new Label(i, j + 1, content, contentFormat);
								sheet.addCell(la);
							}
						}
					}
				}
			} else {// 没用模板时，根据数据源和列框导出数据

				WritableFont fontHead = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD);
				WritableCellFormat formatHead = new WritableCellFormat(fontHead);
				formatHead.setWrap(true);
				formatHead.setVerticalAlignment(VerticalAlignment.CENTRE);
				formatHead.setAlignment(Alignment.CENTRE);
				WritableFont fontContent = new WritableFont(WritableFont.createFont("宋体"), 11);
				WritableCellFormat formatContent = new WritableCellFormat(fontContent);
				formatContent.setWrap(true);
				formatContent.setVerticalAlignment(VerticalAlignment.CENTRE);
				formatContent.setAlignment(Alignment.LEFT);
				CellView cellView = new CellView();
				cellView.setAutosize(true); // 设置自动大小
				workbook = Workbook.createWorkbook(os);
				WritableSheet sheet = workbook.createSheet("First Sheet", 0);
				for (int i = -1; i < columns.size(); i++) {
					if (i == -1) {
						sheet.setColumnView(0, 10);// 根据内容自动设置列宽
						Label label = new Label(0, 0, "序号", formatHead);
						sheet.addCell(label);
						JSONObject column = columns.get(0);
						String[] codes = column.getString("dataItemCode").split("\\.");
						List<Map<String, String>> list = dataMap.get(codes[0] + "_list");
						Paginator page = ((PageList) dataMap.get(codes[0] + "_list")).getPaginator();
						for (int j = 0; j < list.size(); j++) {
							int number = 0;
							if (sortType != null && sortType.equalsIgnoreCase("0")) {// 升序
								number = page.getPage() * page.getLimit() - page.getLimit() + j + 1;
							} else {// 降序
								number = page.getTotalCount() - page.getPage() * page.getLimit() + page.getLimit() - j;
							}

							DecimalFormat df2 = new DecimalFormat(numFormat);
							Label la = new Label(0, j + 1, df2.format(number), formatContent);
							sheet.addCell(la);
						}
					} else {
						// sheet.setColumnView(i+1, cellView);//根据内容自动设置列宽
						// sheet.setRowView(i+1, 1);
						JSONObject column = columns.get(i);
						Label label = new Label(i + 1, 0, column.getString("columnName"), formatHead);
						sheet.addCell(label);// 添加列头
						// 添加列数据
						String dataSource = column.getString("dataItemCode");
						String[] codes = dataSource.split("\\.");
						int max = 0;
						if (codes != null && codes.length > 0) {
							List<Map<String, String>> list = dataMap.get(codes[0] + "_list");
							for (int j = 0; j < list.size(); j++) {
								Map<String, String> data = list.get(j);
								String content = data.get(codes[1]);
								if (content != null) {
									if (content.length() + getChineseNum(content) > max) {
										max = content.length() + getChineseNum(content);
									}
								}
								Label la = new Label(i + 1, j + 1, content, formatContent);
								sheet.addCell(la);
							}
							if (max != 0) {
								sheet.setColumnView(i + 1, max + 4);
							} else {
								sheet.setColumnView(i + 1, cellView);
							}
						}
					}
				}
			}

			workbook.write();
			workbook.close();
		} catch (Exception e) {
			logger.info("ERROR", e);
		}

	}

	public static int getChineseNum(String context) { // /统计context中是汉字的个数
		int lenOfChinese = 0;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]"); // 汉字的Unicode编码范围
		Matcher m = p.matcher(context);
		while (m.find()) {
			lenOfChinese++;
		}
		return lenOfChinese;
	}

	@ResponseBody
	@RequestMapping(value = "/batchPrint")
	public void batchPrint(HttpServletRequest request, HttpServletResponse response) throws ScriptException {
		String dynamicPages = request.getParameter("dynamicPageIds");

		String[] dynamicPageArr = dynamicPages.split(",");
		Map<String, String> map = new HashMap<String, String>();
		Enumeration enumeration = request.getParameterNames();
		for (; enumeration.hasMoreElements();) {

			Object o = enumeration.nextElement();
			String name = o.toString();
			String[] values = request.getParameterValues(name);
			map.put(o.toString(), StringUtils.join(values, ";"));
		}

		String fileName = ((PunUserBaseInfoVO) (SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER)))
				.getName();
		VirtualRequest virtualRequest = new VirtualRequest();
		ZipOutputStream zipOut = null;
		// try {
		// zipOut = new ZipOutputStream(response.getOutputStream());
		// } catch (IOException e3) {
		// // TODO Auto-generated catch block
		// e3.logger.info("ERROR", e)();
		// }
		// zip的名称为
		// zipOut.setComment("评审表.rar");
		response.setContentType("text/html;charset=UTF-8");
		response.setContentType("application/x-msdownload;");
		try {
			response.setHeader("Content-disposition",
					"attachment; filename=" + encodeChineseDownloadFileName(request, fileName + ".pdf"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			logger.info("ERROR", e1);
		}

		List<StoreVO> printManageVOs = new ArrayList<StoreVO>();
		List<Map> dataMaps = new ArrayList<Map>();
		List<List<JSONObject>> componentsList = new ArrayList<List<JSONObject>>();

		for (int i = 0; i < dynamicPageArr.length; i++) {
			String dynamicPageId = dynamicPageArr[i];
			DynamicPageVO pageVO = null;
			pageVO = formdesignerServiceImpl.findById(Long.parseLong(dynamicPageId));
			for (String key : map.keySet()) {
				if (map.get(key) != null) {
					String[] values = map.get(key).split(",");
					if (values != null && values[i] != null) {
						virtualRequest.setParameter(key, values[i]);
					}
				}
			}

			virtualRequest.setParameter("dynamicPageId", dynamicPageId);

			DocumentVO docVo = new DocumentVO();
			// 初始化脚本解释执行器,加载全局工具类
			ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
			// TODO put engine 工具类库
			engine.put("request", virtualRequest);
			engine.put("session", request.getSession());

			Map<String, List<Map<String, String>>> dataMap = documentServiceImpl.initDocumentData(1, 10, docVo, engine,
					pageVO);
			docVo.setListParams(dataMap);
			engine.put("DocumentUtils", new DocumentUtils(docVo, pageVO));

			JSONObject jcon = new JSONObject();
			jcon.put("relatePageId", pageVO.getId());
			jcon.put("componentType", "");
			List<JSONObject> components = formdesignerServiceImpl.getComponentByContainerWithColumn(jcon);
			componentsList.add(components);
			if (components != null && components.size() > 0) {
				for (int k = 0; k < components.size(); k++) {
					JSONObject component = components.get(k);
					logger.debug("component : " + JSON.toJSONString(component));
					if (component != null) {
						JSONObject o = new JSONObject();
						String value = DocUtils.getComponentValue(component, dataMap, engine);
					}
				}
			}

			// 对表格组件进行处理，将真实值转换为显示值；比如一些 日期格式化、code转对应value等；
			if (dataMap != null && !dataMap.isEmpty()) {
				BaseExample baseExample = new BaseExample();
				baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", pageVO.getId()).andLike("CODE",
						StoreService.COMPONENT_CODE + "%");
				List<StoreVO> columnComs = storeServiceImpl.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE,
						null);
				for (int k = 0; k < columnComs.size(); k++) {
					JSONObject o = JSON.parseObject(columnComs.get(k).getContent());
					if ("1017".equals(o.getString("componentType"))) { // 列组件或者表格组件
						String showScript = o.getString("showScript");
						if (StringUtils.isNotBlank(showScript)) {
							Boolean ret = (Boolean) engine.eval(showScript);
							if (ret) {
								break;
							} else {
								// TODO 报错
							}
						}
					}
				}
			}

			// 对查出的表单数据进行处理 转化为<Map<String,String>>形式；如
			// prmi.id="aaaa",方便打印直接根据dataItemCode查询对应的值
			Map finalMap = new HashMap();
			if (dataMap != null && !dataMap.isEmpty()) {
				Iterator iter = dataMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String) entry.getKey();
					String alais = key.substring(0, key.indexOf("_list"));
					List val = (List) entry.getValue();
					if (val != null && val.size() > 0) {
						Map asignMap = (Map) val.get(0);
						if ("epai".equals(alais)) {
							logger.debug(asignMap + "");
						}
						Iterator asignIter = asignMap.entrySet().iterator();
						while (asignIter.hasNext()) {
							Map.Entry asignEntry = (Map.Entry) asignIter.next();
							String asignKey = (String) asignEntry.getKey();
							String asignVal = (String) asignEntry.getValue();
							finalMap.put(alais + "." + asignKey, asignVal);
						}
					}
				}
			}
			dataMap.putAll(finalMap);

			// 打印时需要打印checkBox中所有选项（不止选择的选项）,所以需查找出option所以选项，放到map中。
			Map optionMap = getOptionTextMapByPage(engine, pageVO);
			dataMap.putAll(optionMap);

			String templetId = null;
			BaseExample base = new BaseExample();
			base.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId).andLike("code",
					StoreService.PAGEACT_CODE + "%");
			PageList<StoreVO> stores = storeServiceImpl.selectPagedByExample(base, 1, Integer.MAX_VALUE, null);
			for (StoreVO store : stores) {
				JSONObject json = JSON.parseObject(store.getContent());
				Integer type = json.getInteger("actType");
				if (type == 2014) {
					PageActVO act = JSON.parseObject(store.getContent(), PageActVO.class);
					templetId = act.getExtbute().get("templet");
				}
			}

			StoreVO printManageVO = storeServiceImpl.findById(templetId);

			printManageVOs.add(printManageVO);
			dataMaps.add(dataMap);

		}

		try {
			printServiceImpl.batchPrintPDF(printManageVOs, dataMaps, componentsList, response.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			logger.info("ERROR", e1);
		}

	}

	private Map<String, String> getOptionTextMapByPage(ScriptEngine engine, DynamicPageVO pageVO) {

		Map<String, String> optionMap = new HashMap<String, String>();
		JSONObject jcon = new JSONObject();
		jcon.put("relatePageId", pageVO.getId());
		jcon.put("componentType", "");
		List<JSONObject> components = formdesignerServiceImpl.getComponentByContainerWithColumn(jcon);
		if (components != null && components.size() > 0) {
			for (int i = 0; i < components.size(); i++) {
				JSONObject component = components.get(i);
				logger.debug("component : " + JSON.toJSONString(component));
				if (component != null) {
					int type = component.getIntValue("componentType");

					switch (type) {
					case 1006:
					case 1003:
					case 1004:
						String script = component.getString("optionScript");
						String ret = "";
						if (StringUtils.isNotBlank(script)) {
							try {
								ret = (String) engine.eval(script);
								if (StringUtils.isNotBlank(ret)) {
									optionMap.put(component.getString("dataItemCode") + "_option", ret);
								}

							} catch (ScriptException e) {

								logger.info("ERROR", e);
							}
						}
						break;
					default:
						break;
					}
				}
			}
		}
		return optionMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getDocumentById")
	public DocumentVO getDocumentById(String documentid) {
		DocumentVO docvo = documentServiceImpl.findById(documentid);
		return docvo;
	}

	@ResponseBody
	@RequestMapping(value = "/getStoreById")
	public StoreVO getStoreById(String storeId) {

		if (null != storeId && !"".equals(storeId)) {
			String[] ids = storeId.split("_");
			if (ids.length > 1) {
				StoreVO vo = storeServiceImpl.findById(ids[1]);
				return vo;
			} else {
				StoreVO vo = storeServiceImpl.findById(ids[0]);
				return vo;
			}

		}

		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/getValue")
	public Map<String, List<Map<String, String>>> getValue(String id, String dynamicPageId, String docId,
			HttpServletResponse response, HttpServletRequest request) throws Exception {
		// 校验必要参数

		// 查询模版
		// 渲染模版
		logger.debug(" view.do start");
		// 新增：id为空时，为新增，获取dp，解析成HTML返回
		if (StringUtils.isBlank(id) && StringUtils.isBlank(dynamicPageId)) {
			// TODO ERROR
		} else {
			DocumentVO docVo = null;
			DynamicPageVO pageVO = null;
			logger.debug("start init document and dynamicpage ");
			// initDocAndPage(docVo, pageVO, id, docId, dynamicPageId);
			if (StringUtils.isBlank(docId)) {
				pageVO = formdesignerServiceImpl.findById(Long.parseLong(dynamicPageId));
				docVo = new DocumentVO();
				docVo.setUpdate(false);
				docVo.setDynamicPageId(pageVO.getId().toString());
				if (StringUtils.isNotBlank(id)) {
					docVo.setUpdate(true);
					docVo.setRecordId(id);
				}
				if (StringUtils.isNotEmpty(request.getParameter("selectId"))) {
					docVo.setSelectId(request.getParameter("selectId"));
				}
				docVo.setDynamicPageName(pageVO.getName());
				// 把列表页面配置的属性加到docVo中，在模板解析的时候使用
				docVo.setShowTotalCount(pageVO.getShowTotalCount());
				docVo.setIsLimitPage(pageVO.getIsLimitPage());
				docVo.setPageSize(pageVO.getPageSize());
				docVo.setShowReverseNum(pageVO.getShowReverseNum());
				docVo.setReverseNumMode(pageVO.getReverseNumMode());
				docVo.setReverseSortord(pageVO.getReverseSortord());
				// TODO 设置当前用户
			} else {
				docVo = documentServiceImpl.findById(docId);
				docVo.setUpdate(true);
				if (StringUtils.isNotBlank(dynamicPageId)) {
					pageVO = formdesignerServiceImpl.findById(Long.parseLong(dynamicPageId));
				} else {
					pageVO = formdesignerServiceImpl.findById(Long.parseLong(docVo.getDynamicPageId()));
				}
				docVo.setDynamicPageId(pageVO.getId().toString());
				docVo.setDynamicPageName(pageVO.getName());
				// 把列表页面配置的属性加到docVo中，在模板解析的时候使用
				docVo.setShowTotalCount(pageVO.getShowTotalCount());
				docVo.setIsLimitPage(pageVO.getIsLimitPage());
				docVo.setPageSize(pageVO.getPageSize());
				docVo.setShowReverseNum(pageVO.getShowReverseNum());
				docVo.setReverseNumMode(pageVO.getReverseNumMode());
				docVo.setReverseSortord(pageVO.getReverseSortord());
			}
			logger.debug("end init document and dynamicpage");
			if (docVo == null || pageVO == null) {
				// TODO
			}
			logger.debug("start init request parameters ");
			Map<String, String> map = new HashMap<String, String>();
			Enumeration enumeration = request.getParameterNames();
			for (; enumeration.hasMoreElements();) {
				Object o = enumeration.nextElement();
				String name = o.toString();
				String[] values = request.getParameterValues(name);
				map.put(o.toString(), StringUtils.join(values, ";"));
			}
			logger.debug("end init request parameters ");
			docVo.setRequestParams(map);

			logger.debug("start init engine ");
			// 拿脚本执行引擎
			ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
			engine.put("request", request);
			engine.put("session", request.getSession());
			logger.debug("end init engine ");
			// 分页
			Integer currentPage = 1;
			Integer pageSize = 50;
			if (StringUtils.isNotBlank(request.getParameter("currentPage"))) {
				currentPage = Integer.parseInt(request.getParameter("currentPage"));
			}
			/*
			 * if(StringUtils.isNotBlank(request.getParameter("pageSize"))){
			 * pageSize =Integer.parseInt(request.getParameter("pageSize")); }
			 */
			// 对用户配置的是否分页，以及每页记录条数进行解析
			if (StringUtils.isNotBlank(request.getParameter("pageSize"))) {
				// 如果request中有pageSize则用该pageSize
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			} else if (docVo.getPageSize() != null && !docVo.getPageSize().equals("")) {
				// 否则用用户配置的默认pageSize
				if (docVo.getIsLimitPage() != null && docVo.getIsLimitPage() == 1) {
					pageSize = Integer.parseInt(docVo.getPageSize().toString());
				} else {
					// 不分页处理,则限制在1000条
					pageSize = 1000;
				}
			}
			String orderBy = docVo.getRequestParams().get("orderBy");
			String allowOrderBy = docVo.getRequestParams().get("allowOrderBy");
			docVo.setAllowOrderBy(allowOrderBy);
			docVo.setOrderBy(orderBy);
			logger.debug("start init dataMap ");
			Map<String, List<Map<String, String>>> dataMap = documentServiceImpl.initDocumentData(currentPage, pageSize,
					docVo, engine, pageVO);
			Set<String> key = dataMap.keySet();
			int totalPages = 0;
			for (String string : key) {
				totalPages = ((PageList<Map<String, String>>) dataMap.get(string)).getPaginator().getTotalPages();
			}
			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			Map<String, String> datam = new HashMap<String, String>();
			datam.put("size", Integer.toString(totalPages));
			dataList.add(datam);
			dataMap.put("totalPages", dataList);
			logger.debug("end init engine ");
			docVo.setListParams(dataMap);
			engine.put("DocumentUtils", new DocumentUtils(docVo, pageVO));
			String append = "";
			for (String str : dataMap.keySet()) {
				List<Map<String, String>> appList = dataMap.get(str);
				for (int i = 0; i < appList.size(); i++) {
					Map<String, String> dMap = appList.get(i);
					for (String dStr : dMap.keySet()) {
						append += dMap.get(dStr);
					}
				}
			}
			logger.debug(append);
			return dataMap;
		}

		return null;
	}

	@RequestMapping(value = "/getValueStr")
	@ResponseBody
	public Map<String, String> getValueStr(String id, String dynamicPageId, String docId, HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		// 校验必要参数

		// 查询模版
		// 渲染模版
		Map<String, String> returnMap = new HashMap<String, String>();
		logger.debug(" view.do start");
		// 新增：id为空时，为新增，获取dp，解析成HTML返回
		if (StringUtils.isBlank(id) && StringUtils.isBlank(dynamicPageId)) {
			// TODO ERROR
		} else {
			DocumentVO docVo = null;
			DynamicPageVO pageVO = null;
			logger.debug("start init document and dynamicpage ");
			// initDocAndPage(docVo, pageVO, id, docId, dynamicPageId);
			if (StringUtils.isBlank(docId)) {
				pageVO = formdesignerServiceImpl.findById(Long.parseLong(dynamicPageId));
				docVo = new DocumentVO();
				docVo.setUpdate(false);
				docVo.setDynamicPageId(pageVO.getId().toString());
				if (StringUtils.isNotBlank(id)) {
					docVo.setUpdate(true);
					docVo.setRecordId(id);
				}
				if (StringUtils.isNotEmpty(request.getParameter("selectId"))) {
					docVo.setSelectId(request.getParameter("selectId"));
				}
				docVo.setDynamicPageName(pageVO.getName());
				// 把列表页面配置的属性加到docVo中，在模板解析的时候使用
				docVo.setShowTotalCount(pageVO.getShowTotalCount());
				docVo.setIsLimitPage(pageVO.getIsLimitPage());
				docVo.setPageSize(pageVO.getPageSize());
				docVo.setShowReverseNum(pageVO.getShowReverseNum());
				docVo.setReverseNumMode(pageVO.getReverseNumMode());
				docVo.setReverseSortord(pageVO.getReverseSortord());
				// TODO 设置当前用户
			} else {
				docVo = documentServiceImpl.findById(docId);
				docVo.setUpdate(true);
				if (StringUtils.isNotBlank(dynamicPageId)) {
					pageVO = formdesignerServiceImpl.findById(Long.parseLong(dynamicPageId));
				} else {
					pageVO = formdesignerServiceImpl.findById(Long.parseLong(docVo.getDynamicPageId()));
				}
				docVo.setDynamicPageId(pageVO.getId().toString());
				docVo.setDynamicPageName(pageVO.getName());
				// 把列表页面配置的属性加到docVo中，在模板解析的时候使用
				docVo.setShowTotalCount(pageVO.getShowTotalCount());
				docVo.setIsLimitPage(pageVO.getIsLimitPage());
				docVo.setPageSize(pageVO.getPageSize());
				docVo.setShowReverseNum(pageVO.getShowReverseNum());
				docVo.setReverseNumMode(pageVO.getReverseNumMode());
				docVo.setReverseSortord(pageVO.getReverseSortord());
			}
			logger.debug("end init document and dynamicpage");
			if (docVo == null || pageVO == null) {
				// TODO
			}
			logger.debug("start init request parameters ");
			Map<String, String> map = new HashMap<String, String>();
			Enumeration enumeration = request.getParameterNames();
			for (; enumeration.hasMoreElements();) {
				Object o = enumeration.nextElement();
				String name = o.toString();
				String[] values = request.getParameterValues(name);
				map.put(o.toString(), StringUtils.join(values, ";"));
			}
			logger.debug("end init request parameters ");
			docVo.setRequestParams(map);

			Map<String, Object> root = new HashMap<String, Object>();
			logger.debug("start init engine ");
			// 拿脚本执行引擎
			ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
			engine.put("request", request);
			engine.put("session", request.getSession());
			logger.debug("end init engine ");
			// 分页
			Integer currentPage = 1;
			Integer pageSize = 50;
			if (StringUtils.isNotBlank(request.getParameter("currentPage"))) {
				currentPage = Integer.parseInt(request.getParameter("currentPage"));
			}
			/*
			 * if(StringUtils.isNotBlank(request.getParameter("pageSize"))){
			 * pageSize =Integer.parseInt(request.getParameter("pageSize")); }
			 */
			// 对用户配置的是否分页，以及每页记录条数进行解析
			if (StringUtils.isNotBlank(request.getParameter("pageSize"))) {
				// 如果request中有pageSize则用该pageSize
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			} else if (docVo.getPageSize() != null && !docVo.getPageSize().equals("")) {
				// 否则用用户配置的默认pageSize
				if (docVo.getIsLimitPage() != null && docVo.getIsLimitPage() == 1) {
					pageSize = Integer.parseInt(docVo.getPageSize().toString());
				} else {
					// 不分页处理,则限制在1000条
					pageSize = 1000;
				}
			}
			String orderBy = docVo.getRequestParams().get("orderBy");
			String allowOrderBy = docVo.getRequestParams().get("allowOrderBy");
			docVo.setAllowOrderBy(allowOrderBy);
			docVo.setOrderBy(orderBy);
			logger.debug("start init dataMap ");
			Map<String, List<Map<String, String>>> dataMap = documentServiceImpl.initDocumentData(currentPage, pageSize,
					docVo, engine, pageVO);
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			logger.debug("end init engine ");
			docVo.setListParams(dataMap);
			engine.put("DocumentUtils", new DocumentUtils(docVo, pageVO));
			String append = "";
			for (String str : dataMap.keySet()) {
				List<Map<String, String>> appList = dataMap.get(str);
				for (int i = 0; i < appList.size(); i++) {
					Map<String, String> dMap = appList.get(i);
					String ID = dMap.get("ID") == null ? "" : dMap.get("ID");
					String field1 = dMap.get("field1") == null ? "" : dMap.get("field1");
					String field2 = dMap.get("field2") == null ? "" : dMap.get("field2");
					String field3 = dMap.get("field3") == null ? "" : dMap.get("field3");
					append += "<li class='ui-li-has-thumb'><div class='behind'>";
					append += "<a href='#' id='" + ID + "' class='ui-btn delete-btn' onclick='listDel(this.id)'>删除</a>";
					append += "</div>";
					append += "<a href=\"javascript:location.href='" + basePath + "document/view.do?id=" + ID
							+ "&dynamicPageId=" + DocumentUtils.getMPageIdByMListPageId(dynamicPageId) + "'\" id='" + ID
							+ "' class='ui-btn ui-nodisc-icon ui-alt-icon ui-btn-icon-right ui-icon-carat-r'>";
					append += "<h3>" + field1 + "</h3>";
					append += "<p><span>" + field2 + "</span>&emsp;";
					append += "<span>" + field3 + "</span></p>";
					append += "</a></li>";
				}
				returnMap.put("data", append);
				return returnMap;
			}
		}
		return null;
	}

	@RequestMapping(value = "/publicDelBySql")
	@ResponseBody
	public Map<String, String> publicDelBySql(String id, String dynamicPageId, String docId,
			HttpServletResponse response, HttpServletRequest request) throws Exception {
		// 校验必要参数

		// 查询模版
		// 渲染模版
		Map<String, String> returnMap = new HashMap<String, String>();
		logger.debug(" view.do start");
		// 新增：id为空时，为新增，获取dp，解析成HTML返回
		if (StringUtils.isBlank(id) && StringUtils.isBlank(dynamicPageId)) {
			// TODO ERROR
		} else {
			DocumentVO docVo = null;
			DynamicPageVO pageVO = null;
			logger.debug("start init document and dynamicpage ");
			// initDocAndPage(docVo, pageVO, id, docId, dynamicPageId);
			if (StringUtils.isBlank(docId)) {
				pageVO = formdesignerServiceImpl.findById(Long.parseLong(dynamicPageId));
				docVo = new DocumentVO();
				docVo.setUpdate(false);
				docVo.setDynamicPageId(pageVO.getId().toString());
				if (StringUtils.isNotBlank(id)) {
					docVo.setUpdate(true);
					docVo.setRecordId(id);
				}
				if (StringUtils.isNotEmpty(request.getParameter("selectId"))) {
					docVo.setSelectId(request.getParameter("selectId"));
				}
				docVo.setDynamicPageName(pageVO.getName());
				// 把列表页面配置的属性加到docVo中，在模板解析的时候使用
				docVo.setShowTotalCount(pageVO.getShowTotalCount());
				docVo.setIsLimitPage(pageVO.getIsLimitPage());
				docVo.setPageSize(pageVO.getPageSize());
				docVo.setShowReverseNum(pageVO.getShowReverseNum());
				docVo.setReverseNumMode(pageVO.getReverseNumMode());
				docVo.setReverseSortord(pageVO.getReverseSortord());
				// TODO 设置当前用户
			} else {
				docVo = documentServiceImpl.findById(docId);
				docVo.setUpdate(true);
				if (StringUtils.isNotBlank(dynamicPageId)) {
					pageVO = formdesignerServiceImpl.findById(Long.parseLong(dynamicPageId));
				} else {
					pageVO = formdesignerServiceImpl.findById(Long.parseLong(docVo.getDynamicPageId()));
				}
				docVo.setDynamicPageId(pageVO.getId().toString());
				docVo.setDynamicPageName(pageVO.getName());
				// 把列表页面配置的属性加到docVo中，在模板解析的时候使用
				docVo.setShowTotalCount(pageVO.getShowTotalCount());
				docVo.setIsLimitPage(pageVO.getIsLimitPage());
				docVo.setPageSize(pageVO.getPageSize());
				docVo.setShowReverseNum(pageVO.getShowReverseNum());
				docVo.setReverseNumMode(pageVO.getReverseNumMode());
				docVo.setReverseSortord(pageVO.getReverseSortord());
			}
			logger.debug("end init document and dynamicpage");
			if (docVo == null || pageVO == null) {
				// TODO
			}
			logger.debug("start init request parameters ");
			Map<String, String> map = new HashMap<String, String>();
			Enumeration enumeration = request.getParameterNames();
			for (; enumeration.hasMoreElements();) {
				Object o = enumeration.nextElement();
				String name = o.toString();
				String[] values = request.getParameterValues(name);
				map.put(o.toString(), StringUtils.join(values, ";"));
			}
			logger.debug("end init request parameters ");
			docVo.setRequestParams(map);

			logger.debug("start init engine ");
			// 拿脚本执行引擎
			ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
			engine.put("request", request);
			engine.put("session", request.getSession());
			logger.debug("end init engine ");
			// 分页
			Integer currentPage = 1;
			Integer pageSize = 50;
			if (StringUtils.isNotBlank(request.getParameter("currentPage"))) {
				currentPage = Integer.parseInt(request.getParameter("currentPage"));
			}
			/*
			 * if(StringUtils.isNotBlank(request.getParameter("pageSize"))){
			 * pageSize =Integer.parseInt(request.getParameter("pageSize")); }
			 */
			// 对用户配置的是否分页，以及每页记录条数进行解析
			if (StringUtils.isNotBlank(request.getParameter("pageSize"))) {
				// 如果request中有pageSize则用该pageSize
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			} else if (docVo.getPageSize() != null && !docVo.getPageSize().equals("")) {
				// 否则用用户配置的默认pageSize
				if (docVo.getIsLimitPage() != null && docVo.getIsLimitPage() == 1) {
					pageSize = Integer.parseInt(docVo.getPageSize().toString());
				} else {
					// 不分页处理,则限制在1000条
					pageSize = 1000;
				}
			}
			String orderBy = docVo.getRequestParams().get("orderBy");
			String allowOrderBy = docVo.getRequestParams().get("allowOrderBy");
			docVo.setAllowOrderBy(allowOrderBy);
			docVo.setOrderBy(orderBy);
			logger.debug("start init dataMap ");
			if (id != null || !"".equals(id)) {
				Map<String, String> delMap = new HashMap<String, String>();
				try {
					String sql = "";
					String dataJson = pageVO.getDataJson();
					Map<String, DataDefine> ma = PageDataBeanWorker
							.convertConfToDataDefines(StringEscapeUtils.unescapeHtml4(dataJson));
					Set<String> key = ma.keySet();
					for (String s : key) {
						DataDefine mas = ma.get(s);
						sql = mas.getDeleteSql();
					}
					sql = sql.replace("\"", " ") + " = '" + id + "'";
					DocumentService service = Springfactory.getBean("documentServiceImpl");
					service.excuteUpdate(sql);
					delMap.put("message", "1");
					return delMap;
				} catch (Exception e) {
					// TODO: handle exception
					logger.info("ERROR", e);
					delMap.put("message", "0");
					return delMap;

				}

			}
			Map<String, List<Map<String, String>>> dataMap = documentServiceImpl.initDocumentData(currentPage, pageSize,
					docVo, engine, pageVO);
			Set<String> key = dataMap.keySet();
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			int totalPages = 0;
			for (String string : key) {
				totalPages = ((PageList<Map<String, String>>) dataMap.get(string)).getPaginator().getTotalPages();
			}
			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			Map<String, String> datam = new HashMap<String, String>();
			datam.put("size", Integer.toString(totalPages));
			dataList.add(datam);
			dataMap.put("totalPages", dataList);
			logger.debug("end init engine ");
			docVo.setListParams(dataMap);
			engine.put("DocumentUtils", new DocumentUtils(docVo, pageVO));
			String append = "";
			for (String str : dataMap.keySet()) {
				List<Map<String, String>> appList = dataMap.get(str);
				for (int i = 1; i < appList.size(); i++) {
					Map<String, String> dMap = appList.get(i);
					String ID = dMap.get("ID") == null ? "" : dMap.get("ID");
					String field1 = dMap.get("field1") == null ? "" : dMap.get("field1");
					String field2 = dMap.get("field2") == null ? "" : dMap.get("field2");
					String field3 = dMap.get("field3") == null ? "" : dMap.get("field3");
					append += "<li class='ui-li-has-thumb'><div class='behind'>";
					append += "<a href='#' id='" + ID + "' class='ui-btn delete-btn' onclick='listDel(this.id)'>删除</a>";
					append += "</div>";
					append += "<a href=\"javascript:location.href='" + basePath + "document/view.do?id=" + ID
							+ "&dynamicPageId=" + DocumentUtils.getMPageIdByMListPageId(dynamicPageId) + "'\" id='" + ID
							+ "' class='ui-btn ui-nodisc-icon ui-alt-icon ui-btn-icon-right ui-icon-carat-r'>";
					append += "<h3>" + field1 + "</h3>";
					append += "<p><span>" + field2 + "</span>&nbsp;";
					append += "<span>" + field3 + "</span></p>";
					append += "</a></li>";
				}
			}
			returnMap.put("data", append);
			return returnMap;
		}

		return null;
	}

	public String getFormIdByPageId(String pageId) {
		return "1405";
	}
}
