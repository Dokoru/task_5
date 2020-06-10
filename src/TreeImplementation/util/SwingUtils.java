package TreeImplementation.util;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;


/**
 * Набор функций для работы с оконными приложениями с ипользованием Swing
 * @author Дмитрий Соломатин (кафедра ПиИТ ФКН ВГУ)
 */

public class SwingUtils {

    public static void showErrorMessageBox(String message, String title, Throwable ex) {
        StringBuilder sb = new StringBuilder(ex.toString());
        if (message != null) {
            sb.append(message);
        }
        if (ex != null) {
            sb.append("\n");
            for (StackTraceElement ste : ex.getStackTrace()) {
                sb.append("\n\tat ");
                sb.append(ste);
            }
        }
        JOptionPane.showMessageDialog(null, sb.toString(), title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorMessageBox(String message, Throwable ex) {
        showErrorMessageBox(message, "Ошибка", ex);
    }

    public static void showErrorMessageBox(Throwable ex) {
        showErrorMessageBox(null, ex);
    }

    public static <T extends Component> T setFixedSize(T comp, int width, int height) {
        Dimension d = new Dimension(width, height);
        comp.setMaximumSize(d);
        comp.setMinimumSize(d);
        comp.setPreferredSize(d);
        comp.setSize(d);
        return comp;
    }

    public static void setDefaultFont(String fontName, int size) {
        UIManager.getDefaults().entrySet().forEach((entry) -> {
            Object value = javax.swing.UIManager.get(entry.getKey());
            if (value != null && value instanceof FontUIResource) {
                FontUIResource fr = (FontUIResource) value;
                fr = new FontUIResource(
                    (fontName != null) ? fontName : fr.getFontName(),
                    fr.getStyle(),
                    (size > 0) ? size : fr.getSize()
                );
                UIManager.put(entry.getKey(), fr);
            }
        });
    }
}
