package com.example.controller;

import com.example.dto.lesson.LessonRequest;
import com.example.dto.lesson.LessonResponse;
import com.example.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/courses/{courseId}/lessons")
    public LessonResponse createLesson(
            @PathVariable Long courseId,
            @Valid @RequestBody LessonRequest request,
            Authentication authentication
    ) {
        return lessonService.createLesson(courseId, request, authentication);
    }

    @GetMapping("/courses/{courseId}/lessons")
    public List<LessonResponse> getLessonsByCourse(@PathVariable Long courseId) {
        return lessonService.getLessonsByCourse(courseId);
    }

    @GetMapping("/lessons/{lessonId}")
    public LessonResponse getLessonById(@PathVariable Long lessonId) {
        return lessonService.getLessonById(lessonId);
    }

    @PutMapping("/lessons/{lessonId}")
    public LessonResponse updateLesson(
            @PathVariable Long lessonId,
            @Valid @RequestBody LessonRequest request,
            Authentication authentication
    ) {
        return lessonService.updateLesson(lessonId, request, authentication);
    }

    @DeleteMapping("/lessons/{lessonId}")
    public void deleteLesson(
            @PathVariable Long lessonId,
            Authentication authentication
    ) {
        lessonService.deleteLesson(lessonId, authentication);
    }
}