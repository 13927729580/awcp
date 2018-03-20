package cn.org.awcp.formdesigner.sync;

import cn.org.awcp.extend.formdesigner.DocumentUtils;
import cn.org.awcp.venson.exception.PlatformException;
import cn.org.awcp.venson.service.FileService;
import cn.org.awcp.venson.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * 动态页面同步类
 *
 * @author venson
 * @version 2017-11-14
 */
@Component("syncPageService")
public class SyncPageService {

    private static final String FIND_TABLE_NAME_BY_MODELCODE = "select tableName from fw_mm_metamodel where modelCode=?";
    private static final String FIND_DYNAMIC_PAGE_BY_ID = "select * from p_fm_dynamicpage where id=?";
    private static final String FIND_META_MODEL_BY_MODELCODE = "select * from fw_mm_metamodel where modelCode=?";
    private static final String FIND_META_MODEL_ITEMS_BY_MODELID = "select * from fw_mm_metamodelitems where modelId=?";
    private static final String FIND_TEMPLATE_BY_ID = "select * from p_fm_template where id=?";
    private static final String FIND_STORE_BY_DYNAMICPAGE_ID = "select * from p_fm_store where DYNAMICPAGE_ID=?";
    private static final String metaModelName = "fw_mm_metamodel";
    private static final String modelItemsName = "fw_mm_metamodelitems";


    /**
     * 日志对象
     */
    private Log logger = LogFactory.getLog(getClass());
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 导出应用
     *
     * @param apps
     */
    public void exportApps(Apps apps) {
        List<SyncPage> appPages = apps.getAppPages();
        Set<String> rep = new HashSet<>(appPages.size() * 4);
        for (SyncPage sync : appPages) {
            syncDynamicPage(sync);
            StringBuffer content = new StringBuffer();
            if (apps.isTable()&&sync.getTableNames()!=null) {
                // 1.同步数据源（建表和表数据）
                List<String> tableNames = sync.getTableNames();
                for (String tableName : tableNames) {
                    if (!rep.contains("T_" + tableName)) {
                        content.append(BeanUtil.createTable(jdbcTemplate, tableName, apps.isData()));
                        rep.add("T_" + tableName);
                    }
                }
            }
            if (apps.isMeta()&&sync.getModelCodes()!=null) {
                // 2.同步元数据
                List<String> modelCodes = sync.getModelCodes();
                for (String modelCode : modelCodes) {
                    if (!rep.contains("M_" + modelCode)) {
                        getMetadata(content, modelCode);
                        rep.add("M_" + modelCode);
                    }
                }
            }
            if (apps.isTemplate() && !rep.contains("E_" + sync.getTemplateId())) {
                // 3.同步模板
                content.append(createTemplate(sync.getTemplateId()));
                rep.add("E_" + sync.getTemplateId());
            }
            // 4.同步动态页面和组件
            content.append(createDynamicPageData(getPage(sync.getPageId())));
            // 5.同步API接口
            String apiIds = sync.getApiIds();
            if (StringUtils.isNotBlank(apiIds)) {
                getAPIData(content, apiIds.split(";"));
            }
            sync.setContent(content.toString());
        }
    }

    @Resource(name = "IFileService")
    private FileService fileService;

    public String importApps(String fileId) {
        InputStream in=null;
        if(fileId.startsWith("http")){
            try {
                URL source=new URL(fileId);
                in=source.openStream();
            } catch (IOException e) {
                logger.debug("ERROR",e);
            }
        }else{
            in = fileService.getInputStream(fileId);
        }
        return importApps(in);


    }

    public String importApps(InputStream in) {
        long time = System.currentTimeMillis();
        logger.debug("开始导入----" + time);
        if (in != null) {
            Object obj = BeanUtil.readObject(in);
            // 判断类型，实例为Apps则是合法文件
            if (obj instanceof Apps) {
                importApps((Apps) obj);
                logger.debug("导入结束----耗时：" + (System.currentTimeMillis() - time) / 1000 + "S");
                return "导入成功";
            } else {
                return "导入失败，导入文件有误";
            }
        } else {
            return "导入失败，上传文件丢失";
        }
    }

