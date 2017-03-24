package org.szcloud.framework.workflow.core.business;

import java.util.ArrayList;

import org.szcloud.framework.workflow.core.emun.ELapseToType;
import org.szcloud.framework.workflow.core.module.MGlobal;
import com.alibaba.fastjson.JSON;

/**
 * 公文流转接口实体类
 * 
 * @author Jackie.Wang
 *
 */
public class InnerWorkItem {

	/**
	 * 当前状态标识
	 */
	private int ml_EntryID = 0;

	/**
	 * 当前状态标识
	 * 
	 * @return
	 */
	public int getEntryID() {
		return ml_EntryID;
	}

	/**
	 * 当前状态标识
	 * 
	 * @param value
	 */
	public void setEntryID(int value) {
		ml_EntryID = value;
	}

	/**
	 * 当前步骤标识
	 */
	private int ml_ActivityID = 0;

	/**
	 * 当前步骤标识
	 * 
	 * @return
	 */
	public int getActivityID() {
		return ml_ActivityID;
	}

	/**
	 * 当前步骤标识
	 * 
	 * @param value
	 */
	public void setActivityID(int value) {
		ml_ActivityID = value;
	}

	/**
	 * 是否需要发送动作
	 */
	private boolean mb_SendAction = true;

	/**
	 * 是否需要发送动作
	 * 
	 * @return
	 */
	public boolean getSendAction() {
		return mb_SendAction;
	}

	/**
	 * 是否需要发送动作
	 * 
	 * @param value
	 */
	public void setSendAction(boolean value) {
		mb_SendAction = value;
	}

	/**
	 * 发送标题
	 */
	private String ms_SendLabel = "发送";

	/**
	 * 发送标题
	 * 
	 * @return
	 */
	public String getSendLabel() {
		return ms_SendLabel;
	}

	/**
	 * 发送标题
	 * 
	 * @param value
	 */
	public void setSendLabel(String value) {
		if (MGlobal.isExist(value))
			ms_SendLabel = value;
	}

	/**
	 * 是否需要保存标题 =0-需要; =1-不需要; =2-根据权限访问;
	 */
	private int mi_Save1LabelFlag = 0;

	/**
	 * 是否需要保存标题 =0-需要; =1-不需要; =2-根据权限访问;
	 * 
	 * @return
	 */
	public int getSave1LabelFlag() {
		return mi_Save1LabelFlag;
	}

	/**
	 * 是否需要保存标题 =0-需要; =1-不需要; =2-根据权限访问;
	 * 
	 * @param value
	 */
	public void setSave1LabelFlag(int value) {
		mi_Save1LabelFlag = value;
	}

	/**
	 * 保存标题
	 */
	private String ms_Save1Label = "保存";

	/**
	 * 保存标题
	 * 
	 * @return
	 */
	public String getSave1Label() {
		return ms_Save1Label;
	}

	/**
	 * 保存标题
	 * 
	 * @param value
	 */
	public void setSave1Label(String value) {
		if (MGlobal.isExist(value))
			ms_Save1Label = value;
	}

	/**
	 * 是否需要保存并返回标题 =0-需要; =1-不需要; =2-根据权限访问;
	 */
	private int ms_Save2LabelFlag = 0;

	/**
	 * 是否需要保存并返回标题 =0-需要; =1-不需要; =2-根据权限访问;
	 * 
	 * @return
	 */
	public int getSave2LabelFlag() {
		return ms_Save2LabelFlag;
	}

	/**
	 * 是否需要保存并返回标题 =0-需要; =1-不需要; =2-根据权限访问;
	 * 
	 * @param value
	 */
	public void setSave2LabelFlag(int value) {
		ms_Save2LabelFlag = value;
	}

	/**
	 * 保存并返回标题
	 */
	private String ms_Save2Label = "保存并返回";

	/**
	 * 保存并返回标题
	 * 
	 * @return
	 */
	public String getSave2Label() {
		return ms_Save2Label;
	}

	/**
	 * 保存并返回标题
	 * 
	 * @param value
	 */
	public void setSave2Label(String value) {
		ms_Save2Label = value;
	}

	/**
	 * 转发内容选项
	 */
	private String ms_ResponseChoices = "";

