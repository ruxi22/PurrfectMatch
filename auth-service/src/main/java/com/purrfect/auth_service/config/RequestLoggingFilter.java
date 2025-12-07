package com.purrfect.auth_service.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String path = httpRequest.getRequestURI();
            
            //log all requests
            if (path.startsWith("/auth")) {
                logger.info("==========================================");
                logger.info("INCOMING REQUEST TO AUTH-SERVICE");
                logger.info("Method: {}", httpRequest.getMethod());
                logger.info("Path: {}", path);
                logger.info("Query String: {}", httpRequest.getQueryString());
                logger.info("Content Type: {}", httpRequest.getContentType());
                logger.info("==========================================");
            }
        }
        
        chain.doFilter(request, response);
    }
}

