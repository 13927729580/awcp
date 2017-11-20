package org.jflow.framework.common.model;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.system.ui.core.Button;
import org.jflow.framework.system.ui.core.CheckBox;
import org.jflow.framework.system.ui.core.DDL;
import org.jflow.framework.system.ui.core.TextBox;

import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entity;
import BP.Sys.UIConfig;
import BP.Tools.StringHelper;

public class EnsAppCfgModel extends BaseModel {

	public EnsAppCfgModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	private StringBuffer ucSys1;

	public String getUcSys1() {
		return ucSys1.toString();
	};

	public String getEnsName() {
		if (StringHelper.isNullOrEmpty(get_request().getParameter("EnsName"))) {
			return "BP.GE.Infos";
		}
		return get_request().getParameter("EnsName");
	}

	public final void BindSelectCols() {
		ucSys1 = new StringBuffer();
		ucSys1.append(AddTable("width=100%"));

		ucSys1.append(AddCaptionLeftTX("<a href='?EnsName=" + this.getEnsName()
				+ "'>基本配置</a> - <b>选择列</b> - <a href='EnsDataIO.jsp?EnsName="
				+ this.getEnsName() + "' >数据导入导出</a>"));

		ucSys1.append(AddTR());
		ucSys1.append(AddTDTitle("style='width:60px;text-align:center'", "序"));
		ucSys1.append(AddTDTitle(
				"style='width:40px;text-align:center'",
				"<input type='checkbox' onclick=\"javascript:$('input[type=checkbox]').attr('checked', this.checked)\" />"));
		ucSys1.append(AddTDTitle("列"));
		ucSys1.append(AddTREnd());

		Entity en = BP.En.ClassFactory.GetEns(this.getEnsName()).getGetNewEntity();
		Attrs attrs = en.getEnMap().getHisCfgAttrs();
		UIConfig cfg = new UIConfig(en);
		List<String> showColumns = Arrays.asList(cfg.getShowColumns());
//		boolean is1 = false;
		int idx = 1;
		CheckBox cb = null;
		for (Attr attr : attrs) {
			if (attr.getUIVisible() == false) {
				continue;
			}

			if (attr.getKey().equals("MyNum")) {
				continue;
			}

			// is1 = ucSys1.append(AddTR(is1);
			ucSys1.append(AddTDIdx(idx++));

			cb = new CheckBox();
			cb.setId("CB_" + attr.getKey());
			cb.setChecked(showColumns.contains(attr.getKey()));
			ucSys1.append(AddTD("style='text-align:center'", cb));

			ucSys1.append(AddTD("<label for='" + cb.getId() + "'>"
					+ attr.getDesc() + "</label>"));

			ucSys1.append(AddTREnd());
		}

		ucSys1.append(AddTableEnd());

		Button btns = new Button();
		btns.setId("Btn_Save");
		btns.setCssClass("Btn");
		btns.setText("保存");
		btns.addAttr("onclick", "btn_Click('Btn_Save')");
		ucSys1.append(btns);

		btns = new Button();
		btns.setId("Btn_SaveAndClose");
		btns.setCssClass("Btn");

		btns.setText("保存并关闭");
		btns.addAttr("onclick", "btn_Click('Btn_SaveAndClose')");
		ucSys1.append(btns);
	}
	
