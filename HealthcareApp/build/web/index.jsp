<%
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Healthcare Management System</title>
    <link rel="stylesheet" href="css/style.css">

</head>
<body>

<h1>Welcome, <%= session.getAttribute("username") %></h1>

<ul>
    <li><a href="addDoctor.jsp">Add Doctor</a></li>
    <li><a href="viewDoctor.jsp">View Doctors</a></li>
    <li><a href="addPatient.jsp">Add Patient</a></li>
    <li><a href="viewPatient.jsp">View Patients</a></li>
    <li><a href="register.jsp">Patient Registration</a></li>
    <li><a href="appointment.jsp">Book Appointment</a></li>
    <li><a href="LogoutServlet">Logout</a></li>
</ul>

</body>
</html>
