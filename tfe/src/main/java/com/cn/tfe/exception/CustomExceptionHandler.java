package com.cn.tfe.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public Error handleCustomException(CustomException exception){
        return new Error(500,exception.getMsg());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Error handleDuplicateKey(DuplicateKeyException duplicateKeyException){
        return new Error(500,"新增重复,请检查唯一约束值");
    }

    @ExceptionHandler(Exception.class)
    public Error handleException(Exception exception){

        exception.printStackTrace();

        return new Error(500,"internal error");
    }
}
