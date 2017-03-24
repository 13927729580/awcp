package org.szcloud.framework.workflow.core.base;

/**
 * 操作类型
 * @author jackie.wang
 * @version 2015.08.05
 *
 */
public enum EHandleType {
	NotAny,					//未知
	List_Draft,				//草稿列表
	List_Transacting,		//待办列表
	List_Transacted,		//已办列表
	List_Special			//特殊公文列表
};
