package cn.org.awcp.venson.util;

import cn.org.awcp.venson.controller.base.ControllerHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

/**
 * word(03,07)文档，excel(03,07)文档，txt文本转html
 *
 * @author venson
 * @version 2016-11-11
 */
public class DocumentToHtml {

    private static final Log logger = LogFactory.getLog(DocumentToHtml.class);
    public static final String CHAR_FULL_SPACE = (char) 12288 + "";
    public static final String CHAR_SPACE = " ";
    public static final String HTML_SPACE = "&nbsp;";
    public static final String UNDERLINE = "_";
    public static final String UTF_8 = "UTF-8";

    private DocumentToHtml() {
    }

    private final static DocumentToHtml instance=new DocumentToHtml();

    public static DocumentToHtml getInstance() {
        return instance;
    }

    public void convertTxtToHtml(InputStream inputStream, String outPath, String outFileName) {
        try {
            String content = getStringFormTxt(inputStream);
            FileUtils.writeStringToFile(new File(outPath + outFileName), content, UTF_8);
        } catch (Exception e) {
            logger.info("ERROR", e);
        }
    }

    /**
     * @param inputStream
     * @param outPath
     * @param outFileName
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public void wordToHtml03(InputStream inputStream,final String outPath, final String outFileName)
            throws IOException, ParserConfigurationException, TransformerException {
        STUnderline.Enum.forInt(1);
        HWPFDocument wordDocument = new HWPFDocument(inputStream);
        // TODO此处将文档中的半角空格转为全角空格（为了保存文档中的空格和样式下划线）
        Range range = wordDocument.getRange();
        // 查找下划线
        for (int j = 0; j < range.numCharacterRuns(); j++) {
            CharacterRun that = range.getCharacterRun(j);
            if (that.getUnderlineCode() == 1) {
                that.replaceText(CHAR_SPACE, UNDERLINE);
            } else {
                that.replaceText(CHAR_SPACE, CHAR_FULL_SPACE);
            }
        }
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        wordToHtmlConverter.setPicturesManager((byte[] content, PictureType pictureType, String suggestedName, float widthInches,
                                                float heightInches) ->
                savePic(content, pictureType, outPath, outFileName, widthInches * 100, heightInches * 100)

        );
        wordToHtmlConverter.processDocument(wordDocument);

        Document htmlDocument = wordToHtmlConverter.getDocument();

        OutputStream outStream = new FileOutputStream(new File(outPath + outFileName));
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, UTF_8);
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        outStream.close();
    }

    /**
     * 保存并整理word文档中的图片
     *
     * @param content     图片
     * @param pictureType 图片类型
     * @param outPath     输出路径
     * @param outfileName 输出文件夹名
     */
    public String savePic(byte[] content, PictureType pictureType, String outPath, String outfileName, float width,
                          float height) {
        String outFloder = outfileName.substring(0, outfileName.lastIndexOf("."));
        File outFile = new File(outPath + outFloder);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        try {
            String fileName = outPath + outFloder + File.separator + java.util.UUID.randomUUID().toString() + "."
                    + pictureType.getExtension();
            scaleImg(content, width, height, fileName, pictureType.getExtension());
            return fileName;
        } catch (Exception e) {
            logger.info("ERROR", e);
        }
        return null;
    }

    private void scaleImg(byte[] in, float width, float height, String path, String ext) throws IOException {
        InputStream buffin = new ByteArrayInputStream(in);
        BufferedImage bImage = ImageIO.read(buffin);
        if (bImage != null) {
            Image img = bImage.getScaledInstance((int) width, (int) height, Image.SCALE_SMOOTH);
            BufferedImage bi = new BufferedImage((int) width, (int) height, BufferedImage.SCALE_SMOOTH);
            Graphics2D g = (Graphics2D) bi.getGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
            bi.flush();
            ImageIO.write(bi, ext, new File(path));
        }
    }

    public boolean doGenerateSysOut(InputStream inputStream, String outPath, String outFileName) {
        boolean flag = true;
        try {
            // 例：outFileName = aa.html,outFileNameNoable = aa
            String fileName = outFileName.split(".html")[0];
            String filePath = outPath + outFileName;
            XWPFDocument document = new XWPFDocument(inputStream);
            List<XWPFParagraph> ps = document.getParagraphs();
            for (XWPFParagraph p : ps) {
                List<XWPFRun> rs = p.getRuns();
                for (XWPFRun r : rs) {
                    String text = r.text();
                    text = text.replaceAll(CHAR_SPACE, CHAR_FULL_SPACE);
                    r.setText(text, 0);
                }
            }
            XHTMLOptions options = XHTMLOptions.create().indent(4);
            // Extract image 创建存储图片文件夹
            File imageFolder = new File(outPath + fileName);
            options.setExtractor(new FileImageExtractor(imageFolder));
            // URI resolver
            options.URIResolver(new FileURIResolver(imageFolder));
            File outFile = new File(filePath);
            outFile.getParentFile().mkdirs();
            OutputStream out = new FileOutputStream(outFile);
            XHTMLConverter.getInstance().convert(document, out, options);
        } catch (Exception e) {
            flag = false;
            logger.info("ERROR", e);
        }
        return flag;
    }

