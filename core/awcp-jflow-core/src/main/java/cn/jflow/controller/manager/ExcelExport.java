package cn.jflow.controller.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;

@Controller
@RequestMapping(value="/DES")
public class ExcelExport {
	
	@RequestMapping(value="/TXExport",method=RequestMethod.GET)
	public void  txExport(HttpServletResponse response) throws IOException{
		OutputStream os = response.getOutputStream();// 取得输出流   
        response.reset();// 清空输出流   
        String filename="区县汇总.xls";
        String downLoadName = new String(filename.getBytes("gbk"), "iso8859-1"); 
        response.setHeader("Content-disposition", "attachment; filename="+downLoadName);// 设定输出文件头   
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        String sql="select (select Name from TX_QX where TX_QX.No= ND124Rpt.FK_QX1) FK_QX,(select lab from Sys_Enum where Sys_Enum.IntKey=ND124Rpt.DuiXiangLeiXing and Sys_Enum.EnumKey='duixiangleixing') DuiXiangLeiXing,COUNT(*) as HuShu,SUM(ND128Rpt.FaFangJinE) as JinE from ND124Rpt,ND128Rpt where ND124Rpt.BillNo in(select * from f_split(ND128Rpt.YinCangYu,',')) and ND124Rpt.BillNo!='' group by ND124Rpt.FK_QX1,ND124Rpt.DuiXiangLeiXing";
		
        DataTable dt=DBAccess.RunSQLReturnTable(sql);
		
		try {   
            HSSFWorkbook wb = new HSSFWorkbook();   
            HSSFSheet sheet = wb.createSheet("街道查询excel");   
            HSSFCellStyle style = wb.createCellStyle(); // 样式对象   
  
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直   
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平   
            HSSFRow row = sheet.createRow( 0);    
  

            //合並單元格,下標從0開始
            sheet.addMergedRegion(new CellRangeAddress(0,0,0, (dt.Columns.size()-1)));     
            
            HSSFRow rowT = sheet.createRow(0);
            HSSFCell cellT = rowT.createCell(0);
            
            cellT.setCellValue("Excel");
            int count=0;
            CellRangeAddress cra1=null;
            for(int i=1;i<=dt.Rows.size();i++){
            	HSSFRow hr=sheet.createRow(i+count);
            	DataRow dr=dt.Rows.get(i-1);
            	for(int j=0;j<dr.columns.size();j++){
            		HSSFCell hc=hr.createCell(j);
            		hc.setCellValue(""+dr.getValue(dr.columns.get(j)));
            	}
            	if(i-1>0){
            		//判断本行街道名称与上一行街道名称是否相同   相同则合并单元格
            		if(dt.Rows.get(i-1).getValue(dt.Columns.get(0)).equals(dt.Rows.get(i-2).getValue(dt.Columns.get(0)))){
            			if(cra1==null){
            				cra1=new CellRangeAddress((i-1+count), 0, i+count,0);
            			}
            			else{
            				cra1.setLastRow(cra1.getLastRow()+1);
            			}
            		}else{
            			if(cra1!=null){
            				sheet.shiftRows(sheet.getLastRowNum(), sheet.getLastRowNum(), 1,true,false);
            				
            				HSSFRow hr1=sheet.createRow(i+count);
            				HSSFCell hc1=hr1.createCell(1);
            				hc1.setCellValue("本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra1.getFirstRow();k<=cra1.getLastRow();k++){
            					sum+=Integer.parseInt(sheet.getRow(k).getCell(2).getStringCellValue());
            					db+=Double.parseDouble(sheet.getRow(k).getCell(3).getStringCellValue());
            				}
            				hr1.createCell(2).setCellValue(sum);
            				hr1.createCell(3).setCellValue(db);
            				cra1.setLastRow(cra1.getLastRow()+1);
            				sheet.addMergedRegion(cra1);
            				
            				cra1=null;
            			}
            		}
            		
            		if(i==dt.Rows.size()){
            			if(cra1!=null){
            				
            				HSSFRow hr1=sheet.createRow(sheet.getLastRowNum()+1);
            				count=count+1;
            				HSSFCell hc1=hr1.createCell(1);
            				hc1.setCellValue("本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra1.getFirstRow()-count;k<=cra1.getLastRow()-count;k++){
            					sum+=Integer.parseInt(dt.getValue(k, 2).toString());
            					db+=Double.parseDouble(dt.getValue(k, 3).toString());
            				}
            				hr1.createCell(2).setCellValue(sum);
            				hr1.createCell(3).setCellValue(db);
            				cra1.setLastRow(cra1.getLastRow()+1);
            				sheet.addMergedRegion(cra1);
            				
            				cra1=null;
            			}
            		}
            	}
            }
            HSSFRow hs=sheet.createRow(sheet.getLastRowNum()+1);
            if(cra1!=null) {
                sheet.addMergedRegion(cra1);
            }
            HSSFCell hc2=hs.createCell(0);
            hc2.setCellValue("总计：");
            int i=0;
            double j=0;
            for(DataRow dr:dt.Rows){
            	i+=Integer.parseInt(dr.getValue(2).toString());
            	j+=Double.parseDouble(dr.getValue(3).toString());
            }
            hs.createCell(2).setCellValue(i);
            hs.createCell(3).setCellValue(j);
            
            sheet.setColumnWidth( 0,  5000);
            sheet.setColumnWidth( 1,  6000);
            sheet.setColumnWidth( 2,  4000);
            sheet.setColumnWidth( 3,  3000);
            
            wb.write(os);; // 写入文件   
            wb.close();  
            os.close(); // 关闭流
             
        } catch (Exception ex) {   
            ex.printStackTrace();   
        }  
	}
	
	
	@RequestMapping(value="/JDExport",method=RequestMethod.GET)
	public void  jdExport(HttpServletResponse response) throws IOException{
		OutputStream os = response.getOutputStream();// 取得输出流   
        response.reset();// 清空输出流   

        String filename="街道汇总.xls";
        String downLoadName = new String(filename.getBytes("gbk"), "iso8859-1"); 
        response.setHeader("Content-disposition", "attachment; filename="+downLoadName);// 设定输出文件头   
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        String sql="select TX_JD.Name as FK_JD,Sys_Enum.Lab,COUNT(*) as HuShu,SUM(ND128Rpt.FaFangJinE) as JinE from ND124Rpt,ND128Rpt,TX_JD,Sys_Enum where ND124Rpt.BillNo in(select * from f_split(ND128Rpt.YinCangYu,',')) and ND124Rpt.BillNo!='' and TX_JD.No=ND124Rpt.FK_QX and Sys_Enum.EnumKey='DuiXiangLeiXing' and Sys_Enum.IntKey=ND124Rpt.DuiXiangLeiXing group by Sys_Enum.Lab,TX_JD.Name";
		
        DataTable dt=DBAccess.RunSQLReturnTable(sql);
		
		try {   
            HSSFWorkbook wb = new HSSFWorkbook();   
            HSSFSheet sheet = wb.createSheet("街道查询excel");   
            HSSFCellStyle style = wb.createCellStyle(); // 样式对象   
  
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直   
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平   
            HSSFRow row = sheet.createRow( 0);    
  
            //sheet.addMergedRegion(new Region(0,  0, 1,  0));   

            //合並單元格,下標從0開始
            sheet.addMergedRegion(new CellRangeAddress(0,0,0, (dt.Columns.size()-1)));     
            
            HSSFRow rowT = sheet.createRow(0);
            HSSFCell cellT = rowT.createCell(0);
            
            cellT.setCellValue("Excel");
            int count=0;
            CellRangeAddress cra1=null;
            for(int i=1;i<=dt.Rows.size();i++){
            	HSSFRow hr=sheet.createRow(i+count);
            	DataRow dr=dt.Rows.get(i-1);
            	for(int j=0;j<dr.columns.size();j++){
            		HSSFCell hc=hr.createCell(j);
            		hc.setCellValue(""+dr.getValue(dr.columns.get(j)));
            	}
            	if(i-1>0){
            		//判断本行街道名称与上一行街道名称是否相同   相同则合并单元格
            		if(dt.Rows.get(i-1).getValue(dt.Columns.get(0)).equals(dt.Rows.get(i-2).getValue(dt.Columns.get(0)))){
            			if(cra1==null){
            				cra1=new CellRangeAddress((i-1+count), 0, i+count,0);
            			}
            			else{
            				cra1.setLastRow(cra1.getLastRow()+1);
            			}
            		}else{
            			if(cra1!=null){
            				sheet.shiftRows(sheet.getLastRowNum(), sheet.getLastRowNum(), 1,true,false);
            				
            				HSSFRow hr1=sheet.createRow(i+count);
            				HSSFCell hc1=hr1.createCell(1);
            				hc1.setCellValue("本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra1.getFirstRow();k<=cra1.getLastRow();k++){
            					sum+=Integer.parseInt(sheet.getRow(k).getCell(2).getStringCellValue());
            					db+=Double.parseDouble(sheet.getRow(k).getCell(3).getStringCellValue());
            				}
            				hr1.createCell(2).setCellValue(sum);
            				hr1.createCell(3).setCellValue(db);
            				cra1.setLastRow(cra1.getLastRow()+1);
            				sheet.addMergedRegion(cra1);
            				
            				cra1=null;
            			}
            		}
            		
            		if(i==dt.Rows.size()){
            			if(cra1!=null){
            				
            				HSSFRow hr1=sheet.createRow(sheet.getLastRowNum()+1);
            				count=count+1;
            				HSSFCell hc1=hr1.createCell(1);
            				hc1.setCellValue("本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra1.getFirstRow()-count;k<=cra1.getLastRow()-count;k++){
            					sum+=Integer.parseInt(dt.getValue(k, 2).toString());
            					db+=Double.parseDouble(dt.getValue(k, 3).toString());
            				}
            				hr1.createCell(2).setCellValue(sum);
            				hr1.createCell(3).setCellValue(db);
            				cra1.setLastRow(cra1.getLastRow()+1);
            				sheet.addMergedRegion(cra1);
            				
            				cra1=null;
            			}
            		}
            	}
            }
            HSSFRow hs=sheet.createRow(sheet.getLastRowNum()+1);
            sheet.addMergedRegion(new CellRangeAddress(dt.Rows.size()+1,0,dt.Rows.size()+1, 0));
            HSSFCell hc2=hs.createCell(0);
            hc2.setCellValue("总计：");
            int i=0;
            double j=0;
            for(DataRow dr:dt.Rows){
            	i+=Integer.parseInt(dr.getValue(2).toString());
            	j+=Double.parseDouble(dr.getValue(3).toString());
            }
            hs.createCell(2).setCellValue(i);
            hs.createCell(3).setCellValue(j);
            
            sheet.setColumnWidth( 0,  5000);
            sheet.setColumnWidth( 1,  6000);
            sheet.setColumnWidth( 2,  4000);
            sheet.setColumnWidth( 3,  3000);
            
            wb.write(os);; // 写入文件   
            wb.close();  
            os.close(); // 关闭流
             
        } catch (Exception ex) {   
            ex.printStackTrace();   
        }  
	}
	
