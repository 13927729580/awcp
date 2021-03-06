package BP.WF.HttpHandler;

import java.io.UnsupportedEncodingException;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.*;

/** 
 页面功能实体
 
*/
public class CCMobile extends WebContralBase
{

		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
//C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a string member and was converted to Java 'if-else' logic:
//		switch (this.getDoType())

//ORIGINAL LINE: case "DtlFieldUp":
		if (this.getDoType().equals("DtlFieldUp")) //字段上移
		{
				return "执行成功.";
		}
		else
		{
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记["+this.getDoType()+"]，没有找到.");
	}

		///#endregion 执行父类的重写方法.

	public final String Login_Init()
	{
		AppACE ace = new AppACE();
		return ace.Login_Init();
	}

	public final String Login_Submit() throws UnsupportedEncodingException
	{
		AppACE ace = new AppACE();
		return ace.Login_Submit();
	}
	/** 
	 会签列表
	 
	 @return 
	*/
	public final String HuiQianList_Init()
	{
		WF wf = new WF();
		return wf.HuiQianList_Init();
	}

	public final String GetUserInfo()
	{
		AppACE ace = new AppACE();
		return ace.GetUserInfo();
	}

	public final String Home_Init() throws Exception
	{

		java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("UserNo", BP.Web.WebUser.getNo());
		ht.put("UserName", BP.Web.WebUser.getName());

		//系统名称.
		ht.put("SysName", BP.Sys.SystemConfig.getSysName());
		ht.put("CustomerName", BP.Sys.SystemConfig.getCustomerName());

		ht.put("Todolist_EmpWorks", BP.WF.Dev2Interface.getTodolist_EmpWorks());
		ht.put("Todolist_Runing", BP.WF.Dev2Interface.getTodolist_Runing());
		ht.put("Todolist_Sharing", BP.WF.Dev2Interface.getTodolist_Sharing());
		ht.put("Todolist_CCWorks", BP.WF.Dev2Interface.getTodolist_CCWorks());
		ht.put("Todolist_Apply", BP.WF.Dev2Interface.getTodolist_Apply()); //申请下来的任务个数.
		ht.put("Todolist_Draft", BP.WF.Dev2Interface.getTodolist_Draft()); //草稿数量.

		ht.put("Todolist_HuiQian", BP.WF.Dev2Interface.getTodolist_HuiQian()); //会签数量.

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}
	/** 
	 查询
	 
	 @return 
	*/
	public final String Home_Init_WorkCount()
	{
		String sql = "SELECT  TSpan as No, '' as Name, COUNT(WorkID) as Num, FROM WF_GenerWorkFlow WHERE Emps LIKE '%" + WebUser.getNo() + "%' GROUP BY TSpan";
		DataSet ds = new DataSet();
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		ds.Tables.add(dt);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get(0).ColumnName = "TSpan";
			dt.Columns.get(1).ColumnName = "Num";
		}

