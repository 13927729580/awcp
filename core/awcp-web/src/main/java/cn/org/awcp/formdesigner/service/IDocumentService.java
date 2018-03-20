package cn.org.awcp.formdesigner.service;

import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

/**
 * 文档操作类
 * @author venson
 * @version 20180301
 */
public interface IDocumentService {

    /**
     * 动态页面渲染展示
     * @param dynamicPageId 动态页面ID
     * @param request 请求
     * @return 返回渲染的html
     * @throws ScriptException 执行脚本异常
     */
    String view(String dynamicPageId,HttpServletRequest request)throws ScriptException;

    /**
     * 执行服务端脚本
     * @param actId 动作ID
     * @param dynamicPageId 动态页面ID
     * @param docVo 文档类
     * @param request 请求
     * @return 返回执行结果
     * @throws ScriptException 执行脚本异常
     */
    Object execute(String actId,String dynamicPageId,DocumentVO docVo, HttpServletRequest request) throws ScriptException;

    /**
     * 从请求中获取参数并设置到文档中的数据源
     * @param docVo 文档类
     * @param dynamicPageId 动作页面ID
     * @param request 请求
     * @return 返回动态页面
     */
    DynamicPageVO setDataSorceFromRequestParameter(DocumentVO docVo,String dynamicPageId, HttpServletRequest request);

    /**
     * 执行动作脚本
     * @param actId 动作ID
     * @param pageVO 动态页面类
     * @param docVo 文档类
     * @param request 请求
     * @return 返回执行结果
     * @throws ScriptException 执行脚本异常
     */
    Object eval(String actId, DynamicPageVO pageVO, DocumentVO docVo, HttpServletRequest request) throws ScriptException;

}
