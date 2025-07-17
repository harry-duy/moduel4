<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - Movie Booking System</title>
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
            display: flex;
            justify-content: space-between;
            align-items: center;
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
        .logout-btn {
            background-color: #dc3545;
            color: white;
            padding: 0.5rem 1rem;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }
        .logout-btn:hover {
            background-color: #c82333;
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
        .error {
            color: #dc3545;
            background-color: #f8d7da;
            padding: 1rem;
            border-radius: 4px;
            margin-bottom: 1rem;
        }
        .success {
            color: #155724;
            background-color: #d4edda;
            padding: 1rem;
            border-radius: 4px;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
<div class="header">
    <h1>Movie Booking System</h1>
    <div class="nav-links">
        <a href="/">Home</a>
        <a href="/movies">Movies</a>
        <a href="/booking/my-bookings">My Bookings</a>
        <sec:authorize access="hasRole('ADMIN')">
            <a href="/admin/dashboard">Admin</a>
        </sec:authorize>
        <span>Welcome, <sec:authentication property="name"/>!</span>
        <form action="/logout" method="post" style="margin: 0;">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="logout-btn">Logout</button>
        </form>
    </div>
</div>

<div class="container">
    <div class="welcome">
        <h2>Welcome to your Dashboard!</h2>
        <c:if test="${not empty message}">
            <div class="success">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        <p>You are now logged in and can access all features.</p>
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
                        <a href="/movie/${movie.id}" style="color: #007bff; text-decoration: none;">View Details & Book</a>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>No movies available at the moment.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>