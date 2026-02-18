package com.example.Controlador;

import javax.swing.JOptionPane;

import com.example.Modelo.RegUsuarioModelo;
import com.example.Vista.RegistroVista;

//Controla cuando se presiona el boton registrar para guardar los datos en el txt
//Tambien en el formulario si el usuario a registrar es Estudiante o Empleado

public class RegistroControl {
    private RegistroVista vista;
    private RegUsuarioModelo modelo;

    public RegistroControl(RegistroVista vista, RegUsuarioModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;

        // Listener del boton registrar
        this.vista.btnRegistrar.addActionListener(e -> guardarDatos());

        this.vista.chkMostrarPassword.addActionListener(e -> {
            if (this.vista.chkMostrarPassword.isSelected()) {
                this.vista.txtPassword.setEchoChar((char) 0);
            } else {
                this.vista.txtPassword.setEchoChar('•');
            }
        });
    }

    private void guardarDatos() {
        
        // Extraer datos de la vista
        String nombre = vista.txtNombre.getText();
        String password = new String(vista.txtPassword.getPassword());
        String telf = vista.txtTelefono.getText();
        String email = vista.txtEmail.getText();

        if (!esEmailValido(email)) {
            JOptionPane.showMessageDialog(vista, "Correo invalido. Debe ser @ucv.ve o @gmail.com");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(vista, "La contraseña debe tener al menos 6 caracteres");
            return;
        }

        String rol = modelo.obtenerRolDesdeArchivo(email);
        if (rol == null || rol.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Correo no registrado en Usuarios_UCV.txt");
            return;
        }

        // Llamar al modelo para guardar
        boolean exito = modelo.registrarUsuario(nombre, email, password, rol, telf);

        if (exito) {
            JOptionPane.showMessageDialog(vista, "Usuario registrado con éxito en Usuarios.txt");
            limpiarCampos();
            vista.dispose();
            // Iniciamos un nuevo controlador de Login (esto abrirá la ventana de Login)
            new LoginControl();

        } else {
            JOptionPane.showMessageDialog(vista, "Error: Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        vista.txtNombre.setText("");
        vista.txtPassword.setText("");
        vista.txtTelefono.setText("");
        vista.txtEmail.setText("");
    }

    private boolean esEmailValido(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        return email.matches("^[^@\\s]+@(ucv\\.ve|gmail\\.com)$");
    }
}