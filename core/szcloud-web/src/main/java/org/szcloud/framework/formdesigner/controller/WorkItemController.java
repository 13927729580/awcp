package org.szcloud.framework.formdesigner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.base.BaseController;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.Tools;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.formdesigner.application.vo.WorkItemEntryVO;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.business.TWorkAdmin;
import org.szcloud.framework.workflow.core.emun.EEntryType;
import org.szcloud.framework.workflow.core.entity.CEntry;
import org.szcloud.framework.workflow.core.entity.CWorkItem;

@Controller
@RequestMapping("/workItem")
public class WorkItemController extends BaseController {

	@ResponseBody
	@RequestMapping(value = "/getWorkItem")
	public List<WorkItemEntryVO> getWorkItem() {
		try {
			List<WorkItemEntryVO> workItemEntrylist = new ArrayList<WorkItemEntryVO>();
			PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils
					.getObjectFromSession(SessionContants.CURRENT_USER);
			CLogon lo_Logon = new CLogon(0, user.getName(), user.getUserPwd(), "127.0.0.1");
			if (lo_Logon != null) {
				CWorkItem lo_Item = TWorkAdmin.workItemTrackInfo(lo_Logon, 136, 1);
				workItemEntrylist = cWorkItemToWorkItemEntry(lo_Item);
				lo_Item = null;
				lo_Logon.ClearUp();
			}
			ModelAndView mv = new ModelAndView();
			mv.addObject("workItemEntrylist", workItemEntrylist);
			return workItemEntrylist;
		} catch (Exception e) {
			logger.info("ERROR", e);
			return null;
		}
	}

	public List<WorkItemEntryVO> cWorkItemToWorkItemEntry(CWorkItem item) {
		List<WorkItemEntryVO> workItemEntrylist = new ArrayList<WorkItemEntryVO>();
		List<CEntry> cEntryList = new ArrayList<CEntry>();
		HashMap<Integer, CEntry> entryMap = item.Entries;
		for (Map.Entry<Integer, CEntry> entry : entryMap.entrySet()) {
			if (entry.getValue().EntryType == EEntryType.ActualityEntry) {
				cEntryList.add(entry.getValue());
			}
		}
		if (cEntryList.size() > 0) {
			List<ArrayList<CEntry>> list = groupSort(cEntryList);
			for (int i = 0; i < list.size(); i++) {
				ArrayList<CEntry> cntryList = list.get(i);
				for (CEntry cEntry : cntryList) {
					WorkItemEntryVO workItemEntry = new WorkItemEntryVO();
					workItemEntry.setWorkFlowStatus(item.getStatusName());
					workItemEntry.setActivityName(cEntry.ActivityName);
					workItemEntry.setParticipant(cEntry.Participant);
					workItemEntry.setInceptDate(Tools.format(cEntry.InceptDate, Tools.YYYY_MM_DD_HH_MM_SS));
					long finisheDate = cEntry.FinishedDate.getTime();
					if (finisheDate > 0) {
						workItemEntry.setHandleDate(Tools.format(cEntry.FinishedDate, Tools.YYYY_MM_DD_HH_MM_SS));
					} else {
						workItemEntry.setHandleDate("");
					}
					workItemEntry.setHandleStatus(cEntry.getEntryStatusName());
					workItemEntry.setHandleView(cEntry.ResponseContent);

					if ((i + 1) < list.size()) {
						ArrayList<CEntry> entryList = list.get(i + 1);
						String activityName = "";
						String participant = "";
						for (CEntry e : entryList) {
							if (activityName != null && !"".equals(activityName)) {
								activityName = activityName + "," + e.ActivityName;
							} else {
								activityName = e.ActivityName;
							}
							if (participant != null && !"".equals(activityName)) {
								participant = participant + "," + e.Participant;
							} else {
								participant = e.Participant;
							}
						}
						workItemEntry.setSendStep(activityName);
						workItemEntry.setSender(participant);
					} else {
						workItemEntry.setSendStep("");
						workItemEntry.setSender("");
					}

					workItemEntrylist.add(workItemEntry);
				}

			}
		}
		return workItemEntrylist;
	}

	private List<ArrayList<CEntry>> groupSort(List<CEntry> cEntryList) {
		Map<Integer, String> groups = new HashMap<Integer, String>();
		for (CEntry cEntry : cEntryList) {
			groups.put(cEntry.OrginalID, "");
		}
		List<ArrayList<CEntry>> list = new ArrayList<ArrayList<CEntry>>();
		List<CEntry> entryList = new ArrayList<CEntry>();
		for (Integer orginalID : groups.keySet()) {
			for (CEntry cEntry : cEntryList) {
				if (orginalID.equals(cEntry.OrginalID)) {
					entryList.add(cEntry);
				}
			}
			list.add((ArrayList<CEntry>) entryList);
		}
		return list;
	}

}