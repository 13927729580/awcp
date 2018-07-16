package cn.org.awcp.extend.workflow;

import BP.Port.Emp;
import BP.WF.*;
import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.extend.formdesigner.DocumentUtils;
import cn.org.awcp.venson.api.APIService;
import cn.org.awcp.venson.api.PFMAPI;
import cn.org.awcp.venson.common.I18nKey;
import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.controller.base.ControllerContext;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.util.RedisUtil;
import cn.org.awcp.workflow.service.FlowExecuteType;
import cn.org.awcp.workflow.service.IWorkFlowService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 流程扩展类，流程执行前，执行后可以增加新功能
 *
 * @author venson
 * @version 20180307
 */
@Component("workFlowExtend")
public class WorkFlowExtend {

    @Resource
    private SzcloudJdbcTemplate jdbcTemplate;
    @Autowired
    private IWorkFlowService iWorkFlowServiceImpl;
    @Resource(name = "apiService")
    private APIService apiService;
    /**
     * 流程预设编号
     */
    public static final String FLOW = "005";


    /**
     * 流程执行前执行的方法
     *
     * @param request http请求
     * @param docVo   文档类
     * @throws ScriptException 脚本引擎异常
     */
    public void beforeExecuteFlow(HttpServletRequest request, DocumentVO docVo) throws ScriptException {
        //执行流程前,查看是否有存在流程执行前事件
        PFMAPI before = PFMAPI.get("workFlowExecuteBefore");
        if (before != null) {
            ScriptEngine engine = apiService.getEngine();
            engine.eval(before.getAPISQL());
        }
        //005流程预设
        if (WorkFlowExtend.FLOW.equals(docVo.getFlowTempleteId())
                && FlowExecuteType.UNDO.getValue() != docVo.getActType()) {
            setWorkTitle(docVo);
            presetWork(request, docVo);
            presetCC(request, docVo);


        }
    }

    private void setWorkTitle(DocumentVO docVo) {
        if (!docVo.isUpdate()&&docVo.getListParams().size() >= 1) {
            boolean isCH = ControllerHelper.getLang() == Locale.SIMPLIFIED_CHINESE;
            String field = isCH ? "title" : "enTitle";
            String sql = "select " + field + " from dd_apps where dynamicPageId=?";
            Object value = DocumentUtils.getIntance().excuteQueryForObject(sql, docVo.getDynamicPageId());
            String title = ControllerHelper.getUser().getName() + (isCH ? "的" : "'s ")
                    + (value instanceof String ? (String) value : ControllerHelper.getMessage("wf_approval"));
            DocumentUtils.getIntance().setDataItem(docVo.getListParams().keySet().iterator().next(), "title", title);
        }
    }

    /**
     * 流程执行成功后执行的方法
     *
     * @param docVo     文档类
     * @param resultMap 返回的结果集
     * @throws ScriptException 脚本引擎异常
     */
    public void afterExecuteFlow(DocumentVO docVo, Map<String, Object> resultMap) throws ScriptException {
        Boolean result = (Boolean) resultMap.get("success");
        if (result) {
            PFMAPI after = PFMAPI.get("workFlowExecuteAfter");
            //执行流程后,查看是否有存在流程执行后事件
            if (after != null) {
                ScriptEngine engine = apiService.getEngine();
                engine.eval(after.getAPISQL());
            }
            if (WorkFlowExtend.FLOW.equals(docVo.getFlowTempleteId())) {
                // 执行钉钉消息推送
                pushByDD(docVo);
                addPresetLogs(docVo, resultMap);
            } else {
                // 执行消息推送
                iWorkFlowServiceImpl.addPush(docVo, resultMap);
                // 流程日志
                iWorkFlowServiceImpl.addCommonLogs(docVo, resultMap);
                iWorkFlowServiceImpl.saveComment(docVo, resultMap);
            }
        }
    }

