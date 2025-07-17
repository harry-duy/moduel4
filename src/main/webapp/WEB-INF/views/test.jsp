<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Test JSP</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        .success { color: green; }
    </style>
</head>
<body>
<h1 class="success">✓ JSP is working!</h1>
<p>Current time: <%= new java.util.Date() %></p>
<p>If you see this page, JSP rendering is working correctly.</p>
<a href="/">Go to Home</a>
</body>
</html>