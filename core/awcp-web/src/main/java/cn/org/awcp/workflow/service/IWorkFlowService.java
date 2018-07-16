package cn.org.awcp.workflow.service;

import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;

import java.util.Map;

/**
 * 流程执行类
 *
 * @author Veonson
 * @version 20180301
 */
public interface IWorkFlowService {

    /**
     *  执行流程
     * @param pageVo  DynamicPageVO
     * @param docVo   DocumentVo
     * @param masterDataSource 元数据，可以为空
     * @return Map
     * @see FlowExecuteType
     */
    Map<String, Object> execute(DynamicPageVO pageVo, DocumentVO docVo, String masterDataSource);

    /**
     * 执行流程
     * @param pageVo  DynamicPageVO
     * @param docVo   DocumentVo
     * @return Map
     * @see FlowExecuteType
     */
    Map<String, Object> execute(DynamicPageVO pageVo, DocumentVO docVo);



    /**
     * 增加普通流程的流程日志
     *
     * @param docVo 文档
     * @param resultMap 结果集
     */
    void addCommonLogs(DocumentVO docVo, Map<String, Object> resultMap);

    /**
     * 增加消息推送
     * @param docVo 文档
     * @param resultMap 结果集
     */
    void addPush(DocumentVO docVo, Map<String, Object> resultMap);

    /**
     * 增加普通流程的流程日志
     *
     * @param doc 文档
     * @param resultMap 结果集
     */
    void saveComment(DocumentVO doc, Map<String, Object> resultMap);
}
