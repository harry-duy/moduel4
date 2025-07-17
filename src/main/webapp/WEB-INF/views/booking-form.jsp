<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Tickets - Movie Booking</title>
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
            <a class="nav-link" href="/booking/my-bookings">My Bookings</a>
            <form action="<c:url value='/logout'/>" method="post" class="d-inline">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-link nav-link">Logout</button>
            </form>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h3><i class="fas fa-ticket-alt"></i> Book Your Tickets</h3>
                </div>
                <div class="card-body">
                    <!-- Movie and Showtime Info -->
                    <div class="alert alert-info">
                        <h5>${showtime.movie.title}</h5>
                        <p><strong>Theater:</strong> ${showtime.theater.name}</p>
                        <p><strong>Address:</strong> ${showtime.theater.address}</p>
                        <p><strong>Show Time:</strong>
                            <fmt:formatDate value="${showtime.showTime}" pattern="dd/MM/yyyy HH:mm"/>
                        </p>
                        <p><strong>Price per ticket:</strong> $${showtime.ticketPrice}</p>
                        <p><strong>Available Seats:</strong> ${showtime.availableSeats}</p>
                    </div>

                    <!-- Error Message -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>

                    <!-- Booking Form -->
                    <form action="<c:url value='/booking/create'/>" method="post">
                        <input type="hidden" name="showtimeId" value="${showtime.id}">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <div class="mb-3">
                            <label for="numberOfTickets" class="form-label">Number of Tickets</label>
                            <select class="form-select" id="numberOfTickets" name="numberOfTickets" required>
                                <option value="">Select number of tickets</option>
                                <c:forEach begin="1" end="${showtime.availableSeats > 10 ? 10 : showtime.availableSeats}" var="i">
                                    <option value="${i}">${i}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="mb-3">
                            <div class="card bg-light">
                                <div class="card-body">
                                    <h6>Booking Summary</h6>
                                    <div id="bookingSummary">
                                        <p>Select number of tickets to see total price</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-success btn-lg">
                                <i class="fas fa-credit-card"></i> Confirm Booking
                            </button>
                            <a href="/movie/${showtime.movie.id}" class="btn btn-secondary">
                                <i class="fas fa-arrow-left"></i> Back to Movie
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.getElementById('numberOfTickets').addEventListener('change', function() {
        const numberOfTickets = this.value;
        const ticketPrice = ${showtime.ticketPrice};
        const summaryDiv = document.getElementById('bookingSummary');

        if (numberOfTickets) {
            const totalPrice = numberOfTickets * ticketPrice;
            summaryDiv.innerHTML =
                '<p><strong>Tickets:</strong> ' + numberOfTickets + '</p>' +
                '<p><strong>Price per ticket:</strong> $' + ticketPrice.toFixed(2) + '</p>' +
                '<p><strong>Total Price:</strong> $' + totalPrice.toFixed(2) + '</p>';
        } else {
            summaryDiv.innerHTML = '<p>Select number of tickets to see total price</p>';
        }
    });
</script>
</body>
</html>