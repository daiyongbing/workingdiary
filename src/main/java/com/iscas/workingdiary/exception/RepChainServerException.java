package com.iscas.workingdiary.exception;

/**
 * RepChain服务器异常
 */
public class RepChainServerException extends RuntimeException{
    private static final long serialVersionUID = 2173573331813862568L;

    public RepChainServerException() {
        super();
    }

    public RepChainServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepChainServerException(String message) {
        super(message);
    }

    public RepChainServerException(Throwable cause) {
        super(cause);
    }
}
