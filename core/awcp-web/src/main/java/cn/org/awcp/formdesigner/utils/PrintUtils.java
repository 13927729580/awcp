package cn.org.awcp.formdesigner.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.formdesigner.core.domain.Store;
import cn.org.awcp.formdesigner.core.domain.design.context.component.Layout;

public class PrintUtils {
	/**
	 * @param inputStream
	 *            文件内容输入流
	 */
	public static void printPdf(Map<String, Map<String, String>> data, String path, JSONObject templet) {
		// 页边距
		int left = templet.getIntValue("leftDistance");
		int right = templet.getIntValue("rightDistance");
		int top = templet.getIntValue("topDistance");
		int bottom = templet.getIntValue("botDistance");

		// 页眉页脚
		String headerStr = templet.getString("pageHeader");
		String bottomStr = templet.getString("pageBottom");

		// 打印的页面列表
		List<Long> pageList = new ArrayList<Long>();
		JSONArray objectList = JSON.parseArray(templet.getString("select_dynamicPage"));
		if (objectList != null && objectList.size() > 0) {
			for (int i = 0; i < objectList.size(); i++) {
				pageList.add(objectList.getJSONObject(i).getLongValue("id"));
			}
		}

		String mPdfPath = "E:/11/";
		Document document = new Document(PageSize.A4, left, right, top, bottom);// 建立一个Document对象
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(mPdfPath + "taony125-test.pdf"));// 建立一个PdfWriter对象

			Rectangle rect = new Rectangle(36, 54, 559, 788);
			rect.setBorderColor(BaseColor.BLACK);
			writer.setBoxSize("art", rect);
			PdfHeaderFooter header = new PdfHeaderFooter(headerStr, bottomStr);

			// writer.setPageEvent(header); 页眉页脚

			document.open();
			// BaseFont bfChinese =
			// BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);//设置中文字体

