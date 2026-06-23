// src/main/java/com/example/ocms/controller/LessonController.java
package com.example.controller;

import com.example.dto.lesson.*;
import com.example.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
            @Valid @RequestBody LessonRequest request
    ) {
        return lessonService.createLesson(courseId, request);
    }

    @GetMapping("/courses/{courseId}/lessons")
    public List<LessonResponse> getLessonsByCourse(@PathVariable Long courseId) {
        return lessonService.getLessonsByCourse(courseId);
    }

    @PutMapping("/lessons/{lessonId}")
    public LessonResponse updateLesson(
            @PathVariable Long lessonId,
            @Valid @RequestBody LessonRequest request
    ) {
        return lessonService.updateLesson(lessonId, request);
    }

    @DeleteMapping("/lessons/{lessonId}")
    public void deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
    }
}