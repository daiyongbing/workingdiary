package com.iscas.workingdiary.exception;

public class RuntimeWebException extends RuntimeException{

    private static final long serialVersionUID = 4068183781296431113L;

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