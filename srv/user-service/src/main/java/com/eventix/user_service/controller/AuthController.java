package com.eventix.user_service.controller;

import com.eventix.user_service.dto.LoginDTO;
import com.eventix.user_service.dto.JwtResponseDTO;
import com.eventix.user_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public JwtResponseDTO login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    @PostMapping("/refresh")
    public JwtResponseDTO refresh(@RequestBody String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestBody String email) {
        authService.sendForgotPassword(email);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        authService.resetPassword(token, newPassword);
    }

    @PostMapping("/verify-email")
    public void verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
    }

    @GetMapping("/validate")
    public boolean validate(@RequestParam String token) {
        return authService.validateToken(token);
    }
}
