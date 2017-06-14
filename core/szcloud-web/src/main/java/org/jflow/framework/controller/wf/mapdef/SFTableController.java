package org.jflow.framework.controller.wf.mapdef;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jflow.framework.common.model.BaseModel;
import org.jflow.framework.system.ui.core.BaseWebControl;
import org.jflow.framework.system.ui.core.HtmlUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.En.FieldTypeS;
import BP.En.QueryObject;
import BP.Sys.SFTable;
import BP.Sys.Frm.MapAttr;
import BP.Sys.Frm.MapAttrAttr;
import BP.Sys.Frm.MapAttrs;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import TL.ContextHolderUtils;

@Controller
@RequestMapping("/WF/MapDef")
@Scope("request")
public class SFTableController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	public String getMyPK() {
		String s = ContextHolderUtils.getRequest().getParameter("MyPK");
		return s;
	}

	public String getRefNo(HttpServletRequest request) {
		String s = request.getParameter("RefNo");
		if (StringHelper.isNullOrEmpty(s)) {
			s = request.getParameter("No");
		}
		return s;
	}

	@RequestMapping(value = "/SFTableAdd", method = RequestMethod.POST)
	public void btn_Add_Click(HttpServletRequest request, HttpServletResponse response) {
		String refNo = getRefNo(request);
		SFTable table = new SFTable(refNo);
		if (table.getHisEns().size() == 0) {
			BaseModel.Alert("该表里[" + refNo + "]中没有数据，您需要维护数据才能");
			return;
		}

		try {
			response.sendRedirect(Glo.getCCFlowAppPath() + "WF/MapDef/Do.jsp?DoType=AddSFTableAttr&MyPK="
					+ this.getMyPK() + "&IDX=" + request.getParameter("IDX") + "&RefNo=" + refNo);
		} catch (IOException e) {
			logger.info("ERROR", e);
		}
		BaseModel.WinClose();
		return;
	}

	@RequestMapping(value = "/SFTableEdit", method = RequestMethod.POST)
	public void btn_EditData_Click() {
		// this.Response.Redirect("SFTable.aspx?DoType=Edit&MyPK=" + this.MyPK +
		// "&IDX=" + this.IDX + "&RefNo=" + this.RefNo, true);
		// this.WinClose();
		return;
	}

	@RequestMapping(value = "/SFTableSave", method = RequestMethod.POST)
	public void btn_Save_Click(HttpServletRequest request, HttpServletResponse response) {
		try {
			String fromBody = request.getParameter("BodyHtml");
			HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(fromBody, request);
			SFTable main = new SFTable();
			main = (SFTable) BaseModel.Copy(request, main, null, main.getEnMap(), controls);

			if (main.getNo().length() == 0 || main.getName().length() == 0) {
				// throw new RuntimeException("编号与名称不能为空");
				BaseModel.Alert("编号与名称不能为空!");
				return;
			}

			try {
				main.getHisEns().getGetNewEntity().CheckPhysicsTable();
			} catch (java.lang.Exception e) {
			}

			String refNo = getRefNo(request);
			if (StringHelper.isNullOrEmpty(refNo)) {
				main.setNo(controls.get("TB_No").getText());

				if (main.getIsExits()) {
					String sql = "select No,Name from " + main.getNo() + " WHERE 1=2";
					try {
						BP.DA.DBAccess.RunSQLReturnTable(sql);
					} catch (java.lang.Exception e2) {
						BaseModel.Alert("错误:表或视图不存在No,Name列不符合约定规则  Key=" + main.getNo());
						return;
					}
				}
			} else {
				main.setNo(refNo);
				main.Retrieve();
				main = (SFTable) BaseModel.Copy(request, main, null, main.getEnMap(), controls);
				if (main.getNo().length() == 0 || main.getName().length() == 0) {
					BaseModel.Alert("编号与名称不能为空!");
					return;
					// throw new RuntimeException("编号与名称不能为空");
				}
			}

			if (main.getName().length() == 0) {
				BaseModel.Alert("编号与名称不能为空!");
				return;
				// throw new RuntimeException("编号与名称不能为空");
			}

			if (main.getTableDesc().length() == 0) {
				BaseModel.Alert("描述不能为空!");
				return;
				// throw new RuntimeException("描述不能为空");
			}

			if (StringHelper.isNullOrEmpty(refNo)) {
				// if (main.No.Contains("SF_") == false)
				// throw new Exception("物理表不符合命名规则，必须以 SF_ 开头。");
				// main.FK_Val = main.No.Replace("SF_", "FK_");
				main.setFK_Val(main.getNo()); // .Replace("SF_", "FK_");
			}

			// string cfgVal = "";
			// int idx = -1;
			// while (idx < 19)
			// {
			// idx++;
			// string t = this.Ucsys1.GetTBByID("TB_" + idx).Text.Trim();
			// if (t.Length == 0)
			// continue;
			// cfgVal += "@" + idx + "=" + t;
			// }
			// main.CfgVal = cfgVal;
			// if (main.CfgVal == "")
			// throw new Exception("错误，您必须输入表，请参考帮助。");
			// main.IsDel = true;
			main.Save();

			// 重新生成
			response.sendRedirect(Glo.getCCFlowAppPath() + "WF/MapDef/SFTable.jsp?RefNo=" + main.getNo() + "&MyPK="
					+ this.getMyPK() + "&IDX=" + this.getIDX(request));
		} catch (RuntimeException ex) {
			BaseModel.Alert(ex.getMessage());
		} catch (IOException e) {
			logger.info("ERROR", e);
		}
	}

	private String getIDX(HttpServletRequest request) {
		return request.getParameter("IDX");
	}

	@RequestMapping(value = "/SFTableDel", method = RequestMethod.POST)
	public void btn_Del_Click(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 检查这个类型是否被使用？
			MapAttrs attrs = new MapAttrs();
			QueryObject qo = new QueryObject(attrs);
			qo.AddWhere(MapAttrAttr.MyDataType, FieldTypeS.FK.getValue());
			qo.addAnd();
			qo.AddWhere(MapAttrAttr.KeyOfEn, this.getRefNo(request));
			int i = qo.DoQuery();
			if (i == 0) {
				BP.Sys.SFTable m = new SFTable();
				m.setNo(this.getRefNo(request));
				m.Delete();
				BaseModel.ToMsgPage("外键删除成功");
				return;
			}

			String msg = "错误:下列数据已经引用了外键您不能删除它。";
			for (MapAttr attr : MapAttrs.convertMapAttrs(attrs)) {
				msg += "\t\n" + attr.getField() + "" + attr.getName() + " 表" + attr.getFK_MapData();
			}
			BaseModel.ToErrorPage(msg);
			// throw new RuntimeException(msg);
		} catch (RuntimeException ex) {
			BaseModel.ToErrorPage(ex.getMessage());
		}
	}

}
