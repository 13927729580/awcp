package BP.WF.Template;

import BP.DA.DataType;
import BP.DA.Log;
import BP.En.Dot2DotModelEnum;
import BP.En.Entity;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Port.DeptAttr;
import BP.Port.Depts;
import BP.Port.EmpAttr;
import BP.Sys.AttachmentUploadType;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentAttr;
import BP.Sys.OSModel;
import BP.Sys.PubClass;
import BP.Sys.ToolbarExcel;
import BP.Tools.StringHelper;
import BP.WF.BatchRole;
import BP.WF.BlockModel;
import BP.WF.CancelRole;
import BP.WF.DeliveryWay;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.ReturnRole;
import BP.WF.RunModel;
import BP.WF.SubFlowStartWay;
import BP.WF.ThreadKillRole;
import BP.WF.TodolistModel;
import cn.jflow.common.util.ContextHolderUtils;

/** 
 节点属性.
 
*/
public class NodeSheet extends Entity
{

		///#region 索引
	/** 
	 获取节点的帮助信息url
	 <p></p>
	 <p>added by liuxc,2014-8-19</p> 
	 
	 @param sysNo 帮助网站中所属系统No
	 @param searchTitle 帮助主题标题
	 @return 
	*/
	/*
	private String getItem(String sysNo, String searchTitle)
	{
		if (StringHelper.isNullOrWhiteSpace(sysNo) || StringHelper.isNullOrWhiteSpace(searchTitle))
		{
			return "javascript:alert('此处还没有帮助信息！')";
		}

		return String.format("http://online.ccflow.org/KM/Tree.jsp?no=%1$s&st=%2$s", sysNo, Uri.EscapeDataString(searchTitle));
	}
	*/

		///#endregion


		///#region 常量
	/** 
	 CCFlow流程引擎
	 
	*/
	private static final String SYS_CCFLOW = "001";
	/** 
	 CCForm表单引擎
	 
	*/
	private static final String SYS_CCFORM = "002";

