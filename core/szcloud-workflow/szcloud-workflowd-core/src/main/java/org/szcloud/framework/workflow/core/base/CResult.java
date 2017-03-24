package org.szcloud.framework.workflow.core.base;

import java.text.MessageFormat;

import org.szcloud.framework.workflow.core.module.MGlobal;

/**
 * 执行结果实体类
 * 
 * @author Jackie.Wang
 * @version
 */
public class CResult {

	/**
	 * 执行结果
	 */
	public boolean Result = false;

	/**
	 * 执行结果值
	 */
	public int ResultValue = 0;

	/**
	 * 如果执行错误时的错误编码
	 */
	public int ErrorId = 0;

	/**
	 * 执行位置
	 */
	public String Position = null;

	/**
	 * 错误标题
	 */
	public String ErrorTitle = null;

	/**
	 * 错误内容
	 */
	public String ErrorContent = null;

	/**
	 * 执行正确的提示信息
	 */
	public String Information = null;

	/**
	 * 其他信息1
	 */
	public String Other1 = null;

	/**
	 * 其他信息2
	 */
	public String Other2 = null;

	/**
	 * 其他信息3
	 */
	public String Other3 = null;

	/**
	 * 获取错误处理结果
	 * 
	 * @param al_ErrorId
	 *            错误编号
	 * @param ao_Base
	 *            发生对象
	 * @param as_Position
	 *            产生位置
	 * @param as_Parameter
	 *            执行引起参数
	 * @throws Exception
	 */
	public static CResult getResult(int al_ErrorId, CBase ao_Base, String as_Position, String as_Parameter) {
		CResult lo_Result = new CResult();
		lo_Result.ErrorId = al_ErrorId;
		lo_Result.ErrorTitle = as_Position;
		if (ao_Base == null)
			lo_Result.Position = as_Position;
		else
			lo_Result.Position = ao_Base.getClass().getName() + "->" + as_Position;
		lo_Result.ErrorContent = MessageFormat.format("错误号：{0}，位置：{1}，内容：{2}", String.valueOf(al_ErrorId), as_Position, as_Parameter);
		return lo_Result;
	}

	/**
	 * 获取错误处理结果
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
	public static CResult getResult(Exception ao_Exception, CBase ao_Base, String as_Position, String as_Parameter) {
		CResult lo_Result = new CResult();
		lo_Result.ErrorId = 9999;
		lo_Result.ErrorTitle = as_Position;
		if (ao_Base == null)
			lo_Result.Position = as_Position;
		else
			lo_Result.Position = ao_Base.getClass().getName() + "->" + as_Position;
		if (MGlobal.isEmpty(as_Parameter))
			lo_Result.ErrorContent = ao_Exception.toString();
		else
			lo_Result.ErrorContent = as_Parameter;
		return lo_Result;
	}

	/**
	 * 获取正确的处理结果
	 * 
	 * @return
	 */
	public static CResult getResult() {
		return getResult(null, "", 0, null, null, null, null);
	}

	/**
	 * 获取正确的处理结果
	 * 
	 * @param as_Information
	 *            结果消息
	 * @return
	 */
	public static CResult getResult(String as_Information) {
		return getResult(null, "", 0, as_Information, null, null, null);
	}

	/**
	 * 获取正确的处理结果
	 * 
	 * @param ai_Result
	 *            处理结果
	 * @param as_Information
	 *            结果消息
	 * @return
	 */
	public static CResult getResult(int ai_Result, String as_Information) {
		return getResult(null, "", ai_Result, as_Information, null, null, null);
	}

	/**
	 * 获取正确的处理结果
	 * 
	 * @param ao_Base
	 *            发生对象
	 * @param as_Position
	 *            产生位置
	 * @param as_Information
	 *            结果消息
	 * @return
	 */
	public static CResult getResult(CBase ao_Base, String as_Position, String as_Information) {
		return getResult(ao_Base, as_Position, 0, as_Information, null, null, null);
	}

	/**
	 * 获取正确的处理结果
	 * 
	 * @param ao_Base
	 *            发生对象
	 * @param as_Position
	 *            产生位置
	 * @param ai_Result
	 *            处理结果
	 * @param as_Information
	 *            结果消息
	 * @return
	 */
	public static CResult getResult(CBase ao_Base, String as_Position, int ai_Result, String as_Information) {
		return getResult(ao_Base, as_Position, ai_Result, as_Information, null, null, null);
	}

	/**
	 * 获取正确的处理结果
	 * 
	 * @param ao_Base
	 *            发生对象
	 * @param as_Position
	 *            产生位置
	 * @param ai_Result
	 *            处理结果
	 * @param as_Information
	 *            结果消息
	 * @param as_Other1
	 *            其他消息1
	 * @param as_Other2
	 *            其他消息2
	 * @param as_Other3
	 *            其他消息3
	 * @return
	 */
	public static CResult getResult(CBase ao_Base, String as_Position, int ai_Result, String as_Information, String as_Other1, String as_Other2,
			String as_Other3) {
		CResult lo_Result = new CResult();
		if (ao_Base == null)
			lo_Result.Position = as_Position;
		else
			lo_Result.Position = ao_Base.getClass().getName() + "->" + as_Position;
		lo_Result.Result = true;
		lo_Result.ResultValue = ai_Result;
		lo_Result.Information = as_Information;
		lo_Result.Other1 = as_Other1;
		lo_Result.Other2 = as_Other2;
		lo_Result.Other3 = as_Other3;
		return lo_Result;
	}

}
