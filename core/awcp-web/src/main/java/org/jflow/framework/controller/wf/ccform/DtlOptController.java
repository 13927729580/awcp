package org.jflow.framework.controller.wf.ccform;
//package org.jflow.framework.controller.wf.ccform;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.DecimalFormat;
//import java.util.HashMap;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.jflow.framework.common.model.BaseModel;
//import org.jflow.framework.controller.wf.workopt.BaseController;
//import org.jflow.framework.system.ui.core.BaseWebControl;
//import org.jflow.framework.system.ui.core.Button;
//import org.jflow.framework.system.ui.core.HtmlUtils;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import BP.DA.DBAccess;
//import BP.DA.DataColumn;
//import BP.DA.DataRow;
//import BP.DA.DataTable;
//import BP.En.Attr;
//import BP.En.Entities;
//import BP.En.Entity;
//import BP.En.Map;
//import BP.En.QueryObject;
//import BP.Port.WebUser;
//import BP.Sys.GEDtl;
//import BP.Sys.GEDtlAttr;
//import BP.Sys.GEDtls;
//import BP.Sys.GEEntity;
//import BP.Sys.GENoNames;
//import BP.Sys.SysEnum;
//import BP.Sys.SysEnums;
//import BP.Sys.Frm.FrmEvent;
//import BP.Sys.Frm.FrmEventAttr;
//import BP.Sys.Frm.FrmEvents;
//import BP.Sys.Frm.MapData;
//import BP.Sys.Frm.MapDtl;
//import BP.Tools.StringHelper;
//import BP.WF.Template.Node;
//import BP.WF.XML.EventListDtlList;
//import TL.ContextHolderUtils;
//
//@Controller
//@RequestMapping("/WF/CCForm")
//@Scope("request")
//public class DtlOptController extends BaseController{
//	
//
//	public String getDDL_ImpWay()
//	{
//		return "";
//	}
//	@RequestMapping(value = "/DtlOptImport", method = RequestMethod.POST)
//	private void btn_Click(HttpServletRequest request,
//			HttpServletResponse response)
//	{
//		try
//		{
////			BP.Web.Controls.DDL DDL_ImpWay = (BP.Web.Controls.DDL)this.Pub1.FindControl("DDL_ImpWay");
////			System.Web.UI.WebControls.FileUpload fuit = (System.Web.UI.WebControls.FileUpload)this.Pub1.FindControl("fup");
////			if (DDL_ImpWay.SelectedIndex == 0)
////			{
////				this.Alert("请选择导入方式.");
////				return;
////			}
//
//			String tempPath = request.getRemoteAddr() + "\\Temp\\";
//			File file = new File(tempPath);
//			if (!file.exists())
//			{
//				file.createNewFile();
//			}
//
//			MapDtl dtl = new MapDtl(this.getFK_MapDtl());
//
//			//求出扩展名.
//			String fileName = fuit.FileName.toLowerCase();
//			if (fileName.contains(".xls") == false)
//			{
//				this.Alert("上传的文件必须是excel文件.");
//				return;
//			}
//			String ext = ".xls";
//			if (fileName.contains(".xlsx"))
//			{
//				ext = ".xlsx";
//			}
//
//			//保存临时文件.
//			String file = tempPath + WebUser.No + ext;
//			fuit.SaveAs(file);
//
//			GEDtls dtls = new GEDtls(this.getFK_MapDtl());
//			DataTable dt = BP.DA.DBLoad.GetTableByExt(file);
//
//			file = request.getRemoteAddr() + "\\DataUser\\DtlTemplete\\" + this.getFK_MapDtl() + ext;
//			File f = new File(file);
//			if (!f.exists())
//			{
//				if (ext.equals(".xlsx"))
//				{
//					file = request.getRemoteAddr() + "\\DataUser\\DtlTemplete\\" + this.getFK_MapDtl() + ".xls";
//				}
//				else
//				{
//					file = request.getRemoteAddr() + "\\DataUser\\DtlTemplete\\" + this.getFK_MapDtl() + ".xls";
//				}
//			}
//
//			DataTable dtTemplete = BP.DA.DBLoad.GetTableByExt(file);
//
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//				///#region 检查两个文件是否一致。
//			for (DataColumn dc : dtTemplete.Columns)
//			{
//				boolean isHave = false;
//				for (DataColumn mydc : dt.Columns)
//				{
//					if (dc.ColumnName == mydc.ColumnName)
//					{
//						isHave = true;
//						break;
//					}
//				}
//				if (isHave == false)
//				{
//					throw new RuntimeException("@您导入的excel文件不符合系统要求的格式，请下载模版文件重新填入。");
//				}
//			}
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//				///#endregion 检查两个文件是否一致。
//
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//				///#region 生成要导入的属性.
//
//			BP.En.Attrs attrs = dtls.getGetNewEntity().getEnMap().getAttrs();
//			BP.En.Attrs attrsExp = new BP.En.Attrs();
//			for (DataColumn dc : dtTemplete.Columns)
//			{
//				for (Attr attr : attrs)
//				{
//					if (!attr.getUIVisible())
//					{
//						continue;
//					}
//
//					if (attr.getIsRefAttr())
//					{
//						continue;
//					}
//
//					if (dc.ColumnName.trim().equals(attr.getDesc()))
//					{
//						attrsExp.Add(attr);
//						break;
//					}
//				}
//			}
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//				///#endregion 生成要导入的属性.
//
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//				///#region 执行导入数据.
//			if (getDDL_ImpWay().equals("1"))
//			{
//				BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK='" + this.getWorkID() + "'");
//			}
//
//			int i = 0;
//			long oid = BP.DA.DBAccess.GenerOID(this.getFK_MapDtl(), dt.Rows.size());
//			String rdt = BP.DA.DataType.getCurrentData();
//
//			String errMsg = "";
//			for (DataRow dr : dt.Rows)
//			{
//				GEDtl dtlEn = (GEDtl)((dtls.getGetNewEntity() instanceof GEDtl) ? dtls.getGetNewEntity() : null);
//				dtlEn.ResetDefaultVal();
//
//				for (BP.En.Attr attr : attrsExp)
//				{
//					if (!attr.getUIVisible() || dr.getValue(attr.getDesc()) == null)
//					{
//						continue;
//					}
//					String val = dr.getValue(attr.getDesc()).toString();
//					if (val == null)
//					{
//						continue;
//					}
//					val = val.trim();
//					switch (attr.getMyFieldType())
//					{
//						case Enum:
//						case PKEnum:
//							SysEnums ses = new SysEnums(attr.getUIBindKey());
//							boolean isHavel = false;
//							for (SysEnum se : SysEnums.convertSysEnums(ses))
//							{
//								if (val.equals(se.getLab()))
//								{
//									val = String.valueOf(se.getIntKey());
//									isHavel = true;
//									break;
//								}
//							}
//							if (isHavel == false)
//							{
//								errMsg += "@数据格式不规范,第(" + i + ")行，列(" + attr.getDesc() + ")，数据(" + val + ")不符合格式,改值没有在枚举列表里.";
//								val = attr.getDefaultVal().toString();
//							}
//							break;
//						case FK:
//						case PKFK:
//							Entities ens = null;
//							if (attr.getUIBindKey().contains("."))
//							{
//								ens = BP.En.ClassFactory.GetEns(attr.getUIBindKey());
//							}
//							else
//							{
//								ens = new GENoNames(attr.getUIBindKey(), "desc");
//							}
//
//							ens.RetrieveAll();
//							boolean isHavelIt = false;
//							for (Entity en : Entities.convertEntities(ens))
//							{
//								if (val.equals(en.GetValStrByKey("Name")))
//								{
//									val = en.GetValStrByKey("No");
//									isHavelIt = true;
//									break;
//								}
//							}
//							if (isHavelIt == false)
//							{
//								errMsg += "@数据格式不规范,第(" + i + ")行，列(" + attr.getDesc() + ")，数据(" + val + ")不符合格式,改值没有在外键数据列表里.";
//							}
//							break;
//						default:
//							break;
//					}
//
//					if (attr.getMyDataType() == BP.DA.DataType.AppBoolean)
//					{
//						if (val.trim().equals("是") || val.trim().toLowerCase().equals("yes"))
//						{
//							val = "1";
//						}
//
//						if (val.trim().equals("否") || val.trim().toLowerCase().equals("no"))
//						{
//							val = "0";
//						}
//					}
//
//					dtlEn.SetValByKey(attr.getKey(), val);
//				}
//				dtlEn.setRefPKInt((int)this.getWorkID());
//				dtlEn.SetValByKey("RDT", rdt);
//				dtlEn.SetValByKey("Rec", WebUser.getNo());
//				i++;
//
//				dtlEn.InsertAsOID(oid);
//				oid++;
//			}
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//				///#endregion 执行导入数据.
//
//			if (StringHelper.isNullOrEmpty(errMsg))
//			{
//				this.Alert("共有(" + i + ")条数据导入成功。");
//			}
//			else
//			{
//				this.Alert("共有(" + i + ")条数据导入成功，但是出现如下错误:" + errMsg);
//			}
//		}
//		catch (RuntimeException ex)
//		{
//			String msg = ex.getMessage().replace("'", "‘");
//			this.Alert(msg);
//		}
//	}
//
//	@RequestMapping(value = "/DtlDelUnPass", method = RequestMethod.POST)
//	private void btn_DelUnPass_Click(HttpServletRequest request,
//			HttpServletResponse response)
//	{
//		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
//		Node nd = new Node(dtl.getFK_MapData());
//		MapData md = new MapData(dtl.getFK_MapData());
//
//		String starter = "SELECT Rec FROM " + md.getPTable() + " WHERE OID=" + this.getWorkID();
//		starter = BP.DA.DBAccess.RunSQLReturnString(starter);
//		GEDtls geDtls = new GEDtls(this.getFK_MapDtl());
//		geDtls.Retrieve(GEDtlAttr.Rec, starter, "IsPass", "0");
//		for (GEDtl item : GEDtls.convertGEDtls(geDtls))
//		{
//			if (this.Pub1.GetCBByID("CB_" + item.OID).Checked == false)
//			{
//				continue;
//			}
//			item.Delete();
//		}
//		this.Response.Redirect(this.Request.RawUrl, true);
//	}
//	
//	@RequestMapping(value = "/DtlImp", method = RequestMethod.POST)
//	private void btn_Imp_Click(HttpServletRequest request,
//			HttpServletResponse response)
//	{
//		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
//		Button btn = (Button)((sender instanceof Button) ? sender : null);
//		if (btn.ID.Contains("ImpClear"))
//		{
//			//如果是清空方式导入。
//			BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.PTable + " WHERE RefPK='" + this.getWorkID() + "'");
//		}
//
//		Node nd = new Node(dtl.getFK_MapData());
//		MapData md = new MapData(dtl.getFK_MapData());
//
//		String starter = "SELECT Rec FROM " + md.getPTable() + " WHERE OID=" + this.getWorkID();
//		starter = BP.DA.DBAccess.RunSQLReturnString(starter);
//		GEDtls geDtls = new GEDtls(this.getFK_MapDtl());
//		geDtls.Retrieve(GEDtlAttr.Rec, starter, "IsPass", "0");
//
//		String strs = "";
//		for (GEDtl item : GEDtls.convertGEDtls(geDtls))
//		{
//			if (!this.Pub1.GetCBByID("CB_" + item.getOID()).Checked)
//			{
//				continue;
//			}
//			strs += ",'" + item.getOID() + "'";
//		}
//		if (strs.equals(""))
//		{
//			this.Alert("请选择要执行的数据。");
//			return;
//		}
//		strs = strs.substring(1);
//		BP.DA.DBAccess.RunSQL("UPDATE  " + dtl.getPTable() + " SET RefPK='" + this.getWorkID() + "',BatchID=0,Check_Note='',Check_RDT='" + BP.DA.DataType.getCurrentDataTime() + "', Checker='',IsPass=1  WHERE OID IN (" + strs + ")");
//		//this.WinClose();
//	}
//
//
//}
