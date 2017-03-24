package org.szcloud.framework.formdesigner.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.utils.mongodb.MongoDBUtils;
import org.szcloud.framework.formdesigner.application.service.FormdesignerService;
import org.szcloud.framework.formdesigner.application.service.PrintService;
import org.szcloud.framework.formdesigner.application.service.StoreService;
import org.szcloud.framework.formdesigner.application.vo.DynamicPageVO;
import org.szcloud.framework.formdesigner.application.vo.StoreVO;
import org.szcloud.framework.formdesigner.core.domain.Store;
import org.szcloud.framework.formdesigner.service.MyFontFactory;
import org.szcloud.framework.formdesigner.service.MyImageFactory;
import org.szcloud.framework.formdesigner.util.PdfHeaderFooter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.SplitCharacter;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.DefaultSplitCharacter;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfChunk;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class PrintServiceImpl implements PrintService, ServletContextAware {
	private static final Logger logger = LoggerFactory.getLogger(PrintServiceImpl.class);

	@Autowired
	@Qualifier("storeServiceImpl")
	private StoreService storeService;

	@Autowired
	private FormdesignerService formdesignerServiceImpl;

	// 默认行间距
	public static final float DEFAULT_LINE_HEIGHT = 1.0F;

	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.servletContext = servletContext;

	}

	public void printPDF(StoreVO printManageVO, Map dataMap, OutputStream os) {

		JSONObject oo = JSON.parseObject(printManageVO.getContent());

		float left = 72; // 2.5cm
		float right = 72; // 2.5cm
		float top = 72; // 2.5cm
		float bottom = 72; // 2.5cm

		// 上下页边距
		if (oo.getFloat("leftDistance") != null) {
			left = (float) (oo.getFloat("leftDistance") * 28.35);
		}
		if (oo.getFloat("rightDistance") != null) {
			right = (float) (oo.getFloat("rightDistance") * 28.35);
		}
		if (oo.getFloat("topDistance") != null) {
			top = (float) (oo.getFloat("topDistance") * 28.35);
		}
		if (oo.getFloat("botDistance") != null) {
			bottom = (float) (oo.getFloat("botDistance") * 28.35);
		}

		// 页眉页脚
		String headerStr = oo.getString("pageHeader");
		String bottomStr = oo.getString("pageBottom");
		String showPage = oo.getString("showPage");
		// 打印的页面列表
		List<Long> pageList = new ArrayList<Long>();
		JSONArray objectList = JSON.parseArray(oo.getString("select_dynamicPage"));
		if (objectList != null && objectList.size() > 0) {
			for (int i = 0; i < objectList.size(); i++) {
				pageList.add(objectList.getJSONObject(i).getLongValue("id"));
			}
		}

		// 横向打印列表
		List<Long> rotatePageList = new ArrayList<Long>();
		JSONArray rotateList = JSON.parseArray(oo.getString("rotate_dynamicPage"));
		if (rotateList != null && rotateList.size() > 0) {
			for (int i = 0; i < rotateList.size(); i++) {
				rotatePageList.add(rotateList.getJSONObject(i).getLongValue("id"));
			}
		}

		Document document = new Document(PageSize.A4, left, right, top, bottom);// 建立一个Document对象
		Rectangle pageSize = document.getPageSize(); // pageSize ,若需要横向的话，此处有用

		try {

			PdfWriter writer = PdfWriter.getInstance(document, os);// 建立一个PdfWriter对象

			// 页眉页脚
			Rectangle rect = new Rectangle(document.left(), document.bottom(), document.right(), document.top());
			rect.setBorderColor(BaseColor.BLACK);
			rect.setBorderWidth(0.85f);
			rect.setBorderWidthBottom(0.85f);
			writer.setBoxSize("art", rect);

			// 获取水印号
			String waterMark = "";
			String waterMarkItemCode = "techBase.WATERMARK_NUMBER";
			if (StringUtils.isNotBlank(oo.getString("barCodeItem"))) {
				waterMarkItemCode = oo.getString("barCodeItem");
			}
			if (dataMap.get(waterMarkItemCode) != null) {
				waterMark = (String) dataMap.get(waterMarkItemCode);
			}

			String posX = oo.getString("barCodePosX");
			String posY = oo.getString("barCodePosY");

			String isGutter = oo.getString("isGutter");
			String gutterPosX = oo.getString("gutterPosX");
			String gutterPosY = oo.getString("gutterPosY");
			String startPageNo = oo.getString("startPageNo");
			String firstPageNo = oo.getString("firstPageNo");
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("headerStr", headerStr);
			paramMap.put("bottomStr", bottomStr);
			paramMap.put("waterMark", waterMark);
			paramMap.put("showPage", showPage);
			paramMap.put("barCodePosX", posX);
			paramMap.put("barCodePosY", posY);
			paramMap.put("isGutter", isGutter);
			paramMap.put("gutterPosX", gutterPosX);
			paramMap.put("gutterPosY", gutterPosY);
			paramMap.put("startPageNo", startPageNo);
			paramMap.put("firstPageNo", firstPageNo);

			PdfHeaderFooter headerAndFooter = new PdfHeaderFooter(paramMap);

			writer.setPageEvent(headerAndFooter); // 页眉页脚

			document.open();

			for (int i = 0; i < pageList.size(); i++) {

				boolean isPageHidden = false;
				DynamicPageVO page = formdesignerServiceImpl.findById((Long) pageList.get(i));
				if (StringUtils.isNotEmpty(page.getHiddenScript())) {
					ScriptEngine engine = ScriptEngineUtils.getScriptEngine();
					engine.put("dataMap", dataMap);
					try {
						isPageHidden = (Boolean) engine.eval(StringEscapeUtils.unescapeHtml4(page.getHiddenScript()));
					} catch (ScriptException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (isPageHidden) { // 对于页面hidden的,不打印出来
					continue;
				}

				if (rotatePageList.contains(pageList.get(i))) { // 是否横向打印？
					document.setPageSize(pageSize.rotate());
				}

				document.newPage(); // 新一页

				PdfPTable mainTable = geneaatePTableByPageId((Long) pageList.get(i), dataMap, null,
						PageSize.A4.getWidth() - document.leftMargin() - document.rightMargin());
				mainTable.calculateHeights();
				// mainTable.setExtendLastRow(false,false);
				// mainTable.setExtendLastRow(false,false);
				mainTable.setSplitLate(false);
				// mainTable.setSplitRows(true);

				mainTable.setTotalWidth(PageSize.A4.getWidth() - document.leftMargin() - document.rightMargin());
				float height = mainTable.calculateHeights();
				logger.debug("mainTable.getTotalHeight() is {} and mainTable height is : {}",
						mainTable.getTotalHeight(), height);

				// 划多一个空白框填满整页
				if (PageSize.A4.getHeight() - document.bottomMargin() - document.topMargin()
						- mainTable.getTotalHeight() > 140) {

					PdfPCell cell = new PdfPCell();
					cell.setBorderWidth(0.85f);
					mainTable.addCell(cell);
					mainTable.setExtendLastRow(true, true);

				}

				document.add(mainTable);

				// 对于有横向打印的情况，设置横向后，重新设置回竖向；
				document.setPageSize(pageSize);
			}

		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();

	}

	public void printPDFByTemplate(StoreVO printManageVO, Map dataMap, OutputStream os, List<JSONObject> components) {

		float left = 72; // 2.5cm
		float right = 72; // 2.5cm
		float top = 72; // 2.5cm
		float bottom = 72; // 2.5cm

		Document document = new Document(PageSize.A4, left, right, top, bottom);// 建立一个Document对象
		PdfWriter writer = null;

		// 建立一个PdfWriter对象
		try {
			writer = PdfWriter.getInstance(document, os);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		document.open();

		JSONObject oo = JSON.parseObject(printManageVO.getContent());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		generatePdfByTemplate(printManageVO, dataMap, baos, components);
		PdfReader reader = null;

		try {
			reader = new PdfReader(baos.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int pageOfCurrentReaderPDF = 0;

		PdfImportedPage page;
		PdfContentByte cb = writer.getDirectContent();
		while (pageOfCurrentReaderPDF < reader.getNumberOfPages()) {
			Rectangle r = reader.getPageSize(reader.getPageN(pageOfCurrentReaderPDF + 1));

			document.newPage();
			pageOfCurrentReaderPDF++;

			page = writer.getImportedPage(reader, pageOfCurrentReaderPDF);
			cb.addTemplate(page, 0, 0);

			if (StringUtils.isNotBlank(oo.getString("addPageId"))) { // 如果在模板里面要合并上页面
				Long addPageId = oo.getLong("addPageId");
				Long locationX = oo.getLong("locationX");
				Long locationY = oo.getLong("locationY");
				PdfPTable addPageTable = null;

				try {
					addPageTable = geneaatePTableByPageId(addPageId, dataMap, null,
							PageSize.A4.getWidth() - document.leftMargin() - document.rightMargin());
					addPageTable.calculateHeights();
					addPageTable.setSplitLate(false);
					addPageTable.setExtendLastRow(false, false);
					addPageTable.setTotalWidth(PageSize.A4.getWidth() - document.leftMargin() - document.rightMargin());
					addPageTable.calculateHeights();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				addPageTable.writeSelectedRows(0, -1, locationX, locationY, writer.getDirectContent());
			}
		}

		document.close();
	}

	private void generatePdfByTemplate(StoreVO printManageVO, Map dataMap, OutputStream os,
			List<JSONObject> components) {

		if (components != null && components.size() > 0) {
			for (int i = 0; i < components.size(); i++) {
				JSONObject component = components.get(i);

				int type = component.getIntValue("componentType");

				String optionsText = null;
				switch (type) {
				case 1004:
					String options = (String) dataMap.get(component.getString("dataItemCode") + "_option");

					// 将options "1=是;2=否" 格式转为map
					Map<String, String> optionMap = new LinkedHashMap<String, String>();
					String[] optionStr = options.split("\\;");
					if (optionStr.length > 0) {
						for (String str : optionStr) {
							String[] entry = str.split("\\=");
							optionMap.put(entry[0], entry[1]);
						}
					}
					if (!"1".equals(component.getString("printAllOption"))) { // radio正常情况下，直接打印code对应的value，如果是设置打印所以选项，则传code到模板
						dataMap.put(component.getString("dataItemCode"),
								optionMap.get(dataMap.get(component.getString("dataItemCode"))));
					}
					break;

				}
			}
		}

		JSONObject printJson = JSON.parseObject(printManageVO.getContent());
		String templatePdFileId = printJson.getString("templateFileId");
		try {
			PdfReader reader = new PdfReader(getFileByFileId(templatePdFileId));/* 打开已经定义好字段以后的pdf模板 */
			PdfStamper stamp = new PdfStamper(reader, os); /* 将要生成的目标PDF文件 */
			PdfContentByte under = stamp.getUnderContent(1); /* 使用中文字体 */
			BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font FontChinese = new Font(bf, 12, Font.NORMAL); /* 取出报表模板中的所有字段 */
			AcroFields form = stamp.getAcroFields(); /* 为字段赋值,注意字段名称是区分大小写的 */
			BaseFont bfb = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
			form.addSubstitutionFont(bfb);
			for (Iterator it = form.getFields().keySet().iterator(); it.hasNext();) {

				String name = (String) it.next();
				if (dataMap.get(name) != null) {
					form.setField(name, (String) dataMap.get(name));
				}

			}

			stamp.setFormFlattening(true);
			/* 必须要调用这个，否则文档不会生成的 */
			reader.close();
			stamp.close();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
	}

	/**
	 * 批量打印
	 */

	public void batchPrintPDF(List<StoreVO> printManageVOs, List<Map> dataMaps, List<List<JSONObject>> componentsList,
			OutputStream os) {

		float left = 72; // 2.5cm
		float right = 72; // 2.5cm
		float top = 72; // 2.5cm
		float bottom = 72; // 2.5cm

		Document document = new Document(PageSize.A4, left, right, top, bottom);// 建立一个Document对象
		PdfWriter writer = null;

		// 建立一个PdfWriter对象
		try {
			writer = PdfWriter.getInstance(document, os);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		document.open();

		for (int index = 0; index < printManageVOs.size(); index++) {
			StoreVO printManageVO = printManageVOs.get(index);
			Map dataMap = dataMaps.get(index);
			List<JSONObject> components = componentsList.get(index);

			JSONObject oo = JSON.parseObject(printManageVO.getContent());
			if (StringUtils.isNotBlank(oo.getString("templateFileId"))) { // 有配置自定义模板

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				generatePdfByTemplate(printManageVO, dataMap, baos, components);
				PdfReader reader = null;

				try {
					reader = new PdfReader(baos.toByteArray());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int pageOfCurrentReaderPDF = 0;

				PdfImportedPage page;
				PdfContentByte cb = writer.getDirectContent();
				while (pageOfCurrentReaderPDF < reader.getNumberOfPages()) {
					Rectangle r = reader.getPageSize(reader.getPageN(pageOfCurrentReaderPDF + 1));

					document.newPage();
					pageOfCurrentReaderPDF++;

					page = writer.getImportedPage(reader, pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);
					// cb.beginText();
					// BaseFont bf=null;
					// try {
					// bf = BaseFont.createFont(BaseFont.HELVETICA,
					// BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
					// } catch (DocumentException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					//
					// cb.setFontAndSize(bf, 9);
					// cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "", 520,
					// 5, 0);
					// cb.endText();
					if (StringUtils.isNotBlank(oo.getString("addPageId"))) { // 如果在模板里面要合并上页面
						Long addPageId = oo.getLong("addPageId");
						Long locationX = oo.getLong("locationX");
						Long locationY = oo.getLong("locationY");
						PdfPTable addPageTable = null;

						try {
							addPageTable = geneaatePTableByPageId(addPageId, dataMap, null,
									PageSize.A4.getWidth() - document.leftMargin() - document.rightMargin());
							addPageTable.calculateHeights();
							addPageTable.setSplitLate(false);
							addPageTable.setExtendLastRow(false, false);
							addPageTable.setTotalWidth(
									PageSize.A4.getWidth() - document.leftMargin() - document.rightMargin());
							addPageTable.calculateHeights();
						} catch (DocumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						addPageTable.writeSelectedRows(0, -1, locationX, locationY, writer.getDirectContent());
					}
				}
			} else { // 没有配置模板，则根据表单配置绘制
				// 页眉页脚
				String headerStr = oo.getString("pageHeader");
				String bottomStr = oo.getString("pageBottom");
				String showPage = oo.getString("showPage");
				// 打印的页面列表
				List<Long> pageList = new ArrayList<Long>();
				JSONArray objectList = JSON.parseArray(oo.getString("select_dynamicPage"));
				if (objectList != null && objectList.size() > 0) {
					for (int i = 0; i < objectList.size(); i++) {
						pageList.add(objectList.getJSONObject(i).getLongValue("id"));
					}
				}

				try {

					for (int i = 0; i < pageList.size(); i++) {

						boolean isPageHidden = false;
						DynamicPageVO page = formdesignerServiceImpl.findById((Long) pageList.get(i));
						if (StringUtils.isNotEmpty(page.getHiddenScript())) {
							ScriptEngine engine = ScriptEngineUtils.getScriptEngine();
							engine.put("dataMap", dataMap);
							try {
								isPageHidden = (Boolean) engine
										.eval(StringEscapeUtils.unescapeHtml4(page.getHiddenScript()));
							} catch (ScriptException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						if (isPageHidden) { // 对于页面hidden的,不打印出来
							continue;
						}

						document.newPage(); // 新一页

						// 如果需要添加条形码
						String waterMark = "";
						String waterMarkItemCode = "techBase.WATERMARK_NUMBER";
						if (StringUtils.isNotBlank(oo.getString("barCodeItem"))) {
							waterMarkItemCode = oo.getString("barCodeItem");
						}
						if (dataMap.get(waterMarkItemCode) != null) {
							waterMark = (String) dataMap.get(waterMarkItemCode);
						}
						if (StringUtils.isNotBlank(waterMark)) {
							PdfContentByte cb = writer.getDirectContent();
							// 生成条形码
							if (StringUtils.isNotBlank(waterMark)) {
								Barcode128 code128 = new Barcode128();
								code128.setCode(waterMark);
								code128.setSize(10);
								Image image = code128.createImageWithBarcode(cb, null, null);
								float posX = 70;
								float posY = 795;
								if (StringUtils.isNotBlank(oo.getString("barCodePosX"))) {
									posX = Float.parseFloat(oo.getString("barCodePosX"));
								}
								if (StringUtils.isNotBlank(oo.getString("barCodePosY"))) {
									posY = Float.parseFloat(oo.getString("barCodePosY"));
								}
								image.setAbsolutePosition(posX, posY);

								try {
									cb.addImage(image);
								} catch (DocumentException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}
							}

							Font waterMarkFont = new Font(Font.FontFamily.HELVETICA, 52, Font.NORMAL,
									new GrayColor(0.75f));
							ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER,
									new Phrase(waterMark, waterMarkFont), 297.5f, 351, 45);
						}
						// 添加条形码 END

						PdfPTable mainTable = geneaatePTableByPageId((Long) pageList.get(i), dataMap, null,
								PageSize.A4.getWidth() - document.leftMargin() - document.rightMargin());
						mainTable.calculateHeights();
						mainTable.setSplitLate(false);
						mainTable.setExtendLastRow(false, false);

						mainTable
								.setTotalWidth(PageSize.A4.getWidth() - document.leftMargin() - document.rightMargin());
						float height = mainTable.calculateHeights();
						logger.debug("mainTable.getTotalHeight() is {} and mainTable height is : {}",
								mainTable.getTotalHeight(), height);

						// 划多一个空白框填满整页
						if (PageSize.A4.getHeight() - document.bottomMargin() - document.topMargin()
								- mainTable.getTotalHeight() > 140) {

							PdfPCell cell = new PdfPCell();
							cell.setBorderWidth(0.85f);
							mainTable.addCell(cell);
							mainTable.setExtendLastRow(true, true);

						}
						document.add(mainTable);

					}
				} catch (DocumentException de) {
					System.err.println(de.getMessage());
				} catch (IOException ioe) {
					System.err.println(ioe.getMessage());
				}
			}

		}

		document.close();

	}

	/**
	 * 根据页面Id生成对应的pdf页面
	 * 
	 * @param id
	 *            页面Id
	 * @param dataMap
	 *            对应的数据
	 * @param configures
	 *            对于包含组件，包含页面可能需要替换对应组件的dataItemCode，传进对应的Map<String,String>
	 *            key:要替换的组件Id，value：要替换的新的dataItemCode
	 * @return totalWidth 表格宽度
	 * @throws DocumentException
	 * @throws IOException
	 */

	private PdfPTable geneaatePTableByPageId(Long id, Map dataMap, Map configures, float totalWidth)
			throws DocumentException, IOException {

		DynamicPageVO pageVO = formdesignerServiceImpl.findById(id);

		Map map = getLayoutAndComp(pageVO, configures);
		if (pageVO.getPageType() == 1002) { // 表单页面
			List<JSONObject> mainLayouts = (List<JSONObject>) map.get("layouts");

			// 排序
			Collections.sort(mainLayouts, new Comparator<JSONObject>() {
				@Override
				public int compare(JSONObject o1, JSONObject o2) {
					int o1Order = o1.getIntValue("order");
					int o2Order = o2.getIntValue("order");
					return o1Order - o2Order;
				}
			});

			Map<String, List<JSONObject>> components = (Map<String, List<JSONObject>>) map.get("components");
			PdfPTable mainTable = new PdfPTable(1);
			mainTable.setSplitLate(false);
			mainTable.setWidthPercentage(100); // 宽度占整页100%

			if (mainLayouts != null && mainLayouts.size() > 0) {
				for (int j = 0; j < mainLayouts.size(); j++) {
					PdfPCell row = null;
					JSONObject rowJson = mainLayouts.get(j);
					if (rowJson.getString("textstyle") != null && "0".equals(rowJson.getString("textstyle"))) { // 是否带边框

						float height = 0;
						if (rowJson.getFloat("height") != null) {
							logger.debug(" height is : {}", rowJson.getFloat("height"));
							height = (float) (rowJson.getFloat("height") * 28.35);
						}
						int heightType = 1; // 高度类型 1：固定高度； 2：最小高度
						if (rowJson.getInteger("heightType") != null) {
							heightType = rowJson.getInteger("heightType");
						}
						row = new PdfPCell(createTable(mainLayouts.get(j), components, dataMap, true, height,
								heightType, totalWidth));
						row.setBorder(Rectangle.NO_BORDER);

					} else {

						float height = 0;
						if (rowJson.getFloat("height") != null) {
							logger.debug(rowJson.getFloat("height") + "  height");
							height = (float) (rowJson.getFloat("height") * 28.35);
						}
						int heightType = 1; // 高度类型 1：固定高度； 2：最小高度
						if (rowJson.getInteger("heightType") != null) {
							heightType = rowJson.getInteger("heightType");
						}
						row = new PdfPCell(createTable(mainLayouts.get(j), components, dataMap, false, height,
								heightType, totalWidth));

					}
					mainTable.addCell(row);
				}
			}
			return mainTable;
		} else { // 列表页面

			String lineHeight = pageVO.getLineHeight();
			String minLineCount = pageVO.getMinLineCount();
			String maxLineCount = pageVO.getMaxLineCount();

			if (pageVO.getPdfTemplatePage() != null && pageVO.getPdfTemplatePage() != 0) { // 如果列表页面配置了pdf的显示模板
				List<JSONObject> coms = new ArrayList<JSONObject>();
				coms = (List<JSONObject>) map.get("components");
				JSONObject firstCom = coms.get(0);
				String dataItemCode = firstCom.getString("dataItemCode");
				String alais = dataItemCode.substring(0, dataItemCode.indexOf("."));
				List<Map<String, String>> dataList = (List<Map<String, String>>) dataMap.get(alais);

				PdfPTable listT = createListTableWithTemplate(pageVO.getPdfTemplatePage(), dataList, configures,
						totalWidth);
				return listT;

			} else { // 列表页面没配置pdf显示模板，则按普通列表页面显示

				List<JSONObject> coms = new ArrayList<JSONObject>();

				for (JSONObject tempC : (List<JSONObject>) map.get("components")) {
					Integer type = tempC.getInteger("componentType");
					if (type == 1008) {
						coms.add(tempC);
					}
				}
				PdfPTable listTable = new PdfPTable(1);
				listTable.setWidthPercentage(100); // 宽度占整页100%
				Long pdfTemplatePage = pageVO.getPdfTemplatePage();
				PdfPCell listCell = new PdfPCell(createListTable(coms, dataMap, pageVO, totalWidth));
				listTable.addCell(listCell);
				listTable.setSplitLate(false);
				return listTable;

			}

		}

	}

	/**
	 * 根据列组件生成pdf表格（列表页面）
	 * 
	 * @param coms
	 *            列组件列表
	 * @param dataMap
	 *            数据集合
	 * @param lineHeight
	 *            列表页面表格单元格行高
	 * @param minLineCount
	 *            列表表格最小条数
	 * @param maxLineCount
	 *            列表表格最大条数
	 * @param maxLineCount
	 *            列表表格最大条数
	 * 
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	private PdfPTable createListTable(List<JSONObject> coms, Map dataMap, DynamicPageVO pageVO, float totalWidth)
			throws DocumentException, IOException {

		String lineHeight = pageVO.getLineHeight();
		String minLineCount = pageVO.getMinLineCount();
		String maxLineCount = pageVO.getMaxLineCount();
		String lineHeightType = pageVO.getLineHeightType();

		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 设置中文字体
		Font headFont = new Font(bfChinese, 12f, Font.NORMAL);// 设置字体大小
		Font headFontBold = new Font(bfChinese, 12f, Font.BOLD);// 设置字体大小

		if (coms != null) { // 根据组件列表排序
			Collections.sort(coms, new Comparator<JSONObject>() {
				@Override
				public int compare(JSONObject o1, JSONObject o2) {
					int o1Order = o1.getIntValue("order");
					int o2Order = o2.getIntValue("order");
					return o1Order - o2Order;
				}
			});
		}
		float[] widths = generateWidthsByListPageLayouts(coms, totalWidth);
		PdfPTable table = new PdfPTable(widths);
		JSONObject firstCom = coms.get(0);
		String dataItemCode = firstCom.getString("dataItemCode");
		String alais = dataItemCode.substring(0, dataItemCode.indexOf("."));
		// 添加表头
		for (int i = 0; i < coms.size(); i++) {
			JSONObject o = coms.get(i);

			Paragraph finalContent = new Paragraph();
			finalContent.add(new Chunk(o.getString("columnName"), headFontBold));
			PdfPCell cell = new PdfPCell(finalContent);

			cell.setUseAscender(true);
			cell.setUseDescender(true);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);

			if (StringUtils.isNotEmpty(lineHeight)) { // 设置行高
				if (lineHeightType != null && "2".equals(lineHeightType)) {
					cell.setMinimumHeight((float) (Float.parseFloat(lineHeight) * 28.35));
				} else {
					cell.setFixedHeight((float) (Float.parseFloat(lineHeight) * 28.35));
				}
			} else {
				// cell.setFixedHeight(20f);
			}

			table.addCell(cell);
		}

		table.setSplitLate(false);

		// 添加表body
		List dataList = (List) dataMap.get(alais);
		if (dataList != null && dataList.size() > 0) {
			table.setHeaderRows(1);
		}
		if (dataList != null && dataList.size() > 0) {
			int maxLine = dataList.size();

			if (StringUtils.isNotEmpty(maxLineCount)) { // 如果有设置最大条数，并且最大条数小于数据条数，则只画最大条数
				if (maxLine > Integer.parseInt(maxLineCount)) {
					maxLine = Integer.parseInt(maxLineCount);
				}
			}

			for (int i = 0; i < maxLine; i++) {
				Map map = (Map) dataList.get(i);

				for (int k = 0; k < coms.size(); k++) {
					JSONObject o = coms.get(k);

					Paragraph finalContent = new Paragraph();
					String value = "";
					if (map.get((String) o.getString("dataItemCode")) != null) {
						value = (String) map.get((String) o.getString("dataItemCode"));
					}
					finalContent.add(new Chunk(value, headFont));
					PdfPCell cell = new PdfPCell(finalContent);

					cell.setUseAscender(true);
					cell.setUseDescender(true);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					if (k == 0)
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					else
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);

					if (StringUtils.isNotEmpty(lineHeight)) { // 设置行高
						if (lineHeightType != null && "2".equals(lineHeightType)) {
							cell.setMinimumHeight((float) (Float.parseFloat(lineHeight) * 28.35));
						} else {
							cell.setFixedHeight((float) (Float.parseFloat(lineHeight) * 28.35));
						}
					} else {
						// cell.setFixedHeight(20f);
					}

					table.addCell(cell);
				}

			}

			if (StringUtils.isNotEmpty(minLineCount)) {
				if (Integer.parseInt(minLineCount) > dataList.size()) { // 如果最小条数大于数据条数，则补上空白
					int addLine = Integer.parseInt(minLineCount) - dataList.size();
					for (int i = 0; i < addLine; i++) {
						for (int j = 0; j < coms.size(); j++) {
							Paragraph finalContent = new Paragraph();
							finalContent.add(new Chunk(" ", headFont));
							PdfPCell cell = new PdfPCell(finalContent);

							cell.setUseAscender(true);
							cell.setUseDescender(true);
							cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);

							if (StringUtils.isNotEmpty(lineHeight)) { // 设置行高
								cell.setFixedHeight((float) (Float.parseFloat(lineHeight) * 28.35));
							} else {
								cell.setFixedHeight(20f);
							}
							table.addCell(cell);
						}
					}
				}
			}

		}

		return table;
	}

	/**
	 * 根据表格组件和数据生成对应的PdfPTable
	 * 
	 * @param component
	 * @param dataMap
	 * @param totalWidth
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	private PdfPTable generateDataGridTable(JSONObject component, Map dataMap, float totalWidth)
			throws DocumentException, IOException {

		JSONArray columns = component.getJSONArray("columns");
		columns = sortByOrderArray(columns);

		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 设置中文字体
		Font headFont = new Font(bfChinese, 12f, Font.NORMAL);// 设置字体大小

		PdfPTable table = new PdfPTable(columns.size());
		table.setSplitLate(false);
		String proportions = component.getString("proportions");
		if (StringUtils.isNotBlank(proportions)) { // 如果有配置各个列的打印宽度，则按宽度来分
			String[] widths = proportions.split(",");
			float[] floats = new float[widths.length];
			for (int i = 0; i < floats.length; i++) {
				floats[i] = Float.parseFloat(widths[i]);
				table = new PdfPTable(floats);
			}
		}
		String lineHeight = component.getString("lineHeight");
		String lineHeightType = component.getString("lineHeightType");
		String maxLineCount = component.getString("maxLineCount");
		String minLineCount = component.getString("minLineCount");

		String alais = component.getString("dataAlias");
		// 添加表头
		for (int i = 0; i < columns.size(); i++) {
			JSONObject o = columns.getJSONObject(i);

			Paragraph finalContent = new Paragraph();
			finalContent.add(new Chunk(o.getString("columnTitle"), headFont));
			PdfPCell cell = new PdfPCell(finalContent);

			cell.setUseAscender(true);
			cell.setUseDescender(true);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);

			if (StringUtils.isNotEmpty(lineHeight)) { // 设置行高
				if (lineHeightType != null && "2".equals(lineHeightType)) {
					cell.setMinimumHeight((float) (Float.parseFloat(lineHeight) * 28.35));
				} else {
					cell.setFixedHeight((float) (Float.parseFloat(lineHeight) * 28.35));
				}
			} else {
				cell.setFixedHeight(20f);
			}

			table.addCell(cell);
		}
		table.setHeaderRows(1);
		// table.setSplitLate(false);
		// 添加表body
		List dataList = (List) dataMap.get(alais + "_list");
		if (dataList != null && dataList.size() > 0) {

			int maxLine = dataList.size();

			if (StringUtils.isNotEmpty(maxLineCount)) { // 如果有设置最大条数，并且最大条数小于数据条数，则只画最大条数
				if (maxLine > Integer.parseInt(maxLineCount)) {
					maxLine = Integer.parseInt(maxLineCount);
				}
			}

			for (int i = 0; i < maxLine; i++) {
				Map map = (Map) dataList.get(i);

				for (int k = 0; k < columns.size(); k++) {
					JSONObject o = columns.getJSONObject(k);

					Paragraph finalContent = new Paragraph();
					String value = "";
					if (map.get((String) o.getString("columnField")) != null) {
						value = (String) map.get((String) o.getString("columnField"));
					}
					finalContent.add(new Chunk(value, headFont));
					PdfPCell cell = new PdfPCell(finalContent);

					cell.setUseAscender(true);
					cell.setUseDescender(true);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);

					if (StringUtils.isNotEmpty(lineHeight)) { // 设置行高
						if (lineHeightType != null && "2".equals(lineHeightType)) {
							cell.setMinimumHeight((float) (Float.parseFloat(lineHeight) * 28.35));
						} else {
							cell.setFixedHeight((float) (Float.parseFloat(lineHeight) * 28.35));
						}
					} else {
						cell.setFixedHeight(20f);
					}

					table.addCell(cell);
				}

			}

			if (StringUtils.isNotEmpty(minLineCount)) {
				if (Integer.parseInt(minLineCount) > dataList.size()) { // 如果最小条数大于数据条数，则补上空白
					int addLine = Integer.parseInt(minLineCount) - dataList.size();
					for (int i = 0; i < addLine; i++) {
						for (int j = 0; j < columns.size(); j++) {
							Paragraph finalContent = new Paragraph();
							finalContent.add(new Chunk(" ", headFont));
							PdfPCell cell = new PdfPCell(finalContent);

							cell.setUseAscender(true);
							cell.setUseDescender(true);
							cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);

							if (StringUtils.isNotEmpty(lineHeight)) { // 设置行高
								cell.setFixedHeight((float) (Float.parseFloat(lineHeight) * 28.35));
							} else {
								cell.setFixedHeight(20f);
							}
							table.addCell(cell);
						}
					}
				}
			}

		}

		return table;
	}

	/**
	 * 根据配置的布局layout生成pdf对应的表格PdfpTable
	 * 
	 * @param o
	 *            布局对应的json
	 * @param components
	 *            组件列表map
	 * @param dataMap
	 *            数据map
	 * @param borderless
	 *            是否无边框
	 * @param height
	 *            高度
	 * @param heightType
	 *            固定高度还是最少高度
	 * @return totalWidth 总宽度
	 * @throws DocumentException
	 * @throws IOException
	 */
	private PdfPTable createTable(JSONObject o, Map<String, List<JSONObject>> components, Map dataMap,
			boolean borderless, float height, int heightType, float totalWidth) throws DocumentException, IOException {

		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 设置中文字体

		Font headFont = new Font(bfChinese, 12f, Font.BOLD);// 设置字体大小
		Font headFont1 = new Font(bfChinese, 8, Font.BOLD);// 设置字体大小
		Font headFont2 = new Font(bfChinese, 10, Font.NORMAL);// 设置字体大小

		String type = o.getString("layoutType");
		PdfPTable t = null;
		if (type.equalsIgnoreCase("2")) {
			if (o.getJSONArray("childLayouts") == null || o.getJSONArray("childLayouts").size() == 0) {

				t = generateParageCellByComponents(o, components, dataMap, borderless, height, heightType, totalWidth);

			} else {
				JSONArray array = o.getJSONArray("childLayouts");
				array = sortByOrderArray(array);

				float[] widths = generateWidthsByLayouts(array, totalWidth);

				float widthSum = 0; // 计算widths的总和
				for (int i = 0; i < widths.length; i++) {
					widthSum += widths[i];
				}

				t = new PdfPTable(widths);
				t.setSplitLate(false);
				for (int i = 0; i < array.size(); i++) {
					float tempWidth = (totalWidth * widths[i]) / widthSum;
					PdfPCell p = new PdfPCell(createTable(array.getJSONObject(i), components, dataMap, borderless,
							height, heightType, tempWidth));
					p.setHorizontalAlignment(Element.ALIGN_CENTER);
					JSONObject col = array.getJSONObject(i);
					if (borderless == false) {
						if (StringUtils.isNotBlank(col.getString("textstyle"))) { // 对于个别特殊的单元格有设置
																					// 无边框
							p.setBorder(Rectangle.NO_BORDER);
						} else {
							p.setBorderWidth(0.85f); // 边框
						}
						// p.setBorderWidth(0.85f); //边框
					} else {
						p.setBorder(Rectangle.NO_BORDER);
					}

					if (o.getString("textstyle") != null && o.getString("textstyle").indexOf("1") != -1) { // 对于个别特殊的单元格有设置
																											// 上边框的，加上
																											// 上边框
						p.setBorderWidthTop(0.85f);
					}
					if (o.getString("textstyle") != null && o.getString("textstyle").indexOf("2") != -1) { // 对于个别特殊的单元格有设置
																											// 右边框的，加上右边框
						p.setBorderWidthRight(0.85f);
					}
					if (o.getString("textstyle") != null && o.getString("textstyle").indexOf("3") != -1) { // 对于个别特殊的单元格有设置
																											// 下边框的，加上下边框
						p.setBorderWidthBottom(0.85f);
					}
					if (o.getString("textstyle") != null && o.getString("textstyle").indexOf("4") != -1) { // 对于个别特殊的单元格有设置
																											// 左边框的，加上左边框
						p.setBorderWidthLeft(0.85f);
					}

					t.addCell(p);

				}
			}
		} else {
			t = new PdfPTable(1);
			t.setSplitLate(false);
			if (o.getJSONArray("childLayouts") == null || o.getJSONArray("childLayouts").size() == 0) {

				t = generateParageCellByComponents(o, components, dataMap, borderless, height, heightType, totalWidth);

			} else {
				JSONArray array = o.getJSONArray("childLayouts");
				array = sortByOrderArray(array);
				for (int i = 0; i < array.size(); i++) {
					PdfPCell p = new PdfPCell(createTable(array.getJSONObject(i), components, dataMap, borderless,
							height, heightType, totalWidth));
					p.setHorizontalAlignment(Element.ALIGN_CENTER);
					JSONObject col = array.getJSONObject(i);
					if (borderless == false) {
						if (StringUtils.isNotBlank(col.getString("textstyle"))) { // 对于个别特殊的单元格有设置
																					// 无边框
							p.setBorder(Rectangle.NO_BORDER);
						} else {
							p.setBorderWidth(0.85f); // 边框
						}
						// p.setBorderWidth(0.85f); //边框
					} else {
						p.setBorder(Rectangle.NO_BORDER);
					}

					if (o.getString("textstyle") != null && o.getString("textstyle").indexOf("1") != -1) { // 对于个别特殊的单元格有设置
																											// 上边框的，加上
																											// 上边框
						p.setBorderWidthTop(0.85f);
					}
					if (o.getString("textstyle") != null && o.getString("textstyle").indexOf("2") != -1) { // 对于个别特殊的单元格有设置
																											// 右边框的，加上右边框
						p.setBorderWidthRight(0.85f);
					}
					if (o.getString("textstyle") != null && o.getString("textstyle").indexOf("3") != -1) { // 对于个别特殊的单元格有设置
																											// 下边框的，加上下边框
						p.setBorderWidthBottom(0.85f);
					}
					if (o.getString("textstyle") != null && o.getString("textstyle").indexOf("4") != -1) { // 对于个别特殊的单元格有设置
																											// 左边框的，加上左边框
						p.setBorderWidthLeft(0.85f);
					}

					t.addCell(p);
				}
			}
		}

		return t;

	}

	private PdfPTable createListTableWithTemplate(Long pdfTemplatePage, List<Map<String, String>> dataList,
			Map configures, float totalWidth) throws DocumentException, IOException {

		PdfPTable returnTable = new PdfPTable(1);
		for (int i = 0; i < dataList.size(); i++) {
			Map dataMap = dataList.get(i);
			PdfPTable pageTable = geneaatePTableByPageId(pdfTemplatePage, dataMap, configures, totalWidth);
			returnTable.addCell(new PdfPCell(pageTable));
		}
		return returnTable;
	}

	/**
	 * 对于叶子节点的布局，根据组件内容生成对应的pdfCell单元
	 * 
	 * @param o
	 *            叶子节点布局
	 * @param components
	 *            组件列表
	 * @param dataMap
	 *            数据
	 * @param borderless
	 *            是否带边框
	 * @param height
	 *            高度
	 * @param heightType
	 *            固定高度还是最小高度
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	private PdfPTable generateParageCellByComponents(JSONObject o, Map<String, List<JSONObject>> components,
			Map dataMap, boolean borderless, float height, int heightType, float totalWidth)
			throws DocumentException, IOException {

		PdfPTable t = new PdfPTable(1);
		PdfPCell c = new PdfPCell();

		String layoutId = o.getString("pageId");
		List compList = components.get(layoutId);
		PdfPTable containPageTable = null;
		if (compList != null && compList.size() > 0) {
			// 排序 sort by order
			Collections.sort(compList, new Comparator<JSONObject>() {
				@Override
				public int compare(JSONObject o1, JSONObject o2) {
					int o1Order = o1.getIntValue("order");
					int o2Order = o2.getIntValue("order");
					return o1Order - o2Order;
				}
			});

			Paragraph finalContent = new Paragraph();
			float indent = 0; // 缩进
			float leading = 0; // 行间距
			for (int i = 0; i < compList.size(); i++) {

				JSONObject component = (JSONObject) compList.get(i);
				logger.debug("component name is {}", component.getString("name"));

				if (StringUtils.isNotEmpty(component.getString("textindent"))) { // 如果有设置缩进

					float width = component.getFloat("textindent"); // 多少个字
					if (width > 0) {
						float indentSize = 12f; // 默认字体
						if (StringUtils.isNotEmpty(component.getString("fontsize"))) {
							indentSize = component.getFloat("fontsize");
						}
						indent = width * indentSize;
					}

				}

				if (StringUtils.isNotEmpty(component.getString("lineheight"))) { // 如果有设置行间距
					leading = component.getFloat("lineheight"); // 多少倍

				}

				String comType = component.getString("componentType");
				if ("1012".equals(comType)) { // 包含组件
					Long containPageId = component.getLong("relatePageId");
					JSONArray relComArray = component.getJSONArray("configures");
					Map<String, String> configures = new HashMap();
					if (relComArray != null && relComArray.size() > 0) {
						for (int k = 0; k < relComArray.size(); k++) { // 遍历更改包含页面中组件的dataItemCode，pageId，name
							JSONObject relCom = (JSONObject) relComArray.get(k);
							String relComId = relCom.getString("pageId");
							String newDataCode = relCom.getString("dataItemCode");
							configures.put(relComId, newDataCode);
						}
					}
					containPageTable = geneaatePTableByPageId(containPageId, dataMap, configures, totalWidth);
				} else if ("1017".equals(comType)) { // 表格组件
					containPageTable = generateDataGridTable(component, dataMap, totalWidth);
				} else if ("1016".equals(comType)) { // 图片框

					String dataSource = component.getString("dataItemCode");
					String fileId = (String) dataMap.get(dataSource);
					byte[] buffer = getFileByFileId(fileId);
					if (buffer != null) {
						Image img = Image.getInstance(buffer);
						PdfPCell cell = new PdfPCell();
						cell.addElement(img);
						containPageTable = new PdfPTable(1);
						containPageTable.addCell(cell);
					}
				} else {
					if ("1009".equals(comType) || "1014".equals(comType)) { // 标签文本
						String title = component.getString("title") + "";
						// c.addElement(new Phrase(title,headFont));
						FontSelector chunkFont = generateFont(component.getString("fontfamily"),
								component.getString("fontsize"), component.getString("textstyle"));
						Phrase chunk = chunkFont.process(title);
						finalContent.add(chunk);

						// Font font =
						// getFont(component.getString("fontfamily"),
						// component.getString("fontsize"),
						// component.getString("textstyle"));
						// Chunk chunk = new Chunk(title, font);
						// if("1".equals(component.getString("textRise"))){
						// chunk.setTextRise(5);
						// }
						// chunk.setSplitCharacter(new ChineseSplitCharacter());
						// finalContent.add(chunk);
						//
					} else if ("1018".equals(comType)) { // 纯文本
						String content = "";
						if (StringUtils.isNotBlank(component.getString("textType"))
								&& "2".equals(component.getString("textType"))) { // 纯文本，后台数据
							String dataSource = component.getString("dataItemCode");
							content = (String) dataMap.get(dataSource);
						} else {
							content = component.getString("dataItemCode");
						}
						FontSelector chunkFont = generateFont(component.getString("fontfamily"),
								component.getString("fontsize"), component.getString("textstyle"));
						if (content != null) {
							Phrase chunk = chunkFont.process(content);
							// for (int k = 0; k < chunk.getChunks().size();
							// k++) {
							// Chunk tempChunk = chunk.getChunks().get(k);
							// tempChunk.setSplitCharacter(new
							// ChineseSplitCharacter());
							// }
							finalContent.add(chunk);
						}

					} else if ("1003".equals(comType) || "1004".equals(comType) || "1006".equals(comType)) {// radio,select,checkbox
						String dataSource = component.getString("dataItemCode");
						String content = (String) dataMap.get(dataSource);
						Paragraph itemContent = getParaByOptionItem(content, dataMap, component);
						finalContent.add(itemContent);
					} else if ("1019".equals(comType)) { // 富文本框
						HashMap map = new HashMap();
						map.put("font_factory", new MyFontFactory());
						String contextPath = servletContext.getRealPath("/");
						map.put("img_provider", new MyImageFactory(contextPath));

						String dataSource = component.getString("dataItemCode");
						String content = (String) dataMap.get(dataSource);
						List<Element> objects = new ArrayList<Element>();
						try {
							objects = HTMLWorker.parseToList(new StringReader(content), null, map);
						} catch (Exception e) {

						}

						for (Element element : objects)
							finalContent.add(element);
						// 设置富文本框默认打印属性
						height = 50f;
						heightType = 2;
						o.put("textverticalalign", "顶端对齐");
					} else {
						if (!"1010".equals(comType)) { // 非hidden组件
							String dataSource = component.getString("dataItemCode");
							String content = (String) dataMap.get(dataSource);

							if (content != null) {
								FontSelector chunkFont = generateFont(component.getString("fontfamily"),
										component.getString("fontsize"), component.getString("textstyle"));
								Phrase p = chunkFont.process(content);
								// for (int k = 0; k < p.getChunks().size();
								// k++) {
								// Chunk tempChunk = p.getChunks().get(k);
								// tempChunk.setSplitCharacter(new
								// ChineseSplitCharacter());
								// }
								finalContent.add(p);
							}

						}
					}
				}
			}

			if (containPageTable != null) { // 对于包含页面组件
				c = new PdfPCell(containPageTable);
			} else {
				// finalContent = dealParagraph(finalContent,
				// totalWidth,indent); //处理中文标点符号首列问题
				c = new PdfPCell(finalContent);
				c.setIndent(indent); // 设置文本缩进

				if (leading != 0) {
					c.setLeading(0, leading);
				} else {
					c.setLeading(0, DEFAULT_LINE_HEIGHT);
				}

				c.setUseAscender(true);

				c.setUseDescender(true);

				c.setVerticalAlignment(getVerticalAlignment(o.getString("textverticalalign")));
				c.setHorizontalAlignment(getHorizontalAlignment(o.getString("textalign")));

			}
		}

		if (height > 0) { // 如果行布局有设置了高度，则行下对应的单元格都设为对应高度，并判断设置的是最小高度还是固定高度
			if (heightType == 1) {
				c.setFixedHeight(height);
			} else {
				c.setMinimumHeight(height);
			}
		} else {
			if (containPageTable == null) {
				// c.setFixedHeight(20); //默认固定高度，对应包含组件，则不设默认固定高度
			}
		}

		if (borderless == false) {
			if (StringUtils.isNotBlank(o.getString("textstyle"))) { // 对于个别特殊的单元格有设置
																	// 无边框
				c.setBorder(Rectangle.NO_BORDER);
			} else {
				c.setBorderWidth(0.85f); // 边框
			}
		} else {
			c.setBorder(Rectangle.NO_BORDER);
		}

		if (o.getString("textstyle") != null && o.getString("textstyle").indexOf("1") != -1) { // 对于个别特殊的单元格有设置
																								// 上边框的，加上
																								// 上边框
			c.setBorderWidthTop(0.85f);
		}
		if (o.getString("textstyle") != null && o.getString("textstyle").indexOf("2") != -1) { // 对于个别特殊的单元格有设置
																								// 右边框的，加上右边框
			c.setBorderWidthRight(0.85f);
		}
		if (o.getString("textstyle") != null && o.getString("textstyle").indexOf("3") != -1) { // 对于个别特殊的单元格有设置
																								// 下边框的，加上下边框
			c.setBorderWidthBottom(0.85f);
		}
		if (o.getString("textstyle") != null && o.getString("textstyle").indexOf("4") != -1) { // 对于个别特殊的单元格有设置
																								// 左边框的，加上左边框
			c.setBorderWidthLeft(0.85f);
		}

		t.addCell(c);
		t.setSplitLate(false);
		return t;
	}

	/**
	 * 表单页面: 根据列布局获取对应的列宽度比例，根据占比配置，如果有列设置固定宽度，这先对该列设置固定宽度，其他按占比比例分配
	 * 
	 * @param layouts
	 * @return
	 */
	private float[] generateWidthsByLayouts(JSONArray layouts, float totalWidth) {

		float[] widths = new float[layouts.size()];
		Map<Integer, Float> widthMap = new HashMap<Integer, Float>();
		Map<Integer, Float> proportionMap = new HashMap<Integer, Float>();
		float widthSum = 0; // 保存设置固定宽度的总和
		float proportionSum = 0;
		for (int i = 0; i < layouts.size(); i++) {
			if (layouts.getJSONObject(i).getFloat("width") != null && layouts.getJSONObject(i).getFloat("width") > 0) {

				widthMap.put(i, layouts.getJSONObject(i).getFloat("width"));
				widthSum += layouts.getJSONObject(i).getFloat("width");
			} else {

				proportionMap.put(i, (layouts.getJSONObject(i).getFloat("proportion")));
				proportionSum += layouts.getJSONObject(i).getFloat("proportion");
			}
		}

		if (!widthMap.isEmpty()) {
			Iterator iter = widthMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int index = (Integer) entry.getKey();
				float width = (Float) entry.getValue();
				widths[index] = width;
			}
		}

		// 除去设置的固定宽度，剩余的按比例分配
		// widthSum = (float) (widthSum * 28.35); //单位将厘米转换为 磅
		totalWidth = (float) (totalWidth / 28.35);
		float restWidth = totalWidth - widthSum;
		if (!proportionMap.isEmpty()) {
			Iterator iter = proportionMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int index = (Integer) entry.getKey();
				float proportion = (Float) entry.getValue();
				float width = (restWidth * proportion) / proportionSum;
				widths[index] = width;
			}
		}
		return widths;
	}

	/**
	 * 列表页面: 根据列布局获取对应的列宽度比例，根据占比配置，如果有列设置固定宽度，这先对该列设置固定宽度，其他按比例分配
	 * 
	 * @param layouts
	 * @return
	 */
	private float[] generateWidthsByListPageLayouts(List<JSONObject> layouts, float totalWidth) {

		float[] widths = new float[layouts.size()];
		Map<Integer, Float> widthMap = new HashMap<Integer, Float>();
		Map<Integer, Float> proportionMap = new HashMap<Integer, Float>();
		float widthSum = 0; // 保存设置固定宽度的总和
		float proportionSum = 0;
		for (int i = 0; i < layouts.size(); i++) {
			if (layouts.get(i).getFloat("pdfWidth") != null && layouts.get(i).getFloat("pdfWidth") > 0) {

				widthMap.put(i, layouts.get(i).getFloat("pdfWidth"));
				widthSum += layouts.get(i).getFloat("pdfWidth");
			} else {
				//
				proportionMap.put(i, 1f);
				proportionSum += 1;
			}
		}

		if (!widthMap.isEmpty()) {
			Iterator iter = widthMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int index = (Integer) entry.getKey();
				float width = (Float) entry.getValue();
				widths[index] = width;
			}
		}

		// 除去设置的固定宽度，剩余的按比例分配
		// widthSum = (float) (widthSum * 28.35); //单位将厘米转换为 磅
		totalWidth = (float) (totalWidth / 28.35);
		float restWidth = totalWidth - widthSum;
		if (!proportionMap.isEmpty()) {
			Iterator iter = proportionMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int index = (Integer) entry.getKey();
				float proportion = (Float) entry.getValue();
				float width = (restWidth * proportion) / proportionSum;
				widths[index] = width;
			}
		}
		return widths;
	}

	private Map getLayoutAndComp(DynamicPageVO pageVO, Map configures) {

		Map<String, Object> root = new HashMap<String, Object>();

		BaseExample example = new BaseExample();
		example.createCriteria().andEqualTo("dynamicPage_id", pageVO.getId());

		List<Store> children = Store.selectByExample(example);
		List<JSONObject> mainLayouts = new ArrayList<JSONObject>();
		// 布局组件
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		List<JSONObject> coms = new ArrayList<JSONObject>();
		Map<String, List<JSONObject>> components = new HashMap<String, List<JSONObject>>();
		if (children != null && children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				Store o = children.get(i);
				if (o.getCode().indexOf(StoreService.LAYOUT_CODE) != -1) {
					JSONObject c = JSON.parseObject(o.getContent());
					map.put(c.getString("pageId"), c);
				} else if (o.getCode().indexOf(StoreService.COMPONENT_CODE) != -1) {
					JSONObject c = JSONObject.parseObject(o.getContent());
					if (c != null) {

						// 对于包含组件，需要替换dataItemCode，则替换
						if (configures != null && configures.get(c.getString("pageId")) != null) {
							if (c.containsKey("dataItemCode")) {
								c.put("dataItemCode", configures.get(c.getString("pageId")));
							}
						}

						String layoutId = c.getString("layoutId");
						if (components.get(layoutId) != null) {
							components.get(layoutId).add(c);
						} else {
							List<JSONObject> list = new ArrayList<JSONObject>();
							list.add(c);
							components.put(layoutId, list);
						}
						coms.add(c);
					}
				}
			}
		}
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			JSONObject v = map.get(key);
			if (!StringUtils.isEmpty((String) v.getString("parentId"))) {
				JSONObject p = map.get((String) v.getString("parentId"));
				if (p != null) {

					if (p.getJSONArray("childLayouts") == null) {
						List<JSONObject> tmp = new ArrayList<JSONObject>();
						p.put("childLayouts", tmp);
					}
					List<Object> list = p.getJSONArray("childLayouts");
					list.add(v);
					p.put("childLayouts", list);

				}
			} else {
				mainLayouts.add(v);
			}
		}

		root.put("layouts", mainLayouts);
		if (pageVO.getPageType() == 1002) { // 表单页面
			root.put("components", components);
		} else {
			root.put("components", coms); // 列表页面
		}
		return root;
	}

	private List<JSONObject> getLayout(Long id) {
		BaseExample example = new BaseExample();
		example.createCriteria().andEqualTo("dynamicPage_id", id);

		List<Store> children = Store.selectByExample(example);
		List<JSONObject> mainLayouts = new ArrayList<JSONObject>();
		// 布局组件
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();

		if (children != null && children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				Store o = children.get(i);
				if (o.getCode().indexOf(StoreService.LAYOUT_CODE) != -1) {

					JSONObject c = JSON.parseObject(o.getContent());
					map.put(c.getString("pageId"), c);
				}
			}
		}
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			JSONObject v = map.get(key);
			if (!StringUtils.isEmpty((String) v.getString("parentId"))) {
				JSONObject p = map.get((String) v.getString("parentId"));
				if (p != null) {

					if (p.getJSONArray("childLayouts") == null) {
						List<JSONObject> tmp = new ArrayList<JSONObject>();
						p.put("childLayouts", tmp);
					}
					List<Object> list = p.getJSONArray("childLayouts");
					list.add(v);
					p.put("childLayouts", list);

				}
			} else {
				mainLayouts.add(v);
			}
		}

		return mainLayouts;
	}

	private JSONArray sortByOrderArray(JSONArray list) {

		if (list == null || list.size() == 0)
			return list;

		List<JSONObject> layoutList = new ArrayList<JSONObject>();
		for (int i = 0; i < list.size(); i++) {

			layoutList.add(list.getJSONObject(i));
		}
		list.clear();
		Collections.sort(layoutList, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject o1, JSONObject o2) {
				int o1Order = o1.getIntValue("order");
				int o2Order = o2.getIntValue("order");
				return o1Order - o2Order;
			}
		});

		for (int i = 0; i < layoutList.size(); i++) {
			list.add(layoutList.get(i));
		}
		return list;
	}

	private FontSelector generateFont(String fontFamily, String fontSizeStr, String fontStyleStr)
			throws DocumentException, IOException {
		BaseFont bfChinese = null;
		if (fontFamily != null && "仿宋".equals(fontFamily)) {
			// bfChinese =
			// BaseFont.createFont("FANGSONG_GB2312.ttf",BaseFont.IDENTITY_H,
			// BaseFont.NOT_EMBEDDED);//设置中文字体
			String s = PrintServiceImpl.class.getResource("/").getFile() + "font" + File.separator
					+ "FANGSONG_GB2312.ttf";
			File path = new File(s);
			bfChinese = BaseFont.createFont(path.getCanonicalPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			// bfChinese =
			// BaseFont.createFont("D:\\FANGSONG_GB2312.ttf",BaseFont.IDENTITY_H,
			// BaseFont.NOT_EMBEDDED);//设置中文字体
		} else {
			bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 设置中文字体
		}

		BaseFont bfEnglish = BaseFont.createFont();
		float fontSize = 12f;
		if (fontSizeStr != null && !"".equals(fontSizeStr)) {

			fontSize = Float.parseFloat(fontSizeStr);
		}
		int fontStyle = Font.NORMAL;
		if (fontStyleStr != null && fontStyleStr.indexOf("weight") != -1) {
			fontStyle = fontStyle | Font.BOLD;
		}
		if (fontStyleStr != null && fontStyleStr.indexOf("italic") != -1) {
			fontStyle = fontStyle | Font.ITALIC;
		}
		if (fontStyleStr != null && fontStyleStr.indexOf("underline") != -1) {
			fontStyle = fontStyle | Font.UNDERLINE;
		}
		Font finalFont = new Font(bfChinese, fontSize, fontStyle);// 设置字体大小

		FontSelector selector = new FontSelector();

		selector.addFont(FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize, fontStyle));

		selector.addFont(finalFont);
		return selector;
	}

	private Font getFont(String fontFamily, String fontSizeStr, String fontStyleStr)
			throws DocumentException, IOException {
		BaseFont bfChinese = null;
		if (fontFamily != null && "仿宋".equals(fontFamily)) {
			// bfChinese =
			// BaseFont.createFont("FANGSONG_GB2312.ttf","UniGB-UCS2-H",
			// BaseFont.NOT_EMBEDDED);//设置中文字体
			String s = PrintServiceImpl.class.getResource("/").getFile() + "font" + File.separator
					+ "FANGSONG_GB2312.ttf";
			File path = new File(s);
			bfChinese = BaseFont.createFont(path.getCanonicalPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			// bfChinese =
			// BaseFont.createFont("D:\\FANGSONG_GB2312.ttf",BaseFont.IDENTITY_H,
			// BaseFont.NOT_EMBEDDED);//设置中文字体
		} else {
			bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 设置中文字体
		}

		BaseFont bfEnglish = BaseFont.createFont();
		float fontSize = 12f;
		if (fontSizeStr != null && !"".equals(fontSizeStr)) {

			fontSize = Float.parseFloat(fontSizeStr);
		}
		int fontStyle = Font.NORMAL;
		if (fontStyleStr != null && fontStyleStr.indexOf("weight") != -1) {
			fontStyle = fontStyle | Font.BOLD;
		}
		if (fontStyleStr != null && fontStyleStr.indexOf("italic") != -1) {
			fontStyle = fontStyle | Font.ITALIC;
		}
		if (fontStyleStr != null && fontStyleStr.indexOf("underline") != -1) {
			fontStyle = fontStyle | Font.UNDERLINE;
		}
		Font finalFont = new Font(bfChinese, fontSize, fontStyle);// 设置字体大小

		return finalFont;
	}

	private int getVerticalAlignment(String verticalAlignment) {

		int vAlignment = Element.ALIGN_MIDDLE;
		if (verticalAlignment != null && "顶端对齐".equals(verticalAlignment)) {
			vAlignment = Element.ALIGN_TOP;
		} else if (verticalAlignment != null && "底端对齐".equals(verticalAlignment)) {
			vAlignment = Element.ALIGN_BOTTOM;
		}
		return vAlignment;
	}

	private int getHorizontalAlignment(String horizontalAlignment) {

		int hAlignment = Element.ALIGN_LEFT;
		if (horizontalAlignment != null && "居中".equals(horizontalAlignment)) {
			hAlignment = Element.ALIGN_CENTER;
		} else if (horizontalAlignment != null && "居右".equals(horizontalAlignment)) {
			hAlignment = Element.ALIGN_RIGHT;
		} else if (horizontalAlignment != null && "两端对齐".equals(horizontalAlignment)) {
			hAlignment = Element.ALIGN_JUSTIFIED;
		}
		return hAlignment;
	}

	private Paragraph getParaByOptionItem(String value, Map dataMap, JSONObject component)
			throws DocumentException, IOException {
		Paragraph finalContent = new Paragraph();
		String options = (String) dataMap.get(component.getString("dataItemCode") + "_option");
		char[] selectCode = new char[1];
		selectCode[0] = 0xFE; // 打钩框对应的字符 对应的字符代码为0xFE
		char[] noSelectCode = new char[1];
		noSelectCode[0] = 0xA8; // 没打钩框对应的字符 对应的字符代码为0xFE

		if (StringUtils.isBlank(value) || StringUtils.isBlank(options)) {
			return finalContent;
		}
		// 将options "1=是;2=否" 格式转为map
		Map<String, String> optionMap = new LinkedHashMap<String, String>();
		String[] optionStr = options.split("\\;");
		if (optionStr.length > 0) {
			for (String str : optionStr) {
				String[] entry = str.split("\\=");
				optionMap.put(entry[0], entry[1]);
			}
		}
		List<String> valueList = new ArrayList<String>();
		String[] values = value.split("\\;");
		if (values.length > 0) {
			for (String str : values) {
				valueList.add(str);
			}
		}
		FontSelector chunkFont = generateFont(component.getString("fontfamily"), component.getString("fontsize"),
				component.getString("textstyle"));

		if (component.getString("printAllOption") != null && "1".equals(component.getString("printAllOption"))) {// 打印所有选项
			// logger.debug(PrintServiceImpl.class.getResource("/").getFile()
			// + "font/WINGDING.TTF" +" sassssssssssssssss");
			BaseFont bfChinese = BaseFont.createFont(
					(PrintServiceImpl.class.getResource("/").getFile() + "font" + File.separator + "WINGDING.TTF")
							.substring(1),
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			// BaseFont bfChinese =
			// BaseFont.createFont("C:\\Windows\\Fonts\\WINGDING.TTF",
			// BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Font fontSONG = new Font(bfChinese, 12, Font.NORMAL);
			for (Map.Entry<String, String> entry : optionMap.entrySet()) {
				if (valueList.contains(entry.getKey())) { // 选择值，则打印时前面加上 √
					Chunk chunkSelect = new Chunk(new String(selectCode), fontSONG);
					finalContent.add(chunkSelect);
				} else {
					Chunk chunkNoSelect = new Chunk(new String(noSelectCode), fontSONG);
					finalContent.add(chunkNoSelect);
				}
				finalContent.add(chunkFont.process(entry.getValue() + ""));
			}

		} else {
			StringBuilder sb = new StringBuilder();
			String[] valueArr = value.split("\\;");
			for (int j = 0; j < valueArr.length; j++) {
				sb.append(optionMap.get(valueArr[j]) + ";");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.lastIndexOf(";"));
			}
			if (StringUtils.isNotBlank(sb.toString())) {

				finalContent.add(chunkFont.process(sb.toString() + ""));
			} else {
				finalContent.add(chunkFont.process(value + " ")); // 正常不会执行到这一步，对于导数据的，个别错乱数据，如果coed有值，但是在options中获取不到，则原样打印code
			}
		}
		return finalContent;
	}

	private String getItemByCodeAndScript(String code, String script) {
		if (StringUtils.isEmpty(code)) {
			return " ";
		}

		String value = DocumentUtils.getItem(code);
		if (StringUtils.isNotBlank(value)) {
			return value;
		} else { // 如果key不为空，但value从数据字典中取不到，则option脚本为"1=是;0=否"格式
			if (StringUtils.isNotBlank(script)) {
				String ret = "";
				ScriptEngine engine = ScriptEngineUtils.getScriptEngine();
				try {
					ret = (String) engine.eval(script);
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Map<String, String> map = new HashMap<String, String>();
				String[] optionStr = ret.split("\\;");
				if (optionStr.length > 0) {
					for (String str : optionStr) {
						String[] entry = str.split("\\=");
						map.put(entry[0], entry[1]);
					}

				}
				return (String) map.get(code);
			}
		}
		return " ";
	}

	/**
	 * 根据文件Id获取到mangoDB的相片，返回对应的二进制数组
	 * 
	 * @param fileId
	 * @return
	 */
	public byte[] getFileByFileId(String fileId) {

		if (StringUtils.isBlank(fileId)) {
			return null;
		}

		MongoClient client = MongoDBUtils.getMongoClient();
		DB db = client.getDB("myConfigFiles");
		GridFS myFS = new GridFS(db);
		DBObject query = new BasicDBObject("id", fileId);
		GridFSDBFile file = myFS.findOne(query);
		if (file != null) {
			InputStream is = file.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				byte[] b = new byte[1024];
				int n;
				while ((n = is.read(b)) != -1) {
					bos.write(b, 0, n);
				}
				is.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (client != null) {
					client.close();
				}
			}
			return bos.toByteArray();
		}
		return null;
	}

	private Paragraph dealParagraph(Paragraph p, float remainWidth, float indent) {
		float j = 0;
		Paragraph result = new Paragraph();
		for (int i = 0; i < p.getChunks().size(); i++) {
			Chunk temp = p.getChunks().get(i);
			BaseFont bfChinese = temp.getFont().getBaseFont();
			Font headFont = temp.getFont();
			// String hyphen = "、";
			// float hyphenWidth = bfChinese.getWidthPoint(hyphen,
			// headFont.getSize());
			if (i == 0) { // 考虑缩进
				j += indent;
			}
			float digitalWidth = 0;// 处理数字结尾，需要整体往下挪的情况
			if (temp.getWidthPoint() + j > remainWidth) {
				String s = temp.getContent();
				int len = s.length();
				float width = 0F;
				int start = 0;
				int index = 0;
				float k = j;
				while (index < len) {
					width = k + bfChinese.getWidthPointKerned(s.substring(start, index), headFont.getSize());// 考虑缩进
					if (width >= remainWidth) {
						char c = s.charAt(index - 1);
						if (ChineseSymbolSplit.chSymSplits.contains(c)) { // 中文标点符号收尾
							String tmp = s.substring(start, index);
							Chunk chunk = new Chunk(tmp);
							chunk.setFont(temp.getFont());
							chunk.setAttributes(temp.getAttributes());
							float cz = width - remainWidth + 1;
							float space = 0;
							if (cz > 0) {
								space = cz / (index - start);
							}
							chunk.setCharacterSpacing(0 - space);
							chunk.setSplitCharacter(new ChineseSplitCharacter());
							result.add(chunk);
							start = index;
						} else { // 非中文标点符号收尾
							String tmp = s.substring(start, index - 1);
							Chunk chunk = new Chunk(tmp);
							chunk.setFont(temp.getFont());
							chunk.setAttributes(temp.getAttributes());
							chunk.setSplitCharacter(new ChineseSplitCharacter());
							result.add(chunk);
							start = index - 1;
							char nextChar = s.charAt(index);
							if ((Character.isDigit(c) && Character.isDigit(nextChar))
									|| (Character.isDigit(c) && nextChar == '.')) {
								int digitalIndex = 0;
								// char cTemp = s.charAt(index-1-digitalIndex);
								while (Character.isDigit(s.charAt(index - 1 - digitalIndex))
										&& (index - 1 - digitalIndex > 0)) {
									digitalIndex++;
								}
								digitalWidth += bfChinese.getWidthPointKerned(
										s.substring(index - 1 - digitalIndex, index), headFont.getSize());
							}
						}
						k = 0;
					}
					index++;
				}
				// end while

				if (start < index) {
					String tmp = s.substring(start, index);
					Chunk chunk = new Chunk(tmp);
					chunk.setFont(temp.getFont());
					chunk.setAttributes(temp.getAttributes());
					chunk.setSplitCharacter(new ChineseSplitCharacter());
					result.add(chunk);
				}
			} else {
				result.add(temp);
			}
			j = (j + temp.getWidthPoint() + digitalWidth) % remainWidth;
		}
		return result;
	}

}

/**
 * 不能放在行首的中文标点
 * 
 * @author Administrator
 *
 */
class ChineseSymbolSplit {
	public static List<Character> chSymSplits;

	static {
		chSymSplits = new ArrayList<Character>();
		chSymSplits.add('，');
		chSymSplits.add('。');
		chSymSplits.add('！');
		chSymSplits.add('；');
		chSymSplits.add('？');
		chSymSplits.add('、');

		/** 添加你所需的标点 ***/

	}

	public static boolean isIncludeChar(int srcChar) {
		for (int i = 0; i < chSymSplits.size(); i++) {
			if (chSymSplits.get(i) == srcChar) {
				return true;
			}
		}
		return false;
	}
}

class ChineseSplitCharacter implements SplitCharacter {

	/**
	 * An instance of the default SplitCharacter.
	 */
	public static final SplitCharacter DEFAULT = new DefaultSplitCharacter();

	/**
	 * Checks if a character can be used to split a <CODE>PdfString</CODE>.
	 * <P>
	 * for the moment every character less than or equal to SPACE, the character
	 * '-' and some specific unicode ranges are 'splitCharacters'.
	 * 
	 * @param start
	 *            start position in the array
	 * @param current
	 *            current position in the array
	 * @param end
	 *            end position in the array
	 * @param cc
	 *            the character array that has to be checked
	 * @param ck
	 *            chunk array
	 * @return <CODE>true</CODE> if the character can be used to split a string,
	 *         <CODE>false</CODE> otherwise
	 */
	public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
		char c = getCurrentCharacter(current, cc, ck);
		if (c <= ' ' || c == '-' || c == '\u2010') {
			return true;
		}
		if (c < 0x2002)
			return false;
		if (ChineseSymbolSplit.chSymSplits.contains(c)) {
			return true;
		}
		return ((c >= 0x2002 && c <= 0x200b) || (c >= 0x2e80 && c < 0xd7a0) || (c >= 0xf900 && c < 0xfb00)
				|| (c >= 0xfe30 && c < 0xfe50) || (c >= 0xff61 && c < 0xffa0));
	}

	/**
	 * Returns the current character
	 * 
	 * @param current
	 *            current position in the array
	 * @param cc
	 *            the character array that has to be checked
	 * @param ck
	 *            chunk array
	 * @return the current character
	 */
	protected char getCurrentCharacter(int current, char[] cc, PdfChunk[] ck) {
		if (ck == null) {
			return cc[current];
		}
		return (char) ck[Math.min(current, ck.length - 1)].getUnicodeEquivalent(cc[current]);
	}
}