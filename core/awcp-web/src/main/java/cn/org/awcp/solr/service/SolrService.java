package cn.org.awcp.solr.service;

import cn.org.awcp.extend.formdesigner.DocumentUtils;
import cn.org.awcp.venson.exception.PlatformException;
import cn.org.awcp.venson.service.FileService;
import cn.org.awcp.venson.service.impl.FileServiceImpl.AttachmentVO;
import cn.org.awcp.venson.util.DateFormaterUtil;
import cn.org.awcp.venson.util.PlatfromProp;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * solr操作服务类
 *
 * @author quxiang
 * @version 20180313
 */
@Component("solrService")
public class SolrService {
    /**
     * 日志对象
     */
    protected final Log logger = LogFactory.getLog(SolrService.class);

    /**
     * 增量类型：全量
     */
    private final String RAISE_TYPE_ALL = "1";
    /**
     * 增量类型：增量
     */
    private final String RAISE_TYPE_INCREMENT = "2";

    /**
     * 检索类型:数据
     */
    public static final String SOLR_TYPE_DATA = "1";
    /**
     * 检索类型：文档
     */
    public static final String SOLR_TYPE_DOCUMENT = "0";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Resource(name = "IFileService")
    private FileService fileService;


    /**
     * solr配置路径
     */
    public static final String BASE_SOLR_URL = PlatfromProp.getValue("baseSolrUrl");

    public SolrService() {
    }

    public SolrClient geclient() {
        return new HttpSolrClient.Builder(BASE_SOLR_URL).build();
    }


    public Map<String, Object> getById(String id) {
        try {
            return jdbcTemplate.queryForMap("SELECT readtype,readpath,raisetype,raisestring,maxraise,indexfield from p_fm_solr_indexe WHERE id = ?", id);
        } catch (DataAccessException e) {
            logger.debug("ERROR", e);
            return null;
        }
    }


    /**
     * 根据条件删除solr 数据
     *
     * @param query 查询条件
     * @return boolean
     */
    public boolean solrDeleteByQuery(String query) {
        SolrClient client = geclient();
        try {
            client.deleteByQuery(query);
            client.commit();
        } catch (Exception e) {
            logger.info("ERROR", e);
            return false;
        } finally {
            IOUtils.closeQuietly(client);
        }
        return true;
    }

    /**
     * 查询
     *
     * @param keywords 关键词
     * @param type     检索类型，0：文档，1：数据
     * @return QueryResponse
     */
    public QueryResponse query(String keywords, String type) {
        SolrClient client = geclient();
        try {
            // + – && || ! ( ) { } [ ] ^ " ~ * ? : \ 特殊搜索字符转义
            keywords = ClientUtils.escapeQueryChars(keywords);
            SolrQuery query = new SolrQuery();
            if (SOLR_TYPE_DOCUMENT.equals(type)) {
                query.setQuery("filename:" + keywords);
            } else if (SOLR_TYPE_DATA.equals(type)) {
                query.setQuery("keywords:" + keywords);
            } else {
                throw new PlatformException("不支持的搜索类型");
            }
            return client.query(query);
        } catch (SolrServerException | IOException e) {
            logger.info("ERROR", e);
        } finally {
            IOUtils.closeQuietly(client);
        }
        return null;
    }

    /**
     * 数据库表建立solr检索
     *
     * @param raiseid    配置id
     * @param readpath   SQL语句
     * @param raisetype  增量类型
     * @param raiseField 增量字段
     * @param indexfield 索引字段
     * @param max        增量最大值
     */
    public void indexData(String raiseid, String readpath, String raisetype,
                          String raiseField, String indexfield, String max) {
        List<Map<String, Object>> datas = jdbcTemplate.queryForList(readpath);
        if (!datas.isEmpty()) {
            //处理增量数据
            datas = handleIncrement(raiseid, raisetype, raiseField, max, datas);
            //判断过滤完是否还有数据
            if (!datas.isEmpty()) {
                //设置最大增量值
                setMaxRaise(raiseid, raisetype, raiseField, datas);
                //数据上传到solr服务器
                indexDataToSolr(raiseid, indexfield, datas);
            }

        }
    }


    /**
     * 索引文档
     *
     * @param raiseid     配置id
     * @param readpath    执行sql
     * @param raiseField  增量字段
     * @param fileIdField 文件ID字段
     * @param raisetype   增量模式
     */
    public void indexDocument(String raiseid, String readpath, String raisetype,
                              String raiseField, String fileIdField, String max) {
        // 查询所有的附件ID进行索引
        List<Map<String, Object>> datas = jdbcTemplate.queryForList(readpath);
        if (!datas.isEmpty()) {
            //处理增量数据
            datas = handleIncrement(raiseid, raisetype, raiseField, max, datas);
            if (!datas.isEmpty()) {
                //设置最大增量值
                setMaxRaise(raiseid, raisetype, raiseField, datas);
                //文件上传到solr服务器
                indexFileToSolr(raiseid, fileIdField, datas);
            }
        }
    }

