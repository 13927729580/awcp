package org.jflow.framework.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.system.ui.UiFatory;
import org.jflow.framework.system.ui.core.Button;
import org.jflow.framework.system.ui.core.DDL;
import org.jflow.framework.system.ui.core.RadioButton;
import org.jflow.framework.system.ui.core.TextBox;

import BP.En.EditType;
import BP.Sys.SFTable;
import BP.Sys.Frm.GroupFieldAttr;
import BP.Sys.Frm.GroupFields;
import BP.Sys.Frm.MapAttr;
import BP.Tools.StringHelper;

public class EditTableModel extends BaseModel{
	public UiFatory Pub1=new UiFatory();

	public EditTableModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		// TODO Auto-generated constructor stub
	}
	
	/** 
	 GroupField
	 
	*/
	public String Title;
	public String FType;
	public String IDX;
	public int GroupField;
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public void setFType(String fType) {
		FType = fType;
	}
	public void setIDX(String iDX) {
		IDX = iDX;
	}
	public void setGroupField(int groupField) {
		GroupField = groupField;
	}
	public final int getGroupField()
	{
		String s = get_request().getParameter("GroupField");
        if (StringHelper.isNullOrEmpty(s))
            return 0;
        return Integer.parseInt(s);
	}
	/** 
	 执行类型
	 
	*/
	public final String getFType()
	{
		return get_request().getParameter("FType");
	}
	public final String getIDX()
	{
		return get_request().getParameter("IDX");
	}
	
	public void init()
	{
		this.setTitle("编辑外键类型");
		MapAttr attr = null;
		if (this.getRefNo() == null)
		{
			attr = new MapAttr();
			String sfKey = get_request().getParameter("SFKey");
			SFTable sf = new SFTable(sfKey);
			attr.setKeyOfEn(sf.getFK_Val());
			attr.setUIBindKey(sfKey);
			attr.setName(sf.getName());
		}
		else
		{
			attr = new MapAttr(this.getRefNo());
		}
		BindTable(attr);
	}
	private int idx = 1;
	public final void BindTable(MapAttr mapAttr)
	{
		this.Pub1.append(AddTable());
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDTitle("ID"));
		this.Pub1.append(AddTDTitle("项目"));
		this.Pub1.append(AddTDTitle("值"));
		this.Pub1.append(AddTDTitle("描述"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("字段中文名")); // 字段中文名称
		TextBox tb = Pub1.creatTextBox("TB_Name");
		tb.setId("TB_Name");
		tb.setText(mapAttr.getName());
		tb.addAttr("style", "width:100%");
		this.Pub1.append(AddTD(tb));
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("字段英文名")); // "字段英文名称"
		tb = Pub1.creatTextBox("TB_KeyOfEn");
		if (this.getRefNo() != null)
		{
			this.Pub1.append(AddTD(mapAttr.getKeyOfEn()));
		}
		else
		{
			tb = new TextBox();
			tb.setId("TB_KeyOfEn");
			tb.setText(mapAttr.getKeyOfEn());
			this.Pub1.append(AddTD(tb));
		}

		if (StringHelper.isNullOrEmpty(mapAttr.getKeyOfEn()))
		{
			this.Pub1.append(AddTD("字母/数字/下划线组合"));
		}
		else
		{
			this.Pub1.append(AddTD("<a href=\"javascript:clipboardData.setData('Text','" + mapAttr.getKeyOfEn() + "');alert('已经copy到粘帖版上');\" ><img src='../Img/Btn/Copy.GIF' class='ICON' />复制字段名</a></TD>"));
		}

		this.Pub1.append(AddTREnd());
		
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("外键表/类")); // 字段中文名称
         tb = Pub1.creatTextBox("TB_UIBindKey");
         tb.setText(mapAttr.getUIBindKey());
         tb.addAttr("style", "width:100%");
         this.Pub1.append(tb.toString());
         this.Pub1.append(AddTD(""));
         this.Pub1.append(AddTREnd());


		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("默认值")); // "默认值"

		tb = Pub1.creatTextBox("TB_DefVal");
