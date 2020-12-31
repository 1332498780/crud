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
@Slf4j
@Component
public class LogAnnoAspect {

    @Around("@annotation(com.cn.tfe.anntations.LogAnno)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint){
        long startTime = System.currentTimeMillis();
        Map<String,Object> record = new HashMap<>();
        Object res = null;
        String className = proceedingJoinPoint.getTarget().getClass().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Object[] objects = proceedingJoinPoint.getArgs();
        record.put("className",className);
        record.put("methodName",methodName);
        record.put("params",objects);
        try {
            res = proceedingJoinPoint.proceed();
            return res;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }finally {
            long endTime = System.currentTimeMillis();
            long costSecondes = endTime - startTime;
            record.put("costSeconds",costSecondes);
            log.info("执行结果：{}",record);
        }
        return null;
    }
}
