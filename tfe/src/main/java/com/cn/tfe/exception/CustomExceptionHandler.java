package com.cn.tfe.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public Error handleCustomException(CustomException exception){
        return new Error(222,exception.getMsg());
    }

    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    public Error handleDuplicateKey(){
        return new Error(222,"新增单词重复");
    }

    @ExceptionHandler(Exception.class)
    public Error handleException(Exception exception){

        exception.printStackTrace();

        return new Error(500,"internal error");
    }
}
