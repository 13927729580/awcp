package org.jflow.framework.designer.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.common.model.BaseModel;
import org.jflow.framework.system.ui.UiFatory;
import org.jflow.framework.system.ui.core.Button;
import org.jflow.framework.system.ui.core.CheckBox;
import org.jflow.framework.system.ui.core.TextBox;

import BP.DA.DataTable;
import BP.Sys.Frm.FrmAttachment;
import BP.Sys.Frm.FrmAttachmentAttr;
import BP.Sys.Frm.FrmAttachments;
import BP.Sys.Frm.MapAttr;
import BP.Sys.Frm.MapAttrs;
import BP.Sys.Frm.MapData;
import BP.WF.Template.Form.Sys.Sln.FrmField;
import BP.WF.Template.Form.Sys.Sln.FrmFieldAttr;
import BP.WF.Template.Form.Sys.Sln.FrmFields;
import BP.WF.Template.WorkBase.WorkAttr;

public class SlnModel extends BaseModel{
	private String FK_MapData;
	private String FK_Flow;
	private String FK_Node;
	private String KeyOfEn;
	private String Ath;
	private String Title;
	public DataTable dtNodes;
	public StringBuilder Pub1=null;
	

	public SlnModel(HttpServletRequest request, HttpServletResponse response,String FK_MapData,String FK_Flow,String FK_Node,String
			KeyOfEn,String Ath,DataTable dtNodes) {
		super(request, response);
		this.Pub1=new StringBuilder();
		this.FK_MapData=FK_MapData;
		this.FK_Flow=FK_Flow;
		this.FK_Node=FK_Node;
		this.KeyOfEn=KeyOfEn;
		this.Ath=Ath;
		this.dtNodes=dtNodes;
	}
//	public final String getFK_MapData()
//	{
//		return this.Request.QueryString["FK_MapData"];
//	}
//	public String _fk_flow = null;
//	public final String getFK_Flow()
//	{
//		if (_fk_flow == null)
//		{
//			String flowNo = this.Request.QueryString["FK_Flow"];
//			if (DotNetToJavaStringHelper.isNullOrEmpty(flowNo)==false)
//			{
//				_fk_flow = flowNo;
//			}
//			else
//			{
//				BP.WF.Node nd = new BP.WF.Node(Integer.parseInt(this.getFK_Node()));
//				_fk_flow = nd.FK_Flow;
//			}
//		}
//		return _fk_flow;
//	}
//	public final String getFK_Node()
//	{
//		return this.Request.QueryString["FK_Node"];
//	}
//	public final String getKeyOfEn()
//	{
//		return this.Request.QueryString["KeyOfEn"];
//	}
//	public final String getAth()
//	{
//		return this.Request.QueryString["Ath"];
//	}
//	public DataTable _dtNodes = null;
//	public final DataTable getdtNodes()
//	{
//		if (_dtNodes == null)
//		{
//			String sql = "SELECT NodeID,Name,Step FROM WF_Node WHERE NodeID IN (SELECT FK_Node FROM Sys_FrmSln WHERE FK_MapData='" + this.getFK_MapData() + "' )";
//			_dtNodes = BP.DA.DBAccess.RunSQLReturnTable(sql);
//		}
//		return _dtNodes;
//	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 属性.

	public void init()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 功能执行.
		if (this.getDoType().equals("DeleteFJ"))
		{
			FrmAttachment ath1 = new FrmAttachment();
			ath1.setMyPK(this.getFK_MapData() + "_" + this.getAth() + "_" + FK_Node);
			ath1.Delete();
			this.WinClose();
			return;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 功能执行.


		MapData md = new MapData(this.getFK_MapData());
		FrmField sln = new FrmField();
		sln.CheckPhysicsTable();

//C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a string member and was converted to Java 'if-else' logic:
//		switch (this.DoType)
//ORIGINAL LINE: case "FJ":
		if (this.getDoType().equals("FJ")) //附件方案.
		{
				this.Title = "表单附件权限";
				BindFJ();
		}
//ORIGINAL LINE: case "Field":
		else if (this.getDoType().equals("Field")) //字段方案.
		{
		}
		else
		{
				this.Title = "表单字段权限";
				this.BindSln();
		}
	}

