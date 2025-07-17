<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Movie Booking System - Test</title>
</head>
<body>
<h1>🎬 Movie Booking System</h1>
<p>Welcome! This is a test page.</p>

<h2>Movies Found: ${movies.size()}</h2>

<c:if test="${not empty movies}">
  <ul>
    <c:forEach var="movie" items="${movies}">
      <li>${movie.title} - ${movie.director}</li>
    </c:forEach>
  </ul>
</c:if>

<c:if test="${empty movies}">
  <p>No movies available.</p>
</c:if>

<p><a href="/login">Go to Login</a></p>
</body>
</html>