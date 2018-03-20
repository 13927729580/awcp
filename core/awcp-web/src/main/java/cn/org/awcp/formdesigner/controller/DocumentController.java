package cn.org.awcp.formdesigner.controller;

import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.BeanUtils;
import cn.org.awcp.core.utils.DateUtils;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.DocumentService;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.application.vo.PageActVO;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.formdesigner.core.domain.DynamicPage;
import cn.org.awcp.formdesigner.core.domain.design.context.data.DataDefine;
import cn.org.awcp.formdesigner.core.parse.bean.PageDataBeanWorker;
import cn.org.awcp.formdesigner.engine.util.VirtualRequest;
import cn.org.awcp.formdesigner.service.IDocumentService;
import cn.org.awcp.formdesigner.service.PrintService;
import cn.org.awcp.extend.formdesigner.DocumentUtils;
import cn.org.awcp.formdesigner.utils.ScriptEngineUtils;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.metadesigner.application.MetaModelService;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.exception.PlatformException;
import cn.org.awcp.venson.service.FileService;
import cn.org.awcp.venson.service.impl.FileServiceImpl.AttachmentVO;
import cn.org.awcp.venson.util.BeanUtil;
import cn.org.awcp.venson.util.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.itextpdf.text.DocumentException;
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
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

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
	private IDocumentService iDocumentServiceImpl;

	@Autowired
	private StoreService storeServiceImpl;

	@Autowired
	private PrintService printServiceImpl;

	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;

	@Autowired
	SzcloudJdbcTemplate jdbcTemplate;

	/**
	 *
	 * 根据参数Map和脚本环境，解析出sql语句
	 * 根据sql语句查询数据 根据数据初始化各组件的状态(各个脚本包括选项脚本) 读取模版 模版+数据+状态 =输出
	 *
	 * @param dynamicPageId
	 * @param request
	 * @return
	 * @throws ScriptException
	 * @throws IOException
	 */
	@RequestMapping(value = "/view")
	public void view(String dynamicPageId,HttpServletRequest request,HttpServletResponse response)
			throws ScriptException,IOException {
		response.setContentType("text/html;");
		response.getWriter().write(iDocumentServiceImpl.view(dynamicPageId,request));
	}

	@RequestMapping(value = "/excute")
	public ModelAndView excute(HttpServletRequest request, HttpServletResponse response)
			throws ScriptException, IOException {
		ModelAndView mv = new ModelAndView();
		String actId = request.getParameter("actId");
		String pageId = request.getParameter("dynamicPageId");
		String docId = request.getParameter("docId");
		String update = request.getParameter("update");
		String[] _selects = request.getParameterValues("_selects");
		DocumentVO docVo ;
		DocumentUtils utils = DocumentUtils.getIntance();
		DynamicPageVO pageVO;
		if (StringUtils.isBlank(pageId)) {
			throw new PlatformException("动态页面dynamicPageId为空");
		}
		if (StringUtils.isBlank(actId)) {
			throw new PlatformException("动作actId为空");
		}
		pageVO = formdesignerServiceImpl.findById(pageId);
		if (pageVO == null) {
			throw new PlatformException("动态页面未找到");
		}
		StoreVO store = storeServiceImpl.findById(actId);
		if (store == null) {
			throw new PlatformException("按钮动作ID未找到");
		}
		boolean isUpdate = false;
		if (StringUtils.isNotBlank(update)) {
			isUpdate = "true".equalsIgnoreCase(update);
		}
		// 初始化表单数据
		docVo = documentServiceImpl.findById(docId);
		docVo = BeanUtil.instance(docVo, DocumentVO.class);
		docVo.setUpdate(isUpdate);
		docVo.setDynamicPageId(pageId);
		docVo.setDynamicPageName(pageVO.getName());
		Map<String,String[]> enumeration = request.getParameterMap();
		Map<String, String> map = new HashMap<>(enumeration.size());
		for(Map.Entry<String,String[]> entry:enumeration.entrySet()){
			map.put(entry.getKey(), StringUtils.join(entry.getValue(), ";"));
		}
		docVo.setRequestParams(map);
		docVo = documentServiceImpl.processParams(docVo);
		PageActVO act = JSON.parseObject(store.getContent(), PageActVO.class);
		// 初始化脚本解释执行器,加载全局工具类
		ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
		engine.put("request", request);
		engine.put("session", SessionUtils.getCurrentSession());

		// 如跳转、返回之类的
		if (StringUtils.isNotBlank(act.getServerScript())) {
			String ret;
			try {
				jdbcTemplate.beginTranstaion();
				String script = StringEscapeUtils.unescapeHtml4(act.getServerScript());
				ret = (String) engine.eval(script);
				jdbcTemplate.commit();
				if (ret != null && (ret.startsWith("redirect:") || ret.startsWith("forward:"))) {
					mv.setViewName(ret);
					return mv;
				}
			} catch (Exception e) {
				logger.info("ERROR", e);
				jdbcTemplate.rollback();
			}
		} else {
			// 校验文档
			switch (act.getActType()) {
			// 保存--不带流程
			case 2001:
				PunUserBaseInfoVO user = ControllerHelper.getUser();
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
					docVo.setLastmodified(new Date());
					utils.saveDocument();
				} else {
					docVo.setCreated(new Date());
					docVo.setAuthorId(user.getUserId());
					docVo.setLastmodified(docVo.getCreated());
					// 保存数据 向document插入数据
					for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
						String o = it.next();
						utils.saveData(o);
					}
					utils.saveDocument();
				}
				break;
			case 2002:
				// TODO 返回按钮实现
				Long backId= Long.parseLong(act.getExtbute().get("target"));
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
				}
				break;
			// 新增
			case 2009:
				mv.addObject("dynamicPageId", act.getExtbute().get("target"));
				mv.setViewName("redirect:/document/view.do");
				return mv;
			// 编辑
			case 2010:
				if (_selects != null && _selects.length == 1) {
					mv.setViewName("redirect:/document/view.do?id=" + _selects[0] + "&dynamicPageId="
							+ act.getExtbute().get("target"));
					return mv;
				}
				break;
			// pdf打印
			case 2014:
				// 1、准备参数
				String script = act.getExtbute().get("script");
				Map params = new HashMap(16);
				if (StringUtils.isNotBlank(script)) {
					params = (Map) engine.eval(StringEscapeUtils.unescapeHtml4(script));
				}
				List listPages = formdesignerServiceImpl.getChildListPages(pageVO.getId());
				StringBuilder sb = new StringBuilder();
				if (listPages != null && listPages.size() > 0) {
					for (int k = 0; k < listPages.size(); k++) {
						sb.append(listPages.get(k) + ",");
					}
				}

				Map totalMap = new HashMap(16);
				totalMap.put("pageVOId", pageVO.getId().toString());
				totalMap.put("listPages", sb.toString());
				totalMap.putAll(map);
				totalMap.putAll(params);
				// request的getParament无法重置，所有自己模拟一个virtualRequest用于存跳转参数
				VirtualRequest virtualRequest = new VirtualRequest(totalMap);
				print(virtualRequest, request, response);
				return null;
			// Excel导出
			case 2016:
				// 1、准备参数
				String excelScript = act.getExtbute().get("script");
				Map<String, String> excelParams = new HashMap<>(16);
				if (StringUtils.isNotBlank(excelScript)) {
					excelParams = (Map<String, String>) engine.eval(StringEscapeUtils.unescapeHtml4(excelScript));
				}
				// excel模板的Id
				String templateFileId = act.getExtbute().get("templateFileId");
				// 导出数据的sql
				String sqlScript = act.getExtbute().get("sqlScript");
				String actSql = "";
				if (StringUtils.isNotBlank(sqlScript)) {
					actSql = (String) engine.eval(StringEscapeUtils.unescapeHtml4(sqlScript));
				}

				// 此处改为直接调用函数，参考打印pdf同样的调用方式
				Map paramMap = new HashMap(16);
				paramMap.put("actSql", actSql);
				paramMap.put("templateFileId", templateFileId);
				paramMap.put("excelParams", excelParams);
				paramMap.putAll(map);
				// request的getParament无法重置，所有自己模拟一个virtualRequest用于存跳转参数
				VirtualRequest vRequest = new VirtualRequest(paramMap);
				excelListPage(vRequest, request, response);
				return null;

			default:
				break;
			}

		}

		// 数据校验
		mv.addObject("dynamicPageId", pageId);
		mv.addObject("id", docVo.getRecordId());
		mv.setViewName("redirect:/document/view.do");
		return mv;
	}

	/***
	 * 执行动作脚本
	 * 
	 * @param actId
	 *            动作ID
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "executeAct", method = RequestMethod.GET)
	public ReturnResult executeAct(String actId, HttpServletRequest request) throws ScriptException {
		ReturnResult result = ReturnResult.get();
		result.setStatus(StatusCode.SUCCESS).setData(iDocumentServiceImpl.eval(actId,null,null,request));
		return result;
	}



	@ResponseBody
	@RequestMapping(value = "/excuteOnly")
	public void excuteOnly(HttpServletRequest request,@RequestParam("dynamicPageId") String dynamicPageId,
						   @RequestParam(value="docId",required = false)String docId,
						   @RequestParam(value="actId",required = false)String actId) throws Exception {
		DocumentVO docVo = documentServiceImpl.findById(docId);
		docVo=BeanUtil.instance(docVo,DocumentVO.class);
		ControllerHelper.renderJSON(null,iDocumentServiceImpl.execute(actId,dynamicPageId,docVo,request));
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
			throws ScriptException {

		String docId = paramRequest.getParameter("docId");
		String pageId = paramRequest.getParameter("dynamicPageId");
		String workflowId = paramRequest.getParameter("workflowId");


		String actId = paramRequest.getParameter("actId");
		StoreVO store = storeServiceImpl.findById(actId);
		PageActVO act = JSON.parseObject(store.getContent(), PageActVO.class);

		// 根据pageId，获取组件，然后进行request遍历，组装成Map（documentVo.requestParams），key为组件name

		DocumentVO docVo = new DocumentVO();

		DynamicPageVO pageVO = null;
		// 查找模版
		if (StringUtils.isNotBlank(pageId)) {
			pageVO = formdesignerServiceImpl.findById(pageId);
		}

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

		JSONObject jcon = new JSONObject();
		jcon.put("relatePageId", pageVO.getId());
		jcon.put("componentType", "");
		List<JSONObject> components = formdesignerServiceImpl.getComponentByContainerWithColumn(jcon);
		Map finalMap = getMap(pageVO, engine, dataMap);


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
		if (StringUtils.isBlank(templetId)) {
			throw new PlatformException("打印模板不能为空");
		}
		StoreVO printManageVO = storeServiceImpl.findById(templetId);
		if (printManageVO == null) {
			throw new PlatformException("打印模板不能为空");
		}
		JSONObject printJson = JSON.parseObject(printManageVO.getContent());
		String fileName = "申报书.pdf";
		if (StringUtils.isNoneBlank(printJson.getString("pdfName"))) {
			java.util.Date now = new java.util.Date();
			String dateStr = DateUtils.format(now, "ddMMyyyy_HHmmss");
			fileName = printJson.getString("pdfName") + dateStr + ".pdf";
		}
		// 设置在线预览方式打开Pdf文件
		response.setContentType("application/pdf;charset=UTF-8");
		response.setHeader("Content-disposition", "inline; filename=" + ControllerHelper.processFileName(fileName));
		OutputStream out;
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
			// 有配置自定义模板
			if (StringUtils.isNotBlank(printJson.getString("templateFileId"))) {
				printServiceImpl.printPDFByTemplate(printManageVO, dataMap, out, components);
			} else {
				printServiceImpl.printPDF(printManageVO, dataMap, out);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("ERROR", e);
		}
	}

	private Map getMap(DynamicPageVO pageVO, ScriptEngine engine, Map<String, List<Map<String, String>>> dataMap) throws ScriptException {
		// 对表格组件进行处理，将真实值转换为显示值；比如一些 日期格式化、code转对应value等；
		if (dataMap != null && !dataMap.isEmpty()) {
			BaseExample baseExample = new BaseExample();
			baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", pageVO.getId()).andLike("CODE",
					StoreService.COMPONENT_CODE + "%");
			List<StoreVO> columnComs = storeServiceImpl.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, null);
			for (int k = 0; k < columnComs.size(); k++) {
				JSONObject o = JSON.parseObject(columnComs.get(k).getContent());
				// 列组件或者表格组件
				if ("1017".equals(o.getString("componentType"))) {
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
		return finalMap;
	}

	public void excelListPage(VirtualRequest paramRequest, HttpServletRequest request, HttpServletResponse response)
			throws ScriptException, IOException {
		String docId = paramRequest.getParameter("docId");
		String pageId = paramRequest.getParameter("dynamicPageId");
		String templateFileId = paramRequest.getParameter("templateFileId");
		String actSql = paramRequest.getParameter("actSql");
		// 根据pageId，获取组件，然后进行request遍历，组装成Map（documentVo.requestParams），key为组件name
		DocumentVO docVo = new DocumentVO();
		DynamicPageVO pageVO;
		// 查找模版
		pageVO = formdesignerServiceImpl.findById(pageId);
		if (pageVO == null) {
			throw new PlatformException("动态页面为空");
		}
		docVo.setId(docId);
		docVo.setDynamicPageId(pageId);
		docVo.setDynamicPageName(pageVO.getName());
		// 初始化脚本解释执行器,加载全局工具类
		ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
		// TODO put engine 工具类库
		engine.put("request", request);
		engine.put("session", request.getSession());
		// 分页
		String orderBy = docVo.getRequestParams().get("orderBy");
		String allowOrderBy = docVo.getRequestParams().get("allowOrderBy");
		docVo.setAllowOrderBy(allowOrderBy);
		docVo.setOrderBy(orderBy);
		Map<String, List<Map<String, String>>> dataMap = new HashMap<>();
		// 当excel导出动作的sql脚本不为空，则根据该sql查找数据进行导出
		if (StringUtils.isNotBlank(actSql)) {

			// 此处由于documentService的excuteQueryForList查找出来的类型是List<Map<String,Object>>
			// 需要转换为List<Map<String,String>>类型的数据
			List<Map<String, Object>> sqlData = documentServiceImpl.excuteQueryForList(actSql);
			List<Map<String, String>> stringData = new ArrayList<Map<String, String>>();
			for (Map<String, Object> tem1 : sqlData) {
				Map<String, String> tem2 = new HashMap<>();
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
		// 导出excel动作中的sql脚本为空，则根据列表页面的数据源来导出
		} else {
			dataMap = documentServiceImpl.initDocumentData(1, MAX_EXCEL_PAGE_SIZE, docVo, engine, pageVO);
		}
		if (dataMap.isEmpty()) {
			throw new PlatformException("数据源或sql脚本不能为空");
		}
		List<JSONObject> columns = new ArrayList<JSONObject>();
		// 如果没有模板，则根据执行下面代找到所有列框，然后根据列框来导出数据
		if (!StringUtils.isNotBlank(templateFileId)) {
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
			Collections.sort(columns,Comparator.comparingInt(o->o.getIntValue("order")));
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
		response.setHeader("Content-disposition",
				"attachment; filename=" + ControllerHelper.processFileName(pageVO.getName() + "-列表.xls"));
		printExcel(columns, dataMap, response.getOutputStream(), templateFileId);
	}

	public Map getListPageData(VirtualRequest paramRequest, HttpServletRequest request) throws ScriptException {

		Map<String, List> returnMap = new HashMap<String, List>();

		DynamicPage dp = DynamicPage.get(DynamicPage.class, paramRequest.getParameter("pageVOId"));
		DynamicPageVO vo = BeanUtils.getNewInstance(dp, DynamicPageVO.class);

		String listPagesStr;
		// 列表页面，则返回自己
		if (vo.getPageType() == 1003 && StringUtils.isNotEmpty(paramRequest.getParameter("dynamicPageId")))
        {
            listPagesStr = paramRequest.getParameter("dynamicPageId");
        } else {
            listPagesStr = paramRequest.getParameter("listPages");
        }
		String[] listPages = null;
		if (StringUtils.isNotBlank(listPagesStr)) {
			listPages = listPagesStr.split(",");
		}
		if (listPages != null && listPages.length > 0) {
			for (int index = 0; index < listPages.length; index++) {
				String dynamicPageId = listPages[index];
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
						// 列组件或者表格组件
						if ("1008".equals(o.getString("componentType"))) {
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
						// 取到dataMap第一个value
						List<Map<String, String>> resultList = dataMap.values().iterator().next();
						String key = dataMap.keySet().iterator().next();
						String alias = key.substring(0, key.lastIndexOf("_list"));
						List<Map<String, String>> finalResultList = new ArrayList<>();
						if (resultList != null) {
							// 取出的map中带有多列，过滤，剩列组件对应的列
							for (int i = 0; i < resultList.size(); i++) {
								Map<String, String> map = resultList.get(i);
								Map<String, String> finalMap = new LinkedHashMap<>();

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

	@Autowired
	private MetaModelService metaModelServiceImpl;

	/**
	 * ajax刷新表格数据，如果传入的method为delete，则执行删除，返回查询列表数据
	 * 
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "/refreshDataGrid")
	public ReturnResult refreshDataGrid(HttpServletRequest request,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
		ReturnResult result = ReturnResult.get();
		String componentId = request.getParameter("componentId");

		StoreVO s = storeServiceImpl.findById(componentId);

		String method = request.getParameter("method");

		JSONObject oo = JSON.parseObject(s.getContent());
		String dataAlias;
		if (request.getParameter("dataFile") != null) {
			dataAlias = oo.getString(request.getParameter("dataFile"));
		} else {
			dataAlias = oo.getString("dataAlias");
		}
		String pageId = s.getDynamicPageId();
		DynamicPageVO pageVo = formdesignerServiceImpl.findById(pageId);
		Map<String, DataDefine> map = PageDataBeanWorker
				.convertConfToDataDefines(StringEscapeUtils.unescapeHtml4(pageVo.getDataJson()));
		DataDefine dd = map.get(dataAlias);
		if ("delete".equals(method)) {
			String selects = request.getParameter("_selects");
			String[] _selects = null;
			if (selects != null) {
				_selects = selects.split(",");
			}

			DocumentUtils utils = DocumentUtils.getIntance();
			if (_selects != null && _selects.length >= 1) {
				for (String select : _selects) {
					utils.deleteData(pageVo, select);
				}
			}
		} else if ("save".equals(method)) {
			String json = request.getParameter("json");
			JSONArray arr = JSON.parseArray(json);
			if (arr != null && !arr.isEmpty()) {
				int size = arr.size();
				for (int i = 0; i < size; i++) {
					JSONObject obj = arr.getJSONObject(i);
					try {
						meta.update(obj.toJavaObject(Map.class),
								metaModelServiceImpl.queryByModelCode(dd.getModelCode()).getTableName());
					} catch (Exception e) {
						result.setData(Collections.EMPTY_LIST).setTotal(0);
						return result;
					}
				}
			}

		}

		getDataGridItems(request, s, dd, oo, currentPage, pageSize, result);
		return result;
	}

	/**
	 * 获取对应的表格数据
	 * 
	 * @param request
	 * @param s
	 *            表格组件
	 * @return
	 */
	public void getDataGridItems(HttpServletRequest request, StoreVO s, DataDefine dd, JSONObject oo, int currentPage,
			int pageSize, ReturnResult result) {

		if (s != null) {

			ScriptEngine engine = ScriptEngineUtils.getScriptEngine();
			DocumentVO docVo = new DocumentVO();
			engine.put("request", request);
			try {
				PageList<Map<String, String>> pageList = documentServiceImpl.getDataListByDataDefine(dd, engine,
						currentPage, pageSize, null);

				if (pageList == null || pageList.isEmpty()) {
					result.setData(Collections.EMPTY_LIST).setTotal(0);
					return;
				}

				// 操作显示脚本
				Map<String, List<Map<String, String>>> dataMap = new HashMap<>();
				dataMap.put(dd.getName() + "_list", pageList);
				docVo.setListParams(dataMap);

				String showScript = oo.getString("showScript");
				if (StringUtils.isNoneBlank(showScript)) {
					if (StringUtils.isNotBlank(showScript)) {
						engine.eval(StringEscapeUtils.unescapeHtml4(showScript));
					}
				}
				result.setData(pageList).setTotal(pageList.getPaginator().getTotalCount());
			} catch (ScriptException e) {
				result.setStatus(StatusCode.FAIL.setMessage("服务器出错，请联系管理员"));
				logger.info("ERROR", e);
			}
		}
	}

	@Resource(name = "IFileService")
	private FileService fileService;

	private void printExcel(List<JSONObject> columns, Map<String, List<Map<String, String>>> dataMap, OutputStream os,
			String templateFileId) {
		// 当模板ID不为空，找到模板
		if (templateFileId != null && StringUtils.isNoneBlank(templateFileId)) {
			AttachmentVO att = fileService.get(templateFileId);
			if (att == null) {
				throw new PlatformException("模板未找到，请重新上传");
			}
			InputStream in = fileService.getInputStream(att);
			if (in == null) {
				throw new PlatformException("模板未找到，请重新上传");
			}
			List<Map<String, String>> list;
			if (dataMap.get("sqlData") != null) {
				// 当根据sql导出时，dataMap中数据只有一个List
				list = dataMap.get("sqlData");
			} else {
				list = dataMap.values().iterator().next();
			}
			ExcelUtil.exportExcelByTemplate((List) list, in, os);
		} else {
			List<Map<String, String>> list;
			if (dataMap.containsKey("sqlData")) {
				list = dataMap.get("sqlData");
			} else {
				list = dataMap.values().iterator().next();
			}
			ExcelUtil.exportExcelByCreate(columns.stream().map(j -> {
				Map<String, String> head = new HashMap<>();
				head.put("title", j.getString("columnName"));
				head.put("field", j.getString("dataItemCode").split("\\.")[1]);
				return head;
			}).collect(Collectors.toList()), (List) list, os);

		}

	}

	@RequestMapping(value = "/batchPrint")
	public void batchPrint(HttpServletRequest request, HttpServletResponse response) throws ScriptException {
		String dynamicPages = request.getParameter("dynamicPageIds");

		String[] dynamicPageArr = dynamicPages.split(",");
		Map<String, String[]> ParameterMap = request.getParameterMap();

		String fileName = ((PunUserBaseInfoVO) (SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER)))
				.getName();
		VirtualRequest virtualRequest = new VirtualRequest();
		response.setContentType("text/html;charset=UTF-8");
		response.setContentType("application/x-msdownload;");
		response.setHeader("Content-disposition",
				"attachment; filename=" + ControllerHelper.processFileName(fileName + ".pdf"));

		List<StoreVO> printManageVOs = new ArrayList<>();
		List<Map> dataMaps = new ArrayList<>();
		List<List<JSONObject>> componentsList = new ArrayList<>();

		for (int i = 0; i < dynamicPageArr.length; i++) {
			String dynamicPageId = dynamicPageArr[i];
			DynamicPageVO pageVO = null;
			pageVO = formdesignerServiceImpl.findById(dynamicPageId);
			for (String key : ParameterMap.keySet()) {
				if (ParameterMap.get(key) != null) {
					String[] values = ParameterMap.get(key);
					virtualRequest.setParameter(key, StringUtils.join(values, ";"));
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

			JSONObject jcon = new JSONObject();
			jcon.put("relatePageId", pageVO.getId());
			jcon.put("componentType", "");
			List<JSONObject> components = formdesignerServiceImpl.getComponentByContainerWithColumn(jcon);
			componentsList.add(components);
			Map finalMap = getMap(pageVO, engine, dataMap);
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
			logger.info("ERROR", e1);
		}

	}

	private Map<String, String> getOptionTextMapByPage(ScriptEngine engine, DynamicPageVO pageVO) {

		Map<String, String> optionMap = new HashMap<>();
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



}
