package com.moviebooking.system.controller;

import com.moviebooking.system.entity.Movie;
import com.moviebooking.system.entity.User;
import com.moviebooking.system.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private MovieService movieService;

    // Test endpoint
    @GetMapping("/test-json")
    @ResponseBody
    public String testJson() {
        return "{ \"status\": \"OK\", \"message\": \"Application is running\" }";
    }

    @GetMapping("/jsp-test")
    public String jspTest() {
        return "test";
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        try {
            List<Movie> movies = movieService.getActiveMovies();
            model.addAttribute("movies", movies);
        } catch (Exception e) {
            model.addAttribute("movies", List.of());
            model.addAttribute("error", "Unable to load movies: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully!");
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            List<Movie> movies = movieService.getActiveMovies();
            model.addAttribute("movies", movies);
            model.addAttribute("message", "Welcome to your dashboard!");
        } catch (Exception e) {
            model.addAttribute("movies", List.of());
            model.addAttribute("error", "Unable to load movies: " + e.getMessage());
        }
        return "dashboard";
    }

    @GetMapping("/movies")
    public String movies(@RequestParam(required = false) String search, Model model) {
        try {
            List<Movie> movies;
            if (search != null && !search.trim().isEmpty()) {
                movies = movieService.searchMovies(search.trim());
            } else {
                movies = movieService.getActiveMovies();
            }
            model.addAttribute("movies", movies);
            model.addAttribute("search", search);
        } catch (Exception e) {
            model.addAttribute("movies", List.of());
            model.addAttribute("error", "Unable to load movies: " + e.getMessage());
        }
        return "movies";
    }
}