    /**
     * 读取html文件到字符串
     *
     * @param filename
     * @return
     * @throws Exception
     */
    public String readFile(String filename){
        StringBuffer buffer = new StringBuffer("");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
            buffer = new StringBuffer();
            while (br.ready()) {
                buffer.append((char) br.read());
            }
        } catch (Exception e) {
            logger.info("ERROR", e);
        } finally {
            IOUtils.closeQuietly(br);
        }
        return buffer.toString();
    }

    private String convertToChinese(String dataStr) {
        if (dataStr == null || dataStr.length() == 0) {
            return dataStr;
        }
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            // 进制
            int system = 10;
            if (start == 0) {
                int t = dataStr.indexOf("&#");
                if (start != t) {
                    start = t;
                }
                if (start > 0) {
                    buffer.append(dataStr.substring(0, start));
                }
                if (start == -1) {
                    return dataStr;
                }
            }
            end = dataStr.indexOf(";", start + 2);
            String charStr;
            if (end != -1) {
                charStr = dataStr.substring(start + 2, end);
                // 判断进制
                char s = charStr.charAt(0);
                if (s == 'x' || s == 'X') {
                    system = 16;
                    charStr = charStr.substring(1);
                }
                // 转换
                try {
                    char letter = (char) Integer.parseInt(charStr, system);
                    buffer.append(new Character(letter).toString());
                } catch (NumberFormatException e) {
                    logger.info("ERROR", e);
                }
            }

            // 处理当前unicode字符到下一个unicode字符之间的非unicode字符
            start = dataStr.indexOf("&#", end);
            if (start - end > 1) {
                buffer.append(dataStr.substring(end + 1, start));
            }
            // 处理最后面的非 unicode字符
            if (start == -1) {
                int length = dataStr.length();
                if (end + 1 != length) {
                    buffer.append(dataStr.substring(end + 1, length));
                }
            }
        }
        return buffer.toString();
    }

    public void writeToFile2(String path, String str) throws IOException {
        str = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" + str;
        File file = new File(path);
        FileUtils.writeStringToFile(file, str, UTF_8);
    }

    /**
     * replace image src
     *
     * @return String
     * @throws Exception
     */

    public void convertDocxToHtml(InputStream inputStream, String outPath, String outFileName)
            throws Exception {
        if (doGenerateSysOut(inputStream, outPath, outFileName)) {
            writeToFile2(outPath + outFileName, convertToChinese(readFile(outPath + outFileName)));
        }
    }

    /**
     * @return 读出的txt的内容
     */
    public String getStringFormTxt(InputStream inputStream) throws Exception {
        String code;
        /************* 测试数据 ***************/
        BufferedInputStream bin = new BufferedInputStream(inputStream);
        int p = (bin.read() << 8) + bin.read();
        switch (p) {
            case 0xefbb:
                code = UTF_8;
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "gbk";
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(bin, code));
        StringBuffer buff = new StringBuffer();
        buff.append("<!DOCTYPE html>\r\n");
        buff.append("<head>\r\n");
        buff.append("<meta charset=\"utf-8\" />\r\n");
        buff.append("</head>\r\n");
        buff.append("<body>\r\n");
        buff.append("<div>\r\n");
        String temp;
        while ((temp = br.readLine()) != null) {
            buff.append("<br>");
            buff.append(temp.replaceAll("\\s", "　").replaceAll("\\>", "&gt;").replaceAll("\\<", "&lt;"));
            buff.append("\r\n");
        }
        buff.append("\r\n</div>");
        buff.append("\r\n</body>");
        buff.append("\r\n</html>");
        br.close();
        return buff.toString();
    }

    /**
     * 将word文档转换为html文件;
     *
     * @param inFileName
     * @param outPath
     * @param outFileName
     * @throws Exception
     */
    public void toHtml(InputStream inputStream, String inFileName, String outPath, String outFileName)
            throws Exception {
        logger.debug("文件:" + inFileName + "转换开始....");
        if (!new File(outPath).exists()) {
            new File(outPath).mkdirs();
        }
        if (inFileName.endsWith(".docx") || inFileName.endsWith(".DOCX")) {
            // 07
            convertDocxToHtml(inputStream, outPath, outFileName);
            String filePath = outPath + outFileName;
            handleImgSrc(filePath);
        } else if (inFileName.endsWith(".doc") || inFileName.endsWith(".DOC")) {
            // 03
            wordToHtml03(inputStream, outPath, outFileName);
            String filePath = outPath + outFileName;
            handleImgSrc(filePath);
        } else if (inFileName.endsWith(".xls") || inFileName.endsWith(".XLS")) {
            String html = excelToHtml(inputStream, "xls");
            File file = new File(outPath + outFileName);
            FileUtils.writeStringToFile(file, html, UTF_8);
        } else if (inFileName.endsWith(".xlsx") || inFileName.endsWith(".XLSX")) {
            String html = excelToHtml(inputStream, "xlsx");
            File file = new File(outPath + outFileName);
            FileUtils.writeStringToFile(file, html, UTF_8);
        } else {
            convertTxtToHtml(inputStream, outPath, outFileName);
        }
        logger.debug("文件:" + outFileName + "转换结束....");
    }

    private void handleImgSrc(String filePath) throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.parse(new File(filePath), UTF_8);
        Element body = doc.body();
        // 替换图片路径
        replaceImgSrc(body);
        // 替换文档字符
        String html = doc.toString();
        html = html.replaceAll(CHAR_FULL_SPACE, HTML_SPACE);
        FileUtils.writeStringToFile(new File(filePath), html, UTF_8);
    }

    private void replaceImgSrc(Element body) {
        Elements links = body.getElementsByTag("img");
        if (links != null && links.size() > 0) {
            for (Element link : links) {
                String oldSrc = link.attr("src");
                String imgPath = oldSrc.substring(ControllerHelper.getUploadPath("/").length(), oldSrc.length());
                imgPath = ControllerHelper.getDeployDomain() + imgPath;
                link.attr("src", imgPath.replaceAll("\\\\", "/"));
            }
        }
    }

    public String excelToHtml(InputStream excelFile, String ext) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset=\"utf-8\" />");
        html.append("<style type=\"text/css\">table.gridtable {font-family: verdana,arial,sans-serif;"
                + "font-size:11px;color:#333333;border-width: 1px;border-color: #666666;border-collapse: collapse;"
                + "}table.gridtable th {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;"
                + "background-color: #dedede;}table.gridtable td {border-width: 1px;padding: 8px;border-style: solid;"
                + "border-color: #666666;background-color: #ffffff;}h3{text-align:center;}</style>");
        html.append("</head><body>");
        Workbook workbook = null;
        try {
            if ("xls".equals(ext)) {
                workbook = new HSSFWorkbook(excelFile);
            } else {
                workbook = new XSSFWorkbook(excelFile);
            }
            for (int k = 0; k < workbook.getNumberOfSheets(); k++) {
                // sheet 从0开始m
                Sheet sheet = workbook.getSheetAt(k);
                String tableName = StringUtils.isBlank(sheet.getSheetName()) ? "表格" + k + 1 : sheet.getSheetName();
                html.append("<div><h3>" + tableName + "</h3>");
                html.append("<table class='gridtable' id='table" + k + "'>");
                int rowNum = sheet.getLastRowNum() + 1;
                // 行循环开始
                for (int i = 0; i < rowNum; i++) {
                    html.append("<tr>");
                    // 行
                    Row row = sheet.getRow(i);
                    short cellNum;
                    try {
                        cellNum = row.getLastCellNum();
                    } catch (Exception e) {
                        cellNum = 0;
                    }
                    // 列循环开始
                    for (int j = 0; j < cellNum; j++) {
                        if (i == 0) {
                            html.append("<th>");
                        } else {
                            html.append("<td>");
                        }
                        Cell cell = row.getCell(j);
                        String cellValue = "";
                        if (null != cell) {
                            // 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_NUMERIC:
                                    cellValue = String.valueOf((int) cell.getNumericCellValue());
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    cellValue = cell.getStringCellValue();
                                    break;
                                case Cell.CELL_TYPE_FORMULA:
                                    cellValue = cell.getNumericCellValue() + "";
                                    break;
                                case Cell.CELL_TYPE_BLANK:
                                    cellValue = "";
                                    break;
                                case Cell.CELL_TYPE_BOOLEAN:
                                    cellValue = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case Cell.CELL_TYPE_ERROR:
                                    cellValue = String.valueOf(cell.getErrorCellValue());
                                    break;
                            }
                        }
                        html.append(cellValue);
                        if (i == 0) {
                            html.append("</th>");
                        } else {
                            html.append("</td>");
                        }

                    }
                    html.append("</tr>");

                }
                html.append("</table>");
                html.append("</div>");
            }

        } catch (IOException e) {
            logger.info("ERROR", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.info("ERROR", e);
                }
            }
        }
        html.append("</body></html>");
        return html.toString();
    }

}