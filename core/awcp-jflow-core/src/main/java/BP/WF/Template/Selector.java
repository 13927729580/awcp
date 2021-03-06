package BP.WF.Template;

import cn.jflow.model.designer.Dot2DotModel;
import BP.DA.DBType;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Depositary;
import BP.En.Dot2DotModelEnum;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UAC;
import BP.Sys.OSModel;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Work;
import BP.WF.Port.DeptAttr;
import BP.Web.WebUser;
public class Selector extends Entity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	@Override
	public String getPK()
	{
		return "NodeID";
	}
	/** 
	 选择模式
	 
	*/
	public final SelectorModel getSelectorModel()
	{
		return SelectorModel.forValue(this.GetValIntByKey(SelectorAttr.SelectorModel));
	}
	public final void setSelectorModel(SelectorModel value)
	{
		this.SetValByKey(SelectorAttr.SelectorModel, value.getValue());
	}
	/** 
	 分组数据源
	 
	*/
	public final String getSelectorP1()
	{
		String s= this.GetValStringByKey(SelectorAttr.SelectorP1);
		s = s.replace("~", "'");
		return s;
	}
	public final void setSelectorP1(String value)
	{
		this.SetValByKey(SelectorAttr.SelectorP1, value);
	}
	/** 
	 实体数据源
	 
	*/
	public final String getSelectorP2()
	{
		String s = this.GetValStringByKey(SelectorAttr.SelectorP2);
		s = s.replace("~", "'");
		return s;
	}
	public final void setSelectorP2(String value)
	{
		this.SetValByKey(SelectorAttr.SelectorP2, value);
	}
	/** 是否自动装载上一笔加载的数据
	 
	*/
	public final boolean getIsAutoLoadEmps()
	{
		return this.GetValBooleanByKey(SelectorAttr.IsAutoLoadEmps);
	}
	public final void setIsAutoLoadEmps(boolean value)
	{
		this.SetValByKey(SelectorAttr.IsAutoLoadEmps, value);
	}
	/** 
	 默认选择数据源
	 
	*/
	public final String getSelectorP3()
	{
		String s = this.GetValStringByKey(SelectorAttr.SelectorP3);
		s = s.replace("~", "'");
		return s;
	}
	public final void setSelectorP3(String value)
	{
		this.SetValByKey(SelectorAttr.SelectorP3, value);
	}
	/** 
	 强制选择数据源
	 
	*/
	public final String getSelectorP4()
	{
		String s = this.GetValStringByKey(SelectorAttr.SelectorP4);
		s = s.replace("~", "'");
		return s;
	}
	public final void setSelectorP4(String value)
	{
		this.SetValByKey(SelectorAttr.SelectorP3, value);
	}
	/** 
	 节点ID
	 
	*/
	public final int getNodeID()
	{
		return this.GetValIntByKey(SelectorAttr.NodeID);
	}
	public final void setNodeID(int value)
	{
		this.SetValByKey(SelectorAttr.NodeID, value);
	}
	/** 
	 UI界面上的访问控制
	 
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsDelete = false;
		uac.IsInsert = false;
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			uac.IsUpdate = true;
			uac.IsView = true;
		}
		return uac;
	}
	
	/** 是否单选？
	 
	*/
	public final boolean getIsSimpleSelector()
	{
		return this.GetValBooleanByKey(SelectorAttr.IsSimpleSelector);
	}
	public final void setIsSimpleSelector(boolean value)
	{
		this.SetValByKey(SelectorAttr.IsSimpleSelector, value);
	}
	
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 接受人选择器
	 
	*/
	public Selector()
	{
	}
	/** 
	 接受人选择器
	 
	 @param nodeid
	*/
	public Selector(int nodeid)
	{
		this.setNodeID(nodeid);
		this.Retrieve();
	}

	/** 
	 重写基类方法
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Node", "选择器");

		map.Java_SetDepositaryOfEntity(Depositary.Application);

		map.AddTBIntPK(SelectorAttr.NodeID, 0, "NodeID", true, true);
		map.AddTBString(SelectorAttr.Name, null, "节点名称", true, true, 0, 100, 100);

		map.AddDDLSysEnum(SelectorAttr.SelectorModel, 5, "显示方式", true, true, SelectorAttr.SelectorModel, "@0=按岗位@1=按部门@2=按人员@3=按SQL@4=按SQL模版计算@5=使用通用人员选择器@6=部门与岗位的交集@7=自定义Url");

		map.AddDDLSQL(SelectorAttr.FK_SQLTemplate, null, "SQL模版", "SELECT No,Name FROM WF_SQLTemplate WHERE SQLType=5", true);

		map.AddBoolean(SelectorAttr.IsAutoLoadEmps, true, "是否自动加载上一次选择的人员？", true, true, true);
		map.AddBoolean(SelectorAttr.IsSimpleSelector, false, "是否单项选择(只能选择一个人)？", true, true);

		  //  map.AddDDLSysEnum(SelectorAttr.AccepterDBSort, 0, "选择的数据类别", true, true,
		  //SelectorAttr.AccepterDBSort, "@0=人员@1=部门@2=岗位@3=权限组");


		map.AddTBStringDoc(SelectorAttr.SelectorP1, null, "分组参数:可以为空,比如:SELECT No,Name,ParentNo FROM  Port_Dept", true, false, true);
		map.AddTBStringDoc(SelectorAttr.SelectorP2, null, "操作员数据源:比如:SELECT No,Name,FK_Dept FROM  Port_Emp", true, false, true);

		map.AddTBStringDoc(SelectorAttr.SelectorP3, null, "默认选择的数据源:比如:SELECT FK_Emp FROM  WF_GenerWorkerList WHERE FK_Node=102 AND WorkID=@WorkID", true, false, true);
		map.AddTBStringDoc(SelectorAttr.SelectorP4, null, "强制选择的数据源:比如:SELECT FK_Emp FROM  WF_GenerWorkerList WHERE FK_Node=102 AND WorkID=@WorkID", true, false, true);

			//map.AddTBStringDoc(SelectorAttr.SelectorP1, null, "分组参数,可以为空", true, false, true);
			//map.AddTBStringDoc(SelectorAttr.SelectorP2, null, "操作员数据源", true, false, true);

			// 相关功能。
		map.getAttrsOfOneVSM().Add(new BP.WF.Template.NodeStations(), new BP.WF.Port.Stations(), NodeStationAttr.FK_Node, NodeStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, "节点岗位");

		map.getAttrsOfOneVSM().Add(new BP.WF.Template.NodeDepts(), new BP.WF.Port.Depts(), NodeDeptAttr.FK_Node, NodeDeptAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, "节点部门");

		map.getAttrsOfOneVSM().Add(new BP.WF.Template.NodeEmps(), new BP.WF.Port.Emps(), NodeEmpAttr.FK_Node, NodeEmpAttr.FK_Emp, DeptAttr.Name, DeptAttr.No, "接受人员");


		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 产生数据.
	 
	 @return 
	*/
	public final DataSet GenerDataSet(int nodeid, Entity en)
	{
		DataSet ds = null;
		switch (this.getSelectorModel())
		{
			case Dept:
				ds = ByDept(nodeid);
				break;
			case Emp:
				ds = ByEmp(nodeid);
				break;
			case Station:
				ds = ByStation(nodeid);
				break;
			case DeptAndStation:
				ds = DeptAndStation(nodeid);
				break;
			case SQL:
				ds = BySQL(nodeid, en);
				break;
			case GenerUserSelecter:
				ds = ByEmp(nodeid);
				break;
			default:
				throw new RuntimeException("@错误:没有判断的选择类型:"+this.getSelectorModel());
		}

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			for (DataTable dt : ds.Tables)
			{
				for (int i = 0; i < dt.Columns.size(); i++)
				{
					if (dt.Columns.get(i).ColumnName.equals("NO"))
					{
						dt.Columns.get(i).ColumnName = "No";
					}

					if (dt.Columns.get(i).ColumnName.equals("NAME"))
					{
						dt.Columns.get(i).ColumnName = "Name";
					}

					if (dt.Columns.get(i).ColumnName.equals("PARENTNO"))
					{
						dt.Columns.get(i).ColumnName = "ParentNo";
					}

					if (dt.Columns.get(i).ColumnName.equals("FK_DEPT"))
					{
						dt.Columns.get(i).ColumnName = "FK_Dept";
					}
				}
			}
		}


		return ds;
	}
	/** 
	 按照SQL计算.
	 
	 @param nodeID 节点ID
	 @return 返回值
	*/
	private DataSet BySQL(int nodeID, Entity en)
	{
		// 定义数据容器.
		DataSet ds = new DataSet();

		//求部门.
		String sqlGroup = this.getSelectorP1();
		if (DotNetToJavaStringHelper.isNullOrEmpty(sqlGroup) == false)
		{
			sqlGroup = sqlGroup.replace("@WebUser.No", WebUser.getNo());
			sqlGroup = sqlGroup.replace("@WebUser.Name", WebUser.getName());
			sqlGroup = sqlGroup.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlGroup);
			dt.TableName = "Depts";
			ds.Tables.add(dt);
		}


		//求人员范围.
		String sqlDB = this.getSelectorP2();
		sqlDB = sqlDB.replace("@WebUser.No", WebUser.getNo());
		sqlDB = sqlDB.replace("@WebUser.Name", WebUser.getName());
		sqlDB = sqlDB.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		DataTable dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sqlDB);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);

		//求默认选择的数据.
		if (!this.getSelectorP3().equals(""))
		{
			sqlDB = this.getSelectorP3();

			sqlDB = sqlDB.replace("@WebUser.No", WebUser.getNo());
			sqlDB = sqlDB.replace("@WebUser.Name", WebUser.getName());
			sqlDB = sqlDB.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

			sqlDB = sqlDB.replace("@WorkID", en.GetValStringByKey("OID"));
			sqlDB = sqlDB.replace("@OID", en.GetValStringByKey("OID"));

			if (sqlDB.contains("@"))
			{
				sqlDB = BP.WF.Glo.DealExp(sqlDB, en, null);
			}

			DataTable dtDef = BP.DA.DBAccess.RunSQLReturnTable(sqlDB);
			dtDef.TableName = "DefaultSelected";

			ds.Tables.add(dtDef);
		}


		//求强制选择的数据源.
		if (!this.getSelectorP4().equals(""))
		{
			sqlDB = this.getSelectorP4();

			sqlDB = sqlDB.replace("@WebUser.No", WebUser.getNo());
			sqlDB = sqlDB.replace("@WebUser.Name", WebUser.getName());
			sqlDB = sqlDB.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

			sqlDB = sqlDB.replace("@WorkID", en.GetValStringByKey("OID"));
			sqlDB = sqlDB.replace("@OID", en.GetValStringByKey("OID"));

			if (sqlDB.contains("@"))
			{
				sqlDB = BP.WF.Glo.DealExp(sqlDB, en, null);
			}

			DataTable dtForce = BP.DA.DBAccess.RunSQLReturnTable(sqlDB);
			dtForce.TableName = "ForceSelected";
			ds.Tables.add(dtForce);
		}

		return ds;
	}

	/** 
	 按照部门获取部门人员树.
	 
	 @param nodeID 节点ID
	 @return 返回数据源dataset
	*/
	private DataSet ByDept(int nodeID)
	{
		// 定义数据容器.
		DataSet ds = new DataSet();

		//部门.
		String sql = "SELECT distinct a.No,a.Name, a.ParentNo FROM Port_Dept a,  WF_NodeDept b WHERE a.No=b.FK_Dept AND B.FK_Node=" + nodeID;
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);

		//人员.
		sql = "SELECT distinct a.No, a.Name, a.FK_Dept FROM Port_Emp a, WF_NodeDept b WHERE a.FK_Dept=b.FK_Dept AND B.FK_Node=" + nodeID;
		DataTable dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
		ds.Tables.add(dtEmp);
		dtEmp.TableName = "Emps";
		return ds;
	}
	/** 
	 按照Emp获取部门人员树.
	 
	 @param nodeID 节点ID
	 @return 返回数据源dataset
	*/
	private DataSet ByEmp(int nodeID)
	{
		// 定义数据容器.
		DataSet ds = new DataSet();

		//部门.
		String sql = "SELECT distinct a.No,a.Name, a.ParentNo FROM Port_Dept a, WF_NodeEmp b, Port_Emp c WHERE b.FK_Emp=c.No AND a.No=c.FK_Dept AND B.FK_Node=" + nodeID;
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);

		//人员.
		sql = "SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeEmp b WHERE a.No=b.FK_Emp AND b.FK_Node=" + nodeID;
		DataTable dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);
		return ds;
	}
	private DataSet DeptAndStation(int nodeID)
	{
		// 定义数据容器.
		DataSet ds = new DataSet();

		//部门.
		String sql = "";
		if (SystemConfig.getOSModel() == OSModel.OneOne)
		{
			sql = "SELECT distinct a.No, a.Name, a.ParentNo FROM Port_Dept a, WF_NodeStation b, Port_EmpStation c, Port_Emp d WHERE a.No=d.FK_Dept AND b.FK_Station=c.FK_Station AND C.FK_Emp=D.No AND B.FK_Node=" + nodeID;
		}
		else
		{
			sql = "SELECT distinct a.No, a.Name, a.ParentNo FROM Port_Dept a, WF_NodeStation b, Port_DeptEmpStation c, Port_Emp d WHERE a.No=d.FK_Dept AND b.FK_Station=c.FK_Station AND C.FK_Emp=D.No AND B.FK_Node=" + nodeID;
		}

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);

		//人员.
		if (SystemConfig.getOSModel() == OSModel.OneOne)
		{
			sql = "SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_EmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID;
		}
		else
		{
			sql = "SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID;
		}

		DataTable dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtEmp.TableName = "Emps";
		ds.Tables.add(dtEmp);
		return ds;
	}

	/** 
	 按照Station获取部门人员树.
	 
	 @param nodeID 节点ID
	 @return 返回数据源dataset
	*/
	private DataSet ByStation(int nodeID)
	{
		// 定义数据容器.
		DataSet ds = new DataSet();

		if (SystemConfig.getOSModel() == OSModel.OneOne)
		{
			//部门.
			String sql = "SELECT distinct a.No, a.Name, a.ParentNo FROM Port_Dept a, WF_NodeStation b, Port_EmpStation c, Port_Emp d WHERE a.No=d.FK_Dept AND b.FK_Station=c.FK_Station AND C.FK_Emp=D.No AND B.FK_Node=" + nodeID;
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Depts";
			ds.Tables.add(dt);


			//人员.
			sql = "SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_EmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID;
			DataTable dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dtEmp.TableName = "Emps";
			ds.Tables.add(dtEmp);
		}

		if (SystemConfig.getOSModel() == OSModel.OneMore)
		{
			//部门.
			String sql = "SELECT distinct a.No, a.Name, a.ParentNo FROM Port_Dept a, WF_NodeStation b, Port_DeptEmpStation c, Port_Emp d WHERE a.No=d.FK_Dept AND b.FK_Station=c.FK_Station AND C.FK_Emp=D.No AND B.FK_Node=" + nodeID;
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "Depts";
			ds.Tables.add(dt);

			//人员.
			sql = "SELECT distinct a.No,a.Name, a.FK_Dept FROM Port_Emp a,  WF_NodeStation b, Port_DeptEmpStation c WHERE a.No=c.FK_Emp AND B.FK_Station=C.FK_Station AND b.FK_Node=" + nodeID;
			DataTable dtEmp = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dtEmp.TableName = "Emps";
			ds.Tables.add(dtEmp);
		}


		return ds;
	}
}
