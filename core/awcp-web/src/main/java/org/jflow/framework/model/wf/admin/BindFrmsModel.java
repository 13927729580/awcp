package org.jflow.framework.model.wf.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.common.model.BaseModel;
import org.jflow.framework.system.ui.core.Button;
import org.jflow.framework.system.ui.core.CheckBox;
import org.jflow.framework.system.ui.core.DDL;
import org.jflow.framework.system.ui.core.ListItem;
import org.jflow.framework.system.ui.core.TextBox;

import BP.En.QueryObject;
import BP.Sys.Frm.AppType;
import BP.Sys.Frm.FrmType;
import BP.Sys.Frm.MapData;
import BP.Sys.Frm.MapDataAttr;
import BP.Sys.Frm.MapDatas;
import BP.WF.Template.Node;
import BP.WF.Template.Form.FrmNode;
import BP.WF.Template.Form.FrmNodeAttr;
import BP.WF.Template.Form.FrmNodes;
import BP.WF.Template.Form.Sys.SysFormTree;
import BP.WF.Template.Form.Sys.SysFormTreeAttr;
import BP.WF.Template.Form.Sys.SysFormTrees;

public class BindFrmsModel extends BaseModel {

	private StringBuffer Pub1;

	public String getPub1() {
		return Pub1.toString();
	}

	public BindFrmsModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public String getEnsName() {
		return this.get_request().getParameter("EnsName");
	}

	// /#region 属性.
	public  String getFK_MapData() {
		return getParameter("FK_MapData");
	}

	public  String getFK_Flow() {
		return getParameter("FK_Flow");
	}

	/**
	 * 节点
	 */
	public  int getFK_Node() {
		try {
			return Integer.parseInt(getParameter("FK_Node"));
		} catch (java.lang.Exception e) {
			return Integer.parseInt(getParameter("FK_Flow"));
		}
	}

	// /#endregion 属性.

	public  void loadPage() {
		// switch (this.DoType)
		if (this.getDoType().equals("Up")) {
			FrmNode fnU = new FrmNode(this.getMyPK());
			fnU.DoUp();
			this.BindList();
		} else if (this.getDoType().equals("Down")) {
			FrmNode fnD = new FrmNode(this.getMyPK());
			fnD.DoDown();
			this.BindList();
		} else if (this.getDoType().equals("SelectedFrm")) {
			this.SelectedFrm();
		} else {
			this.BindList();
		}
	}

	// /#region 绑定表单.
	public  void SelectedFrm() {
		Pub1 = new StringBuffer();
		Node nd = new Node(this.getFK_Node());

		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		Pub1.append(AddTable("align=left"));
		Pub1.append(AddCaption("设置节点:(" + nd.getName() + ")绑定的表单"));
		Pub1.append(AddTR());
		Pub1.append(AddTDTitle("Idx"));
		Pub1.append(AddTDTitle("表单编号"));
		Pub1.append(AddTDTitle("名称"));
		Pub1.append(AddTDTitle("表/视图"));
		Pub1.append(AddTREnd());

		MapDatas mds = new MapDatas();
		QueryObject obj_mds = new QueryObject(mds);
		obj_mds.AddWhere(MapDataAttr.AppType, AppType.Application.getValue());
		obj_mds.addOrderBy(MapDataAttr.Name);
		obj_mds.DoQuery();

		SysFormTrees formTrees = new SysFormTrees();
		QueryObject objInfo = new QueryObject(formTrees);
		objInfo.AddWhere(SysFormTreeAttr.ParentNo, "0");
		objInfo.addOrderBy(SysFormTreeAttr.Name);
		objInfo.DoQuery();

		int idx = 0;
		for (SysFormTree fs : SysFormTrees.convertSysFormTrees(formTrees)) {
			idx++;
			Pub1.append(AddTRSum());
			Pub1.append(AddTDIdx(idx));
			Pub1.append(AddTD("colspan=4", fs.getName()));
			Pub1.append(AddTREnd());
			for (MapData md : MapDatas.convertMapDatas(mds)) {
				if (!md.getFK_FormTree().equals(fs.getNo())) {
					continue;
				}
				idx++;
				Pub1.append(AddTR());
				Pub1.append(AddTDIdx(idx));

				CheckBox cb = new CheckBox();
				cb.setId("CB_" + md.getNo());
				cb.setText(md.getNo());
				cb.setChecked(fns.Contains(FrmNodeAttr.FK_Frm, md.getNo()));

				Pub1.append(AddTD(cb));
				if (cb.getChecked()) {
					Pub1.append(AddTDB("<a href=\"javascript:WinOpen('../MapDef/CCForm/Frm.jsp?FK_MapData="
							+ md.getNo()
							+ "&FK_Flow="
							+ this.getFK_Flow()
							+ "');\" ><b>" + md.getName() + "</b></a>"));
					Pub1.append(AddTDB(md.getPTable()));
				} else {
					Pub1.append(AddTD("<a href=\"javascript:WinOpen('../MapDef/CCForm/Frm.jsp?FK_MapData="
							+ md.getNo()
							+ "&FK_Flow="
							+ this.getFK_Flow()
							+ "');\" >" + md.getName() + "</a>"));
					Pub1.append(AddTD(md.getPTable()));
				}
				Pub1.append(AddTREnd());
			}
			AddChildNode(fs.getNo(), mds, fns);
		}
		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setText("保存并设置绑定方案属性");
		btn.setCssClass("Btn");
		btn.addAttr("onclick", "SaveFlowFrmsClick();");
		Pub1.append(AddTR());
		Pub1.append(AddTD("colspan=4", btn));
		Pub1.append(AddTREnd());
		Pub1.append(AddTableEnd());
	}

