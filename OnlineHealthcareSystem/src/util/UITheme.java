package util;

import javax.swing.*;
import java.awt.*;

/**
 * Premium Glassmorphism UI Theme for entire system
 */
public class UITheme {

    public static void applyTheme() {

        // Global Font
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextArea.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));

        // Smooth rounded borders
        UIManager.put("Button.arc", 20);
        UIManager.put("Component.arc", 20);
        UIManager.put("TextComponent.arc", 15);

        // Glass background look
        UIManager.put("Panel.background", new Color(245, 248, 255));
        UIManager.put("Button.background", new Color(58, 123, 213));
        UIManager.put("Button.foreground", Color.WHITE);

        // Table color clean
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.selectionBackground", new Color(58, 123, 213));
        UIManager.put("Table.selectionForeground", Color.WHITE);
    }

    // Gradient Header Creator
    public static JPanel createHeader(String text) {

        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(58, 123, 213),
                        getWidth(), 0, new Color(83, 203, 241)
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
    }

    // Rounded Card Panel
    public static JPanel card() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setOpaque(false);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2.setColor(new Color(220, 220, 220));
                g2.drawRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        p.setLayout(new BorderLayout());
        return p;
    }
}
