package org.szcloud.framework.workflow.core.base;

import java.util.ArrayList;

/**
 * 部门对象
 * 
 * @author Jackie.Wang
 *
 */
public class CDept {

	/**
	 * 部门标识
	 */
	public int Id = 0;

	/**
	 * 部门名称
	 */
	public String Name = null;

	/**
	 * 上级部门标识
	 */
	public int PId = 0;

	/**
	 * 子部门数组
	 */
	public ArrayList<CDept> Children = new ArrayList<CDept>();

	/**
	 * 所属的上级部门
	 */
	public CDept Parent = null;

}
