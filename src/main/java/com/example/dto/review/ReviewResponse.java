package com.example.dto.review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long courseId,
        String courseTitle,
        Long studentId,
        String studentName,
        Integer rating,
        String comment,
        LocalDateTime createdAt
) {
}