package cn.org.awcp.metadesigner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.org.awcp.unit.service.PunSystemService;
import cn.org.awcp.unit.vo.PunSystemVO;

/**
 * 项目管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/projectManage")
public class ProjectManageController {
	
	@Autowired
	@Qualifier("punSystemServiceImpl")
	PunSystemService  sysService;
	
	/**
	 * 增加
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(value="/save")
//	public String save(@ModelAttribute ProjectManageVO vo){
//		long id=this.projectManageServiceImpl.save(vo);
//		return String.valueOf(id);
//	}
//	
//	@RequestMapping(value="/remove")
//	public String remove(long id){
//		ProjectManageVO pmvo=this.projectManageServiceImpl.get(id);
//		this.projectManageServiceImpl.remove(pmvo);
//		return "";
//	}
	
	@ResponseBody
	@RequestMapping(value="/findAll")
	public List<PunSystemVO> findAll(){
		List<PunSystemVO> ls=sysService.findAll();
		return ls;
	}
	
}
