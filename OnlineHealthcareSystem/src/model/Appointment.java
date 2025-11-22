package model;

import java.util.Date;

public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private Date appointmentDateTime;
    private String status;
    private String notes;

    public Appointment() { }

    public Appointment(int id, int patientId, int doctorId, Date appointmentDateTime, String status, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
        this.notes = notes;
    }

    public Appointment(int patientId, int doctorId, Date appointmentDateTime, String status, String notes) {
        this(0, patientId, doctorId, appointmentDateTime, status, notes);
    }

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public Date getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(Date appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
