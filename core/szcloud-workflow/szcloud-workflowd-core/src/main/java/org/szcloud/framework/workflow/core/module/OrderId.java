package org.szcloud.framework.workflow.core.module;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderId {

	/**
	 * 生成流程单号【单据的编号规则为：RW+年月+序号（三位数），例：RW-201408-001】
	 * 
	 * @param num
	 *            当前数据库最大单号 如下sql查询出【当前年月】下最大的单号 SELECT MAX(o.order) FROM `t` o
	 *            WHERE o.`order` LIKE 'RW-201402-%' String[] temp =
	 *            "RW-201402-001".split("-"); getOddNumber(temp[temp.length])
	 * @return
	 */
	public static String getOddNumber(String num) {
		// 前缀
		String prefix = "RW";

		// 分隔符
		String Delimiter = "-";

		// 默认单号3位数(不足3位补零)
		int defaultNum = 4;

		// 最终返回的单号
		StringBuffer sb = new StringBuffer();

		// 添加前缀
		sb.append(prefix).append(Delimiter);

		// 添加时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		sb.append(sdf.format(new Date())).append(Delimiter);

		// 计算下一单号数字
		Integer nums = Integer.valueOf(num) + 1;
		String temp = nums + "";
		for (int i = temp.length(); i < defaultNum; i++) {
			sb.append("0");
		}
		sb.append(nums);
		return sb.toString();
	}

}
