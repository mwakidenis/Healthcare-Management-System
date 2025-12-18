<!DOCTYPE html>
<html>
<head>
    <title>Book Appointment</title>
    <link rel="stylesheet" href="css/style.css">

</head>
<body>

<h2>Book Appointment</h2>

<form action="AppointmentServlet" method="post">
    Patient Name: <input type="text" name="pname" required /><br><br>

    Doctor:
    <select name="doctor">
        <option>Dr. Sharma</option>
        <option>Dr. Verma</option>
        <option>Dr. Khan</option>
    </select><br><br>

    <input type="submit" value="Book Appointment" />
</form>

</body>
</html>
