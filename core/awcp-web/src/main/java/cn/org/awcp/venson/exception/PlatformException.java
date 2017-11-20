package cn.org.awcp.venson.exception;

public class PlatformException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public PlatformException() {
		super();
	}

	public PlatformException(String msg) {
		super(msg);
	}
}
