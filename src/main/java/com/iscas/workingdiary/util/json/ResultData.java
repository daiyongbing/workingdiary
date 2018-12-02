package com.iscas.workingdiary.util.json;

import com.iscas.workingdiary.util.exception.*;

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
     * 204-没有证书
     */
    public final static String NO_CERT = "204";

    /**
     * 205-未认证的证书
     */
    public final static String CERT_NOT_PROOF = "205";

    /**
     * 206-RepChain服务连接失败
     */
    public final static String BLOCKCHAIN_CONNECTION_FAILED = "206";

    /**
     * RepChain服务器错误
     */
    public final static String BLOCKCHAIN_SERVER_ERROR = "207";

    /**
     * 500-服务器错误
     */
    public final static String CODE_ERROR_SERVICE = "500";

    /**
     * 501-未实现功能
     */
    public final static String CODE_ERROR_FUNCTION = "501";

    /**
     * 502-网络异常
     */
    public final static String CODE_ERROR_WEB = "502";


    /**
     * 503-未知错误
     */
    public final static String CODE_ERROR_OTHER = "503";

    /**
     * 504-数据库插入异常
     */
    public final static String DATABASE_INSERT_ERROR = "504";

    /**
     * 505-查询异常
     */
    public final static String CODE_ERROR_EXCEPTION = "505";

    /**
     * 506-参数有误
     */
    public final static String CODE_ERROR_PARAM = "506";

    /**
     * 507-查询为空
     */
    public final static String CODE_ERROR_NULL = "507";

    /**
     * 508-数据库异常
     */
    public final static String DATABASE_EXCEPTION = "508";

    /**
     * 509-用户已存在
     */
    public final static String CODE_ERROR_EXIST = "509";

    /**
     * 文件格式错误
     */
    public final static String FILE_EXT_ERROR = "510";



    public static ResultData addSuccess() {
        return new ResultData(CODE_SUCCESS, null);
    }

    public static ResultData addSuccess(String message) {
        return new ResultData(CODE_SUCCESS, message);
    }

    public static ResultData addAddSuccess() {
        return new ResultData(CODE_SUCCESS, "新增成功");
    }

    public static ResultData updateSuccess() {
        return new ResultData(CODE_SUCCESS, "更新成功");
    }

    public static ResultData addUpdateCheckSuccess() {
        return new ResultData(CODE_SUCCESS, "确定成功");
    }

    public static ResultData deleteSuccess() {
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
    public ResultData(RuntimeFunctionException rex) {
        super();
        this.code = CODE_ERROR_FUNCTION;
        this.message = rex.getMessage();
    }

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
     * 207-RepChain服务异常
     * @param rex
     */
    /*public ResultData(RepChainServerException rex){
        super();
        this.code = BLOCKCHAIN_SERVER_ERROR;
        this.message = rex.getMessage();
    }*/

    /**
     * 结果编码
     */
    //@XmlElement(name = "code")
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

    /*@Override
    public String toString() {
        return "\"ResultData\":{" +
                "\"code\":" + "\""+code + "\"," +
                "\"message\":" + "\""+message + "\"," +
                "\"datas\":" + datas +
                '}';
    }*/
}
