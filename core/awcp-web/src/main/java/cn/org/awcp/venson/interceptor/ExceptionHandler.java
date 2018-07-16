package cn.org.awcp.venson.interceptor;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.exception.PlatformException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.session.UnknownSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * spring mvc 异常处理器
 * 
 * @author Venson
 *
 */
public class ExceptionHandler implements HandlerExceptionResolver {
	/**
	 * 日志对象
	 */
	private static Log logger = LogFactory.getLog(ExceptionHandler.class);
	@Autowired
	private SzcloudJdbcTemplate jdbcTemplate;

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj,
			Exception ex) {
		ReturnResult result = ReturnResult.get();
		// 将事务进行回滚
		jdbcTemplate.rollback();
		handler(ex, result);
		String requestType = request.getHeader("X-Requested-With");
		boolean returnJson=requestType != null && (requestType.equals("XMLHttpRequest")||requestType.equals("APP"));
		// 2.决定错误状态码
		response.setStatus(HttpServletResponse.SC_OK);
		try {
			if (returnJson) {
				ControllerHelper.renderJSON(null, result);
			} else {
				request.setAttribute("message", result.getMessage());
				request.setAttribute("error", result.getData());
				request.getRequestDispatcher("/error.jsp").forward(request,response);
			}
		} catch (Exception e) {
			logger.debug("ERROR",e);
		}
		return new ModelAndView();
	}

	public static void handler(Exception ex, ReturnResult result) {

		if (ex instanceof MaxUploadSizeExceededException) {
			result.setStatus(StatusCode.FAIL).setMessage("文件过大，请重新上传");
		} else if (ex instanceof UncategorizedSQLException) {
			result.setStatus(StatusCode.NO_ACCESS).setMessage("检测到非法字符，禁止访问");
		} else if (ex instanceof UnknownSessionException) {
			result.setStatus(StatusCode.NO_LOGIN).setMessage("登录失效，请重新登录");
		} else if (ex instanceof PlatformException) {
			logger.debug(ex.getMessage());
			result.setStatus(StatusCode.FAIL).setMessage(ex.getMessage());
		} else {
			logger.error("ERROR", ex);
			result.setStatus(StatusCode.FAIL).setMessage("服务器出错啦").setData(ex.getClass().getName());
		}
	}
}
