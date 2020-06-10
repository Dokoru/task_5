package TreeImplementation;

import java.awt.EventQueue;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.util.Locale;
import javax.swing.JFrame;
import TreeImplementation.util.SwingUtils;


public class Main {

    public static void main(String[] args) {

        Locale.setDefault(Locale.ROOT);
        SwingUtils.setDefaultFont("Arial", 20);

        EventQueue.invokeLater(() -> {
            try {
                JFrame frameMain = new TreeFrame();
                frameMain.setVisible(true);
                frameMain.setExtendedState(MAXIMIZED_BOTH);
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });
    }
}
