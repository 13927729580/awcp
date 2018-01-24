package cn.org.awcp.formdesigner.sync;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.Springfactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.io.Serializable;
import java.util.List;

public class Apps implements Serializable {
    private String id;
    private String name;
    private boolean source;
    private boolean template;
    private boolean meta;
    private boolean data;
    private boolean table;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSource() {
        return source;
    }

    public void setSource(boolean source) {
        this.source = source;
    }

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    public boolean isMeta() {
        return meta;
    }

    public void setMeta(boolean meta) {
        this.meta = meta;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    public boolean isTable() {
        return table;
    }

    public void setTable(boolean table) {
        this.table = table;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    private List<SyncPage> appPages;

    public List<SyncPage> getAppPages() {
        return appPages;
    }

    public void setAppPages(List<SyncPage> appPages) {
        this.appPages = appPages;
    }

    public static Apps getApp(String id) {
        SzcloudJdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
        String sql = "select `name`,`source`,IS_TEMPLATE as template,IS_META as `meta`," +
                "IS_DATA as `data`,IS_TABLE as `table`,remark from p_fm_apps where id=?";
        try {
            Apps result = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Apps.class), id);
            sql = "select page_id as pageId,api_ids as apiIds from p_fm_app_page where APP_ID=?";
            List<SyncPage> list = jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<>(SyncPage.class));
            result.setAppPages(list);
            result.setId(id);
            return result;
        } catch (DataAccessException e) {
            return null;
        }

    }
}
