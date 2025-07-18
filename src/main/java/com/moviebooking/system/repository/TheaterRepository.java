package com.moviebooking.system.repository;

import com.moviebooking.system.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

    // Basic Queries
    List<Theater> findByCity(String city);
    List<Theater> findByCityOrderByName(String city);
    List<Theater> findByNameContainingIgnoreCase(String name);
    List<Theater> findByOrderByName();

    // Search Queries
    @Query("SELECT t FROM Theater t WHERE " +
            "LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.city) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Theater> searchTheaters(@Param("keyword") String keyword);

    // Location Queries
    List<Theater> findByAddressContainingIgnoreCase(String address);

    @Query("SELECT DISTINCT t.city FROM Theater t WHERE t.city IS NOT NULL ORDER BY t.city")
    List<String> findDistinctCities();

    // Capacity Queries
    List<Theater> findByTotalSeatsGreaterThanEqual(Integer minSeats);
    List<Theater> findByTotalSeatsLessThanEqual(Integer maxSeats);
    List<Theater> findByTotalSeatsBetween(Integer minSeats, Integer maxSeats);

    @Query("SELECT AVG(t.totalSeats) FROM Theater t")
    Double findAverageCapacity();

    @Query("SELECT SUM(t.totalSeats) FROM Theater t")
    Long findTotalCapacity();

    // Count Queries
    long countByCity(String city);

    // Statistics
    @Query("SELECT t.city, COUNT(t) as theaterCount FROM Theater t GROUP BY t.city ORDER BY theaterCount DESC")
    List<Object[]> findTheaterCountByCity();

    @Query("SELECT t.city, SUM(t.totalSeats) as totalCapacity FROM Theater t GROUP BY t.city ORDER BY totalCapacity DESC")
    List<Object[]> findCapacityByCity();
}