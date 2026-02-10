package com.example;

import javax.swing.SwingUtilities;
import com.example.Controlador.*;
import com.example.Modelo.RegUsuarioModelo;
import com.example.Vista.*;


public class Main {
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        //Esto es para abrir la ventana de Admin, ya que aun no se ha realizado la 
        //la validacion para cuando sea admin, pueda entrar a esta interfaz
        
        //System.out.println("Intentando iniciar AdminControl..."); // PASO 1
        RegistroVista reg = new RegistroVista();
        RegUsuarioModelo regC = new RegUsuarioModelo();
/*        try {
            RegistroControl admin = new RegistroControl(reg, regC);
            System.out.println("AdminControl iniciado con éxito."); // PASO 2
        } catch (Exception e) {
            e.printStackTrace(); // Esto te dirá el error exacto en la consola roja
        }  */

            //RegistroControl bista = new RegistroControl(reg, regC);
            

        //Interza de inicio de sesion
        LoginControl login = new LoginControl();
        //Si se quiere ver la Interfaz de Registro de Usuario
        //RegistroVista reg = new RegistroVista();
        //reg.setVisible(true);

    });
    }
}
