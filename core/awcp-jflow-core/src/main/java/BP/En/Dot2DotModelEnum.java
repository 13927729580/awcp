package BP.En;

public enum Dot2DotModelEnum
{
		/** 
		 默认模式
		*/
		Default,
		/** 
		 树模式
		*/
		TreeDept,
		/** 
		 树叶子模式
		*/
		TreeDeptEmp;

		public int getValue()
		{
			return this.ordinal();
		}

		public static Dot2DotModelEnum forValue(int value)
		{
			return values()[value];
		}
}
