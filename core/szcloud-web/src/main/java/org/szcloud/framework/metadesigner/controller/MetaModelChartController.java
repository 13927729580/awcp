package org.szcloud.framework.metadesigner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.szcloud.framework.metadesigner.application.MetaModelChartService;
import org.szcloud.framework.metadesigner.vo.MetaModelChartVO;
import org.szcloud.framework.metadesigner.vo.MetaModelVO;

@Controller
@RequestMapping(value="/metaModelChart")
public class MetaModelChartController {
	
	@Autowired
	private MetaModelChartService metaModelChartServiceImpl;
	
	/**
	 * 创建一个图表
	 * 
	 * @param classId
	 * @param chartName
	 * @return	
	 */
	
	@ResponseBody
	@RequestMapping(value="/create")
	public String create(long classId,String chartName,@ModelAttribute MetaModelVO vo){
		MetaModelChartVO mmc=new MetaModelChartVO();
		vo.setModelClassId(classId);
		vo.setModelSynchronization(false);
		mmc.setClassId(classId);
		mmc.setChartName(chartName);
		long id=this.metaModelChartServiceImpl.save(mmc);
		return String.valueOf(id);
	}
	
	
	
	/**
	 * 根据条件查询图表
	 * 点击模型分类时触发
	 * @param classId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryByClassId")
	public List<MetaModelChartVO> queryByClassId(long projectId){
		try {
			List<MetaModelChartVO> ls=this.metaModelChartServiceImpl.queryByClassId(projectId);
			return ls;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
}
