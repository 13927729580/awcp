package org.szcloud.framework.workflow.core.emun;

/*
 * 用户连接串表现类型
 */
public class EUserDisplayType {
	public static final int DisplayUserID = 0; //#用户标识，形式如：“1;2;3;...”
	public static final int DisplayUserCode = 1; //#用户代码，形式如：“administrator;wangp;lil;...”
	public static final int DisplayUserName = 2; //#用户名称，形式如：“系统管理员;王平;李力;...”
	public static final int DisplayUserIDCode = 3; //#用户标识+代码，形式如：“1.administrator;2.wangp;3.lil;...”
	public static final int DisplayUserIDName = 4; //#用户标识+名称，形式如：“1.系统管理员;2.王平;3.李力;...”
	public static final int DisplayUserNameCode = 5; //#用户名称+代码，形式如：“系统管理员(administrator);王平(wangp);李力(lil);...”
	public static final int DisplayUserGUID = 6; //#用户唯一标识，形式如：“GID60AB4E2_7A03_47BE_AC31_507133026CA9;GID60AB4E2_7A03_47BE_AC31_507133026CB1;GID60AB4E2_7A03_47BE_AC31_507133026CB3;...”
}
