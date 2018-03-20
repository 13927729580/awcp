package cn.org.awcp.workflow.service;

import BP.WF.*;
import BP.Web.WebUser;
import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.extend.formdesigner.DocumentUtils;
import cn.org.awcp.unit.message.PunNotification;
import cn.org.awcp.venson.common.I18nKey;
import cn.org.awcp.venson.controller.base.ControllerContext;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.exception.PlatformException;
import cn.org.awcp.venson.service.QueryService;
import cn.org.awcp.workflow.controller.wf.JFlowAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程执行类
 *
 * @author Veonson
 * @version 20180301
 */
@Service("iWorkFlowServiceImpl")
public class IWorkFlowServiceImpl implements IWorkFlowService {


    @Resource
    private SzcloudJdbcTemplate jdbcTemplate;

    @Resource(name = "queryServiceImpl")
    private QueryService query;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> execute(DynamicPageVO pageVo, DocumentVO docVo, String masterDataSource) {
        String userIdCardNumber = ControllerHelper.getUser().getUserIdCardNumber();
        // 如果不是新建流程则判断是否有权限处理
        int actType = docVo.getActType();
        if (StringUtils.isNumeric(docVo.getWorkItemId())) {
            if (FlowExecuteType.UNDO.getValue() != actType && FlowExecuteType.RETRACEMENT.getValue() != actType) {
                if (!Dev2Interface.Flow_IsCanDoCurrentWork(docVo.getFlowTempleteId(), Integer.parseInt(docVo.getEntryId()),
                        Long.parseLong(docVo.getWorkItemId()), userIdCardNumber)) {
                    throw new PlatformException(ControllerHelper.getMessage("wf_not_can_do"));
                }
            }
        } else {
            Flow currFlow = new Flow(docVo.getFlowTempleteId());
            Work currWK = currFlow.NewWork();
            docVo.setWorkItemId(currWK.getOID() + "");
        }
        Map<String, Object> resultMap = new HashMap<>(5);
        //执行流程
        switch (FlowExecuteType.forValue(actType)) {
            // 流程回撤
            case RETRACEMENT:
                JFlowAdapter.Flow_DoUnSend(resultMap, docVo.getFlowTempleteId(), docVo.getWorkItemId());
                break;
            case SAVE:
                JFlowAdapter.Node_SaveWork(masterDataSource, resultMap, pageVo, docVo);
                break;
            // 流程流转(发送)
            case SEND:
                // 流程转发
            case SKIP:
                if (StringUtils.isNotBlank(docVo.getNextUser())) {
                    JFlowAdapter.Node_SendWork(masterDataSource, resultMap, pageVo, docVo, docVo.getNextUser());
                } else {
                    throw new PlatformException(I18nKey.wf_select_least_person);
                }
                break;
            // 流程传阅
            case AGREE:
                JFlowAdapter.Node_SendWork(masterDataSource, resultMap, pageVo, docVo, null);
                break;
            // 流程撤销
            case UNDO:
                JFlowAdapter.Flow_DoFlowOverByCoercion(resultMap, docVo.getFlowTempleteId(), Integer.valueOf(docVo.getEntryId()),
                        Long.valueOf(docVo.getWorkItemId()), Long.valueOf(docVo.getFid()), WFState.Undo);
                break;
            // 流程拒绝
            case REJECT:
                JFlowAdapter.Flow_DoFlowOverByCoercion(resultMap, docVo.getFlowTempleteId(), Integer.valueOf(docVo.getEntryId()),
                        Long.valueOf(docVo.getWorkItemId()), Long.valueOf(docVo.getFid()), WFState.Reject);
                break;
            // 流程办结
            case SHOTDOWN:
                JFlowAdapter.Flow_DoFlowOverByCoercion(resultMap, docVo.getFlowTempleteId(), Integer.valueOf(docVo.getEntryId()),
                        Long.valueOf(docVo.getWorkItemId()), Long.valueOf(docVo.getFid()), "");
                break;
            // 流程退回
            case RETURN:
                HttpServletRequest request = ControllerContext.getRequest();
                boolean isBackToThisNode = !"N".equals(request.getParameter("isBackToThisNode"));
                JFlowAdapter.Flow_returnWork(resultMap, Long.valueOf(docVo.getFid()), "", WebUser.getNo(), request.getParameter("toNode"),
                        masterDataSource, pageVo, docVo, isBackToThisNode);
                break;
            // 加签
            case COUNTERSIGN:
                // 根据选中的人员ID，组装信息
                if (StringUtils.isNotBlank(docVo.getNextUser())) {
                    JFlowAdapter.Node_AskFor(masterDataSource, resultMap, pageVo, docVo, docVo.getNextUser());
                } else {
                    throw new PlatformException(I18nKey.wf_select_least_person);
                }
                break;
            default:
                break;
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> execute(DynamicPageVO pageVo, DocumentVO docVo) {
        return execute(pageVo, docVo, null);
    }


    @Override
    public void addCommonLogs(DocumentVO docVo, Map<String, Object> resultMap) {
        String userId = ControllerHelper.getUser().getUserIdCardNumber();
        HttpServletRequest request = ControllerContext.getRequest();
        String sql = "insert into p_fm_work_logs(CONTENT,WORK_ID,PAGE_ID,CREATOR,SEND_TIME,CURRENT_NODE,state) values(?,?,?,?,?,?,?)";
        String content = request.getParameter("work_logs_content");
        WFState workStatus = (WFState) resultMap.get(GenerWorkFlowAttr.WFState);
        //意见为空则添加默认意见
        if (StringUtils.isBlank(content)) {
            //撤销
            if (workStatus == WFState.Undo) {
                content = ControllerHelper.getMessage(I18nKey.wfState_cancel);
                //拒绝
            } else if (workStatus == WFState.Reject) {
                content = ControllerHelper.getMessage(I18nKey.wfState_reject);
                //退回
            } else if (workStatus == WFState.ReturnSta) {
                content = ControllerHelper.getMessage(I18nKey.wfState_return);
                //同意
            } else {
                content = ControllerHelper.getMessage(I18nKey.wfState_agree);
            }
        }
        String today = DocumentUtils.getIntance().today();
        jdbcTemplate.update(sql, content, docVo.getWorkItemId(), docVo.getDynamicPageId(), userId, today,
                docVo.getEntryId(), workStatus.getValue());
    }


    @Override
    public void addPush(DocumentVO docVo, Map<String, Object> resultMap) {
        Object workStatus = resultMap.get(GenerWorkFlowAttr.WFState);
        String workItemId = docVo.getWorkItemId();
        //进行中
        if (workStatus == WFState.Runing || workStatus == WFState.ReturnSta) {
            Object todoEmps = resultMap.get(GenerWorkFlowAttr.TodoEmps);
            if (todoEmps != null) {
                //找出下一步执行人并推送
                String[] nextExecutor = ((String) todoEmps).split(",");
                PunNotification.sendFlow(nextExecutor);
            }
        } else if (workStatus == WFState.Complete || workStatus == WFState.Reject) {
            String baseUrl = ControllerHelper.getBasePath();
            //判断是通过还是拒绝
            String message = workStatus == WFState.Complete ? I18nKey.wf_approval_result : I18nKey.wf_approval_reject;
            Map<String, Object> map = query.getStarter(docVo.getWorkItemId());
            String title = map.get("title") + "-" + ControllerHelper.getMessage(message);
            String url = baseUrl + "workflow/wf/openTask.do?IsRead=1&FK_Flow=" + docVo.getFlowTempleteId() + "&WorkID="
                    + workItemId + "&FID=" + docVo.getFid() + "&FK_Node=" + docVo.getEntryId()
                    + "&dynamicPageId=" + docVo.getDynamicPageId() + "&id=" + docVo.getRecordId();
            //消息推送
            DocumentUtils.getIntance().pushNotify(title, "", PunNotification.KEY_NOTIFY, url, (String) map.get("starter"), PunNotification.SOCKET);
        }
    }


}