			PdfPTable table = new PdfPTable(4);
			for (int i = 0; i < pageList.size(); i++) {
				document.newPage(); // 新一页
				Map map = getLayoutAndComp((Long) pageList.get(i));
				List<JSONObject> mainLayouts = (List<JSONObject>) map.get("layouts");
				mainLayouts = sortByOrder(mainLayouts); // 排序
				Map<String, List<JSONObject>> components = (Map<String, List<JSONObject>>) map.get("components");
				PdfPTable mainTable = new PdfPTable(1);

				if (mainLayouts != null && mainLayouts.size() > 0) {
					for (int j = 0; j < mainLayouts.size(); j++) {
						mainTable.addCell(new PdfPCell(createTable(mainLayouts.get(j), components, data)));
					}
				}
				document.add(mainTable);
			}

		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}

	private static PdfPTable createTable(JSONObject o, Map<String, List<JSONObject>> components, Map dataMap)
			throws DocumentException, IOException {

		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 设置中文字体

		Font headFont = new Font(bfChinese, 10, Font.BOLD);// 设置字体大小

		String type = o.getString("layoutType");
		PdfPTable t = null;
		if ("2".equalsIgnoreCase(type)) {
			if (o.getJSONArray("childLayouts") == null || o.getJSONArray("childLayouts").size() == 0) {
				// t = new PdfPTable(1);
				PdfPCell c = new PdfPCell();
				// c.setHorizontalAlignment(Element.ALIGN_CENTER);
				// c.setColspan(1);
				// c.addElement(new Phrase(" "));
				String layoutId = o.getString("pageId");
				List compList = components.get(layoutId);
				if (compList != null && compList.size() > 0) {
					Phrase finalContent = new Phrase();
					for (int i = 0; i < compList.size(); i++) {
						JSONObject component = (JSONObject) compList.get(i);
						String comType = component.getString("componentType");
						if ("1009".equals(comType)) {
							String title = component.getString("title");
							// c.addElement(new Phrase(title,headFont));
							finalContent.add(new Chunk(title, headFont));
						} else {
							String dataSource = component.getString("dataItemCode");
							String content = (String) dataMap.get(dataSource);
							// c.addElement(new Phrase(content,headFont));
							finalContent.add(new Chunk(content, headFont));
						}
					}
					c.addElement(finalContent);

				}

				t.addCell(c);
				// return new PdfPCell();
			} else {
				JSONArray array = o.getJSONArray("childLayouts");
				array = sortByOrderArray(array);
				float[] widths = new float[array.size()];
				for (int i = 0; i < array.size(); i++) {
					widths[i] = array.getJSONObject(i).getIntValue("proportion");
				}
				// t = new PdfPTable(array.size());
				t = new PdfPTable(widths);
				for (int i = 0; i < array.size(); i++) {
					PdfPCell p = new PdfPCell(createTable(array.getJSONObject(i), components, dataMap));
					// p.setColspan(array.getJSONObject(i).getIntValue("proportion"));
					t.addCell(p);
					// t.addCell(new
					// PdfPCell(createTable(array.getJSONObject(i))));
				}
			}
		} else {
			t = new PdfPTable(1);
			if (o.getJSONArray("childLayouts") == null || o.getJSONArray("childLayouts").size() == 0) {
				// t = new PdfPTable(1);
				PdfPCell c = new PdfPCell();
				// c.setHorizontalAlignment(Element.ALIGN_CENTER);
				// c.setColspan(1);
				// c.addElement(new Phrase(" "));
				String layoutId = o.getString("pageId");
				List compList = components.get(layoutId);
				if (compList != null && compList.size() > 0) {
					Phrase finalContent = new Phrase();
					for (int i = 0; i < compList.size(); i++) {
						JSONObject component = (JSONObject) compList.get(i);
						String comType = component.getString("componentType");
						if ("1009".equals(comType)) {
							String title = component.getString("title");
							// c.addElement(new Phrase(title,headFont));
							finalContent.add(new Chunk(title, headFont));
						} else {
							String dataSource = component.getString("dataItemCode");
							String content = (String) dataMap.get(dataSource);
							// c.addElement(new Phrase(content,headFont));
							finalContent.add(new Chunk(content, headFont));
						}
					}
					c.addElement(finalContent);

				}

				t.addCell(c);
				// return new PdfPCell();
			} else {
				JSONArray array = o.getJSONArray("childLayouts");
				for (int i = 0; i < array.size(); i++) {
					t.addCell(new PdfPCell(createTable(array.getJSONObject(i), components, dataMap)));
				}
			}
		}

		return t;

	}

	public static Map getLayoutAndComp(Long id) {

		Map<String, Object> root = new HashMap<String, Object>();

		BaseExample example = new BaseExample();
		example.createCriteria().andEqualTo("dynamicPage_id", id);

		List<Store> children = Store.selectByExample(Store.class, example);
		List<JSONObject> mainLayouts = new ArrayList<JSONObject>();
		// 布局组件
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();

		Map<String, List<JSONObject>> components = new HashMap<String, List<JSONObject>>();
		if (children != null && children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				Store o = children.get(i);
				if (o.getCode().indexOf(StoreService.LAYOUT_CODE) != -1) {
					// SimpleComponent c = JSON.parseObject(o.getContent(),
					// SimpleComponent.class);
					JSONObject c = JSON.parseObject(o.getContent());
					map.put(c.getString("pageId"), c);
				} else if (o.getCode().indexOf(StoreService.COMPONENT_CODE) != -1) {
					// SimpleComponent c = (SimpleComponent)
					// PageComponentBeanWorker.convertConfToComponet(o.getContent());
					JSONObject c = JSONObject.parseObject(o.getContent());
					if (c != null) {
						String layoutId = c.getString("layoutId");
						if (components.get(layoutId) != null) {
							components.get(layoutId).add(c);
						} else {
							List<JSONObject> list = new ArrayList<JSONObject>();
							list.add(c);
							components.put(layoutId, list);
						}
						// coms.add(c);
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
		root.put("components", components);

		return root;
	}

	private List<JSONObject> getLayout(Long id) {
		BaseExample example = new BaseExample();
		example.createCriteria().andEqualTo("dynamicPage_id", id);

		List<Store> children = Store.selectByExample(Store.class, example);
		List<JSONObject> mainLayouts = new ArrayList<JSONObject>();
		// 布局组件
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();

		if (children != null && children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				Store o = children.get(i);
				if (o.getCode().indexOf(StoreService.LAYOUT_CODE) != -1) {
					// SimpleComponent c = JSON.parseObject(o.getContent(),
					// SimpleComponent.class);
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

	public static List<JSONObject> sortByOrder(List<JSONObject> list) {

		if (list == null || list.size() == 0) {
            return list;
        }

		List<Layout> layoutList = new ArrayList<Layout>();
		for (int i = 0; i < list.size(); i++) {
			Layout o = JSON.toJavaObject(list.get(i), Layout.class);
			layoutList.add(o);
		}
		list.clear();
		Collections.sort(layoutList);
		for (int i = 0; i < layoutList.size(); i++) {
			list.add((JSONObject) JSON.toJSON(layoutList.get(i)));
		}
		return list;
	}

	public static JSONArray sortByOrderArray(JSONArray list) {

		if (list == null || list.size() == 0) {
            return list;
        }

		List<Layout> layoutList = new ArrayList<Layout>();
		for (int i = 0; i < list.size(); i++) {
			Layout o = JSON.toJavaObject(list.getJSONObject(i), Layout.class);
			layoutList.add(o);
		}
		list.clear();
		Collections.sort(layoutList);
		for (int i = 0; i < layoutList.size(); i++) {
			list.add((JSONObject) JSON.toJSON(layoutList.get(i)));
		}
		return list;
	}

}