	/**
	 * 转发内容选项
	 * 
	 * @return
	 */
	public String getResponseChoices() {
		return ms_ResponseChoices;
	}

	/**
	 * 转发内容选项
	 * 
	 * @param value
	 */
	public void setResponseChoices(String value) {
		ms_ResponseChoices = value;
	}

	/**
	 * 获取意见选项数量
	 * 
	 * @return
	 */
	public int getResponseCount() {
		if (MGlobal.isEmpty(ms_ResponseChoices))
			return 0;
		int li_Count = 0;
		for (String ls_Text : ms_ResponseChoices.split(";")) {
			if (MGlobal.isExist(ls_Text))
				li_Count++;
		}
		return li_Count;
	}

	/**
	 * 可能发送的后继步骤
	 */
	private String ms_NextActivityIDs = "";

	/**
	 * 可能发送的后继步骤
	 * 
	 * @return
	 */
	public String getNextActivityIDs() {
		return ms_NextActivityIDs;
	}

	/**
	 * 可能发送的后继步骤
	 * 
	 * @param value
	 */
	public void setNextActivityIDs(String value) {
		ms_NextActivityIDs = value;
	}

	/**
	 * 获取可能发送的后继步骤数量
	 * 
	 * @return
	 */
	public int getNextActivityCount() {
		if (MGlobal.isEmpty(ms_NextActivityIDs))
			return 0;
		int li_Count = 0;
		for (String ls_Text : ms_NextActivityIDs.split(";")) {
			if (MGlobal.isExist(ls_Text))
				li_Count++;
		}
		return li_Count;
	}

	/**
	 * 后继步骤和收件人链表
	 */
	private ArrayList<NextActivityPara> mo_NextParas = new ArrayList<NextActivityPara>();

	/**
	 * 后继步骤和收件人链表
	 * 
	 * @return
	 */
	public ArrayList<NextActivityPara> getNextParas() {
		return mo_NextParas;
	}

	/**
	 * 根据步骤标识获取后继步骤和收件人链表
	 * 
	 * @param al_ActId
	 * @return
	 */
	public NextActivityPara getNextParaByActId(int al_ActId) {
		for (NextActivityPara lo_Part : mo_NextParas) {
			if (lo_Part.getActivityID() == al_ActId)
				return lo_Part;
		}
		return null;
	}

	/**
	 * 增加步骤和后继收件人
	 * 
	 * @param ai_TransactType
	 *            处理类型：=1-正常发送；=2-正常转发；=4-内部传阅；
	 * @param ai_Type
	 *            选择类型：=-1-不选；=0-必选；=1-可选；
	 * @param al_ActivityID
	 *            步骤标识
	 * @param as_ActivityName
	 *            步骤名称
	 * @param ai_RangeType
	 *            用户选择范围类型：=0-全体成员；=1-有范围的；
	 * @param as_Range
	 *            用户选择范围，当ai_RangeType=1时有效
	 */
	public void addNextActivityPara(int ai_TransactType, int ai_Type, int al_ActivityID, String as_ActivityName, int ai_RangeType, String as_Range,
			int ai_MaxLimitedNum) {
		NextActivityPara lo_Part = getNextParaByActId(al_ActivityID);
		if (lo_Part == null) {
			lo_Part = new NextActivityPara();
			lo_Part.setTransactType(ai_TransactType);
			mo_NextParas.add(lo_Part);

			lo_Part.setType(al_ActivityID == 1 ? -1 : ai_Type);
			lo_Part.setActivityID(al_ActivityID);
			lo_Part.setActivityName(as_ActivityName);
			lo_Part.setRangeType(ai_RangeType);
			lo_Part.setRange(as_Range);
			lo_Part.setMaxLimitedNum(ai_MaxLimitedNum);
		} else {
			if ((lo_Part.getTransactType() & ai_TransactType) == 0)
				lo_Part.setTransactType(lo_Part.getTransactType() + ai_TransactType);
		}
	}

