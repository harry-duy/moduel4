package com.moviebooking.finaltest.system.controller;


import com.moviebooking.finaltest.system.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @Autowired
    private SanPhamService sanPhamService;

    // Redirect root path to product management
    @GetMapping({"/", "/home", "/index"})
    public String home() {
        return "redirect:/san-pham/quan-ly";
    }

    // Health check endpoint
    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "{ \"status\": \"UP\", \"message\": \"Auction System is running\", \"products\": " +
                sanPhamService.countAllSanPham() + " }";
    }

    // Test JSON endpoint
    @GetMapping("/test-json")
    @ResponseBody
    public String testJson() {
        return "{ \"status\": \"OK\", \"message\": \"Auction System Application is running\", \"timestamp\": \"" +
                System.currentTimeMillis() + "\" }";
    }

    // Dashboard (nếu cần)
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            long totalProducts = sanPhamService.countAllSanPham();
            model.addAttribute("totalProducts", totalProducts);
            model.addAttribute("title", "Dashboard - Auction System");
        } catch (Exception e) {
            model.addAttribute("error", "Unable to load dashboard: " + e.getMessage());
        }
        return "redirect:/san-pham/quan-ly";
    }
}