    /**
     * 抄送预设
     *
     * @param request http请求
     * @param doc     文档类
     */
    public void presetCC(HttpServletRequest request, DocumentVO doc) {
        // 查看是否属于预置流程并且是新建流程
        if (doc.getWorkItemId()==null) {
            String CC_slectsUserIds = request.getParameter("CC_slectsUserIds");
            Flow currFlow = new Flow(FLOW);
            Work currWK = currFlow.NewWork();
            String workItemId = currWK.getOID() + "";
            // 如果没有选择抄送人则取数据库查找
            if (StringUtils.isBlank(CC_slectsUserIds)) {
                // 去数据库取默认预置人员
                String sql = "select JSON from p_fm_cc_flow where PAGE_ID=?";
                CC_slectsUserIds = (String) DocumentUtils.getIntance().excuteQueryForObject(sql, doc.getDynamicPageId());
            }
            // 如果存在抄送人
            if (StringUtils.isNotBlank(CC_slectsUserIds)) {
                JSONArray jsonArr = JSON.parseArray(CC_slectsUserIds);
                // 插入抄送记录
                int len = jsonArr.size();
                for (int i = 0; i < len; i++) {
                    Emp emp = new Emp(jsonArr.getString(i));
                    Dev2Interface.Node_CC(FLOW, 999, Long.parseLong(workItemId), emp.getNo(), emp.getName(),
                            null, null, null, 0);
                }
            }
        }
    }

    /**
     * 流程预设
     *
     * @param request http请求
     * @param doc     文档类
     */
    public void presetWork(HttpServletRequest request, DocumentVO doc) {
        String currentUserID = ControllerHelper.getUser().getUserIdCardNumber();
        String slectsUserIds = doc.getNextUser();
        // 流程新建立
        if (doc.getWorkItemId()==null) {
            // 查看是否有选择流程人员
            if (StringUtils.isBlank(slectsUserIds)) {
                // 去数据库取默认预置人员
                String sql = "select JSON from p_fm_flow where PAGE_ID=?";
                slectsUserIds = (String) DocumentUtils.getIntance().excuteQueryForObject(sql, doc.getDynamicPageId());
                if (slectsUserIds == null) {
                    return;
                }
            }
            Flow currFlow = new Flow(FLOW);
            Work currWK = currFlow.NewWork();
            String workItemId = currWK.getOID() + "";
            JSONArray jsonArr = JSON.parseArray(slectsUserIds);
            // 将人员数据保存
            String sql = "select dynamicPageId from dd_apps where dynamicPageId=? or pcDynamicPageId=?";
            String dynamicPageId = jdbcTemplate.queryForObject(sql,String.class, doc.getDynamicPageId(),doc.getDynamicPageId());
            sql = " insert into p_fm_work(JSON,CREATOR,WORK_ID,FLOW_ID,NODE_ID,CURRENT_NODE,PAGE_ID) values(?,?,?,?,?,?,?)";
            this.jdbcTemplate.update(sql, slectsUserIds, currentUserID, workItemId, FLOW, doc.getEntryId(), 0, dynamicPageId);
            // 将人员记录到流程日志表中
            final String insertSql = "insert into p_fm_work_logs(WORK_ID,PAGE_ID,CREATOR,CURRENT_NODE) values(?,?,?,?)";
            this.jdbcTemplate.update(insertSql, workItemId, doc.getDynamicPageId(), currentUserID, 0);
            int len = jsonArr.size();
            for (int i = 0; i < len; i++) {
                String v = jsonArr.getString(i);
                String[] arr = v.split(",");
                for (String str : arr) {
                    if (StringUtils.isNotBlank(str)) {
                        this.jdbcTemplate.update(insertSql, workItemId, doc.getDynamicPageId(), str, i);
                    }
                }
            }
            // 取出第一步人员
            slectsUserIds = jsonArr.getString(0);
            // 设置下一节点人员
            doc.setNextUser(slectsUserIds);
            // 第一步走转发
            doc.setActType(2011);
            // 设置当前节点
            request.setAttribute("CURRENT_NODE", 0);
        } else {
            Integer workItemId = doc.getWorkItemId();
            // 去数据库取默认预置人员
            String sql = "select ID,JSON,CURRENT_NODE from p_fm_work where PAGE_ID=? and WORK_ID=? limit 0,1";
            Map<String, Object> map = this.jdbcTemplate.queryForMap(sql, doc.getDynamicPageId(), workItemId);
            int CURRENT_NODE = (int) map.get("CURRENT_NODE");
            if(doc.getActType()!=FlowExecuteType.REJECT.getValue()){
                // 如果存在选择的人员则属于加签，否则是属于传阅
                if (StringUtils.isNotBlank(slectsUserIds)) {
                    // 如果是加签，则将加签人员添加到人员记录表中(未实现)sql = "update p_fm_work set JSON=? where ID=?";
                    // 将加签人保存到流程日志表
                    final String insertSql = "insert into p_fm_work_logs(WORK_ID,PAGE_ID,CREATOR,CURRENT_NODE) values(?,?,?,?)";
                    String[] arr = slectsUserIds.split(",");
                    for (String str : arr) {
                        if (StringUtils.isNotBlank(str)) {
                            // 如果当前节点不存在相同处理人则插入
                            if (this.jdbcTemplate.queryForObject(
                                    "select count(1) from p_fm_work_logs where WORK_ID=? and CREATOR=? and CURRENT_NODE=? and send_time is null",
                                    Integer.class, workItemId, str, CURRENT_NODE) == 0) {
                                this.jdbcTemplate.update(insertSql, workItemId, doc.getDynamicPageId(), str, CURRENT_NODE);
                            }
                        }
                    }
                    // 设置下一节点人员
                    doc.setNextUser(slectsUserIds);
                    // 将流程动作类型改为加签
                    doc.setActType(2024);
                    // 状态移交
                } else {
                    String text = map.get("JSON") + "";
                    JSONArray jsonArr = JSON.parseArray(text);
                    int nextNode = CURRENT_NODE + 1;
                    int len = jsonArr.size();
                    // 查询是否还有下一节点人员
                    if (nextNode < len) {
                        // 查询当前节点所有人员是否已经处理完
                        sql = "select count(1) from wf_generworkerlist where workid=? and ispass=0 and fk_emp<>?";
                        int count = this.jdbcTemplate.queryForObject(sql, Integer.class, workItemId, currentUserID);
                        if (count == 0) {
                            Object ID = map.get("ID");
                            // 当前节点已经处理完就去获取定制的下一节点接收人
                            slectsUserIds = jsonArr.getString(nextNode);
                            // 更新当前节点到数据库
                            sql = "update p_fm_work set CURRENT_NODE=? where ID=?";
                            this.jdbcTemplate.update(sql, nextNode, ID);
                            // 设置下一节点人员
                            doc.setNextUser(slectsUserIds);
                            // 将流程动作类型改为加签
                            doc.setActType(2024);
                        } else {
                            // 将流程动作类型改为传阅
                            doc.setActType(2019);
                        }
                    } else {
                        // 将流程动作类型改为传阅
                        doc.setActType(2019);
                    }
                }
            }
            // 设置当前节点
            request.setAttribute("CURRENT_NODE", CURRENT_NODE);
            // 状态设置为同意
        }
    }

