package BP.WF.Entity;

import BP.En.Entity;
import BP.En.EntityOID;
import BP.En.Map;
import BP.En.QueryObject;
import BP.WF.Flow;
import BP.WF.Node;

/**
 * 产生分合流程控制
 */
public class GenerFH extends EntityOID
{
	private Map _enMap;
	
	// 基本属性
	@Override
	public String getPK()
	{
		return "OID";
	}
	
	/**
	 * HisFlow
	 */
	public final Flow getHisFlow()
	{
		return new Flow(this.getFK_Flow());
	}
	
	public final String getRDT()
	{
		return this.GetValStringByKey(GenerFHAttr.RDT);
	}
	
	public final void setRDT(String value)
	{
		SetValByKey(GenerFHAttr.RDT, value);
	}
	
	public final String getTitle()
	{
		return this.GetValStringByKey(GenerFHAttr.Title);
	}
	
	public final void setTitle(String value)
	{
		SetValByKey(GenerFHAttr.Title, value);
	}
	
	/**
	 * 工作流程编号
	 */
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(GenerFHAttr.FK_Flow);
	}
	
	public final void setFK_Flow(String value)
	{
		SetValByKey(GenerFHAttr.FK_Flow, value);
	}
	
	public final String getToEmpsMsg()
	{
		return this.GetValStringByKey(GenerFHAttr.ToEmpsMsg);
	}
	
	public final void setToEmpsMsg(String value)
	{
		SetValByKey(GenerFHAttr.ToEmpsMsg, value);
	}
	
	/**
	 * 流程ID
	 */
	public final long getFID()
	{
		return this.getOID(); //  this.GetValInt64ByKey(GenerFHAttr.FID);
		
		
	}
	
	public final void setFID(long value)
	{
		SetValByKey(GenerFHAttr.FID, value);
		SetValByKey("OID", value);
	}
	
	public final String getGroupKey()
	{
		return this.GetValStringByKey(GenerFHAttr.GroupKey);
	}
	
	public final void setGroupKey(String value)
	{
		this.SetValByKey(GenerFHAttr.GroupKey, value);
	}
	
	public final String getFK_NodeText()
	{
		Node nd = new Node(this.getFK_Node());
		return nd.getName();
	}
	
	/**
	 * 当前工作到的节点
	 */
	public final int getFK_Node()
	{
		return this.GetValIntByKey(GenerFHAttr.FK_Node);
	}
	
	public final void setFK_Node(int value)
	{
		SetValByKey(GenerFHAttr.FK_Node, value);
	}
	
	/**
	 * 工作流程状态( 0, 未完成,1 完成, 2 强制终止 3, 删除状态,)
	 */
	public final int getWFState()
	{
		return this.GetValIntByKey(GenerFHAttr.WFState);
	}
	
	public final void setWFState(int value)
	{
		SetValByKey(GenerFHAttr.WFState, value);
	}
	
	// 构造函数
	/**
	 * 产生分合流程控制流程
	 */
	public GenerFH()
	{
	}
	
	/**
	 * 产生分合流程控制流程
	 * 
	 * @param FID
	 */
	public GenerFH(long FID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(GenerFHAttr.FID, FID);
		if (qo.DoQuery() == 0)
		{
			throw new RuntimeException("查询 GenerFH 工作[" + FID + "]不存在，可能是已经完成。");
		}
	}
	
	/**
	 * 产生分合流程控制流程
	 * 
	 * @param FID
	 *            工作流程ID
	 * @param flowNo
	 *            流程编号
	 */
	public GenerFH(long FID, String flowNo)
	{
		try
		{
			this.setFID(FID);
			this.setFK_Flow(flowNo);
			this.Retrieve();
		} catch (RuntimeException ex)
		{
			// WorkFlow wf = new WorkFlow(new Flow(flowNo), FID, FID);
			// StartWork wk = wf.HisStartWork;
			// if (wf.WFState == BP.WF.WFState.Complete)
			// {
			// throw new
			// Exception("@已经完成流程，不存在于当前工作集合里，如果要得到此流程的详细信请查看历史工作。技术信息:" +
			// ex.Message);
			// }
			// else
			// {
			// this.Copy(wk);
			// //string msg =
			// "@流程内部错误，给您带来的不便，深表示抱歉，请把此情况通知给系统管理员。error code:0001更多的信息:" +
			// ex.Message;
			// string msg =
			// "@流程内部错误，给您带来的不便，深表示抱歉，请把此情况通知给系统管理员。error code:0001更多的信息:" +
			// ex.Message;
			// Log.DefaultLogWriteLine(LogType.Error, "@工作完成后在使用它抛出的异常：" + msg);
			// //throw new Exception(msg);
			// }
		}
	}
	
	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("WF_GenerFH");
		map.setEnDesc("分合流程控制");
		
		map.AddTBIntPK("OID", 0, "流程ID", true, true);
		map.AddTBInt(GenerFHAttr.FID, 0, "FID", true, false);
		
		map.AddTBString(GenerFHAttr.Title, null, "标题", true, false, 0, 4000, 10);
		map.AddTBString(GenerFHAttr.GroupKey, null, "分组主键", true, false, 0,
				3000, 10);
		map.AddTBString(GenerFHAttr.FK_Flow, null, "流程", true, false, 0, 500,
				10);
		map.AddTBString(GenerFHAttr.ToEmpsMsg, null, "接受人员", true, false, 0,
				4000, 10);
		map.AddTBInt(GenerFHAttr.FK_Node, 0, "停留节点", true, false);
		map.AddTBInt(GenerFHAttr.WFState, 0, "WFState", true, false);
		map.AddTBDate(GenerFHAttr.RDT, null, "RDT", true, false);
		
		// RefMethod rm = new RefMethod();
		// rm.Title = "工作报告"; // "工作报告";
		// rm.ClassMethodName = this.ToString() + ".DoRpt";
		// rm.Icon = "..../Img/Btn/doc.gif";
		// map.AddRefMethod(rm);
		
		// rm = new RefMethod();
		// rm.Title = this.ToE("FlowSelfTest", "流程自检"); // "流程自检";
		// rm.ClassMethodName = this.ToString() + ".DoSelfTestInfo";
		// map.AddRefMethod(rm);
		
		// rm = new RefMethod();
		// rm.Title = "流程自检并修复";
		// rm.ClassMethodName = this.ToString() + ".DoRepare";
		// rm.Warning =
		// "您确定要执行此功能吗？ \t\n 1)如果是断流程，并且停留在第一个节点上，系统为执行删除它。\t\n 2)如果是非地第一个节点，系统会返回到上次发起的位置。";
		// map.AddRefMethod(rm);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	// 重载基类方法
	@Override
	protected void afterDelete()
	{
		super.afterDelete();
	}
}