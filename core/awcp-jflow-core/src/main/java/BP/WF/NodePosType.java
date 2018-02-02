package BP.WF;

/** 
 位置类型
 
*/
public enum NodePosType
{
	/** 
	 开始
	 
	*/
	Start,
	/** 
	 中间
	 
	*/
	Mid,
	/** 
	 结束
	 
	*/
	End;

	public int getValue()
	{
		return this.ordinal();
	}

	public static NodePosType forValue(int value)
	{
		return values()[value];
	}
}