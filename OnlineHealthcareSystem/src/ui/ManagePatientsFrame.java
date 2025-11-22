package ui;
import util.UITheme;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.Patient;
import model.User;
import util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;

public class ManagePatientsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField tfName, tfEmail, tfPassword, tfAge, tfPhone;
    private JComboBox<String> cbGender;
    private JTextArea taHistory;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnClear, btnViewHistory;

    private UserDAO userDAO = new UserDAOImpl();

    public ManagePatientsFrame() {
        UITheme.applyTheme();
        setTitle("Manage Patients - Admin");
        setSize(980, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top header
        JLabel header = new JLabel("Patient Management", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(55, 115, 170));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 64));
        add(header, BorderLayout.NORTH);

        // Table area (center-left)
        tableModel = new DefaultTableModel(new Object[]{"PatientRowID","UserID","Name","Email","Age","Gender","Phone"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.removeColumn(table.getColumnModel().getColumn(0)); // hide patient.id
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(600, 0));
        add(sp, BorderLayout.CENTER);

        // Right: form panel
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        tfName = new JTextField(20); form.add(tfName, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        tfEmail = new JTextField(20); form.add(tfEmail, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        tfPassword = new JTextField(20); form.add(tfPassword, gbc);
        tfPassword.setToolTipText("Initial password for patient (required at creation).");

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        tfAge = new JTextField(10); form.add(tfAge, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        cbGender = new JComboBox<>(new String[]{"M","F","O"}); form.add(cbGender, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        tfPhone = new JTextField(15); form.add(tfPhone, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Medical History:"), gbc);
        gbc.gridx = 1;
        taHistory = new JTextArea(6, 20);
        taHistory.setLineWrap(true);
        taHistory.setWrapStyleWord(true);
        JScrollPane histScroll = new JScrollPane(taHistory);
        form.add(histScroll, gbc);

        // Buttons row
        JPanel bp = new JPanel(new GridLayout(1, 6, 6, 6));
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");
        btnClear = new JButton("Clear");
        btnViewHistory = new JButton("View History");
        bp.add(btnAdd); bp.add(btnUpdate); bp.add(btnDelete); bp.add(btnRefresh); bp.add(btnClear); bp.add(btnViewHistory);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        form.add(bp, gbc);

        add(form, BorderLayout.EAST);

        // Actions
        btnRefresh.addActionListener(e -> loadPatients());
        btnClear.addActionListener(e -> clearForm());

        btnAdd.addActionListener(e -> {
            try { addPatient(); } catch (Exception ex) { showError(ex); }
        });

        btnUpdate.addActionListener(e -> {
            try { updatePatient(); } catch (Exception ex) { showError(ex); }
        });

        btnDelete.addActionListener(e -> {
            try { deletePatient(); } catch (Exception ex) { showError(ex); }
        });

        btnViewHistory.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a patient to view history."); return; }
            int modelRow = table.convertRowIndexToModel(sel);
            int userId = (int) tableModel.getValueAt(modelRow, 1);
            showHistoryDialog(userId);
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelectedRow();
        });

        // initial load
        loadPatients();
    }

    private void showError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }

    private void clearForm() {
        tfName.setText("");
        tfEmail.setText("");
        tfPassword.setText("");
        tfAge.setText("");
        cbGender.setSelectedIndex(0);
        tfPhone.setText("");
        taHistory.setText("");
        table.clearSelection();
    }

    private void fillFormFromSelectedRow() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        int modelRow = table.convertRowIndexToModel(r);
        tfName.setText((String) tableModel.getValueAt(modelRow, 2));
        tfEmail.setText((String) tableModel.getValueAt(modelRow, 3));
        Object ageObj = tableModel.getValueAt(modelRow, 4);
        tfAge.setText(ageObj == null ? "" : String.valueOf(ageObj));
        String gender = (String) tableModel.getValueAt(modelRow, 5);
        cbGender.setSelectedItem(gender == null ? "M" : gender);
        tfPhone.setText((String) tableModel.getValueAt(modelRow, 6));

        // load medical history from patients table by userId
        int userId = (int) tableModel.getValueAt(modelRow, 1);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT medical_history FROM patients WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) taHistory.setText(rs.getString("medical_history"));
                else taHistory.setText("");
            }
        } catch (Exception ex) {
            showError(ex);
        }

        tfPassword.setText(""); // do not show password
    }

    // Loads patients (JOIN users + patients)
    private void loadPatients() {
        tableModel.setRowCount(0);
        String sql = "SELECT p.id AS pid, u.id AS uid, u.name, u.email, p.age, p.gender, p.phone " +
                "FROM patients p JOIN users u ON p.user_id = u.id ORDER BY u.name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Object[]> list = new ArrayList<>(); // demonstrates use of collections framework
            while (rs.next()) {
                Object[] row = new Object[7];
                row[0] = rs.getInt("pid");   // hidden patient.id
                row[1] = rs.getInt("uid");   // users.id
                row[2] = rs.getString("name");
                row[3] = rs.getString("email");
                row[4] = rs.getObject("age");
                row[5] = rs.getString("gender");
                row[6] = rs.getString("phone");
                list.add(row);
            }
            // add to table model
            for (Object[] r : list) {
                Vector<Object> vec = new Vector<>();
                for (Object o : r) vec.add(o);
                tableModel.addRow(vec);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    // Add patient using a JDBC transaction: create user then patient row (atomic)
    private void addPatient() throws Exception {
        String name = tfName.getText().trim();
        String email = tfEmail.getText().trim();
        String password = tfPassword.getText().trim();
        String ageStr = tfAge.getText().trim();
        String gender = (String) cbGender.getSelectedItem();
        String phone = tfPhone.getText().trim();
        String history = taHistory.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Email and Password are required.");
            return;
        }

        Integer age = null;
        if (!ageStr.isEmpty()) {
            try { age = Integer.parseInt(ageStr); }
            catch (NumberFormatException nfe) { JOptionPane.showMessageDialog(this, "Age must be a number."); return; }
        }

        Connection con = null;
        PreparedStatement psUser = null;
        PreparedStatement psPatient = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false); // begin transaction

            // 1) create user
            String uSql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
            psUser = con.prepareStatement(uSql, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, name);
            psUser.setString(2, email);
            psUser.setString(3, password);
            psUser.setString(4, "PATIENT");
            int r = psUser.executeUpdate();
            if (r <= 0) throw new SQLException("Failed to insert user");

            int userId;
            try (ResultSet g = psUser.getGeneratedKeys()) {
                if (g.next()) userId = g.getInt(1);
                else throw new SQLException("Failed to retrieve user id");
            }

            // 2) insert patient row
            String pSql = "INSERT INTO patients (user_id, age, gender, phone, medical_history) VALUES (?, ?, ?, ?, ?)";
            psPatient = con.prepareStatement(pSql);
            psPatient.setInt(1, userId);
            if (age == null) psPatient.setNull(2, Types.INTEGER); else psPatient.setInt(2, age);
            psPatient.setString(3, gender);
            psPatient.setString(4, phone);
            psPatient.setString(5, history);
            psPatient.executeUpdate();

            con.commit(); // success
            JOptionPane.showMessageDialog(this, "Patient added successfully.");
            clearForm();
            loadPatients();
        } catch (Exception ex) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ignore) {}
            }
            throw ex;
        } finally {
            if (psUser != null) try { psUser.close(); } catch (Exception ignore) {}
            if (psPatient != null) try { psPatient.close(); } catch (Exception ignore) {}
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (Exception ignore) {}
        }
    }

    private void updatePatient() throws Exception {
        int sel = table.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a patient to update."); return; }
        int modelRow = table.convertRowIndexToModel(sel);
        int userId = (int) tableModel.getValueAt(modelRow, 1);
        int patientRowId = (int) tableModel.getValueAt(modelRow, 0);

        String name = tfName.getText().trim();
        String email = tfEmail.getText().trim();
        String password = tfPassword.getText().trim();
        String ageStr = tfAge.getText().trim();
        String gender = (String) cbGender.getSelectedItem();
        String phone = tfPhone.getText().trim();
        String history = taHistory.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Email are required.");
            return;
        }

        Integer age = null;
        if (!ageStr.isEmpty()) {
            try { age = Integer.parseInt(ageStr); }
            catch (NumberFormatException nfe) { JOptionPane.showMessageDialog(this, "Age must be a number."); return; }
        }

        // Update users table
        User u = new User();
        u.setId(userId);
        u.setName(name);
        u.setEmail(email);
        u.setRole("PATIENT");
        userDAO.updateUser(u);

        // optional password update
        if (!password.isEmpty()) {
            String psql = "UPDATE users SET password = ? WHERE id = ?";
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(psql)) {
                ps.setString(1, password);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }
        }

        // update patients table
        String sql = "UPDATE patients SET age = ?, gender = ?, phone = ?, medical_history = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (age == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, age);
            ps.setString(2, gender);
            ps.setString(3, phone);
            ps.setString(4, history);
            ps.setInt(5, patientRowId);
            ps.executeUpdate();
        }

        JOptionPane.showMessageDialog(this, "Patient updated.");
        clearForm();
        loadPatients();
    }

    private void deletePatient() throws Exception {
        int sel = table.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a patient to delete."); return; }
        int modelRow = table.convertRowIndexToModel(sel);
        int userId = (int) tableModel.getValueAt(modelRow, 1);

        int ok = JOptionPane.showConfirmDialog(this, "Delete selected patient?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        boolean deleted = userDAO.deleteUser(userId);
        if (deleted) {
            JOptionPane.showMessageDialog(this, "Patient deleted.");
            clearForm();
            loadPatients();
        } else {
            JOptionPane.showMessageDialog(this, "Delete failed.");
        }
    }

    private void showHistoryDialog(int userId) {
        String history = "";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT medical_history FROM patients WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) history = rs.getString("medical_history");
            }
        } catch (Exception ex) {
            showError(ex);
            return;
        }

        JTextArea area = new JTextArea(history, 15, 40);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Medical History", JOptionPane.INFORMATION_MESSAGE);
    }

    // quick launcher for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagePatientsFrame().setVisible(true));
    }
}
