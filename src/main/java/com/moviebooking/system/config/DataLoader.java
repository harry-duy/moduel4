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
        // Create sample movies with real-looking data
        if (movieService.getAllMovies().isEmpty()) {
            createSampleMovies();
        }

        // Create sample theaters
        if (theaterService.getAllTheaters().isEmpty()) {
            createSampleTheaters();
        }

        // Create sample showtimes
        if (showtimeService.getAllShowtimes().isEmpty()) {
            createSampleShowtimes();
        }

        // Create users
        createSampleUsers();
    }

    private void createSampleMovies() {
        // Movie 1: Avengers Endgame
        Movie movie1 = new Movie("Avengers: Endgame",
                "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and undo the chaos to the universe, no matter what consequences may be in store, and no matter who they face...",
                "Anthony Russo, Joe Russo", "Action", 181, 8.4, LocalDate.of(2019, 4, 26));
        movie1.setPosterUrl("https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg");
        movie1.setBackdropUrl("https://image.tmdb.org/t/p/w1280/7RyHsO4yDXtBv1zUU3mTpHeQ0d5.jpg");
        movie1.setTrailerUrl("https://www.youtube.com/watch?v=TcMBFSGVi1c");
        movie1.setCast("Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth, Scarlett Johansson");
        movie1.setLanguage("English");
        movie1.setCountry("USA");
        movie1.setAgeRating("PG-13");
        movie1.setIsFeatured(true);
        movieService.saveMovie(movie1);

        // Movie 2: The Dark Knight
        Movie movie2 = new Movie("The Dark Knight",
                "Batman raises the stakes in his war on crime. With the help of Lt. Jim Gordon and District Attorney Harvey Dent, Batman sets out to dismantle the remaining criminal organizations that plague the streets. The partnership proves to be effective, but they soon find themselves prey to a reign of chaos unleashed by a rising criminal mastermind known to the terrified citizens of Gotham as the Joker.",
                "Christopher Nolan", "Action", 152, 9.0, LocalDate.of(2008, 7, 18));
        movie2.setPosterUrl("https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg");
        movie2.setBackdropUrl("https://image.tmdb.org/t/p/w1280/dn3gbDpXPSwC6saMJOHkCiFA9jn.jpg");
        movie2.setTrailerUrl("https://www.youtube.com/watch?v=EXeTwQWrcwY");
        movie2.setCast("Christian Bale, Heath Ledger, Aaron Eckhart, Michael Caine, Maggie Gyllenhaal");
        movie2.setLanguage("English");
        movie2.setCountry("USA");
        movie2.setAgeRating("PG-13");
        movie2.setIsFeatured(true);
        movieService.saveMovie(movie2);

        // Movie 3: Inception
        Movie movie3 = new Movie("Inception",
                "Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible: inception, the implantation of another person's idea into a target's subconscious.",
                "Christopher Nolan", "Sci-Fi", 148, 8.8, LocalDate.of(2010, 7, 16));
        movie3.setPosterUrl("https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg");
        movie3.setBackdropUrl("https://image.tmdb.org/t/p/w1280/s3TBrRGB1iav7gFOCNx3H31MoES.jpg");
        movie3.setTrailerUrl("https://www.youtube.com/watch?v=YoHD9XEInc0");
        movie3.setCast("Leonardo DiCaprio, Marion Cotillard, Tom Hardy, Elliot Page, Ken Watanabe");
        movie3.setLanguage("English");
        movie3.setCountry("USA");
        movie3.setAgeRating("PG-13");
        movieService.saveMovie(movie3);

        // Movie 4: Parasite
        Movie movie4 = new Movie("Parasite",
                "All unemployed, Ki-taek's family takes peculiar interest in the wealthy and glamorous Parks for their livelihood until they get entangled in an unexpected incident.",
                "Bong Joon-ho", "Thriller", 132, 8.6, LocalDate.of(2019, 5, 30));
        movie4.setPosterUrl("https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg");
        movie4.setBackdropUrl("https://image.tmdb.org/t/p/w1280/TU9NIjwzjoKPwQHoHshkBcQeIIJ.jpg");
        movie4.setTrailerUrl("https://www.youtube.com/watch?v=5xH0HfJHsaY");
        movie4.setCast("Song Kang-ho, Lee Sun-kyun, Cho Yeo-jeong, Choi Woo-shik, Park So-dam");
        movie4.setLanguage("Korean");
        movie4.setCountry("South Korea");
        movie4.setAgeRating("R");
        movieService.saveMovie(movie4);

        // Movie 5: Spider-Man: No Way Home
        Movie movie5 = new Movie("Spider-Man: No Way Home",
                "Peter Parker's secret identity is revealed to the entire world. Desperate for help, Peter turns to Doctor Strange to make the world forget that he is Spider-Man. The spell goes horribly wrong and shatters the multiverse, bringing in foes from every Spider-Man film ever made.",
                "Jon Watts", "Action", 148, 8.2, LocalDate.of(2021, 12, 17));
        movie5.setPosterUrl("https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg");
        movie5.setBackdropUrl("https://image.tmdb.org/t/p/w1280/14QbnygCuTO0vl7CAFmPf1fgZfV.jpg");
        movie5.setTrailerUrl("https://www.youtube.com/watch?v=JfVOs4VSpmA");
        movie5.setCast("Tom Holland, Zendaya, Benedict Cumberbatch, Jacob Batalon, Jon Favreau");
        movie5.setLanguage("English");
        movie5.setCountry("USA");
        movie5.setAgeRating("PG-13");
        movie5.setIsFeatured(true);
        movieService.saveMovie(movie5);

        // Movie 6: Everything Everywhere All at Once
        Movie movie6 = new Movie("Everything Everywhere All at Once",
                "An aging Chinese immigrant is swept up in an insane adventure, where she alone can save what's important to her by connecting with the lives she could have led in other universes.",
                "Daniels", "Comedy", 139, 7.8, LocalDate.of(2022, 3, 25));
        movie6.setPosterUrl("https://image.tmdb.org/t/p/w500/w3LxiVYdWWRvEVdn5RYq6jIqkb1.jpg");
        movie6.setBackdropUrl("https://image.tmdb.org/t/p/w1280/uUiId6cGCiuGg0Ig1y6vtML7T5J.jpg");
        movie6.setTrailerUrl("https://www.youtube.com/watch?v=wxN1T1uxQ2g");
        movie6.setCast("Michelle Yeoh, Stephanie Hsu, Ke Huy Quan, James Hong, Jamie Lee Curtis");
        movie6.setLanguage("English");
        movie6.setCountry("USA");
        movie6.setAgeRating("R");
        movieService.saveMovie(movie6);
    }

    private void createSampleTheaters() {
        Theater theater1 = new Theater("CGV Vincom Center", "72 Le Thanh Ton, District 1", "Ho Chi Minh City", 250);
        theater1.setPhoneNumber("028-1234-5678");
        theaterService.saveTheater(theater1);

        Theater theater2 = new Theater("Lotte Cinema Diamond Plaza", "34 Le Duan, District 1", "Ho Chi Minh City", 180);
        theater2.setPhoneNumber("028-8765-4321");
        theaterService.saveTheater(theater2);

        Theater theater3 = new Theater("Galaxy Cinema Nguyen Du", "116 Nguyen Du, District 1", "Ho Chi Minh City", 200);
        theater3.setPhoneNumber("028-9876-5432");
        theaterService.saveTheater(theater3);

        Theater theater4 = new Theater("BHD Star Bitexco", "2 Hai Trieu, District 1", "Ho Chi Minh City", 150);
        theater4.setPhoneNumber("028-5555-6666");
        theaterService.saveTheater(theater4);
    }

    private void createSampleShowtimes() {
        var movies = movieService.getAllMovies();
        var theaters = theaterService.getAllTheaters();

        if (!movies.isEmpty() && !theaters.isEmpty()) {
            // Create showtimes for each movie in different theaters
            for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);

                // Create multiple showtimes for each movie
                for (int day = 0; day < 7; day++) {
                    for (int theaterIndex = 0; theaterIndex < Math.min(theaters.size(), 3); theaterIndex++) {
                        Theater theater = theaters.get(theaterIndex);

                        // Morning show
                        LocalDateTime morningShow = LocalDateTime.now().plusDays(day)
                                .withHour(10).withMinute(0).withSecond(0).withNano(0);
                        Showtime showtime1 = new Showtime(movie, theater, morningShow, 120000.0 + (i * 10000));
                        showtimeService.saveShowtime(showtime1);

                        // Afternoon show
                        LocalDateTime afternoonShow = LocalDateTime.now().plusDays(day)
                                .withHour(14).withMinute(30).withSecond(0).withNano(0);
                        Showtime showtime2 = new Showtime(movie, theater, afternoonShow, 150000.0 + (i * 10000));
                        showtimeService.saveShowtime(showtime2);

                        // Evening show
                        LocalDateTime eveningShow = LocalDateTime.now().plusDays(day)
                                .withHour(19).withMinute(0).withSecond(0).withNano(0);
                        Showtime showtime3 = new Showtime(movie, theater, eveningShow, 180000.0 + (i * 10000));
                        showtimeService.saveShowtime(showtime3);

                        // Late night show (weekends only)
                        if (day == 5 || day == 6) { // Friday and Saturday
                            LocalDateTime lateShow = LocalDateTime.now().plusDays(day)
                                    .withHour(22).withMinute(30).withSecond(0).withNano(0);
                            Showtime showtime4 = new Showtime(movie, theater, lateShow, 160000.0 + (i * 10000));
                            showtimeService.saveShowtime(showtime4);
                        }
                    }
                }
            }
        }
    }

    private void createSampleUsers() {
        // Create admin user
        if (userService.getUserByUsername("admin").isEmpty()) {
            User admin = new User("admin", "admin123", "admin@moviebooking.com", "Administrator");
            admin.setRole(User.Role.ADMIN);
            admin.setPhoneNumber("0901234567");
            userService.saveUser(admin);
        }

        // Create sample user
        if (userService.getUserByUsername("user").isEmpty()) {
            User user = new User("user", "user123", "user@moviebooking.com", "John Doe");
            user.setPhoneNumber("0123456789");
            userService.saveUser(user);
        }

        // Create more sample users
        if (userService.getUserByUsername("jane").isEmpty()) {
            User jane = new User("jane", "jane123", "jane@moviebooking.com", "Jane Smith");
            jane.setPhoneNumber("0987654321");
            userService.saveUser(jane);
        }
    }
}