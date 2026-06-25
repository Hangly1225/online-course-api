package com.example.repository;

import com.example.domain.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    Optional<LessonProgress> findByEnrollmentIdAndLessonId(Long enrollmentId, Long lessonId);

    List<LessonProgress> findByEnrollmentId(Long enrollmentId);

    long countByEnrollmentIdAndCompletedTrue(Long enrollmentId);

    void deleteByLessonId(Long lessonId);

    void deleteByEnrollmentId(Long enrollmentId);
}