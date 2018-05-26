package cn.org.awcp.dingding.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.extend.formdesigner.DocumentUtils;
import cn.org.awcp.formdesigner.application.service.DocumentService;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.formdesigner.utils.ScriptEngineUtils;
import cn.org.awcp.venson.controller.base.ControllerHelper;

/**
 * 钉钉不走流程的表单页面显示
 * @author yqtao
 *
 */
@RequestMapping(value="/dingding")
@Controller
public class DdDocController {

	protected static final Log logger = LogFactory.getLog(DdDocController.class);
	
	@Autowired
	private FormdesignerService formdesignerServiceImpl;
	
	@Autowired
	private DocumentService documentServiceImpl;

	@Autowired
	private StoreService storeServiceImpl;

	@Autowired
	private SzcloudJdbcTemplate jdbcTemplate;
	
	/**
	 * 非流程表单View页面
	 * @param id			表单数据ID
	 * @param dynamicPageId	动态页面ID
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/view")
	public ModelAndView view(String id, String dynamicPageId, HttpServletResponse response,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("dingding/workLogDetail");
		try{
			mav.addObject("id", id);	//表单数据ID
			mav.addObject("dynamicPageId", dynamicPageId);	//动态页面ID
			
			logger.debug(" view.do start");
			if (StringUtils.isBlank(id) || StringUtils.isBlank(dynamicPageId)) {
				throw new Exception("显示页面必须有id和dynamicPageId");
			} else {
				logger.debug("start init document and dynamicpage ");
				DocumentVO docVo = null;
				DynamicPageVO pageVO = null;
				pageVO = formdesignerServiceImpl.findById(dynamicPageId);
				docVo = new DocumentVO();
				logger.debug("end init document and dynamicpage");
				
				//请求传过来的参数
				logger.debug("start init request parameters ");
				Map<String, String> map = new HashMap<String, String>();
				Map<String, String[]> parameterMap = request.getParameterMap();
				for (String key : parameterMap.keySet()) {
					map.put(key, StringUtils.join(parameterMap.get(key), ";"));
				}
				docVo.setRequestParams(map);
				logger.debug("end init request parameters ");
				
				// 脚本执行引擎
				logger.debug("start init engine ");
				ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
				engine.put("request", request);
				logger.debug("end init engine ");
				
				//通过配置的数据源脚本获取数据
				logger.debug("start init dataMap ");
				Map<String, List<Map<String, String>>> dataMap = documentServiceImpl.initDocumentData(1, 1,docVo, engine, pageVO);
				docVo.setListParams(dataMap);
				logger.debug("end init dataMap ");
				
				//获取所有组件(用于解析数据)
				BaseExample baseExample = new BaseExample();
				baseExample.createCriteria().andEqualTo("dynamicPage_id", pageVO.getId())
										   .andLike("code",StoreService.COMPONENT_CODE + "%");
				List<StoreVO> stores = storeServiceImpl.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, "T_ORDER");
				
				/* 表单只有一个数据源配置
				 * 表单对应的表需要有创建人(Creator),创建时间(Createtime)这2个字段，
				 */
				String dateSource = JSONObject.parseObject(stores.get(0).getContent()).getString("dataItemCode").split("\\.")[0];
				Map<String,String> data = dataMap.get(dateSource + "_list").get(0);	//表单数据
				String creator = data.get("Creator");		//该记录创建者
				String createTime = data.get("Createtime");	//该记录创建时间
				Map<String,Object> user = getUserInfo(creator,id);
				user.put("createTime", createTime);
				mav.addObject("user", user);
				
