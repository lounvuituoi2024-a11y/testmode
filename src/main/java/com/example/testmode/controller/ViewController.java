package com.example.testmode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    
    @GetMapping("/admin")
    public String adminRedirect() {
        return "redirect:/admin/dashboard.html";
    }
    
    @GetMapping("/admin/")
    public String adminRedirectSlash() {
        return "redirect:/admin/dashboard.html";
    }
}
