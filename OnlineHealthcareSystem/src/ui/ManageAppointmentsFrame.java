package ui;
import util.UITheme;

import dao.AppointmentDAO;
import dao.impl.AppointmentDAOImpl;
import model.Appointment;
import util.DBConnection;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ManageAppointmentsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<ComboItem> cbDoctor;
    private JComboBox<ComboItem> cbPatient;

    private UtilDateModel dateModel;
    private JDatePickerImpl datePicker;

    private JComboBox<String> cbStatus;
    private JTextArea taNotes;
    private JTextField txtSearch;

    private final SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();

    public ManageAppointmentsFrame() {
        UITheme.applyTheme();

        setTitle("Manage Appointments - Admin");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Appointment Management", SwingConstants.CENTER);
        header.setOpaque(true);
        header.setBackground(new Color(55, 110, 170));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.setPreferredSize(new Dimension(0, 55));
        add(header, BorderLayout.NORTH);

        // SEARCH BAR
        txtSearch = new JTextField();
        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("🔍 "), BorderLayout.WEST);
        top.add(txtSearch, BorderLayout.CENTER);

        // TABLE
        String[] cols = {"ID", "Patient", "Doctor", "DateTime", "Status", "Notes"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);

        JPanel center = new JPanel(new BorderLayout());
        center.add(top, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        // --------------------------------------------
        // RIGHT SIDE FORM
        // --------------------------------------------
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Doctor
        form.add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 1;
        cbDoctor = new JComboBox<>();
        form.add(cbDoctor, gbc);

        // Patient
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        cbPatient = new JComboBox<>();
        form.add(cbPatient, gbc);

        // DATE PICKER
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;
        dateModel = new UtilDateModel();
        dateModel.setSelected(false);

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        datePicker = new JDatePickerImpl(datePanel, new SafeDateFormatter());

        form.add(datePicker, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        cbStatus = new JComboBox<>(new String[]{"SCHEDULED", "CONFIRMED", "COMPLETED", "CANCELLED"});
        form.add(cbStatus, gbc);

        // Notes
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1;
        taNotes = new JTextArea(4, 18);
        form.add(new JScrollPane(taNotes), gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnClear = new JButton("Clear");
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnClear);
        form.add(btnPanel, gbc);

        add(form, BorderLayout.EAST);

        // SEARCH FILTER
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilter(); }
            public void removeUpdate(DocumentEvent e) { applyFilter(); }
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
            private void applyFilter() {
                String s = txtSearch.getText();
                if (s.trim().isEmpty()) sorter.setRowFilter(null);
                else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + s));
            }
        });

        // TABLE SELECTION
        table.getSelectionModel().addListSelectionListener(e -> fillForm());

        // HANDLERS
        btnRefresh.addActionListener(e -> loadEverything());
        btnClear.addActionListener(e -> clearForm());
        btnAdd.addActionListener(e -> addAppointment());
        btnUpdate.addActionListener(e -> updateAppointment());
        btnDelete.addActionListener(e -> deleteAppointment());

        loadEverythingAsync();

    }

    // ====================== COMBO ITEM ======================
    private static class ComboItem {
        int id; String label;
        ComboItem(int id, String label) { this.id = id; this.label = label; }
        public String toString() { return label; }
    }

    // ====================== LOAD DATA ======================

    private void loadEverything() {
        loadDoctors();
        loadPatients();
        loadAppointments();
        clearForm();
    }

    private void loadDoctors() {
        cbDoctor.removeAllItems();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT d.id, u.name FROM doctors d JOIN users u ON d.user_id=u.id ORDER BY u.name")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) cbDoctor.addItem(new ComboItem(rs.getInt(1), rs.getString(2)));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadPatients() {
        cbPatient.removeAllItems();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT p.id, u.name FROM patients p JOIN users u ON p.user_id=u.id ORDER BY u.name")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) cbPatient.addItem(new ComboItem(rs.getInt(1), rs.getString(2)));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT a.id, u_p.name, u_d.name, a.appointment_datetime, a.status, a.notes " +
                             "FROM appointments a " +
                             "JOIN patients p ON a.patient_id=p.id JOIN users u_p ON p.user_id=u_p.id " +
                             "JOIN doctors d ON a.doctor_id=d.id JOIN users u_d ON d.user_id=u_d.id " +
                             "ORDER BY appointment_datetime DESC")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("appointment_datetime");
                String formatted = ts == null ? "" : dtf.format(new java.util.Date(ts.getTime()));

                tableModel.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        formatted,
                        rs.getString(5),
                        rs.getString(6)
                });
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    // ====================== FILL FORM ======================
    private void fillForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        int modelRow = table.convertRowIndexToModel(row);

        String patient = (String) tableModel.getValueAt(modelRow, 1);
        String doctor = (String) tableModel.getValueAt(modelRow, 2);
        String dt = (String) tableModel.getValueAt(modelRow, 3);
        String status = (String) tableModel.getValueAt(modelRow, 4);
        String notes = (String) tableModel.getValueAt(modelRow, 5);

        selectCombo(cbPatient, patient);
        selectCombo(cbDoctor, doctor);

        try {
            java.util.Date d = dtf.parse(dt);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            dateModel.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dateModel.setSelected(true);
        } catch (Exception ignored) {}

        cbStatus.setSelectedItem(status);
        taNotes.setText(notes);
    }

    private void selectCombo(JComboBox<ComboItem> cb, String label) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (cb.getItemAt(i).label.equals(label)) {
                cb.setSelectedIndex(i);
                break;
            }
        }
    }

    // ====================== ADD ======================
   private void addAppointment() {

    if (!dateModel.isSelected()) {
        JOptionPane.showMessageDialog(this, "Select a date");
        return;
    }

    ComboItem doc = (ComboItem) cbDoctor.getSelectedItem();
    ComboItem pat = (ComboItem) cbPatient.getSelectedItem();
java.util.Date dt = (java.util.Date) datePicker.getModel().getValue();

    Appointment a = new Appointment(
            pat.id,
            doc.id,
            dt,
            (String) cbStatus.getSelectedItem(),
            taNotes.getText()
    );

    new SwingWorker<Boolean, Void>() {

        @Override
        protected Boolean doInBackground() throws Exception {
            return appointmentDAO.createAppointment(a);
        }

        @Override
        protected void done() {
            try {
                if (get()) {
                    JOptionPane.showMessageDialog(null, "Appointment added!");
                    loadEverythingAsync();
                } else {
                    JOptionPane.showMessageDialog(null, "Add failed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }.execute();
}

    // ====================== UPDATE ======================
   private void updateAppointment() {

    int row = table.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(this, "Select row to update");
        return;
    }

    if (!dateModel.isSelected()) {
        JOptionPane.showMessageDialog(this, "Select a date");
        return;
    }

    int id = (int) table.getValueAt(row, 0);
    ComboItem doc = (ComboItem) cbDoctor.getSelectedItem();
    ComboItem pat = (ComboItem) cbPatient.getSelectedItem();
java.util.Date dt = (java.util.Date) datePicker.getModel().getValue();

    Appointment a = new Appointment();
    a.setId(id);
    a.setDoctorId(doc.id);
    a.setPatientId(pat.id);
    a.setAppointmentDateTime(dt);
    a.setStatus((String) cbStatus.getSelectedItem());
    a.setNotes(taNotes.getText());

    new SwingWorker<Boolean, Void>() {

        @Override
        protected Boolean doInBackground() throws Exception {
            return appointmentDAO.updateAppointment(a);
        }

        @Override
        protected void done() {
            try {
                if (get()) {
                    JOptionPane.showMessageDialog(null, "Updated!");
                    loadEverythingAsync();
                } else {
                    JOptionPane.showMessageDialog(null, "Update failed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }.execute();
}


    // ====================== DELETE ======================
   private void deleteAppointment() {

    int row = table.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(this, "Select row to delete");
        return;
    }

    int id = (int) table.getValueAt(row, 0);

    new SwingWorker<Boolean, Void>() {

        @Override
        protected Boolean doInBackground() throws Exception {
            return appointmentDAO.deleteAppointment(id);
        }

        @Override
        protected void done() {
            try {
                if (get()) {
                    JOptionPane.showMessageDialog(null, "Deleted!");
                    loadEverythingAsync();
                } else {
                    JOptionPane.showMessageDialog(null, "Delete failed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }.execute();
}


    // ====================== CLEAR ======================
    private void clearForm() {

        if (cbDoctor.getItemCount() > 0) cbDoctor.setSelectedIndex(0);
        if (cbPatient.getItemCount() > 0) cbPatient.setSelectedIndex(0);

        dateModel.setSelected(false);
        cbStatus.setSelectedIndex(0);
        taNotes.setText("");
        table.clearSelection();
    }

    // ====================== SAFE FORMATTER ======================
    // Fix for: "Cannot format given Object as Date"
    public static class SafeDateFormatter extends JFormattedTextField.AbstractFormatter {

        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Object stringToValue(String text) throws ParseException {
            if (text == null || text.trim().isEmpty()) return null;
            return sdf.parse(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value == null) return "";
            if (value instanceof java.util.Date) return sdf.format((java.util.Date) value);
            if (value instanceof Calendar) return sdf.format(((Calendar) value).getTime());
            if (value instanceof java.sql.Date) return sdf.format(new java.util.Date(((java.sql.Date) value).getTime()));
            return "";
        }
    }
    private void loadEverythingAsync() {

    new SwingWorker<Void, Void>() {

        @Override
        protected Void doInBackground() throws Exception {
            loadDoctors();
            loadPatients();
            loadAppointments();
            return null;
        }

        @Override
        protected void done() {
            clearForm();
        }

    }.execute();
}

}
