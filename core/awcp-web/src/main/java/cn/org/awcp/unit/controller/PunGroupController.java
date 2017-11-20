package cn.org.awcp.unit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import cn.org.awcp.core.domain.QueryChannelService;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.utils.DocumentUtils;
import cn.org.awcp.unit.core.domain.PunPosition;
import cn.org.awcp.unit.service.PunGroupService;
import cn.org.awcp.unit.service.PunGroupSysService;
import cn.org.awcp.unit.service.PunSystemService;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.service.PunUserGroupService;
import cn.org.awcp.unit.utils.GroupTreeUtils;
import cn.org.awcp.unit.utils.JsonFactory;
import cn.org.awcp.unit.vo.PunAJAXStatusVO;
import cn.org.awcp.unit.vo.PunGroupSysVO;
import cn.org.awcp.unit.vo.PunGroupVO;
import cn.org.awcp.unit.vo.PunResourceTreeNode;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/unit")
public class PunGroupController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	@Qualifier("punGroupServiceImpl")
	PunGroupService groupService;

	@Autowired
	@Qualifier("punGroupSysServiceImpl")
	PunGroupSysService groupSysService;// 组与系统关联Service

	@Autowired
	@Qualifier("punSystemServiceImpl")
	PunSystemService sysService;// 系统Service

	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	PunUserBaseInfoService userService;// 用户Service
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource(name = "punUserGroupServiceImpl")
	private PunUserGroupService punUserGroupService;

	private StringBuffer valMessage = null;// 校验信息

	@Resource(name = "queryChannel")
	private QueryChannelService queryChannel;

	/**
	 * 
	 * @Title: intoPunGroup @Description: 进入组编辑页面 @author ljw @param @return @return
	 *         ModelAndView @throws
	 */
	@RequestMapping(value = "/intoPunGroup", method = RequestMethod.GET)
	public ModelAndView intoPunGroup(Model model) {
		PunGroupVO group = (PunGroupVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
		if (null != group) {
			model.addAttribute("parentGroupId", group.getParentGroupId());
		}
		return new ModelAndView("/unit/punGroup-edit", "vo", group);
	}

	/**
	 * 保存
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/punGroupSave", method = RequestMethod.POST)
	public ModelAndView punGroupSave(@ModelAttribute("vo") PunGroupVO vo, Model model, RedirectAttributes ra) {
		try {
			ModelAndView mv = new ModelAndView("redirect:/unit/selfGroupGet.do");
			if (validate(vo)) {
				if (null == vo.getParentGroupId() || vo.getParentGroupId().longValue() == 0) {
					vo.setPid(null);
				} else {
					PunGroupVO parentGroupVO = (PunGroupVO) groupService.findById(vo.getParentGroupId());
					if (null == parentGroupVO) {
						ra.addFlashAttribute("result", "No parent group exsit.");
						return mv;
					}
					vo.setPid(parentGroupVO.getPid() + vo.getParentGroupId().toString() + ",");
				}
				if (null == vo.getParentGroupId()) {
					vo.setParentGroupId(new Long(0));// 根节点的ParentId为0
				}
				groupService.addOrUpdate(vo);
				ra.addFlashAttribute("result", "Updated successfully!");
				return mv;
			} else {
				model.addAttribute("result", "Fail:" + valMessage.toString() + "。");
			}
		} catch (Exception e) {
			logger.info("ERROR", e);
			model.addAttribute("result", "System error.");
		}
		return new ModelAndView("/unit/punGroup-edit");
	}

	/**
	 * AJAX保存
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/punGroupAJAXSave")
	public PunAJAXStatusVO punGroupAJAXSave(@ModelAttribute("vo") PunGroupVO vo) {
		PunAJAXStatusVO respStatus = new PunAJAXStatusVO();
		try {
			// vo.setGroupType("1");
			if (validate(vo)) {
				if (null == vo.getParentGroupId() || vo.getParentGroupId().longValue() == 0) {
					vo.setPid(null);
				} else {
					PunGroupVO parentGroupVO = (PunGroupVO) groupService.findById(vo.getParentGroupId());
					if (null == parentGroupVO) {
						respStatus.setStatus(1);
						respStatus.setMessage("System error.");
					}
					String pid = parentGroupVO.getPid();
					if (pid == null)
						pid = "";
					vo.setPid(pid + vo.getParentGroupId().toString() + ",");
				}
				if (null == vo.getParentGroupId()) {
					vo.setParentGroupId(new Long(0));// 根节点的ParentId为0
				}
				groupService.addOrUpdate(vo);
				respStatus.setStatus(0);
				respStatus.setData(vo);
			} else {
				respStatus.setStatus(1);
				respStatus.setMessage("Failed:" + valMessage.toString());
			}
		} catch (Exception e) {
			logger.info("ERROR", e);
			respStatus.setStatus(1);
			respStatus.setMessage("System error.");
		}
		return respStatus;
	}

	/**
	 * 根据ID查找
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "punGroupEdit")
	public ModelAndView punGroupEdit(Long[] boxs) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punGroupTree-edit");
		try {
			PunGroupVO vo = groupService.findById(boxs[0]);
			List<Map<String, Object>> data = jdbcTemplate.queryForList(
					"select group_id id,parent_group_id pId,group_ch_name name,number,group_type groupType from p_un_group");
			mv.addObject("groupJson", JSON.toJSON(data));
			mv.addObject("vo", vo);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	/**
	 * 根据ID加载组织树
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getGroupTreeByAjax")
	public String getGroupTreeByAjax(Long[] boxs) {

		try {
			PunGroupVO vo = groupService.findById(boxs[0]);
			String pid = vo.getPid();
			if (pid == null) {
				pid = "";
			}
			pid = pid + vo.getGroupId().toString() + ",";
			List<PunGroupVO> posterityGroups = groupService.getGroupListByPid(pid);
			posterityGroups.add(vo);
			GroupTreeUtils rtu = new GroupTreeUtils();
			List<PunGroupVO[]> list = rtu.generateTreeView(posterityGroups);
			Map<Integer, List<PunResourceTreeNode>> resultMap = new HashMap<Integer, List<PunResourceTreeNode>>();
			Integer index = 1;
			for (PunGroupVO[] prvo : list) {
				// 以目录根节点的index为key，以manyNodeTree为value
				List<PunResourceTreeNode> manyNodeTree = GroupTreeUtils.getPlainZNodes(prvo);
				resultMap.put(index, manyNodeTree);
				index++;
			}
			JsonFactory factory = new JsonFactory();
			return factory.encode2Json(resultMap);
		} catch (Exception e) {
			logger.info("ERROR", e);
		}
		// return mv;
		return null;
	}

	/**
	 * 获取当前登陆组织信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "selfGroupGet")
	public ModelAndView selfGroupGet() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punGroup-edit");
		try {
			PunGroupVO vo = (PunGroupVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
			PunGroupVO vo1 = groupService.findById(vo.getGroupId());
			SessionUtils.addObjectToSession(SessionContants.CURRENT_USER_GROUP, vo1);
			mv.addObject("vo", vo1);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	/**
	 * 
	 * @Title: punGroupAJAXDelete @Description: 根据ID AJAX删除 @author
	 *         ljw @param @param id @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "punGroupAJAXDelete")
	@ResponseBody
	public PunAJAXStatusVO punGroupAJAXDelete(@ModelAttribute("vo") PunGroupVO vo) {
		PunAJAXStatusVO respStatus = new PunAJAXStatusVO();
		try {
			PunGroupVO groupVO = groupService.findById(vo.getGroupId());
			if (null != groupVO) {
				Long id = groupVO.getGroupId();
				groupService.delete(id);
				respStatus.setStatus(0);
			} else {
				respStatus.setStatus(1);
				respStatus.setMessage("Parent group not exsit.");
			}
		} catch (Exception e) {
			logger.info("ERROR", e);
			respStatus.setStatus(1);
			respStatus.setMessage("System error.");
		}
		return respStatus;
	}

	/**
	 * 关联系统
	 * 
	 * @param sysId
	 * @return
	 */
	@RequestMapping(value = "relateSys")
	public ModelAndView relateSys(Long[] boxs) {
		ModelAndView mv = new ModelAndView("redirect:punSysQuery.do");
		try {
			for (Long sysId : boxs) {
				PunGroupSysVO groupSys = new PunGroupSysVO();
				PunGroupVO group = (PunGroupVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
				groupSys.setSysId(sysId);
				groupSys.setGroupId(group.getGroupId());
				groupSysService.addOrUpdate(groupSys);
			}
			mv.addObject("relateOrNot", '0');
			mv.addObject("result", "Add successfully.");
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	/**
	 * 取消组与系统的关联关系
	 * 
	 * @param boxs
	 * @return
	 */
	@RequestMapping(value = "cancelRelate")
	public ModelAndView cancelRelate(Long[] boxs) {
		ModelAndView mv = new ModelAndView("redirect:punSysQuery.do");
		try {
			PunGroupVO group = (PunGroupVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
			for (Long sysId : boxs) {
				groupSysService.deleteBySysAndGroup(sysId, group.getGroupId());
			}
			mv.addObject("relateOrNot", '1');
			mv.addObject("resutl", "Cancel relate successful");
		} catch (Exception e) {
			logger.info("ERROR", e);
		}
		return mv;
	}

	/**
	 * 校验必填项
	 * 
	 * @param vo
	 * @return boolean
	 */
	private boolean validate(PunGroupVO vo) {
		valMessage = new StringBuffer();
		if (StringUtils.isEmpty(vo.getGroupChName())) {
			valMessage.append(",请填写组织名称。");
			return false;
		}
		// 组织机构代码不为空，因组织机构只有在一级组织才是必填的
		if (StringUtils.isNotEmpty(vo.getOrgCode())) {
			// 新增时校验
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("orgCode", vo.getOrgCode());
			params.put("groupChName", vo.getGroupChName());
			params.put("parentGroupId", vo.getParentGroupId());
			List<PunGroupVO> gVOs = null;
			if (null == vo.getGroupId()) {// 若新增
				gVOs = groupService.queryResult("eqQueryList", params);
			} else {// 更新校验
				PunGroupVO gVo = groupService.findById(vo.getGroupId());
				// 新组织机构代码与原组织机构代码不同
				if (!gVo.getOrgCode().equals(vo.getOrgCode())) {
					gVOs = groupService.queryResult("eqQueryList", params);
				}
			}
			// 判断组是否已录入过,组织的下属机构无需填该项

			if (null != gVOs && gVOs.size() > 0) {
				valMessage.append(",该组织已经存在");
				return false;
			}
		}
		return true;
	}

	/**
	 * 按部门或人员搜索
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "searchByType")
	public ModelAndView searchByType(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		String type = request.getParameter("searchType");

		Map<String, Object> params = new HashMap<String, Object>();

		List<PunUserBaseInfoVO> users = null;
		if ("0".equals(type)) {
			params.put("groupChName", request.getParameter("searchName"));
			List<PunGroupVO> vo = groupService.queryResult("eqQueryList", params);
			if (null != vo && vo.size() > 0) {
				PunGroupVO pvo = vo.get(0);
				users = punUserGroupService.queryUserListByGroupId(pvo.getGroupId());
				if (users != null) {
					for (int i = 0; i < users.size(); i++) {
						users.get(i).setDeptName(pvo.getGroupChName());
					}
				}
			}
		} else {
			params.put("name", request.getParameter("searchName"));
			users = userService.queryResult("eqQueryList", params);
			if (users != null) {
				for (int i = 0; i < users.size(); i++) {
					PunUserBaseInfoVO pvo = users.get(i);
					List<PunGroupVO> group = DocumentUtils.getIntance().listGroupByUser(pvo.getUserId());
					if (null != group && group.size() > 0) {
						PunGroupVO pun = group.get(0);
						pvo.setDeptName(pun.getGroupChName());
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", pvo.getUserId());
					List<PunPosition> positionList = queryChannel.queryResult(PunPosition.class, "getByUserID", map);
					pvo.setPositionList(positionList);
				}
			}
			type = "1";
		}
		mv.addObject("userList", users);
		mv.addObject("searchName", request.getParameter("searchName"));
		mv.addObject("searchType", type);
		mv.setViewName("/unit/punGroup-usersView");
		return mv;
	}

	/**
	 * all dept manage
	 * 
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "listGroup")
	public List<PunGroupVO> listGroup() {
		ModelAndView mv = new ModelAndView();
		List<PunGroupVO> vo = groupService.findAll();
		mv.addObject("vo", vo);
		return vo;
	}

	// 编辑组织number
	@RequestMapping(value = "editGroup")
	public ModelAndView editGroup(Long groupId, String number) {
		ModelAndView mv = new ModelAndView();
		PunGroupVO pun = groupService.findById(groupId);

		String str = "";
		// 获取父级的排序
		if (null != pun.getPid() && !"".equals(pun.getPid())) {
			String ids[] = pun.getPid().split(",");
			for (String s : ids) {
				PunGroupVO tem = groupService.findById(Long.parseLong(s));
				if ("".equals(tem.getNumber()) || null == tem.getNumber()) {
					tem.setNumber("NO");
				}
				String nu = tem.getNumber();
				// 只取父节点本身的排序
				str += nu.substring(nu.lastIndexOf("-") + 1, nu.length()) + "-";
			}
		}

		pun.setNumber(str + number);
		groupService.addOrUpdate(pun);
		mv.setViewName("/unit/departmentList");
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "getById")
	public PunGroupVO get(Long groupId) {
		PunGroupVO vo = groupService.findById(groupId);
		return vo;
	}

}
