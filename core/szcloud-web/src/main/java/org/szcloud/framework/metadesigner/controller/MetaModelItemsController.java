package org.szcloud.framework.metadesigner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.domain.Criteria;
import org.szcloud.framework.core.utils.Tools;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.metadesigner.application.MetaModelItemService;
import org.szcloud.framework.metadesigner.application.MetaModelService;
import org.szcloud.framework.metadesigner.application.ModelRelationService;
import org.szcloud.framework.metadesigner.util.ICheckIsInt;
import org.szcloud.framework.metadesigner.util.ICreateTables;
import org.szcloud.framework.metadesigner.util.UpdateColumn;
import org.szcloud.framework.metadesigner.vo.MetaModelItemsVO;
import org.szcloud.framework.metadesigner.vo.MetaModelVO;
import org.szcloud.framework.metadesigner.vo.ModelRelationVO;
import org.szcloud.framework.unit.vo.PunSystemVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Controller("metaModelItemsController")
@RequestMapping(value = "/metaModelItems")
public class MetaModelItemsController {

	@Autowired
	private MetaModelService metaModelServiceImpl;

	@Autowired
	private MetaModelItemService metaModelItemsServiceImpl;

	@Autowired
	private ModelRelationService modelRelationServiceImpl;

	@Autowired
	private ICreateTables createTables;

	@Autowired
	private ICheckIsInt checkIsInt;

