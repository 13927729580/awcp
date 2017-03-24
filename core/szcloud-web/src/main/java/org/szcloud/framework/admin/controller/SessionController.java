package org.szcloud.framework.admin.controller;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.szcloud.framework.base.BaseController;

@Controller
@RequestMapping("/sessionutils")
public class SessionController extends BaseController {
	@Autowired
	private SessionDAO sessionDAO;
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

	@RequestMapping("/list")
	public String list(Model model, HttpServletResponse response, HttpServletRequest request) {
		Collection<Session> sessions = sessionDAO.getActiveSessions();
		model.addAttribute("sessions", sessions);
		model.addAttribute("sesessionCount", sessions.size());
		// Collection<Session> list = (Collection<Session>)
		// request.getAttribute("sessions");
		// 区分开发者、管理者和使用者
		for (Iterator<Session> it = sessions.iterator(); it.hasNext();) {
			Session o = it.next();
			for (Iterator<Object> it2 = o.getAttributeKeys().iterator(); it2.hasNext();) {
				Object key = it2.next();
				logger.debug(o.getAttribute(key) + "");
			}
		}

		return "sessions/list";
	}
}
