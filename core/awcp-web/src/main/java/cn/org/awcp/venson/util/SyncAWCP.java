package cn.org.awcp.venson.util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * awcp同步类
 * 
 * @author venson
 * @version 2017-11-14
 */
public class SyncAWCP {

	static JdbcTemplate jdbcTemplate;
	static {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setDbType("dbType");
		dataSource.setUrl(
				"jdbc:mysql://5704b9845a977.gz.cdb.myqcloud.com:9492/awcp_huaxia?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull&amp;transformedBitIsBoolean=true&amp;useOldAliasMetadataBehavior=true");
		dataSource.setUsername("cdb_outerroot");
		dataSource.setPassword("Dcba4321@#");
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static String export_path = "d://export.sql";

	public static void main(String[] args) {
		String id = "2705b804-fbdc-452b-92e6-fe407aa3742a";

		StringBuffer content = new StringBuffer();
		// 1.同步数据源（建表和表数据）
		content.append(createDataSource(id));
		// 2.同步动态页面和组件
		content.append(createDynamicPageData(id));
		// 3.同步模板
		content.append(createTemplate(id));
		// // 4.同步元数据
		// content.append(createDynamicPageData(id));
		writeStringToFile(content.toString());

	}

	/**
	 * 根据表单页面ID找出元数据并同步元数据
	 * 
	 * @param pageId
	 * @return
	 */
	public static String createMetadata(String pageId) {
		return "";
	}

	/**
	 * 根据表单页面ID找出模板并同步模板
	 * 
	 * @param pageId
	 * @return
	 */
	private static String createTemplate(String pageId) {
		StringBuffer buffer = new StringBuffer();
		Object template_id = getPage(pageId).get("template_id");
		Map<String, Object> data = jdbcTemplate.queryForMap("select * from p_fm_template where id=?", template_id);
		buffer.append(getInsertSQL(data, "p_fm_template"));
		return buffer.toString();
	}

	private static Map<String, Map<String, Object>> cache = new HashMap<>();

	public static Map<String, Object> getPage(String pageId) {
		if (cache.containsKey(pageId)) {
			return cache.get(pageId);
		}
		return jdbcTemplate.queryForMap("select * from p_fm_dynamicpage where id=?", pageId);
	}

	/**
	 * 根据表单页面ID找出数据源表并创建表
	 * 
	 * @param pageId
	 * @return
	 */
	private static String createDataSource(String pageId) {
		String model = String.valueOf(getPage(pageId).get("model_xml"));
		StringBuffer buffer = new StringBuffer();
		List<String> list = getMetadataTableName(model);
		for (String table : list) {
			buffer.append(createTable(table, true));
		}
		return buffer.toString();
	}

	private static List<String> getMetadataTableName(String model) {
		JSONArray arr = JSON.parseArray(StringEscapeUtils.unescapeHtml4(model));
		int size = arr.size();
		String sql = "select tableName from fw_mm_metamodel where modelCode=?";
		List<String> list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			JSONObject json = arr.getJSONObject(i);
			String modelCode = json.getString("modelCode");
			list.add(jdbcTemplate.queryForObject(sql, String.class, modelCode));
		}
		return list;
	}

	/**
	 * 根据表单页面ID同步动态页面和组件
	 * 
	 * @param pageId
	 * @return
	 */
	private static String createDynamicPageData(String pageId) {
		StringBuffer buffer = new StringBuffer();
		Map<String, Object> data = jdbcTemplate.queryForMap("select * from  p_fm_dynamicpage where id=?", pageId);
		buffer.append(getInsertSQL(data, "p_fm_dynamicpage"));
		List<Map<String, Object>> stores = jdbcTemplate.queryForList("select * from p_fm_store where DYNAMICPAGE_ID=?",
				pageId);
		for (Map<String, Object> store : stores) {
			buffer.append(getInsertSQL(store, "p_fm_store"));
		}
		return buffer.toString();
	}

