package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 附件删除规则
 
*/
public enum AthDeleteWay
{
	/** 
	 不删除 0
	 
	*/
	None,
	/** 
	 删除所有 1
	 
	*/
	DelAll,
	/** 
	 只删除自己上传 2
	 
	*/
	DelSelf;

	public int getValue()
	{
		return this.ordinal();
	}

	public static AthDeleteWay forValue(int value)
	{
		return values()[value];
	}
}