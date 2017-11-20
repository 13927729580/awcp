package org.jflow.framework.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.En.QueryObject;
import BP.Port.WebUser;
import BP.Sys.SystemConfig;
import BP.WF.FlowOpList;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Entity.GenerWorkerListAttr;
import BP.WF.Entity.GenerWorkerLists;
import BP.WF.Entity.GetTask;
import BP.WF.Template.PubLib.WFState;

public class OpModel extends BaseModel{
	
	public StringBuffer Pub2=null;
	private String basePath;
	private String DoType;
	private String FK_Node;
	private String FK_Flow;
	private int WorkID;
	private int FID;
	
	public OpModel(HttpServletRequest request, HttpServletResponse response,String basePath,String DoType,String FK_Node,String FK_Flow,int WorkID,int FID) {
		super(request, response);
		Pub2=new StringBuffer();
		this.basePath=basePath;
		this.DoType=DoType;
		this.FK_Node=FK_Node;
		this.FK_Flow=FK_Flow;
		this.WorkID=WorkID;
		this.FID=FID;
	}

	public void init(){
		 int wfState = BP.DA.DBAccess.RunSQLReturnValInt("SELECT WFState FROM WF_GenerWorkFlow WHERE WorkID=" + WorkID, 1);
         WFState wfstateEnum = WFState.forValue(wfState);
         //this.Pub2.AddH3("您可执行的操作<hr>");
         if(wfstateEnum==WFState.Runing){
        	 this.FlowOverByCoercion(); /*删除流程.*/
             this.TackBackCheck(); /*取回审批*/
             this.Hurry(); /*催办*/
             this.UnSend(); /*撤销发送*/
         }
         else if(wfstateEnum==WFState.Complete){
        	 
         }
         else if(wfstateEnum==WFState.Delete){
        	 this.RollBack();
         }
         else if(wfstateEnum==WFState.HungUp){
        	 this.AddUnHungUp();
         }
         else{
        	 
         }
//         switch (wfstateEnum)
//         {
//             case WFState.Runing: /* 运行时*/
//                 this.FlowOverByCoercion(); /*删除流程.*/
//                 this.TackBackCheck(); /*取回审批*/
//                 this.Hurry(); /*催办*/
//                 this.UnSend(); /*撤销发送*/
//                 break;
//             case WFState.Complete: // 完成.
//             case WFState.Delete: // 逻辑删除..
//                 this.RollBack(); /*恢复使用流程*/
//                 break;
//             case WFState.HungUp: // 挂起.
//                 this.AddUnHungUp(); /*撤销挂起*/
//                 break;
//             default:
//                 break;
//         }
	}
	
			
    /// 取回审批
    public void TackBackCheck()
    {
        GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
        /* 判断是否有取回审批的权限。*/
        //Pub2.append(this.AddEasyUiPanelInfoBegin("取回审批","icon-tip", 250));//.AddEasyUiPanelInfoBegin("取回审批");
        Pub2.append(this.AddEasyUiPanelInfoBegin("取回审批", "collapse-panel-1"));
        String sql = "SELECT NodeID FROM WF_Node WHERE CheckNodes LIKE '%" + gwf.getFK_Node() + "%'";
        int myNode = DBAccess.RunSQLReturnValInt(sql, 0);
        if (myNode != 0)
        {
            GetTask gt = new GetTask(myNode);
            if (gt.Can_I_Do_It() == true)
            {
            	
            	Pub2.append("功能执行:<a href=\"javascript:Takeback('" + WorkID + "','" + FK_Flow + "','" + gwf.getFK_Node() + "','" + myNode + "')\" >点击执行取回审批流程</a>。");
                Pub2.append(this.AddBR("说明：如果被成功取回，ccflow就会把停留在别人工作节点上的工作发送到您的待办列表里。"));//.AddBR("说明：如果被成功取回，ccflow就会把停留在别人工作节点上的工作发送到您的待办列表里。");
            }
        }
        else
        {
        	Pub2.append("您没有此权限");
        }
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
    
    // 强制删除流程
    public void FlowOverByCoercion()
    {
        GenerWorkFlow gwf = new GenerWorkFlow(WorkID);
        //Pub2.append(this.AddEasyUiPanelInfoBegin("删除流程", "icon-tip", 250));//.AddEasyUiPanelInfoBegin("删除流程");
        Pub2.append(this.AddEasyUiPanelInfoBegin("删除流程", "collapse-panel-2"));
        if (WebUser.getNo().equals(SystemConfig.getAppSettings().get("Admin").toString()))
        {
        	Pub2.append("功能执行:<a href=\"javascript:DoFunc('" + FlowOpList.FlowOverByCoercion + "','" + WorkID + "','" + FK_Flow + "','" + FK_Node + "')\" >点击执行删除流程</a>。");
            Pub2.append(this.AddBR("说明：如果执行流程将会被彻底的删除。"));//.AddBR();
        }
        else
        {
        	Pub2.append("只有admin才能删除流程，您没有此权限.");
        }	
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
    
    // 催办
    public void Hurry()
    {
        /*催办*/
        //Pub2.append(this.AddEasyUiPanelInfoBegin("工作催办","icon-tip", 250));
        Pub2.append(this.AddEasyUiPanelInfoBegin("工作催办", "collapse-panel-3"));
        Pub2.append("您没有此权限.");
        //this.Pub2.Add("您没有此权限.");

        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
    
    
    
    // 撤销发送
    public void UnSend()
    {
        /*撤销发送*/
        //Pub2.append(this.AddEasyUiPanelInfoBegin("撤销发送", "icon-tip", 250));//.AddEasyUiPanelInfoBegin("撤销发送");
        Pub2.append(this.AddEasyUiPanelInfoBegin("撤销发送", "collapse-panel-4"));

        //查询是否有权限撤销发送
        GenerWorkerLists workerlists = new GenerWorkerLists();

        QueryObject info = new QueryObject(workerlists);
       
        info.AddWhere(GenerWorkerListAttr.FK_Emp, WebUser.getNo());
        info.addAnd();
        info.AddWhere(GenerWorkerListAttr.IsPass, "1");
        info.addAnd();
        info.AddWhere(GenerWorkerListAttr.IsEnable, "1");
        info.addAnd();
        info.AddWhere(GenerWorkerListAttr.WorkID, this.WorkID);
        int count = info.DoQuery();
        if (count > 0)
        {
        	Pub2.append("<a href =\"javascript:UnSend('" + this.FK_Flow + "','" + this.WorkID + "','" + FID + "')\" >撤销发送</a>");
        }
        else
        {
        	Pub2.append("您没有此权限.");
        }

        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
    
    
    
    
    // 恢复启用流程数据到结束节点
    public void RollBack()
    {
    	//Pub2.append(this.AddEasyUiPanelInfoBegin("恢复启用流程数据到结束节点","icon-tip", 250));
    	Pub2.append(this.AddEasyUiPanelInfoBegin("恢复启用流程数据到结束节点", "collapse-panel-5"));
        if (WebUser.getNo().equals(SystemConfig.getAppSettings().get("Admin").toString()))
        {
        	Pub2.append("功能执行:<a href=\"javascript:DoFunc('ComeBack','" + WorkID + "','" + FK_Flow + "','" + FK_Node + "')\" >点击执行恢复流程</a>。");
            Pub2.append(this.AddBR("说明：如果被成功恢复，ccflow就会把待办工作发送给最后一个结束流程的工作人员。"));
        }
        else
        {
        	Pub2.append("您没有权限.");
        }
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }

    // 取消挂起
    public void AddUnHungUp()
    {
    	//Pub2.append(this.AddEasyUiPanelInfoBegin("取消挂起","icon-tip", 250));
    	Pub2.append(this.AddEasyUiPanelInfoBegin("取消挂起", "collapse-panel-6"));
        if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(FK_Flow, Integer.parseInt(FK_Node), WorkID, WebUser.getNo()))
        {
        	Pub2.append("功能执行:<a href=\"javascript:DoFunc('UnHungUp','" + WorkID + "','" + FK_Flow + "','" + FK_Node + "')\" >点击执行取消挂起流程</a>。");
            Pub2.append(this.AddBR("说明：解除流程挂起的状态。"));
        }
        else
        {
        	Pub2.append(this.AddBR("您没有此权限，或者当前不是挂起的状态。"));
        }
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
    /// 挂起
    public void AddHungUp()
    {
    	//Pub2.append(this.AddEasyUiPanelInfoBegin("挂起", "icon-tip", 250));
    	Pub2.append(this.AddEasyUiPanelInfoBegin("挂起", "collapse-panel-7"));
        if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(FK_Flow, Integer.parseInt(FK_Node), WorkID, WebUser.getNo()))
        {
        	Pub2.append("功能执行:<a href=\"javascript:DoFunc('" + FlowOpList.HungUp + "','" + WorkID + "','" + FK_Flow + "','" + FK_Node + "','')\" >点击执行挂起流程</a>。");
            Pub2.append(this.AddBR("说明：对该流程执行挂起，挂起后可以解除挂起，挂起的时间不计算考核。"));
        }
        else
        {
        	Pub2.append("您没有此权限.");
        }
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
    /// 移交
    public void AddShift()
    {
    	//Pub2.append(this.AddEasyUiPanelInfoBegin("移交","icon-tip", 250));
    	Pub2.append(this.AddEasyUiPanelInfoBegin("移交", "collapse-panel-8"));
        if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(FK_Flow, Integer.parseInt(FK_Node), WorkID, WebUser.getNo()));
        {
        	Pub2.append("功能执行:<a href=\"javascript:DoFunc('" + FlowOpList.UnHungUp + "','" + WorkID + "','" + FK_Flow + "','" + FK_Node + "')\" >点击执行取消挂起流程</a>。");
        	Pub2.append(this.AddBR("说明：解除流程挂起的状态。"));
        }
        if(!BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(FK_Flow, Integer.parseInt(FK_Node), WorkID, WebUser.getNo()))//else
        {
            Pub2.append(this.AddBR("您没有此权限，或者当前不是挂起的状态。"));//.AddBR("您没有此权限，或者当前不是挂起的状态。");
        }
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }

    public void AddShiftByCoercion()
    {
    	//Pub2.append(this.AddEasyUiPanelInfoBegin("强制移交", "icon-tip", 250));
    	Pub2.append(this.AddEasyUiPanelInfoBegin("强制移交", "collapse-panel-9"));
        if (WebUser.getNo().equals(SystemConfig.getAppSettings().get("Admin").toString()))
        {
        	Pub2.append("功能执行:<a href=\"javascript:DoFunc('" + FlowOpList.ShiftByCoercion + "','" + WorkID + "','" + FK_Flow + "','" + FK_Node + "')\" >点击执行取消挂起流程</a>。");
           Pub2.append(this.AddBR("说明：解除流程挂起的状态。"));
        }
        else
        {
        	Pub2.append(this.AddBR("您没有此权限。"));
        }
        Pub2.append(this.AddEasyUiPanelInfoEnd());
        Pub2.append(this.AddBR());
    }
}
