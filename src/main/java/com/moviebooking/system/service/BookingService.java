package com.moviebooking.system.service;

import com.moviebooking.system.entity.Booking;
import com.moviebooking.system.entity.Showtime;
import com.moviebooking.system.entity.User;
import com.moviebooking.system.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowtimeService showtimeService;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Optional<Booking> getBookingByReference(String reference) {
        return bookingRepository.findByBookingReference(reference);
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserIdOrderByBookingTimeDesc(userId);
    }

    public Booking createBooking(User user, Showtime showtime, Integer numberOfTickets) {
        if (showtimeService.updateAvailableSeats(showtime.getId(), numberOfTickets)) {
            Double totalPrice = showtime.getTicketPrice() * numberOfTickets;
            Booking booking = new Booking(user, showtime, numberOfTickets, totalPrice);
            return bookingRepository.save(booking);
        }
        throw new RuntimeException("Not enough seats available");
    }

    public void cancelBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            bookingRepository.save(booking);

            // Return seats to showtime
            Showtime showtime = booking.getShowtime();
            showtime.setAvailableSeats(showtime.getAvailableSeats() + booking.getNumberOfTickets());
            showtimeService.saveShowtime(showtime);
        }
    }
}