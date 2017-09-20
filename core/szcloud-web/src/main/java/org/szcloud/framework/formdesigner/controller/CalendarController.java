package org.szcloud.framework.formdesigner.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.base.BaseController;
import org.szcloud.framework.formdesigner.application.service.CalendarService;
import org.szcloud.framework.formdesigner.application.vo.CalendarVO;
import org.szcloud.framework.venson.controller.base.ControllerHelper;

@Controller
@RequestMapping("/calendar")
public class CalendarController extends BaseController {

	@Autowired
	@Qualifier("calendarServiceImpl")
	private CalendarService calendarService;

	@RequestMapping(value = "/myCalendar")
	private ModelAndView myCalendar() {
		ModelAndView mv = new ModelAndView();

		mv.setViewName("/formdesigner/modules/calendar");
		mv.addObject("userId", ControllerHelper.getUserId());

		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/save")
	public CalendarVO save(CalendarVO vo) {
		String id = calendarService.save(vo);
		vo.setId(id);
		return vo;
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public void delete(String id) {
		CalendarVO vo = calendarService.findById(id);
		vo.getCalendarInstance().remove();
	}

	@ResponseBody
	@RequestMapping(value = "/findByUserId")
	public List<CalendarVO> findByUserId(Long userId) {
		List<CalendarVO> result = new ArrayList<CalendarVO>();
		if (userId != null) {
			result = calendarService.findByUserId(userId);
		}
		return result;

	}

}
