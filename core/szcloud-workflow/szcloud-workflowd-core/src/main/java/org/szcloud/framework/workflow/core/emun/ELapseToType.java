package org.szcloud.framework.workflow.core.emun;

/*
 * 步骤转发类型
 */
public class ELapseToType {
	public static final int LapseToNotAny = 0; //#不转发
	public static final int LapseToStart = 1; //#转发到开始步骤
	public static final int LapseToSend = 2; //#转发到送达的步骤
	public static final int LapseToPreActivity = 4; //#可以转发给步骤顺序排在当前步骤前面的步骤
	public static final int LapseToNextActivity = 8; //#可以转发给步骤顺序排在当前步骤后面的步骤
	public static final int LapseToFisished = 16; //#可以转发给已流转的步骤
	public static final int LapseToNotFisished = 32; //#可以转发给未流转的步骤
	public static final int LapseToAppoint = 64; //#指定转发
	public static final int LapseToSpecial = 128; //#转发到特殊身份人员[系统角色，如：文管员等]
	public static final int LapseToNewBack = 256; //#转发到一个新步骤，该新步骤处理完成后再转回本步骤
	public static final int LapseToNewSend = 512; //#转发到一个新步骤，该新步骤处理完成后不需要转回本步骤直接继续往下流转
	public static final int LapseToStop = 1024; //#转发到结束步骤
}
