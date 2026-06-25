package com.example.service;

import com.example.domain.entity.Course;
import com.example.domain.entity.Enrollment;
import com.example.domain.entity.User;
import com.example.domain.enums.CourseStatus;
import com.example.domain.enums.Role;
import com.example.dto.course.CourseRequest;
import com.example.dto.course.CourseResponse;
import com.example.repository.CourseRepository;
import com.example.repository.EnrollmentRepository;
import com.example.repository.LessonProgressRepository;
import com.example.repository.LessonRepository;
import com.example.repository.ReviewRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressRepository progressRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public CourseResponse createCourse(
            CourseRequest request,
            Authentication authentication
    ) {
        User instructor = getCurrentUser(authentication);

        Course course = Course.builder()
                .title(request.title())
                .description(request.description())
                .thumbnailUrl(request.thumbnailUrl())
                .status(request.status())
                .instructor(instructor)
                .build();

        Course savedCourse = courseRepository.save(course);

        return toResponse(savedCourse);
    }

    public List<CourseResponse> getActiveCourses() {
        return courseRepository.findByStatus(CourseStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CourseResponse getCourseById(Long id) {
        Course course = findCourse(id);
        return toResponse(course);
    }

    @Transactional
    public CourseResponse updateCourse(
            Long id,
            CourseRequest request,
            Authentication authentication
    ) {
        User currentUser = getCurrentUser(authentication);
        Course course = findCourse(id);

        checkCourseOwnerOrAdmin(course, currentUser);

        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setThumbnailUrl(request.thumbnailUrl());
        course.setStatus(request.status());

        Course updatedCourse = courseRepository.save(course);

        return toResponse(updatedCourse);
    }

    @Transactional
    public void deleteCourse(
            Long id,
            Authentication authentication
    ) {
        User currentUser = getCurrentUser(authentication);
        Course course = findCourse(id);

        checkCourseOwnerOrAdmin(course, currentUser);

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(id);

        for (Enrollment enrollment : enrollments) {
            progressRepository.deleteByEnrollmentId(enrollment.getId());
        }

        reviewRepository.deleteByCourseId(id);
        enrollmentRepository.deleteByCourseId(id);
        lessonRepository.deleteByCourseId(id);

        courseRepository.delete(course);
    }

    private User getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    private void checkCourseOwnerOrAdmin(Course course, User user) {
        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = course.getInstructor().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("You do not have permission to modify this course");
        }
    }

    private CourseResponse toResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getThumbnailUrl(),
                course.getStatus(),
                course.getInstructor().getId(),
                course.getInstructor().getFullName()
        );
    }
}