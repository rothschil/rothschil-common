package io.github.rothschil.common.exception;

import io.github.rothschil.common.response.Result;
import io.github.rothschil.common.response.enums.Status;
import jakarta.annotation.Priority;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 全局异常处理 对统一返回实体 进行封装 Handler
 *
 * @author <a href="https://github.com/rothschil">Sam</a>
 */
@Priority(value=1)
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * NullPointerException 拦截抛出的异常
     *
     * @param ex 异常
     * @return ErR 异常响应
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    @ExceptionHandler(NullPointerException.class)
    public Result nullPointerException(HttpServletRequest request, NullPointerException ex) {
        String urlStr = request.getRequestURI().replaceAll(".*//([^//]*:{0,1}[0-9])", "");
        String params = "";
        LOG.error("[URI]:\n{}\n[params]:\n{}\n[exception]:{}", urlStr, params, ex.getMessage());
        return Result.fail(Status.NULL_POINTER_EXCEPTION, ex);
    }

    /**
     * 拦截抛出的异常
     *
     * @param ex 异常
     * @return ErR 异常响应
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    @ExceptionHandler(CommonException.class)
    public Result handleWeathertopException(CommonException ex, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOG.error("url:{} code:{},msg:{}",requestURI, ex.getCode(), ex.getMessage());
        return Result.fail(ex.getCode(), requestURI+" "+ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        LOG.error("参数解析失败", e);
        return Result.fail(Status.API_PARAM_EXCEPTION, new CommonException("请求入参无法被解析或者序列化"));
    }

    /**
     * 自定义验证异常
     *
     * @param e 异常
     * @return request 请求
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException e, HttpServletRequest request) {
        LOG.error("发生参数校验异常！原因是：", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return Result.fail(Status.API_PARAM_EXCEPTION, collect);
    }


    /** 自定义验证异常
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param e 异常实例
     * @param request   请求
     * @return io.github.rothschil.common.response.Result
     **/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorMessages = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        LOG.warn("[参数校验异常] - {}", errorMessages);
        return Result.fail(Status.API_PARAM_EXCEPTION, errorMessages);
    }


    /**
     * 处理参数校验异常 (JSR 303 - 在Controller方法上直接校验参数)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<List<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errorMessages = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        LOG.warn("[参数校验异常] - {}", errorMessages);
        return Result.fail(Status.API_PARAM_VIOLATION_EXCEPTION,"处理参数校验异常", errorMessages);
    }


    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOG.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return Result.fail(Status.API_METHOD_NOT_SUPPORTED_EXCEPTION, requestURI+" "+e.toString());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOG.error("请求地址'{}',发生未知异常.", requestURI, e);
        return Result.fail(Status.RUNTIME_EXCEPTION,requestURI+" "+e.toString());
    }

    /**
     * 处理其他所有未捕获的异常 (兜底处理)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 设置HTTP状态码为500
    public Result handleException(Exception ex, HttpServletRequest request) {
        // 1. 打印详细的错误堆栈到日志 (ERROR级别，包含请求路径)
        LOG.error("[系统异常] 请求路径: {}，异常信息：", request.getRequestURI(), ex);
        // 2. 返回给前端一个通用的错误信息 (避免泄露敏感信息)
        return Result.fail(Status.SYSTEM_BUSY_EXCEPTION, "系统繁忙，请稍后再试", ex.toString());
    }
}
