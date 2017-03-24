package org.szcloud.framework.workflow.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.EParticipantRangeType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 步骤收件人对象：对象模型中使用于流程步骤参与者，它包括固定收件人、角色(系统角色和用户定制的角色)、<br>
 * 由二次开发函数计算出的收件人以及一些不定的收件人范围（由实际使用公文流转的用户来选择）。是步骤对象<br>
 * 的一个子对象。
 * 
 * @author Jackie.Wang
 * 
 */
public class CParticipant extends CBase {

	/**
	 * 初始化
	 */
	public CParticipant() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CParticipant(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		this.Activity = null;

		super.ClearUp();
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的步骤
	 */
	public CActivity Activity = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 收件人，需要按照一定的顺序排序<br>
	 * 由：固定收件人、用户角色、系统角色组成，格式如下:<br>
	 * “用户一标识;用户二标;@拟稿人标;用户三标;&拟稿人的经理标识;@部门领导标识;用户N标识;...”<br>
	 * 其中: <br>
	 * *** —> 固定收件人用户或分组<br>
	 * @*** —> 用户角色<br>
	 * &*** —> 系统角色<拟稿人的><br>
	 * #*** —> 系统角色<当前收件人的>
	 */
	public String Participant = "";

	/**
	 * 读取外部显示收件人
	 */
	public String getDisplayParticipant() {
		return dataToParticipant(this.Participant, false);
	}

	/**
	 * 设置外部显示收件人
	 */
	public void setDisplayParticipant(String value) {
		this.Participant = participantToData(value);
	}

	/**
	 * 计算公式：用来计算出所要发送的用户
	 */
	public String CaculateFormula = "";

	/**
	 * 收件人范围类型
	 */
	public int RangeType = EParticipantRangeType.NotExistSelectParticipant;

	/**
	 * 收件人范围：限制当前步骤收件人的选择范围
	 */
	public String ParticipantRange = "";

	/**
	 * 是否需要弹出选取下继步骤收件人的选取对话框
	 */
	public boolean SelectParticipant = false;

	/**
	 * 选取人员时的最大限制人数(=0表示没有限制)
	 */
	public int MaxLimitedNum = 0;

	/**
	 * 设置选取人员时的最大限制人数(=0表示没有限制)
	 * 
	 * @param value
	 */
	public void setMaxLimitedNum(int value) {
		if (value < 0) {
			this.MaxLimitedNum = 0;
		} else {
			this.MaxLimitedNum = value;
		}
	}

	/**
	 * 选取人员时需要选择的次数(=0表示不限制选择次数，如果超过次数，则以最近一次选择的内容作为下一次选择的情况)
	 */
	public int SelectUserNum = 0;

	/**
	 * 设置选取人员时需要选择的次数(=0表示不限制选择次数，如果超过次数，则以最近一次选择的内容作为下一次选择的情况)
	 * 
	 * @param value
	 */
	public void setSelectUserNum(int value) {
		if (value < 0) {
			this.SelectUserNum = 0;
		} else {
			this.SelectUserNum = value;
		}
	}

	/**
	 * 弹出选取下继步骤收件人的步骤名称
	 */
	public String NextActivityNames = "";

	/**
	 * 采用部门组或岗位组时限制处理的人数
	 */
	public int GroupLimitedNum = 1;

	/**
	 * 参与人选择范围限制类型(可以相加) =1 - 只选择用户 =2 - 只选择分组 =4 - 只选择系统角色 =8 - 只选择用户角色(公文中定义的)
	 */
	public int SelectRangeType = 15;

	/**
	 * 参与人选择限制范围标识，与“多级权限应用范围MUTI_RIGHT_FLAG”对应起来
	 */
	public int FlagId = 0;

	/**
	 * 解析收件人
	 * 
	 * @param as_DataValue
	 * @param ab_ReturnNameOnly
	 * @return
	 * @throws SQLException
	 */
	private String dataToParticipant(String as_DataValue,
			boolean ab_ReturnNameOnly) {
		try {
			if (as_DataValue.equals(""))
				return "";

			String[] ls_Array = as_DataValue.split(";");
			String ls_User = "";
			String ls_SysRole = "";
			String ls_UserRole = "";
			String ls_DeptID = "";
			String ls_DeptRole = "";
			String ls_Text, ls_Sign;

			for (int i = 0; i < ls_Array.length; i++) {
				ls_Text = ls_Array[i];
				if (ls_Text != null || ls_Text != "") {
					ls_Sign = ls_Array[i].substring(0, 1);
					// 用户和分组
					if (ls_Sign.equals("U") || ls_Sign.equals("G")) {
						ls_User += ls_Text.substring(1, ls_Text.length()) + ",";
					}
					// 系统角色[拟稿人的、当前收件人的]
					if (ls_Sign.equals("&") || ls_Sign.equals("#")) {
						ls_SysRole += ls_Text.substring(1, ls_Text.length())
								+ ",";
					}
					// 用户定义角色
					if (ls_Sign.equals("@")) {
						ls_UserRole += ls_Text.substring(1, ls_Text.length())
								+ ",";
					}
					// 某一个部门角色
					if (ls_Sign.equals("%")) {
						int j = ls_Text.indexOf("%", 1) + 1;
						ls_DeptID += ls_Text.substring(1, j - 2) + ", ";
						ls_DeptRole += ls_Text.substring(j, ls_Text.length())
								+ ",";
					}
				}
			}

			ls_Text = as_DataValue;
			String ls_Sql;
			List<Map<String, Object>> lo_Set;
			// 计算设置用户分组
			if (ls_User != "") {
				ls_Sql = "SELECT UserID, UserName, UserType FROM UserInfo "
						+ "WHERE UserID IN (" + ls_User + "-1)";
				lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				for (int i = 0; i < lo_Set.size(); i++) {
					if ((Integer) lo_Set.get(i).get("UserType") == 1) {
						ls_Text = ls_Text.replaceAll(
								"G"
										+ String.valueOf(lo_Set.get(i).get(
												"UserID")) + ";",
								"G"
										+ lo_Set.get(i).get("UserName")
										+ "["
										+ String.valueOf(lo_Set.get(i).get(
												"UserID")) + "];");
					} else {
						ls_Text = ls_Text.replaceAll(
								"U"
										+ String.valueOf(lo_Set.get(i).get(
												"UserID")) + ";",
								"U"
										+ lo_Set.get(i).get("UserName")
										+ "["
										+ String.valueOf(lo_Set.get(i).get(
												"UserID")) + "];");
					}
				}
				lo_Set = null;
			}

			// 计算设置系统角色
			if (ls_SysRole != "") {
				ls_Sql = "SELECT PositionID, PositionName FROM UserPosition WHERE PositionID IN ("
						+ ls_SysRole + "-1)";
				lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				for (int i = 0; i < lo_Set.size(); i++) {
					ls_Text = ls_Text.replaceAll(
							"&"
									+ String.valueOf(lo_Set.get(i).get(
											"PositionID")) + ";",
							"&"
									+ lo_Set.get(i).get("PositionName")
									+ "["
									+ String.valueOf(lo_Set.get(i).get(
											"PositionID")) + "];");
					ls_Text = ls_Text.replaceAll(
							"#"
									+ String.valueOf(lo_Set.get(i).get(
											"PositionID")) + ";",
							"#"
									+ lo_Set.get(i).get("PositionName")
									+ "["
									+ String.valueOf(lo_Set.get(i).get(
											"PositionID")) + "];");
				}
				lo_Set = null;
			}

			// 计算设置用户角色
			if (this.Activity.Flow.Cyclostyle == null) {
				Document lo_Xml = MXmlHandle.LoadXml("<Activity />");
				if (("<Root>" + this.Activity.Flow.RoleXml + "</Root>") != null) {
					NodeList lo_List = lo_Xml.getDocumentElement()
							.getChildNodes();
					for (int k = 0; k < lo_List.getLength(); k++) {
						Element lo_Node = (Element) lo_List.item(k);
						ls_Text = ls_Text.replaceAll(
								"@" + lo_Node.getAttribute("RoleID") + ";", "@"
										+ lo_Node.getAttribute("RoleName")
										+ "[" + lo_Node.getAttribute("RoleID")
										+ "];");
					}
				}
			} else {
				for (CRole lo_Role : this.Activity.Flow.Cyclostyle.Roles) {
					ls_Text = ls_Text.replaceAll(
							"@" + Integer.toString(lo_Role.RoleID) + ";",
							"@" + lo_Role.RoleName + "["
									+ Integer.toString(lo_Role.RoleID) + "];");
				}
			}

			// 计算设置部门角色
			if (ls_DeptRole != "") {
				ls_Sql = "SELECT PositionID, PositionName FROM UserPosition WHERE PositionID IN ("
						+ ls_DeptRole + "-1)";
				lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				ArrayList<String> lo_Ids = new ArrayList<String>();
				ArrayList<String> lo_Names = new ArrayList<String>();
				for (int i = 0; i < lo_Set.size(); i++) {
					lo_Ids.add(String.valueOf(lo_Set.get(i).get("PositionID")));
					lo_Names.add((String) lo_Set.get(i).get("PositionName"));
				}
				lo_Set = null;

				ls_Sql = "SELECT DeptID, DeptName FROM DeptInfo WHERE DeptID IN ("
						+ ls_DeptID + "-1)";
				lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				for (int i = 0; i < lo_Set.size(); i++) {
					for (int k = 0; k < lo_Ids.size(); k++) {
						ls_DeptID = "%"
								+ String.valueOf(lo_Set.get(i).get("DeptID"))
								+ "%" + lo_Ids.get(k) + ";";
						ls_DeptRole = "%部门[" + lo_Set.get(i).get("DeptName")
								+ "]的" + lo_Names.get(k) + "["
								+ String.valueOf(lo_Set.get(i).get("DeptID"))
								+ "%" + lo_Ids.get(k) + "];";
						ls_Text = ls_Text.replaceAll(ls_DeptID, ls_DeptRole);
					}
				}
				lo_Set = null;
			}

			// 解析
			ls_Array = ls_Text.split(";");
			ls_Text = "";
			for (int k = 0; k < ls_Array.length; k++) {
				if (ls_Array[k] != "") {
					if ((ls_Array[k]).indexOf("[") + 1 > 0
							&& (ls_Array[k]).indexOf("]") + 1 > 0) {
						if (ab_ReturnNameOnly) {
							ls_Text += ls_Array[k].substring(1,
									(ls_Array[k]).indexOf("[") - 1)
									+ ";";
						} else {
							ls_Text += ls_Array[k] + ";";
						}
					}
				}
			}
			return ls_Text;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}

	}

	/**
	 * 当前对象是否可用
	 * 
	 * @param ai_SpaceLength
	 * @return
	 * @throws Exception
	 */
	public String IsValid(int ai_SpaceLength) throws Exception {
		try {
			String ls_ErrMsg = "";
			if (this.Participant.equals("")
					&& this.CaculateFormula.equals("")
					& this.RangeType == EParticipantRangeType.NotExistSelectParticipant) {
				ls_ErrMsg += MGlobal.addSpace(ai_SpaceLength) + "步骤“"
						+ Activity.ActivityName + "”有错误：步骤必须具有收件人；\n";
			}
			if (this.RangeType == EParticipantRangeType.ExistInCaculate
					& this.ParticipantRange.equals("")) {
				ls_ErrMsg += MGlobal.addSpace(ai_SpaceLength) + "步骤“"
						+ Activity.ActivityName + "”有错误：收件人没有设置可选人员；\n";
			}
			return ls_ErrMsg;
		} catch (Exception e) {
			this.Raise(e, "IsValid", null);
			return e.toString();
		}
	}

	/**
	 * 转变收件人
	 * 
	 * @param as_Participant
	 * @return
	 */
	private String participantToData(String as_Participant) {
		try {
			if (as_Participant == null || as_Participant.equals(""))
				return "";

			String ls_Array[] = as_Participant.split(";");
			String ls_Value = "";
			for (int i = 0; i < ls_Array.length; i++) {
				if (ls_Array[i] != "") {
					int j = ls_Array[i].indexOf("[") + 2;
					ls_Value += ls_Array[i].substring(0, 1)
							+ ls_Array[i].substring(j, ls_Array[i].length())
							+ ";";
				}
			}
			return ls_Value;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 参与人员转化
	 */
	public String convertParticipantValue(String as_Value, int ai_Type) {
		String ls_Value = "";
		if (ai_Type == 0) // 转出
		{
			ls_Value = as_Value.replace("@", "P");
			ls_Value = ls_Value.replace("&", "S");
			ls_Value = ls_Value.replace("#", "C");
		} else {
			ls_Value = as_Value.replace("P", "@");
			ls_Value = ls_Value.replace("S", "&");
			ls_Value = ls_Value.replace("C", "#");
		}
		return ls_Value;
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
	protected void Import(Element ao_Node, int ai_Type) {
		if (ai_Type == 1 && ao_Node.hasAttribute("P")) {
			this.Participant = ao_Node.getAttribute("P");
			this.CaculateFormula = ao_Node.getAttribute("CF");
			this.RangeType = Integer.parseInt(ao_Node.getAttribute("RT"));
			this.ParticipantRange = ao_Node.getAttribute("PR");
			this.SelectParticipant = Boolean.parseBoolean(ao_Node
					.getAttribute("SP"));
			this.MaxLimitedNum = Integer.parseInt(ao_Node.getAttribute("MN"));
			this.SelectUserNum = Integer.parseInt(ao_Node.getAttribute("SU"));
			this.NextActivityNames = ao_Node.getAttribute("NN");
			this.GroupLimitedNum = Integer.parseInt(ao_Node.getAttribute("GL"));
		} else {
			this.Participant = ao_Node.getAttribute("Participant");
			this.CaculateFormula = ao_Node.getAttribute("CaculateFormula");
			this.RangeType = Integer
					.parseInt(ao_Node.getAttribute("RangeType"));
			this.ParticipantRange = ao_Node.getAttribute("ParticipantRange");
			this.SelectParticipant = Boolean.parseBoolean(ao_Node
					.getAttribute("SelectParticipant"));
			this.MaxLimitedNum = Integer.parseInt(ao_Node
					.getAttribute("MaxLimitedNum"));
			this.SelectUserNum = Integer.parseInt(ao_Node
					.getAttribute("SelectUserNum"));
			this.NextActivityNames = ao_Node.getAttribute("NextActivityNames");
			this.GroupLimitedNum = Integer.parseInt(ao_Node
					.getAttribute("GroupLimitedNum"));
		}
		if (ao_Node.hasAttribute("SelectRangeType"))
			this.SelectRangeType = Integer.parseInt(ao_Node
					.getAttribute("SelectRangeType"));
		if (this.SelectRangeType == 0)
			this.SelectRangeType = 15;
		if (ao_Node.hasAttribute("FlagId"))
			this.FlagId = Integer.parseInt(ao_Node.getAttribute("FlagId"));
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
	protected void Export(Element ao_Node, int ai_Type) {
		if (ai_Type == 1) {
			ao_Node.setAttribute("P", this.Participant);
			ao_Node.setAttribute("CF", this.CaculateFormula);
			ao_Node.setAttribute("RT", Integer.toString(this.RangeType));
			ao_Node.setAttribute("PR", this.ParticipantRange);
			ao_Node.setAttribute("SP", Boolean.toString(this.SelectParticipant));
			ao_Node.setAttribute("MN", Integer.toString(this.MaxLimitedNum));
			ao_Node.setAttribute("SU", Integer.toString(this.SelectUserNum));
			ao_Node.setAttribute("NN", this.NextActivityNames);
			ao_Node.setAttribute("GL", Integer.toString(this.GroupLimitedNum));
		} else {
			ao_Node.setAttribute("Participant", this.Participant);
			ao_Node.setAttribute("CaculateFormula", this.CaculateFormula);
			ao_Node.setAttribute("RangeType", Integer.toString(this.RangeType));
			ao_Node.setAttribute("ParticipantRange", this.ParticipantRange);
			ao_Node.setAttribute("SelectParticipant",
					Boolean.toString(this.SelectParticipant));
			ao_Node.setAttribute("MaxLimitedNum",
					Integer.toString(this.MaxLimitedNum));
			ao_Node.setAttribute("SelectUserNum",
					Integer.toString(this.SelectUserNum));
			ao_Node.setAttribute("NextActivityNames", this.NextActivityNames);
			ao_Node.setAttribute("GroupLimitedNum",
					Integer.toString(this.GroupLimitedNum));
		}
		ao_Node.setAttribute("SelectRangeType",
				Integer.toString(this.SelectRangeType));
		ao_Node.setAttribute("FlagId", Integer.toString(this.FlagId));
	}

	/**
	 * 对象保存到数据库行对象中
	 * 
	 * @param ao_Set
	 *            保存的结果集
	 * @param ai_Type
	 *            保存方式：=0-正常保存；=1-短属性保存；
	 */
	@Override
	protected void Save(Map<String, Object> ao_Set, int ai_Type) {
		// 不需要保存-无需实现
	}

	/**
	 * 从数据库行对象中读取数据到对象
	 * 
	 * @param ao_Set
	 *            打开的结果集
	 * @param ai_Type
	 *            打开方式：=0-正常打开；=1-短属性打开；
	 */
	@Override
	public void Open(Map<String, Object> ao_Set, int ai_Type) {
		// 不需要打开-无需实现
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
		if (ai_Type == 1) {
			ao_Bag.Write("P", this.Participant);
			ao_Bag.Write("CF", this.CaculateFormula);
			ao_Bag.Write("RT", this.RangeType);
			ao_Bag.Write("PR", this.ParticipantRange);
			ao_Bag.Write("SP", this.SelectParticipant);
			ao_Bag.Write("MN", this.MaxLimitedNum);
			ao_Bag.Write("SU", this.SelectUserNum);
			ao_Bag.Write("NN", this.NextActivityNames);
			ao_Bag.Write("GL", this.GroupLimitedNum);
		} else {
			ao_Bag.Write("ms_Participant", this.Participant);
			ao_Bag.Write("ms_CaculateFormula", this.CaculateFormula);
			ao_Bag.Write("mi_RangeType", this.RangeType);
			ao_Bag.Write("ms_ParticipantRange", this.ParticipantRange);
			ao_Bag.Write("mb_SelectParticipant", this.SelectParticipant);
			ao_Bag.Write("mi_MaxLimitedNum", this.MaxLimitedNum);
			ao_Bag.Write("mi_SelectUserNum", this.SelectUserNum);
			ao_Bag.Write("ms_NextActivityNames", this.NextActivityNames);
			ao_Bag.Write("mi_GroupLimitedNum", this.GroupLimitedNum);
		}

		ao_Bag.Write("mi_SelectRangeType", this.SelectRangeType);
		ao_Bag.Write("ml_FlagId", this.FlagId);
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
		if (ai_Type == 1) {
			this.Participant = ao_Bag.ReadString("P");
			this.CaculateFormula = ao_Bag.ReadString("CF");
			this.RangeType = ao_Bag.ReadInt("RT");
			this.ParticipantRange = ao_Bag.ReadString("PR");
			this.SelectParticipant = ao_Bag.ReadBoolean("SP");
			this.MaxLimitedNum = ao_Bag.ReadInt("MN");
			this.SelectUserNum = ao_Bag.ReadInt("SU");
			this.NextActivityNames = ao_Bag.ReadString("NN");
			this.GroupLimitedNum = ao_Bag.ReadInt("GL");
		} else {
			this.Participant = ao_Bag.ReadString("ms_Participant");
			this.CaculateFormula = ao_Bag.ReadString("ms_CaculateFormula");
			this.RangeType = ao_Bag.ReadInt("mi_RangeType");
			this.ParticipantRange = ao_Bag.ReadString("ms_ParticipantRange");
			this.SelectParticipant = ao_Bag.ReadBoolean("mb_SelectParticipant");
			this.MaxLimitedNum = ao_Bag.ReadInt("mi_MaxLimitedNum");
			this.SelectUserNum = ao_Bag.ReadInt("mi_SelectUserNum");
			this.NextActivityNames = ao_Bag.ReadString("ms_NextActivityNames");
			this.GroupLimitedNum = ao_Bag.ReadInt("mi_GroupLimitedNum");
		}

		this.SelectRangeType = ao_Bag.ReadInt("mi_SelectRangeType");
		if (this.SelectRangeType == 0) {
			this.SelectRangeType = 15;
		}
		this.FlagId = ao_Bag.ReadInt("ml_FlagId");
	}

}
