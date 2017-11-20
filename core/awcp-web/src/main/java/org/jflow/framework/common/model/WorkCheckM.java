package org.jflow.framework.common.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.system.ui.core.TextBox;
import org.jflow.framework.system.ui.core.TextBoxMode;

import BP.DA.DataType;
import BP.Port.WebUser;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.Entity.FrmWorkCheck;
import BP.WF.Entity.FrmWorkCheckSta;
import BP.WF.Entity.FrmWorkShowModel;
import BP.WF.Entity.GenerWorkerList;
import BP.WF.Entity.GenerWorkerLists;
import BP.WF.Entity.Track;
import BP.WF.Entity.Tracks;
import BP.WF.Entity.WorkCheck;
import BP.WF.Template.Node;
import BP.WF.Template.Nodes;
import BP.WF.Template.AccepterRole.SelectAccper;
import BP.WF.Template.AccepterRole.SelectAccperAttr;
import BP.WF.Template.AccepterRole.SelectAccpers;
import BP.WF.Template.CC.CCList;
import BP.WF.Template.CC.CCListAttr;
import BP.WF.Template.CC.CCLists;
import BP.WF.Template.CC.CCSta;
import BP.WF.Template.PubLib.ActionType;
import BP.WF.Template.PubLib.TodolistModel;

public class WorkCheckM extends BaseModel{
	 
	
	public StringBuffer Pub1 = null;
	
	
	//#region 属性
    public boolean getIsHidden()
    {
            try
            {
                if (getDoType().equals("View"))
                    return true;
                return Boolean.parseBoolean(this.get_request().getParameter("IsHidden"));
            }
            catch (Exception e)
            {
                return false;
            }
    }
    public int getNodeID()
    {
            try
            {
                return Integer.parseInt(this.get_request().getParameter("FK_Node"));
            }
            catch(Exception e)
            {
                return 0;
            }
    }

    /// <summary>
    /// 工作ID
    /// </summary>
//    public int getWorkID()
//    {
//    	String workid = this.get_request().getParameter("OID");
//    	if (workid == null)
//    		workid = this.get_request().getParameter("WorkID");
//    	return Integer.parseInt(workid);
//    }
//    
//    public int getFID()
//    {
//    	String workid = this.get_request().getParameter("FID");
//    	if (StringHelper.isNullOrEmpty(workid) == true)
//    		return 0;
//    	return Integer.parseInt(workid);
//    }

    /// <summary>
    /// 流程编号
    /// </summary>
    public String getFK_Flow()
    {
    	return this.get_request().getParameter("FK_Flow");
    }

    /// <summary>
    /// 操作View
    /// </summary>
    public String getDoType()
    {
            return this.get_request().getParameter("DoType");
    }
    /// <summary>
    /// 是否是抄送.
    /// </summary>
//    public boolean getIsCC()
//    {
//    	String s = this.get_request().getParameter("Paras");
//    	if (s == null)
//    		return false;
//
//    	if (s.contains("IsCC") == true)
//    		return true;
//    	return false;
//    }

	
	public WorkCheckM(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		Pub1 = new StringBuffer();
	}


     public void Page_Load()
     {
         //工作流编号不存在绑定空框架.
         if (StringHelper.isNullOrEmpty(this.getFK_Flow()))
         {
             ViewEmptyForm();
             return;
         }

         //审批节点.
         FrmWorkCheck wcDesc = new FrmWorkCheck(getNodeID());
//         if (wcDesc.getHisFrmWorkShowModel() == FrmWorkShowModel.Free)
//             this.BindFreeModel(wcDesc);
//         else
//             this.BindFreeModel(wcDesc);
//         
         if (wcDesc.getHisFrmWorkShowModel() == FrmWorkShowModel.Free)
             this.BindFreeModel(wcDesc);
         else if (wcDesc.getHisFrmWorkShowModel() == FrmWorkShowModel.Table)
             this.BindFreeModel(wcDesc);
         else if (wcDesc.getHisFrmWorkShowModel() == FrmWorkShowModel.Sign)
             this.BindSignModel(wcDesc);
     }

     /// <summary>
     /// 实现的功能：
     /// 1，显示轨迹表。
     /// 2，如果启用了审核，就把审核信息显示出来。
     /// 3，如果启用了抄送，就把抄送的人显示出来。
     /// 4，可以把子流程的信息与处理的结果显示出来。
     /// 5，可以把子线程的信息列出来。
     /// 6，可以把未来到达节点处理人显示出来。
     /// </summary>
     /// <param name="wcDesc"></param>
     public void BindFreeModel(FrmWorkCheck wcDesc)
     {
         WorkCheck wc = null;
         if (getFID() != 0)
             wc = new WorkCheck(this.getFK_Flow(), this.getNodeID(), this.getFID(), 0);
         else
             wc = new WorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID());

