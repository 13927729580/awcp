package org.szcloud.framework.core.common.exception;

/**
 * 数据库规则相关运行时异常
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
public class MDbRultRTException extends MRTException {
    public MDbRultRTException() {
        super();
    }

    public MDbRultRTException(String string) {
        super(string);
    }

    public MDbRultRTException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public MDbRultRTException(Throwable throwable) {
        super(throwable);
    }
}
