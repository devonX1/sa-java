package com.v1.sealert.sa.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
@Order(0)
public class ClientFilter implements Filter {
    @Value("${spring.rest-client.auth-header}")
    private String authHeader;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
                                                                            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String authH = httpServletRequest.getHeader("X-Auth");
        if (authH instanceof String) {
            if (authH.equalsIgnoreCase(authHeader)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //throw new AccessDeniedException("Access denied");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
