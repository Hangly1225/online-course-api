package com.example.dto.lesson;

public record LessonResponse(
        Long id,
        Long courseId,
        String title,
        String content,
        Integer position
) {
}