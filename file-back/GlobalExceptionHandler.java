package com.iscas.workingdiary.jwtsecurity;

import com.iscas.workingdiary.util.exception.StateCode;
import com.iscas.workingdiary.util.json.ResultData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResultData handleException(Exception e) {
        return new ResultData(StateCode.SERVER_ERROR, "未登录", e.getMessage());
    }
}
