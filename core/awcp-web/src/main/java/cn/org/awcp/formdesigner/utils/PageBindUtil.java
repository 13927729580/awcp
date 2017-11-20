package cn.org.awcp.formdesigner.utils;

import java.text.MessageFormat;

import org.springframework.jdbc.core.JdbcTemplate;
import cn.org.awcp.core.utils.Springfactory;

public class PageBindUtil {
	// 注入SpringJdbc
	private JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");

	private final static PageBindUtil util = new PageBindUtil();

	public static PageBindUtil getInstance() {
		return util;
	}

	private PageBindUtil() {
	}

	/**
	 * 根据手机列表ID返回手机表单ID
	 * 
	 * @param mListPageId
	 * @return
	 */
	public String getMPageIdByMListPageId(String mListPageId) {
		String ls_Sql = "select PAGEID_APP from p_un_page_binding where PAGEID_APP_LIST={0}";
		return jdbcTemplate.queryForObject(MessageFormat.format(ls_Sql, mListPageId), String.class);
	}

	/**
	 * 根据PC列表ID返回PC表单ID
	 * 
	 * @param pcListPageId
	 * @return
	 */
	public String getPCPageIdByPCListPageId(String pcListPageId) {
		String ls_Sql = "select PAGEID_PC from p_un_page_binding where PAGEID_PC_LIST={0}";
		return jdbcTemplate.queryForObject(MessageFormat.format(ls_Sql, pcListPageId), String.class);
	}

	/**
	 * 根据任意PageID返回手机表单ID
	 * 
	 * @param defaultId
	 * @return
	 */
	public String getMPageIDByDefaultId(String defaultId) {
		String ls_Sql = "select PAGEID_APP from p_un_page_binding where PAGEID_PC={0} or PAGEID_PC_LIST={1} or PAGEID_APP={2} or PAGEID_APP_LIST={3}";
		return jdbcTemplate.queryForObject(MessageFormat.format(ls_Sql, defaultId, defaultId, defaultId, defaultId),
				String.class);
	}

	/**
	 * 根据任意PageId返回PC表单ID
	 * 
	 * @param defaultId
	 * @return
	 */
	public String getPCPageIDByDefaultId(String defaultId) {
		String ls_Sql = "select PAGEID_PC from p_un_page_binding where PAGEID_PC={0} or PAGEID_PC_LIST={1} or PAGEID_APP={2} or PAGEID_APP_LIST={3}";
		return jdbcTemplate.queryForObject(MessageFormat.format(ls_Sql, defaultId, defaultId, defaultId, defaultId),
				String.class);
	}
}
