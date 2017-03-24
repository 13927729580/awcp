package org.jflow.framework.wf.mapdef.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.common.model.BaseModel;
import org.jflow.framework.system.ui.UiFatory;
import org.jflow.framework.system.ui.core.Button;
import org.jflow.framework.system.ui.core.CheckBox;
import org.jflow.framework.system.ui.core.DDL;
import org.jflow.framework.system.ui.core.ListItem;
import org.jflow.framework.system.ui.core.TextBox;

import BP.En.AddAllLocation;
import BP.En.TBType;
import BP.Sys.Frm.FrmAttachment;
import BP.Sys.Frm.FrmAttachmentAttr;
import BP.Sys.Frm.GroupFieldAttr;
import BP.Sys.Frm.GroupFields;

public class AttachmentModel {
	private HttpServletRequest req;
	private HttpServletResponse res;
	public UiFatory Pub1 = null;

	public AttachmentModel() {

	}

	public AttachmentModel(HttpServletRequest req, HttpServletResponse res) {
		this.req = req;
		this.res = res;
	}

	// #region 属性.
	// / <summary>
	// / 类型
	// / </summary>
	private String UploadType;

	public String getUploadType() {
		String s = req.getParameter("UploadType");
		if (s == null)
			s = "1";
		return s;
	}

	public void setUploadType(String uploadType) {
		UploadType = uploadType;
	}

	// public string UploadType
	// {
	// get
	// {
	// string s = this.Request.QueryString["UploadType"];
	// if (s == null)
	// s = "1";
	// return s;
	// }
	// }
	private String FK_MapData;

	public String getFK_MapData() {
		String s = req.getParameter("FK_MapData");
		if (s == null)
			s = "test";
		return s;
	}

	public void setFK_MapData(String fK_MapData) {
		FK_MapData = fK_MapData;
	}

	// public string FK_MapData
	// {
	// get
	// {
	// string s = this.Request.QueryString["FK_MapData"];
	// if (s == null)
	// s = "test";
	// return s;
	// }
	// }
	private String Ath;

	public String getAth() {
		return req.getParameter("Ath");
	}

	public void setAth(String ath) {
		Ath = ath;
	}

	// public string Ath
	// {
	// get
	// {
	// return this.Request.QueryString["Ath"];
	// }
	// }
	private int FK_Node;

	public int getFK_Node() {
		try {
			return Integer.parseInt(req.getParameter("FK_Node"));
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public void setFK_Node(int fK_Node) {
		FK_Node = fK_Node;
	}

	// public int FK_Node
	// {
	// get
	// {
	// try
	// {
	// return int.Parse(this.Request.QueryString["FK_Node"]);
	// }
	// catch
	// {
	// return 0;
	// }
	// }
	// }
	// #endregion 属性.

	public void Page_Load() {
		Pub1 = new UiFatory();
		FrmAttachment ath = new FrmAttachment();
		ath.setFK_MapData(this.getFK_MapData());
		ath.setNoOfObj(this.getAth());
		ath.setFK_Node(this.getFK_Node());

		if (this.getFK_Node() == 0)
			ath.setMyPK(this.getFK_MapData() + "_" + this.getAth());
		else
			ath.setMyPK(this.getFK_MapData() + "_" + this.getAth() + "_"
					+ this.getFK_Node());

		int i = ath.RetrieveFromDBSources();
		if (i == 0 && this.getFK_Node() != 0) {
			ath.setFK_MapData(this.getFK_MapData());
			ath.setNoOfObj(this.getAth());
			ath.setFK_Node(this.getFK_Node());
			ath.setMyPK(getFK_MapData() + "_" + getAth());
			ath.RetrieveFromDBSources();

			// 插入一个新的.
			ath.setMyPK(getFK_MapData() + "_" + getAth() + "_" + getFK_Node());
			ath.setFK_Node(getFK_Node());
			ath.setFK_MapData(getFK_MapData());
			ath.setNoOfObj(getAth());
			ath.DirectInsert();
		}

		this.Pub1.append(BaseModel.AddTable());
		Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDTitleLeft1("colspan='3'","附件属性设置"));
		this.Pub1.append(BaseModel.AddTREnd());
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDTitle("项目"));
		this.Pub1.append(BaseModel.AddTDTitle("采集"));
		this.Pub1.append(BaseModel.AddTDTitle("说明"));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("编号"));
		TextBox tb = Pub1.creatTextBox("TB_" + FrmAttachmentAttr.NoOfObj);
		// tb.ID = "TB_" + FrmAttachmentAttr.NoOfObj;
		tb.setText(ath.getNoOfObj());
		if (this.getAth() != null)
			tb.setEnabled(false);

