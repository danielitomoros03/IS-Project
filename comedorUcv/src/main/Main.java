package main;

import javax.swing.SwingUtilities;

import main.Controlador.LoginControl; 
import main.Vista.BienvenidoVista;



public class Main {
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginControl();
        });
    }
}
