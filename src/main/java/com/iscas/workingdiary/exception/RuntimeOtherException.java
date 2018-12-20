package com.iscas.workingdiary.exception;

/**
 * 其他异常
 */
public class RuntimeOtherException extends RuntimeException{
    public RuntimeOtherException() {
        super();
    }

    public RuntimeOtherException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeOtherException(String message) {
        super(message);
    }

    public RuntimeOtherException(Throwable cause) {
        super(cause);
    }
}