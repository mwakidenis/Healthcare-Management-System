<%
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Add Patient</title>
    <link rel="stylesheet" href="css/style.css">

</head>
<body>

<h2>Patient Registration</h2>

<form action="PatientServlet" method="post">
    Name: <input type="text" name="name" required><br><br>
    Age: <input type="number" name="age" required><br><br>
    Gender:
    <select name="gender" required>
        <option value="Male">Male</option>
        <option value="Female">Female</option>
    </select><br><br>
    Contact: <input type="text" name="contact" required><br><br>

    <input type="submit" value="Register Patient">
</form>

<br>
<a href="index.jsp">Back to Home</a>

</body>
</html>
