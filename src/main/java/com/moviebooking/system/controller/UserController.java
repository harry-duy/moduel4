package com.moviebooking.system.controller;

import com.moviebooking.system.entity.User;
import com.moviebooking.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            // Kiểm tra username đã tồn tại
            if (userService.existsByUsername(user.getUsername())) {
                model.addAttribute("error", "Username already exists!");
                return "register";
            }

            // Kiểm tra email đã tồn tại
            if (userService.existsByEmail(user.getEmail())) {
                model.addAttribute("error", "Email already exists!");
                return "register";
            }

            // Lưu user (password sẽ được hash trong UserService)
            userService.saveUser(user);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    // Xóa method login POST vì Spring Security sẽ handle
    // @PostMapping("/login") - KHÔNG CẦN NỮA

    @GetMapping("/logout")
    public String logout() {
        // Spring Security sẽ handle logout
        return "redirect:/logout";
    }
}