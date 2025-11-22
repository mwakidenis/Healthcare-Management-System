package dao;

import model.Appointment;
import java.util.List;

public interface AppointmentDAO {
    boolean createAppointment(Appointment a) throws Exception;
    boolean updateAppointment(Appointment a) throws Exception;
    boolean deleteAppointment(int appointmentId) throws Exception;
    Appointment getAppointmentById(int id) throws Exception;
    List<Appointment> getAllAppointments() throws Exception;
    List<Appointment> getAppointmentsByDoctor(int doctorId) throws Exception;
    List<Appointment> getAppointmentsByPatient(int patientId) throws Exception;
}
