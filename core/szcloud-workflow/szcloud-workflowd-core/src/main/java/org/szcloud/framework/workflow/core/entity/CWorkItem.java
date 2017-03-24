package org.szcloud.framework.workflow.core.entity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.business.InnerWorkItem;
import org.szcloud.framework.workflow.core.business.NextActivityPara;
import org.szcloud.framework.workflow.core.business.TPublicFunc;
import org.szcloud.framework.workflow.core.business.TWorkAdmin;
import org.szcloud.framework.workflow.core.business.TWorkItem;
import org.szcloud.framework.workflow.core.emun.EActivityType;
import org.szcloud.framework.workflow.core.emun.ECaculateStatusType;
import org.szcloud.framework.workflow.core.emun.ECurActivityTransType;
import org.szcloud.framework.workflow.core.emun.EDataHandleType;
import org.szcloud.framework.workflow.core.emun.EDocumentRightType;
import org.szcloud.framework.workflow.core.emun.EDocumentType;
import org.szcloud.framework.workflow.core.emun.EDueAlertType;
import org.szcloud.framework.workflow.core.emun.EEntryStatus;
import org.szcloud.framework.workflow.core.emun.EEntryType;
import org.szcloud.framework.workflow.core.emun.EFormAccessType;
import org.szcloud.framework.workflow.core.emun.EFormCellAccessType;
import org.szcloud.framework.workflow.core.emun.EFormCellStyle;
import org.szcloud.framework.workflow.core.emun.EImpressFormType;
import org.szcloud.framework.workflow.core.emun.ELapseToType;
import org.szcloud.framework.workflow.core.emun.EParticipantRangeType;
import org.szcloud.framework.workflow.core.emun.EResponseRight;
import org.szcloud.framework.workflow.core.emun.EResponseType;
import org.szcloud.framework.workflow.core.emun.EWorkItemStatus;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MBase64;
import org.szcloud.framework.workflow.core.module.MFile;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 工作流处理对象：动态公文处理对象模型的基础对象，是动态处理公文的一个入口，<br>
 * 它包含一公文模板、流转的动态数据以及监督处理数据。
 * 
 * 流转系统流转计算规则：
 * 
 * 1、自由流转规则：该种流转方式是没有流程的流转，它只基于公文表单中的权限定义<br>
 * 的处理而进行，每一个处理人员都可以直接转交公文给下一个处理人员，此时缺省权限<br>
 * 将作为一个特殊的中转方式，任意个流转情况的人员不但可以指定给任何人，也可以给<br>
 * 缺省权限的人员处理，由该特定中转人员来转发，其中对于多人处理时的情况，各处理<br>
 * 人员不能转发，而必须都处理完成后通过缺省权限的中转处理来决定 下一个接受权限<br>
 * （类似于步骤）；
 * 
 * 2、顺序流转规则：该种流转方式存在只有步骤的流程的流转，每一种步骤只需定义各个<br>
 * 流转的步骤，而不需要指定任何的条件，但这些步骤存在一定的流转顺序，在流转中，<br>
 * 严格遵守该流转顺序，当然某些步骤可以打回或转发，对于多人处理时的情况，只针对缺<br>
 * 省的后继步骤才有效，而对于打回或转发情况，将不在发生作用，既当一个处理人选择打<br>
 * 回或转发时，其他未处理的人员将变成只读（不可处理方式）；
 * 
 * 3、结构处理方式：该种流转方式严格遵守结构化流程处理方式，在这种方式的流转中，<br>
 * 流程将包括步骤和条件，流转情况是已触发条件作为基准，即以流转中当前步骤作为下一<br>
 * 个触发步骤的前提，算法体现为：根据当前状态所处的步骤，查找所有可能的后继步骤，<br>
 * 在判断各后继步骤的触发条件是否成立，如果成立，将触发下一个步骤，进行公文的流转<br>
 * （在这种流转中，也可以包括灵活的转发来特殊处理流转）。
 * 
 * @author Jackie.Wang
 * 
 */
public class CWorkItem extends CBase {

