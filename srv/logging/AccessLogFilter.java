package com.eventix.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccessLogFilter implements Filter {

    private static final Logger accessLogger = LoggerFactory.getLogger("access");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            MDC.put("remoteAddr", req.getRemoteAddr());
            MDC.put("method", req.getMethod());
            MDC.put("path", req.getRequestURI());
            MDC.put("query", req.getQueryString() == null ? "" : req.getQueryString());

            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            accessLogger.info("[{}] {} {} ms", req.getMethod(), req.getRequestURI(), duration);
            MDC.clear();
        }
    }
}
