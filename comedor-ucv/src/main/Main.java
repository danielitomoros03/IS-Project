package PracticaJava.Inge;
import javax.swing.SwingUtilities;

import PracticaJava.Inge.Vista.BienvenidoVista;
//import PracticaJava.Inge.Vista.Login;
import PracticaJava.Inge.Controlador.LoginControl;



public class Main {
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginControl();
            new BienvenidoVista().setVisible(true);
        });
    }
}
