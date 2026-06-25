package com.example.repository;

import com.example.domain.entity.Course;
import com.example.domain.entity.Enrollment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    long countByCourseId(Long courseId);

    void deleteByCourseId(Long courseId);

    @Query("SELECT e.course FROM Enrollment e GROUP BY e.course ORDER BY COUNT(e.id) DESC")
    List<Course> findMostPopularCourses(Pageable pageable);
}