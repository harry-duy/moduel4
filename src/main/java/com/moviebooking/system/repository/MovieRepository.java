package com.moviebooking.system.repository;

import com.moviebooking.system.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Basic Queries
    List<Movie> findByIsActiveTrueOrderByReleaseDateDesc();
    List<Movie> findByIsActiveTrueAndIsFeaturedTrueOrderByReleaseDateDesc();
    Page<Movie> findByIsActiveTrue(Pageable pageable);

    // Search Queries
    @Query("SELECT m FROM Movie m WHERE m.isActive = true AND " +
            "(LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.director) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.genre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.cast) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Movie> searchMovies(@Param("keyword") String keyword);

    // Genre and Director Queries
    List<Movie> findByGenreAndIsActiveTrueOrderByReleaseDateDesc(String genre);
    List<Movie> findByDirectorContainingIgnoreCaseAndIsActiveTrueOrderByReleaseDateDesc(String director);

    // Date Range Queries
    List<Movie> findByReleaseDateBetweenAndIsActiveTrueOrderByReleaseDateDesc(LocalDate start, LocalDate end);
    List<Movie> findByReleaseDateAfterAndIsActiveTrueOrderByReleaseDateDesc(LocalDate date);
    List<Movie> findByReleaseDateBeforeAndIsActiveTrueOrderByReleaseDateDesc(LocalDate date);

    // Rating Queries
    List<Movie> findByRatingBetweenAndIsActiveTrueOrderByRatingDesc(Double minRating, Double maxRating);
    List<Movie> findByRatingGreaterThanEqualAndIsActiveTrueOrderByRatingDesc(Double rating);

    // Count Queries
    long countByIsActiveTrue();
    long countByIsActiveTrueAndIsFeaturedTrue();
    long countByGenreAndIsActiveTrue(String genre);

    // Distinct Queries
    @Query("SELECT DISTINCT m.genre FROM Movie m WHERE m.isActive = true AND m.genre IS NOT NULL ORDER BY m.genre")
    List<String> findDistinctGenres();

    @Query("SELECT DISTINCT m.director FROM Movie m WHERE m.isActive = true AND m.director IS NOT NULL ORDER BY m.director")
    List<String> findDistinctDirectors();

    @Query("SELECT DISTINCT m.language FROM Movie m WHERE m.isActive = true AND m.language IS NOT NULL ORDER BY m.language")
    List<String> findDistinctLanguages();

    @Query("SELECT DISTINCT m.ageRating FROM Movie m WHERE m.isActive = true AND m.ageRating IS NOT NULL ORDER BY m.ageRating")
    List<String> findDistinctAgeRatings();

    // Top Movies
    @Query("SELECT m FROM Movie m WHERE m.isActive = true ORDER BY m.rating DESC")
    List<Movie> findTopRatedMovies(Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.isActive = true ORDER BY m.releaseDate DESC")
    List<Movie> findLatestMovies(Pageable pageable);

    // Statistics
    @Query("SELECT AVG(m.rating) FROM Movie m WHERE m.isActive = true AND m.rating IS NOT NULL")
    Double findAverageRating();

    @Query("SELECT MAX(m.rating) FROM Movie m WHERE m.isActive = true")
    Double findMaxRating();

    @Query("SELECT MIN(m.rating) FROM Movie m WHERE m.isActive = true")
    Double findMinRating();
}