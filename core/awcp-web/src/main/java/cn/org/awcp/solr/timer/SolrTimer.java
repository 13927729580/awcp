package cn.org.awcp.solr.timer;

import cn.org.awcp.solr.service.SolrService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * solr同步定时器
 *
 * @author quxiang
 * @version 20180313
 */
@Lazy(false)
@Component
@EnableScheduling
public class SolrTimer {
    private static final Log logger = LogFactory.getLog(SolrTimer.class);
    private static final int TIME = 23;

    @Autowired
    SolrService solrService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 一小时执行一次
     */
    @Scheduled(cron = "0 0 *  * * ? ")
    public void execute() {
        try {
            // 执行任务逻辑
            List<Map<String, Object>> datas = jdbcTemplate.queryForList(
                    "select ID,readtype,readpath,raisetype,raisestring,indexfield,maxraise,time from p_fm_solr_indexe where log_time='0'");
            // 当前时间的小时
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            int time = Integer.valueOf(sdf.format(date));
            for (Map<String, Object> data : datas) {
                String h = (String) data.get("time");
                if(StringUtils.isNumeric(h)){
                    // 判断时间是否过了
                    if (time - Integer.valueOf(h) >= 0) {
                        String raiseid = (String) data.get("ID");
                        String readpath = (String) data.get("readpath");
                        String raisetype = (String) data.get("raisetype");
                        String raisestring = (String) data.get("raisestring");
                        String indexfield = (String) data.get("indexfield");
                        String maxraise = (String) data.get("maxraise");
                        //判断执行类型
                        if (SolrService.SOLR_TYPE_DATA.equals(data.get("readtype"))) {
                            solrService.indexData(raiseid, readpath, raisetype, raisestring,
                                    indexfield, maxraise);
                        } else {
                            solrService.indexDocument(raiseid, readpath, raisetype, raisestring,
                                    indexfield, maxraise);
                        }
                    }
                }
            }
            // 23点重置增量更新log
            if (time == TIME) {
                jdbcTemplate.update("UPDATE p_fm_solr_indexe SET log_time = ? ", "0");
            }
        } catch (Exception e) {
            logger.info("ERROR", e);
        }
    }

}
