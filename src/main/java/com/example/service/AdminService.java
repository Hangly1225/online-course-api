package com.example.service;

import com.example.domain.entity.User;
import com.example.dto.admin.AdminStatsResponse;
import com.example.dto.course.CourseResponse;
import com.example.dto.user.UserResponse;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;

    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserResponse)
                .toList();
    }

    public List<CourseResponse> getCourses() {
        return courseRepository.findAll()
                .stream()
                .map(course -> new CourseResponse(
                        course.getId(),
                        course.getTitle(),
                        course.getDescription(),
                        course.getThumbnailUrl(),
                        course.getStatus(),
                        course.getInstructor().getId(),
                        course.getInstructor().getFullName()
                ))
                .toList();
    }

    public AdminStatsResponse getStats() {
        var popularCourses = enrollmentRepository.findMostPopularCourses(PageRequest.of(0, 1));

        String mostPopularCourse = popularCourses.isEmpty()
                ? null
                : popularCourses.get(0).getTitle();

        return new AdminStatsResponse(
                userRepository.count(),
                courseRepository.count(),
                enrollmentRepository.count(),
                reviewRepository.count(),
                mostPopularCourse
        );
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );
    }
}