// package com.eventix.booking.security;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.context.annotation.Bean;

// @Configuration
// public class SecurityConfig {

//     @Autowired
//     private JwtAuthFilter jwtFilter;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//         http.csrf(cs -> cs.disable());

//         http.authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/health", "/version").permitAll()
//                 .anyRequest().authenticated()
//         );

//         http.addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
// }
