package com.example.dto.user;

import com.example.domain.enums.Role;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        Role role
) {
}