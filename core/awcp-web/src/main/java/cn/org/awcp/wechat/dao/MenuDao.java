package cn.org.awcp.wechat.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 根据公众号id查询微信菜单
 * @author yqtao
 *
 */
@Repository
public class MenuDao {

	private static Logger logger = LoggerFactory.getLogger(MenuDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * @return
	 */
	public List<Map<String,Object>> getWeChatMenu(){
		String sql = "select name,keyValue,url,type,orderNum,parentId from wechat_menu order by orderNum asc";
		logger.info("查询WeChatMenu的sql为:"+sql);
		try{
			return jdbcTemplate.queryForList(sql);
		}catch(DataAccessException e){
			e.printStackTrace();
			return null;
		}
	}
		
}
