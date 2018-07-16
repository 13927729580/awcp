package cn.org.awcp.venson.controller.base;

/**
 * 返回结果类
 * 
 * @author venson
 */
public class ReturnResult {
	private int status;
	private Object total;
	private String message;
	private Object data;

	public ReturnResult() {
	}

	public int getStatus() {
		return this.status;
	}

	public ReturnResult setStatus(StatusType status) {
		this.status = status.getStatus();
		this.message = status.getMessage();
		return this;
	}

	public String getMessage() {
		return this.message;
	}

	public ReturnResult setMessage(String message) {
		this.message = message;
		return this;
	}

	public Object getTotal() {
		return this.total;
	}

	public ReturnResult setTotal(Object total) {
		this.total = total;
		return this;
	}

	public Object getData() {
		return data;
	}

	public ReturnResult setData(Object data) {
		this.data = data;
		return this;
	}

	public static ReturnResult get() {
		return ControllerContext.getResult();
	}

}