<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Bookings - Movie Booking</title>
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
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }
        .page-title {
            color: #333;
            margin-bottom: 2rem;
        }
        .no-bookings {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 3rem;
            text-align: center;
        }
        .no-bookings h3 {
            color: #6c757d;
            margin-bottom: 1rem;
        }
        .no-bookings p {
            color: #6c757d;
            margin-bottom: 2rem;
        }
        .no-bookings a {
            background-color: #007bff;
            color: white;
            padding: 0.75rem 1.5rem;
            text-decoration: none;
            border-radius: 4px;
        }
        .no-bookings a:hover {
            background-color: #0056b3;
        }
        .bookings-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
            gap: 2rem;
        }
        .booking-card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 1.5rem;
            position: relative;
        }
        .booking-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #eee;
        }
        .booking-reference {
            font-weight: bold;
            color: #007bff;
            font-size: 1.1rem;
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
        .status-cancelled {
            background-color: #f8d7da;
            color: #721c24;
        }
        .status-completed {
            background-color: #cce5ff;
            color: #004085;
        }
        .booking-movie {
            font-size: 1.3rem;
            font-weight: bold;
            color: #333;
            margin-bottom: 1rem;
        }
        .booking-details {
            margin-bottom: 1.5rem;
        }
        .detail-item {
            display: flex;
            align-items: center;
            margin-bottom: 0.5rem;
            color: #666;
        }
        .detail-item i {
            margin-right: 0.5rem;
            width: 16px;
        }
        .booking-actions {
            display: flex;
            gap: 0.5rem;
        }
        .btn-cancel {
            background-color: #dc3545;
            color: white;
            border: none;
            padding: 0.5rem 1rem;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.875rem;
        }
        .btn-cancel:hover {
            background-color: #c82333;
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
        .booking-time {
            font-size: 0.875rem;
            color: #6c757d;
            margin-top: 1rem;
        }
        @media (max-width: 768px) {
            .bookings-grid {
                grid-template-columns: 1fr;
            }
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
    <h2 class="page-title">📋 My Bookings</h2>

    <c:choose>
        <c:when test="${empty bookings}">
            <div class="no-bookings">
                <h3>No bookings found</h3>
                <p>You haven't made any bookings yet. Browse our movies and book your first ticket!</p>
                <a href="/movies">🎬 Browse Movies</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="bookings-grid">
                <c:forEach items="${bookings}" var="booking">
                    <div class="booking-card">
                        <div class="booking-header">
                            <div class="booking-reference">${booking.bookingReference}</div>
                            <c:choose>
                                <c:when test="${booking.status == 'CONFIRMED'}">
                                    <span class="status-badge status-confirmed">CONFIRMED</span>
                                </c:when>
                                <c:when test="${booking.status == 'CANCELLED'}">
                                    <span class="status-badge status-cancelled">CANCELLED</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-badge status-completed">COMPLETED</span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="booking-movie">${booking.showtime.movie.title}</div>

                        <div class="booking-details">
                            <div class="detail-item">
                                <span>🏢</span>
                                <span>${booking.showtime.theater.name}</span>
                            </div>
                            <div class="detail-item">
                                <span>📍</span>
                                <span>${booking.showtime.theater.address}</span>
                            </div>
                            <div class="detail-item">
                                <span>🕒</span>
                                <span><fmt:formatDate value="${booking.showtime.showTime}" pattern="dd/MM/yyyy HH:mm"/></span>
                            </div>
                            <div class="detail-item">
                                <span>🎫</span>
                                <span>${booking.numberOfTickets} ticket(s)</span>
                            </div>
                            <div class="detail-item">
                                <span>💰</span>
                                <span style="font-weight: bold; color: #007bff;">Total: ${booking.totalPrice}</span>
                            </div>
                        </div>

                        <c:if test="${booking.status == 'CONFIRMED'}">
                            <div class="booking-actions">
                                <form action="/booking/cancel/${booking.id}" method="post" style="display: inline;">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn-cancel"
                                            onclick="return confirm('Are you sure you want to cancel this booking?')">
                                        ❌ Cancel Booking
                                    </button>
                                </form>
                            </div>
                        </c:if>

                        <div class="booking-time">
                            Booked on: <fmt:formatDate value="${booking.bookingTime}" pattern="dd/MM/yyyy HH:mm"/>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>