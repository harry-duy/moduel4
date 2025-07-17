<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Home - Movie Booking</title>
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
  <!-- Hero Section -->
  <div class="bg-primary text-white p-5 rounded mb-4">
    <h1 class="display-4">Welcome to Movie Booking System</h1>
    <p class="lead">Book your favorite movies with ease!</p>
    <a class="btn btn-light btn-lg" href="/movies">
      <i class="fas fa-film"></i> Browse Movies
    </a>
  </div>

  <!-- Movies Grid -->
  <div class="row">
    <div class="col-12">
      <h2 class="mb-4">Featured Movies</h2>
    </div>

    <c:forEach items="${movies}" var="movie">
      <div class="col-md-4 mb-4">
        <div class="card h-100">
          <img src="${movie.posterUrl}"
               class="card-img-top"
               style="height: 300px; object-fit: cover;"
               alt="${movie.title}"
               onerror="this.src='https://via.placeholder.com/300x450/1f1f1f/ffffff?text=No+Image'">
          <div class="card-body">
            <h5 class="card-title">${movie.title}</h5>
            <p class="card-text">${movie.description}</p>
            <div class="mt-auto">
              <small class="text-muted">
                <i class="fas fa-star text-warning"></i>
                  ${movie.rating}
              </small>
              <br>
              <small class="text-muted">
                <i class="fas fa-clock"></i>
                  ${movie.durationMinutes} min
              </small>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>

  <c:if test="${empty movies}">
    <div class="alert alert-info">
      <h4>No movies available</h4>
      <p>Please check back later for new movies!</p>
    </div>
  </c:if>
</div>
</body>
</html>