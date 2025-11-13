package io.github.rothschil.common.exception;

import io.github.rothschil.common.response.Result;
import io.github.rothschil.common.response.enums.Status;
import jakarta.annotation.Priority;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
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


//    /**
//     * 拦截抛出的异常，兜底异常
//     *
//     * @param ex 异常
//     * @return ErR 异常响应
//     * @author <a href="https://github.com/rothschil">Sam</a>
//     **/
//    @ExceptionHandler(Exception.class)
//    public Result globalException(HttpServletRequest request, HandlerMethod handlerMethod,Exception ex) {
//        String urlStr = request.getRequestURI().replaceAll(".*//([^//]*:{0,1}[0-9])", "");
//        String params = "";
//        LOG.error("[URI]:\n{}\n[params]:\n{}\n[exception]:{}", urlStr, params, ex.getMessage());
//        ex.printStackTrace();
//        return Result.fail(Status.EXCEPTION, ex);
//    }

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
    public Result handleWeathertopException(CommonException ex) {
        LOG.error("code:{},msg:{}", ex.getCode(), ex.getMessage());
        return Result.fail(ex.getCode(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        LOG.error("参数解析失败", e);
        return Result.fail(Status.API_PARAM_EXCEPTION, new CommonException("请求入参无法被解析或者序列化"));
    }

    /**
     * 参数校验异常
     *
     * @param e 异常
     * @return ErR 异常响应
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException e) {
        LOG.error("发生参数校验异常！原因是：", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return Result.fail(Status.API_PARAM_EXCEPTION, collect);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(HttpServletRequest request,MethodArgumentNotValidException e) {
        LOG.error("发生参数校验异常！原因是 ", e);
        // List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        // List<String> collect = fieldErrors.stream()
        //         .map(DefaultMessageSourceResolvable::getDefaultMessage)
        //         .collect(Collectors.toList());
        StringBuilder errors = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(error -> {
            errors.append(error.getDefaultMessage()).append(";");
        });
        // BindingResult bindingResult = e.getBindingResult();
        // FieldError firstFieldError = CollectionUtil.getFirst(bindingResult.getFieldErrors());
        // String exceptionStr = Optional.ofNullable(firstFieldError)
        //         .map(FieldError::getDefaultMessage)
        //         .orElse(StrUtil.EMPTY);
        // LOG.error("[{}] {} [ex] {}", request.getMethod(),"URL:", exceptionStr);
        return Result.fail(Status.API_PARAM_EXCEPTION, errors.toString());
    }
}
