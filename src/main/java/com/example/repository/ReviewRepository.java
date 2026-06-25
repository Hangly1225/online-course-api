package com.example.repository;

import com.example.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Review> findByCourseId(Long courseId);

    void deleteByCourseId(Long courseId);
}