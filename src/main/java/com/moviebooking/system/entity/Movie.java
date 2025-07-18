package com.moviebooking.system.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String director;
    private String genre;
    private Integer durationMinutes;
    private Double rating;
    private LocalDate releaseDate;

    // Image and media fields
    private String posterUrl;
    private String trailerUrl;
    private String backdropUrl; // Background image

    // Additional movie details
    private String cast;
    private String language;
    private String country;
    private String ageRating; // G, PG, PG-13, R, etc.

    private Boolean isActive = true;
    private Boolean isFeatured = false; // For highlighting special movies

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Showtime> showtimes;

    // Constructors
    public Movie() {}

    public Movie(String title, String description, String director, String genre,
                 Integer durationMinutes, Double rating, LocalDate releaseDate) {
        this.title = title;
        this.description = description;
        this.director = director;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    // Helper methods
    public String getDurationFormatted() {
        if (durationMinutes == null) return "N/A";
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }
        return minutes + "m";
    }

    public String getRatingStars() {
        if (rating == null) return "";
        int stars = (int) Math.round(rating / 2.0);
        return "★".repeat(stars) + "☆".repeat(5 - stars);
    }

    public String getGenreBadgeClass() {
        if (genre == null) return "badge-secondary";
        return switch (genre.toLowerCase()) {
            case "action" -> "badge-danger";
            case "comedy" -> "badge-warning";
            case "drama" -> "badge-info";
            case "horror" -> "badge-dark";
            case "romance" -> "badge-pink";
            case "sci-fi", "science fiction" -> "badge-primary";
            case "thriller" -> "badge-secondary";
            default -> "badge-secondary";
        };
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }

    public String getBackdropUrl() { return backdropUrl; }
    public void setBackdropUrl(String backdropUrl) { this.backdropUrl = backdropUrl; }

    public String getCast() { return cast; }
    public void setCast(String cast) { this.cast = cast; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getAgeRating() { return ageRating; }
    public void setAgeRating(String ageRating) { this.ageRating = ageRating; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }

    public List<Showtime> getShowtimes() { return showtimes; }
    public void setShowtimes(List<Showtime> showtimes) { this.showtimes = showtimes; }
}