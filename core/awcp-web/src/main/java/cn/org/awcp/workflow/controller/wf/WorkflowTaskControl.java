package cn.org.awcp.workflow.controller.wf;

import BP.WF.Dev2Interface;
import BP.WF.GenerWorkFlowAttr;
import BP.WF.WFState;
import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.extend.workflow.WorkFlowExtend;
import cn.org.awcp.formdesigner.application.service.DocumentService;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.service.IDocumentService;
import cn.org.awcp.extend.formdesigner.DocumentUtils;
import cn.org.awcp.formdesigner.utils.PageBindUtil;
import cn.org.awcp.unit.core.domain.PunUserBaseInfo;
import cn.org.awcp.unit.message.PunNotification;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.common.I18nKey;
import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.exception.PlatformException;
import cn.org.awcp.venson.service.QueryService;
import cn.org.awcp.venson.util.BeanUtil;
import cn.org.awcp.workflow.controller.util.HttpRequestDeviceUtils;
import cn.org.awcp.workflow.service.IWorkFlowService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("workflow/wf")
public class WorkflowTaskControl extends BaseController {
    /**
     * 日志对象
     */
    protected static final Log logger = LogFactory.getLog(WorkflowTaskControl.class);

    @Resource
    private SzcloudJdbcTemplate jdbcTemplate;

    @Autowired
    private DocumentService documentServiceImpl;


    @Resource(name = "queryServiceImpl")
    private QueryService query;

    @Autowired
    private IDocumentService iDocumentServiceImpl;
    @Autowired
    private IWorkFlowService iWorkFlowServiceImpl;
    @Autowired
    private WorkFlowExtend workFlowExtend;

    /**
     * 由我创建
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "createByMe", method = RequestMethod.GET)
    public ReturnResult createByMe(@RequestParam(required = false, defaultValue = "1") int currentPage,
                                   @RequestParam(required = false, defaultValue = "15") int pageSize,
                                   @RequestParam(required = false) String FK_Flow, @RequestParam(required = false) String workItemName) {
        ReturnResult result = ReturnResult.get();
        PunUserBaseInfoVO user = ControllerHelper.getUser();
        Map<String, Object> data = query.getCreateByMeData(pageSize, (currentPage - 1) * pageSize, FK_Flow,
                workItemName, user.getUserIdCardNumber());
        result.setData(data.get(SC.DATA)).setTotal(data.get(SC.TOTAL));
        return result;
    }

    /**
     * 待办任务（个人任务）
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "listPersonalTasks", method = RequestMethod.GET)
    public ReturnResult listPersonalTasks(@RequestParam(required = false, defaultValue = "1") int currentPage,
                                          @RequestParam(required = false, defaultValue = "15") int pageSize,
                                          @RequestParam(required = false, value = "FK_Flow") String[] FK_Flow,
                                          @RequestParam(required = false, value = "excludeFlow") String[] excludeFlow,
                                          @RequestParam(required = false, value = "workItemName") String workItemName) {
        ReturnResult result = ReturnResult.get();
        PunUserBaseInfoVO user = ControllerHelper.getUser();
        Map<String, Object> data = query.getUntreatedData(pageSize, (currentPage - 1) * pageSize,
                FK_Flow, excludeFlow, workItemName, user.getUserIdCardNumber(), true);
        result.setData(data.get(SC.DATA)).setTotal(data.get(SC.TOTAL));
        return result;
    }

    /**
     * 查询待办件数量
     */
    @ResponseBody
    @RequestMapping(value = "getUntreatedCount", method = RequestMethod.GET)
    public ReturnResult getUntreatedCount(@RequestParam(required = false, value = "FK_Flow") String[] FK_Flow,
                                          @RequestParam(required = false, value = "excludeFlow") String[] excludeFlow,
                                          @RequestParam(required = false, value = "workItemName") String workItemName) {
        ReturnResult result = ReturnResult.get();
        // 获取当前登录用户
        PunUserBaseInfoVO user = ControllerHelper.getUser();
        Map<String, Object> data = query.getUntreatedData(Integer.MAX_VALUE, 1,
                FK_Flow, excludeFlow, workItemName,
                user.getUserIdCardNumber(), true);
        result.setData(data.get("count")).setStatus(StatusCode.SUCCESS);
        return result;
    }

