// HomeController.java - Updated
package com.moviebooking.system.controller;

import com.moviebooking.system.entity.Movie;
import com.moviebooking.system.entity.User;
import com.moviebooking.system.service.*;
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

    @Autowired
    private TheaterService theaterService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    // Test endpoint
    @GetMapping("/test-json")
    @ResponseBody
    public String testJson() {
        return "{ \"status\": \"OK\", \"message\": \"Application is running\" }";
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "{ \"status\": \"UP\", \"message\": \"CinemaMax is running\" }";
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        try {
            // Get featured movies (limit to 8 for homepage)
            List<Movie> movies = movieService.getActiveMovies();
            if (movies.size() > 8) {
                movies = movies.subList(0, 8);
            }
            model.addAttribute("movies", movies);

            // Add statistics
            model.addAttribute("totalMovies", movieService.getAllMovies().size());
            model.addAttribute("totalTheaters", theaterService.getAllTheaters().size());
            model.addAttribute("totalBookings", bookingService.getAllBookings().size());

            // Set page title
            model.addAttribute("title", "CinemaMax - Your Premier Movie Experience");
            model.addAttribute("contentTemplate", "index");
        } catch (Exception e) {
            model.addAttribute("movies", List.of());
            model.addAttribute("error", "Unable to load movies: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "registered", required = false) String registered,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        if (logout != null) {
            model.addAttribute("success", "You have been logged out successfully!");
        }
        if (registered != null) {
            model.addAttribute("success", "Registration successful! Please login.");
        }
        model.addAttribute("title", "Login - CinemaMax");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "Register - CinemaMax");
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            List<Movie> movies = movieService.getActiveMovies();
            model.addAttribute("movies", movies);
            model.addAttribute("success", "Welcome to your dashboard!");
            model.addAttribute("title", "Dashboard - CinemaMax");
        } catch (Exception e) {
            model.addAttribute("movies", List.of());
            model.addAttribute("error", "Unable to load movies: " + e.getMessage());
        }
        return "dashboard";
    }

    @GetMapping("/movies")
    public String movies(@RequestParam(required = false) String search,
                         @RequestParam(required = false) String genre,
                         Model model) {
        try {
            List<Movie> movies;

            if (search != null && !search.trim().isEmpty()) {
                movies = movieService.searchMovies(search.trim());
                model.addAttribute("search", search);
            } else if (genre != null && !genre.trim().isEmpty()) {
                movies = movieService.getMoviesByGenre(genre);
                model.addAttribute("genre", genre);
            } else {
                movies = movieService.getActiveMovies();
            }

            model.addAttribute("movies", movies);
            model.addAttribute("title", "Movies - CinemaMax");
        } catch (Exception e) {
            model.addAttribute("movies", List.of());
            model.addAttribute("error", "Unable to load movies: " + e.getMessage());
        }
        return "movies";
    }
}