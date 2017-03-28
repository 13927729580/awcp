package org.szcloud.framework.formdesigner.application.service;

import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.formdesigner.application.vo.DocumentVO;
import org.szcloud.framework.formdesigner.application.vo.DynamicPageVO;
import org.szcloud.framework.formdesigner.core.domain.design.context.data.DataDefine;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

public interface DocumentService {
	
	/**
	 * 仅保存或更新文档
	 * @param vo
	 * @return
	 */
	 String save(DocumentVO vo);
	/**
	 * 删除文档
	 * @param vo
	 * @return
	 */
	 Boolean delete(DocumentVO vo);
	
	/**
	 * 
	 * 根据ID在数据库中查找出来
	 * 
	 * @param id
	 * @return
	 */
	 DocumentVO findById(String id);
	 
	 DocumentVO findDocByWorkItemId(String flowTemplateId,String workItemId);
	/**
	 * 获取动态页面的模版内容
	 * @param pageVO
	 * @return
	 */
	
	 String getTemplateString(DynamicPageVO pageVO);
	 
	 /**
	  * 查询发布后页面的所有组件ID
	 * @param pageVO
	 * @return
	 */
	public String getStoreString(DynamicPageVO pageVO);
	
	
	/**
	 * 删除元数据table 表中的一条数据
	 */
	 Boolean deleteModelData(DynamicPageVO page,String recordId);
	/**
	 * 根据传递的文档和动态页面，即数据和模版，展示模版和数据
	 * 
	 * @param pageVO
	 * @param docVo
	 * @return
	 */
	// String view(DynamicPageVO pageVO, DocumentVO docVo, Object... objects)  throws Exception ;
	
	
	/**
	 * 处理request带来的数据
	 * @param vo
	 * @return
	 */
	 DocumentVO processParams(DocumentVO vo);
	
	
	 /**
	  *  初始化文档数据
	  * 读取数据库中数据放入DocumentVO.ListParams中
	  * @param currentPage
	  * @param pageSize
	  * @param docVo
	  * @param pageVo
	  * @return
	  */
	// Map<String,List<Map<String,String>>> initDocumentData(Integer currentPage, Integer pageSize,Map<String,String>params, DynamicPageVO pageVo);
	 /**
	  *  初始化文档数据
	  * 读取数据库中数据放入DocumentVO.ListParams中
	  * @param currentPage
	  * @param pageSize
	  * @param docVo
	  * @param pageVo
	  * @return
	  */
	 public Map<String,List<Map<String,String>>> initDocumentData(Integer currentPage, Integer pageSize, DocumentVO docVo, ScriptEngine engine, DynamicPageVO pageVo);
	 public Map<String,List<Map<String,String>>> initDocumentDataFlow(Integer currentPage, Integer pageSize, DocumentVO docVo, ScriptEngine engine, DynamicPageVO pageVo);

	 String print(String docId);
	 PageList<DocumentVO> selectPagedByExample(BaseExample baseExample,int currentPage, int pageSize, String sortString);
	
	public void excuteUpdate(String sql);
	
	public Map<String,Object> excuteQuery(String sql);
	 
	public void excuteUpdate(String sql,String dsName);
	
	public Map excuteQuery(String sql,String dsName);
	
	public List<Map<String,Object>> excuteQueryForList(String sql,String dsName);
	/**
	 * 条件删除 document 
	 * @param example
	 * @return
	 */
	boolean deleteByExample(BaseExample example);
	/**
	 * 更新元数据 中的一条记录
	 * @param pageVO
	 * @param vo
	 * @param datadefineName
	 * @return
	 */
	boolean updateModelData(DynamicPageVO pageVO, DocumentVO vo,
			String datadefineName);
	/**
	 * 更新元数据 中的一条记录
	 * @param pageVO
	 * @param vo
	 * @param datadefineName
	 * @return
	 */
	boolean updateModelDataFlow(DynamicPageVO pageVO, DocumentVO vo,
			String datadefineName);	/**
	 *	向元数据 相应表中插入一条数据
	 * @param pageVO
	 * @param vo
	 * @param datadefineName
	 * @return
	 */
	String saveModelData(DynamicPageVO pageVO, DocumentVO vo, String datadefineName);
	@SuppressWarnings("rawtypes")
	Map saveModelDataFlow(DynamicPageVO pageVO, DocumentVO vo,String datadefineName, boolean masterDateSource) throws Exception;
	/**
	 *	向元数据 相应表中插入一条数据
	 * @param pageVO
	 * @param vo
	 * @param datadefineName
	 * @return
	 */
	String insertModelData(Map<String, String> map, String modelCode);
	
	/**
	 *	 更新对应向元数据
	 * @param map
	 * @param modelCode
	 * @return
	 */	
	String updateModelData(Map<String, String> map, String modelCode);
	
	
	/**
	 * 分页查询 document
	 * @param baseExample
	 * @param currentPage
	 * @param pageSize
	 * @param sortString
	 * @return
	 */
	List<DocumentVO> findPageByExample(BaseExample baseExample,
			int currentPage, int pageSize, String sortString);
	/**
	 *	根据definationId查找workflowID
	 * @param definationId
	 * @return
	 */
	 Long  findWorkId(String definationId);
	 /**
		 *	根据workflowID和taskDefinationKey 查找nodeId
		 * @param definationId
		 * @param workId
		 * @return
		 */
	Long findNodeId(String taskDefinationKey, Long workId);
	List<Map<String, Object>> excuteQueryForList(String sql);
	
	
	public PageList<Map<String,String>> getDataListByDataDefine(DataDefine dd,ScriptEngine engine,Integer currentPage,Integer pageSize, String orderBy) throws ScriptException;
}