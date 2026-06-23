package com.example.service;

import com.example.domain.entity.Course;
import com.example.domain.entity.User;
import com.example.domain.enums.CourseStatus;
import com.example.dto.course.*;
import com.example.repository.CourseRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseResponse createCourse(CourseRequest request, Authentication authentication) {
        User instructor = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = Course.builder()
                .title(request.title())
                .description(request.description())
                .thumbnailUrl(request.thumbnailUrl())
                .status(request.status())
                .instructor(instructor)
                .build();

        return toResponse(courseRepository.save(course));
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

    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = findCourse(id);

        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setThumbnailUrl(request.thumbnailUrl());
        course.setStatus(request.status());

        return toResponse(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        Course course = findCourse(id);
        courseRepository.delete(course);
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
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