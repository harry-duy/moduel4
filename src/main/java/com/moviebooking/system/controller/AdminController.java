package com.moviebooking.system.controller;

import com.moviebooking.system.entity.*;
import com.moviebooking.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        model.addAttribute("totalMovies", movieService.getAllMovies().size());
        model.addAttribute("totalTheaters", theaterService.getAllTheaters().size());
        model.addAttribute("totalBookings", bookingService.getAllBookings().size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        return "admin/dashboard";
    }

    // Movie Management
    @GetMapping("/movies")
    public String manageMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "admin/movies";
    }

    @GetMapping("/movies/add")
    public String addMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "admin/add-movie";
    }

    @PostMapping("/movies/add")
    public String addMovie(@ModelAttribute Movie movie) {
        movieService.saveMovie(movie);
        return "redirect:/admin/movies";
    }

    @GetMapping("/movies/edit/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        Optional<Movie> movie = movieService.getMovieById(id);
        if (movie.isPresent()) {
            model.addAttribute("movie", movie.get());
            return "admin/edit-movie";
        }
        return "redirect:/admin/movies";
    }

    @PostMapping("/movies/edit/{id}")
    public String editMovie(@PathVariable Long id, @ModelAttribute Movie movie) {
        movie.setId(id);
        movieService.saveMovie(movie);
        return "redirect:/admin/movies";
    }

    @PostMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/admin/movies";
    }

    // Theater Management
    @GetMapping("/theaters")
    public String manageTheaters(Model model) {
        model.addAttribute("theaters", theaterService.getAllTheaters());
        return "admin/theaters";
    }

    @GetMapping("/theaters/add")
    public String addTheaterForm(Model model) {
        model.addAttribute("theater", new Theater());
        return "admin/add-theater";
    }

    @PostMapping("/theaters/add")
    public String addTheater(@ModelAttribute Theater theater) {
        theaterService.saveTheater(theater);
        return "redirect:/admin/theaters";
    }

    // Showtime Management
    @GetMapping("/showtimes")
    public String manageShowtimes(Model model) {
        model.addAttribute("showtimes", showtimeService.getAllShowtimes());
        return "admin/showtimes";
    }

    @GetMapping("/showtimes/add")
    public String addShowtimeForm(Model model) {
        model.addAttribute("showtime", new Showtime());
        model.addAttribute("movies", movieService.getActiveMovies());
        model.addAttribute("theaters", theaterService.getAllTheaters());
        return "admin/add-showtime";
    }

    @PostMapping("/showtimes/add")
    public String addShowtime(@ModelAttribute Showtime showtime) {
        showtimeService.saveShowtime(showtime);
        return "redirect:/admin/showtimes";
    }

    // Booking Management
    @GetMapping("/bookings")
    public String manageBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin/bookings";
    }
}