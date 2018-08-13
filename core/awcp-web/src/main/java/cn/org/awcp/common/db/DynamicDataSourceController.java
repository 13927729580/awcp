package cn.org.awcp.common.db;

import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import com.alibaba.druid.util.JdbcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.org.awcp.common.db.DynamicDataSource.MASTER_DATA_SOURCE;

@RestController
@Controller
public class DynamicDataSourceController {

    @Autowired
    private DataSource masterDataSource;

    @Autowired
    DynamicDataSource dataSource;

    @RequestMapping(value = "anon/group/list", method = RequestMethod.GET)
    public ReturnResult list() {
        ReturnResult result = ReturnResult.get();
        String sql = "select GROUP_NAME name,DATASOURCE_NAME id from fw_mm_group where DATASOURCE_NAME <>? order by name";
        try {
            List<Map<String, Object>> list = JdbcUtils.executeQuery(masterDataSource, sql, MASTER_DATA_SOURCE);
            Map<String, Object> map = new HashMap<>(2);
            map.put("name", "AWCP");
            map.put("id", MASTER_DATA_SOURCE);
            list.add(0, map);
            result.setStatus(StatusCode.SUCCESS).setData(list);
        } catch (SQLException e) {
            result.setStatus(StatusCode.FAIL).setMessage(e.getMessage());
        }
        return result;
    }

}
