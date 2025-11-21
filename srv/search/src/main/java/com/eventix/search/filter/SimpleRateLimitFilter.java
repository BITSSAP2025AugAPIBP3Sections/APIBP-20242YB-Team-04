package com.eventix.search.filter;

import com.eventix.search.exception.TooManyRequestsException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Very small in-memory per-IP limiter for dev/local use.
 * Single-node only.
 */
@Component
public class SimpleRateLimitFilter implements Filter {

    private static class Window {
        volatile Instant start;
        final AtomicInteger cnt = new AtomicInteger(0);
    }

    private final Map<String, Window> store = new ConcurrentHashMap<>();
    private final int windowSeconds = 60;
    private final int maxRequests = 30;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (!(req instanceof HttpServletRequest)) {
            chain.doFilter(req, res);
            return;
        }
        HttpServletRequest r = (HttpServletRequest) req;
        String ip = r.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) ip = r.getRemoteAddr();

        Window w = store.computeIfAbsent(ip, k -> { Window n = new Window(); n.start = Instant.now(); return n; });

        synchronized (w) {
            Instant now = Instant.now();
            if (now.isAfter(w.start.plusSeconds(windowSeconds))) {
                w.start = now;
                w.cnt.set(0);
            }
            int cur = w.cnt.incrementAndGet();
            if (cur > maxRequests) throw new TooManyRequestsException("Rate limit exceeded.");
        }

        chain.doFilter(req, res);
    }
}