	/**
	 * 街道明细汇总
	 * @throws IOException 
	 */
	@RequestMapping(value="/JDDTExport")
	public void jddtExport(HttpServletResponse response) throws IOException{
		String sql="select TX_JD.Name as FK_JD,TX_SQ.Name as FK_SQ,Sys_Enum.Lab,COUNT(*) as HuShu,SUM(ND128Rpt.FaFangJinE) as JinE from ND124Rpt,ND128Rpt,TX_JD,TX_SQ,Sys_Enum where ND124Rpt.BillNo in(select * from f_split(ND128Rpt.YinCangYu,',')) and ND124Rpt.BillNo!='' and Sys_Enum.EnumKey='DuiXiangLeiXing' and ND124Rpt.FK_QX=TX_JD.No and ND124Rpt.FK_JD=TX_SQ.No and ND124Rpt.DuiXiangLeiXing=Sys_Enum.IntKey group by Sys_Enum.Lab,TX_SQ.Name,TX_JD.Name";
		OutputStream os = response.getOutputStream();// 取得输出流   
        response.reset();// 清空输出流   
        String filename="街道明细汇总.xls";
        String downLoadName = new String(filename.getBytes("gbk"), "iso8859-1");  
        response.setHeader("Content-disposition", "attachment; filename="+downLoadName);// 设定输出文件头   
      
        response.setContentType("application/vnd.ms-excel;charset=UTF-8"); 

		
		DataTable dt=DBAccess.RunSQLReturnTable(sql);
		
		try {   
            HSSFWorkbook wb = new HSSFWorkbook();   
            HSSFSheet sheet = wb.createSheet("街道查询excel"); 
            
            // 设置字体
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints( (short) 20); //字体高度
            font.setColor(HSSFFont.COLOR_RED); //字体颜色
           font.setFontName("黑体"); //字体
           font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //宽度
           font.setItalic(true); //是否使用斜体
//         font.setStrikeout(true); //是否使用划线
           // 设置单元格类型
           HSSFCellStyle cellStyle = wb.createCellStyle();
           cellStyle.setFont(font);
           cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中中      cellStyle.setWrapText(true);    
            
            

            sheet.setVerticallyCenter(true);  

            HSSFRow rowT = sheet.createRow(0);
            rowT.createCell(0).setCellValue("街道");
            rowT.createCell(1).setCellValue("社区");
            rowT.createCell(2).setCellValue("对象类型");
            rowT.createCell(3).setCellValue("户数");
            rowT.createCell(4).setCellValue("金额");
  
       
            
            HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式  
            cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中 
            
            //sheet.addMergedRegion(new Region(0,  0, 1,  0));   

            //合並單元格,下標從0開始
            //sheet.addMergedRegion(new Region(0,0,0, (dt.Columns.size()-1)));     
            
            
            int count=0;
            CellRangeAddress cra1=null;
            CellRangeAddress cra2=null;
            for(int i=1;i<=dt.Rows.size();i++){
            	HSSFRow hr=sheet.createRow(i+count);
            	DataRow dr=dt.Rows.get(i-1);
            	for(int j=0;j<dr.columns.size();j++){
            		HSSFCell hc=hr.createCell(j);
            		hc.setCellValue(""+dr.getValue(dr.columns.get(j)));
            	}
            	if(i-1>0){
            		//判断本行街道名称与上一行街道名称是否相同   相同则合并单元格
            		if(dt.Rows.get(i-1).getValue(dt.Columns.get(0)).equals(dt.Rows.get(i-2).getValue(dt.Columns.get(0)))){
            			if(cra1==null){
            				cra1=new CellRangeAddress((i-1+count), 0, i+count,0);
            			}
            			else{
            				cra1.setLastRow(cra1.getLastRow()+1);
            			}
            			
            			//判断社区是否相等
            			if(dt.Rows.get(i-1).getValue(dt.Columns.get(1)).equals(dt.Rows.get(i-2).getValue(dt.Columns.get(1)))){
	            			if(cra2==null){
	            				cra2=new CellRangeAddress((i-1+count), 1, i+count,1);
	            			}else{
	            				cra2.setLastRow(cra2.getLastRow()+1);
	            			}
            			}else{
            				if(cra2!=null){
                				sheet.shiftRows(i+count, sheet.getLastRowNum(), 1,true,false);
                				HSSFRow hr1=sheet.createRow(i+count);
                				//添加行
                				cra1.setLastRow(cra1.getLastRow()+1);
                				count=count+1;
                				HSSFCell hc1=hr1.createCell(2);
                				hc1.setCellValue(sheet.getRow(cra2.getFirstRow()).getCell(cra2.getFirstColumn()).getStringCellValue()+"社区本级汇总：");
                				int sum=0;
                				double db=0;
                				for(int k=cra2.getFirstRow();k<=cra2.getLastRow();k++){
                					sum+=Integer.parseInt(sheet.getRow(k).getCell(3).getStringCellValue());
                					db+=Double.parseDouble(sheet.getRow(k).getCell(4).getStringCellValue());
                				}
                				hr1.createCell(3).setCellValue(sum);
                				hr1.createCell(4).setCellValue(db);
                				cra2.setLastRow(cra2.getLastRow()+1);
                				sheet.addMergedRegion(cra2);
                				cra2=null;
                			}
            			}
            			
            		}else{
            			if(cra2!=null){
            				sheet.shiftRows(i+count, sheet.getLastRowNum(), 1,true,false);
            				HSSFRow hr1=sheet.createRow(i+count);
            				//添加行
            				cra1.setLastRow(cra1.getLastRow()+1);
            				count=count+1;
            				HSSFCell hc1=hr1.createCell(2);
            				hc1.setCellValue(sheet.getRow(cra2.getFirstRow()).getCell(cra2.getFirstColumn()).getStringCellValue()+"社区本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra2.getFirstRow()-count+1;k<=cra2.getLastRow()-count+1;k++){
            					sum+=Integer.parseInt(dt.getValue(k, 3).toString());
            					db+=Double.parseDouble(dt.getValue(k, 4).toString());
            				}
            				hr1.createCell(3).setCellValue(""+sum);
            				hr1.createCell(4).setCellValue(""+db);
            				cra2.setLastRow(cra2.getLastRow()+1);
            				sheet.addMergedRegion(cra2);
            				cra2=null;
            			}
            			
            			if(cra1!=null){
            			
            				sheet.shiftRows(i+count, sheet.getLastRowNum(), 1,true,false);
            				sheet.addMergedRegion(new CellRangeAddress(i+count,1,i+count,2));
            				HSSFRow hr1=sheet.createRow(i+count);
            				count=count+1;
            				HSSFCell hc1=hr1.createCell(1);
            				hc1.setCellValue(sheet.getRow(cra1.getFirstRow()).getCell(cra1.getFirstColumn()).getStringCellValue()+"本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra1.getFirstRow()-count+1;k<cra1.getLastRow()-count+1;k++){
            					sum+=Integer.parseInt(dt.getValue(k, 3).toString());
            					db+=Double.parseDouble(dt.getValue(k, 4).toString());
            				}
            				hr1.createCell(3).setCellValue(""+sum);
            				hr1.createCell(4).setCellValue(""+db);
            				cra1.setLastRow(cra1.getLastRow()+1);
            				sheet.addMergedRegion(cra1);
            				sheet.getRow(cra1.getFirstRow()).getCell(cra1.getFirstColumn()).setCellValue(""+dt.Rows.get(i-3).getValue(dt.Columns.get(0)));
            				cra1=null;
            			}
            			
            			
            		}
            		
            		if(i==dt.Rows.size()){
            			if(cra2!=null){
            				sheet.shiftRows(i+count, sheet.getLastRowNum(), 1,true,false);
            				HSSFRow hr1=sheet.createRow(i+count);
            				//添加行
            				cra1.setLastRow(cra1.getLastRow()+1);
            				count=count+1;
            				HSSFCell hc1=hr1.createCell(2);
            				hc1.setCellValue(sheet.getRow(cra2.getFirstRow()).getCell(cra2.getFirstColumn()).getStringCellValue()+"社区本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra2.getFirstRow()-count;k<=cra2.getLastRow()-count;k++){
            					sum+=Integer.parseInt(dt.getValue(k, 3).toString());
            					db+=Double.parseDouble(dt.getValue(k, 4).toString());
            				}
            				hr1.createCell(3).setCellValue(""+sum);
            				hr1.createCell(4).setCellValue(""+db);
            				cra2.setLastRow(cra2.getLastRow()+1);
            				sheet.addMergedRegion(cra2);
            				cra2=null;
            			}
            			
            			if(cra1!=null){
            			
            				sheet.shiftRows(i+count, sheet.getLastRowNum(), 1,true,false);
            				sheet.addMergedRegion(new CellRangeAddress(i+count,1,i+count,2));
            				HSSFRow hr1=sheet.createRow(i+count);
            				count=count+1;
            				HSSFCell hc1=hr1.createCell(1);
            				hc1.setCellValue(sheet.getRow(cra1.getFirstRow()).getCell(cra1.getFirstColumn()).getStringCellValue()+"本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra1.getFirstRow()-count+1;k<cra1.getLastRow()-count+1;k++){
            					sum+=Integer.parseInt(dt.getValue(k, 3).toString());
            					db+=Double.parseDouble(dt.getValue(k, 4).toString());
            				}
            				hr1.createCell(3).setCellValue(""+sum);
            				hr1.createCell(4).setCellValue(""+db);
            				cra1.setLastRow(cra1.getLastRow()+1);
            				sheet.addMergedRegion(cra1);
            				sheet.getRow(cra1.getFirstRow()).getCell(cra1.getFirstColumn()).setCellValue(""+dt.Rows.get(i-3).getValue(dt.Columns.get(0)));
            				cra1=null;
            			}
            		}
            	}
            }

            sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum()+1,0,sheet.getLastRowNum()+1, 2));
            HSSFRow hs=sheet.createRow(sheet.getLastRowNum()+1);
            HSSFCell hc2=hs.createCell(0);
            hc2.setCellValue("总计：");
            int i=0;
            double j=0;
            for(DataRow dr:dt.Rows){
            	i+=Integer.parseInt(dr.getValue(3).toString());
            	j+=Double.parseDouble(dr.getValue(4).toString());
            }
            hs.createCell(3).setCellValue(""+i);
            hs.createCell(4).setCellValue(""+j);
            

            sheet.setColumnWidth( 0,  5000);
            sheet.setColumnWidth( 1,  6000);
            sheet.setColumnWidth( 2,  4000);
            sheet.setColumnWidth( 3,  3000);
            sheet.setColumnWidth( 4,  3000);
            
            wb.write(os);; // 写入文件   
            wb.close();  
            os.close(); // 关闭流
        } catch (Exception ex) {   
            ex.printStackTrace();   
        }  
	}
	
}
