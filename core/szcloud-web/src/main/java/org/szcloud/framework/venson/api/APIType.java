package org.szcloud.framework.venson.api;

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
	PAGE(7);
	private int type;

	private APIType() {
	}

	private APIType(int type) {
		this.type = type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getValue() {
		return type;
	}

}
