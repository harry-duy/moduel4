package com.moviebooking.system.controller;

import com.moviebooking.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private TheaterService theaterService;

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        try {
            model.addAttribute("totalMovies", movieService.getAllMovies().size());
            model.addAttribute("totalTheaters", theaterService.getAllTheaters().size());
            model.addAttribute("totalBookings", bookingService.getAllBookings().size());
            model.addAttribute("totalUsers", userService.getAllUsers().size());
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("title", "Admin Dashboard - CinemaMax");
        } catch (Exception e) {
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
        }
        return "admin/dashboard";
    }

    @GetMapping("/movies")
    public String manageMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("title", "Manage Movies - CinemaMax");
        return "admin/movies";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("title", "Manage Users - CinemaMax");
        return "admin/users";
    }

    @GetMapping("/bookings")
    public String manageBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("title", "Manage Bookings - CinemaMax");
        return "admin/bookings";
    }

    @GetMapping("/theaters")
    public String manageTheaters(Model model) {
        model.addAttribute("theaters", theaterService.getAllTheaters());
        model.addAttribute("title", "Manage Theaters - CinemaMax");
        return "admin/theaters";
    }
}