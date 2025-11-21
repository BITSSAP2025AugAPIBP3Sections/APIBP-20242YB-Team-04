package com.eventix.booking.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final String SECRET = "your-secret-key"; // MUST MATCH user-service secret

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, java.io.IOException {
            String path = request.getServletPath();

                // ⛔ Skip authentication for public paths
             if (path.startsWith("/auth/login") || path.startsWith("/auth/refresh")) {
                    filterChain.doFilter(request, response);
                    return;
                }
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {

            String token = auth.substring(7);

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token)
                        .getBody();

                String role = (String) claims.get("role");
                request.setAttribute("role", role);

            } catch (Exception e) {
                response.setStatus(401);
                response.getWriter().write("Invalid token");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
