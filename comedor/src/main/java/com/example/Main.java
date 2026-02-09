package com.example;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.example.Controlador.*;
import com.example.Vista.*;


public class Main {
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        //Esto es para abrir la ventana de Admin, ya que aun no se ha realizado la 
        //la validacion para cuando sea admin, pueda entrar a esta interfaz
        System.out.println("Intentando iniciar AdminControl..."); // PASO 1
        try {
            AdminControl admin = new AdminControl("Administrador_UCV", "Admin");
            System.out.println("AdminControl iniciado con éxito."); // PASO 2
        } catch (Exception e) {
            e.printStackTrace(); // Esto te dirá el error exacto en la consola roja
        }

        //Interza de inicio de sesion
        LoginControl login = new LoginControl();
        //Si se quiere ver la Interfaz de Registro de Usuario
        //RegistroVista reg = new RegistroVista();
        //reg.setVisible(true);

    });
    }
}
