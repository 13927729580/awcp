package org.szcloud.framework.formdesigner.controller;

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
import org.szcloud.framework.base.BaseController;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.utils.Tools;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.formdesigner.application.service.StoreService;
import org.szcloud.framework.formdesigner.application.vo.CompareValidatorVO;
import org.szcloud.framework.formdesigner.application.vo.CustomValidatorVO;
import org.szcloud.framework.formdesigner.application.vo.InputValidatorVO;
import org.szcloud.framework.formdesigner.application.vo.RegexValidatorVO;
import org.szcloud.framework.formdesigner.application.vo.StoreVO;
import org.szcloud.framework.formdesigner.application.vo.ValidatorVO;
import org.szcloud.framework.unit.vo.PunSystemVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;


@Controller
@RequestMapping("/fd/validator")
public class ValidatorController extends BaseController{
	
	@Autowired
	@Qualifier("storeServiceImpl")
	private StoreService storeService;
	
	
	/**
	 * 根据code、name、description字段进行查询分页显示列表
	 * 
	 * @param validatorCode 
	 * @param name
	 * @param description
	 * @param currentPage
	 * @return
	 */
	@RequestMapping(value="/list")
	public ModelAndView listValidators(@RequestParam(value="name",required=false) String name,
			@RequestParam(value="validatorDes",required=false) String description,
			@RequestParam(value="isSelect",required=false) boolean isSelect,
			@RequestParam(value="currentPage",required=false,defaultValue="1") int currentPage,
			@RequestParam(value="pageSize",required=false,defaultValue="2") int pageSize){
		if (pageSize < 10) {
			pageSize = 10;
		}
		if(isSelect){
			pageSize = 30;
		}
		if (currentPage <= 0) {
			currentPage = 1;
		}
		BaseExample baseExample = new BaseExample();
		baseExample.createCriteria().andLike("code", StoreService.VALIDATOR_CODE + "%");
		if(StringUtils.isNoneBlank(name)){
			baseExample.getOredCriteria().get(0).andLike("name", "%" + name + "%");
		}
		if(StringUtils.isNoneBlank(description)){
			baseExample.getOredCriteria().get(0).andLike("description", "%" + description + "%");
		}
		PageList<StoreVO> list = storeService.selectPagedByExample(baseExample, currentPage, pageSize, "code desc");
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("vos", list);
		mv.addObject("currentPage", currentPage);
		mv.addObject("pageSize", pageSize);
		mv.addObject("name", name);
		mv.addObject("validatorDes", description);
		mv.addObject("isSelect", isSelect);
		mv.setViewName("formdesigner/page/validator/validator-list");
		return mv;
	}
	
