package cn.org.awcp.wechat.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.wechat.domain.message.Article;

/**
 * 获取后台设置的消息内容:自动回复内容,关键字对应的回复
 * @author yqtao
 *
 */
@Repository
public class MessageDao {

	private static Logger logger = LoggerFactory.getLogger(MessageDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 查询公众号关联的首次关注的回复类型和ID
	 * @return
	 */
	public Map<String,Object> getSubscribeReply(){
		String sql = "select ID,replyType from wechat_subscribe_reply";
		logger.info("查询首次关注sql:" + sql);
		try{
			return jdbcTemplate.queryForMap(sql);
		}catch(DataAccessException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取文本消息
	 * @param fkId	关联表主键
	 * @return
	 */
	public String getTextMessage(String fkId){
		String sql = "select content from wechat_message_text where fkID = '" + fkId + "'";
		logger.info("查询文本消息sql：" + sql);
		try{
			return (String) jdbcTemplate.queryForMap(sql).get("content");
		}catch(DataAccessException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取所有图文消息
	 * @param fkId
	 * @param isMore 
	 * @return
	 */
	public List<Article> getArticles(String fkId,String isMore){
		final List<Article> list = new ArrayList<Article>();
		final String basepath = ControllerHelper.getBasePath() + "common/file/showPicture.do?id=";
		
		String sql ;
		//单图文消息
		if("0".equals(isMore)){
			sql = "select title,description,picUrl,url from wechat_article where isMore='0' and fkID = '" + fkId + "'";
			logger.info("查询单条图文消息sql:"+sql);
			jdbcTemplate.query(sql, new RowCallbackHandler(){
				public void processRow(ResultSet rs) throws SQLException {
					String title = rs.getString("title");
					String description = rs.getString("description"); 
					String picUrl = basepath + rs.getString("picUrl");
					String url = rs.getString("url");
					Article article = new Article(title, description, picUrl, url);
					list.add(article);
				}
			});
			return list;
		}
		//多图文消息
		else{
			sql = "select title,description,picUrl,url from wechat_article where isMore='1' and fkID = '" + fkId + 
				  "' order by orderNum asc";
			logger.info("查询多条图文消息sql:"+sql);
			jdbcTemplate.query(sql, new RowCallbackHandler(){
				public void processRow(ResultSet rs) throws SQLException {
					String title = rs.getString("title");
					String description = rs.getString("description"); 
					String picUrl = basepath + rs.getString("picUrl");
					String url = rs.getString("url");
					Article article = new Article(title, description, picUrl, url);
					list.add(article);
				}
			});
			return list;
		}
	}
	
}
