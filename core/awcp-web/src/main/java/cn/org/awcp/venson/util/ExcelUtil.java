package cn.org.awcp.venson.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 利用开源组件POI导出,导入，校验EXCEL文档！
 * 
 * @author YWX
 */
@SuppressWarnings("rawtypes")
public class ExcelUtil {

	/**
	 * 
	 * 根据模板导出excel
	 *
	 * @param data
	 *            数据源
	 * @param file
	 *            模板文件
	 * @param keys
	 *            单元格的键值
	 * @param stream
	 *            输出流
	 */
	public static void exportExcelByTemplate(List<Map<?, ?>> data, InputStream in, OutputStream stream) {
		HSSFWorkbook workbook = null;
		try {

			POIFSFileSystem poi = new POIFSFileSystem(in);
			workbook = new HSSFWorkbook(poi);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow fristRow = sheet.getRow(0);
			int rowNum = sheet.getLastRowNum() + 1;
			short cellNum = fristRow.getLastCellNum();
			for (Map map : data) {
				// 创建行
				HSSFRow row = sheet.createRow(rowNum);
				for (int i = 0; i < cellNum; i++) {
					HSSFCell fricell = fristRow.getCell(i);
					String key = getValue(fristRow.getCell(i));
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(fricell.getCellStyle());
					if ("序号".equals(key)) {
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(rowNum);
					} else {
						Object value = map.get(key);
						// 判断值是否为空
						if (value != null) {
							// 判断值的类型后进行强制类型转换
							if (value instanceof String) {
								cell.setCellValue((String) value);
							} else if (value instanceof Boolean) {
								cell.setCellValue((Boolean) value);
							} else if (value instanceof Date) {
								cell.setCellValue((Date) value);
							} else if (value instanceof Double) {
								cell.setCellValue((Double) value);
							} else if (value instanceof Calendar) {
								cell.setCellValue((Calendar) value);
							} else {
								cell.setCellValue(value.toString());
							}
						} else {
							cell.setCellValue("");
						}
					}

				}
				rowNum++;
			}
			// 创建输出流
			workbook.write(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(workbook);
		}
	}

	/**
	 * 创建excel并写入数据
	 * 
	 * @param workbook
	 *            要写入的工作簿
	 * @param headStyle
	 *            头部标题样式
	 * @param contentStyle
	 *            内容样式
	 * @param headers
	 *            表格属性列名数组
	 * @param data
	 *            数据源
	 * @param keys
	 *            单元格的键值
	 */
	public static void exportExcelByCreate(List<Map<?, ?>> cloums, List<Map<?, ?>> data, OutputStream stream) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		// 产生表格标题行
		HSSFRow header = sheet.createRow(0);
		HSSFCellStyle headStyle = getBasicStyle(workbook);
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 16); // 字体高度
		font.setBold(true); // 宽度
		font.setFontName("宋体"); // 字体
		headStyle.setFont(font);
		List<String> fields = new ArrayList<String>();
		List<String> headers = new ArrayList<String>();
		for (Map head : cloums) {
			headers.add(head.get("title").toString());
			fields.add(head.get("field").toString());
		}
		for (int i = 0; i < headers.size(); i++) {
			HSSFCell cell = header.createCell(i);
			cell.setCellStyle(headStyle);
			HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
			cell.setCellValue(text);
		}
		HSSFCellStyle cloumnStyle = getBasicStyle(workbook);
		for (int i = 0; i < data.size(); i++) {
			Map map = data.get(i);
			// 创建行
			HSSFRow row = sheet.createRow(i + 1);
			// 创建列
			for (int j = 0; j < fields.size(); j++) {
				Object value = map.get(fields.get(j));
				// 创建单元格
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(cloumnStyle);
				// 判断值是否为空
				if (value != null) {
					// 判断值的类型后进行强制类型转换
					if (value instanceof String) {
						cell.setCellValue((String) value);
					} else if (value instanceof Boolean) {
						cell.setCellValue((Date) value);
					} else if (value instanceof Date) {
						cell.setCellValue((Date) value);
					} else if (value instanceof Double) {
						cell.setCellValue((Double) value);
					} else if (value instanceof Calendar) {
						cell.setCellValue((Calendar) value);
					} else {
						cell.setCellValue(value.toString());
					}
				} else {
					cell.setCellValue("");
				}
			}
		}
		// 自动适应列宽
		for (int i = 0; i < headers.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		try {
			workbook.write(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(workbook);
		}

	}

	// 设置基本样式
	public static HSSFCellStyle getBasicStyle(HSSFWorkbook workbook) {
		// 设置字体
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 14); // 字体号数
		font.setFontName("宋体"); // 字体
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
		// font.setItalic(true); // 是否使用斜体
		// font.setStrikeout(true); //是否使用划线

		// 设置单元格类型
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
		style.setWrapText(true);
		return style;
	}

