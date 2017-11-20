package cn.org.awcp.formdesigner.controller;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.Criteria;
import cn.org.awcp.core.utils.Tools;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.unit.vo.PunSystemVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/print")
public class PrintManageController extends BaseController{
	
	@Autowired
	@Qualifier("storeServiceImpl")
	private StoreService storeService;
	
	
	@ResponseBody
	@RequestMapping(value = "/save")
	public String save(StoreVO vo) {
		JSONObject rtn = new JSONObject();
		//名称查重，如果有id，那么如果通过名称查出来的记录的id与本身的id不匹配，则有重名现象
		//如果没有id，那么通过名称查询，如果记录数大于0则重名
		boolean error = false;
		BaseExample baseExample = new BaseExample();
		Criteria criteria = baseExample.createCriteria();
		criteria.andEqualTo("NAME", vo.getName()).andLike("CODE", StoreService.PRINT_CODE + "%");
		
		List<StoreVO> storeVOs = storeService.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, null);
		if(storeVOs != null) {
			if(StringUtils.isNotBlank(vo.getId())){
				for(StoreVO s : storeVOs) {
					if(!s.getId().equalsIgnoreCase(vo.getId())) {
						error = true;
						break;
					}
				}
			} else {
				error = storeVOs.size() > 0;
			}
		}
		rtn.put("success", false);
		rtn.put("msg", "名称重复，请修改名称");
		//validate name duplicate
		if (!error) {
			Object obj=Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			if(obj instanceof PunSystemVO){
				PunSystemVO system = (PunSystemVO)obj;
				vo.setSystemId(system.getSysId());
			}
			vo.setContent(StringEscapeUtils.unescapeHtml4(vo.getContent()));
			vo.setCode(StoreService.PRINT_CODE + System.currentTimeMillis());
			JSONObject json = JSON.parseObject(vo.getContent());	//将json中的order转为int类型（freeMarker sort_by('order') 必须）
			String id = storeService.save(vo);// 保存Store并返回storeId
			vo.setId(id);
			rtn.put("success", true);
			rtn.put("msg", vo);
		}
		return rtn.toJSONString();
	}
	
	private boolean validate(StoreVO vo) {
		
		return true;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getPrintBySystemId")
	public List<StoreVO> getPrintBySystemId(String name,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "400") int pageSize,
			@RequestParam(required = false, defaultValue = " ID ASC") String sortString) {
		Object obj=Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		PunSystemVO system = (PunSystemVO)obj;
		Long systemId = system.getSysId();
		BaseExample baseExample = new BaseExample();
		Criteria criteria = baseExample.createCriteria();
		criteria.andEqualTo("SYSTEM_ID", systemId)
		.andLike("CODE", StoreService.PRINT_CODE + "%");
		if(StringUtils.isNotBlank(name)) {
//			String[] keys = name.split(" ");
//			for(String item : keys) {
//				if(StringUtils.isNotBlank(item)) {
//					baseExample.getOredCriteria().a
//				}
//			}
			criteria.andLike("NAME", "%" + name + "%");
		}
		return storeService.selectPagedByExample(baseExample, currentPage, pageSize, sortString);
	}
	
	/**
	 * 异步ajax删除选中的数据,更改数据状态
	 * @param selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteByAjax")
	public String deleteComponentByAjax(@RequestParam(value="_selects") String selects){
		String[] ids = selects.split(",");
		if(storeService.delete(ids)){
			return "1";
		}else{
			return "0";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getPrintById")
	public StoreVO getPrintById(@RequestParam(required = true) String storeId) {	
		return storeService.findById(storeId);
	}
}
