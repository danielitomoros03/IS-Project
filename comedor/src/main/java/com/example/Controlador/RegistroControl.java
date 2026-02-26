package com.example.Controlador;

import javax.swing.JOptionPane;

import com.example.Modelo.RegUsuarioModelo;
import com.example.Vista.RegistroVista;

//Controla cuando se presiona el boton registrar para guardar los datos en el txt
//Tambien en el formulario si el usuario a registrar es Estudiante o Empleado

public class RegistroControl {
    private RegistroVista vista;
    private RegUsuarioModelo modelo;
    private boolean correoValidado = false;
    private String correoValidadoValor;
    private String nombreDesdeSecretaria;
    private String rolDesdeSecretaria;
    private boolean pasoContrasena = false;

    public RegistroControl(RegistroVista vista, RegUsuarioModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;

        // Listener del boton registrar
        this.vista.btnRegistrar.addActionListener(e -> guardarDatos());
        this.vista.btnRegistrar.setText("Continuar");
        this.vista.txtPassword.setEnabled(true);
        this.vista.chkMostrarPassword.setEnabled(true);

        this.vista.chkMostrarPassword.addActionListener(e -> {
            if (this.vista.chkMostrarPassword.isSelected()) {
                this.vista.txtPassword.setEchoChar((char) 0);
            } else {
                this.vista.txtPassword.setEchoChar('•');
            }
        });
    }

    private void validarCorreo() {
        String email = vista.txtEmail.getText();
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Debe ingresar un correo");
            marcarCorreoInvalido("Correo requerido");
            return;
        }
        email = email.trim();

        if (modelo.existeEnUsuarios(email)) {
            JOptionPane.showMessageDialog(vista, "Este correo ya esta registrado");
            marcarCorreoInvalido("Correo ya registrado");
            return;
        }

        String[] datos = modelo.obtenerNombreYRolDesdeArchivo(email);
        if (datos == null || datos.length < 2) {
            JOptionPane.showMessageDialog(vista, "Correo no registrado en el listado de Secretaria (Usuarios_UCV.txt)");
            marcarCorreoInvalido("Correo no encontrado");
            return;
        }

        correoValidado = true;
        correoValidadoValor = email;
        nombreDesdeSecretaria = datos[0];
        rolDesdeSecretaria = datos[1];

        vista.lblEstadoCorreo.setForeground(new java.awt.Color(34, 120, 64));
        vista.lblEstadoCorreo.setText("Correo valido");
        vista.mostrarPasoContrasena();
        vista.btnRegistrar.setText("Registrar");
        pasoContrasena = true;
    }

    private void guardarDatos() {
        if (!pasoContrasena) {
            validarCorreo();
            return;
        }

        String password = new String(vista.txtPassword.getPassword());
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(vista, "La contraseña debe tener al menos 6 caracteres");
            return;
        }

        String nombre = nombreDesdeSecretaria;
        String rol = rolDesdeSecretaria;
        String email = correoValidadoValor;

        boolean exito = modelo.registrarUsuario(nombre, email, password, rol, "N/A");

        if (exito) {
            JOptionPane.showMessageDialog(vista, "Usuario registrado con éxito en Usuarios.txt");
            limpiarCampos();
            vista.dispose();
            new LoginControl();

        } else {
            JOptionPane.showMessageDialog(vista, "Error: Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        vista.txtPassword.setText("");
        vista.txtEmail.setText("");
        correoValidado = false;
        correoValidadoValor = null;
        nombreDesdeSecretaria = null;
        rolDesdeSecretaria = null;
        vista.lblEstadoCorreo.setForeground(java.awt.Color.GRAY);
        vista.lblEstadoCorreo.setText(" ");
        vista.mostrarPasoCorreo();
        vista.btnRegistrar.setText("Continuar");
        pasoContrasena = false;
    }

    private void marcarCorreoInvalido(String mensajeEstado) {
        correoValidado = false;
        correoValidadoValor = null;
        nombreDesdeSecretaria = null;
        rolDesdeSecretaria = null;
        vista.lblEstadoCorreo.setForeground(java.awt.Color.RED);
        vista.lblEstadoCorreo.setText(mensajeEstado);
        vista.mostrarPasoCorreo();
        vista.btnRegistrar.setText("Continuar");
        pasoContrasena = false;
    }

}