package com.healthcare.dao;

import com.healthcare.model.Doctor;
import com.healthcare.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DoctorDAO {

    public static void addDoctor(Doctor d) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO doctor (name, specialization, contact) VALUES (?, ?, ?)"
        );

        ps.setString(1, d.getName());
        ps.setString(2, d.getSpecialization());
        ps.setString(3, d.getContact());

        ps.executeUpdate();
    }
}
