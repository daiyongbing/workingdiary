package com.iscas.workingdiary.exception.handler;

import com.iscas.workingdiary.exception.ExampleException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 处理Controller抛出的异常
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ExampleException.class)
    public String sqlExpHandler(ExampleException ee){ //just an example
        return null;
    }
}
