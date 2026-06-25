package com.example.service;

import com.example.domain.entity.Course;
import com.example.domain.entity.Lesson;
import com.example.domain.entity.User;
import com.example.domain.enums.Role;
import com.example.dto.lesson.LessonRequest;
import com.example.dto.lesson.LessonResponse;
import com.example.repository.CourseRepository;
import com.example.repository.LessonProgressRepository;
import com.example.repository.LessonRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final LessonProgressRepository progressRepository;

    @Transactional
    public LessonResponse createLesson(
            Long courseId,
            LessonRequest request,
            Authentication authentication
    ) {
        User currentUser = getCurrentUser(authentication);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        checkCourseOwnerOrAdmin(course, currentUser);

        Lesson lesson = Lesson.builder()
                .title(request.title())
                .content(request.content())
                .position(request.position())
                .course(course)
                .build();

        Lesson savedLesson = lessonRepository.save(lesson);

        return toResponse(savedLesson);
    }

    public List<LessonResponse> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseIdOrderByPositionAsc(courseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LessonResponse getLessonById(Long lessonId) {
        Lesson lesson = findLesson(lessonId);
        return toResponse(lesson);
    }

    @Transactional
    public LessonResponse updateLesson(
            Long lessonId,
            LessonRequest request,
            Authentication authentication
    ) {
        User currentUser = getCurrentUser(authentication);

        Lesson lesson = findLesson(lessonId);

        checkCourseOwnerOrAdmin(lesson.getCourse(), currentUser);

        lesson.setTitle(request.title());
        lesson.setContent(request.content());
        lesson.setPosition(request.position());

        Lesson updatedLesson = lessonRepository.save(lesson);

        return toResponse(updatedLesson);
    }

    @Transactional
    public void deleteLesson(
            Long lessonId,
            Authentication authentication
    ) {
        User currentUser = getCurrentUser(authentication);

        Lesson lesson = findLesson(lessonId);

        checkCourseOwnerOrAdmin(lesson.getCourse(), currentUser);

        progressRepository.deleteByLessonId(lessonId);

        lessonRepository.delete(lesson);
    }

    private Lesson findLesson(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    private User getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void checkCourseOwnerOrAdmin(Course course, User user) {
        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = course.getInstructor().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("You do not have permission to modify lessons in this course");
        }
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