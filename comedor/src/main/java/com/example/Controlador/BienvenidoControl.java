package com.example.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.example.Modelo.FaceRecognitionModel;
import com.example.Modelo.ValidacionFacialService;
import com.example.Vista.BienvenidoVista;

public class BienvenidoControl implements ActionListener {
    private static final int UMBRAL_DHASH = 10;

    private BienvenidoVista vista;
    private String usuarioEmail;
    private final ValidacionFacialService validacionFacialService;
    private boolean accesoComedorValidado;

    public BienvenidoControl(String usuario, String rol) {
        this.vista = new BienvenidoVista(usuario, rol);
        this.usuarioEmail = usuario;
        this.validacionFacialService = new ValidacionFacialService();
        this.accesoComedorValidado = false;

        this.vista.getBtnDashboard().addActionListener(this);
        this.vista.getBtnMenuDia().addActionListener(this);
        this.vista.getBtnRegTurno().addActionListener(this);
        this.vista.getBtnHistorial().addActionListener(this);
        this.vista.getBtnMonederoSidebar().addActionListener(this);
        this.vista.getBtnPerfil().addActionListener(this);
        this.vista.getBtnLogout().addActionListener(this);

        if (this.vista.getBtnMonedero() != null) {
            this.vista.getBtnMonedero().addActionListener(this);
        }


        // Estado inicial
        this.vista.marcarBotonActivo(this.vista.getBtnDashboard());
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (requiereValidacionFacialParaFuncion(source) && !accesoComedorValidado) {
            if (!validarAccesoFuncionesComedor()) {
                return;
            }
            accesoComedorValidado = true;
        }

        if (source == vista.getBtnDashboard()) {
            vista.changeView("DASH_VISTA");
            vista.marcarBotonActivo(vista.getBtnDashboard());
        } 
        else if (source == vista.getBtnMenuDia()) {
            vista.changeView("MENU_VISTA");
            vista.marcarBotonActivo(vista.getBtnMenuDia());
        }         
        else if (source == vista.getBtnRegTurno()) {
            vista.changeView("TURNO_VISTA");
            vista.marcarBotonActivo(vista.getBtnRegTurno());
        }
        else if (source == vista.getBtnHistorial()) {
            vista.changeView("HIST_VISTA");
            vista.marcarBotonActivo(vista.getBtnHistorial());
        }
        else if (source == vista.getBtnPerfil()) {
            vista.changeView("PERFIL_VISTA");
            vista.marcarBotonActivo(vista.getBtnPerfil());
        }
        else if (source == vista.getBtnMonederoSidebar() || source == vista.getBtnMonedero()) {
            vista.marcarBotonActivo(vista.getBtnMonederoSidebar());
            new MonederoControl(vista, usuarioEmail);
        }
        // Logout
        else if (source == vista.getBtnLogout()) {
            int confirm = JOptionPane.showConfirmDialog(
                vista,
                "¿Estás seguro de cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                vista.dispose();
                // new LoginControl(); 
            }
        }
    }

    private boolean requiereValidacionFacialParaFuncion(Object source) {
        if (source == vista.getBtnLogout()) {
            return false;
        }

        if (source == vista.getBtnPerfil()) {
            return false;
        }

        return source == vista.getBtnDashboard()
            || source == vista.getBtnMenuDia()
            || source == vista.getBtnRegTurno()
            || source == vista.getBtnHistorial()
            || source == vista.getBtnMonederoSidebar()
            || source == vista.getBtnMonedero();
    }

    private boolean validarAccesoFuncionesComedor() {
        File fotoBase = validacionFacialService.obtenerFotoBase(usuarioEmail);
        if (fotoBase == null) {
            JOptionPane.showMessageDialog(
                vista,
                "No hay identificacion facial registrada en Secretaria para este usuario. "
                    + "Debes acudir a la oficina de Secretaria para registro o actualizacion.",
                "Validacion facial",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Validacion facial para usar funciones del comedor (JPG/PNG)");

        File directorioFotos = validacionFacialService.obtenerDirectorioFotos();
        if (directorioFotos != null && directorioFotos.exists()) {
            chooser.setCurrentDirectory(directorioFotos);
        } else if (fotoBase.getParentFile() != null && fotoBase.getParentFile().exists()) {
            chooser.setCurrentDirectory(fotoBase.getParentFile());
        }
        chooser.setSelectedFile(fotoBase);

        int result = chooser.showOpenDialog(vista);
        if (result != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(vista, "Validacion facial cancelada.");
            return false;
        }

        File fotoIngresada = chooser.getSelectedFile();
        try {
            FaceRecognitionModel.ResultadoReconocimiento reconocimiento =
                validacionFacialService.validarContraSecretaria(usuarioEmail, fotoIngresada, UMBRAL_DHASH);

            if (!reconocimiento.esValido()) {
                JOptionPane.showMessageDialog(
                    vista,
                    "No coincide con la foto de Secretaria. Puntaje: "
                        + formatearPorcentaje(reconocimiento.getPuntajeFinal())
                        + " | Distancia: "
                        + reconocimiento.getDistanciaHash(),
                    "Validacion facial",
                    JOptionPane.ERROR_MESSAGE
                );
                return false;
            }

            JOptionPane.showMessageDialog(
                vista,
                "Identidad validada. Ya puedes usar las funciones del comedor.",
                "Validacion facial",
                JOptionPane.INFORMATION_MESSAGE
            );
            return true;
        } catch (IllegalArgumentException | IllegalStateException | IOException ex) {
            JOptionPane.showMessageDialog(
                vista,
                ex.getMessage(),
                "Validacion facial",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    private String formatearPorcentaje(double valor) {
        return String.format(java.util.Locale.ROOT, "%.2f%%", valor * 100.0);
    }
}