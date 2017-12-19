package cn.org.awcp.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperation;
import net.sf.jsqlparser.statement.select.SetOperationList;

public class MySqlSmartCountUtil {

	// countSql缓存
	private static HashMap<String, String> countSqlCache = new HashMap<String, String>();
	private static HashMap<String, String> queryCacheableCountSqlCache = new HashMap<String, String>();

	private static final List<SelectItem> countItem = new ArrayList<SelectItem>();
	private static final List<SelectItem> sqlCachedCountItem = new ArrayList<SelectItem>();
	static {
		countItem.add(new SelectExpressionItem(new Column("count(*) as totalX")));
		sqlCachedCountItem.add(new SelectExpressionItem(new Column("sql_cache count(*) as totalX")));
	}

	private static void cacheSmartCountSql(String srcSql, String countSql, boolean queryCacheable) {
		if (queryCacheable)
			queryCacheableCountSqlCache.put(srcSql, countSql);
		else
			countSqlCache.put(srcSql, countSql);
	}

	private static List<SelectItem> getCountItem(boolean queryCacheable) {
		return queryCacheable ? sqlCachedCountItem : countItem;
	}

	private static void smartCountPlainSelect(PlainSelect plainSelect, boolean queryCacheable)
			throws JSQLParserException {

		// 去除orderby
		plainSelect.setOrderByElements(null);
		// 判断是否包含group by
		if (plainSelect.getGroupByColumnReferences() == null || plainSelect.getGroupByColumnReferences().isEmpty()) {
			plainSelect.setSelectItems(getCountItem(queryCacheable));
		} else {
			throw new JSQLParserException("不支持智能count的sql格式: GROUP BY ");
		}
	}

	private static final Log logger = LogFactory.getLog(MySqlSmartCountUtil.class);

	public static String getCountSql(String sql, boolean isCache) {
		try {
			return getSmartCountSql(sql, isCache);
		} catch (Exception e) {
			logger.debug("ERROR", e);
		}
		return "select count(1) from (" + sql + ") tmp_count";
	}

	public static String getSmartCountSql(String srcSql, boolean queryCacheable) throws JSQLParserException {

		// 直接从缓存中取
		if (!queryCacheable && countSqlCache.containsKey(srcSql))
			return countSqlCache.get(srcSql);
		if (queryCacheable && queryCacheableCountSqlCache.containsKey(srcSql))
			return queryCacheableCountSqlCache.get(srcSql);

		Statement stmt = CCJSqlParserUtil.parse(srcSql);
		Select select = (Select) stmt;
		SelectBody selectBody = select.getSelectBody();

		if (selectBody instanceof PlainSelect) {
			PlainSelect plainSelect = ((PlainSelect) selectBody);
			smartCountPlainSelect(plainSelect, queryCacheable);

		} else if (selectBody instanceof SetOperationList) {
			SetOperationList setOperationList = (SetOperationList) selectBody;
			boolean isUnion = false;
			for (SetOperation o : setOperationList.getOperations()) {
				isUnion = (o.toString().contains("UNION"));
				if (!isUnion)
					break;
			}
			// union all 语句的智能count
			if (isUnion) {
				for (PlainSelect ps : setOperationList.getPlainSelects()) {
					smartCountPlainSelect(ps, false);// TODO 强制不允许缓存
				}
				String resultSql = "select sum(totalX) from (" + select.toString() + ") as t ";
				cacheSmartCountSql(srcSql, resultSql, false);// TODO 强制不允许缓存
				return resultSql;
			} else {
				throw new JSQLParserException("不支持智能count的sql格式");
			}
		} else {
			throw new JSQLParserException("不支持智能count的sql格式");
		}

		cacheSmartCountSql(srcSql, select.toString(), queryCacheable);

		return select.toString();
	}

}