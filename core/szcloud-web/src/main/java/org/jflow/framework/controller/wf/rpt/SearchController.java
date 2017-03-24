package org.jflow.framework.controller.wf.rpt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jflow.framework.common.model.TempObject;
import org.jflow.framework.controller.wf.workopt.BaseController;
import org.jflow.framework.system.ui.core.BaseWebControl;
import org.jflow.framework.system.ui.core.HtmlUtils;
import org.jflow.framework.system.ui.core.NamesOfBtn;
import org.jflow.framework.system.ui.core.ToolBar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.Sys.Frm.MapAttr;
import BP.Sys.Frm.MapAttrs;
import BP.Sys.Frm.MapData;
import BP.Tools.FileAccess;

@Controller
@RequestMapping("/WF/Rpt")
public class SearchController extends BaseController {

	@RequestMapping(value = "/Search", method = RequestMethod.POST)
	public ModelAndView execute(TempObject object, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView("WF/Rpt/Search");
		String btnName = request.getParameter("btnName");
		String rptNo = request.getParameter("RptNo");
		switch (NamesOfBtn.getEnumByCode(btnName)) {
		case Export:
		case Excel:
			request.setAttribute("state", null);
			// 生成提示信息，
			response.setContentType("application/vnd.ms-excel");
			String codedFileName = null;
			OutputStream fOut = null;
			codedFileName = java.net.URLEncoder.encode("中文", "UTF-8");
			MapData md = new MapData(rptNo);
			Entities ens = md.getHisEns();
			Entity en = ens.getGetNewEntity();
			QueryObject qo = new QueryObject(ens);
			HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(
					object.getFormHtml(), true);
			for (Map.Entry<String, BaseWebControl> entry : controls.entrySet())// this.UCSys1.Controls)
			{
				String id = entry.getKey();
				if (id.equals("Btn_Export")) {
					ToolBar ToolBar1 = (ToolBar) entry;
					qo = ToolBar1.GetnQueryObject(ens, en);
				}
			}

			String title = en.getEnDesc().trim();
			response.setHeader("content-disposition", "attachment;filename="
					+ title + ".xls");
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet(title);
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short

			// 第四步，创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			DataTable dt = qo.DoQueryToTable();
			DataTable myDT = new DataTable();
			MapAttrs attrs = new MapAttrs(rptNo);
			HSSFRow row = sheet.createRow((int) 0);
			// 创建表头
			for (int i = 0; i < attrs.size(); i++) {
				MapAttr attr = (MapAttr) attrs.get(i);
				// }
				// for (MapAttr attr : attrs)
				// {
				HSSFCell cell1 = row.createCell(i);
				cell1.setCellValue(attr.getName());
				myDT.Columns.Add(new DataColumn(attr.getName(), String.class));
			}

			for (int j = 0; j < dt.Rows.size(); j++) {
				row = sheet.createRow((int) j + 1);
				DataRow dr = dt.Rows.get(j);
				DataRow myDR = myDT.NewRow();
				for (int i = 0; i < attrs.size(); i++) {
					MapAttr attr = (MapAttr) attrs.get(i);
					// }
					// for (MapAttr attr : attrs)
					// {
					HSSFCell cell1 = row.createCell(i,
							HSSFCell.CELL_TYPE_STRING);
					if (dr.getValue(attr.getField()) != null) {
						cell1.setCellValue(dr.getValue(attr.getField())
								.toString());
					} else {
						cell1.setCellValue("");
					}
					// row.createCell(i).setCellValue(dr.getValue(attr.getField()).toString());
					myDR.put(attr.getName(), dr.getValue(attr.getField()));//
					// .Add(attr.getName());
					// =
					// dr[attr.getField()];
				}
				myDT.Rows.add(myDR);
			}
			// ExportDGToExcel(myDT, en.getEnDesc());
			// 第一步，创建一个webbook，对应一个Excel文件
			// int num=0;
			// HSSFRow row = sheet.createRow(num);
			// for (int i = 0; i < myDT.Columns.size(); i++) {
			// HSSFCell cell = row.createCell(i);
			// DataColumn col=myDT.Columns.get(i);
			// cell.setCellValue(col.);
			// }
			// for (int i = 0; i < myDT.Rows.size(); i++) {
			// HSSFRow row = sheet.createRow(i);
			// for (int j = 0; j < myDT.Columns.size(); j++) {
			// HSSFCell cell = row.createCell(j);
			// row.createCell(i).setCellValue(
			// myDT.Columns.get(j).toString());
			// }
			// // cell.setCellValue(myDT.Rows.get(i).getValue(i));
			// }
			// 第六步，将文件存到指定位置
			try {
				fOut = response.getOutputStream();
				wb.write(fOut);
				// FileOutputStream fout = new FileOutputStream(filePath);
				// wb.write(fout);

				return mv;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
				}
				request.setAttribute("state", "open");
				request.setAttribute("PageIdx", "1");
			}
			// file = filename;
			// boolean flag = true;
			// // File filepath = this.Request.PhysicalApplicationPath
			// +"\\Temp\\";
			//
			// // #region 参数及变量设置
			// // //参数校验
			// // if (dg == null || dg.Items.Count <=0 || filename == null ||
			// filename == "" || filepath == null || filepath == "")
			// // return null;
			//
			// //如果导出目录没有建立，则建立.
			// if(!filepath.exists())
			// {
			// try {
			// filepath.createNewFile();
			// } catch (Exception e) {
			// // TODO: handle exception
			// e.printStackTrace();
			// }
			// }
			// // if (Directory.Exists(filepath) == false)
			// // Directory.CreateDirectory(filepath);
			//
			// filename = filepath + filename;
			//
			// //删除原有的重名文件，否则在下载时，会造成下载内容的一些乱码 added by liuxc,2014-11-3
			//
			// File[] files_1 = new File(filename).listFiles();
			// File[] files_2 = new File(filename).listFiles();
			//
			// for (File f : files_1) {
			// for (File file2 : files_2) {
			// if (f.getName() == file2.getName() ||
			// f.length()==file2.length()){
			// //logger.debug(file2.getName());
			// String temp = file2.getPath();
			// if (file2.delete())
			// logger.debug(temp + "\t\t<---已经被删除");
			// }
			// }
			// }
			// // if (File.Exists(filename))
			// // File.Delete(filename);
			//
			// FileInputStream fis=new FileInputStream(filename);
			//
			// FileStream objFileStream = new FileStream(filename,
			// FileMode.OpenOrCreate, FileAccess.Write);
			// StreamWriter objStreamWriter = new StreamWriter(objFileStream,
			// System.Text.Encoding.Unicode);
			// // #endregion
			// //
			// // #region 生成导出文件
			// try
			// {
			// objStreamWriter.WriteLine(Convert.ToChar(9) + title +
			// Convert.ToChar(9));
			// String strLine = "";
			// //生成文件标题
			// for (DataColumn attr : dt.Columns)
			// {
			// strLine = strLine + attr.ColumnName + Convert.ToChar(9);
			// }
			//
			// objStreamWriter.WriteLine(strLine);
			// strLine = "";
			// for (DataRow dr in dt.Rows)
			// {
			// for (DataColumn attr in dt.Columns)
			// {
			// strLine = strLine + dr[attr.ColumnName] + Convert.ToChar(9);
			// }
			// objStreamWriter.WriteLine(strLine);
			// strLine = "";
			// }
			// // objStreamWriter.WriteLine();
			// // objStreamWriter.WriteLine(Convert.ToChar(9) + " 制表人：" +
			// Convert.ToChar(9) + WebUser.Name + Convert.ToChar(9) + "日期：" +
			// Convert.ToChar(9) + DateTime.Now.ToShortDateString());
			// }
			// catch(Exception e)
			// {
			// flag = false;
			// }
			// finally
			// {
			// objStreamWriter.Close();
			// objFileStream.Close();
			// }
			// // #endregion
			// //
			// // #region 删除掉旧的文件
			// // //DelExportedTempFile(filepath);
			// // #endregion
			//
			// if (flag)
			// {
			//
			// BP.Web.Ctrl.Glo MyFileDown = new BP.Web.Ctrl.Glo();
			// MyFileDown.DownFileByPath(filename,
			// file);
			// //this.WinOpen(this.Request.ApplicationPath + "/Temp/" +
			// file,"down",90,90);
			// //this.Write_Javascript(" window.open('"+ Request.ApplicationPath
			// + @"/Report/Exported/" + filename +"'); " );
			// //this.Write_Javascript(" window.open('"+Request.ApplicationPath+"/Temp/"
			// + file +"'); " );
			// }
			//
			// return file;
			// } catch (Exception ex) {
			// try {
			// // throw new Exception(
			// // "数据没有正确导出可能的原因之一是:系统管理员没正确的安装Excel组件，请通知他，参考安装说明书解决。@系统异常信息："
			// // + ex.getMessage());
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			break;
		default:
			request.setAttribute("PageIdx", "1");
			// this.ToolBar1.SaveSearchState(this.RptNo, null);
			break;
		}
		return mv;
	}
}
