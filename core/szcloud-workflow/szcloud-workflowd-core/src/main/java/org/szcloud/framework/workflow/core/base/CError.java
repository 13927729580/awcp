package org.szcloud.framework.workflow.core.base;

import java.text.MessageFormat;

import org.szcloud.framework.workflow.core.module.MGlobal;

/**
 * 错误处理基础类
 */
public class CError {

	/**
	 * 储存记录和错误内容
	 */
	private StringBuffer mo_Message = null;

	/**
	 * 获取储存记录和错误内容
	 * 
	 * @return
	 */
	public StringBuffer Message() {
		return mo_Message;
	}

	/**
	 * 初始化
	 */
	CError() {
		mo_Message = new StringBuffer();
	}

	/**
	 * 注销
	 */
	public void ClearUp() {
		mo_Message = null;
	}

	/**
	 * 清除消息内容
	 */
	public void clearMessage() {
		mo_Message = new StringBuffer();
	}

	/**
	 * 获取消息内容
	 * 
	 * @return
	 */
	public String getMessage() {
		return mo_Message.toString();
	}

	/**
	 * 错误处理函数
	 * 
	 * @param al_ErrorId
	 *            错误编号
	 * 
	 * @param ao_Base
	 *            发生对象
	 * 
	 * @param as_Position
	 *            产生位置
	 * 
	 * @param as_Parameter
	 *            执行引起参数
	 * @throws Exception
	 */
	public void Raise(int al_ErrorId, CBase ao_Base, String as_Position, String as_Parameter) throws Exception {
		// 执行错误信息
		// ...
		// 记录错误内容
		mo_Message.append(getError(al_ErrorId, ao_Base.getClass().getName() + "::" + as_Position, as_Parameter));
		// throw new Exception(MessageFormat.format("错误号：{0}，位置：{1}，内容：{2}",
		// String.valueOf(al_ErrorId), as_Position, as_Parameter));
	}

	/**
	 * 错误处理函数
	 * 
	 * @param ao_Exception
	 *            错误对象
	 * 
	 * @param ao_Base
	 *            发生对象
	 * 
	 * @param as_Position
	 *            产生位置
	 * 
	 * @param as_Parameter
	 *            执行引起参数
	 * @throws Exception
	 */
	public void Raise(Exception ao_Exception, CBase ao_Base, String as_Position, String as_Parameter) throws Exception {
		// 执行错误信息
		ao_Exception.printStackTrace();
		// ...
		// 记录错误内容
		mo_Message.append(getError(ao_Exception, ao_Base.getClass().getName() + "::" + as_Position, as_Parameter));
		// throw ao_Exception;
	}

	/**
	 * 获取错误处理内容
	 * 
	 * @param al_ErrorId
	 *            错误编号
	 * @param as_Position
	 *            产生位置
	 * @param as_Parameter
	 *            执行引起参数
	 */
	public static String getError(int al_ErrorId, String as_Position, String as_Parameter) {
		String ls_Text = "时间：{0} 编号：{1} 位置：{2} 参数：{3}\n";
		return MessageFormat.format(ls_Text, MGlobal.dateToString(MGlobal.getNow()), String.valueOf(al_ErrorId),
				as_Position, as_Parameter);
	}

	/**
	 * 获取日志内容
	 * 
	 * @param as_Position
	 *            产生位置
	 * @param as_Parameter
	 *            执行引起参数
	 */
	public static String getLog(String as_Position, String as_Parameter) {
		String ls_Text = "记录时间：{0} 位置：{1} 参数：{2}\n";
		return MessageFormat.format(ls_Text, MGlobal.dateToString(MGlobal.getNow()), as_Position, as_Parameter);
	}

	/**
	 * 获取错误处理内容
	 * 
	 * @param ao_Exception
	 *            错误对象
	 * @param ao_Base
	 *            发生对象
	 * @param as_Position
	 *            产生位置
	 * @param as_Parameter
	 *            执行引起参数
	 */
	public static String getError(Exception ao_Exception, String as_Position, String as_Parameter) {
		String ls_Text = "时间：{0} 位置：{1} 参数：{2} 错误：{3}\n";
		return MessageFormat.format(ls_Text, MGlobal.dateToString(MGlobal.getNow()), as_Position, as_Parameter,
				ao_Exception.getMessage());
	}

	/**
	 * 记录错误
	 * 
	 * @param al_ErrorId
	 *            错误编号
	 * 
	 * @param ao_Base
	 *            发生对象
	 * 
	 * @param as_Position
	 *            产生位置
	 * 
	 * @param as_Parameter
	 *            执行引起参数
	 */
	public void Record(int al_ErrorId, CBase ao_Base, String as_Position, String as_Parameter) {
		mo_Message.append(getError(al_ErrorId, ao_Base.getClass().getName() + "::" + as_Position, as_Parameter));
	}

	/**
	 * 记录错误
	 * 
	 * @param ao_Exception
	 *            错误对象
	 * 
	 * @param ao_Base
	 *            发生对象
	 * 
	 * @param as_Position
	 *            产生位置
	 * 
	 * @param as_Parameter
	 *            执行引起参数
	 */
	public void Record(Exception ao_Exception, CBase ao_Base, String as_Position, String as_Parameter) {
		mo_Message.append(getError(ao_Exception, ao_Base.getClass().getName() + "::" + as_Position, as_Parameter));
	}

	/**
	 * 记录日志
	 * 
	 * @param ao_Base
	 *            发生对象
	 * 
	 * @param as_Position
	 *            产生位置
	 * 
	 * @param as_Parameter
	 *            执行引起参数
	 */
	public void Record(CBase ao_Base, String as_Position, String as_Parameter) {
		mo_Message.append(getLog(ao_Base.getClass().getName() + "::" + as_Position, as_Parameter));
	}

}
