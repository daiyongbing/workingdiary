package com.iscas.workingdiary.exception;

/**
 * 应用服务器异常
 */
public class RuntimeServiceException extends RuntimeException{

    public RuntimeServiceException() {
        super();
    }

    public RuntimeServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeServiceException(String message) {
        super(message);
    }

    public RuntimeServiceException(Throwable cause) {
        super(cause);
    }
}

