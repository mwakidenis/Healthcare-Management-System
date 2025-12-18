<!DOCTYPE html>
<html>
<head>
    <title>Healthcare Login</title>
    <link rel="stylesheet" href="css/style.css">

</head>
<body>

<h2>Healthcare Management System - Login</h2>

<form action="LoginServlet" method="post">
    Username:
    <input type="text" name="username" required /><br><br>

    Password:
    <input type="password" name="password" required /><br><br>

    <input type="submit" value="Login" />
</form>

<%
    String error = request.getParameter("error");
    if (error != null) {
%>
    <p style="color:red;">Invalid Username or Password</p>
<%
    }
%>

</body>
</html>
