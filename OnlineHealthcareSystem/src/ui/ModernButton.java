package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Rounded gradient modern button.
 */
public class ModernButton extends JButton {

    public ModernButton(String text) {
        super(text);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(Theme.BUTTON_FONT);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        // hover behavior handled via mouse listener
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(Theme.PRIMARY_DARK);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(Theme.PRIMARY);
            }
        });

        // initial background (used in paintComponent)
        setBackground(Theme.PRIMARY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth(), h = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color c1 = getBackground().brighter();
        Color c2 = getBackground().darker();
        GradientPaint gp = new GradientPaint(0, 0, c1, 0, h, c2);

        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, w, h, 18, 18);

        // text
        super.paintComponent(g2);
        g2.dispose();
    }

    // ensure text paints over gradient
    @Override
    public boolean isOpaque() { return false; }
}
