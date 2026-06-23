package com.example.dto.enrollment;

import java.time.LocalDateTime;

public record ProgressResponse(
        Long id,
        Long lessonId,
        String lessonTitle,
        Boolean completed,
        LocalDateTime completedAt
) {
}