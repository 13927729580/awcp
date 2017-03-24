package org.szcloud.framework.formdesigner.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.base.BaseController;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.domain.Criteria;
import org.szcloud.framework.core.utils.Tools;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.formdesigner.application.service.StoreService;
import org.szcloud.framework.formdesigner.application.vo.StoreVO;
import org.szcloud.framework.formdesigner.application.vo.StyleVO;
import org.szcloud.framework.unit.vo.PunSystemVO;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Controller
@RequestMapping("/func")
public class FunController extends BaseController{
	@Autowired
	@Qualifier("storeServiceImpl")
	private StoreService storeService;
	
	
	/**
	 * 根据code、name、description字段进行查询分页显示列表
	 * 
	 * @param styleCode 
	 * @param name
	 * @param description
	 * @param currentPage
	 * @return
	 */
	@RequestMapping(value="/list")
	public ModelAndView listPageStyles(@RequestParam(value="name",required=false) String name,
			@RequestParam(value="currentPage",required=false,defaultValue="1") int currentPage,
			@RequestParam(value="pageSize",required=false,defaultValue="3") int pageSize){
		BaseExample baseExample = new BaseExample();
		if (pageSize < 10) {
			pageSize = 10;
		}
		if (currentPage <= 0) {
			currentPage = 1;
		}
		baseExample.createCriteria().andLike("code", StoreService.FUNC_CODE + "%");
		if(StringUtils.isNoneBlank(name)){
			baseExample.getOredCriteria().get(0).andLike("name", "%" + name + "%");
		}
		PageList<StoreVO> list = storeService.selectPagedByExample(baseExample, currentPage, pageSize, "code desc");
		
		for(StoreVO vo : list){
			String content = vo.getContent();
			vo.setContent(JSON.parseObject(content, StyleVO.class).getScript());
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("vos", list);
		mv.addObject("currentPage", currentPage);
		mv.addObject("pageSize", pageSize);
		mv.addObject("name", name);
		mv.setViewName("formdesigner/page/func/func-list");
		return mv;
	}
	
	/**
	 * 返回单个style对象json格式字符串
	 * 
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(value="/getByAjax")
//	public String getStyleByAjax(String id){
//		
//		StoreVO vo = storeService.findById(id);
//		return vo.getContent();
//	}
//	/**
//	 * 返回所有style对象json格式字符串
//	 * 
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/getAllByAjax")
//	public List<StyleVO> getStylesByAjax(){
//		BaseExample baseExample = new BaseExample();
//		baseExample.createCriteria().andLike("code", StoreService.STYLE_CODE + "%");
//		List<StoreVO> list = storeService.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, "code desc");
//		List<StyleVO> styles =new ArrayList<StyleVO>();
//		for(StoreVO vo:list){
//			StyleVO style=JSON.parseObject(vo.getContent(),StyleVO.class);
//			styles.add(style);
//		}
//		list.clear();
//		return styles;
//	}
	
	/**
	 * 跳转到编辑页面，编辑函数库信息
	 * 1.点击某个函数库进行编辑
	 * 2.保存时校验失败，跳转到编辑页面，带有错误信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit")
	public ModelAndView editPageStyle(@RequestParam(value="_selects", required=false) String id){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isNoneBlank(id)){
			StoreVO store = storeService.findById(id);
			StyleVO vo = JSON.parseObject(store.getContent(), StyleVO.class);
			mv.addObject("vo", vo);
		}
		mv.setViewName("formdesigner/page/func/func-edit");//JSP页面
		return mv;
	}
	
	
	/**
	 * 保存函数库
	 * 	保存成功跳转到list页面
	 * @return
	 */
	@RequestMapping(value="/save")
	public ModelAndView savePageStyle(StyleVO vo){
		ModelAndView mv = new ModelAndView();
		if(validate(mv, vo)){
			Object obj=Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			if(obj instanceof PunSystemVO){
				PunSystemVO system = (PunSystemVO)obj;
				vo.setSystemId(system.getSysId());
			}
			StoreVO store = convert(vo);
			storeService.save(store);
			mv.setViewName("redirect:/func/list.do");
		} else {
			mv.addObject("vo", vo);
			mv.setViewName("formdesigner/page/func/func-edit");//编辑页面
		}
		return mv;
	}
	
//	@ResponseBody
//	@RequestMapping(value="/saveByAjax")
//	public StyleVO saveByAjax(StyleVO vo){
//		Object obj=Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
//		if(obj instanceof PunSystemVO){
//			PunSystemVO system = (PunSystemVO)obj;
//			vo.setSystemId(system.getSysId());
//		}
//		StoreVO store = convert(vo);
//		String id = storeService.save(store);
//		vo.setPageId(id);
//		return vo;
//		
//	}
	
	
	/**
	 * 删除选中的页面动作数据,更改数据状态
	 * @param selects
	 * @return
	 */
	@RequestMapping(value="delete")
	public ModelAndView deletePageStyle(@RequestParam(value="_selects") String selects){
		ModelAndView mv = new ModelAndView();
		String[] ids = selects.split(",");
		if(storeService.delete(ids)){
			addMessage(mv, "删除成功");
		}
		mv.setViewName("redirect:/func/list.do");
		return mv;
	}
	
	
	/**
	 * 校验
	 * @param mv
	 * @param vo
	 * @return
	 */
	private boolean validate(ModelAndView mv, StyleVO vo){
		//
		if(StringUtils.isEmpty(vo.getName())){
			addMessage(mv, " 名称必填 ");
			return false;
		}
		if(StringUtils.isNotBlank(vo.getPageId())) {
			//同一个系统下面不允许有同名的函数库名称
			BaseExample baseExample = new BaseExample();
			Criteria criteria = baseExample.createCriteria();
			Object obj=Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			if(obj instanceof PunSystemVO){
				PunSystemVO system = (PunSystemVO)obj;
				vo.setSystemId(system.getSysId());
			}
			criteria.andEqualTo("system_id", vo.getSystemId()).andEqualTo("name", vo.getName()).andLike("code", StoreService.FUNC_CODE + "%");
			List<StoreVO> vos = storeService.selectPagedByExample(baseExample, 1, 3, null);
			if(vos != null) {
				for(StoreVO s : vos) {
					if(!s.getId().equalsIgnoreCase(vo.getPageId())) {
						addMessage(mv, "名称不可重复");
						return false;
					}
				}
			}
		}
		if(StringUtils.isEmpty(vo.getScript())){
			addMessage(mv, "脚本必填");
			return false;
		}
		return true;
		}

	/**
	 * 把StyleVO 转换成StoreVO
	 * @Title: convert 
	 * @Description: TODO 
	 * @param vo
	 * @return StoreVO    
	 * @throws
	 */
	private StoreVO convert(StyleVO vo){
		StoreVO store = new StoreVO();
		if(StringUtils.isBlank(vo.getCode())){
			vo.setCode(StoreService.FUNC_CODE+System.currentTimeMillis());
		}
		store.setCode(vo.getCode());
		store.setName(vo.getName());
		store.setContent(JSON.toJSONString(vo));
		store.setId(vo.getPageId());
		store.setDescription(vo.getDescription());
		store.setSystemId(vo.getSystemId());
		return store;
	}	
}
