package cn.org.awcp.venson.api;

/**
 * api接口枚举
 * 
 * @author venson
 *
 */
public enum APIType {

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
	private APIType() {
	}

	private APIType(int type) {
	}

	public void setType(int type) {
	}

	public static APIType valueOf(int value) { // 手写的从int到enum的转换函数
		switch (value) {
		case 1:
			return APIType.ADD;
		case 2:
			return APIType.UPDATE;
		case 3:
			return APIType.GET;
		case 4:
			return APIType.DELETE;
		case 5:
			return APIType.EXECUTE;
		case 6:
			return APIType.QUERY;
		case 7:
			return APIType.PAGE;
		case 8:
			return APIType.EXECUTE_SCRIPT;
		default:
			return null;
		}
	}

}