	/**
	 * 获取后继步骤需要选择人员的步骤参数链表
	 * 
	 * @param ai_Type
	 *            处理类型：=1-正常发送；=2-正常转发；=4-内部传阅；
	 * @return
	 */
	public ArrayList<NextActivityPara> getNextParasByType(int ai_Type) {
		ArrayList<NextActivityPara> lo_Parts = new ArrayList<NextActivityPara>();
		for (NextActivityPara lo_Part : mo_NextParas) {
			if ((lo_Part.getTransactType() & ai_Type) == ai_Type) {
				if (lo_Part.getType() > -1)
					lo_Parts.add(lo_Part);
			}
		}
		return lo_Parts;
	}

	/**
	 * 发送时需要选取收件人的步骤标识连接串，使用【;】分隔
	 */
	private String ms_SelectParticipantActivityIDs = "";

	/**
	 * 发送时需要选取收件人的步骤标识连接串，使用【;】分隔
	 * 
	 * @return
	 */
	public String getSelectParticipantActivityIDs() {
		return ms_SelectParticipantActivityIDs;
	}

	/**
	 * 发送时需要选取收件人的步骤标识连接串，使用【;】分隔
	 * 
	 * @param value
	 */
	public void setSelectParticipantActivityIDs(String value) {
		ms_SelectParticipantActivityIDs = value;
	}

	/**
	 * 是否需要内部处理动作
	 */
	private boolean mb_InsideReading = false;

	/**
	 * 是否需要内部处理动作
	 * 
	 * @return
	 */
	public boolean getInsideReading() {
		return mb_InsideReading;
	}

	/**
	 * 是否需要内部处理动作
	 * 
	 * @param value
	 */
	public void setInsideReading(boolean value) {
		mb_InsideReading = value;
		if (value) {
			mo_InsideReadingPara = new NextActivityPara();
		} else {
			mo_InsideReadingPara = null;
		}
	}

	/**
	 * 内部传阅（会签）标题
	 */
	private String ms_ReadLabel = "传阅";

	/**
	 * 内部传阅（会签）标题
	 * 
	 * @return
	 */
	public String getReadLabel() {
		return ms_ReadLabel;
	}

	/**
	 * 内部传阅（会签）标题
	 * 
	 * @param value
	 */
	public void setReadLabel(String value) {
		if (MGlobal.isExist(value))
			ms_ReadLabel = value;
	}

	/**
	 * 需要内部处理时当前步骤的选取动作，当mb_InsideReading=true时不为空
	 */
	private NextActivityPara mo_InsideReadingPara = null;

	/**
	 * 需要内部处理时当前步骤的选取动作
	 * 
	 * @return
	 */
	public NextActivityPara getInsideReadingPara() {
		return mo_InsideReadingPara;
	}

	/**
	 * 是否需要转发处理动作
	 */
	private boolean mb_LapseTo = false;

	/**
	 * 是否需要转发处理动作
	 * 
	 * @return
	 */
	public boolean getLapseTo() {
		return mb_LapseTo;
	}

	/**
	 * 是否需要转发处理动作
	 * 
	 * @param value
	 */
	public void setLapseTo(boolean value) {
		mb_LapseTo = value;
	}

	/**
	 * 转发步骤数目限制 n=-1 选择任意多个步骤转发 n=0 缺省使用多个步骤转发 n>0 使用1到N个步骤转发
	 */
	private int mi_TransActivityNum = 1;

	/**
	 * 转发步骤数目限制 n=-1 选择任意多个步骤转发 n=0 缺省使用多个步骤转发 n>0 使用1到N个步骤转发
	 * 
	 * @return
	 */
	public int getTransActivityNum() {
		return mi_TransActivityNum;
	}

	/**
	 * 转发步骤数目限制 n=-1 选择任意多个步骤转发 n=0 缺省使用多个步骤转发 n>0 使用1到N个步骤转发
	 * 
	 * @param value
	 */
	public void setTransActivityNum(int value) {
		mi_TransActivityNum = value;
	}

	/**
	 * 转发标题
	 */
	private String ms_LapseToLabel = "转发";

	/**
	 * 转发标题
	 * 
	 * @return
	 */
	public String getLapseToLabel() {
		return ms_LapseToLabel;
	}

