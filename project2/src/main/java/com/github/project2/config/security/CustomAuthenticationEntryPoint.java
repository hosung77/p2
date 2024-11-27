package com.github.project2.config.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException; // javax.servlet 사용
import javax.servlet.http.HttpServletRequest; // javax.servlet 사용
import javax.servlet.http.HttpServletResponse; // javax.servlet 사용
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        response.sendRedirect("/exceptions/entrypoint");
    }
}
