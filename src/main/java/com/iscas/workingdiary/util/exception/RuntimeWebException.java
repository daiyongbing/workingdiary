package com.iscas.workingdiary.util.exception;

/**
 * Created by adams on 2017/4/11.
 */
public class RuntimeWebException extends RuntimeException{

    public RuntimeWebException() {
        super();
    }

    public RuntimeWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeWebException(String message) {
        super(message);
    }

    public RuntimeWebException(Throwable cause) {
        super(cause);
    }
}