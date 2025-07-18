package com.moviebooking.system.service;

import com.moviebooking.system.entity.Movie;
import com.moviebooking.system.repository.MovieRepository;
import com.moviebooking.system.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // Basic CRUD Operations
    public List<Movie> getAllMovies() {
        return movieRepository.findAll(Sort.by(Sort.Direction.DESC, "releaseDate"));
    }

    public List<Movie> getActiveMovies() {
        return movieRepository.findByIsActiveTrueOrderByReleaseDateDesc();
    }

    public List<Movie> getFeaturedMovies() {
        return movieRepository.findByIsActiveTrueAndIsFeaturedTrueOrderByReleaseDateDesc();
    }

    public Page<Movie> getMoviesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "releaseDate"));
        return movieRepository.findByIsActiveTrue(pageable);
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie getMovieByIdOrThrow(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }

    @Transactional
    public Movie saveMovie(Movie movie) {
        // Validate movie data
        validateMovie(movie);

        // Set default values if not provided
        if (movie.getIsActive() == null) {
            movie.setIsActive(true);
        }
        if (movie.getIsFeatured() == null) {
            movie.setIsFeatured(false);
        }

        return movieRepository.save(movie);
    }

    @Transactional
    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = getMovieByIdOrThrow(id);

        // Update fields
        movie.setTitle(movieDetails.getTitle());
        movie.setDescription(movieDetails.getDescription());
        movie.setDirector(movieDetails.getDirector());
        movie.setGenre(movieDetails.getGenre());
        movie.setDurationMinutes(movieDetails.getDurationMinutes());
        movie.setRating(movieDetails.getRating());
        movie.setReleaseDate(movieDetails.getReleaseDate());
        movie.setCast(movieDetails.getCast());
        movie.setLanguage(movieDetails.getLanguage());
        movie.setCountry(movieDetails.getCountry());
        movie.setAgeRating(movieDetails.getAgeRating());
        movie.setTrailerUrl(movieDetails.getTrailerUrl());
        movie.setIsActive(movieDetails.getIsActive());
        movie.setIsFeatured(movieDetails.getIsFeatured());

        // Only update URLs if new ones are provided
        if (movieDetails.getPosterUrl() != null) {
            movie.setPosterUrl(movieDetails.getPosterUrl());
        }
        if (movieDetails.getBackdropUrl() != null) {
            movie.setBackdropUrl(movieDetails.getBackdropUrl());
        }

        return movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = getMovieByIdOrThrow(id);
        // Soft delete by setting isActive to false
        movie.setIsActive(false);
        movieRepository.save(movie);
    }

    @Transactional
    public void hardDeleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    // Search and Filter Operations
    public List<Movie> searchMovies(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getActiveMovies();
        }
        return movieRepository.searchMovies(keyword.trim());
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreAndIsActiveTrueOrderByReleaseDateDesc(genre);
    }

    public List<Movie> getMoviesByDirector(String director) {
        return movieRepository.findByDirectorContainingIgnoreCaseAndIsActiveTrueOrderByReleaseDateDesc(director);
    }

    public List<Movie> getMoviesByYear(int year) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);
        return movieRepository.findByReleaseDateBetweenAndIsActiveTrueOrderByReleaseDateDesc(startOfYear, endOfYear);
    }

    public List<Movie> getMoviesByRatingRange(Double minRating, Double maxRating) {
        return movieRepository.findByRatingBetweenAndIsActiveTrueOrderByRatingDesc(minRating, maxRating);
    }

    public List<Movie> getRecentMovies(int days) {
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        return movieRepository.findByReleaseDateAfterAndIsActiveTrueOrderByReleaseDateDesc(cutoffDate);
    }

    // Statistics
    public long getTotalMoviesCount() {
        return movieRepository.count();
    }

    public long getActiveMoviesCount() {
        return movieRepository.countByIsActiveTrue();
    }

    public long getFeaturedMoviesCount() {
        return movieRepository.countByIsActiveTrueAndIsFeaturedTrue();
    }

    public List<String> getAllGenres() {
        return movieRepository.findDistinctGenres();
    }

    public List<String> getAllDirectors() {
        return movieRepository.findDistinctDirectors();
    }

    // Utility Methods
    private void validateMovie(Movie movie) {
        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be empty");
        }

        if (movie.getDurationMinutes() != null && movie.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Movie duration must be positive");
        }

        if (movie.getRating() != null && (movie.getRating() < 0 || movie.getRating() > 10)) {
            throw new IllegalArgumentException("Movie rating must be between 0 and 10");
        }

        if (movie.getReleaseDate() != null && movie.getReleaseDate().isAfter(LocalDate.now().plusYears(2))) {
            throw new IllegalArgumentException("Release date cannot be more than 2 years in the future");
        }
    }

    @Transactional
    public Movie toggleFeaturedStatus(Long id) {
        Movie movie = getMovieByIdOrThrow(id);
        movie.setIsFeatured(!movie.getIsFeatured());
        return movieRepository.save(movie);
    }

    @Transactional
    public Movie toggleActiveStatus(Long id) {
        Movie movie = getMovieByIdOrThrow(id);
        movie.setIsActive(!movie.getIsActive());
        return movieRepository.save(movie);
    }
}