	/**
	 * 返回单个validator对象json格式字符串
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getByAjax")
	public String getValidatorByAjax(String id){
		StoreVO vo = storeService.findById(id);
		return vo.getContent();
	}
	
	/**
	 * 返回多个对象json格式字符串
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getResultsByAjax")
	public String getValidatorsByAjax(String ids){
		StringBuilder rtn = new StringBuilder("[");
		if(StringUtils.isNotBlank(ids)){
			ids=StringEscapeUtils.unescapeHtml4(ids);
			String[] idArray = ids.split("\\,");
			List<StoreVO> list = storeService.findByIds(idArray);
			for(int i = 0; i < idArray.length; i++){
				String id = idArray[i];
				for(StoreVO vo : list){
					if(id.equalsIgnoreCase(vo.getId())){
						rtn.append(vo.getContent());
						rtn.append(",");
						list.remove(vo);
						break;
					}
				}
			}
			rtn.deleteCharAt(rtn.length() - 1);
		}
		rtn.append("]");
		return rtn.toString();
	}
	
	/**
	 * 跳转到编辑页面，编辑校验信息
	 * 1.点击某个校验进行编辑
	 * 2.保存时校验失败，跳转到编辑页面，带有错误信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit")
	public ModelAndView editValidator(@RequestParam(value="_selects", required=false) String id,
			@RequestParam(value="isSelect",required=false) boolean isSelect,
			@RequestParam(value="type", required=false) String type){
		ModelAndView mv = new ModelAndView();
		mv.addObject("isSelect", isSelect);
		if(StringUtils.isBlank(type)&&StringUtils.isBlank(id)){
			addMessage(mv, "请选择类型");
			mv.setViewName("formdesigner/page/validator/list.do");//JSP页面
			return mv;
		}
		StoreVO store  = new StoreVO();
		if(StringUtils.isNotBlank(id)){
			store = storeService.findById(id);
			if(StringUtils.isBlank(type)){
				JSONObject o =JSON.parseObject(store.getContent());
				type= (String) o.get("validatorType");
			}
		}
		JSONObject vo = JSON.parseObject(store.getContent());
		mv.addObject("vo", vo);
		if("1".equals(type)){
			mv.setViewName("formdesigner/page/validator/inputValidator-edit");//JSP页面
		}else if("2".equals(type)){
			mv.setViewName("formdesigner/page/validator/regexValidator-edit");//JSP页面
		}else if("3".equals(type)){
			mv.setViewName("formdesigner/page/validator/compareValidator-edit");//JSP页面
		}else if("4".equals(type)){
			mv.setViewName("formdesigner/page/validator/customValidator-edit");//JSP页面
		}
		return mv;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getValidatorList")
	public List<StoreVO> getValidatorList(
			
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "1000") int pageSize,
			@RequestParam(required = false, defaultValue = " T_ORDER ASC") String sortString) {
		BaseExample baseExample = new BaseExample();
		baseExample.createCriteria()
				//.andEqualTo("DYNAMICPAGE_ID", dynamicPageId)
				.andLike("CODE", StoreService.VALIDATOR_CODE + "%");
		return storeService.selectPagedByExample(baseExample, currentPage,
				pageSize, sortString);
	}
	
	
	/**
	 * 保存校验
	 * 	保存成功跳转到list页面
	 * @return
	 */
	@RequestMapping(value="/saveInputValidator")
	public ModelAndView saveInputValidator(InputValidatorVO vo,boolean isSelect){
		ModelAndView mv = new ModelAndView();
		mv.addObject("isSelect", isSelect);
		if(validateInput(mv, vo)){
			Object obj=Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			if(obj instanceof PunSystemVO){
				PunSystemVO system = (PunSystemVO)obj;
				vo.setSystemId(system.getSysId());
			}
			StoreVO store = convert(vo);
			storeService.save(store);
			mv.setViewName("redirect:/fd/validator/list.do");
		} else {
			mv.addObject("vo", vo);
			mv.addObject("type", "1");
			mv.setViewName("redirect:/fd/validator/edit.do");//编辑页面
		}
		return mv;
	}
	/**
	 * 保存校验
	 * 	保存成功跳转到list页面
	 * @return
	 */
	@RequestMapping(value="/saveRegexValidator")
	public ModelAndView saveRegexValidator(RegexValidatorVO vo,boolean isSelect){
		ModelAndView mv = new ModelAndView();
		mv.addObject("isSelect", isSelect);
		if(validateRegex(mv, vo)){
			Object obj=Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			if(obj instanceof PunSystemVO){
				PunSystemVO system = (PunSystemVO)obj;
				vo.setSystemId(system.getSysId());
			}
			StoreVO store = convert(vo);
			storeService.save(store);
			mv.setViewName("redirect:/fd/validator/list.do");
		} else {
			mv.addObject("vo", vo);
			mv.addObject("type", "2");
			mv.setViewName("redirect:/fd/validator/edit.do");//编辑页面
		}
		return mv;
	}
	/**
	 * 保存校验
	 * 	保存成功跳转到list页面
	 * @return
	 */
	@RequestMapping(value="/saveCompareValidator")
	public ModelAndView saveCompareValidator(CompareValidatorVO vo,boolean isSelect){
		ModelAndView mv = new ModelAndView();
		mv.addObject("isSelect", isSelect);
		if(validateCompare(mv, vo)){
			Object obj=Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			if(obj instanceof PunSystemVO){
				PunSystemVO system = (PunSystemVO)obj;
				vo.setSystemId(system.getSysId());
			}
			StoreVO store = convert(vo);
			storeService.save(store);
			mv.setViewName("redirect:/fd/validator/list.do");
		} else {
			mv.addObject("vo", vo);
			mv.addObject("type", "3");
			mv.setViewName("redirect:/fd/validator/edit.do");//编辑页面
		}
		return mv;
	}
	/**
	 * 保存校验
	 * 	保存成功跳转到list页面
	 * @return
	 */
	@RequestMapping(value="/saveCustomValidator")
	public ModelAndView CustomValidator(CustomValidatorVO vo,boolean isSelect){
		ModelAndView mv = new ModelAndView();
		mv.addObject("isSelect", isSelect);
		if(validateCustom(mv, vo)){
			Object obj=Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			if(obj instanceof PunSystemVO){
				PunSystemVO system = (PunSystemVO)obj;
				vo.setSystemId(system.getSysId());
			}
			StoreVO store = convert(vo);
			storeService.save(store);
			mv.setViewName("redirect:/fd/validator/list.do");
		} else {
			mv.addObject("vo", vo);
			mv.addObject("type", "4");
			mv.setViewName("redirect:/fd/validator/edit.do");//编辑页面
		}
		return mv;
	}
	
