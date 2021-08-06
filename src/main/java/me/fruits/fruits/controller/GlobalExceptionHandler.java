package me.fruits.fruits.controller;

import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.utils.FruitsException;
import me.fruits.fruits.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * FruitsException异常捕获
     */
    @ExceptionHandler(FruitsException.class)
    @ResponseBody
    public Result FruitsExceptionHandler(HttpServletRequest request, FruitsException ex) {
        return Result.FAILED(ex);
    }
}
