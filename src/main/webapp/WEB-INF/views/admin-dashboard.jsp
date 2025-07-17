<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
  <title>Admin Dashboard - Movie Booking System</title>
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
      background-color: #dc3545;
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
    .stats {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 2rem;
      margin-bottom: 2rem;
    }
    .stat-card {
      background: white;
      padding: 2rem;
      border-radius: 8px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      text-align: center;
    }
    .stat-card h3 {
      margin-top: 0;
      color: #dc3545;
    }
    .stat-number {
      font-size: 2rem;
      font-weight: bold;
      color: #007bff;
    }
    .admin-nav {
      background: white;
      padding: 1rem;
      border-radius: 8px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      margin-bottom: 2rem;
    }
    .admin-nav h3 {
      margin-top: 0;
    }
    .admin-nav ul {
      list-style: none;
      padding: 0;
      display: flex;
      flex-wrap: wrap;
      gap: 1rem;
    }
    .admin-nav a {
      color: #007bff;
      text-decoration: none;
      padding: 0.5rem 1rem;
      border: 1px solid #007bff;
      border-radius: 4px;
      display: block;
    }
    .admin-nav a:hover {
      background-color: #007bff;
      color: white;
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
    .logout-btn {
      background-color: #6c757d;
      color: white;
      padding: 0.5rem 1rem;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    .logout-btn:hover {
      background-color: #5a6268;
    }
    .error {
      color: #dc3545;
      background-color: #f8d7da;
      padding: 1rem;
      border-radius: 4px;
      margin-bottom: 1rem;
    }
  </style>
</head>
<body>
<div class="header">
  <h1>🔧 Admin Dashboard</h1>
  <div class="nav-links">
    <a href="/">Home</a>
    <a href="/dashboard">User Dashboard</a>
    <span>Welcome, <sec:authentication property="name"/>!</span>
    <form action="/logout" method="post" style="margin: 0;">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <button type="submit" class="logout-btn">Logout</button>
    </form>
  </div>
</div>

<div class="container">
  <c:if test="${not empty error}">
    <div class="error">${error}</div>
  </c:if>

  <div class="stats">
    <div class="stat-card">
      <h3>Total Movies</h3>
      <div class="stat-number">${totalMovies}</div>
    </div>
    <div class="stat-card">
      <h3>Total Theaters</h3>
      <div class="stat-number">${totalTheaters}</div>
    </div>
    <div class="stat-card">
      <h3>Total Bookings</h3>
      <div class="stat-number">${totalBookings}</div>
    </div>
    <div class="stat-card">
      <h3>Total Users</h3>
      <div class="stat-number">${totalUsers}</div>
    </div>
  </div>

  <div class="admin-nav">
    <h3>Admin Actions</h3>
    <ul>
      <li><a href="/admin/movies">Manage Movies</a></li>
      <li><a href="/admin/users">Manage Users</a></li>
      <li><a href="/admin/bookings">Manage Bookings</a></li>
      <li><a href="/admin/theaters">Manage Theaters</a></li>
    </ul>
  </div>

  <div style="background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
    <h3>Recent Movies</h3>
    <c:choose>
      <c:when test="${not empty movies}">
        <table style="width: 100%; border-collapse: collapse;">
          <thead>
          <tr style="background-color: #f8f9fa;">
            <th style="padding: 1rem; text-align: left; border-bottom: 1px solid #dee2e6;">Title</th>
            <th style="padding: 1rem; text-align: left; border-bottom: 1px solid #dee2e6;">Director</th>
            <th style="padding: 1rem; text-align: left; border-bottom: 1px solid #dee2e6;">Genre</th>
            <th style="padding: 1rem; text-align: left; border-bottom: 1px solid #dee2e6;">Rating</th>
            <th style="padding: 1rem; text-align: left; border-bottom: 1px solid #dee2e6;">Status</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach items="${movies}" var="movie">
            <tr>
              <td style="padding: 1rem; border-bottom: 1px solid #dee2e6;">${movie.title}</td>
              <td style="padding: 1rem; border-bottom: 1px solid #dee2e6;">${movie.director}</td>
              <td style="padding: 1rem; border-bottom: 1px solid #dee2e6;">${movie.genre}</td>
              <td style="padding: 1rem; border-bottom: 1px solid #dee2e6;">${movie.rating}/10</td>
              <td style="padding: 1rem; border-bottom: 1px solid #dee2e6;">
                                        <span style="color: ${movie.isActive ? 'green' : 'red'};">
                                            ${movie.isActive ? 'Active' : 'Inactive'}
                                        </span>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </c:when>
      <c:otherwise>
        <p>No movies found.</p>
      </c:otherwise>
    </c:choose>
  </div>
</div>
</body>
</html>