//		tb.setId("TB_DefVal");
		tb.setText(mapAttr.getName());
		tb.addAttr("style", "width:100%");
		tb.setText(mapAttr.getDefValReal());
		this.Pub1.append(AddTD(tb));

		//DDL ddl = new DDL();
		//ddl.ID = "DDL";
		//ddl.BindEntities(mapAttr.HisEntitiesNoName);
		//ddl.SetSelectItem(mapAttr.DefVal);
		//   this.Pub1.AddTD(ddl);
		// this.Pub1.AddTD(ddl);

		if (mapAttr.getUIBindKey().contains("."))
		{
			this.Pub1.append(AddTD("<a href=\"javascript:WinOpen('../Comm/Search.jsp?EnsName=" + mapAttr.getUIBindKey() + "','df');\" >打开</a>"));
		}
		else
		{
			this.Pub1.append(AddTD("<a href=\"javascript:WinOpen('../MapDef/SFTableEditData.jsp?RefNo=" + mapAttr.getUIBindKey() + "','df');\" >打开</a>"));
		}
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("是否可编辑"));
		this.Pub1.append("<TD>");
		RadioButton rb = Pub1.creatRadioButton("RB_UIIsEnable_0");
//		rb.setId("RB_UIIsEnable_0");
		rb.setText("不可编辑"); //"不可编辑";
		rb.setGroupName("s");
		if (mapAttr.getUIIsEnable())
		{
			rb.setChecked(false);
		}
		else
		{
			rb.setChecked(true);
		}

		this.Pub1.append(rb);
		rb = Pub1.creatRadioButton("RB_UIIsEnable_1");
//		rb.setId("RB_UIIsEnable_1");
		rb.setText("可编辑"); //"可编辑";
		rb.setGroupName("s");

		if (mapAttr.getUIIsEnable())
		{
			rb.setChecked(true);
		}
		else
		{
			rb.setChecked(false);
		}

		this.Pub1.append(rb);
		this.Pub1.append(AddTDEnd());
		this.Pub1.append(AddTD(""));
		this.Pub1.append(AddTREnd());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 是否可界面可见
		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("是否界面可见")); //是否界面可见
		this.Pub1.append(AddTDBegin());
		rb = Pub1.creatRadioButton("RB_UIVisible_0");
//		rb.setId("RB_UIVisible_0");
		rb.setText("不可见"); // 界面不可见
		rb.setGroupName("sa3");
		if (mapAttr.getUIVisible())
		{
			rb.setChecked(false);
		}
		else
		{
			rb.setChecked(true);
		}

		this.Pub1.append(rb);
		if (mapAttr.getIsTableAttr())
		{
			rb.setEnabled(false);
		}

		rb = Pub1.creatRadioButton("RB_UIVisible_1");
//		rb.setId("RB_UIVisible_1");
		rb.setText("界面可见"); // 界面可见;
		rb.setGroupName("sa3");

		if (mapAttr.getUIVisible())
		{
			rb.setChecked(true);
		}
		else
		{
			rb.setChecked(false);
		}

		if (mapAttr.getIsTableAttr())
		{
			rb.setEnabled(false);
		}

		this.Pub1.append(rb);
		this.Pub1.append(AddTDEnd());

		this.Pub1.append(AddTD("不可见则为隐藏字段."));
		this.Pub1.append(AddTREnd());
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 是否可界面可见

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 是否可单独行显示
		this.Pub1.append(AddTR1());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("呈现方式")); //呈现方式;
		this.Pub1.append(AddTDBegin());
		rb = Pub1.creatRadioButton("RB_UIIsLine_0");
//		rb.setId("RB_UIIsLine_0");
		rb.setText("两列显示"); // 两行
		rb.setGroupName("sa");
		if (mapAttr.getUIIsLine())
		{
			rb.setChecked(false);
		}
		else
		{
			rb.setChecked(true);
		}

		this.Pub1.append(rb);
		rb = Pub1.creatRadioButton("RB_UIIsLine_1");
//		rb.setId("RB_UIIsLine_1 ");
		rb.setText("整行显示"); // "一行";
		rb.setGroupName("sa");

		if (mapAttr.getUIIsLine())
		{
			rb.setChecked(true);
		}
		else
		{
			rb.setChecked(false);
		}

		this.Pub1.append(rb);
		this.Pub1.append(AddTDEnd());
		this.Pub1.append(AddTD("对傻瓜表单有效"));

		//this.Pub1.AddTD("控制该它在表单的显示方式");
		this.Pub1.append(AddTREnd());
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 是否可编辑

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 字段分组
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTDIdx(idx++));
		this.Pub1.append(AddTD("字段分组"));
		DDL ddlGroup = Pub1.creatDDL("DDL_GroupID");
