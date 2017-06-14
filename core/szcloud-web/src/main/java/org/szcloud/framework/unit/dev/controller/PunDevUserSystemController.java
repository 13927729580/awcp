package org.szcloud.framework.unit.dev.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.domain.Criteria;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.metadesigner.application.DataSourceManageService;
import org.szcloud.framework.metadesigner.vo.DataSourceManageVO;
import org.szcloud.framework.unit.core.domain.SysDataSource;
import org.szcloud.framework.unit.service.PunMenuService;
import org.szcloud.framework.unit.service.PunRoleAccessService;
import org.szcloud.framework.unit.service.PunRoleInfoService;
import org.szcloud.framework.unit.service.PunSystemService;
import org.szcloud.framework.unit.service.SysSourceRelationService;
import org.szcloud.framework.unit.utils.JsonFactory;
import org.szcloud.framework.unit.utils.PermissionException;
import org.szcloud.framework.unit.utils.ResourceTreeUtils;
import org.szcloud.framework.unit.vo.PunGroupVO;
import org.szcloud.framework.unit.vo.PunMenuVO;
import org.szcloud.framework.unit.vo.PunResourceTreeNode;
import org.szcloud.framework.unit.vo.PunResourceVO;
import org.szcloud.framework.unit.vo.PunSystemVO;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.unit.vo.SysDataSourceVO;
import org.szcloud.framework.venson.controller.base.ControllerHelper;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Controller
@RequestMapping("/dev")
public class PunDevUserSystemController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	public static final String SYS_STATUS_OFFLINE = "0";
	public static final String SYS_STATUS_ONLINE = "1";
	@Autowired
	@Qualifier("punSystemServiceImpl")
	PunSystemService sysService;
	@Autowired
	@Qualifier("punRoleAccessServiceImpl")
	PunRoleAccessService roleAccService;
	@Autowired
	@Qualifier("punRoleInfoServiceImpl")
	PunRoleInfoService roleService;
	@Autowired
	@Qualifier("punMenuServiceImpl")
	PunMenuService menuService;
	@Autowired
	@Qualifier("dataSourceManageServiceImpl")
	DataSourceManageService dataSourceService;
	@Autowired
	@Qualifier("sysSourceRelationServiceImpl")
	SysSourceRelationService sysSourceRelationService;
	private StringBuffer valMessage = null;// 校验信息

	/**
	 * ljw 2014-12-10 进入应用中心
	 * 
	 * @return
	 */
	@RequestMapping(value = "/intoSystemCenter")
	public ModelAndView intoSystemCenter(Long[] boxs, RedirectAttributes rAttributes) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/dev/system-center");
		try {
			PunSystemVO vo = sysService.findById(boxs[0]);
			// TODO 暂时不作限制，发布状态的系统不能进行修改
			/*
			 * if(vo.getSysStatus().equals(PunSysStatusConstants.
			 * SYS_STATUS_ONLINE)){ rAttributes.addFlashAttribute("result",
			 * "已发布系统不能修改！"); mv.setViewName("redirect:punSystemList.do");
			 * return mv; }
			 */
			SessionUtils.addObjectToSession(SessionContants.TARGET_SYSTEM, vo);
			mv.addObject("vo", vo);
			mv.addObject("sysId", vo.getSysId());
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "异常");
		}
		return mv;
	}

	/**
	 * 
	 * @Title: intoPunSystem @Description: 进入组编辑页面 @param @return @return
	 *         ModelAndView @throws
	 */
	@RequestMapping(value = "/intoPunSystem")
	public ModelAndView intoPunSystem() {
		ModelAndView mv = new ModelAndView("/unit/dev/punDevSystem-edit");
		List<DataSourceManageVO> dataVOs = dataSourceService.findAll();
		mv.addObject("ss", dataVOs);
		return mv;
	}

	/**
	 * 保存
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/punSystemSave")
	public ModelAndView punSystemSave(PunSystemVO vo, Long defaultDataSource, Model model, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		try {
			/*
			 * if(!BackEndUserUtils.isBackEndRole()){//判断是否为可操作改功能的角色 throw new
			 * PermissionException("您无权限进行该操作！"); }
			 */
			boolean mark = false;// 用于标识是否记录
			if (validate(vo)) {
				if (null == vo.getSysId()) {
					PunUserBaseInfoVO user = ControllerHelper.getUser();
					vo.setSysCreater(user.getUserId().toString());// 设置系统创建者

					vo.setSysCreateTime(new Date());// 设置创建时间
					PunGroupVO group = (PunGroupVO) SessionUtils
							.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
					vo.setSysCreateGroup(group.getGroupId().toString());
					vo.setGroupId(group.getGroupId());
					vo.setSysStatus(SYS_STATUS_OFFLINE);// 未发布
				} else {
					mark = true;
				}
				sysService.addOrUpdate(vo);
				// TODO 2017/02/22 modify by venson
				SysDataSource dataSource = new SysDataSource();
				dataSource.setIsDefault(true);
				dataSource.setSystemId(vo.getSysId());
				dataSource.setDataSourceId(defaultDataSource);
				dataSource.save();
				if (mark) {// 编辑时，返回编辑页面
					ra.addFlashAttribute("result", "系统'" + vo.getSysName() + "'更新成功！");
					mv.setViewName("redirect:punSystemGet.do?boxs=" + vo.getSysId());
				} else {// 新增时返回列表页面
					ra.addFlashAttribute("result", "系统'" + vo.getSysName() + "'新增功！");
					mv.setViewName("redirect:punSystemList.do");
				}
				return mv;
			} else {
				model.addAttribute("vo", vo);
				model.addAttribute("result", "校验失败" + valMessage.toString() + "。");
			}
		} catch (PermissionException ea) {
			model.addAttribute("result", ea.getMessage());
		} catch (Exception e) {
			logger.info("ERROR", e);
			model.addAttribute("result", "系统异常");
		}
		mv.setViewName("unit/dev/punDevSystem-edit");
		return mv;
	}

	/**
	 * 根据ID查找
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "punSystemGet")
	public ModelAndView punSystemGet(Long[] boxs) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/dev/punDevSystem-edit");
		try {
			PunSystemVO vo = sysService.findById(boxs[0]);
			List<SysDataSourceVO> sysDataRela = sysService.getSystemDataSource(vo.getSysId());
			List<DataSourceManageVO> dataS = new ArrayList<DataSourceManageVO>();
			if (sysDataRela != null && sysDataRela.size() > 0) {
				for (SysDataSourceVO dataVo : sysDataRela) {
					Long sourceId = dataVo.getDataSourceId();
					DataSourceManageVO datasource = dataSourceService.get(sourceId);
					dataS.add(datasource);
					if (dataVo.getIsDefault()) {
						mv.addObject("source", datasource);
					}
				}
				mv.addObject("ss", dataS);
			}
			mv.addObject("vo", vo);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "异常");
		}
		return mv;
	}

	/**
	 * 根据SystemId返回系统的菜单树(json)
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "punSysMenuEdit", method = RequestMethod.GET)
	public ModelAndView punSysMenuEdit(@ModelAttribute("sysVO") PunSystemVO sysVO) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/dev/punSysMenu-edit");
		try {
			List<PunMenuVO> resoVOs = menuService.getPunMenuBySys(sysVO);
			ResourceTreeUtils rtu = new ResourceTreeUtils();
			List<PunMenuVO[]> list = rtu.generateTreeView(resoVOs);
			Map<Integer, List<PunResourceTreeNode>> resultMap = new HashMap<Integer, List<PunResourceTreeNode>>();
			Integer index = 1;
			for (PunMenuVO[] prvo : list) {
				// 以目录根节点的index为key，以manyNodeTree为value
				List<PunResourceTreeNode> manyNodeTree = ResourceTreeUtils.getPlainZNodes(prvo);
				resultMap.put(index, manyNodeTree);
				index++;
			}
			JsonFactory factory = new JsonFactory();
			mv.addObject("menuJson", factory.encode2Json(resultMap));
			mv.addObject("sysVO", sysVO);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "异常");
		}
		return mv;
	}

	@RequestMapping(value = "intoPunSystemList", method = RequestMethod.GET)
	public ModelAndView intoPunSystemList() {
		// if(!BackEndUserUtils.isBackEndRole()){//判断是否为可操作改功能的角色
		// return null;
		// }
		return new ModelAndView("redirect:/dev/punSystemList.do?currentPage=0");
	}

	/**
	 * 
	 * @Title: punSystemList @Description: 分页查询并以列表的形式显示 @param @return @return
	 *         ModelAndView @throws
	 */
	@RequestMapping(value = "punSystemList", method = RequestMethod.GET)
	public ModelAndView punSystemList(@ModelAttribute PunSystemVO vo, Model model,
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		try {
			if (currentPage <= 0)
				currentPage = 1;
			if (pageSize <= 5)
				pageSize = 10;
			BaseExample example = new BaseExample();
			Criteria criteria = example.createCriteria();
			if (StringUtils.isNotBlank(vo.getSysName())) {
				criteria.andLike("SYS_NAME", "%" + vo.getSysName() + "%");
			}
			if (StringUtils.isNotBlank(vo.getSysShortName())) {
				criteria.andLike("SYS_SHORT_NAME", "%" + vo.getSysShortName() + "%");
			}
			PunGroupVO group = (PunGroupVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
			criteria.andEqualTo("GROUP_ID", group.getGroupId());
			String sortString = "SYS_ID desc";
			/*
			 * if(BackEndUserUtils.isDeveloper()){//若登陆用户为开发人员 UserVO user =
			 * (UserVO)SessionUtils.getObjectFromSession(SessionContants.
			 * CURRENT_USER); criteria.andEqualTo("SYS_CREATER", user.getId());
			 * }
			 */

			PageList<PunSystemVO> vos = sysService.selectPagedByExample(example, currentPage, pageSize, sortString);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("vos", vos);
			model.addAttribute("currentPage", currentPage);
			// 返回查询条件
			model.addAttribute("vo", vo);
			return new ModelAndView("unit/dev/punSystem-list");
		} catch (PermissionException ea) {
			model.addAttribute("result", ea.getMessage());
		} catch (Exception e) {
			logger.info("ERROR", e);
			model.addAttribute("result", "系统异常");
		}
		return new ModelAndView("unit/dev/punSystem-list");
	}

	/**
	 * 
	 * @Title: punSystemDelete @Description: 根据ID删除 @author ljw @param @param
	 *         id @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "punSystemDelete", method = RequestMethod.POST)
	public ModelAndView punSystemDelete(Long[] boxs, int currentPage, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:punSystemList.do?currentPage=" + currentPage);
		try {
			/*
			 * if(!BackEndUserUtils.isBackEndRole()){//判断是否为可操作改功能的角色 throw new
			 * PermissionException("您无权限进行该操作！"); }
			 */
			for (Long id : boxs) {
				String result = sysService.delete(id);
				mv.addObject("result", "用户名" + result + "删除成功！");
			}
		} catch (PermissionException ea) {
			mv.addObject("result", ea.getMessage());
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "操作失败：系统异常");
		}
		return mv;
	}

	/**
	 * 
	 * @Title: sysChoose @Description: 选择系统，添加资源 @param @param
	 *         box @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "sysChoose", method = RequestMethod.POST)
	public ModelAndView sysChoose(Long chooseSys, Model model, RedirectAttributes ra) {
		if (null == chooseSys) {// 未选择待增加资源的系统
			ra.addFlashAttribute("result", "请选择系统后操作");
			return new ModelAndView("redirect:/dev/punSystemList.do?currentPage=0");
		}
		PunResourceVO vo = new PunResourceVO();
		vo.setSysId(chooseSys);
		model.addAttribute("vo", vo);
		return new ModelAndView("/unit/punResource-edit");

	}

	/**
	 * 系统发布申请
	 * 
	 * @author wsh
	 * @param vo
	 * @param model
	 * @param ra
	 * @return
	 */
	@RequestMapping(value = "/punSystemOnline", method = RequestMethod.POST)
	public ModelAndView punSystemOnline(Long[] boxs, Model model, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:punSystemList.do");// 返回到查询页面
		try {
			PunSystemVO vo = sysService.findById(boxs[0]);
			vo.setSysStatus(SYS_STATUS_ONLINE);
			sysService.addOrUpdate(vo);
			ra.addFlashAttribute("result", "系统'" + vo.getSysName() + "'发布成功");
			// return punSystemSave(vo, model, ra);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "异常");
		}
		return mv;

	}

	/**
	 * 系统下线申请
	 * 
	 * @author wsh
	 * @param vo
	 * @param model
	 * @param ra
	 * @return
	 */
	@RequestMapping(value = "/punSystemOffline", method = RequestMethod.POST)
	public ModelAndView punSystemOffline(Long[] boxs, Model model, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:punSystemList.do");
		try {
			PunSystemVO vo = sysService.findById(boxs[0]);
			vo.setSysStatus(SYS_STATUS_OFFLINE);
			sysService.addOrUpdate(vo);
			ra.addFlashAttribute("result", "系统‘" + vo.getSysName() + "’下线操作成功");
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "异常");
		}
		return mv;

	}

	/**
	 * 进入新增页面
	 * 
	 * @author wsh
	 * @return
	 */
	@RequestMapping(value = "/intoPunSystemNew", method = RequestMethod.GET)
	public ModelAndView intoPunSystemNew() {
		return new ModelAndView("/unit/dev/punDevSystem-edit");
	}

	/**
	 * 校验必填项
	 * 
	 * @param vo
	 * @return boolean
	 */
	private boolean validate(PunSystemVO vo) {
		valMessage = new StringBuffer();
		if (null == vo) {
			return false;
		}
		if (StringUtils.isEmpty(vo.getSysName())) {
			valMessage.append("系统名必填");
			return false;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (null == vo.getSysId()) {// 新增校验
			params.put("sysName", vo.getSysName());
			if (!queryByparams(params)) {// 校验是否有重名的系统
				return false;
			}
			params.clear();
			if (StringUtils.isNotEmpty(vo.getSysAddress())) {
				params.put("sysAddress", vo.getSysAddress());
				if (!queryByparams(params)) {// 校验是否有重地址的系统
					return false;
				}
			}

		} else {// 更新校验
			PunSystemVO systemVO = sysService.findById(vo.getSysId());
			if (!systemVO.getSysName().equals(vo.getSysName())) {
				params.put("sysName", vo.getSysName());
				if (!queryByparams(params)) {// 校验是否有重名的系统
					return false;
				}
			}
			if (StringUtils.isNotEmpty(vo.getSysAddress()) && !systemVO.getSysAddress().equals(vo.getSysAddress())) {
				params.clear();
				params.put("sysAddress", vo.getSysAddress());
				if (!queryByparams(params)) {// 校验是否有重地址的系统
					return false;
				}
			}
		}
		return true;
	}

	private boolean queryByparams(Map<String, Object> params) {
		List<PunSystemVO> sVos = sysService.queryResult("eqQueryList", params);
		if (null != sVos && sVos.size() > 0 && null != params.get("sysName")) {
			valMessage.append("，请更换系统名");
			return false;
		} else if (null != sVos && sVos.size() > 0 && null != params.get("sysAddress")) {
			valMessage.append("，请更换系统地址");
			return false;
		}
		return true;
	}

}
