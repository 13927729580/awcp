package cn.org.awcp.wechat.domain.message;

import java.util.List;

/**
 * 
 * @author yqtao
 *
 */
public class NewsMessage extends BaseMessage{
    private int articleCount;  		// 图文消息个数，限制为10条以内 
    private List<Article> articles;	// 多条图文消息信息，默认第1条为大图 
	
    public int getArticleCount() {
		return articleCount;
	}
    
	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}
	
	public List<Article> getArticles() {
		return articles;
	}
	
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
  
}