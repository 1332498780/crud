package com.cn.tfe.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CorsFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest instanceof HttpServletRequest){
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response  = (HttpServletResponse) servletResponse;
            StringBuilder sb = new StringBuilder();
            sb.append(request.getRemoteHost()).append("-").append(request.getRemotePort());
            log.info(sb.toString());
//            if(request.getMethod().equals(HttpMethod.OPTIONS)){
//                response.setHeader("Access-Control-Allow-Origin", "*");
//                response.setHeader("Access-Control-Allow-Methods", "*");
//                response.setHeader("Access-Control-Allow-Headers","Content-Type,token,Cookie");
//            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
