package com.moviebooking.system.controller;

import com.moviebooking.system.entity.Movie;
import com.moviebooking.system.entity.User;
import com.moviebooking.system.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private MovieService movieService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        System.out.println("=== HOME CONTROLLER CALLED ===");
        try {
            List<Movie> movies = movieService.getActiveMovies();
            System.out.println("Movies loaded: " + movies.size());
            model.addAttribute("movies", movies);
        } catch (Exception e) {
            System.out.println("Error loading movies: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("movies", List.of());
            model.addAttribute("error", "Unable to load movies: " + e.getMessage());
        }
        System.out.println("=== RETURNING INDEX VIEW ===");
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        System.out.println("=== LOGIN CONTROLLER CALLED ===");

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
        System.out.println("=== REGISTER CONTROLLER CALLED ===");
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        System.out.println("=== DASHBOARD CONTROLLER CALLED ===");
        try {
            List<Movie> movies = movieService.getActiveMovies();
            model.addAttribute("movies", movies);
            model.addAttribute("message", "Welcome to your dashboard!");
        } catch (Exception e) {
            System.out.println("Error loading movies in dashboard: " + e.getMessage());
            model.addAttribute("movies", List.of());
            model.addAttribute("error", "Unable to load movies: " + e.getMessage());
        }
        return "dashboard";
    }

    @GetMapping("/movies")
    public String movies(@RequestParam(required = false) String search, Model model) {
        System.out.println("=== MOVIES CONTROLLER CALLED ===");
        try {
            List<Movie> movies;
            if (search != null && !search.trim().isEmpty()) {
                movies = movieService.searchMovies(search.trim());
                System.out.println("Search results for '" + search + "': " + movies.size());
            } else {
                movies = movieService.getActiveMovies();
                System.out.println("All active movies: " + movies.size());
            }
            model.addAttribute("movies", movies);
            model.addAttribute("search", search);
        } catch (Exception e) {
            System.out.println("Error loading movies: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("movies", List.of());
            model.addAttribute("error", "Unable to load movies: " + e.getMessage());
        }
        return "movies";
    }
}