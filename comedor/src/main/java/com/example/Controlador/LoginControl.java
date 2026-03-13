package com.example.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.example.Modelo.FaceRecognitionModel;
import com.example.Modelo.LoginModel;
import com.example.Modelo.RegUsuarioModelo;
import com.example.Modelo.ValidacionFacialService;
import com.example.Vista.Login;
import com.example.Vista.RegistroVista;


public class LoginControl implements ActionListener{
    private static final int UMBRAL_DHASH = 10;

    private Login vista;
    private LoginModel modelo;
    private ValidacionFacialService validacionFacialService;
 
    public LoginControl(){
        
        this.vista = new Login(); 
        //this.vista.setSize(1200, 750);                  //Inicializamos la vista y el modelo(instanciado)
        this.modelo = new LoginModel();        
        this.validacionFacialService = new ValidacionFacialService();
 
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

            if (requiereValidacionFacial(email)) {
                validarAccesoFacial(email);
            }

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

    private void validarAccesoFacial(String email) throws Exception {
        File fotoBase = validacionFacialService.obtenerFotoBase(email);
        if (fotoBase == null) {
            throw new Exception("No hay foto base en Secretaria para este usuario.");
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Validacion facial de acceso (JPG/PNG)");

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
                    "Validacion facial fallida. Puntaje: "
                        + formatearPorcentaje(reconocimiento.getPuntajeFinal())
                        + " | Distancia: "
                        + reconocimiento.getDistanciaHash()
                );
            }

            JOptionPane.showMessageDialog(
                vista,
                "Identidad validada por reconocimiento facial. Puntaje: "
                    + formatearPorcentaje(reconocimiento.getPuntajeFinal()),
                "Acceso",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IllegalArgumentException | IllegalStateException | IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private boolean requiereValidacionFacial(String email) {
        if (email == null) {
            return true;
        }

        return !"admin@ucv.ve".equalsIgnoreCase(email.trim());
    }

    private String formatearPorcentaje(double valor) {
        return String.format(java.util.Locale.ROOT, "%.2f%%", valor * 100.0);
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
