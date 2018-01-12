package cn.org.awcp.venson.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

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
	public void test(HttpServletResponse response, HttpServletRequest request) {
		// API名称
		String rsid = request.getParameter("rsId");

		ReturnResult rr = aPIController.execute(rsid, request);
		if (rr.getStatus() == 0) {
			try {
				// API配置模板
				JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
				Map<String, Object> queryForMap = null;
				try {
					queryForMap = jdbcTemplate.queryForMap(
							"select rsTemplateID,rsName,rsType,rsSubTemplateID from p_fm_repService where ApiID=?",
							rsid);
				} catch (DataAccessException e) {
					throw new PlatformException("数据源没有找到");
				}
				if (!(rr.getData() instanceof List)) {
					throw new PlatformException("数据源格式有误");
				}
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
					ServletOutputStream servletOutputStream = response.getOutputStream();
					servletOutputStream.write(data);
					servletOutputStream.flush();
					servletOutputStream.close();
				} else if (rsType.equals("2")) {
					// rsType=".xlsx";
					// response.setContentType("application/vnd.ms-excel");
					// response.setHeader("Content-disposition", "attachment; filename=" +
					// URLEncoder.encode(request.getParameter("rsName"),"utf8")+".xlsx");
					// //打印程序当前路径
					// System.out.println(System.getProperty("user.dir"));
					// Map<String,Object> params = new HashMap<String,Object>();
					// //UserJs.getUser()是List类型数据源
					// //print文件
					// JasperPrint print = JasperFillManager.fillReport(is, params, dataSource);
					// //设置导出时参数
					// SimpleXlsxReportConfiguration conf = new SimpleXlsxReportConfiguration();
					// conf.setWhitePageBackground(false);
					// conf.setDetectCellType(true);
					// JRXlsxExporter exporter = new JRXlsxExporter();
					// exporter.setConfiguration(conf);
					// //设置输入项
					// ExporterInput exporterInput = new SimpleExporterInput(print);
					// exporter.setExporterInput(exporterInput);
					// //设置输出项
					// OutputStreamExporterOutput exporterOutput = new
					// SimpleOutputStreamExporterOutput(response.getOutputStream());
					// exporter.setExporterOutput(exporterOutput);
					// exporter.exportReport();
				} else if (rsType.equals("3")) {
					rsType = ".docx";
				}
			} catch (JRException e) {
				logger.debug("ERROR", e);
			} catch (IOException e) {
				logger.debug("ERROR", e);
			}

		}
	}
}
