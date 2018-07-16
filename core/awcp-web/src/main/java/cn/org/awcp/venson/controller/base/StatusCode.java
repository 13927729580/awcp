package cn.org.awcp.venson.controller.base;

/**
 * 返回结果状态枚举类
 * 
 * @author venson
 */
public enum StatusCode implements StatusType {
	SUCCESS(0, "success"), NO_ACCESS(-1, "noAccess"), PARAMETER_ERROR(-2, "parameterError"), FAIL(-3,
			"fail"), NO_LOGIN(-4, "noLogin");

	private int code;
	private String message;

	private StatusCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
    public int getStatus() {
		return this.code;
	}

	public StatusCode setCode(int code) {
		this.code = code;
		return this;
	}

	@Override
    public String getMessage() {
		return this.message;
	}

}