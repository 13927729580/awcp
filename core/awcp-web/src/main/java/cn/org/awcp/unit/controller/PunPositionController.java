package cn.org.awcp.unit.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.core.utils.ConverToJson;
import cn.org.awcp.unit.service.PunPositionService;
import cn.org.awcp.unit.vo.PunPositionVO;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;

@Controller
@RequestMapping("/punPositionController")
public class PunPositionController {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(PunPositionController.class);
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

	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ReturnResult save(PunPositionVO punPosition, HttpServletResponse response) {
		ReturnResult result = ReturnResult.get();
		/**
		 * 增加岗位唯一校验 ljw 2014-12-8
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", punPosition.getName());
		List<PunPositionVO> vos = punPositionService.queryResult("queryList", params);
		if (null != vos && vos.size() > 0) {
			result.setStatus(StatusCode.FAIL.setMessage("岗位" + punPosition + "已存在"));
		} else {
			///////////////////////// end/////////////////////////////////
			punPosition.setShortName(punPosition.getName());
			punPositionService.save(punPosition);
			result.setStatus(StatusCode.SUCCESS);
		}
		return result;
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
	public ModelAndView remove(long[] positionId, RedirectAttributes ra) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/punPositionController/pageList.do");
		try {
			if (null != positionId && positionId.length > 0) {
				for (Long id : positionId) {
					PunPositionVO p = new PunPositionVO();
					p.setPositionId(id);
					punPositionService.remove(p);
				}
			}
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
			logger.info("ERROR", e);
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
			logger.info("ERROR", e);
			return null;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ReturnResult update(PunPositionVO punPosition, HttpServletResponse response) {
		ReturnResult result = ReturnResult.get();
		logger.debug(punPosition.getGrade() + "");
		punPosition.setShortName(punPosition.getName());
		punPositionService.update(punPosition, "update");
		result.setStatus(StatusCode.SUCCESS);
		return result;
	}

	@RequestMapping(value = "/pageList", method = RequestMethod.GET)
	public ModelAndView selectPagedByExample(
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punGroup-Positions");

		PageList<PunPositionVO> list = punPositionService.selectPagedByExample("queryList", null, currentPage, pageSize,
				null);
		mv.addObject("vos", list);

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