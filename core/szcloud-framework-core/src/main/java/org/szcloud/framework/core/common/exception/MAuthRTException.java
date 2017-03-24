package org.szcloud.framework.core.common.exception;

/**
 * 权限相关运行时异常
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
public class MAuthRTException extends MRTException {
    public MAuthRTException() {
        super();
    }

    public MAuthRTException(String string) {
        super(string);
    }

    public MAuthRTException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public MAuthRTException(Throwable throwable) {
        super(throwable);
    }
}
