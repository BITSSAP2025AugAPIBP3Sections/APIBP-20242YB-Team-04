package com.eventix.user_service.service;

import com.eventix.user_service.dto.LoginDTO;
import com.eventix.user_service.dto.JwtResponseDTO;

public interface AuthService {
    JwtResponseDTO login(LoginDTO loginDTO);
    JwtResponseDTO refreshToken(String refreshToken);
    void sendForgotPassword(String email);
    void resetPassword(String token, String newPassword);
    void verifyEmail(String token);
    boolean validateToken(String token);
}
