package com.zeffon.danzhu.core;

import com.zeffon.danzhu.core.configuration.ExceptionCodeConfiguration;
import com.zeffon.danzhu.exception.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Create by Zeffon on 2020/10/1
 */
@ControllerAdvice
public class GlobalExceptionAdvice {

    @Autowired
    private ExceptionCodeConfiguration codeConfiguration;

    // 处理未知异常
    @ExceptionHandler(value=Exception.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public UnifyResponse handleException(HttpServletRequest req, Exception e) {
        String requestUrl = req.getRequestURI();
        String method = req.getMethod();
        UnifyResponse message = new UnifyResponse(9999, "服务器异常", method + " " +requestUrl);
        return message;
    }

    // 处理已知异常
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<UnifyResponse> handleHttpException(HttpServletRequest req, HttpException e) {
        String requestUrl = req.getRequestURI();
        String method = req.getMethod();

        UnifyResponse message = new UnifyResponse(e.getCode(), codeConfiguration.getMessage(e.getCode()), method + " " + requestUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.resolve(e.getHttpStatusCode());

        ResponseEntity<UnifyResponse> r = new ResponseEntity<>(message, headers, httpStatus);
        return r;
    }

    // 处理body里的参数异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public UnifyResponse handleBeanValidation(HttpServletRequest req, MethodArgumentNotValidException e) {
        String requestUrl = req.getRequestURI();
        String method = req.getMethod();

        List<ObjectError> errors =  e.getBindingResult().getAllErrors();
        String message = this.formatAllErrorMessages(errors);
        return new UnifyResponse(10001, message, method + " " + requestUrl);
    }

    // url上的参数异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public UnifyResponse handleConstraintException(HttpServletRequest req, ConstraintViolationException e) {
        String requestUrl = req.getRequestURI();
        String method = req.getMethod();
        String message = e.getMessage();

        return new UnifyResponse(10001, message, method + " " + requestUrl);
    }

    // 请求方法类型出错
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public UnifyResponse handleHttpRequestMethodException(HttpServletRequest req, ConstraintViolationException e) {
        String requestUrl = req.getRequestURI();
        String method = req.getMethod();
        String message = e.getMessage();

        return new UnifyResponse(10001, message, method + " " + requestUrl);
    }

    private String formatAllErrorMessages(List<ObjectError> errors) {
        StringBuffer errorMsg = new StringBuffer();
        errors.forEach(error ->
                errorMsg.append(error.getDefaultMessage()).append(';')
        );
        return errorMsg.toString();
    }

}
