package com.example;

import javax.swing.SwingUtilities;

import com.example.Controlador.*;


public class Main {
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //new LoginControl();
            new LoginControl();
            AdminControl admin = new AdminControl("Administrador_UCV", "Admin");
            
            // La hacemos visible

        });
    }
}