	/**
	 * 增加
	 * 
	 * @param vo
	 * @param modelIds
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/save")
	public String save(@ModelAttribute MetaModelItemsVO vo, long modelIdss) {
		try {
			// 1.判断数据库是否已经存在这一属性
			List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryColumn("queryList", vo.getModelId(),
					vo.getItemCode());
			if (!(ls.size() > 0)) {
				ModelRelationVO model = new ModelRelationVO();
				if (checkIsInt.isInt(vo.getItemType())) {
					long i = Integer.parseInt(vo.getItemType().toString());
					if (vo.getItemType().equals("1")) {
						model.setRelationName("一对一 ");
						model.setRelationType("OneToOne");
						vo.setItemType("一对一");
					}
					if (vo.getItemType().equals("2")) {
						model.setRelationName("多对一");
						model.setRelationType("ManyToOne");
						vo.setItemType("多对一");
					}
					vo.setItemValid(0);
					this.metaModelItemsServiceImpl.save(vo);
					long maxId = 0;
					vo.setItemValid(0);
					List<MetaModelItemsVO> mmvo = this.metaModelItemsServiceImpl.queryResult("maxId", vo.getModelId());
					if (mmvo.size() > 0) {
						MetaModelItemsVO voss = mmvo.get(0);
						maxId = voss.getId();
					}
					model.setItemId(maxId);
					model.setModelId(modelIdss);
					modelRelationServiceImpl.save(model);
				} else {
					vo.setItemValid(0);
					this.metaModelItemsServiceImpl.save(vo);
				}
				return "redirect:queryResultByParams.do?id=" + vo.getModelId();
			} else {
				return "redirect:queryResultByParams.do?id=" + vo.getModelId();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
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
		try {
			// 1.判断数据库是否已经存在这一属性
			List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryColumn("queryList", vo.getModelId(),
					vo.getItemCode());
			if (!(ls.size() > 0)) {
				ModelRelationVO model = new ModelRelationVO();
				if (checkIsInt.isInt(vo.getItemType())) {
					long i = Integer.parseInt(vo.getItemType().toString());
					if (vo.getItemType().equals("1")) {
						model.setRelationName("一对一 ");
						model.setRelationType("OneToOne");
						vo.setItemType("一对一");
					}
					if (vo.getItemType().equals("2")) {
						model.setRelationName("多对一");
						model.setRelationType("ManyToOne");
						vo.setItemType("多对一");
					}
					vo.setItemValid(0);
					this.metaModelItemsServiceImpl.save(vo);
					long maxId = 0;
					vo.setItemValid(0);
					List<MetaModelItemsVO> mmvo = this.metaModelItemsServiceImpl.queryResult("maxId", vo.getModelId());
					if (mmvo.size() > 0) {
						MetaModelItemsVO voss = mmvo.get(0);
						maxId = voss.getId();
					}
					model.setItemId(maxId);
					modelRelationServiceImpl.save(model);
				} else {
					vo.setItemValid(0);
					this.metaModelItemsServiceImpl.save(vo);
				}
				map.put("id", vo.getId());
				return map;// "redirect:queryResultByParams.do?id="+vo.getModelId();
			} else {
				map.put("id", vo.getId());
				return map;// "redirect:queryResultByParams.do?id="+vo.getModelId();
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("id", 0);
			return map;// "error";
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
	public String remove(Long id) {

		MetaModelItemsVO vo = this.metaModelItemsServiceImpl.get(id);
		// MetaModelVO
		// metaModelVO=this.metaModelServiceImpl.get(vo.getModelId());
		// //1.判断属性是否有外键关系
		// ModelRelationVO mr=this.modelRelationServiceImpl.queryByItem(id);
		// try {
		// long ids=mr.getId();
		// //存在 删除关系
		// this.modelRelationServiceImpl.remove(mr);
		// //删除表
		// String str="alter table "+metaModelVO.getTableName()+" drop foreign
		// key fk_"+vo.getItemCode();
		// this.metaModelServiceImpl.excuteSql(str);
		// } catch (Exception e) {
		// logger.debug("没有外键关系");
		// }
		this.metaModelItemsServiceImpl.remove(vo);
		// String sql="alter table "+metaModelVO.getTableName()+" drop column
		// "+vo.getItemCode();
		// this.metaModelServiceImpl.excuteSql(sql);
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
	public MetaModelItemsVO get(Long id, Model model) {
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
	 * 根据itemId查询ModelRelation
	 * 
	 * @param itemId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryRelation")
	public MetaModelVO queryRelation(long itemId) {
		ModelRelationVO modelRelation = this.modelRelationServiceImpl.queryByItem(itemId);
		// 查询出对应的元数据
		MetaModelVO mmvo = this.metaModelServiceImpl.get(modelRelation.getModelId());
		return mmvo;
	}

	/**
	 * 修改
	 * 
	 * @param vo
	 * @param modelIds
	 * @return
	 */
	@RequestMapping(value = "/update")
	public String update(@ModelAttribute MetaModelItemsVO vo, long modelIdsss) {
		MetaModelItemsVO mmiso = this.metaModelItemsServiceImpl.get(vo.getId());
		this.metaModelItemsServiceImpl.update("update", vo);
		// 查询对应的表
		// MetaModelVO vos=this.metaModelServiceImpl.get(vo.getModelId());
		// if(this.metaModelServiceImpl.tableIsExist(vos.getTableName())){
		// //1.判断是否有外键关系
		// if(checkIsInt.isInt(vo.getItemType())){
		// ModelRelationVO mro=new ModelRelationVO();
		// //查询对应的关系表
		// MetaModelVO mm=this.metaModelServiceImpl.get(modelIdsss);
		// //2.判断关系类型
		// if(vo.getItemType().equals("1")){
		// mro.setRelationName("一对一");
		// mro.setRelationType("OneToOne");
		// vo.setItemType("一对一");
		// }else{
		// mro.setRelationName("多对一");
		// mro.setRelationType("ManyToOne");
		// vo.setItemType("多对一");
		// }
		// mro.setItemId(vo.getId());
		// mro.setModelId(modelIdsss);
		// //判断是否已有外键关系
		// ModelRelationVO
		// mrvo=this.modelRelationServiceImpl.queryByItem(vo.getId());
		// try {
		// //存在
		// long id=mrvo.getId();
		// //修改关系表
		// mro.setId(mrvo.getId());
		// this.modelRelationServiceImpl.executeUpdate(mro, "update");
		// //删除外键
		// StringBuffer sbr=new StringBuffer();
		// sbr.append("alter table ");
		// sbr.append(vos.getTableName());
		// sbr.append(" drop foreign key fk_");
		// sbr.append(mmiso.getItemCode());
		// sbr.append("_");
		// sbr.append(vos.getTableName());
		// this.metaModelServiceImpl.excuteSql(sbr.toString());
		// //修改列
		// StringBuffer sbs=new StringBuffer("alter table
		// ").append(vos.getTableName()).append(" change
		// ").append(mmiso.getItemCode()).append(" ").append(vo.getItemCode());
		// //String sj="alter table "+vos.getTableName()+" change
		// "+mmiso.getItemCode()+" "+vo.getItemCode();
		// List<MetaModelItemsVO>
		// list=this.metaModelItemsServiceImpl.queryResult("queryResult",modelIdsss);
		// for(MetaModelItemsVO mmi:list){
		// if(mmi.getUsePrimaryKey()==1){
		// sbs.append(" ");
		// sbs.append(vo.getItemType());
		// //sj+=mmi.getItemType();
		// continue;
		// }
		// }
		// this.metaModelServiceImpl.excuteSql(sbs.toString());
		// //添加外键
		// StringBuffer sb=new StringBuffer();
		// sb.append("alter table ");
		// sb.append(vos.getTableName());
		// sb.append(" add constraint fk_");
		// sb.append(vo.getItemCode());
		// sb.append("_");
		// sb.append(vos.getTableName());
		// sb.append(" foreign key("+vo.getItemCode()+")");
		// sb.append(" REFERENCES ");
		// for(MetaModelItemsVO mmi:list){
		// if(mmi.getUsePrimaryKey()==1){
		// sb.append(mm.getTableName()+"("+mmi.getItemCode()+")");
		// continue;
		// }
		// }
		// this.metaModelServiceImpl.excuteSql(sb.toString());
		// } catch (Exception e) {//不存在
		// //添加外键关系
		// this.modelRelationServiceImpl.save(mro);
		// //修改列
		// StringBuffer sjs=new StringBuffer("alter table
		// ").append(vos.getTableName()).append(" change
		// ").append(mmiso.getItemCode()).append(" ").append(vo.getItemCode());
		// //String sj="alter table "+vos.getTableName()+" change
		// "+mmiso.getItemCode()+" "+vo.getItemCode();
		// List<MetaModelItemsVO>
		// list=this.metaModelItemsServiceImpl.queryResult("queryResult",modelIdsss);
		// for(MetaModelItemsVO mmi:list){
		// if(mmi.getUsePrimaryKey()==1){
		// sjs.append(" ");
		// sjs.append(mmi.getItemType());
		// //sj+=mmi.getItemType();
		// continue;
		// }
		// }
		// this.metaModelServiceImpl.excuteSql(sjs.toString());
		// //添加外键
		// StringBuffer sb=new StringBuffer();
		// sb.append("alter table ");
		// sb.append(vos.getTableName());
		// sb.append(" add constraint fk_");
		// sb.append(vo.getItemCode());
		// sb.append("_");
		// sb.append(vos.getTableName());
		// sb.append(" foreign key("+vo.getItemCode()+")");
		// sb.append(" REFERENCES ");
		// for(MetaModelItemsVO mmi:list){
		// if(mmi.getUsePrimaryKey()==1){
		// sb.append(mm.getTableName()+"("+mmi.getItemCode()+")");
		// continue;
		// }
		// }
		// this.metaModelServiceImpl.excuteSql(sb.toString());
		// }
		// }else{ //没有外键
		// //判断是否存在原有外键关系
		// ModelRelationVO
		// mrvo=this.modelRelationServiceImpl.queryByItem(vo.getId());
		// try{
		// //存在
		// long id=mrvo.getId();
		// //删除外键
		// StringBuffer sbr=new StringBuffer();
		// sbr.append("alter table ");
		// sbr.append(vos.getTableName());
		// sbr.append(" drop foreign key fk_");
		// sbr.append(mmiso.getItemCode());
		// sbr.append("_");
		// sbr.append(vos.getTableName());
		// this.metaModelServiceImpl.excuteSql(sbr.toString());
		// //删除关系表中的关系
		// ModelRelationVO
		// mj=this.modelRelationServiceImpl.queryByItem(vo.getId());
		// this.modelRelationServiceImpl.remove(mj);
		// //修改列
		// StringBuffer sjs=new StringBuffer("alter table
		// ").append(vos.getTableName()).append(" change
		// ").append(mmiso.getItemCode()).append(" ").append(vo.getItemCode());
		// if(vo.getItemType().equals("varchar") ||
		// vo.getItemType().equals("decimal")){
		// sjs.append(" ");
		// sjs.append(vo.getItemType());
		// sjs.append("(");
		// sjs.append(vo.getItemLength());
		// sjs.append(")");
		// }else{
		// sjs.append(" ");
		// sjs.append(vo.getItemType());
		// }
		// this.metaModelServiceImpl.excuteSql(sjs.toString());
		// }catch(Exception e){//不存在
		// //直接修改
		// StringBuffer sjs=new StringBuffer("alter table
		// ").append(vos.getTableName()).append(" change
		// ").append(mmiso.getItemCode()).append("
		// ").append(vo.getItemCode()).append(" ");
		// if(vo.getItemType().equals("varchar") ||
		// vo.getItemType().equals("decimal")){
		// sjs.append(vo.getItemType());
		// sjs.append("(");
		// sjs.append(vo.getItemLength());
		// sjs.append(")");
		// }else{
		// sjs.append(vo.getItemType());
		// }
		// logger.debug(sjs.toString());
		// this.metaModelServiceImpl.excuteSql(sjs.toString());
		// }
		// }
		// vo.setItemValid(1);
		// this.metaModelItemsServiceImpl.update("update",vo);
		// }
		// else{
		// //判断是否有外键
		// if(checkIsInt.isInt(vo.getItemType())){
		// ModelRelationVO mro=new ModelRelationVO();
		// //2.判断关系类型
		// if(vo.getItemType().equals("1")){
		// mro.setRelationName("一对一");
		// mro.setRelationType("OneToOne");
		// vo.setItemType("一对一");
		// }else{
		// mro.setRelationName("多对一");
		// mro.setRelationType("ManyToOne");
		// vo.setItemType("多对一");
		// }
		// mro.setItemId(vo.getId());
		// mro.setModelId(modelIdsss);
		// //判断是否已经存在关系
		// ModelRelationVO
		// mrvo=this.modelRelationServiceImpl.queryByItem(vo.getId());
		// try {
		// //存在
		// long id=mrvo.getId();
		// //修改关系
		// mro.setId(mrvo.getId());
		// this.modelRelationServiceImpl.executeUpdate(mro, "update");
		// vo.setItemValid(0);
		// this.metaModelItemsServiceImpl.update("update", vo);;
		// } catch (Exception e) {//不存在
		// this.modelRelationServiceImpl.save(mro);
		// vo.setItemValid(0);
		// this.metaModelItemsServiceImpl.update("update",vo);
		// }
		// }else{
		// //判断是否已经存在关系
		// ModelRelationVO
		// mrvo=this.modelRelationServiceImpl.queryByItem(vo.getId());
		// try {
		// //存在
		// long id=mrvo.getId();
		// //修改关系
		// this.modelRelationServiceImpl.remove(mrvo);
		// vo.setItemValid(0);
		// this.metaModelItemsServiceImpl.update("update", vo);
		// } catch (Exception e) {//不存在
		// vo.setItemValid(0);
		// this.metaModelItemsServiceImpl.update("update",vo);
		// }
		//
		// }
		// }

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
	public String addToggle(Model model, long modelId) {
		List<MetaModelVO> ms = new ArrayList<MetaModelVO>();
		BaseExample baseExample = new BaseExample();
		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			baseExample.createCriteria().andEqualTo("SYSTEM_ID", system.getSysId());
		}
		List<MetaModelVO> mm = this.metaModelServiceImpl.selectPagedByExample(baseExample, 1, 10, null);
		for (MetaModelVO m : mm) {
			if (this.metaModelServiceImpl.tableIsExist(m.getTableName())) {
				if (m.getId() != modelId) {
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
	public String updateToggle(Model model, long id) {
		MetaModelItemsVO vo = this.metaModelItemsServiceImpl.get(id);
		model.addAttribute("vo", vo);
		ModelRelationVO mro = this.modelRelationServiceImpl.queryByItem(id);
		if (mro != null) {
			model.addAttribute("mro", mro);
		}
		List<MetaModelVO> ms = new ArrayList<MetaModelVO>();

		BaseExample baseExample = new BaseExample();
		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
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
	public String queryResult(@RequestParam(value = "id") long modelId,
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
		if (itemName != null && !itemName.equals("")) {
			c.andLike("itemName", "%" + itemName + "%");
		}
		String itemCode = vo.getItemCode();
		if (itemCode != null && !itemCode.equals("")) {
			c.andLike("itemCode", "%" + itemCode + "%");
		}
		String itemType = vo.getItemType();
		if (itemType != null && !itemType.equals("")) {
			c.andLike("itemType", "%" + itemType + "%");
		}
		String defaultValue = vo.getDefaultValue();
		if (defaultValue != null && !defaultValue.equals("")) {
			c.andLike("defaultValue", "%" + defaultValue + "%");
		}
		long modelId = vo.getModelId();
		if (modelId != 0) {
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
	public String createTable(@RequestParam(value = "modelId") long idss, Model model) {
		MetaModelVO vo = this.metaModelServiceImpl.get(idss);
		// 判断表是否存在
		if (metaModelServiceImpl.tableIsExist(vo.getTableName())) {
			long id = vo.getId();
			List<MetaModelItemsVO> mmo = this.metaModelItemsServiceImpl.queryByState("queryByState", idss);
			// 去查找数据库是否有这一列
			for (MetaModelItemsVO mm : mmo) {
				if (!metaModelItemsServiceImpl.columnIsExist("columnIsExist", vo.getTableName(), mm.getItemName())) {
					// 需要判断是否有外键关系
					if (mm.getItemType().equals("多对一") || mm.getItemType().equals("一对一")) {
						StringBuffer sjs = new StringBuffer("alter table ").append(vo.getTableName())
								.append(" add constraint fk_").append(mm.getItemCode()).append("_")
								.append(vo.getTableName()).append(" foreign key(").append(mm.getItemCode())
								.append(") references ");
						// String sss="alter table "+vo.getTableName()+" add
						// constraint fk_"+mm.getItemCode()+" foreign
						// key("+mm.getItemCode()+") references ";
						String type = null;
						// 查询所对应的关系
						ModelRelationVO mr = this.modelRelationServiceImpl.queryByItem(mm.getId());
						// 查询所对应的表
						MetaModelVO mmmro = this.metaModelServiceImpl.get(mr.getModelId());
						sjs.append(mmmro.getTableName());
						// sss+=mmmro.getTableName();
						// 查询该表对应的主键列
						List<MetaModelItemsVO> list = this.metaModelItemsServiceImpl.queryResult("queryResult",
								mr.getModelId());
						for (MetaModelItemsVO mmi : list) {
							if (mmi.getUsePrimaryKey() == 1) {
								sjs.append("(");
								sjs.append(mmi.getItemCode());
								sjs.append(")");
								// sss+="("+mmi.getItemCode()+")";
								type = mmi.getItemType();
								continue;
							}
						}
						// 添加列
						StringBuffer str = new StringBuffer("alter table ").append(vo.getTableName())
								.append(" add column ").append(mm.getItemCode()).append(" ").append(type).append(";");
						// String strss="alter table "+vo.getTableName()+" add
						// column "+mm.getItemCode()+" "+type+";";
						sjs.append(";");
						// sss+=";";
						this.metaModelServiceImpl.excuteSql(str.toString());
						// 修改状态
						mm.setItemValid(1);
						this.metaModelItemsServiceImpl.update("updateSelective", mm);
						this.metaModelServiceImpl.excuteSql(sjs.toString());
					} else {
						String str = UpdateColumn.newAddColumn(mm, vo.getTableName());
						// 修改状态
						mm.setItemValid(1);
						this.metaModelItemsServiceImpl.update("updateSelective", mm);
						metaModelServiceImpl.excuteSql(str);
						model.addAttribute("msg", 1);
					}
				}
			}
			vo.setModelSynchronization(true);
			this.metaModelServiceImpl.update("update", vo);
			return "redirect:../metaModel/queryResult.do?currentPage=" + 1;
		} else {
			try {
				List<MetaModelItemsVO> pl = this.metaModelItemsServiceImpl.queryResult("queryResult", vo.getId());
				String str = createTables.getSql(vo, pl);
				boolean b = metaModelServiceImpl.excuteSql(str);
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
	public String release(@RequestParam(value = "modelId") long idss, Model model,
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
	public String dataValidate(String itemCode, long modelId,
			@RequestParam(required = false, defaultValue = "0") long id) {
		// 判断是增加时验证，还是修改时验证
		if (id == 0) {
			List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryColumn("queryColumn", modelId, itemCode);
			int count = ls.size();
			String str = "{id:" + count + "}";
			return str;
		} else {

			MetaModelItemsVO mmi = this.metaModelItemsServiceImpl.get(id);

			List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryColumn("queryColumn", modelId, itemCode);
			for (int i = 0; i < ls.size(); i++) {
				if (ls.get(i).equals(mmi.getItemCode())) {
					ls.remove(i);
				}
			}
			int count = ls.size();
			String str = "{id:" + count + "}";
			return str;
		}

	}

}