    /**
     * 已处理
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "inDoingTasks", method = RequestMethod.GET)
    public ReturnResult inDoingTasks(@RequestParam(required = false, defaultValue = "1") int currentPage,
                                     @RequestParam(required = false, defaultValue = "15") int pageSize,
                                     @RequestParam(required = false) String FK_Flow, @RequestParam(required = false) String workItemName) {
        ReturnResult result = ReturnResult.get();
        // 获取当前登录用户
        PunUserBaseInfoVO user = ControllerHelper.getUser();
        Map<String, Object> data = query.getHandledData(pageSize, (currentPage - 1) * pageSize, FK_Flow, workItemName,
                user.getUserIdCardNumber());
        result.setData(data.get(SC.DATA)).setTotal(data.get(SC.TOTAL));
        return result;
    }

    /**
     * 归档
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "listHistoryTasks", method = RequestMethod.GET)
    public ReturnResult listHistoryTasks(@RequestParam(required = false, defaultValue = "1") int currentPage,
                                         @RequestParam(required = false, defaultValue = "15") int pageSize,
                                         @RequestParam(required = false) String FK_Flow, @RequestParam(required = false) String workItemName) {
        ReturnResult result = ReturnResult.get();
        // 获取当前登录用户
        PunUserBaseInfoVO user = ControllerHelper.getUser();
        Map<String, Object> data = query.getCompileData(pageSize, (currentPage - 1) * pageSize, FK_Flow, workItemName,
                user.getUserIdCardNumber());
        result.setData(data.get(SC.DATA)).setTotal(data.get(SC.TOTAL));
        return result;
    }


    /**
     * 打开任务
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("openTask")
    public void openTask(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String dynamicPageId;
        String entryId = request.getParameter("FK_Node");
        String flowTempleteId = request.getParameter("FK_Flow");
        dynamicPageId = request.getParameter("dynamicPageId");
        // 如果页面指定动态页面则以传来页面为准
        if (StringUtils.isBlank(dynamicPageId)) {
            // 如果是阅知节点则默认打开1301的绑定页面
            dynamicPageId = findDynamicpageByflowTempleteId(flowTempleteId, entryId);
            // 新增移动参数配置
            if (HttpRequestDeviceUtils.isMobileDevice(request)) {
                dynamicPageId = PageBindUtil.getInstance().getMPageIDByDefaultId(dynamicPageId);
            }
        }
        response.setContentType("text/html;");
        response.getWriter().write(iDocumentServiceImpl.view(dynamicPageId, request));

    }


    /**
     * 保存任务
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("excute")
    public Map execute(HttpServletRequest request, @RequestParam("FK_Flow") String FK_Flow, @RequestParam("FK_Node") String FK_Node,
                       @RequestParam(value = "dynamicPageId", required = false) String dynamicPageId,
                       @RequestParam(value = "WorkID", required = false) String WorkID,
                       @RequestParam(value = "FID", required = false, defaultValue = "0") String FID,
                       @RequestParam(value = "actType", required = false, defaultValue = "0") int actType,
                       @RequestParam(value = "masterDataSource", required = false) String masterDataSource) {
        // 返回信息
        String slectsUserIds = request.getParameter("slectsUserIds");
        Map<String, Object> resultMap = new HashMap<>(16);
        resultMap.put(GenerWorkFlowAttr.WFState, WFState.Blank);
        DocumentVO docVo;
        DocumentUtils utils = DocumentUtils.getIntance();
        try {
            // 如果不存在workId则是新建
            if (!StringUtils.isNumeric(WorkID)) {
                docVo = new DocumentVO();
                docVo.setUpdate(false);
            } else {
                docVo = documentServiceImpl.findDocByWorkItemId(FK_Flow, WorkID);
                docVo = BeanUtil.instance(docVo, DocumentVO.class);
                docVo.setWorkItemId(WorkID);
                docVo.setUpdate(true);
            }
            //设置参数
            docVo.setFlowTempleteId(FK_Flow);
            docVo.setEntryId(FK_Node);
            docVo.setWorkflowId(FK_Flow);
            docVo.setFid(FID);
            docVo.setActType(actType);
            docVo.setNextUser(slectsUserIds);
            //设置数据源值
            DynamicPageVO pageVo = iDocumentServiceImpl.setDataSorceFromRequestParameter(docVo, dynamicPageId, request);
            //开启事务
            jdbcTemplate.beginTranstaion();
            //执行服务端脚本
            Object eval = iDocumentServiceImpl.eval(request.getParameter("actId"), pageVo, docVo, request);
            // 如果前台没有设置主数据源，则尝试从组件里查找
            if (StringUtils.isBlank(masterDataSource)) {
                if (docVo.getListParams().size() >= 1) {
                    masterDataSource = docVo.getListParams().keySet().iterator().next();
                }
            }
            //设置数据源ID
            if (StringUtils.isNotBlank(docVo.getRecordId()) && StringUtils.isNotBlank(masterDataSource)) {
                if (StringUtils.isBlank(utils.getDataItem(masterDataSource, "ID"))) {
                    utils.setDataItem(masterDataSource, "ID", docVo.getRecordId());
                }
            }
            resultMap.put("eval", eval);
            workFlowExtend.beforeExecuteFlow(request, docVo);
            resultMap.putAll(iWorkFlowServiceImpl.execute(pageVo, docVo, masterDataSource));
            workFlowExtend.afterExecuteFlow(docVo, resultMap);
            //刷新自己桌面
            PunNotification.sendFlow(ControllerHelper.getUser().getUserIdCardNumber());
            jdbcTemplate.commit();
            resultMap.put("docId", docVo.getId());
            resultMap.put("WorkItemID", docVo.getWorkItemId());
            resultMap.put("EntryID", FK_Node);
            resultMap.put("dynamicPageId", dynamicPageId);
            resultMap.put("flowTempleteId", FK_Flow);
            resultMap.put("FID", FID);
            return resultMap;
        } catch (Exception e) {
            logger.info("ERROR", e);
            jdbcTemplate.rollback();
            resultMap.put("success", false);
            if (e instanceof PlatformException) {
                resultMap.put("message", e.getMessage());
            } else {
                resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_operation_failure));
            }
        }
        return resultMap;
    }


    @ResponseBody
    @RequestMapping(value = "/shiftWork")
    public ReturnResult shiftWork(Long userId) {
        ReturnResult result = ReturnResult.get();
        Map<String, Object> map = query.getUntreatedData(10000, 0, null, null, null,
                ControllerHelper.getUser().getUserIdCardNumber(), true);
        List<Map<String, Object>> ls = (List<Map<String, Object>>) map.get("data");
        int count = ls.size();
        PunUserBaseInfo punUserBaseInfoVO = PunUserBaseInfo.get(PunUserBaseInfo.class, userId);
        for (int i = 0; i < count; i++) {

            Dev2Interface.Node_Shift(ls.get(i).get("FK_Flow").toString(),
                    Integer.parseInt(ls.get(i).get("FK_Node").toString()),
                    Long.parseLong(ls.get(i).get("WorkID").toString()), Long.parseLong(ls.get(i).get("FID").toString()),
                    punUserBaseInfoVO.getUserIdCardNumber(), "");
        }
        result.setStatus(StatusCode.SUCCESS);
        return result;
    }


    private String findDynamicpageByflowTempleteId(String flowTempleteId, String entryId) {
        String sql = "SELECT a.id FROM p_fm_dynamicpage a WHERE WORKFLOW_NODE_INFO LIKE CONCAT('%workflowId\":\"',?,'%') AND WORKFLOW_NODE_INFO LIKE CONCAT('%id\":\"',?,'%')  ORDER BY created DESC";
        List<Map<String, Object>> vo = jdbcTemplate.queryForList(sql, flowTempleteId, entryId);
        if (vo.size() == 1) {
            return String.valueOf(vo.get(0).get("id"));
        } else if (vo.size() > 0) {
            sql = "SELECT a.id FROM p_fm_dynamicpage a LEFT JOIN p_un_page_binding b ON a.id=b.PAGEID_PC "
                    + " WHERE b.PAGEID_PC IS NOT NULL AND WORKFLOW_NODE_INFO LIKE CONCAT('%workflowId\":\"',?,'%') AND WORKFLOW_NODE_INFO LIKE CONCAT('%id\":\"',?,'%') "
                    + " ORDER BY created DESC ";
            vo = jdbcTemplate.queryForList(sql, flowTempleteId, entryId);
            if (!vo.isEmpty()) {
                return String.valueOf(vo.get(0).get("id"));
            }
        }
        return null;
    }

}
