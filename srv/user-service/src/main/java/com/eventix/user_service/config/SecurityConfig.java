package com.eventix.user_service.config;
import com.eventix.user_service.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/users/register**",
                                "/users/validate/**",
                                "/users/details/**",
                                "/version",
                                "/health",
                                "/openapi.yaml",
                                "/swagger-ui/**",
                                "/api-specs/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs/swagger-config",
                                "/graphql"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            
                            ErrorResponse error = new ErrorResponse(
                                "Unauthorized",
                                "Authentication required. Please provide a valid JWT token in the Authorization header.",
                                HttpStatus.UNAUTHORIZED.value(),
                                request.getRequestURI(),
                                java.util.UUID.randomUUID().toString()
                            );
                            
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
                            response.getWriter().write(mapper.writeValueAsString(error));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            
                            ErrorResponse error = new ErrorResponse(
                                "Forbidden",
                                "Access denied. You don't have permission to access this resource.",
                                HttpStatus.FORBIDDEN.value(),
                                request.getRequestURI(),
                                java.util.UUID.randomUUID().toString()
                            );
                            
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
                            response.getWriter().write(mapper.writeValueAsString(error));
                        })
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
