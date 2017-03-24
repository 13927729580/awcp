package org.szcloud.framework.workflow.core.business;

/**
 * 后继步骤和收件人定义
 * 
 * @author Jackie.Wang
 *
 */
public class NextActivityPara {

	/**
	 * 处理类型：=1-正常发送；=2-正常转发；=4-内部传阅；
	 */
	private int mi_TransactType = 0;

	/**
	 * 处理类型：=1-正常发送；=2-正常转发；=4-内部传阅；
	 * 
	 * @return
	 */
	public int getTransactType() {
		return mi_TransactType;
	}

	/**
	 * 处理类型：=1-正常发送；=2-正常转发；=4-内部传阅；
	 * 
	 * @param value
	 */
	public void setTransactType(int value) {
		mi_TransactType = value;
	}

	/**
	 * 选择类型：=-1-不选；=0-必选；=1-可选；
	 */
	private int mi_Type = 0;

	/**
	 * 选择类型：=-1-不选；=0-必选；=1-可选；
	 * 
	 * @return
	 */
	public int getType() {
		return mi_Type;
	}

	/**
	 * 选择类型：=-1-不选；=0-必选；=1-可选；
	 * 
	 * @param value
	 */
	public void setType(int value) {
		mi_Type = value;
	}

	/**
	 * 步骤标识
	 */
	private int ml_ActivityID = 0;

	/**
	 * 步骤标识
	 * 
	 * @return
	 */
	public int getActivityID() {
		return ml_ActivityID;
	}

	/**
	 * 步骤标识
	 * 
	 * @param value
	 */
	public void setActivityID(int value) {
		ml_ActivityID = value;
	}

	/**
	 * 步骤名称
	 */
	private String ms_ActivityName = "";

	/**
	 * 步骤名称
	 * 
	 * @return
	 */
	public String getActivityName() {
		return ms_ActivityName;
	}

	/**
	 * 步骤名称
	 * 
	 * @param value
	 */
	public void setActivityName(String value) {
		ms_ActivityName = value;
	}

	/**
	 * 用户选择范围类型：=0-全体成员；=1-有范围的；
	 */
	private int mi_RangeType = 0;

	/**
	 * 用户选择范围类型：=0-全体成员；=1-有范围的；
	 * 
	 * @return
	 */
	public int getRangeType() {
		return mi_RangeType;
	}

	/**
	 * 用户选择范围类型：=0-全体成员；=1-有范围的；
	 * 
	 * @param value
	 */
	public void setRangeType(int value) {
		mi_RangeType = value;
	}

	/**
	 * mi_RangeType=1时的用户限制范围
	 */
	private String ms_Range = "";

	/**
	 * mi_RangeType=1时的用户限制范围
	 * 
	 * @return
	 */
	public String getRange() {
		return ms_Range;
	}

	/**
	 * mi_RangeType=1时的用户限制范围
	 * 
	 * @param value
	 */
	public void setRange(String value) {
		ms_Range = value;
	}

	/**
	 * 选择人员的最大数量
	 */
	private int mi_MaxLimitedNum = 0;

	/**
	 * 选择人员的最大数量
	 * 
	 * @return
	 */
	public int getMaxLimitedNum() {
		return mi_MaxLimitedNum;
	}

	/**
	 * 选择人员的最大数量
	 * 
	 * @param value
	 */
	public void setMaxLimitedNum(int value) {
		mi_MaxLimitedNum = value;
	}

}
