package cn.org.awcp.venson.service;

public interface WorkflowService {

	String defaut_flow = "001";

	/**
	 * 创建流程
	 * 
	 * @param dataId
	 *            数据Id
	 * @param user
	 *            下一步接收用户
	 * @return
	 */
	long create(String dataId, String user);

	/**
	 * 流程转发
	 * 
	 * @param workId
	 *            流程Id
	 * @param user
	 *            下一步接收用户
	 * @return
	 */
	String transfer(long workId, String user);

	/**
	 * 执行流程
	 * 
	 * @param workId
	 *            流程Id
	 * @return
	 */
	String execute(long workId);
}
