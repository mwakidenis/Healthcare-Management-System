package dao.impl;

import dao.AppointmentDAO;
import model.Appointment;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public synchronized boolean createAppointment(Appointment a) throws Exception {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_datetime, status, notes, created_at) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, a.getPatientId());
                ps.setInt(2, a.getDoctorId());

                Timestamp ts = (a.getAppointmentDateTime() == null) ? null :
                        new Timestamp(a.getAppointmentDateTime().getTime());
                ps.setTimestamp(3, ts);

                ps.setString(4, a.getStatus());
                ps.setString(5, a.getNotes());

                int rows = ps.executeUpdate();
                if (rows == 0) {
                    con.rollback();
                    return false;
                }

                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) a.setId(keys.getInt(1));

                con.commit();
                return true;

            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    @Override
    public synchronized Appointment getAppointmentById(int id) throws Exception {

        String sql = "SELECT id, patient_id, doctor_id, appointment_datetime, status, notes " +
                "FROM appointments WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Appointment a = new Appointment();
                a.setId(rs.getInt("id"));
                a.setPatientId(rs.getInt("patient_id"));
                a.setDoctorId(rs.getInt("doctor_id"));

                Timestamp ts = rs.getTimestamp("appointment_datetime");
                a.setAppointmentDateTime(ts == null ? null : new Date(ts.getTime()));

                a.setStatus(rs.getString("status"));
                a.setNotes(rs.getString("notes"));

                return a;
            }
        }
        return null;
    }

    @Override
    public synchronized List<Appointment> getAppointmentsByDoctor(int doctorId) throws Exception {

        String sql = "SELECT id, patient_id, doctor_id, appointment_datetime, status, notes " +
                "FROM appointments WHERE doctor_id = ? ORDER BY appointment_datetime DESC";

        List<Appointment> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setId(rs.getInt("id"));
                a.setPatientId(rs.getInt("patient_id"));
                a.setDoctorId(rs.getInt("doctor_id"));

                Timestamp ts = rs.getTimestamp("appointment_datetime");
                a.setAppointmentDateTime(ts == null ? null : new Date(ts.getTime()));

                a.setStatus(rs.getString("status"));
                a.setNotes(rs.getString("notes"));

                list.add(a);
            }
        }

        return list;
    }

    @Override
    public synchronized  List<Appointment> getAppointmentsByPatient(int patientId) throws Exception {

        String sql = "SELECT id, patient_id, doctor_id, appointment_datetime, status, notes " +
                "FROM appointments WHERE patient_id = ? ORDER BY appointment_datetime DESC";

        List<Appointment> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setId(rs.getInt("id"));
                a.setPatientId(rs.getInt("patient_id"));
                a.setDoctorId(rs.getInt("doctor_id"));

                Timestamp ts = rs.getTimestamp("appointment_datetime");
                a.setAppointmentDateTime(ts == null ? null : new Date(ts.getTime()));

                a.setStatus(rs.getString("status"));
                a.setNotes(rs.getString("notes"));

                list.add(a);
            }
        }

        return list;
    }

    @Override
    public synchronized  boolean updateAppointment(Appointment a) throws Exception {

        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_datetime = ?, status = ?, notes = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, a.getPatientId());
                ps.setInt(2, a.getDoctorId());

                Timestamp ts = (a.getAppointmentDateTime() == null) ? null :
                        new Timestamp(a.getAppointmentDateTime().getTime());
                ps.setTimestamp(3, ts);

                ps.setString(4, a.getStatus());
                ps.setString(5, a.getNotes());
                ps.setInt(6, a.getId());

                int rows = ps.executeUpdate();
                if (rows == 0) {
                    con.rollback();
                    return false;
                }

                con.commit();
                return true;

            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    @Override
    public synchronized boolean deleteAppointment(int id) throws Exception {

        String sql = "DELETE FROM appointments WHERE id = ?";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                int rows = ps.executeUpdate();

                if (rows == 0) {
                    con.rollback();
                    return false;
                }

                con.commit();
                return true;

            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    @Override
    public synchronized List<Appointment> getAllAppointments() throws Exception {

        List<Appointment> list = new ArrayList<>();

        String sql = "SELECT id, patient_id, doctor_id, appointment_datetime, status, notes FROM appointments ORDER BY appointment_datetime DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Appointment a = new Appointment();
                a.setId(rs.getInt("id"));
                a.setPatientId(rs.getInt("patient_id"));
                a.setDoctorId(rs.getInt("doctor_id"));

                Timestamp ts = rs.getTimestamp("appointment_datetime");
                a.setAppointmentDateTime(ts == null ? null : new Date(ts.getTime()));

                a.setStatus(rs.getString("status"));
                a.setNotes(rs.getString("notes"));

                list.add(a);
            }
        }
        return list;
    }
}