		sql = "SELECT IntKey as No, Lab as Name FROM Sys_Enum WHERE EnumKey='TSpan'";
		DataTable dt1 = BP.DA.DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			for (DataRow mydr : dt1.Rows)
			{

			}
		}

		return BP.Tools.Json.ToJson(dt);
	}

	public final String Runing_Init()
	{
		BP.WF.HttpHandler.WF wfPage = new WF();
	  return wfPage.Runing_Init();
	}
	/** 
	 旧版本
	 
	 @return 
	*/
	public final String Todolist_Init111()
	{
		BP.WF.HttpHandler.WF wfPage = new WF();
		return wfPage.Todolist_Init();
	}
	/** 
	 新版本.
	 
	 @return 
	*/
	public final String Todolist_Init()
	{
		String fk_node = this.GetRequestVal("FK_Node");
		DataTable dt = BP.WF.Dev2Interface.DB_Todolist(WebUser.getNo(), this.getFK_Node());
		return BP.Tools.Json.DataTableToJson(dt, false);
	}
	public final String DB_GenerReturnWorks()
	{
		AppACE ace = new AppACE();
		return ace.DB_GenerReturnWorks();
	}

	public final String Start_Init()
	{
		BP.WF.HttpHandler.WF wfPage = new WF();
		return wfPage.Start_Init();
	}

	public final String HandlerMapExt() throws UnsupportedEncodingException
	{
		WF_CCForm en = new WF_CCForm();
		return en.HandlerMapExt();
	}

	/** 
	 打开手机端
	 
	 @return 
	*/
	public final String Do_OpenFlow()
	{
		String sid = this.GetRequestVal("SID");
		String[] strs = sid.split("[_]", -1);
		GenerWorkerList wl = new GenerWorkerList();
		int i = wl.Retrieve(GenerWorkerListAttr.FK_Emp, strs[0], GenerWorkerListAttr.WorkID, strs[1], GenerWorkerListAttr.IsPass, 0);

		if (i == 0)
		{
			return "err@提示:此工作已经被别人处理或者此流程已删除。";
		}

		BP.Port.Emp empOF = new BP.Port.Emp(wl.getFK_Emp());
		WebUser.SignInOfGener(empOF);
		return "MyFlow.htm?FK_Flow=" + wl.getFK_Flow() + "&WorkID=" + wl.getWorkID() + "&FK_Node=" + wl.getFK_Node() + "&FID=" + wl.getFID();
	}
	/** 
	 流程单表单查看.
	 
	 @return json
	*/
	public final String FrmView_Init()
	{
		BP.WF.HttpHandler.WF wf = new WF();
		return wf.FrmView_Init();
	}

	public final String StartGuide_MulitSend()
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.StartGuide_MulitSend();
	}
	
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 关键字查询.
			/** 
			 打开表单
			 
			 @return 
			*/
			public final String SearchKey_OpenFrm()
			{
				BP.WF.HttpHandler.WF_RptSearch search = new WF_RptSearch();
				return search.KeySearch_OpenFrm();
			}
			/** 
			 执行查询
			 
			 @return 
			*/
			public final String SearchKey_Query()
			{
				BP.WF.HttpHandler.WF_RptSearch search = new WF_RptSearch();
				return search.KeySearch_Query();
			}
			///#endregion 关键字查询.

			///#region 查询.
			/** 
			 初始化
			 
			 @return 
			*/
			public final String Search_Init()
			{
				DataSet ds = new DataSet();
				String sql = "";

				String tSpan = this.GetRequestVal("TSpan");
				if (tSpan.equals(""))
				{
					tSpan = null;
				}

				///#region 1、获取时间段枚举/总数.
				SysEnums ses = new SysEnums("TSpan");
				DataTable dtTSpan = ses.ToDataTableField();
				dtTSpan.TableName = "TSpan";
				ds.Tables.add(dtTSpan);

				if (this.getFK_Flow() == null)
				{
					sql = "SELECT  TSpan as No, COUNT(WorkID) as Num FROM WF_GenerWorkFlow WHERE Emps LIKE '%" + WebUser.getNo() + "%' GROUP BY TSpan";
				}
				else
				{
					sql = "SELECT  TSpan as No, COUNT(WorkID) as Num FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.getFK_Flow() + "' AND Emps LIKE '%" + WebUser.getNo() + "%' GROUP BY TSpan";
				}

				DataTable dtTSpanNum = BP.DA.DBAccess.RunSQLReturnTable(sql);
				for (DataRow drEnum : dtTSpan.Rows)
				{
					String no = drEnum.getValue_2017("IntKey").toString();
					for (DataRow dr : dtTSpanNum.Rows)
					{
						if (dr.getValue_2017("No").toString().equals(no))
						{
							drEnum.setValue2017("Lab", drEnum.getValue_2017("Lab").toString() + "(" + dr.getValue_2017("Num") + ")");
							break;
						}
					}
				}
				///#endregion

				///#region 2、处理流程类别列表.

				if (tSpan == null)
				{
					sql = "SELECT  FK_Flow as No, FlowName as Name, COUNT(WorkID) as Num FROM WF_GenerWorkFlow WHERE  Emps LIKE '%" + WebUser.getNo() + "%' GROUP BY FK_Flow, FlowName";
				}
				else
				{
					sql = "SELECT  FK_Flow as No, FlowName as Name, COUNT(WorkID) as Num FROM WF_GenerWorkFlow WHERE TSpan=" + tSpan + " AND Emps LIKE '%" + WebUser.getNo() + "%' GROUP BY FK_Flow, FlowName";
				}


				DataTable dtFlows = BP.DA.DBAccess.RunSQLReturnTable(sql);
				if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
				{
					dtFlows.Columns.get(0).ColumnName = "No";
					dtFlows.Columns.get(1).ColumnName = "Name";
					dtFlows.Columns.get(2).ColumnName = "Num";
				}
				dtFlows.TableName = "Flows";
				ds.Tables.add(dtFlows);
				///#endregion

				///#region 3、处理流程实例列表.

				GenerWorkFlows gwfs = new GenerWorkFlows();
				BP.En.QueryObject qo = new QueryObject(gwfs);
				qo.AddWhere(GenerWorkFlowAttr.Emps, " LIKE ", "%" + BP.Web.WebUser.getNo() + "%");

				if (tSpan != null)
				{
					qo.addAnd();
					qo.AddWhere(GenerWorkFlowAttr.TSpan, tSpan);
				}

				if (this.getFK_Flow() != null)
				{
					qo.addAnd();
					qo.AddWhere(GenerWorkFlowAttr.FK_Flow, this.getFK_Flow());
				}
				qo.addOrderBy("WFSta");
				qo.addOrderByDesc("RDT");
				qo.setTop(50);


				DataTable mydt = null;
				if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
				{
					qo.DoQuery();
					mydt = gwfs.ToDataTableField("WF_GenerWorkFlow");
				}
				else
				{
					mydt = qo.DoQueryToTable();
					mydt.TableName = "WF_GenerWorkFlow";
				}
				///#endregion

				ds.Tables.add(mydt);

				return BP.Tools.Json.ToJson(ds);
			}
			/** 
			 查询
			 
			 @return 
			*/
			public final String Search_Search()
			{
				String TSpan = this.GetRequestVal("TSpan");
				String FK_Flow = this.GetRequestVal("FK_Flow");

				GenerWorkFlows gwfs = new GenerWorkFlows();
				QueryObject qo = new QueryObject(gwfs);
				qo.AddWhere(GenerWorkFlowAttr.Emps, " LIKE ", "%" + BP.Web.WebUser.getNo() + "%");
				if (!DotNetToJavaStringHelper.isNullOrEmpty(TSpan))
				{
					qo.addAnd();
					qo.AddWhere(GenerWorkFlowAttr.TSpan, this.GetRequestVal("TSpan"));
				}
				if (!DotNetToJavaStringHelper.isNullOrEmpty(FK_Flow))
				{
					qo.addAnd();
					qo.AddWhere(GenerWorkFlowAttr.FK_Flow, this.GetRequestVal("FK_Flow"));
				}
				qo.setTop(50);

				if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
				{
					qo.DoQuery();
					DataTable dt = gwfs.ToDataTableField("Ens");
					return BP.Tools.Json.ToJson(dt);
				}
				else
				{
					DataTable dt = qo.DoQueryToTable();
					return BP.Tools.Json.ToJson(dt);
				}
			}
			
			/// 撤销发送
	        /// </summary>
	        /// <returns></returns>
	        public String FrmView_UnSend()
	        {
	            BP.WF.HttpHandler.WF_WorkOpt_OneWork en = new WF_WorkOpt_OneWork(this.context);
	            return en.OP_UnSend();
	        }
}