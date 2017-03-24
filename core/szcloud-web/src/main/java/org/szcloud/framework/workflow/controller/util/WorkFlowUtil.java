package org.szcloud.framework.workflow.controller.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.core.utils.Tools;
import org.szcloud.framework.formdesigner.application.vo.WorkItemEntryVO;
import org.szcloud.framework.workflow.core.emun.EEntryType;
import org.szcloud.framework.workflow.core.entity.CEntry;
import org.szcloud.framework.workflow.core.entity.CWorkItem;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class WorkFlowUtil {

	public static List<WorkItemEntryVO> cWorkItemToWorkItemEntry(CWorkItem item){
		List<WorkItemEntryVO> workItemEntrylist = new ArrayList<WorkItemEntryVO>();
		List<CEntry> cEntryList = new ArrayList<CEntry>();
		HashMap<Integer, CEntry> entryMap = item.Entries;
		for (Map.Entry<Integer, CEntry> entry : entryMap.entrySet()) {
			if(entry.getValue().EntryType == EEntryType.ActualityEntry){
				cEntryList.add(entry.getValue());
			}
		}
		if(cEntryList.size()>0){
			for(CEntry cEntry : cEntryList){
				WorkItemEntryVO workItemEntry =	new WorkItemEntryVO();
				workItemEntry.setOrginalID(cEntry.OrginalID);
				workItemEntry.setWorkFlowStatus(item.getStatusName());
				workItemEntry.setActivityName(cEntry.ActivityName);
				workItemEntry.setParticipant(cEntry.Participant);
				workItemEntry.setInceptDate(Tools.format(cEntry.InceptDate,Tools.YYYY_MM_DD));
				long finisheDate = cEntry.FinishedDate.getTime();
				if(finisheDate >0){
					workItemEntry.setHandleDate(Tools.format(cEntry.FinishedDate,Tools.YYYY_MM_DD));
				}else {
					workItemEntry.setHandleDate("");
				}
				workItemEntry.setHandleStatus(cEntry.getEntryStatusName());
				workItemEntry.setHandleView(cEntry.ResponseContent);
				CEntry entry = cEntry.getOrginalEntry();
				if(entry == null){
					workItemEntry.setSendStep("启动");
					workItemEntry.setSender("系统");
				}else {
					workItemEntry.setSendStep(entry.ActivityName);
					workItemEntry.setSender(cEntry.Participant);
				}
				workItemEntrylist.add(workItemEntry);
			}
		}
		return workItemEntrylist;
	}
	
	/**
	 * 根据对象的OrginalID排序
	 * @param workItemEntrylist
	 * @return
	 */
	public static List<WorkItemEntryVO> workItemEntrylistSort(List<WorkItemEntryVO> workItemEntrylist){
		if(workItemEntrylist != null && workItemEntrylist.size()>1){
			Collections.sort(workItemEntrylist, new Comparator<WorkItemEntryVO>(){  
				/*  
				 * int compare(WorkItemEntryVO o1, WorkItemEntryVO o2) 返回一个基本类型的整型，  
				 * 返回负数表示：o1 小于o2，  
				 * 返回0 表示：o1和o2相等，  
				 * 返回正数表示：o1大于o2。  
				 */  
				public int compare(WorkItemEntryVO o1, WorkItemEntryVO o2) {  
					//按照流程步骤ID进行升序排列  
					if(o1.getOrginalID() > o2.getOrginalID()){  
						return 1;  
					}  
					if(o1.getOrginalID() == o2.getOrginalID()){  
						return 0;  
					}  
					return -1;  
				}  
			}); 
		}
		return workItemEntrylist;
	}
	
	/**
	 * 根据名称获取JSON数组中的JSON对象
	 * 
	 * @param ao_JSONArray
	 *            JSON数组
	 * @param as_Name
	 *            名称
	 * @return
	 */
	public static JSONObject getJSONObject(String ao_JSONArray, String as_Name) {
		JSONArray arrayJson = JSONArray.parseArray(ao_JSONArray);
		for (int i = 0; i < arrayJson.size(); i++) {
			JSONObject lo_Json = arrayJson.getJSONObject(i);
			if (lo_Json.getString("name").equals(as_Name))
				return lo_Json;
		}
		return null;
	}
}
