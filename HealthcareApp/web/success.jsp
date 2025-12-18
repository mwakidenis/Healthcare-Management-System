<%
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Success</title>
    <link rel="stylesheet" href="css/style.css">

</head>
<body>

<h2>Operation Successful</h2>

<p><%= request.getAttribute("message") %></p>

<a href="index.jsp">Go to Home</a>

</body>
</html>