    /**
     * 增加钉钉预设流程的流程日志
     *
     * @param docVo     文档类
     * @param resultMap 结果集
     */
    public void addPresetLogs(DocumentVO docVo, Map<String, Object> resultMap) {
        String userId = ControllerHelper.getUser().getUserIdCardNumber();
        HttpServletRequest request = ControllerContext.getRequest();
        String content = request.getParameter("work_logs_content");
        Object CURRENT_NODE = request.getAttribute("CURRENT_NODE");
        int currentNode = 0;
        if (CURRENT_NODE != null) {
            // 先从流程预设中获取
            currentNode = (int) CURRENT_NODE;
        }
        // 默认意见为同意。
        if (StringUtils.isBlank(content)) {
            content = ControllerHelper.getMessage(I18nKey.wfState_agree);
        }
        WFState workStatus = (WFState) resultMap.get(GenerWorkFlowAttr.WFState);
        String today = DocumentUtils.getIntance().today();
        try {
            // 取出发送时间为空，并且创建时间最早的那条记录ID
            Integer id = this.jdbcTemplate.queryForObject(
                    "select ID from p_fm_work_logs where WORK_ID=? and creator=? and CURRENT_NODE=? and send_time is null order by create_time limit 0,1",
                    Integer.class, docVo.getWorkItemId(), userId, currentNode);
            // 如果记录表已经存在该成员的日志则直接更新,没有则插入
            String sql = "update p_fm_work_logs set send_time=?, content=?,state=? where id=?";
            this.jdbcTemplate.update(sql, today, content, workStatus.getValue(), id);
        } catch (DataAccessException e) {
            docVo.setEntryId(currentNode);
            iWorkFlowServiceImpl.addCommonLogs(docVo, resultMap);
        }
    }

