package com.example.Controlador;

import com.example.Modelo.RegUsuarioModelo;
import com.example.Vista.RegistroVista;
import javax.swing.JOptionPane;

//Controla cuando se presiona el boton registrar para guardar los datos en el txt
//Tambien en el formulario si el usuario a registrar es Estudiante o Empleado

public class RegistroControl {
    private RegistroVista vista;
    private RegUsuarioModelo modelo;

    public RegistroControl(RegistroVista vista, RegUsuarioModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;

        // Listener para el cambio de Rol
        this.vista.comboRol.addActionListener(e -> {
            actualizarVisibilidad();
        });

        // Listener del boton registrar
        this.vista.btnRegistrar.addActionListener(e -> guardarDatos());
    }

    private void actualizarVisibilidad() {
        String seleccion = (String) vista.comboRol.getSelectedItem();
        boolean esEstudiante = "Estudiante".equals(seleccion);

        // Mostramos u ocultamos segun la seleccion
        vista.lblFacultad.setVisible(esEstudiante);
        vista.txtFacultad.setVisible(esEstudiante);
        vista.lblCarrera.setVisible(esEstudiante);
        vista.txtCarrera.setVisible(esEstudiante);

        // Forzamos a la ventana a redibujarse para que no queden huecos
        vista.revalidate();
        vista.repaint();
    }

    private void guardarDatos() {
        
        // Extraer datos de la vista
        String nombre = vista.txtNombre.getText();
        String password = new String(vista.txtPassword.getPassword());
        String rol = (String) vista.comboRol.getSelectedItem();
        String telf = vista.txtTelefono.getText();
        String email = vista.txtEmail.getText();

        // Si no es estudiante, enviamos "N/A" para mantener la estructura del TXT
        String facultad = vista.txtFacultad.isVisible() ? vista.txtFacultad.getText() : "N/A";
        String carrera = vista.txtCarrera.isVisible() ? vista.txtCarrera.getText() : "N/A";
 
        //Validacion del rol
        if (rol.equals("Seleccione un rol")) {
            JOptionPane.showMessageDialog(vista, "Por favor seleccione un rol (Estudiante/Empleado)");
            return;
        }

        // Llamar al modelo para guardar
        boolean exito = modelo.registrarUsuario(nombre, email, password, rol, telf, facultad, carrera);

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
        vista.comboRol.setSelectedIndex(0);
        vista.txtTelefono.setText("");
        vista.txtEmail.setText("");
        vista.txtFacultad.setText("");
        vista.txtCarrera.setText("");

        //Al limpiar se ocultan de nuevo los campos
        actualizarVisibilidad();
    }
}