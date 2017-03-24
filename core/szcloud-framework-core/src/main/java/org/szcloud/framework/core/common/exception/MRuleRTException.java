package org.szcloud.framework.core.common.exception;

/**
 * 规则相关运行时异常
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
public class MRuleRTException extends MRTException {
    public MRuleRTException() {
        super();
    }

    public MRuleRTException(String string) {
        super(string);
    }

    public MRuleRTException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public MRuleRTException(Throwable throwable) {
        super(throwable);
    }
}
