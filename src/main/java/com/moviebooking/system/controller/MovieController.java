package com.moviebooking.system.controller;

import com.moviebooking.system.entity.Movie;
import com.moviebooking.system.entity.Showtime;
import com.moviebooking.system.service.MovieService;
import com.moviebooking.system.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ShowtimeService showtimeService;

    @GetMapping("/{id}")
    public String movieDetail(@PathVariable Long id, Model model) {
        Optional<Movie> movie = movieService.getMovieById(id);
        if (movie.isPresent()) {
            List<Showtime> showtimes = showtimeService.getShowtimesByMovie(id);
            model.addAttribute("movie", movie.get());
            model.addAttribute("showtimes", showtimes);
            return "movie-detail";
        }
        return "redirect:/movies";
    }

    @GetMapping("/genre/{genre}")
    public String moviesByGenre(@PathVariable String genre, Model model) {
        List<Movie> movies = movieService.getMoviesByGenre(genre);
        model.addAttribute("movies", movies);
        model.addAttribute("genre", genre);
        return "movies";
    }
}