package com.moviebooking.system.config;

import com.moviebooking.system.exception.BookingException;
import com.moviebooking.system.exception.ResourceNotFoundException;
import com.moviebooking.system.exception.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException e, Model model) {
        logger.error("Resource not found: ", e);
        model.addAttribute("error", e.getMessage());
        model.addAttribute("title", "Resource Not Found - CinemaMax");
        return "error/404";
    }

    @ExceptionHandler(BookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBookingException(BookingException e, RedirectAttributes redirectAttributes) {
        logger.error("Booking exception: ", e);
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/movies";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserAlreadyExists(UserAlreadyExistsException e, RedirectAttributes redirectAttributes) {
        logger.error("User already exists: ", e);
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/register";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException e, Model model) {
        logger.error("Access denied: ", e);
        model.addAttribute("error", "You don't have permission to access this resource.");
        model.addAttribute("title", "Access Denied - CinemaMax");
        return "error/403";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDataIntegrityViolation(DataIntegrityViolationException e, RedirectAttributes redirectAttributes) {
        logger.error("Data integrity violation: ", e);
        redirectAttributes.addFlashAttribute("error", "Data integrity violation. Please check your input.");
        return "redirect:/";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e, RedirectAttributes redirectAttributes) {
        logger.error("File size exceeded: ", e);
        redirectAttributes.addFlashAttribute("error", "File size exceeds maximum limit. Please choose a smaller file.");
        return "redirect:/admin/movies";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException e, RedirectAttributes redirectAttributes) {
        logger.error("Illegal argument: ", e);
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(RuntimeException e, Model model) {
        logger.error("Runtime exception occurred: ", e);
        model.addAttribute("error", "An unexpected error occurred. Please try again later.");
        model.addAttribute("title", "Server Error - CinemaMax");
        return "error/500";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception e, Model model) {
        logger.error("Unexpected error occurred: ", e);
        model.addAttribute("error", "An unexpected error occurred. Please contact support if the problem persists.");
        model.addAttribute("title", "Server Error - CinemaMax");
        return "error/500";
    }
}
