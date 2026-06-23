package com.example.controller;

import com.example.dto.admin.AdminStatsResponse;
import com.example.dto.course.CourseResponse;
import com.example.dto.user.UserResponse;
import com.example.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return adminService.getUsers();
    }

    @GetMapping("/courses")
    public List<CourseResponse> getCourses() {
        return adminService.getCourses();
    }

    @GetMapping("/stats")
    public AdminStatsResponse getStats() {
        return adminService.getStats();
    }
}