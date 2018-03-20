package cn.org.awcp.venson.service;

import java.util.Map;

public interface QueryService {

	/**
	 * 创建人
	 * 
	 * @param workId
	 * @return
	 */
	Map<String, Object> getStarter(String workId);
	/**
	 * 由我创建
	 *
	 * @param limit
	 * @param offset
	 * @param FK_Flow
	 * @param workItemName
	 * @param userName
	 * @return
	 */
	Map<String, Object> getCreateByMeData(int limit, int offset, String FK_Flow, String workItemName, String userName);

	/**
	 * 待处理件
	 * 
	 * @param limit
	 * @param offset
	 * @param includeFlow
	 * @param excludeFlow
	 * @param workItemName
	 * @param userName
	 * @param hasReturn
	 *            是否包括退回件
	 * @return
	 */
	Map<String, Object> getUntreatedData(int limit, int offset, String[] includeFlow,String[] excludeFlow, String workItemName, String userName,
			boolean hasReturn);

	/**
	 * 退回件
	 * 
	 * @param limit
	 * @param offset
	 * @param FK_Flow
	 * @param workItemName
	 * @param userName
	 * @return
	 */
	Map<String, Object> getReturnData(int limit, int offset, String FK_Flow, String workItemName, String userName);

	/**
	 * 已处理件
	 * 
	 * @param limit
	 * @param offset
	 * @param FK_Flow
	 * @param workItemName
	 * @param userName
	 * @return
	 */
	Map<String, Object> getHandledData(int limit, int offset, String FK_Flow, String workItemName, String userName);

	/**
	 * 所有件
	 * 
	 * @param limit
	 * @param offset
	 * @param FK_Flow
	 * @param workItemName
	 * @param userName
	 * @return
	 */
	Map<String, Object> getAllData(int limit, int offset, String FK_Flow, String workItemName, String userName);

	/**
	 * 归档件
	 * 
	 * @param limit
	 * @param offset
	 * @param FK_Flow
	 * @param workItemName
	 * @param userName
	 * @param emps
	 * @return
	 */
	Map<String, Object> getCompileData(int limit, int offset, String FK_Flow, String workItemName, String userName);

}
