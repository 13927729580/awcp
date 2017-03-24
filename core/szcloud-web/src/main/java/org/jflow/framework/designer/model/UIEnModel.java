package org.jflow.framework.designer.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.common.model.BaseModel;

import BP.Tools.StringHelper;

public class UIEnModel extends BaseModel{

	public HttpServletRequest request;
	
	public HttpServletResponse response;
	
	public String basePath;
	
	public UIEnModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		this.request=request;
		this.response=response;
	}


	public void init()
	{
		String enName = request.getParameter("EnName");
		if (StringHelper.isNullOrEmpty(enName))
			enName = request.getParameter("EnsName");

		if (enName.contains(".") == false)
		{
			try {
				response.sendRedirect("SysMapEn.jsp?EnsName=" + enName + "&PK=" + request.getParameter("PK"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
	}
}
