<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title != null ? title : 'Movie Booking System'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .navbar-brand { font-weight: bold; color: #dc3545 !important; }
        .movie-card { transition: transform 0.2s; }
        .movie-card:hover { transform: translateY(-5px); }
        .footer { background-color: #343a40; color: white; padding: 2rem 0; margin-top: 3rem; }
    </style>
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

            <!-- Not authenticated -->
            <sec:authorize access="!isAuthenticated()">
                <a class="nav-link" href="/login">Login</a>
                <a class="nav-link" href="/register">Register</a>
            </sec:authorize>

            <!-- Authenticated -->
            <sec:authorize access="isAuthenticated()">
                <span class="navbar-text me-3">Hi, <sec:authentication property="name"/>!</span>
                <a class="nav-link d-inline" href="/dashboard">Dashboard</a>
                <a class="nav-link d-inline" href="/booking/my-bookings">My Bookings</a>
                <sec:authorize access="hasRole('ADMIN')">
                    <a class="nav-link d-inline" href="/admin/dashboard">Admin</a>
                </sec:authorize>
                <form action="<c:url value='/logout'/>" method="post" class="d-inline">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-link nav-link">Logout</button>
                </form>
            </sec:authorize>
        </div>
    </div>
</nav>

<main class="container mt-4">
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>

    <!-- Page content will be included here -->
</main>

<footer class="footer">
    <div class="container text-center">
        <p>&copy; 2024 Movie Booking System. All rights reserved.</p>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>