package org.szcloud.framework.workflow.core.module;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.szcloud.framework.workflow.core.base.CSqlHandle;

public class TPropertyBag {
	public byte[] Contents;
	public String Content;

	/**
	 * 取某一个表格的列名称，使用“, ”分隔
	 *
	 * @param as_TableName
	 * @return
	 * @throws SQLException
	 */
	public String GetTableFields(String as_TableName) throws SQLException {
		List<Map<String, Object>> rs = CSqlHandle.getJdbcTemplate().queryForList("select * from " + as_TableName);
		// ResultSetMetaData rsmd = rs.getMetaData();
		// int size = rsmd.getColumnCount();
		StringBuffer sb = new StringBuffer();
		// for (int i = 0; i < size; i++) {
		// sb.append(rsmd.getColumnName(i + 1));
		// sb.append(",");
		// }
		// sb.deleteCharAt(sb.length() - 1);

		Iterator iterator = rs.get(0).entrySet().iterator();
		while (iterator.hasNext()) {// 只遍历一次,速度快
			Entry e = (Entry) iterator.next();
			sb.append(e.getKey());
			if (iterator.hasNext())
				sb.append(";");
		}
		return sb.toString();
	}

	public void WriteContent(String str, Object str1) {
	}

	public String ReadContent(String str) {
		return null;
	}

	public String ReadProperty(String str) {
		return null;
	}

	public void WriteProperty(String string, Object obj) {
		// TODO Auto-generated method stub

	}
}
