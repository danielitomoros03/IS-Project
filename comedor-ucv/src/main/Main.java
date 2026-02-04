package PracticaJava.Inge;
import javax.swing.SwingUtilities;

import PracticaJava.Inge.Vista.Login;

public class Main {
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
