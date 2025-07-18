package com.moviebooking.system.controller;

import com.moviebooking.system.entity.Booking;
import com.moviebooking.system.entity.Showtime;
import com.moviebooking.system.entity.User;
import com.moviebooking.system.service.BookingService;
import com.moviebooking.system.service.ShowtimeService;
import com.moviebooking.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ShowtimeService showtimeService;

    @Autowired
    private UserService userService;

    @GetMapping("/create/{showtimeId}")
    public String bookingForm(@PathVariable Long showtimeId, Model model) {
        Optional<Showtime> showtime = showtimeService.getShowtimeById(showtimeId);
        if (showtime.isPresent()) {
            model.addAttribute("showtime", showtime.get());
            model.addAttribute("title", "Book Tickets - CinemaMax");
            return "booking-form";
        }
        return "redirect:/movies";
    }

    @PostMapping("/create")
    public String processBooking(@RequestParam Long showtimeId,
                                 @RequestParam Integer numberOfTickets,
                                 RedirectAttributes redirectAttributes) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userService.getUserByUsername(username);
            Optional<Showtime> showtime = showtimeService.getShowtimeById(showtimeId);

            if (user.isPresent() && showtime.isPresent()) {
                if (numberOfTickets <= 0 || numberOfTickets > 10) {
                    redirectAttributes.addFlashAttribute("error",
                            "Invalid number of tickets. Please select between 1-10 tickets.");
                    return "redirect:/movie/" + showtime.get().getMovie().getId();
                }

                if (numberOfTickets > showtime.get().getAvailableSeats()) {
                    redirectAttributes.addFlashAttribute("error",
                            "Not enough seats available. Only " + showtime.get().getAvailableSeats() + " seats left.");
                    return "redirect:/movie/" + showtime.get().getMovie().getId();
                }

                Booking booking = bookingService.createBooking(user.get(), showtime.get(), numberOfTickets);
                redirectAttributes.addFlashAttribute("success",
                        "Booking confirmed! Your reference: " + booking.getBookingReference());
                return "redirect:/booking/confirmation/" + booking.getId();
            } else {
                redirectAttributes.addFlashAttribute("error", "Booking failed. Please try again.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Booking failed: " + e.getMessage());
        }

        return "redirect:/movies";
    }

    @GetMapping("/confirmation/{bookingId}")
    public String bookingConfirmation(@PathVariable Long bookingId, Model model) {
        Optional<Booking> booking = bookingService.getBookingById(bookingId);
        if (booking.isPresent()) {
            // Verify the booking belongs to the current user
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (booking.get().getUser().getUsername().equals(username)) {
                model.addAttribute("booking", booking.get());
                model.addAttribute("title", "Booking Confirmation - CinemaMax");
                return "booking-confirmation";
            }
        }
        return "redirect:/booking/my-bookings";
    }

    @GetMapping("/my-bookings")
    public String myBookings(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent()) {
            List<Booking> bookings = bookingService.getBookingsByUser(user.get().getId());
            model.addAttribute("bookings", bookings);
            model.addAttribute("title", "My Bookings - CinemaMax");
            return "my-bookings";
        }
        return "redirect:/login";
    }

    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId, RedirectAttributes redirectAttributes) {
        try {
            // Verify booking belongs to current user
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Booking> bookingOpt = bookingService.getBookingById(bookingId);

            if (bookingOpt.isPresent() &&
                    bookingOpt.get().getUser().getUsername().equals(username)) {
                bookingService.cancelBooking(bookingId);
                redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Booking not found or access denied.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to cancel booking: " + e.getMessage());
        }
        return "redirect:/booking/my-bookings";
    }
}