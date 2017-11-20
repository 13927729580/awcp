package cn.org.awcp.formdesigner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.utils.DocumentUtils;
import cn.org.awcp.unit.service.PunGroupService;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.vo.PunGroupVO;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.common.SC;

@Controller
@RequestMapping("/common/user")
public class CommonUserController {

	@Autowired
	@Qualifier("punGroupServiceImpl")
	PunGroupService groupService;

	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	PunUserBaseInfoService userService;// 用户Service

	@Autowired
	private FormdesignerService formdesignerServiceImpl;

	/**
	 * 加入常用联系人
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userCount", method = RequestMethod.POST)
	public String userCount(String user) {

		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");

		PunUserBaseInfoVO userInfo = (PunUserBaseInfoVO) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_USER);
		String[] userId = user.split(",");
		String sql = "";
		for (String s : userId) {

			// 查询用户number
			PunUserBaseInfoVO vo = userService.findById(Long.parseLong(s));

			sql = "select * from p_un_common_user where user_id='" + userInfo.getUserId() + "' and user='" + s + "'";
			List<Map<String, Object>> mapResult = jdbcTemplate.queryForList(sql);
			// 存在增加记录否则修改记录
			if (mapResult != null && mapResult.size() > 0) {
				Map<String, Object> map = mapResult.get(0);
				sql = "update p_un_common_user set click_number="
						+ (Integer.parseInt(String.valueOf(map.get("click_number"))) + 1) + " where id='"
						+ map.get("id") + "'";
				jdbcTemplate.update(sql);
			} else {

				sql = "insert into p_un_common_user values('" + UUID.randomUUID().toString() + "','"
						+ userInfo.getUserId() + "','" + s + "',0,null,'" + vo.getNumber() + "')";
				jdbcTemplate.execute(sql);
			}

		}

		return "success";
	}

	/**
	 * 加入常用部门
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/groupCount", method = RequestMethod.POST)
	public String groupCount(String user) {

		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");

		PunUserBaseInfoVO userInfo = (PunUserBaseInfoVO) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_USER);
		String[] userId = user.split(",");
		String sql = "";
		for (String s : userId) {

			PunGroupVO vo = groupService.findById(Long.parseLong(s));
			List<PunGroupVO> list = DocumentUtils.getIntance().listGroupByUser(Long.parseLong(s));

			if (list != null) {
				PunGroupVO pun = list.get(0);
				s = String.valueOf(pun.getGroupId());
			}

			sql = "select * from p_un_common_group where user_id='" + userInfo.getUserId() + "' and group_id='" + s
					+ "'";
			List<Map<String, Object>> mapResult = jdbcTemplate.queryForList(sql);
			// 存在增加记录否则修改记录
			if (mapResult != null && mapResult.size() > 0) {
				Map<String, Object> map = mapResult.get(0);
				sql = "update p_un_common_group set click_number="
						+ (Integer.parseInt(String.valueOf(map.get("click_number"))) + 1) + " where id='"
						+ map.get("id") + "'";
				jdbcTemplate.update(sql);
			} else {

				sql = "insert into p_un_common_group values('" + UUID.randomUUID().toString() + "','" + s + "','"
						+ userInfo.getUserId() + "',0,null,'" + vo.getNumber() + "')";
				jdbcTemplate.execute(sql);
			}
		}

		return "success";
	}

	/**
	 * 查询当前用户下常用联系人
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryByUser")
	public List<PunUserBaseInfoVO> queryByUser() {

		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");

		PunUserBaseInfoVO userInfo = (PunUserBaseInfoVO) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_USER);
		String sql = "select * from (select * from p_un_common_user where user_id='" + userInfo.getUserId()
				+ "' order by click_number desc limit 15) as t order by  case  when t.order_  is null or t.order_=''  then 0 end  ,  length(t.order_) asc, t.order_  asc";
		List<Map<String, Object>> mapResult = jdbcTemplate.queryForList(sql);

		List<PunUserBaseInfoVO> list = new ArrayList<PunUserBaseInfoVO>();
		for (Map<String, Object> map : mapResult) {
			PunUserBaseInfoVO pbi = DocumentUtils.getIntance().getUserById((Long) map.get("user"));
			list.add(pbi);
		}
		return list;
	}

	/**
	 * 查询当前用户下级
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/querySubUserByUser")
	public List<PunUserBaseInfoVO> querySubUserByUser() {
		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		PunUserBaseInfoVO userInfo = (PunUserBaseInfoVO) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_USER);
		String sql = "select USER_ID from p_un_user_group  where leader = " + userInfo.getUserId();
		List<Map<String, Object>> mapResult = jdbcTemplate.queryForList(sql);
		List<PunUserBaseInfoVO> list = new ArrayList<PunUserBaseInfoVO>();
		for (Map<String, Object> map : mapResult) {
			PunUserBaseInfoVO pbi = DocumentUtils.getIntance().getUserById((Long) map.get("USER_ID"));
			list.add(pbi);
		}
		return list;
	}

	/**
	 * 通过sql查询用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/querySqlUser")
	public List<PunUserBaseInfoVO> querySqlUser(String sql, String userName) {
		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		if (userName != null && !"".equals(userName)) {
			sql = "select USER_ID from p_un_user_base_info where USER_ID in (" + sql + ") and NAME like '%" + userName
					+ "%'";
		}
		List<Map<String, Object>> mapResult = jdbcTemplate.queryForList(sql);
		List<PunUserBaseInfoVO> list = new ArrayList<PunUserBaseInfoVO>();
		for (Map<String, Object> map : mapResult) {
			PunUserBaseInfoVO pbi = DocumentUtils.getIntance().getUserById((Long) map.get("USER_ID"));
			list.add(pbi);
		}
		return list;
	}

	/**
	 * 获取节点的接收人
	 * 
	 * @param WorkID
	 * @param NodeID
	 * @param FK_Flow
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryReceiver")
	public List<PunUserBaseInfoVO> queryReceiver(String WorkID, String NodeID, String FK_Flow) {

		String sql = "select FK_Emp from wf_generworkerlist where fk_Node=" + NodeID + " and workID=" + WorkID
				+ " and fk_flow=" + FK_Flow;
		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sql);
		List<PunUserBaseInfoVO> list = new ArrayList<PunUserBaseInfoVO>();
		for (Map<String, Object> map : ls) {
			PunUserBaseInfoVO vo = userService.getUserBaseInfoByGroupIdAndCardNumber(SC.GROUP_ID,
					map.get("FK_Emp").toString());
			list.add(vo);
		}
		return list;
	}

	/**
	 * 查询当前用户下常用部门
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryByGroup")
	public List<PunGroupVO> queryByGroup() {

		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");

		PunUserBaseInfoVO userInfo = (PunUserBaseInfoVO) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_USER);
		String sql = "select * from (select * from p_un_common_group where user_id='" + userInfo.getUserId()
				+ "' order by click_number desc limit 15) as t order by  case  when t.order_  is null or t.order_=''  then 0 end  ,  length(t.order_) asc, t.order_  asc";
		List<Map<String, Object>> mapResult = jdbcTemplate.queryForList(sql);

		List<PunGroupVO> list = new ArrayList<PunGroupVO>();
		for (Map<String, Object> map : mapResult) {
			PunGroupVO pbi = groupService.findById(Long.parseLong(String.valueOf(map.get("group_id"))));
			list.add(pbi);
		}
		return list;
	}

	/**
	 * 所有动态页面绑定列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list-bind")
	public ModelAndView list(String name) {

		ModelAndView mv = new ModelAndView();

		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		String sql = "select * from p_un_page_binding";

		if (name != null && !"".equals(name)) {
			sql = "select * from p_un_page_binding where PAGEID_PC ='" + name + "' or PAGEID_PC_LIST='" + name
					+ "' or PAGEID_APP='" + name + "' or PAGEID_APP_LIST='" + name + "'";
		}

		List<Map<String, Object>> mapResult = jdbcTemplate.queryForList(sql);

		/*
		 * for (Map<String, Object> map : mapResult) { DynamicPageVO
		 * vo=formdesignerServiceImpl.findById(Long.parseLong(map.get("pageId_1"
		 * ).toString())); list.add(vo); }
		 */