    /**
     * 导入应用
     *
     * @param apps
     */
    public void importApps(Apps apps) {
        List<SyncPage> syncPages = apps.getAppPages();
        String id = apps.getId();
        if (StringUtils.isBlank(id)) {
            id = UUID.randomUUID().toString();
        } else {
            jdbcTemplate.update("delete from p_fm_apps where id=?", id);
            jdbcTemplate.update("delete from p_fm_app_page where app_id=?", id);
        }
        String sql = "insert into p_fm_apps(ID,NAME,SOURCE,IS_DATA,IS_META,IS_TEMPLATE,IS_TABLE,CREATE_TIME) values(?,?,'1',?,?,?,?,?)";
        jdbcTemplate.update(sql, id, apps.getName(), apps.isData(), apps.isMeta(), apps.isTemplate(), apps.isTable(), DocumentUtils.getIntance().today());
        sql = "insert into p_fm_app_page(page_id,api_ids,app_id) values(?,?,?)";
        for (SyncPage sync : syncPages) {
            importDynamicPage(sync);
            jdbcTemplate.update(sql, sync.getPageId(), sync.getApiIds(), id);
        }
    }

    private void getAPIData(StringBuffer buffer, String... ids) {
        for (String id : ids) {
            Map<String, Object> map;
            try {
                map = jdbcTemplate.queryForMap("select * from p_fm_api where api_id=?", id);
            } catch (DataAccessException e) {
                continue;
            }
            buffer.append(BeanUtil.getInsertSQL(map, "p_fm_api", "API_ID"));

            List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from p_fm_api_describe where APIID=?", id);
            for (Map<String, Object> m : list) {
                buffer.append(BeanUtil.getInsertSQL(m, "p_fm_api_describe", "ID"));
            }
        }
    }


    /***
     *
     * @param ids
     *            动态页面IDS
     * @param hasTable
     *            是否同步数据源表
     * @param hasTableData
     *            是否同步数据源表数据
     * @param hasMeta
     *            是否同步元数据
     * @param hasTemplate
     *            是否同步模板
     */
    public List<SyncPage> exportPage(String[] ids, boolean hasTable, boolean hasTableData, boolean hasMeta,
                                     boolean hasTemplate) {
        long time = System.currentTimeMillis();
        logger.debug("开始导出----" + time);
        List<SyncPage> syncPages = new ArrayList<>(ids.length);
        Set<String> rep = new HashSet<>(ids.length * 4);
        for (String id : ids) {
            SyncPage sync = syncDynamicPage(id);
            StringBuffer content = new StringBuffer();
            if (hasTable) {
                // 1.同步数据源（建表和表数据）
                List<String> tableNames = sync.getTableNames();
                for (String tableName : tableNames) {
                    if (!rep.contains("T_" + tableName)) {
                        content.append(BeanUtil.createTable(jdbcTemplate, tableName, hasTableData));
                        rep.add("T_" + tableName);
                    }
                }
            }
            if (hasMeta) {
                // 2.同步元数据
                List<String> modelCodes = sync.getModelCodes();
                for (String modelCode : modelCodes) {
                    if (!rep.contains("M_" + modelCode)) {
                        getMetadata(content, modelCode);
                        rep.add("M_" + modelCode);
                    }
                }
            }
            if (hasTemplate && !rep.contains("E_" + sync.getTemplateId())) {
                // 3.同步模板
                content.append(createTemplate(sync.getTemplateId()));
                rep.add("E_" + sync.getTemplateId());
            }
            // 4.同步动态页面和组件
            content.append(createDynamicPageData(getPage(sync.getPageId())));
            sync.setContent(content.toString());
            syncPages.add(sync);
        }
        logger.debug("导出结束----耗时：" + (System.currentTimeMillis() - time) / 1000 + "S");
        return syncPages;
    }