	public final void BindAdv()
	{
		ucSys1 = new StringBuffer();
		ucSys1.append(AddTable("width=100%"));

		ucSys1.append(AddCaptionLeftTX("<b>基本配置</b> - <a href='?EnsName=" + this.getEnsName() + "&DoType=SelectCols'>选择列</a> - <a href='EnsDataIO.jsp?EnsName=" + this.getEnsName() + "' >数据导入导出</a>"));

		ucSys1.append(AddTR());
		ucSys1.append(AddTDTitle("配置项"));
		ucSys1.append(AddTDTitle("内容"));
		ucSys1.append(AddTDTitle("信息"));
		ucSys1.append(AddTREnd());

		Entity en1 = BP.En.ClassFactory.GetEns(this.getEnsName()).getGetNewEntity();
		Attrs attrs = en1.getEnMap().getHisCfgAttrs();
		UIConfig cfg = new UIConfig(en1);
//		boolean is1 = false;
		for (Attr attr : attrs)
		{
			if (attr.getIsRefAttr())
			{
				continue;
			}

			if (attr.getUIVisible() == false) //added by liuxc,2015-8-7
			{
				continue;
			}

//			is1 = ucSys1.append(AddTR(is1);
			ucSys1.append(AddTD(attr.getKey()));
			ucSys1.append(AddTD(attr.getDesc()));
			switch (attr.getUIContralType())
			{
				case DDL:
					DDL ddl = new DDL();
					ddl.setId("DDL_" + attr.getKey());

//					SysEnums ses = new SysEnums(attr.getKey(), attr.UITag);
					ddl.BindSysEnum(attr.getKey());

					if (StringHelper.isNullOrEmpty(cfg.HisAP.GetValStrByKey(attr.getKey())))
					{
						ddl.SetSelectItem(attr.getDefaultVal().toString());
					}
					else
					{
						ddl.SetSelectItem(cfg.HisAP.GetValIntByKey(attr.getKey()));
					}

					ucSys1.append(AddTD(ddl));
					break;
				case CheckBok:
					CheckBox cb = new CheckBox();
					cb.setId("CB_" + attr.getKey());
					cb.setText(attr.getDesc());
					if (StringHelper.isNullOrEmpty(cfg.HisAP.GetValStrByKey(attr.getKey())))
					{
						if (attr.getDefaultVal().toString().equals("0"))
						{
							cb.setChecked(false);
						}
						else
						{
							cb.setChecked(true);
						}
					}
					else
					{
						cb.setChecked(cfg.HisAP.GetValBoolenByKey(attr.getKey())); // en.CfgValOfBoolen;
					}
					ucSys1.append(AddTD(cb));
					break;
				default:
					TextBox tb = new TextBox();
					tb.setId("TB_" + attr.getKey());
					if (cfg.HisAP.GetValStrByKey(attr.getKey()) == null)
					{
						tb.setText(attr.getDefaultVal().toString());
					}
					else
					{
						tb.setText(cfg.HisAP.GetValStrByKey(attr.getKey()));
					}
					tb.attributes.put("style", "width=100%");
					ucSys1.append(AddTD(tb));
					break;
			}
			ucSys1.append(AddTREnd());
		}

		ucSys1.append(AddTableEnd());
		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setCssClass("Btn");
		btn.setText("保存");
		btn.addAttr("onclick", "btn_Click('Btn_Save')");
		ucSys1.append(btn);

		btn = new Button();
		btn.setId("Btn_SaveAndClose");
		btn.setCssClass("Btn");
		btn.addAttr("onclick", "btn_Click('Btn_SaveAndClose')");
		btn.setText("保存并关闭");
		ucSys1.append(btn);
	}

//	public void BindAdv() {
//		EnsAppCfgs ens = new EnsAppCfgs();
//		ens.Retrieve(EnsAppCfgAttr.EnsName, this.getEnsName());
//
//		if (ens.size() == 0) {
//			uiFatory.append(AddMsgOfInfo("提示：", "没有设置应用程序配置。"));
//			return;
//		}
//
//		uiFatory.append(AddTable1("width=100%"));
//		if (WebUser.getNo().equals("admin")) {
//			/*
//			 * warning uiFatory.append(AddCaptionLeftTX1("<a href='?EnsName=" +
//			 * this.getEnsName() + "'>基本设置</a> - <a href='?EnsName=" +
//			 * this.getEnsName() +
//			 * "&DoType=Adv'>高级设置</a> - <a href='EnsDataIO.aspx?EnsName=" +
//			 * this.getEnsName() + "' >导入导出</a>"));
//			 */
//			uiFatory.append(AddCaptionLeftTX1("<a href='?EnsName="
//					+ this.getEnsName() + "'>基本设置</a> - <a href='?EnsName="
//					+ this.getEnsName() + "&DoType=Adv'>高级设置</a>"));
//		} else {
//			uiFatory.append(AddCaptionLeftTX1("基本设置"));
//		}
//
//		uiFatory.append(AddTR());
//		uiFatory.append(AddTDTitle("配置项"));
//		uiFatory.append(AddTDTitle("内容"));
//		uiFatory.append(AddTDTitle("信息"));
//		uiFatory.append(AddTREnd());
//
//		Entity en1 = BP.En.ClassFactory.GetEns(this.getEnsName())
//				.getGetNewEntity();
//		Attrs attrs = en1.getEnMap().getHisCfgAttrs();
//		// boolean is1 = false;
//		for (Attr attr : Attrs.convertAttrs(attrs)) {
//			if (attr.getIsRefAttr()) {
//				continue;
//			}
//			Object tempVar = ens.GetEntityByKey(EnsAppCfgAttr.CfgKey,
//					attr.getKey());
//			EnsAppCfg en = (EnsAppCfg) ((tempVar instanceof EnsAppCfg) ? tempVar
//					: null);
//			// is1 = uiFatory.append(AddTR(is1));
//			uiFatory.append(AddTD(attr.getKey()));
//			uiFatory.append(AddTD(attr.getDesc()));
//			switch (attr.getUIContralType()) {
//			case DDL:
//				DDL ddl = new DDL();
//				ddl.setId("DDL_" + attr.getKey());
//
//				SysEnums ses = new SysEnums(attr.getKey(), attr.UITag);
//				ddl.BindSysEnum(attr.getKey());
//				if (en == null) {
//					ddl.SetSelectItem(attr.getDefaultVal().toString());
//				} else {
//					ddl.SetSelectItem(en.getCfgValOfInt());
//				}
//
//				uiFatory.append(AddTD(ddl));
//				break;
//			case CheckBok:
//				CheckBox cb = new CheckBox();
//				cb.setId("CB_" + attr.getKey());
//				cb.setText(attr.getDesc());
//				if (en == null) {
//					if (attr.getDefaultVal().toString().equals("0")) {
//						cb.setChecked(false);
//					} else {
//						cb.setChecked(true);
//					}
//				} else {
//					cb.setChecked(en.getCfgValOfBoolen());
//				}
//				uiFatory.append(AddTD(cb));
//				break;
//			default:
//				TextBox tb = new TextBox();
//				tb.setId("TB_" + attr.getKey());
//				if (en == null) {
//					tb.setText(attr.getDefaultVal().toString());
//				} else {
//					if (en.getCfgVal() == null || en.getCfgVal().equals("null")) {
//						tb.setText("");
//					} else {
//						tb.setText(en.getCfgVal());
//					}
//				}
//				tb.addAttr("width", "100%");
//				uiFatory.append(AddTD(tb));
//				break;
//			}
//			uiFatory.append(AddTREnd());
//		}
//
//		uiFatory.append(AddTableEnd());
//		Button btn = new Button();
//		btn.setId("Btn_Save");
//		btn.setName("Btn_Save");
//		btn.setCssClass("Btn");
//		btn.setText("保存");
//		btn.addAttr("onclick", "btn_Click('Btn_Save')");
//		uiFatory.append(btn);
//
//		btn = new Button();
//		btn.setId("Btn_SaveAndClose");
//		btn.setName("Btn_SaveAndClose");
//		btn.setCssClass("Btn");
//
//		btn.setText("保存并关闭");
//		btn.addAttr("onclick", "btn_Click('Btn_SaveAndClose')");
//		uiFatory.append(btn);
//	}

