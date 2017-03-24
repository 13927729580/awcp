package org.szcloud.framework.workflow.core.base;

import java.sql.Date;

/**
 * 各种接口实体类
 */
public class CInterface {

	/**
	 * 用户标识
	 */
	public int UserId = 0;

	/**
	 * 用户代码
	 */
	public String UserCode = null;

	/**
	 * 访问Ip地址
	 */
	public String IpAddres = null;

	/**
	 * 操作类型
	 */
	private EHandleType mi_Handle = EHandleType.NotAny;

	/**
	 * 读取操作类型
	 * 
	 * @return
	 */
	public EHandleType getHandle() {
		return mi_Handle;
	}

	/**
	 * 设置操作类型
	 * 
	 * @param ai_Value
	 */
	public void setHandle(EHandleType ai_Value) {
		mi_Handle = ai_Value;
	}

	/**
	 * 执行操作结果：=true-成功；=1-失败；
	 */
	public boolean HandleResult = false;

	/**
	 * 执行位置
	 */
	public String Position = null;

	/**
	 * 错误标题
	 */
	public String ErrTitle = null;

	/**
	 * 错误说明
	 */
	public String ErrDescription = null;

	/**
	 * 布尔参数1（可以是输入或输出）
	 */
	public boolean BlnPara1 = false;

	/**
	 * 布尔参数2（可以是输入或输出）
	 */
	public boolean BlnPara2 = false;

	/**
	 * 短整型参数1（可以是输入或输出）
	 */
	public short ShtPara1 = 0;
	/**
	 * 短整型参数2（可以是输入或输出）
	 */
	public short ShtPara2 = 0;

	/**
	 * 整型参数1（可以是输入或输出）
	 */
	public int IntPara1 = 0;
	/**
	 * 整型参数2（可以是输入或输出）
	 */
	public int IntPara2 = 0;

	/**
	 * 整型参数3（可以是输入或输出）
	 */
	public int IntPara3 = 0;
	/**
	 * 整型参数4（可以是输入或输出）
	 */
	public int IntPara4 = 0;

	/**
	 * 长整型参数1（可以是输入或输出）
	 */
	public long LngPara1 = 0;
	/**
	 * 长整型参数2（可以是输入或输出）
	 */
	public long LngPara2 = 0;

	/**
	 * 单精度型参数1（可以是输入或输出）
	 */
	public float FltPara1 = 0;
	/**
	 * 单精度型参数2（可以是输入或输出）
	 */
	public float FltPara2 = 0;

	/**
	 * 双精度型参数1（可以是输入或输出）
	 */
	public double DblPara1 = 0;
	/**
	 * 双精度型参数2（可以是输入或输出）
	 */
	public double DblPara2 = 0;

	/**
	 * 日期型参数1（可以是输入或输出）
	 */
	public Date DatPara1 = null;
	/**
	 * 日期型参数2（可以是输入或输出）
	 */
	public Date DatPara2 = null;

	/**
	 * 日期型参数3（可以是输入或输出）
	 */
	public Date DatPara3 = null;
	/**
	 * 日期型参数4（可以是输入或输出）
	 */
	public Date DatPara4 = null;

	/**
	 * 字符串参数1（可以是输入或输出）
	 */
	public String StrPara1 = null;
	/**
	 * 字符串参数2（可以是输入或输出）
	 */
	public String StrPara2 = null;

	/**
	 * 字符串参数3（可以是输入或输出）
	 */
	public String StrPara3 = null;
	/**
	 * 字符串参数4（可以是输入或输出）
	 */
	public String StrPara4 = null;

	/**
	 * 初始化
	 * 
	 * @param al_UserId
	 *            用户标识
	 * @param as_IpAddress
	 *            IP地址
	 */
	public CInterface(int al_UserId, String as_IpAddress) {
		this.UserId = al_UserId;
		this.IpAddres = as_IpAddress;
	}

	/**
	 * 初始化
	 * 
	 * @param as_UserCode
	 *            用户代码
	 * @param as_IpAddress
	 *            IP地址
	 */
	public CInterface(String as_UserCode, String as_IpAddress) {
		this.UserCode = as_UserCode;
		this.IpAddres = as_IpAddress;
	}

}
