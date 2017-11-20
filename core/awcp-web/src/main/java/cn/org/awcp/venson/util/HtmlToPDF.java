package cn.org.awcp.venson.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class HtmlToPDF {
	/**
	 * 日志对象
	 */
	protected static final Log logger = LogFactory.getLog(HtmlToPDF.class);
	private static final HtmlToPDF instance = new HtmlToPDF();

	private HtmlToPDF() {
	}

	public static HtmlToPDF get() {
		return instance;
	}

	/**
	 * 使用Itex将html转pdf
	 * 
	 * @param html
	 *            html路径
	 * 
	 * @param pdf
	 *            pdf输出流
	 */
	public void convert(InputStream html, OutputStream pdf) throws IOException, DocumentException {
		Document document = new Document(PageSize.LETTER);
		PdfWriter pdfWriter = PdfWriter.getInstance(document, pdf);
		document.open();
		document.addAuthor("venson");
		document.addCreator("venson");
		document.addSubject("venson");
		document.addCreationDate();
		document.addTitle("XHTML to PDF");

		XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
		worker.parseXHtml(pdfWriter, document, html, XMLWorkerHelper.class.getResourceAsStream("/default.css"), null,
				get().new AsianFontProvider());
		document.close();
	}

	class AsianFontProvider extends XMLWorkerFontProvider {

		public Font getFont(final String fontname, final String encoding, final boolean embedded, final float size,
				final int style, final BaseColor color) {
			BaseFont bf = null;
			try {
				bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			} catch (Exception e) {
				logger.info("ERROR", e);
			}
			Font font = new Font(bf, size, style, color);
			font.setColor(color);
			return font;
		}
	}
}
