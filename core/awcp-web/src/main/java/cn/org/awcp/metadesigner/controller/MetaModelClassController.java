package cn.org.awcp.metadesigner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.org.awcp.metadesigner.application.MetaModelClassService;
import cn.org.awcp.metadesigner.vo.MetaModelClassVO;

@Controller
@RequestMapping(value = "/metaModelClass")
public class MetaModelClassController {

	@Autowired
	private MetaModelClassService metaModelClassServiceImpl;

	@ResponseBody
	@RequestMapping(value = "/findAll")
	public List<MetaModelClassVO> findAll(long id) {
		try {
			List<MetaModelClassVO> mmc = this.metaModelClassServiceImpl.findAll(id);
			return mmc;
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/remove")
	public String remove(Long id) {
		try {
			MetaModelClassVO mmc = this.metaModelClassServiceImpl.get(id);
			this.metaModelClassServiceImpl.remove(mmc);
			return "";
		} catch (Exception e) {
			return "error";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/queryByProjectId")
	public List<MetaModelClassVO> queryByProjectId(long projectId) {
		List<MetaModelClassVO> ls = this.metaModelClassServiceImpl.queryByProjectId(projectId);
		return ls;
	}

}
