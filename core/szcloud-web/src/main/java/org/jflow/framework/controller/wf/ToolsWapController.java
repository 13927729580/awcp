package org.jflow.framework.controller.wf;

import java.io.File;
import java.net.BindException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import BP.Port.Emp;
import BP.Port.WebUser;
import BP.Sys.Glo;
import BP.WF.Template.Flow;
import BP.WF.Template.Flows;

@Controller
@RequestMapping("/WF")
public class ToolsWapController extends HttpServlet {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(ToolsWapController.class);

	@RequestMapping(value = "/ToolsWap", method = RequestMethod.POST)
	public ModelAndView btn_Profile_Click(String tel, String mail, int way, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(WebUser.getNo());
		emp.setTel(tel);
		emp.setEmail(mail);
		emp.setHisAlertWay(BP.WF.Port.AlertWay.forValue(way));
		try {
			emp.Update();
		} catch (Exception e) {
			logger.info("ERROR", e);
		}
		mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=Profile");
		return mv;
	}

	@RequestMapping(value = "/btn_Click", method = RequestMethod.POST)
	public ModelAndView btn_Click(String p1, String p2, String p3, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		Emp emp = new Emp(WebUser.getNo());
		if (emp.getPass().equals(p1)) {
			emp.setPass(p2);
			emp.Update();
			logger.debug("密码修改成功，请牢记新密码。");
		} else {
			logger.debug("老密码错误，不允许您修改它。");
		}
		mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=Pass");
		return mv;
	}

	@RequestMapping(value = "/btnSaveIt_Click", method = RequestMethod.POST)
	public ModelAndView btnSaveIt_Click(String FK_Emp, String TB_DT, int DDL_AuthorWay, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(WebUser.getNo());
		emp.setAuthorDate(BP.DA.DataType.getCurrentData());
		emp.setAuthor(FK_Emp);
		emp.setAuthorToDate(TB_DT);
		emp.setAuthorWay(DDL_AuthorWay);
		if (emp.getAuthorWay() == 2 && emp.getAuthorFlows().length() < 3) {
			logger.debug("您指定授权方式是按指定的流程范围授权，但是您没有指定流程的授权范围.");
			mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=AutoDtl");
			return mv;
		}
		emp.Update();
		// BP.Sys.UserLog.AddLog("Auth", WebUser.No, "全部授权");
		Glo.WriteUserLog("Auth", WebUser.getNo(), "全部授权");
		mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=Per");
		return mv;

	}

	@RequestMapping(value = "/btnSaveAthFlows_Click", method = RequestMethod.POST)
	public ModelAndView btnSaveAthFlows_Click(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		Flows fls = new Flows();
		fls.RetrieveAll();
		String strs = "";
		for (Flow fl : Flows.convertFlows(fls)) {
			String check = request.getParameter("CB_" + fl.getNo());
			if (check == null)
				continue;
			if (!check.equals("on"))
				continue;
			strs += "," + fl.getNo();
		}
		BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(WebUser.getNo());
		emp.setAuthorFlows(strs);
		emp.Update();

		Glo.WriteUserLog("Auth", WebUser.getNo(), "授权:" + strs);
		mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=AthFlows");
		return mv;

	}

	@RequestMapping(value = "/btn_AdminSet_Click", method = RequestMethod.POST)
	public ModelAndView btn_AdminSet_Click(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=AdminSet");
		return mv;
	}

	@RequestMapping(value = "/btn_Siganture_Click", method = RequestMethod.POST)
	public ModelAndView btn_Siganture_Click(HttpServletRequest request, HttpServletResponse response,
			BindException errors) throws Exception {
		ModelAndView mv = new ModelAndView();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

		String name = multipartRequest.getParameter("name");
		logger.debug("name: " + name);
		// 获得文件名：
		String realFileName = file.getOriginalFilename();
		logger.debug("获得文件名：" + realFileName);
		// 获取路径
		String ctxPath = request.getSession().getServletContext().getRealPath("/") + File.separator + "DataUser"
				+ File.separator + "Siganture" + File.separator;
		// 创建文件
		File dirPath = new File(ctxPath);
		if (!dirPath.exists()) {
			dirPath.mkdir();
		}
		// File uploadFile = new File(ctxPath + WebUser.getNo()+".jpg");
		File uploadFile = new File(ctxPath + realFileName);
		logger.debug("上传路径" + ctxPath + realFileName);
		FileCopyUtils.copy(file.getBytes(), uploadFile);
		request.setAttribute("files", loadFiles(request));
		mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=Per");
		return mv;
	}

	public List<String> loadFiles(HttpServletRequest request) {
		List<String> files = new ArrayList<String>();
		String ctxPath = request.getSession().getServletContext().getRealPath("/") + File.separator + "DataUser"
				+ File.separator + "Siganture" + File.separator;
		File file = new File(ctxPath);
		if (file.exists()) {
			File[] fs = file.listFiles();
			String fname = null;
			for (File f : fs) {
				fname = f.getName();
				if (f.isFile()) {
					files.add(fname);
				}
			}
		}
		return files;
	}

}
