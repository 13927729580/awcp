package org.szcloud.framework.formdesigner.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.formdesigner.application.service.PfmTemplateService;
import org.szcloud.framework.formdesigner.application.vo.PfmTemplateVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Controller
@RequestMapping("/pfmTemplateController")
public class PfmTemplateController {

	@Resource(name = "pfmTemplateServiceImpl")
	private PfmTemplateService pfmTemplateService;

	private String saveDir = "resources" + File.separator + "template" + File.separator;

	@RequestMapping(value = "/pageList")
	public ModelAndView listPageActs(@ModelAttribute PfmTemplateVO vo,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
		BaseExample baseExample = new BaseExample();
		if (StringUtils.isNotEmpty(vo.getFileName())) {
			if (StringUtils.isNumeric(vo.getFileName())) {
				baseExample.createCriteria().andEqualTo("id", vo.getFileName());
			} else {
				baseExample.createCriteria().andLike("file_name", "%" + vo.getFileName() + "%");
			}
		}
		PageList<PfmTemplateVO> list = pfmTemplateService.selectPagedByExample(baseExample, currentPage, pageSize,
				"id desc");
		ModelAndView mv = new ModelAndView();
		mv.addObject("vos", list);
		mv.addObject("currentPage", currentPage);
		mv.addObject("pageSize", pageSize);
		mv.addObject("vo", vo);
		mv.setViewName("/formdesigner/page/template-list");
		return mv;
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute PfmTemplateVO vo, MultipartFile file, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/pfmTemplateController/pageList.do");

		// 新增
		if (vo.getId() == null) {
			if (file != null && !file.isEmpty()) {
				String logoRealPathDir = request.getSession().getServletContext().getRealPath(File.separator);
				String localtion = saveDir + System.currentTimeMillis() + file.getOriginalFilename();
				vo.setFileLocation(localtion);
				// 将模板内容存入DB
				try {
					String content = getStrFromInputSteam(file.getInputStream());
					if (StringUtils.isNotBlank(content)) {
						vo.setContent(content);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				File dir = new File(logoRealPathDir + saveDir);
				if (!dir.exists()) {
					dir.mkdir();
				}
				try {
					file.transferTo(new File(logoRealPathDir + localtion));
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			pfmTemplateService.save(vo);
		} else { // 修改

			if (file != null && !file.isEmpty()) {
				// 如果有修改附件，则删除原来旧的附件
				File oldFile = new File(
						request.getSession().getServletContext().getRealPath(File.separator) + vo.getFileLocation());
				oldFile.delete();

				String logoRealPathDir = request.getSession().getServletContext().getRealPath(File.separator);
				String localtion = saveDir + System.currentTimeMillis() + file.getOriginalFilename();
				vo.setFileLocation(localtion);

				// 将模板内容存入DB
				try {
					String content = getStrFromInputSteam(file.getInputStream());
					vo.setContent(content);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				File dir = new File(logoRealPathDir + saveDir);
				if (!dir.exists()) {
					dir.mkdir();
				}
				try {
					file.transferTo(new File(logoRealPathDir + localtion));
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			pfmTemplateService.update(vo, "update");
		}

		return mv;
	}

	public String getStrFromInputSteam(InputStream in) throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		// 最好在将字节流转换为字符流的时候 进行转码
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = bf.readLine()) != null) {
			buffer.append(line);
		}

		return buffer.toString();
	}

	@RequestMapping(value = "delete")
	public ModelAndView punGroupDelete(Long[] boxs, int currentPage, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("redirect:/pfmTemplateController/pageList.do?currentPage=" + currentPage);
			for (Long id : boxs) {

				// 先删除文件
				PfmTemplateVO pfm = pfmTemplateService.get(id);
				File file = new File(
						request.getSession().getServletContext().getRealPath(File.separator) + pfm.getFileLocation());
				file.delete();
				// 删除对应的记录
				pfmTemplateService.remove(pfm);
			}
			mv.addObject("result", "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("result", "操作失败：系统异常");
		}
		return mv;
	}

	/**
	 * 根据ID查找
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "get")
	public ModelAndView punGroupGet(Long[] boxs) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/formdesigner/page/template-edit");
		if (boxs != null) {
			try {
				PfmTemplateVO vo = pfmTemplateService.get(boxs[0]);
				mv.addObject("vo", vo);
			} catch (Exception e) {
				e.printStackTrace();
				mv.addObject("result", "异常");
			}
		}
		return mv;
	}

	/**
	 * 返回所有Template对象json格式字符串
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllByAjax")
	public List<PfmTemplateVO> getStylesByAjax() {
		BaseExample baseExample = new BaseExample();
		PageList<PfmTemplateVO> list = pfmTemplateService.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE,
				"id desc");
		return list;
	}

}