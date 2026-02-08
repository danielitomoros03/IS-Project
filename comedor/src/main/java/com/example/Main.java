package com.example;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.example.Controlador.*;
import com.example.Vista.*;


public class Main {
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            new LoginControl();
            //Descomentar para ver la interfaz del Admin
            //AdminControl admin = new AdminControl("Administrador_UCV", "Admin");


        });
    }
}
