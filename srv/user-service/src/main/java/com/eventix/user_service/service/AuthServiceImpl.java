package com.eventix.user_service.service;

import com.eventix.user_service.dto.LoginDTO;
import com.eventix.user_service.dto.JwtResponseDTO;
import com.eventix.user_service.exception.NotFoundException;
import com.eventix.user_service.exception.UnauthorizedException;
import com.eventix.user_service.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger access = LogManager.getLogger("access");
    private static final Logger error  = LogManager.getLogger("error");
    private static final Logger debug  = LogManager.getLogger("debug");

    @Autowired
    private com.eventix.user_service.repository.UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private com.eventix.user_service.config.JwtUtil jwtUtil;

    @Override
    public JwtResponseDTO login(LoginDTO loginDTO) {

        debug.info("Attempting login for email={}", loginDTO.getEmail());

        var userOpt = userRepository.findByEmail(loginDTO.getEmail());
        if (userOpt.isEmpty()) {
            error.error("Login failed: user not found email={}", loginDTO.getEmail());
            throw new NotFoundException("User not found with email: " + loginDTO.getEmail());
        }

        var user = userOpt.get();

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            error.error("Login failed: wrong password email={}", loginDTO.getEmail());
            throw new UnauthorizedException("Invalid credentials");
        }

        debug.info("Password valid for email={}", loginDTO.getEmail());

        // Claims
        java.util.Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("userId", user.getId());
        String role = user.getRoles() != null && !user.getRoles().isEmpty()
                ? user.getRoles().iterator().next().getName()
                : "user";
        claims.put("role", role);
        claims.put("organizerId", user.getOrganizerId());

        String accessToken = jwtUtil.generateToken(claims, user.getEmail());
        String refreshToken = java.util.UUID.randomUUID().toString();

        access.info("Login successful userId={} email={}", user.getId(), user.getEmail());

        JwtResponseDTO response = new JwtResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    @Override
    public JwtResponseDTO refreshToken(String refreshToken) {

        debug.info("Refresh token request token={}", refreshToken);

        if (refreshToken == null || refreshToken.isEmpty()) {
            error.error("Refresh token failed: token missing");
            throw new BadRequestException("Refresh token is required");
        }

        java.util.Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("userId", 1L);
        claims.put("role", "attendee");

        String accessToken = jwtUtil.generateToken(claims, "demo@eventix.com");

        access.info("Refresh token successful for token={}", refreshToken);

        JwtResponseDTO response = new JwtResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    @Override
    public void sendForgotPassword(String email) {

        debug.info("Forgot password request for email={}", email);

        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            error.error("Forgot password failed: user not found email={}", email);
            throw new NotFoundException("User not found with email: " + email);
        }

        String resetToken = java.util.UUID.randomUUID().toString();
        access.info("Generated password reset token for email={}", email);

        // In production: send via email
        debug.info("Reset token={} for email={}", resetToken, email);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        debug.info("Reset password requested token={}", token);
        access.info("Password reset operation token={}", token);
    }

    @Override
    public void verifyEmail(String token) {
        debug.info("Email verification requested token={}", token);
        access.info("Email verification token={}", token);
    }

    public boolean validateToken(String token) {
        debug.info("Validating JWT token");
        return jwtUtil.validateToken(token);
    }
}
