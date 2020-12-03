package com.cn.tfe.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class BridgeAspect {

    @Pointcut("@annotation(com.cn.tfe.aop.Bridge)")
    public void cutMethod(){}


    @Before("cutMethod()")
    public void preHandle(ProceedingJoinPoint joinPoint){
    }

    public void getDeclaredAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);
        // 拿到方法定义的注解信息
//        Lingyejun annotation = objMethod.getDeclaredAnnotation(Lingyejun.class);

    }
}