         boolean isCanDo = BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(), this.getNodeID(), this.getWorkID(),
             WebUser.getNo());


         //#region 输出历史审核信息.
         if (wcDesc.getFWCListEnable() == true)
         {
             //求轨迹表.
             Tracks tks = wc.getHisWorkChecks();

             //求抄送列表,把抄送的信息与抄送的读取状态显示出来.
             CCLists ccls = new CCLists(this.getFK_Flow(), this.getWorkID(), this.getFID());

             //查询出来未来节点处理人信息,以方便显示未来没有运动到节点轨迹.
             long wfid = this.getWorkID();
             if (this.getFID() != 0)
                 wfid = this.getFID();

             //获得 节点处理人数据。
             SelectAccpers accepts = new SelectAccpers(wfid);

             //取出来该流程的所有的节点。
             Nodes nds = new Nodes(this.getFK_Flow());
             Nodes ndsOrder = new Nodes();
             //求出已经出现的步骤.
             String nodes = ""; //已经出现的步骤.
             for(Track tk:Tracks.convertTracks(tks))
             {
                 switch (tk.getHisActionType())
                 {
                     //case ActionType.Forward:
                     case WorkCheck:
                         if (nodes.contains(tk.getNDFrom() + ",") == false)
                         {
                             nodes += tk.getNDFrom() + ",";
                         }
                         break;
                     case StartChildenFlow:
                         if (nodes.contains(tk.getNDFrom() + ",") == false)
                         {
                             nodes += tk.getNDFrom() + ",";
                         }
                         break;
                     default:
                         continue;
                 }
             }

             int biaoji = 0;
             for(Node nd:Nodes.convertNodes(nds))
             {
            	
                 if (nodes.contains(nd.getNodeID() + ",") == true)
                 {
                     //输出发送审核信息与抄送信息.
                     String emps = "";
                     String empsorder = "";    //保存队列显示中的人员，做判断，避免重复显示
                     String empcheck = "";   //记录当前节点已经输出的
                     for(Track tk:Tracks.convertTracks(tks))
                     {
						if (tk.getNDFrom() != nd.getNodeID())
                             continue;

                         //#region 如果是前进，并且当前节点没有启用审核组件
                         if (tk.getHisActionType() == ActionType.Forward)
                         {
                             continue;
//                             FrmWorkCheck fwc = new FrmWorkCheck(nd.getNodeID());
//                             if (fwc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Disable)
//                             {
//                                 this.Pub1.append(AddBR());
//                                 this.Pub1.append(tk.getMsgHtml());
//                                 this.Pub1.append(AddBR());
//
//                                 this.Pub1.append("<div style='float:right' >");
//                               //  this.Pub1.Add("<img src='../Img/Mail_Read.png' border=0 />" + tk.ActionTypeText);
//                                 if (wcDesc.getSigantureEnabel() == true)
//                                     this.Pub1.append(BP.WF.Glo.GenerUserSigantureHtml(ContextHolderUtils.getRequest().getRealPath("/"), tk.getEmpFrom(), tk.getEmpFromT()));
//                                 else
//                                     this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(tk.getEmpFrom(), tk.getEmpFromT()));
//                                 this.Pub1.append("</div>");
//                                 this.Pub1.append(AddHR());
//                                 continue;
//                             }
                         }

                         if (tk.getHisActionType() != ActionType.WorkCheck
                             && tk.getHisActionType() != ActionType.StartChildenFlow)
                             continue;

                         emps += tk.getEmpFrom() + ",";

                         if (tk.getHisActionType() == ActionType.WorkCheck)
                         {
                             //#region 显示出来队列流程中未审核的那些人.
                             if (nd.getTodolistModel() == TodolistModel.Order)
                             {
                                 /* 如果是队列流程就要显示出来未审核的那些人.*/
                                 String empsNodeOrder = "";  //记录当前节点队列访问未执行的人员

                                 GenerWorkerLists gwls = new GenerWorkerLists(this.getWorkID());
                                 for(GenerWorkerList item:GenerWorkerLists.convertGenerWorkerLists(gwls))
                                 {
                                     if (item.getFK_Node() == nd.getNodeID())
                                     {
                                         empsNodeOrder += item.getFK_Emp();
                                     }
                                 }

                                 for(SelectAccper accper:SelectAccpers.convertSelectAccpers(accepts))
                                 {
                                     if (empsorder.contains(accper.getFK_Emp()) == true)
                                         continue;
                                     if (empsNodeOrder.contains(accper.getFK_Emp()) == false)
                                         continue;
                                     if (tk.getEmpFrom().equals(accper.getFK_Emp()))
                                     {
                                         /* 审核信息,首先输出它.*/

                                         this.Pub1.append(tk.getMsgHtml());


                                         this.Pub1.append("<img src='../Img/Mail_Read.png' border=0/>" + tk.getActionTypeText());
                                         this.Pub1.append(tk.getRDT());
                                         this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(tk.getEmpFrom(), tk.getEmpFromT()));

                                         this.Pub1.append(AddHR());
                                         empcheck += tk.getEmpFrom();
                                     }
                                     else
                                     {
                                         this.Pub1.append(AddTR());
                                         if (accper.getAccType() == 0)
                                             this.Pub1.append(" <font style='color:Red;' >执行</font>");
                                         else
                                             this.Pub1.append(" <font style='color:Red;' >抄送</font>");
                                         this.Pub1.append("无");
                                         this.Pub1.append(" <font style='color:Red;' >"+ BP.WF.Glo.GenerUserImgSmallerHtml(accper.getFK_Emp(), accper.getEmpName())+"</font>");
                                         this.Pub1.append(" <font style='color:Red;' >" + accper.getInfo() + "</font>");
                                         this.Pub1.append(AddHR());
                                         empsorder += accper.getFK_Emp();
                                     }
                                 }
                             }
                             //#endregion 显示出来队列流程中未审核的那些人.
                             else
                             {
                                 /*审核信息,首先输出它.*/

                                 this.Pub1.append("<b>" + nd.getFWCNodeName() + "</b> <br><br>");

                                 this.Pub1.append(tk.getMsgHtml());


                                // this.Pub1.Add("<img src='../Img/Mail_Read.png' border=0/>" + tk.ActionTypeText);

                                 this.Pub1.append(AddBR());
                                 this.Pub1.append(AddBR());

                                 //if (wcDesc.SigantureEnabel == true)
                                 //    this.Pub1.Add(BP.WF.Glo.GenerUserSigantureHtml(Server.MapPath("../../"), tk.EmpFrom, tk.EmpFromT));
                                 //else
                                 //    this.Pub1.Add(BP.WF.Glo.GenerUserImgSmallerHtml(tk.EmpFrom, tk.EmpFromT));

                                 //this.Pub1.Add( "<div style='float:right'>审核人:"+tk.EmpFrom+ ","+ tk.EmpFromT+" 日期:"+tk.RDT+"</div>");

                                 this.Pub1.append("<div style='float:right'>审核人:" + tk.getEmpFrom() + "," + tk.getEmpFromT() + " 日期:" + tk.getRDT() + "</div>");

                                 //this.Pub1.Add(tk.RDT);
                                  this.Pub1.append(AddHR());
                                 empcheck += tk.getEmpFrom();
                             }
                         }

                         //#region 检查是否有调用子流程的情况。如果有就输出调用子流程信息. (手机部分的翻译暂时不考虑).
                         // int atTmp = (int)ActionType.StartChildenFlow;
                         WorkCheck wc2 = new WorkCheck(getFK_Flow(), tk.getNDFrom(), tk.getWorkID(), tk.getFID());
                         if (wc2.FID != 0)
                         {
                             //Tracks ztks = wc2.HisWorkChecks;    //重复循环！
                             //foreach (BP.WF.Track subTK in ztks)
                             //{
                             if (tk.getHisActionType() == ActionType.StartChildenFlow)
                             {
                                 /*说明有子流程*/
                                 /*如果是调用子流程,就要从参数里获取到都是调用了那个子流程，并把他们显示出来.*/
                                 String[] paras = tk.getTag().split("@");
                                 String[] p1 = paras[1].split("=");
                                 String fk_flow = p1[1]; //子流程编号

                                 String[] p2 = paras[2].split("=");
                                 String workId = p2[1]; //子流程ID.

                                 WorkCheck subwc = new WorkCheck(fk_flow, Integer.parseInt(fk_flow + "01"), Long.parseLong(workId), 0);

                                 Tracks subtks = subwc.getHisWorkChecks();

                                 //取出来子流程的所有的节点。
                                 Nodes subNds = new Nodes(fk_flow);
                                 for(Node item : Nodes.convertNodes(subNds))     //主要按顺序显示
                                 {
                                     for(Track mysubtk:Tracks.convertTracks(subtks))
                                     {
                                         if (item.getNodeID() != mysubtk.getNDFrom())
                                             continue;
                                         /*输出该子流程的审核信息，应该考虑子流程的子流程信息, 就不考虑那样复杂了.*/
                                         if (mysubtk.getHisActionType() == ActionType.WorkCheck)
                                         {
                                             //biaojie  发起多个子流程时，发起人只显示一次
                                             if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01") && biaoji == 1)
                                                 continue;

                                             /*如果是审核.*/
                                             this.Pub1.append(mysubtk.getActionTypeText() + "<img src='../Img/Mail_Read.png' border=0/>");
                                             this.Pub1.append(mysubtk.getRDT());
                                             this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(mysubtk.getEmpFrom(), mysubtk.getEmpFromT()));

                                             this.Pub1.append(AddBR()); 
                                             this.Pub1.append(mysubtk.getMsgHtml());
                                             this.Pub1.append(AddBR()); 
                                             if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01"))
                                             {
                                                 biaoji = 1;
                                             }
                                         }
                                     }
                                 }

                             }
                             //}
                         }
                         //#endregion 检查是否有调用子流程的情况。如果有就输出调用子流程信息.
                     }

                     for(SelectAccper item:SelectAccpers.convertSelectAccpers(accepts))
                     {
                         if (item.getFK_Node() != nd.getNodeID())
                             continue;
                         if (empcheck.contains(item.getFK_Emp()) == true)
                             continue;
                         if (item.getAccType() == 0)
                             continue;
                         if (ccls.IsExits(CCListAttr.FK_Node, nd.getNodeID()) == true)
                             continue;


                         this.Pub1.append("<font style='color:Red;'>执行</font>");

                         this.Pub1.append("<font style='color:Red;'>无</font>");

                         this.Pub1.append("<font style='color:Red;'>"+BP.WF.Glo.GenerUserImgSmallerHtml(item.getFK_Emp(), item.getEmpName())+"</font>");
                         this.Pub1.append(AddBR(item.getInfo()));
                         this.Pub1.append(AddHR());
                     }

                     //#region 输出抄送
                     for(SelectAccper it:SelectAccpers.convertSelectAccpers(accepts))
                     {
                    	 SelectAccper item=(SelectAccper)it;
                         if (item.getFK_Node() != nd.getNodeID())
                             continue;
                         if (item.getAccType() != 1)
                             continue;
                         if (ccls.IsExits(CCListAttr.FK_Node, nd.getNodeID()) == false)
                         {
                             this.Pub1.append("<font style='color:Red;'> 抄送</font>");
                             this.Pub1.append("<font style='color:Red;'> 无</font>");
                             // 显示要执行的人员。
                             this.Pub1.append("<font style='color:Red;'> "+BP.WF.Glo.GenerUserImgSmallerHtml(item.getFK_Emp(), item.getEmpName())+"</font>");

                             //info.
                             this.Pub1.append("<font style='color:Red;'>"+ item.getInfo()+"</font>");
                             this.Pub1.append(AddHR());
                             	
                         }
                         else
                         {
                             for(CCList cc:CCLists.convertCCLists(ccls))
                             {
                                 if (cc.getFK_Node() != nd.getNodeID())
                                     continue;

                                 if (cc.getHisSta() == CCSta.CheckOver)
                                     continue;
                                 if (!cc.getCCTo().equals(item.getFK_Emp()))
                                     continue;

                                 this.Pub1.append(AddTR());
                                 if (cc.getHisSta() == CCSta.Read)
                                 {
                                     if (nd.getIsEndNode() == true)
                                     {
                                         this.Pub1.append("<img src='../Img/Mail_Read.png' border=0/>抄送已阅");

                                         this.Pub1.append(cc.getCDT()); //读取时间.
                                         this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(cc.getCCTo(), cc.getCCToName()));
                                         this.Pub1.append(AddBR());
                                         this.Pub1.append(cc.getCheckNoteHtml());
                                     }
                                     else
                                     {
                                         continue;
                                     }
                                 }
                                 else
                                 {
                                     if (WebUser.getNo().equals(cc.getCCTo()))
                                     {
                                         continue;

                                         /*如果打开的是我,*/
//                                         if (cc.getHisSta() == CCSta.UnRead)
//                                             BP.WF.Dev2Interface.Node_CC_SetRead(cc.getMyPK());
//                                         this.Pub1.append("<img src='../Img/Mail_Read.png' border=0/>正在查阅");

                                     }
                                     else
                                     {
                                         this.Pub1.append("<img src='../Img/Mail_UnRead.png' border=0/>抄送未阅");
                                     }

                                     this.Pub1.append("无");
                                     this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(cc.getCCTo(), cc.getCCToName()));
                                     this.Pub1.append("无");
                                 }
                                 this.Pub1.append(AddHR());
                             }
                         }
                     }
                    
                 }
                 else
                 {
                     if (wcDesc.getFWCIsShowAllStep() == false)
                         continue;

                     /*判断该节点下是否有人访问，或者已经设置了抄送与接收人对象, 如果没有就不输出*/
                     if (accepts.IsExits(SelectAccperAttr.FK_Node, nd.getNodeID()) == false)
                         continue;

                     
                     /*未出现的节点.*/
                     this.Pub1.append(nd.getName());

                     //是否输出了.
                     boolean isHaveIt = false;
                     for(SelectAccper item:SelectAccpers.convertSelectAccpers(accepts))
                     {
                         if (item.getFK_Node() != nd.getNodeID())
                             continue;
                         if (item.getAccType() != 0)
                             continue;

                         this.Pub1.append("<font style='color:Red;'>执行</font>");
                         this.Pub1.append("<font style='color:Red;'>无</font>");

                         // 显示要执行的人员。
                         this.Pub1.append("<font style='color:Red;'>"+BP.WF.Glo.GenerUserImgSmallerHtml(item.getFK_Emp(), item.getEmpName())+"</font>");

                         //info.
                         this.Pub1.append("<font style='color:Red;'>" + item.getInfo() + "</font>");
                         this.Pub1.append(AddHR());
                         isHaveIt = true;
                     }

                     //#region 输出抄送
                     for(SelectAccper item:SelectAccpers.convertSelectAccpers(accepts))
                     {
                         if (item.getFK_Node() != nd.getNodeID())
                             continue;
                         if (item.getAccType() != 1)
                             continue;
                         if (ccls.IsExits(CCListAttr.FK_Node, nd.getNodeID()) == false)
                         {
                             this.Pub1.append("<font style='color:Red;'>抄送</font>");
                             this.Pub1.append("<font style='color:Red;'>无</font>");
                             // 显示要执行的人员。
                             this.Pub1.append("<font style='color:Red;'>"+BP.WF.Glo.GenerUserImgSmallerHtml(item.getFK_Emp(), item.getEmpName())+"</font>");

                             //info.
                             this.Pub1.append("<font style='color:Red;'>"+item.getInfo()+"</font>");
                             this.Pub1.append(AddHR());
                             isHaveIt = true;
                         }
                         else
                         {
                             for(CCList cc :CCLists.convertCCLists(ccls))
                             {
                                 if (cc.getFK_Node() != nd.getNodeID())
                                     continue;

                                 if (cc.getHisSta() == CCSta.CheckOver)
                                     continue;
                                 if (!cc.getCCTo().equals(item.getFK_Emp()))
                                     continue;

                                 this.Pub1.append(AddTR());
                                 if (cc.getHisSta() == CCSta.Read)
                                 {
                                     if (nd.getIsEndNode() == true)
                                     {
                                         this.Pub1.append("<img src='../Img/Mail_Read.png' border=0/>抄送已阅");
                                         this.Pub1.append(cc.getCDT()); //读取时间.
                                         this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(cc.getCCTo(), cc.getCCToName()));
                                         this.Pub1.append(cc.getCheckNoteHtml());
                                     }
                                     else
                                     {
                                         continue;
                                     }
                                 }
                                 else
                                 {
                                     if (WebUser.getNo().equals(cc.getCCTo()))
                                     {
                                         continue;

                                         /*如果打开的是我,*/
//                                         if (cc.getHisSta() == CCSta.UnRead)
//                                             BP.WF.Dev2Interface.Node_CC_SetRead(cc.getMyPK());
//                                         this.Pub1.append("<img src='../Img/Mail_Read.png' border=0/>正在查阅");
                                     }
                                     else
                                     {
                                         this.Pub1.append("<img src='../Img/Mail_UnRead.png' border=0/>抄送未阅");
                                     }

                                     this.Pub1.append("无");
                                     this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(cc.getCCTo(), cc.getCCToName()));
                                     this.Pub1.append("无");
                                 }
                                 this.Pub1.append(AddHR());
                             }
                         }
                     }
                      
                 }
             }
         } // 输出轨迹.

         if (getIsHidden() == false 
             && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable 
             && isCanDo)
         {
             this.Pub1.append(AddTable("border=0 style='padding:0px;width:100%;' leftMargin=0 topMargin=0"));
             this.Pub1.append(AddTR());

             String lab = wcDesc.getFWCOpLabel();
             lab = lab.replace("@WebUser.No", WebUser.getNo());
             lab = lab.replace("@WebUser.Name", WebUser.getName());
             lab = lab.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());

             //this.Pub1.append(AddTD("<div style='float:left'>" + wcDesc.getFWCOpLabel() + "</div><div style='float:right'><a href=javascript:TBHelp('TB_Doc','ND" + getNodeID() + "')" + "><img src='" + BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Emps.gif' align='middle' border=0 />选择词汇</a>&nbsp;&nbsp;</div>"));
            
             this.Pub1.append(AddTREnd());

             TextBox tb = new TextBox();
             tb.setId("TB_Doc");
             tb.setTextMode(TextBoxMode.MultiLine);
             tb.addAttr("onblur", "btn_Click()");