	public final void BindFJ()
	{
		FrmAttachments fas = new FrmAttachments();
		fas.Retrieve(FrmAttachmentAttr.FK_MapData, this.getFK_MapData());

		this.Pub1.append(AddTable("width='100%'"));
		this.Pub1.append(AddCaptionLeft("表单附件权限."));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("Idx"));
		this.Pub1.append(AddTDTitle("编号"));
		this.Pub1.append(AddTDTitle("名称"));
		this.Pub1.append(AddTDTitle("类型"));
		this.Pub1.append(AddTDTitle("编辑"));
		this.Pub1.append(AddTDTitle("删除"));
		this.Pub1.append(AddTREnd());

		int idx = 0;
		for (FrmAttachment item : FrmAttachments.convertFrmAttachments(fas))
		{
			if (item.getFK_Node() != 0)
			{
				continue;
			}

			idx++;
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx));
			this.Pub1.append(AddTD(item.getNoOfObj()));
			this.Pub1.append(AddTD(item.getName()));
			this.Pub1.append(AddTD(item.getUploadTypeT()));
			this.Pub1.append(AddTD("<a href=\"javascript:EditFJ('"+this.getFK_Node()+"','"+this.getFK_MapData()+"','"+item.getNoOfObj()+"')\">编辑</a>"));

			FrmAttachment en = new FrmAttachment();
			en.setMyPK(this.getFK_MapData() + "_" + item.getNoOfObj() + "_" + this.getFK_Node());
			if (en.RetrieveFromDBSources()==0)
			{
				this.Pub1.append(AddTD());
			}
			else
			{
				this.Pub1.append(AddTD("<a href=\"javascript:DeleteFJ('" + this.getFK_Node() + "','" + this.getFK_MapData() + "','" + item.getNoOfObj() + "')\">删除</a>"));
			}

			this.Pub1.append(AddTREnd());
		}
		this.Pub1.append(AddTableEnd());
	}
	/** 
	 绑定方案
	 
	*/
	public final void BindSln()
	{
		// 查询出来解决方案.
		FrmFields fss = new FrmFields(this.getFK_MapData(),this.getFK_Node());

		// 处理好.
		MapAttrs attrs = new MapAttrs(this.getFK_MapData());

		this.Pub1.append(AddTable("80%"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("Idx"));
		this.Pub1.append(AddTDTitle("字段"));
		this.Pub1.append(AddTDTitle("名称"));
		this.Pub1.append(AddTDTitle("类型"));

		this.Pub1.append(AddTDTitle("width='90px'","可见？"));
		this.Pub1.append(AddTDTitle("<input type='checkbox' id='s' onclick=\"CheckAll('UIIsEnable')\" />可用？"));

		this.Pub1.append(AddTDTitle("是否是签名？"));
		this.Pub1.append(AddTDTitle("默认值"));

		this.Pub1.append(AddTDTitle("<input type='checkbox' id='s' onclick=\"CheckAll('IsNotNull')\" />检查必填？"));
		this.Pub1.append(AddTDTitle("正则表达式"));

		this.Pub1.append(AddTDTitle("<input type='checkbox' id='s' onclick=\"CheckAll('IsWriteToFlowTable')\" />写入流程数据表？"));
		this.Pub1.append(AddTDTitle(""));
		this.Pub1.append(AddTREnd());

		CheckBox cb = new CheckBox();
		TextBox tb = new TextBox();

		int idx = 0;
		for (MapAttr attr : MapAttrs.convertMapAttrs(attrs))
		{
			if(attr.getKeyOfEn().equals(WorkAttr.RDT)){
				
			}else if(attr.getKeyOfEn().equals(WorkAttr.FID)){
				
			}else if(attr.getKeyOfEn().equals(WorkAttr.OID)){
				
			}else if(attr.getKeyOfEn().equals(WorkAttr.Rec)){
				
			}else if(attr.getKeyOfEn().equals(WorkAttr.MyNum)){
				
			}else if(attr.getKeyOfEn().equals(WorkAttr.MD5)){
				
			}else if(attr.getKeyOfEn().equals(WorkAttr.Emps)){
				
			}else if(attr.getKeyOfEn().equals(WorkAttr.CDT)){
				//continue;
			}else{
				//break;
			}
//			switch (attr.getKeyOfEn())
//			{
//				case BP.WF.WorkAttr.RDT:
//				case BP.WF.WorkAttr.FID:
//				case BP.WF.WorkAttr.OID:
//				case BP.WF.WorkAttr.Rec:
//				case BP.WF.WorkAttr.MyNum:
//				case BP.WF.WorkAttr.MD5:
//				case BP.WF.WorkAttr.Emps:
//				case BP.WF.WorkAttr.CDT:
//					continue;
//				default:
//					break;
//			}

			idx++;
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(idx));
			this.Pub1.append(AddTD(attr.getKeyOfEn()));
			this.Pub1.append(AddTD(attr.getName()));
			this.Pub1.append(AddTD(attr.getLGTypeT()));

			Object tempVar = fss.GetEntityByKey(FrmFieldAttr.KeyOfEn, attr.getKeyOfEn());
			FrmField sln = (FrmField)((tempVar instanceof FrmField) ? tempVar : null);
			if (sln == null)
			{
				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_UIVisible");
				cb.setName("CB_" + attr.getKeyOfEn() + "_UIVisible");
				cb.setChecked(attr.getUIVisible());
				cb.setText("可见？");
				this.Pub1.append(AddTD("width=90px", cb));

				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_UIIsEnable");
				cb.setName("CB_" + attr.getKeyOfEn() + "_UIIsEnable");
				cb.setChecked(attr.getUIIsEnable());
				cb.setText("可用？");
				this.Pub1.append(AddTD("width=90px", cb));

				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_IsSigan");
				cb.setName("CB_" + attr.getKeyOfEn() + "_IsSigan");
				cb.setChecked(attr.getIsSigan());
				cb.setText("是否数字签名？");
				this.Pub1.append(AddTD("width=150px", cb));

				tb = new TextBox();
				tb.setId("TB_" + attr.getKeyOfEn() + "_DefVal");
				tb.setName("TB_" + attr.getKeyOfEn() + "_DefVal");
				tb.setText(attr.getDefValReal());
				this.Pub1.append(AddTD(tb));


				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_" + FrmFieldAttr.IsNotNull);
				cb.setName("CB_" + attr.getKeyOfEn() + "_" + FrmFieldAttr.IsNotNull);
			   // cb.Checked = attr.IsNotNull;
				cb.setChecked(false);
				cb.setText("检查必填？");
				this.Pub1.append(AddTD(cb));

				tb = new TextBox();
				tb.setId("TB_" + attr.getKeyOfEn() + "_" + FrmFieldAttr.RegularExp);
				tb.setName("TB_" + attr.getKeyOfEn() + "_" + FrmFieldAttr.RegularExp);
				//tb.Text = attr.RegularExp;
			  //  tb.Columns = 150;
				this.Pub1.append(AddTD(tb));


				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_"+FrmFieldAttr.IsWriteToFlowTable);
				cb.setName("CB_" + attr.getKeyOfEn() + "_"+FrmFieldAttr.IsWriteToFlowTable);
				cb.setChecked(false);
				cb.setText("是否写入流程表？");
				this.Pub1.append(AddTD(cb));

				this.Pub1.append(AddTD());
				//this.Pub2.AddTD("<a href=\"javascript:EditSln('" + this.FK_MapData + "','" + this.SlnString + "','" + attr.KeyOfEn + "')\" >Edit</a>");
			}
			else
			{
				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_UIVisible");
				cb.setName("CB_" + attr.getKeyOfEn() + "_UIVisible");
				cb.setChecked(sln.getUIVisible());
				cb.setText("可见？");
				this.Pub1.append(AddTD("width=90px", cb));

				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_UIIsEnable");
				cb.setName("CB_" + attr.getKeyOfEn() + "_UIIsEnable");
				cb.setChecked(sln.getUIIsEnable());
				cb.setText("可用？");
				this.Pub1.append(AddTD("width=90px", cb));

				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_IsSigan");
				cb.setName("CB_" + attr.getKeyOfEn() + "_IsSigan");
				cb.setChecked(sln.getIsSigan());
				cb.setText("是否数字签名？");
				this.Pub1.append(AddTD("width=150px", cb));

				tb = new TextBox();
				tb.setId("TB_" + attr.getKeyOfEn() + "_DefVal");
				tb.setName("TB_" + attr.getKeyOfEn() + "_DefVal");
				tb.setText(sln.getDefVal());
				this.Pub1.append(AddTD(tb));

				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_"+FrmFieldAttr.IsNotNull);
				cb.setChecked(sln.getIsNotNull());
				cb.setText("必填？");
				this.Pub1.append(AddTD(cb));

				tb = new TextBox();
				tb.setId("TB_" + attr.getKeyOfEn() + "_RegularExp");
				tb.setName("TB_" + attr.getKeyOfEn() + "_RegularExp");
				tb.setText(sln.getRegularExp());
				this.Pub1.append(AddTD(tb));

				cb = new CheckBox();
				cb.setId("CB_" + attr.getKeyOfEn() + "_" + FrmFieldAttr.IsWriteToFlowTable);
				cb.setName("CB_" + attr.getKeyOfEn() + "_" + FrmFieldAttr.IsWriteToFlowTable);
				cb.setChecked(sln.getIsWriteToFlowTable());
				cb.setText("写入流程数据表？");
				this.Pub1.append(AddTD(cb));

				this.Pub1.append(AddTD("<a href=\"javascript:DelSln('" + this.getFK_MapData() + "','"+this.getFK_Flow()+"','"+this.getFK_Node()+"','" + this.getFK_Node() + "','" + attr.getKeyOfEn() + "')\" ><img src='../Img/Btn/Delete.gif' border=0/>Delete</a>"));
			}
			this.Pub1.append(AddTREnd());
		}
		this.Pub1.append(AddTableEnd());

		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setName("Btn_Save");
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
		//btn.Click += new EventHandler(btn_Field_Click);
		btn.addAttr("onclick", "btn_Field_Click('Btn_Save')");
		btn.setText(" Save ");
		this.Pub1.append(btn); //保存.

		if (fss.size() != 0)
		{
			btn = new Button();
			btn.setId("Btn_Del");
			btn.setName("Btn_Del");
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
			//btn.Click += new EventHandler(btn_Field_Click);
			btn.addAttr("onclick", "btn_Field_Click('Btn_Del')");
			btn.setText(" Delete All ");
//			btn.Attributes["onclick"] = "return confirm('Are you sure?');";
			this.Pub1.append(btn); //删除定义..
		}

		if (dtNodes.Rows.size() >= 1)
		{
			btn = new Button();
			btn.setId("Btn_Copy");
			btn.setName("Btn_Copy");
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
			//btn.Click += new EventHandler(btn_Field_Click);
			btn.addAttr("onclick", "btn_Field_Click('Btn_Copy')");
			btn.setText(" Copy From Node ");
//			btn.Attributes["onclick"] = "CopyIt('" + this.getFK_MapData() + "','" + this.getFK_Node() + "')";
			this.Pub1.append(btn); //删除定义..
		}
	}

	


}
