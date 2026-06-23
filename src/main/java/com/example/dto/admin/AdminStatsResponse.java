package com.example.dto.admin;

public record AdminStatsResponse(
        long totalUsers,
        long totalCourses,
        long totalEnrollments,
        long totalReviews,
        String mostPopularCourse
) {
}