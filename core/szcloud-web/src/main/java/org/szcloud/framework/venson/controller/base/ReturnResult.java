package org.szcloud.framework.venson.controller.base;

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
	private Object rows;

	private ReturnResult() {
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

	public ReturnResult setMessage(StatusType message) {
		this.message = message.getMessage();
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
	public Object getRows() {
		return rows;
	}

	public ReturnResult setRows(Object rows) {
		this.rows = rows;
		return this;
	}

	public static ReturnResult get() {
		return new ReturnResult();
	}

}