    public void importOrRestorePage(InputStream input) {
        importOrRestorePage(input, false);
    }

    @SuppressWarnings("unchecked")
    public void importOrRestorePage(InputStream input, boolean hasBack) {
        long time = System.currentTimeMillis();
        logger.debug("开始导入----" + time);
        Object obj = BeanUtil.readObject(input);
        if (obj instanceof SyncPage) {
            restorePage((SyncPage) obj);
        } else {
            importPage((List<? extends SyncPage>) obj, hasBack);
        }
        logger.debug("导入结束----耗时：" + (System.currentTimeMillis() - time) / 1000 + "S");
    }

    /**
     * 还原备份文件
     *
     * @param input 文件
     */
    public void restorePage(InputStream input) {
        SyncPage sync = BeanUtil.readObject(input);
        restorePage(sync);
    }

    public void restorePage(SyncPage sync) {
        List<String> sqls = sync.extract(sync.getContent());
        if (sqls.isEmpty()) {
            return;
        }
        // 4.开始导入
        jdbcTemplate.batchUpdate(sqls.toArray(new String[]{}));
    }

    public void importPage(InputStream input) {
        importPage(input, false);
    }

    /***
     * 导入动态表单
     *
     * @param input
     *            文件
     * @param hasBack
     *            导入前是否备份之前文件
     */
    public String importPage(InputStream input, boolean hasBack) {
        List<? extends SyncPage> syncs = BeanUtil.readObject(input);
        return importPage(syncs, hasBack);
    }

    public String importPage(List<? extends SyncPage> syncs, boolean hasBack) {
        if (hasBack) {
            StringBuffer buffer = new StringBuffer();
            for (SyncPage sync : syncs) {
                // 先备份
                buffer.append(backup(sync));
                // 后导入
                importDynamicPage(sync);
            }
            if (buffer.length() > 0) {
                return buffer.toString();
            } else {
                return null;
            }
        } else {
            for (SyncPage sync : syncs) {
                // 后导入
                importDynamicPage(sync);
            }
            return null;
        }
    }

    public void importDynamicPage(SyncPage sync) {
        // 1.查找动态页面是否已经存在
        List<String> sqls = sync.extract(sync.getContent());
        // 4.开始导入
        jdbcTemplate.batchUpdate(sqls.toArray(new String[]{}));
    }

    private String backup(SyncPage sync) {
        StringBuffer buffer = new StringBuffer();
        String oldPageId = sync.getPageId();
        String oldTemplateId = sync.getTemplateId();
        String oldModelXml = sync.getModelXml();
        List<String> oldModelCodes = sync.getModelCodes(oldModelXml);
        // 还原时清空导入的数据
        buffer.append(sync.extractEmpty(sync.extract(sync.getContent())));
        // 备份模板
        buffer.append(createTemplate(oldTemplateId));
        for (String modelCode : oldModelCodes) {
            String table = getTableName(modelCode);
            // 判断表是否存在
            if (isExist(table)) {
                // 备份表
                buffer.append(BeanUtil.createTable(jdbcTemplate, table, true));
            }
            // 备份元数据
            getMetadata(buffer, modelCode);
        }
        // 查看还原页面在本系统是否存在
        Map<String, Object> page = getPage(oldPageId);
        if (page != null) {
            String template_id = (String) page.get("template_id");
            String model_xml = (String) page.get("model_xml");
            List<String> modelCodes = sync.getModelCodes(model_xml);
            // 如果存在并且和还原的模板不一致则备份
            if (template_id != null && !oldTemplateId.equals(template_id)) {
                buffer.append(createTemplate(template_id));
            }
            for (String modelCode : modelCodes) {
                // 如果存在并且和还原的元数据不一致则备份
                if (!oldModelCodes.contains(modelCode)) {
                    String table = getTableName(modelCode);
                    // 判断表是否存在
                    if (isExist(table)) {
                        // 备份表
                        buffer.append(BeanUtil.createTable(jdbcTemplate, table, true));
                    }
                    // 备份元数据
                    getMetadata(buffer, modelCode);
                }
            }
            // 备份动态页面
            buffer.append(createDynamicPageData(page));
        }
        return buffer.toString();
    }