				mav.addAllObjects(getFormData(data,id,stores,engine));
				mav.addObject("readers", getLogRead(id));
				mav.addObject("comments", getLogComment(id));
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return mav;
	}
	
	/*
	 * 创建者的用户信息
	 * creator	创建者
	 * id		表单数据表ID
	 * dd_log_read_info 这张表保存表单的读取信息;p_fm_logs	这张表保存表单的评论信息
	 */
	private Map<String,Object> getUserInfo(String creator,String id){
		Map<String,Object> ret = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(creator)){
			String currentUser = DocumentUtils.getIntance().getUser().getUserIdCardNumber();	//当前登录用户
			String sql = "update dd_log_read_info set isRead='Y',readTime=CURRENT_TIMESTAMP where fkId=? and userId=?";
			jdbcTemplate.update(sql,id,currentUser);	//设置当前用户已读
			if(creator.equals(currentUser)){//自己查看自己的记录事,不需要是否读,是否评论数据
				ret.put("notComment", true);
				ret.put("notRead", true);
			}
			else{
				sql = "select count(*) from p_fm_logs where RECORD_ID=? and CREATOR=?";
				int count = jdbcTemplate.queryForObject(sql, Integer.class,id,currentUser);	//当前用户是否评论
				if(count==0){
					ret.put("notComment", true);
					ret.put("notRead", false);
				}
				else{
					ret.put("notComment", false);
					ret.put("notRead", true);
				}
			}
			
			sql = "select name,USER_HEAD_IMG from p_un_user_base_info where USER_ID_CARD_NUMBER=? ";
			Map<String,Object> map = jdbcTemplate.queryForMap(sql,creator);	//创建者用户信息
			String name = (String) map.get("name");			//姓名
			String img = (String) map.get("USER_HEAD_IMG");	//头像	
			DdUtil.dealHeadImg(ret,name,img);
			ret.put("name",name);
		}		
		return ret;
	}
	
	//处理表单数据
	@SuppressWarnings("unchecked")
	private Map<String,Object> getFormData(Map<String,String> data,String id,List<StoreVO> stores,ScriptEngine engine){
		Map<String,Object> retMap = new HashMap<String,Object>();
		List<Map<String,Object>> txtList = new ArrayList<Map<String,Object>>();	//获取Input,Textarea,Select,Datepicker的值
		List<Map<String,Object>> otherList = new ArrayList<Map<String,Object>>();//获取Img,File,Location的值
		boolean isCH = ControllerHelper.getLang() == Locale.SIMPLIFIED_CHINESE;
		for(StoreVO store : stores){
			JSONObject obj = JSONObject.parseObject(store.getContent());
			Map<String,Object> m = new HashMap<String,Object>();
			String dataItemCode = obj.getString("dataItemCode");
			String value = "";
			if(dataItemCode != null){
				value = data.get(dataItemCode.split("\\.")[1]);
			}
			String componentType = obj.getString("componentType");
			m.put("componentType", componentType);
			if(StringUtils.isNotBlank(value) || "1103".equals(componentType)){
				switch(componentType){
					case "1001":
					case "1002":
					case "1005":
					case "1006":
						if (isCH) {
							m.put("title", obj.getString("title"));
						} else {
							m.put("title", obj.getString("enTitle"));
						}
						m.put("value", value);
						if("1006".equals(componentType)){	//下拉框取值处理
							String optionScript = obj.getString("optionScript");
							if (StringUtils.isNotBlank(optionScript)) {
								String ret = null;
								try {
									ret = (String) engine.eval(optionScript);
								} catch (ScriptException e) {
									e.printStackTrace();
								}
								if (StringUtils.isNotBlank(ret)) {
									m.put("value", DdUtil.getVal(value, ret));
								}
							}
						}
						txtList.add(m);
						break;
					case "1011":
						m.put("type", "file");
						m.put("data", getFile(value));
						otherList.add(m);
						break;
					case "1016":
						m.put("type", "img");
						m.put("data", new ArrayList<String>(Arrays.asList(value.split(","))));
						otherList.add(m);
						break;
					case "1101":
						String funType = obj.getString("funType");
						if("0".equals(funType)){
							m.put("type", "location");
							m.put("data", value);
							otherList.add(m);
						}
					case "1103":
						String dataSource = obj.getString("dataSource");
						JSONArray details = obj.getJSONArray("details");
						String title = obj.getString("title");
						try {
							List<Map<String,Object>> arr = (List<Map<String,Object>>) engine.eval(dataSource);
							retMap.put("detailsList", getDetails(arr, details));
							retMap.put("detailsTitle", title);
						} catch (ScriptException e) {
							e.printStackTrace();
						}
				}
			}			
		}
		retMap.put("txtList", txtList);
		retMap.put("otherList", otherList);
		return retMap;
	}
	
	//处理文件
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map> getFile(String value){
		List<Map> list = JSONObject.parseArray(value, Map.class);
		for(Map file : list){
			int fileSize = (int) file.get("fileSize");
			file.put("fileSize", String.format("%.2f", fileSize/1024.0));
			String fileType = (String) file.get("fileType");
			file.put("css", DdUtil.getCss(fileType));
			file.put("json", JSONObject.toJSONString(file));
		}	
		return list;
	}

	//获取已读信息
	private Map<String,Object> getLogRead(String id){
		Map<String,Object> ret = new HashMap<String,Object>();
		String sql = "select t2.name,t2.USER_HEAD_IMG as img from dd_log_read_info t1 "
				+ "join p_un_user_base_info t2 on t1.userId=t2.USER_ID_CARD_NUMBER where fkId=? and t1.isRead='Y'";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,id);
		int count = list.size();
		for(Map<String,Object> map : list){
			String name = (String) map.get("name");
			String img = (String) map.get("img");
			DdUtil.dealHeadImg(map,name,img);
		}
		ret.put("data", list);
		ret.put("count", count);
		return ret;
	}

	//获取评论信息
	private Map<String,Object> getLogComment(String id){
		Map<String,Object> ret = new HashMap<String,Object>();
		String sql = "select a.content,date_format(a.CREATE_TIME,'%Y-%m-%d %H:%i') as date,b.name,b.user_head_img as userImg "
				+ "from p_fm_logs a join p_un_user_base_info b on a.creator=b.USER_ID_CARD_NUMBER "
				+ "where a.record_id=? order by a.seq ,a.send_time";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,id);
		int count = list.size();
		ret.put("count", count);
		for(Map<String,Object> map : list){
			String name = (String) map.get("name");
			String img = (String) map.get("userImg");
			DdUtil.dealHeadImg(map,name,img);
		}
		ret.put("data", list);
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> getDetails(List<Map<String,Object>> arr,JSONArray details){
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		for(int i=0;i<arr.size();i++){
			Map<String,Object> item = new HashMap<String,Object>();
			List<Map<String,Object>> txtList = new ArrayList<Map<String,Object>>();	
			List<Map<String,Object>> otherList = new ArrayList<Map<String,Object>>();
			for(Object obj : details){
				Map<String,Object> temp = (Map<String, Object>) obj;			
				String field = (String) temp.get("field");
				String title = (String) temp.get("title");
				String type = (String) temp.get("type");
				if("ID".equals(field)){
					continue;
				}
				String value = arr.get(i).get(field) + "";
				switch(type){
					case "1001":
					case "1002":
					case "1005":
						Map<String,Object> text = new HashMap<String,Object>();
						text.put("title", title);
						text.put("value", value);
						txtList.add(text);
						break;
					case "1011":
						Map<String,Object> file = new HashMap<String,Object>();
						file.put("type", "file");
						file.put("data", getFile(value));
						otherList.add(file);
						break;
					case "1016":
						Map<String,Object> img = new HashMap<String,Object>();
						img.put("type", "img");
						img.put("data", new ArrayList<String>(Arrays.asList(value.split(","))));
						otherList.add(img);
						break;
				}			
			}	
			item.put("txtList", txtList);
			item.put("otherList", otherList);
			items.add(item);
		}	
		System.out.println("=================" + items);
		return items;
	}
	
	/**
	 * 提交并发送消息通知
	 * @param dataSource	数据源
	 * @param zhTitle		中文标题
	 * @param enTitle		英文标题
	 * @param dynamicPageId	动态页面ID
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/commitAndSendMsg")
	public String commitAndSendMsg(String dataSource,String zhTitle,String enTitle,String dynamicPageId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		DocumentVO docVO = new DocumentVO();
		DynamicPageVO pageVO = null;
		// 查找模版
		if (StringUtils.isNotBlank(dynamicPageId)) {
			pageVO = formdesignerServiceImpl.findById(dynamicPageId);
		}
		// 表单页面
		if (pageVO.getPageType() == 1002) {// 初始化表单数据
			docVO.setDynamicPageId(dynamicPageId);
			docVO.setDynamicPageName(pageVO.getName());
			Map<String, String> map = new HashMap<String, String>();
			Map<String, String[]> parameterMap = request.getParameterMap();
			for (String key : parameterMap.keySet()) {
				map.put(key, StringUtils.join(parameterMap.get(key), ";"));
			}
			docVO.setRequestParams(map);
			docVO = documentServiceImpl.processParams(docVO);
		} 
		ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVO, pageVO);
		engine.put("request", request);
		DocumentUtils docUtils = DocumentUtils.getIntance();
		Map<String,String> data = processParams(docVO,engine);	//将传入的参数转为Key为组件数据源的Map
		
		//保存数据
		boolean bool = true;
		String id = docUtils.getDataItem(dataSource,"ID");
		docUtils.setDataItem(dataSource,"Createtime",docUtils.getDay());
		docUtils.setDataItem(dataSource,"State","1");
		if(id == null || id == "") {
			id = UUID.randomUUID().toString();
			docUtils.setDataItem(dataSource,"ID",id);		
			if(docUtils.saveData(dataSource)==null && docUtils.saveDocument()==null){
				bool = false;
			}
		} 
		else {
			if(docUtils.updateData(dataSource)==null){
				bool = false;
			} else{
				bool = true;
			}
		}
		if(bool){//发送消息通知
			sendMsg(dataSource,docUtils,id,dynamicPageId,zhTitle,enTitle,data);
		}
		return "1";
	}
	
	//发送钉钉消息通知
	private void sendMsg(String dataSource, DocumentUtils docUtils,String id,String dynamicPageId,
			String zhTitle,String enTitle,Map<String,String> data) {
		String ddUserIds = docUtils.getDataItem(dataSource,"Sendfor");   //选择的人员
		String groupIds = docUtils.getDataItem(dataSource,"Sendgroup");  //选择的部门
		insertPaperReadInfo(ddUserIds,groupIds,id);

		//消息推送
		Map<String,Object> content = new LinkedHashMap<String,Object>();
		boolean isEnglish = ControllerHelper.getLang() == Locale.ENGLISH;
		String agentId = "";
		String sql = "";
        if(isEnglish){
        	sql = "select enSql as contentSql,agentId from dd_apps where dynamicPageId=? or pcDynamicPageId=?";
        } else{
        	sql = "select chSql as contentSql,agentId from dd_apps where dynamicPageId=? or pcDynamicPageId=?";
        }
		try{
        	Map<String,Object> temp = jdbcTemplate.queryForMap(sql, dynamicPageId, dynamicPageId);
        	String contentSql = temp.get("contentSql") + "";
        	agentId = (String)temp.get("agentId");
            if(StringUtils.isNotBlank(contentSql)){
            	contentSql += " where ID=?";
                Map<String,Object> result = jdbcTemplate.queryForMap(contentSql,id);           
                for(String key : result.keySet()){
                	String val = result.get(key) + "";
                	if(StringUtils.isNotBlank(val)){
                		if(isEnglish){
                    		String newKey = key.replaceAll("_", " ");
                    		content.put(newKey, val);
                    	} else{
                    		content.put(key, val);
                    	}
                	} 
                }
            } 
        } catch(Exception e){
        	e.printStackTrace();
        }   
		String title = docUtils.getDataItem(dataSource,"Creatorname");
		if(ControllerHelper.getLang()==Locale.SIMPLIFIED_CHINESE){
			title += "的" + zhTitle;
		}
		else{
			title += "'s " + enTitle;
		}
		String url = ControllerHelper.getBasePath() + "dingding/view.do?dynamicPageId=" + dynamicPageId + "&id=" + id;
		if(StringUtils.isNotBlank(ddUserIds)){
			docUtils.sendMessage(url,"0",content,title,ddUserIds.replaceAll(",","|"),agentId,null,null);
		}
		if(StringUtils.isNotBlank(groupIds)){	
			docUtils.sendMessage(url,"1",content,title,groupIds.replaceAll(",","|"),agentId,null,null);
		}
	}

	private Map<String,String> processParams(DocumentVO vo , ScriptEngine engine) {
		Map<String, String> map = vo.getRequestParams();
		String pageId = (String) map.get("dynamicPageId");
		BaseExample baseExample = new BaseExample();
		baseExample.createCriteria().andEqualTo("dynamicPage_id", pageId)
								   .andLike("code",StoreService.COMPONENT_CODE + "%");
		List<StoreVO> stores = storeServiceImpl.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, "T_ORDER");
		
		Map<String, String> tmpMap = new HashMap<String, String>();
		for(StoreVO store : stores){
			JSONObject obj = JSONObject.parseObject(store.getContent());
			String key = obj.getString("name");
			String code = obj.getString("dataItemCode");	//组件数据源
			if (!map.containsKey(key)) {
				continue;
			}
			String value = map.get(key);
			int type = obj.getIntValue("componentType");
			if(type==1006){
				String optionScript = obj.getString("optionScript");
				if (StringUtils.isNotBlank(optionScript)) {
					String ret = null;
					try {
						ret = (String) engine.eval(optionScript);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					if (StringUtils.isNotBlank(ret)) {
						tmpMap.put(code, DdUtil.getVal(value, ret));
					}
				}
			}
			else{
				tmpMap.put(code, value);
			}
		}
		return tmpMap;
	}
	
	private void insertPaperReadInfo(String userIds, String groupIds, String fkId) {
		Set<String> allUserIds = new HashSet<String>();
		if(StringUtils.isNotBlank(userIds)){
			allUserIds.addAll(Arrays.asList(userIds.split(",")));
		}
		if(StringUtils.isNotBlank(groupIds)){
			for (String groupId : groupIds.split(",")) {
				if (StringUtils.isNotBlank(groupId)) {
					String sql = "select t1.USER_ID_CARD_NUMBER from p_un_user_base_info t1 "
							+ "join p_un_user_group t2 on t1.USER_ID=t2.USER_ID where t2.GROUP_ID=" + groupId;
					allUserIds.addAll(jdbcTemplate.queryForList(sql, String.class));
				}
			}
		}
		
		String sql = "insert into dd_log_read_info(fkId,userId)values(?,?)";
		for (String userId : allUserIds) {
			if (StringUtils.isNotBlank(userId)) {
				jdbcTemplate.update(sql, fkId, userId);
			}
		}
	}
}
