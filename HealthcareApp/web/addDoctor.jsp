<%
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Add Doctor</title>
    <link rel="stylesheet" href="css/style.css">

</head>
<body>

<h2>Add Doctor</h2>

<form action="DoctorServlet" method="post">
    Name: <input type="text" name="name" required><br><br>
    Specialization: <input type="text" name="specialization" required><br><br>
    Contact: <input type="text" name="contact" required><br><br>

    <input type="submit" value="Add Doctor">
</form>

<br>
<a href="index.jsp">Back to Home</a>

</body>
</html>
