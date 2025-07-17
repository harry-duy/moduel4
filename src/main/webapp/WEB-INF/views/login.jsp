<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
</head>
<body>
<h2>Login Page</h2>
<form method="post" action="/perform_login">
  <p>Username: <input type="text" name="username" required></p>
  <p>Password: <input type="password" name="password" required></p>
  <p><button type="submit">Login</button></p>
</form>
<p><a href="/">Back to Home</a></p>
<p>Test accounts: admin/admin123 or user/user123</p>
</body>
</html>