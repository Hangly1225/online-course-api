package com.example.service;

import com.example.domain.entity.Course;
import com.example.domain.entity.Lesson;
import com.example.dto.lesson.*;
import com.example.repository.CourseRepository;
import com.example.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public LessonResponse createLesson(Long courseId, LessonRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Lesson lesson = Lesson.builder()
                .title(request.title())
                .content(request.content())
                .position(request.position())
                .course(course)
                .build();

        return toResponse(lessonRepository.save(lesson));
    }

    public List<LessonResponse> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseIdOrderByPositionAsc(courseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LessonResponse updateLesson(Long lessonId, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        lesson.setTitle(request.title());
        lesson.setContent(request.content());
        lesson.setPosition(request.position());

        return toResponse(lessonRepository.save(lesson));
    }

    public void deleteLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        lessonRepository.delete(lesson);
    }

    private LessonResponse toResponse(Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getCourse().getId(),
                lesson.getTitle(),
                lesson.getContent(),
                lesson.getPosition()
        );
    }
}