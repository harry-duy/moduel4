package com.moviebooking.system.config;

import com.moviebooking.system.entity.*;
import com.moviebooking.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MovieService movieService;

    @Autowired
    private TheaterService theaterService;

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Create sample movies
        if (movieService.getAllMovies().isEmpty()) {
            Movie movie1 = new Movie("Avengers: Endgame", "The epic conclusion to the Infinity Saga",
                    "Anthony Russo, Joe Russo", "Action", 181, 8.4, LocalDate.of(2019, 4, 26));
            movie1.setPosterUrl("https://example.com/avengers-endgame.jpg");
            movieService.saveMovie(movie1);

            Movie movie2 = new Movie("The Dark Knight", "Batman faces the Joker",
                    "Christopher Nolan", "Action", 152, 9.0, LocalDate.of(2008, 7, 18));
            movie2.setPosterUrl("https://example.com/dark-knight.jpg");
            movieService.saveMovie(movie2);

            Movie movie3 = new Movie("Inception", "A thief who steals corporate secrets through dream-sharing technology",
                    "Christopher Nolan", "Sci-Fi", 148, 8.8, LocalDate.of(2010, 7, 16));
            movie3.setPosterUrl("https://example.com/inception.jpg");
            movieService.saveMovie(movie3);
        }

        // Create sample theaters
        if (theaterService.getAllTheaters().isEmpty()) {
            Theater theater1 = new Theater("CGV Vincom", "72 Le Thanh Ton, District 1", "Ho Chi Minh City", 200);
            theater1.setPhoneNumber("028-1234-5678");
            theaterService.saveTheater(theater1);

            Theater theater2 = new Theater("Lotte Cinema", "469 Nguyen Huu Tho, District 7", "Ho Chi Minh City", 150);
            theater2.setPhoneNumber("028-8765-4321");
            theaterService.saveTheater(theater2);
        }

        // Create sample showtimes
        if (showtimeService.getAllShowtimes().isEmpty()) {
            Movie movie1 = movieService.getMovieById(1L).orElse(null);
            Movie movie2 = movieService.getMovieById(2L).orElse(null);
            Theater theater1 = theaterService.getTheaterById(1L).orElse(null);
            Theater theater2 = theaterService.getTheaterById(2L).orElse(null);

            if (movie1 != null && theater1 != null) {
                Showtime showtime1 = new Showtime(movie1, theater1, LocalDateTime.now().plusDays(1).withHour(14).withMinute(0), 150000.0);
                showtimeService.saveShowtime(showtime1);

                Showtime showtime2 = new Showtime(movie1, theater1, LocalDateTime.now().plusDays(1).withHour(19).withMinute(30), 150000.0);
                showtimeService.saveShowtime(showtime2);
            }

            if (movie2 != null && theater2 != null) {
                Showtime showtime3 = new Showtime(movie2, theater2, LocalDateTime.now().plusDays(2).withHour(16).withMinute(0), 120000.0);
                showtimeService.saveShowtime(showtime3);
            }
        }

        // Create admin user
        if (userService.getUserByUsername("admin").isEmpty()) {
            User admin = new User("admin", "admin123", "admin@moviebooking.com", "Administrator");
            admin.setRole(User.Role.ADMIN);
            userService.saveUser(admin);
        }

        // Create sample user
        if (userService.getUserByUsername("user").isEmpty()) {
            User user = new User("user", "user123", "user@moviebooking.com", "Regular User");
            user.setPhoneNumber("0123456789");
            userService.saveUser(user);
        }
    }
}