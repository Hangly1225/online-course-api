package com.example.dto.course;

import com.example.domain.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseRequest(
        @NotBlank String title,
        String description,
        String thumbnailUrl,
        @NotNull CourseStatus status
) {
}