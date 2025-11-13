package io.github.rothschil.common.exception;

import io.github.rothschil.common.base.dto.RestBean;
import io.github.rothschil.common.response.enums.Status;

/**
 * 属于自定义异常，使用过程中，需要注意构造函数 {@link Status} 中的定义，是否满足要求，不满足的话
 * 则扩展 {@link Status} 枚举值
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public class CommonException extends RuntimeException {

    private static final long serialVersionUID = -6370612186038915645L;

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误信息
     */
    private String message;

    private RestBean restBean;

    private Status status;

    public CommonException() {
        super();
    }


    /**
     * 利用 {@link Status} 定义的枚举来创建异常信息
     *
     * @param code {@link Status} 类
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    public CommonException(Status code) {

        super(code.getMsg());
        this.code = code.getStatus();
        this.message = code.getMsg();
    }

    /**
     * 定义特殊的异常，可以自定义状态码和异常信息
     *
     * @param status  自定义状态码
     * @param restBean 定义的消息
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    public CommonException(Status status, RestBean restBean) {
        super(restBean.getResp());
        this.status = status;
        this.restBean = restBean;
    }

    /**
     * 利用 {@link Status} 定义的枚举来创建异常信息
     *
     * @param code {@link Status} 类
     * @param append 补充内容
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    public CommonException(Status code, String append) {
        super(code.getMsg());
        this.code = code.getStatus();
        this.message = code.getMsg() + " Detailed information is " + append;
    }

    /**
     * 利用 {@link Status} 定义的枚举来创建异常信息
     *
     * @param code {@link Status} 类
     * @param cause  异常原因信息
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    public CommonException(Status code, Throwable cause) {
        super(code.getMsg(), cause);
        this.code = code.getStatus();
        this.message = code.getMsg();
    }

    /**
     * 利用 {@link Status} 定义的枚举来创建异常信息
     *
     * @param message 定义的消息
     * @param cause   异常原因信息
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    public CommonException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    /**
     * 定义特殊的异常，状态码为 -1
     *
     * @param message 定义的消息
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    public CommonException(String message) {
        super(message);
        this.setCode(-1);
        this.message = message;
    }

    /**
     * 定义特殊的异常，可以自定义状态码和异常信息
     *
     * @param code  自定义状态码
     * @param message 定义的消息
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    public CommonException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 定义特殊的异常，可以自定义状态码和异常信息
     *
     * @param code  自定义状态码
     * @param message 定义的消息
     * @param cause   异常原因信息
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    public CommonException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
