package org.jflow.framework.designer.model;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.common.model.BaseModel;
import org.jflow.framework.system.ui.core.CheckBox;
import org.jflow.framework.system.ui.core.DDL;
import org.jflow.framework.system.ui.core.Label;
import org.jflow.framework.system.ui.core.LinkButton;
import org.jflow.framework.system.ui.core.NamesOfBtn;
import org.jflow.framework.system.ui.core.TextBox;

import BP.Port.Dept;
import BP.Port.Depts;
import BP.WF.Template.Node;
import BP.WF.Template.Condition.Cond;
import BP.WF.Template.Condition.CondType;
import BP.WF.Template.Condition.ConnDataFrom;

public class CondDeptModel {
	public StringBuffer Pub1=new StringBuffer();
	public Map<String,Object> map=null;
	private HttpServletRequest request;
	private HttpServletResponse respone;
	public CondDeptModel()
	{
		
	}
	public CondDeptModel(HttpServletRequest request,HttpServletResponse respone)
	{
		this.request=request;
		this.respone=respone;
	}
	 //#region 属性
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
//	public string FK_Flow
//     {
//         get
//         {
//             return this.Request.QueryString["FK_Flow"];
//         }
//     }
	private int FK_Attr;
     public int getFK_Attr() {
    	 try {
    		 return Integer.parseInt(request.getParameter("FK_Attr"));
		} catch (Exception e) {
			try
            {
                return DDL_Attr.getSelectedItemIntVal();
            }
            catch(Exception e1)
            {
                return 0;
            }
		}
	}
	public void setFK_Attr(int fK_Attr) {
		FK_Attr = fK_Attr;
	}
//	public int FK_Attr
//     {
//         get
//         {
//             try
//             {
//                 return int.Parse(this.Request.QueryString["FK_Attr"]);
//             }
//             catch
//             {
//                 try
//                 {
//                     return this.DDL_Attr.SelectedItemIntVal;
//                 }
//                 catch
//                 {
//                     return 0;
//                 }
//             }
//         }
//     }
     /// <summary>
     /// 节点
     /// </summary>
	private int FK_Node;
     public int getFK_Node() {
		return Integer.parseInt(request.getParameter("FK_Node"));
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
    	 try {
    		 return Integer.parseInt(request.getParameter("ToNodeID"));
		} catch (Exception e) {
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
     public int HisCondType;
     public int getHisCondType() {
		return Integer.parseInt(request.getParameter("CondType").trim());
	}
	public void setHisCondType(int hisCondType) {
		HisCondType = hisCondType;
	}

//	{
//         get
//         {
//             return (CondType)int.Parse(this.Request.QueryString["CondType"]);
//         }
//     }
	private String GetOperValText;
     public String getGetOperValText() {
    	 if (map.get("TB_Val")!=null)
             return ((TextBox)map.get("TB_Val")).getText();
         return ((DDL)map.get("DDL_Val")).getSelectedItem().getText();
	}
	public void setGetOperValText(String getOperValText) {
		GetOperValText = getOperValText;
	}
//	public string GetOperValText
//     {
//         get
//         {
//             if (this.Pub1.IsExit("TB_Val"))
//                 return this.Pub1.GetTBByID("TB_Val").Text;
//             return this.Pub1.GetDDLByID("DDL_Val").SelectedItem.Text;
//         }
//     }
     //#endregion 属性

     public void Page_Load() throws IOException
     {
         if (request.getParameter("DoType") == "Del")
         {
             Cond nd = new Cond(this.getMyPK());
             nd.Delete();
             respone.sendRedirect("CondDept.jsp?CondType=" + getHisCondType() + "&FK_Flow=" + this.getFK_Flow() + "&FK_MainNode=" + nd.getNodeID() + "&FK_Node=" + this.getFK_MainNode() + "&ToNodeID=" + nd.getToNodeID());
             return;
         }

         this.BindCond();
     }

     public void BindCond()
     {
         Cond cond = new Cond();
         cond.setMyPK(getGenerMyPK());
         cond.RetrieveFromDBSources();

         Node nd = new Node(this.getFK_MainNode());
         Node tond = new Node(this.getToNodeID());

         this.Pub1.append(BaseModel.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
         this.Pub1.append(BaseModel.AddTR());
         this.Pub1.append(BaseModel.AddTD("colspan=3 class='GroupTitle'", "部门选择"));
         this.Pub1.append(BaseModel.AddTREnd());

         Depts sts = new Depts();
         sts.RetrieveAllFromDBSource();

         int i = 0;

         for (int j = 0; j < sts.size(); j++) {
        	 Dept st=(Dept)sts.get(j);
        	 i++;

             if (i == 4)
                 i = 1;

             if (i == 1)
                 Pub1.append(BaseModel.AddTR());

             CheckBox cb = new CheckBox();
             cb.setId("CB_" + st.getNo());
             cb.setText(st.getName());
             if (cond.getOperatorValue().toString().contains("@_" + st.getNo() + "@"))
                 cb.setChecked(true);

             //map.put("CB_" + st.getNo(), cb);
             this.Pub1.append(BaseModel.AddTD(cb));

             if (i == 3)
                 Pub1.append(BaseModel.AddTREnd());
		}
//         for (Dept st : sts)
//         {
//             i++;
//
//             if (i == 4)
//                 i = 1;
//
//             if (i == 1)
//                 Pub1.AddTR();
//
//             CheckBox cb = new CheckBox();
//             cb.ID = "CB_" + st.No;
//             cb.Text = st.Name;
//             if (cond.OperatorValue.ToString().Contains("@" + st.No + "@"))
//                 cb.Checked = true;
//
//             this.Pub1.AddTD(cb);
//
//             if (i == 3)
//                 Pub1.AddTREnd();
//         }

         switch (i)
         {
             case 1:
                 Pub1.append(BaseModel.AddTD());
                 Pub1.append(BaseModel.AddTD());
                 Pub1.append(BaseModel.AddTREnd());
                 break;
             case 2:
                 Pub1.append(BaseModel.AddTD());
                 Pub1.append(BaseModel.AddTREnd());
                 break;
             default:
                 break;
         }

         this.Pub1.append(BaseModel.AddTableEnd());
         Pub1.append(BaseModel.AddBR());
         Pub1.append(BaseModel.AddSpace(1));

         LinkButton btn = new LinkButton(false, NamesOfBtn.Save.getCode(), "保存");
         btn.setHref("onsave()");
         //btn.Click += new EventHandler(btn_Save_Click);
         this.Pub1.append(btn);
         Pub1.append(BaseModel.AddSpace(1));

         btn = new LinkButton(false, NamesOfBtn.Delete.getCode(), "删除");
         btn.addAttr("onclick", " return confirm('您确定要删除吗？');");
         //btn.Attributes["onclick"] = " return confirm('您确定要删除吗？');";
         btn.setHref("btn_Del_Click()");
        // btn.Click += new EventHandler(btn_Save_Click);
         this.Pub1.append(btn);
     }

     public Label Lab_Msg;
     public Label getLab_Msg() {
		return (Label) map.get("Lab_Msg");
	}
	public void setLab_Msg(Label lab_Msg) {
		Lab_Msg = lab_Msg;
	}

//	
//         get
//         {
//             return this.Pub1.GetLabelByID("Lab_Msg");
//         }{
//     }

     public Label Lab_Note;
     public Label getLab_Note() {
		return (Label) map.get("Lab_Note");
	}
	public void setLab_Note(Label lab_Note) {
		Lab_Note = lab_Note;
	}

//	{
//         get
//         {
//             return this.Pub1.GetLabelByID("Lab_Note");
//         }
//     }

     /// <summary>
     /// 属性
     /// </summary>
     private DDL DDL_Attr;
     public DDL getDDL_Attr() {
		return (DDL) map.get("DDL_Attr");
	}
	public void setDDL_Attr(DDL dDL_Attr) {
		DDL_Attr = dDL_Attr;
	}

//	{
//         get
//         {
//             return this.Pub1.GetDDLByID("DDL_Attr");
//         }
//     }

     private DDL DDL_Oper;
     public DDL getDDL_Oper() {
		return (DDL) map.get("DDL_Oper");
	}
	public void setDDL_Oper(DDL dDL_Oper) {
		DDL_Oper = dDL_Oper;
	}

//	{
//         get
//         {
//             return this.Pub1.GetDDLByID("DDL_Oper");
//         }
//     }

	
     private DDL DDL_ConnJudgeWay;
     public DDL getDDL_ConnJudgeWay() {
		return (DDL) map.get("DDL_ConnJudgeWay");
	}
	public void setDDL_ConnJudgeWay(DDL dDL_ConnJudgeWay) {
		DDL_ConnJudgeWay = dDL_ConnJudgeWay;
	}

//	{
//         get
//         {
//             return this.Pub1.GetDDLByID("DDL_ConnJudgeWay");
//         }
//     }

     private String GenerMyPK;
     public String getGenerMyPK() {
    	 return this.getFK_MainNode() + "_" + getToNodeID() + "_" + CondType.forValue(getHisCondType()) + "_" + ConnDataFrom.Depts;
	}
	public void setGenerMyPK(String generMyPK) {
		GenerMyPK = generMyPK;
	}

//	{
//         get
//         {
//             return this.FK_MainNode + "_" + this.ToNodeID + "_" + this.HisCondType.ToString() + "_" + ConnDataFrom.Depts.ToString();
//         }
//     }

}
