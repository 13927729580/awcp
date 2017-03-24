package org.szcloud.framework.workflow.core.entity;

import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.w3c.dom.Element;

/**
 * 把需要远程传输处理的文件添加到数据库中
 * 
 * @author Jackie.Wang
 * 
 */
public class CFileExchange {

	/**
	 * 处理的公文实例对象
	 */
	private CWorkItem mo_WorkItem = null;

	/**
	 * 处理的公文实例对象
	 * 
	 * @return
	 */
	public CWorkItem getWorkItem() {
		return mo_WorkItem;
	}

	/**
	 * 远程启动结果集
	 */
	private List<Map<String, Object>> mo_Launch = null;

	/**
	 * 远程启动结果集
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getLaunch() {
		return mo_Launch;
	}

	/**
	 * 文件列表结果集
	 */
	private List<Map<String, Object>> mo_FileInfo = null;

	/**
	 * 文件列表结果集
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getFileInfo() {
		return mo_FileInfo;
	}

	/**
	 * 文件接收服务器结果集
	 */
	private List<Map<String, Object>> mo_RecieveServer = null;

	/**
	 * 文件接收服务器结果集
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getRecieveServer() {
		return mo_RecieveServer;
	}

	/**
	 * 文件接收服务器结果集
	 */
	private List<Map<String, Object>> mo_Servers = null;

	/**
	 * 文件接收服务器结果集
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getServers() {
		return mo_Servers;
	}

	/**
	 * 当前服务器代码
	 */
	private String ms_CurServerCode = "";

	/**
	 * 当前服务器代码
	 * 
	 * @return
	 */
	public String getCurServerCode() {
		return ms_CurServerCode;
	}

	/**
	 * 初始化
	 * 
	 * @param ao_WorkItem
	 * @param ai_Type
	 */
	public CFileExchange(CWorkItem ao_WorkItem, int ai_Type) {
		mo_WorkItem = ao_WorkItem;

		if (ai_Type == 0)
			mo_Launch = CSqlHandle.getJdbcTemplate().queryForList(
					"SELECT * FROM LaunchInfo WHERE 1=2", false);
		mo_FileInfo = CSqlHandle.getJdbcTemplate().queryForList(
				"SELECT * FROM FILE_INFO_LIST WHERE 1=2", false);
		mo_RecieveServer = CSqlHandle.getJdbcTemplate().queryForList(
				"SELECT * FROM RECIEVE_SRV_LIST WHERE 1=2", false);
		mo_Servers = CSqlHandle.getJdbcTemplate().queryForList(
				"SELECT SRV_ID, SRV_CODE, SRV_NAME FROM FILE_SRV_DEF");
		ms_CurServerCode = mo_WorkItem.Logon.getParaValue("SERVER_CODE");
	}

	/**
	 * 注销
	 */
	public void ClearUp() {
		try {
			mo_WorkItem = null;

			if (mo_FileInfo != null) {
				mo_FileInfo = null;
			}

			if (mo_RecieveServer != null) {
				mo_RecieveServer = null;
			}
			if (mo_Servers != null) {
				mo_Servers = null;
			}
		} catch (Exception ex) {
		}
	}

	public boolean transactLaunch(String as_XmlPara) {
		return true;
	}

	public boolean transactReturnLaunch(Element ao_Node) {
		return true;
	}

}
