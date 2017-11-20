package cn.org.awcp.formdesigner.controller;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.metadesigner.application.MetaModelItemService;
import cn.org.awcp.metadesigner.application.MetaModelService;
import cn.org.awcp.metadesigner.vo.MetaModelItemsVO;
import cn.org.awcp.metadesigner.vo.MetaModelVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("fd/datadefine")
public class DataDefineController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private MetaModelService metaModelServiceImpl;
	@Autowired
	private FormdesignerService formdesignerServiceImpl;
	@Autowired
	private MetaModelItemService metaModelItemsServiceImpl;

	@RequestMapping("/edit")
	public ModelAndView edit() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("formdesigner/page/data/domainModel-edit");
		// 初始化模型列表
		// List<MetaModelVO> models =
		// metaModelServiceImpl.selectPagedByExample(baseExample, currentPage,
		// pageSize, sortString)
		return mv;
	}

	@ResponseBody
	@RequestMapping("/freshModel")
	public String freshModel(String _selects, String pageId) {
		try {
			DynamicPageVO page = formdesignerServiceImpl.findById(pageId);
			if (page != null) {
				String dataJson = page.getDataJson();
				if (StringUtils.isNotBlank(dataJson)) {
					JSONArray arr = JSON.parseArray(StringEscapeUtils.unescapeHtml4(dataJson));
					if (arr != null && arr.size() > 0) {
						for (int i = 0; i < arr.size(); i++) {
							JSONObject model = arr.getJSONObject(i);
							String id = model.getString("id");
							if (_selects.contains(id)) {
								MetaModelVO vo = this.metaModelServiceImpl
										.queryByModelCode(model.getString("modelCode"));
								List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryResult("queryResult",
										vo.getId());
								StringBuilder sb = new StringBuilder();
								if (ls != null && ls.size() > 0) {
									for (MetaModelItemsVO item : ls) {
										sb.append(item.getItemCode()).append(",");
									}
								}
								model.put("modelItemCodes", sb.substring(0, sb.length() - 1));
								arr.set(i, model);
							}
						}
						page.setDataJson(StringEscapeUtils.escapeHtml4(arr.toJSONString()));
						formdesignerServiceImpl.updateModelInfo(page);
					}

				}
			}
			return StringEscapeUtils.unescapeHtml4(page.getDataJson());
		} catch (Exception e) {
			logger.info("ERROR", e);
			return "0";
		}
	}

}
