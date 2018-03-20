package cn.org.awcp.solr.controller;

import cn.org.awcp.solr.service.SolrService;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("solr")
public class SolrController {

    @Autowired
    SolrService solrService;

    @RequestMapping(value = "query/{keyword}", method = RequestMethod.POST)
    public ReturnResult query(@PathVariable("keyword") String keyword, String type) {
        ReturnResult result=ReturnResult.get();
        //执行查询
        if (StringUtils.isNotBlank(keyword)) {
            QueryResponse res = solrService.query(keyword, type);
            if(res!=null){
                //取查询结果
                return result.setStatus(StatusCode.SUCCESS).setData(res.getResults());
            }else{
                return result.setStatus(StatusCode.FAIL.setMessage("无法获取，查询失败"));
            }
        } else {
            return result.setStatus(StatusCode.PARAMETER_ERROR.setMessage("查询关键字不能为空"));
        }
    }

    @RequestMapping(value = "clean/{id}", method = RequestMethod.POST)
    public ReturnResult clean(@PathVariable("id")String id) {
        ReturnResult result = ReturnResult.get();
        //批量清空索引
        solrService.solrDeleteByQuery("clean_id:" + id);
        return result.setStatus(StatusCode.SUCCESS);
    }

    @RequestMapping(value = "index/{id}", method = RequestMethod.POST)
    public ReturnResult index(@PathVariable("id")String id) {
        ReturnResult result = ReturnResult.get();
        Map<String, Object> map = solrService.getById(id);
        if(map==null){
            return result.setStatus(StatusCode.PARAMETER_ERROR.setMessage("ID未找到"));
        }
        String readtype =(String) map.get("readtype");
        String raisetype =(String) map.get("raisetype");
        String readpath =(String) map.get("readpath");
        String raisestring =(String) map.get("raisestring");
        String indexfield =(String) map.get("indexfield");
        String maxraise =(String) map.get("maxraise");
        // 索引数据
        if (SolrService.SOLR_TYPE_DATA.equals(readtype)) {
            solrService.indexData(id, readpath, raisetype,raisestring, indexfield, maxraise);
        } else if (SolrService.SOLR_TYPE_DOCUMENT.equals(readtype)) {
            solrService.indexDocument(id, readpath, raisetype, raisestring, indexfield, maxraise);
        } else {
            result.setStatus(StatusCode.FAIL.setMessage("不支持的索引类型"));
        }
        return result.setStatus(StatusCode.SUCCESS);
    }

}