    /**
     * @param raiseid    配置id
     * @param raisetype  增量类型
     * @param raiseField 增量字段
     * @param max        增量最大值
     * @param datas      数据源
     * @return
     */
    private List<Map<String, Object>> handleIncrement(String raiseid, String raisetype, String raiseField, String max, List<Map<String, Object>> datas) {
        //如果增量值不为空并且增量类型为增量则进行过滤
        boolean isIncrement = StringUtils.isNotBlank(max) && RAISE_TYPE_INCREMENT.equals(raisetype);
        if (isIncrement) {
            String raiseValue = String.valueOf(datas.get(0).get(raiseField));
            datas = filter(raiseField, max, datas, raiseValue);
        } else {
            //全量更新前清空索引
            solrDeleteByQuery("clean_id:" + raiseid);
        }
        return datas;
    }

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * solr上传服务开启多线程的次数分隔
     */
    public static final int SPLIT = 10;

    /**
     * 将数据提交到solr服务器
     *
     * @param raiseId 配置id
     * @param data    要提交的数据
     */
    private void indexDataToSolr(String raiseId, String indexField, List<Map<String, Object>> data) {
        String[] indexFields = indexField.split(",");
        List<SolrInputDocument> solrInputDocuments = new ArrayList<>(data.size());
        for (Map<String, Object> map : data) {
            Object[] values = new Object[indexFields.length];
            for (int j = 0; j < indexFields.length; j++) {
                values[j] = map.get(indexFields[j]);
            }
            String dataId = map.toString();
            // 拆分多个字段
            SolrInputDocument document = new SolrInputDocument("clean_id", raiseId, "id", dataId, "metadata", JSON.toJSONString(map));
            document.addField("content", values);
            solrInputDocuments.add(document);
        }
        SolrClient client = geclient();
        try {
            client.add(solrInputDocuments);
            client.commit();
        } catch (SolrServerException | IOException e) {
            logger.debug("ERROR", e);
            try {
                client.rollback();
            } catch (SolrServerException | IOException e1) {
                logger.debug("ERROR", e1);
            }
        } finally {
            IOUtils.closeQuietly(client);
        }

    }


    /**
     * 索引文件
     *
     * @param raiseId     索引配置ID
     * @param fileIdField 文件ID字段
     * @param data        数据源
     */
    private void indexFileToSolr(String raiseId, String fileIdField, List<Map<String, Object>> data) {
        String today = DocumentUtils.getIntance().today();
        SolrClient client = geclient();
        int size = data.size();
        //SPLIT次以下就不开启多线程
        if (size / SPLIT <= 1) {
            for (int j = 0; j < size; j++) {
                pushFileToSolr(raiseId, client, today, (String) (data.get(j).get(fileIdField)));
            }
        } else {
            int count = size / SPLIT;
            CountDownLatch countDownLatch = new CountDownLatch(count);
            for (int i = 1; i <= count; i++) {
                int splitSize = i * SPLIT;
                threadPoolTaskExecutor.execute(() -> {
                    int init;
                    //如果相等则为0，否则就减去SPLIT
                    if (splitSize == SPLIT) {
                        init = 0;
                    } else {
                        init = splitSize - SPLIT;
                    }
                    for (int j = init; j < splitSize; j++) {
                        pushFileToSolr(raiseId, client, today, (String) (data.get(j).get(fileIdField)));
                    }
                    countDownLatch.countDown();
                });
            }
            //循环余数
            for (int i = count * SPLIT; i < size; i++) {
                pushFileToSolr(raiseId, client, today, (String) (data.get(i).get(fileIdField)));
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                logger.debug("ERROR", e);
            }
        }
        IOUtils.closeQuietly(client);

    }

