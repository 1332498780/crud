package com.cn.tfe.anntations;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    // controller包下所有的类
    @Pointcut("execution(public * com.cn.tfe.controller.*.*(..))")
    public void pointCut(){}


    @Around("pointCut()")
    public void aroundFunc(ProceedingJoinPoint proceedingJoinPoint){
        long startMillis = System.currentTimeMillis();
        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long endMillis = System.currentTimeMillis();
        long costSeconds = (endMillis - startMillis)/1000;
        log.info("执行耗时："+costSeconds);
    }

}
