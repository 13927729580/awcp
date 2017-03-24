package org.szcloud.framework.workflow.core.emun;

/*
 * 工作实例锁类型，两种锁类型是相互排斥存在的
 */
public class ELockType {
	public static final int RepellentLock = 0; //#排它锁，使用该锁类别时只有一个人处理公文
	public static final int IntercurrentLock = 1; //#共享锁，使用该锁类别时只有多个人同时处理公文
}