	/**
	 * 初始化
	 */
	public CWorkItem() {
		super();
		this.initWorkItem();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CWorkItem(CLogon ao_Logon) {
		super(ao_Logon);
		this.initWorkItem();
	}

	/**
	 * 初始化公文变量
	 */
	private void initWorkItem() {
		mo_Extends = new HashMap<String, Object>();

		for (int i = 1; i <= 5; i++) {
			mo_Extends.put("FI" + String.valueOf(i), 0);
			mo_Extends.put("FSA" + String.valueOf(i), "");
			mo_Extends.put("FSB" + String.valueOf(i), "");
		}
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所包含的公文模板
	 */
	public CCyclostyle Cyclostyle = null;

	/**
	 * 流转状态集合
	 */
	public HashMap<Integer, CEntry> Entries = new HashMap<Integer, CEntry>();

	/**
	 * 获取流转状态集合
	 * 
	 * @return
	 */
	public Collection<CEntry> getEntries() {
		if (this.Entries == null)
			return null;
		return this.Entries.values();
	}

	/**
	 * 根据标识获取流转状态
	 * 
	 * @param al_Id
	 *            标识
	 * @return
	 */
	public CEntry getEntryById(int al_Id) {
		try {
			if (this.Entries == null)
				return null;

			return this.Entries.get(al_Id);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 人工监督管理的集合
	 */
	public List<CSupervise> Supervises = new ArrayList<CSupervise>();

	/**
	 * 根据标识获取人工监督管理对象
	 * 
	 * @param al_Id
	 *            标识
	 * @return
	 */
	public CSupervise getSuperviseById(int al_Id) {
		if (this.Supervises == null)
			return null;

		for (CSupervise lo_Supervise : this.Supervises) {
			if (lo_Supervise.SuperviseID == al_Id) {
				return lo_Supervise;
			}
		}
		return null;
	}

	/**
	 * 当前工作流对象
	 */
	public CFlow CurFlow = null;
	/**
	 * 工作流对象的集合
	 */
	public List<CFlow> CFlows = new ArrayList<CFlow>();

	/**
	 * 根据标识获取工作流对象
	 * 
	 * @param al_Id
	 *            标识
	 * @return
	 */
	public CFlow getFlowByID(int al_Id) {
		if (this.CFlows == null)
			return null;

		for (CFlow lo_Flow : this.CFlows) {
			if (lo_Flow.FlowID == al_Id) {
				return lo_Flow;
			}
		}
		return null;
	}

	/**
	 * 实例流转的当前状态
	 */
	public CEntry CurEntry = null;

	/**
	 * 当前权限（由本步骤所拥有的所有权限组成）
	 */
	public CRight CurRight = null;

	/**
	 * 当前步骤
	 */
	public CActivity CurActivity = null;

	/**
	 * 二次开发接口控件
	 */
	private ScriptEngine mo_Script = null;

	/**
	 * 初始化二次开发控件[加载公共函数和系统函数]
	 * 
	 * @return
	 * @throws
	 * @throws Exception
	 */
	public ScriptEngine getScript() throws Exception {
		if (mo_Script == null) {
			// 获得JS引擎
			ScriptEngineManager lo_Manager = new ScriptEngineManager();
			mo_Script = lo_Manager.getEngineByExtension("js");
			// 初始化...
			mo_Script.put("theLogon", this.Logon);
			mo_Script.put("theWorkItem", this);
			// 导入公共二次开发函数
			String ls_Sql = "SELECT ScriptID, ScriptName, ScriptContent FROM ScriptInfo WHERE "
					+ "TypeID IN (SELECT TypeID FROM ScriptType WHERE TypeFlag = 4)";
			List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate()
					.queryForList(ls_Sql);
			for (int i = 0; i < lo_Set.size(); i++) {
				mo_Script.eval((String) lo_Set.get(i).get("ScriptContent"));
			}
			lo_Set = null;
			// 导入专用二次开发函数
			for (CScript lo_Script : this.Cyclostyle.Scripts) {
				mo_Script.eval(lo_Script.ScriptContent);
			}
		}
		return mo_Script;
	}

	/**
	 * 与公文同时更新的结果集状态
	 */
	private ArrayList<PreparedStatement> mo_UpdateStates = null;

	/**
	 * 增加与公文同时更新的结果集状态
	 * 
	 * @param ao_State
	 */
	// public void appendUpdateStates(PreparedStatement ao_State) {
	// if (mo_UpdateStates == null)
	// mo_UpdateStates = new ArrayList<PreparedStatement>();
	// mo_UpdateStates.add(ao_State);
	// }

	/**
	 * 增加与公文同时更新的结果集状态
	 * 
	 * @param ao_State
	 */
	public String getUpdateStateBySql(String as_Sql) {
		try {
			// PreparedStatement lo_State = CSqlHandle.getState(as_Sql, 0);
			// appendUpdateStates(as_Sql);
			return as_Sql;
		} catch (Exception ex) {
			this.Record(ex, "getUpdateStateBySql", as_Sql);
			return null;
		}
	}

	/**
	 * 执行与公文同时更新的结果集状态
	 * 
	 * @throws SQLException
	 */
	public void runUpdateStates() throws SQLException {
		if (mo_UpdateStates == null)
			return;
		for (PreparedStatement lo_State : mo_UpdateStates) {
			lo_State.addBatch();
			lo_State.executeBatch();
		}
	}

	/**
	 * 业务数据库连接代码
	 */
	private String ms_ConnCodes = "";
	/**
	 * 业务数据库同时更新的结果集状态
	 */
	private HashMap<String, PreparedStatement> mo_OperationStates = null;

	/**
	 * 存储所有更新的结果集状态
	 */
	// public List<PreparedStatement> Statements = null;

	/**
	 * 增加业务数据库与公文同时更新的结果集状态
	 * 
	 * @param ao_State
	 */
	public void appendOperationStates(String as_ConnCode,
			PreparedStatement ao_State) {
		if (MGlobal.isEmpty(ms_ConnCodes)) {
			ms_ConnCodes = as_ConnCode;
		} else {
			if ((";" + ms_ConnCodes + ";").indexOf(";" + as_ConnCode + ";") == -1)
				ms_ConnCodes += ";" + as_ConnCode;
		}
		if (mo_OperationStates == null) {
			mo_OperationStates = new HashMap<String, PreparedStatement>();
		}
		mo_OperationStates.put(
				as_ConnCode + String.valueOf(mo_OperationStates.size()),
				ao_State);
	}

	/**
	 * 增加业务数据库与公文同时更新的结果集状态
	 * 
	 * @param ao_State
	 * @throws SQLException
	 */
	// public PreparedStatement getOperationStateBySql(String as_ConnCode,
	// String as_Sql) {
	// try {
	// PreparedStatement lo_State = CSqlHandle.getState(as_Sql, 0);
	// appendOperationStates(as_ConnCode, lo_State);
	// return lo_State;
	// } catch (Exception ex) {
	// this.Record(ex, "getOperationStateBySql", as_Sql);
	// return null;
	// }
	// }

	/**
	 * 执行业务数据库与公文同时更新的结果集状态
	 * 
	 * @throws SQLException
	 */
	public void runOperationStates() throws SQLException {
		if (MGlobal.isEmpty(ms_ConnCodes))
			return;
		for (String ls_Code : ms_ConnCodes.split(";")) {
			try {
				for (int i = 0; i < mo_OperationStates.size(); i++) {
					if (mo_OperationStates.containsKey(ls_Code
							+ String.valueOf(i))) {
						PreparedStatement lo_State = mo_OperationStates
								.get(ls_Code + String.valueOf(i));
						lo_State.addBatch();
						lo_State.executeBatch();
					}
				}
			} catch (Exception ex) {
			} finally {
			}
		}
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 实例标识
	 */
	public int WorkItemID = -1;

	/**
	 * 实例标识流水号，针对新建预先申请流水号情况
	 */
	public int WorkItemSeq = 0;

	/**
	 * 实例名称
	 */
	public String WorkItemName = "";

	/**
	 * 实例名称
	 * 
	 * @param value
	 */
	public void setWorkItemName(String value) {
		if (value == null || value.equals(""))
			return;
		if (MGlobal.BeyondOfLength(value, 800))
			return;

		this.WorkItemName = value;
	}

	/**
	 * 实例创建人标识
	 */
	public int CreatorID = 0;

	/**
	 * 实例创建人
	 */
	public String Creator = "";

	/**
	 * 实例创建人部门标识，=0 - 表示当前用户为无部门用户
	 */
	public int DeptID = 0;

	/**
	 * 实例创建人部门
	 */
	public String DeptName = "";

	/**
	 * 分公司标识
	 */
	public int BelongID = 0;

	/**
	 * 实例创建时间
	 */
	public Date CreateDate = MGlobal.getNow();

	/**
	 * 实例的处理人员标识
	 */
	public int EditorID = 0;

	/**
	 * 实例的处理人员
	 */
	public String Editor = "";

	/**
	 * 实例的处理时间
	 */
	public Date EditDate = MGlobal.getNow();

	/**
	 * 实例状态
	 */
	public int Status = EWorkItemStatus.WorkFlowing;

	/**
	 * 实例状态
	 */
	public String getStatusName() {
		switch (this.Status) {
		case EWorkItemStatus.ExteralStatus:
			return "预留状态";
		case EWorkItemStatus.WorkFlowing:
			return "流转中";
		case EWorkItemStatus.HaveFinished:
			return "已结束";
		case EWorkItemStatus.HavePigeonholed:
			return "已归档";
		case EWorkItemStatus.HaveDeleted:
			return "已删除";
		case EWorkItemStatus.QuiteDeleted:
			return "已完全删除";
		case EWorkItemStatus.PauseFlowing:
			return "被暂停流转";
		case EWorkItemStatus.OtherItemStatus:
			return "其它未定状态";
		default:
			return "";
		}
	}

	/**
	 * 删除前状态
	 */
	public int OrignStatus = EWorkItemStatus.WorkFlowing;

	/**
	 * 读取实例是否已流转完成
	 * 
	 * @return
	 */
	public boolean getIsFinished() {
		return (this.Status > EWorkItemStatus.WorkFlowing ? true : false);
	}

	/**
	 * 设置实例是否已流转完成
	 * 
	 * @param value
	 */
	public void setIsFinished(boolean value) {
		if (value) {
			if (this.Status < EWorkItemStatus.HaveFinished)
				this.Status = EWorkItemStatus.HaveFinished;
		}
	}

	/**
	 * 实例完成时间
	 */
	public Date FinishedDate = MGlobal.getNow();

	/**
	 * 是否有正文
	 */
	public boolean HaveBody = true;

	/**
	 * 是否有附件
	 */
	public boolean HaveDocument = false;

	/**
	 * 是否有表单
	 */
	public boolean HaveForm = true;

	/**
	 * 公文处理有效周期
	 */
	public int ValidLimited = 0;

	/**
	 * 公文秘密程度: =0 - 公开(缺省) =1 - 内部(一级) =2 - 秘密(二级) =3 - 机密(三级) =9 - 绝密(最高秘密级别)
	 */
	public int Secutity = 0;

	/**
	 * 公文扩展字段一
	 */
	public int ExtendOne = 0;

	/**
	 * 公文扩展字段二
	 */
	public int ExtendTwo = 0;

	/**
	 * 公文扩展字段三
	 */
	public String ExtendThree = "";

	/**
	 * 公文扩展字段四
	 */
	public Date ExtendFour = MGlobal.getInitDate();

	/**
	 * 公文扩展字段五
	 */
	public String ExtendFive = "";

	/**
	 * 公文扩展字段六
	 */
	public String ExtendSix = "";

	/**
	 * 公文扩展字段七
	 */
	public String ExtendSeven = "";

	/**
	 * 公文扩展字段八
	 */
	public String ExtendEight = "";

	/**
	 * 公文扩展字段九
	 */
	public String ExtendNine = "";

	/**
	 * 公文扩展字段十
	 */
	public String ExtendTen = "";

	/**
	 * 公文扩展字段集合：FI1~FI5,FSA1~FSA5,FSB1~FSB5
	 */
	private Map<String, Object> mo_Extends = null;

	/**
	 * 获取扩展字段FI1~FI5的值
	 * 
	 * @param ai_Index
	 * @return
	 */
	public int getExtendFI(int ai_Index) {
		return Integer.parseInt(mo_Extends.get("FI" + String.valueOf(ai_Index))
				.toString());
	}

	/**
	 * 设置扩展字段FI1~FI5的值
	 * 
	 * @param ai_Index
	 * @param al_Value
	 */
	public void setExtendFI(int ai_Index, int al_Value) {
		mo_Extends.remove("FI" + String.valueOf(ai_Index));
		mo_Extends.put("FI" + String.valueOf(ai_Index), al_Value);
	}

	/**
	 * 获取扩展字段FSA1~FSA5的值
	 * 
	 * @param ai_Index
	 * @return
	 */
	public String getExtendFSA(int ai_Index) {
		return (String) mo_Extends.get("FSA" + String.valueOf(ai_Index));
	}

	/**
	 * 设置扩展字段FSA1~FSA5的值
	 * 
	 * @param ai_Index
	 * @param as_Value
	 */
	public void setExtendFSA(int ai_Index, String as_Value) {
		mo_Extends.remove("FSA" + String.valueOf(ai_Index));
		mo_Extends.put("FSA" + String.valueOf(ai_Index), as_Value);
	}

	/**
	 * 获取扩展字段FSB1~FSB5的值
	 * 
	 * @param ai_Index
	 * @return
	 */
	public String getExtendFSB(int ai_Index) {
		return (String) mo_Extends.get("FSB" + String.valueOf(ai_Index));
	}

	/**
	 * 设置扩展字段FSB1~FSB5的值
	 * 
	 * @param ai_Index
	 * @param as_Value
	 */
	public void setExtendFSB(int ai_Index, String as_Value) {
		mo_Extends.remove("FSB" + String.valueOf(ai_Index));
		mo_Extends.put("FSB" + String.valueOf(ai_Index), as_Value);
	}

	/**
	 * 公文最近一次更新形式 该参数用来确定公文的提醒信息<br>
	 * =0-没有更新； =1-正常更新，发送，保存等； =2-报警更新； =4-过期更新； =8-结束更新；
	 */
	public int LastUpdateType = 1;

	/**
	 * 公文最近一次更新时间
	 */
	public Date LastUpdateDate = MGlobal.getNow();

	/**
	 * 状态最大标识
	 */
	public int EntryMaxID = 0;

	/**
	 * 人工监督对象的最大标识
	 */
	public int SuperviseMaxID = 0;

	/**
	 * 来源信息扩展 正常公文该字段为空，对于由于本地嵌入或远程启动生成的公文，该字段存储有关嵌入或启动的必要信息，结构格式如下： 嵌入< Launch
	 * Type='0' WorkItemID='#' EntryID='#' /> 启动< Launch Type='1' WorkItemID='#'
	 * EntryID='#' /> 说明：其他信息联合公文远程启动信息表的内容进行关联
	 */
	public String WorkItemSource = "";

	/**
	 * 公文是否允许转发
	 */
	public int ConfigTransfer = 0;

	/**
	 * 工作实例的读写属性
	 */
	public boolean WorkItemAccess = false;

	/**
	 * 发送或保存工作实例后的信息
	 */
	public String Information = "";

	/**
	 * 和公文处理同时提交数据库保存的SQL语句和结果集
	 */
	private ArrayList<Object> mo_UpdateSqlSet = null;

	/**
	 * 和公文处理同时提交数据库保存的SQL语句和结果集
	 * 
	 * @return
	 */
	public ArrayList<Object> getUpdateSqlSet() {
		return mo_UpdateSqlSet;
	}

	/**
	 * 和公文处理同时提交数据库(外部数据库保存的SQL语句和结果集)
	 */
	private ArrayList<Object> mo_UpdateSqlSet2 = null;

	/**
	 * 和公文处理同时提交数据库(外部数据库保存的SQL语句和结果集)
	 * 
	 * @return
	 */
	public ArrayList<Object> getUpdateSqlSet2() {
		return mo_UpdateSqlSet2;
	}

	/**
	 * 和公文处理同时提交数据库(外部数据库保存的数据库在本系统定义的代码)
	 */
	private ArrayList<String> mo_UpdateConnCodes = null;

	/**
	 * 和公文处理同时提交数据库(外部数据库保存的数据库在本系统定义的代码)
	 * 
	 * @return
	 */
	public ArrayList<String> getUpdateConnCodes() {
		return mo_UpdateConnCodes;
	}

	/**
	 * 当前步骤处理类型
	 */
	public int TransType = ECurActivityTransType.CommondTransType;

	/**
	 * 如果为特殊步骤，转发过来的步骤标识
	 */
	public int TransActivityID = 0;

	/**
	 * ****注意：转发步骤为多步时的处理情况 当步骤转发时给多个选择时，转发步骤标识的连接串，使用“”分隔
	 */
	public String TransmitActivityIDs = "";

	/**
	 * 特殊步骤转发人员
	 */
	public String TransParticipant = "";

	/**
	 * 已计算的步骤标识(在条件中已触发的情况)，避免重复计算步骤条件[内存变量，不需要保存]
	 */
	public String CacuActIDs = "";

	/**
	 * 本地嵌入和远程启动参数
	 */
	public String LaunchParameter = "";

	/**
	 * 已经成功流转的分支步骤名称
	 */
	public String FlowSucceedSplits = "";

	/**
	 * 公文流转结束后结果类型 该参数用于公文在嵌入、启动等处理情况时，完成的结果，如下: =-1 公文流转出现错误 =0 未完成已完成的正常处理情况
	 * >0 其他处理结果(该情况由系统在流转过程中的二次开发函数设置得到)
	 */
	public int ResultType = 0;

	/**
	 * 公文流转结束后结果描述
	 */
	public String ResultDescribe = "";

	/**
	 * 公文属性 =0或NULL为正常公文； =1为京华导过来的公文；
	 */
	public int WorkItemProp = 0;

	// 以下几个临时变量供二次开发函数调用[主要针对外部应用]
	/**
	 * 布尔变量
	 */
	public boolean blnParameter = false;
	/**
	 * 整型变量
	 */
	public int intParameter = 0;
	/**
	 * 整型变量2
	 */
	public int intParameter2 = 0;
	/**
	 * 长整型变量
	 */
	public int lngParameter = 0;
	/**
	 * 长整型变量2
	 */
	public int lngParameter2 = 0;
	/**
	 * 字符型变量
	 */
	public String strParameter = null;
	/**
	 * 字符型变量2
	 */
	public String strParameter2 = null;
	/**
	 * 日期型变量
	 */
	public Date datParameter = MGlobal.getInitDate();
	/**
	 * 对象型变量
	 */
	public Object objParameter = null;
	/**
	 * 对象型变量2
	 */
	public Object varParameter = null;

	// 以下几个临时变量供函数处理结果参数传递
	/**
	 * 所有状态个数
	 */
	public int ResultCount = 0;
	/**
	 * 已处理状态个数
	 */
	public int ResultFinishedCount = 0;
	/**
	 * 意见选项
	 */
	public int ResultChoice = 0;
	/**
	 * 比较内容-专为嵌入或启动步骤使用
	 */
	public String ResultCompareContent = "";
	/**
	 * 比较符号-专为嵌入或启动步骤使用
	 */
	public int ResultCompareType = 0;

	/**
	 * 内存变量(是否是内部操作，该使用应用于打开公文，不计较任何使用权限)
	 */
	private boolean mb_IsInterHandle = false;

	/**
	 * 内存变量(是否是内部操作，该使用应用于打开公文，不计较任何使用权限)
	 * 
	 * @return
	 */
	public boolean getIsInterHandle() {
		return mb_IsInterHandle;
	}

	/**
	 * 在保存公文中，用于处理嵌入情况时，决定是否删除临时文件
	 */
	public int DeleteTempFile = 0;

	/**
	 * 用户部分信息的XML对象
	 */
	private Document mo_UserMsgXml = null;

	/**
	 * 用户部分信息的XML对象
	 * 
	 * @return
	 */
	public Document getUserMsgXml() {
		return mo_UserMsgXml;
	}

	/**
	 * 签名打印处理对象
	 */
	private CSignPrint mo_SignPrint = null;

	/**
	 * 签名打印处理对象
	 * 
	 * @return
	 */
	public CSignPrint getSignPrint() {
		if (mo_SignPrint == null) {
			mo_SignPrint = new CSignPrint(this.Logon);
			mo_SignPrint.WorkItem = this;
			mo_SignPrint.Logon = this.Logon;
			mo_SignPrint.Cyclostyle = this.Cyclostyle;
		}
		return mo_SignPrint;
	}

	/**
	 * 获取相关路径
	 * 
	 * @return
	 */
	public String getRelativelyFilePath() {
		String ls_Path = this.Cyclostyle.getFileSavePath();
		return ls_Path.substring(ls_Path.length()
				- (ls_Path.length() - this.Logon.SaveDataPath.length()),
				ls_Path.length() - this.Logon.SaveDataPath.length());
	}

	/**
	 * 读取公共函数对象
	 * 
	 * @return
	 */
	public TPublicFunc getPublicFunc() {
		return this.Logon.getPublicFunc();
	}

	/**
	 * 期限报警执行类型，内存变量，当设置为>NotAnyDueAlertType后，针对人工或自动转人工的后续触发步骤，系统设置催办报警处理
	 */
	public int DueAlertType = EDueAlertType.NotAnyDueAlertType;

	/**
	 * 当前对象是否可用
	 * 
	 * @param ai_SpaceLength
	 * @return
	 * @throws Exception
	 */
	public String IsValid(int ai_SpaceLength) throws Exception {
		try {
			// Add Get Valid Code...
			return "";
		} catch (Exception e) {
			this.Raise(e, "IsValid", null);
			return e.toString();
		}
	}

	/**
	 * 调用特定计算，并返回函数内容
	 * 
	 * @throws Exception
	 */
	public Object caculateScriptContentEval(String as_ScriptContent)
			throws Exception {
		try {
			if (as_ScriptContent == null || as_ScriptContent.equals("")) {
				return null;
			}
			// 初始化二次开发控件[加载公共函数和系统函数]
			ScriptEngine lo_Script = getScript();
			// 计算
			return lo_Script.eval(as_ScriptContent);
		} catch (Exception e) {
			this.Raise(e, "caculateScriptContentEval", null);
		}
		return null;
	}

	/**
	 * 执行特定计算
	 * 
	 * @throws Exception
	 */
	public void runScriptContentEval(String as_ScriptContent) throws Exception {
		try {
			if (as_ScriptContent == null || as_ScriptContent.equals("")) {
				return;
			}
			// 初始化二次开发控件[加载公共函数和系统函数]
			ScriptEngine lo_Script = getScript();
			// 计算
			lo_Script.eval(as_ScriptContent);
		} catch (Exception e) {
			this.Raise(e, "runScriptContentEval", null);
		}
	}

	/**
	 * 根据编码获取扩展字段索引号（1-10）
	 * 
	 * @param as_Code
	 *            编码
	 * @return
	 */
	private int getIndexByCode(String as_Code) {
		String[] ls_Array = "1;2;3;4;5;6;7;8;9;10".split(";");
		for (int i = 0; i < ls_Array.length; i++) {
			if (ls_Array[i].equals(as_Code))
				return i + 1;
		}
		ls_Array = "one;two;three;four;five;six;seven;eight;nine;ten"
				.split(";");
		for (int i = 0; i < ls_Array.length; i++) {
			if (ls_Array[i].equals(as_Code.toLowerCase()))
				return i + 1;
			if (("extend" + ls_Array[i]).equals(as_Code.toLowerCase()))
				return i + 1;
		}
		ls_Array = "一;二;三;四;五;六;七;八;九;十".split(";");
		for (int i = 0; i < ls_Array.length; i++) {
			if (ls_Array[i].equals(as_Code))
				return i + 1;
			if (("扩展字段" + ls_Array[i]).equals(as_Code.toLowerCase()))
				return i + 1;
		}
		return 0;
	}

	/**
	 * 根据编码获取扩展字段呢荣
	 * 
	 * @param as_Code
	 *            编码
	 * @return
	 */
	public Object getExtendValue(String as_Code) {
		switch (this.getIndexByCode(as_Code)) {
		case 1:
			return this.ExtendOne;
		case 2:
			return this.ExtendTwo;
		case 3:
			return this.ExtendThree;
		case 4:
			return this.ExtendFour;
		case 5:
			return this.ExtendFive;
		case 6:
			return this.ExtendSix;
		case 7:
			return this.ExtendSeven;
		case 8:
			return this.ExtendEight;
		case 9:
			return this.ExtendNine;
		case 10:
			return this.ExtendTen;
		default:
			return mo_Extends.get(getExtendIndex(as_Code));
		}
	}

	/**
	 * 根据编码获取扩展字段序号
	 * 
	 * @param as_Code
	 *            编码
	 * @return
	 */
	public int getExtendIndex(String as_Code) {
		String ls_Sign = MGlobal.left(as_Code, 2).toLowerCase();
		if (ls_Sign.equals("fi"))
			return Integer.parseInt(MGlobal.right(as_Code, 1)) - 1;

		if (ls_Sign.equals("fsa"))
			return Integer.parseInt(MGlobal.right(as_Code, 1)) + 4;

		if (ls_Sign.equals("fsb"))
			return Integer.parseInt(MGlobal.right(as_Code, 1)) + 9;

		return 0;
	}

	/**
	 * 打包公文实例内容传递信息，该数据为嵌入步骤(使用其他公文模板时使用)或启动步骤时使用(原PackageWorkItem2)
	 * 
	 * @param ao_TumbleIn
	 *            启动本地子公文
	 * @param ao_Launch
	 *            启动远程子公文
	 * @param ao_Node
	 *            返回远程/本地公文信息XML节点对象
	 * @return
	 */
	public Document packageWorkItem(CTumbleIn ao_TumbleIn, CLaunch ao_Launch,
			Element ao_Node) {
		return packageWorkItem(ao_TumbleIn, ao_Launch, ao_Node);
	}

	/**
	 * 打包公文实例内容传递信息，该数据为嵌入步骤(使用其他公文模板时使用)或启动步骤时使用(原PackageWorkItem2)
	 * 
	 * @param ao_TumbleIn
	 *            启动本地子公文
	 * @param ao_Launch
	 *            启动远程子公文
	 * @param ao_Node
	 *            返回远程/本地公文信息XML节点对象
	 * @param al_EntryID
	 *            嵌入/启动状态标识
	 * @return
	 * @throws Exception
	 */
	public Document packageWorkItem(CTumbleIn ao_TumbleIn, CLaunch ao_Launch,
			Element ao_Node, int al_EntryID) throws Exception {
		try {
			int li_Type = 0;
			CImpressData lo_Data = null;

			if (ao_Launch != null) {
				li_Type = 2;
				lo_Data = ao_Launch.ImpressData;
			} else if (ao_TumbleIn != null) {
				li_Type = 1;
				lo_Data = ao_TumbleIn.ImpressData;
			} else {
				li_Type = 3;
			}
			Document lo_Xml = MXmlHandle.LoadXml("<Activity />");
			Element lo_Node = lo_Xml.getDocumentElement();
			Element lo_Child = lo_Xml.getDocumentElement();

			// 传递基本信息
			lo_Node = MXmlHandle
					.getNodeByPath(lo_Xml.getDocumentElement(),
							"<?xml version=\'1.0\' encoding=\'gb2312\'?><WorkItemInst />");
			// lo_Xml.loadXML("<?xml version=\'1.0\' encoding=\'gb2312\'?><WorkItemInst />");
			lo_Xml.getDocumentElement().setAttribute("WorkItemID",
					Integer.toString(this.WorkItemID));
			lo_Xml.getDocumentElement().setAttribute("WorkItemName",
					this.WorkItemName);
			if (li_Type == 3) {
				lo_Xml.getDocumentElement().setAttribute("Type", "1");
				lo_Xml.getDocumentElement().setAttribute("ResultType",
						Integer.toString(this.ResultType));
				lo_Xml.getDocumentElement().setAttribute("ResultDescribe",
						this.ResultDescribe);
			} else {
				lo_Xml.getDocumentElement().setAttribute("Type", "0");
			}

			// 传递参数设置
			lo_Node = lo_Xml.createElement("Parameter");
			lo_Xml.getDocumentElement().appendChild(lo_Node);
			if (li_Type == 2) // 远程，需要设置服务器信息
			{
				lo_Child = lo_Xml.createElement("Data");
				lo_Node.appendChild(lo_Child);
				if (ao_Launch.CurLaunchData.LaunchUserCode != "") {
					lo_Child.setAttribute("UserCode",
							ao_Launch.CurLaunchData.LaunchUserCode);
				}
				if (ao_Launch.CurLaunchData.LaunchUserName != "") {
					lo_Child.setAttribute("UserName",
							ao_Launch.CurLaunchData.LaunchUserName);
				}
				if (ao_Launch.CurLaunchData.LaunchServiceName != "") {
					lo_Child.setAttribute("ServerName",
							ao_Launch.CurLaunchData.LaunchServiceName);
				}
				lo_Child.setAttribute("ServerIP",
						ao_Launch.CurLaunchData.LaunchServiceIP);
				lo_Child.setAttribute("TemplateName", lo_Data.CyclostyleName);
				if (lo_Data.FlowName != "") {
					lo_Child.setAttribute("FlowName", lo_Data.FlowName);
				}
			}
			if (li_Type == 3) // 回传情况
			{
				if (ao_Node.getAttribute("FileID") != null) {
				}
			}
			Element lo_Body = lo_Xml.getDocumentElement();
			CDocument lo_Doc = new CDocument();
			byte[] ly_Byte = null;
			int li_Result = 0;

			// 传递正文内容
			if (li_Type == 3) {
				// 传递正文类型
				li_Result = Integer
						.parseInt(ao_Node.getAttribute("ReturnBody"));
				if (!this.Cyclostyle.HaveBody) {
					li_Result = 0;
				}
			} else {
				// 传递正文类型
				li_Result = lo_Data.ImpressBody;
				if (!this.Cyclostyle.HaveBody) {
					li_Result = 0;
				}
			}
			// 传递正文类型
			if (li_Result == 0) {
				lo_Node.setAttribute("NeedBody", "0");
			} else {
				lo_Doc = Cyclostyle.Body;
				if (li_Result == 1) {
					// 传递正文类型
					lo_Node.setAttribute("NeedBody", "1");
					lo_Body = lo_Xml.createElement("Body");
					// lo_Body.dataType = "bin.base64";
					lo_Body.setAttribute("HaveBody", "1");
				} else {
					// 传递正文类型
					lo_Node.setAttribute("NeedBody", "0");
					lo_Node.setAttribute("NeedDocument", "1");
					lo_Body = lo_Xml.createElement("Document");
					// lo_Body.dataType = "bin.base64";
					lo_Body.setAttribute("HaveBody", "0");
				}
				lo_Body.setAttribute("DocumentID",
						Integer.toString(lo_Doc.DocumentID));
				lo_Body.setAttribute("DocumentName", lo_Doc.DocumentName);
				lo_Body.setAttribute("DocumentExt", lo_Doc.DocumentExt);
				ly_Byte = (TWorkAdmin.readDocumentContent(this.Logon, 1,
						this.WorkItemID, lo_Doc.DocumentID));
				// lo_Body.nodeTypedValue =
				// Microsoft.VisualBasic.Compatibility.VB6.Support.CopyArray(ly_Byte);
				lo_Xml.getDocumentElement().appendChild(lo_Body);
			}

			// 附件内容
			if (lo_Node.getAttribute("NeedDocument") == null) {
				lo_Node.setAttribute("NeedDocument", "0");
			}
			li_Result = 0;
			if (li_Type == 3) {
				li_Result = Integer.parseInt(ao_Node
						.getAttribute("ReturnDocument"));
			} else {
				li_Result = lo_Data.ImpressDocument;
			}
			if (li_Result != 0) // 0 = NotImpressDocument
			{
				for (CDocument tempLoopVar_lo_Doc : Cyclostyle.Documents) {
					lo_Doc = tempLoopVar_lo_Doc;
					if (lo_Doc.DocumentType == EDocumentType.CommondDocument
							|| lo_Doc.DocumentType == EDocumentType.FileVersionDocument
							|| lo_Doc.DocumentType == EDocumentType.ScanJpgDocument
							|| lo_Doc.DocumentType == EDocumentType.ScanTifDocument) {
						lo_Body = lo_Xml.createElement("Document");
						// lo_Body.dataType = "bin.base64";
						lo_Body.setAttribute("DocumentID",
								Integer.toString(lo_Doc.DocumentID));
						lo_Body.setAttribute("DocumentName",
								lo_Doc.DocumentName);
						lo_Body.setAttribute("DocumentExt", lo_Doc.DocumentExt);
						ly_Byte = TWorkAdmin.readDocumentContent(this.Logon, 1,
								this.WorkItemID, lo_Doc.DocumentID);
						// lo_Body.nodeTypedValue =
						// Microsoft.VisualBasic.Compatibility.VB6.Support.CopyArray(ly_Byte);
						lo_Xml.getDocumentElement().appendChild(lo_Body);
						lo_Node.setAttribute("NeedDocument", "1");
					}
				}
			}
			// 表单->附件
			String ls_File = "";
			// DragonFlightPublicFunction.TFileHandle lo_File = new
			// DragonFlightPublicFunction.TFileHandle();
			Object lv_Variant = null;
			if (li_Type < 3 && (!(lo_Data == null))) {
				if (lo_Data.ImpressForm > EImpressFormType.NotImpressForm) {
					ls_File = "";
					if (this.Logon.getParaValue("WORK_ITEM_PRINT_FLAG") == "1") // 使用普通方式打印
					{
						ls_File = launchWorkItemFile(lo_Data);
					} else // 使用服务打印
					{
						ls_File = PrintWorkItemByService(al_EntryID);
					}
					if (ls_File.equals("") || ls_File == null) {
						// 记录错误日志
						// this.Logon.Error.Raise(al_ErrorId, ao_Base,
						// as_Position, as_Parameter);.RecordErrorMsg(9999,
						// Information.TypeName(this) + "->PackageWorkItem",
						// GetLogMsg(0, 0) + "表单生成附件附件失败");
					} else {
						lo_Body = lo_Xml.createElement("Document");
						// lo_Body.dataType = "bin.base64";
						this.Cyclostyle.DocumentMaxID++;
						lo_Body.setAttribute("DocumentID",
								Integer.toString((Cyclostyle.DocumentMaxID)));
						lo_Body.setAttribute("DocumentName", "公文附件");
						lo_Body.setAttribute("DocumentExt",
								MFile.getFileExt(ls_File));
						ly_Byte = MFile
								.readFile(this.Logon.GlobalPara.TempDataPath
										+ ls_File);
						lo_Body.setNodeValue(MBase64
								.encodeBase64String(ly_Byte));
						ly_Byte = null;
						lo_Xml.getDocumentElement().appendChild(lo_Body);
						lo_Node.setAttribute("NeedDocument", "1");
					}
				}
			} else // 回传情况
			{
			}
			Document lo_XmlTemp = MXmlHandle.LoadXml("<Activity />");
			Element lo_NodeTemp = lo_XmlTemp.getDocumentElement();
			int i = 0;
			Element lo_NewNode = lo_XmlTemp.getDocumentElement();

			// 传递状态信息
			if (li_Type == 3) {
				li_Result = Integer.parseInt(ao_Node
						.getAttribute("StateCToPType"));
			} else {
				li_Result = lo_Data.StateCToPType;
			}
			if (li_Result == 0) // 0 = NotPassStateData
			{
				lo_Node.setAttribute("NeedEntry", "0");
			} else {
				lo_Node.setAttribute("NeedEntry", "1");
				if (MXmlHandle.getNodeByPath(
						lo_XmlTemp.getDocumentElement(),
						"<Entry EntryType=\'"
								+ Integer.toString(li_Result)
								+ "\'>"
								+ this.Logon.getWorkAdmin().WorkItemTrackInfo2(
										this.WorkItemID, CurEntry.EntryID)
								+ "</Entry>") != null) {
					lo_Xml.getDocumentElement().appendChild(
							lo_XmlTemp.getDocumentElement());
				}
				lo_XmlTemp = null;
			}

			List<Map<String, Object>> lo_Rec = CSqlHandle.getJdbcTemplate()
					.queryForList("SELECT * FROM SystemFieldCollate", true);

			// 表头属性
			if (li_Type == 3) {
				lo_XmlTemp = MXmlHandle.LoadXml("<Activity />");
				MXmlHandle.getNodeByPath(lo_XmlTemp.getDocumentElement(),
						ao_Node.getAttribute("ReturnProps"));
				lo_XmlTemp = MXmlHandle.LoadXml(ao_Node
						.getAttribute("ReturnProps"));
			} else {
				lo_XmlTemp = lo_Data.getDefineObject("ImpressProps");
			}
			i = 1;
			NodeList no = lo_XmlTemp.getDocumentElement().getChildNodes();
			for (int j = 0; j < no.getLength(); j++) {
				lo_NodeTemp = (Element) no.item(i);
				lo_NewNode = lo_Xml.createElement("Prop");
				lo_NewNode.setAttribute("PropID", Integer.toString(i));
				lo_NewNode.setAttribute("PropName",
						lo_NodeTemp.getAttribute("Name"));
				lo_NewNode.setAttribute(
						"Value",
						caculateScriptContentEval(
								this.getAnalisysScript(lo_Rec, (lo_NodeTemp
										.getAttribute("Source")).toString()))
								.toString());
				i++;
				lo_Xml.getDocumentElement().appendChild(lo_NewNode);
			}
			// foreach (MSXML2.IXMLDOMElement tempLoopVar_lo_NodeTemp in
			// lo_XmlTemp.documentElement.childNodes)
			// {
			// lo_NodeTemp = tempLoopVar_lo_NodeTemp;
			// lo_NewNode = lo_Xml.createElement("Prop");
			// lo_NewNode.setAttribute("PropID", (i).ToString());
			// lo_NewNode.setAttribute("PropName",
			// lo_NodeTemp.getAttribute("Name"));
			// lo_NewNode.setAttribute("Value",
			// CaculateScriptContentEval(GetAnalisysScript(lo_Rec,(lo_NodeTemp.getAttribute("Source")))));
			// i++;
			// lo_Xml.documentElement.appendChild(lo_NewNode);
			// }

			// 角色信息
			if (li_Type == 3) {
				lo_XmlTemp = MXmlHandle.LoadXml("<Activity />");
				MXmlHandle.getNodeByPath(lo_XmlTemp.getDocumentElement(),
						ao_Node.getAttribute("ReturnRoles"));
				lo_XmlTemp = MXmlHandle.LoadXml(ao_Node
						.getAttribute("ReturnRoles"));
			} else {
				lo_XmlTemp = lo_Data.getDefineObject("ImpressRoles");
			}
			i = 1;
			no = lo_XmlTemp.getDocumentElement().getChildNodes();
			for (int j = 0; j < no.getLength(); j++) {
				lo_NodeTemp = (Element) no.item(i);
				lo_NewNode = lo_Xml.createElement("Role");
				lo_NewNode.setAttribute("RoleID", Integer.toString(i));
				lo_NewNode.setAttribute("RoleName",
						lo_NodeTemp.getAttribute("Name"));
				lo_NewNode.setAttribute(
						"Value",
						caculateScriptContentEval(
								this.getAnalisysScript(lo_Rec,
										(lo_NodeTemp.getAttribute("Source"))))
								.toString());
				lo_NewNode.setAttribute("DisplayValue",
						lo_NewNode.getAttribute("Value"));
				i++;
				lo_Xml.getDocumentElement().appendChild(lo_NewNode);
			}
			// foreach (MSXML2.IXMLDOMElement tempLoopVar_lo_NodeTemp in
			// lo_XmlTemp.documentElement.childNodes)
			// {
			// lo_NodeTemp = tempLoopVar_lo_NodeTemp;
			// lo_NewNode = lo_Xml.createElement("Role");
			// lo_NewNode.setAttribute("RoleID", (i).ToString());
			// lo_NewNode.setAttribute("RoleName",
			// lo_NodeTemp.getAttribute("Name"));
			// lo_NewNode.setAttribute("Value",
			// CaculateScriptContentEval(GetAnalisysScript(lo_Rec,(lo_NodeTemp.getAttribute("Source")))));
			// lo_NewNode.setAttribute("DisplayValue",
			// lo_NewNode.getAttribute("Value"));
			// i++;
			// lo_Xml.documentElement.appendChild(lo_NewNode);
			// }
			lo_Rec = null;
			return lo_Xml;
		} catch (Exception e) {
			this.Raise(e, "PackageWorkItem2", null);
		}
		return MXmlHandle.LoadXml("<Activity />");
	}

	/**
	 * 公文启动（嵌入）表单处理成附件 返回文件名称（临时文件），返回空为处理错误
	 * 
	 * @param ao_Impress
	 * @return
	 * @throws Exception
	 */
	public String launchWorkItemFile(CImpressData ao_Impress) throws Exception {
		try {
			// 记录日志
			this.Record("LaunchWorkItemFile", getLogMsg(0, 0));

			String ls_TempName = "";
			String ls_FileName = "";
			String ls_RightSign = "";
			String ls_LeftSign = "";
			int li_Index = 0;
			int ll_StamperID = 0;
			String ls_Password = "";
			Document lo_Para = MXmlHandle
					.LoadXml(ao_Impress.ImpressFormPrintXml);

			if (lo_Para == null)
				return "";

			Element lo_Node = lo_Para.getDocumentElement();
			if (!lo_Node.getAttribute("Index").equals(""))
				li_Index = Integer.parseInt(lo_Node.getAttribute("Index"));
			if (!lo_Node.getAttribute("StamperID").equals(""))
				ll_StamperID = Integer.parseInt(lo_Node.getAttribute("Index"));
			ls_Password = lo_Node.getAttribute("Password");

			Element lo_First = (Element) lo_Node.getFirstChild();
			// 左标记设置
			if (lo_Node.getAttribute("LeftSign").equals("")) {
				ls_LeftSign = "[@";
			} else {
				ls_LeftSign = lo_Node.getAttribute("LeftSign");
			}
			// 右标记设置
			if (lo_Node.getAttribute("RightSign").equals("")) {
				ls_RightSign = "[@";
			} else {
				ls_RightSign = lo_Node.getAttribute("RightSign");
			}
			ls_FileName = lo_First.getAttribute("File");

			// 拷贝文件到临时文件夹
			ls_FileName = this.Logon.SaveDataPath + "Template\\" + ls_FileName;
			ls_TempName = MFile.getTempFile("doc", this.Logon.TempPath);
			MFile.copyFile(ls_FileName, ls_TempName);

			// 读取模板
			Document lo_Xml = MXmlHandle.LoadXml("<Cyclostyle />");

			if (RealeaseDocument(ls_TempName, lo_Xml.getDocumentElement(),
					ls_LeftSign, ls_RightSign, li_Index, ll_StamperID,
					ls_Password, lo_Node, "", (-1), "")) {
				// LaunchWorkItemFile = Right(ls_TempName, Len(ls_TempName) -
				// Len(GlobalPara.strTempDataPath))
				return ls_TempName;
			}
			lo_Para = null;
		} catch (Exception e) {
			this.Raise(e, "LaunchWorkItemFile", null);
		}
		return "";
	}

	public String PrintWorkItemByService(int al_EntryID) throws SQLException {
		String returnValue = "";
		int ll_Seq = TWorkItem.appendPrint(this, al_EntryID);
		if (ll_Seq == 0) {
			return "";
		}
		String ls_Sql = "";
		int ll_MaxTime = 0;
		ll_MaxTime = 30;
		Map<String, Object> lo_Rec = null;
		while (ll_MaxTime > 0) {
			ls_Sql = "SELECT ID, FileName, IfPrint FROM WorkFlowPrint WHERE ID = "
					+ (ll_Seq);
			lo_Rec = CSqlHandle.getJdbcTemplate().queryForMap(ls_Sql);
			if (lo_Rec == null) {
				return "";
			}
			if (lo_Rec.size() < 0) {
				lo_Rec = null;
				return "";
			}
			if ((Integer) lo_Rec.get("IfPrint") > 0) {
				if ((Integer) lo_Rec.get("IfPrint") == 1) {
					returnValue = (String) (lo_Rec.get("FileName"));
				}
				lo_Rec = null;
				return returnValue;
			}
			lo_Rec = null;
			ll_MaxTime--;
		}
		ls_Sql = "UPDATE WorkFlowPrint SET IfPrint = 98, Remark = \'打印超时！\' WHERE ID = "
				+ (ll_Seq);
		CSqlHandle.execSql(ls_Sql);
		return returnValue;
	}

	/**
	 * 解析简单计算，只需与加减乘除整除(& + - * / \)，其他运算不许存在
	 * 
	 * @param ao_Set
	 * @param as_FieldValue
	 * @throws Exception
	 */
	public String getAnalisysScript(List<Map<String, Object>> ao_Set,
			String as_FieldValue) throws Exception {
		int i = 0;
		int j = 0;
		String ls_Content = "";
		String ls_Result = "";
		String ls_Value = "";
		String ls_Str1 = "";
		CProp lo_Prop = new CProp();
		CRole lo_Role = new CRole();
		ls_Value = " &+-*/\\()^,";
		ls_Content = as_FieldValue;
		for (i = 1; i <= ls_Value.length(); i++) {
			ls_Content = ls_Content.replace(ls_Value.substring(i - 1, 1), "&");
		}
		ls_Result = "";
		j = 1;
		for (i = 1; i <= ls_Content.length() + 1; i++) {
			if (ls_Content.substring(i - 1, 1) == "&"
					|| (i == (ls_Content.length() + 1) && j < i)) {
				ls_Value = ls_Content.substring(j - 1, i - j);
				if (ls_Value.substring(0, 1) == "["
						&& ls_Value.substring(ls_Value.length() - 1, 1) == "]") // #系统字段
				{
					ls_Str1 = this.getScriptContent(ao_Set, ls_Value);
					if (ls_Str1 == null) {
						ls_Result = ls_Result + ls_Value;
					} else {
						ls_Result = ls_Result + ls_Str1;
					}
				} else if (ls_Value.substring(0, 1) == "{"
						&& ls_Value.substring(ls_Value.length() - 1, 1) == "}") // #系统角色
				{
					lo_Role = this.Cyclostyle.getRoleByName(ls_Value.substring(
							1, ls_Value.length() - 2));
					if (lo_Role == null) {
						ls_Result = ls_Result + "\"\"";
					} else {
						ls_Result = ls_Result + "WorkItem.GetRoleValue(\""
								+ ls_Value.substring(1, ls_Value.length() - 2)
								+ "\", 1)";
					}
				} else // #表头属性
				{
					lo_Prop = this.Cyclostyle.getPropByName(ls_Value);
					if (lo_Prop == null) {
						ls_Result = ls_Result + ls_Value;
					} else {
						ls_Result = ls_Result + "WorkItem.GetPropValue(\""
								+ ls_Value + "\")";
					}
				}
				if (i < (ls_Content.length() + 1)) {
					ls_Result = ls_Result + as_FieldValue.substring(i - 1, 1);
				}
				j = (i + 1);
			}
		}
		return ls_Result;
	}

	/**
	 * 根据定义，把公文内容导出到一个Word文档中，提供前台做成文、发布和打印使用
	 * 
	 * @param as_FileName
	 * @param ao_XMLNode
	 * @param as_LeftSign
	 * @param as_RightSign
	 * @param ai_Index
	 * @param al_StamperID
	 * @param as_Password
	 * @param ao_ParaNode
	 * @param as_SaveAsHtmlFile
	 * @param ai_ProtectType
	 * @param as_ProtectPassword
	 * @return
	 * @throws Exception
	 */
	public boolean RealeaseDocument(String as_FileName, Element ao_XMLNode,
			String as_LeftSign, String as_RightSign, int ai_Index,
			int al_StamperID, String as_Password, Element ao_ParaNode,
			String as_SaveAsHtmlFile, int ai_ProtectType,
			String as_ProtectPassword) throws Exception {
		String ls_TempCont = "";
		CProp lo_Prop = new CProp();
		CRole lo_Role = new CRole();
		CForm lo_Form = new CForm();
		CFormCell lo_Cell = new CFormCell();
		int i = 0;
		int j = 0;
		String ls_Script = "";
		String ls_Value = "";
		String ls_Sql = "";
		String ls_BodyFile = "";

		String[] ls_Array = null;
		// DragonFlightPublicFunction.TWordHandle lo_WordHandle = new
		// DragonFlightPublicFunction.TWordHandle();
		// 打开文件
		// lo_WordHandle.OpenWord();
		// lo_WordHandle.TemplateDoc = as_FileName;
		// Call .OpenDoc(False) '隐式打开
		// lo_WordHandle.OpenDoc(true); //显式打开
		// ls_TempCont = lo_WordHandle.TemplateDocText;
		// 设置内容
		// 定义数据规则
		// [@公文标识@] 替换公文标识
		// [@公文名称@] 替换公文名称
		// [@拟稿人@]或[@创建人@] 替换公文创建人
		// [@创建时间@] 替换公文创建时间
		// [@修改人@] 替换公文最近修改人
		// [@修改时间@] 替换公文最近修改时间
		// [@处理状态@] 替换处理状态解释名称
		// [@完成时间@] 替换公文完成时间
		// [@有效周期@] 替换公文处理有效周期
		// [@密级@] 替换公文密级
		// [@字段一@] 替换公文扩展字段一
		// [@字段N@] 替换公文扩展字段N
		// [@字段十@] 替换公文扩展字段十
		// [@接收人@] 替换公文接收人
		// [@转发人@] 替换公文转发人
		// [@转发时间@] 替换公文转发时间
		// [@阅读时间@] 替换公文阅读时间
		// [@转发意见@] 替换公文转发意见
		// [@处理意见@] 替换公文处理意见
		// [@P+属性名称@] 替换属性
		// [@R+角色名称@] 替换角色
		// [@正文内容@] 替换正文内容
		// [@S+计算公式@] 替换二次开发函数内容
		// [@Q+SQL语句@] 替换数据库内容
		// [@公文印章@] 替换公文印章(图片)
		// [@C+控件名称@] 替换批示栏内容、下拉列表内容、单选框内容、复选框内容
		// [@T+控件名称@] 替换单选框打印内容、复选框打印内容
		// ⊙[X]×〇[V]
		// [@N+签名栏名称@] 替换签名栏内容
		// 替换公文特殊字段内容
		ls_Value = "公文标识,公文名称,拟稿人,创建人,创建时间,修改人,修改时间,处理状态,完成时间,有效周期,密级";
		ls_Value = ls_Value + ",字段一,字段二,字段三,字段四,字段五,字段六,字段七,字段八,字段九,字段十";
		ls_Value = ls_Value + ",接收人,转发人,转发时间,阅读时间,转发意见,处理意见";
		ls_Array = ls_Value.split(",");
		SimpleDateFormat sdf = new SimpleDateFormat();
		for (i = 0; i <= (ls_Array.length - 1); i++) {
			if (ls_TempCont.indexOf(as_LeftSign + ls_Array[i] + as_RightSign) + 1 > 0) {
				ls_Value = "";
				String str = ls_Array[i];
				if (str != null) {
					if (str == "公文标识") {
						ls_Value = Integer.toString(this.WorkItemID);
					}
					if (str == "公文名称")
						ls_Value = this.WorkItemName;
					if (str == "拟稿人")
						if (str == "创建人")
							ls_Value = this.Creator;
					if (str == "创建时间")

						ls_Value = sdf.format(this.CreateDate);
					if (str == "修改人")
						ls_Value = this.Editor;
					if (str == "修改时间")
						ls_Value = sdf.format(this.EditDate);
					if (str == "处理状态")
						switch (this.Status) {
						case EWorkItemStatus.ExteralStatus:
							ls_Value = "未知状态";
							break;
						case EWorkItemStatus.WorkFlowing:
							ls_Value = "流转中";
							break;
						case EWorkItemStatus.HaveFinished:
							ls_Value = "已结束";
							break;
						case EWorkItemStatus.HavePigeonholed:
							ls_Value = "已归档";
							break;
						case EWorkItemStatus.HaveDeleted:
							ls_Value = "已删除";
							break;
						case EWorkItemStatus.QuiteDeleted:
							ls_Value = "已完全删除";
							break;
						case EWorkItemStatus.PauseFlowing:
							ls_Value = "暂停流转";
							break;
						case EWorkItemStatus.OtherItemStatus:
							ls_Value = "其它状态";
							break;
						}
					if (str == "完成时间")
						ls_Value = sdf.format(this.FinishedDate);
					if (str == "有效周期")
						ls_Value = Integer.toString(this.ValidLimited);
					if (str == "密级")
						ls_Value = Integer.toString(this.Secutity);
					if (str == "字段一")
						ls_Value = getExtendValue("1").toString();
					if (str == "字段二")
						ls_Value = getExtendValue("2").toString();
					if (str == "字段三")
						ls_Value = getExtendValue("3").toString();
					if (str == "字段四")
						ls_Value = getExtendValue("4").toString();
					if (str == "字段五")
						ls_Value = getExtendValue("5").toString();
					if (str == "字段六")
						ls_Value = getExtendValue("6").toString();
					if (str == "字段七")
						ls_Value = getExtendValue("7").toString();
					if (str == "字段八")
						ls_Value = getExtendValue("8").toString();
					if (str == "字段九")
						ls_Value = getExtendValue("9").toString();
					if (str == "字段十")
						ls_Value = getExtendValue("10").toString();
					if (str == "接收人")
						// ls_Value
						if (str == "转发人")
							if (str == "转发时间")
								if (str == "阅读时间")
									if (str == "转发意见")
										if (str == "处理意见") {
										}
				}
				// lo_WordHandle.ReplaceChar(as_LeftSign + ls_Array[i] +
				// as_RightSign, ls_Value, 0);
			}
		}

		boolean lb_Boolean = false;
		lb_Boolean = false;
		Map<String, Object> lo_Rec = null;
		// 替换公文印章
		String ls_FileName = "";
		// DragonFlightPublicFunction.TFileHandle lo_FileHandle = new
		// DragonFlightPublicFunction.TFileHandle();
		if (ls_TempCont.indexOf(as_LeftSign + "公文印章" + as_RightSign) + 1 > 0) {
			if (ao_XMLNode.getAttribute("UsingStamper") != null) // =NULL 没有印章设置
			{
				if (ao_XMLNode.getAttribute("UsingStamper") == "1") // =0
																	// 不需要印章设置
				{
					if (al_StamperID == 0) {
						if (ao_XMLNode.getAttribute("StamperIDs") != "") // =""
																			// 没有印章设置
						{
							i = ((ao_XMLNode.getAttribute("StamperIDs"))
									.indexOf(";") + 1);
							if (i == 0) {
								al_StamperID = Integer.parseInt(ao_XMLNode
										.getAttribute("StamperIDs"));
							} else {
								al_StamperID = Integer.parseInt(ao_XMLNode
										.getAttribute("StamperIDs").substring(
												0, i));
							}
						}
					}
					if (al_StamperID > 0) {
						lb_Boolean = this.Logon.getWorkAdmin().IsValidPassword(
								al_StamperID, as_Password);
					}
				}
			}
			if (lb_Boolean) {
				// 替换图片
				// 读取内容
				lo_Rec = CSqlHandle.getJdbcTemplate().queryForMap(
						"SELECT StamperID, FileExt, SignContent FROM StamperUsing WHERE StamperID = "
								+ (al_StamperID));
				// 保存成临时文件
				if (lo_Rec.size() > 0) {
					ls_FileName = MFile.getTempFile((String)lo_Rec.get("FileExt"));
					if (Boolean.parseBoolean(MFile.writeFile(ls_FileName,
							lo_Rec.get("SignContent").toString().getBytes()))) {
						// 替换图片文件
						// lo_WordHandle.PicFile = ls_FileName;
						// lo_WordHandle.ReplacePic(as_LeftSign + "公文印章" +
						// as_RightSign, int.Parse(""));
						// 删除临时文件
						MFile.killFile(ls_FileName); // 替换原语句：Kill ls_FileName
					} else {
						// 清空
						// lo_WordHandle.ReplaceChar(as_LeftSign + "公文印章" +
						// as_RightSign, "", 0);
					}
				}
				lo_Rec = null;
			} else {
				// 清空
				// lo_WordHandle.ReplaceChar(as_LeftSign + "公文印章" +
				// as_RightSign, "", 0);
			}
		}

		// 替换属性
		for (CProp tempLoopVar_lo_Prop : Cyclostyle.Props) {
			lo_Prop = tempLoopVar_lo_Prop;
			if (ls_TempCont.indexOf(as_LeftSign + "P" + lo_Prop.PropName
					+ as_RightSign) + 1 > 0) {
				// lo_WordHandle.ReplaceChar(as_LeftSign + "P" +
				// lo_Prop.PropName + as_RightSign, lo_Prop.Value, 0);
			}
		}

		// 替换角色
		for (CRole tempLoopVar_lo_Role : Cyclostyle.Roles) {
			lo_Role = tempLoopVar_lo_Role;
			if (ls_TempCont.indexOf(as_LeftSign + "R" + lo_Role.RoleName
					+ as_RightSign) + 1 > 0) {
				// lo_WordHandle.ReplaceChar(as_LeftSign + "R" +
				// lo_Role.RoleName + as_RightSign, lo_Role.DisplayValue, 0);
			}
		}

		Element lo_Node;

		// 替换签名图片
		if (ao_ParaNode != null) {
			for (CForm tempLoopVar_lo_Form : Cyclostyle.Forms) {
				lo_Form = tempLoopVar_lo_Form;
				for (CFormCell tempLoopVar_lo_Cell : lo_Form.FormCells) {
					lo_Cell = tempLoopVar_lo_Cell;
					if (lo_Cell.CellStyle == EFormCellStyle.SignStyle) {
						if (ls_TempCont.indexOf(as_LeftSign + "N"
								+ lo_Cell.CellName + as_RightSign) + 1 > 0) {
							lo_Node = (Element) ao_ParaNode
									.getAttributeNode("Cell[@ID=\'"
											+ (lo_Form.FormID) + "-"
											+ (lo_Cell.CellID) + "\']");
							if (lo_Node == null) {
								// lo_WordHandle.ReplaceChar(as_LeftSign + "N" +
								// lo_Cell.CellName + as_RightSign, "", 0);
							} else {
								// lo_WordHandle.PicFile
								// =(lo_Node.getAttribute("File"));
								// lo_WordHandle.ReplacePic(as_LeftSign + "N" +
								// lo_Cell.CellName + as_RightSign, 0);
							}
						}
					}
				}
			}
		}

		// 替换批示意见及其他特殊控件内容
		for (CForm tempLoopVar_lo_Form : Cyclostyle.Forms) {
			lo_Form = tempLoopVar_lo_Form;
			for (CFormCell tempLoopVar_lo_Cell : lo_Form.FormCells) {
				lo_Cell = tempLoopVar_lo_Cell;
				if (ls_TempCont.indexOf(as_LeftSign + "C" + lo_Cell.CellName
						+ as_RightSign) + 1 > 0) {
					switch (lo_Cell.CellStyle) {
					case EFormCellStyle.CommentStyle: // 批示意见
						// lo_WordHandle.ReplaceChar(as_LeftSign + "C" +
						// lo_Cell.getCellName() + as_RightSign,
						// GetCommentContent(lo_Cell), 0);
						break;
					case EFormCellStyle.ComboStyle: // 下拉列表
						// lo_WordHandle.ReplaceChar(as_LeftSign + "C" +
						// lo_Cell.getCellName() + as_RightSign,
						// lo_Cell.GetDisplayValue(), 0);
						break;
					case EFormCellStyle.CheckStyle: // 复选框
						ls_Value = lo_Cell.getDisplayValue();
						// lo_WordHandle.ReplaceChar(as_LeftSign + "C" +
						// lo_Cell.getCellName() + as_RightSign, as_LeftSign +
						// "C" + lo_Cell.CellName + as_RightSign +
						// ls_Value.Substring(ls_Value.Length - (ls_Value.Length
						// - 3), ls_Value.Length - 3), 0);
						if (ls_Value.substring(0, 3) == "[V]") {
							// lo_WordHandle.PicFile =
							// GlobalPara.strSaveDataPath +
							// "Template\\Select.gif";
						} else {
							// lo_WordHandle.PicFile =
							// GlobalPara.strSaveDataPath +
							// "Template\\NotSelect.gif";
						}
						// lo_WordHandle.ReplacePic(as_LeftSign + "C" +
						// lo_Cell.CellName + as_RightSign, 0);
						break;
					case EFormCellStyle.RadioStyle: // 单选框
						// lo_WordHandle.ReplaceChar(as_LeftSign + "C" +
						// lo_Cell.CellName + as_RightSign,
						// lo_Cell.GetDisplayValue(), 0);
						break;
					}
				}
				if (ls_TempCont.indexOf(as_LeftSign + "T" + lo_Cell.CellName
						+ as_RightSign) + 1 > 0) {
					switch (lo_Cell.CellStyle) {
					case EFormCellStyle.CheckStyle: // 复选框
						break;
					case EFormCellStyle.RadioStyle: // 单选框
						break;
					}
				}
			}
		}

		// 替换计算公式内容
		i = (ls_TempCont.indexOf(as_LeftSign + "S") + 1);
		while (i > 0) {
			j = (Integer.toString(i).indexOf(ls_TempCont) + 1);
			if (j == 0) {
				i = 0;
			} else {
				ls_Script = ls_TempCont.substring(i + 3 - 1, j - i - 3);
				if (ls_Script != "") {
					ls_Value = caculateScriptContentEval(ls_Script).toString();
					// lo_WordHandle.ReplaceChar(as_LeftSign + "S" + ls_Script +
					// as_RightSign, ls_Value, 0);
				}
				i = (Integer.toString(j).indexOf(ls_TempCont) + 1);
			}
		}

		// 替换数据库内容
		i = (ls_TempCont.indexOf(as_LeftSign + "Q") + 1);
		while (i > 0) {
			j = (Integer.toString(i).indexOf(as_RightSign) + 1);
			if (j == 0) {
				i = 0;
			} else {
				ls_Sql = ls_TempCont.substring(i + 3 - 1, j - i - 3);
				if (ls_Sql != "") {
					ls_Value = CSqlHandle.getValueBySql(ls_Sql);
					// lo_WordHandle.ReplaceChar(as_LeftSign + "Q" + ls_Sql +
					// as_RightSign, ls_Value, 0);
				}
				i = (Integer.toString(j).indexOf(ls_TempCont) + 1);
			}
		}
		// 替换正文
		String ls_TempFile = "";
		// DragonFlightPublicFunction.TWordHandle lo_Word = new
		// DragonFlightPublicFunction.TWordHandle();
		if (this.Cyclostyle.HaveBody) {
			// ls_BodyFile = GlobalPara.strSaveDataPath & "WorkItem\Data" &
			// CStr(Cyclostyle.CyclostyleID)
			// ls_BodyFile = ls_BodyFile & "\File" & CStr(this.WorkItemID) & "_"
			// &
			// CStr(Cyclostyle.Body.DocumentID)
			ls_BodyFile = this.Cyclostyle.getFileSavePath() + "File"
					+ (this.WorkItemID) + "_"
					+ (this.Cyclostyle.Body.DocumentID);
			ls_BodyFile = ls_BodyFile + "."
					+ MFile.getFileExt(this.Cyclostyle.Body.DocumentExt);
			if (ls_TempCont.indexOf(as_LeftSign + "正文内容" + as_RightSign) + 1 > 0) {
				// ls_TempFile = lo_FileHandle.GetTempFile("doc",
				// GlobalPara.strTempDataPath);
				// FileSystem.FileCopy(ls_BodyFile, ls_TempFile);
				// lo_Word.OpenWord();
				// lo_Word.TemplateDoc = ls_TempFile;
				// lo_Word.OpenDoc(true);
				// lo_Word.SetLastRevisions(true);
				// lo_Word.SaveToDoc(true);
				// lo_Word = null;
				//
				// lo_WordHandle.ReplaceFile(as_LeftSign + "正文内容" +
				// as_RightSign, ls_TempFile, 0);
				// lo_WordHandle.SetLastRevisions(-1);
				//
				MFile.killFile(ls_TempFile); // 替换原语句：Kill ls_TempFile
			}
		}
		// 增加水印
		String ls_Text = TWorkItem.getWaterMarkText(this);
		if (ls_Text != "") {
			// lo_WordHandle.AddWaterMark("", ls_Text, 400,
			// 100);
		}
		String ls_WordToPdfType;
		ls_WordToPdfType = this.Logon.getParaValue("WORD_TO_PDF_TYPE");

		if (as_SaveAsHtmlFile.equals("")) {
			// 文件保存
			if (ai_ProtectType == -1) {
				if (ls_WordToPdfType == "1") // 不生成PDF格式
				{
					// lo_WordHandle.SaveToDoc(false);
				} else {
					// lo_WordHandle.SaveToDocAndPdf(true);
				}
			} else {
				// lo_WordHandle.SaveToDocProtect(ai_ProtectType,
				// as_ProtectPassword, true);
			}
		} else {
			// 文件保存
			if (ls_WordToPdfType == "1") // 不生成PDF格式
			{
				// lo_WordHandle.SaveToDoc(false);
			} else {
				// lo_WordHandle.SaveToDocAndPdf(false);
			}
			// 公文发布时使用
			// lo_WordHandle.SaveToHtml(as_SaveAsHtmlFile);
			// lo_WordHandle.CloseDoc();
		}

		// 清除临时文件
		if (!(ao_ParaNode == null)) {
			NodeList no = ao_ParaNode.getElementsByTagName("Cell");
			for (int k = 0; k < no.getLength(); k++) {
				lo_Node = (Element) no.item(ai_Index);
				MFile.killFile(lo_Node.getAttribute("File").toString()); // 替换原语句：Kill
																			// lo_Node.getAttribute("File")
			}
			// for(MSXML2.IXMLDOMElement tempLoopVar_lo_Node in
			// ao_ParaNode.selectNodes())
			// {
			// lo_Node = tempLoopVar_lo_Node;
			// modFile.KillFile(System.Convert.ToString(lo_Node.getAttribute("File")));
			// //替换原语句：Kill lo_Node.getAttribute("File")
			// }
		}
		// lo_WordHandle = null;
		// GlobalPara.RaiseError(Information.Err().Number,
		// Information.TypeName(this) + "::BuildWorkItemFile() Function",
		// Information.Err().Description);
		return true;
	}

	/**
	 * 
	 * @param ao_Set
	 * @param as_FieldValue
	 * @return
	 * @throws Exception
	 */
	public String getScriptContent(List<Map<String, Object>> ao_Set,
			String as_FieldValue) throws Exception {

//		try {
		for (Map<String, Object> map : ao_Set) {
			if(map.get("FieldName") != null && map.get("FieldName").equals(as_FieldValue.replace("[", "").replace("]", ""))){
				return (String) map.get("SystemValue");
			}
		}

//		try {
//			ao_Set.first();
//			ao_Set.findColumn("FieldName=\'"
//					+ as_FieldValue.replace("[", "").replace("]", "") + "\'");
//			if (ao_Set.next()) {
//				return (String) ao_Set.getObject("SystemValue");
//			}
//		} catch (Exception e) {
//			this.Raise(e, "GetScriptContent", null);
//		}

//			ao_Set.findColumn("FieldName=\'"
//					+ as_FieldValue.replace("[", "").replace("]", "") + "\'");
//			if (ao_Set.next()) {
//				return (String) ao_Set.getObject("SystemValue");
//			}
//		} catch (Exception e) {
//			this.Raise(e, "GetScriptContent", null);
//		}
		return "";
	}

	/**
	 * 从Xml节点导入到对象
	 * 
	 * @param ao_Node
	 *            导入节点
	 * @param ai_Type
	 *            导入类型：=0-正常导入；=1-短属性导入；
	 */
	@Override
	protected void Import(Element ao_Node, int ai_Type) throws Exception {
		this.WorkItemID = Integer.parseInt(ao_Node.getAttribute("WorkItemID"));
		this.WorkItemName = ao_Node.getAttribute("WorkItemName");
		this.CreatorID = Integer.parseInt(ao_Node.getAttribute("CreatorID"));
		this.Creator = ao_Node.getAttribute("Creator");
		this.DeptID = Integer.parseInt(ao_Node.getAttribute("DeptID"));
		this.DeptName = ao_Node.getAttribute("DeptName");
		this.CreateDate = MGlobal.stringToDate(ao_Node
				.getAttribute("CreateDate"));
		this.EditorID = Integer.parseInt(ao_Node.getAttribute("EditorID"));
		this.Editor = ao_Node.getAttribute("Editor");
		this.EditDate = MGlobal.stringToDate(ao_Node.getAttribute("EditDate"));
		this.Status = Integer.parseInt(ao_Node.getAttribute("Status"));
		this.OrignStatus = Integer
				.parseInt(ao_Node.getAttribute("OrignStatus"));
		this.FinishedDate = MGlobal.stringToDate(ao_Node
				.getAttribute("FinishedDate"));
		this.HaveBody = Boolean.parseBoolean(ao_Node.getAttribute("HaveBody"));
		this.HaveDocument = Boolean.parseBoolean(ao_Node
				.getAttribute("HaveDocument"));
		this.HaveForm = Boolean.parseBoolean(ao_Node.getAttribute("HaveForm"));
		this.ValidLimited = Integer.parseInt(ao_Node
				.getAttribute("ValidLimited"));
		this.Secutity = Integer.parseInt(ao_Node.getAttribute("Secutity"));
		this.ExtendOne = Integer.parseInt(ao_Node.getAttribute("ExtendOne"));
		this.ExtendTwo = Integer.parseInt(ao_Node.getAttribute("ExtendTwo"));
		this.ExtendThree = ao_Node.getAttribute("ExtendThree");
		this.ExtendFour = MGlobal.stringToDate(ao_Node
				.getAttribute("ExtendFour"));
		this.ExtendFive = ao_Node.getAttribute("ExtendFive");
		this.ExtendSix = ao_Node.getAttribute("ExtendSix");
		this.ExtendSeven = ao_Node.getAttribute("ExtendSeven");
		this.ExtendEight = ao_Node.getAttribute("ExtendEight");
		this.ExtendNine = ao_Node.getAttribute("ExtendNine");
		this.ExtendTen = ao_Node.getAttribute("ExtendTen");
		this.BelongID = Integer.parseInt(ao_Node.getAttribute("BelongID"));
		mo_Extends.clear();
		for (int i = 1; i <= 5; i++) {
			String ls_Name = "FI" + String.valueOf(i);
			mo_Extends.put(ls_Name,
					Integer.parseInt(ao_Node.getAttribute(ls_Name)));
			ls_Name = "FSA" + String.valueOf(i);
			mo_Extends.put(ls_Name,
					ao_Node.getAttribute("FSA" + String.valueOf(i)));
			ls_Name = "FSB" + String.valueOf(i);
			mo_Extends.put(ls_Name,
					ao_Node.getAttribute("FSB" + String.valueOf(i)));
		}
		this.WorkItemAccess = Boolean.parseBoolean(ao_Node
				.getAttribute("WorkItemAccess"));
		this.TransType = Integer.parseInt(ao_Node.getAttribute("TransType"));
		this.TransActivityID = Integer.parseInt(ao_Node
				.getAttribute("TransActivityID"));
		this.TransmitActivityIDs = ao_Node.getAttribute("TransmitActivityIDs");
		this.WorkItemSource = ao_Node.getAttribute("WorkItemSource");
		this.ConfigTransfer = Integer.parseInt(ao_Node
				.getAttribute("ConfigTransfer"));
		this.LastUpdateType = Integer.parseInt(ao_Node
				.getAttribute("LastUpdateType"));
		this.LastUpdateDate = MGlobal.stringToDate(ao_Node
				.getAttribute("LastUpdateDate"));
		this.ResultType = Integer.parseInt(ao_Node.getAttribute("ResultType"));
		this.ResultDescribe = ao_Node.getAttribute("ResultDescribe");
		this.WorkItemProp = Integer.parseInt(ao_Node
				.getAttribute("WorkItemProp"));
		this.WorkItemSeq = Integer
				.parseInt(ao_Node.getAttribute("WorkItemSeq"));

		NodeList lo_List;
		Element lo_Node;
		// 导入公文模板
		if (ao_Node.getAttribute("theCyclostyle").equals("1")) {
			lo_List = ao_Node.getElementsByTagName("Cyclostyle");
			for (int i = 0; i < lo_List.getLength(); i++) {
				this.Cyclostyle = new CCyclostyle(this.Logon);
				this.Cyclostyle.WorkItem = this;
				// this.Cyclostyle.InitCyclostyle();
				lo_Node = (Element) lo_List.item(i);
				this.Cyclostyle.Import(lo_Node, ai_Type);
			}
		}

		// 导入当前工作流对象标识
		if (this.Cyclostyle != null) {
			int ll_FlowId = Integer.parseInt(ao_Node
					.getAttribute("theCurFlowID"));
			for (CFlow lo_Flow : this.Cyclostyle.Flows) {
				if (lo_Flow.FlowID == ll_FlowId)
					this.CurFlow = lo_Flow;
				break;
			}
		}

		// 导入流转状态
		if (this.Entries == null) {
			this.Entries = new HashMap<Integer, CEntry>();
		} else {
			this.Entries.clear();
		}
		lo_List = ao_Node.getElementsByTagName("Entry");
		for (int i = 0; i < lo_List.getLength(); i++) {
			CEntry lo_Entry = new CEntry(this.Logon);
			lo_Entry.WorkItem = this;
			lo_Node = (Element) lo_List.item(i);
			lo_Entry.Import(lo_Node, ai_Type);
			if (this.EntryMaxID < lo_Entry.EntryID)
				this.EntryMaxID = lo_Entry.EntryID;
			this.Entries.put(lo_Entry.EntryID, lo_Entry);
			// if (lo_Entry.EntryID > 1)
			// lo_Entry.ParentEntry = this.Entries.get(lo_Entry.ParentID);
		}

		// 导入人工监督管理
		this.Supervises = new ArrayList<CSupervise>();
		lo_List = ao_Node.getElementsByTagName("Supervise");
		for (int i = 0; i < lo_List.getLength(); i++) {
			CSupervise lo_Supervise = new CSupervise(this.Logon);
			lo_Supervise.WorkItem = this;
			this.Supervises.add(lo_Supervise);
			lo_Node = (Element) lo_List.item(i);
			lo_Supervise.Import(lo_Node, ai_Type);
			if (this.SuperviseMaxID < lo_Supervise.SuperviseID)
				this.SuperviseMaxID = lo_Supervise.SuperviseID;
		}

		// 导入实例流转的当前状态标识
		int ll_EntryId = Integer
				.parseInt(ao_Node.getAttribute("theCurEntryID"));
		this.CurEntry = this.getEntryById(ll_EntryId);

		// 对于新建附件状态标识设置
		for (CDocument lo_Document : this.Cyclostyle.Documents) {
			if (lo_Document.EntryID == 0)
				lo_Document.EntryID = ll_EntryId;
		}

		// 导入当前权限（由本步骤所拥有的所有权限组成）
		if (ao_Node.getAttribute("theCurRightID").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "Right");
			this.CurRight = new CRight(this.Logon);
			this.CurRight.Cyclostyle = this.Cyclostyle;
			this.CurRight.Import(lo_Node, ai_Type);
		}

		// 导入当前步骤标识
		int ll_ActivityID = Integer.parseInt(ao_Node
				.getAttribute("theCurActivityID"));
		if (this.CurFlow != null) {
			for (CActivity lo_Activity : this.CurFlow.Activities) {
				if (lo_Activity.ActivityID == ll_ActivityID) {
					this.CurActivity = lo_Activity;
					break;
				}
			}
		}

		for (int i = 0; i < ao_Node.getAttributes().getLength(); i++) {
			Attr lo_Attr = (Attr) ao_Node.getAttributes().item(i);
			String ls_Key = lo_Attr.getName();
			if (MGlobal.left(ls_Key, "SelectParticipant".length()).equals(
					"SelectParticipant")) {
				ls_Key = MGlobal.right(ls_Key, ls_Key.length()
						- "SelectParticipant".length());
				if (MGlobal.isNumber(ls_Key)) {
					if (ls_Key.equals("0")) {// 特殊步骤
						this.TransParticipant = lo_Attr.getTextContent();
					} else {// 其他步骤
						CActivity lo_Activity = this.CurFlow
								.getActivityById(Integer.parseInt(ls_Key));
						if (lo_Activity.Participant != null)
							lo_Activity.Participant.Participant += lo_Attr
									.getTextContent();
					}
				}
			}
		}

		// 导如XML内容成功后需要处理当前步骤的批示内容
		if (!this.CurEntry.Comment.equals("")) {
			if ((this.CurRight.ResponseType & EResponseType.CommentResponseType) == EResponseType.CommentResponseType) {
				if (this.CurRight.ResponseRight == EResponseRight.PublicResponseRight)
					this.CurEntry.setResponseContent(this.CurEntry.Comment);
			} else {
				this.CurEntry.setResponseContent(this.CurEntry.Comment);
			}
		}

		// 判断导入的公文内容与当前数据库的实际数据是否合法
		if (this.WorkItemAccess) {
			if (!checkIsValidImportData())
				return;
		}

	}

	/**
	 * 判断导入的公文内容与当前数据库的实际数据是否合法
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean checkIsValidImportData() throws Exception {
		if (this.WorkItemID == -1)
			return true;// 新建公文

		// 判断数据库中是否已经存在公文锁
		String ls_Sql = "SELECT * FROM WorkItemLock WHERE WorkItemID = "
				+ String.valueOf(this.WorkItemID) + " AND EntryID = "
				+ String.valueOf(this.CurEntry.EntryID);
		int ll_ErrorID = 0;
		Map<String, Object> lo_Set = CSqlHandle.getJdbcTemplate().queryForMap(
				ls_Sql);
		if (lo_Set.size() > 0) {
			// 错误：当前公文处理的时间已超时(已经超过一天的公文做超时处理)
			if (MGlobal.getTwoDateDay(MGlobal.getNow(),
					(Date) lo_Set.get("LockDate")) > 1)
				ll_ErrorID = 1126;
		} else {
			// 错误：当前公文处理已经超时或当前公文已经被处理
			ll_ErrorID = 1125;
		}
		lo_Set = null;

		if (ll_ErrorID == 0) {
			ls_Sql = "SELECT WorkItemID, EntryID, StateID, RecipientID, InceptDate, ReadDate, FinishedDate, CallDate FROM EntryInst "
					+ "WHERE WorkItemID = "
					+ String.valueOf(this.WorkItemID)
					+ " AND EntryID = " + String.valueOf(this.CurEntry.EntryID);
			lo_Set = CSqlHandle.getJdbcTemplate().queryForMap(ls_Sql);

			if (lo_Set.size() > 0) {
				switch ((Integer) lo_Set.get("StateID")) {
				case 0:
				case 1:
					// 正常
					break;
				case 2:
					// 保存过的公文
					// 错误：当前公文已处理，请关闭后重新打开处理
					if (MGlobal.compareDate((Date) (lo_Set.get("ReadDate")),
							this.CurEntry.ReadingDate) == 1)
						ll_ErrorID = 1128;
					break;
				case 3:
					// 已处理的公文
					// 错误：当前公文您已处理，不能重复处理
					ll_ErrorID = 1129;
					break;
				case 4:
					// 错误：当前公文在规定的时间内您没有处理，已经过期，请与管理员联系后再处理
					ll_ErrorID = 1130;
					break;
				case 5:
					// 错误：当前公文被前一步骤的人撤回，您不可以处理
					ll_ErrorID = 1131;
					break;
				case 7:
					// 错误：当前公文由其他步骤处理过，您不需要处理
					ll_ErrorID = 1132;
					break;
				default:
					// 错误：错误的公文处理情况，请与管理员联系
					ll_ErrorID = 1133;
					break;
				}
			} else {
				// 错误：当前公文不存在，请确认是否已经被管理员删除
				ll_ErrorID = 1127;
			}
			lo_Set = null;
		}

		if (ll_ErrorID > 0) {
			this.Raise(ll_ErrorID, "CheckIsValidImportData", "");
			return false;
		}

		return true;
	}

	/**
	 * 获取日志内容
	 * 
	 * @param al_WorkItemID
	 * @param al_EntryID
	 * @return
	 */
	private String getLogMsg(int al_WorkItemID, int al_EntryID) {
		String ls_Value = "({0}, {1})";
		return MessageFormat.format(ls_Value, String
				.valueOf(al_WorkItemID == 0 ? this.WorkItemID : al_WorkItemID),
				String.valueOf(al_EntryID == 0 ? (this.CurEntry == null ? 0
						: this.CurEntry.EntryID) : al_EntryID));
	}

	/**
	 * 从对象导出到Xml节点
	 * 
	 * @param ao_Node
	 *            导出节点
	 * @param ai_Type
	 *            导出类型：=0-正常导出；=1-短属性导出；
	 */
	@Override
	protected void Export(Element ao_Node, int ai_Type) throws Exception {
		// 记录日志
		this.Record("Export", getLogMsg(0, 0));

		ao_Node.setAttribute("WorkItemID", String.valueOf(this.WorkItemID));
		ao_Node.setAttribute("WorkItemName", this.WorkItemName);
		ao_Node.setAttribute("CreatorID", String.valueOf(this.CreatorID));
		ao_Node.setAttribute("Creator", this.Creator);
		ao_Node.setAttribute("DeptID", String.valueOf(this.DeptID));
		ao_Node.setAttribute("DeptName", this.DeptName);
		ao_Node.setAttribute("CreateDate",
				MGlobal.dateToString(this.CreateDate));
		ao_Node.setAttribute("EditorID", String.valueOf(this.EditorID));
		ao_Node.setAttribute("Editor", this.Editor);
		ao_Node.setAttribute("EditDate", MGlobal.dateToString(this.EditDate));
		ao_Node.setAttribute("Status", String.valueOf(this.Status));
		ao_Node.setAttribute("OrignStatus", String.valueOf(this.OrignStatus));
		ao_Node.setAttribute("FinishedDate",
				MGlobal.dateToString(this.FinishedDate));
		ao_Node.setAttribute("HaveBody", String.valueOf(this.HaveBody));
		ao_Node.setAttribute("HaveDocument", String.valueOf(this.HaveDocument));
		ao_Node.setAttribute("HaveForm", String.valueOf(this.HaveForm));
		ao_Node.setAttribute("ValidLimited", String.valueOf(this.ValidLimited));
		ao_Node.setAttribute("Secutity", String.valueOf(this.Secutity));
		ao_Node.setAttribute("ExtendOne", String.valueOf(this.ExtendOne));
		ao_Node.setAttribute("ExtendTwo", String.valueOf(this.ExtendTwo));
		ao_Node.setAttribute("ExtendThree", this.ExtendThree);
		ao_Node.setAttribute("ExtendFour",
				MGlobal.dateToString(this.ExtendFour));
		ao_Node.setAttribute("ExtendFive", this.ExtendFive);
		ao_Node.setAttribute("ExtendSix", this.ExtendSix);
		ao_Node.setAttribute("ExtendSeven", this.ExtendSeven);
		ao_Node.setAttribute("ExtendEight", this.ExtendEight);
		ao_Node.setAttribute("ExtendNine", this.ExtendNine);
		ao_Node.setAttribute("ExtendTen", this.ExtendTen);
		ao_Node.setAttribute("BelongID", String.valueOf(this.BelongID));
		for (int i = 1; i <= 5; i++) {
			String ls_Name = "FI" + String.valueOf(i);
			ao_Node.setAttribute(ls_Name, mo_Extends.get(ls_Name).toString());
			ls_Name = "FSA" + String.valueOf(i);
			ao_Node.setAttribute(ls_Name, mo_Extends.get(ls_Name).toString());
			ls_Name = "FSB" + String.valueOf(i);
			ao_Node.setAttribute(ls_Name, mo_Extends.get(ls_Name).toString());
		}
		ao_Node.setAttribute("WorkItemAccess",
				String.valueOf(this.WorkItemAccess));
		ao_Node.setAttribute("TransType", String.valueOf(this.TransType));
		ao_Node.setAttribute("TransActivityID",
				String.valueOf(this.TransActivityID));
		ao_Node.setAttribute("TransmitActivityIDs", this.TransmitActivityIDs);
		ao_Node.setAttribute("WorkItemSource", this.WorkItemSource);
		ao_Node.setAttribute("ConfigTransfer",
				String.valueOf(this.ConfigTransfer));
		ao_Node.setAttribute("LastUpdateType",
				String.valueOf(this.LastUpdateType));
		ao_Node.setAttribute("LastUpdateDate",
				MGlobal.dateToString(this.LastUpdateDate));
		ao_Node.setAttribute("ResultType", String.valueOf(this.ResultType));
		ao_Node.setAttribute("ResultDescribe", this.ResultDescribe);
		ao_Node.setAttribute("WorkItemProp", String.valueOf(this.WorkItemProp));
		// 2015-1-12修改
		ao_Node.setAttribute("WorkItemSeq", String.valueOf(this.WorkItemSeq));

		if (this.WorkItemAccess
				&& this.CurEntry.EntryStatus != EEntryStatus.TumbleInLaunchStatus) {
			this.setNeedParameter(ao_Node, ai_Type);
		}

		Element lo_Node;
		// 导出公文模板
		if (this.Cyclostyle == null) {
			ao_Node.setAttribute("theCyclostyle", "0");
		} else {
			ao_Node.setAttribute("theCyclostyle", "1");
			lo_Node = MXmlHandle.addElement(ao_Node, "Cyclostyle");
			this.Cyclostyle.Export(lo_Node, ai_Type);
		}

		// 导出当前工作流对象标识
		if (this.CurFlow == null) {
			ao_Node.setAttribute("theCurFlowID", "0");
		} else {
			ao_Node.setAttribute("theCurFlowID",
					String.valueOf(this.CurFlow.FlowID));
		}

		// 导出流转状态
		ao_Node.setAttribute("theEntriesCount",
				String.valueOf(this.Entries.size()));
		for (CEntry lo_Entry : this.Entries.values()) {
			lo_Node = MXmlHandle.addElement(ao_Node, "Entry");
			lo_Entry.Export(lo_Node, ai_Type);
		}

		// 导出人工监督管理
		ao_Node.setAttribute("theSupervisesCount",
				String.valueOf(this.Supervises.size()));
		for (CSupervise lo_Supervise : this.Supervises) {
			lo_Node = MXmlHandle.addElement(ao_Node, "Supervise");
			lo_Supervise.Export(lo_Node, ai_Type);
		}

		// 导出实例流转的当前状态标识
		if (this.CurEntry == null) {
			ao_Node.setAttribute("theCurEntryID", "0");
		} else {
			ao_Node.setAttribute("theCurEntryID",
					String.valueOf(this.CurEntry.EntryID));
		}

		// 导出当前权限（由本步骤所拥有的所有权限组成）
		if (this.CurRight == null) {
			ao_Node.setAttribute("theCurRightID", "0");
		} else {
			ao_Node.setAttribute("theCurRightID", "1");
			lo_Node = MXmlHandle.addElement(ao_Node, "Right");
			this.CurRight.Export(lo_Node, ai_Type);
		}

		// 导出当前步骤标识
		if (this.CurActivity == null) {
			ao_Node.setAttribute("theCurActivityID", "0");
		} else {
			ao_Node.setAttribute("theCurActivityID",
					String.valueOf(this.CurActivity.ActivityID));
		}
	}

	/**
	 * 获取后继参数
	 * 
	 * @return
	 */
	public String getNeedParameter() {
		Document lo_Xml = MXmlHandle.LoadXml("<WorkItem />");
		try {
			setNeedParameter(lo_Xml.getDocumentElement(), 0);
		} catch (Exception ex) {

		}
		return MXmlHandle.getXml(lo_Xml);
	}

	/**
	 * 设置在公文可处理情况下必须的参数内容
	 * 
	 * @param ao_Node
	 * @param ai_Type
	 * @throws Exception
	 */
	private void setNeedParameter(Element ao_Node, int ai_Type)
			throws Exception {
		// 可能发送的后继步骤
		String ls_Value = getNextActivityIDs(this.CurActivity);
		ao_Node.setAttribute("NextActivityIDs", ls_Value);

		// 可能发送的后继步骤中需要选择步骤收件人的步骤标识
		this.getScript(); // 初始化二次开发函数

		String ls_Str = "";
		String ls_Range = "";
		String ls_SendToPar = "";
		boolean lb_Value = false;

		// 没有后继步骤，转到内部处理和转发情况
		if (!ls_Value.equals("")) {
			for (CActivity lo_Activity : this.CurFlow.Activities) {
				if ((";" + ls_Value).indexOf(";"
						+ String.valueOf(lo_Activity.ActivityID) + ";") > -1) {
					if (lo_Activity.Participant != null) {
						switch (lo_Activity.Participant.RangeType) {
						case EParticipantRangeType.ExistAllParticipant:
							// 全体成员
							if (lo_Activity.Participant.SelectUserNum == 0
									|| lo_Activity.Participant.SelectUserNum > lo_Activity
											.getSpringNumber()) {
								ls_Str += String
										.valueOf(lo_Activity.ActivityID) + ";";
								if (lo_Activity.Participant.Participant
										.equals("")) {// 如果后一步没有收件人，那么为必选
									ao_Node.setAttribute(
											"ParticipantRange"
													+ String.valueOf(lo_Activity.ActivityID),
											"N");
								} else {
									ao_Node.setAttribute(
											"ParticipantRange"
													+ String.valueOf(lo_Activity.ActivityID),
											"V");
								}
								ao_Node.setAttribute(
										"SelectParticipant"
												+ String.valueOf(lo_Activity.ActivityID),
										"");
								ao_Node.setAttribute(
										"MaxLimitedNum"
												+ String.valueOf(lo_Activity.ActivityID),
										String.valueOf(lo_Activity.Participant.MaxLimitedNum));
							} else {
								// 不需要选择收件人
								if (lo_Activity.Participant.SelectUserNum <= lo_Activity
										.getSpringNumber()) {
									lo_Activity.Participant.Participant = lo_Activity
											.getLastParticipants();
								}
							}
							break;
						case EParticipantRangeType.ExistInCaculate:
							// 有范围
							ls_Range = String
									.valueOf(mo_Script
											.eval(lo_Activity.Participant.ParticipantRange));
							if (!ls_Range.equals("")) {
								ls_Range = this.Logon.getUserAdmin()
										.convertUsers(2, 7, ls_Range);
								if (!ls_Range.equals("")) {
									// 判断是否需要选择收件人
									lb_Value = (lo_Activity.Participant.SelectUserNum == 0);
									if (!lb_Value)
										lb_Value = (lo_Activity.Participant.SelectUserNum > lo_Activity
												.getSpringNumber());
									if (lb_Value) {
										// 需要选择收件人
										if (lo_Activity.Participant.Participant
												.equals("")) {
											// 只有一个收件人的情况，采用特殊处理
											if (ls_Range.length()
													- ls_Range.replaceAll(";",
															"").length() == 1) {
												lo_Activity.Participant.Participant = ls_Range;
												lb_Value = false;
											}
										}
									} else {
										// 不需要选择收件人
										lo_Activity.Participant.Participant = lo_Activity
												.getLastParticipants();
									}

									if (lb_Value) {
										ls_Str += String
												.valueOf(lo_Activity.ActivityID)
												+ ";";
										if (lo_Activity.Participant.Participant
												.equals("")) {// 如果后一步没有收件人，那么为必选
											ao_Node.setAttribute(
													"ParticipantRange"
															+ String.valueOf(lo_Activity.ActivityID),
													"N" + ls_Range);
										} else {
											ao_Node.setAttribute(
													"ParticipantRange"
															+ String.valueOf(lo_Activity.ActivityID),
													"V" + ls_Range);
										}
										ao_Node.setAttribute(
												"SelectParticipant"
														+ String.valueOf(lo_Activity.ActivityID),
												"");
										ao_Node.setAttribute(
												"MaxLimitedNum"
														+ String.valueOf(lo_Activity.ActivityID),
												String.valueOf(lo_Activity.Participant.MaxLimitedNum));
									}
								}
							}
							break;
						default: // EParticipantRangeType.NotExistSelectParticipant
							// 没有
							break;
						}
					}
				}
			}
			// 发送时需要选取收件人的步骤标识连接串，使用“;”分隔
			ao_Node.setAttribute("SelectParticipantActivityIDs", ls_Str);
		}

		// 当前步骤信息
		if (this.CurActivity.Transact.InsideReading) {// 需要内部处理
			if (this.CurActivity.Participant.RangeType == EParticipantRangeType.NotExistSelectParticipant) {
				// 不需要选取收件人
				ao_Node.setAttribute("SelectParticipantCurActivityID", "0");
			} else {
				if ((";" + ls_Str).indexOf(";"
						+ String.valueOf(this.CurActivity.ActivityID) + ";") == -1) {
					ls_Str += String.valueOf(this.CurActivity.ActivityID) + ";";
					ls_Range = "";
					if (this.CurActivity.Participant.RangeType == EParticipantRangeType.ExistInCaculate) {
						// 有范围
						ls_Range = String
								.valueOf(mo_Script
										.eval(this.CurActivity.Participant.ParticipantRange));
						if (!ls_Range.equals(""))
							ls_Range = this.Logon.getUserAdmin().convertUsers(
									2, 7, ls_Range);
						if (ls_Range.equals("")) {// 范围没有内容
							ao_Node.setAttribute(
									"SelectParticipantCurActivityID", "0");
						} else {
							ao_Node.setAttribute(
									"SelectParticipantCurActivityID",
									String.valueOf(this.CurActivity.ActivityID));
							if (this.CurActivity.Participant.Participant
									.equals("")) {// 如果后一步没有收件人，那么为必选
								ao_Node.setAttribute(
										"ParticipantRange"
												+ String.valueOf(this.CurActivity.ActivityID),
										"N");
							} else {
								ao_Node.setAttribute(
										"ParticipantRange"
												+ String.valueOf(this.CurActivity.ActivityID),
										"V");
							}
							ao_Node.setAttribute(
									"SelectParticipant"
											+ String.valueOf(this.CurActivity.ActivityID),
									"");
						}
					} else {
						// 全体成员
						ao_Node.setAttribute("SelectParticipantCurActivityID",
								String.valueOf(this.CurActivity.ActivityID));
						if (this.CurActivity.Participant.Participant.equals("")) {// 如果后一步没有收件人，那么为必选
							ao_Node.setAttribute(
									"ParticipantRange"
											+ String.valueOf(this.CurActivity.ActivityID),
									"N");
						} else {
							ao_Node.setAttribute(
									"ParticipantRange"
											+ String.valueOf(this.CurActivity.ActivityID),
									"V");
						}
						ao_Node.setAttribute(
								"SelectParticipant"
										+ String.valueOf(this.CurActivity.ActivityID),
								"");
					}
					ao_Node.setAttribute(
							"MaxLimitedNum"
									+ String.valueOf(this.CurActivity.ActivityID),
							String.valueOf(this.CurActivity.Participant.MaxLimitedNum));
				} else {
					// 已存在，在上面计算中已有
					ao_Node.setAttribute("SelectParticipantCurActivityID",
							String.valueOf(this.CurActivity.ActivityID));
				}
			}
		}

		// 步骤转发情况
		if (this.CurActivity.Transact.LapseTo > ELapseToType.LapseToNotAny) {
			// 计算已流转步骤
			String ls_FlowedActivityIDs = ";";
			for (CEntry lo_Entry : this.getEntryById(1).ChildEntries) {
				if (lo_Entry.ActivityID > 0) {
					ls_FlowedActivityIDs += String.valueOf(lo_Entry.ActivityID)
							+ ";";
				}
			}
			ao_Node.setAttribute("FlowedActivityIDs", ls_FlowedActivityIDs);

			// 计算送达步骤标识
			int ll_ServiceActivityID = 0;
			if (this.CurEntry.OrginalID == this.CurEntry.EntryID
					|| this.CurEntry.OrginalID == 0) {
				ll_ServiceActivityID = 0;
			} else {
				ll_ServiceActivityID = this
						.getEntryById(this.CurEntry.OrginalID).ActivityID;
			}

			// 计算前后步骤的顺序值
			int ll_PreOrderID = 0;
			int ll_NextOrderID = 0;
			for (CActivity lo_Activity : this.CurFlow.Activities) {
				if (";0;1;3;4;9;19;".indexOf(";"
						+ String.valueOf(lo_Activity.ActivityType) + ";") > -1) {
					if (lo_Activity.OrderID > ll_PreOrderID
							&& lo_Activity.OrderID < this.CurActivity.OrderID) {
						ll_PreOrderID = lo_Activity.OrderID;
					}
					if ((lo_Activity.OrderID < ll_NextOrderID || ll_NextOrderID == 0)
							&& lo_Activity.OrderID > this.CurActivity.OrderID) {
						ll_NextOrderID = lo_Activity.OrderID;
					}
				}
			}
			if (ll_NextOrderID == this.CurActivity.OrderID)
				ll_NextOrderID = 0;

			int li_LapseTo = this.CurActivity.Transact.LapseTo;
			ls_Value = "";
			boolean lb_Boolean = false;
			for (CActivity lo_Activity : this.CurFlow.Activities) {
				lb_Boolean = false;
				switch (lo_Activity.ActivityType) {
				case EActivityType.StartActivity:
					if (this.CurActivity.ActivityType == EActivityType.TransactActivity) {
						// 开始或已流转步骤
						if (((li_LapseTo & ELapseToType.LapseToStart) == ELapseToType.LapseToStart)
								|| ((li_LapseTo & ELapseToType.LapseToFisished) == ELapseToType.LapseToFisished)) {
							lb_Boolean = true;
						} else {
							// 送达步骤
							if ((li_LapseTo & ELapseToType.LapseToSend) == ELapseToType.LapseToSend) {
								if (lo_Activity.ActivityID == ll_ServiceActivityID)
									lb_Boolean = true;
							}
							// 前面步骤
							if ((li_LapseTo & ELapseToType.LapseToPreActivity) == ELapseToType.LapseToPreActivity) {
								if (lo_Activity.OrderID < this.CurActivity.OrderID)
									lb_Boolean = true;
							}
							// 后面步骤
							if ((li_LapseTo & ELapseToType.LapseToNextActivity) == ELapseToType.LapseToNextActivity) {
								if (lo_Activity.OrderID > this.CurActivity.OrderID)
									lb_Boolean = true;
							}
							// 指定步骤
							if ((li_LapseTo & ELapseToType.LapseToAppoint) == ELapseToType.LapseToAppoint) {
								if ((";" + this.CurActivity.Transact.TransmitContent)
										.indexOf(";" + lo_Activity.ActivityName
												+ ";") > 0)
									lb_Boolean = true;
							}
						}
					}
					break;
				case EActivityType.TransactActivity:
				case EActivityType.TumbleInActivity:
				case EActivityType.LaunchActivity:
				case EActivityType.NotLimitedActivity:
				case EActivityType.FYIActivity:
					// 已流转步骤
					if ((li_LapseTo & ELapseToType.LapseToFisished) == ELapseToType.LapseToFisished) {
						if (ls_FlowedActivityIDs.indexOf(";"
								+ String.valueOf(lo_Activity.ActivityID) + ";") > -1)
							lb_Boolean = true;
					}
					// 未流转步骤
					if ((li_LapseTo & ELapseToType.LapseToNotFisished) == ELapseToType.LapseToNotFisished) {
						if (ls_FlowedActivityIDs.indexOf(";"
								+ String.valueOf(lo_Activity.ActivityID) + ";") == 0)
							lb_Boolean = true;
					}
					// 送达步骤
					if ((li_LapseTo & ELapseToType.LapseToSend) == ELapseToType.LapseToSend) {
						if (lo_Activity.ActivityID == ll_ServiceActivityID)
							lb_Boolean = true;
					}
					// 前面步骤
					if ((li_LapseTo & ELapseToType.LapseToPreActivity) == ELapseToType.LapseToPreActivity) {
						if (lo_Activity.OrderID < this.CurActivity.OrderID)
							lb_Boolean = true;
					}
					// 后面步骤
					if ((li_LapseTo & ELapseToType.LapseToNextActivity) == ELapseToType.LapseToNextActivity) {
						if (lo_Activity.OrderID > this.CurActivity.OrderID)
							lb_Boolean = true;
					}
					// 指定步骤
					if ((li_LapseTo & ELapseToType.LapseToAppoint) == ELapseToType.LapseToAppoint) {
						if ((";" + this.CurActivity.Transact.TransmitContent)
								.indexOf(";" + lo_Activity.ActivityName + ";") > 0)
							lb_Boolean = true;
					}
					break;
				case EActivityType.StopActivity:
					// if (this.CurActivity.getActivityType() ==
					// EActivityType.TransactActivity ||
					// this.CurEntry.EntryID > 3) {
					// 结束或未流转步骤
					if ((li_LapseTo & ELapseToType.LapseToFisished) == ELapseToType.LapseToFisished
							|| (li_LapseTo & ELapseToType.LapseToNotFisished) == ELapseToType.LapseToNotFisished) {
						lb_Boolean = true;
					} else {
						// 前面步骤
						if ((li_LapseTo & ELapseToType.LapseToPreActivity) == ELapseToType.LapseToPreActivity) {
							if (lo_Activity.OrderID < this.CurActivity.OrderID)
								lb_Boolean = true;
						}
						// 后面步骤
						if ((li_LapseTo & ELapseToType.LapseToNextActivity) == ELapseToType.LapseToNextActivity) {
							if (lo_Activity.OrderID > this.CurActivity.OrderID)
								lb_Boolean = true;
						}
						// 指定步骤
						if ((li_LapseTo & ELapseToType.LapseToAppoint) == ELapseToType.LapseToAppoint) {
							if ((";" + this.CurActivity.Transact.TransmitContent)
									.indexOf(";" + lo_Activity.ActivityName
											+ ";") > 0)
								lb_Boolean = true;
						}
					}
					// }
					break;
				default:
					break;
				}
				if (lb_Boolean) {
					ls_Value += String.valueOf(lo_Activity.ActivityID) + ";";
					if (lo_Activity.Participant != null
							&& lo_Activity.ActivityType != EActivityType.StartActivity) {
						if ((";" + ls_Str).indexOf(";"
								+ String.valueOf(lo_Activity.ActivityID) + ";") == -1) {
							switch (lo_Activity.Participant.RangeType) {
							case EParticipantRangeType.ExistAllParticipant:
								// 全体成员
								if (lo_Activity.Participant.SelectUserNum == 0
										|| lo_Activity.Participant.SelectUserNum > lo_Activity
												.getSpringNumber()) {
									ls_SendToPar += String
											.valueOf(lo_Activity.ActivityID)
											+ ";";
									if (lo_Activity.Participant.Participant
											.equals("")) {// 如果后一步没有收件人，那么为必选
										ao_Node.setAttribute(
												"ParticipantRange"
														+ String.valueOf(lo_Activity.ActivityID),
												"N");
									} else {
										ao_Node.setAttribute(
												"ParticipantRange"
														+ String.valueOf(lo_Activity.ActivityID),
												"V");
									}
									ao_Node.setAttribute(
											"SelectParticipant"
													+ String.valueOf(lo_Activity.ActivityID),
											"");
								}
								break;
							case EParticipantRangeType.ExistInCaculate:
								// 有范围
								ls_Range = String
										.valueOf(mo_Script
												.eval(lo_Activity.Participant.ParticipantRange));
								if (!ls_Range.equals("")) {
									ls_Range = this.Logon.getUserAdmin()
											.convertUsers(2, 7, ls_Range);
									if (!ls_Range.equals("")) {
										// 判断是否需要选择收件人
										lb_Value = (lo_Activity.Participant.SelectUserNum == 0);
										if (!lb_Value)
											lb_Value = (lo_Activity.Participant.SelectUserNum > lo_Activity
													.getSpringNumber());
										if (lb_Value) {
											// 需要选择收件人
											if (lo_Activity.Participant.Participant
													.equals("")) {
												// 只有一个收件人的情况，采用特殊处理
												if (ls_Range.length()
														- ls_Range.replaceAll(
																";", "")
																.length() == 1) {
													lo_Activity.Participant.Participant = ls_Range;
													lb_Value = false;
												}
											}
										} else {
											// 不需要选择收件人
											lo_Activity.Participant.Participant = lo_Activity
													.getLastParticipants();
										}

										if (lb_Value) {
											ls_SendToPar += String
													.valueOf(lo_Activity.ActivityID)
													+ ";";
											if (lo_Activity.Participant.Participant
													.equals("")) {// 如果后一步没有收件人，那么为必选
												ao_Node.setAttribute(
														"ParticipantRange"
																+ String.valueOf(lo_Activity.ActivityID),
														"N" + ls_Range);
											} else {
												ao_Node.setAttribute(
														"ParticipantRange"
																+ String.valueOf(lo_Activity.ActivityID),
														"V" + ls_Range);
											}
											ao_Node.setAttribute(
													"SelectParticipant"
															+ String.valueOf(lo_Activity.ActivityID),
													"");
										}
									}
								}
								break;
							default: // EParticipantRangeType.NotExistSelectParticipant:
								// 没有
								break;
							}
							ao_Node.setAttribute(
									"MaxLimitedNum"
											+ String.valueOf(lo_Activity.ActivityID),
									String.valueOf(lo_Activity.Participant.MaxLimitedNum));
						}
					} else {
						ls_SendToPar += String.valueOf(lo_Activity.ActivityID)
								+ ";";
					}
				}
			}

			// 指定转发步骤的步骤标识连接串
			ao_Node.setAttribute("SendToActivityIDs",
					orderActivityIDs(ls_Value));
			// 指定转发步骤中需要选取收件人的步骤标识连接串
			ao_Node.setAttribute("SendToParActivityIDs", ls_SendToPar);

			// 特殊转发设置
			if (this.CurActivity.getTransType() == ECurActivityTransType.CommondTransType) {
				// 是否需要转发到特殊步骤
				if ((li_LapseTo & ELapseToType.LapseToSpecial) == ELapseToType.LapseToSpecial) {
					ao_Node.setAttribute("ExistSpecialActivity", "1");
				} else {
					ao_Node.setAttribute("ExistSpecialActivity", "0");
				}
				// 是否需要在当前步骤前添加一新步骤
				if ((li_LapseTo & ELapseToType.LapseToNewBack) == ELapseToType.LapseToNewBack) {
					ao_Node.setAttribute("ExistPreActivity", "1");
				} else {
					ao_Node.setAttribute("ExistPreActivity", "0");
				}
				// 是否需要在当前步骤后添加一新步骤
				if ((li_LapseTo & ELapseToType.LapseToNewSend) == ELapseToType.LapseToNewSend) {
					ao_Node.setAttribute("ExistNextActivity", "1");
				} else {
					ao_Node.setAttribute("ExistNextActivity", "0");
				}
			} else {// 特殊步骤不能再转发给特殊步骤
				ao_Node.setAttribute("ExistSpecialActivity", "0");
				ao_Node.setAttribute("ExistPreActivity", "0");
				ao_Node.setAttribute("ExistNextActivity", "0");
			}
			ao_Node.setAttribute("SelectParticipant0", "");
		}
	}

	/**
	 * 获取后继参数
	 * 
	 * @return
	 */
	public InnerWorkItem getWorkItemInterface() {
		InnerWorkItem lo_Interface = new InnerWorkItem();

		lo_Interface.setEntryID(this.CurEntry.EntryID);
		CTransact lo_Transact = this.CurActivity.Transact;
		lo_Interface.setSendLabel(lo_Transact.SendLabel);
		lo_Interface.setSave1LabelFlag(lo_Transact.Save1LabelFlag);
		lo_Interface.setSave1Label(lo_Transact.Save1Label);
		lo_Interface.setSave2LabelFlag(lo_Transact.Save2LabelFlag);
		lo_Interface.setSave2Label(lo_Transact.Save2Label);
		lo_Interface.setReadLabel(lo_Transact.getReadingLabel());
		lo_Interface.setLapseToLabel(lo_Transact.getLapseToLabel());

		if (this.CurActivity.Transact.UseTransOnly)
			lo_Interface.setSendAction(false);
		lo_Interface.setResponseChoices(lo_Transact.ResponseChoices);
		lo_Interface.setTransActivityNum(lo_Transact.TransActivityNum);

		try {
			setNeedParameter(lo_Interface);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return lo_Interface;
	}

	/**
	 * 设置在公文可处理情况下必须的参数内容
	 * 
	 * @param ao_Inner
	 *            公文流转接口实体类
	 * @throws Exception
	 */
	private void setNeedParameter(InnerWorkItem ao_Inner) throws Exception {
		// 可能发送的后继步骤
		String ls_Value = getNextActivityIDs(this.CurActivity);
		ao_Inner.setNextActivityIDs(ls_Value);
		ao_Inner.setActivityID(this.CurActivity.ActivityID);

		// 可能发送的后继步骤中需要选择步骤收件人的步骤标识
		this.getScript(); // 初始化二次开发函数

		String ls_Str = "";
		String ls_Range = "";
		String ls_SendToPar = "";
		boolean lb_Value = false;

		// 没有后继步骤，转到内部处理和转发情况
		if (!ls_Value.equals("")) {
			for (CActivity lo_Activity : this.CurFlow.Activities) {
				if ((";" + ls_Value).indexOf(";"
						+ String.valueOf(lo_Activity.ActivityID) + ";") > -1) {
					if (lo_Activity.Participant != null) {
						switch (lo_Activity.Participant.RangeType) {
						case EParticipantRangeType.ExistAllParticipant:
							// 全体成员
							if (lo_Activity.Participant.SelectUserNum == 0
									|| lo_Activity.Participant.SelectUserNum > lo_Activity
											.getSpringNumber()) {
								ls_Str += String
										.valueOf(lo_Activity.ActivityID) + ";";
								if (lo_Activity.Participant.Participant
										.equals("")) {// 如果后一步没有收件人，那么为必选
									ao_Inner.addNextActivityPara(
											1,
											0,
											lo_Activity.ActivityID,
											lo_Activity.ActivityName,
											0,
											"",
											lo_Activity.Participant.MaxLimitedNum);
								} else {
									ao_Inner.addNextActivityPara(
											1,
											1,
											lo_Activity.ActivityID,
											lo_Activity.ActivityName,
											0,
											"",
											lo_Activity.Participant.MaxLimitedNum);
								}
							} else {
								// 不需要选择收件人
								if (lo_Activity.Participant.SelectUserNum <= lo_Activity
										.getSpringNumber()) {
									lo_Activity.Participant.Participant = lo_Activity
											.getLastParticipants();
								}
							}
							break;
						case EParticipantRangeType.ExistInCaculate:
							// 有范围
							ls_Range = String
									.valueOf(mo_Script
											.eval(lo_Activity.Participant.ParticipantRange));
							if (!ls_Range.equals("")) {
								ls_Range = this.Logon.getUserAdmin()
										.convertUsers(2, 7, ls_Range);
								if (!ls_Range.equals("")) {
									// 判断是否需要选择收件人
									lb_Value = (lo_Activity.Participant.SelectUserNum == 0);
									if (!lb_Value)
										lb_Value = (lo_Activity.Participant.SelectUserNum > lo_Activity
												.getSpringNumber());
									if (lb_Value) {
										// 需要选择收件人
										if (lo_Activity.Participant.Participant
												.equals("")) {
											// 只有一个收件人的情况，采用特殊处理
											if (ls_Range.length()
													- ls_Range.replaceAll(";",
															"").length() == 1) {
												lo_Activity.Participant.Participant = ls_Range;
												lb_Value = false;
											}
										}
									} else {
										// 不需要选择收件人
										lo_Activity.Participant.Participant = lo_Activity
												.getLastParticipants();
									}

									if (lb_Value) {
										ls_Str += String
												.valueOf(lo_Activity.ActivityID)
												+ ";";
										if (lo_Activity.Participant.Participant
												.equals("")) {// 如果后一步没有收件人，那么为必选
											ao_Inner.addNextActivityPara(
													1,
													0,
													lo_Activity.ActivityID,
													lo_Activity.ActivityName,
													1,
													ls_Range,
													lo_Activity.Participant.MaxLimitedNum);
										} else {
											ao_Inner.addNextActivityPara(
													1,
													1,
													lo_Activity.ActivityID,
													lo_Activity.ActivityName,
													1,
													ls_Range,
													lo_Activity.Participant.MaxLimitedNum);
										}
									}
								}
							}
							break;
						default: // EParticipantRangeType.NotExistSelectParticipant
							// 没有
							ao_Inner.addNextActivityPara(1, -1,
									lo_Activity.ActivityID,
									lo_Activity.ActivityName, 0, ls_Range,
									lo_Activity.Participant.MaxLimitedNum);
							break;
						}
					}
				}
			}
			// 发送时需要选取收件人的步骤标识连接串，使用【;】分隔
			ao_Inner.setSelectParticipantActivityIDs(ls_Str);
		}

		// 当前步骤信息
		if (this.CurActivity.Transact.InsideReading) {// 需要内部处理
			ao_Inner.setInsideReading(true);
			NextActivityPara lo_Para = ao_Inner.getInsideReadingPara();
			lo_Para.setTransactType(4);
			lo_Para.setType(-1);
			lo_Para.setActivityID(this.CurActivity.ActivityID);
			lo_Para.setActivityName(this.CurActivity.ActivityName);
			lo_Para.setMaxLimitedNum(this.CurActivity.Participant.MaxLimitedNum);
			lo_Para.setRangeType(this.CurActivity.Participant.RangeType);
			if (this.CurActivity.Participant.RangeType == EParticipantRangeType.NotExistSelectParticipant) {
				// 不需要选取收件人
			} else {
				if ((";" + ls_Str).indexOf(";"
						+ String.valueOf(this.CurActivity.ActivityID) + ";") == -1) {
					ls_Str += String.valueOf(this.CurActivity.ActivityID) + ";";
					ls_Range = "";
					if (this.CurActivity.Participant.RangeType == EParticipantRangeType.ExistInCaculate) {
						// 有范围
						ls_Range = String
								.valueOf(mo_Script
										.eval(this.CurActivity.Participant.ParticipantRange));
						if (!ls_Range.equals(""))
							ls_Range = this.Logon.getUserAdmin().convertUsers(
									2, 7, ls_Range);

						if (ls_Range.equals("")) {// 范围没有内容
							// lo_Part.setActivityID(0);
						} else {
							if (this.CurActivity.Participant.Participant
									.equals("")) {// 如果后一步没有收件人，那么为必选
								lo_Para.setType(0);
							} else {
								lo_Para.setType(1);
							}
							lo_Para.setRange(ls_Range);
						}
					} else {
						// 全体成员
						if (this.CurActivity.Participant.Participant.equals("")) {// 如果后一步没有收件人，那么为必选
							lo_Para.setType(0);
						} else {
							lo_Para.setType(1);
						}
					}
				} else {
					// 已存在，在上面计算中已有
				}
			}
		}

		// 步骤转发情况
		if (this.CurActivity.Transact.LapseTo > ELapseToType.LapseToNotAny) {
			ao_Inner.setLapseTo(true);

			// 计算已流转步骤
			String ls_FlowedActivityIDs = ";";
			for (CEntry lo_Entry : this.getEntryById(1).ChildEntries) {
				if (lo_Entry.ActivityID > 0) {
					ls_FlowedActivityIDs += String.valueOf(lo_Entry.ActivityID)
							+ ";";
				}
			}
			ao_Inner.setFlowedActivityIDs(ls_FlowedActivityIDs);

			// 计算送达步骤标识
			int ll_ServiceActivityID = 0;
			if (this.CurEntry.OrginalID == this.CurEntry.EntryID
					|| this.CurEntry.OrginalID == 0) {
				ll_ServiceActivityID = 0;
			} else {
				ll_ServiceActivityID = this
						.getEntryById(this.CurEntry.OrginalID).ActivityID;
			}
			ao_Inner.setServiceActivityID(ll_ServiceActivityID);

			// 计算前后步骤的顺序值
			int ll_PreOrderID = 0;
			int ll_NextOrderID = 0;
			for (CActivity lo_Activity : this.CurFlow.Activities) {
				if (";0;1;3;4;9;19;".indexOf(";"
						+ String.valueOf(lo_Activity.ActivityType) + ";") > -1) {
					if (lo_Activity.OrderID > ll_PreOrderID
							&& lo_Activity.OrderID < this.CurActivity.OrderID) {
						ll_PreOrderID = lo_Activity.OrderID;
					}
					if ((lo_Activity.OrderID < ll_NextOrderID || ll_NextOrderID == 0)
							&& lo_Activity.OrderID > this.CurActivity.OrderID) {
						ll_NextOrderID = lo_Activity.OrderID;
					}
				}
			}
			if (ll_NextOrderID == this.CurActivity.OrderID)
				ll_NextOrderID = 0;

			int li_LapseTo = this.CurActivity.Transact.LapseTo;
			ls_Value = "";
			boolean lb_Boolean = false;
			for (CActivity lo_Activity : this.CurFlow.Activities) {
				lb_Boolean = false;
				switch (lo_Activity.ActivityType) {
				case EActivityType.StartActivity:
					if (this.CurActivity.ActivityType == EActivityType.TransactActivity) {
						// 开始或已流转步骤
						if (((li_LapseTo & ELapseToType.LapseToStart) == ELapseToType.LapseToStart)
								|| ((li_LapseTo & ELapseToType.LapseToFisished) == ELapseToType.LapseToFisished)) {
							lb_Boolean = true;
						} else {
							// 送达步骤
							if ((li_LapseTo & ELapseToType.LapseToSend) == ELapseToType.LapseToSend) {
								if (lo_Activity.ActivityID == ll_ServiceActivityID)
									lb_Boolean = true;
							}
							// 前面步骤
							if ((li_LapseTo & ELapseToType.LapseToPreActivity) == ELapseToType.LapseToPreActivity) {
								if (lo_Activity.OrderID < this.CurActivity.OrderID)
									lb_Boolean = true;
							}
							// 后面步骤
							if ((li_LapseTo & ELapseToType.LapseToNextActivity) == ELapseToType.LapseToNextActivity) {
								if (lo_Activity.OrderID > this.CurActivity.OrderID)
									lb_Boolean = true;
							}
							// 指定步骤
							if ((li_LapseTo & ELapseToType.LapseToAppoint) == ELapseToType.LapseToAppoint) {
								if ((";" + this.CurActivity.Transact.TransmitContent).indexOf(";" + lo_Activity.ActivityName + ";") > -1)
									lb_Boolean = true;
							}
						}
					}
					break;
				case EActivityType.TransactActivity:
				case EActivityType.TumbleInActivity:
				case EActivityType.LaunchActivity:
				case EActivityType.NotLimitedActivity:
				case EActivityType.FYIActivity:
					// 已流转步骤
					if ((li_LapseTo & ELapseToType.LapseToFisished) == ELapseToType.LapseToFisished) {
						if (ls_FlowedActivityIDs.indexOf(";"
								+ String.valueOf(lo_Activity.ActivityID) + ";") > -1)
							lb_Boolean = true;
					}
					// 未流转步骤
					if ((li_LapseTo & ELapseToType.LapseToNotFisished) == ELapseToType.LapseToNotFisished) {
						if (ls_FlowedActivityIDs.indexOf(";" + String.valueOf(lo_Activity.ActivityID) + ";") == -1)
							lb_Boolean = true;
					}
					// 送达步骤
					if ((li_LapseTo & ELapseToType.LapseToSend) == ELapseToType.LapseToSend) {
						if (lo_Activity.ActivityID == ll_ServiceActivityID)
							lb_Boolean = true;
					}
					// 前面步骤
					if ((li_LapseTo & ELapseToType.LapseToPreActivity) == ELapseToType.LapseToPreActivity) {
						if (lo_Activity.OrderID < this.CurActivity.OrderID)
							lb_Boolean = true;
					}
					// 后面步骤
					if ((li_LapseTo & ELapseToType.LapseToNextActivity) == ELapseToType.LapseToNextActivity) {
						if (lo_Activity.OrderID > this.CurActivity.OrderID)
							lb_Boolean = true;
					}
					// 指定步骤
					if ((li_LapseTo & ELapseToType.LapseToAppoint) == ELapseToType.LapseToAppoint) {
						if ((";" + this.CurActivity.Transact.TransmitContent).indexOf(";" + lo_Activity.ActivityName + ";") > -1)
							lb_Boolean = true;
					}
					break;
				case EActivityType.StopActivity:
					// if (this.CurActivity.ActivityType ==
					// EActivityType.TransactActivity || this.CurEntry.EntryID >
					// 3) {
					// 结束或未流转步骤
					if ((li_LapseTo & ELapseToType.LapseToFisished) == ELapseToType.LapseToFisished
							|| (li_LapseTo & ELapseToType.LapseToNotFisished) == ELapseToType.LapseToNotFisished) {
						lb_Boolean = true;
					} else {
						// 前面步骤
						if ((li_LapseTo & ELapseToType.LapseToPreActivity) == ELapseToType.LapseToPreActivity) {
							if (lo_Activity.OrderID < this.CurActivity.OrderID)
								lb_Boolean = true;
						}
						// 后面步骤
						if ((li_LapseTo & ELapseToType.LapseToNextActivity) == ELapseToType.LapseToNextActivity) {
							if (lo_Activity.OrderID > this.CurActivity.OrderID)
								lb_Boolean = true;
						}
						// 指定步骤
						if ((li_LapseTo & ELapseToType.LapseToAppoint) == ELapseToType.LapseToAppoint) {
							if ((";" + this.CurActivity.Transact.TransmitContent).indexOf(";" + lo_Activity.ActivityName + ";") > -1)
								lb_Boolean = true;
						}
					}
					// }
					break;
				default:
					break;
				}
				if (lb_Boolean) {
					ao_Inner.addNextActivityPara(2, -1, lo_Activity.ActivityID,
							lo_Activity.ActivityName, 0, "", 0);
					ls_Value += String.valueOf(lo_Activity.ActivityID) + ";";
					if (lo_Activity.Participant != null
							&& lo_Activity.ActivityType != EActivityType.StartActivity) {
						if ((";" + ls_Str).indexOf(";"
								+ String.valueOf(lo_Activity.ActivityID) + ";") == -1) {
							switch (lo_Activity.Participant.RangeType) {
							case EParticipantRangeType.ExistAllParticipant:
								// 全体成员
								if (lo_Activity.Participant.SelectUserNum == 0
										|| lo_Activity.Participant.SelectUserNum > lo_Activity
												.getSpringNumber()) {
									ls_SendToPar += String
											.valueOf(lo_Activity.ActivityID)
											+ ";";
									if (lo_Activity.Participant.Participant
											.equals("")) {// 如果后一步没有收件人，那么为必选
										ao_Inner.addNextActivityPara(
												2,
												0,
												lo_Activity.ActivityID,
												lo_Activity.ActivityName,
												0,
												"",
												lo_Activity.Participant.MaxLimitedNum);
									} else {
										ao_Inner.addNextActivityPara(
												2,
												1,
												lo_Activity.ActivityID,
												lo_Activity.ActivityName,
												0,
												"",
												lo_Activity.Participant.MaxLimitedNum);
									}
								}
								break;
							case EParticipantRangeType.ExistInCaculate:
								// 有范围
								ls_Range = String
										.valueOf(mo_Script
												.eval(lo_Activity.Participant.ParticipantRange));
								if (!ls_Range.equals("")) {
									ls_Range = this.Logon.getUserAdmin()
											.convertUsers(2, 7, ls_Range);
									if (!ls_Range.equals("")) {
										// 判断是否需要选择收件人
										lb_Value = (lo_Activity.Participant.SelectUserNum == 0);
										if (!lb_Value)
											lb_Value = (lo_Activity.Participant.SelectUserNum > lo_Activity
													.getSpringNumber());
										if (lb_Value) {
											// 需要选择收件人
											if (lo_Activity.Participant.Participant
													.equals("")) {
												// 只有一个收件人的情况，采用特殊处理
												if (ls_Range.length()
														- ls_Range.replaceAll(
																";", "")
																.length() == 1) {
													lo_Activity.Participant.Participant = ls_Range;
													lb_Value = false;
												}
											}
										} else {
											// 不需要选择收件人
											lo_Activity.Participant.Participant = lo_Activity
													.getLastParticipants();
										}

										if (lb_Value) {
											ls_SendToPar += String
													.valueOf(lo_Activity.ActivityID)
													+ ";";
											if (lo_Activity.Participant.Participant
													.equals("")) {// 如果后一步没有收件人，那么为必选
												ao_Inner.addNextActivityPara(
														2,
														0,
														lo_Activity.ActivityID,
														lo_Activity.ActivityName,
														1,
														ls_Range,
														lo_Activity.Participant.MaxLimitedNum);
											} else {
												ao_Inner.addNextActivityPara(
														2,
														1,
														lo_Activity.ActivityID,
														lo_Activity.ActivityName,
														1,
														ls_Range,
														lo_Activity.Participant.MaxLimitedNum);
											}
										}
									}
								}
								break;
							default: // EParticipantRangeType.NotExistSelectParticipant:
								// 没有
								break;
							}
						}
					} else {
						ls_SendToPar += String.valueOf(lo_Activity.ActivityID)
								+ ";";
					}
				}
			}

			// 指定转发步骤的步骤标识连接串
			ao_Inner.setSendToActivityIDs(orderActivityIDs(ls_Value));
			// 指定转发步骤中需要选取收件人的步骤标识连接串
			ao_Inner.setSendToParActivityIDs(ls_SendToPar);

			// 特殊转发设置
			if (this.CurActivity.getTransType() == ECurActivityTransType.CommondTransType) {
				// 是否需要转发到特殊步骤
				ao_Inner.setExistSpecialActivity((li_LapseTo & ELapseToType.LapseToSpecial) == ELapseToType.LapseToSpecial);
				// 是否需要在当前步骤前添加一新步骤
				ao_Inner.setExistPreActivity((li_LapseTo & ELapseToType.LapseToNewBack) == ELapseToType.LapseToNewBack);
				// 是否需要在当前步骤后添加一新步骤
				ao_Inner.setExistNextActivity((li_LapseTo & ELapseToType.LapseToNewSend) == ELapseToType.LapseToNewSend);
			} else {// 特殊步骤不能再转发给特殊步骤
				// 是否需要转发到特殊步骤
				ao_Inner.setExistSpecialActivity(false);
				// 是否需要在当前步骤前添加一新步骤
				ao_Inner.setExistPreActivity(false);
				// 是否需要在当前步骤后添加一新步骤
				ao_Inner.setExistNextActivity(false);
			}
		}
	}

	/**
	 * 对象保存到数据库行对象中
	 * 
	 * @param ao_Set
	 *            保存的结果集
	 * @param ai_Type
	 *            保存方式：=0-正常保存；=1-短属性保存；
	 * @throws Exception
	 */
	@Override
	public void Save(Map<String, Object> ao_Set, int ai_Type) throws Exception {
		// Add Open Code...
		ao_Set.put("WorkItemID", this.WorkItemID);
		ao_Set.put("WorkItemName", this.WorkItemName);
		ao_Set.put("CreateID", this.CreatorID);
		ao_Set.put("Creator", this.Creator);
		ao_Set.put("DeptID", this.DeptID);
		ao_Set.put("DeptName",
				(this.DeptName.equals("") ? null : this.DeptName));
		ao_Set.put("CreateDate", MGlobal.dateToSqlTime(this.CreateDate));
		ao_Set.put("COMP_ID", this.BelongID);
		ao_Set.put("EditID", this.EditorID);
		ao_Set.put("Editor", (this.Editor.equals("") ? null : this.Editor));
		ao_Set.put("EditDate", MGlobal.dateToSqlTime(this.EditDate));
		ao_Set.put("WorkItemStatus", this.Status);
		ao_Set.put("OrignStatus", this.OrignStatus);
		ao_Set.put("FinishedDate", MGlobal.dateToSqlTime(this.FinishedDate));
		ao_Set.put("HaveBody", (this.HaveBody ? 1 : 0));
		ao_Set.put("HaveDocument", (this.HaveDocument ? 1 : 0));
		ao_Set.put("HaveForm", (this.HaveForm ? 1 : 0));
		ao_Set.put("ValidPeriod", this.ValidLimited);
		ao_Set.put("WorkItemSecret", this.Secutity);
		ao_Set.put("ExtendOne", this.ExtendOne);
		ao_Set.put("ExtendTwo", this.ExtendTwo);
		ao_Set.put("ExtendThree", (this.ExtendThree.equals("") ? null
				: this.ExtendThree));
		ao_Set.put("ExtendFour", MGlobal.dateToSqlTime(this.ExtendFour));
		ao_Set.put("ExtendFive", (this.ExtendFive.equals("") ? null
				: this.ExtendFive));
		ao_Set.put("ExtendSix", (this.ExtendSix.equals("") ? null
				: this.ExtendSix));
		ao_Set.put("ExtendSeven", (this.ExtendSeven.equals("") ? null
				: this.ExtendSeven));
		ao_Set.put("ExtendEight", (this.ExtendEight.equals("") ? null
				: this.ExtendEight));
		ao_Set.put("ExtendNine", (this.ExtendNine.equals("") ? null
				: this.ExtendNine));
		ao_Set.put("ExtendTen", (this.ExtendTen.equals("") ? null
				: this.ExtendTen));

		// 判断公文是不是扩展字段
		if (this.Logon.getExistExtendField()) {
			for (int i = 1; i <= 5; i++) {
				String ls_Name = "FI" + Integer.toString(i);
				ao_Set.put(ls_Name, mo_Extends.get(ls_Name));
				ls_Name = "FSA" + Integer.toString(i);
				ao_Set.put(ls_Name, mo_Extends.get(ls_Name));
				ls_Name = "FSB" + Integer.toString(i);
				ao_Set.put(ls_Name, mo_Extends.get(ls_Name));
			}
		}
		ao_Set.put("WorkItemSource", (this.WorkItemSource.equals("") ? null
				: this.WorkItemSource));
		ao_Set.put("ConfigTransfer", this.ConfigTransfer);
		ao_Set.put("LastUpdateType", this.LastUpdateType);
		ao_Set.put("LastUpdateDate", MGlobal.dateToSqlTime(this.LastUpdateDate));
		ao_Set.put("ResultType", this.ResultType);
		ao_Set.put("ResultDescribe", (this.ResultDescribe.equals("") ? null
				: this.ResultDescribe));
		ao_Set.put("WorkItemProp", this.WorkItemProp);
	}

	/**
	 * 保存获取保存数据库执行状态对象
	 * 
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型：=0-XmlType；=1-OrignType；
	 * @return
	 * @throws SQLException
	 */
	public String getSaveState(int ai_SaveType, int ai_Type)
			throws SQLException {
		String ls_Sql = null;
		if (ai_SaveType == 0) {
			ls_Sql = "INSERT INTO WorkItemInst "
					+ "(WorkItemID, WorkItemName, CreateID, Creator, CreateDate, DeptID, DeptName, "
					+ "EditID, Editor, EditDate, WorkItemStatus, OrignStatus, IsFinished, FinishedDate, "
					+ "HaveBody, HaveDocument, HaveForm, ValidPeriod, WorkItemSecret, "
					+ "ExtendOne, ExtendTwo, ExtendThree, ExtendFour, ExtendFive, "
					+ "ExtendSix, ExtendSeven, ExtendEight, ExtendNine, ExtendTen, "
					+ "WorkItemSource, LastUpdateType, LastUpdateDate, ConfigTransfer, "
					+ "ResultType, ResultDescribe, WorkItemProp, "
					+ "FI1, FI2, FI3, FI4, FI5, FSA1, FSA2, FSA3, FSA4, FSA5, FSB1, FSB2, FSB3, FSB4, FSB5, COMP_ID)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		} else {
			ls_Sql = "UPDATE WorkItemInst SET "
					+ "WorkItemName = ?, CreateID = ?, Creator = ?, CreateDate = ?, DeptID = ?, DeptName = ?, "
					+ "EditID = ?, Editor = ?, EditDate = ?, WorkItemStatus = ?, OrignStatus = ?, IsFinished = ?, FinishedDate = ?, "
					+ "HaveBody = ?, HaveDocument = ?, HaveForm = ?, ValidPeriod = ?, WorkItemSecret = ?, "
					+ "ExtendOne = ?, ExtendTwo = ?, ExtendThree = ?, ExtendFour = ?, ExtendFive = ?, "
					+ "ExtendSix = ?, ExtendSeven = ?, ExtendEight = ?, ExtendNine = ?, ExtendTen = ?, "
					+ "WorkItemSource = ?, LastUpdateType = ?, LastUpdateDate = ?, ConfigTransfer = ?, "
					+ "ResultType = ?, ResultDescribe = ?, WorkItemProp = ?, "
					+ "FI1 = ?, FI2 = ?, FI3 = ?, FI4 = ?, FI5 = ?, FSA1 = ?, FSA2 = ?, FSA3 = ?, FSA4 = ?, FSA5 = ?, "
					+ "FSB1 = ?, FSB2 = ?, FSB3 = ?, FSB4 = ?, FSB5 = ?, COMP_ID = ? WHERE WorkItemID = ?";
		}

		return ls_Sql;
	}

	/**
	 * 保存
	 * 
	 * @param ao_State
	 *            更新状态对象
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @param ai_Update
	 *            更新类型：=0-缺省更新；=1-最后更新；=2-单独更新；
	 * @throws SQLException
	 */
	public int Save(String ao_State, int ai_SaveType, int ai_Type, int ai_Update)
			throws Exception {
		List parasList = new ArrayList();
		if (ai_SaveType == 0)
			parasList.add(this.WorkItemID);

		parasList.add(this.WorkItemName);
		parasList.add(this.CreatorID);
		parasList.add(this.Creator);
		parasList.add(MGlobal.dateToSqlTime(this.CreateDate));
		parasList.add(this.DeptID);
		parasList.add(MGlobal.getDbValue(this.DeptName));
		parasList.add(this.EditorID);
		parasList.add(MGlobal.getDbValue(this.Editor));
		parasList.add(MGlobal.dateToSqlTime(this.EditDate));
		parasList.add(this.Status);
		parasList.add(this.OrignStatus);
		parasList.add((getIsFinished() ? 1 : 0));
		parasList.add(MGlobal.dateToSqlTime(this.FinishedDate));
		parasList.add((this.HaveBody ? 1 : 0));
		parasList.add((this.HaveDocument ? 1 : 0));
		parasList.add((this.HaveForm ? 1 : 0));
		parasList.add(this.ValidLimited);
		parasList.add(this.Secutity);
		parasList.add(this.ExtendOne);
		parasList.add(this.ExtendTwo);
		parasList.add(MGlobal.getDbValue(this.ExtendThree));
		parasList.add(MGlobal.dateToSqlTime(this.ExtendFour));
		parasList.add(MGlobal.getDbValue(this.ExtendFive));
		parasList.add(MGlobal.getDbValue(this.ExtendSix));
		parasList.add(MGlobal.getDbValue(this.ExtendSeven));
		parasList.add(MGlobal.getDbValue(this.ExtendEight));
		parasList.add(MGlobal.getDbValue(this.ExtendNine));
		parasList.add(MGlobal.getDbValue(this.ExtendTen));
		parasList.add(MGlobal.getDbValue(this.WorkItemSource));
		parasList.add(this.LastUpdateType);
		parasList.add(MGlobal.dateToSqlTime(this.LastUpdateDate));
		parasList.add(this.ConfigTransfer);
		parasList.add(this.ResultType);
		parasList.add(MGlobal.getDbValue(this.ResultDescribe));
		parasList.add(this.WorkItemProp);

		// 判断公文是不是扩展字段
		if (this.Logon.getExistExtendField()) {
			for (int i = 0; i < 15; i++) {
				if (i < 5) {
					parasList.add(mo_Extends.get("FI" + String.valueOf(i)));
				} else if (i < 10) {
					parasList.add(MGlobal.getDbValue((String) mo_Extends
							.get("FSA" + String.valueOf(i - 5))));
				} else {
					parasList.add(MGlobal.getDbValue((String) mo_Extends
							.get("FSB" + String.valueOf(i - 10))));
				}
			}
		}
		parasList.add(this.BelongID);

		if (ai_SaveType == 1)
			parasList.add(this.WorkItemID);

		return CSqlHandle.getJdbcTemplate().update(ao_State,
				parasList.toArray());
	}

	/**
	 * 从数据库行对象中读取数据到对象
	 * 
	 * @param ao_Set
	 *            打开的结果集
	 * @param ai_Type
	 *            打开方式：=0-正常打开；=1-短属性打开；=9-管理打开；
	 * @throws Exception
	 */
	@Override
	public void Open(Map<String, Object> ao_Set, int ai_Type) throws Exception {
		// Add Open Code...
		this.WorkItemID = MGlobal.readInt(ao_Set, "WorkItemID");
		this.WorkItemName = MGlobal.readString(ao_Set,"WorkItemName");
		this.CreatorID = MGlobal.readInt(ao_Set, "CreateID");
		this.Creator = MGlobal.readString(ao_Set,"Creator");
		this.DeptID = MGlobal.readInt(ao_Set, "DeptID");
		this.DeptName = MGlobal.readString(ao_Set, "DeptName");
		this.CreateDate = MGlobal.readTime(ao_Set, "CreateDate");
		this.BelongID = MGlobal.readInt(ao_Set, "COMP_ID");
		this.EditorID = MGlobal.readInt(ao_Set, "EditID");
		this.Editor = MGlobal.readString(ao_Set, "Editor");
		this.EditDate = MGlobal.readTime(ao_Set, "EditDate");

		this.Status = MGlobal.readInt(ao_Set,"WorkItemStatus");

		if (ai_Type == 9)
			return;

		this.OrignStatus = MGlobal.readInt(ao_Set, "OrignStatus");
		this.FinishedDate = MGlobal.readTime(ao_Set, "FinishedDate");

		this.HaveBody = (MGlobal.readInt(ao_Set,"HaveBody") == 1);
		this.HaveDocument = (MGlobal.readInt(ao_Set,"HaveDocument") == 1);
		this.HaveForm = (MGlobal.readInt(ao_Set,"HaveForm") == 1);
		this.ValidLimited = MGlobal.readInt(ao_Set,"ValidPeriod");
		this.Secutity = MGlobal.readInt(ao_Set,"WorkItemSecret");
		this.ExtendOne = MGlobal.readInt(ao_Set,"ExtendOne");
		this.ExtendTwo = MGlobal.readInt(ao_Set,"ExtendTwo");
		this.ExtendThree = MGlobal.readString(ao_Set, "ExtendThree");
		this.ExtendFour = MGlobal.readTime(ao_Set, "ExtendFour");
		this.ExtendFive = MGlobal.readString(ao_Set, "ExtendFive");
		this.ExtendSix = MGlobal.readString(ao_Set, "ExtendSix");
		this.ExtendSeven = MGlobal.readString(ao_Set, "ExtendSeven");
		this.ExtendEight = MGlobal.readString(ao_Set, "ExtendEight");
		this.ExtendNine = MGlobal.readString(ao_Set, "ExtendNine");
		this.ExtendTen = MGlobal.readString(ao_Set, "ExtendTen");

		// 判断公文是不是扩展字段
		if (this.Logon.getExistExtendField()) {
			mo_Extends.clear();
			for (int i = 1; i <= 15; i++) {
				if (i < 6) {
					String ls_Name = "FI" + String.valueOf(i);
					mo_Extends.put(ls_Name, MGlobal.readInt(ao_Set, ls_Name));
				} else if (i < 11) {
					String ls_Name = "FSA" + String.valueOf(i - 5);
					mo_Extends
							.put(ls_Name, MGlobal.readString(ao_Set, ls_Name));
				} else {
					String ls_Name = "FSB" + String.valueOf(i - 10);
					mo_Extends
							.put(ls_Name, MGlobal.readString(ao_Set, ls_Name));
				}
			}
		}

		this.WorkItemSource = MGlobal.readString(ao_Set, "WorkItemSource");
		this.ConfigTransfer = MGlobal.readInt(ao_Set, "ConfigTransfer");
		this.LastUpdateType = MGlobal.readInt(ao_Set, "LastUpdateType");
		this.LastUpdateDate = MGlobal.readTime(ao_Set, "LastUpdateDate");
		this.ResultType = MGlobal.readInt(ao_Set, "ResultType");
		this.ResultDescribe = MGlobal.readString(ao_Set, "ResultDescribe");
		this.WorkItemProp = MGlobal.readInt(ao_Set, "WorkItemProp");
	}

	/**
	 * 对象打包
	 * 
	 * @param ao_Bag
	 *            包对象
	 * @param ai_Type
	 *            类型：=0-正常导出；=1-短属性导出；
	 */
	@Override
	protected void Write(MBag ao_Bag, int ai_Type) {
		ao_Bag.Write("ml_WorkItemID", this.WorkItemID);
		ao_Bag.Write("ms_WorkItemName", this.WorkItemName);
		ao_Bag.Write("ml_CreatorID", this.CreatorID);
		ao_Bag.Write("ms_Creator", this.Creator);
		ao_Bag.Write("ml_DeptID", this.DeptID);
		ao_Bag.Write("ms_DeptName", this.DeptName);
		ao_Bag.Write("md_CreateDate", this.CreateDate);
		ao_Bag.Write("ml_EditorID", this.EditorID);
		ao_Bag.Write("ms_Editor", this.Editor);
		ao_Bag.Write("md_EditDate", this.EditDate);
		ao_Bag.Write("mi_Status", this.Status);
		ao_Bag.Write("mi_OrignStatus", this.OrignStatus);
		ao_Bag.Write("md_FinishedDate", this.FinishedDate);
		ao_Bag.Write("mb_HaveBody", this.HaveBody);
		ao_Bag.Write("mb_HaveDocument", this.HaveDocument);
		ao_Bag.Write("mb_HaveForm", this.HaveForm);
		ao_Bag.Write("mi_ValidLimited", this.ValidLimited);
		ao_Bag.Write("mi_Secutity", this.Secutity);
		ao_Bag.Write("mi_ExtendOne", this.ExtendOne);
		ao_Bag.Write("ml_ExtendTwo", this.ExtendTwo);
		ao_Bag.Write("ms_ExtendThree", this.ExtendThree);
		ao_Bag.Write("md_ExtendFour", this.ExtendFour);
		ao_Bag.Write("ms_ExtendFive", this.ExtendFive);
		ao_Bag.Write("ms_ExtendSix", this.ExtendSix);
		ao_Bag.Write("ms_ExtendSeven", this.ExtendSeven);
		ao_Bag.Write("ms_ExtendEight", this.ExtendEight);
		ao_Bag.Write("ms_ExtendNine", this.ExtendNine);
		ao_Bag.Write("ms_ExtendTen", this.ExtendTen);
		ao_Bag.Write("ml_BelongID", this.BelongID);
		for (int i = 1; i <= 5; i++) {
			String ls_Name = "FI" + String.valueOf(i);
			ao_Bag.Write(ls_Name,
					Integer.parseInt(mo_Extends.get(ls_Name).toString()));
			ls_Name = "FSA" + String.valueOf(i);
			ao_Bag.Write(ls_Name, (String) mo_Extends.get(ls_Name));
			ls_Name = "FSB" + String.valueOf(i);
			ao_Bag.Write(ls_Name, (String) mo_Extends.get(ls_Name));
		}
		ao_Bag.Write("ms_WorkItemSource", this.WorkItemSource);
		ao_Bag.Write("mi_ConfigTransfer", this.ConfigTransfer);
		ao_Bag.Write("mi_LastUpdateType", this.LastUpdateType);
		ao_Bag.Write("md_LastUpdateDate", this.LastUpdateDate);
		ao_Bag.Write("mi_ResultType", this.ResultType);
		ao_Bag.Write("ms_ResultDescribe", this.ResultDescribe);
		ao_Bag.Write("mi_WorkItemProp", this.WorkItemProp);
	}

	/**
	 * 对象解包
	 * 
	 * @param ao_Bag
	 *            包对象
	 * @param ai_Type
	 *            类型：=0-正常导入；=1-短属性导入；
	 */
	@Override
	protected void Read(MBag ao_Bag, int ai_Type) {
		this.WorkItemID = ao_Bag.ReadInt("ml_WorkItemID");
		this.WorkItemName = ao_Bag.ReadString("ms_WorkItemName");
		this.CreatorID = ao_Bag.ReadInt("ml_CreatorID");
		this.Creator = ao_Bag.ReadString("ms_Creator");
		this.DeptID = ao_Bag.ReadInt("ml_DeptID");
		this.DeptName = ao_Bag.ReadString("ms_DeptName");
		this.CreateDate = ao_Bag.ReadDate("this.CreateDate");
		this.EditorID = ao_Bag.ReadInt("ml_EditorID");
		this.Editor = ao_Bag.ReadString("ms_Editor");
		this.EditDate = ao_Bag.ReadDate("md_EditDate");
		this.Status = ao_Bag.ReadInt("mi_Status");
		this.OrignStatus = ao_Bag.ReadInt("mi_OrignStatus");
		this.FinishedDate = ao_Bag.ReadDate("md_FinishedDate");
		this.HaveBody = ao_Bag.ReadBoolean("mb_HaveBody");
		this.HaveDocument = ao_Bag.ReadBoolean("mb_HaveDocument");
		this.HaveForm = ao_Bag.ReadBoolean("mb_HaveForm");
		this.ValidLimited = ao_Bag.ReadInt("mi_ValidLimited");
		this.Secutity = ao_Bag.ReadInt("mi_Secutity");
		this.ExtendOne = ao_Bag.ReadInt("mi_ExtendOne");
		this.ExtendTwo = ao_Bag.ReadInt("ml_ExtendTwo");
		this.ExtendThree = ao_Bag.ReadString("ms_ExtendThree");
		this.ExtendFour = ao_Bag.ReadDate("md_ExtendFour");
		this.ExtendFive = ao_Bag.ReadString("ms_ExtendFive");
		this.ExtendSix = ao_Bag.ReadString("ms_ExtendSix");
		this.ExtendSeven = ao_Bag.ReadString("ms_ExtendSeven");
		this.ExtendEight = ao_Bag.ReadString("ms_ExtendEight");
		this.ExtendNine = ao_Bag.ReadString("ms_ExtendNine");
		this.ExtendTen = ao_Bag.ReadString("ms_ExtendTen");
		this.BelongID = ao_Bag.ReadInt("ml_BelongID");
		mo_Extends.clear();
		for (int i = 1; i <= 5; i++) {
			String ls_Name = "FI" + String.valueOf(i);
			mo_Extends.put(ls_Name, ao_Bag.ReadInt(ls_Name));
			mo_Extends.put(ls_Name, ao_Bag.ReadString(ls_Name));
			mo_Extends.put(ls_Name, ao_Bag.ReadString(ls_Name));
		}
		this.WorkItemSource = ao_Bag.ReadString("ms_WorkItemSource");
		this.ConfigTransfer = ao_Bag.ReadInt("mi_ConfigTransfer");
		this.LastUpdateType = ao_Bag.ReadInt("mi_LastUpdateType");
		this.LastUpdateDate = ao_Bag.ReadDate("md_LastUpdateDate");
		this.ResultType = ao_Bag.ReadInt("mi_ResultType");
		this.ResultDescribe = ao_Bag.ReadString("ms_ResultDescribe");
		this.WorkItemProp = ao_Bag.ReadInt("mi_WorkItemProp");
	}

	/**
	 * 权限实例化套用
	 */
	public boolean RightToInst() {
		if (this.CurRight == null)
			return false;

		// 正文权限[设置读写属性]
		if (this.Cyclostyle.HaveBody) {
			this.Cyclostyle.Body.RightType = this.CurRight.BodyAccess;
			if (!this.WorkItemAccess) {
				if (this.Cyclostyle.Body.RightType > EDocumentRightType.ReadOnlyDocument)
					this.Cyclostyle.Body.RightType = EDocumentRightType.ReadOnlyDocument;
			}
		}

		// 附件权限[设置读写属性]
		for (CDocument lo_Document : this.Cyclostyle.Documents) {
			lo_Document.RightType = this.CurRight.DocumentAccess;
			if (!this.WorkItemAccess) {
				if (lo_Document.RightType > EDocumentRightType.ReadOnlyDocument)
					lo_Document.RightType = EDocumentRightType.ReadOnlyDocument;
			}
		}

		// 表单权限[设置可见性]
		String ls_DisVisibleIDs = ";" + this.CurRight.getCellDisVisibleIDs();
		String ls_ValidHandleIDs = ";" + this.CurRight.getCellValidHandleIDs();
		String ls_NeedHandleIDs = ";" + this.CurRight.getCellNeedHandleIDs();
		for (CForm lo_Form : this.Cyclostyle.Forms) {
			lo_Form.Access = ((";" + this.CurRight.getVisbleFormNames())
					.indexOf(";" + lo_Form.FormName + ";") == -1 ? EFormAccessType.FormDisVisible
					: EFormAccessType.FormVisble);
			if (lo_Form.Access == EFormAccessType.FormVisble) {
				for (CFormCell lo_Cell : lo_Form.FormCells) {
					String ls_Value = ";" + String.valueOf(lo_Cell.CellID)
							+ ";";
					if (ls_DisVisibleIDs.indexOf(ls_Value) > -1) {
						lo_Cell.Access = EFormCellAccessType.FormCellDisVisible;
					} else {
						if (ls_ValidHandleIDs.indexOf(ls_Value) > -1) {
							lo_Cell.Access = EFormCellAccessType.FormCellValidHandle;
						} else {
							if (ls_NeedHandleIDs.indexOf(ls_Value) > -1) {
								lo_Cell.Access = EFormCellAccessType.FormCellNeedHandle;
							} else {
								lo_Cell.Access = EFormCellAccessType.FormCellReadOnly;
							}
							if (this.WorkItemAccess) {
								if (lo_Cell.Access > EFormCellAccessType.FormCellReadOnly)
									lo_Cell.Access = EFormCellAccessType.FormCellReadOnly;
							}
						}
					}
				}
			}
		}

		// 角色权限[设置计算方式]
		String ls_Opens = ";" + this.CurRight.RoleOpenAccesses;
		String ls_Sends = ";" + this.CurRight.RoleSendAccesses;
		String ls_Saves = ";" + this.CurRight.RoleSaveAccesses;
		String ls_SendAfters = ";" + this.CurRight.RoleSendAfterAccesses;
		for (CRole lo_Role : this.Cyclostyle.Roles) {
			String ls_Value = ";" + String.valueOf(lo_Role.RoleID) + ";";
			int li_Type = (ls_Opens.indexOf(ls_Value) == -1 ? ECaculateStatusType.NeedNotCaculate
					: ECaculateStatusType.CaculateOpenStatus);
			if (ls_Sends.indexOf(ls_Value) > -1)
				li_Type += ECaculateStatusType.CaculateSendStatus;
			if (ls_Saves.indexOf(ls_Value) > -1)
				li_Type += ECaculateStatusType.CaculateSaveStatus;
			if (ls_SendAfters.indexOf(ls_Value) > -1)
				li_Type += ECaculateStatusType.CaculateSendAfterStatus;
			lo_Role.CaculateStatus = li_Type;
			if (this.WorkItemAccess)
				lo_Role.CaculateStatus = ECaculateStatusType.NeedNotCaculate;
		}

		// 属性权限[设置计算方式]
		ls_Opens = ";" + this.CurRight.PropOpenAccesses;
		ls_Sends = ";" + this.CurRight.PropSendAccesses;
		ls_Saves = ";" + this.CurRight.PropSaveAccesses;
		ls_SendAfters = ";" + this.CurRight.PropSendAfterAccesses;
		for (CProp lo_Prop : this.Cyclostyle.Props) {
			String ls_Value = ";" + String.valueOf(lo_Prop.PropID) + ";";
			int li_Type = (ls_Opens.indexOf(ls_Value) == -1 ? ECaculateStatusType.NeedNotCaculate
					: ECaculateStatusType.CaculateOpenStatus);
			if (ls_Sends.indexOf(ls_Value) > -1)
				li_Type += ECaculateStatusType.CaculateSendStatus;
			if (ls_Saves.indexOf(ls_Value) > -1)
				li_Type += ECaculateStatusType.CaculateSaveStatus;
			if (ls_SendAfters.indexOf(ls_Value) > -1)
				li_Type += ECaculateStatusType.CaculateSendAfterStatus;
			lo_Prop.setCaculateStatus(li_Type);
			if (this.WorkItemAccess)
				lo_Prop.setCaculateStatus(ECaculateStatusType.NeedNotCaculate);
		}

		return true;
	}

	/**
	 * 取步骤的后继步骤标识
	 * 
	 * @param ao_Activity
	 *            参照步骤
	 * @return
	 */
	public String getNextActivityIDs(CActivity ao_Activity) {
		return getNextActivityIDs(ao_Activity, ";");
	}

	/**
	 * 取步骤的后继步骤标识
	 * 
	 * @param ao_Activity
	 *            参照步骤
	 * @param as_HaveActIDs
	 *            已得到的步骤标识，避免重复
	 * @return
	 */
	private String getNextActivityIDs(CActivity ao_Activity,
			String as_HaveActIDs) {
		String ls_Result = "";
		String ls_CacuActIDs = as_HaveActIDs;
		boolean lb_Boolean = false;// 记录根据条件连线是否存在下继步骤

		for (CFlowLine lo_Line : ao_Activity.Flow.FlowLines) {
			if (lo_Line.SourceActivity == ao_Activity) {
				lb_Boolean = true;
				CActivity lo_Goal = lo_Line.GoalActivity;
				if (ls_CacuActIDs.indexOf(";"
						+ String.valueOf(lo_Goal.ActivityID) + ";") == -1) {
					ls_CacuActIDs += String.valueOf(lo_Goal.ActivityID) + ";";
					switch (lo_Goal.ActivityType) {
					// 分支步骤继续查找
					case EActivityType.SplitActivity:
						ls_Result += getNextActivityIDs(lo_Goal, ls_CacuActIDs);
						break;
					// 嵌入和启动步骤，先加入后再继续查找
					case EActivityType.TumbleInActivity:
					case EActivityType.LaunchActivity:
						ls_Result += String.valueOf(lo_Goal.ActivityID) + ";"
								+ getNextActivityIDs(lo_Goal, ls_CacuActIDs);
						break;
					// 开始、处理、通知和结束步骤只做查找
					case EActivityType.StartActivity:
					case EActivityType.TransactActivity:
					case EActivityType.FYIActivity:
					case EActivityType.StopActivity:
						ls_Result += String.valueOf(lo_Goal.ActivityID) + ";";
						break;
					default:
						break;
					}
				}
			}
		}

		if (!lb_Boolean) {// 根据条件连线未能找到下继步骤情况，使用顺序流转方式
			int ll_OrderID = -1;
			int ll_CurOrderID = ao_Activity.OrderID;
			for (CActivity lo_Act : ao_Activity.Flow.Activities) {
				if (lo_Act.OrderID > ll_CurOrderID) {
					if (!(lo_Act.ActivityType == EActivityType.FYIActivity || lo_Act.ActivityType == EActivityType.SplitActivity)) {
						if (ll_OrderID > lo_Act.OrderID || ll_OrderID == -1)
							ll_OrderID = lo_Act.OrderID;
					}
				}
			}

			if (ll_OrderID != -1) {
				CActivity lo_Goal = ao_Activity.Flow
						.getActivityByOrder(ll_OrderID);
				ls_Result = String.valueOf(lo_Goal.ActivityID) + ";";
			}
		}

		return ls_Result;
	}

	/**
	 * 排列步骤顺序
	 * 
	 * @param as_Ids
	 * @return
	 */
	private String orderActivityIDs(String as_Ids) {
		if (as_Ids.length() - as_Ids.replaceAll(";", "").length() < 2)
			return as_Ids;

		String[] ls_Array = as_Ids.split(";");
		String ls_ID = "";
		ArrayList<CActivity> lo_List = new ArrayList<CActivity>();
		for (int i = 0; i < ls_Array.length; i++) {
			ls_ID = ls_Array[i];
			if (!ls_ID.equals("")) {
				CActivity lo_Act = this.CurFlow.getActivityById(Integer
						.parseInt(ls_ID));
				if (lo_List.size() == 0) {
					lo_List.add(lo_Act);
				} else {
					boolean lb_Boolean = true;
					int j = 0;
					for (CActivity lo_Activity : lo_List) {
						if (lo_Activity.OrderID > lo_Act.OrderID) {
							lo_List.add(j, lo_Act); // 看看是否需要调整
							lb_Boolean = false;
							break;
						}
						j++;
					}
					if (lb_Boolean)
						lo_List.add(lo_Act);
				}
			}
		}
		ls_ID = "";
		for (CActivity lo_Act : lo_List) {
			ls_ID += String.valueOf(lo_Act.ActivityID) + ";";
		}
		return ls_ID;
	}

	/**
	 * 以下函数提供一些常用的二次开发函数给用户直接使用而无须书写
	 */

	/**
	 * 提取控制属性值
	 * 
	 * @param as_PropName
	 *            控制属性名称
	 * @return
	 * @throws Exception
	 */
	public String getPropValue(String as_PropName) throws Exception {
		CProp lo_Prop = this.Cyclostyle.getPropByName(as_PropName);
		if (lo_Prop == null)
			return "";
		return lo_Prop.getValue();
	}

	/**
	 * 设置控制属性值
	 * 
	 * @param as_PropName
	 *            控制属性名称
	 * @param as_Value
	 *            控制属性值
	 * @return
	 */
	public boolean setPropValue(String as_PropName, String as_Value) {
		CProp lo_Prop = this.Cyclostyle.getPropByName(as_PropName);
		if (lo_Prop == null)
			return false;
		lo_Prop.setValue(as_Value);
		return true;
	}

	/**
	 * 根据角色名称获得角色值
	 * 
	 * @param as_RoleName
	 *            角色名称
	 * @param ai_ReturnType
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String getRoleValue(String as_RoleName, int ai_ReturnType)
			throws SQLException {
		CRole lo_Role = this.Cyclostyle.getRoleByName(as_RoleName);
		if (lo_Role == null)
			return "";
		return getRoleUsers(lo_Role, ai_ReturnType);
	}

	/**
	 * 解析用户定义角色，返回格式：“用户标识1;用户标识2;...”
	 * 
	 * @param ao_Role
	 *            角色对象
	 * @param ai_ReturnType
	 *            返回值的类型：=0-返回用户标识；=1-返回用户名称；=2-返回用户代码；
	 * @return
	 * @throws SQLException
	 */
	private String getRoleUsers(CRole ao_Role, int ai_ReturnType)
			throws SQLException {
		if (ao_Role == null)
			return "";

		// 固定收件人<用户>
		String ls_Users = "";
		// 解析角色，格式如返回格式：用户名称[用户标识]
		String ls_Participants = ao_Role.Value;
		if (MGlobal.isEmpty(ls_Participants))
			return "";
		// 角色人员[未解析]
		String[] ls_PartEvery = ls_Participants.split(";");
		for (int i = 0; i < ls_PartEvery.length; i++) {
			if (MGlobal.isEmpty(ls_PartEvery[i]))
				break;
			String ls_One = ls_PartEvery[i].substring(ls_PartEvery[i].length()
					- (ls_PartEvery[i].length() - 1),
					ls_PartEvery[i].length() - 1);
			String ls_Value = ls_PartEvery[i].substring(0, 1);
			if (ls_Value.equals("U")) {// 固定收件人<用户/分组>

			} else if (ls_Value.equals("G")) {
				// 解析并替换'固定收件人<用户/分组>
				ls_Users += ls_One + ",";
			} else if (ls_Value.equals("@")) {// 用户角色
				// 解析并替换用户角色
				ls_Users += getRoleUsers(
						this.Cyclostyle.getRoleById(Integer.parseInt(ls_One)),
						0) + ",";
			} else if (ls_Value.equals("#")) {// 系统角色<当前收件人的>
				// 解析并替换系统角色<当前收件人的>
				ls_Users += getSystemRoleUsers(this.CurEntry.ParticipantID,
						Integer.parseInt(ls_One), "#") + ",";
			} else if (ls_Value.equals("&")) {// 系统角色<拟稿人的>
				// 解析并替换系统角色<拟稿人的>
				ls_Users += getSystemRoleUsers(this.CreatorID,
						Integer.parseInt(ls_One), "&")
						+ ",";
			} else { // 固定收件人<用户/分组>
				ls_Users += ls_PartEvery[i] + ",";
			}

		}

		ls_Participants = "";
		// 解析并替换固定用户
		if (!MGlobal.isEmpty(ls_Users)) {
			ls_Users = ls_Users.substring(0, ls_Users.length() - 1);
			String ls_Sql = "SELECT UserID, UserCode, UserName FROM UserInfo WHERE UserType = 0 AND UserID IN ("
					+ ls_Users
					+ ") OR UserID IN (SELECT UserID FROM UserGroup WHERE GroupID IN ("
					+ ls_Users + "))";
			List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate()
					.queryForList(ls_Sql);

			switch (ai_ReturnType) {
			case 0: // 标识
				for (int i = 0; i < lo_Set.size(); i++) {
					ls_Participants += lo_Set.get(i).get("UserID") + ";";
				}
				break;
			case (short) 1: // 名称
				for (int i = 0; i < lo_Set.size(); i++) {
					ls_Participants += lo_Set.get(i).get("UserName") + ";";
				}
				break;
			case (short) 2: // 代码
				for (int i = 0; i < lo_Set.size(); i++) {
					ls_Participants += lo_Set.get(i).get("UserCode") + ";";
				}
				break;
			}
			lo_Set = null;
		}

		return ls_Participants;
	}

	/**
	 * 查找计算系统角色对应的用户,返回格式如：“用户名称一[用户标识一]角色名称[&角色标识];用户名称二[用户标识二]角色名称[&角色标识];...”
	 * 
	 * @param al_UserID
	 *            需要找出系统角色的用户标识
	 * @param al_SystemRoleID
	 *            需要查找的系统角色标识
	 * @param as_RoleSign
	 *            系统角色标志：=&-系统角色<拟稿人的>；=#-系统角色<当前收件人的>；
	 * @return
	 * @throws SQLException
	 */
	private String getSystemRoleUsers(int al_UserID, int al_SystemRoleID,
			String as_RoleSign) throws SQLException {
		// 得到该角色的用户标识
		String ls_RoleUserIDs = "";

		// 所有参照用户部门及其上级部门的部门标识
		String ls_DeptIDs = "-1";

		// 读取参照用户的部门以及上级部门
		String ls_Sql = "SELECT DeptID, ParentID FROM DeptInfo WHERE DeptID <= (SELECT DeptID "
				+ "FROM UserInfo WHERE UserID = "
				+ String.valueOf(al_UserID)
				+ ") ORDER BY DeptID DESC";
		// 使用的结果集
		List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate()
				.queryForList(ls_Sql);
		int ll_Id = -1;
		for (int i = 0; i < lo_Set.size(); i++) {
			ls_DeptIDs += ", " + lo_Set.get(i).get("DeptID");
			if (lo_Set.get(i).get("ParentID").equals("")) {
				break;
			} else {
				ll_Id = (Integer) lo_Set.get(i).get("ParentID");
			}
		}
		lo_Set = null;

		if (!ls_DeptIDs.equals("-1")) {
			ls_Sql = "SELECT UserDeptPosition.*, UserInfo.UserName, UserPosition.PositionName FROM "
					+ "UserDeptPosition, UserInfo, UserPosition WHERE UserDeptPosition.DeptID IN ("
					+ ls_DeptIDs
					+ ") AND UserDeptPosition.PositionID = "
					+ String.valueOf(al_SystemRoleID)
					+ " AND UserDeptPosition.UserID = UserInfo.UserID AND UserDeptPosition.PositionID"
					+ " = UserPosition.PositionID ORDER BY UserDeptPosition.DeptID ASC";
			lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
			ll_Id = -1;
			for (int i = 0; i < lo_Set.size(); i++) {
				if (ll_Id == -1)
					ll_Id = (Integer) lo_Set.get(i).get("DeptID");
				if (ll_Id < (Integer) lo_Set.get(i).get("DeptID"))
					break;
				ls_RoleUserIDs += lo_Set.get(i).get("UserName") + "["
						+ lo_Set.get(i).get("UserID") + "]"
						+ lo_Set.get(i).get("PositionName") + "{" + as_RoleSign
						+ lo_Set.get(i).get("PositionID") + "};";
			}
			lo_Set = null;
		}

		return ls_RoleUserIDs;
	}

	/**
	 * 根据角色名称设置角色值
	 * 
	 * @param as_RoleName
	 *            角色名称
	 * @param as_RoleValue
	 *            设置的角色值
	 * @param ai_SetType
	 *            设置角色值的类型：=0-使用用户名称设置；=1-使用用户标识设置；=2-使用用户代码设置；
	 * @return
	 * @throws SQLException
	 */
	public boolean setRoleValue(String as_RoleName, String as_RoleValue,
			int ai_SetType) throws SQLException {
		CRole lo_Role = this.Cyclostyle.getRoleByName(as_RoleName);
		if (lo_Role == null)
			return false;

		if (MGlobal.isEmpty(as_RoleValue)) {
			lo_Role.Value = "";
			return true;
		}

		String ls_Sql = as_RoleValue.replaceAll(";", ",").trim();
		if (MGlobal.right(ls_Sql, 1).equals(","))
			ls_Sql = ls_Sql.substring(0, ls_Sql.length() - 1);

		switch (ai_SetType) {
		case 0:
			ls_Sql = "UserName IN ('" + ls_Sql.replaceAll(",", "','") + "')";
			break;
		case 1:
			ls_Sql = "UserID IN (" + ls_Sql + ")";
			break;
		default:
			ls_Sql = "UserCode IN ('" + ls_Sql.replaceAll(",", "','") + "')";
			break;
		}
		ls_Sql = "SELECT UserID FROM UserInfo WHERE " + ls_Sql;
		List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate()
				.queryForList(ls_Sql);
		ls_Sql = "";
		for (int i = 0; i < lo_Set.size(); i++) {
			ls_Sql += "U" + lo_Set.get(i).get("UserID") + ";";
		}
		lo_Set = null;

		lo_Role.Value = ls_Sql;
		return true;
	}

	/**
	 * 取某一个状态的总状态
	 * 
	 * @param ao_Entry
	 * @return
	 */
	public CEntry getRootEntry(CEntry ao_Entry) {
		CEntry lo_Entry = ao_Entry;
		if (ao_Entry == null)
			lo_Entry = this.CurEntry;

		while (lo_Entry.ParentEntry != null) {
			lo_Entry = lo_Entry.ParentEntry;
		}

		return lo_Entry;
	}

	/**
	 * 保存获取保存数据库执行状态对象
	 * 
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @param ai_ContentType
	 *            内容存储类型：=0-按公文存储内容；=1-按公文状态存储内容（存在版本）；
	 * @return
	 * @throws SQLException
	 */
	public String getSaveContentState(int ai_SaveType, int ai_Type,
			int ai_ContentType) throws SQLException {
		String ls_Sql = null;
		if (ai_ContentType == 1) {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO WorkEntryContent "
						+ "(ContentIndex, WorkItemID, EntryID, ContentValue, SaveFlag) "
						+ "VALUES (?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE WorkEntryContent SET "
						+ "ContentValue = ?, SaveFlag = ? "
						+ "WHERE ContentIndex = ?, WorkItemID = ?, EntryID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO WorkItemContent "
						+ "(WorkItemID, ContentIndex, ContentValue, SaveFlag) "
						+ "VALUES (?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE WorkItemContent SET "
						+ "ContentValue = ?, SaveFlag = ? "
						+ "WHERE WorkItemID = ? AND ContentIndex = ?";
			}
		}

		return ls_Sql;
	}

	/**
	 * 保存
	 * 
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型：EDataHandleType.OrignType/EDataHandleType.XmlType
	 * @param as_Props
	 *            表头属性打包内容
	 * @param as_Roles
	 *            角色打包内容
	 * @param ab_SaveVersion
	 *            是否保存版本
	 * @param ai_EntrySaveType
	 *            版本保存类型：=0-插入；=1-更新；
	 * @throws SQLException
	 */
	public void SaveContent(int ai_SaveType, int ai_Type, String as_Props,
			String as_Roles, boolean ab_SaveVersion, int ai_EntrySaveType)
			throws Exception {
		String sql = getSaveContentState(ai_SaveType, ai_Type, 0);
		int li_Index = 1;
		if (ai_SaveType == 0) {
			// lo_State.setInt(li_Index++, this.WorkItemID);
			// lo_State.setString(li_Index++, "PropValue");
			// lo_State.setString(li_Index++, as_Props);
			// lo_State.setInt(li_Index++, ai_Type);
			CSqlHandle.getJdbcTemplate().update(
					sql,
					new Object[] { this.WorkItemID, "PropValue", as_Props,
							ai_Type });
		} else {
			// lo_State.setString(li_Index++, as_Props);
			// lo_State.setInt(li_Index++, ai_Type);
			// lo_State.setInt(li_Index++, this.WorkItemID);
			// lo_State.setString(li_Index++, "PropValue");
			CSqlHandle.getJdbcTemplate().update(
					sql,
					new Object[] { as_Props, ai_Type, this.WorkItemID,
							"PropValue" });
		}
		// lo_State.addBatch();
		// lo_State.executeBatch();

		li_Index = 1;
		if (ai_SaveType == 0) {
			// lo_State.setInt(li_Index++, this.WorkItemID);
			// lo_State.setString(li_Index++, "RoleValue");
			// lo_State.setString(li_Index++, as_Roles);
			// lo_State.setInt(li_Index++, ai_Type);
			CSqlHandle.getJdbcTemplate().update(
					sql,
					new Object[] { this.WorkItemID, "RoleValue", as_Roles,
							ai_Type });
		} else {
			// lo_State.setString(li_Index++, as_Roles);
			// lo_State.setInt(li_Index++, ai_Type);
			// lo_State.setInt(li_Index++, this.WorkItemID);
			// lo_State.setString(li_Index++, "RoleValue");
			CSqlHandle.getJdbcTemplate().update(
					sql,
					new Object[] { as_Roles, ai_Type, this.WorkItemID,
							"PropValue" });
		}
		// lo_State.addBatch();
		// lo_State.executeBatch();

		if (ab_SaveVersion) {
			sql = getSaveContentState(ai_SaveType, ai_Type, 1);
			li_Index = 1;
			if (ai_EntrySaveType == 0) {
				// lo_State.setString(li_Index++, "PropValue");
				// lo_State.setInt(li_Index++, this.WorkItemID);
				// lo_State.setInt(li_Index++, this.CurEntry.EntryID);
				// lo_State.setString(li_Index++, as_Props);
				// lo_State.setInt(li_Index++, ai_Type);
				CSqlHandle.getJdbcTemplate().update(
						sql,
						new Object[] { "PropValue", this.WorkItemID,
								this.CurEntry.EntryID, as_Props, ai_Type });
			} else {
				// lo_State.setString(li_Index++, as_Props);
				// lo_State.setInt(li_Index++, ai_Type);
				// lo_State.setString(li_Index++, "PropValue");
				// lo_State.setInt(li_Index++, this.WorkItemID);
				// lo_State.setInt(li_Index++, this.CurEntry.EntryID);
				CSqlHandle.getJdbcTemplate().update(
						sql,
						new Object[] { as_Props, ai_Type, "PropValue",
								this.WorkItemID, this.CurEntry.EntryID });
			}

			li_Index = 1;
			if (ai_EntrySaveType == 0) {
				// lo_State.setString(li_Index++, "RoleValue");
				// lo_State.setInt(li_Index++, this.WorkItemID);
				// lo_State.setInt(li_Index++, this.CurEntry.EntryID);
				// lo_State.setString(li_Index++, as_Roles);
				// lo_State.setInt(li_Index++, (ai_Type ==
				// EDataHandleType.XmlType ? 1 : 0));
				CSqlHandle.getJdbcTemplate().update(
						sql,
						new Object[] { "RoleValue", this.WorkItemID,
								this.CurEntry.EntryID, as_Roles,
								(ai_Type == EDataHandleType.XmlType ? 1 : 0) });
			} else {
				// lo_State.setString(li_Index++, as_Roles);
				// lo_State.setInt(li_Index++, (ai_Type ==
				// EDataHandleType.XmlType ? 1 : 0));
				// lo_State.setString(li_Index++, "RoleValue");
				// lo_State.setInt(li_Index++, this.WorkItemID);
				// lo_State.setInt(li_Index++, this.CurEntry.EntryID);
				CSqlHandle.getJdbcTemplate().update(
						sql,
						new Object[] { as_Roles,
								(ai_Type == EDataHandleType.XmlType ? 1 : 0),
								"RoleValue", this.WorkItemID,
								this.CurEntry.EntryID });
			}

		}
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 * 
	 */
	public void ClearUp() throws Exception {
		this.ClearUp(false);
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @param ab_DeleteGuid
	 *            是否注销公文（解除公文锁）
	 * @throws Exception
	 */
	public void ClearUp(boolean ab_DeleteGuid) throws Exception {
		// 解除公文锁
		if (this.WorkItemID != -1 && this.WorkItemAccess
				&& this.CurEntry != null) {
			if (ab_DeleteGuid) {
				TWorkAdmin.releaseLock(this.Logon, this.WorkItemID,
						this.CurEntry.EntryID);
				this.WorkItemAccess = false;
			}
		}

		this.CurFlow = null;// 当前工作流对象
		this.CurEntry = null;// 实例流转的当前状态
		// 当前权限（由本步骤所拥有的所有权限组成）
		if (this.CurRight != null) {
			this.CurRight.ClearUp();
			this.CurRight = null;
		}
		this.CurActivity = null;// 当前步骤
		this.Logon = null; // 所包含的环境变量
		// 所包含的公文模板
		if (this.Cyclostyle != null) {
			this.Cyclostyle.ClearUp();
			this.Cyclostyle = null;
		}
		// 流转状态集合
		if (this.Entries != null) {
			for (CEntry lo_Entry : this.Entries.values()) {
				lo_Entry.ClearUp();
			}
			this.Entries.clear();
			this.Entries = null;
		}
		// 人工监督管理的集合
		if (this.Supervises != null) {
			while (this.Supervises.size() > 0) {
				CSupervise lo_Supervise = this.Supervises.get(0);
				this.Supervises.remove(lo_Supervise);
				lo_Supervise.ClearUp();
				lo_Supervise = null;
			}
			this.Supervises = null;
		}
		mo_Script = null;// 二次开发接口控件
		// 签名打印处理对象
		// 公文扩展字段集合：FI1~FI5,FSA1~FSA5,FSB1~FSB5
		mo_Extends = null;

		// 注销临时变量
	}

	/**
	 * 以下内容提供公文特定情况的修改使用
	 * 
	 * @param ao_Item
	 *            公文对象
	 * @param as_XmlData
	 *            修改数据，结构：<br>
	 *            <Root WorkItemID="公文标识" DeleteEntry=""删除状态标识连接串，采用“;”分隔""><br>
	 *            <Entry Memo="修改的节点"><br>
	 *            <Entry EntryID="状态标识" Status="状态" PartID="参与人"
	 *            InceptDate="接收时间" ReadingDate="阅读时间" FinishedDate="完成时间"
	 *            OverdueDate="超期时间" RecallDate="撤回时间" Choice="意见选项"
	 *            ActivityID="转发选择步骤标识" Comment="修改意见" /><br>
	 *            </Entry><br>
	 *            <NewEntry><br>
	 *            <Entry ActID="流程标识_步骤标识" ActName="步骤名称" EntryID="来源状态标识"
	 *            UserNames="用户名称连接串" UserIDs="用户标识连接串" />...<br>
	 *            </NewEntry><br>
	 *            <AppendEntry><br>
	 *            <Entry EntryID="虚拟根状态标识" UserName="用户名称连接串" UserID="用户标识连接串"
	 *            />...<br>
	 *            </AppendEntry><br>
	 *            <Prop><br>
	 *            <Prop PropID="属性标识" Value="属性值" />...<br>
	 *            </Prop><br>
	 *            <Role><br>
	 *            <Role RoleID="角色标识" UserName="用户名称连接串" UserID="用户标识连接串" />...<br>
	 *            </Role><br>
	 *            </Root><br>
	 * @return
	 * @throws SQLException
	 */
	public static boolean editWorkItem(CWorkItem ao_Item, String as_XmlData)
			throws SQLException {
		Document lo_Xml = MXmlHandle.LoadXml(as_XmlData);
		if (lo_Xml == null)
			return false;

		// 记录关键日志
		ao_Item.Record("TWorkItem->EditWorkItem", "修改公文：" + as_XmlData);

		Element lo_Root = lo_Xml.getDocumentElement();
		String ls_Sql;
		// 修改公文名称和正文名称
		if (MGlobal.isExist(lo_Root.getAttribute("WorkItemName"))) {
			ls_Sql = "UPDATE WorkItemInst SET WorkItemName = '{0}' WHERE WorkItemID = {1}";
			ls_Sql = MessageFormat.format(ls_Sql, MGlobal.replace(
					lo_Root.getAttribute("WorkItemName"), "'", "''"), String
					.valueOf(ao_Item.WorkItemID));
			CSqlHandle.execSql(ls_Sql);
		}
		if (MGlobal.isExist(lo_Root.getAttribute("BodyName"))) {
			ls_Sql = "UPDATE DocumentInst SET DocumentName = '{0}' WHERE WorkItemID = {1} AND DocumentID = {2}";
			ls_Sql = MessageFormat.format(ls_Sql, MGlobal.replace(
					lo_Root.getAttribute("BodyName"), "'", "''"), String
					.valueOf(ao_Item.Cyclostyle.Body.DocumentID), String
					.valueOf(ao_Item.WorkItemID));
			CSqlHandle.execSql(ls_Sql);
		}

		String lo_Entry = "SELECT * FROM EntryInst WHERE 1=2";
		// String lo_Entry = CSqlHandle.getState(ls_Sql, 0);

		String lo_Response = "SELECT * FROM ResponseLookupRight WHERE 1=2";
		// String lo_Response = CSqlHandle.getState(ls_Sql, 0);

		CEntry lo_RootEntry = ao_Item.getEntryById(1);

		// 全新增加的状态
		Element lo_Node = MXmlHandle.getNodeByName(lo_Root, "NewEntry");
		NodeList lo_List = lo_Node.getChildNodes();
		boolean lb_Boolean = false;
		for (int i = 0; i < lo_List.getLength(); i++) {
			Element lo_Child = (Element) lo_List.item(i);
			for (CFlow lo_Flow : ao_Item.Cyclostyle.Flows) {
				for (CActivity lo_Act : lo_Flow.Activities) {
					if (lo_Child.getAttribute("ActID").equals(
							String.valueOf(lo_Flow.FlowID) + "_"
									+ String.valueOf(lo_Act.ActivityID))) {
						lb_Boolean = true;
						break;
					}
				}
				if (lb_Boolean)
					break;
			}
			if (lb_Boolean) {
				/*
				 * 
				 * CEntry lo_pEntry = ao_Item.insertEntry(VirtualEntry, lo_Act,
				 * Nothing, lo_RootEntry); lo_pEntry.OrginalID =
				 * Integer.valueOf(lo_Child.getAttribute("EntryID"));
				 * lo_Entry.AddNew lo_pEntry.SaveTo(lo_rsEntry, 0,
				 * lo_rsResponse) ls_UserID =
				 * Split(lo_Child.getAttribute("UserIDs"), ";") ls_UserName =
				 * Split(lo_Child.getAttribute("UserNames"), ";") For i =
				 * LBound(ls_UserID) To UBound(ls_UserID) If ls_UserID(i) <> ""
				 * Then Set lo_Entry = InsertEntry(ActualityEntry, lo_Act,
				 * Nothing, lo_pEntry) lo_Entry.OrginalID = lo_pEntry.OrginalID
				 * lo_Entry.ParticipantID = CLng(ls_UserID(i))
				 * lo_Entry.Participant = ls_UserName(i) lo_rsEntry.AddNew Call
				 * lo_Entry.SaveTo(lo_rsEntry, 0) End If Next
				 */
			}
		}

		// 新增加的状态
		/**
		 * Set lo_Node = lo_Xml.documentElement.selectSingleNode("AppendEntry")
		 * For Each lo_Child In lo_Node.childNodes Set lo_pEntry =
		 * theEntries(lo_Child.getAttribute("EntryID")) ls_UserID =
		 * Split(lo_Child.getAttribute("UserID"), ";") ls_UserName =
		 * Split(lo_Child.getAttribute("UserName"), ";") Set lo_Act =
		 * lo_pEntry.theActivity For i = LBound(ls_UserID) To UBound(ls_UserID)
		 * If ls_UserID(i) <> "" Then Set lo_Entry = InsertEntry(ActualityEntry,
		 * lo_Act, Nothing, lo_pEntry) lo_Entry.OrginalID = lo_pEntry.OrginalID
		 * lo_Entry.ParticipantID = CLng(ls_UserID(i)) lo_Entry.Participant =
		 * ls_UserName(i) lo_rsEntry.AddNew Call lo_Entry.SaveTo(lo_rsEntry, 0,
		 * lo_rsResponse) End If Next Next
		 * 
		 * lo_rsEntry.UpdateBatch If Not (lo_rsResponse.BOF And
		 * lo_rsResponse.EOF) Then lo_rsResponse.UpdateBatch
		 * 
		 * lo_rsEntry.Close Set lo_rsEntry = Nothing
		 * 
		 * lo_rsResponse.Close Set lo_rsResponse = Nothing
		 * 
		 * '属性/角色 Dim lo_Prop As TProp, lo_Role As TRole, li_Type As Integer
		 * 
		 * li_Type = 0 Set lo_Node =
		 * lo_Xml.documentElement.selectSingleNode("Prop") If
		 * lo_Node.hasChildNodes Then li_Type = li_Type + 1 For Each lo_Child In
		 * lo_Node.childNodes Set lo_Prop =
		 * theCyclostyle.theProps(lo_Child.getAttribute("PropID")) lo_Prop.Value
		 * = lo_Child.getAttribute("Value") Next End If
		 * 
		 * Set lo_Node = lo_Xml.documentElement.selectSingleNode("Role") If
		 * lo_Node.hasChildNodes Then li_Type = li_Type + 2 For Each lo_Child In
		 * lo_Node.childNodes Set lo_Role =
		 * theCyclostyle.theRoles(lo_Child.getAttribute("RoleID")) lo_Role.Value
		 * = lo_Child.getAttribute("UserID") 'lo_Role.ExteriorValue =
		 * lo_Child.getAttribute("UserName") Next End If
		 * 
		 * Dim ls_Content As String If li_Type > 0 Then Dim lo_Content As New
		 * ADODB.Recordset '存储数据的结果集 With lo_Content .CursorLocation =
		 * adUseClient .Open "SELECT * FROM WorkItemContent WHERE WorkItemID = "
		 * & CStr(ml_WorkItemID), GlobalPara.objConnection, adOpenKeyset,
		 * adLockBatchOptimistic End With
		 * 
		 * If (li_Type And 2) = 2 Then '保存角色更改 ls_Content =
		 * GetPackRoles(theCyclostyle.SaveType) With lo_Content If Not (.BOF And
		 * .EOF) Then .MoveFirst .Find "ContentIndex = 'RoleValue'" End If If
		 * .BOF Or .EOF Then .AddNew .Fields("WorkItemID").Value = ml_WorkItemID
		 * .Fields("ContentIndex").Value = "RoleValue" End If
		 * .Fields("ContentValue").Value = ls_Content .Fields("SaveFlag").Value
		 * = IIf(theCyclostyle.SaveType = OrignType, 0, 1) End With End If
		 * 
		 * If (li_Type And 1) = 1 Then '保存属性更改 ls_Content =
		 * GetPackProps(theCyclostyle.SaveType) With lo_Content If Not (.BOF And
		 * .EOF) Then .MoveFirst .Find "ContentIndex = 'PropValue'" End If If
		 * .BOF Or .EOF Then .AddNew .Fields("WorkItemID").Value = ml_WorkItemID
		 * .Fields("ContentIndex").Value = "PropValue" End If
		 * .Fields("ContentValue").Value = ls_Content .Fields("SaveFlag").Value
		 * = IIf(theCyclostyle.SaveType = OrignType, 0, 1) End With End If
		 * 
		 * lo_Content.UpdateBatch lo_Content.Close Set lo_Content = Nothing End
		 * If
		 * 
		 * '删除状态 If lo_Xml.documentElement.getAttribute("DeleteEntry") <> ""
		 * Then ls_Sql = "" For i = theEntries.Count To 1 Step -1 Set lo_Entry =
		 * theEntries(i) If InStr(1, ";" &
		 * lo_Xml.documentElement.getAttribute("DeleteEntry"), ";" &
		 * CStr(lo_Entry.EntryId) & ";") > 0 Then ls_Sql = ls_Sql &
		 * CStr(lo_Entry.EntryId) & "," theEntries.Remove CStr(lo_Entry.EntryId)
		 * lo_Entry.ClearUp Set lo_Entry = Nothing End If Next For Each lo_Entry
		 * In lo_RootEntry.theChildEntries If lo_Entry.theChildEntries.Count = 0
		 * Then ls_Sql = ls_Sql & CStr(lo_Entry.EntryId) & "," End If Next If
		 * ls_Sql <> "" Then Dim ls_Cond As String: ls_Cond = Left(ls_Sql,
		 * Len(ls_Sql) - 1) ls_Sql = "DELETE FROM EntryInst WHERE WorkItemID = "
		 * & CStr(ml_WorkItemID) & " AND EntryID IN (" & ls_Cond & ")"
		 * GlobalPara.objConnection.Execute ls_Sql
		 * 
		 * '删除提醒信息 ls_Sql =
		 * "UPDATE RemindMessage SET RemindStatus = 1 WHERE RemindStatus = 0 AND Parameter1 = "
		 * & CStr(ml_WorkItemID) ls_Sql = ls_Sql & " AND Parameter2 IN (" &
		 * ls_Cond & ")" GlobalPara.objConnection.Execute ls_Sql End If End If
		 * 
		 * '修改状态 Set lo_Node = lo_Xml.documentElement.selectSingleNode("Entry")
		 * If Not lo_Node Is Nothing Then ls_Sql = "" For Each lo_Child In
		 * lo_Node.childNodes ls_Sql = ls_Sql & lo_Child.getAttribute("EntryID")
		 * & "," Next If ls_Sql <> "" Then ls_Sql = Left(ls_Sql, Len(ls_Sql) -
		 * 1) ls_Sql = "SELECT * FROM EntryInst WHERE WorkItemID = " &
		 * CStr(ml_WorkItemID) & " AND EntryID IN (" & ls_Sql & ")" Set
		 * lo_rsEntry = New ADODB.Recordset lo_rsEntry.CursorLocation =
		 * adUseClient Call lo_rsEntry.Open(ls_Sql, GlobalPara.objConnection,
		 * adOpenKeyset, adLockBatchOptimistic) While (Not lo_rsEntry.EOF) Set
		 * lo_Entry = theEntries(CStr(lo_rsEntry.Fields("EntryID"))) Set
		 * lo_Child = lo_Node.selectSingleNode("Entry[@EntryID='" &
		 * CStr(lo_rsEntry.Fields("EntryID")) & "']") With lo_Entry .EntryStatus
		 * = CInt(lo_Child.getAttribute("Status")) '状态 .ParticipantID =
		 * CLng(lo_Child.getAttribute("PartID")) .Participant =
		 * lo_Child.getAttribute("PartName") If
		 * IsDate(lo_Child.getAttribute("InceptDate")) Then .InceptDate =
		 * CDate(lo_Child.getAttribute("InceptDate")) If
		 * IsDate(lo_Child.getAttribute("ReadingDate")) Then .ReadingDate =
		 * CDate(lo_Child.getAttribute("ReadingDate")) If
		 * IsDate(lo_Child.getAttribute("FinishedDate")) Then .FinishedDate =
		 * CDate(lo_Child.getAttribute("FinishedDate")) If
		 * IsDate(lo_Child.getAttribute("OverdueDate")) Then .OverdueDate =
		 * CDate(lo_Child.getAttribute("OverdueDate")) If
		 * IsDate(lo_Child.getAttribute("RecallDate")) Then .RecallDate =
		 * CDate(lo_Child.getAttribute("RecallDate")) If
		 * CInt(lo_Child.getAttribute("Choice")) > 0 Then .Choice =
		 * CInt(lo_Child.getAttribute("Choice")) If
		 * CLng(lo_Child.getAttribute("ActivityID")) > 0 Then
		 * .TransmitActivityID = CLng(lo_Child.getAttribute("ActivityID")) If
		 * .Comment = lo_Node.getAttribute("Comment") Then Call
		 * lo_Entry.SaveTo(lo_rsEntry, 1) Else .Comment =
		 * lo_Child.getAttribute("Comment") .ResponseContent = .Comment Call
		 * lo_Entry.SaveTo(lo_rsEntry, 0) End If End With lo_rsEntry.MoveNext
		 * Wend lo_rsEntry.UpdateBatch lo_rsEntry.Close Set lo_rsEntry = Nothing
		 * End If End If
		 * 
		 * Set lo_Xml = Nothing
		 * 
		 * EditWorkItem = True Exit Function l_Err: GlobalPara.RaiseError
		 * Err.Number, TypeName(Me) & "::EditWorkItem Function", Err.Description
		 * End Function
		 */
		return false;
	}

	/**
	 * 获取状态数量
	 * 
	 * @param ai_Type
	 *            获取类型：=0-所有未处理状态（不含虚拟状态）；=1-所有状态（不含虚拟状态） ；=2-所有已处理状态（不含虚拟状态）；
	 * @return
	 */
	public int getEntryCount(int ai_Type) {
		return getEntryCountAll(ai_Type, false);
	}

	/**
	 * 获取状态数量
	 * 
	 * @param ai_Type
	 *            获取类型：=0-所有未处理状态（不含虚拟状态）；=1-所有状态（不含虚拟状态） ；=2-所有已处理状态（不含虚拟状态）；
	 * @param ab_Include
	 *            是否包含当前状态，缺省不包含；
	 * @return
	 */
	public int getEntryCountAll(int ai_Type, boolean ab_Include) {
		int li_Count = 0;
		for (CEntry lo_Entry : this.Entries.values()) {
			if (lo_Entry.EntryType == EEntryType.ActualityEntry) {
				switch (ai_Type) {
				case 0:
					if (lo_Entry == this.CurEntry) {
						if (ab_Include)
							li_Count++;
					} else {
						if (lo_Entry.EntryStatus < EEntryStatus.HadTransactStatus)
							li_Count++;
					}
					break;
				case 1:
					li_Count++;
					break;
				case 2:
					if (lo_Entry == this.CurEntry) {
						if (!ab_Include)
							li_Count++;
					} else {
						if (lo_Entry.EntryStatus >= EEntryStatus.HadTransactStatus)
							li_Count++;
					}					
					break;
				default:
					break;
				}
			}
		}
		return li_Count;
	}

}