//             tb.OnBlur += new EventHandler(btn_Click);

             tb.addAttr("style", "width:100%");
             tb.setRows(3);
             if (!StringHelper.isNullOrEmpty(getDoType()) && getDoType().equals("View"))
             {
                 tb.setReadOnly(true);
             }

             tb.setText(BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID()));

             if ("同意".equals(tb.getText()))
                 tb.setText("");

             if (StringHelper.isNullOrEmpty(tb.getText()))
             {
                 tb.setText(wcDesc.getFWCDefInfo());

                 // 以下手机端都不要去处理
                 if ("1".equals(this.getIsCC()))
                 {
                     /*如果当前工作是抄送. */
                     BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), tb.getText(), "抄送");

                     //设置当前已经审核完成.
                     BP.WF.Dev2Interface.Node_CC_SetSta(this.getNodeID(), this.getWorkID(), WebUser.getNo(), CCSta.CheckOver);

                 }
                 else
                 {
                     if (wcDesc.getFWCIsFullInfo() == true)
                         BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), tb.getText(), wcDesc.getFWCOpLabel());
                 }
                 // 以上手机端都不要去处理.

             }
             this.Pub1.append(AddTR());
             this.Pub1.append(AddTD(tb));
             this.Pub1.append(AddTREnd());
             this.Pub1.append(AddTableEnd());
         }





     }
     public void BindFreeModelV1_del(FrmWorkCheck wcDesc)
     {
    	 SimpleDateFormat sdf=new SimpleDateFormat("yy年MM月dd日HH时mm分");
         WorkCheck wc = new WorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID());
         this.Pub1.append(AddTable("border=0 style='padding:0px;width:100%;' leftMargin=0 topMargin=0"));
         if (getIsHidden() == false && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable)
         {
             this.Pub1.append(AddTR());
             this.Pub1.append(AddTD("<div style='float:right'><img src='/WF/Img/Btn/Save.gif' border=0 />保存</div>"));
             this.Pub1.append(AddTREnd());

             TextBox tb = new TextBox();
             tb.setId("TB_Doc");
             tb.setTextMode(TextBoxMode.MultiLine);
             //tb.OnBlur += new EventHandler(btn_Click);
             tb.addAttr("onblur", "btn_Click");
             tb.addAttr("width","100%");
             tb.setRows(3);
             if (!StringHelper.isNullOrEmpty(getDoType()) && getDoType().equals("View"))
                 tb.setReadOnly(true);
             tb.setText(BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID()));
             if (StringHelper.isNullOrEmpty(tb.getText()))
             {
                 tb.setText(wcDesc.getFWCDefInfo());
                 BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), tb.getText(), wcDesc.getFWCOpLabel());
             }

             this.Pub1.append(AddTR());
             this.Pub1.append(AddTD(tb));
             this.Pub1.append(AddTREnd());
         }

         if (wcDesc.getFWCListEnable() == false)
         {
             this.Pub1.append(AddTableEnd());
             return;
         }

         int i = 0;
         Tracks tks = wc.getHisWorkChecks();
         for (Track tk:Tracks.convertTracks(tks))
         {
             //#region 输出审核.
             if (tk.getHisActionType() == ActionType.WorkCheck)
             {
                 /*如果是审核.*/
                 i++;
                 ActionType at = tk.getHisActionType();
                 Date dtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());

                 this.Pub1.append(AddTR());
                 this.Pub1.append(AddTDBegin());
                 this.Pub1.append(AddB(tk.getNDFromT()));
                 this.Pub1.append(AddBR(tk.getMsgHtml()));
                 this.Pub1.append(AddBR("<div style='float:right'>" + BP.WF.Glo.GenerUserImgSmallerHtml(tk.getEmpFrom(), tk.getEmpFromT()) + " &nbsp;&nbsp;&nbsp; " + sdf.format(dtt) + "</div>"));
                 this.Pub1.append(AddTDEnd());
                 this.Pub1.append(AddTREnd());
             }
             //#endregion 输出审核.

             //#region 检查是否有子流程.
             if (tk.getHisActionType() == ActionType.StartChildenFlow)
             {
                 /*如果是调用子流程,就要从参数里获取到都是调用了那个子流程，并把他们显示出来.*/
                 String[] paras = tk.getTag().split("@");
                 String[] p1 = paras[1].split("=");
                 String fk_flow = p1[1];

                 String[] p2 = paras[2].split("=");
                 String workId = p2[1];

                 WorkCheck subwc = new WorkCheck(fk_flow, Integer.parseInt(fk_flow + "01"), Long.parseLong(workId), 0);
                 Tracks subtks = subwc.getHisWorkChecks();
                 for(Track subtk:Tracks.convertTracks(subtks))
                 {
                     if (subtk.getHisActionType() == ActionType.WorkCheck)
                     {
                         /*如果是审核.*/
                         i++;
                         ActionType at = subtk.getHisActionType();
                         Date dtt = BP.DA.DataType.ParseSysDateTime2DateTime(subtk.getRDT());

                         this.Pub1.append(AddTR());
                         this.Pub1.append(AddTDBegin());

                         this.Pub1.append(AddB(subtk.getNDFromT()));
                         this.Pub1.append(AddBR(subtk.getMsgHtml()));
                         this.Pub1.append(AddBR("<div style='float:right'>" + BP.WF.Glo.GenerUserImgSmallerHtml(subtk.getEmpFrom(), subtk.getEmpFromT()) + " &nbsp;&nbsp;&nbsp; " + sdf.format(dtt) + "</div>"));

                         this.Pub1.append(AddTDEnd());
                         this.Pub1.append(AddTREnd());

                     }
                 }
             }
             //#endregion 检查是否有子流程.

         }
         this.Pub1.append(AddTableEnd());
     }
     public void BindTableModel(FrmWorkCheck wcDesc)
     {
    	 SimpleDateFormat sdf=new SimpleDateFormat("yy年MM月dd日HH时mm分");
         WorkCheck wc = new WorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID());
         
         this.Pub1.append(AddTable("border=1 style='padding:0px;width:100%;'"));
         this.Pub1.append(AddTR());
         this.Pub1.append(AddTD("colspan=8", "<div style='float:left'>审批意见</div> <div style='float:right'><img src='../Img/Btn/Save.gif' border=0 /></div>"));
         this.Pub1.append(AddTREnd());

         if (!getIsHidden())
         {
             TextBox tb = new TextBox();
             tb.setId("TB_Doc");
             tb.setTextMode(TextBoxMode.MultiLine);
             //tb.OnBlur += new EventHandler(btn_Click);
             tb.addAttr("onblur", "btn_Click()");
             tb.addAttr("style", "width:100%");
             tb.setRows(3);
             if (!StringHelper.isNullOrEmpty(getDoType()) && getDoType() == "View") tb.setReadOnly(true);

             tb.setText(BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID()));

             this.Pub1.append(AddTD("colspan=8", tb));
             this.Pub1.append(AddTREnd());
         }

         this.Pub1.append(AddTR());
         this.Pub1.append(AddTD("IDX"));
         this.Pub1.append(AddTD("发生时间"));
         this.Pub1.append(AddTD("发生节点"));
         //   this.Pub1.AddTD("人员");
         this.Pub1.append(AddTD("活动"));
         this.Pub1.append(AddTD("信息/审批意见"));
         this.Pub1.append(AddTD("执行人"));
         this.Pub1.append(AddTREnd());

         int i = 0;
         Tracks tks = wc.getHisWorkChecks();
         for(Track tk:Tracks.convertTracks(tks))
         {
             if (tk.getHisActionType() == ActionType.Forward
                 || tk.getHisActionType() == ActionType.ForwardFL
                 || tk.getHisActionType() == ActionType.ForwardHL
                 )
             {
                 String nd = ""+tk.getNDFrom();
                 if (nd.substring(nd.length() - 2) != "01")
                     continue;
                 //string len=tk.NDFrom.ToString();
                 //if (
                 //if (tk.NDFrom.ToString().Contains
             }

             if (tk.getHisActionType() != ActionType.WorkCheck)
                 continue;

             i++;
             this.Pub1.append(AddTR());
             this.Pub1.append(AddTD(i));
             Date dtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());
             this.Pub1.append(AddTD(sdf.format(dtt)));
             this.Pub1.append(AddTD(tk.getNDFromT()));
             //  this.Pub1.AddTD(tk.EmpFromT);
             ActionType at = tk.getHisActionType();
             //this.Pub1.AddTD("<img src='./../Img/Action/" + at.ToString() + ".png' class='ActionType' width='16px' border=0/>" + BP.WF.Track.GetActionTypeT(at));
             this.Pub1.append(AddTD("<img src='./../Img/Action/" + at.toString() + ".png' class='ActionType' width='16px' border=0/>" + tk.getActionTypeText()));
             this.Pub1.append(AddTD(tk.getMsgHtml()));
             this.Pub1.append(AddTD(tk.getExer())); //如果是委托，增加一个  人员(委托))
             this.Pub1.append(AddTREnd());
         }
         this.Pub1.append(AddTableEnd());
     }

     //展示空表单
     private void ViewEmptyForm()
     {
         this.Pub1.append(AddTable(" border=1 style='padding:0px;width:100%;'"));
         this.Pub1.append(AddTR());
         this.Pub1.append(AddTD("colspan=6 style='text-align:left' ", "审批意见"));
         this.Pub1.append(AddTREnd());

         this.Pub1.append(AddTR());
         this.Pub1.append(AddTD("IDX"));
         this.Pub1.append(AddTD("发生时间"));
         this.Pub1.append(AddTD("发生节点"));
         this.Pub1.append(AddTD("活动"));
         this.Pub1.append(AddTD("信息/审批意见"));
         this.Pub1.append(AddTD("执行人"));
         this.Pub1.append(AddTREnd());

         this.Pub1.append(AddTableEnd());
     }

     
