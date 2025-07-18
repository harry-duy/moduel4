package com.moviebooking.system.controller;

import com.moviebooking.system.entity.Movie;
import com.moviebooking.system.entity.Showtime;
import com.moviebooking.system.entity.Theater;
import com.moviebooking.system.service.MovieService;
import com.moviebooking.system.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ShowtimeService showtimeService;

    @GetMapping("/{id}")
    public String movieDetail(@PathVariable Long id, Model model) {
        Optional<Movie> movieOpt = movieService.getMovieById(id);
        if (movieOpt.isPresent()) {
            Movie movie = movieOpt.get();
            List<Showtime> showtimes = showtimeService.getShowtimesByMovie(id);

            // Group showtimes by theater for better display
            Map<Theater, List<Showtime>> showtimesByTheater = showtimes.stream()
                    .collect(Collectors.groupingBy(Showtime::getTheater));

            model.addAttribute("movie", movie);
            model.addAttribute("showtimes", showtimes);
            model.addAttribute("showtimesByTheater", showtimesByTheater);
            model.addAttribute("title", movie.getTitle() + " - CinemaMax");
            return "movie-detail";
        }
        model.addAttribute("error", "Movie not found");
        return "redirect:/movies";
    }

    @GetMapping("/genre/{genre}")
    public String moviesByGenre(@PathVariable String genre, Model model) {
        List<Movie> movies = movieService.getMoviesByGenre(genre);
        model.addAttribute("movies", movies);
        model.addAttribute("genre", genre);
        model.addAttribute("title", genre + " Movies - CinemaMax");
        return "movies";
    }
}