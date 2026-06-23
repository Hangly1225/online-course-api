package com.example.service;

import com.example.domain.entity.*;
import com.example.dto.review.*;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;

    public ReviewResponse createReview(
            Long courseId,
            ReviewRequest request,
            Authentication authentication
    ) {
        User student = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = enrollmentRepository
                .findByStudentIdAndCourseId(student.getId(), courseId)
                .orElseThrow(() -> new RuntimeException("You must enroll before reviewing"));

        if (enrollment.getCompletedAt() == null) {
            throw new RuntimeException("You can review only after completing the course");
        }

        reviewRepository.findByStudentIdAndCourseId(student.getId(), courseId)
                .ifPresent(review -> {
                    throw new RuntimeException("You already reviewed this course");
                });

        Review review = Review.builder()
                .student(student)
                .course(course)
                .rating(request.rating())
                .comment(request.comment())
                .build();

        return toResponse(reviewRepository.save(review));
    }

    public java.util.List<ReviewResponse> getReviewsByCourse(Long courseId) {
        return reviewRepository.findByCourseId(courseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getCourse().getId(),
                review.getCourse().getTitle(),
                review.getStudent().getId(),
                review.getStudent().getFullName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}