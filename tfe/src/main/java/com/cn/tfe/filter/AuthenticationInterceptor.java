package com.cn.tfe.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.cn.tfe.entity.User;
import com.cn.tfe.exception.CustomException;
import com.cn.tfe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class AuthenticationInterceptor implements HandlerInterceptor{

    @Autowired
    UserRepository userRepository;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;

        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }else{
            //如果类上有passtoken也直接跳过
            if(handlerMethod.getBeanType().isAnnotationPresent(PassToken.class)){
                PassToken tokenAnnotation = handlerMethod.getBeanType().getAnnotation(PassToken.class);
                if (tokenAnnotation.required()) {
                    return true;
                }
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                return authenticate(token);
            }
        }else{
            //如果类上有usertoken也需要验证
            if(handlerMethod.getBeanType().isAnnotationPresent(UserLoginToken.class)){
                UserLoginToken userLoginToken = handlerMethod.getBeanType().getAnnotation(UserLoginToken.class);
                if (userLoginToken.required()) {
                    return authenticate(token);
                }
            }
        }
        return true;
    }


    private boolean authenticate(String token){
        // 执行认证
        if (token == null) {
            throw new CustomException("无token，请重新登录");
        }
        // 获取 token 中的 user id
        String username;
        try {
            username = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new CustomException("401");
        }
        User user = userRepository.findUsersByUsername(username);
        if (user == null) {
            throw new CustomException("用户不存在，请重新登录");
        }
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new CustomException("401");
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}
