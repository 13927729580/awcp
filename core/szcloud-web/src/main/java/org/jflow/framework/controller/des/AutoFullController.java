package org.jflow.framework.controller.des;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.system.ui.core.BaseWebControl;
import org.jflow.framework.system.ui.core.DDL;
import org.jflow.framework.system.ui.core.HtmlUtils;
import org.jflow.framework.system.ui.core.RadioButton;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.En.Attr;
import BP.En.Attrs;
import BP.Sys.Frm.MapAttr;
import BP.Sys.Frm.MapAttrs;
import BP.Sys.Frm.MapData;
import BP.Sys.Frm.MapDtl;
import BP.Sys.Frm.MapDtls;
import BP.Sys.Frm.MapExt;
import BP.Sys.Frm.MapExtXmlList;

@Controller
@RequestMapping("/WF/MapDef/")
public class AutoFullController {
	/**
	 * 日志对象
	 */
	private static Log logger = LogFactory.getLog(AutoFullController.class);
	PrintWriter out;

	@RequestMapping(value = "/save_data", method = RequestMethod.POST)
	public void save(HttpServletRequest request, HttpServletResponse response, String RefNo, String FK_MapData) {
		String object = request.getParameter("formHtml");
		RefNo = request.getParameter("refNo");
		FK_MapData = request.getParameter("fK_MapData");
		HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(object, request);
		MapAttr mattrNew = new MapAttr(RefNo);
		try {
			out = response.getWriter();
		} catch (IOException e) {
			logger.info("ERROR", e);
		}
		MapExt me = new MapExt();
		me.setMyPK(RefNo + "_AutoFull");
		me.RetrieveFromDBSources();
		me.setFK_MapData(FK_MapData);
		me.setAttrOfOper(mattrNew.getKeyOfEn());
		me.setExtType(MapExtXmlList.AutoFull);
		RadioButton rb = (RadioButton) controls.get("RB_Way_0");
		if (rb.getChecked()) {
			me.setTag("0");
		}

		// JS 方式。
		RadioButton rb1 = (RadioButton) controls.get("RB_Way_1");
		if (rb1.getChecked()) {
			me.setTag("1");
			me.setDoc(request.getParameter("TB_JS"));

			/* 检查字段是否填写正确. */
			MapAttrs attrsofCheck = new MapAttrs(FK_MapData);
			String docC = me.getDoc();
			for (MapAttr attrC : MapAttrs.convertMapAttrs(attrsofCheck)) {
				if (attrC.getIsNum() == false)
					continue;
				docC = docC.replace("@" + attrC.getKeyOfEn(), "");
				docC = docC.replace("@" + attrC.getName(), "");
			}

			if (docC.contains("@")) {
				out.print("您填写的表达公式不正确，导致一些数值类型的字段没有被正确的替换。" + docC);
				return;
			}
		}

		// 外键方式。
		RadioButton rb2 = (RadioButton) controls.get("RB_Way_2");
		if (rb2.getChecked()) {
			me.setTag("2");
			me.setDoc(request.getParameter("TB_SQL"));

			// mattr.HisAutoFull = AutoFullWay.Way2_SQL;
			// mattr.AutoFullDoc = this.Pub1.GetTextBoxByID("TB_SQL").getText(;
		}

		// 本表单中外键列。
		String doc = "";
		RadioButton rb3 = (RadioButton) controls.get("RB_Way_3");
		if (rb3.getChecked()) {
			me.setTag("3");

			// mattr.HisAutoFull = AutoFullWay.Way3_FK;
			MapData md = new MapData(FK_MapData);
			Attrs attrs = md.GenerHisMap().getHisFKAttrs();
			for (Attr attr : attrs) {
				if (attr.getIsRefAttr())
					continue;
				RadioButton rb4 = (RadioButton) controls.get("RB_FK_" + attr.getKey());
				if (rb4.getChecked() == false)
					continue;
				// doc = " SELECT " + this.Pub1.GetDDLByID("DDL_" +
				// attr.Key).SelectedValue + " FROM " +
				// attr.HisFKEn.EnMap.PhysicsTable + " WHERE NO=@" + attr.Key;
				DDL ddl = (DDL) controls.get("DDL_" + attr.getKey());
				doc = "@AttrKey=" + attr.getKey() + "@Field=" + ddl.getSelectedItemStringVal() + "@Table="
						+ attr.getHisFKEn().getEnMap().getPhysicsTable();
			}
			me.setDoc(doc);
		}

		// 本表单中从表列。
		RadioButton rb4 = (RadioButton) controls.get("RB_Way_4");
		if (rb4.getChecked()) {
			me.setTag("4");

			MapDtls dtls = new MapDtls(FK_MapData);
			// mattr.HisAutoFull = AutoFullWay.Way4_Dtl;
			for (MapDtl dtl : MapDtls.convertMapDtls(dtls)) {
				try {
					RadioButton rb5 = (RadioButton) controls.get("RB_" + dtl.getNo());
					if (rb5.getChecked() == false)
						continue;
				} catch (Exception e) {
					continue;
				}
				// doc = "SELECT " + this.Pub1.GetDDLByID( "DDL_"+dtl.No +
				// "_Way").SelectedValue + "(" +
				// this.Pub1.GetDDLByID("DDL_"+dtl.No+"_F").SelectedValue + ")
				// FROM " + dtl.No + " WHERE REFOID=@OID";
				DDL dd = (DDL) controls.get("DDL_" + dtl.getNo() + "_F");
				doc = "@Table=" + dtl.getNo() + "@Field=" + dd.getSelectedItemStringVal();
			}
			me.setDoc(doc);
		}

		try {
			me.Save();
		} catch (Exception ex) {
			logger.info("ERROR", ex);
			return;
		}

		out.print("保存成功");

		// BaseModel.Alert("保存成功");
		// Button btn = sender as Button;
		// if (btn.ID.Contains("Close"))
		// {
		// this.WinClose();
		// return;
		// }
		// else
		// {
		// this.Response.Redirect(this.Request.RawUrl, true);
		// }
	}

}