    private void pushFileToSolr(String raiseId, SolrClient client, String today, String fileId) {
        if (fileId == null) {
            return;
        }
        AttachmentVO vo = fileService.get(fileId);
        if (vo != null) {
            //查看文件是否是文档类型
            if (!isDoc(vo.getFileName())) {
                return;
            }
            //查看文件流是否存在
            InputStream is = fileService.getInputStream(vo);
            if (is == null) {
                return;
            }
            //得出KB
            ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
            org.apache.solr.common.util.ContentStream contentStream = new IContentStream(vo, is);
            up.addContentStream(contentStream);
            up.setParam("literal.id", vo.getId());
            up.setParam("literal.filename", vo.getFileName());
            //文件大小
            up.setParam("literal.filesize", vo.getSize() / 1024 + " KB");
            //每个索引条件的id
            up.setParam("literal.clean_id", raiseId);
            // 文件索引时间
            up.setParam("literal.pathuploaddate", today);
            // 文件类型，doc,pdf
            up.setParam("literal.pathftype", vo.getContentType());
            // 文件内容
            up.setParam("fmap.content", "attr_content");
            up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
            try {
                client.request(up);
                client.commit();
            } catch (IOException | SolrServerException e) {
                logger.debug(e);
            }
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 根据文件名判断是否属于文档类型文件
     *
     * @param fileName 文件名
     * @return boolean
     */
    private boolean isDoc(String fileName) {
        String type = fileName.toLowerCase();
        return type.contains("xls") || type.contains("pdf") ||
                type.contains("doc") || type.contains("ppt") ||
                type.contains("txt");
    }


    /**
     * 设置增量的最大值
     *
     * @param raiseid    索引配置ID
     * @param raiseType  增量类型
     * @param raiseField 增量字段
     * @param datas      数据源
     */
    private void setMaxRaise(String raiseid, String raiseType, String raiseField, List<Map<String, Object>> datas) {
        if (RAISE_TYPE_INCREMENT.equals(raiseType)) {
            String raiseValue = String.valueOf(datas.get(0).get(raiseField));
            //根据数值比较找出最大的增量值，并更新到数据库
            if (StringUtils.isNumeric(raiseValue)) {
                datas.stream().max(Comparator.comparingLong(m -> {
                    Object sort = m.get(raiseField);
                    if (sort == null) {
                        return 0L;
                    } else {
                        return Integer.parseInt(sort.toString());
                    }
                })).ifPresent(m -> jdbcTemplate.update("UPDATE p_fm_solr_indexe SET maxraise=?,log_time='1' WHERE id = ?", m.get(raiseField), raiseid));
                //根据日期比较找出最大的增量值，并更新到数据库
            } else if (DateFormaterUtil.isValidDate(raiseValue)) {
                datas.stream().max(Comparator.comparingLong(m -> {
                    Object sort = m.get(raiseField);
                    if (sort == null) {
                        return 0L;
                    } else {
                        return DateFormaterUtil.stringToDate(sort.toString()).getTime();
                    }
                })).ifPresent(m -> jdbcTemplate.update("UPDATE p_fm_solr_indexe SET maxraise=?,log_time='1' WHERE id = ?", m.get(raiseField), raiseid));
            } else {
                throw new PlatformException("不支持的增量类型");
            }
        } else {
            jdbcTemplate.update("UPDATE p_fm_solr_indexe SET log_time='1' WHERE id = ?", raiseid);
        }
    }

    /**
     * 过滤比最大值小的数据源
     *
     * @param raiseField 增量字段
     * @param max        最大值
     * @param datas      数据源
     * @param raiseValue 增量值
     * @return List
     */
    private List<Map<String, Object>> filter(String raiseField, String max, List<Map<String, Object>> datas, String raiseValue) {
        //根据数值比较大小进行过滤
        if (StringUtils.isNumeric(raiseValue)) {
            int num = Integer.parseInt(max);
            datas = datas.stream().filter(m -> {
                Object sort = m.get(raiseField);
                return sort != null && Integer.parseInt(sort.toString()) > num;
            }).collect(Collectors.toList());
            // 根据时间比较大小进行过滤
        } else if (DateFormaterUtil.isValidDate(raiseValue)) {
            Date date = DateFormaterUtil.stringToDate(max);
            datas = datas.stream().filter(m -> {
                Object sort = m.get(raiseField);
                return sort != null && date.before(DateFormaterUtil.stringToDate(sort.toString()));
            }).collect(Collectors.toList());

        } else {
            throw new PlatformException("不支持的增量字段类型");
        }
        return datas;
    }

    static public class IContentStream implements org.apache.solr.common.util.ContentStream {

        private AttachmentVO vo;
        private InputStream is;

        public IContentStream(AttachmentVO vo, InputStream is) {
            this.vo = vo;
            this.is = is;
        }

        @Override
        public String getName() {
            return vo.getFileName();
        }

        @Override
        public String getSourceInfo() {
            return vo.getStorageId();
        }

        @Override
        public String getContentType() {
            return vo.getContentType();
        }

        @Override
        public Long getSize() {
            return vo.getSize();
        }

        @Override
        public InputStream getStream() {
            return is;
        }

        @Override
        public Reader getReader() {
            return new InputStreamReader(is);
        }
    }

}