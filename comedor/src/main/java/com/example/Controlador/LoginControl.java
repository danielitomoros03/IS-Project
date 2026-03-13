package com.example.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.example.Modelo.LoginModel;
import com.example.Modelo.RegUsuarioModelo;
import com.example.Vista.Login;
import com.example.Vista.RegistroVista;


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
        this.vista.getBtnRecuperar().addActionListener(this);
        
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnLogin()) {
            handleLogin();
        } else if (e.getSource() == vista.getBtnRegistrar()) {
            irARegistro();
        } else if (e.getSource() == vista.getBtnRecuperar()) {
            recuperarContrasena();
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

            // Redirigir segun rol
            if (role != null && role.equalsIgnoreCase("Administrador")) {
                new AdminControl(email, role);
            } else {
                new BienvenidoControl(email, role);
            }

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

    private void recuperarContrasena() {
        String emailBase = vista.getEmail().getText() == null ? "" : vista.getEmail().getText().trim();
        String email = JOptionPane.showInputDialog(vista, "Ingresa tu correo @ucv.ve", emailBase);
        if (email == null) {
            return;
        }

        email = email.trim();
        if (email.isEmpty() || !email.toLowerCase().endsWith("@ucv.ve")) {
            JOptionPane.showMessageDialog(vista, "Debes ingresar un correo valido del dominio @ucv.ve");
            return;
        }

        try {
            if (!modelo.existeUsuario(email)) {
                JOptionPane.showMessageDialog(vista, "No existe un usuario registrado con ese correo.");
                return;
            }

            String codigo = modelo.generarCodigoRecuperacion();
            JOptionPane.showMessageDialog(
                vista,
                "Simulacion activa: codigo enviado por correo.\nCodigo: " + codigo
            );

            String codigoIngresado = JOptionPane.showInputDialog(vista, "Ingresa el codigo de verificacion");
            if (codigoIngresado == null) {
                return;
            }

            if (!codigo.equals(codigoIngresado.trim())) {
                JOptionPane.showMessageDialog(vista, "Codigo de verificacion incorrecto.");
                return;
            }

            String nueva = JOptionPane.showInputDialog(vista, "Ingresa tu nueva contraseña (6 a 10 caracteres)");
            if (nueva == null) {
                return;
            }
            nueva = nueva.trim();
            if (nueva.length() < 6 || nueva.length() > 10) {
                JOptionPane.showMessageDialog(vista, "La contraseña debe tener entre 6 y 10 caracteres.");
                return;
            }

            String confirmar = JOptionPane.showInputDialog(vista, "Confirma tu nueva contraseña");
            if (confirmar == null) {
                return;
            }
            if (!nueva.equals(confirmar.trim())) {
                JOptionPane.showMessageDialog(vista, "La confirmacion no coincide.");
                return;
            }

            boolean actualizado = modelo.actualizarPassword(email, nueva);
            if (!actualizado) {
                JOptionPane.showMessageDialog(vista, "No fue posible actualizar la contraseña.");
                return;
            }

            vista.setEmail(email);
            vista.setTxtPassword("");
            JOptionPane.showMessageDialog(vista, "Contraseña actualizada con exito. Ya puedes iniciar sesion.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(vista, "Ocurrio un error al recuperar la contraseña.");
        }
    }
}
