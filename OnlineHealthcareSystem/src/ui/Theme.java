package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Theme utility: centralizes colors, fonts and helper methods to style components.
 * Place in package ui and call Theme.apply() at app start (before creating frames).
 */
public final class Theme {

    // Colors (Healthcare Blue palette)
    public static final Color PRIMARY = Color.decode("#3B82F6");      // main blue
    public static final Color PRIMARY_DARK = Color.decode("#1E40AF"); // deep blue
    public static final Color SUCCESS = Color.decode("#10B981");      // green
    public static final Color DANGER = Color.decode("#EF4444");       // red
    public static final Color BG = Color.decode("#F8FAFC");           // soft background
    public static final Color CARD = Color.decode("#FFFFFF");         // white card
    public static final Color SHADOW = new Color(0, 0, 0, 30);

    // Fonts
    public static final Font H1 = new Font("SansSerif", Font.BOLD, 22);
    public static final Font H2 = new Font("SansSerif", Font.BOLD, 16);
    public static final Font UI_FONT = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 15);

    private Theme() { }

    // Call once on app startup
    public static void apply() {
        UIManager.put("Label.font", UI_FONT);
        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("TextField.font", UI_FONT);
        UIManager.put("TextArea.font", UI_FONT);
        UIManager.put("Table.font", UI_FONT);
        UIManager.put("TableHeader.font", H2);
        UIManager.put("ComboBox.font", UI_FONT);
        UIManager.put("ScrollBar.width", 12);

        // Optionally tune some look-and-feel properties
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(8, 8, 8, 8));
    }

    // Style a table to look modern
    public static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setIntercellSpacing(new Dimension(6, 6));
        table.setShowGrid(false);
        JTableHeader hdr = table.getTableHeader();
        hdr.setBackground(PRIMARY);
        hdr.setForeground(Color.WHITE);
        hdr.setFont(H2);
    }

    // Small helper to make a form card look neat
    public static void styleCard(JPanel p) {
        p.setBackground(CARD);
        p.setBorder(new EmptyBorder(12, 12, 12, 12));
    }
}
