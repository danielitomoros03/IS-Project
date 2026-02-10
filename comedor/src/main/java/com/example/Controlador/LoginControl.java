package com.example.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.example.Vista.Login;
import com.example.Vista.RegistroVista;
import com.example.Modelo.*;


public class LoginControl implements ActionListener{

    private Login vista;
    private LoginModel modelo;

    public LoginControl(){
        
        this.vista = new Login(); 
        //this.vista.setSize(1200, 750);                  //Inicializamos la vista y el modelo(instanciado)
        this.modelo = new LoginModel();        

        // Configurar los listeners para ambos botones
        this.vista.getBtnLogin().addActionListener(this);
        this.vista.getBtnRegistrar().addActionListener(this);
        
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnLogin()) {
            handleLogin();
        } else if (e.getSource() == vista.getBtnRegistrar()) {
            irARegistro();
        }
    }

    private void handleLogin(){
        
        // Captura de datos con getters
        String email = vista.getEmail().getText();   //Obtener datos de la vista
        String pass = new String(vista.getTxtPassword().getPassword()); 

        try{

            vista.getBtnLogin().setEnabled(false);            //Desactivar botón mientras carga
            vista.getBtnLogin().setText("Validando...");

            String role = modelo.autenticar(email, pass);  //Llamar al modelo para autenticar

            //Esta instancia hace que al presionnar "Iniciar Sesion", cambie la vista a Bienvenida
            new BienvenidoControl(email, role);
            vista.dispose();

        } catch (Exception ex) {
            vista.showErrorMessage(ex.getMessage());
            if (modelo.isBloqueado()) {
                bloquearInterfaz();
            }
        } finally {
            if (!modelo.isBloqueado()) {
                vista.getBtnLogin().setEnabled(true);
                vista.getBtnLogin().setText("Iniciar Sesión");
                vista.setTxtPassword("");
            }
        }
    }

    // Este es el método que abre la ventana de Registro
    private void irARegistro() {
        // Creamos la vista de registro
        RegistroVista vistaRegistro = new RegistroVista();
        
        // Creamos el modelo de registro
        RegUsuarioModelo modeloRegistro = new RegUsuarioModelo();
        
        // Creamos su controlador 
        new RegistroControl(vistaRegistro, modeloRegistro);
        
        vistaRegistro.setVisible(true);
        this.vista.dispose();
    }

    // Método para gestionar el cambio visual tras el bloqueo
    private void bloquearInterfaz() {
        vista.getBtnLogin().setEnabled(false);
        vista.getBtnLogin().setText("BLOQUEADO");
        vista.getEmail().setEnabled(false);
        vista.getTxtPassword().setEnabled(false);
        vista.getLblError().setText("Usuario bloqueado por seguridad.");
    }
}
