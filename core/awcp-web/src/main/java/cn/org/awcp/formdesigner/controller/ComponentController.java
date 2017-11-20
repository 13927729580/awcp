package cn.org.awcp.formdesigner.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.utils.BeanUtils;
import cn.org.awcp.core.utils.Tools;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.formdesigner.core.constants.FormDesignGlobal;
import cn.org.awcp.formdesigner.core.constants.FormDesignerGlobal;
import cn.org.awcp.unit.vo.PunSystemVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/component")
public class ComponentController extends BaseController {

	@Autowired
	@Qualifier("storeServiceImpl")
	private StoreService storeService;

	/**
	 * 校验名称是否唯一
	 * 
	 * @param dynamicPageId
	 * @param componentName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/validateComponentNameInPage")
	public boolean checkComponentNameInPage(String dynamicPageId, String componentName, String componentId) {
		BaseExample baseExample = new BaseExample();
		baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId).andEqualTo("NAME", componentName);
		List<StoreVO> list = storeService.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, null);
		// 更新操作（componentId不为空）时，如果输入的名字在数据库中已经存在，而且查询出来的结果与提交的componentId匹配，则说明该name可以用，否则，该name不允许使用
		if (list == null || list.isEmpty()) {
			return true;
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getId().equalsIgnoreCase(componentId)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 重置包含页面
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "refreshContainerComponent")
	public String refreshContainerComponent(@RequestParam(value = "_selects") String selects, String alias) {
		String[] ids = selects.split(",");
		boolean flag = StringUtils.isNotBlank(alias);
		if (ids != null && ids.length > 0) {
			for (int j = 0; j < ids.length; j++) {
				StoreVO s = storeService.findById(ids[j]);
				JSONObject obj = JSON.parseObject(s.getContent());
				String pageId = obj.getString("relatePageId");
				List<JSONObject> components = storeService.findComponentByDyanamicPageId(pageId);
				JSONArray arr = new JSONArray();
				for (JSONObject component : components) {
					if (FormDesignerGlobal.isValuedNoneContain(component)) {
						JSONObject temp = new JSONObject();
						temp.put("componentType", component.getString("componentType"));
						if (flag) {
							StringBuilder sb = new StringBuilder();
							sb.append(alias).append(".").append(component.getString("dataItemCode").split("\\.")[1]);
							temp.put("dataItemCode", sb.toString());
						} else {
							temp.put("dataItemCode", component.getString("dataItemCode"));
						}
						temp.put("name", component.getString("name"));
						temp.put("pageId", component.getString("pageId"));
						arr.add(temp);
					}
				}
				obj.put("configures", arr);
				s.setContent(obj.toJSONString());
				storeService.save(s);
			}
		}
		return "1";
	}

	@RequestMapping("/toedit")
	public ModelAndView toEditComponent(String dynamicPageId, String componentType, String componentId) {
		ModelAndView mv = new ModelAndView();
		// componentId is blank, then both of dynamicPageId and componentType
		// will be not blank, vice versa.
		if (StringUtils.isBlank(componentId) && (dynamicPageId == null || StringUtils.isBlank(componentType))) {
			mv.setViewName("");// error
		} else {
			if (!StringUtils.isBlank(componentId)) {
				mv.addObject("componentId", componentId);
				StoreVO vo = storeService.findById(componentId);
				if (vo != null && StringUtils.isNotBlank(vo.getContent())) {
					JSONObject o = JSONObject.parseObject(vo.getContent());
					componentType = o.getString("componentType");
					mv.addObject("componentType", componentType);
				}
			} else {
				mv.addObject("dynamicPageId", dynamicPageId);
				mv.addObject("componentType", componentType);
			}
			if (StringUtils.isBlank(componentType)) {
				// error
			} else {
				String url = FormDesignGlobal.COMPOENT_TYPE_URL.get(componentType);
				if (StringUtils.isBlank(url)) {
					// error
				} else {
					mv.setViewName(url.replaceFirst("\\.jsp", ""));
				}
			}

		}
		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			BaseExample baseExample = new BaseExample();
			baseExample.createCriteria().andEqualTo("SYSTEM_ID", system.getSysId()).andLike("code",
					"%" + StoreService.STYLE_CODE + "%");

			List<StoreVO> styles = storeService.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, null);
			mv.addObject("styles", styles);
			// vo.setSystemId(system.getSysId());
		}

		mv.addObject("_ComponentTypeName", FormDesignGlobal.COMPOENT_TYPE_NAME);

		return mv;
	}

	@RequestMapping("/perComponentsEdit")
	public ModelAndView perComponentsedit(String dynamicPageId, @RequestParam(value = "_selects") String selects) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(selects) && dynamicPageId == null) {
			mv.setViewName("");// error
		} else {
			String[] ids = selects.split(",");
			JSONObject o = JSONObject.parseObject(storeService.findById(ids[0]).getContent());
			String type = o.getString("componentType");
			for (int i = 1; i < ids.length; i++) {
				StoreVO s = storeService.findById(ids[i]);
				o = JSONObject.parseObject(s.getContent());
				if (!o.getString("componentType").equals(type)) {
					mv.addObject("result", "您需要批量修改的组件类型不一致");
					break;
				}
			}

			mv.setViewName("formdesigner/page/component/common/componentsEdit");

			mv.addObject("dynamicPageId", dynamicPageId);
			mv.addObject("componentType", type);
			mv.addObject("componentIds", selects);

		}
		return mv;
	}

	@RequestMapping("/toedits")
	public ModelAndView toEditComponents(String dynamicPageId, String componentType, String componentId,
			@RequestParam(value = "_selects", required = false) String selects) {

		ModelAndView mv = new ModelAndView();
		// componentId is blank, then both of dynamicPageId and componentType
		// will be not blank, vice versa.

		if (dynamicPageId != null && !StringUtils.isBlank(selects)) {
			String[] ids = selects.split(",");
			JSONObject o = JSONObject.parseObject(storeService.findById(ids[0]).getContent());
			componentType = o.getString("componentType");
			boolean isSame = true;
			for (int i = 1; i < ids.length; i++) {
				StoreVO s = storeService.findById(ids[i]);
				o = JSONObject.parseObject(s.getContent());
				if (!o.getString("componentType").equals(componentType)) {
					mv.setViewName("");// error
					isSame = false;
					break;
				}
			}
			if (isSame) {
				mv.addObject("dynamicPageId", dynamicPageId);
				mv.addObject("componentType", componentType);
			}

		} else if (StringUtils.isBlank(componentId) && (dynamicPageId == null || StringUtils.isBlank(componentType))) {
			mv.setViewName("");// error
		} else {
			if (!StringUtils.isBlank(componentId)) {
				mv.addObject("componentId", componentId);
				StoreVO vo = storeService.findById(componentId);
				if (vo != null && StringUtils.isNotBlank(vo.getContent())) {
					JSONObject o = JSONObject.parseObject(vo.getContent());
					componentType = o.getString("componentType");
					mv.addObject("componentType", componentType);
				}
			} else {
				mv.addObject("dynamicPageId", dynamicPageId);
				mv.addObject("componentType", componentType);
			}
		}
		if (StringUtils.isBlank(componentType)) {
			// error
		} else {
			String url = FormDesignGlobal.COMPOENT_TYPE_URL.get(componentType);
			if (StringUtils.isBlank(url)) {
				// error
			} else {
				mv.setViewName(url.replaceFirst("\\.jsp", ""));
			}
		}

		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			BaseExample baseExample = new BaseExample();
			baseExample.createCriteria().andEqualTo("SYSTEM_ID", system.getSysId()).andLike("code",
					"%" + StoreService.STYLE_CODE + "%");

			List<StoreVO> styles = storeService.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, null);
			mv.addObject("styles", styles);
			// vo.setSystemId(system.getSysId());
		}

		mv.addObject("_ComponentTypeName", FormDesignGlobal.COMPOENT_TYPE_NAME);

		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/save")
	public StoreVO save(StoreVO vo, @RequestParam(value = "componentIds", required = false) String componentIds) {
		vo.setContent(StringEscapeUtils.unescapeHtml4(vo.getContent()));
		if (componentIds == null || "".equals(componentIds)) {
			if (validate(vo)) {
				Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
				if (obj instanceof PunSystemVO) {
					PunSystemVO system = (PunSystemVO) obj;
					vo.setSystemId(system.getSysId());
				}
				vo.setCode(StoreService.COMPONENT_CODE + System.currentTimeMillis());
				JSONObject json = JSON.parseObject(vo.getContent()); // 将json中的order转为int类型（freeMarker
																		// sort_by('order')
																		// 必须）
				json.put("order", json.getInteger("order"));
				vo.setContent(json.toJSONString());
				String id = storeService.save(vo);// 保存Store并返回storeId
				vo.setId(id);
			}
		} else {
			String[] componentIds1 = componentIds.split(",");
			List<String> notAllowedKeys = new ArrayList<String>(
					Arrays.asList("name", "order", "layoutId", "layoutName"));
			HashMap temp = new HashMap();
			for (String componentId : componentIds1) {
				StoreVO voi = storeService.findById(componentId);
				JSONObject oi = JSONObject.parseObject(voi.getContent());
				JSONObject o = JSONObject.parseObject(vo.getContent());
				Iterator<String> it = o.keySet().iterator();
				boolean isChanged = false;
				while (it.hasNext()) {
					String key = it.next();
					if (!notAllowedKeys.contains(key)) {
						String valueString = o.getString(key);
						if ("\"\"".equals(valueString)) {
							oi.put(key, "");
							temp.put(key, "");
							isChanged = true;
						} else if (valueString != "" && !"".equals(valueString) && !valueString.equals(oi.get(key))) {
							oi.put(key, valueString);
							temp.put(key, valueString);
							isChanged = true;
						}
					}
				}
				if (isChanged) {
					voi.setContent(oi.toJSONString());
					storeService.save(voi);// 保存Store并返回storeId
				}
			}
		}
		return vo;
	}

	@ResponseBody
	@RequestMapping(value = "/getComponentById")
	public StoreVO getComponentById(@RequestParam(required = true) String storeId) {
		StoreVO store = storeService.findById(storeId);
		return store;
	}

	@ResponseBody
	@RequestMapping(value = "/getSystemId")
	public StoreVO getSystemId() {
		StoreVO store = new StoreVO();
		Object obj2 = Tools.getObjectFromSession(SessionContants.CURRENT_SYSTEM);
		if (obj2 instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj2;
			store.setSystemId(system.getSysId());
		}
		return store;
	}

	@ResponseBody
	@RequestMapping(value = "/refreshComponentOrder")
	public String refreshLayoutOrder(@RequestParam(required = true) String pageId) {
		boolean flag = storeService.freshComponentOrder(pageId);
		if (flag) {
			return "1";
		} else {
			return "0";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getComponentListByPageId")
	public List<StoreVO> getComponentListByPageId(@RequestParam(required = true) String dynamicPageId,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = " T_ORDER ASC") String sortString) {
		BaseExample baseExample = new BaseExample();
		baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId).andLike("CODE",
				StoreService.COMPONENT_CODE + "%");
		return storeService.selectPagedByExample(baseExample, currentPage, pageSize, sortString);
	}

	/**
	 * 佈局樹聯動函數，聯動查詢組件 wuhg 2015.3.30
	 * 
	 * @param dynamicPageId
	 * @param layoutId
	 * @param currentPage
	 * @param pageSize
	 * @param sortString
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getComponentListByLayoutId")
	public List<StoreVO> getComponentListByLayoutId(@RequestParam(required = true) String dynamicPageId,
			@RequestParam(required = true) String layoutId,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = " T_ORDER ASC") String sortString) {
		BaseExample baseExample = new BaseExample();
		baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId).andLike("CODE",
				StoreService.COMPONENT_CODE + "%");
		List<StoreVO> storeVos = storeService.selectPagedByExample(baseExample, currentPage, pageSize, sortString);

		List<StoreVO> result = new ArrayList<StoreVO>();
		if (!layoutId.equals("NULL")) {
			// 查找子節點
			BaseExample layoutExample = new BaseExample();
			layoutExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId)
					.andLike("CODE", storeService.LAYOUT_CODE + "%").andEqualTo("DESCRIPTION", layoutId);
			List<StoreVO> layouts = storeService.selectPagedByExample(layoutExample, currentPage, pageSize, sortString);
			List<String> layoutIds = new ArrayList<String>();
			for (StoreVO storeVo : layouts) {
				layoutIds.add(storeVo.getId());
			}
			layoutIds.add(layoutId);
			// 判斷組件是否屬於要在該佈局的及其子佈局下
			for (StoreVO storeVo : storeVos) {
				for (String lid : layoutIds) {
					JSONObject content = JSONObject.parseObject(storeVo.getContent());
					String tem_id = content.getString("layoutId");
					if (tem_id != null && lid.equals(tem_id)) {
						result.add(storeVo);
					}
				}
			}
		} else {
			result = storeVos;
		}

		return result;
	}

	private boolean validate(StoreVO vo) {
		return true;
	}

	/**
	 * 异步ajax删除选中的页面动作数据,更改数据状态
	 * 
	 * @param selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteByAjax")
	public String deleteComponentByAjax(@RequestParam(value = "_selects") String selects) {
		String[] ids = selects.split(",");
		if (storeService.delete(ids)) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 批量拷贝组件
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "copyComponentByAjax")
	public String copyComponentByAjax(@RequestParam(value = "_selects") String selects) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			StoreVO copy = BeanUtils.getNewInstance(s, StoreVO.class);
			copy.setId(""); // 置Id为空，表示新增
			copy.setName(copy.getName() + "-copy");
			JSONObject copyJson = JSON.parseObject(copy.getContent());
			if (copyJson.containsKey("pageId")) { // 置pageId为空，表示新增
				copyJson.put("pageId", "");
			}
			copyJson.put("name", copy.getName());
			copy.setContent(copyJson.toJSONString());
			storeService.save(copy); // 保存
		}
		return "1";
	}

	/**
	 * 批量修改 数据源
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "quickModifyItemCode")
	public String quickModifyItemCode(@RequestParam(value = "_selects") String selects, Long dynamicPageId,
			String dataItemCode) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);

			JSONObject json = JSON.parseObject(s.getContent());
			json.put("dataItemCode", dataItemCode);
			s.setContent(json.toJSONString());
			storeService.save(s); // 保存
		}
		return "1";
	}

	/**
	 * 批量修改 布局
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyLayout")
	public String batchModifyLayout(@RequestParam(value = "_selects") String selects, Long dynamicPageId,
			String layoutId, String layoutName) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			// 置Id为空，表示新增
			JSONObject json = JSON.parseObject(s.getContent());
			if (json.containsKey("layoutId")) { // 修改layoutId
				json.put("layoutId", layoutId);
			}
			if (json.containsKey("layoutName")) { // 修改layoutName
				json.put("layoutName", layoutName);
			}
			s.setContent(json.toJSONString());
			storeService.save(s); // 保存
		}
		return "1";
	}

	/**
	 * 批量修改 布局
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyStyle")
	public String batchModifyStyle(@RequestParam(value = "_selects") String selects, Long dynamicPageId, String style) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			// 置Id为空，表示新增
			JSONObject json = JSON.parseObject(s.getContent());
			json.put("style", style);
			s.setContent(json.toJSONString());
			storeService.save(s); // 保存
		}
		return "1";
	}

	/**
	 * 批量修改 布局
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyPrintHeight")
	public String batchModifyPrintHeight(@RequestParam(value = "_selects") String selects, Long dynamicPageId,
			String heightType, String height) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			JSONObject json = JSON.parseObject(s.getContent());
			JSONObject belongLayout = JSON.parseObject(storeService.findById(json.getString("layoutId")).getContent());
			while (belongLayout != null && belongLayout.getInteger("layoutType") != 2) { // 找到对应组件放的所在行布局
				belongLayout = JSON.parseObject(storeService.findById(belongLayout.getString("parentId")).getContent());
			}
			belongLayout.put("heightType", Integer.parseInt(heightType));
			belongLayout.put("height", Float.parseFloat(height));
			logger.debug("toBeModify  row  :  " + belongLayout.getString("pageId"));
			StoreVO rowStoreVO = storeService.findById(belongLayout.getString("pageId"));
			rowStoreVO.setContent(belongLayout.toJSONString());

			storeService.save(rowStoreVO); // 保存
		}
		return "1";
	}

	/**
	 * 批量修改 布局
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyValidator")
	public String batchModifyValidator(@RequestParam(value = "_selects") String selects, Long dynamicPageId,
			String validator) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			// 置Id为空，表示新增
			JSONObject json = JSON.parseObject(s.getContent());
			json.put("validatJson", validator);
			s.setContent(json.toJSONString());
			storeService.save(s); // 保存
		}
		return "1";
	}

	/**
	 * 批量修改 打印样式
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyPrintStyle")
	public String batchModifyPrintStyle(@RequestParam(value = "_selects") String selects, Long dynamicPageId,
			String fontfamily, String fontsize, String fontcolor, String backgroundcolor, String textstyle,
			String textalign, String textverticalalign, String textindent, String lineheight) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			// 置Id为空，表示新增
			JSONObject json = JSON.parseObject(s.getContent());
			if (StringUtils.isNotBlank(fontfamily)) {
				json.put("fontfamily", fontfamily);
			}
			if (StringUtils.isNotBlank(fontsize)) {
				json.put("fontsize", fontsize);
			}
			if (StringUtils.isNotBlank(fontcolor)) {
				json.put("fontcolor", fontcolor);
			}
			if (StringUtils.isNotBlank(backgroundcolor)) {
				json.put("backgroundcolor", backgroundcolor);
			}
			if (StringUtils.isNotBlank(textstyle)) {
				json.put("textstyle", textstyle);
			}
			if (StringUtils.isNotBlank(textalign)) {
				json.put("textalign", textalign);
			}
			if (StringUtils.isNotBlank(textverticalalign)) {
				json.put("textverticalalign", textverticalalign);
			}
			if (StringUtils.isNotBlank(textindent)) {
				json.put("textindent", textindent);
			}
			if (StringUtils.isNotBlank(lineheight)) {
				json.put("lineheight", lineheight);
			}

			// json.put("validatJson", validator);
			s.setContent(json.toJSONString());
			storeService.save(s); // 保存
		}
		return "1";
	}

	/**
	 * 批量修改 布局
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyComponentType")
	public String batchModifyComponentType(@RequestParam(value = "_selects") String selects, Long dynamicPageId,
			String componentType) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			// 置Id为空，表示新增
			JSONObject json = JSON.parseObject(s.getContent());
			json.put("componentType", componentType);
			s.setContent(json.toJSONString());
			storeService.save(s); // 保存
		}
		return "1";
	}

	/**
	 * 批量修改 数据源名称
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyDataAlias")
	public String batchModifyDataAlias(@RequestParam(value = "_selects") String selects, Long dynamicPageId,
			String alias) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			// 置Id为空，表示新增
			JSONObject json = JSON.parseObject(s.getContent());
			String dataItemCode = json.getString("dataItemCode");
			dataItemCode = dataItemCode.substring(dataItemCode.indexOf("."));
			dataItemCode = alias + dataItemCode;
			json.put("dataItemCode", dataItemCode);
			s.setContent(json.toJSONString());
			storeService.save(s); // 保存
		}
		return "1";
	}

	/**
	 * 批量修改 是否为空
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifiAllowNull")
	public String batchModifiAllowNull(@RequestParam(value = "_selects") String selects, String allowNull) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			// 置Id为空，表示新增
			JSONObject json = JSON.parseObject(s.getContent());

			json.put("validateAllowNull", allowNull); // 1允许为空，0不允许为空

			s.setContent(json.toJSONString());
			storeService.save(s); // 保存
		}
		return "1";
	}

	/**
	 * 根据id查询出批量修改的所有组件
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "toBatchModifiAll")
	public List<StoreVO> toBatchModifiAll(@RequestParam(value = "_selects") String selects) {
		String[] ids = selects.split(",");
		List<StoreVO> list = new ArrayList<StoreVO>();
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			list.add(s);
		}

		return list;
	}

	/**
	 * 快捷新增，对于输入框等，对应也生成输入框前面的文本
	 * 
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/quickSave")
	public StoreVO quickSave(StoreVO vo) {

		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			vo.setSystemId(system.getSysId());
		}
		vo.setContent(StringEscapeUtils.unescapeHtml4(vo.getContent())); // 保存对应的组件
		vo.setCode(StoreService.COMPONENT_CODE + System.currentTimeMillis());
		JSONObject json = JSON.parseObject(vo.getContent()); // 将表单传的labelLayoutId、labelLayoutName
																// json串移除
		String labelLayoutId = json.getString("labelLayoutId");
		String labelLayoutName = json.getString("labelLayoutName");
		json.remove("labelLayoutId");
		json.remove("labelLayoutName");
		json.put("order", json.getInteger("order"));
		vo.setContent(json.toJSONString());

		StoreVO labelVO = BeanUtils.getNewInstance(vo, StoreVO.class);
		labelVO.setName("label_" + labelVO.getName());
		labelVO.setCode(StoreService.COMPONENT_CODE + System.currentTimeMillis());
		JSONObject labelJson = JSON.parseObject(vo.getContent());
		if (labelJson.containsKey("componentType")) { // 组件类型设置为文本标签，
			labelJson.put("componentType", "1009");
		}
		if (labelJson.containsKey("name")) { // 组件类型设置为文本标签，
			labelJson.put("name", "label_" + labelJson.getString("name"));
		}
		if (labelJson.containsKey("dataItemCode")) {
			labelJson.put("dataItemCode", "");
		}
		if (labelJson.containsKey("layoutId")) {
			labelJson.put("dataItemCode", "");
		}
		if (labelJson.containsKey("dataItemCode")) {
			labelJson.put("layoutId", labelLayoutId);
		}
		if (labelJson.containsKey("layoutName")) {
			labelJson.put("layoutName", labelLayoutName);
		}
		labelVO.setContent(labelJson.toJSONString());

		storeService.save(labelVO); // 保存label文本标签

		String id = storeService.save(vo);// 保存组件Store并返回storeId
		vo.setId(id);

		return vo;
	}

	/**
	 * 快捷新增，根据数据源配置添加组件
	 * 
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/quickComByData")
	public String quickComByData(@RequestParam(value = "_dataSource") String dataSources,
			@RequestParam(value = "_labelTitle") String labelTitles,
			@RequestParam(value = "_layoutName") String layoutNames,
			@RequestParam(value = "_componentType") String componentTypes,
			@RequestParam(value = "_layoutId") String layoutIds,
			@RequestParam(value = "_labelLayoutId") String labelLayoutIds,
			@RequestParam(value = "_labelLayoutName") String labelLayoutNames, 
			@RequestParam(value = "dynamicPageId") String dynamicPageId) {

		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		Long systemId = null;
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			systemId = system.getSysId();
		}
		String[] dataSource = dataSources.split(",");
		String[] labelTitle = labelTitles.split(",");
		String[] layoutName = layoutNames.split(",");
		String[] componentType = componentTypes.split(",");
		String[] layoutId = layoutIds.split(",");
		String[] labelLayoutId = labelLayoutIds.split(",");
		String[] labelLayoutName = labelLayoutNames.split(",");
		for (int i = 0; i < dataSource.length; i++) {
			StoreVO s = new StoreVO();
			s.setSystemId(systemId);
			s.setCode(StoreService.COMPONENT_CODE + System.currentTimeMillis());
			s.setDynamicPageId(dynamicPageId);
			s.setName("quickData" + System.currentTimeMillis());
			s.setOrder(2); // 默认设为2

			JSONObject json = new JSONObject();
			json.put("dataItemCode", dataSource[i]); // 修改数据源
			json.put("componentType", componentType[i]);
			json.put("dynamicPageId", dynamicPageId);
			json.put("layoutId", layoutId[i]);
			json.put("layoutName", layoutName[i]);
			json.put("name", s.getName());
			json.put("pageId", "");
			json.put("order", s.getOrder());
			s.setContent(json.toJSONString());

			storeService.save(s); // 保存组件

			StoreVO labelStore = new StoreVO(); // 对应组件的文本组件
			labelStore.setSystemId(systemId);

			labelStore.setCode(StoreService.COMPONENT_CODE + System.currentTimeMillis());
			labelStore.setDynamicPageId(dynamicPageId);
			labelStore.setName("label_" + System.currentTimeMillis());
			labelStore.setOrder(1); // 默认设为1
			JSONObject labelJson = new JSONObject();

			labelJson.put("componentType", "1009");
			labelJson.put("dynamicPageId", dynamicPageId);
			labelJson.put("layoutId", labelLayoutId[i]); // 文本布局
			labelJson.put("layoutName", labelLayoutName[i]); // 文本布局名称
			labelJson.put("name", labelStore.getName());
			labelJson.put("title", labelTitle[i]);
			labelJson.put("pageId", "");
			labelJson.put("order", labelStore.getOrder());
			labelStore.setContent(labelJson.toJSONString());

			storeService.save(labelStore); // 保存相应的文本组件

		}
		return "1";

	}

	/**
	 * 快捷更改组件
	 * 
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/quickModifyAll")
	public String quickModifyAll(@RequestParam(value = "_selects") String _selects,
			@RequestParam(value = "_dataItemCode") String _dataItemCode,
			@RequestParam(value = "_layoutName") String _layoutName,
			@RequestParam(value = "_layoutId") String _layoutId,
			@RequestParam(value = "_isAllowNull") String _isAllowNull, Long dynamicPageId) {

		String[] selects = _selects.split(",");
		String[] dataItemCodes = _dataItemCode.split(",");
		String[] layoutNames = _layoutName.split(",");
		String[] layoutIds = _layoutId.split(",");
		String[] isAllowNulls = _isAllowNull.split(",");
		for (int i = 0; i < selects.length; i++) {
			StoreVO s = storeService.findById(selects[i]);
			// 默认设为2

			JSONObject json = JSON.parseObject(s.getContent());
			if (json.containsKey("dataItemCode")) {
				if (!"00000000".equals(dataItemCodes[i])) { // 由于js中的join对于空直接忽略，所有push
															// '00000000'占位符，防止数组越界,对于'00000000'不予处理
					json.put("dataItemCode", dataItemCodes[i]);
				}

			}
			if (json.containsKey("layoutName")) {
				json.put("layoutName", layoutNames[i]);
			}
			if (json.containsKey("layoutId")) {
				json.put("layoutId", layoutIds[i]);
			}
			if (json.containsKey("validateAllowNull")) {
				json.put("validateAllowNull", isAllowNulls[i]);
			}
			s.setContent(json.toJSONString());
			storeService.save(s); // 保存组件

		}
		return "1";
	}

	@ResponseBody
	@RequestMapping(value = "/validateCheckOut")
	public String validateCheckOut(String id) {
		if (id == null) { // 新增页面，无需校验是否签出；
			return "1";
		}

		StoreVO vo = storeService.findById(id);
		if (vo.getIsCheckOut() != null && vo.getIsCheckOut() == 1) { // 已签出状态

			if (!vo.getCheckOutUser().equals(ControllerHelper.getUser().getName())) {
				String ret = "页面已被  " + vo.getCheckOutUser() + " 签出，你暂时无法修改";
				return ret;
			} else {
				return "1";
			}
		} else {
			String ret = "编辑修改前，请先签出";
			return ret;
		}
	}
}
