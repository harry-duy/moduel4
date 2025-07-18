package com.moviebooking.system.repository;

import com.moviebooking.system.entity.Booking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Basic Queries
    List<Booking> findByUserIdOrderByBookingTimeDesc(Long userId);
    List<Booking> findByUserIdOrderByBookingTimeDesc(Long userId, Pageable pageable);
    List<Booking> findByShowtimeIdOrderByBookingTimeDesc(Long showtimeId);
    Optional<Booking> findByBookingReference(String bookingReference);

    // Status Queries
    List<Booking> findByStatus(Booking.BookingStatus status);
    List<Booking> findByUserIdAndStatus(Long userId, Booking.BookingStatus status);
    List<Booking> findByShowtimeIdAndStatus(Long showtimeId, Booking.BookingStatus status);

    // Date Range Queries
    List<Booking> findByBookingTimeBetweenOrderByBookingTimeDesc(LocalDateTime start, LocalDateTime end);
    List<Booking> findByBookingTimeAfterOrderByBookingTimeDesc(LocalDateTime dateTime);
    List<Booking> findByBookingTimeBeforeOrderByBookingTimeDesc(LocalDateTime dateTime);

    // Complex Queries
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.showtime.showTime > :currentTime AND b.status = :status ORDER BY b.showtime.showTime")
    List<Booking> findByUserIdAndShowtimeShowTimeAfterAndStatusOrderByShowtimeShowTime(
            @Param("userId") Long userId,
            @Param("currentTime") LocalDateTime currentTime,
            @Param("status") Booking.BookingStatus status
    );

    @Query("SELECT b FROM Booking b WHERE b.showtime.showTime BETWEEN :start AND :end ORDER BY b.bookingTime DESC")
    List<Booking> findByShowtimeShowTimeBetweenOrderByBookingTimeDesc(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Count Queries
    long countByStatus(Booking.BookingStatus status);
    long countByUserId(Long userId);
    long countByShowtimeId(Long showtimeId);
    long countByBookingTimeBetween(LocalDateTime start, LocalDateTime end);

    // Revenue Queries
    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE b.status = :status")
    Double sumTotalPriceByStatus(@Param("status") Booking.BookingStatus status);

    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE b.status = :status AND b.bookingTime BETWEEN :start AND :end")
    Double sumTotalPriceByStatusAndBookingTimeBetween(
            @Param("status") Booking.BookingStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT SUM(b.numberOfTickets) FROM Booking b WHERE b.status = :status")
    Long sumTicketsByStatus(@Param("status") Booking.BookingStatus status);

    // Statistics Queries
    @Query("SELECT AVG(b.totalPrice) FROM Booking b WHERE b.status = :status")
    Double findAverageBookingValue(@Param("status") Booking.BookingStatus status);

    @Query("SELECT AVG(b.numberOfTickets) FROM Booking b WHERE b.status = :status")
    Double findAverageTicketsPerBooking(@Param("status") Booking.BookingStatus status);

    // Top Customers
    @Query("SELECT b.user.id, COUNT(b) as bookingCount FROM Booking b WHERE b.status = :status GROUP BY b.user.id ORDER BY bookingCount DESC")
    List<Object[]> findTopCustomersByBookingCount(@Param("status") Booking.BookingStatus status, Pageable pageable);

    @Query("SELECT b.user.id, SUM(b.totalPrice) as totalSpent FROM Booking b WHERE b.status = :status GROUP BY b.user.id ORDER BY totalSpent DESC")
    List<Object[]> findTopCustomersBySpending(@Param("status") Booking.BookingStatus status, Pageable pageable);
}