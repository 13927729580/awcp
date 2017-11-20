package cn.org.awcp.test;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.formdesigner.application.vo.StoreVO;

public class Test extends BaseJunit4Test {
	@Autowired
	@Qualifier("storeServiceImpl")
	private StoreService storeService;

	@org.junit.Test
	public void test() {
		updatePageID("6", "11");
		updatePageID("7", "12");
	}

	public Map<String, Object> updatePageID(String oldID, String newID) {
		SzcloudJdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		jdbcTemplate.beginTranstaion();
		try {
			String sql = "update p_fm_dynamicpage set id=? where id=?";
			String updateMenuSQL = "update p_un_menu set DYNAMICPAGE_ID=? where DYNAMICPAGE_ID=?";
			jdbcTemplate.update(sql, newID, oldID);
			jdbcTemplate.update(updateMenuSQL, newID, oldID);
			PageList<StoreVO> stores = storeService.findByDyanamicPageId(oldID);
			for (StoreVO store : stores) {
				String content = store.getContent();
				JSONObject json = JSONObject.parseObject(content);
				if (StringUtils.isNotBlank(json.getString("dynamicPageId"))) {
					json.put("dynamicPageId", newID);
				}
				store.setContent(json.toJSONString());
				store.setDynamicPageId(newID);
				storeService.save(store);
			}
			jdbcTemplate.commit();
		} catch (Exception e) {
			e.printStackTrace();
			jdbcTemplate.rollback();
		}
		return null;
	}

}
