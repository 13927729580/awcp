package org.jflow.framework.designer.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.common.model.BaseModel;
import org.jflow.framework.system.ui.UiFatory;
import org.jflow.framework.system.ui.core.DDL;
import org.jflow.framework.system.ui.core.LinkButton;
import org.jflow.framework.system.ui.core.NamesOfBtn;
import org.jflow.framework.system.ui.core.TextBox;

import BP.DA.DataType;
import BP.WF.Template.Condition.Cond;
import BP.WF.Template.Condition.CondType;
import BP.WF.Template.Condition.ConnDataFrom;

public class CondByUrlModel {
	private HttpServletRequest request;
	private HttpServletResponse response;
	public UiFatory ui=null;
	public CondByUrlModel()
	{
		
	}
	public CondByUrlModel(HttpServletRequest request,HttpServletResponse response)
	{
		this.request=request;
		this.response=response;
	}
	// #region 属性
     /// <summary>
     /// 主键
     /// </summary>
	private String MyPK;
     public String getMyPK() {
		return request.getParameter("MyPK");
	}
	public void setMyPK(String myPK) {
		MyPK = myPK;
	}
//	public new string MyPK
//     {
//         get
//         {
//             return this.Request.QueryString["MyPK"];
//         }
//     }
     /// <summary>
     /// 流程编号
     /// </summary>
	private String FK_Flow;
    public String getFK_Flow() {
		return request.getParameter("FK_Flow");
	}
	public void setFK_Flow(String fK_Flow) {
		FK_Flow = fK_Flow;
	}
     /// <summary>
     /// 节点
     /// </summary>
	private int FK_Node;
     public int getFK_Node() {
    	 try
         {
             return Integer.parseInt(request.getParameter("FK_Node"));//int.Parse(this.Request.QueryString["FK_Node"]);
         }
         catch(Exception e)
         {
             return getFK_MainNode();
         }
	}
	public void setFK_Node(int fK_Node) {
		FK_Node = fK_Node;
	}
//	public int FK_Node
//     {
//         get
//         {
//             try
//             {
//                 return int.Parse(this.Request.QueryString["FK_Node"]);
//             }
//             catch
//             {
//                 return this.FK_MainNode;
//             }
//         }
//     }
	private int FK_MainNode;
     public int getFK_MainNode() {
		return Integer.parseInt(request.getParameter("FK_MainNode"));
	}
	public void setFK_MainNode(int fK_MainNode) {
		FK_MainNode = fK_MainNode;
	}
//	public int FK_MainNode
//     {
//         get
//         {
//             return int.Parse(this.Request.QueryString["FK_MainNode"]);
//         }
//     }
	private int ToNodeID;
     public int getToNodeID() {
    	 try
         {
             return Integer.parseInt(request.getParameter("ToNodeID"));//int.Parse(this.Request.QueryString["ToNodeID"]);
         }
         catch(Exception e)
         {
             return 0;
         }
	}
	public void setToNodeID(int toNodeID) {
		ToNodeID = toNodeID;
	}
//	public int ToNodeID
//     {
//         get
//         {
//             try
//             {
//                 return int.Parse(this.Request.QueryString["ToNodeID"]);
//             }
//             catch
//             {
//                 return 0;
//             }
//         }
//     }
     /// <summary>
     /// 执行类型
     /// </summary>
	private int HisCondType;
     public int getHisCondType() {
		return Integer.parseInt(request.getParameter("CondType").trim());
	}
	public void setHisCondType(int hisCondType) {
		HisCondType = hisCondType;
	}
//	public CondType HisCondType
//     {
//         get
//         {
//             return (CondType)int.Parse(this.Request.QueryString["CondType"]);
//         }
//     }
     public String GetOperVal;
     public String getGetOperVal() {
    	 if(ui.GetUIByID("TB_Val")!=null)
    	 {
    		 return ((TextBox)ui.GetUIByID("TB_Val")).getText();
    	 }
		return ((DDL)ui.GetUIByID("DDL_Val")).getSelectedItemStringVal();
	}
	public void setGetOperVal(String getOperVal) {
		GetOperVal = getOperVal;
	}

//	{
//         get
//         {
//             if (this.IsExit("TB_Val"))
//                 return this.GetTBByID("TB_Val").Text;
//             return this.GetUIByID("DDL_Val").SelectedItemStringVal;
//         }
//     }
     public String GetOperValText;
     public String getGetOperValText() {
    	 if(ui.GetUIByID("TB_Val")!=null)
    	 {
    		 return ((TextBox)ui.GetUIByID("TB_Val")).getText();
    	 }
		return ((DDL)ui.GetUIByID("DDL_Val")).getSelectedItem().getText();
	}
	public void setGetOperValText(String getOperValText) {
		GetOperValText = getOperValText;
	}

//	{
//         get
//         {
//             if (this.IsExit("TB_Val"))
//                 return this.GetTBByID("TB_Val").Text;
//             return this.GetUIByID("DDL_Val").SelectedItem.Text;
//         }
//     }
     public String GenerMyPK;
     public String getGenerMyPK() {
    	 return this.getFK_MainNode() + "_" + getToNodeID() + "_" + CondType.forValue(getHisCondType()) + "_" + ConnDataFrom.Url;
	}
	public void setGenerMyPK(String generMyPK) {
		GenerMyPK = generMyPK;
	}

//	{
//         get
//         {
//             return this.FK_MainNode + "_" + this.ToNodeID + "_" + this.HisCondType.ToString() + "_" + ConnDataFrom.Url.ToString();
//         }
//     }
//     #endregion 属性

     public void Page_Load()
     {
         Cond cond = new Cond();
         cond.setMyPK(getGenerMyPK());
         cond.RetrieveFromDBSources();

         ui=new UiFatory();
         ui.append("<table class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'>");
         ui.append("<tr>");
         ui.append("<td class='GroupTitle'>设置一个URL，此Url返回一个数值，用与判断该方向是否通过</td>");
         ui.append("</tr>");

         ui.append("<tr>");

         TextBox tb = ui.creatTextBox("TB_Para");
         //tb.ID = "TB_Para";
         tb.setRows(1);
         tb.setCols(80);
         tb.addAttr("width", "99%");
         //tb.setStyle().Add("width", "99%");
         tb.setText(cond.getOperatorValue().toString());
         ui.append("<td>"+tb+"</td>");

         ui.append("</tr>");
         ui.append("</table>");

         ui.append("</br>");
         ui.append(DataType.GenerSpace(1));

         LinkButton btn = ui.creatLinkButton(false, NamesOfBtn.Save.getCode(), "保存");
         btn.setHref("btn_Click()");
         //btn.Click += new EventHandler(btn_Click);
         ui.append(btn);
         ui.append(DataType.GenerSpace(1));

         btn = ui.creatLinkButton(false, NamesOfBtn.Delete.getCode(), "删除");
         btn.setHref("return confirm('您确定要删除吗？');");
         //btn.Attributes["onclick"] = "return confirm('您确定要删除吗？');";
         //btn.Click += new EventHandler(btn_Click);
         btn.addAttr("onclick", "btn_del()");
         ui.append(btn);
         ui.append("<br/>");
         ui.append("<br/>");


         BaseModel bm=new BaseModel(request, response);
         ui.append(bm.AddEasyUiPanelInfo("帮助", "格式: http://xxxx/xxx.xxx?s=sss , 此url返回小于等于0是不通过，大于等于1是通过。系统在调用此url时会向它传入4大参数:FK_Flow,FK_Node,WorkID,FID您通过这四个参数,您可以获得当前流程数据。"));
         // 还有WebUser.No,SID两个校验参数,
     }

}