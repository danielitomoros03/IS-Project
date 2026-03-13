package com.example.Controlador;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.example.Modelo.FaceRecognitionModel;
import com.example.Modelo.RegUsuarioModelo;
import com.example.Modelo.ValidacionFacialService;
import com.example.Vista.RegistroVista;

//Controla cuando se presiona el boton registrar para guardar los datos en el txt
//Tambien en el formulario si el usuario a registrar es Estudiante o Empleado

public class RegistroControl {
    private static final int UMBRAL_DHASH = 10;

    private RegistroVista vista;
    private RegUsuarioModelo modelo;
    private final ValidacionFacialService validacionFacialService;
    private boolean correoValidado = false;
    private String correoValidadoValor;
    private String nombreDesdeSecretaria;
    private String rolDesdeSecretaria;
    private boolean pasoContrasena = false;

    public RegistroControl(RegistroVista vista, RegUsuarioModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.validacionFacialService = new ValidacionFacialService();

        // Listener del boton registrar
        this.vista.btnRegistrar.addActionListener(e -> guardarDatos());
        this.vista.btnVolverLogin.addActionListener(e -> volverALogin());
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

        try {
            validarRostroRegistro(email);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage());
            marcarCorreoInvalido("Validacion facial fallida");
            return;
        }

        correoValidado = true;
        correoValidadoValor = email;
        nombreDesdeSecretaria = datos[0];
        rolDesdeSecretaria = datos[1];

        vista.lblEstadoCorreo.setForeground(new java.awt.Color(34, 120, 64));
        vista.lblEstadoCorreo.setText("Correo y rostro validados");
        vista.mostrarPasoContrasena();
        vista.btnRegistrar.setText("Registrar");
        pasoContrasena = true;
    }

    private void validarRostroRegistro(String email) throws Exception {
        File fotoBase = validacionFacialService.obtenerFotoBase(email);
        if (fotoBase == null) {
            throw new Exception("No hay foto base en Secretaria para este correo.");
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Validacion facial para registro (JPG/PNG)");

        File directorioFotos = validacionFacialService.obtenerDirectorioFotos();
        if (directorioFotos != null && directorioFotos.exists()) {
            chooser.setCurrentDirectory(directorioFotos);
        } else if (fotoBase.getParentFile() != null && fotoBase.getParentFile().exists()) {
            chooser.setCurrentDirectory(fotoBase.getParentFile());
        }
        chooser.setSelectedFile(fotoBase);

        int result = chooser.showOpenDialog(vista);
        if (result != JFileChooser.APPROVE_OPTION) {
            throw new Exception("Validacion facial cancelada.");
        }

        File fotoIngresada = chooser.getSelectedFile();
        try {
            FaceRecognitionModel.ResultadoReconocimiento reconocimiento =
                validacionFacialService.validarContraSecretaria(email, fotoIngresada, UMBRAL_DHASH);

            if (!reconocimiento.esValido()) {
                throw new Exception(
                    "No coincide con la foto de Secretaria. Puntaje: "
                        + formatearPorcentaje(reconocimiento.getPuntajeFinal())
                        + " | Distancia: "
                        + reconocimiento.getDistanciaHash()
                );
            }

            JOptionPane.showMessageDialog(
                vista,
                "Identidad validada por reconocimiento facial.",
                "Registro",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IllegalArgumentException | IllegalStateException | IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private String formatearPorcentaje(double valor) {
        return String.format(java.util.Locale.ROOT, "%.2f%%", valor * 100.0);
    }

    private void guardarDatos() {
        if (!pasoContrasena) {
            validarCorreo();
            return;
        }

        String password = new String(vista.txtPassword.getPassword());
        if (password.length() < 6 || password.length() > 10) {
            JOptionPane.showMessageDialog(vista, "La contraseña debe tener entre 6 y 10 caracteres");
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

    private void volverALogin() {
        limpiarCampos();
        vista.dispose();
        new LoginControl();
    }

}