package com.eventix.user_service.service;

import com.eventix.user_service.dto.LoginDTO;
import com.eventix.user_service.dto.JwtResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private com.eventix.user_service.repository.UserRepository userRepository;

    public JwtResponseDTO login(LoginDTO loginDTO) {
        // 1. Find user by email
        var userOpt = userRepository.findByEmail(loginDTO.getEmail());
        if (userOpt.isEmpty()) return null;
        var user = userOpt.get();
        // 2. Check password (should hash and compare, here plain for demo)
        if (!user.getPassword().equals(loginDTO.getPassword())) return null;
        // 3. Generate JWT and refresh token
        java.util.Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRoles().iterator().next().getName());
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
        if (refreshToken == null || refreshToken.isEmpty()) return null;
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
    if (userOpt.isEmpty()) return;
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
