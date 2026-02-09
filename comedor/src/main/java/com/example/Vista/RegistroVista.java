package com.example.Vista;

import javax.swing.*;
import java.awt.*;

public class RegistroVista extends JFrame {
    // Definición de componentes
    public JTextField txtNombre, txtTelefono, txtEmail, txtFacultad, txtCarrera, txtRol;
    public JPasswordField txtPassword; 
    public JButton btnRegistrar;

    public RegistroVista() {
        setTitle("Sistema de Comedor - Registro UCV");
        setSize(450, 750); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // PANEL SUPERIOR (Logo y Títulos) 
        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel lblLogo = new JLabel(" UCV ", SwingConstants.CENTER);
        lblLogo.setOpaque(true);
        lblLogo.setBackground(new Color(34, 120, 64)); // Verde UCV
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Registro de Usuario");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelHeader.add(lblLogo);
        panelHeader.add(Box.createRigidArea(new Dimension(0, 15)));
        panelHeader.add(lblTitulo);

        // PANEL CENTRAL (Formulario)
        JPanel panelForm = new JPanel(new GridLayout(14, 1, 2, 2)); 
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // Inicialización de campos
        txtNombre = crearCampo();
        txtPassword = new JPasswordField();
        estilizarComponente(txtPassword); // Aplicamos el estilo manualmente
        
        txtRol = crearCampo();
        txtTelefono = crearCampo();
        txtEmail = crearCampo();
        txtFacultad = crearCampo();
        txtCarrera = crearCampo();

        // Agregamos en el orden solicitado
        panelForm.add(new JLabel("Nombre Completo:"));
        panelForm.add(txtNombre);
        
        panelForm.add(new JLabel("Contraseña:"));
        panelForm.add(txtPassword);
        
        panelForm.add(new JLabel("Rol:"));
        panelForm.add(txtRol);

        panelForm.add(new JLabel("Teléfono:"));
        panelForm.add(txtTelefono);

        panelForm.add(new JLabel("Correo Electrónico:"));
        panelForm.add(txtEmail);

        panelForm.add(new JLabel("Facultad:"));
        panelForm.add(txtFacultad);

        panelForm.add(new JLabel("Carrera:"));
        panelForm.add(txtCarrera);

        // PANEL INFERIOR (Botón)
        JPanel panelFooter = new JPanel();
        panelFooter.setBackground(Color.WHITE);
        panelFooter.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));

        btnRegistrar = new JButton("Registrar Usuario");
        btnRegistrar.setBackground(new Color(34, 120, 64));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnRegistrar.setPreferredSize(new Dimension(300, 45));

        panelFooter.add(btnRegistrar);

        add(panelHeader, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);
        add(panelFooter, BorderLayout.SOUTH);
    }

    // Método para crear JTextFields ya estilizados
    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        estilizarComponente(campo);
        return campo;
    }

    // Método corregido para estilizar cualquier JComponent (JTextField, JPasswordField, etc.)
    private void estilizarComponente(JComponent c) {
        c.setPreferredSize(new Dimension(200, 35));
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
}