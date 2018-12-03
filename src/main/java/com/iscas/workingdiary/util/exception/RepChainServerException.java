package com.iscas.workingdiary.util.exception;

/**
 * RepChain服务器异常
 */
public class RepChainServerException extends RuntimeException{
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
