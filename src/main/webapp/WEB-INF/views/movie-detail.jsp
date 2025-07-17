<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${movie.title} - Movie Booking</title>
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
            <a class="nav-link" href="/">Home</a>
            <a class="nav-link" href="/movies">Movies</a>
            <sec:authorize access="!isAuthenticated()">
                <a class="nav-link" href="/login">Login</a>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <a class="nav-link" href="/dashboard">Dashboard</a>
                <form action="<c:url value='/logout'/>" method="post" class="d-inline">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-link nav-link">Logout</button>
                </form>
            </sec:authorize>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <div class="row">
        <div class="col-md-4">
            <img src="${movie.posterUrl}"
                 alt="${movie.title}"
                 class="img-fluid rounded"
                 onerror="this.src='https://via.placeholder.com/400x600/1f1f1f/ffffff?text=No+Image'">
        </div>
        <div class="col-md-8">
            <h1>${movie.title}</h1>
            <p class="lead">${movie.description}</p>

            <div class="movie-info mb-4">
                <div class="row">
                    <div class="col-sm-3"><strong>Director:</strong></div>
                    <div class="col-sm-9">${movie.director}</div>
                </div>
                <div class="row">
                    <div class="col-sm-3"><strong>Genre:</strong></div>
                    <div class="col-sm-9">${movie.genre}</div>
                </div>
                <div class="row">
                    <div class="col-sm-3"><strong>Duration:</strong></div>
                    <div class="col-sm-9">${movie.durationMinutes} minutes</div>
                </div>
                <div class="row">
                    <div class="col-sm-3"><strong>Release Date:</strong></div>
                    <div class="col-sm-9">
                        <fmt:formatDate value="${movie.releaseDate}" pattern="dd/MM/yyyy"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-3"><strong>Rating:</strong></div>
                    <div class="col-sm-9">
                            <span class="text-warning">
                                <i class="fas fa-star"></i>
                                ${movie.rating}/10
                            </span>
                    </div>
                </div>
            </div>

            <c:if test="${not empty movie.trailerUrl}">
                <div class="mb-4">
                    <a href="${movie.trailerUrl}" class="btn btn-outline-primary" target="_blank">
                        <i class="fas fa-play"></i> Watch Trailer
                    </a>
                </div>
            </c:if>
        </div>
    </div>

    <hr class="my-5">

    <!-- Showtimes Section -->
    <div class="row">
        <div class="col-12">
            <h3>Available Showtimes</h3>
            <c:choose>
                <c:when test="${empty showtimes}">
                    <div class="alert alert-info">
                        No showtimes available for this movie.
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row">
                        <c:forEach items="${showtimes}" var="showtime">
                            <div class="col-md-6 mb-3">
                                <div class="card">
                                    <div class="card-body">
                                        <h5 class="card-title">
                                            <i class="fas fa-building"></i>
                                                ${showtime.theater.name}
                                        </h5>
                                        <p class="card-text">
                                            <i class="fas fa-map-marker-alt"></i>
                                                ${showtime.theater.address}
                                        </p>
                                        <p class="card-text">
                                            <i class="fas fa-clock"></i>
                                            <fmt:formatDate value="${showtime.showTime}" pattern="dd/MM/yyyy HH:mm"/>
                                        </p>
                                        <p class="card-text">
                                            <i class="fas fa-dollar-sign"></i>
                                            Price: $${showtime.ticketPrice}
                                        </p>
                                        <p class="card-text">
                                            <i class="fas fa-chair"></i>
                                            Available Seats: ${showtime.availableSeats}
                                        </p>

                                        <sec:authorize access="isAuthenticated()">
                                            <a href="/booking/create/${showtime.id}" class="btn btn-success">
                                                <i class="fas fa-ticket-alt"></i> Book Now
                                            </a>
                                        </sec:authorize>

                                        <sec:authorize access="!isAuthenticated()">
                                            <a href="/login" class="btn btn-primary">
                                                <i class="fas fa-sign-in-alt"></i> Login to Book
                                            </a>
                                        </sec:authorize>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Back Button -->
    <div class="row mt-4">
        <div class="col-12">
            <a href="/movies" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Back to Movies
            </a>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>