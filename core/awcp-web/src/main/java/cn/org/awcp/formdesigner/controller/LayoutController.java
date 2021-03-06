package cn.org.awcp.formdesigner.controller;

import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.utils.BeanUtils;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.formdesigner.core.constants.FormDesignGlobal;
import cn.org.awcp.formdesigner.core.domain.design.context.component.Layout;
import cn.org.awcp.unit.vo.PunSystemVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("/layout")
public class LayoutController extends BaseController {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(LayoutController.class);
	@Autowired
	@Qualifier("storeServiceImpl")
	private StoreService storeService;

	@Autowired
	private FormdesignerService formdesignerServiceImpl;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@ResponseBody
	@RequestMapping(value = "/save")
	public StoreVO save(StoreVO vo) {
		vo.setContent(StringEscapeUtils.unescapeHtml4(vo.getContent()));
		if (vo.getId().indexOf(",") < 0) {
			if (validate(vo)) {
				Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
				if (obj instanceof PunSystemVO) {
					PunSystemVO system = (PunSystemVO) obj;
					vo.setSystemId(system.getSysId());
				}
				JSONObject json = JSON.parseObject(vo.getContent()); // 将json中的order转为int类型（freeMarker
																		// sort_by('order')
																		// 必须）
				json.put("order", json.getInteger("order"));
				vo.setContent(json.toJSONString());

				vo.setCode(StoreService.LAYOUT_CODE + System.currentTimeMillis());
				String id = storeService.save(vo);// 保存Store并返回storeId
				vo.setId(id);
			}
		} else {
			String[] ids = vo.getId().split(",");
			List<String> notAllowedKeys = new ArrayList<String>(Arrays.asList("pageId", "name", "order", "layoutType"));
			HashMap temp = new HashMap();
			for (String componentId : ids) {
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
			if (validate(vo)) {
				Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
				if (obj instanceof PunSystemVO) {
					PunSystemVO system = (PunSystemVO) obj;
					vo.setSystemId(system.getSysId());
				}
				vo.setContent(StringEscapeUtils.unescapeHtml4(vo.getContent()));

				JSONObject json = JSON.parseObject(vo.getContent()); // 将json中的order转为int类型（freeMarker
																		// sort_by('order')
																		// 必须）
				json.put("order", json.getInteger("order"));
				vo.setContent(json.toJSONString());

				vo.setCode(StoreService.LAYOUT_CODE + System.currentTimeMillis());
				String id = storeService.save(vo);// 保存Store并返回storeId
				vo.setId(id);
			}
		}
		return vo;
	}

	@ResponseBody
	@RequestMapping("listLayoutInTree")
	public String treeLayouts(String dynamicPageId) {
		BaseExample baseExample = new BaseExample();
		baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId).andLike("CODE",
				StoreService.LAYOUT_CODE + "%");
		List<StoreVO> list = storeService.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, "T_ORDER ASC");
		// by ayesd 拼装为ztree的格式返回给前台 id,pId,name
		JSONArray datas = new JSONArray();
		JSONObject root = new JSONObject();
		root.put("id", null);
		root.put("pId", null);
		root.put("name", "root");
		datas.add(root);
		for (StoreVO vo : list) {
			JSONObject o = new JSONObject();
			if (vo.getDescription() != null && !"".equals(vo.getDescription())) {
				o.put("id", vo.getId());
				o.put("pId", vo.getDescription());
				o.put("name", vo.getName());
				datas.add(o);
			} else {
				o.put("id", vo.getId());
				o.put("pId", root.get("id"));
				o.put("name", vo.getName());
				datas.add(o);
			}

		}
		return datas.toJSONString();
	}

	@ResponseBody
	@RequestMapping("relocat")
	public String relocat(HttpServletRequest request) {
		String moveType = request.getParameter("moveType");
		String targetNodeId = request.getParameter("target");
		String currentNodeId = request.getParameter("current");
		boolean copy = "true".equalsIgnoreCase(request.getParameter("copy")) ? true : false;
		StoreVO currentNode = storeService.findById(currentNodeId);
		if (copy) {
			currentNode.setId("");
		}
		if ("inner".equalsIgnoreCase(moveType)) {
			String lastId = request.getParameter("last");
			StoreVO parent = storeService.findById(targetNodeId);

			StoreVO lastChild = storeService.findById(lastId);

			JSONObject current = JSON.parseObject(currentNode.getContent());
			current.put("parentId", parent.getId());
			current.put("order", lastChild.getOrder() + 1);
			currentNode.setOrder(lastChild.getOrder() + 1);
			currentNode.setDescription(parent.getId());
			currentNode.setContent(current.toJSONString());
			storeService.save(currentNode);
		} else {
			boolean fresh = "true".equalsIgnoreCase(request.getParameter("fresh")) ? true : false;
			String pageId = request.getParameter("pageId");
			if (fresh) {
				// 刷新布局的order
				storeService.freshLayoutOrder(pageId);
				// 刷新组件的order
				storeService.freshComponentOrder(pageId);
			}
			if ("next".equalsIgnoreCase(moveType)) {
				// parentNode
				// currentNode

			} else if ("prev".equalsIgnoreCase(moveType)) {

			}
		}
		return currentNode.getId();
	}

	@ResponseBody
	@RequestMapping("listPageInTree")
	public String treePage(String dynamicPageId) {
		BaseExample layoutExample = new BaseExample();
		// 找layout
		layoutExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId).andLike("CODE",
				StoreService.LAYOUT_CODE + "%");
		List<StoreVO> layout = storeService.selectPagedByExample(layoutExample, 1, Integer.MAX_VALUE, "T_ORDER ASC");
		// 找组件
		BaseExample componentExample = new BaseExample();
		componentExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId).andLike("CODE",
				StoreService.COMPONENT_CODE + "%");
		List<StoreVO> components = storeService.selectPagedByExample(componentExample, 1, Integer.MAX_VALUE,
				"T_ORDER ASC");

		// by ayesd 拼装为ztree的格式返回给前台 id,pId,name
		JSONArray datas = new JSONArray();
		for (StoreVO vo : layout) {
			JSONObject o = new JSONObject();
			o.put("id", vo.getId());
			if (StringUtils.isNotBlank(vo.getDescription())) {
				o.put("pId", vo.getDescription());
			} else {
				o.put("pId", "0");
			}

			JSONObject lay = JSON.parseObject(vo.getContent());
			String type = lay.getString("layoutType");
			o.put("type", type);
			o.put("order", vo.getOrder());
			StringBuilder name = new StringBuilder().append(vo.getName());
			if (StringUtils.isNotBlank(type)) {
				name.append("_").append(FormDesignGlobal.LAYOUT_TYPE_SHOW.get(type.trim())).append("_(占比:")
						.append(lay.getString("proportion")).append(")");
			}
			o.put("name", name.toString());
			datas.add(o);
		}
		for (StoreVO vo : components) {
			JSONObject o = new JSONObject();
			o.put("id", vo.getId());
			JSONObject component = JSON.parseObject(vo.getContent());
			o.put("pId", component.getString("layoutId"));
			o.put("name", vo.getName() + "_组件");
			o.put("type", component.getString("componentType"));
			o.put("order", vo.getOrder());
			datas.add(o);
		}
		return datas.toJSONString();
	}

	@ResponseBody
	@RequestMapping(value = "/refreshLayoutOrder")
	public String refreshLayoutOrder(@RequestParam(required = true) String pageId) {
		boolean flag = storeService.freshLayoutOrder(pageId);
		if (flag) {
			return "1";
		} else {
			return "0";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getLayoutById")
	public StoreVO getComponentById(@RequestParam(required = true) String storeId) {
		return storeService.findById(storeId);
	}

	@ResponseBody
	@RequestMapping(value = "/getLayoutListByPageId")
	public List<StoreVO> getComponentListByPageId(@RequestParam(required = true) String dynamicPageId,
			@RequestParam(required = false, defaultValue = "") String rows,
			@RequestParam(required = false, defaultValue = "") String columns,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10000") int pageSize,
			@RequestParam(required = false, defaultValue = " T_ORDER ASC") String sortString) {

		List<StoreVO> rowsTemp = new ArrayList<StoreVO>();
		List<StoreVO> resultS = new ArrayList<StoreVO>();
		List<StoreVO> rowsStore = null, columnsStore = null;
		BaseExample rowsBaseExample = new BaseExample();
		rowsBaseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId)
				.andLike("CODE", StoreService.LAYOUT_CODE + "%").andIsNull("DESCRIPTION");
		rowsStore = storeService.selectPagedByExample(rowsBaseExample, currentPage, pageSize, sortString);

		BaseExample columnBaseExample = new BaseExample();
		columnBaseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId)
				.andLike("CODE", StoreService.LAYOUT_CODE + "%").andIsNotNull("DESCRIPTION");
		columnsStore = storeService.selectPagedByExample(columnBaseExample, currentPage, pageSize, sortString);

		if ("".equals(rows)) {
			rowsTemp.addAll(rowsStore);
		} else if (!"".equals(rows)) {
			List<Integer> rowsList = parseScopeString(rows);
			for (int row : rowsList) {
				if (row <= rowsStore.size() && row > 0) {
					rowsTemp.add(rowsStore.get(row - 1));
				}
			}
		}
		// 根据要显示的行，查找需要显示的列
		if (rowsTemp.size() > 0) {
			List<Integer> columnsList = parseScopeString(columns);
			for (StoreVO rowStore : rowsTemp) {
				resultS.add(rowStore);
				if (!"".equals(columns)) {
					int i = 0;
					for (StoreVO colstoreVO : columnsStore) {
						if (rowStore.getId().equals(colstoreVO.getDescription())) {
							i++;
							for (int column : columnsList) {
								if (column == i) {
									resultS.add(colstoreVO);
								}
							}
						}
					}
				} else {
					for (StoreVO colstoreVO : columnsStore) {
						if (rowStore.getId().equals(colstoreVO.getDescription())) {
							resultS.add(colstoreVO);
						}
					}
				}
			}
		}
		return resultS;
	}

	// 行列从1开始，支持1,323,2-12,2
	private List<Integer> parseScopeString(String src) {
		List<Integer> list = new ArrayList<Integer>();
		if (StringUtils.isNumeric(src)) {
			try {
				list.add(Integer.parseInt(src));
			} catch (Exception e) {
			}
		} else {
			String[] strings = src.split(",");
			for (String te : strings) {
				if (StringUtils.isNumeric(te)) {
					try {
						list.add(Integer.parseInt(te));
					} catch (Exception e) {
					}
				} else {
					String[] strings2 = te.split("-");
					if (strings2.length == 2) {
						try {
							int i1 = Integer.parseInt(strings2[0]);
							int i2 = Integer.parseInt(strings2[1]);
							for (int i = i1; i <= i2; i++) {
								list.add(i);
							}
						} catch (Exception e) {
						}
					}
				}
			}
		}
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/getLayoutListByPageIdInTree")
	public String getComponentListByPageIdInTree(@RequestParam(required = true) String dynamicPageId,
			@RequestParam(required = false, defaultValue = "") String rows,
			@RequestParam(required = false, defaultValue = "") String columns,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10000") int pageSize,
			@RequestParam(required = false, defaultValue = " T_ORDER ASC") String sortString) {

		List<StoreVO> rowsTemp = new ArrayList<StoreVO>();
		List<StoreVO> resultS = new ArrayList<StoreVO>();
		List<StoreVO> rowsStore = null, columnsStore = null;
		BaseExample rowsBaseExample = new BaseExample();
		rowsBaseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId)
				.andLike("CODE", StoreService.LAYOUT_CODE + "%").andIsNull("DESCRIPTION");
		rowsStore = storeService.selectPagedByExample(rowsBaseExample, currentPage, pageSize, sortString);

		BaseExample columnBaseExample = new BaseExample();
		columnBaseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId)
				.andLike("CODE", StoreService.LAYOUT_CODE + "%").andIsNotNull("DESCRIPTION");
		columnsStore = storeService.selectPagedByExample(columnBaseExample, currentPage, pageSize, sortString);

		if ("".equals(rows)) {
			rowsTemp.addAll(rowsStore);
		} else if (!"".equals(rows)) {
			List<Integer> rowsList = parseScopeString(rows);
			for (int row : rowsList) {
				if (row <= rowsStore.size() && row > 0) {
					rowsTemp.add(rowsStore.get(row - 1));
				}
			}
		}
		// 根据要显示的行，查找需要显示的列
		if (rowsTemp.size() > 0) {
			List<Integer> columnsList = parseScopeString(columns);
			for (StoreVO rowStore : rowsTemp) {
				resultS.add(rowStore);
				if (!"".equals(columns)) {
					int i = 0;
					for (StoreVO colstoreVO : columnsStore) {
						if (rowStore.getId().equals(colstoreVO.getDescription())) {
							i++;
							for (int column : columnsList) {
								if (column == i) {
									resultS.add(colstoreVO);
								}
							}
						}
					}
				} else {
					for (StoreVO colstoreVO : columnsStore) {
						if (rowStore.getId().equals(colstoreVO.getDescription())) {
							resultS.add(colstoreVO);
						}
					}
				}
			}
		}
		JSONArray datas = new JSONArray();
		for (StoreVO vo : resultS) {
			JSONObject o = new JSONObject();
			o.put("id", vo.getId());
			o.put("pId", vo.getDescription());
			o.put("name", vo.getName());
			datas.add(o);
		}
		return datas.toString();
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

	@ResponseBody
	@RequestMapping(value = "/quickSave")
	public StoreVO quickSave(Long rows, Long cods, String parentId, String dynamicPageId, String name) {
		int maxOrder = jdbcTemplate.queryForObject("select ifnull(max(T_ORDER),0) from p_fm_store where DYNAMICPAGE_ID=? " +
				"and code like '0.1.8%' and CONTENT like '%\"layoutType\":\"2\"%'",Integer.class,dynamicPageId);
		for (int i = 0; i < rows; i++) {
			// 保存行布局
			StoreVO rowSV = new StoreVO();
			Object o = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			if (o instanceof PunSystemVO) {
				PunSystemVO system = (PunSystemVO) o;
				rowSV.setSystemId(system.getSysId());
			}
			rowSV.setDescription(parentId);
			rowSV.setCode(StoreService.LAYOUT_CODE + System.currentTimeMillis());
			rowSV.setDynamicPageId(dynamicPageId);
			rowSV.setName(name + "_row" + (i + 1 + maxOrder));
			rowSV.setOrder(i + 1 + maxOrder);
			Layout s = new Layout();
			s.setName(rowSV.getName());
			s.setOrder(rowSV.getOrder());
			s.setPageId("");
			s.setDynamicPageId(dynamicPageId);
			s.setTop(0);
			s.setLeft(0);
			s.setLayoutType(2);
			s.setProportion(12L);
			s.setParentId(parentId);
			s.setDynamicPageId(dynamicPageId);

			String jContent = JSONObject.toJSONString(s);
			rowSV.setContent(jContent);
			String id = storeService.save(rowSV);

			// 遍历存列布局
			for (int j = 0; j < cods; j++) {
				StoreVO codSV = new StoreVO();
				Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
				if (obj instanceof PunSystemVO) {
					PunSystemVO system = (PunSystemVO) obj;
					codSV.setSystemId(system.getSysId());
				}
				codSV.setDescription(id);
				codSV.setCode(StoreService.LAYOUT_CODE + System.currentTimeMillis());
				codSV.setDynamicPageId(dynamicPageId);
				codSV.setName(name + "_row" + (i + 1 + maxOrder) + "_cod" + (j + 1));
				codSV.setOrder((i + 1 + maxOrder) * (j + 1));
				codSV.setDescription(id);
				Layout s1 = new Layout();
				s1.setName(codSV.getName());
				s1.setOrder(codSV.getOrder());
				s1.setPageId("");
				s1.setTop(0);
				s1.setLeft(0);
				s1.setLayoutType(1); // 垂直布局
				s1.setProportion(12 / cods); // 占比为12除以列数
				s1.setParentId(id); // parentId为行布局的Id
				s1.setDynamicPageId(dynamicPageId);
				String jContent1 = JSONObject.toJSONString(s1);
				codSV.setContent(jContent1);
				storeService.save(codSV);
			}

		}
		return new StoreVO();
	}

	/**
	 * 异步合并单元格，
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "merageLayoutByAjax")
	public String merageLayoutByAjax(@RequestParam(value = "_selects") String selects) {

		return merageLayoutByIds(selects);

	}

	private String merageLayoutByIds(String selects) {
		String[] ids = selects.split(",");
		List<StoreVO> list = new ArrayList<StoreVO>(); // 待合并的布局
		StringBuilder sb = new StringBuilder();
		long sumPortition = 0;
		String layoutType = "";
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				StoreVO s = storeService.findById(ids[i]);
				if (s != null) {
					list.add(s);
					sb.append(s.getName() + "_");
				}
				JSONObject jo = JSON.parseObject(s.getContent());
				if (jo != null) {
					sumPortition += jo.getIntValue("proportion");
					layoutType = jo.getString("layoutType");
				}
			}
		}
		if (!belongToOneParent(list)) { // 判断是否能合并（是否要限定不合并行，zui合并行布局与删除一行等同）
			return "-1"; // 所属不同父布局，无法合并
		}

		// 生成合并的布局，并保存；
		StoreVO codSV = new StoreVO();
		Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			codSV.setSystemId(system.getSysId());
		}
		codSV.setDescription(list.get(0).getDescription());
		codSV.setCode(StoreService.LAYOUT_CODE + System.currentTimeMillis());
		codSV.setDynamicPageId(list.get(0).getDynamicPageId());
		// codSV.setName("merage" + sb.toString());
		codSV.setName("merage" + System.currentTimeMillis());

		codSV.setOrder(list.get(0).getOrder());
		codSV.setDescription(list.get(0).getDescription());
		Layout s1 = new Layout();
		s1.setName(codSV.getName());
		s1.setOrder(codSV.getOrder());
		s1.setPageId("");
		s1.setDynamicPageId(list.get(0).getDynamicPageId());

		s1.setTop(0);
		s1.setLeft(0);
		s1.setLayoutType(Integer.valueOf(layoutType)); // 布局类型
		s1.setProportion(sumPortition);
		s1.setParentId(list.get(0).getDescription()); // parentId为行布局的Id
		String jContent1 = JSONObject.toJSONString(s1);
		codSV.setContent(jContent1);
		String id1 = storeService.save(codSV); // 保存合并布局并生成对应Id

		Long systemId = null;
		Object oo = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (oo instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) oo;
			systemId = system.getSysId();
		}
		// 原有组件所属layout是否要设置成 合并layout
		for (int i = 0; i < list.size(); i++) {
			List<?> componentList = storeService.queryComponentByLayoutId(list.get(i).getId(), systemId);
			if (componentList != null && componentList.size() > 0) {
				for (int j = 0; j < componentList.size(); j++) {
					StoreVO s = (StoreVO) componentList.get(j);
					JSONObject o = JSONObject.parseObject(s.getContent());
					if (o.containsKey("layoutId")) {
						o.put("layoutId", id1);
					}
					if (o.containsKey("layoutName")) {
						o.put("layoutName", codSV.getName());
					}
					s.setContent(o.toJSONString());
					storeService.save(s);
				}
			}
		}

		if (storeService.delete(ids)) {
			return "1";
		} else {
			return "-2";
		}
	}

	private boolean belongToOneParent(List<StoreVO> list) {

		if (list.get(0).getDescription() == null) { // 顶层布局
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getDescription() != null) {
					return false;
				}
			}
			return false; // 暂不支持行合并，待定（）
		} else {
			String parentId = list.get(0).getDescription();
			for (int i = 0; i < list.size(); i++) {
				if (!parentId.equals(list.get(i).getDescription())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 批量拷贝布局，包括布局中的组件
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "copyLayoutByAjax")
	public String copyLayoutByAjax(@RequestParam(value = "_selects") String selects) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			copyLayout(ids[i], s.getDescription());
		}
		return "1";
	}

	/**
	 * 拷贝布局，包括布局中的组件
	 * 
	 * @param id:布局id;
	 *            parentId:父布局Id 递归调用
	 * @return
	 */
	private void copyLayout(String id, String parentId) {
		StoreVO s = storeService.findById(id);
		StoreVO o = BeanUtils.getNewInstance(s, StoreVO.class);
		o.setId(""); // 置Id为空，表示新增
		o.setName("copy_" + s.getName());
		Long systemId = null;
		Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			systemId = system.getSysId();
			o.setSystemId(system.getSysId());
		}
		o.setDescription(parentId);
		JSONObject jsonO = JSON.parseObject(o.getContent());
		if (jsonO.containsKey("parentId")) { // 新拷贝的布局，父布局需更新
			jsonO.put("parentId", parentId);
		}
		if (jsonO.containsKey("pageId")) { // 置pageId为空，表示新增
			jsonO.put("pageId", "");
		}
		if (jsonO.containsKey("name")) {
			jsonO.put("name", "copy_" + jsonO.getString("name"));
		}
		o.setContent(jsonO.toJSONString());
		String newLayoutId = storeService.save(o); // 保存新的layout

		// 查询出布局中所在的component
		List<?> componentList = storeService.queryComponentByLayoutId(id, systemId);
		if (componentList != null && componentList.size() > 0) {
			for (int i = 0; i < componentList.size(); i++) {
				StoreVO component = (StoreVO) componentList.get(i);
				StoreVO copyComponent = BeanUtils.getNewInstance(component, StoreVO.class);
				copyComponent.setId(""); // 置Id为空，表示新增
				copyComponent.setName("copy_" + component.getName());
				JSONObject copyJson = JSON.parseObject(copyComponent.getContent());
				if (copyJson.containsKey("pageId")) { // 置pageId为空，表示新增
					copyJson.put("pageId", "");
				}
				if (copyJson.containsKey("layoutId")) { // 置组件的所属Layout为新copy的Layout
					copyJson.put("layoutId", newLayoutId);
				}
				if (copyJson.containsKey("layoutName")) { // 置组件的所属Layout为新copy的Layout
					copyJson.put("layoutName", o.getName());
				}
				if (copyJson.containsKey("name")) {
					copyJson.put("name", "copy_" + copyJson.getString("name"));
				}
				copyComponent.setContent(copyJson.toJSONString());
				storeService.save(copyComponent); // 保存
			}
		}

		// 查询出子布局,递归处理子布局
		BaseExample example = new BaseExample();
		example.createCriteria().andEqualTo("description", id).andLike("code", StoreService.LAYOUT_CODE + "%");
		List<StoreVO> result = storeService.selectPagedByExample(example, 1, Integer.MAX_VALUE, null);
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				StoreVO temp = result.get(i);
				copyLayout(temp.getId(), newLayoutId); // 递归调用
			}

		}
	}

	private JSONObject configToJson(String config) {
		if (StringUtils.isNotBlank(config) && config.length() > 3) {
			JSONObject o = new JSONObject();
			if (config.indexOf(":") != -1) {
				String content = config.substring(1);
				o = JSON.parseObject(content);
				if (!StringUtils.isNotBlank(o.getString("type"))) {
					o.put("componentType", "1001");
				} else {
					o.put("componentType", o.getString("type"));
					o.remove("type");
				}
			} else {
				String content = config.substring(2, config.length() - 1);
				o.put("componentType", content);
			}
			return o;
		}
		return null;
	}

	private void createLabel(String content, String dynamicPageId, String codId, String codName, int index) {
		if (index <= 0) {
			index = 1;
		}
		Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		StoreVO labelStore = new StoreVO(); // 对应组件的文本组件
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			labelStore.setSystemId(system.getSysId());
		}
		labelStore.setCode(StoreService.COMPONENT_CODE + UUID.randomUUID().toString());
		labelStore.setDynamicPageId(dynamicPageId);
		labelStore.setName("excel_label_" + UUID.randomUUID().toString());
		labelStore.setOrder(index); // 默认设为1
		JSONObject componentJson = new JSONObject();

		componentJson.put("componentType", "1009");
		componentJson.put("dynamicPageId", dynamicPageId);
		componentJson.put("layoutId", codId);
		componentJson.put("layoutName", codName);
		componentJson.put("name", labelStore.getName());
		componentJson.put("title", content);
		componentJson.put("pageId", "");
		componentJson.put("order", labelStore.getOrder());
		labelStore.setContent(componentJson.toJSONString());

		storeService.save(labelStore); // 保存相应的文本组件
	}

	private void createSpan(String content, String dynamicPageId, String codId, String codName, int index) {
		if (index <= 0) {
			index = 1;
		}
		Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		StoreVO labelStore = new StoreVO(); // 对应组件的文本组件
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			labelStore.setSystemId(system.getSysId());
		}
		labelStore.setCode(StoreService.COMPONENT_CODE + UUID.randomUUID().toString());
		labelStore.setDynamicPageId(dynamicPageId);
		labelStore.setName("excel_span_" + UUID.randomUUID().toString());
		labelStore.setOrder(index); // 默认设为1
		JSONObject componentJson = new JSONObject();

		componentJson.put("componentType", "1014");
		componentJson.put("dynamicPageId", dynamicPageId);
		componentJson.put("layoutId", codId);
		componentJson.put("layoutName", codName);
		componentJson.put("name", labelStore.getName());
		componentJson.put("title", content);
		componentJson.put("pageId", "");
		componentJson.put("order", labelStore.getOrder());
		labelStore.setContent(componentJson.toJSONString());

		storeService.save(labelStore); // 保存相应的文本组件
	}

	/**
	 * 批量修改 布局占比
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyProportion")
	public String batchModifyProportion(@RequestParam(value = "_selects") String selects, String dynamicPageId,
			String proportion) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			// 置Id为空，表示新增
			JSONObject json = JSON.parseObject(s.getContent());
			json.put("proportion", Long.valueOf(proportion));
			s.setContent(json.toJSONString());
			storeService.save(s); // 保存
		}
		return "1";
	}

	/**
	 * 批量修改 行高度
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyHeight")
	public String batchModifyHeight(@RequestParam(value = "_selects") String selects, String dynamicPageId,
			String height, String heightType, String width) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			// 置Id为空，表示新增
			JSONObject json = JSON.parseObject(s.getContent());
			if (StringUtils.isNotBlank(height)) {
				json.put("height", height);
			}
			if (StringUtils.isNotBlank(heightType)) {
				json.put("heightType", heightType);
			}
			if (StringUtils.isNotBlank(width)) {
				json.put("width", width);
			}
			if (StringUtils.isNotBlank(heightType) || StringUtils.isNotBlank(height) || StringUtils.isNotBlank(width)) {
				s.setContent(json.toJSONString());
				storeService.save(s);
			}
		}
		return "1";
	}

	/**
	 * 批量清除 行高度
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchClearHeight")
	public String batchClearHeight(@RequestParam(value = "_selects") String selects, String dynamicPageId,
			String height, String width) {
		String[] ids = selects.split(",");
		if (StringUtils.isNotBlank(height) || StringUtils.isNotBlank(width)) {
			for (int i = 0; i < ids.length; i++) {
				StoreVO s = storeService.findById(ids[i]);
				// 置Id为空，表示新增
				JSONObject json = JSON.parseObject(s.getContent());
				if (StringUtils.isNotBlank(height) && "1".equalsIgnoreCase(height)) {
					json.put("height", "");
				}
				if (StringUtils.isNotBlank(width) && "1".equalsIgnoreCase(width)) {
					json.put("width", "");
				}
				s.setContent(json.toJSONString());
				storeService.save(s);
			}
		}
		return "1";
	}

	/**
	 * 批量修改 对齐方式
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyAlign")
	public String batchModifyAlign(@RequestParam(value = "_selects") String selects, String dynamicPageId,
			String textalign, String textverticalalign) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			// 置Id为空，表示新增
			JSONObject json = JSON.parseObject(s.getContent());
			if ("1".equalsIgnoreCase(json.getString("layoutType"))) {
				json.put("textalign", textalign);
				json.put("textverticalalign", textverticalalign);
				s.setContent(json.toJSONString());
				storeService.save(s);
			} else {
				continue;
			}
		}
		return "1";
	}

	/**
	 * 批量修改 边框
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyBorder")
	public String batchModifyBorder(@RequestParam(value = "_selects") String selects, String dynamicPageId,
			String textstyle) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			// 置Id为空，表示新增
			JSONObject json = JSON.parseObject(s.getContent());
			json.put("textstyle", textstyle);
			s.setContent(json.toJSONString());
			storeService.save(s);
		}
		return "1";
	}


	private boolean isSimpleLabel(JSONObject component) {
		boolean flag = true;
		String comType = component.getString("componentType");
		if ("1009".equals(comType) || "1014".equals(comType)) {
			flag = StringUtils.isBlank(component.getString("style"));
		}
		return flag;
	}

	private String appendProperties(JSONObject component) {
		StringBuilder sb = new StringBuilder();
		sb.append("type:").append(component.getString("componentType")).append(",").append("name:").append("\"")
				.append(component.getString("name")).append("\",");
		if (StringUtils.isNotBlank(component.getString("title"))) {
			sb.append("title:\"").append(component.getString("title")).append("\",");
		}
		if (StringUtils.isNotBlank(component.getString("style"))) {
			sb.append("style:").append("\"").append(component.getString("style")).append("\",");
		}
		if (StringUtils.isNotBlank(component.getString("dataItemCode"))) {
			sb.append("dataItemCode:\"").append(component.getString("dataItemCode")).append("\",");
		}
		if (StringUtils.isNotBlank(component.getString("description"))) {
			sb.append("description:\"").append(component.getString("description")).append("\",");
		}
		if (StringUtils.isNotBlank(component.getString("validateAllowNull"))) {
			sb.append("validateAllowNull:\"").append(component.getString("validateAllowNull")).append("\",");
		}
		if (StringUtils.isNotBlank(component.getString("validatJson"))) {
			sb.append("validatJson:\"").append(component.getString("validatJson")).append("\",");
		}
		if (StringUtils.isNotBlank(component.getString("hiddenScript"))) {
			sb.append("hiddenScript:\"").append(component.getString("hiddenScript")).append("\",");
		}
		if (StringUtils.isNotBlank(component.getString("disabledScript"))) {
			sb.append("disabledScript:\"").append(component.getString("disabledScript")).append("\",");
		}
		if (StringUtils.isNotBlank(component.getString("readonlyScript"))) {
			sb.append("readonlyScript:\"").append(component.getString("readonlyScript")).append("\",");
		}
		if (StringUtils.isNotBlank(component.getString("defaultValueScript"))) {
			sb.append("defaultValueScript:\"").append(component.getString("defaultValueScript")).append("\",");
		}
		if (StringUtils.isNotBlank(component.getString("optionScript"))) {
			sb.append("optionScript:\"").append(component.getString("optionScript")).append("\",");
		}
		if (StringUtils.isNotBlank(component.getString("onchangeScript"))) {
			sb.append("onchangeScript:\"").append(component.getString("onchangeScript")).append("\",");
		}
		return sb.substring(0, sb.lastIndexOf(","));
	}


	@ResponseBody
	@RequestMapping(value = "/loadComByCondition")
	public List<StoreVO> loadComByCondition(@RequestParam(required = true) String dynamicPageId,
			@RequestParam(required = false) String cname, @RequestParam(required = false) String dataCode,
			@RequestParam(required = false) Integer typeId, @RequestParam(required = false) String rowValue,
			@RequestParam(required = false) String colValue,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = " T_ORDER ASC") String sortString) {
		BaseExample baseExample = new BaseExample();
		try {

			cname = java.net.URLDecoder.decode(cname, "UTF-8");
			dataCode = java.net.URLDecoder.decode(dataCode, "UTF-8");
			// cname = new String(cname.getBytes("ISO8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("ERROR", e);
		}
		logger.debug("cname:" + cname + ", dataCode:" + dataCode);

		baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId).andLike("CODE",
				StoreService.COMPONENT_CODE + "%");

		List<StoreVO> storeVos = storeService.selectPagedByExample(baseExample, currentPage, pageSize, sortString);
		// 结果的中间值，经过名称、数据源名称，类型过滤后的值
		List<StoreVO> temResult1 = new ArrayList<StoreVO>();
		// 结果中间值，temResult经过布局行列条件过滤后的值
		List<StoreVO> temResult2 = new ArrayList<StoreVO>();
		List<StoreVO> result = new ArrayList<StoreVO>();
		temResult1 = this.restritByTypeAndData(typeId, dataCode, storeVos);
		temResult2 = this.restrictByLayout(dynamicPageId, rowValue, colValue, currentPage, pageSize, temResult1);
		if (cname != null && !"".equals(cname)) {
			for (StoreVO storeVo : temResult2) {
				JSONObject json = JSONObject.parseObject(storeVo.getContent());
				Integer componentType = json.getInteger("componentType");
				if (componentType == 1009 || componentType == 1014) {
					String title = json.getString("title");
					if (title != null) {
						if (title.indexOf(cname) != -1) {
							result.add(storeVo);
						}
					}
				}
				String name = storeVo.getName();
				if (name.indexOf(cname) != -1) {
					result.add(storeVo);
				}

			}
		} else {
			result = temResult2;
		}

		return result;
	}

	private List<StoreVO> restritByTypeAndData(Integer typeId, String dataCode, List<StoreVO> storeVos) {
		List<StoreVO> result = new ArrayList<StoreVO>();
		if (typeId != null && !"".equals(typeId) && dataCode != null && !"".equals(dataCode)) {
			for (StoreVO storeVo : storeVos) {
				JSONObject json = JSONObject.parseObject(storeVo.getContent());
				Integer componentType = json.getInteger("componentType");
				String dataItemCode = json.getString("dataItemCode");
				if (dataItemCode != null) {
					if (typeId.equals(componentType) && dataItemCode.indexOf(dataCode) != -1) {
						result.add(storeVo);
					}
				}
				if (typeId == 1009 || typeId == 1014) {
					String title = json.getString("title");
					if (title != null) {
						if (title.indexOf(dataCode) != -1) {
							result.add(storeVo);
						}
					}
				}
			}
			// result = this.restrictByLayout(dynamicPageId, rowValue, colValue,
			// currentPage, pageSize, temResult);
		} else if (typeId != null && !"".equals(typeId)) {
			for (StoreVO storeVo : storeVos) {
				JSONObject json = JSONObject.parseObject(storeVo.getContent());
				Integer componentType = json.getInteger("componentType");
				if (typeId.equals(componentType)) {
					result.add(storeVo);
				}
			}
			// result = this.restrictByLayout(dynamicPageId, rowValue, colValue,
			// currentPage, pageSize, temResult);
		} else if (dataCode != null && !"".equals(dataCode)) {
			for (StoreVO storeVo : storeVos) {
				JSONObject json = JSONObject.parseObject(storeVo.getContent());
				String dataItemCode = json.getString("dataItemCode");
				Integer componentType = json.getInteger("componentType");
				logger.debug("dataItemCode:" + dataItemCode);
				if (dataItemCode != null) {
					if (dataItemCode.indexOf(dataCode) != -1) {
						result.add(storeVo);
					}
				}
				if (componentType == 1009 || componentType == 1014) {
					String title = json.getString("title");
					logger.debug("title:" + title);
					if (title != null) {
						if (title.indexOf(dataCode) != -1) {
							result.add(storeVo);
						}
					}
				}

			}
			// result = this.restrictByLayout(dynamicPageId, rowValue, colValue,
			// currentPage, pageSize, temResult);
		} else {
			// result = this.restrictByLayout(dynamicPageId, rowValue, colValue,
			// currentPage, pageSize, storeVos);
			result = storeVos;

		}
		return result;
	}

	// 根据布局行列条件过滤
	private List<StoreVO> restrictByLayout(String dynamicPageId, String rowValue, String colValue, int currentPage,
			int pageSize, List<StoreVO> storeVos) {
		List<StoreVO> result = new ArrayList<StoreVO>();
		if ((colValue != null && !"".equals(colValue)) || (rowValue != null && !"".equals(rowValue))) {
			List<StoreVO> tems = this.getComponentListByPageId(dynamicPageId, rowValue, colValue, currentPage, pageSize,
					" T_ORDER ASC");
			for (StoreVO s1 : storeVos) {
				String layoutId = JSON.parseObject(s1.getContent()).getString("layoutId");
				if (layoutId != null) {
					for (StoreVO s2 : tems) {
						if (layoutId.equals(s2.getId())) {
							result.add(s1);
						}
					}
				}
			}
		} else {
			result = storeVos;
		}
		return result;
	}

	/**
	 * wuhg 2015.4.1 布局树页面，跳转至新页面打开
	 * 
	 * @param dynamicPageId
	 * @return
	 */
	@RequestMapping(value = "/turnToLayoutComponent")
	public ModelAndView turnToLayoutComponent(String dynamicPageId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("vo", formdesignerServiceImpl.findById(dynamicPageId));
		mv.setViewName("formdesigner/page/tabs/component-layout-list");
		mv.addObject("_COMPOENT_TYPE_NAME", FormDesignGlobal.COMPOENT_TYPE_NAME);
		return mv;
	}

}
