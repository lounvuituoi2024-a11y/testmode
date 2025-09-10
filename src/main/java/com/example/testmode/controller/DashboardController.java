package com.example.testmode.controller;

import com.example.testmode.model.NguoiDung;
import com.example.testmode.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

import java.util.List;

@Controller
public class DashboardController {
    
    @Autowired
    private NguoiDungService nguoiDungService;
    
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        // Lấy thống kê
        long totalUsers = nguoiDungService.getTotalUsers();
        long totalActiveUsers = nguoiDungService.countByTrangThai("HOATDONG");
        long totalInactiveUsers = nguoiDungService.countByTrangThai("KHOA");
        long totalAdmins = nguoiDungService.countByQuyen("ADMIN");
        long totalEmployees = nguoiDungService.countByQuyen("NHANVIEN");
        long totalCustomers = nguoiDungService.countByQuyen("KHACHHANG");
        
        // Lấy 5 user mới nhất
        Pageable pageable = PageRequest.of(0, 5);
        List<NguoiDung> recentUsers = nguoiDungService.getAllUsers(pageable).getContent();
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalActiveUsers", totalActiveUsers);
        model.addAttribute("totalInactiveUsers", totalInactiveUsers);
        model.addAttribute("totalAdmins", totalAdmins);
        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("recentUsers", recentUsers);
        
        return "admin/dashboard";
    }
    
    @GetMapping("/employee/dashboard")
    public String employeeDashboard(Model model) {
        return "employee/dashboard";
    }
    
    @GetMapping("/customer/dashboard")
    public String customerDashboard(Model model, Principal principal) {
        if (principal != null) {
            Optional<NguoiDung> userOpt = nguoiDungService.findByUsername(principal.getName());
            if (userOpt.isPresent()) {
                model.addAttribute("currentUser", userOpt.get());
            }
        }
        return "customer/dashboard";
    }
    
    @GetMapping("/admin/users")
    public String adminUsers(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(defaultValue = "") String search,
                           @RequestParam(defaultValue = "all") String role,
                           @RequestParam(defaultValue = "all") String status) {
        
        try {
            Page<NguoiDung> users;
            Pageable pageable = PageRequest.of(page, size);
            
            // Tìm kiếm và lọc users
            if (!search.isEmpty() || !role.equals("all") || !status.equals("all")) {
                users = nguoiDungService.searchUsers(search, role, status, pageable);
            } else {
                users = nguoiDungService.getAllUsers(pageable);
            }
            
            // Thêm data vào model
            model.addAttribute("users", users);
            model.addAttribute("search", search);
            model.addAttribute("role", role);
            model.addAttribute("status", status);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", users.getTotalPages());
            
            return "admin/users";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải danh sách users: " + e.getMessage());
            return "admin/users";
        }
    }
    
    // Employee Products Page
    @GetMapping("/employee/products")
    public String employeeProducts(Model model) {
        return "employee/products";
    }
    
    // Employee Profile Page
    @GetMapping("/employee/profile")
    public String employeeProfile(Model model, Principal principal) {
        try {
            // Lấy thông tin user hiện tại từ authentication
            String username = principal.getName();
            NguoiDung currentUser = nguoiDungService.findByTenDangNhap(username);
            
            if (currentUser == null) {
                model.addAttribute("error", "Không tìm thấy thông tin người dùng");
                return "error";
            }
            
            model.addAttribute("currentUser", currentUser);
            return "employee/profile";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải thông tin cá nhân: " + e.getMessage());
            return "error";
        }
    }
}