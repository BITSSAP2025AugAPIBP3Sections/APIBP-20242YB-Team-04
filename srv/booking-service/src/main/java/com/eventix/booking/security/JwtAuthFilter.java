// package com.eventix.booking.security;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import java.io.IOException;

// @Component
// public class JwtAuthFilter extends OncePerRequestFilter {

//     @Override
//     protected void doFilterInternal(
//             HttpServletRequest request,
//             HttpServletResponse response,
//             FilterChain filterChain
//     ) throws ServletException, IOException {

//         // If no token → just continue
//         String auth = request.getHeader("Authorization");
//         if (auth == null || !auth.startsWith("Bearer ")) {
//             filterChain.doFilter(request, response);
//             return;
//         }

//         // extract token
//         String token = auth.substring(7);

//         // TODO: validate token (later)
//         // Set dummy role so your deleteBooking works again
//         request.setAttribute("role", "ADMIN");

//         filterChain.doFilter(request, response);
//     }
// }
