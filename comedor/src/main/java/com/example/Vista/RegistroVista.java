package com.example.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RegistroVista extends JFrame {
    // Definición de componentes
    public JTextField txtEmail;
    public JPasswordField txtPassword; 
    public JButton btnRegistrar;
    public JCheckBox chkMostrarPassword;
    public JLabel lblEstadoCorreo;

    private CardLayout cardLayout;
    private JPanel panelForm;

    public RegistroVista() {
        setTitle("Sistema de Comedor - Registro UCV");
        setSize(450, 420); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        cardLayout = new CardLayout();
        panelForm = new JPanel(cardLayout);
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // Inicialización de campos
        txtPassword = new JPasswordField();
        estilizarComponente(txtPassword); // Aplicamos el estilo manualmente
        txtEmail = crearCampo();

        JPanel panelCorreo = crearPanelCorreo();
        JPanel panelContrasena = crearPanelContrasena();
        panelForm.add(panelCorreo, "PASO_CORREO");
        panelForm.add(panelContrasena, "PASO_PASSWORD");
        cardLayout.show(panelForm, "PASO_CORREO");

        // Panel inferior para el boton
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

    private void addLabeledField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(6, 0, 2, 0);
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 0, 8, 0);
        panel.add(field, gbc);
    }

    private void addCheckboxRow(JPanel panel, GridBagConstraints gbc, int row, JCheckBox checkbox) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 8, 0);
        panel.add(checkbox, gbc);
    }

    private void addFullRow(JPanel panel, GridBagConstraints gbc, int row, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 8, 0);
        panel.add(component, gbc);
    }

    private JPanel crearPanelCorreo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        addLabeledField(panel, gbc, 0, "Correo Electrónico:", txtEmail);

        lblEstadoCorreo = new JLabel(" ");
        lblEstadoCorreo.setForeground(Color.GRAY);
        addFullRow(panel, gbc, 1, lblEstadoCorreo);

        return panel;
    }

    private JPanel crearPanelContrasena() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        addLabeledField(panel, gbc, 0, "Contraseña:", txtPassword);

        chkMostrarPassword = new JCheckBox("Mostrar contraseña");
        chkMostrarPassword.setBackground(Color.WHITE);
        addCheckboxRow(panel, gbc, 1, chkMostrarPassword);

        return panel;
    }

    public void mostrarPasoCorreo() {
        cardLayout.show(panelForm, "PASO_CORREO");
    }

    public void mostrarPasoContrasena() {
        cardLayout.show(panelForm, "PASO_PASSWORD");
    }
}