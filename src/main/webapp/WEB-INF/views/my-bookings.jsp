<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Bookings - Movie Booking</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="/">
            <i class="fas fa-film"></i> Movie Booking
        </a>
        <div class="navbar-nav ms-auto">
            <a class="nav-link" href="/dashboard">Dashboard</a>
            <a class="nav-link" href="/movies">Movies</a>
            <span class="navbar-text me-3">Hi, <sec:authentication property="name"/>!</span>
            <form action="<c:url value='/logout'/>" method="post" class="d-inline">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-link nav-link">Logout</button>
            </form>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <h2><i class="fas fa-list"></i> My Bookings</h2>

    <c:choose>
        <c:when test="${empty bookings}">
            <div class="alert alert-info">
                <h5>No bookings found</h5>
                <p>You haven't made any bookings yet. <a href="/movies">Browse movies</a> to book your first ticket!</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row">
                <c:forEach items="${bookings}" var="booking">
                    <div class="col-md-6 mb-4">
                        <div class="card">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <span>${booking.bookingReference}</span>
                                <c:choose>
                                    <c:when test="${booking.status == 'CONFIRMED'}">
                                        <span class="badge bg-success">CONFIRMED</span>
                                    </c:when>
                                    <c:when test="${booking.status == 'CANCELLED'}">
                                        <span class="badge bg-danger">CANCELLED</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-info">COMPLETED</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">${booking.showtime.movie.title}</h5>
                                <p class="card-text">
                                    <i class="fas fa-building"></i>
                                        ${booking.showtime.theater.name}
                                </p>
                                <p class="card-text">
                                    <i class="fas fa-clock"></i>
                                    <fmt:formatDate value="${booking.showtime.showTime}" pattern="dd/MM/yyyy HH:mm"/>
                                </p>
                                <p class="card-text">
                                    <i class="fas fa-ticket-alt"></i>
                                        ${booking.numberOfTickets} tickets
                                </p>
                                <p class="card-text">
                                    <i class="fas fa-dollar-sign"></i>
                                    Total: $${booking.totalPrice}
                                </p>
                                <p class="card-text">
                                    <small class="text-muted">
                                        Booked on: <fmt:formatDate value="${booking.bookingTime}" pattern="dd/MM/yyyy HH:mm"/>
                                    </small>
                                </p>

                                <c:if test="${booking.status == 'CONFIRMED'}">
                                    <div class="mt-3">
                                        <form action="<c:url value='/booking/cancel/${booking.id}'/>" method="post" style="display: inline;">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <button type="submit" class="btn btn-sm btn-outline-danger"
                                                    onclick="return confirm('Are you sure you want to cancel this booking?')">
                                                <i class="fas fa-times"></i> Cancel Booking
                                            </button>
                                        </form>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>