package com.zhsj.community.yanglao_yiliao.old_activity.exception;

/**
 * 自定义异常处理
 * @author liulq
 */
public class BootException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BootException(String message) {
        super(message);
    }

    public BootException(Throwable cause) {
        super(cause);
    }

    public BootException(String message, Throwable cause) {
        super(message, cause);
    }
}
