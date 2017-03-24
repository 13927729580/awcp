package org.szcloud.framework.unit.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.szcloud.framework.core.utils.ConverToJson;
import org.szcloud.framework.unit.service.PunPositionService;
import org.szcloud.framework.unit.utils.HttpUtil;
import org.szcloud.framework.unit.vo.PunAJAXStatusVO;
import org.szcloud.framework.unit.vo.PunPositionVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Controller
@RequestMapping("/punPositionController")
public class PunPositionController {
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(PunPositionController.class);
	public static final int pagesize = 8;

	@Resource(name = "punPositionServiceImpl")
	private PunPositionService punPositionService;

	@RequestMapping(value = "/manager/punPositions", method = RequestMethod.GET)
	public ModelAndView listPunPosition(String pagenum, PunPositionVO punPosition) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("punPositions");
		mv.addObject("sidebar", "punPositions");
		int num = 1;
		if (null != pagenum) {
			num = Integer.parseInt(pagenum);
		}
		List<PunPositionVO> list = punPositionService.findAll();
		mv.addObject("punPositiontList", list);
		mv.addObject("length", list.size());
		mv.addObject("pagenum", num);
		mv.addObject("punPosition", punPosition);
		return mv;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save(PunPositionVO punPosition, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/punPositionController/pageList.do?groupId=" + punPosition.getGroupId());
		PunAJAXStatusVO respStatus = new PunAJAXStatusVO();
		/**
		 * 增加岗位唯一校验 ljw 2014-12-8
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupId", punPosition.getGroupId());
		params.put("name", punPosition.getName());
		List<PunPositionVO> vos = punPositionService.queryResult("queryList", params);
		if (null != vos && vos.size() > 0) {
			respStatus.setStatus(1);
			respStatus.setMessage("岗位" + punPosition + "已存在");
		} else {
			///////////////////////// end/////////////////////////////////
			punPosition.setShortName(punPosition.getName());
			punPositionService.save(punPosition);
			respStatus.setStatus(0);
			// ra.addFlashAttribute("result","保存成功");
		}
		HttpUtil.writeDataToResponse(response, respStatus);
	}

	/**
	 * ljw 2014-12-5 修改
	 * 
	 * @param positionId
	 *            岗位Id
	 * @param groupId
	 *            组ID
	 * @param ra
	 * @return
	 */
	@RequestMapping(value = "/remove")
	public ModelAndView remove(long[] positionId, int groupId, RedirectAttributes ra) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/punPositionController/pageList.do?groupId=" + groupId);
		try {
			if (null != positionId && positionId.length > 0) {
				for (Long id : positionId) {
					PunPositionVO p = new PunPositionVO();
					p.setPositionId(id);
					punPositionService.remove(p);
				}
			}
			mv.addObject("groupId", groupId);
			ra.addFlashAttribute("result", "成功删除" + positionId.length + "个岗位");
		} catch (Exception e) {
			ra.addFlashAttribute("result", "执行删除异常");
		}
		return mv;
	}

	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	public ModelAndView findAll(Model model, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();
		try {
			List<PunPositionVO> list = this.punPositionService.findAll();
			model.addAttribute("class", list);
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ModelAndView get(long id, PrintWriter pw) {
		try {
			PunPositionVO mmo = this.punPositionService.get(id);
			String str = ConverToJson.convertObjectToJson(mmo);
			logger.debug(str);
			pw.write(str);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void update(PunPositionVO punPosition, HttpServletResponse response) {
		logger.debug(punPosition.getGrade() + "");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/punPositionController/pageList.do?groupId=" + punPosition.getGroupId());
		PunAJAXStatusVO respStatus = new PunAJAXStatusVO();
		punPosition.setShortName(punPosition.getName());
		punPositionService.update(punPosition, "update");
		respStatus.setStatus(0);
		HttpUtil.writeDataToResponse(response, respStatus);
	}

	@RequestMapping(value = "/pageList", method = RequestMethod.GET)
	public ModelAndView selectPagedByExample(String queryStr, Map<String, Object> params, String sortString,
			int groupId) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punGroup-Positions");

		Map queryParam = new HashMap();
		queryParam.put("groupId", groupId);
		PageList<PunPositionVO> list = punPositionService.selectPagedByExample("queryList", queryParam, 1, 9999, null);

		Map map = new HashMap();
		map.put("groupId", groupId);

		mv.addObject("punPositiontList", list);
		mv.addObject("vo", map);

		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/pageListByAjax", method = RequestMethod.GET)
	public List<PunPositionVO> selectPagedByExampleByAjax(String queryStr, Map<String, Object> params,
			String sortString, int groupId) {
		ModelAndView mv = new ModelAndView();
		Map queryParam = new HashMap();
		queryParam.put("groupId", groupId);
		PageList<PunPositionVO> list = punPositionService.selectPagedByExample("queryList", queryParam, 1, 9999, null);
		mv.addObject("punPositiontList", list);
		return list;
	}

}