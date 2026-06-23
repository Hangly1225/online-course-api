package com.example.service;

import com.example.domain.entity.Course;
import com.example.domain.entity.Enrollment;
import com.example.domain.entity.Lesson;
import com.example.domain.entity.LessonProgress;
import com.example.domain.entity.User;
import com.example.domain.enums.CourseStatus;
import com.example.dto.enrollment.EnrollmentResponse;
import com.example.dto.enrollment.ProgressResponse;
import com.example.repository.CourseRepository;
import com.example.repository.EnrollmentRepository;
import com.example.repository.LessonProgressRepository;
import com.example.repository.LessonRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

private final UserRepository userRepository;
private final CourseRepository courseRepository;
private final EnrollmentRepository enrollmentRepository;
private final LessonRepository lessonRepository;
private final LessonProgressRepository progressRepository;

@Transactional
public EnrollmentResponse enroll(Long courseId, Authentication authentication) {
    User student = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

    if (course.getStatus() != CourseStatus.ACTIVE) {
        throw new RuntimeException("Course is not active");
    }

    enrollmentRepository.findByStudentIdAndCourseId(student.getId(), courseId)
            .ifPresent(enrollment -> {
                throw new RuntimeException("Already enrolled in this course");
            });

    Enrollment enrollment = Enrollment.builder()
            .student(student)
            .course(course)
            .build();

    Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

    List<Lesson> lessons = lessonRepository.findByCourseIdOrderByPositionAsc(courseId);

    for (Lesson lesson : lessons) {
        LessonProgress progress = LessonProgress.builder()
                .enrollment(savedEnrollment)
                .lesson(lesson)
                .completed(false)
                .build();

        progressRepository.save(progress);
    }

    return toResponse(savedEnrollment);
}

@Transactional
public ProgressResponse completeLesson(
        Long courseId,
        Long lessonId,
        Authentication authentication
) {
    User student = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Enrollment enrollment = enrollmentRepository
            .findByStudentIdAndCourseId(student.getId(), courseId)
            .orElseThrow(() -> new RuntimeException("You are not enrolled in this course"));

    Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));

    if (!lesson.getCourse().getId().equals(courseId)) {
        throw new RuntimeException("Lesson does not belong to this course");
    }

    LessonProgress progress = progressRepository
            .findByEnrollmentIdAndLessonId(enrollment.getId(), lessonId)
            .orElseGet(() -> LessonProgress.builder()
                    .enrollment(enrollment)
                    .lesson(lesson)
                    .completed(false)
                    .build());

    progress.setCompleted(true);
    progress.setCompletedAt(LocalDateTime.now());

    LessonProgress savedProgress = progressRepository.save(progress);

    checkCourseCompleted(enrollment);

    return toProgressResponse(savedProgress);
}

public List<ProgressResponse> getProgress(
        Long courseId,
        Authentication authentication
) {
    User student = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Enrollment enrollment = enrollmentRepository
            .findByStudentIdAndCourseId(student.getId(), courseId)
            .orElseThrow(() -> new RuntimeException("You are not enrolled in this course"));

    return progressRepository.findByEnrollmentId(enrollment.getId())
            .stream()
            .map(this::toProgressResponse)
            .toList();
}

private void checkCourseCompleted(Enrollment enrollment) {
    long totalLessons = lessonRepository.countByCourseId(enrollment.getCourse().getId());

    long completedLessons = progressRepository
            .countByEnrollmentIdAndCompletedTrue(enrollment.getId());

    if (totalLessons > 0 && totalLessons == completedLessons) {
        enrollment.setCompletedAt(LocalDateTime.now());
        enrollmentRepository.save(enrollment);
    }
}

private EnrollmentResponse toResponse(Enrollment enrollment) {
    return new EnrollmentResponse(
            enrollment.getId(),
            enrollment.getStudent().getId(),
            enrollment.getStudent().getFullName(),
            enrollment.getCourse().getId(),
            enrollment.getCourse().getTitle(),
            enrollment.getEnrolledAt(),
            enrollment.getCompletedAt()
    );
}

private ProgressResponse toProgressResponse(LessonProgress progress) {
    return new ProgressResponse(
            progress.getId(),
            progress.getLesson().getId(),
            progress.getLesson().getTitle(),
            progress.getCompleted(),
            progress.getCompletedAt()
    );
}

}
