package com.example.repository;

import com.example.domain.entity.Course;
import com.example.domain.entity.Enrollment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
// import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Enrollment> findByStudentId(Long studentId);

    long countByCourseId(Long courseId);

    @Query("SELECT e.course FROM Enrollment e GROUP BY e.course ORDER BY COUNT(e.id) DESC")
    List<Course> findMostPopularCourses(Pageable pageable);
}