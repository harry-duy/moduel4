package com.moviebooking.system.service;

import com.moviebooking.system.entity.Booking;
import com.moviebooking.system.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.name:CinemaMax}")
    private String appName;

    @Value("${spring.mail.enabled:false}")
    private boolean emailEnabled;

    // In a real application, you would integrate with an email service like:
    // - Spring Mail with SMTP
    // - SendGrid
    // - Amazon SES
    // - Mailgun

    public void sendBookingConfirmation(Booking booking) {
        if (!emailEnabled) {
            logger.info("Email disabled. Would send booking confirmation to: {}", booking.getUser().getEmail());
            return;
        }

        String subject = appName + " - Booking Confirmation";
        String message = buildBookingConfirmationMessage(booking);

        // Simulate email sending
        logger.info("Sending booking confirmation email to: {}", booking.getUser().getEmail());
        logger.debug("Email content: {}", message);

        // TODO: Implement actual email sending
    }

    public void sendBookingCancellation(Booking booking) {
        if (!emailEnabled) {
            logger.info("Email disabled. Would send cancellation notice to: {}", booking.getUser().getEmail());
            return;
        }

        String subject = appName + " - Booking Cancelled";
        String message = buildBookingCancellationMessage(booking);

        logger.info("Sending booking cancellation email to: {}", booking.getUser().getEmail());
        logger.debug("Email content: {}", message);

        // TODO: Implement actual email sending
    }

    public void sendAdminCancellation(Booking booking, String reason) {
        if (!emailEnabled) {
            logger.info("Email disabled. Would send admin cancellation to: {}", booking.getUser().getEmail());
            return;
        }

        String subject = appName + " - Booking Cancelled by Administrator";
        String message = buildAdminCancellationMessage(booking, reason);

        logger.info("Sending admin cancellation email to: {}", booking.getUser().getEmail());
        logger.debug("Email content: {}", message);

        // TODO: Implement actual email sending
    }

    public void sendWelcomeEmail(User user) {
        if (!emailEnabled) {
            logger.info("Email disabled. Would send welcome email to: {}", user.getEmail());
            return;
        }

        String subject = "Welcome to " + appName;
        String message = buildWelcomeMessage(user);

        logger.info("Sending welcome email to: {}", user.getEmail());
        logger.debug("Email content: {}", message);

        // TODO: Implement actual email sending
    }

    public void sendPasswordResetEmail(User user, String resetToken) {
        if (!emailEnabled) {
            logger.info("Email disabled. Would send password reset to: {}", user.getEmail());
            return;
        }

        String subject = appName + " - Password Reset";
        String message = buildPasswordResetMessage(user, resetToken);

        logger.info("Sending password reset email to: {}", user.getEmail());
        logger.debug("Email content: {}", message);

        // TODO: Implement actual email sending
    }

    // Message Building Methods
    private String buildBookingConfirmationMessage(Booking booking) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return String.format("""
            Dear %s,
            
            Your booking has been confirmed!
            
            Booking Details:
            - Reference: %s
            - Movie: %s
            - Theater: %s
            - Show Time: %s
            - Tickets: %d
            - Total: ₫%,.0f
            
            Please arrive at the theater at least 15 minutes before the show time.
            Present this confirmation at the entrance.
            
            Thank you for choosing %s!
            
            Best regards,
            %s Team
            """,
                booking.getUser().getFullName(),
                booking.getBookingReference(),
                booking.getShowtime().getMovie().getTitle(),
                booking.getShowtime().getTheater().getName(),
                booking.getShowtime().getShowTime().format(formatter),
                booking.getNumberOfTickets(),
                booking.getTotalPrice(),
                appName,
                appName
        );
    }

    private String buildBookingCancellationMessage(Booking booking) {
        return String.format("""
            Dear %s,
            
            Your booking has been cancelled successfully.
            
            Cancelled Booking Details:
            - Reference: %s
            - Movie: %s
            - Theater: %s
            
            If you have any questions, please contact our support team.
            
            Best regards,
            %s Team
            """,
                booking.getUser().getFullName(),
                booking.getBookingReference(),
                booking.getShowtime().getMovie().getTitle(),
                booking.getShowtime().getTheater().getName(),
                appName
        );
    }

    private String buildAdminCancellationMessage(Booking booking, String reason) {
        return String.format("""
            Dear %s,
            
            We regret to inform you that your booking has been cancelled by our administration.
            
            Cancelled Booking Details:
            - Reference: %s
            - Movie: %s
            - Theater: %s
            
            Reason: %s
            
            We apologize for any inconvenience. Please contact our support team for assistance.
            
            Best regards,
            %s Team
            """,
                booking.getUser().getFullName(),
                booking.getBookingReference(),
                booking.getShowtime().getMovie().getTitle(),
                booking.getShowtime().getTheater().getName(),
                reason,
                appName
        );
    }

    private String buildWelcomeMessage(User user) {
        return String.format("""
            Dear %s,
            
            Welcome to %s!
            
            Your account has been created successfully. You can now:
            - Browse our latest movies
            - Book tickets online
            - Manage your bookings
            - Enjoy exclusive offers
            
            Start exploring our movies now!
            
            Best regards,
            %s Team
            """,
                user.getFullName(),
                appName,
                appName
        );
    }

    private String buildPasswordResetMessage(User user, String resetToken) {
        return String.format("""
            Dear %s,
            
            You have requested to reset your password for your %s account.
            
            Please use the following token to reset your password: %s
            
            This token will expire in 24 hours.
            
            If you did not request this reset, please ignore this email.
            
            Best regards,
            %s Team
            """,
                user.getFullName(),
                appName,
                resetToken,
                appName
        );
    }
}