	/**
	 * 根据表名备份当前数据库表和数据
	 * 
	 * @param tableName
	 *            表名
	 * @param inCludeData
	 *            是否包含数据
	 * @return
	 */
	private static String createTable(String tableName, boolean inCludeData) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("SHOW FULL FIELDS FROM " + tableName);
		StringBuffer buffer = new StringBuffer();
		buffer.append("/*Delete on exists table `" + tableName + "` */\n");
		buffer.append("DROP TABLE IF EXISTS `" + tableName + "`;\n\n");
		buffer.append("/*Table structure for table `" + tableName + "` start */\n");
		buffer.append("CREATE TABLE `" + tableName + "`(\n");
		for (Map<String, Object> map : list) {
			Object Field = map.get("Field");
			Object Type = map.get("Type");
			Object key = map.get("Key");
			Object Null = map.get("Null");
			Object Default = map.get("Default");
			Object Extra = map.get("Extra");
			String Comment = map.get("Comment").toString();
			buffer.append("\t`" + Field + "` ");
			buffer.append(Type + " ");
			if ("PRI".equals(key)) {
				buffer.append(" PRIMARY KEY ");
			} else if ("UNI".equals(key)) {
				buffer.append(" UNIQUE KEY ");

			}
			if ("NO".equals(Null)) {
				buffer.append(" NOT NULL ");
			}
			if (Default != null) {
				buffer.append(" default " + Default + " ");
			}
			if ("auto_increment".equals(Extra)) {
				buffer.append(" AUTO_INCREMENT ");
			}
			if (StringUtils.isNotBlank(Comment)) {
				buffer.append(" COMMENT '" + Comment + "'");
			}
			buffer.append(",\n");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;\n");
		buffer.append("/*Table structure for table `" + tableName + "` end */\n\n");
		if (inCludeData) {
			buffer.append("/*Table data for table `" + tableName + "` start */\n");
			List<Map<String, Object>> data = jdbcTemplate.queryForList("select * from " + tableName);
			for (Map<String, Object> map : data) {
				buffer.append(getInsertSQL(map, tableName));
			}
			buffer.append("/*Table data for table `" + tableName + "` end */\n");
		}
		return buffer.toString();
	}

	private static void writeStringToFile(String content) {
		try {
			FileUtils.writeStringToFile(new File(export_path), content, "UTF-8", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自动生成插入语句
	 * 
	 * @param map
	 * @param tableName
	 * @return
	 */
	public static String getInsertSQL(Map<String, Object> map, String tableName) {
		StringBuffer buffer = new StringBuffer();
		Object id = map.get("id");
		if (id == null) {
			id = map.get("ID");
		}
		buffer.append("/*DELETE table `" + tableName + "` where id= " + id + "  start */\n");
		buffer.append("DELETE FROM `" + tableName + "` WHERE ID='" + id + "';\n");
		buffer.append("/*backup table `" + tableName + "` where id= " + id + "  start */\n");
		buffer.append("INSERT INTO `" + tableName + "` ");
		buffer.append("VALUES (");
		Set<String> keys = map.keySet();
		for (String key : keys) {
			Object value = map.get(key);
			if (value == null) {
				buffer.append("null,");
			} else {
				if (value instanceof String) {

					buffer.append(handSQL(value.toString()) + ",");
				} else if (value instanceof Date) {

					buffer.append("'" + DateFormaterUtil.dateToString(DateFormaterUtil.FORMART4, (Date) value) + "',");
				} else {
					buffer.append("'" + value + "',");
				}
			}
		}
		buffer.delete(buffer.length() - 1, buffer.length());
		buffer.append(");\n");
		return buffer.toString();
	}

	private static String handSQL(String x) {
		int stringLength = x.length();
		StringBuffer buf = new StringBuffer((int) (stringLength * 1.1));
		CharsetEncoder charsetEncoder = Charset.forName("UTF-8").newEncoder();
		buf.append('\'');
		for (int i = 0; i < stringLength; ++i) {
			char c = x.charAt(i);

			switch (c) {
			case 0: /* Must be escaped for 'mysql' */
				buf.append('\\');
				buf.append('0');

				break;

			case '\n': /* Must be escaped for logs */
				buf.append('\\');
				buf.append('n');

				break;

			case '\r':
				buf.append('\\');
				buf.append('r');

				break;

			case '\\':
				buf.append('\\');
				buf.append('\\');

				break;

			case '\'':
				buf.append('\\');
				buf.append('\'');

				break;

			case '"': /* Better safe than sorry */

				buf.append('"');

				break;

			case '\032': /* This gives problems on Win32 */
				buf.append('\\');
				buf.append('Z');

				break;

			case '\u00a5':
			case '\u20a9':
				// escape characters interpreted as backslash by mysql
				CharBuffer cbuf = CharBuffer.allocate(1);
				ByteBuffer bbuf = ByteBuffer.allocate(1);
				cbuf.put(c);
				cbuf.position(0);
				charsetEncoder.encode(cbuf, bbuf, true);
				if (bbuf.get(0) == '\\') {
					buf.append('\\');
				}
				// fall through

			default:
				buf.append(c);
			}
		}

		buf.append('\'');

		return buf.toString();
	}

}
