package io.github.rothschil.common.response.enums;

/**
 * Basic Constant Information，Can be extended
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public enum Status {

    /**
     * 成功
     **/
    SUCCESS(200, "SUCCESS"),

    /**
     * 失败
     **/
    FAILURE(500, "FAILURE"),

    /**
     * 10～99 IO处理类
     */
    PRCESS_COMPLETED(11, "处理完毕"),
    IO_EXCEPTION(12, "IO异常"),
    FILE_NOT_EXIST(13, "文件不存在"),

    /**
     * 200 ~ 999 系统异常
     */
    SYSTEM_EXCEPTION(200, "系统异常"),
    RUNTIME_EXCEPTION(202, "运行时异常"),
    NULL_POINTER_EXCEPTION(203, "空指针异常"),
    NOT_FOUND(404, "Not Found"),

    TARGET_EXISTED(741, "目标数据已存在"),
    TARGET_NOT_EXIST(742, "数据不存在"),
    TARGET_IS_NULL(405, "TARGET is Null"),
    TARGET_IS_EMPTY(405, "TARGET is empty"),
    TARGET_IS_BANK(405, "TARGET is Blank"),

    LACK_RESOURCES(501, "资源不足"),
    READ_FAIL(502, "读取资源异常"),
    RESOURCE_NOT_FOUND(503, "资源不存在异常"),
    CLASS_CAST_EXCEPTION(601, "类型转换异常"),
    DATE_PARSE_EXCEPTION(602, "日期转换异常"),
    DATA_PARSE_EXCEPTION(603, "数据转换异常"),

    ENCODE_EXCEPTION(701, "编码异常"),
    ENCODE_UNSUPPORTED_EXCEPTION(702, "编码不支持异常"),

    ARRAY_EXCEPTION(710, "数组异常"),
    ARRAY_OUT_BOUNDS(711, "数组越界异常"),

    SERIALIZE_FAIL(720, "序列化数据失败"),
    DESERIALIZE_FAIL(721, "反序列化数据"),

    COMPRESS_FAIL(732, "数据压缩异常"),
    DE_COMPRESS_FAIL(733, "数据解压缩异常"),

    CHANGE_DIR_ERR(745, "用户切换目录错误"),
    EXCEPTION(777, "未知异常"),

    FILE_NAME_LIMIT(801, "The file Name is too long"),
    FILE_SIZE_LIMIT(802, "The file size too big"),
    INIT_FAIL_PROPERTIE(804, "资源文件初始化失败"),


    /**
     * 1000～1999 区间表示参数错误
     */
    PARAMS_IS_INVALID(1001, "参数无效"),
    PARAMS_TYPE_BIND_ERROR(1003, "参数类型错误"),
    PARAMS_NOT_COMPLETE(1004, "参数缺失"),
    NUMBER_FORMAT(1005, "[The Character to number failed]"),
    QR_CODE_ERR(1006, "[The QR CODE Generation failed]"),

    /**
     * 2000～2999 区间表示用户错误
     */
    USER_NOT_LOGGED_IN(2101, "Access to Resources Requires Identity! Please Sign In"),
    USER_NOT_LOGIN_ERROR(2102, "The User Does Not Exist Or The PassWord Is Wrong"),

    USER_ACCOUNT_FORBIDDEN(2203, "Account is disabled"),
    USER_NOT_EXIST(2204, "Account not exists"),
    USER_HAS_EXISTED(2205, "Account exists"),
    USER_IS_EXPIRED(2206, "Account expired"),
    USER_FIRST_LANDING(2207, "Login for the first time"),
    USER_WAS_DEL(2208, "Account deleted"),
    USER_WAS_LOCK(2209, "Account Locked"),
    USER_UNKNOWN_IDENTITY(2210, "Unknown Identity"),


    USER_SIGN_VERIFY_NOT_COMPLIANT(2310, "Signature does not match"),
    USER_PASSWORD_RESET_FAILED(2311, "Password reset failed"),
    UNSUCCESSFUL_AUTHENTICATION(2314, "Authentication failed"),
    ROLE_WAS_LOCK(2315, "Role Locked"),
    USER_PASSWORD_ERR(2316, "密码错误"),
    USER_SMS_ERR(2317, "短信验证码错误"),

    MANY_USER_LOGINS(2411, "Users are online"),
    USER_KEY_EXCEPTION(2408, "Key generation failed"),

    TOO_MANY_PASSWD_ENTER(2412, "Enter password frequently"),
    VERIFICATION_CODE_INCORRECT(2402, "Verification code error"),
    VERIFICATION_CODE_FAIL(2403, "Verification code generation failed"),

    MANY_ERRORS_OPT(2504, "用户操作错误次数过多"),

    TOKEN_EXPIRED(2608, "Token Expired"),
    TOKEN_GENERATION_FAIL(2609, "The Token generation failed"),
    TOKEN_INVALID(2610, "The Token is invalid"),
    TOKEN_VERIFICATION_FAIL(2611, "The Token verification failed"),
    TOKEN_VERIFICATION_PROCESS_ERR(2612, "The Token verification process error"),
    /**
     * 3000～3999 区间表示接口异常
     */
    API_EXCEPTION(3000, "接口异常"),
    API_NOT_FOUND_EXCEPTION(3002, "接口不存在"),
    API_REQ_MORE_THAN_SET(3003, "接口访问过于频繁，请稍后再试"),
    API_IDEMPOTENT_EXCEPTION(3004, "接口不可以重复提交，请稍后再试"),
    API_PARAM_EXCEPTION(3005, "参数异常"),
    API_PARAM_MISSING_EXCEPTION(3006, "缺少参数"),
    API_METHOD_NOT_SUPPORTED_EXCEPTION(3007, "不支持的Method类型"),
    API_METHOD_PARAM_TYPE_EXCEPTION(3008, "参数类型不匹配"),

    DATA_EXCEPTION(32004, "数据异常"),
    DATA_NOT_FOUND_EXCEPTION(32005, "未找到符合条件的数据异常"),
    DATA_CALCULATION_EXCEPTION(32006, "数据计算异常"),

    SYNC_LOCKED(4000, "资源锁定"),
    GET_SYNC_LOCK_FAILURE(4001, "获取锁失败"),
    GET_SYNC_LOCK_SUCCESS(4002, "获取锁成功"),
    SYNC_LOCK_MANY_REQ(4003, "请求太多"),



    ELASTIC_ERR(4022, "写入Elastic Search失败"),
    REMOTE_EMPTY(4020, "远程目录为空"),
    FTP_SESSION(4019, "Session获取或者连接失败"),
    FTP_CONNET_FAIL(4009, "连接不成功"),
    FTP_IP_ERR(4005, "FTP IP 错误"),
    FTP_PORT_ERR(4005, "FTP Port 错误"),
    DOWNLOAD_ERR(4006, "文件下载失败"),
    UNMATCHED_FILE(4007, "没有符合条件的文件"),

    QUEUE_CAPACITY(4101, "队列中元素占比已超过定义容量的一半"),
    QUEUE_FULL(4102, "队列中元素已超过上限，启动保护机制"),

    MALE_DELIVERY_FAIL(35001, "邮件发送失败"),
    SMS_DELIVERY_FAIL(35011, "短信发送失败"),
    SMS_CONTENT_EXCEEDS_THE_LIMIT(35012, "短信内容长度超过限制"),


    PAY_ACTUAL_PRICE(5010, "The discount amount is greater than the actual price");




    /**
     * Status Code
     */
    private int status;

    /**
     * The Content Description
     */
    private String msg;

    Status(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
