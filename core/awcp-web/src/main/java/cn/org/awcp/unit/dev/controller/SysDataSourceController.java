package cn.org.awcp.unit.dev.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.metadesigner.application.DataSourceManageService;
import cn.org.awcp.metadesigner.application.SysSourceRelationService;
import cn.org.awcp.metadesigner.vo.DataSourceManageVO;
import cn.org.awcp.metadesigner.vo.SysDataSourceVO;
import cn.org.awcp.unit.service.PunSystemService;
import cn.org.awcp.unit.vo.PunSystemVO;

@Controller
@RequestMapping("/dataSys")
public class SysDataSourceController {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(SysDataSourceController.class);
	@Autowired
	@Qualifier("dataSourceManageServiceImpl")
	DataSourceManageService dataSourceService;
	@Autowired
	@Qualifier("sysSourceRelationServiceImpl")
	SysSourceRelationService sysSourceRelationService;
	@Autowired
	@Qualifier("punSystemServiceImpl")
	PunSystemService sysService;

	@RequestMapping("/config")
	public ModelAndView getConfigure(Long id, @RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/dev/sys-datasource-list");
		if (pageSize < 10) {
			pageSize = 10;
		}
		if (currentPage <= 0) {
			currentPage = 1;
		}
		// 查找系统
		PunSystemVO system = sysService.findById(id);
		mv.addObject("sys", system);
		// 查找系统关联id
		BaseExample baseExample = new BaseExample();
		baseExample.createCriteria().andEqualTo("SYSTEM_ID", id);
		PageList<SysDataSourceVO> relations = sysSourceRelationService.selectPagedByExample(baseExample, currentPage,
				pageSize, null);
		// 查找数据源
		List<DataSourceManageVO> temp = new ArrayList<DataSourceManageVO>();
		if (relations != null && relations.size() > 0) {
			for (SysDataSourceVO relation : relations) {
				String dataId = relation.getDataSourceId();
				DataSourceManageVO vo = dataSourceService.get(dataId);
				temp.add(vo);
			}
		}
		PageList<DataSourceManageVO> retObj = new PageList<DataSourceManageVO>(temp, relations.getPaginator());
		mv.addObject("vos", retObj);
		return mv;

	}

	@RequestMapping("/removeRelate")
	public ModelAndView removeRelate(@RequestParam(value = "_selects", required = false) Long dataSourceId,
			Long systemId) {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("redirect:/dataSys/config.do?id=" + systemId);
		BaseExample base = new BaseExample();
		base.createCriteria().andEqualTo("SYSTEM_ID", systemId).andEqualTo("DATASOURCE_ID", dataSourceId);
		List<SysDataSourceVO> vos = sysSourceRelationService.selectPagedByExample(base, 1, 10, null);
		if (vos != null && vos.size() > 0) {
			for (SysDataSourceVO vo : vos) {
				sysSourceRelationService.remove(vo);
			}
		}
		return mv;

	}

	@ResponseBody
	@RequestMapping("/relate")
	public String relate(SysDataSourceVO vo) {
		sysSourceRelationService.saveOrUpdate(vo);
		return "1";

	}

	@RequestMapping("/toRelate")
	public ModelAndView toRelate(@RequestParam(value = "_selects", required = false) Long sysId) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/dev/sys-relate-data1");
		logger.debug("viewName:" + mv.getViewName());
		// 查找系统
		PunSystemVO system = sysService.findById(sysId);
		mv.addObject("sys", system);
		// 找所有数据源
		List<DataSourceManageVO> dataVOs = dataSourceService.findAll();
		mv.addObject("dataVOs", dataVOs);

		return mv;

	}
}
