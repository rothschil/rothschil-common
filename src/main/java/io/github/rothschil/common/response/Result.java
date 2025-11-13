package io.github.rothschil.common.response;


import cn.hutool.core.util.ObjectUtil;
import io.github.rothschil.common.response.enums.Status;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 定义正常响应实体类
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -4505655308965878999L;
    /**
     * 结果编码
     **/
    int code;
    /**
     * 消息描述
     **/
    String message;

    /**
     * 对需要响应的数据进行封装
     */
    private T data;

    /**
     * 错误
     **/
    private String exception;

    private Result() {
    }

    public Result(Status status, T data) {
        this.code = status.getStatus();
        this.message = status.getMsg();
        this.data = data;
    }

    private void setStatus(Status status) {
        this.code = status.getStatus();
        this.message = status.getMsg();
    }


    /**
     * 成功
     *
     * @return Result
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2018/4/26-16:43
     **/
    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.setStatus(Status.SUCCESS);
        return result;
    }

    /**
     * 成功
     *
     * @param data 数据实体
     * @return Result
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2018/4/26-16:43
     **/
    public static <T> Result<T> success(T data) {
        Result<T> result = success();
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(Status status, Exception ex) {
        return fail(status,null,ex,null,null);
    }

    public static <T> Result<T> fail(Status status,  T data) {
        return fail(status,null,null,null,data);
    }

    public static <T> Result<T> fail(Status status, String message, T data) {
        return fail(status,message,null,null,data);
    }

    public static <T> Result<T> fail(Status status, String message) {
        return fail(status,message,null,null,null);
    }

    public static <T> Result<T> fail(Status status, String message,Exception ex, Throwable e,T data) {
        Result<T> erResult = new Result<T>();
        erResult.setCode(status.getStatus());
        erResult.setMessage(StringUtils.isBlank(message)?status.getMsg():message);
        if(ObjectUtil.isNotNull(ex)){
            erResult.setException(ex.getMessage());
        } else{
            if(ObjectUtil.isNotNull(e)){
                erResult.setException(e.getClass().getName());
            }
        }
        if(ObjectUtil.isNotNull(data)){
            erResult.setData(data);
        }
        return erResult;
    }

    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> erResult = new Result<T>();
        erResult.setCode(code);
        erResult.setMessage(message);
        return erResult;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
