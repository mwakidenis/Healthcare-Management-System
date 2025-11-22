package ui;
import util.UITheme;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        UITheme.applyTheme();
        setTitle("Online Healthcare System - Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblEmail = new JLabel("Email:");
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        JLabel lblPassword = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        btnLogin = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        add(panel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> loginAction());
    }


    private void loginAction() {

        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email & password");
            return;
        }

        System.out.println("LOGIN TRY -> Email: " + email + " Password: " + password);

        UserDAO dao = new UserDAOImpl();

        try {
            User user = dao.authenticate(email, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid login credentials");
                return;
            }

            JOptionPane.showMessageDialog(this, "Login Successful");

            new AdminDashboard().setVisible(true);
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
