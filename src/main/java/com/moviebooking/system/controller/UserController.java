package com.moviebooking.system.controller;

import com.moviebooking.system.entity.User;
import com.moviebooking.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "Register - CinemaMax");
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        try {
            // Basic validation
            if (user.getUsername() == null || user.getUsername().trim().length() < 3) {
                redirectAttributes.addFlashAttribute("error", "Username must be at least 3 characters long!");
                return "redirect:/register";
            }

            if (user.getPassword() == null || user.getPassword().length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters long!");
                return "redirect:/register";
            }

            if (user.getEmail() == null || !user.getEmail().contains("@")) {
                redirectAttributes.addFlashAttribute("error", "Please enter a valid email address!");
                return "redirect:/register";
            }

            if (userService.existsByUsername(user.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "Username already exists!");
                return "redirect:/register";
            }

            if (userService.existsByEmail(user.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email already exists!");
                return "redirect:/register";
            }

            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("success",
                    "Registration successful! Please login with your credentials.");
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/logout";
    }
}