public final void BindSignModel(FrmWorkCheck wcDesc)
{

	Node currentNode = new Node(this.getNodeID());

	this.Pub1.append(AddTable(" style='border:0px solie white'  cellspacing='0' cellpadding='0' "));
	if (!getIsHidden() && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable)
	{

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("colspan=2 style='font-weight:bold;'", currentNode.getName() + ":"));

		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTR());
		TextBox tb = new TextBox();
		tb.setId("TB_Doc");
		tb.setTextMode(TextBoxMode.MultiLine);
		tb.attributes.put("onblur", "width:100%");
		tb.setRows(3);
		if (getDoType() != null && getDoType().equals("View"))
		{
			tb.setReadOnly(true);
		}

		String text = null;
		if (currentNode.getTodolistModel() == TodolistModel.Order)
		{
			text = Dev2Interface.GetOrderCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID());
		}
		else
		{
			text = BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID());
		}
		tb.setText(StringHelper.isNullOrEmpty(text) ? "同意" : text);

		if (currentNode.getTodolistModel() == TodolistModel.Order)
		{
			InsertDefault(tb.getText());
		}
		else
		{
			if (StringHelper.isNullOrEmpty(text))
			{
				InsertDefault("同意");
			}
		}

		this.Pub1.append(AddTD("colspan=2", tb));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		String v = WebUser.getNo();

		this.Pub1.append(AddTD("<img src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/" + v + ".jpg' border=0 onerror=\"this.src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/UnName.jpg'\"/>"));
		this.Pub1.append(AddTD(DataType.getCurrentDateByFormart("yyyy-MM-dd")));

		this.Pub1.append(AddTREnd());
	}

	int i = 0;
	WorkCheck wc = new WorkCheck(getFK_Flow(), getNodeID(), getWorkID(), getFID());
	Tracks tks = wc.getHisWorkChecks();
	java.util.ArrayList<Track> list = new java.util.ArrayList<Track>();

	boolean isFirst = false;
	for (Track tk : Tracks.convertTracks(tks))
	{
		if (tk.getHisActionType() == ActionType.Forward || tk.getHisActionType() == ActionType.ForwardFL || tk.getHisActionType() == ActionType.ForwardHL)
		{
			String nd = String.valueOf(tk.getNDFrom());
			if ( ! nd.substring(nd.length() - 2).equals("01"))
			{
				continue;
			}
			//string len=tk.NDFrom.ToString();
			//if (
			//if (tk.NDFrom.ToString().Contains
		}

		if (tk.getHisActionType() != ActionType.WorkCheck)
		{
			continue;
		}

		Node node = new Node(tk.getNDFrom());
		if (!getIsHidden() && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable && node.getNodeID() == this.getNodeID())
		{
			if (i == 0)
			{

				if (node.getTodolistModel() == TodolistModel.Order)
				{
					for (Track singleTk : Tracks.convertTracks(tks))
					{
						if (singleTk.getHisActionType() != ActionType.WorkCheck)
						{
							continue;
						}

						if (singleTk.getNDFrom() == tk.getNDFrom() && singleTk.getExer() != tk.getExer())
						{
							this.Pub1.append(AddTR());

							java.util.Date singleDtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());

							this.Pub1.append(AddTD("<img src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/" + singleTk.getEmpFrom() + ".jpg' border=0 onerror=\"this.src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/UnName.jpg'\"/>")); //如果是委托，增加一个 人员(委托)

							this.Pub1.append(AddTD(DataType.getDateByFormart(singleDtt, "yyyy-MM-dd")));

							this.Pub1.append(AddTREnd());
							if (!list.contains(singleTk))
							{
								list.add(singleTk);
							}
						}

					}
					i++;
					isFirst = true;
					continue;
				}
				i++;
				isFirst = false;
				continue;
			}
		}

		if (list.contains(tk))
		{
			continue;
		}
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("colspan=2 style='font-weight:bold;'", node.getName() + ":"));

		this.Pub1.append(AddTREnd());

		if (node.getTodolistModel() == TodolistModel.Order)
		{

			if (!isFirst)
			{
				this.Pub1.append(AddTR());

				this.Pub1.append(AddTD("colspan=2 style='height:50px'", tk.getMsgHtml()));
				this.Pub1.append(AddTREnd());

				this.Pub1.append(AddTR());

				java.util.Date singleDtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());

				this.Pub1.append(AddTD("<img src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/" + tk.getEmpFrom() + ".jpg' border=0 onerror=\"this.src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/UnName.jpg'\"/>")); //如果是委托，增加一个 人员(委托)

				this.Pub1.append(AddTD(DataType.getDateByFormart(singleDtt, "yyyy-MM-dd")));

				this.Pub1.append(AddTREnd());
				if (!list.contains(tk))
				{
					list.add(tk);
				}
			}

			for (Track singleTk : Tracks.convertTracks(tks))
			{
				if (singleTk.getHisActionType() != ActionType.WorkCheck)
				{
					continue;
				}

				if (singleTk.getNDFrom() == tk.getNDFrom() && !singleTk.getExer().equals(tk.getExer()))
				{
					this.Pub1.append(AddTR());

					java.util.Date singleDtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());

					this.Pub1.append(AddTD("<img src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/" + singleTk.getEmpFrom() + ".jpg' border=0 onerror=\"this.src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/UnName.jpg'\"/>")); //如果是委托，增加一个 人员(委托)

					this.Pub1.append(AddTD(DataType.getDateByFormart(singleDtt, "yyyy-MM-dd")));

					this.Pub1.append(AddTREnd());
					if (!list.contains(singleTk))
					{
						list.add(singleTk);
					}
				}

			}
			i++;
			continue;
		}
		else
		{
			this.Pub1.append(AddTR());

			this.Pub1.append(AddTD("colspan=2 style='height:50px'", tk.getMsgHtml()));
			this.Pub1.append(AddTREnd());
		}


		this.Pub1.append(AddTR());

		java.util.Date dtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());

		this.Pub1.append(AddTD("<img src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/" + tk.getEmpFrom() + ".jpg' border=0 onerror=\"this.src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/UnName.jpg'\"/>")); //如果是委托，增加一个 人员(委托)

		this.Pub1.append(AddTD(DataType.getDateByFormart(dtt, "yyyy-MM-dd")));

		this.Pub1.append(AddTREnd());
		i++;
	}
	this.Pub1.append(AddTableEnd());
	}