	// public void BindNormal()
	// {
	// EnsAppCfgs ens = new EnsAppCfgs();
	// ens.Retrieve(EnsAppCfgAttr.EnsName, this.getEnsName());
	//
	// EnsAppXmls xmls = new EnsAppXmls();
	// xmls.Retrieve(EnsAppCfgAttr.EnsName, this.getEnsName());
	//
	// uiFatory.append(AddTable1("width=100%"));
	// if (WebUser.getNo().equals("admin"))
	// {
	// /*
	// * warning
	// * uiFatory.append(AddCaptionLeftTX1("<a href='?EnsName=" +
	// this.getEnsName() + "'>基本设置</a> - <a href='?EnsName=" + this.getEnsName()
	// + "&DoType=Adv'>高级设置</a> - <a href='EnsDataIO.aspx?EnsName=" +
	// this.getEnsName() + "' >导入导出</a>"));*/
	// uiFatory.append(AddCaptionLeftTX1("<a href='?EnsName=" +
	// this.getEnsName() + "'>基本设置</a> - <a href='?EnsName=" + this.getEnsName()
	// + "&DoType=Adv'>高级设置</a>"));
	// }
	// else
	// {
	// uiFatory.append(AddCaptionLeftTX1("基本设置"));
	// }
	// uiFatory.append(AddTR());
	//
	// uiFatory.append(AddTR());
	// uiFatory.append(AddTDTitle("配置项"));
	// uiFatory.append(AddTDTitle("内容"));
	// uiFatory.append(AddTDTitle("信息"));
	// uiFatory.append(AddTDTitle("备注"));
	//
	// //this.UCSys1.AddTDTitle("配置项");
	// //this.UCSys1.AddTDTitle("内容");
	// //this.UCSys1.AddTDTitle("信息");
	// //this.UCSys1.AddTDTitle("备注");
	// uiFatory.append(AddTREnd());
	//
	// boolean is1 = false;
	// for (EnsAppXml xml : EnsAppXmls.convertEnsAppXmls(xmls))
	// {
	// Object tempVar = ens.GetEntityByKey(EnsAppCfgAttr.CfgKey, xml.getNo());
	// EnsAppCfg en = (EnsAppCfg)((tempVar instanceof EnsAppCfg) ? tempVar :
	// null);
	// // is1 = uiFatory.append(AddTR(is1));
	// uiFatory.append(AddTD(xml.getNo()));
	// uiFatory.append(AddTD(xml.getName()));
	// //C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a string
	// member and was converted to Java 'if-else' logic:
	// // switch (xml.DBType)
	// //ORIGINAL LINE: case "Enum":
	// if (xml.getDBType().equals("Enum"))
	// {
	// DDL ddl = new DDL();
	// ddl.setId("DDL_" + xml.getNo());
	//
	// SysEnums ses = new SysEnums(xml.getEnumKey(), xml.getEnumVals());
	// ddl.BindSysEnum(xml.getEnumKey());
	//
	// if (en == null)
	// {
	// ddl.SetSelectItem(xml.getDefValInt());
	// }
	// else
	// {
	// ddl.SetSelectItem(en.getCfgValOfInt());
	// }
	// uiFatory.append(AddTD(ddl));
	// }
	// //ORIGINAL LINE: case "Boolen":
	// else if (xml.getDBType().equals("Boolen"))
	// {
	// CheckBox cb = new CheckBox();
	// cb.setId("CB_" + xml.getNo());
	// cb.setText(xml.getName());
	// if (en == null)
	// {
	// cb.setChecked(xml.getDefValBoolen());
	// }
	// else
	// {
	// cb.setChecked(en.getCfgValOfBoolen());
	// }
	// uiFatory.append(AddTD(cb));
	// }
	// else
	// {
	// TextBox tb = new TextBox();
	// tb.setId("TB_" + xml.getNo());
	// if (en == null)
	// {
	// tb.setText(xml.getDefVal());
	// }
	// else
	// {
	// if(en.getCfgVal()==null || en.getCfgVal().equals("null")){
	// tb.setText("");
	// }else{
	// tb.setText(en.getCfgVal());
	// }
	// }
	// tb.addAttr("width", "100%");
	// uiFatory.append(AddTD(tb));
	// }
	// uiFatory.append(AddTDBigDoc(xml.getDesc()));
	// uiFatory.append(AddTREnd());
	// }
	//
	// if (xmls.size() == 0)
	// {
	// uiFatory.append(AddTableEnd());
	// return;
	// }
	//
	//
	// uiFatory.append(AddTableEnd());
	// Button btn = new Button();
	// btn.setId("Btn_Save");
	// btn.setName("Btn_Save");
	// btn.setText("保存");
	// btn.setCssClass("Btn");
	// event wireups:
	// // btn.Click += new EventHandler(btn_Click);
	// btn.addAttr("onclick", "btn_Click('Btn_Save')");
	// uiFatory.append(btn);
	//
	// btn = new Button();
	// btn.setId("Btn_SaveAndClose");
	// btn.setName("Btn_SaveAndClose");
	// btn.setCssClass("Btn");
	// btn.setText("保存并关闭");
	// event wireups:
	// // btn.Click += new EventHandler(btn_Click);
	// btn.addAttr("onclick", "btn_Click('Btn_SaveAndClose')");
	// uiFatory.append(btn);
	// }

	public void init() {
		if (this.getDoType().equals("SelectCols")) {
			this.BindSelectCols();
		} else {
			this.BindAdv();
		}
	}

}
