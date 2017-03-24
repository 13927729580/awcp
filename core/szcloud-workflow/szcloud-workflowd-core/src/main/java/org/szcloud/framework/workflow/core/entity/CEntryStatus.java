package org.szcloud.framework.workflow.core.entity;

/**
 * 状态处理情况
 *
 * @author Jackie.Wang
 *
 */
public class CEntryStatus {
	
	/**
	 * 是否未处理
	 */
	public boolean NotTransact = false;
	/**
	 * 是否处理中
	 */
	public boolean Transacting = false;
	/**
	 * 是否已处理
	 */
	public boolean HadTransact = false;
	/**
	 * 是否已过期
	 */
	public boolean HadOverdue = false;
	/**
	 * 是否已撤回
	 */
	public boolean HadRecall = false;

}
