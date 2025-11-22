package onlinehealthcaresystem;

import com.formdev.flatlaf.FlatIntelliJLaf;
import ui.LoginFrame;

public class OnlineHealthcareSystem {
    public static void main(String[] args) {

        // Apply modern UI theme
        FlatIntelliJLaf.setup();

        new LoginFrame().setVisible(true);
    }
}
