<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Movies - Movie Booking</title>
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
            <a class="nav-link" href="/login">Login</a>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="fas fa-film"></i> Movies</h2>

        <!-- Search Form -->
        <form action="/movies" method="get" class="d-flex">
            <input class="form-control me-2" type="search" name="search"
                   value="${param.search}" placeholder="Search movies..." aria-label="Search">
            <button class="btn btn-outline-primary" type="submit">
                <i class="fas fa-search"></i>
            </button>
        </form>
    </div>

    <!-- Search Results Info -->
    <c:if test="${not empty param.search}">
        <div class="alert alert-info">
            <c:choose>
                <c:when test="${empty movies}">
                    No movies found for "<strong>${param.search}</strong>".
                    <a href="/movies">View all movies</a>
                </c:when>
                <c:otherwise>
                    Found ${movies.size()} movie(s) for "<strong>${param.search}</strong>".
                    <a href="/movies">View all movies</a>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <!-- No Movies Message -->
    <c:if test="${empty movies and empty param.search}">
        <div class="alert alert-info">
            No movies available at the moment. Please check back later!
        </div>
    </c:if>

    <!-- Movies Grid -->
    <c:if test="${not empty movies}">
        <div class="row">
            <c:forEach items="${movies}" var="movie">
                <div class="col-md-4 col-lg-3 mb-4">
                    <div class="card movie-card h-100">
                        <img src="${movie.posterUrl}"
                             alt="${movie.title}"
                             class="card-img-top"
                             style="height: 400px; object-fit: cover;"
                             onerror="this.src='https://via.placeholder.com/300x450/1f1f1f/ffffff?text=No+Image'">
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title">${movie.title}</h5>
                            <p class="card-text">
                                <c:choose>
                                    <c:when test="${movie.description.length() > 100}">
                                        ${movie.description.substring(0, 100)}...
                                    </c:when>
                                    <c:otherwise>
                                        ${movie.description}
                                    </c:otherwise>
                                </c:choose>
                            </p>

                            <div class="mt-auto">
                                <div class="mb-2">
                                    <small class="text-muted">
                                        <i class="fas fa-star text-warning"></i>
                                            ${movie.rating}
                                    </small>
                                    <br>
                                    <small class="text-muted">
                                        <i class="fas fa-clock"></i>
                                            ${movie.durationMinutes} min
                                    </small>
                                    <br>
                                    <small class="text-muted">
                                        <i class="fas fa-tags"></i>
                                            ${movie.genre}
                                    </small>
                                </div>
                                <a href="/movie/${movie.id}" class="btn btn-primary w-100">
                                    <i class="fas fa-info-circle"></i> View Details & Book
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>