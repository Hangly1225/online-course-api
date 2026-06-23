package com.example.controller;

import com.example.dto.review.*;
import com.example.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/courses/{courseId}")
    public ReviewResponse createReview(
            @PathVariable Long courseId,
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication
    ) {
        return reviewService.createReview(courseId, request, authentication);
    }

    @GetMapping("/courses/{courseId}")
    public List<ReviewResponse> getReviewsByCourse(@PathVariable Long courseId) {
        return reviewService.getReviewsByCourse(courseId);
    }
}