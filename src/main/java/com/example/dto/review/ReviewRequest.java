package com.example.dto.review;

import jakarta.validation.constraints.*;

public record ReviewRequest(
        @Min(1) @Max(5) Integer rating,
        String comment
) {
}