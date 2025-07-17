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
            return "booking-form";
        }
        return "redirect:/movies";
    }

    @PostMapping("/create")
    public String processBooking(@RequestParam Long showtimeId,
                                 @RequestParam Integer numberOfTickets,
                                 Model model) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userService.getUserByUsername(username);
            Optional<Showtime> showtime = showtimeService.getShowtimeById(showtimeId);

            if (user.isPresent() && showtime.isPresent()) {
                Booking booking = bookingService.createBooking(user.get(), showtime.get(), numberOfTickets);
                return "redirect:/booking/confirmation/" + booking.getId();
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            Optional<Showtime> showtime = showtimeService.getShowtimeById(showtimeId);
            if (showtime.isPresent()) {
                model.addAttribute("showtime", showtime.get());
            }
            return "booking-form";
        }

        return "redirect:/movies";
    }

    @GetMapping("/confirmation/{bookingId}")
    public String bookingConfirmation(@PathVariable Long bookingId, Model model) {
        Optional<Booking> booking = bookingService.getBookingById(bookingId);
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "booking-confirmation";
        }
        return "redirect:/movies";
    }

    @GetMapping("/my-bookings")
    public String myBookings(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent()) {
            List<Booking> bookings = bookingService.getBookingsByUser(user.get().getId());
            model.addAttribute("bookings", bookings);
            return "my-bookings";
        }
        return "redirect:/login";
    }

    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return "redirect:/booking/my-bookings";
    }
}