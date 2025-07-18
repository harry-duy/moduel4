package com.moviebooking.system.repository;

import com.moviebooking.system.entity.Showtime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    // Basic Queries
    List<Showtime> findByMovieIdOrderByShowTime(Long movieId);
    List<Showtime> findByTheaterIdOrderByShowTime(Long theaterId);
    List<Showtime> findByShowTimeBetweenOrderByShowTime(LocalDateTime start, LocalDateTime end);

    // Upcoming Showtimes
    @Query("SELECT s FROM Showtime s WHERE s.movie.id = :movieId AND s.showTime >= :currentTime ORDER BY s.showTime")
    List<Showtime> findUpcomingShowtimesByMovieIdOrderByShowTime(
            @Param("movieId") Long movieId,
            @Param("currentTime") LocalDateTime currentTime
    );

    @Query("SELECT s FROM Showtime s WHERE s.theater.id = :theaterId AND s.showTime >= :currentTime ORDER BY s.showTime")
    List<Showtime> findUpcomingShowtimesByTheaterIdOrderByShowTime(
            @Param("theaterId") Long theaterId,
            @Param("currentTime") LocalDateTime currentTime
    );

    @Query("SELECT s FROM Showtime s WHERE s.showTime >= :currentTime ORDER BY s.showTime")
    List<Showtime> findAllUpcomingShowtimesOrderByShowTime(@Param("currentTime") LocalDateTime currentTime);

    // Conflict Detection
    @Query("SELECT s FROM Showtime s WHERE s.theater.id = :theaterId AND s.showTime BETWEEN :startTime AND :endTime AND s.id != :excludeId")
    List<Showtime> findConflictingShowtimes(
            @Param("theaterId") Long theaterId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId
    );

    // Count Queries
    long countByMovieId(Long movieId);
    long countByTheaterId(Long theaterId);
    long countByShowTimeAfter(LocalDateTime dateTime);
    long countByShowTimeBetween(LocalDateTime start, LocalDateTime end);

    // Availability Queries
    List<Showtime> findByAvailableSeatsGreaterThan(Integer seats);
    List<Showtime> findByAvailableSeatsGreaterThanAndShowTimeAfter(Integer seats, LocalDateTime dateTime);

    // Popular Showtimes (least available seats = most popular)
    @Query("SELECT s FROM Showtime s WHERE s.showTime >= :currentTime ORDER BY (s.theater.totalSeats - s.availableSeats) DESC")
    List<Showtime> findMostPopularShowtimes(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);

    @Query("SELECT s FROM Showtime s ORDER BY (s.theater.totalSeats - s.availableSeats) DESC")
    List<Showtime> findMostPopularShowtimes(Pageable pageable);

    // Statistics
    @Query("SELECT AVG(s.ticketPrice) FROM Showtime s")
    Double findAverageTicketPrice();

    @Query("SELECT MAX(s.ticketPrice) FROM Showtime s")
    Double findMaxTicketPrice();

    @Query("SELECT MIN(s.ticketPrice) FROM Showtime s")
    Double findMinTicketPrice();

    @Query("SELECT AVG(CAST(s.theater.totalSeats - s.availableSeats AS double)) FROM Showtime s WHERE s.showTime < :currentTime")
    Double findAverageOccupancyRate(@Param("currentTime") LocalDateTime currentTime);

    // Revenue Statistics
    @Query("SELECT s.movie.id, SUM((s.theater.totalSeats - s.availableSeats) * s.ticketPrice) as revenue FROM Showtime s WHERE s.showTime < :currentTime GROUP BY s.movie.id ORDER BY revenue DESC")
    List<Object[]> findRevenueByMovie(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);

    @Query("SELECT s.theater.id, SUM((s.theater.totalSeats - s.availableSeats) * s.ticketPrice) as revenue FROM Showtime s WHERE s.showTime < :currentTime GROUP BY s.theater.id ORDER BY revenue DESC")
    List<Object[]> findRevenueByTheater(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);
}
