package org.szcloud.framework.formdesigner.controller;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.formdesigner.application.service.FormdesignerService;
import org.szcloud.framework.formdesigner.application.vo.DynamicPageVO;
import org.szcloud.framework.metadesigner.application.MetaModelItemService;
import org.szcloud.framework.metadesigner.application.MetaModelService;
import org.szcloud.framework.metadesigner.vo.MetaModelItemsVO;
import org.szcloud.framework.metadesigner.vo.MetaModelVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("fd/datadefine")
public class DataDefineController {
	
	@Autowired
	private MetaModelService metaModelServiceImpl;
	@Autowired
	private FormdesignerService formdesignerServiceImpl;
	@Autowired
	private MetaModelItemService metaModelItemsServiceImpl;
	@RequestMapping("/edit")
	public ModelAndView edit(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("formdesigner/page/data/domainModel-edit");
		//初始化模型列表
		//List<MetaModelVO> models = metaModelServiceImpl.selectPagedByExample(baseExample, currentPage, pageSize, sortString)
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/freshModel")
	public String  freshModel(String _selects,String pageId){
		try{
			DynamicPageVO page= formdesignerServiceImpl.findById(Long.parseLong(pageId));
			if(page!=null){
				String dataJson = page.getDataJson();
				if(StringUtils.isNotBlank(dataJson)){
				JSONArray arr=JSON.parseArray(StringEscapeUtils.unescapeHtml4(dataJson));
				if(arr!=null&&arr.size()>0){
					for(int i=0;i<arr.size();i++){
						JSONObject model= arr.getJSONObject(i);
						String id = model.getString("id");
						if(_selects.contains(id)){
							MetaModelVO vo=this.metaModelServiceImpl.queryByModelCode(model.getString("modelCode"));
							List<MetaModelItemsVO> ls=this.metaModelItemsServiceImpl.queryResult("queryResult", vo.getId());
							StringBuilder sb = new StringBuilder();
							if(ls!=null&&ls.size()>0){
								for(MetaModelItemsVO item:ls){
									sb.append(item.getItemCode()).append(",");
								}
							}
							model.put("modelItemCodes",sb.substring(0,sb.length()-1));
							arr.set(i, model);
						}
					}
					page.setDataJson(StringEscapeUtils.escapeHtml4(arr.toJSONString()));
					formdesignerServiceImpl.updateModelInfo(page);
				}
				
				}
			}
			return StringEscapeUtils.unescapeHtml4(page.getDataJson());
		}catch(Exception e){
			e.printStackTrace();
			return"0";
		}
	}
	
	
}