    /***
     * 同步动态页面
     *
     * @param sync
     *            动态页面
     * @return
     */
    public void syncDynamicPage(SyncPage sync) {
        Map<String, Object> page = getPage(sync.getPageId());
        if (page == null) {
            throw new PlatformException("未找到 该动态页面[" + sync.getPageId() + "]");
        }

        String template_id = (String) page.get("template_id");
        if (StringUtils.isBlank(template_id)) {
            throw new PlatformException("template_id,动态模板为空");
        }
        String model_xml = (String) page.get("model_xml");
        if (StringUtils.isNotBlank(model_xml)) {
            List<String> modelCodes;
            try {
                modelCodes = sync.getModelCodes(model_xml);
            } catch (Exception e) {
                throw new PlatformException("数据源脚本解析出错");
            }
            sync.setModelXml(model_xml);
            sync.setModelCodes(modelCodes);
            sync.setTableNames(getTableNames(modelCodes));
        }
        sync.setTemplateId(template_id);
    }

    /***
     * 同步动态页面
     *
     * @param pageId
     *            动态页面ID
     * @return
     */
    public SyncPage syncDynamicPage(String pageId) {
        Map<String, Object> page = getPage(pageId);
        if (page == null) {
            throw new PlatformException("未找到 该动态页面[" + pageId + "]");
        }
        String model_xml = (String) page.get("model_xml");
        if (StringUtils.isBlank(model_xml)) {
            throw new PlatformException("model_xml,数据源为空");
        }
        String template_id = (String) page.get("template_id");
        if (StringUtils.isBlank(template_id)) {
            throw new PlatformException("template_id,动态模板为空");
        }
        SyncPage sync = new SyncPage();
        List<String> modelCodes;
        try {
            modelCodes = sync.getModelCodes(model_xml);
        } catch (Exception e) {
            throw new PlatformException("数据源脚本解析出错");
        }
        sync.setModelXml(model_xml);
        sync.setPageId(pageId);
        sync.setModelCodes(modelCodes);
        sync.setTableNames(getTableNames(modelCodes));
        sync.setTemplateId(template_id);
        return sync;
    }

    private Map<String, Object> getPage(String pageId) {
        Map<String, Object> page;
        try {
            page = jdbcTemplate.queryForMap(FIND_DYNAMIC_PAGE_BY_ID, pageId);
        } catch (Exception e) {
            return null;
        }
        return page;
    }

    /**
     * 根据动态页面找出元数据并同步元数据
     *
     * @param modelCodes 元数据
     * @return
     */
    public String createMetadata(List<String> modelCodes) {
        StringBuffer buffer = new StringBuffer();
        for (String modelCode : modelCodes) {
            getMetadata(buffer, modelCode);
        }
        return buffer.toString();
    }

    private void getMetadata(StringBuffer buffer, String modelCode) {
        Map<String, Object> map;
        try {
            map = jdbcTemplate.queryForMap(FIND_META_MODEL_BY_MODELCODE, modelCode);
        } catch (DataAccessException e) {
            return;
        }
        //清空存在的元数据
        buffer.append("DELETE FROM fw_mm_metamodelitems WHERE modelId=(SELECT ID FROM fw_mm_metamodel WHERE modelCode='"+modelCode+"');\n");
        buffer.append("DELETE FROM fw_mm_metamodel WHERE modelCode='"+modelCode+"';\n");
        buffer.append(BeanUtil.getInsertSQL(map, metaModelName, "id"));
        List<Map<String, Object>> list = jdbcTemplate.queryForList(FIND_META_MODEL_ITEMS_BY_MODELID, map.get("id"));
        for (Map<String, Object> m : list) {
            buffer.append(BeanUtil.getInsertSQL(m, modelItemsName, "id"));
        }
    }

