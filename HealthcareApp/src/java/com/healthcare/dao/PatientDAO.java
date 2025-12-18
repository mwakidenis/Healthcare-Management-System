package com.healthcare.dao;

import com.healthcare.model.Patient;
import com.healthcare.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PatientDAO {

    public static void addPatient(Patient p) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO patient (name, age, gender, contact) VALUES (?, ?, ?, ?)"
        );

        ps.setString(1, p.getName());
        ps.setInt(2, p.getAge());
        ps.setString(3, p.getGender());
        ps.setString(4, p.getContact());

        ps.executeUpdate();
    }
}