		this.Pub1.append(BaseModel.AddTD(tb));
		this.Pub1.append(BaseModel.AddTD("标示号只能英文字母数字或下滑线."));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("名称"));
		tb = Pub1.creatTextBox("TB_" + FrmAttachmentAttr.Name);
		// tb.ID = "TB_" + FrmAttachmentAttr.Name;
		tb.setText(ath.getName());
		this.Pub1.append(BaseModel.AddTD(tb));
		this.Pub1.append(BaseModel.AddTD("附件的中文名称."));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("文件格式"));
		tb = Pub1.creatTextBox("TB_" + FrmAttachmentAttr.Exts);
		// tb.ID = "TB_" + FrmAttachmentAttr.Exts;
		tb.setText(ath.getExts());
		this.Pub1.append(BaseModel.AddTD(tb));
		this.Pub1.append(BaseModel.AddTD("实例:doc,docx,xls,多种格式用逗号分开."));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("保存到"));
		tb = Pub1.creatTextBox("TB_" + FrmAttachmentAttr.SaveTo);
		// tb.ID = "TB_" + FrmAttachmentAttr.SaveTo;
		tb.setText(ath.getSaveTo());
		tb.setCols(60);
		this.Pub1.append(BaseModel.AddTD("colspan=2", tb));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("类别"));
		tb = Pub1.creatTextBox("TB_" + FrmAttachmentAttr.Sort);
		// tb.ID = "TB_" + FrmAttachmentAttr.Sort;
		tb.setText(ath.getSort());
		tb.setCols(60);
		this.Pub1.append(BaseModel.AddTD("colspan=2", tb));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("colspan=3",
				"帮助:类别可以为空,设置的格式为:类别名1,类别名2,类别名3"));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("高度"));
		TextBox mytb = Pub1.creatTextBox("TB_" + FrmAttachmentAttr.H);
		// mytb.ID = "TB_" + FrmAttachmentAttr.H;
		mytb.setText(Float.toString(ath.getH()));
		mytb.setShowType(TBType.Float);
		this.Pub1.append(BaseModel.AddTD("colspan=1", mytb));
		this.Pub1.append(BaseModel.AddTD("对傻瓜表单有效"));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("宽度"));
		mytb = Pub1.creatTextBox("TB_" + FrmAttachmentAttr.W);
		// mytb.ID = "TB_" + FrmAttachmentAttr.W;
		mytb.setText(Float.toString(ath.getW()));
		mytb.setShowType(TBType.Float);
		mytb.setCols(60);
		this.Pub1.append(BaseModel.AddTD("colspan=1", mytb));
		this.Pub1.append(BaseModel.AddTD("对傻瓜表单有效"));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("自动控制"));
		CheckBox cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsAutoSize);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsAutoSize;
		cb.setText("自动控制高度与宽度(对傻瓜表单有效)");
		cb.setChecked(ath.getIsAutoSize());
		this.Pub1.append(BaseModel.AddTD("colspan=2", cb));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsNote);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsNote;
		cb.setText("是否增加备注列");
		cb.setChecked(ath.getIsNote());
		this.Pub1.append(BaseModel.AddTD(cb));

		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsShowTitle);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsShowTitle;
		cb.setText("是否显示标题列");
		cb.setChecked(ath.getIsShowTitle());
		this.Pub1.append(BaseModel.AddTD(cb));
		this.Pub1.append(BaseModel.AddTD(""));
		this.Pub1.append(BaseModel.AddTREnd());

		GroupFields gfs = new GroupFields(ath.getFK_MapData());

		this.Pub1.append(BaseModel.AddTR1());
		this.Pub1.append(BaseModel.AddTD("显示在分组"));
		DDL ddl = Pub1.creatDDL("DDL_GroupField");
		// ddl.ID = "DDL_GroupField";
		ddl.BindEntities(gfs, GroupFieldAttr.OID, GroupFieldAttr.Lab, false,
				AddAllLocation.None);
		ddl.SetSelectItem(ath.getGroupID());
		this.Pub1.append(BaseModel.AddTD("colspan=2", ddl));

		this.Pub1.append(BaseModel.AddTREnd());
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDTitle("colspan=3", "权限控制"));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsDownload);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsDownload;
		cb.setText("是否可下载");
		cb.setChecked(ath.getIsDownload());
		this.Pub1.append(BaseModel.AddTD(cb));

