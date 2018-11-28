package com.iscas.workingdiary.util;

import com.iscas.workingdiary.util.exception.RuntimeFunctionException;
import com.iscas.workingdiary.util.exception.RuntimeOtherException;
import com.iscas.workingdiary.util.exception.RuntimeServiceException;
import com.iscas.workingdiary.util.exception.RuntimeWebException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlElement;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ResultData {
    /**
     * 200-成功
     */
    public final static String CODE_SUCCESS = "200";

    /**
     * 201 登录超时
     */
    public final static String CODE_OUT_TIME = "201";

    /**
     * 500-业务逻辑错误
     */
    public final static String CODE_ERROR_SERVICE = "500";

    /**
     * 502-网络异常
     */
    public final static String CODE_ERROR_WEB = "502";
    /**
     * 503-未知其它
     */
    public final static String CODE_ERROR_OTHER = "503";

    /**
     *查询异常
     */
    public final static String CODE_ERROR_EXCEPTION = "505";
    /**
     *参数有误
     */
    public final static String CODE_ERROR_PARAM = "506";

    /**
     *查询为空
     */
    public final static String CODE_ERROR_NULL = "507";




    public static ResultData addSuccess() {
        return new ResultData(CODE_SUCCESS, null);
    }

    public static ResultData addSuccess(String message) {
        return new ResultData(CODE_SUCCESS, message);
    }

    public static ResultData addAddSuccess() {
        return new ResultData(CODE_SUCCESS, "新增成功");
    }

    public static ResultData addUpdateSuccess() {
        return new ResultData(CODE_SUCCESS, "更新成功");
    }

    public static ResultData addUpdateCheckSuccess() {
        return new ResultData(CODE_SUCCESS, "确定成功");
    }

    public static ResultData addDeleteSuccess() {
        return new ResultData(CODE_SUCCESS, "删除成功");
    }

    public static ResultData addOperationSuccess() {
        return new ResultData(CODE_SUCCESS, "操作成功");
    }

    public ResultData() {
        super();
    }

    public ResultData(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ResultData(String code, String message, Object obj) {
        super();
        this.code = code;
        this.message = message;
        this.datas = obj;
    }

    public ResultData(String message) {
        super();
        this.code = CODE_SUCCESS;
        this.message = message;
    }

    /**
     * 返回单个实体
     */
    public <T> ResultData(T entity) {
        super();
        this.code = CODE_SUCCESS;
        this.datas = entity;
    }

    /**
     * 返回集合类型
     */
    public ResultData(List<?> list) {
        super();
        this.code = CODE_SUCCESS;
        this.datas = list;
    }

    /**
     * 返回Map集合类型
     */
    public ResultData(Map<String, Object> map) {
        super();
        this.code = CODE_SUCCESS;
        this.datas = map;
    }

    /**
     * 500-业务逻辑错误
     */
    public ResultData(RuntimeServiceException rex) {
        super();
        this.code = CODE_ERROR_SERVICE;
        this.message = rex.getMessage();
    }

    /**
     * 501-功能不完善，无对应方法
     */
    /*public ResultData(RuntimeFunctionException rex) {
        super();
        this.code = CODE_ERROR_FUNCTION;
        this.message = rex.getMessage();
    }*/

    /**
     * 502-网络异常
     */
    public ResultData(RuntimeWebException rex) {
        super();
        this.code = CODE_ERROR_WEB;
        this.message = rex.getMessage();
    }

    /**
     * 503-未知其它
     */
    public ResultData(RuntimeOtherException rex) {
        super();
        this.code = CODE_ERROR_OTHER;
        this.message = rex.getMessage();
    }

    /**
     * 异常
     */
    public ResultData(Exception ex) {
        super();
        this.code = CODE_ERROR_OTHER;
        this.message = getErrorMessage(ex);
        ex.printStackTrace();
    }

    /**
     * 运行时异常
     */
    public ResultData(RuntimeException rex) {
        super();
        this.code = CODE_ERROR_OTHER;
        this.message = rex.getMessage();
    }

    /**
     * 运行时异常
     */
    public ResultData(Throwable tx) {
        super();
        this.code = CODE_ERROR_OTHER;
        this.message = tx.getMessage();
    }

    /**
     * 结果编码
     */
    @XmlElement(name = "code")
    private String code;

    /**
     * 消息
     */
    private String message;

    /**
     * 结果数据，单个实体 或 List<T>
     */
    private Object datas;

    // -------------------------- getter and setter -----------------------------
    public String getCode() {
        return code;
    }

    public ResultData setCode(String code) {
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
