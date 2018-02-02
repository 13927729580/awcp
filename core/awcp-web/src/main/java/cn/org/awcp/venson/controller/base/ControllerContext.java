package cn.org.awcp.venson.controller.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.org.awcp.core.utils.ContextContentUtils;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;

public final class ControllerContext {

	private static final ThreadLocal<DynamicPageVO> page_threadLocal = new ThreadLocal<>();
	private static final ThreadLocal<DocumentVO> doc_threadLocal = new ThreadLocal<>();
	private static final ThreadLocal<ReturnResult> result_threadLocal = new ThreadLocal<>();


	public static HttpServletRequest getRequest() {
		return ContextContentUtils.getRequest();
	}


	public static HttpSession getSession() {
		return getRequest().getSession();
	}


	public static HttpServletResponse getResponse() {
		return ContextContentUtils.getResponse();
	}


	public static void setDoc(DocumentVO docVO) {
		doc_threadLocal.set(docVO);
	}

	public static DocumentVO getDoc() {
		return doc_threadLocal.get();
	}

	public static void setPage(DynamicPageVO pageVO) {
		page_threadLocal.set(pageVO);
	}

	public static DynamicPageVO getPage() {
		return page_threadLocal.get();
	}

	public static void setResult(ReturnResult result) {
		result_threadLocal.set(result);
	}

	public static ReturnResult getResult() {
		return result_threadLocal.get();
	}

}