	private void AddChildNode(String parentNo, MapDatas mds, FrmNodes fns) {
		SysFormTrees formTrees = new SysFormTrees();
		QueryObject objInfo = new QueryObject(formTrees);
		objInfo.AddWhere(SysFormTreeAttr.ParentNo, parentNo);
		objInfo.addOrderBy(SysFormTreeAttr.Name);
		objInfo.DoQuery();

		int idx = 0;
		for (SysFormTree fs : SysFormTrees.convertSysFormTrees(formTrees)) {
			idx++;
			for (MapData md : MapDatas.convertMapDatas(mds)) {
				if (md.getFK_FormTree().equals(fs.getNo())) {
					continue;
				}
				idx++;
				Pub1.append(AddTR());
				Pub1.append(AddTDIdx(idx));

				CheckBox cb = new CheckBox();
				cb.setId("CB_" + md.getNo());
				cb.setText(md.getNo());
				cb.setChecked(fns.Contains(FrmNodeAttr.FK_Frm, md.getNo()));

				Pub1.append(AddTD(cb));
				Pub1.append(AddTD(md.getName()));
				Pub1.append(AddTD(md.getPTable()));
				Pub1.append(AddTREnd());
			}
			AddChildNode(fs.getNo(), mds, fns);
		}
	}

	// /#endregion 绑定表单.