		///#endregion
	/** 
	 超时处理方式
	 
	*/
	public final OutTimeDeal getHisOutTimeDeal()
	{
		return OutTimeDeal.forValue(this.GetValIntByKey(NodeAttr.OutTimeDeal));
	}
	public final void setHisOutTimeDeal(OutTimeDeal value)
	{
		this.SetValByKey(NodeAttr.OutTimeDeal, value.getValue());
	}
	/** 
	 访问规则
	 
	*/
	public final ReturnRole getHisReturnRole()
	{
		return ReturnRole.forValue(this.GetValIntByKey(NodeAttr.ReturnRole));
	}
	public final void setHisReturnRole(ReturnRole value)
	{
		this.SetValByKey(NodeAttr.ReturnRole, value.getValue());
	}
	/** 
	 访问规则
	 
	*/
	public final DeliveryWay getHisDeliveryWay()
	{
		return DeliveryWay.forValue(this.GetValIntByKey(NodeAttr.DeliveryWay));
	}
	public final void setHisDeliveryWay(DeliveryWay value)
	{
		this.SetValByKey(NodeAttr.DeliveryWay, value.getValue());
	}
	/** 
	 步骤
	 
	*/
	public final int getStep()
	{
		return this.GetValIntByKey(NodeAttr.Step);
	}
	public final void setStep(int value)
	{
		this.SetValByKey(NodeAttr.Step, value);
	}
	/** 
	 节点ID
	 
	*/
	public final int getNodeID()
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value)
	{
		this.SetValByKey(NodeAttr.NodeID, value);
	}
	/** 
	 超时处理内容
	 
	*/
	public final String getDoOutTime()
	{
		return this.GetValStringByKey(NodeAttr.DoOutTime);
	}
	public final void setDoOutTime(String value)
	{
		this.SetValByKey(NodeAttr.DoOutTime, value);
	}
	/** 
	 超时处理条件
	 
	*/
	public final String getDoOutTimeCond()
	{
		return this.GetValStringByKey(NodeAttr.DoOutTimeCond);
	}
	public final void setDoOutTimeCond(String value)
	{
		this.SetValByKey(NodeAttr.DoOutTimeCond, value);
	}
	/** 
	 节点名称
	 
	*/
	public final String getName()
	{
		return this.GetValStringByKey(NodeAttr.Name);
	}
	public final void setName(String value)
	{
		this.SetValByKey(NodeAttr.Name, value);
	}
	/** 
	 流程编号
	 
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(NodeAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(NodeAttr.FK_Flow, value);
	}
	/** 
	 流程名称
	 
	*/
	public final String getFlowName()
	{
		return this.GetValStringByKey(NodeAttr.FlowName);
	}
	public final void setFlowName(String value)
	{
		this.SetValByKey(NodeAttr.FlowName, value);
	}
	/** 
	 接受人sql
	 
	*/
	public final String getDeliveryParas()
	{
		return this.GetValStringByKey(NodeAttr.DeliveryParas);
	}
	public final void setDeliveryParas(String value)
	{
		this.SetValByKey(NodeAttr.DeliveryParas, value);
	}
	/** 
	 是否可以退回
	 
	*/
	public final boolean getReturnEnable()
	{
		return this.GetValBooleanByKey(BtnAttr.ReturnRole);
	}
	/** 
	 主键
	 
	*/
	@Override
	public String getPK()
	{
		return "NodeID";
	}

		///#endregion 属性.


		///#region 初试化全局的 Node
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		Flow fl = new Flow(this.getFK_Flow());
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			uac.IsUpdate = true;
		}
		return uac;
	}

		///#endregion


		
	/** 
	 节点
	 
	*/
	public NodeSheet()
	{
	}
	public NodeSheet(int nodeid)
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

			//map 的基 础信息.
		Map map = new Map("WF_Node", "节点");


			///#region  基础属性
		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.SetHelperUrl(NodeAttr.NodeID, "http://ccbpm.mydoc.io/?v=5404&t=17901");
		map.AddTBInt(NodeAttr.Step, 0, "步骤(无计算意义)", true, false);
		map.SetHelperUrl(NodeAttr.Step, "http://ccbpm.mydoc.io/?v=5404&t=17902");
			//map.SetHelperAlert(NodeAttr.Step, "它用于节点的排序，正确的设置步骤可以让流程容易读写."); //使用alert的方式显示帮助信息.
		map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", false, false, 3, 3, 10, false, "http://ccbpm.mydoc.io/?v=5404&t=17023");

		map.AddTBString(NodeAttr.Name, null, "名称", true, true, 0, 100, 10, false, "http://ccbpm.mydoc.io/?v=5404&t=17903");

		map.AddTBString(NodeAttr.Tip, null, "操作提示", true, false, 0, 100, 10, false, "http://ccbpm.mydoc.io/?v=5404&t=18084");

		String str = "";
		   // str += "@0=01.按岗位智能计算";
		str += "@0=01.按岗位智能计算";
		str += "@1=02.按节点绑定的部门计算";
		str += "@2=03.按设置的SQL获取接受人计算";
		str += "@3=04.按节点绑定的人员计算";
		str += "@4=05.由上一节点发送人通过“人员选择器”选择接受人";
		str += "@5=06.按上一节点表单指定的字段值作为本步骤的接受人";
		str += "@6=07.与上一节点处理人员相同";
		str += "@7=08.与开始节点处理人相同";
		str += "@8=09.与指定节点处理人相同";
		str += "@9=10.按绑定的岗位与部门交集计算";
		str += "@10=11.按绑定的岗位计算并且以绑定的部门集合为纬度";
		str += "@11=12.按指定节点的工作人员或者指定字段人员的岗位计算";
		str += "@12=13.按SQL确定子线程接受人与数据源";
		str += "@13=14.由上一节点的明细表来决定子线程的接受人";
		str += "@14=15.仅按绑定的岗位计算";
		str += "@15=16.由FEE来决定";
		str += "@16=17.按绑定部门计算,该部门一人处理标识该工作结束(子线程).";
		str += "@100=18.按ccflow的BPM模式处理";
		map.AddDDLSysEnum(NodeAttr.DeliveryWay, 0, "节点访问规则", true, true, NodeAttr.DeliveryWay,str);
		map.SetHelperUrl(NodeAttr.DeliveryWay, "http://ccbpm.mydoc.io/?v=5404&t=17907");
		map.AddBoolean(NodeAttr.IsExpSender, true, "本节点接收人不允许包含上一步发送人?", true, true, true, "http://ccbpm.mydoc.io/?v=5404&t=17904");

		map.AddTBString(NodeAttr.DeliveryParas, null, "访问规则设置内容", true, false, 0, 500, 10,true, "http://ccbpm.mydoc.io/?v=5404&t=17907");

		map.AddDDLSysEnum(NodeAttr.WhoExeIt, 0, "谁执行它",true, true, NodeAttr.WhoExeIt, "@0=操作员执行@1=机器执行@2=混合执行");
		map.SetHelperUrl(NodeAttr.WhoExeIt, "http://ccbpm.mydoc.io/?v=5404&t=17913");

		map.AddDDLSysEnum(NodeAttr.TurnToDeal, 0, "发送后转向", true, true, NodeAttr.TurnToDeal, "@0=提示ccflow默认信息@1=提示指定信息@2=转向指定的url@3=按照条件转向");
		map.SetHelperUrl(NodeAttr.TurnToDeal, "http://ccbpm.mydoc.io/?v=5404&t=17914");
		map.AddTBString(NodeAttr.TurnToDealDoc, null, "转向处理内容", true, false, 0, 1000, 10, true, "http://ccbpm.mydoc.io/?v=5404&t=17914");
		map.AddDDLSysEnum(NodeAttr.ReadReceipts, 0, "已读回执", true, true, NodeAttr.ReadReceipts, "@0=不回执@1=自动回执@2=由上一节点表单字段决定@3=由SDK开发者参数决定");
		map.SetHelperUrl(NodeAttr.ReadReceipts, "http://ccbpm.mydoc.io/?v=5404&t=17915");


		map.AddDDLSysEnum(NodeAttr.CondModel, 0, "方向条件控制规则", true, true, NodeAttr.CondModel, "@0=由连接线条件控制@1=让用户手工选择@2=发送按钮旁下拉框选择");
		map.SetHelperUrl(NodeAttr.CondModel, "http://ccbpm.mydoc.io/?v=5404&t=17917"); //增加帮助

			// 撤销规则.
		map.AddDDLSysEnum(NodeAttr.CancelRole,CancelRole.OnlyNextStep.getValue(), "撤销规则", true, true, NodeAttr.CancelRole,"@0=上一步可以撤销@1=不能撤销@2=上一步与开始节点可以撤销@3=指定的节点可以撤销");
		map.SetHelperUrl(NodeAttr.CancelRole, "http://ccbpm.mydoc.io/?v=5404&t=17919");

			// 节点工作批处理. edit by peng, 2014-01-24. 
		map.AddDDLSysEnum(NodeAttr.BatchRole, BatchRole.None.getValue(), "工作批处理", true, true, NodeAttr.BatchRole, "@0=不可以批处理@1=批量审核@2=分组批量审核");
		map.AddTBInt(NodeAttr.BatchListCount, 12, "批处理数量", true, false);
			//map.SetHelperUrl(NodeAttr.BatchRole, this[SYS_CCFLOW, "节点工作批处理"]); //增加帮助
		map.SetHelperUrl(NodeAttr.BatchRole, "http://ccbpm.mydoc.io/?v=5404&t=17920");
		map.SetHelperUrl(NodeAttr.BatchListCount, "http://ccbpm.mydoc.io/?v=5404&t=17920");

		map.AddTBString(NodeAttr.BatchParas, null, "批处理参数", true, false, 0, 300, 10, true);
		map.SetHelperUrl(NodeAttr.BatchParas, "http://ccbpm.mydoc.io/?v=5404&t=17920");

		map.AddBoolean(NodeAttr.IsTask, true, "允许分配工作否?", true, true, false, "http://ccbpm.mydoc.io/?v=5404&t=17904");
		map.AddBoolean(NodeAttr.IsRM, true, "是否启用投递路径自动记忆功能?", true, true, false, "http://ccbpm.mydoc.io/?v=5404&t=17905");

		map.AddTBDateTime("DTFrom", "生命周期从", true, true);
		map.AddTBDateTime("DTTo", "生命周期到", true, true);

		map.AddBoolean(NodeAttr.IsBUnit, false, "是否是节点模版（业务单元）?", true, true, true, "http://ccbpm.mydoc.io/?v=5404&t=17904");

			///#endregion  基础属性


			///#region 表单.
		map.AddDDLSysEnum(NodeAttr.FormType, NodeFormType.FreeForm.getValue(), "节点表单方案", true, true, "NodeFormType", "@0=傻瓜表单(ccflow6取消支持)@1=自由表单@2=嵌入式表单@3=SDK表单@4=SL表单(ccflow6取消支持)@5=表单树@6=动态表单树@7=公文表单(WebOffice)@8=Excel表单(测试中)@9=Word表单(测试中)@100=禁用(对多表单流程有效)");
		map.SetHelperUrl(NodeAttr.FormType, "http://ccbpm.mydoc.io/?v=5404&t=17921");

		map.AddTBString(NodeAttr.NodeFrmID, null, "节点表单ID", true, false, 0, 50, 10);
		map.SetHelperUrl(NodeAttr.NodeFrmID, "http://ccbpm.mydoc.io/?v=5404&t=17933");
		map.AddTBString(NodeAttr.FormUrl, null, "表单URL", true, false, 0, 200, 10, true, "http://ccbpm.mydoc.io/?v=5404&t=17925");

		map.AddTBString(NodeAttr.FocusField, null, "焦点字段", true, false, 0, 50, 10, true, "http://ccbpm.mydoc.io/?v=5404&t=17932");
		map.AddDDLSysEnum(NodeAttr.SaveModel, 0, "保存方式", true, true);
		map.SetHelperUrl(NodeAttr.SaveModel, "http://ccbpm.mydoc.io/?v=5404&t=17934");

			///#endregion 表单.


			///#region 分合流子线程属性
		map.AddDDLSysEnum(NodeAttr.RunModel, 0, "节点类型", true, true, NodeAttr.RunModel, "@0=普通@1=合流@2=分流@3=分合流@4=子线程");

		map.SetHelperUrl(NodeAttr.RunModel, "http://ccbpm.mydoc.io/?v=5404&t=17940"); //增加帮助.

			//子线程类型.
		map.AddDDLSysEnum(NodeAttr.SubThreadType, 0, "子线程类型", true, true, NodeAttr.SubThreadType, "@0=同表单@1=异表单");
		map.SetHelperUrl(NodeAttr.SubThreadType, "http://ccbpm.mydoc.io/?v=5404&t=17944"); //增加帮助


		map.AddTBDecimal(NodeAttr.PassRate, 100, "完成通过率", true, false);
		map.SetHelperUrl(NodeAttr.PassRate, "http://ccbpm.mydoc.io/?v=5404&t=17945"); //增加帮助.

			// 启动子线程参数 2013-01-04
		map.AddDDLSysEnum(NodeAttr.SubFlowStartWay, SubFlowStartWay.None.getValue(), "子线程启动方式", true, true, NodeAttr.SubFlowStartWay, "@0=不启动@1=指定的字段启动@2=按明细表启动");
		map.AddTBString(NodeAttr.SubFlowStartParas, null, "启动参数", true, false, 0, 100, 10, true);
		map.SetHelperUrl(NodeAttr.SubFlowStartWay, "http://ccbpm.mydoc.io/?v=5404&t=17946"); //增加帮助

			//待办处理模式.
		map.AddDDLSysEnum(NodeAttr.TodolistModel, TodolistModel.QiangBan.getValue(), "待办处理模式", true, true, NodeAttr.TodolistModel, "@0=抢办模式@1=协作模式@2=队列模式@3=共享模式@4=协作组长模式");
		map.SetHelperUrl(NodeAttr.TodolistModel, "http://ccbpm.mydoc.io/?v=5404&t=17947"); //增加帮助.


			//发送阻塞模式.
		map.AddDDLSysEnum(NodeAttr.BlockModel, BlockModel.None.getValue(), "发送阻塞模式", true, true, NodeAttr.BlockModel, "@0=不阻塞@1=当前节点有未完成的子流程时@2=按约定格式阻塞未完成子流程@3=按照SQL阻塞@4=按照表达式阻塞");
		map.SetHelperUrl(NodeAttr.BlockModel, "http://ccbpm.mydoc.io/?v=5404&t=17948"); //增加帮助.

		map.AddTBString(NodeAttr.BlockExp, null, "阻塞表达式", true, false, 0, 700, 10,true);
		map.SetHelperUrl(NodeAttr.BlockExp, "http://ccbpm.mydoc.io/?v=5404&t=17948");

		map.AddTBString(NodeAttr.BlockAlert, null, "被阻塞时提示信息", true, false, 0, 700, 10, true);
		map.SetHelperUrl(NodeAttr.BlockAlert, "http://ccbpm.mydoc.io/?v=5404&t=17948");

			//map.AddBoolean(NodeAttr.IsCheckSubFlowOver, false, "(当前节点启动子流程时)是否检查所有子流程结束后,该节点才能向下发送?",
		   //true, true, true);

			////  add 2013-09-14 
			//map.AddBoolean(NodeAttr.IsEnableTaskPool, true,
			//    "是否启用共享任务池(与web.config中的IsEnableTaskPool配置启用才有效,与子线程无关)？", true, true, true);
			//map.SetHelperBaidu(NodeAttr.IsEnableTaskPool); //增加帮助.


		map.AddBoolean(NodeAttr.IsAllowRepeatEmps, false, "是否允许子线程接受人员重复(仅当分流点向子线程发送时有效)?", true, true, true);
		map.AddBoolean(NodeAttr.IsGuestNode, false, "是否是外部用户执行的节点(非组织结构人员参与处理工作的节点)?", true, true, true);

			///#endregion 分合流子线程属性


			///#region 自动跳转规则
		map.AddBoolean(NodeAttr.AutoJumpRole0, false, "处理人就是发起人", true, true, false);
		map.SetHelperUrl(NodeAttr.AutoJumpRole0, "http://ccbpm.mydoc.io/?v=5404&t=17949"); //增加帮助

		map.AddBoolean(NodeAttr.AutoJumpRole1, false, "处理人已经出现过", true, true, false);
		map.AddBoolean(NodeAttr.AutoJumpRole2, false, "处理人与上一步相同", true, true, false);
		map.AddBoolean(NodeAttr.WhenNoWorker, false, "(是)找不到人就跳转,(否)提示错误.", true, true, false);

   //         map.AddDDLSysEnum(NodeAttr.WhenNoWorker, 0, "找不到处理人处理规则",
   //true, true, NodeAttr.WhenNoWorker, "@0=提示错误@1=自动转到下一步");

			///#endregion


			///#region  功能按钮状态
		map.AddTBString(BtnAttr.SendLab, "发送", "发送按钮标签", true, false, 0, 50, 10);
		map.SetHelperUrl(BtnAttr.SendLab, "http://ccbpm.mydoc.io/?v=5404&t=16219");
		map.AddTBString(BtnAttr.SendJS, "", "按钮JS函数", true, false, 0, 999, 10);
			//map.SetHelperBaidu(BtnAttr.SendJS, "ccflow 发送前数据完整性判断"); //增加帮助.
		map.SetHelperUrl(BtnAttr.SendJS, "http://ccbpm.mydoc.io/?v=5404&t=17967");

		map.AddTBString(BtnAttr.SaveLab, "保存", "保存按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.SaveEnable, true, "是否启用", true, true);
		map.SetHelperUrl(BtnAttr.SaveLab, "http://ccbpm.mydoc.io/?v=5404&t=24366"); //增加帮助

		map.AddTBString(BtnAttr.ThreadLab, "子线程", "子线程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ThreadEnable, false, "是否启用", true, true);
		map.SetHelperUrl(BtnAttr.ThreadLab, "http://ccbpm.mydoc.io/?v=5404&t=16263"); //增加帮助

		map.AddDDLSysEnum(NodeAttr.ThreadKillRole, ThreadKillRole.None.getValue(), "子线程删除方式", true, true, NodeAttr.ThreadKillRole, "@0=不能删除@1=手工删除@2=自动删除",true);
			//map.SetHelperUrl(NodeAttr.ThreadKillRole, ""); //增加帮助


		map.AddTBString(BtnAttr.SubFlowLab, "子流程", "子流程按钮标签", true, false, 0, 50, 10);
		map.SetHelperUrl(BtnAttr.SubFlowLab, "http://ccbpm.mydoc.io/?v=5404&t=16262");
		map.AddBoolean(BtnAttr.SubFlowEnable, false, "是否启用", true, true);

		  //  map.AddDDLSysEnum(BtnAttr.SubFlowCtrlRole, 0, "控制规则", true, true, BtnAttr.SubFlowCtrlRole, "@0=无@1=不可以删除子流程@2=可以删除子流程");

		map.AddTBString(BtnAttr.JumpWayLab, "跳转", "跳转按钮标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(NodeAttr.JumpWay, 0, "跳转规则", true, true, NodeAttr.JumpWay);
		map.AddTBString(NodeAttr.JumpToNodes, null, "可跳转的节点", true, false, 0, 200, 10, true);
		map.SetHelperUrl(NodeAttr.JumpWay, "http://ccbpm.mydoc.io/?v=5404&t=16261"); //增加帮助.

		map.AddTBString(BtnAttr.ReturnLab, "退回", "退回按钮标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(NodeAttr.ReturnRole, 0,"退回规则",true, true, NodeAttr.ReturnRole);
		  //  map.AddTBString(NodeAttr.ReturnToNodes, null, "可退回节点", true, false, 0, 200, 10, true);
		map.SetHelperUrl(NodeAttr.ReturnRole, "http://ccbpm.mydoc.io/?v=5404&t=16255"); //增加帮助.

		map.AddBoolean(NodeAttr.IsBackTracking, false, "是否可以原路返回(启用退回功能才有效)", true, true, false);
		map.AddTBString(BtnAttr.ReturnField, "", "退回信息填写字段", true, false, 0, 50, 10);
		map.SetHelperUrl(NodeAttr.IsBackTracking, "http://ccbpm.mydoc.io/?v=5404&t=16255"); //增加帮助.

		map.AddTBString(BtnAttr.CCLab, "抄送", "抄送按钮标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(NodeAttr.CCRole, 0, "抄送规则", true, true, NodeAttr.CCRole, "@@0=不能抄送@1=手工抄送@2=自动抄送@3=手工与自动@4=按表单SysCCEmps字段计算@5=在发送前打开抄送窗口");
		map.SetHelperUrl(BtnAttr.CCLab, "http://ccbpm.mydoc.io/?v=5404&t=16259"); //增加帮助.

			// add 2014-04-05.
		map.AddDDLSysEnum(NodeAttr.CCWriteTo, 0, "抄送写入规则", true, true, NodeAttr.CCWriteTo, "@0=写入抄送列表@1=写入待办@2=写入待办与抄送列表", true);
		map.SetHelperUrl(NodeAttr.CCWriteTo, "http://ccbpm.mydoc.io/?v=5404&t=17976"); //增加帮助

		map.AddTBString(BtnAttr.ShiftLab, "移交", "移交按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ShiftEnable, false, "是否启用", true, true);
		map.SetHelperUrl(BtnAttr.ShiftLab, "http://ccbpm.mydoc.io/?v=5404&t=16257"); //增加帮助.note:none

		map.AddTBString(BtnAttr.DelLab, "删除", "删除按钮标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.DelEnable, 0, "删除规则", true, true, BtnAttr.DelEnable);
		map.SetHelperUrl(BtnAttr.DelLab, "http://ccbpm.mydoc.io/?v=5404&t=17992"); //增加帮助.

		map.AddTBString(BtnAttr.EndFlowLab, "结束流程", "结束流程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.EndFlowEnable, false, "是否启用", true, true);
		map.SetHelperUrl(BtnAttr.EndFlowLab, "http://ccbpm.mydoc.io/?v=5404&t=17989"); //增加帮助

		map.AddTBString(BtnAttr.PrintDocLab, "打印单据", "打印单据按钮标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.PrintDocEnable, 0, "打印方式", true, true, BtnAttr.PrintDocEnable, "@0=不打印@1=打印网页@2=打印RTF模板@3=打印Word模版");
		map.SetHelperUrl(BtnAttr.PrintDocEnable, "http://ccbpm.mydoc.io/?v=5404&t=17979"); //增加帮助

			// map.AddBoolean(BtnAttr.PrintDocEnable, false, "是否启用", true, true);
			//map.AddTBString(BtnAttr.AthLab, "附件", "附件按钮标签", true, false, 0, 50, 10);
			//map.AddDDLSysEnum(NodeAttr.FJOpen, 0, this.ToE("FJOpen", "附件权限"), true, true, 
			//    NodeAttr.FJOpen, "@0=关闭附件@1=操作员@2=工作ID@3=流程ID");

		map.AddTBString(BtnAttr.TrackLab, "轨迹", "轨迹按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.TrackEnable, true, "是否启用", true, true);
			//map.SetHelperUrl(BtnAttr.TrackLab, this[SYS_CCFLOW, "轨迹"]); //增加帮助
		map.SetHelperUrl(BtnAttr.TrackLab, "http://ccbpm.mydoc.io/?v=5404&t=24369");

		map.AddTBString(BtnAttr.HungLab, "挂起", "挂起按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.HungEnable, false, "是否启用", true, true);
		map.SetHelperUrl(BtnAttr.HungLab, "http://ccbpm.mydoc.io/?v=5404&t=16267"); //增加帮助.

		map.AddTBString(BtnAttr.SelectAccepterLab, "接受人", "接受人按钮标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.SelectAccepterEnable, 0, "工作方式", true, true, BtnAttr.SelectAccepterEnable);
		map.SetHelperUrl(BtnAttr.SelectAccepterLab, "http://ccbpm.mydoc.io/?v=5404&t=16256"); //增加帮助


		map.AddTBString(BtnAttr.SearchLab, "查询", "查询按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.SearchEnable, false, "是否启用", true, true);
			//map.SetHelperUrl(BtnAttr.SearchLab, this[SYS_CCFLOW, "查询"]); //增加帮助
		map.SetHelperUrl(BtnAttr.SearchLab, "http://ccbpm.mydoc.io/?v=5404&t=24373");

		map.AddTBString(BtnAttr.WorkCheckLab, "审核", "审核按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.WorkCheckEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.BatchLab, "批处理", "批处理按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.BatchEnable, false, "是否启用", true, true);
		map.SetHelperUrl(BtnAttr.BatchLab, "http://ccbpm.mydoc.io/?v=5404&t=17920"); //增加帮助

		map.AddTBString(BtnAttr.AskforLab, "加签", "加签按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.AskforEnable, false, "是否启用", true, true);
		map.SetHelperUrl(BtnAttr.AskforLab, "http://ccbpm.mydoc.io/?v=5404&t=16258");

			// add by 周朋 2014-11-21. 让用户可以自己定义流转.
		map.AddTBString(BtnAttr.TCLab, "流转自定义", "流转自定义", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.TCEnable, false, "是否启用", true, true);
		map.SetHelperUrl(BtnAttr.TCEnable, "http://ccbpm.mydoc.io/?v=5404&t=17978");

			//map.AddTBString(BtnAttr.AskforLabRe, "执行", "加签按钮标签", true, false, 0, 50, 10);
			//map.AddBoolean(BtnAttr.AskforEnable, false, "是否启用", true, true);

		   // map.SetHelperUrl(BtnAttr.AskforLab, this[SYS_CCFLOW, "加签"]); //增加帮助
		map.AddTBString(BtnAttr.WebOfficeLab, "公文", "文档按钮标签", true, false, 0, 50, 10);
			//  map.AddBoolean(BtnAttr.WebOfficeEnable, false, "是否启用", true, true);
		map.AddDDLSysEnum(BtnAttr.WebOfficeEnable, 0, "文档启用方式", true, true, BtnAttr.WebOfficeEnable, "@0=不启用@1=按钮方式@2=表单在前@3=公文在前"); //edited by liuxc,2016-01-18,from xc
		map.SetHelperUrl(BtnAttr.WebOfficeLab, "http://ccbpm.mydoc.io/?v=5404&t=17993");

			// add by 周朋 2015-08-06. 重要性.
		map.AddTBString(BtnAttr.PRILab, "重要性", "重要性", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.PRIEnable, false, "是否启用", true, true);

			// add by 周朋 2015-08-06. 节点时限.
		map.AddTBString(BtnAttr.CHLab, "节点时限", "节点时限", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.CHEnable, false, "是否启用", true, true);


			// add by 周朋 2015-12-24. 节点时限.
		map.AddTBString(BtnAttr.FocusLab, "关注", "关注", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.FocusEnable, true, "是否启用", true, true);

			//map.AddBoolean(BtnAttr.SelectAccepterEnable, false, "是否启用", true, true);

			///#endregion  功能按钮状态


			///#region 考核属性
			// 考核属性
		map.AddTBFloat(NodeAttr.TimeLimit, 0, "限期(天)", true, false); //"限期(天)".
		map.AddTBFloat(NodeAttr.TSpanHour, 8, "小时", true, false); //"限期(天)".

		map.AddTBFloat(NodeAttr.WarningDay, 0, "工作预警(天)", true, false); // "警告期限(0不警告)"
		map.AddTBFloat(NodeAttr.WarningHour, 0, "工作预警(小时)", true, false); // "警告期限(0不警告)"
		map.SetHelperUrl(NodeAttr.WarningHour, "http://ccbpm.mydoc.io/?v=5404&t=17999");
		map.AddTBFloat(NodeAttr.TCent, 1, "扣分(每延期1小时)", true, false); //"扣分(每延期1天扣)"

			//map.AddTBFloat(NodeAttr.MaxDeductCent, 0, "最高扣分", true, false);   //"最高扣分"
			//map.AddTBFloat(NodeAttr.SwinkCent, float.Parse("0.1"), "工作得分", true, false); //"工作得分".
			//map.AddTBString(NodeAttr.FK_Flows, null, "flow", false, false, 0, 100, 10);

		map.AddDDLSysEnum(NodeAttr.CHWay, 0, "考核方式", true, true, NodeAttr.CHWay,"@0=不考核@1=按时效@2=按工作量");

		   // map.AddTBFloat(NodeAttr.Workload, 0, "工作量(单位:小时)", true, false);

			// 是否质量考核点？
		map.AddBoolean(NodeAttr.IsEval, false, "是否质量考核点", true, true, true);

		map.AddDDLSysEnum(NodeAttr.OutTimeDeal, 0, "超时处理", true, true, NodeAttr.OutTimeDeal, "@0=不处理@1=自动向下运动@2=自动跳转指定的节点@3=自动移交给指定的人员@4=向指定的人员发消息@5=删除流程@6=执行SQL");
		map.AddTBString(NodeAttr.DoOutTime, null, "处理内容", true, false, 0, 300, 10, true);
		map.AddTBString(NodeAttr.DoOutTimeCond, null, "执行超时条件", true, false, 0, 100, 10, true);
		map.SetHelperUrl(NodeAttr.OutTimeDeal, "http://ccbpm.mydoc.io/?v=5404&t=18001");

			///#endregion 考核属性


			///#region 审核组件属性, 此处变更了BP.Sys.FrmWorkCheck 也要变更.
			// BP.Sys.FrmWorkCheck
		map.AddDDLSysEnum(FrmWorkCheckAttr.FWCSta, FrmWorkCheckSta.Disable.getValue(), "审核组件状态", true, true, FrmWorkCheckAttr.FWCSta, "@0=禁用@1=启用@2=只读");
		map.SetHelperUrl(FrmWorkCheckAttr.FWCSta, "http://ccbpm.mydoc.io/?v=5404&t=17936");
		map.AddDDLSysEnum(FrmWorkCheckAttr.FWCShowModel, FrmWorkShowModel.Free.getValue(), "显示方式", true, true, FrmWorkCheckAttr.FWCShowModel, "@0=表格方式@1=自由模式"); //此属性暂时没有用.
		map.SetHelperUrl(FrmWorkCheckAttr.FWCShowModel, "http://ccbpm.mydoc.io/?v=5404&t=17937");
		map.AddDDLSysEnum(FrmWorkCheckAttr.FWCType, FWCType.Check.getValue(), "工作方式", true, true, FrmWorkCheckAttr.FWCType, "@0=审核组件@1=日志组件@2=周报组件@3=月报组件");
		map.SetHelperUrl(FrmWorkCheckAttr.FWCType, "http://ccbpm.mydoc.io/?v=5404&t=17938");
			// add by stone 2015-03-19. 如果为空，就去节点名称显示到步骤里.
		map.AddTBString(FrmWorkCheckAttr.FWCNodeName, null, "节点意见名称", true, false, 0, 100, 10);

		map.AddDDLSysEnum(FrmWorkCheckAttr.FWCAth, FWCAth.None.getValue(), "附件上传", true, true, FrmWorkCheckAttr.FWCAth, "@0=不启用@1=多附件@2=单附件(暂不支持)@3=图片附件(暂不支持)");
		map.SetHelperAlert(FrmWorkCheckAttr.FWCAth, "在审核期间，是否启用上传附件？启用什么样的附件？注意：附件的属性在节点表单里配置。"); //使用alert的方式显示帮助信息.

		map.AddBoolean(FrmWorkCheckAttr.FWCTrackEnable, true, "轨迹图是否显示？", true, true, false);

		map.AddBoolean(FrmWorkCheckAttr.FWCListEnable, true, "历史审核信息是否显示？(否,仅出现意见框)", true, true, true);

		map.AddBoolean(FrmWorkCheckAttr.FWCIsShowAllStep, false, "在轨迹表里是否显示所有的步骤？", true, true);

		map.AddTBString(FrmWorkCheckAttr.FWCOpLabel, "审核", "操作名词(审核/审阅/批示)", true, false, 0, 50, 10);
		map.AddTBString(FrmWorkCheckAttr.FWCDefInfo, "同意", "默认审核信息", true, false, 0, 50, 10);
		map.AddBoolean(FrmWorkCheckAttr.SigantureEnabel, false, "操作人是否显示为图片签名？", true, true);
		map.AddBoolean(FrmWorkCheckAttr.FWCIsFullInfo, true, "如果用户未审核是否按照默认意见填充？", true, true, true);

			//map.AddTBFloat(FrmWorkCheckAttr.FWC_X, 5, "位置X", true, false);
			//map.AddTBFloat(FrmWorkCheckAttr.FWC_Y, 5, "位置Y", true, false);

			// 高度与宽度, 如果是自由表单就不要变化该属性.
		map.AddTBFloat(FrmWorkCheckAttr.FWC_H, 300, "高度", true, false);
		map.SetHelperAlert(FrmWorkCheckAttr.FWC_H, "如果是自由表单就不要变化该属性,为0，则标识为100%,应用的组件模式."); //增加帮助
		map.AddTBFloat(FrmWorkCheckAttr.FWC_W, 400, "宽度", true, false);
		map.SetHelperAlert(FrmWorkCheckAttr.FWC_W, "如果是自由表单就不要变化该属性,为0，则标识为100%,应用的组件模式."); //增加帮助

		map.AddTBStringDoc(FrmWorkCheckAttr.FWCFields, null, "审批格式化字段", true, false,true);

			///#endregion 审核组件属性.


			///#region 父子流程
		map.AddDDLSysEnum(FrmSubFlowAttr.SFSta, FrmSubFlowSta.Disable.getValue(), "控件状态", true, true, FrmSubFlowAttr.SFSta, "@0=禁用@1=启用@2=只读");

		map.AddDDLSysEnum(FrmSubFlowAttr.SFShowModel, FrmWorkShowModel.Free.getValue(), "显示方式", true, true, FrmSubFlowAttr.SFShowModel, "@0=表格方式@1=自由模式"); //此属性暂时没有用.

		map.AddTBString(FrmSubFlowAttr.SFCaption, null, "标题(显示控件头部)", true, false, 0, 50, 10,true);
		map.AddTBString(FrmSubFlowAttr.SFDefInfo, null, "可手工启动的子流程", true, false, 0, 50, 10,true);

		map.AddTBString(FrmSubFlowAttr.SFActiveFlows, null, "可自动触发的子流程", true, false, 0, 50, 10, false);

		map.AddTBString(FrmSubFlowAttr.SFFields, null, "显示的字段列", true, false, 0, 50, 10, true);



		map.AddTBFloat(FrmSubFlowAttr.SF_X, 5, "位置X", true, false);
		map.AddTBFloat(FrmSubFlowAttr.SF_Y, 5, "位置Y", true, false);

		map.AddTBFloat(FrmSubFlowAttr.SF_H, 300, "高度", true, false);
		map.AddTBFloat(FrmSubFlowAttr.SF_W, 400, "宽度", true, false);

			///#endregion 父子流程.


			///#region 公文按钮 del by zhoupeng. 按照新昌的标准修改.
			//map.AddTBString(BtnAttr.OfficeOpenLab, "打开本地", "打开本地标签", true, false, 0, 50, 10);
			//map.SetHelperUrl(BtnAttr.OfficeOpenLab, "http://ccbpm.mydoc.io/?v=5404&t=17998");
			//map.AddBoolean(BtnAttr.OfficeOpenEnable, false, "是否启用", true, true);

			//map.AddTBString(BtnAttr.OfficeOpenTemplateLab, "打开模板", "打开模板标签", true, false, 0, 50, 10);
			//map.SetHelperUrl(BtnAttr.OfficeOpenTemplateLab, "http://ccbpm.mydoc.io/?v=5404&t=17998");
			//map.AddBoolean(BtnAttr.OfficeOpenTemplateEnable, false, "是否启用", true, true);

			//map.AddTBString(BtnAttr.OfficeSaveLab, "保存", "保存标签", true, false, 0, 50, 10);
			//map.SetHelperUrl(BtnAttr.OfficeSaveLab, "http://ccbpm.mydoc.io/?v=5404&t=17998");
			//map.AddBoolean(BtnAttr.OfficeSaveEnable, true, "是否启用", true, true);

			//map.AddTBString(BtnAttr.OfficeAcceptLab, "接受修订", "接受修订标签", true, false, 0, 50, 10);
			//map.SetHelperUrl(BtnAttr.OfficeAcceptLab, "http://ccbpm.mydoc.io/?v=5404&t=17998");
			//map.AddBoolean(BtnAttr.OfficeAcceptEnable, false, "是否启用", true, true);

			//map.AddTBString(BtnAttr.OfficeRefuseLab, "拒绝修订", "拒绝修订标签", true, false, 0, 50, 10);
			//map.SetHelperUrl(BtnAttr.OfficeRefuseLab, "http://ccbpm.mydoc.io/?v=5404&t=17998");
			//map.AddBoolean(BtnAttr.OfficeRefuseEnable, false, "是否启用", true, true);

			//map.AddTBString(BtnAttr.OfficeOverLab, "套红", "套红按钮标签", true, false, 0, 50, 10);
			//map.SetHelperUrl(BtnAttr.OfficeOverLab, "http://ccbpm.mydoc.io/?v=5404&t=17998");
			//map.AddBoolean(BtnAttr.OfficeOverEnable, false, "是否启用", true, true);



			//map.AddTBString(BtnAttr.OfficePrintLab, "打印", "打印按钮标签", true, false, 0, 50, 10);
			//map.SetHelperUrl(BtnAttr.OfficePrintLab, "http://ccbpm.mydoc.io/?v=5404&t=17998");
			//map.AddBoolean(BtnAttr.OfficePrintEnable, false, "是否启用", true, true);

			//map.AddTBString(BtnAttr.OfficeSealLab, "签章", "签章按钮标签", true, false, 0, 50, 10);
			//map.SetHelperUrl(BtnAttr.OfficeSealLab, "http://ccbpm.mydoc.io/?v=5404&t=17998");
			//map.AddBoolean(BtnAttr.OfficeSealEnable, false, "是否启用", true, true);



			//map.AddTBString(BtnAttr.OfficeDownLab, "下载", "下载按钮标签", true, false, 0, 50, 10);
			//map.SetHelperUrl(BtnAttr.OfficeDownLab, "http://ccbpm.mydoc.io/?v=5404&t=17998");
			//map.AddBoolean(BtnAttr.OfficeDownEnable, false, "是否启用", true, true);

			//map.AddTBString(BtnAttr.OfficeInsertFlowLab, "插入流程", "插入流程标签", true, false, 0, 50, 10);
			//map.SetHelperUrl(BtnAttr.OfficeInsertFlowLab, "http://ccbpm.mydoc.io/?v=5404&t=17998");
			//map.AddBoolean(BtnAttr.OfficeInsertFlowEnable, false, "是否启用", true, true);

			//map.AddBoolean(BtnAttr.OfficeNodeInfo, false, "是否记录节点信息", true, true);
			//map.AddBoolean(BtnAttr.OfficeReSavePDF, false, "是否该自动保存为PDF", true, true);


			//map.AddBoolean(BtnAttr.OfficeIsMarks, true, "是否进入留痕模式", true, true);
			//map.AddTBString(BtnAttr.OfficeTemplate, "", "指定文档模板", true, false, 0, 100, 10);
			//map.SetHelperUrl(BtnAttr.OfficeTemplate, "http://ccbpm.mydoc.io/?v=5404&t=17998");

			//map.AddBoolean(BtnAttr.OfficeMarksEnable, true, "是否查看用户留痕", true, true, true);

			//map.AddBoolean(BtnAttr.OfficeIsParent, true, "是否使用父流程的文档", true, true);

			//map.AddBoolean(BtnAttr.OfficeTHEnable, false, "是否自动套红", true, true);
			//map.AddTBString(BtnAttr.OfficeTHTemplate, "", "自动套红模板", true, false, 0, 200, 10);
			//map.SetHelperUrl(BtnAttr.OfficeTHTemplate, "http://ccbpm.mydoc.io/?v=5404&t=17998");

			//if (Glo.IsEnableZhiDu)
			//{
			//    map.AddTBString(BtnAttr.OfficeFengXianTemplate, "", "风险点模板", true, false, 0, 100, 10);
			//    map.AddTBString(BtnAttr.OfficeInsertFengXian, "插入风险点", "插入风险点标签", true, false, 0, 50, 10);
			//    map.AddBoolean(BtnAttr.OfficeInsertFengXianEnabel, false, "是否启用", true, true);
			//}

			//map.AddTBString(BtnAttr.OfficeDownLab, "下载", "下载按钮标签", true, false, 0, 50, 10);
			//map.AddBoolean(BtnAttr.OfficeIsDown, false, "是否启用", true, true);


			///#endregion


			///#region 公文按钮
		map.AddTBString(BtnAttr.OfficeOpen, "打开本地", "打开本地标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeOpenEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeOpenTemplate, "打开模板", "打开模板标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeOpenTemplateEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeSave, "保存", "保存标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeSaveEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeAccept, "接受修订", "接受修订标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeAcceptEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeRefuse, "拒绝修订", "拒绝修订标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeRefuseEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeOver, "套红按钮", "套红按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeOverEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.OfficeMarks, true, "是否查看用户留痕", true, true);
		map.AddBoolean(BtnAttr.OfficeReadOnly, false, "是否只读", true, true);

		map.AddTBString(BtnAttr.OfficePrint, "打印按钮", "打印按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficePrintEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeSeal, "签章按钮", "签章按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeSealEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeInsertFlow, "插入流程", "插入流程标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeInsertFlowEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.OfficeNodeInfo, false, "是否记录节点信息", true, true);
		map.AddBoolean(BtnAttr.OfficeReSavePDF, false, "是否该自动保存为PDF", true, true);

		map.AddTBString(BtnAttr.OfficeDownLab, "下载", "下载按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeIsDown, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.OfficeIsMarks, true, "是否进入留痕模式", true, true);
		map.AddTBString(BtnAttr.OfficeTemplate, "", "指定文档模板", true, false, 0, 100, 10);

		map.AddBoolean(BtnAttr.OfficeIsParent, true, "是否使用父流程的文档", true, true);

		if (BP.WF.Glo.getIsEnableZhiDu())
		{
			map.AddTBString(BtnAttr.OfficeFengXianTemplate, "", "风险点模板", true, false, 0, 100, 10);
			map.AddTBString(BtnAttr.OfficeInsertFengXian, "插入风险点", "插入风险点标签", true, false, 0, 50, 10);
			map.AddBoolean(BtnAttr.OfficeInsertFengXianEnabel, false, "是否启用", true, true);
		}

		map.AddBoolean(BtnAttr.OfficeIsTrueTH, false, "是否自动套红", true, true);
		map.AddTBString(BtnAttr.OfficeTHTemplate, "", "自动套红模板", true, false, 0, 200, 10);

			///#endregion


			///#region 移动设置.
		map.AddDDLSysEnum(NodeAttr.MPhone_WorkModel, 0, "手机工作模式", true, true, NodeAttr.MPhone_WorkModel, "@0=原生态@1=浏览器@2=禁用");
		map.AddDDLSysEnum(NodeAttr.MPhone_SrcModel, 0, "手机屏幕模式", true, true, NodeAttr.MPhone_SrcModel, "@0=强制横屏@1=强制竖屏@2=由重力感应决定");

		map.AddDDLSysEnum(NodeAttr.MPad_WorkModel, 0, "平板工作模式", true, true, NodeAttr.MPad_WorkModel, "@0=原生态@1=浏览器@2=禁用");
		map.AddDDLSysEnum(NodeAttr.MPad_SrcModel, 0, "平板屏幕模式", true, true, NodeAttr.MPad_SrcModel, "@0=强制横屏@1=强制竖屏@2=由重力感应决定");
		map.SetHelperUrl(NodeAttr.MPhone_WorkModel, "http://bbs.ccflow.org/showtopic-2866.jsp");

			///#endregion 移动设置.

			//节点工具栏, 主从表映射.
		map.AddDtl(new NodeToolbars(), NodeToolbarAttr.FK_Node);


			///#region 对应关系
			// 相关功能。
		if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
		{
			map.getAttrsOfOneVSM().Add(new BP.WF.Template.NodeStations(), new BP.Port.Stations(), BP.WF.Template.NodeStationAttr.FK_Node, BP.WF.Template.NodeStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, "节点绑定岗位",Dot2DotModelEnum.Default,null,null);

				//判断是否为集团使用，集团时打开新页面以树形展示
			if (BP.WF.Glo.getIsUnit() == true)
			{
				RefMethod rmDept = new RefMethod();
				rmDept.Title = "节点绑定部门";
				rmDept.ClassMethodName = this.toString() + ".DoDepts";
				rmDept.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
				map.AddRefMethod(rmDept);
			}
			else
			{
				map.getAttrsOfOneVSM().Add(new BP.WF.Template.NodeDepts(), new BP.Port.Depts(), NodeDeptAttr.FK_Node, NodeDeptAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, "节点绑定部门", Dot2DotModelEnum.TreeDept,null,null);
			}
		}
		else
		{
				//节点岗位.
			map.getAttrsOfOneVSM().Add(new BP.WF.Template.NodeStations(), new BP.GPM.Stations(), NodeStationAttr.FK_Node, NodeStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, "节点绑定岗位",Dot2DotModelEnum.Default,null,null);
				//判断是否为集团使用，集团时打开新页面以树形展示
			if (BP.WF.Glo.getIsUnit() == true)
			{
				RefMethod rmDept = new RefMethod();
				rmDept.Title = "节点绑定部门";
				rmDept.ClassMethodName = this.toString() + ".DoDepts";
				rmDept.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
				map.AddRefMethod(rmDept);
			}
			else
			{
					//节点部门.
				map.getAttrsOfOneVSM().Add(new BP.WF.Template.NodeDepts(), new BP.GPM.Depts(), NodeDeptAttr.FK_Node, NodeDeptAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, "节点绑定部门", Dot2DotModelEnum.TreeDept,null,null);
			}
		}


		map.getAttrsOfOneVSM().Add(new BP.WF.Template.NodeEmps(),
				new BP.Port.Emps(), NodeEmpAttr.FK_Node, NodeEmpAttr.FK_Emp,
				DeptAttr.Name, DeptAttr.No, "节点绑定接受人",
				Dot2DotModelEnum.TreeDeptEmp, new Depts(), EmpAttr.FK_Dept);

			// 傻瓜表单可以调用的子流程. 2014.10.19 去掉.
			//map.getAttrsOfOneVSM().Add(new BP.WF.NodeFlows(), new Flows(), NodeFlowAttr.FK_Node, NodeFlowAttr.FK_Flow, DeptAttr.Name, DeptAttr.No,
			//    "傻瓜表单可调用的子流程");

			///#endregion

		RefMethod rm = new RefMethod();
		rm.Title = "可退回的节点(当退回规则设置可退回指定的节点时,该设置有效.)"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoCanReturnNodes";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkModel;
			//设置相关字段.
		rm.RefAttrKey = NodeAttr.ReturnRole;
		rm.RefAttrLinkLabel = "设置可退回的节点";
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "触发子流程的条件(如果不设置默认触发,该设置有效.)"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoActiveFlows";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkModel;
			//设置相关字段.
		rm.RefAttrKey = NodeAttr.SFActiveFlows;
		rm.RefAttrLinkLabel = "触发子流程的条件";
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		   // rm.Title = "可撤销发送节点(只有撤销规则是指定的节点可以撤销时,该设置有效.)"; // "可撤销发送的节点";
		rm.Title = "可撤销的节点"; // "可撤销发送的节点";
		rm.ClassMethodName = this.toString() + ".DoCanCancelNodes";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;

			//设置相关字段.
		rm.RefAttrKey = NodeAttr.CancelRole;
		rm.RefAttrLinkLabel = "";
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置自动抄送规则(当节点为自动抄送时,该设置有效.)"; // "抄送规则";
		rm.ClassMethodName = this.toString() + ".DoCCRole";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
			//设置相关字段.
		rm.RefAttrKey = NodeAttr.CCRole;
		rm.RefAttrLinkLabel = "自动抄送设置";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "设计傻瓜表单(当节点表单类型设置为傻瓜表单时,该设置有效.)"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoFormCol4";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
			//设置相关字段.
		rm.RefAttrKey = NodeAttr.SaveModel;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "设计自由表单(当节点表单类型设置为自由表单时,该设置有效.)"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoFormFree";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
			//设置相关字段.
		rm.RefAttrKey = NodeAttr.SaveModel;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "绑定独立表单"; // "设计表单"; (当节点表单类型设置为树形表单时,该设置有效.)
		rm.ClassMethodName = this.toString() + ".DoFormTree";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
			//设置相关字段.
		rm.RefAttrKey = NodeAttr.SaveModel;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "绑定rtf打印格式模版(当打印方式为打印RTF格式模版时,该设置有效)"; //"单据&单据";
		rm.ClassMethodName = this.toString() + ".DoBill";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/FileType/doc.gif";
		rm.refMethodType = RefMethodType.LinkeWinOpen;

			//设置相关字段.
		rm.RefAttrKey = NodeAttr.PrintDocEnable;
		rm.RefAttrLinkLabel = "";
		rm.Target = "_blank";
		map.AddRefMethod(rm);
		if (BP.Sys.SystemConfig.getCustomerNo().equals("HCBD"))
		{
				// 为海成邦达设置的个性化需求. 
			rm = new RefMethod();
			rm.Title = "DXReport设置";
			rm.ClassMethodName = this.toString() + ".DXReport";
			rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/FileType/doc.gif";
			map.AddRefMethod(rm);
		}

		rm = new RefMethod();
		rm.Title = "节点处理人"; // "节点处理人";
		rm.ClassMethodName = this.toString() + ".DoSelectAccepter";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		   // map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "节点事件&消息"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoAction";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Event.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "向当事人推送消息"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoPush2Current";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Message24.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		  //  map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "向指定人推送消息"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoPush2Spec";
		  //  rm.Icon = BP.WF.Glo.CCFlowAppPath + "WF/Img/Btn/DTS.gif";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Message32.png";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		  //  map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "消息收听"; // "调用事件接口";
		rm.ClassMethodName = this.toString() + ".DoListen";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "流程完成条件"; // "流程完成条件";
		rm.ClassMethodName = this.toString() + ".DoCond";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "发送成功转向条件"; // "转向条件";
		rm.ClassMethodName = this.toString() + ".DoTurn";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
			//设置相关字段.
		rm.RefAttrKey = NodeAttr.TurnToDealDoc;
		rm.RefAttrLinkLabel = "";
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置“接受人选择器”的人员选择范围。"; // "个性化接受人窗口"; //(访问规则设置为第05项有效)
		rm.ClassMethodName = this.toString() + ".DoAccepter";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
			//设置相关字段.
		rm.RefAttrKey = NodeAttr.DeliveryWay;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.RefAttrLinkLabel = "";
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		if (BP.WF.Glo.getOSModel() == OSModel.OneMore)
		{
			rm = new RefMethod();
			rm.Title = "BPM模式接受人设置规则";
			rm.ClassMethodName = this.toString() + ".DoAccepterRole";
			rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";

				//设置相关字段.
				//rm.RefAttrKey = NodeAttr.WhoExeIt;
			rm.refMethodType = RefMethodType.RightFrameOpen;
			rm.RefAttrLinkLabel = "";
			rm.Target = "_blank";
			map.AddRefMethod(rm);
		}

		rm = new RefMethod();
		rm.Title = "设置独立表单树权限";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoNodeFormTree";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置微信端表单字段顺序";
		rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoSortingMapAttrs";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		if (BP.WF.Glo.getIsEnableZhiDu())
		{
			rm = new RefMethod();
			rm.Title = "对应制度章节"; // "个性化接受人窗口";
			rm.ClassMethodName = this.toString() + ".DoZhiDu";
			rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
			map.AddRefMethod(rm);

			rm = new RefMethod();
			rm.Title = "风险点"; // "个性化接受人窗口";
			rm.ClassMethodName = this.toString() + ".DoFengXianDian";
			rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
			map.AddRefMethod(rm);

			rm = new RefMethod();
			rm.Title = "岗位职责"; // "个性化接受人窗口";
			rm.ClassMethodName = this.toString() + ".DoGangWeiZhiZe";
			rm.Icon = BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/DTS.gif";
			map.AddRefMethod(rm);
		}

		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 排序字段顺序
	 
	 @return 
	*/
	public final String DoSortingMapAttrs()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/AttrNode/SortingMapAttrs.htm?FK_Flow=" + this.getFK_Flow() + "&FK_MapData=ND" + this.getNodeID() + "&t=" + DataType.getCurrentDataTime();
	}
	/** 
	 集团部门树
	 
	 @return 
	*/
	public final String DoDepts()
	{
		try {
			PubClass.WinOpen(ContextHolderUtils.getResponse(),Glo.getCCFlowAppPath() + "WF/Comm/Port/DeptTree.jsp?s=d34&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getNodeID() + "&RefNo=" + DataType.getCurrentDataTime(), 500, 550);
		} catch (Exception e) {
			Log.DebugWriteError("NodeSheet DoDepts "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	/** 
	 设置独立表单树权限
	 
	 @return 
	*/
	public final String DoNodeFormTree()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/FlowFormTree.jsp?s=d34&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getNodeID() + "&RefNo=" + DataType.getCurrentDataTime();
	}
	/** 
	 制度
	 
	 @return 
	*/
	public final String DoZhiDu()
	{
		//PubClass.WinOpen(BP.WF.Glo.getCCFlowAppPath() + "ZhiDu/NodeZhiDuDtl.jsp?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow(), "制度", "Bill", 700, 400, 200, 300);
		try {
			PubClass.WinOpen(ContextHolderUtils.getResponse(),Glo.getCCFlowAppPath() + "ZhiDu/NodeZhiDuDtl.jsp?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow(), "制度", "Bill", 700, 400, 200, 300);
		} catch (Exception e) {
			Log.DebugWriteError("NodeSheet DoZhiDu "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	/** 
	 风险点
	 
	 @return 
	*/
	public final String DoFengXianDian()
	{
		//PubClass.WinOpen(BP.WF.Glo.getCCFlowAppPath() + "ZhiDu/NodeFengXianDian.jsp?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow(), "制度", "Bill", 700, 400, 200, 300);
		try {
			PubClass.WinOpen(ContextHolderUtils.getResponse(),Glo.getCCFlowAppPath() + "ZhiDu/NodeFengXianDian.jsp?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow(), "制度", "Bill", 700, 400, 200, 300);
		} catch (Exception e) {
			Log.DebugWriteError("NodeSheet DoFengXianDian "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	/** 
	 接收人
	 
	 @return 
	*/
	public final String DoSelectAccepter()
	{
		BP.WF.Node nd = new BP.WF.Node(this.getNodeID());
		if (nd.getHisDeliveryWay() != DeliveryWay.ByCCFlowBPM)
		{
			return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/FindWorker/List.jsp?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
		}
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/FindWorker/NodeAccepterRole.jsp?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 找人规则
	 
	 @return 
	*/
	public final String DoAccepterRole()
	{
		BP.WF.Node nd = new BP.WF.Node(this.getNodeID());

		if (nd.getHisDeliveryWay() != DeliveryWay.ByCCFlowBPM)
		{
			return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/FindWorker/List.jsp?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
		}
		//    return "节点访问规则您没有设置按照bpm模式，所以您能执行该操作。要想执行该操作请选择节点属性中节点规则访问然后选择按照bpm模式计算，点保存按钮。";

		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/FindWorker/List.jsp?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	 //   return null;
	}
	public final String DoTurn()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/TurnTo.jsp?FK_Node=" + this.getNodeID();
		//, "节点完成转向处理", "FrmTurn", 800, 500, 200, 300);
		//BP.WF.Node nd = new BP.WF.Node(this.NodeID);
		//return nd.DoTurn();
	}
	/** 
	 抄送规则
	 
	 @return 
	*/
	public final String DoCCRole()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Comm/RefFunc/UIEn.jsp?EnName=BP.WF.Template.CC&PK=" + this.getNodeID();
		//PubClass.WinOpen("./RefFunc/UIEn.jsp?EnName=BP.WF.CC&PK=" + this.NodeID, "抄送规则", "Bill", 800, 500, 200, 300);
		//return null;
	}
	/** 
	 个性化接受人窗口
	 
	 @return 
	*/
	public final String DoAccepter()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Comm/RefFunc/UIEn.jsp?EnName=BP.WF.Template.Selector&PK=" + this.getNodeID();
	}
	/** 
	 可触发的子流程
	 
	 @return 
	*/
	public final String DoActiveFlows()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/ConditionSubFlow.jsp?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 退回节点
	 
	 @return 
	*/
	public final String DoCanReturnNodes()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/CanReturnNodes.jsp?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 撤销发送的节点
	 
	 @return 
	*/
	public final String DoCanCancelNodes()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/CanCancelNodes.jsp?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 DXReport
	 
	 @return 
	*/
	public final String DXReport()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/DXReport.jsp?FK_Node=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	public final String DoPush2Current()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/Listen.jsp?CondType=0&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getNodeID() + "&FK_Attr=&DirType=&ToNodeID=";
	}
	public final String DoPush2Spec()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/Listen.jsp?CondType=0&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getNodeID() + "&FK_Attr=&DirType=&ToNodeID=";
	}
	/** 
	 执行消息收听
	 
	 @return 
	*/
	public final String DoListen()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/Listen.jsp?CondType=0&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getNodeID() + "&FK_Attr=&DirType=&ToNodeID=";
	}
	public final String DoFeatureSet()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/FeatureSetUI.jsp?CondType=0&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getNodeID() + "&FK_Attr=&DirType=&ToNodeID=";
	}
	public final String DoShowSheets()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/ShowSheets.jsp?CondType=0&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getNodeID() + "&FK_Attr=&DirType=&ToNodeID=";
	}
	public final String DoCond()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/Condition.jsp?CondType=" + CondType.Flow.getValue() + "&FK_Flow=" + this.getFK_Flow() + "&FK_MainNode=" + this.getNodeID() + "&FK_Node=" + this.getNodeID() + "&FK_Attr=&DirType=&ToNodeID=" + this.getNodeID();
	}
	/** 
	 设计傻瓜表单
	 
	 @return 
	*/
	public final String DoFormCol4()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/Designer.htm?PK=ND" + this.getNodeID();
	}
	/** 
	 设计自由表单
	 
	 @return 
	*/
	public final String DoFormFree()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/CCForm/Frm.jsp?FK_MapData=ND" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 绑定独立表单
	 
	 @return 
	*/
	public final String DoFormTree()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/BindFrms.jsp?ShowType=FlowFrms&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getNodeID() + "&Lang=CH";
	}

	public final String DoMapData()
	{
		int i = this.GetValIntByKey(NodeAttr.FormType);

		// 类型.
		NodeFormType type = NodeFormType.forValue(i);
		switch (type)
		{
			case FreeForm:
				//PubClass.WinOpen(BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/CCForm/Frm.jsp?FK_MapData=ND" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow(), "设计表单", "sheet", 1024, 768, 0, 0);
				try {
					PubClass.WinOpen(ContextHolderUtils.getResponse(),Glo.getCCFlowAppPath() + "WF/MapDef/CCForm/Frm.jsp?FK_MapData=ND" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow(), "设计表单", "sheet", 1024, 768, 0, 0);
				} catch (Exception e) {
					Log.DebugWriteError("1 NodeSheet DoMapData "+ e.getMessage());
					e.printStackTrace();
				}
				break;
			default:
			case FoolForm:
				//PubClass.WinOpen(BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/Designer.htm?PK=ND" + this.getNodeID(), "设计表单", "sheet", 800, 500, 210, 300);
				try {
					PubClass.WinOpen(ContextHolderUtils.getResponse(),Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/Designer.htm?PK=ND" + this.getNodeID(), "设计表单", "sheet", 800, 500, 210, 300);
				} catch (Exception e) {
					Log.DebugWriteError("2 NodeSheet DoMapData "+ e.getMessage());
					e.printStackTrace();
				}
				break;
		}
		return null;
	}
	public final String DoAction()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/AttrNode/Action.jsp?NodeID=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow() + "&tk=" + new java.util.Random().nextDouble();
	}
	/** 
	 单据打印
	 
	 @return 
	*/
	public final String DoBill()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/Bill.jsp?NodeID=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 设置
	 
	 @return 
	*/
	public final String DoFAppSet()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "WF/Admin/FAppSet.jsp?NodeID=" + this.getNodeID() + "&FK_Flow=" + this.getFK_Flow();
	}

	@Override
	protected boolean beforeUpdate()
	{
		//更新流程版本
		Flow.UpdateVer(this.getFK_Flow());

		//把工具栏的配置放入 sys_mapdata里.
		ToolbarExcel te = new ToolbarExcel("ND" + this.getNodeID());
		te.Copy(this);
		try
		{
			te.Update();
		}
		catch (java.lang.Exception e)
		{
		}


			///#region  检查考核逾期处理的设置的完整性.
		String doOutTime = this.GetValStrByKey(NodeAttr.DoOutTime);
		switch (this.getHisOutTimeDeal())
		{
			case AutoJumpToSpecNode:
				String[] jumps = doOutTime.split("[,]", -1);
				if (jumps.length > 2)
				{
					String msg = "自动跳转到相应节点,配置的内容不正确,格式应该为: Node,EmpNo , 比如: 101,zhoupeng  现在设置的格式为:" + doOutTime;
					throw new RuntimeException(msg);
				}
				break;
			case AutoShiftToSpecUser:
			case RunSQL:
			case SendMsgToSpecUser:
				if (StringHelper.isNullOrEmpty(doOutTime) == false)
				{
					throw new RuntimeException("@在考核逾期处理方式上，您选择的是:" + this.getHisOutTimeDeal() + " ,但是您没有为该规则设置内容。");
				}
				break;
			default:
				break;
		}

			///#endregion 检查考核逾期处理的设置的完整性


			///#region 处理节点数据.
		Node nd = new Node(this.getNodeID());
		if (nd.getIsStartNode() == true)
		{
			//处理按钮的问题
			//不能退回, 加签，移交，退回, 子线程.
			this.SetValByKey(BtnAttr.ReturnRole,ReturnRole.CanNotReturn.getValue());
			this.SetValByKey(BtnAttr.HungEnable, false);
			this.SetValByKey(BtnAttr.ThreadEnable, false); //子线程.
		}

		if (nd.getHisRunModel() == RunModel.HL || nd.getHisRunModel() == RunModel.FHL)
		{
			//如果是合流点
		}
		else
		{
			this.SetValByKey(BtnAttr.ThreadEnable, false); //子线程.
		}

			///#endregion 处理节点数据.


			///#region 处理消息参数字段.
		//this.SetPara(NodeAttr.MsgCtrl, this.GetValIntByKey(NodeAttr.MsgCtrl));
		//this.SetPara(NodeAttr.MsgIsSend, this.GetValIntByKey(NodeAttr.MsgIsSend));
		//this.SetPara(NodeAttr.MsgIsReturn, this.GetValIntByKey(NodeAttr.MsgIsReturn));
		//this.SetPara(NodeAttr.MsgIsShift, this.GetValIntByKey(NodeAttr.MsgIsShift));
		//this.SetPara(NodeAttr.MsgIsCC, this.GetValIntByKey(NodeAttr.MsgIsCC));

		//this.SetPara(NodeAttr.MailEnable, this.GetValIntByKey(NodeAttr.MailEnable));
		//this.SetPara(NodeAttr.MsgMailTitle, this.GetValStrByKey(NodeAttr.MsgMailTitle));
		//this.SetPara(NodeAttr.MsgMailDoc, this.GetValStrByKey(NodeAttr.MsgMailDoc));

		//this.SetPara(NodeAttr.MsgSMSEnable, this.GetValIntByKey(NodeAttr.MsgSMSEnable));
		//this.SetPara(NodeAttr.MsgSMSDoc, this.GetValStrByKey(NodeAttr.MsgSMSDoc));

			///#endregion

		//创建审核组件附件
		FrmAttachment workCheckAth = new FrmAttachment();
		boolean isHave = workCheckAth.RetrieveByAttr(FrmAttachmentAttr.MyPK, this.getNodeID() + "_FrmWorkCheck");
		//不包含审核组件
		if (isHave == false)
		{
			workCheckAth = new FrmAttachment();
			//如果没有查询到它,就有可能是没有创建.
			workCheckAth.setMyPK(this.getNodeID() + "_FrmWorkCheck");
			workCheckAth.setFK_MapData((new Integer(this.getNodeID())).toString());
			workCheckAth.setNoOfObj(this.getNodeID() + "_FrmWorkCheck");
			workCheckAth.setExts("*.*");

			//存储路径.
			workCheckAth.setSaveTo("/DataUser/UploadFile/");
			workCheckAth.setIsNote(false); //不显示note字段.
			workCheckAth.setIsVisable(false); // 让其在form 上不可见.

			//位置.
			workCheckAth.setX((float)94.09);
			workCheckAth.setY((float)333.18);
			workCheckAth.setW((float)626.36);
			workCheckAth.setH((float)150);

			//多附件.
			workCheckAth.setUploadType(AttachmentUploadType.Multi);
			workCheckAth.setName("审核组件");
			workCheckAth.SetValByKey("AtPara", "@IsWoEnablePageset=1@IsWoEnablePrint=1@IsWoEnableViewModel=1@IsWoEnableReadonly=0@IsWoEnableSave=1@IsWoEnableWF=1@IsWoEnableProperty=1@IsWoEnableRevise=1@IsWoEnableIntoKeepMarkModel=1@FastKeyIsEnable=0@IsWoEnableViewKeepMark=1@FastKeyGenerRole=@IsWoEnableTemplete=1");
			workCheckAth.Insert();
		}

		//清除所有的缓存.
		BP.DA.CashEntity.getDCash().clear();

		return super.beforeUpdate();
	}

		///#endregion
}