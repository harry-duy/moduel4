<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Booking Confirmation - Movie Booking</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="alert alert-success text-center">
                <h2><i class="fas fa-check-circle"></i> Booking Confirmed!</h2>
                <p>Your tickets have been successfully booked.</p>
            </div>

            <div class="card">
                <div class="card-header">
                    <h4><i class="fas fa-ticket-alt"></i> Booking Details</h4>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <h6>Booking Information</h6>
                            <table class="table table-borderless">
                                <tr>
                                    <td><strong>Booking Reference:</strong></td>
                                    <td>${booking.bookingReference}</td>
                                </tr>
                                <tr>
                                    <td><strong>Booking Time:</strong></td>
                                    <td><fmt:formatDate value="${booking.bookingTime}" pattern="dd/MM/yyyy HH:mm"/></td>
                                </tr>
                                <tr>
                                    <td><strong>Status:</strong></td>
                                    <td><span class="badge bg-success">${booking.status}</span></td>
                                </tr>
                            </table>
                        </div>
                        <div class="col-md-6">
                            <h6>Movie & Show Information</h6>
                            <table class="table table-borderless">
                                <tr>
                                    <td><strong>Movie:</strong></td>
                                    <td>${booking.showtime.movie.title}</td>
                                </tr>
                                <tr>
                                    <td><strong>Theater:</strong></td>
                                    <td>${booking.showtime.theater.name}</td>
                                </tr>
                                <tr>
                                    <td><strong>Show Time:</strong></td>
                                    <td><fmt:formatDate value="${booking.showtime.showTime}" pattern="dd/MM/yyyy HH:mm"/></td>
                                </tr>
                                <tr>
                                    <td><strong>Tickets:</strong></td>
                                    <td>${booking.numberOfTickets}</td>
                                </tr>
                                <tr>
                                    <td><strong>Total Price:</strong></td>
                                    <td><strong>$${booking.totalPrice}</strong></td>
                                </tr>
                            </table>
                        </div>
                    </div>

                    <div class="alert alert-warning mt-3">
                        <h6><i class="fas fa-info-circle"></i> Important Information</h6>
                        <ul class="mb-0">
                            <li>Please arrive at the theater at least 15 minutes before the show time.</li>
                            <li>Keep your booking reference for entry: <strong>${booking.bookingReference}</strong></li>
                            <li>Tickets can be cancelled up to 2 hours before the show time.</li>
                        </ul>
                    </div>

                    <div class="text-center mt-4">
                        <a href="/booking/my-bookings" class="btn btn-primary">
                            <i class="fas fa-list"></i> View All Bookings
                        </a>
                        <a href="/dashboard" class="btn btn-secondary">
                            <i class="fas fa-home"></i> Back to Dashboard
                        </a>
                        <button onclick="window.print()" class="btn btn-outline-secondary">
                            <i class="fas fa-print"></i> Print Confirmation
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>