private void InsertDefault(String msg)
{
	//查看时取消保存
	if (getDoType() != null && getDoType().equals("View"))
	{
		return;
	}

	//内容为空，取消保存
	if (StringHelper.isNullOrEmpty(msg))
	{
		return;
	}
	// 加入审核信息.


	FrmWorkCheck wcDesc = new FrmWorkCheck(this.getNodeID());

	// 处理人大的需求，需要把审核意见写入到FlowNote里面去.
	String sql = "UPDATE WF_GenerWorkFlow SET FlowNote='" + msg + "' WHERE WorkID=" + this.getWorkID();
	BP.DA.DBAccess.RunSQL(sql);



	// 判断是否是抄送?
	if (this.getIsCC().equals("1"))
	{
		// 写入审核信息，有可能是update数据。
		BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), msg, wcDesc.getFWCOpLabel());

		//设置抄送状态 - 已经审核完毕.
		BP.WF.Dev2Interface.Node_CC_SetSta(this.getNodeID(), this.getWorkID(), WebUser.getNo(), CCSta.CheckOver);
	}
	else
	{
		BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), msg, wcDesc.getFWCOpLabel());
	}

}

}


// //自定义控件
// public class PostBackTextBox : System.Web.UI.WebControls.TextBox, System.Web.UI.IPostBackEventHandler
// {
//     protected override void Render(System.Web.UI.HtmlTextWriter writer)
//     {
//         Attributes["onblur"] = Page.GetPostBackEventReference(this);
//         base.Render(writer);
//     }
//
//     public event EventHandler OnBlur;
//
//     public virtual void RaisePostBackEvent(string eventArgument)
//     {
//         if (OnBlur != null)
//         {
//             OnBlur(this, null);
//         }
//     }
//}