	/**
	 * 删除选中的校验数据,更改数据状态
	 * @param selects
	 * @return
	 */
	@RequestMapping(value="delete")
	public ModelAndView deleteValidator(@RequestParam(value="_selects") String selects,boolean isSelect){
		ModelAndView mv = new ModelAndView();
		String[] ids = selects.split(",");
		if(storeService.delete(ids)){
			addMessage(mv, "删除成功");
		}
		mv.addObject("isSelect", isSelect);
		mv.setViewName("redirect:/fd/validator/list.do");
		return mv;
	}
	
	/**
	 * 彻底删除选中的校验数据
	 * 
	 * @param selects
	 * @return
	 */
	@RequestMapping(value="/remove")
	public ModelAndView removeValidator(@RequestParam(value="_selects") String selects,boolean isSelect){
		//TODO
		ModelAndView mv = new ModelAndView();
		//从session中获取当前页？
		mv.setViewName("redirect:/fd/validator/list.do");
		return mv;
	}
	
	
	/**
	 * 校验
	 * @param mv
	 * @param vo
	 * @return
	 */
	private boolean validateInput(ModelAndView mv, InputValidatorVO vo){		
		if(StringUtils.isEmpty(vo.getName())){
			addMessage(mv, " 名称必填 ");
			return false;
		}
		if(StringUtils.isEmpty(vo.getDescription())){
			addMessage(mv, "描述必填");
			return false;
		}
		if(StringUtils.isEmpty(vo.getMax())){
			addMessage(mv, "最大值必填");
			return false;
		}
		if(StringUtils.isEmpty(vo.getMin())){
			addMessage(mv, "最小值必填");
			return false;
		}
		
		return true;
	}
	
	private boolean validateRegex(ModelAndView mv, RegexValidatorVO vo){		
		if(StringUtils.isEmpty(vo.getName())){
			addMessage(mv, " 名称必填 ");
			return false;
		}
		if(StringUtils.isEmpty(vo.getDescription())){
			addMessage(mv, "描述必填");
			return false;
		}
		if(StringUtils.isEmpty(vo.getRegExp())){
			addMessage(mv, "表达式必填");
			return false;
		}
		if(StringUtils.isEmpty(vo.getOnError())){
			addMessage(mv, "错误提示必填");
			return false;
		}
		return true;
	}
	private boolean validateCompare(ModelAndView mv, CompareValidatorVO vo){		
		if(StringUtils.isEmpty(vo.getName())){
			addMessage(mv, " 名称必填 ");
			return false;
		}
		if(StringUtils.isEmpty(vo.getDescription())){
			addMessage(mv, "描述必填");
			return false;
		}
		if(StringUtils.isEmpty(vo.getDesID())){
			addMessage(mv, "组件id必填");
			return false;
		}
		if(StringUtils.isEmpty(vo.getOperateor())){
			addMessage(mv, "比较符号必填");
			return false;
		}
		return true;
	}
	private boolean validateCustom(ModelAndView mv, CustomValidatorVO vo){		
		if(StringUtils.isEmpty(vo.getName())){
			addMessage(mv, " 名称必填 ");
			return false;
		}
		if(StringUtils.isEmpty(vo.getDescription())){
			addMessage(mv, "描述必填");
			return false;
		}
		return true;
	}
	
	private StoreVO convert(ValidatorVO vo){
		StoreVO store = new StoreVO();
		if(StringUtils.isBlank(vo.getCode()))
		{
			vo.setCode(StoreService.VALIDATOR_CODE+System.currentTimeMillis());
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
