package com.github.project2.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException; // javax.servlet 사용
import javax.servlet.http.HttpServletRequest; // javax.servlet 사용
import javax.servlet.http.HttpServletResponse; // javax.servlet 사용
import java.io.IOException;
@Slf4j
@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        accessDeniedException.printStackTrace();
        response.sendRedirect("/exceptions/access-denied");
    }
}
