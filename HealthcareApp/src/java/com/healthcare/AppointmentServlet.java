package com.healthcare;

import com.healthcare.util.DBConnection;
import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/AppointmentServlet")
public class AppointmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pname = request.getParameter("pname");
        String doctor = request.getParameter("doctor");

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO appointments (patient_name, doctor) VALUES (?, ?)"
            );
            ps.setString(1, pname);
            ps.setString(2, doctor);

            ps.executeUpdate();

            request.setAttribute("message", "Appointment Booked Successfully");
            request.getRequestDispatcher("success.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
