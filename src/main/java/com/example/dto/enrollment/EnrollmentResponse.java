package com.example.dto.enrollment;

import java.time.LocalDateTime;

public record EnrollmentResponse(
        Long id,
        Long studentId,
        String studentName,
        Long courseId,
        String courseTitle,
        LocalDateTime enrolledAt,
        LocalDateTime completedAt
) {
}