	/**
	 * 合并单元格处理--加入list
	 * 
	 * @param sheet
	 * @return
	 */
	public static List<CellRangeAddress> getCombineCell(Sheet sheet) {
		// 获得一个 sheet 中合并单元格的数量
		int sheetmergerCount = sheet.getNumMergedRegions();
		List<CellRangeAddress> list = new ArrayList<>();
		// 遍历合并单元格
		for (int i = 0; i < sheetmergerCount; i++) {
			// 获得合并单元格加入list中
			CellRangeAddress ca = sheet.getMergedRegion(i);
			list.add(ca);
		}
		return list;
	}

	static class Result {
		public boolean merged = false;
		public int startRow;
		public int endRow;
		public int startCol;
		public int endCol;

		public Result(int startRow, int endRow, int startCol, int endCol) {
			this.merged = true;
			this.startRow = startRow;
			this.endRow = endRow;
			this.startCol = startCol;
			this.endCol = endCol;
		}

		@Override
		public String toString() {
			return "Result [merged=" + merged + ", startRow=" + startRow + ", endRow=" + endRow + ", startCol="
					+ startCol + ", endCol=" + endCol + "]";
		}

	}

	/**
	 * 判断单元格是否为合并单元格
	 * 
	 * @param listCombineCell
	 *            存放合并单元格的list
	 * @param cell
	 *            需要判断的单元格
	 * @return
	 */
	public static Result isCombineCell(List<CellRangeAddress> listCombineCell, Cell cell) {
		int firstC = 0;
		int lastC = 0;
		int firstR = 0;
		int lastR = 0;
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (cell.getColumnIndex() <= lastC && cell.getColumnIndex() >= firstC) {
				if (cell.getRowIndex() <= lastR && cell.getRowIndex() >= firstR) {
					return new Result(firstR, lastR, firstC, lastC);
				}
			}
		}
		return null;
	}

	/**
	 * 从excel中获取数据
	 * 
	 * @param excelFile
	 *            输入文件流
	 * @param fileName
	 *            文件名称，包含后缀名
	 * @param startRow
	 *            开始行号
	 * @param startCol
	 *            开始列号
	 * @param sheetNum
	 *            哪一页
	 * @throws IOException
	 */
	public static List<Map<String, String>> getDataFromExcel(InputStream excelFile, String ext, int startRow,
			int startCol, int sheetNum) {
		List<Map<String, String>> varList = new ArrayList<Map<String, String>>();
		Workbook workbook = null;
		try {
			if ("xls".equals(ext)) {
				workbook = new HSSFWorkbook(excelFile);
			} else {
				workbook = new XSSFWorkbook(excelFile);
			}
			getSheetData(workbook, varList, sheetNum, startRow, startCol);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(excelFile);
			IOUtils.closeQuietly(workbook);
		}
		return varList;
	}

	public static List<Map<String, String>> getDataFromExcel(InputStream excelFile, String ext, int startRow,
			int startCol, int[] numArr) {
		List<Map<String, String>> varList = new ArrayList<Map<String, String>>();
		Workbook workbook = null;
		try {
			if ("xls".equals(ext)) {
				workbook = new HSSFWorkbook(excelFile);
			} else {
				workbook = new XSSFWorkbook(excelFile);
			}
			for (int sheetNum : numArr) {
				getSheetData(workbook, varList, sheetNum, startRow, startCol);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(excelFile);
			IOUtils.closeQuietly(workbook);
		}
		return varList;
	}

	private static void getSheetData(Workbook workbook, List<Map<String, String>> varList, int sheetNum, int startRow,
			int startCol) {
		// sheet 从0开始
		Sheet sheet = workbook.getSheetAt(sheetNum);
		// 获取合并单元格
		List<CellRangeAddress> combineCell = getCombineCell(sheet);
		// 取得最后一行的行号
		int rowNum = sheet.getLastRowNum() + 1;
		// 行循环开始
		for (int i = startRow; i < rowNum; i++) {
			// 行
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}

			Row firstRow = sheet.getRow(0);
			if (firstRow == null) {
				return;
			}
			// 每行的最后一个单元格位置
			int cellNum = row.getLastCellNum();
			Map<String, String> rowData = new HashMap<>(cellNum);
			// 列循环开始
			for (int j = startCol; j < cellNum; j++) {
				Cell cell = row.getCell(j);
				// 如果合并单元格不为空则判断当前单元格是否是合并单元格
				if (!combineCell.isEmpty()) {
					Result isMerge = isCombineCell(combineCell, cell);
					if (null != isMerge) {
						cell = sheet.getRow(isMerge.startRow).getCell(isMerge.startCol);
					}
				}
				String cellValue = null;
				if (null != cell) {
					cellValue = getValue(cell);
				} else {
					cellValue = "";
				}

				rowData.put(firstRow.getCell(j).toString(), cellValue.trim());

			}
			varList.add(rowData);
		}
	}

	private static String getValue(Cell cell) {
		String cellValue;
		// 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
		switch (cell.getCellTypeEnum()) {
		case NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				cellValue = DateFormatUtils.format(date, "yyyy-MM-dd");
			} else {
				DecimalFormat df = new DecimalFormat("0");
				cellValue = df.format(cell.getNumericCellValue());
			}
			break;
		case STRING:
			cellValue = cell.getStringCellValue();
			break;
		case FORMULA:
			cellValue = cell.getCellFormula();
			break;
		case BLANK:
			cellValue = "";
			break;
		case BOOLEAN:
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		case ERROR:
			cellValue = String.valueOf(cell.getErrorCellValue());
			break;
		default:
			cellValue = "";
			break;
		}
		return cellValue;
	}

	/**
	 * 从excel中获取数据
	 * 
	 * @param excelFile
	 *            输入文件流
	 * @param fileName
	 *            文件名称，包含后缀名
	 * @throws IOException
	 */
	public static List<Map<String, String>> getDataFromExcel(InputStream excelFile, String ext) {
		return ExcelUtil.getDataFromExcel(excelFile, ext, 1, 0, 0);
	}

	/**
	 * 校验导入excel文件的数据
	 * 
	 * @param userFile
	 *            导入文件
	 * @param templateFile
	 *            模板文件
	 * @param startRow
	 *            起始行
	 * @param startCol
	 *            起始列
	 * @param sheetNum
	 *            哪一页
	 * @param isNotNull
	 *            是否允许为空
	 * 
	 */
	public static String matchExcelData(InputStream userFile, String usrExt, InputStream templateFile, int startRow,
			int startCol, int sheetNum, boolean isNotNull) {
		String result = null;
		Workbook usrbook = null;
		Workbook tmpbook = null;
		try {
			// 获取文件，并创建工作簿
			if ("xls".equalsIgnoreCase(usrExt)) {
				usrbook = new HSSFWorkbook(userFile);
			} else {
				usrbook = new XSSFWorkbook(userFile);
			}
			tmpbook = new HSSFWorkbook(templateFile);
			// 获取指定页
			Sheet usrSheet = usrbook.getSheetAt(sheetNum);
			Sheet tmpSheet = tmpbook.getSheetAt(sheetNum);
			if (usrSheet.getLastRowNum() < 1 || tmpSheet.getLastRowNum() < 1) {
				return "导入文件中并未找到可导入的数据";
			}

			// 获取指定行
			Row usrRow = usrSheet.getRow(startRow);
			Row tmpRow = tmpSheet.getRow(startRow);
			if (usrRow == null || tmpRow == null) {
				return "导入文件的指定行列值为空";
			}

			// 匹配指定列值
			for (int i = startCol; i < tmpRow.getLastCellNum(); i++) {
				Cell usrCell = usrRow.getCell(i);
				Cell tmpCell = tmpRow.getCell(i);
				if (usrCell == null || tmpCell == null) {
					return "导入文件的指定行列值为空";
				}
				String usrValue = usrCell.getStringCellValue();
				String tmpValue = tmpCell.getStringCellValue();
				if (!usrValue.equals(tmpValue)) {
					return "导入文件和模板文件的指定行列值不匹配,模板指定行值为:" + tmpValue + ",导入文件指定行值为:" + usrValue;
				}
			}
			// 是否允许单元格为空
			if (isNotNull) {
				int usrRowNum = usrSheet.getLastRowNum();
				for (int i = 0; i < usrRowNum; i++) {
					usrRow = usrSheet.getRow(i);
					int cellNum = 0;
					for (int j = 0; j < usrRow.getLastCellNum(); j++) {
						Cell usrCell = usrRow.getCell(cellNum);
						if (usrCell == null) {
							return "导入文件的单元格值为空！";
						} else if (StringUtils.isBlank(usrCell.getStringCellValue())) {
							return "导入文件的单元格值为空！";
						}
						cellNum++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (usrbook != null && tmpbook != null) {
				try {
					usrbook.close();
					tmpbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return result;
	}

	/**
	 * 校验导入excel文件的数据
	 * 
	 * @param userFile
	 *            导入文件
	 * @param templateFile
	 *            模板文件
	 * @param isNotNull
	 *            是否允许为空
	 * 
	 */
	public static String matchExcelData(InputStream userFile, String usrExt, InputStream templateFile,
			boolean isNotNull) throws IOException {
		return matchExcelData(userFile, usrExt, templateFile, 0, 0, 0, isNotNull);
	};

	/**
	 * 校验导入excel文件的数据
	 * 
	 * @param userFile
	 *            导入文件
	 * @param templateFile
	 *            模板文件
	 * 
	 */
	public static String matchExcelData(InputStream userFile, String usrExt, InputStream templateFile)
			throws IOException {
		return matchExcelData(userFile, usrExt, templateFile, false);
	};
}