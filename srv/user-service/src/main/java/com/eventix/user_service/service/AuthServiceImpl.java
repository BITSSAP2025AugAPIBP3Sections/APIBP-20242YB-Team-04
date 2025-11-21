package com.eventix.user_service.service;

import com.eventix.user_service.dto.LoginDTO;
import com.eventix.user_service.dto.JwtResponseDTO;
import com.eventix.user_service.exception.NotFoundException;
import com.eventix.user_service.exception.UnauthorizedException;
import com.eventix.user_service.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private com.eventix.user_service.repository.UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public JwtResponseDTO login(LoginDTO loginDTO) {
        // 1. Find user by email
        var userOpt = userRepository.findByEmail(loginDTO.getEmail());
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found with email: " + loginDTO.getEmail());
        }
        var user = userOpt.get();
        // 2. Check password using BCrypt
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        // 3. Generate JWT and refresh token
        java.util.Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("userId", user.getId());
        // Add role if user has roles, otherwise default to "user"
        String role = user.getRoles() != null && !user.getRoles().isEmpty() 
            ? user.getRoles().iterator().next().getName() 
            : "user";
        claims.put("role", role);
        claims.put("organizerId", user.getOrganizerId());
        String accessToken = jwtUtil.generateToken(claims, user.getEmail());
        String refreshToken = java.util.UUID.randomUUID().toString(); // For demo, use UUID
        // 4. Return JwtResponseDTO
        JwtResponseDTO response = new JwtResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    @Override
    public JwtResponseDTO refreshToken(String refreshToken) {
        // For demo: accept any non-empty refresh token
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new BadRequestException("Refresh token is required");
        }
        // In production, validate refresh token and get user info
        // Here, just return a dummy token for demonstration
        // You would typically look up the user by refresh token
        // For now, return a new access token with dummy claims
        java.util.Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("userId", 1L);
        claims.put("role", "attendee");
        claims.put("organizerId", null);
        String accessToken = jwtUtil.generateToken(claims, "demo@eventix.com");
        JwtResponseDTO response = new JwtResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    @Override
    public void sendForgotPassword(String email) {
        // 1. Find user by email
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found with email: " + email);
        }
        // 2. Generate reset token (UUID for demo)
        String resetToken = java.util.UUID.randomUUID().toString();
        // 3. Log the token (in production, send email)
        System.out.println("Password reset token for " + email + ": " + resetToken);
        // TODO: Save token and associate with user for later validation
    }

    public void resetPassword(String token, String newPassword) {
        // DEMO: In production, validate token and find user
        // Here, just print the action
        System.out.println("Resetting password for token: " + token);
        // TODO: Lookup user by token, hash password, update user, invalidate token
    }

    @Override
    public void verifyEmail(String token) {
        // DEMO: In production, validate token and find user
        // Here, just print the action
        System.out.println("Verifying email for token: " + token);
        // TODO: Lookup user by token, set emailVerified=true, invalidate token
    }

    @Autowired
    private com.eventix.user_service.config.JwtUtil jwtUtil;

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