    /**
     * 钉钉消息推送
     *
     * @param vo 文档类
     */
    public void pushByDD(DocumentVO vo) {
        Integer workItemId = vo.getWorkItemId();
        String fid = vo.getFid();
        if (fid != null && !"0".equals(fid)) {
            workItemId = Integer.valueOf(fid);
        }
        String sql;
        String wf_approval_result = ControllerHelper.getMessage(I18nKey.wf_approval_result);
        String wf_approval_reject = ControllerHelper.getMessage(I18nKey.wf_approval_reject);
        sql = "select starter FK_EMP,FK_Flow,workid,FK_Node,RDT,FID,concat(title,(CASE wfstate WHEN 13 THEN '"
                + wf_approval_reject + "' ELSE  '" + wf_approval_result
                + "' END)) title from wf_generworkflow where wfstate<>12 and workid=? and wfsta=1";
        List<Map<String, Object>> emps = this.jdbcTemplate.queryForList(sql, workItemId);
        if (!emps.isEmpty()) {
            if ((emps.get(0).get("title") + "").contains(wf_approval_result)) {
                sql = "select a.CCTo FK_EMP,b.FK_Flow,a.workid,a.FK_Node,a.RDT,a.FID,concat(b.title,'"
                        + ControllerHelper.getMessage(I18nKey.wf_approval_CC)
                        + "') title from wf_cclist a left join wf_generworkflow b on a.workid=b.workid where a.workid=?";
                emps.addAll(this.jdbcTemplate.queryForList(sql, workItemId));
            }
        } else {
            sql = "SELECT DISTINCT a.FID,a.FK_Node,b.FK_Flow,a.workid,a.FK_EMP,a.RDT,b.title  FROM wf_generworkerlist a left join "
                    + "wf_generworkflow b on a.workid=b.workid WHERE IsPass=0 AND (a.workid=? or a.FID=?) and b.FK_Node=a.FK_Node";
            emps = jdbcTemplate.queryForList(sql, workItemId, workItemId);
        }
        Map<String, String> content = new LinkedHashMap<>();
        String dynamicPageId = vo.getDynamicPageId();
        String recordId = vo.getRecordId();
        String agentId = "";
        boolean isEnglish = ControllerHelper.getLang() == Locale.ENGLISH;
        if (isEnglish) {
            sql = "select enSql as contentSql,agentId from dd_apps where dynamicPageId=? or pcDynamicPageId=?";
        } else {
            sql = "select chSql as contentSql,agentId from dd_apps where dynamicPageId=? or pcDynamicPageId=?";
        }
        try {
            Map<String, Object> temp = jdbcTemplate.queryForMap(sql, dynamicPageId, dynamicPageId);
            String contentSql = temp.get("contentSql") + "";
            agentId =(String) temp.get("agentId");
            if (StringUtils.isNotBlank(contentSql)) {
                contentSql += " where ID=?";
                Map<String, Object> result = jdbcTemplate.queryForMap(contentSql, recordId);
                for (String key : result.keySet()) {
                    String val = result.get(key) + "";
                    if (StringUtils.isNotBlank(val)) {
                        if (isEnglish) {
                            String newKey = key.replaceAll("_", " ");
                            content.put(newKey, val);
                        } else {
                            content.put(key, val);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String baseUrl = ControllerHelper.getBasePath();
        String gotoUrl;
        for (Map<String, Object> map : emps) {
            String user = map.get("FK_EMP") + "";
            gotoUrl = baseUrl + "dingding/wf/openTask.do?FK_Flow=" + vo.getFlowTempleteId() + "&WorkID="
                    + map.get("workid") + "&FID=" + map.get("FID") + "&FK_Node=" + map.get("FK_Node")
                    + "&dynamicPageId=" + vo.getDynamicPageId() + "&RECORD_ID=" + vo.getRecordId() +
                    "&uid=" + user + "&key=" + RedisUtil.getInstance().get(SC.SECRET_KEY+user);
            content.put("validRepeat", map.get("RDT") + "");
            String title = (String) map.get("title");
            DocumentUtils.getIntance().sendMessage(gotoUrl, "0", content, title, user, agentId, null, null);
        }
    }

}