//		ddlGroup.setId("DDL_GroupID");
		GroupFields gfs = new GroupFields(mapAttr.getFK_MapData());
		ddlGroup.Bind(gfs, GroupFieldAttr.OID, GroupFieldAttr.Lab);
		if (mapAttr.getGroupID() == 0)
		{
			mapAttr.setGroupID(this.getGroupField());
		}

		ddlGroup.SetSelectItem(mapAttr.getGroupID());

		this.Pub1.append(AddTD("colspan=2", ddlGroup));
		this.Pub1.append(AddTD()); //( this.to "隶属分组");
		this.Pub1.append(AddTREnd());
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 字段分组

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 字段按钮
		this.Pub1.append(AddTRSum());
		this.Pub1.append("<TD colspan=4>");
		Button btn = Pub1.creatButton("Btn_Save");
//		btn.setId("Btn_Save");
//		btn.setName("Btn_Save");
		btn.setText("保存");
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
//		btn.Click += new EventHandler(btn_Save_Click);
		btn.addAttr("onclick", "btn_Save_Click('Btn_Save')");
		btn.setCssClass("Btn");
		this.Pub1.append(btn);

		btn = Pub1.creatButton("Btn_SaveAndClose1");
//		btn.setId("Btn_SaveAndClose1");
//		btn.setName("Btn_SaveAndClose1");
		btn.setCssClass("Btn");
		btn.setText("关闭");
//		btn.Attributes["onclick"] = " window.close(); return false;";
		btn.addAttr("onclick", "window.close(); return false;");
		this.Pub1.append(btn);

		btn = Pub1.creatButton("Btn_SaveAndClose");
//		btn.setId("Btn_SaveAndClose");
//		btn.setName("Btn_SaveAndClose");
		btn.setCssClass("Btn");
		btn.setText("保存并关闭"); //"保存并关闭";
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
//		btn.Click += new EventHandler(btn_Save_Click);
		btn.addAttr("onclick", "btn_Save_Click('Btn_SaveAndClose')");
		this.Pub1.append(btn);

		btn = Pub1.creatButton("Btn_SaveAndNew");
//		btn.setId("Btn_SaveAndNew");
//		btn.setName("Btn_SaveAndNew");
		btn.setCssClass("Btn");
		btn.setText("保存并新建");
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
//		btn.Click += new EventHandler(btn_Save_Click);
		btn.addAttr("onclick", "btn_Save_Click('Btn_SaveAndNew')");
		this.Pub1.append(btn);

		if (this.getRefNo() != null)
		{
			btn = Pub1.creatButton("Btn_AutoFull");
//			btn.setId("Btn_AutoFull");
//			btn.setName("Btn_AutoFull");
			btn.setCssClass("Btn");
			btn.setText("扩展设置");
			//  btn.Click += new EventHandler(btn_Save_Click);
//			btn.Attributes["onclick"] = "javascript:WinOpen('AutoFull.aspx?RefNo=" + this.RefNo + "&FK_MapData=" + mapAttr.FK_MapData + "',''); return false;";
			btn.addAttr("onclick", "javascript:WinOpen('AutoFull.jsp?RefNo=" + this.getRefNo() + "&FK_MapData=" + mapAttr.getFK_MapData() + "',''); return false;");
			this.Pub1.append(btn);

			if (mapAttr.getHisEditType() == EditType.Edit)
			{
				btn = Pub1.creatButton("Btn_Del");
//				btn.setId("Btn_Del");
//				btn.setName("Btn_Del");
				btn.setCssClass("Btn");
				btn.setText("删除");
//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
//				btn.Click += new EventHandler(btn_Save_Click);
				btn.addAttr("onclick", "btn_Save_Click('Btn_Del')");
//				btn.Attributes["onclick"] = " return confirm('您确认吗？');";
				this.Pub1.append(btn);
			}
			String myUrl = "EleBatch.jsp?KeyOfEn=" + mapAttr.getKeyOfEn() + "&FK_MapData=" + mapAttr.getFK_MapData() + "&EleType=MapAttr";
			this.Pub1.append("<a href='" + myUrl + "' target='M" + mapAttr.getKeyOfEn() + "' ><img src='../Img/Btn/Apply.gif' border=0>批处理</a>");
		}

		String url = "Do.jsp?DoType=AddF&MyPK=" + mapAttr.getFK_MapData() + "&IDX=" + mapAttr.getIDX();
		this.Pub1.append("<a href='" + url + "'><img src='../Img/Btn/New.gif' border=0>新建</a></TD>");
		this.Pub1.append(AddTREnd());
		this.Pub1.append(AddTableEnd());
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 字段按钮
	}
	public final String getGetCaption()
	{
		if (this.getDoType().equals("Add"))
		{
			return "增加新字段向导  - <a href='Do.jsp?DoType=ChoseFType&GroupField=" + this.getGroupField() + "'>选择类型</a> -" + "编辑字段";
		}
		else
		{
			return "编辑字段"; // "编辑字段";
		}
	}


}
