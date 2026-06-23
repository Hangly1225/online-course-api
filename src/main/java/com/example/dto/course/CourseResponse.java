package com.example.dto.course;

import com.example.domain.enums.CourseStatus;

public record CourseResponse(
        Long id,
        String title,
        String description,
        String thumbnailUrl,
        CourseStatus status,
        Long instructorId,
        String instructorName
) {
}