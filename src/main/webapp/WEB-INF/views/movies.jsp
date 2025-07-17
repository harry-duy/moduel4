<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Movies - Movie Booking</title>
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
        .search-section {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 2rem;
        }
        .search-form {
            display: flex;
            gap: 0.5rem;
        }
        .search-form input {
            padding: 0.5rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            width: 300px;
        }
        .search-form button {
            padding: 0.5rem 1rem;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .search-form button:hover {
            background-color: #0056b3;
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
            height: auto;
            display: flex;
            flex-direction: column;
        }
        .movie-card h3 {
            color: #007bff;
            margin-top: 0;
            margin-bottom: 1rem;
        }
        .movie-info {
            flex-grow: 1;
            margin-bottom: 1rem;
        }
        .movie-actions {
            margin-top: auto;
        }
        .movie-actions a {
            display: inline-block;
            background-color: #007bff;
            color: white;
            padding: 0.5rem 1rem;
            text-decoration: none;
            border-radius: 4px;
            width: 100%;
            text-align: center;
            box-sizing: border-box;
        }
        .movie-actions a:hover {
            background-color: #0056b3;
        }
        .alert {
            padding: 1rem;
            margin-bottom: 1rem;
            border-radius: 4px;
        }
        .alert-info {
            background-color: #d1ecf1;
            color: #0c5460;
        }
        .alert-info a {
            color: #0c5460;
        }
    </style>
</head>
<body>
<div class="header">
    <h1>🎬 Movie Booking</h1>
    <div class="nav-links">
        <a href="/">Home</a>
        <a href="/movies">Movies</a>
        <a href="/login">Login</a>
        <a href="/register">Register</a>
    </div>
</div>

<div class="container">
    <div class="search-section">
        <h2>Movies</h2>
        <form class="search-form" action="/movies" method="get">
            <input type="search" name="search" value="${param.search}" placeholder="Search movies..." />
            <button type="submit">Search</button>
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
        <div class="movies-grid">
            <c:forEach items="${movies}" var="movie">
                <div class="movie-card">
                    <h3>${movie.title}</h3>
                    <div class="movie-info">
                        <p><strong>Director:</strong> ${movie.director}</p>
                        <p><strong>Genre:</strong> ${movie.genre}</p>
                        <p><strong>Duration:</strong> ${movie.durationMinutes} minutes</p>
                        <p><strong>Rating:</strong> ${movie.rating}/10</p>
                        <p><strong>Release Date:</strong> ${movie.releaseDate}</p>
                        <p>
                            <c:choose>
                                <c:when test="${movie.description.length() > 100}">
                                    ${movie.description.substring(0, 100)}...
                                </c:when>
                                <c:otherwise>
                                    ${movie.description}
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                    <div class="movie-actions">
                        <a href="/movie/${movie.id}">View Details & Book</a>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>
</div>
</body>
</html>