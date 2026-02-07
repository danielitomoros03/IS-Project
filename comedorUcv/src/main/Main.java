package comedorUcv.src.main;
import comedorUcv.src.main.Controlador.LoginControl;

import javax.swing.SwingUtilities;

import comedorUcv.src.main.Controlador.LoginControl; 




public class Main {
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginControl();
        });
    }
}
