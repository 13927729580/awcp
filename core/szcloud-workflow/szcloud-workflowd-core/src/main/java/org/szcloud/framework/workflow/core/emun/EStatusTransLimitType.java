package org.szcloud.framework.workflow.core.emun;

//多状态处理限制
public class EStatusTransLimitType {
			public final static int NotAnyLimit = 0; //没有限制
			public final static int AllTransMaxLimit = 1; //所有处理人员达到一定程度时其他人不能再处理
			public final static int RoleTransMaxLimit = 2; //某一类型处理人员（按角色、所有没有角色的用户属于一种角色）达到一定程度时其他人不能再处理
}
