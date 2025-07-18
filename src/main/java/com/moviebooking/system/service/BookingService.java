package com.moviebooking.system.service;

import com.moviebooking.system.entity.Booking;
import com.moviebooking.system.entity.Showtime;
import com.moviebooking.system.entity.User;
import com.moviebooking.system.exception.BookingException;
import com.moviebooking.system.exception.ResourceNotFoundException;
import com.moviebooking.system.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private EmailService emailService;

    @Value("${app.booking.max-tickets-per-booking:10}")
    private int maxTicketsPerBooking;

    @Value("${app.booking.cancellation-hours:2}")
    private int cancellationHours;

    // Basic CRUD Operations
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "bookingTime"));
    }

    public Page<Booking> getBookingsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "bookingTime"));
        return bookingRepository.findAll(pageable);
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking getBookingByIdOrThrow(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    public Optional<Booking> getBookingByReference(String reference) {
        return bookingRepository.findByBookingReference(reference);
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserIdOrderByBookingTimeDesc(userId);
    }

    public List<Booking> getBookingsByShowtime(Long showtimeId) {
        return bookingRepository.findByShowtimeIdOrderByBookingTimeDesc(showtimeId);
    }

    // Booking Creation
    @Transactional
    public Booking createBooking(User user, Showtime showtime, Integer numberOfTickets) {
        // Validate booking request
        validateBookingRequest(user, showtime, numberOfTickets);

        // Check seat availability
        if (!showtimeService.hasSufficientSeats(showtime.getId(), numberOfTickets)) {
            throw new BookingException("Not enough seats available. Only " +
                    showtime.getAvailableSeats() + " seats left.");
        }

        // Calculate total price
        Double totalPrice = showtime.getTicketPrice() * numberOfTickets;

        // Create booking
        Booking booking = new Booking(user, showtime, numberOfTickets, totalPrice);
        booking = bookingRepository.save(booking);

        // Update seat availability
        showtimeService.reserveSeats(showtime.getId(), numberOfTickets);

        // Send confirmation email (async)
        try {
            emailService.sendBookingConfirmation(booking);
        } catch (Exception e) {
            // Log error but don't fail the booking
            // In production, you might want to use a message queue for this
        }

        return booking;
    }

    // Booking Cancellation
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = getBookingByIdOrThrow(bookingId);

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new BookingException("Booking is already cancelled");
        }

        if (booking.getStatus() == Booking.BookingStatus.COMPLETED) {
            throw new BookingException("Cannot cancel completed booking");
        }

        // Check if cancellation is allowed (e.g., at least 2 hours before showtime)
        LocalDateTime showtimeStart = booking.getShowtime().getShowTime();
        LocalDateTime cancellationDeadline = showtimeStart.minusHours(cancellationHours);

        if (LocalDateTime.now().isAfter(cancellationDeadline)) {
            throw new BookingException("Cannot cancel booking. Cancellation deadline has passed.");
        }

        // Update booking status
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Return seats to showtime
        showtimeService.releaseSeats(booking.getShowtime().getId(), booking.getNumberOfTickets());

        // Send cancellation email
        try {
            emailService.sendBookingCancellation(booking);
        } catch (Exception e) {
            // Log error but don't fail the cancellation
        }
    }

    @Transactional
    public void adminCancelBooking(Long bookingId, String reason) {
        Booking booking = getBookingByIdOrThrow(bookingId);

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Return seats
        showtimeService.releaseSeats(booking.getShowtime().getId(), booking.getNumberOfTickets());

        // Send admin cancellation email with reason
        try {
            emailService.sendAdminCancellation(booking, reason);
        } catch (Exception e) {
            // Log error
        }
    }

    // Booking Status Updates
    @Transactional
    public void markBookingAsCompleted(Long bookingId) {
        Booking booking = getBookingByIdOrThrow(bookingId);
        booking.setStatus(Booking.BookingStatus.COMPLETED);
        bookingRepository.save(booking);
    }

    @Transactional
    public void markBookingAsUsed(String bookingReference) {
        Booking booking = getBookingByReference(bookingReference)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with reference: " + bookingReference));
        booking.setStatus(Booking.BookingStatus.COMPLETED);
        bookingRepository.save(booking);
    }

    // Statistics and Reports
    public long getTotalBookingsCount() {
        return bookingRepository.count();
    }

    public long getConfirmedBookingsCount() {
        return bookingRepository.countByStatus(Booking.BookingStatus.CONFIRMED);
    }

    public long getCancelledBookingsCount() {
        return bookingRepository.countByStatus(Booking.BookingStatus.CANCELLED);
    }

    public Double getTotalRevenue() {
        return bookingRepository.sumTotalPriceByStatus(Booking.BookingStatus.CONFIRMED);
    }

    public Double getRevenueForPeriod(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.sumTotalPriceByStatusAndBookingTimeBetween(
                Booking.BookingStatus.CONFIRMED, start, end);
    }

    public List<Booking> getBookingsForToday() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return bookingRepository.findByBookingTimeBetweenOrderByBookingTimeDesc(startOfDay, endOfDay);
    }

    public List<Booking> getUpcomingBookings(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return bookingRepository.findByUserIdAndShowtimeShowTimeAfterAndStatusOrderByShowtimeShowTime(
                userId, now, Booking.BookingStatus.CONFIRMED);
    }

    // Validation Methods
    private void validateBookingRequest(User user, Showtime showtime, Integer numberOfTickets) {
        if (user == null) {
            throw new BookingException("User cannot be null");
        }

        if (showtime == null) {
            throw new BookingException("Showtime cannot be null");
        }

        if (numberOfTickets == null || numberOfTickets <= 0) {
            throw new BookingException("Number of tickets must be positive");
        }

        if (numberOfTickets > maxTicketsPerBooking) {
            throw new BookingException("Cannot book more than " + maxTicketsPerBooking + " tickets per booking");
        }

        // Check if showtime is in the future
        if (showtime.getShowTime().isBefore(LocalDateTime.now())) {
            throw new BookingException("Cannot book tickets for past showtimes");
        }

        // Check if booking is too close to showtime (e.g., 30 minutes before)
        LocalDateTime bookingDeadline = showtime.getShowTime().minusMinutes(30);
        if (LocalDateTime.now().isAfter(bookingDeadline)) {
            throw new BookingException("Booking deadline has passed for this showtime");
        }
    }

    // User-specific methods
    public boolean canUserCancelBooking(Long userId, Long bookingId) {
        Optional<Booking> bookingOpt = getBookingById(bookingId);
        if (bookingOpt.isEmpty()) {
            return false;
        }

        Booking booking = bookingOpt.get();

        // Check if booking belongs to user
        if (!booking.getUser().getId().equals(userId)) {
            return false;
        }

        // Check if booking is cancellable
        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            return false;
        }

        // Check cancellation deadline
        LocalDateTime cancellationDeadline = booking.getShowtime().getShowTime().minusHours(cancellationHours);
        return LocalDateTime.now().isBefore(cancellationDeadline);
    }

    public List<Booking> getUserBookingHistory(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "bookingTime"));
        return bookingRepository.findByUserIdOrderByBookingTimeDesc(userId, pageable);
    }
}