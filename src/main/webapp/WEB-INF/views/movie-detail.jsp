<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${movie.title} - Movie Booking</title>
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
        .movie-info {
            display: grid;
            grid-template-columns: 1fr 2fr;
            gap: 2rem;
            margin-bottom: 2rem;
        }
        .movie-poster {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 1rem;
            text-align: center;
        }
        .movie-poster img {
            width: 100%;
            max-width: 400px;
            height: auto;
            border-radius: 8px;
        }
        .movie-details {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 2rem;
        }
        .movie-details h1 {
            color: #007bff;
            margin-top: 0;
        }
        .detail-row {
            display: grid;
            grid-template-columns: 1fr 2fr;
            margin-bottom: 1rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #eee;
        }
        .detail-row:last-child {
            border-bottom: none;
        }
        .detail-label {
            font-weight: bold;
            color: #333;
        }
        .showtimes-section {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 2rem;
            margin-bottom: 2rem;
        }
        .showtimes-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 1rem;
        }
        .showtime-card {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 1rem;
            background-color: #f9f9f9;
        }
        .showtime-card h4 {
            margin-top: 0;
            color: #007bff;
        }
        .book-btn {
            background-color: #28a745;
            color: white;
            padding: 0.5rem 1rem;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
            margin-top: 1rem;
        }
        .book-btn:hover {
            background-color: #218838;
        }
        .login-btn {
            background-color: #007bff;
            color: white;
            padding: 0.5rem 1rem;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
            margin-top: 1rem;
        }
        .login-btn:hover {
            background-color: #0056b3;
        }
        .back-btn {
            background-color: #6c757d;
            color: white;
            padding: 0.5rem 1rem;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
        }
        .back-btn:hover {
            background-color: #5a6268;
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
        <a href="/">Home</a>
        <a href="/movies">Movies</a>
        <sec:authorize access="!isAuthenticated()">
            <a href="/login">Login</a>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <a href="/dashboard">Dashboard</a>
            <span>Hi, <sec:authentication property="name"/>!</span>
            <form action="/logout" method="post" style="margin: 0;">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" class="logout-btn">Logout</button>
            </form>
        </sec:authorize>
    </div>
</div>

<div class="container">
    <div class="movie-info">
        <div class="movie-poster">
            <img src="${movie.posterUrl}" alt="${movie.title}"
                 onerror="this.src='https://via.placeholder.com/400x600/1f1f1f/ffffff?text=No+Image'">
        </div>
        <div class="movie-details">
            <h1>${movie.title}</h1>
            <p style="font-size: 1.1em; color: #666; margin-bottom: 1.5rem;">${movie.description}</p>

            <div class="detail-row">
                <div class="detail-label">Director:</div>
                <div>${movie.director}</div>
            </div>
            <div class="detail-row">
                <div class="detail-label">Genre:</div>
                <div>${movie.genre}</div>
            </div>
            <div class="detail-row">
                <div class="detail-label">Duration:</div>
                <div>${movie.durationMinutes} minutes</div>
            </div>
            <div class="detail-row">
                <div class="detail-label">Release Date:</div>
                <div><fmt:formatDate value="${movie.releaseDate}" pattern="dd/MM/yyyy"/></div>
            </div>
            <div class="detail-row">
                <div class="detail-label">Rating:</div>
                <div style="color: #ffc107; font-weight: bold;">★ ${movie.rating}/10</div>
            </div>

            <c:if test="${not empty movie.trailerUrl}">
                <div style="margin-top: 1rem;">
                    <a href="${movie.trailerUrl}" target="_blank"
                       style="color: #007bff; text-decoration: none; font-weight: bold;">
                        ▶ Watch Trailer
                    </a>
                </div>
            </c:if>
        </div>
    </div>

    <div class="showtimes-section">
        <h2>Available Showtimes</h2>
        <c:choose>
            <c:when test="${empty showtimes}">
                <div class="alert alert-info">
                    No showtimes available for this movie.
                </div>
            </c:when>
            <c:otherwise>
                <div class="showtimes-grid">
                    <c:forEach items="${showtimes}" var="showtime">
                        <div class="showtime-card">
                            <h4>🏢 ${showtime.theater.name}</h4>
                            <p><strong>📍 Address:</strong> ${showtime.theater.address}</p>
                            <p><strong>🕒 Show Time:</strong>
                                <fmt:formatDate value="${showtime.showTime}" pattern="dd/MM/yyyy HH:mm"/>
                            </p>
                            <p><strong>💰 Price:</strong> $${showtime.ticketPrice}</p>
                            <p><strong>🪑 Available Seats:</strong> ${showtime.availableSeats}</p>

                            <sec:authorize access="isAuthenticated()">
                                <a href="/booking/create/${showtime.id}" class="book-btn">
                                    🎫 Book Now
                                </a>
                            </sec:authorize>

                            <sec:authorize access="!isAuthenticated()">
                                <a href="/login" class="login-btn">
                                    🔐 Login to Book
                                </a>
                            </sec:authorize>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div style="text-align: center;">
        <a href="/movies" class="back-btn">← Back to Movies</a>
    </div>
</div>
</body>
</html>