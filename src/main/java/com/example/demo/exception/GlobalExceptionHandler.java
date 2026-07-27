package com.example.demo.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import com.example.demo.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

//业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(
            BusinessException e){

        return Result.error(e.getMessage());

    }
    //参数异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidException(
            MethodArgumentNotValidException e){

        String message =
                e.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage();


        return Result.error(message);

    }

}