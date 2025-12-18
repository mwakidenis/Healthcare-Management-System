<!DOCTYPE html>
<html>
<head>
    <title>Patient Registration</title>
    <link rel="stylesheet" href="css/style.css">

</head>
<body>

<h2>Patient Registration</h2>

<form action="PatientServlet" method="post">
    Name: <input type="text" name="name" required /><br><br>
    Age: <input type="number" name="age" required /><br><br>
    Gender:
    <select name="gender">
        <option>Male</option>
        <option>Female</option>
    </select><br><br>

    <input type="submit" value="Register" />
</form>

</body>
</html>
