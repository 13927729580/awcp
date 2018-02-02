package BP.En;

/**
 * DDLShowType
 */
public enum Dot2DotModel
{
	/**
	 * None
	 */
	None,
	/**
	 * Gender
	 */
	Gender,
	/**
	 * Boolean
	 */
	Boolean,
	/** 
	*/
	SysEnum,
	/**
	 * Self
	 */
	Self,
	/**
	 * 实体集合
	 */
	Ens,
	/**
	 * 与Table 相关联
	 */
	BindTable;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static Dot2DotModel forValue(int value)
	{
		return values()[value];
	}
}