package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.Flows;


/** 
 延续子流程.	 
 
*/
public class NodeYGFlow extends EntityOID
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 UI界面上的访问控制
	 
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 流程编号
	 
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(NodeYGFlowAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(NodeYGFlowAttr.FK_Flow, value);
	}
	/** 
	 流程名称
	 
	*/
	public final String getFlowName()
	{
		return this.GetValRefTextByKey(NodeYGFlowAttr.FK_Flow);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 延续子流程
	 
	*/
	public NodeYGFlow()
	{
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

		Map map = new Map("WF_NodeSubFlow", "延续子流程");

		map.AddTBIntPKOID();

		map.AddTBInt(NodeYGFlowAttr.FK_Node, 0, "节点", false, true);
		map.AddDDLEntities(NodeYGFlowAttr.FK_Flow, null, "延续子流程", new Flows(), true);
		map.AddDDLSysEnum(NodeYGFlowAttr.YGWorkWay, 1, "工作方式", true, true, NodeYGFlowAttr.YGWorkWay, "@0=停止当前节点等待延续子流程行完毕后该节点自动向下运行@1=启动越轨流程运行到下一步骤上去");
		map.AddTBInt(NodeYGFlowAttr.Idx, 0, "显示顺序", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
