package org.jflow.framework.designer.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jflow.framework.common.model.BaseModel;

import BP.Tools.StringHelper;

public class UIEnModel extends BaseModel {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	public HttpServletRequest request;

	public HttpServletResponse response;

	public String basePath;

	public UIEnModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		this.request = request;
		this.response = response;
	}

	public void init() {
		String enName = request.getParameter("EnName");
		if (StringHelper.isNullOrEmpty(enName))
			enName = request.getParameter("EnsName");

		if (enName.contains(".") == false) {
			try {
				response.sendRedirect("SysMapEn.jsp?EnsName=" + enName + "&PK=" + request.getParameter("PK"));
			} catch (IOException e) {
				logger.info("ERROR", e);
			}
			return;
		}
	}
}