		mv.addObject("name", name);
		mv.addObject("vos", mapResult);

		mv.setViewName("formdesigner/page/dynamicPage-list-bind");

		return mv;

	}

	/**
	 * delete
	 * 
	 * @return
	 */
	@RequestMapping(value = "/delete")
	public ModelAndView delete(String _selects) {

		// ModelAndView mv = new ModelAndView();

		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		if (_selects != null && !"".equals(_selects)) {
			String ids[] = _selects.split(",");

			for (String s : ids) {
				String sql = "delete from p_un_page_binding where id='" + s + "'";
				jdbcTemplate.execute(sql);
			}
			// mv.setViewName("formdesigner/page/dynamicPage-list-bind");

			return new ModelAndView("redirect:/common/user/list-bind.do");

		}

		return null;

	}

	/**
	 * 所有动态页面绑定列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/get")
	public Map<String, Object> get(String id) {

		// Map<String,Object> res=new HashMap<String, Object>();

		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		String sql = "select * from p_un_page_binding where id='" + id + "'";

		Map<String, Object> map = jdbcTemplate.queryForMap(sql);
		/*
		 * Set<String > key=map.keySet();
		 * 
		 * for (String s : key) {
		 * if(!"id".equals(s)&&!"".equals(map.get(s))&&map.get(s)!=null){ DynamicPageVO
		 * vo=formdesignerServiceImpl.findById(Long.parseLong(map.get(s). toString()));
		 * res.put(s,map.get(s)+"("+vo.getName()+")"); }else if("id".equals(s)){
		 * res.put("id",map.get(s)); } }
		 */

		return map;

	}

	/**
	 * 所有动态页面绑定列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update")
	public Map<String, Object> update(String id, String pc, String pc_list, String app, String app_list) {

		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		String sql = "update p_un_page_binding set PAGEID_PC='" + pc + "',PAGEID_PC_LIST='" + pc_list + "',PAGEID_APP='"
				+ app + "',PAGEID_APP_LIST='" + app_list + "' where id='" + id + "'";
		jdbcTemplate.execute(sql);

		sql = "select * from p_un_page_binding where id='" + id + "'";

		Map<String, Object> map = jdbcTemplate.queryForMap(sql);

		return map;

	}

	/**
	 * 所有动态页面绑定列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/binding")
	public ModelAndView binding(String _selects) {

		if (_selects != null && !"".endsWith(_selects)) {

			String ids[] = _selects.split(",");

			JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
			Map<String, Object> map = new HashMap<String, Object>();

			for (String s : ids) {

				// 是否重复

				String sql = "select * from p_un_page_binding where PAGEID_PC ='" + s + "' or PAGEID_PC_LIST='" + s
						+ "' or PAGEID_APP='" + s + "' or PAGEID_APP_LIST='" + s + "'";
				List<Map<String, Object>> mapResult = jdbcTemplate.queryForList(sql);
				if (null != mapResult && mapResult.size() > 0) {

					return new ModelAndView("redirect:/fd/list-bind.do?mes=error");
				}

				DynamicPageVO vo = formdesignerServiceImpl.findById(s);
				if (vo.getPageType() == 1002 && !"APP".equalsIgnoreCase(vo.getDescription())) {
					map.put("PAGEID_PC", s);
				}

				if (vo.getPageType() == 1003 && !"APP".equalsIgnoreCase(vo.getDescription())) {
					map.put("PAGEID_PC_LIST", s);
				}

				if (vo.getPageType() == 1002 && "APP".equalsIgnoreCase(vo.getDescription())) {
					map.put("PAGEID_APP", s);
				}

				if (vo.getPageType() == 1003 && "APP".equalsIgnoreCase(vo.getDescription())) {
					map.put("PAGEID_APP_LIST", s);
				}

			}

			if (null != map && map.size() > 0) {

				String sql = "insert into p_un_page_binding values('" + UUID.randomUUID().toString() + "','"
						+ map.get("PAGEID_PC") + "','" + map.get("PAGEID_PC_LIST") + "','" + map.get("PAGEID_APP")
						+ "','" + map.get("PAGEID_APP_LIST") + "')";
				jdbcTemplate.execute(sql);
				// mv.setViewName("redirect:/list-bind/list-bind");
				return new ModelAndView("redirect:/common/user/list-bind.do");
			}

		}

		return null;

	}

}
