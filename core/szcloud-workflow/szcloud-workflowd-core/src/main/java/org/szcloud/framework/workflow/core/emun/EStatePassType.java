package org.szcloud.framework.workflow.core.emun;

//状态数据传递类型
public class EStatePassType {
	public static final int NotPassStateData = 0; //不传递状态内容
	public static final int PassStateDataAsEntry = 1; //作为一个状态传递(在表现时可直接显示整体)
	public static final int PassStateDataAsState = 2; //作为状态数据传递
}
