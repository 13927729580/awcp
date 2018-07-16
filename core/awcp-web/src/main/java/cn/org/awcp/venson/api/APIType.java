package cn.org.awcp.venson.api;

/**
 * api接口枚举
 *
 * @author venson
 */
public enum APIType {


    /**
     * 默认
     */
    DEFAULT(0),
    /**
     * 增加
     */
    ADD(1),
    /**
     * 更新
     */
    UPDATE(2),
    /**
     * 获取
     */
    GET(3),
    /**
     * 删除
     */
    DELETE(4),

    /**
     * 执行
     */
    EXECUTE(5),
    /**
     * 查询
     */
    QUERY(6),

    /**
     * 分页
     */
    PAGE(7),
    /**
     * 执行脚本
     */
    EXECUTE_SCRIPT(8);

    private int type;

    APIType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static APIType valueOf(int value) {
        for (APIType apiType : APIType.values()) {
            if (apiType.getType() == value) {
                return apiType;
            }
        }
        return DEFAULT;
    }

}
