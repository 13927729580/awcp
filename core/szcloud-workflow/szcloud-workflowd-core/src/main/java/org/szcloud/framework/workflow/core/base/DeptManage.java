package org.szcloud.framework.workflow.core.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.module.MGlobal;

/**
 * 部门管理对象
 * 
 * @author Jackie.Wang
 *
 */
public class DeptManage {

	/**
	 * 包含的所有部门对象
	 */
	public HashMap<Integer, CDept> Depts = new HashMap<Integer, CDept>();

	/**
	 * 初始化
	 * 
	 * @param ao_Set
	 */
	public DeptManage(List<Map<String, Object>> ao_Set) {
		try {
			/*while (ao_Set.next()) {
				CDept lo_Dept = new CDept();
				lo_Dept.Id = ao_Set.getInt("DeptID");
				lo_Dept.Name = ao_Set.getString("DeptName");
				if (ao_Set.getObject("ParentID") != null)
					lo_Dept.PId = ao_Set.getInt("ParentID");
				this.Depts.put(lo_Dept.Id, lo_Dept);
			}*/
			for(int i = 0; i < ao_Set.size(); i++){
				CDept lo_Dept = new CDept();
				lo_Dept.Id = (Integer) ao_Set.get(i).get("DeptID");
				if (ao_Set.get(i).get("ParentID") != null)
					lo_Dept.PId = (Integer) ao_Set.get(i).get("ParentID");
				this.Depts.put(lo_Dept.Id, lo_Dept);
			}
			orderDept();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Set
	 */
	public DeptManage(CLogon ao_Logon) {
		try {
			String ls_Sql = "SELECT DeptID, DeptName, ParentID FROM DeptInfo";
			List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
//			while (lo_Set.next()) {
			for(int i = 0; i < lo_Set.size(); i++){
				CDept lo_Dept = new CDept();
				lo_Dept.Id = (Integer) lo_Set.get(i).get("DeptID");
				if (lo_Set.get(i).get("ParentID") != null)
					lo_Dept.PId = (Integer) lo_Set.get(i).get("ParentID");
				this.Depts.put(lo_Dept.Id, lo_Dept);
			}
			orderDept();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 排列部门结构
	 */
	private void orderDept() {
		for (CDept lo_Dept : this.Depts.values()) {
			if (lo_Dept.PId > 0) {
				CDept lo_Parent = this.Depts.get(lo_Dept.PId);
				if (lo_Parent != null) {
					lo_Parent.Children.add(lo_Dept);
					lo_Dept.Parent = lo_Parent;
				}
			}
		}
	}

	/**
	 * 获取某一部门的子部门的标识
	 * 
	 * @param al_DeptID
	 *            部门标识
	 * @param ab_IncludeSelf
	 *            是否包含自己
	 * @return
	 */
	public String getChildIds(int al_DeptID, boolean ab_IncludeSelf) {
		String ls_Ids = getChildIds(al_DeptID, ",", ab_IncludeSelf);
		if (MGlobal.right(ls_Ids, 1).equals(","))
			ls_Ids = ls_Ids.substring(0, ls_Ids.length()-1);
		return ls_Ids;
  	}
	
	/**
	 * 获取某一部门的子部门的标识
	 * 
	 * @param al_DeptID
	 *            部门标识
	 * @param as_Split
	 *            分隔符号
	 * @param ab_IncludeSelf
	 *            是否包含自己
	 * @return
	 */
	public String getChildIds(int al_DeptID, String as_Split, boolean ab_IncludeSelf) {
		CDept lo_Dept = this.Depts.get(al_DeptID);
		if (lo_Dept == null)
			return "";
		return getChildIds(lo_Dept, as_Split, ab_IncludeSelf);
	}

	/**
	 * 获取某一部门的子部门的标识
	 * 
	 * @param ao_Dept
	 *            部门对象
	 * @param as_Split
	 *            分隔符号
	 * @param ab_IncludeSelf
	 *            是否包含自己
	 * @return
	 */
	public static String getChildIds(CDept ao_Dept, String as_Split, boolean ab_IncludeSelf) {
		String ls_Ids = (ab_IncludeSelf ? String.valueOf(ao_Dept.Id) + as_Split : "");
		for (CDept lo_Child : ao_Dept.Children) {
			ls_Ids += getChildIds(lo_Child, as_Split, true);
		}
		return ls_Ids;
	}

	/**
	 * 获取某一部门的所有部门(包括上下级)的标识
	 * 
	 * @param al_DeptID
	 *            部门标识
	 * @return
	 */
	public String getAllIds(int al_DeptID) {
		String ls_Ids = getAllIds(al_DeptID, ",");
		if (MGlobal.right(ls_Ids, 1).equals(","))
			ls_Ids = ls_Ids.substring(0, ls_Ids.length()-1);
		return ls_Ids;
	}
	
	/**
	 * 获取某一部门的所有部门(包括上下级)的标识
	 * 
	 * @param al_DeptID
	 *            部门标识
	 * @param as_Split
	 *            分隔符号
	 * @return
	 */
	public String getAllIds(int al_DeptID, String as_Split) {
		CDept lo_Dept = this.Depts.get(al_DeptID);
		if (lo_Dept == null)
			return "";
		return getAllIds(lo_Dept, as_Split);
	}

	/**
	 * 获取某一部门的所有部门(包括上下级)的标识
	 * 
	 * @param ao_Dept
	 *            部门对象
	 * @param as_Split
	 *            分隔符号
	 * @return
	 */
	public static String getAllIds(CDept ao_Dept, String as_Split) {
		CDept lo_Parent = ao_Dept.Parent;
		String ls_Ids = "";
		while (lo_Parent != null) {
			ls_Ids = String.valueOf(lo_Parent.Id) + as_Split + ls_Ids;
			lo_Parent = lo_Parent.Parent;
		}
		ls_Ids += getChildIds(ao_Dept, as_Split, true);
		return ls_Ids;
	}

	/**
	 * 获取同一级别部门及他们的子部门标识的连接串
	 * 
	 * @param al_DeptID
	 *            部门标识
	 * @param as_Split
	 *            分隔符号
	 * @return
	 */
	public String getSameLevelAllIds(int al_DeptID, String as_Split) {
		CDept lo_Dept = this.Depts.get(al_DeptID);
		if (lo_Dept == null)
			return "";
		return getSameLevelAllIds(lo_Dept, as_Split);
	}

	/**
	 * 获取同一级别部门及他们的子部门标识的连接串
	 * 
	 * @param ao_Dept
	 *            部门对象
	 * @param as_Split
	 *            分隔符号
	 * @return
	 */
	public static String getSameLevelAllIds(CDept ao_Dept, String as_Split) {
		if (ao_Dept.Parent == null)
			return getChildIds(ao_Dept, as_Split, true);
		String ls_Ids = "";
		for (CDept lo_Dept : ao_Dept.Parent.Children) {
			ls_Ids += getChildIds(lo_Dept, as_Split, true);
		}
		return ls_Ids;
	}

	/**
	 * 获取某一部门的所有上级部门的标识
	 * 
	 * @param al_DeptID
	 *            部门标识
	 * @param ab_IncludeSelf
	 *            是否包含自己
	 * @return
	 */
	public String getParentIds(int al_DeptID, boolean ab_IncludeSelf) {
		String ls_Ids = getParentIds(al_DeptID, ",", ab_IncludeSelf);
		if (MGlobal.right(ls_Ids, 1).equals(","))
			ls_Ids = ls_Ids.substring(0, ls_Ids.length()-1);
		return ls_Ids;
	}
	
	/**
	 * 获取某一部门的所有上级部门的标识
	 * 
	 * @param al_DeptID
	 *            部门标识
	 * @param as_Split
	 *            分隔符号
	 * @param ab_IncludeSelf
	 *            是否包含自己
	 * @return
	 */
	public String getParentIds(int al_DeptID, String as_Split, boolean ab_IncludeSelf) {
		CDept lo_Dept = this.Depts.get(al_DeptID);
		if (lo_Dept == null)
			return "";
		return getParentIds(lo_Dept, as_Split, ab_IncludeSelf);
	}

	/**
	 * 获取某一部门的所有上级部门的标识
	 * 
	 * @param ao_Dept
	 *            部门对象
	 * @param as_Split
	 *            分隔符号
	 * @param ab_IncludeSelf
	 *            是否包含自己
	 * @return
	 */
	public static String getParentIds(CDept ao_Dept, String as_Split, boolean ab_IncludeSelf) {
		String ls_Ids = (ab_IncludeSelf ? String.valueOf(ao_Dept.Id) + as_Split : "");
		CDept lo_Parent = ao_Dept.Parent;
		while (lo_Parent != null) {
			ls_Ids = String.valueOf(lo_Parent.Id) + as_Split + ls_Ids;
			lo_Parent = lo_Parent.Parent;
		}
		
		return ls_Ids;
	}
}
