package com.healthcare;

import com.healthcare.util.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/DoctorServlet")
public class DoctorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String specialization = request.getParameter("specialization");
        String contact = request.getParameter("contact");

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO doctor (name, specialization, contact) VALUES (?, ?, ?)"
            );

            ps.setString(1, name);
            ps.setString(2, specialization);
            ps.setString(3, contact);

            ps.executeUpdate();

            request.setAttribute("message", "Doctor added successfully");
            request.getRequestDispatcher("success.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("addDoctor.jsp");
        }
    }
}
