package cn.org.awcp.venson.controller.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;

import cn.org.awcp.core.utils.ContextContentUtils;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;

/**
 * 上下文工具类
 * @author venson
 * @version 20180309
 */
public final class ControllerContext {

	private static final ThreadLocal<DynamicPageVO> DYNAMIC_PAGE_VO_THREAD_LOCAL = new ThreadLocal<>();
	private static final ThreadLocal<DocumentVO> DOCUMENT_VO_THREAD_LOCAL = new ThreadLocal<>();
	private static final ThreadLocal<ReturnResult> RETURN_RESULT_THREAD_LOCAL = new ThreadLocal<>();

	public static HttpServletRequest getRequest() {
		return ContextContentUtils.getRequest();
	}

	public static Session getSession() {
		return SessionUtils.getCurrentSession();
	}

	public static HttpServletResponse getResponse() {
		return ContextContentUtils.getResponse();
	}

	public static void setDoc(DocumentVO docVO) {
		DOCUMENT_VO_THREAD_LOCAL.set(docVO);
	}

	public static DocumentVO getDoc() {
		return DOCUMENT_VO_THREAD_LOCAL.get();
	}

	public static void setPage(DynamicPageVO pageVO) {
		DYNAMIC_PAGE_VO_THREAD_LOCAL.set(pageVO);
	}

	public static DynamicPageVO getPage() {
		return DYNAMIC_PAGE_VO_THREAD_LOCAL.get();
	}

	public static void setResult(ReturnResult result) {
		RETURN_RESULT_THREAD_LOCAL.set(result);
	}

	public static ReturnResult getResult() {
		return RETURN_RESULT_THREAD_LOCAL.get();
	}

	public static void removeResult(){
		RETURN_RESULT_THREAD_LOCAL.remove();
	}
	public static void removePage(){
		DYNAMIC_PAGE_VO_THREAD_LOCAL.remove();
	}
	public static void removeDoc(){
		DOCUMENT_VO_THREAD_LOCAL.remove();
	}
}