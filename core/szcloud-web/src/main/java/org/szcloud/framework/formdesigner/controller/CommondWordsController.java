package org.szcloud.framework.formdesigner.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.formdesigner.application.service.SuggestionService;
import org.szcloud.framework.formdesigner.application.vo.SuggestionVO;
import org.szcloud.framework.formdesigner.utils.DocumentUtils;
import org.szcloud.framework.unit.service.PunUserBaseInfoService;
import org.szcloud.framework.unit.vo.PunGroupVO;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.business.TWorkItem;
import org.szcloud.framework.workflow.core.entity.CWorkItem;

@Controller
@RequestMapping("/commondWords")
public class CommondWordsController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	@Qualifier("suggestionServiceImpl")
	private SuggestionService suggestionService;

	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	PunUserBaseInfoService userService;// 用户Service

	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public SuggestionVO save(String id, String workId, String EntryID, String busessid, String content, String status,
			String type) throws Exception {

		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		CLogon lo_Logon = new CLogon(0, user.getUserIdCardNumber(), user.getName(), "192.168.0.1");
		CWorkItem lo_Item = null;
		String workName = "";
		try {
			lo_Item = TWorkItem.openWorkItem(lo_Logon, Integer.valueOf(workId), Integer.valueOf(EntryID));
		} catch (Exception e) {
			logger.info("ERROR", e);
			lo_Logon.ClearUp();
			lo_Item.ClearUp();
		}

		SuggestionVO vo = new SuggestionVO();

		// 保存修改动作
		if (!"".equals(id)) {
			String suid[] = { id };
			suggestionService.delete(suid);
		}

		if (lo_Item != null) {
			workName = lo_Item.CurActivity.ActivityName;
			if (lo_Item.CurActivity.ActivityID == 1) {
				workName = "呈批意见";
			}
			vo.setLink(String.valueOf(lo_Item.CurActivity.ActivityID));

			vo.setStatus(status);
			vo.setBusinessid(busessid);
			vo.setDate(new Date());
			vo.setConment(content);
			// 保存类别 类别从流程中获取
			vo.setType(workName);
			vo.setLinkName(workName);

			vo.setPerson(String.valueOf(user.getUserId()));
			vo.setPersonName(user.getName());

			List<PunGroupVO> group = DocumentUtils.listGroupByUser(user.getUserId());

			if (group != null) {
				PunGroupVO pun = group.get(0);
				vo.setDept(String.valueOf(pun.getGroupId()));
				vo.setDeptName(pun.getGroupChName());

			}

			String ids = suggestionService.save(vo);
			vo.setId(ids);

			lo_Logon.ClearUp();
			lo_Logon = null;
			lo_Item.ClearUp();
			lo_Item = null;

			return vo;

		}

		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/getActivityID")
	public SuggestionVO getActivityID(String workId, String EntryID) throws Exception {

		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		CLogon lo_Logon = new CLogon(0, user.getUserIdCardNumber(), user.getName(), "192.168.0.1");
		CWorkItem lo_Item = null;
		try {
			lo_Item = TWorkItem.openWorkItem(lo_Logon, Integer.valueOf(workId), Integer.valueOf(EntryID));
		} catch (Exception e) {
			logger.info("ERROR", e);
			lo_Logon.ClearUp();
			lo_Item.ClearUp();
		}
		SuggestionVO vo = new SuggestionVO();

		if (lo_Item != null) {
			vo.setFlag(lo_Item.CurActivity.ActivityID);
			vo.setType(String.valueOf(lo_Item.Status));
			vo.setLinkName(lo_Item.CurActivity.ActivityName);

			lo_Logon.ClearUp();
			lo_Logon = null;
			lo_Item.ClearUp();
			lo_Item = null;
		} else {
			vo.setFlag(00);
		}

		return vo;
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public boolean delete(String id) {
		String[] ids = { id };
		return suggestionService.delete(ids);
	}

	@ResponseBody
	@RequestMapping(value = "/findTextByWorkId")
	public List<SuggestionVO> findByUserId(String busId, String[] nodes, String type, String workId, String EntryID)
			throws Exception {
		// 获取当前环节

		PunUserBaseInfoVO userinfo = (PunUserBaseInfoVO) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_USER);

		CLogon lo_Logon = new CLogon(0, userinfo.getUserIdCardNumber(), userinfo.getName(), "192.168.0.1");
		CWorkItem lo_Item = null;
		String flow = "";
		try {
			lo_Item = TWorkItem.openWorkItem(lo_Logon, Integer.valueOf(workId), Integer.valueOf(EntryID));
		} catch (Exception e) {
			logger.info("ERROR", e);
			lo_Logon.ClearUp();
			if (lo_Item != null) {
				lo_Item.ClearUp();
			}

		}

		if (lo_Item != null) {

			flow = String.valueOf(lo_Item.CurActivity.ActivityID);
			lo_Logon.ClearUp();
			lo_Logon = null;
			lo_Item.ClearUp();
			lo_Item = null;
		}

		Map<String, Object> resParams = new HashMap<String, Object>();
		List<SuggestionVO> resoVOs = new ArrayList<SuggestionVO>();
		if (nodes.length > 0) {

			for (String st : nodes) {
				String str = st.substring("flowNodes".length(), st.length());
				if (str != null && !"".equals(str) && !"".equals(busId)) {
					if (!"".equals(type) && !"0".equals(type)) {
						resParams.put("type", type);
					}
					resParams.put("link", str);
					resParams.put("businessid", busId);
					List<SuggestionVO> list = suggestionService.queryResult("eqQueryList", resParams);
					for (SuggestionVO vo : list) {
						PunUserBaseInfoVO user = userService.findById(Long.parseLong(vo.getPerson()));
						// 找出当前登录用户的意见

						if (!"".equals(vo.getPerson()) && String.valueOf(userinfo.getUserId()).equals(vo.getPerson())
								&& vo.getLink().equals(flow)) {
							vo.setFlag(11);
						}
						vo.setPerson(vo.getDeptName() + " " + user.getName());
						resoVOs.add(vo);
					}
				}

			}

		}

		return resoVOs;
	}

	/*
	 * 根据业务ID 用户ID 类别组合查询
	 */
	@ResponseBody
	@RequestMapping(value = "/findByWhere")
	public List<SuggestionVO> findByWhere(String busId, String userId, String type) {

		Map<String, Object> resParams = new HashMap<String, Object>();
		resParams.put("person", userId);
		resParams.put("businessid", busId);
		resParams.put("type", type);
		List<SuggestionVO> list = suggestionService.queryResult("eqQueryList", resParams);
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/findTextByGroup")
	public List<SuggestionVO> findTextByGroup(String activityId) {

		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/findByTypeId")
	public List<SuggestionVO> findByTypeId(String typeId) {

		return null;
	}

	// 获取单签用户常用意见
	@ResponseBody
	@RequestMapping(value = "/getNoteAll")
	public List<SuggestionVO> getNoteAll() {

		PunUserBaseInfoVO userinfo = (PunUserBaseInfoVO) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_USER);

		Map<String, Object> resParams = new HashMap<String, Object>();
		resParams.put("person", userinfo.getUserId());
		resParams.put("flag", 10);
		List<SuggestionVO> list = suggestionService.queryResult("eqQueryList", resParams);
		if (list != null) {
			if (list.size() > 0) {
				return list;
			}
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/addNote")
	public SuggestionVO addNote(String str) {

		PunUserBaseInfoVO userinfo = (PunUserBaseInfoVO) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_USER);

		SuggestionVO vo = new SuggestionVO();

		vo.setDate(new Date());
		if (str != null && str != "") {
			vo.setConment(str.replace(" ", ""));
		}
		vo.setBusinessid("0");
		vo.setPerson(String.valueOf(userinfo.getUserId()));
		vo.setPersonName(userinfo.getName());
		vo.setFlag(10);

		String ids = suggestionService.save(vo);
		vo.setId(ids);
		return vo;
	}

	@ResponseBody
	@RequestMapping(value = "/editNote")
	public SuggestionVO editNote(String id, String str) {

		SuggestionVO vo = suggestionService.findById(id);
		if (str != null && str != "") {
			vo.setConment(str.replace(" ", ""));
		}
		suggestionService.save(vo);
		return vo;
	}

	@ResponseBody
	@RequestMapping(value = "/delNote")
	public SuggestionVO delNote(String id) {

		String ids[] = { id };
		boolean bl = suggestionService.delete(ids);
		SuggestionVO vo = new SuggestionVO();
		vo.setFlag(00);
		return vo;
	}

}
