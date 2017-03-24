package org.szcloud.framework.venson.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

public class H5ToXHtml {

	private H5ToXHtml() {
	}

	public static String parseXhtml(String f_in) {

		ByteArrayInputStream stream = new ByteArrayInputStream(f_in.getBytes());

		ByteArrayOutputStream tidyOutStream = new ByteArrayOutputStream();
		// 实例化Tidy对象
		Tidy tidy = new Tidy();
		// 设置输入
		tidy.setQuiet(true);
		tidy.setCharEncoding(Configuration.UTF8);
		// 设置输出
		// 不显示警告信息
		tidy.setShowWarnings(false);
		// 缩进适当的标签内容。
		tidy.setIndentContent(true);
		// 内容缩进
		tidy.setSmartIndent(true);
		tidy.setIndentAttributes(false);
		// 只输出body内部的内容
		// 多长换行
		tidy.setWraplen(1024);
		// 输出为xhtml
		tidy.setXHTML(true);
		// 去掉没用的标签
		tidy.setMakeClean(true);
		// 清洗word2000的内容
		tidy.setWord2000(true);
		// 设置错误输出信息
		tidy.setErrout(new PrintWriter(System.out));
		tidy.parse(stream, tidyOutStream);
		return tidyOutStream.toString();
	}
}