//		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsDelete);
//		// cb.ID = "CB_" + FrmAttachmentAttr.IsDelete;
//		cb.setText("是否可删除");
//		cb.setChecked(ath.getIsDelete());
//		this.Pub1.append(BaseModel.AddTD(cb));
		
		ddl = Pub1.creatDDL("DDL_"+ FrmAttachmentAttr.IsDelete);
        ddl.Items.clear();
        ddl.Items.add(new ListItem("不能删除", "0"));
        ddl.Items.add(new ListItem("删除所有", "1"));
        ddl.Items.add(new ListItem("只能删除自己上传的", "2"));
        ddl.SetSelectItem(ath.getIsDeleteInt());
        this.Pub1.append(BaseModel.AddTD((ddl)));

		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsUpload);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsUpload;
		cb.setText("是否可上传");
		cb.setChecked(ath.getIsUpload());
		this.Pub1.append(BaseModel.AddTD(cb));
		this.Pub1.append(BaseModel.AddTREnd());
		
		 cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsOrder);
//         cb.ID = ;
         cb.setText("是否可以排序");
         cb.setChecked(ath.getIsOrder());
         this.Pub1.append(BaseModel.AddTD(cb));
         this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR1());
		this.Pub1.append(BaseModel.AddTD("数据显示控制方式"));
		ddl = Pub1.creatDDL("DDL_CtrlWay");
		// ddl.ID = "DDL_CtrlWay";
		ddl.Items.clear();
		ddl.Items.add(new ListItem("按主键", "0"));
		ddl.Items.add(new ListItem("FID", "1"));
		ddl.Items.add(new ListItem("ParentWorkID", "2"));
		ddl.SetSelectItem(ath.getHisCtrlWay().getValue());
		this.Pub1.append(BaseModel.AddTD("colspan=2", ddl));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR1());
		this.Pub1.append(BaseModel.AddTD("数据上传控制方式"));
		ddl = Pub1.creatDDL("DDL_AthUploadWay");
		// ddl.ID = "DDL_AthUploadWay";
		ddl.Items.clear();
		ddl.Items.add(new ListItem("继承模式", "0"));
		ddl.Items.add(new ListItem("协作模式", "1"));
		ddl.SetSelectItem(ath.getAthUploadWay().getValue());
		this.Pub1.append(BaseModel.AddTD("colspan=2", ddl));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR1());
		this.Pub1.append(BaseModel.AddTD("展现方式"));
		ddl = Pub1.creatDDL("DDL_" + FrmAttachmentAttr.FileShowWay);
		// ddl.ID = "DDL_"+FrmAttachmentAttr.FileShowWay;
		ddl.Items.clear();
		ddl.Items.add(new ListItem("Table方式", "0"));
		ddl.Items.add(new ListItem("图片轮播方式", "1"));
		ddl.Items.add(new ListItem("自由模式", "2"));

		ddl.SetSelectItem(ath.getFileShowWay().getValue());
		this.Pub1.append(BaseModel.AddTD("colspan=2", ddl));
		this.Pub1.append(BaseModel.AddTREnd());

		// #region WebOffice控制方式.

		this.Pub1.append(BaseModel.AddTR1());
		this.Pub1.append(BaseModel.AddTDTitle("colspan=3", "WebOffice控制方式."));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsWoEnableWF);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableWF;
		cb.setText("是否启用weboffice？");
		cb.setChecked(ath.getIsWoEnableWF());
		this.Pub1.append(BaseModel.AddTD(cb));

		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsWoEnableSave);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableSave;
		cb.setText("是否启用保存？");
		cb.setChecked(ath.getIsWoEnableSave());
		this.Pub1.append(BaseModel.AddTD(cb));

		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsWoEnableReadonly);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableReadonly;
		cb.setText("是否只读？");
		cb.setChecked(ath.getIsWoEnableReadonly());
		this.Pub1.append(BaseModel.AddTD(cb));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsWoEnableRevise);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableRevise;
		cb.setText("是否启用修订？");
		cb.setChecked(ath.getIsWoEnableRevise());
		this.Pub1.append(BaseModel.AddTD(cb));

		cb = Pub1.creatCheckBox("CB_"
				+ FrmAttachmentAttr.IsWoEnableViewKeepMark);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableViewKeepMark;
		cb.setText("是否查看用户留痕？");
		cb.setChecked(ath.getIsWoEnableViewKeepMark());
		this.Pub1.append(BaseModel.AddTD(cb));

		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsWoEnablePrint);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnablePrint;
		cb.setText("是否打印？");
		cb.setChecked(ath.getIsWoEnablePrint());
		this.Pub1.append(BaseModel.AddTD(cb));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsWoEnableOver);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableOver;
		cb.setText("是否启用套红？");
		cb.setChecked(ath.getIsWoEnableOver());
		this.Pub1.append(BaseModel.AddTD(cb));

		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsWoEnableSeal);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableSeal;
		cb.setText("是否启用签章？");
		cb.setChecked(ath.getIsWoEnableSeal());
		this.Pub1.append(BaseModel.AddTD(cb));

		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsWoEnableTemplete);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableTemplete;
		cb.setText("是否启用模板文件？");
		cb.setChecked(ath.getIsWoEnableTemplete());
		this.Pub1.append(BaseModel.AddTD(cb));

		this.Pub1.append(BaseModel.AddTREnd());
		this.Pub1.append(BaseModel.AddTR());
		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsWoEnableCheck);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableCheck;
		cb.setText("是否记录节点信息？");
		cb.setChecked(ath.getIsWoEnableCheck());
		this.Pub1.append(BaseModel.AddTD(cb));
		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsWoEnableInsertFlow);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableInsertFlow;
		cb.setText("是否启用插入流程？");
		cb.setChecked(ath.getIsWoEnableInsertFlow());
		this.Pub1.append(BaseModel.AddTD(cb));
		cb = Pub1.creatCheckBox("CB_"
				+ FrmAttachmentAttr.IsWoEnableInsertFengXian);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableInsertFengXian;
		cb.setText("是否启用插入风险点？");
		cb.setChecked(ath.getIsWoEnableInsertFengXian());
		this.Pub1.append(BaseModel.AddTD(cb));
		this.Pub1.append(BaseModel.AddTR());
		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsWoEnableMarks);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableMarks;
		cb.setText("是否进入留痕模式？");
		cb.setChecked(ath.getIsWoEnableMarks());
		this.Pub1.append(BaseModel.AddTD(cb));
		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.IsWoEnableDown);
		// cb.ID = "CB_" + FrmAttachmentAttr.IsWoEnableDown;
		cb.setText("是否启用下载？");
		cb.setChecked(ath.getIsWoEnableDown());
		this.Pub1.append(BaseModel.AddTD(cb));
		this.Pub1.append(BaseModel.AddTD(""));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTREnd());
		// #endregion WebOffice控制方式.

		// #region 快捷键生成规则.
		this.Pub1.append(BaseModel.AddTR1());
		this.Pub1.append(BaseModel.AddTDTitle("colspan=3", "快捷键生成规则."));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		cb = Pub1.creatCheckBox("CB_" + FrmAttachmentAttr.FastKeyIsEnable);
		// cb.ID = "CB_" + FrmAttachmentAttr.FastKeyIsEnable;
		cb.setText("是否启用生成快捷键？(启用就会按照规则生成放在附件的同一个目录里面)");
		cb.setChecked(ath.getFastKeyIsEnable());
		this.Pub1.append(BaseModel.AddTD("colspan=3", cb));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		tb = Pub1.creatTextBox("TB_" + FrmAttachmentAttr.FastKeyGenerRole);
		// tb.ID = "TB_" + FrmAttachmentAttr.FastKeyGenerRole;
		tb.setText(ath.getFastKeyGenerRole());
		tb.setCols(30);
		this.Pub1.append(BaseModel.AddTD("colspan=3", tb));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("colspan=3", "格式:*FiledName.*OID"));
		this.Pub1.append(BaseModel.AddTREnd());
		// #endregion 快捷键生成规则.

		// #region 保存按钮.
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD(""));
		Button btn = Pub1.creatButton("Btn_Save");
		// btn.ID = "Btn_Save";
		btn.setText("  Save  ");
		btn.setCssClass("Btn");
		btn.addAttr("onclick", "btn_Save()");
		// btn.Click += new EventHandler(btn_Click);
		this.Pub1.append(BaseModel.AddTD(btn));

		if (getAth() != null) {
			btn = Pub1.creatButton("Btn_Delete");
			// btn.ID = "Btn_Delete";
			btn.setText("  Delete  ");
			btn.setCssClass("Btn");
			// btn.addAttr("onclick", " return confirm('您确认吗？');");
			// btn.Attributes["onclick"] = " return confirm('您确认吗？');";
			btn.addAttr("onclick", "btn_Del()");
			// btn.Click += new EventHandler(btn_Click);
			this.Pub1.append(BaseModel.AddTD(btn));
		} else {
			this.Pub1.append(BaseModel.AddTD());
		}
		this.Pub1.append(BaseModel.AddTREnd());
		// #endregion 保存按钮.

		this.Pub1.append(BaseModel.AddTableEnd());
	}

}
