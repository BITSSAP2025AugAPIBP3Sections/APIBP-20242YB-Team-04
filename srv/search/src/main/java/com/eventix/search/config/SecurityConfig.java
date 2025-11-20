package com.eventix.search.config;

import com.eventix.search.config.JwtFilter; // adjust package if JwtFilter placed elsewhere
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // PUBLIC (permit all)
                .requestMatchers(
                    "/api/v1/events/search",
                    "/api/v1/events/map",
                    "/api/v1/events/recent",
                    "/api/v1/events/filters",
                    "/api/v1/events/suggestions",
                    "/api/v1/events/calendar",
                    "/api/v1/search/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/api-specs/**",
                    "/openapi.yaml"
                ).permitAll()
                // AUTHENTICATED (explicit)
                .requestMatchers("/graphql").authenticated()
                // fallback: everything else requires authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
