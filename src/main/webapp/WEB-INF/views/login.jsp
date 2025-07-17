<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Login - Movie Booking System</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f4f4;
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      margin: 0;
    }
    .login-container {
      background: white;
      padding: 2rem;
      border-radius: 8px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      width: 100%;
      max-width: 400px;
    }
    .form-group {
      margin-bottom: 1rem;
    }
    label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: bold;
    }
    input[type="text"], input[type="password"] {
      width: 100%;
      padding: 0.5rem;
      border: 1px solid #ddd;
      border-radius: 4px;
      box-sizing: border-box;
    }
    .btn {
      background-color: #007bff;
      color: white;
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      width: 100%;
      font-size: 1rem;
    }
    .btn:hover {
      background-color: #0056b3;
    }
    .error {
      color: #d32f2f;
      margin-bottom: 1rem;
      padding: 0.5rem;
      background-color: #ffebee;
      border-radius: 4px;
    }
    .success {
      color: #2e7d32;
      margin-bottom: 1rem;
      padding: 0.5rem;
      background-color: #e8f5e8;
      border-radius: 4px;
    }
    .links {
      text-align: center;
      margin-top: 1rem;
    }
    .links a {
      color: #007bff;
      text-decoration: none;
    }
    .links a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
<div class="login-container">
  <h2>Login</h2>

  <c:if test="${not empty error}">
    <div class="error">${error}</div>
  </c:if>

  <c:if test="${not empty message}">
    <div class="success">${message}</div>
  </c:if>

  <form method="post" action="/perform_login">
    <div class="form-group">
      <label for="username">Username:</label>
      <input type="text" id="username" name="username" required>
    </div>

    <div class="form-group">
      <label for="password">Password:</label>
      <input type="password" id="password" name="password" required>
    </div>

    <button type="submit" class="btn">Login</button>
  </form>

  <div class="links">
    <a href="/register">Don't have an account? Register here</a><br>
    <a href="/">Back to Home</a>
  </div>

  <hr>
  <div style="background-color: #f8f9fa; padding: 10px; border-radius: 4px; margin-top: 1rem;">
    <strong>Test Accounts:</strong><br>
    <small>
      Admin: username=<code>admin</code>, password=<code>admin123</code><br>
      User: username=<code>user</code>, password=<code>user123</code>
    </small>
  </div>
</div>
</body>
</html>