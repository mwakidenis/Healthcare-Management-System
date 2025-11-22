package ui;
import java.util.regex.Pattern;
import util.UITheme;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.User;
import util.DBConnection;
import util.PasswordUtil;
import util.Validation;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**
 * ManageDoctorsFrame
 * Full CRUD UI for doctors (Admin)
 */
public class ManageDoctorsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    private JTextField tfName, tfEmail, tfPassword, tfSpecialization, tfPhone;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnClear;
    private JLabel lblErrors;               // class-level error label (used everywhere)

    private final UserDAO userDAO = new UserDAOImpl();

    public ManageDoctorsFrame() {
        UITheme.applyTheme();
        setTitle("Manage Doctors - Admin");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top header
        JLabel header = new JLabel("Doctor Management", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(60, 120, 180));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 60));
        add(header, BorderLayout.NORTH);

        // -------------------------
        // SEARCH BAR - MUST be created BEFORE listener uses it
        // -------------------------
        txtSearch = new JTextField();
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(220, 32));
        txtSearch.setToolTipText("Search doctors by name, email, phone, specialization...");

        JPanel searchPanel = new JPanel(new BorderLayout(6, 6));
        JLabel lblSearchIcon = new JLabel("🔍");
        lblSearchIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        searchPanel.add(lblSearchIcon, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // -------------------------
        // TABLE SETUP
        // -------------------------
        String[] headers = {"DoctorRowID", "UserID", "Name", "Email", "Specialization", "Phone"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);

        // hide the first column (internal doctors.id)
        // removeColumn expects a TableColumn from the columnModel, so only do this after table is created
        table.removeColumn(table.getColumnModel().getColumn(0));

        // Add table inside scroll pane
        JScrollPane sp = new JScrollPane(table);
        centerPanel.add(sp, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // -------------------------
        // LIVE SEARCH (after txtSearch and table are created)
        // -------------------------
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { search(); }
            @Override public void removeUpdate(DocumentEvent e) { search(); }
            @Override public void changedUpdate(DocumentEvent e) { search(); }

            private void search() {
                String text = txtSearch.getText();
                if (text == null || text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    // (?i) for case-insensitive
       sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
                }
            }
        });

        // -------------------------
        // RIGHT: FORM PANEL
        // -------------------------
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        tfName = new JTextField(18);
        form.add(tfName, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        tfEmail = new JTextField(18);
        form.add(tfEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        form.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        tfPassword = new JTextField(18);
        tfPassword.setToolTipText("Set initial password for doctor (required for creation).");
        form.add(tfPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        form.add(new JLabel("Specialization:"), gbc);
        gbc.gridx = 1;
        tfSpecialization = new JTextField(18);
        form.add(tfSpecialization, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        form.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        tfPhone = new JTextField(18);
        form.add(tfPhone, gbc);

        // Buttons row
        JPanel bp = new JPanel(new GridLayout(1, 5, 6, 6));
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");
        btnClear = new JButton("Clear");
        bp.add(btnAdd);
        bp.add(btnUpdate);
        bp.add(btnDelete);
        bp.add(btnRefresh);
        bp.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        form.add(bp, gbc);

        // ERROR label (use the class-level lblErrors)
        lblErrors = new JLabel("");
        lblErrors.setForeground(Color.RED);
        lblErrors.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        form.add(lblErrors, gbc);

        add(form, BorderLayout.EAST);

        // -------------------------
        // EVENTS
        // -------------------------
        btnRefresh.addActionListener(e -> loadDoctors());
        btnClear.addActionListener(e -> clearForm());

        btnAdd.addActionListener(e -> {
            try {
                addDoctor();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                updateDoctor();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                deleteDoctor();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelectedRow();
        });

        // initial load
        loadDoctors();
    }

    // Show error nicely
    private void showError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }

    // Clear input fields
    private void clearForm() {
        tfName.setText("");
        tfEmail.setText("");
        tfPassword.setText("");
        tfSpecialization.setText("");
        tfPhone.setText("");
        table.clearSelection();
        lblErrors.setText("");
    }

    // Fill form when selecting a row
    private void fillFormFromSelectedRow() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) return;
        int modelRow = table.convertRowIndexToModel(viewRow);

        // Note: model columns are: 0=did, 1=uid, 2=name, 3=email, 4=specialization, 5=phone
        tfName.setText((String) tableModel.getValueAt(modelRow, 2));
        tfEmail.setText((String) tableModel.getValueAt(modelRow, 3));
        tfSpecialization.setText((String) tableModel.getValueAt(modelRow, 4));
        tfPhone.setText((String) tableModel.getValueAt(modelRow, 5));
        tfPassword.setText(""); // do not display password
    }

    // Load doctors from DB (join users + doctors)
    private void loadDoctors() {
        tableModel.setRowCount(0);
        String sql = "SELECT d.id AS did, u.id AS uid, u.name, u.email, d.specialization, d.phone " +
                "FROM doctors d JOIN users u ON d.user_id = u.id ORDER BY u.name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("did"));   // doctors table id (hidden)
                row.add(rs.getInt("uid"));   // user id
                row.add(rs.getString("name"));
                row.add(rs.getString("email"));
                row.add(rs.getString("specialization"));
                row.add(rs.getString("phone"));
                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    // Add new doctor: create user (role DOCTOR) then insert into doctors table
    private void addDoctor() throws Exception {
        String name = tfName.getText().trim();
        String email = tfEmail.getText().trim();
        String password = tfPassword.getText(); // do not trim password to avoid removing spaces
        String specialization = tfSpecialization.getText().trim();
        String phone = tfPhone.getText().trim();

        StringBuilder errors = new StringBuilder();

        // VALIDATION
        if (!Validation.isValidName(name))
            errors.append("⚠️ Invalid name (letters only)<br>");
        if (!Validation.isValidEmail(email))
            errors.append("⚠️ Invalid email<br>");
        if (Validation.isEmpty(password))
            errors.append("⚠️ Password cannot be empty<br>");
        if (!Validation.isValidPhone(phone))
            errors.append("⚠️ Phone must be 10 digits<br>");

        if (errors.length() > 0) {
            lblErrors.setText("<html>" + errors + "</html>");
            JOptionPane.showMessageDialog(this, "Please correct the highlighted errors!");
            return;
        }

        lblErrors.setText("");

        // Backend logic
        User u = new User(name, email, password, "DOCTOR");
        boolean ok = userDAO.createUser(u);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Failed to create user.");
            return;
        }

        String sql = "INSERT INTO doctors (user_id, specialization, phone) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, u.getId());
            ps.setString(2, specialization);
            ps.setString(3, phone);
            ps.executeUpdate();
        }

        JOptionPane.showMessageDialog(this, "Doctor added successfully.");
        clearForm();
        loadDoctors();
    }

    // Update doctor: update users table and doctors table
    private void updateDoctor() throws Exception {
        int sel = table.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Select a doctor to update.");
            return;
        }

        int modelRow = table.convertRowIndexToModel(sel);
        int userId = (int) tableModel.getValueAt(modelRow, 1);
        int doctorRowId = (int) tableModel.getValueAt(modelRow, 0);

        String name = tfName.getText().trim();
        String email = tfEmail.getText().trim();
        String password = tfPassword.getText(); // optional: only update if not empty
        String specialization = tfSpecialization.getText().trim();
        String phone = tfPhone.getText().trim();

        StringBuilder errors = new StringBuilder();

        if (!Validation.isValidName(name))
            errors.append("⚠️ Invalid name (letters only)<br>");
        if (!Validation.isValidEmail(email))
            errors.append("⚠️ Invalid email<br>");
        if (!Validation.isValidPhone(phone))
            errors.append("⚠️ Phone must be 10 digits<br>");

        if (errors.length() > 0) {
            lblErrors.setText("<html>" + errors + "</html>");
            JOptionPane.showMessageDialog(this, "Please correct the errors!");
            return;
        }

        lblErrors.setText("");

        // Update users table (name,email). Password update handled separately if provided.
        userDAO.updateUser(new User(userId, name, email, null, "DOCTOR"));

        if (password != null && !password.isEmpty()) {
            String psql = "UPDATE users SET password = ? WHERE id = ?";
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(psql)) {
                ps.setString(1, PasswordUtil.hashPassword(password));
                ps.setInt(2, userId);
                ps.executeUpdate();
            }
        }

        String dsql = "UPDATE doctors SET specialization = ?, phone = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(dsql)) {
            ps.setString(1, specialization);
            ps.setString(2, phone);
            ps.setInt(3, doctorRowId);
            ps.executeUpdate();
        }

        JOptionPane.showMessageDialog(this, "Doctor updated.");
        clearForm();
        loadDoctors();
    }

    // Delete doctor: delete user (cascades to doctors)
    private void deleteDoctor() throws Exception {
        int sel = table.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Select a doctor to delete.");
            return;
        }

        int modelRow = table.convertRowIndexToModel(sel);
        int userId = (int) tableModel.getValueAt(modelRow, 1);

        int ok = JOptionPane.showConfirmDialog(this, "Delete selected doctor?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        boolean deleted = userDAO.deleteUser(userId);
        if (deleted) {
            JOptionPane.showMessageDialog(this, "Doctor deleted.");
            clearForm();
            loadDoctors();
        } else {
            JOptionPane.showMessageDialog(this, "Delete failed.");
        }
    }

    // helper main for quick test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageDoctorsFrame().setVisible(true));
    }
}
