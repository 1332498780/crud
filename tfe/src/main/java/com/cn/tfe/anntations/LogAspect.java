package com.cn.tfe.anntations;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
//@Component
@Slf4j
public class LogAspect {

    // controller包下所有的类
    @Pointcut("execution(public * com.cn.tfe.controller.*.*(..))")
    public void pointCut(){}


    @Around("pointCut()")
    public Object aroundFunc(ProceedingJoinPoint proceedingJoinPoint){
        long startMillis = System.currentTimeMillis();
        Object object = null;
        Map<String,Object> record = new HashMap<>();
        try {
            object =  proceedingJoinPoint.proceed();
            String className = proceedingJoinPoint.getTarget().getClass().getSimpleName();
            String methodName = proceedingJoinPoint.getSignature().getName();
            Object[] params = proceedingJoinPoint.getArgs();
            record.put("className",className);
            record.put("methodName",methodName);
            record.put("requestParam",params);
            record.put("result",object);
            long endMillis = System.currentTimeMillis();
            long costSeconds = (endMillis - startMillis)/1000;
            record.put("costSeconds",costSeconds);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        log.info("执行情况:{}",record);
        return object;
    }

}
