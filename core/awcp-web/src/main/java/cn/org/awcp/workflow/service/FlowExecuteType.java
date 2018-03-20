package cn.org.awcp.workflow.service;

import cn.org.awcp.venson.exception.PlatformException;

/**
 * 流程动作类型枚举类
 * @author venson
 * @version 20180301
 */
public enum FlowExecuteType {
    /**
     * 回撤
     */
    RETRACEMENT(2006),
    /**
     * 保存
     */
    SAVE(2008),
    /**
     * 发送
     */
    SEND(2011),
    /**
     * 办结
     */
    SHOTDOWN(2017),
    /**
     *  跳转
     */
    SKIP(2018),
    /**
     * 传阅
     */
    AGREE(2019),
    /**
     * 退回
     */
    RETURN(2023),
    /**
     * 加签
     */
    COUNTERSIGN(2024),
    /**
     * 抄送
     */
    COPY(2025),
    /**
     * 撤销
     */
    UNDO(2026),
    /**
     * 拒绝
     */
    REJECT(2027);

    private int value;

    FlowExecuteType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static FlowExecuteType forValue(int value) {
        switch (value) {
            case 2006:
                return RETRACEMENT;
            case 2008:
                return SAVE;
            case 2011:
                return SEND;
            case 2017:
                return SHOTDOWN;
            case 2018:
                return SKIP;
            case 2019:
                return AGREE;
            case 2023:
                return RETURN;
            case 2024:
                return COUNTERSIGN;
            case 2025:
                return COPY;
            case 2026:
                return UNDO;
            case 2027:
                return REJECT;
            default:
                throw new PlatformException("未知的流程类型类型");
        }
    }
}
