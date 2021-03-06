package BP.WF;

/** 
 子线程启动方式
 
*/
public enum SubFlowStartWay
{
	/** 
	 不启动
	 
	*/
	None,
	/** 
	 按表单字段
	 
	*/
	BySheetField,
	/** 
	 按从表数据
	 
	*/
	BySheetDtlTable;

	public int getValue()
	{
		return this.ordinal();
	}

	public static SubFlowStartWay forValue(int value)
	{
		return values()[value];
	}
}