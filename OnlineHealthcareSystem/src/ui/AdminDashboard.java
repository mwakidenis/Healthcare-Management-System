package ui;
import util.UITheme;
import javax.swing.*;
import java.awt.*;

/**
 * Modern Admin Dashboard using Theme and ModernButton
 */
public class AdminDashboard extends JFrame {

    private ModernButton btnManageDoctors;
    private ModernButton btnManagePatients;
    private ModernButton btnManageAppointments;
    private ModernButton btnLogout;

    public AdminDashboard() {
        UITheme.applyTheme();
        setTitle("Admin Dashboard - Online Healthcare System");
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG);

        getContentPane().setBackground(Theme.BG);

        // Header with gradient
        JPanel header = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Theme.PRIMARY, getWidth(), 0, Theme.PRIMARY_DARK);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 70));
        header.setLayout(new BorderLayout());
        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(Theme.H1);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // Left menu card
        RoundedPanel leftCard = new RoundedPanel();
        leftCard.setLayout(new GridBagLayout());
        leftCard.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 16));

        btnManageDoctors = new ModernButton("Manage Doctors");
        btnManagePatients = new ModernButton("Manage Patients");
        btnManageAppointments = new ModernButton("Manage Appointments");
        btnLogout = new ModernButton("Logout");

        // layout the buttons vertically
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0; leftCard.add(btnManageDoctors, gbc);
        gbc.gridy = 1; leftCard.add(btnManagePatients, gbc);
        gbc.gridy = 2; leftCard.add(btnManageAppointments, gbc);
        gbc.gridy = 3; leftCard.add(Box.createVerticalStrut(12), gbc);
        gbc.gridy = 4; leftCard.add(btnLogout, gbc);

        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.setBackground(Theme.BG);
        leftWrapper.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        leftWrapper.add(leftCard, BorderLayout.CENTER);


        add(leftWrapper, BorderLayout.WEST);

        // Center content area (simple welcome card)
        RoundedPanel centerCard = new RoundedPanel();
        centerCard.setLayout(new BorderLayout());
        centerCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel welcome = new JLabel("<html><div style='text-align:center'>Welcome Admin<br><small style='font-size:12px;color:#555;'>Manage doctors, patients and appointments</small></div></html>", SwingConstants.CENTER);
        welcome.setFont(Theme.H2);
        centerCard.add(welcome, BorderLayout.CENTER);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Theme.BG);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerWrapper.add(centerCard, BorderLayout.CENTER);

        add(centerWrapper, BorderLayout.CENTER);

        // Button actions (open frames)
        btnManageDoctors.addActionListener(e -> SwingUtilities.invokeLater(() -> new ManageDoctorsFrame().setVisible(true)));
        btnManagePatients.addActionListener(e -> SwingUtilities.invokeLater(() -> new ManagePatientsFrame().setVisible(true)));
        btnManageAppointments.addActionListener(e -> SwingUtilities.invokeLater(() -> new ManageAppointmentsFrame().setVisible(true)));
        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    // quick test main (you can delete if you have main elsewhere)
    public static void main(String[] args) {
        try { com.formdev.flatlaf.FlatIntelliJLaf.setup(); } catch (Exception ignored) {}
        Theme.apply();
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
