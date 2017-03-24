package org.szcloud.framework.workflow.core.base;

import org.szcloud.framework.workflow.core.emun.EDatabaseType;
import org.szcloud.framework.workflow.core.emun.EFlowArithmeticType;
import org.szcloud.framework.workflow.core.emun.EUserType;
import org.szcloud.framework.workflow.core.emun.EWorkItemDisplayType;

/*
 * 全局变量
 */
public class CGlobalPara {
	// 登录唯一标识
	public String LogonGuid = null;

	// 用户标识
	public int UserID;
	// 用户唯一标识（该标识提供与AD中用户同步的功能对照）
	public String UserGuid = null;
	// 用户代码（登陆名称）
	public String UserCode = null;
	// 用户名称
	public String UserName = null;
	// 用户别名
	public String OUserName = null;
	// 用户呢称
	public String NUserName = null;
	// 用户密码
	public String UserPassword = null;
	// 用户类型
	public int UserType = EUserType.CommondUser;

	// 用户职位：总经理、部门经理、秘书、局长、处长、科长、总工...
	public String UserPosition = null;
	// 用户职称：助理工程师、工程师、高级工程师、讲师、副教授、教授、硕士、博士...
	public String UserTitle = null;

	// 部门标识
	public int DeptID = 0;
	// 部门唯一标识
	public String DeptGuid = null;
	// 部门名称
	public String DeptName = null;
	// 分公司标识
	public int BelongID = 0;

	// 数据库连接串
	public String ConnectionString = null;
	// 数据库连接对象
	// public TSqlHandle theSqlHandle;
	// 数据库类型
	public int DbType = EDatabaseType.MSSQL;

	// 是否集成NT密码验证
	public boolean IsIntegrationNT = false;
	// NT Server的域名
	public String DomainName = null;

	// 是否可以使用本系统
	public boolean IsValidSystem = false;

	// 是否本系统内部处理方式
	public boolean IsInsideTransact = false;

	// 流转状态算法参数
	public int FlowArithmeticType = EFlowArithmeticType.ArithmeticAllEntry;

	// 流转实例显示操作参数
	public int WorkItemDisplay = EWorkItemDisplayType.NotAnyDisplay;

	// 是否使用数据表格，该参数是用于公文流转中表头数据的存储而设置
	public boolean UserTableSaveData = false;

	// 服务器IP地址
	public String DomainIpAddress = null;

	// 公文中正文和附件文件存放位置，该文件路径是不提供给前台应用程序使用
	// 该文件路径只存储以下三种文件数据：实例化的正文附件、流转中发送给远程的数据和从远程接收过来的数据
	public String SaveDataPath = null;

	// 后台临时文件存放位置，该文件路径提供给前台的应用程序所使用，对于Web应用程序
	// 需要使用的临时文件全部存储于该文件夹，在本核心构件中，文件的使用和更新的规则
	// 如下：
	// 1、对于公文数据模板，正文和附件的内容存储于数据库中，而对于流转中的公文，正
	// 文和附件的内容则存储于文件中，主要的原因是在数据库中存储文件内容较大时，会影
	// 响数据的存储时间，而对于模板来说正文和附件的内容基本为空，或者是一些模板，它
	// 们的尺寸也很小，并不影响存储速度
	// 2、当新建公文实例时，读取文件（包括正文和附件）过程为：从数据库中读文件内容
	// 存储于临时文件中，操作完成后，保存到后台文件设定存储文件的位置
	// 3、当打开公文实例时，读取文件（包括正文和附件）过程为：从后台文件设定存储文
	// 件的位置读文件内容存储于临时文件中，操作完成后，再保存到后台文件设定存储文件
	// 的位置

	// 读取数据文件夹，该路径对于后台应用程序是一个绝对路径，对Web应用是一个只读的虚拟目录
	public String TempReadDataPath = null;

	// 上传数据文件夹，该路径对于后台应用程序是一个绝对路径，对Web应用是一个只写的虚拟目录
	public String TempWriteDataPath = null;

	// 临时文件目录，该路径对于后台应用程序是一个绝对路径，对Web应用是一个虚拟目录
	public String TempDataPath = null;

	// 系统超时时间
	public int OverTime = 360;

	// 日志记录类型(0-不记录; 1-文件; 2-数据库;)
	public int LogType = 2;
	// 日志记录位置
	public String LogPath = null;

	// 客户端登陆IP地址
	public String IPAddress = null;

	/*
	 * 初始化
	 */
	public CGlobalPara() {

	}

	/*
	 * 注销
	 */
	public void ClearUp() {

	}
}
