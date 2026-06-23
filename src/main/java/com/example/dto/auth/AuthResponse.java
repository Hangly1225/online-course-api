package com.example.dto.auth;

public record AuthResponse(
        String token,
        Long userId,
        String fullName,
        String email,
        String role
) {
}