package com.example.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.example.Modelo.SecretariaModel;

public class PerfilPanel extends JPanel {

    private final String usuarioEmail;
    private final SecretariaModel secretariaModel = new SecretariaModel();

    private final JLabel lblRutaFoto;
    private final JLabel lblEstado;

    public PerfilPanel(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(25, 35, 25, 35));

        JLabel titulo = new JLabel("Perfil y Foto de Reconocimiento");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        add(titulo, BorderLayout.NORTH);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblUsuario = new JLabel("Usuario: " + (usuarioEmail == null ? "" : usuarioEmail));
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JLabel lblDescripcion = new JLabel(
            "Esta foto se usara para validar tu identidad al registrar turnos en el comedor."
        );
        lblDescripcion.setForeground(new Color(90, 90, 90));

        lblRutaFoto = new JLabel("Foto registrada: (sin foto)");
        lblRutaFoto.setFont(new Font("SansSerif", Font.PLAIN, 14));

        lblEstado = new JLabel(" ");
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblEstado.setForeground(new Color(34, 120, 64));

        JButton btnSeleccionar = new JButton("Cargar o actualizar foto");
        btnSeleccionar.setBackground(new Color(34, 120, 64));
        btnSeleccionar.setForeground(Color.WHITE);
        btnSeleccionar.setFocusPainted(false);
        btnSeleccionar.setMaximumSize(new Dimension(240, 36));
        btnSeleccionar.addActionListener(e -> seleccionarYGuardarFoto());

        card.add(lblUsuario);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(lblDescripcion);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(lblRutaFoto);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(btnSeleccionar);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(lblEstado);

        add(card, BorderLayout.CENTER);

        actualizarEstadoFoto();
    }

    private void seleccionarYGuardarFoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona tu foto (JPG/PNG)");

        File directorioFotos = secretariaModel.obtenerDirectorioFotos();
        if (directorioFotos != null && directorioFotos.exists()) {
            chooser.setCurrentDirectory(directorioFotos);
        }

        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File seleccionada = chooser.getSelectedFile();
        try {
            File guardada = secretariaModel.guardarFotoUsuario(usuarioEmail, seleccionada);
            lblEstado.setForeground(new Color(34, 120, 64));
            lblEstado.setText("Foto guardada correctamente para reconocimiento facial.");
            lblRutaFoto.setText("Foto registrada: " + guardada.getAbsolutePath());
        } catch (IllegalArgumentException | IOException ex) {
            lblEstado.setForeground(Color.RED);
            lblEstado.setText(ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Foto de Perfil", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEstadoFoto() {
        File foto = secretariaModel.obtenerArchivoFoto(usuarioEmail);
        if (foto != null) {
            lblRutaFoto.setText("Foto registrada: " + foto.getAbsolutePath());
            lblEstado.setForeground(new Color(34, 120, 64));
            lblEstado.setText("Foto disponible para validacion facial.");
        } else {
            lblRutaFoto.setText("Foto registrada: (sin foto)");
            lblEstado.setForeground(new Color(160, 85, 0));
            lblEstado.setText("Aun no tienes una foto configurada.");
        }
    }
}
