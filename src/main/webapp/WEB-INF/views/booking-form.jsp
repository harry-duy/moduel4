<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Tickets - Movie Booking</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .header {
            background-color: #007bff;
            color: white;
            padding: 1rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .nav-links {
            display: flex;
            gap: 1rem;
            align-items: center;
        }
        .nav-links a {
            color: white;
            text-decoration: none;
            padding: 0.5rem 1rem;
            border-radius: 4px;
        }
        .nav-links a:hover {
            background-color: rgba(255,255,255,0.1);
        }
        .container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 0 1rem;
        }
        .booking-card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 2rem;
            margin-bottom: 2rem;
        }
        .movie-info {
            background-color: #e3f2fd;
            padding: 1.5rem;
            border-radius: 8px;
            margin-bottom: 2rem;
        }
        .movie-info h3 {
            margin-top: 0;
            color: #1976d2;
        }
        .form-group {
            margin-bottom: 1.5rem;
        }
        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: bold;
            color: #333;
        }
        .form-group select {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 1rem;
        }
        .booking-summary {
            background-color: #f8f9fa;
            padding: 1.5rem;
            border-radius: 8px;
            margin-bottom: 2rem;
        }
        .booking-summary h4 {
            margin-top: 0;
            color: #333;
        }
        .btn-group {
            display: flex;
            gap: 1rem;
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
        .btn-success {
            background-color: #28a745;
            color: white;
        }
        .btn-success:hover {
            background-color: #218838;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
        .error {
            color: #dc3545;
            background-color: #f8d7da;
            padding: 1rem;
            border-radius: 4px;
            margin-bottom: 1rem;
        }
        .logout-btn {
            background-color: #dc3545;
            color: white;
            padding: 0.5rem 1rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .logout-btn:hover {
            background-color: #c82333;
        }
    </style>
</head>
<body>
<div class="header">
    <h1>🎬 Movie Booking</h1>
    <div class="nav-links">
        <a href="/dashboard">Dashboard</a>
        <a href="/movies">Movies</a>
        <a href="/booking/my-bookings">My Bookings</a>
        <span>Hi, <sec:authentication property="name"/>!</span>
        <form action="/logout" method="post" style="margin: 0;">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="logout-btn">Logout</button>
        </form>
    </div>
</div>

<div class="container">
    <div class="booking-card">
        <h2>🎫 Book Your Tickets</h2>

        <!-- Movie and Showtime Info -->
        <div class="movie-info">
            <h3>${showtime.movie.title}</h3>
            <p><strong>🏢 Theater:</strong> ${showtime.theater.name}</p>
            <p><strong>📍 Address:</strong> ${showtime.theater.address}</p>
            <p><strong>🕒 Show Time:</strong>
                <fmt:formatDate value="${showtime.showTime}" pattern="dd/MM/yyyy HH:mm"/>
            </p>
            <p><strong>💰 Price per ticket:</strong> $${showtime.ticketPrice}</p>
            <p><strong>🪑 Available Seats:</strong> ${showtime.availableSeats}</p>
        </div>

        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <!-- Booking Form -->
        <form action="/booking/create" method="post">
            <input type="hidden" name="showtimeId" value="${showtime.id}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="form-group">
                <label for="numberOfTickets">Number of Tickets</label>
                <select id="numberOfTickets" name="numberOfTickets" required>
                    <option value="">Select number of tickets</option>
                    <c:forEach begin="1" end="${showtime.availableSeats > 10 ? 10 : showtime.availableSeats}" var="i">
                        <option value="${i}">${i}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="booking-summary">
                <h4>Booking Summary</h4>
                <div id="bookingSummary">
                    <p>Select number of tickets to see total price</p>
                </div>
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-success">
                    💳 Confirm Booking
                </button>
                <a href="/movie/${showtime.movie.id}" class="btn btn-secondary">
                    ← Back to Movie
                </a>
            </div>
        </form>
    </div>
</div>

<script>
    document.getElementById('numberOfTickets').addEventListener('change', function() {
        const numberOfTickets = this.value;
        const ticketPrice = ${showtime.ticketPrice};
        const summaryDiv = document.getElementById('bookingSummary');

        if (numberOfTickets) {
            const totalPrice = numberOfTickets * ticketPrice;
            summaryDiv.innerHTML =
                '<p><strong>🎫 Tickets:</strong> ' + numberOfTickets + '</p>' +
                '<p><strong>💰 Price per ticket:</strong> $' + ticketPrice.toFixed(2) + '</p>' +
                '<p><strong>💵 Total Price:</strong> $' + totalPrice.toFixed(2) + '</p>';
        } else {
            summaryDiv.innerHTML = '<p>Select number of tickets to see total price</p>';
        }
    });
</script>
</body>
</html>