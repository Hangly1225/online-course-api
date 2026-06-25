package com.example.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LessonRequest(
        @NotBlank String title,
        String content,
        @NotNull Integer position
) {
}