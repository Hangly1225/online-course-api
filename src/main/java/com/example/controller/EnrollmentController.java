package com.example.controller;

import com.example.dto.enrollment.EnrollmentResponse;
import com.example.dto.enrollment.ProgressResponse;
import com.example.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/courses/{courseId}")
    public EnrollmentResponse enroll(
            @PathVariable Long courseId,
            Authentication authentication
    ) {
        return enrollmentService.enroll(courseId, authentication);
    }

    @PostMapping("/courses/{courseId}/lessons/{lessonId}/complete")
    public ProgressResponse completeLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            Authentication authentication
    ) {
        return enrollmentService.completeLesson(courseId, lessonId, authentication);
    }

    @GetMapping("/courses/{courseId}/progress")
    public List<ProgressResponse> getProgress(
            @PathVariable Long courseId,
            Authentication authentication
    ) {
        return enrollmentService.getProgress(courseId, authentication);
    }
}