package com.example.exception;


import com.example.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;




@ControllerAdvice("com.example.controller")
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        log.error("系统异常",e);
        return Result.error("系统异常");
    }

    @ExceptionHandler(value = CustomerEeception.class)
    @ResponseBody
    public Result CustomerError(CustomerEeception  e) {
        log.error("自定义异常",e);
        return Result.error(e.getCode(),e.getMsg());
    }
}
