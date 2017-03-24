package org.szcloud.framework.core.common.exception;

/**
 * 运行时异常
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({"unchecked","serial"})
public class MRTException
    extends RuntimeException {
    public MRTException() {
        super();
    }

    public MRTException(String string) {
        super(string);
    }

    public MRTException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public MRTException(Throwable throwable) {
        super(throwable);
    }
}
