<%@ page import="java.sql.*" %>
<%@ page import="com.healthcare.util.DBConnection" %>

<%
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Connection con = DBConnection.getConnection();
    PreparedStatement ps = con.prepareStatement("SELECT * FROM doctor");
    ResultSet rs = ps.executeQuery();
%>

<!DOCTYPE html>
<html>
<head>
    <title>View Doctors</title>
    <link rel="stylesheet" href="css/style.css">

</head>
<body>

<h2>Doctor List</h2>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Specialization</th>
        <th>Contact</th>
    </tr>

    <%
        while (rs.next()) {
    %>
    <tr>
        <td><%= rs.getInt("id") %></td>
        <td><%= rs.getString("name") %></td>
        <td><%= rs.getString("specialization") %></td>
        <td><%= rs.getString("contact") %></td>
    </tr>
    <%
        }
    %>
</table>

<br>
<a href="index.jsp">Back to Home</a>

</body>
</html>