	/**
	 * value
	 * 
	 * @param value
	 */
	public void setLapseToLabel(String value) {
		if (MGlobal.isExist(value))
			ms_LapseToLabel = value;
	}

	/**
	 * 已流转步骤标识连接串，多个使用【;】分隔
	 */
	private String ms_FlowedActivityIDs = "";

	/**
	 * 已流转步骤标识连接串，多个使用【;】分隔
	 * 
	 * @return
	 */
	public String getFlowedActivityIDs() {
		return ms_FlowedActivityIDs;
	}

	/**
	 * 已流转步骤标识连接串，多个使用【;】分隔
	 * 
	 * @param value
	 */
	public void setFlowedActivityIDs(String value) {
		ms_FlowedActivityIDs = value;
	}

	/**
	 * 送达步骤标识
	 */
	private int ml_ServiceActivityID = 0;

	/**
	 * 送达步骤标识
	 * 
	 * @return
	 */
	public int getServiceActivityID() {
		return ml_ServiceActivityID;
	}

	/**
	 * 送达步骤标识
	 * 
	 * @param value
	 */
	public void setServiceActivityID(int value) {
		ml_ServiceActivityID = value;
	}

	/**
	 * 指定转发步骤的步骤标识连接串
	 */
	private String ms_SendToActivityIDs = "";

	/**
	 * 指定转发步骤的步骤标识连接串
	 * 
	 * @return
	 */
	public String getSendToActivityIDs() {
		return ms_SendToActivityIDs;
	}

	/**
	 * 指定转发步骤的步骤标识连接串
	 * 
	 * @param value
	 */
	public void setSendToActivityIDs(String value) {
		ms_SendToActivityIDs = value;
	}

	/**
	 * 指定转发步骤中需要选取收件人的步骤标识连接串
	 */
	private String ms_SendToParActivityIDs = "";

	/**
	 * 指定转发步骤中需要选取收件人的步骤标识连接串
	 * 
	 * @return
	 */
	public String getSendToParActivityIDs() {
		return ms_SendToParActivityIDs;
	}

	/**
	 * 指定转发步骤中需要选取收件人的步骤标识连接串
	 * 
	 * @param value
	 */
	public void setSendToParActivityIDs(String value) {
		ms_SendToParActivityIDs = value;
	}

	/**
	 * 是否需要转发到特殊步骤
	 */
	private boolean mb_ExistSpecialActivity = false;

	/**
	 * 是否需要转发到特殊步骤
	 * 
	 * @return
	 */
	public boolean getExistSpecialActivity() {
		return mb_ExistSpecialActivity;
	}

	/**
	 * 是否需要转发到特殊步骤
	 * 
	 * @param value
	 */
	public void setExistSpecialActivity(boolean value) {
		mb_ExistSpecialActivity = value;
	}

	/**
	 * 是否需要在当前步骤前添加一新步骤
	 */
	private boolean mb_ExistPreActivity = false;

	/**
	 * 是否需要在当前步骤前添加一新步骤
	 * 
	 * @return
	 */
	public boolean getExistPreActivity() {
		return mb_ExistPreActivity;
	}

	/**
	 * 是否需要在当前步骤前添加一新步骤
	 * 
	 * @param value
	 */
	public void setExistPreActivity(boolean value) {
		mb_ExistPreActivity = value;
	}

	/**
	 * 是否需要在当前步骤后添加一新步骤
	 */
	private boolean mb_ExistNextActivity = false;

	/**
	 * 是否需要在当前步骤后添加一新步骤
	 * 
	 * @return
	 */
	public boolean getExistNextActivity() {
		return mb_ExistNextActivity;
	}

	/**
	 * 是否需要在当前步骤后添加一新步骤
	 * 
	 * @param value
	 */
	public void setExistNextActivity(boolean value) {
		mb_ExistNextActivity = value;
	}

	/**
	 * 导出到Json内容
	 * 
	 * @return
	 */
	public String toJson() {
		return JSON.toJSONString(this);
	}

	/**
	 * 从Json内容导入
	 * 
	 * @return
	 */
	public static InnerWorkItem fromJson(String as_Text) {
		return JSON.toJavaObject(JSON.parseObject(as_Text), InnerWorkItem.class);
	}

}
