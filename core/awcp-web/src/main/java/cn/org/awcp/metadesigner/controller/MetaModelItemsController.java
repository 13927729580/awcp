package cn.org.awcp.metadesigner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.Criteria;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.metadesigner.application.MetaModelItemService;
import cn.org.awcp.metadesigner.application.MetaModelService;
import cn.org.awcp.metadesigner.util.CreateTables;
import cn.org.awcp.metadesigner.util.UpdateColumn;
import cn.org.awcp.metadesigner.vo.MetaModelItemsVO;
import cn.org.awcp.metadesigner.vo.MetaModelVO;
import cn.org.awcp.unit.vo.PunSystemVO;

@Controller("metaModelItemsController")
@RequestMapping(value = "/metaModelItems")
public class MetaModelItemsController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private MetaModelService metaModelServiceImpl;

	@Autowired
	private MetaModelItemService metaModelItemsServiceImpl;

	@Autowired
	private CreateTables createTables;

	/**
	 * 增加
	 * 
	 * @param vo
	 * @param modelIds
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/save")
	public String save(@ModelAttribute MetaModelItemsVO vo, String modelIdss) {
		// 1.判断数据库是否已经存在这一属性
		List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryColumn("queryList", vo.getModelId(),
				vo.getItemCode());
		if (ls.isEmpty()) {
			vo.setItemValid(0);
			this.metaModelItemsServiceImpl.save(vo);
			return "redirect:queryResultByParams.do?id=" + vo.getModelId();
		} else {
			return "redirect:queryResultByParams.do?id=" + vo.getModelId();
		}

	}

	/**
	 * 增加
	 * 
	 * @param vo
	 * @param modelIds
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saves")
	public Map<String, Object> saves(@ModelAttribute MetaModelItemsVO vo) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 1.判断数据库是否已经存在这一属性
		List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryColumn("queryList", vo.getModelId(),
				vo.getItemCode());
		if (ls.isEmpty()) {
			vo.setItemValid(0);
			this.metaModelItemsServiceImpl.save(vo);
			map.put("id", vo.getModelId());
			return map;
		} else {
			map.put("id", vo.getModelId());
			return map;
		}
	}

	/**
	 * 删除操作
	 * 
	 * @param classId
	 *            metaModel中的ID
	 * @return
	 */
	@RequestMapping(value = "/remove")
	public String remove(MetaModelItemsVO vo) {
		this.metaModelItemsServiceImpl.remove(vo);
		return "redirect:queryResultByParams.do?id=" + vo.getModelId();

	}

	/**
	 * /查询单个 用于弹窗
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/gets")
	public MetaModelItemsVO get(String id, Model model) {
		MetaModelItemsVO vo = this.metaModelItemsServiceImpl.get(id);
		List<MetaModelVO> ms = new ArrayList<MetaModelVO>();
		List<MetaModelVO> mm = this.metaModelServiceImpl.findAll();
		for (MetaModelVO m : mm) {
			if (this.metaModelServiceImpl.tableIsExist(m.getTableName())) {
				if (m.getId() != vo.getModelId()) {
					ms.add(m);
				}
			}
		}
		model.addAttribute("modelId", vo.getModelId());
		model.addAttribute("metaModel", ms);
		model.addAttribute("vo", vo);
		return vo;
	}

	/**
	 * 修改
	 * 
	 * @param vo
	 * @param modelIds
	 * @return
	 */
	@RequestMapping(value = "/update")
	public String update(@ModelAttribute MetaModelItemsVO vo) {
		this.metaModelItemsServiceImpl.update("update", vo);
		return "redirect:queryResultByParams.do?id=" + vo.getModelId();
	}

	/**
	 * 页面跳转 点击增加
	 * 
	 * @param model
	 * @param modelId
	 * @return
	 */
	@RequestMapping(value = "/add_toggle")
	public String addToggle(Model model, String modelId) {
		List<MetaModelVO> ms = new ArrayList<MetaModelVO>();
		BaseExample baseExample = new BaseExample();
		Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			baseExample.createCriteria().andEqualTo("SYSTEM_ID", system.getSysId());
		}
		List<MetaModelVO> mm = this.metaModelServiceImpl.selectPagedByExample(baseExample, 1, 10, null);
		for (MetaModelVO m : mm) {
			if (this.metaModelServiceImpl.tableIsExist(m.getTableName())) {
				if (!m.getId().equals(modelId)) {
					ms.add(m);
				}
			}
		}
		String type = "no";
		List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryResult("queryResult", modelId);
		for (MetaModelItemsVO mmi : ls) {
			if (mmi.getUsePrimaryKey() != null && mmi.getUsePrimaryKey() == 1) {
				type = "yes";
			}
		}
		model.addAttribute("type", type);
		model.addAttribute("modelId", modelId);
		model.addAttribute("metaModel", ms);
		return "metadesigner/metaModelItem/metaModelItem_add";
	}

	/**
	 * 页面跳转 修改页面
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/update_toggle")
	public String updateToggle(Model model, String id) {
		MetaModelItemsVO vo = this.metaModelItemsServiceImpl.get(id);
		model.addAttribute("vo", vo);
		List<MetaModelVO> ms = new ArrayList<MetaModelVO>();

		BaseExample baseExample = new BaseExample();
		Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			baseExample.createCriteria().andEqualTo("SYSTEM_ID", system.getSysId());
		}
		List<MetaModelVO> mm = this.metaModelServiceImpl.selectPagedByExample(baseExample, 1, 10, null);
		for (MetaModelVO m : mm) {
			if (this.metaModelServiceImpl.tableIsExist(m.getTableName())) {
				if (m.getId() != vo.getModelId()) {
					ms.add(m);
				}
			}
		}
		String type = "no";
		List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryResult("queryResult", vo.getModelId());
		for (MetaModelItemsVO mmi : ls) {
			if (mmi.getUsePrimaryKey() != null && (1 == mmi.getUsePrimaryKey())) {
				type = "yes";
			}
		}
		model.addAttribute("type", type);
		model.addAttribute("metaModel", ms);
		return "metadesigner/metaModelItem/metaModelItem_edit";
	}

	/**
	 * /条件查询
	 * 
	 * @param modelId
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/queryResultByParams")
	public String queryResult(@RequestParam(value = "id") String modelId,
			@RequestParam(required = false, defaultValue = "1") int currentPage, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		String queryStr = "queryList";
		map.put("modelId", modelId);
		MetaModelVO vo = metaModelServiceImpl.get(modelId);
		model.addAttribute("vo", vo);
		int pageSize = 20;
		PageList<MetaModelItemsVO> pl = this.metaModelItemsServiceImpl.queryResult(queryStr, map, currentPage, pageSize,
				"id.desc");
		model.addAttribute("list", pl);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("ids", modelId);
		return "metadesigner/metaModelItem/metaModelItem_list";
	}

	/**
	 * 条件加分页查询 用于搜索
	 * 
	 * @param modelId
	 * @param model
	 * @param currentPage
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "queryPageResult")
	public String queryPageResult(Model model, @RequestParam(required = false, defaultValue = "1") int currentPage,
			MetaModelItemsVO vo) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (vo != null) {
			map.put("itemName", vo.getItemName());
			map.put("itemCode", vo.getItemCode());
			map.put("itemType", vo.getItemType());
			map.put("defaultValue", vo.getDefaultValue());
			map.put("modelId", vo.getModelId());
		}
		String queryStr = "queryList";
		int pageSize = 5;
		PageList<MetaModelItemsVO> pl = this.metaModelItemsServiceImpl.queryResult(queryStr, map, currentPage, pageSize,
				"id.desc");
		model.addAttribute("list", pl);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("ids", vo.getModelId());
		return "metadesigner/metaModelItem/metaModelItem_list";
	}

	@RequestMapping(value = "/selectPage")
	public String selectPageByExample(Model model, @RequestParam(required = false, defaultValue = "1") int currentPage,
			MetaModelItemsVO vo) {
		BaseExample be = new BaseExample();
		Criteria c = be.createCriteria();
		String itemName = vo.getItemName();
		if (currentPage == 0) {
			currentPage = 1;
		}
		if (itemName != null && !"".equals(itemName)) {
			c.andLike("itemName", "%" + itemName + "%");
		}
		String itemCode = vo.getItemCode();
		if (itemCode != null && !"".equals(itemCode)) {
			c.andLike("itemCode", "%" + itemCode + "%");
		}
		String itemType = vo.getItemType();
		if (itemType != null && !"".equals(itemType)) {
			c.andLike("itemType", "%" + itemType + "%");
		}
		String defaultValue = vo.getDefaultValue();
		if (defaultValue != null && !"".equals(defaultValue)) {
			c.andLike("defaultValue", "%" + defaultValue + "%");
		}
		String modelId = vo.getModelId();
		if (StringUtils.isNotBlank(modelId)) {
			c.andEqualTo("modelId", modelId);
			MetaModelVO m = metaModelServiceImpl.get(modelId);
			model.addAttribute("vo", m);
		}
		PageList<MetaModelItemsVO> pl = this.metaModelItemsServiceImpl.selectPagedByExample(be, currentPage, 20,
				"id desc");
		model.addAttribute("list", pl);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("ids", vo.getModelId());
		return "metadesigner/metaModelItem/metaModelItem_list";
	}

	@RequestMapping(value = "/createTable")
	public String createTable(@RequestParam(value = "modelId") String idss, Model model) {
		MetaModelVO vo = this.metaModelServiceImpl.get(idss);
		// 判断表是否存在
		if (metaModelServiceImpl.tableIsExist(vo.getTableName())) {
			List<MetaModelItemsVO> mmo = this.metaModelItemsServiceImpl.queryByState("queryByState", idss);
			// 去查找数据库是否有这一列
			for (MetaModelItemsVO mm : mmo) {
				if (!metaModelItemsServiceImpl.columnIsExist("columnIsExist", vo.getTableName(), mm.getItemName())) {
					String str = UpdateColumn.newAddColumn(mm, vo.getTableName());
					// 修改状态
					mm.setItemValid(1);
					this.metaModelItemsServiceImpl.update("updateSelective", mm);
					metaModelServiceImpl.excuteSql(str);
					model.addAttribute("msg", 1);
				}
			}
			vo.setModelSynchronization(true);
			this.metaModelServiceImpl.update("update", vo);
			return "redirect:../metaModel/queryResult.do?currentPage=" + 1;
		} else {
			try {
				List<MetaModelItemsVO> pl = this.metaModelItemsServiceImpl.queryResult("queryResult", vo.getId());
				String str = createTables.getSql(vo, pl);
				metaModelServiceImpl.excuteSql(str);
				// 执行完之后，改变其状态为1
				for (MetaModelItemsVO mmm : pl) {
					mmm.setItemValid(1);
					this.metaModelItemsServiceImpl.update("updateSelective", mmm);
				}
				model.addAttribute("msg", 1);
			} catch (Exception e) {
				model.addAttribute("msg", 0);
			}

			vo.setModelSynchronization(true);
			this.metaModelServiceImpl.update("update", vo);
			return "redirect:../metaModel/queryResult.do?currentPage=" + 1;
		}
	}

	/**
	 * /点击发布
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/release")
	public String release(@RequestParam(value = "modelId") String idss, Model model,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {
		MetaModelVO vo = this.metaModelServiceImpl.get(idss);
		vo.setModelSynchronization(true);
		metaModelServiceImpl.update("update", vo);
		model.addAttribute("currentPage", currentPage);
		return "redirect:../metaModel/queryResult.do";
	}

	// --------------------------数据校验------------------------------------
	/**
	 * 数据校验
	 * 
	 * @param itemCode
	 * @param modelId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "dataValidate")
	public Object dataValidate(@RequestParam("itemCode") String itemCode, @RequestParam("modelId") String modelId,
			@RequestParam(required = false, value = "id") String id) {
		// 判断是增加时验证，还是修改时验证
		if (StringUtils.isBlank(id)) {
			List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryColumn("queryColumn", modelId, itemCode);
			return ls.size();
		} else {
			MetaModelItemsVO mmi = this.metaModelItemsServiceImpl.get(id);
			List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryColumn("queryColumn", modelId, itemCode);
			for (int i = 0; i < ls.size(); i++) {
				if (ls.get(i).getItemCode().equals(mmi.getItemCode())) {
					ls.remove(i);
				}
			}
			return ls.size();
		}

	}

}
