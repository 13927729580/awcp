package cn.org.awcp.unit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.unit.service.PunRoleInfoService;
import cn.org.awcp.unit.utils.BackEndUserUtils;
import cn.org.awcp.unit.utils.PermissionException;
import cn.org.awcp.unit.vo.PunRoleInfoVO;
import cn.org.awcp.unit.vo.PunSystemVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Controller
@RequestMapping("/dev")
public class PunTargetRoleInfoController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	public static final int pagesize = 8;

	private StringBuffer valMessage = null;// 校验信息

	@Resource(name = "punRoleInfoServiceImpl")
	private PunRoleInfoService punRoleInfoService;

	@RequestMapping(value = "/intoPunTargetRoleInfo", method = RequestMethod.GET)
	public ModelAndView intoPunTargetRoleInfo(HttpSession session) {
		try {

			if (!BackEndUserUtils.isBackEndRole()) {// 判断是否为可操作改功能的角色
				throw new PermissionException("You has not permission to operate");
			}
			PunSystemVO sysVO = (PunSystemVO) session.getAttribute(SessionContants.CURRENT_SYSTEM);
			if (null != sysVO) {
				PunRoleInfoVO vo = new PunRoleInfoVO();
				vo.setSysId(sysVO.getSysId());
				return new ModelAndView("/unit/punTargetRoleInfo-edit", "vo", vo);
			} else {
				return new ModelAndView("/unit/punTargetRoleInfo-edit");
			}
		} catch (Exception e) {
			logger.info("ERROR", e);
			return null;
		}
	}

	@RequestMapping(value = "intoPunTargetRoleInfoList", method = RequestMethod.GET)
	public ModelAndView intoPunTargetRoleInfoList() {
		ModelAndView mv = new ModelAndView("redirect:/dev/punTargetRoleInfoList.do?currentPage=0");
		return mv;
	}

	@RequestMapping(value = "/punTargetRoleInfoSave", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("vo") PunRoleInfoVO vo, Model model) {
		if (!BackEndUserUtils.isBackEndRole()) {// 判断是否为可操作改功能的角色
			throw new PermissionException("You has not permission to operate.");
		}
		ModelAndView mv = new ModelAndView("/unit/punTargetRoleInfo-edit");
		Long id = new Long(0);
		if (null != vo.getRoleId()) {
			;
			id = vo.getRoleId();
		}
		try {
			if (validate(vo)) {
				if (id == 0) {
					this.punRoleInfoService.addOrUpdateRole(vo);
				} else {
					this.punRoleInfoService.addOrUpdateRole(vo);
					mv.addObject("vo", vo);
				}
				return new ModelAndView("redirect:/dev/punTargetRoleInfoList.do?currentPage=0&boxs=" + vo.getSysId());
			} else {
				model.addAttribute("result", "校验失败" + valMessage.toString() + "。");
			}
		} catch (Exception e) {
			logger.info("ERROR", e);
			model.addAttribute("result", "System error.");
		}
		return mv;
	}

	@RequestMapping(value = "/punTargetRoleInfoDelete", method = RequestMethod.POST)
	public ModelAndView remove(Long[] boxs, Long[] sysId, int currentPage) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/dev/punTargetRoleInfoList.do?currentPage=" + currentPage + "&boxs=" + sysId[0]);
		try {

			if (!BackEndUserUtils.isBackEndRole()) {// 判断是否为可操作改功能的角色
				throw new PermissionException("You has not permission to operate");
			}

			for (Long id : boxs) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("roleId", id);
				PunRoleInfoVO vo = new PunRoleInfoVO();
				vo.setRoleId(id);
				punRoleInfoService.delete(vo.getRoleId());
			}
			mv.addObject("result", "Delete successfully.");
		} catch (PermissionException ea) {
			mv.addObject("result", ea.getMessage());
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	@RequestMapping(value = "/punTargetRoleInfoGet", method = RequestMethod.POST)
	public ModelAndView get(Long[] boxs) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punTargetRoleInfo-edit");
		try {
			PunRoleInfoVO vo = this.punRoleInfoService.findById(boxs[0]);
			mv.addObject("vo", vo);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	/**
	 * 更新
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/punTargetRoleInfoUpdate", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute PunRoleInfoVO vo) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punTargetRoleInfo-edit");
		try {
			if (!BackEndUserUtils.isBackEndRole()) {// 判断是否为可操作改功能的角色
				throw new PermissionException("You has not permission to operate.");
			}
			this.punRoleInfoService.addOrUpdateRole(vo);
			mv.addObject("vo", vo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	/**
	 * 校验必填项
	 * 
	 * @param vo
	 * @return boolean
	 */
	private boolean validate(PunRoleInfoVO vo) {
		if (null == vo) {
			return false;
		}
		valMessage = new StringBuffer();
		if (null == vo.getSysId()) {
			valMessage.append(",System Id required");
			return false;
		}
		if (null == vo.getRoleName()) {
			valMessage.append(",Role Name required");
			return false;
		}
		// 新增时校验
		if (null == vo.getRoleId()) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sysId", vo.getSysId());
			params.put("roleName", vo.getRoleName());
			List<PunRoleInfoVO> rVOs = punRoleInfoService.queryResult("eqQueryList", params);
			if (null != rVOs && rVOs.size() > 0) {
				valMessage.append(",Role exsit");
				return false;
			}
		}
		return true;
	}

	/**
	 * 精确查询查询DevRoleInfo
	 * 
	 * @param vo
	 * @param model
	 * @param currentPage
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/punTargetRoleInfoList")
	public ModelAndView queryPagedResultForTarget(@ModelAttribute PunRoleInfoVO vo,
			@RequestParam(value = "boxs", required = false) Long boxs,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punTargetRoleInfo-list");
		try {

			int pageSize = 10;

			String sortString = "ROLE_ID.desc";
			Map<String, Object> params = new HashMap<String, Object>();
			if (StringUtils.isNotEmpty(vo.getRoleName())) {
				params.put("roleName", vo.getRoleName());
			}
			if (boxs != null) {
				params.put("sysId", boxs);
			}
			String queryStr = "eqQueryListJoin";
			PageList<PunRoleInfoVO> vos = punRoleInfoService.queryPagedResult(queryStr, params, currentPage, pageSize,
					sortString);

			mv.addObject("vos", vos);
			// 返回查询条件
			mv.addObject("vo", vo);
			mv.addObject("currentPage", currentPage);
		} catch (PermissionException ea) {
			mv.addObject("result", "System error.");
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;
	}

}