package com.iscas.workingdiary.util.json;

import com.iscas.workingdiary.bean.ResponseStatus;
import com.iscas.workingdiary.exception.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ResultData {

    public ResultData() { }

    public ResultData(String message) {
        this.message = message;
    }

    public ResultData(String message, Object obj) {
        this.message = message;
        this.datas = obj;
    }


    /**
     * 返回单个实体
     */
    public <T> ResultData(T entity) {
        this.code = ResponseStatus.SUCCESS;
        this.datas = entity;
    }

    /**
     * 返回集合类型
     */
    public ResultData(List<?> list) {
        this.code = ResponseStatus.SUCCESS;
        this.datas = list;
    }

    /**
     * 返回Map集合类型
     */
    public ResultData(Map<String, Object> map) {
        this.code = ResponseStatus.SUCCESS;
        this.datas = map;
    }

    /**
     * 500-业务逻辑错误
     */
    public ResultData(RuntimeServiceException rex) {
        this.code = ResponseStatus.SERVER_ERROR;
        this.message = rex.getMessage();
    }

    /**
     * 501-功能不完善，无对应方法
     */
    public ResultData(RuntimeFunctionException rex) {
        this.code = ResponseStatus.SERVER_FUNCTION_UNREALIZED_ERROR;
        this.message = rex.getMessage();
    }

    /**
     * 502-网络异常
     */
    public ResultData(RuntimeWebException rex) {
        this.code = ResponseStatus.SERVER_WEB_ERROR;
        this.message = rex.getMessage();
    }

    /**
     * 506-未知其它
     */
    public ResultData(RuntimeOtherException rex) {
        this.code = ResponseStatus.SERVER_OTHER_UNKNOWN_ERROR;
        this.message = rex.getMessage();
    }

    /**
     * 异常
     */
    public ResultData(Exception ex) {
        this.code = ResponseStatus.SERVER_OTHER_UNKNOWN_ERROR;
        this.message = getErrorMessage(ex);
        ex.printStackTrace();
    }

    /**
     * 运行时异常
     */
    public ResultData(RuntimeException rex) {
        this.code = ResponseStatus.SERVER_OTHER_UNKNOWN_ERROR;
        this.message = rex.getMessage();
    }

    /**
     * 运行时异常
     */
    public ResultData(Throwable tx) {
        this.code = ResponseStatus.SERVER_OTHER_UNKNOWN_ERROR;
        this.message = tx.getMessage();
    }

    /**
     * 600-RepChain服务异常
     * @param rex
     */
    public ResultData(RepChainServerException rex){
        this.code = ResponseStatus.REPCHAIN_SERVER_ERROR;
        this.message = rex.getMessage();
    }

    /**
     * 结果编码
     */
    //@XmlElement(name = "code")
    private int code;

    /**
     * 消息
     */
    private String message;

    /**
     * 结果数据，单个实体 或 List<T>
     */
    private Object datas;

    public int getCode() {
        return code;
    }

    public ResultData setCode(int code) {
        this.code = code;
        return this;
    }

    public Object getDatas() {
        return datas;
    }

    public ResultData setDatas(Object datas) {
        this.datas = datas;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResultData setMessage(String message) {
        this.message = message;
        return this;
    }

    private static String getErrorMessage(Exception ex) {
        if (ex instanceof ArithmeticException) {
            return "系统异常：计算错误";
        }
        if (ex instanceof NullPointerException) {
            return "系统异常：输入错误，缺少输入值";
        }
        if (ex instanceof ClassCastException) {
            return "系统异常：类型转换错误";
        }
        if (ex instanceof NegativeArraySizeException) {
            return "系统异常：集合负数";
        }
        if (ex instanceof ArrayIndexOutOfBoundsException) {
            return "系统异常：集合超出范围";
        }
        if (ex instanceof FileNotFoundException) {
            return "系统异常：文件未找到";
        }
        if (ex instanceof NumberFormatException) {
            return "系统异常：输入数字错误";
        }
        if (ex instanceof SQLException) {
            return "系统异常：数据库异常";
        }
        if (ex instanceof IOException) {
            return "系统异常：文件读写错误";
        }
        if (ex instanceof NoSuchMethodException) {
            return "系统异常：方法找不到";
        }
        return ex.getMessage();
    }
}
