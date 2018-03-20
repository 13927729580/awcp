package cn.org.awcp.base.tag;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 列表页面分页标签(英文)
 */
public class EnglishPageNavTag extends TagSupport {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;

	private String dpName;
	private String css;

	/**
	 * @param dpName
	 */
	public void setDpName(String dpName) {
		this.dpName = dpName;
	}

	/**
	 * @return
	 */
	public String getCss() {
		return css;
	}

	/**
	 * @param css
	 */
	public void setCss(String css) {
		this.css = css;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	@Override
    public int doEndTag() throws JspException {
		PageList<?> bean = (PageList<?>) pageContext.getRequest().getAttribute(dpName);
		// 当前第几页
		int currentPage = 0;
		// 共多少页
		int pageCount = 0;
		// 每页多少条记录
		int pageSize = 0;
		// 共多少条记录
		int totalCount = 0;

		if (bean != null) {
			currentPage = bean.getPaginator().getPage();
			pageCount = bean.getPaginator().getTotalPages();
			pageSize = bean.getPaginator().getLimit();
			totalCount = bean.getPaginator().getTotalCount();
			if (totalCount == 0) {
				currentPage = 0;
			}
		}
		StringBuilder html = new StringBuilder();
		if (this.css != null && "line-pagers".equals(this.css)) {
			html.append("<div class=\"m_p_bottom\"><span><span><select class='form-control' data-pageSize='" + pageSize
					+ "'><option value='10'>10</option><option value='15'>15</option><option value='20'>20</option><option value='25'>25</option><option value='50'>50</option></select></span>&nbsp;&nbsp;条记录/页</span><span>");
			if (currentPage > 1) {
				html.append("<a href=\"javascript:;\" onclick=\"page(1," + pageSize
						+ ");return false;\" title=\"First Page\"><i class=\"icon-step-backward\"></i>&nbsp;First Page</a>"
						+ "<a href=\"javascript:;\" onclick=\"page(" + (currentPage - 1) + "," + pageSize
						+ ");return false;\" title=\"Previous\"><i class=\"icon-double-angle-left\"></i>&nbsp;Previous</a>");

			} else {
				html.append(
						"<a href=\"javascript:;\" class=\"disabled\" title=\"First Page\"><i class=\"icon-step-backward\"></i>&nbsp;First Page</a>"
								+ "<a href=\"javascript:;\" class=\"disabled\" title=\"Previous\"><i class=\"icon-double-angle-left\"></i>&nbsp;Previous</a>");
			}
			if (currentPage < pageCount) {
				html.append("<a href=\"javascript:;\" onclick=\"page(" + (currentPage + 1) + "," + pageSize
						+ ");return false;\" title=\"Next\">Next&nbsp;<i class=\"icon-double-angle-right\"></i></a>"
						+ "<a href=\"javascript:;\" onclick=\"page(" + pageCount + "," + pageSize
						+ ");return false;\" title=\"Last Page\">Last Page&nbsp;<i class=\"icon-step-forward\"></i></a>");
			} else {
				html.append(
						"<a href=\"javascript:;\" class=\"disabled\" title=\"Next\">Next&nbsp;<i class=\"icon-double-angle-right\"></i></a>"
								+ "<a href=\"javascript:;\" class=\"disabled\" title=\"Last Page\">Last Page&nbsp;<i class=\"icon-step-forward\"></i></a>");
			}
			html.append("</span><span>Current <label class=\"text-danger\">" + currentPage + "</label> Page,</span>"
					+ "<span>Total<label class=\"text-danger\" id=\"totalpage\">" + pageCount + "</label> Pages,</span>"
					+ "<span>Total<label class=\"text-danger\">" + totalCount + "</label> Records</span>"
					+ "<span><label></label><input class=\"form-control\" type=\"text\"><label>Page</label>"
					+ "<a href=\"javascript:;\" onclick=\"page(" + pageSize
					+ ")\" class=\"btn btn-sm btn-primary\">Go</a></span></div>");
		} else {
			html.append("<div class=\"m_p_top\"><ul class=\"pager\">");
			if (currentPage > 1) {
				html.append("<li class=\"previous\"><a href='javascript:page(1,").append(pageSize)
						.append(")'>First Page</a></li>");
				html = html.append("<li class=\"previous\"><a href='javascript:page(").append(currentPage - 1)
						.append(",").append(pageSize).append(")'>«Previous</a></li>");
			} else {
				html.append("<li class=\"previous disabled\"><a href='javascript:return false;'>First Page</a></li>");
				html = html.append(
						"<li class=\"previous disabled\"><a href='javascript:return false;'>«Previous</a></li>");
			}
			if (currentPage < pageCount) {
				html.append("<li class=\"next\"><a href='javascript:page(").append(currentPage + 1).append(",")
						.append(pageSize).append(")'>Next »</a></li>");
				html.append("<li class=\"next\"><a href='javascript:page(").append(pageCount).append(",")
						.append(pageSize).append(")'>Last Page</a></li>");

			} else {
				html.append("<li class=\"next disabled\"><a href='javascript:return false;'>Next »</a></li>");
				html.append("<li class=\"next disabled\"><a href='javascript:return false;'>Last Page</a></li>");
			}

			html.append("</ul></div><div class=\"m_p_bottom\">");
			html.append("<span><select id='pageSizeSelect' class='form-control' data-pageSize='" + pageSize
					+ "'><option value='10'>10</option><option value='15'>15</option><option value='20'>20</option><option value='25'>25</option><option value='50'>50</option></select></span>"
					+ "<span>Current <label class=\"text-danger\">" + currentPage + "</label> Page,</span>"
					+ "<span>Total<label class=\"text-danger\" id=\"totalpage\">" + pageCount + "</label> Pages,</span>"
					+ "<span>Total<label class=\"text-danger\">" + totalCount + "</label> Records</span>"
					+ "<span><label></label><input class=\"form-control\" type=\"text\"><label>Page</label>"
					+ "<a href=\"javascript:;\" onclick=\"page(" + pageSize
					+ ")\" class=\"btn btn-sm btn-primary\">Go</a></span></div>");
		}

		try {
			pageContext.getOut().print(html.toString());
		} catch (Exception ex) {
			logger.info("ERROR", ex);
		}

		return super.doEndTag();
	}
}
