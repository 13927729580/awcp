package cn.org.awcp.unit.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;

@RestController
public class PunNotificationController {
	@Resource(name = "namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	@RequestMapping(value = "notifications", method = RequestMethod.GET)
	public ReturnResult notifications(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize, @RequestParam(value = "read") byte read,
			@RequestParam(value = "type") String type) {
		ReturnResult result = ReturnResult.get();
		// 获取当前登录用户
		PunUserBaseInfoVO user = ControllerHelper.getUser();
		String orderSql = " ORDER BY a.is_read ASC,b.CREATE_TIME DESC ";
		String fields = "b.id,b.title,b.content,b.msg_url,b.type,b.create_name,b.create_time,a.is_read";
		String sql = " FROM p_un_notify_user a LEFT JOIN p_un_notification b ON a.MSG_ID=b.ID WHERE b.state=1 and a.user=:user ";
		if (!"all".equals(type)) {
			sql += " and type=:type ";
		} else {
			sql += " and type<>'flow'";
		}
		if (read != -1) {
			sql += " and a.is_read=:read ";
		}
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("user", user.getUserIdCardNumber());
		paramMap.put("type", type);
		paramMap.put("read", read);
		Long count = jdbcTemplate.queryForObject("SELECT COUNT(1) " + sql, paramMap, Long.class);
		sql += orderSql + " limit " + (currentPage - 1) * pageSize + "," + pageSize;
		List<Map<String, Object>> data = jdbcTemplate.queryForList("SELECT " + fields + sql, paramMap);
		result.setData(data).setTotal(count).setStatus(StatusCode.SUCCESS);
		return result;
	}

	@RequestMapping(value = "notifications/count", method = RequestMethod.GET)
	public ReturnResult countNotifications(
			@RequestParam(required = false, value = "read", defaultValue = "0") byte read,
			@RequestParam(required = false, value = "type", defaultValue = "system") String type) {
		ReturnResult result = ReturnResult.get();
		// 获取当前登录用户
		PunUserBaseInfoVO user = ControllerHelper.getUser();
		String sql = "SELECT count(*) FROM p_un_notify_user a LEFT JOIN p_un_notification b ON a.MSG_ID=b.ID WHERE b.state=1 and a.user=:user ";
		if (!"all".equals(type)) {
			sql += " and type=:type ";
		} else {
			sql += " and type<>'flow'";
		}
		if (read != -1) {
			sql += " and a.is_read=:read ";
		}
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("user", user.getUserIdCardNumber());
		paramMap.put("type", type);
		paramMap.put("read", read);
		Long count = jdbcTemplate.queryForObject(sql, paramMap, Long.class);
		result.setData(count).setStatus(StatusCode.SUCCESS);
		return result;
	}

	@RequestMapping(value = "notifications/{id}", method = RequestMethod.GET)
	public ReturnResult notificationsById(@PathVariable("id") String id) {
		ReturnResult result = ReturnResult.get();
		// 获取当前登录用户
		String sql = "SELECT b.id,b.title,b.content,b.msg_url,b.icon,b.type,b.create_name,b.create_time,a.is_read FROM p_un_notify_user a LEFT JOIN p_un_notification b ON a.MSG_ID=b.ID WHERE b.id=:id";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, paramMap);
		if (data.isEmpty()) {
			return result.setStatus(StatusCode.SUCCESS);
		} else {
			return result.setData(data.get(0)).setStatus(StatusCode.SUCCESS);
		}
	}

	@RequestMapping(value = "notifications/read/{id}", method = RequestMethod.GET)
	public ReturnResult readNotifications(@PathVariable("id") String id) {
		ReturnResult result = ReturnResult.get();
		// 获取当前登录用户
		String sql = "UPDATE p_un_notify_user SET is_read='1' WHERE msg_id=:id AND USER=:user";
		// 获取当前登录用户
		PunUserBaseInfoVO user = ControllerHelper.getUser();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("user", user.getUserIdCardNumber());
		paramMap.put("id", id);
		return result.setStatus(StatusCode.SUCCESS).setData(jdbcTemplate.update(sql, paramMap));
	}

}
