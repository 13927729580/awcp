package cn.org.awcp.venson.mybatis;

import org.apache.ibatis.mapping.MappedStatement;

import com.github.miemiedev.mybatis.paginator.dialect.MySQLDialect;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import cn.org.awcp.venson.util.MySqlSmartCountUtil;

/**
 * 根据SQL语句获取优化后的count语句
 * 
 * @author venson
 * @version 2017/11/13
 */
public class MyDialect extends MySQLDialect {

	public MyDialect(MappedStatement mappedStatement, Object parameterObject, PageBounds pageBounds) {
		super(mappedStatement, parameterObject, pageBounds);
	}

	@Override
	protected String getCountString(String sql) {
		return MySqlSmartCountUtil.getCountSql(sql, false);
	}

}