	// /#region 设置方案.
	public  void BindList() {
		Pub1 = new StringBuffer();
		String text = "";
		Node nd = new Node(this.getFK_Node());
		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		if (fns.size() == 0) {
			text = "当前没有任何流程表单绑定到该节点上，请您执行绑定表单：<input type=button onclick=\"javascript:BindFrms('"
					+ this.getFK_Node()
					+ "','"
					+ this.getFK_Flow()
					+ "');\" value='修改表单绑定'  class=Btn />";
			Pub1.append(AddFieldSet("提示", text));
			return;
		}

		Pub1.append(AddTable("width=100%"));
		Pub1.append(AddCaption("设置节点:(" + nd.getName() + ")绑定表单"));
		Pub1.append(AddTR());
		Pub1.append(AddTDTitle("Idx"));
		Pub1.append(AddTDTitle("表单编号"));
		Pub1.append(AddTDTitle("名称"));
		Pub1.append(AddTDTitle("显示方式"));
		Pub1.append(AddTDTitle("可编辑否？"));
		Pub1.append(AddTDTitle("可打印否？"));
		Pub1.append(AddTDTitle("是否启用<br>装载填充事件"));
		Pub1.append(AddTDTitle("权限控制<br>方案"));
		Pub1.append(AddTDTitle("表单元素<br>自定义设置"));
		Pub1.append(AddTDTitle("谁是主键？"));
		Pub1.append(AddTDTitle("顺序"));
		Pub1.append(AddTDTitle(""));
		Pub1.append(AddTDTitle(""));
		Pub1.append(AddTREnd());

		int idx = 1;
		for (FrmNode fn : FrmNodes.convertFrmNodes(fns)) {
			Pub1.append(AddTR());
			Pub1.append(AddTDIdx(idx++));
			Pub1.append(AddTD(fn.getFK_Frm()));

			MapData md = new MapData();
			md.setNo(fn.getFK_Frm());
			try {
				md.Retrieve();
			} catch (java.lang.Exception e) {
				// 说明该表单不存在了，就需要把这个删除掉.
				fn.Delete();
			}

			Pub1.append(AddTD("<a href=\"javascript:WinOpen('../MapDef/CCForm/Frm.jsp?FK_MapData="
					+ md.getNo()
					+ "&FK_Flow="
					+ this.getFK_Flow()
					+ "');\" >"
					+ md.getName() + "</a>"));

			DDL ddl = new DDL();
			ddl.setId("DDL_FrmType_" + fn.getFK_Frm());
			ddl.BindSysEnum("FrmType", fn.getHisFrmType().getValue());
			Pub1.append(AddTD(ddl));

			CheckBox cb = new CheckBox();
			cb.setId("CB_IsEdit_" + md.getNo());
			cb.setText("可编辑否？");
			cb.setChecked(fn.getIsEdit());
			Pub1.append(AddTD(cb));

			cb = new CheckBox();
			cb.setId("CB_IsPrint_" + md.getNo());
			cb.setText("打印否？");
			cb.setChecked(fn.getIsPrint());
			Pub1.append(AddTD(cb));

			cb = new CheckBox();
			cb.setId("CB_IsEnableLoadData_" + md.getNo());
			cb.setText("启用否？");
			cb.setChecked(fn.getIsEnableLoadData());
			Pub1.append(AddTD(cb));

			ddl = new DDL();
			ddl.setId("DDL_Sln_" + md.getNo());
			ddl.Items.add(new ListItem("默认方案", "0"));
			ddl.Items.add(new ListItem("自定义", (new Integer(this.getFK_Node()))
					.toString()));
			ddl.SetSelectItem(fn.getFrmSln()); // 设置权限控制方案.
			Pub1.append(AddTD(ddl));

			Pub1.append(AddTDBegin());
			Pub1.append("<a href=\"javascript:WinField('" + md.getNo() + "','"
					+ this.getFK_Node() + "','" + this.getFK_Flow()
					+ "')\" >字段</a>");
			Pub1.append("-<a href=\"javascript:WinFJ('" + md.getNo() + "','"
					+ this.getFK_Node() + "','" + this.getFK_Flow()
					+ "')\" >附件</a>");
			Pub1.append("-<a href=\"javascript:WinDtl('" + md.getNo() + "','"
					+ this.getFK_Node() + "','" + this.getFK_Flow()
					+ "')\" >从表</a>");

			if (md.getHisFrmType() == FrmType.ExcelFrm) {
				Pub1.append("-<a href=\"javascript:ToolbarExcel('" + md.getNo()
						+ "','" + this.getFK_Node() + "','" + this.getFK_Flow()
						+ "')\" >ToolbarExcel</a>");
			}

			if (md.getHisFrmType() == FrmType.WordFrm) {
				Pub1.append("-<a href=\"javascript:ToolbarWord('" + md.getNo()
						+ "','" + this.getFK_Node() + "','" + this.getFK_Flow()
						+ "')\" >ToolbarWord</a>");
			}

			Pub1.append(AddTDEnd());

			ddl = new DDL();
			ddl.setId("DDL_WhoIsPK_" + md.getNo());
			ddl.BindSysEnum("WhoIsPK");
			ddl.SetSelectItem(fn.getWhoIsPK().getValue()); // 谁是主键？.
			Pub1.append(AddTD(ddl));

			TextBox tb = new TextBox();
			tb.setId("TB_Idx_" + md.getNo());
			tb.setText(fn.getIdx() + "");
			tb.setColumns(5);
			Pub1.append(AddTD(tb));

			Pub1.append(AddTDA("BindFrms.jsp?ShowType=EditPowerOrder&FK_Node="
					+ this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow()
					+ "&MyPK=" + fn.getMyPK() + "&DoType=Up", "上移"));
			Pub1.append(AddTDA("BindFrms.jsp?ShowType=EditPowerOrder&FK_Node="
					+ this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow()
					+ "&MyPK=" + fn.getMyPK() + "&DoType=Down", "下移"));

			Pub1.append(AddTREnd());
		}

		Pub1.append(AddTableEnd());

		text = "<input type=button onclick=\"javascript:BindFrms('"
				+ this.getFK_Node() + "','" + this.getFK_Flow()
				+ "');\" value='修改表单绑定'  class=Btn />";
		Pub1.append(text);

		Button btn = new Button();
		btn.setId("Save");
		btn.setText("保存设置");
		btn.setCssClass("Btn");

		btn.addAttr("onclick", "SavePowerOrdersClick();");
		Pub1.append(btn);

		text = "<input type=button onclick=\"javascript:window.close();\" value='关闭'  class=Btn />";
		Pub1.append(text);
	}

}
