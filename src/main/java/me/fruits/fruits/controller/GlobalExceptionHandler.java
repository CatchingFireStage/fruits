package me.fruits.fruits.controller;

import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.utils.FruitsException;
import me.fruits.fruits.utils.FruitsRuntimeException;
import me.fruits.fruits.utils.Result;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * FruitsException异常捕获
     */
    @ExceptionHandler(FruitsException.class)
    @ResponseBody
    public Result<String> FruitsExceptionHandler(HttpServletRequest request, FruitsException ex) {
        return Result.failed(ex);
    }

    @ExceptionHandler(FruitsRuntimeException.class)
    @ResponseBody
    public Result<String> FruitsRuntimeExceptionHandler(HttpServletRequest request, FruitsRuntimeException ex){
        return Result.failed(ex.getMessage());
    }


    /**
     * 所有验证框架异常捕获处理
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Result<String> validationExceptionHandler(BindException exception) {
        return Result.failed(exception.getAllErrors().get(0).getDefaultMessage());

    }
}
