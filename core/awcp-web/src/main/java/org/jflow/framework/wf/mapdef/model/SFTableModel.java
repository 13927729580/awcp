package org.jflow.framework.wf.mapdef.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.common.model.BaseModel;
import org.jflow.framework.system.ui.core.Button;
import org.jflow.framework.system.ui.core.TextBox;

import BP.Sys.SFTable;
import BP.Tools.StringHelper;
import BP.WF.Glo;

public class SFTableModel extends BaseModel{
	
	private StringBuilder pubBuilder;
	
	private String refNo;
	
	private String title;
	
	public String getTitle() {
		return title;
	}
	
	public String getPubBuilder() {
		return pubBuilder.toString();
	}

	public SFTableModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public String getIDX(){
		return getParameter("IDX");
	}
	private void appendPubBuilder(String str){
		pubBuilder.append(str);
	}
	
	public void pageLoad()
	{
		pubBuilder = new StringBuilder();
		refNo = getRefNo();
		
		SFTable main = new SFTable();
		if (!StringHelper.isNullOrEmpty(refNo))
		{
			main.setNo(refNo);
			main.Retrieve();
		}
		this.BindSFTable(main);
	}
	
	public void BindSFTable(SFTable en)
	{

		String star = "<font color=red><b>(*)</b></font>";
		appendPubBuilder(AddTable());
		if (StringHelper.isNullOrEmpty(refNo))
		{
			appendPubBuilder(AddCaption("<a href='Do.aspx?DoType=AddF&MyPK=" + this.getMyPK() + "&IDX=" + this.getIDX() + "'>增加新字段向导</a> - <a href='Do.aspx?DoType=AddSFTable&MyPK=" + this.getMyPK() + "&IDX=" + this.getIDX() + "'>外键</a> - 新建表"));
		}
		else
		{
			appendPubBuilder(AddCaption("<a href='Do.aspx?DoType=AddF&MyPK=" + this.getMyPK() + "&IDX=" + this.getIDX() + "'>增加新字段向导</a> - <a href='Do.aspx?DoType=AddSFTable&MyPK=" + this.getMyPK() + "&IDX=" + this.getIDX() + "'>外键</a> - 编辑表"));
		}

		if (StringHelper.isNullOrEmpty(refNo))
		{
			this.title = "新建表";
		}
		else
		{
			this.title = "编辑表";
		}

		appendPubBuilder(AddTR());
		appendPubBuilder(AddTDTitle("项目"));
		appendPubBuilder(AddTDTitle("采集"));
		appendPubBuilder(AddTDTitle("备注"));
		appendPubBuilder(AddTREnd());

		appendPubBuilder(AddTR());
		appendPubBuilder(AddTD("表英文名称" + star));
		TextBox tb = new TextBox();
		tb.setId("TB_No");
		tb.setText(en.getNo());
		if (StringHelper.isNullOrEmpty(refNo))
		{
			tb.setEnabled(true);
		}
		else
		{
			tb.setEnabled(false);
		}

		if (tb.getText().equals(""))
		{
			tb.setText("SF_");
		}

		appendPubBuilder(AddTD(tb));
		appendPubBuilder(AddTD("输入:新表名或已经存在的表名"));
		appendPubBuilder(AddTREnd());

		appendPubBuilder(AddTR());
		appendPubBuilder(AddTD("表中文名称" + star));
		tb = new TextBox();
		tb.setId("TB_Name");
		tb.setText(en.getName());
		appendPubBuilder(AddTD(tb));
		appendPubBuilder(AddTD());
		appendPubBuilder(AddTREnd());


		appendPubBuilder(AddTR());
		appendPubBuilder(AddTD("描述" + star));
		tb = new TextBox();
		tb.setId("TB_TableDesc");
		tb.setText(en.getTableDesc());
		appendPubBuilder(AddTD(tb));
		appendPubBuilder(AddTD(""));
		appendPubBuilder(AddTREnd());

		appendPubBuilder(AddTR());
		appendPubBuilder("<TD colspan=3 align=center>");
		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setCssClass("Btn");
		if (StringHelper.isNullOrEmpty(refNo))
		{
			btn.setText("创建");
		}
		else
		{
			btn.setText("保存");
		}
		btn.addAttr("onclick", " btn_Save_Click();");
		appendPubBuilder(btn.toString());

		btn = new Button();
		btn.setId("Btn_Edit");
		btn.setCssClass("Btn");
		btn.setText("编辑数据"); // "编辑数据"
		if (StringHelper.isNullOrEmpty(refNo))
		{
			btn.setEnabled(false);
		}
		if (en.getIsClass()) {
			btn.addAttr("onclick", "WinOpen('"+Glo.getCCFlowAppPath()+"WF/Search.jsp?EnsName=" + en.getNo() + "','dg' ); return false;");
		}else {
			btn.addAttr("onclick", "WinOpen('"+Glo.getCCFlowAppPath()+"WF/MapDef/SFTableEditData.jsp?RefNo=" + refNo + "','dg' ); return false;");
		}
		appendPubBuilder(btn.toString());


		btn = new Button();
		btn.setId("Btn_Add");
		btn.setCssClass("Btn");

		btn.setText("添加到表单"); // "添加到表单";
		btn.addAttr("onclick", " return confirm('您确认吗？');");
		btn.addAttr("onclick", " btn_Add_Click();");
		if (StringHelper.isNullOrEmpty(refNo))
		{
			btn.setEnabled(false);
		}

		appendPubBuilder(btn.toString());
		btn = new Button();
		btn.setId("Btn_Del");
		btn.setCssClass("Btn");

		btn.setText("删除");
		btn.addAttr("onclick", " return confirm('您确认吗？');");
		btn.addAttr("onclick", " btn_Del_Click();");
		if (StringHelper.isNullOrEmpty(refNo))
		{
			btn.setEnabled(false);
		}

		appendPubBuilder(btn.toString());
		appendPubBuilder("</TD>");
		appendPubBuilder(AddTREnd());
		appendPubBuilder(AddTableEnd());
	}

}
