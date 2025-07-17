<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Booking Confirmation - Movie Booking</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 0 1rem;
        }
        .success-banner {
            background-color: #d4edda;
            color: #155724;
            padding: 2rem;
            border-radius: 8px;
            text-align: center;
            margin-bottom: 2rem;
            border: 1px solid #c3e6cb;
        }
        .success-banner h2 {
            margin-top: 0;
            font-size: 2rem;
        }
        .confirmation-card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 2rem;
            margin-bottom: 2rem;
        }
        .confirmation-details {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 2rem;
        }
        .detail-section h4 {
            color: #007bff;
            margin-top: 0;
            margin-bottom: 1rem;
        }
        .detail-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 0.5rem;
            padding-bottom: 0.5rem;
            border-bottom: 1px solid #eee;
        }
        .detail-row:last-child {
            border-bottom: none;
        }
        .detail-label {
            font-weight: bold;
            color: #333;
        }
        .detail-value {
            color: #666;
        }
        .important-info {
            background-color: #fff3cd;
            border: 1px solid #ffeaa7;
            padding: 1.5rem;
            border-radius: 8px;
            margin-bottom: 2rem;
        }
        .important-info h4 {
            margin-top: 0;
            color: #856404;
        }
        .important-info ul {
            margin-bottom: 0;
        }
        .booking-reference {
            font-size: 1.2rem;
            font-weight: bold;
            color: #007bff;
            background-color: #e3f2fd;
            padding: 0.5rem 1rem;
            border-radius: 4px;
            display: inline-block;
        }
        .btn-group {
            display: flex;
            gap: 1rem;
            flex-wrap: wrap;
            justify-content: center;
        }
        .btn {
            padding: 0.75rem 1.5rem;
            border: none;
            border-radius: 4px;
            font-size: 1rem;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        .btn-primary:hover {
            background-color: #0056b3;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
        .btn-outline {
            background-color: transparent;
            color: #6c757d;
            border: 1px solid #6c757d;
        }
        .btn-outline:hover {
            background-color: #6c757d;
            color: white;
        }
        .status-badge {
            padding: 0.25rem 0.75rem;
            border-radius: 4px;
            font-size: 0.875rem;
            font-weight: bold;
        }
        .status-confirmed {
            background-color: #d4edda;
            color: #155724;
        }
        @media (max-width: 768px) {
            .confirmation-details {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="success-banner">
        <h2>✅ Booking Confirmed!</h2>
        <p>Your tickets have been successfully booked.</p>
    </div>

    <div class="confirmation-card">
        <h3>📋 Booking Details</h3>

        <div class="confirmation-details">
            <div class="detail-section">
                <h4>🎫 Booking Information</h4>
                <div class="detail-row">
                    <span class="detail-label">Booking Reference:</span>
                    <span class="booking-reference">${booking.bookingReference}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Booking Time:</span>
                    <span class="detail-value">
                            <fmt:formatDate value="${booking.bookingTime}" pattern="dd/MM/yyyy HH:mm"/>
                        </span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Status:</span>
                    <span class="status-badge status-confirmed">${booking.status}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Number of Tickets:</span>
                    <span class="detail-value">${booking.numberOfTickets}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Total Price:</span>
                    <span class="detail-value" style="font-weight: bold; color: #007bff;">
                            $${booking.totalPrice}
                        </span>
                </div>
            </div>

            <div class="detail-section">
                <h4>🎬 Movie & Show Information</h4>
                <div class="detail-row">
                    <span class="detail-label">Movie:</span>
                    <span class="detail-value">${booking.showtime.movie.title}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Theater:</span>
                    <span class="detail-value">${booking.showtime.theater.name}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Address:</span>
                    <span class="detail-value">${booking.showtime.theater.address}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Show Time:</span>
                    <span class="detail-value">
                            <fmt:formatDate value="${booking.showtime.showTime}" pattern="dd/MM/yyyy HH:mm"/>
                        </span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Price per ticket:</span>
                    <span class="detail-value">$${booking.showtime.ticketPrice}</span>
                </div>
            </div>
        </div>
    </div>

    <div class="important-info">
        <h4>⚠️ Important Information</h4>
        <ul>
            <li>Please arrive at the theater at least 15 minutes before the show time.</li>
            <li>Keep your booking reference for entry: <strong>${booking.bookingReference}</strong></li>
            <li>Tickets can be cancelled up to 2 hours before the show time.</li>
            <li>Present a valid ID along with your booking reference at the theater.</li>
        </ul>
    </div>

    <div class="btn-group">
        <a href="/booking/my-bookings" class="btn btn-primary">
            📋 View All Bookings
        </a>
        <a href="/dashboard" class="btn btn-secondary">
            🏠 Back to Dashboard
        </a>
        <button onclick="window.print()" class="btn btn-outline">
            🖨️ Print Confirmation
        </button>
    </div>
</div>
</body>
</html>