package org.szcloud.framework.venson.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;

public class OpenOffice2PDF {
	private final static Log log = LogFactory.getLog(OpenOffice2PDF.class);
	/**
	 * office中各种格式
	 */
	private static final String[] OFFICE_POSTFIXS = { "doc", "docx", "xls", "xlsx", "ppt", "pptx", "temp" };
	private ArrayList<String> Office_Formats = new ArrayList<String>();

	private OpenOffice2PDF() {
	}

	private final static OpenOffice2PDF openOffice2PDF = new OpenOffice2PDF();

	public static OpenOffice2PDF getInstance() {
		return openOffice2PDF;
	}

	/**
	 * pdf格式
	 */
	private static final String PDF_POSTFIX = "pdf";

	/**
	 * 根据操作系统的名称，获取OpenOffice.org 3的安装目录 如我的OpenOffice.org 3安装在：C:/Program
	 * Files/OpenOffice.org 3
	 */

	public String getOfficeHome() {
		String osName = System.getProperty("os.name");
		if (Pattern.matches("Linux.*", osName)) {
			return "/usr/java/openoffice4";
		} else if (Pattern.matches("Windows.*", osName)) {
			return "D:\\software\\work\\openoffice4";
		}
		return null;
	}

	/**
	 * 转换文件
	 * 
	 * @param inputFilePath
	 * @param outputFilePath
	 * @param converter
	 */
	public void converterFile(String inputFilePath, String outputFilePath, OfficeDocumentConverter converter) {
		File inputFile = new File(inputFilePath);
		File outputFile = new File(outputFilePath);
		// 假如目标路径不存在,则新建该路径
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}
		try {
			converter.convert(inputFile, outputFile);
			log.debug("文件：" + inputFilePath + "\n转换为\n目标文件：" + outputFile + "\n成功！");
		} catch (OfficeException e) {
			// TODO Auto-generated catch block
			log.debug("文件：" + inputFilePath + "\n转换为\n目标文件：" + outputFile + "\n失败！", e);
		}
	}

	/**
	 * 使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为pdf文件
	 * 
	 * @param inputFilePath
	 *            源文件路径，如："e:/test.docx"
	 * @param outputFilePath
	 *            如果指定则按照指定方法，如果未指定（null）则按照源文件路径自动生成目标文件路径，如："e:/test_docx.pdf"
	 * @return
	 */
	public boolean openOffice2Pdf(String inputFilePath, String outputFilePath) {
		boolean flag = false;
		DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
		// 获取OpenOffice安装目录
		config.setOfficeHome(getOfficeHome());
		OfficeManager officeManager = config.buildOfficeManager();
		try {
			// 启动OpenOffice
			officeManager.start();
			OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
			File inputFile = new File(inputFilePath);
			Collections.addAll(Office_Formats, OFFICE_POSTFIXS);
			if ((null != inputFilePath) && (inputFile.exists())) {
				// 判断目标文件路径是否为空
				if (Office_Formats.contains(getPostfix(inputFilePath))) {
					if (null == outputFilePath) {
						// 转换后的文件路径
						String outputFilePath_new = generateDefaultOutputFilePath(inputFilePath);
						converterFile(inputFilePath, outputFilePath_new, converter);
						flag = true;

					} else {
						converterFile(inputFilePath, outputFilePath, converter);
						flag = true;
					}
				}

			} else {
				log.debug("路径出错：" + inputFilePath);
			}
		} catch (Exception e) {
			log.debug("转换出错:", e);
		} finally {
			if (officeManager != null) {
				officeManager.stop();
			}
		}
		return flag;
	}

	public boolean openOffice2Pdf(String inputFilePath) {
		return openOffice2Pdf(inputFilePath, null);
	}

	public boolean openOffice2Pdf(InputStream input, String outputFilePath) {
		File destination = null;
		try {
			destination = File.createTempFile(System.currentTimeMillis() + "_", ".temp");
			FileUtils.copyInputStreamToFile(input, destination);
			return openOffice2Pdf(destination.getAbsolutePath(), outputFilePath);
		} catch (IOException e) {
			log.debug("转换失败：", e);
			return false;
		} finally {
			destination.delete();
		}
	}

	/**
	 * 如果未设置输出文件路径则按照源文件路径和文件名生成输出文件地址。例，输入为 D:/fee.xlsx 则输出为D:/fee_xlsx.pdf
	 */
	public String generateDefaultOutputFilePath(String inputFilePath) {
		String outputFilePath = inputFilePath.replaceAll("." + getPostfix(inputFilePath),
				"_" + getPostfix(inputFilePath) + ".pdf");
		return outputFilePath;
	}

	/**
	 * 获取inputFilePath的后缀名，如："e:/test.pptx"的后缀名为："pptx"
	 */
	public String getPostfix(String inputFilePath) {
		String[] p = inputFilePath.split("\\.");
		if (p.length > 0) {// 判断文件有无扩展名
			// 比较文件扩展名
			return p[p.length - 1];
		} else {
			return null;
		}
	}

	public static void main(String[] args) {

		OpenOffice2PDF office2pdf = OpenOffice2PDF.getInstance();
		// office2pdf.openOffice2Pdf("g:/接口管理文档(1).docx", "g:/接口管理文档(1)_" + new
		// Date().getTime() + "." + PDF_POSTFIX);
		// office2pdf.openOffice2Pdf("g:/人大系统bug.xls", "g:/人大系统bug_" + new
		// Date().getTime() + "." + PDF_POSTFIX);
		// office2pdf.openOffice2Pdf("g:/修改密码接口.xlsx", "g:/修改密码接口_" + new
		// Date().getTime() + "." + PDF_POSTFIX);
		office2pdf.openOffice2Pdf("g:/sss.doc", "g:/深圳市科协办公系统虚拟机服务申请书_" + new Date().getTime() + "." + PDF_POSTFIX);
	}

}