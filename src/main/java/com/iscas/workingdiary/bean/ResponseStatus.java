package com.iscas.workingdiary.bean;

/**
 * 错误状态码
 */
public class ResponseStatus {

    /******************************* 200系列：常规HTTP异常 ******************************/

    /**
     * 200-成功
     */
    public final static int SUCCESS = 200;

    /**
     * 201-登录超时
     */
    public final static int TIME_OUT = 201;


    /******************************** 300系列：数据库类异常 ********************************/

    /**
     * 300 数据库异常（异常原因：鬼知道）
     */
    public final static int DB_ERROR = 300;

    /**
     * 301 数据库插入异常
     */
    public final static int DB_INSERT_ERROR = 301;

    /**
     * 302 数据库删除异常
     */
    public final static int DB_DELETE_ERROR = 302;

    /**
     * 303 数据库更新异常
     */
    public final static int DB_UPDATE_ERROR = 303;

    /**
     * 304 数据库查询异常
     */
    public final static int DB_QUERY_ERROR = 304;

    /**
     * 305 数据库连接失败
     */
    public final static int DB_CONNECTION_ERROR = 305;

    /**
     * 306 查询结果为空
     */
    public final static int DB_QUERY_NULL_ERROR = 306;

    /**
     * 307 数据库中没有证书
     */
    public final static int DB_CERT_NOT_EXIST = 307;

    /**
     * 308 数据库中的证书状态为未上链
     */
    public final static int DB_CERT_NOT_PROOF = 308;

    /**
     * 309 已存在
     */
    public final static int DB_ALREADY_EXIST_ERROR = 309;

    /**
     * 300-1 查询成功，但结果超出预期
     */
    public final static int DB_SUCCESS_BUT_UNEXPECTED_RESULT = 3001;



    /******************************** 400系列：客户端异常 ********************************/

    /**
     * 401 JWTTOKEN异常
     */
    public final static int CLIENT_JWTTOKEN_NULL_ERROR = 401;

    /**
     * 403 禁止访问
     */
    public final static int CLIENT_NO_ACCESS_ERROR = 403;


    /******************************** 500系列：应用服务器异常 ********************************/

    /**
     * 500-服务器错误
     */
    public final static int SERVER_ERROR = 500;

    /**
     * 501-功能未实现
     */
    public final static int SERVER_FUNCTION_UNREALIZED_ERROR = 501;

    /**
     * 502-网络异常
     */
    public final static int SERVER_WEB_ERROR = 502;

    /**
     * 503-参数错误
     */
    public final static int SERVER_PARAM_ERROR = 503;

    /**
     * 文件格式错误
     */
    public final static int SERVER_FILE_EXT_ERROR = 504;

    /**
     * 505 bad request
     */
    public final static int SERVER_BAD_REQUEST_ERROR = 505;

    /**
     * 506 其他未知异常
     */
    public final static int SERVER_OTHER_UNKNOWN_ERROR = 506;

    /**
     * 507 权限不足
     */
    public final static int SERVER_NO_ACCESS_ERROR = 507;

    /**
     * 508 JWT token过期异常
     */
    public final static int SERVER_TOKEN_EXPIRED_ERROR = 508;

    /**
     * 509 JWT 错误格式异常
     */
    public final static int SERVER_TOKEN_MALFORMED_ERROR = 509;


    /********************************* 600系列：RepChain服务器相关的异常 ********************************/

    /**
     * 600-RepChain 服务器错误
     */
    public final static int REPCHAIN_SERVER_ERROR = 600;

    /**
     * 601-RepChain链码ID不存在
     */
    public final static int REPCHAIN_CHAINCODEID_NOT_EXIST_ERROR = 601;

    /**
     * 602-证书未上链
     */
    public final static int REPCHAIN_INVALID_CERT_ERROR = 602;

    /**
     * 603-证书已存在
     */
    public final static int REPCHAIN_CERT_ALREADY_EXIST_ERROR = 603;

    /**
     * 604-RepChain查询结果为空
     */
    public final static int REPCHAIN_QUERY_RESULT_NULL_ERROR = 604;

    /**
     * 605-RepChain 服务器连接失败
     */
    public final static int REPCHAIN_SERVER_CONNECTION_ERROR = 605;

    /**
     * 606-RepChain bad request（向RepChain服务器发送了错误请求）
     */
    public final static int REPCHAIN_BAD_REQUEST_ERROR = 606;

    /**
     * 607 RepChain请求错误（非RepChain服务器错误）
     */
    public final static int REPCHAIN_REQUEST_ERROR = 607;

}
