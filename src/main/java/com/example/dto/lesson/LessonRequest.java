package com.example.dto.lesson;

import jakarta.validation.constraints.*;

public record LessonRequest(
        @NotBlank String title,
        String content,
        @NotNull Integer position
) {
}