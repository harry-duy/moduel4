package com.moviebooking.system.repository;

import com.moviebooking.system.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByMovieId(Long movieId);
    List<Showtime> findByTheaterId(Long theaterId);

    @Query("SELECT s FROM Showtime s WHERE s.movie.id = :movieId AND s.showTime >= :currentTime ORDER BY s.showTime")
    List<Showtime> findUpcomingShowtimesByMovieId(@Param("movieId") Long movieId, @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT s FROM Showtime s WHERE s.showTime >= :currentTime ORDER BY s.showTime")
    List<Showtime> findAllUpcomingShowtimes(@Param("currentTime") LocalDateTime currentTime);
}