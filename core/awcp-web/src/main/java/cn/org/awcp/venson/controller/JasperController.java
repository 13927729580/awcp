package cn.org.awcp.venson.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.venson.api.APIController;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.exception.PlatformException;
import cn.org.awcp.venson.service.FileService;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

@SuppressWarnings("deprecation")
@RestController
public class JasperController {
	@Autowired
	private APIController aPIController;
	@Resource(name = "IFileService")
	private FileService fileService;

	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "printService", method = RequestMethod.GET)
	public void jasperReport(HttpServletResponse response, HttpServletRequest request) {
		// API名称
		String rsid = request.getParameter("rsId");
		OutputStream outputStream=null;
		ReturnResult rr = aPIController.execute(rsid, request);
		//返回结果0表示有这个API
		if (rr.getStatus() == 0) {
			try {
				// API配置模板
				JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
				Map<String, Object> queryForMap = null;
				try {
					//获取数据源
					queryForMap = jdbcTemplate.queryForMap(
							"select rsTemplateID,rsName,rsType,rsSubTemplateID from p_fm_repService where ApiID=?",
							rsid);
				} catch (DataAccessException e) {
					throw new PlatformException("数据源没有找到");
				}
				if (!(rr.getData() instanceof List)) {
					throw new PlatformException("数据源格式有误");
				}
				//获取报表的主模板
				InputStream is = fileService
						.getInputStream(((String) queryForMap.get("rsTemplateID")).replaceAll(";", ""));
				if (is == null) {
					throw new PlatformException("主模板没有找到");
				}
				// 查看是否有子报表
				Map<String, Object> subReport = new HashMap<String, Object>();
				String rsSubTemplateID = (String) queryForMap.get("rsSubTemplateID");
				if (StringUtils.isNotBlank(rsSubTemplateID)) {
					String[] subParameter = rsSubTemplateID.split(";");
					for (int i = 0; i < subParameter.length; i++) {
						if (StringUtils.isNotBlank(subParameter[i])) {
							InputStream subIs = fileService.getInputStream(subParameter[i]);
							subReport.put("sub" + String.valueOf(i + 1), subIs);
						}
					}
				}
				//执行数据源,返回结果集
				JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(
						(List<Map<String, ?>>) rr.getData());
				// 查询结果集传入模板
				// API文件类型
				String rsType = (String) queryForMap.get("rsType");
				response.setCharacterEncoding("UTF-8");
				if (rsType.equals("pdf")) {
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "inline;fileName="
							+ ControllerHelper.processFileName((String) queryForMap.get("rsName")) + ".pdf");
					byte[] data = JasperRunManager.getInstance(DefaultJasperReportsContext.getInstance()).runToPdf(is,
							subReport, dataSource);
					outputStream = response.getOutputStream();
					outputStream.write(data);
				} else if (rsType.equals("excel")) {
			        JasperPrint jasperPrint = JasperFillManager.fillReport(is, subReport,dataSource);  
			        outputStream= response.getOutputStream();
		            response.setContentType("application/x-download");
		            response.setHeader("Content-Disposition", "attachment; filename=" + ControllerHelper.processFileName((String) queryForMap.get("rsName")) + ".xlsx");
		            // 使用JRXlsxExporter导出器导出 
		            JRXlsxExporter exporter = new JRXlsxExporter();
		            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint); 
		            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
		            exporter.exportReport();
				} else if (rsType.equals("word")) {
					//导出word文档,只需要更换一个导出器就行了JRDocxExporter exporter=new JRDocxExporter()
			        JasperPrint jasperPrint = JasperFillManager.fillReport(is, subReport,dataSource);  
			        JRDocxExporter exporter=new JRDocxExporter();   
			        outputStream = response.getOutputStream();
		            response.setContentType("application/x-download");
		            response.setHeader("Content-Disposition", "attachment; filename=" + ControllerHelper.processFileName((String) queryForMap.get("rsName")) + ".docx");
		            // 使用JRXlsxExporter导出器导出 
		            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint); 
		            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
		            exporter.exportReport();
				}
			} catch (JRException e) {
				logger.debug("ERROR", e);
			} catch (IOException e) {
				logger.debug("ERROR", e);
			}finally {
				IOUtils.closeQuietly(outputStream);
			}

		}
	}
}
