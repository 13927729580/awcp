package org.szcloud.framework.workflow.core.emun;

/**
 * 步骤条件处理类型
 * @author admin
 *
 */
public enum ActivityConditionType {
	AllFinished(0),//全部人员已完成
	AtListOneFinished(1),//至少一个人已完成
	FinishedNumber(2), //完成人数 
	FinishedPercent(3), //完成百分比
	UnFinishedNumber(4), //未完成人数 
	UnFinishedPercent(5);//未完成百分比
	
	private int value = -1;
	// 构造方法
    private ActivityConditionType(int value) {
        this.value = value;
    }
    
    // 覆盖方法
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

}

	
	
	
	
	
	
