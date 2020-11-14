package com.cn.tfe.exception;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public Error handleCustomException(CustomException exception){
        return new Error(222,exception.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public Error handleException(Exception exception){
        return new Error(500,"internal error");
    }
}