    /**
     * 根据动态页面找出模板并同步模板
     *
     * @param template_id 模板
     * @return
     */
    private String createTemplate(String template_id) {
        Map<String, Object> data;
        try {
            data = jdbcTemplate.queryForMap(FIND_TEMPLATE_BY_ID, template_id);
        } catch (DataAccessException e) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(BeanUtil.getInsertSQL(data, "p_fm_template", "id"));
        return buffer.toString();
    }

    /**
     * 根据动态页面同步动态页面和组件
     *
     * @param page 动态页面
     * @return
     */
    private String createDynamicPageData(Map<String, Object> page) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(BeanUtil.getInsertSQL(page, "p_fm_dynamicpage", "id"));
        List<Map<String, Object>> stores = jdbcTemplate.queryForList(FIND_STORE_BY_DYNAMICPAGE_ID, page.get("id"));
        for (Map<String, Object> store : stores) {
            buffer.append(BeanUtil.getInsertSQL(store, "p_fm_store", "ID"));
        }
        return buffer.toString();
    }

    private boolean isExist(String table) {
        try {
            jdbcTemplate.queryForObject("select count(1) from " + table, Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据动态页面找出数据源表并创建表
     *
     * @param tableNames 元数据
     * @param hasData    是否包含数据
     * @return
     */
    private String createDataSource(List<String> tableNames, boolean hasData) {
        StringBuffer buffer = new StringBuffer();
        for (String tableName : tableNames) {
            buffer.append(BeanUtil.createTable(jdbcTemplate, tableName, hasData));
        }
        return buffer.toString();

    }

    private String getTableName(String modelCode) {
        try {
            return jdbcTemplate.queryForObject(FIND_TABLE_NAME_BY_MODELCODE, String.class, modelCode);
        } catch (DataAccessException e) {
            return "";
        }
    }

    private List<String> getTableNames(List<String> modelCodes) {
        List<String> list = new ArrayList<>(modelCodes.size());
        for (String modelCode : modelCodes) {
            list.add(getTableName(modelCode));
        }
        return list;
    }

    @Deprecated
    public String createTable1(String tableName, boolean inCludeData) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("SHOW FULL FIELDS FROM " + tableName);
        StringBuffer buffer = new StringBuffer();
        buffer.append("DROP TABLE IF EXISTS `" + tableName + "`;\n\n");
        buffer.append("CREATE TABLE `" + tableName + "`(\n");
        for (Map<String, Object> map : list) {
            Object Field = map.get("Field");
            Object Type = map.get("Type");
            Object key = map.get("Key");
            Object Null = map.get("Null");
            Object Default = map.get("Default");
            Object Extra = map.get("Extra");
            String Comment = map.get("Comment").toString();
            buffer.append("\t`" + Field + "` ");
            buffer.append(Type + " ");
            if ("PRI".equals(key)) {
                buffer.append(" PRIMARY KEY ");
            } else if ("UNI".equals(key)) {
                buffer.append(" UNIQUE KEY ");

            }
            if ("NO".equals(Null)) {
                buffer.append(" NOT NULL ");
            }
            if (Default != null) {
                buffer.append(" default " + Default + " ");
            }
            if ("auto_increment".equals(Extra)) {
                buffer.append(" AUTO_INCREMENT ");
            }
            if (StringUtils.isNotBlank(Comment)) {
                buffer.append(" COMMENT '" + Comment + "'");
            }
            buffer.append(",\n");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        buffer.append("\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;\n");
        if (inCludeData) {
            List<Map<String, Object>> data = jdbcTemplate.queryForList("select * from " + tableName);
            for (Map<String, Object> map : data) {
                buffer.append(BeanUtil.getInsertSQL(map, tableName, "ID"));
            }
        }
        return buffer.toString();
    }
}
