package com.iscas.workingdiary.util.exception;

/**
 *
 */
public class RuntimeFunctionException extends RuntimeException{
    public RuntimeFunctionException() {
        super();
    }

    public RuntimeFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeFunctionException(String message) {
        super(message);
    }

    public RuntimeFunctionException(Throwable cause) {
        super(cause);
    }
}
