<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Movie Booking System</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
      text-align: center;
    }
    .container {
      max-width: 1200px;
      margin: 2rem auto;
      padding: 0 1rem;
    }
    .welcome {
      background: white;
      padding: 2rem;
      border-radius: 8px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      text-align: center;
      margin-bottom: 2rem;
    }
    .movies-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 2rem;
    }
    .movie-card {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      padding: 1.5rem;
    }
    .movie-card h3 {
      color: #007bff;
      margin-top: 0;
    }
    .nav-links {
      display: flex;
      justify-content: center;
      gap: 1rem;
      margin-top: 1rem;
    }
    .nav-links a {
      color: white;
      text-decoration: none;
      padding: 0.5rem 1rem;
      border-radius: 4px;
      background-color: rgba(255,255,255,0.1);
    }
    .nav-links a:hover {
      background-color: rgba(255,255,255,0.2);
    }
    .error {
      color: red;
      background-color: #ffebee;
      padding: 1rem;
      border-radius: 4px;
      margin-bottom: 1rem;
    }
  </style>
</head>
<body>
<div class="header">
  <h1>🎬 Movie Booking System</h1>
  <div class="nav-links">
    <a href="/">Home</a>
    <a href="/movies">Movies</a>
    <a href="/login">Login</a>
    <a href="/register">Register</a>
  </div>
</div>

<div class="container">
  <div class="welcome">
    <h2>Welcome to Movie Booking System!</h2>
    <p>Book your favorite movies with ease.</p>

    <c:if test="${not empty error}">
      <div class="error">${error}</div>
    </c:if>
  </div>

  <h2>Available Movies</h2>
  <div class="movies-grid">
    <c:choose>
      <c:when test="${not empty movies}">
        <c:forEach var="movie" items="${movies}">
          <div class="movie-card">
            <h3>${movie.title}</h3>
            <p><strong>Director:</strong> ${movie.director}</p>
            <p><strong>Genre:</strong> ${movie.genre}</p>
            <p><strong>Duration:</strong> ${movie.durationMinutes} minutes</p>
            <p><strong>Rating:</strong> ${movie.rating}/10</p>
            <p><strong>Release Date:</strong> ${movie.releaseDate}</p>
            <p>${movie.description}</p>
          </div>
        </c:forEach>
      </c:when>
      <c:otherwise>
        <div class="movie-card">
          <p>No movies available at the moment.</p>
        </div>
      </c:otherwise>
    </c:choose>
  </div>
</div>
</body>
</html>