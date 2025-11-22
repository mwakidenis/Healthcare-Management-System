package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Simple rounded white card panel used for forms.
 */
public class RoundedPanel extends JPanel {
    private final int arc = 18;
    public RoundedPanel() {
        setOpaque(false);
        setBackground(Theme.CARD);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        // shadow
        g2.setColor(Theme.SHADOW);
        g2.fillRoundRect(4, 4, w - 8, h - 8, arc, arc);
        // card
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, w - 8, h - 8, arc, arc);
        g2.dispose();
        super.paintComponent(g);
    }
}
