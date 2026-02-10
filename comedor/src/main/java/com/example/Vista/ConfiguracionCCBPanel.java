package com.example.Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class ConfiguracionCCBPanel extends JPanel {

    private JTextField txtCostosFijos;
    private JTextField txtCostosVariables;
    private JTextField txtNumBandejas;
    private JTextField txtMerma;
    private JLabel lblResultadoCCB;
    private JButton btnCalcular;
    private JButton btnGuardar;

    private final Color COLOR_PRIMARY = new Color(34, 120, 64);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);

    public ConfiguracionCCBPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Título
        JLabel title = new JLabel("Configuración del Costo Cubierto de Bandeja (CCB)");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(COLOR_PRIMARY);
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Costos Fijos
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(crearLabel("Costos Fijos (Bs):"), gbc);
        gbc.gridx = 1;
        txtCostosFijos = crearInput();
        formPanel.add(txtCostosFijos, gbc);

        // Costos Variables
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(crearLabel("Costos Variables (Bs):"), gbc);
        gbc.gridx = 1;
        txtCostosVariables = crearInput();
        formPanel.add(txtCostosVariables, gbc);

        // Número de Bandejas
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(crearLabel("Nro. Bandejas Proyectadas:"), gbc);
        gbc.gridx = 1;
        txtNumBandejas = crearInput();
        formPanel.add(txtNumBandejas, gbc);

        //  Porcentaje de Merma
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(crearLabel("Porcentaje de Merma (%):"), gbc);
        gbc.gridx = 1;
        txtMerma = crearInput();
        formPanel.add(txtMerma, gbc);

        //Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        
        btnCalcular = new JButton("Calcular CCB");
        estilizarBoton(btnCalcular, false);
        
        btnGuardar = new JButton("Guardar y Actualizar Precios");
        estilizarBoton(btnGuardar, true);
        btnGuardar.setEnabled(false); // Desactivado hasta que se calcule

        buttonPanel.add(btnCalcular);
        buttonPanel.add(btnGuardar);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        //Resultado
        gbc.gridy = 5;
        lblResultadoCCB = new JLabel("CCB Actual: - ");
        lblResultadoCCB.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblResultadoCCB.setForeground(COLOR_TEXT_DARK);
        formPanel.add(lblResultadoCCB, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Acción Calcular
        btnCalcular.addActionListener(e -> calcularCCB());

        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Costo guardado correctamente.\nSe han actualizado las tarifas para Estudiantes, Profesores y Empleados.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void calcularCCB() {
        try {
            double cf = Double.parseDouble(txtCostosFijos.getText());
            double cv = Double.parseDouble(txtCostosVariables.getText());
            double nb = Double.parseDouble(txtNumBandejas.getText());
            double merma = Double.parseDouble(txtMerma.getText());

            if (nb == 0) {
                JOptionPane.showMessageDialog(this, "El número de bandejas no puede ser 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //[(CF + CV) / NB] * (1 + %Merma/100)
            double costoBase = (cf + cv) / nb;
            double factorMerma = 1 + (merma / 100.0);
            double ccb = costoBase * factorMerma;

            DecimalFormat df = new DecimalFormat("#.00");
            lblResultadoCCB.setText("CCB Calculado: " + df.format(ccb) + " Bs/Bandeja");
            lblResultadoCCB.setForeground(new Color(34, 120, 64)); 
            
            btnGuardar.setEnabled(true);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese valores numéricos válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helpers UI
    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        return lbl;
    }

    private JTextField crearInput() {
        JTextField txt = new JTextField(15);
        txt.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return txt;
    }

    private void estilizarBoton(JButton btn, boolean isPrimary) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (isPrimary) {
            btn.setBackground(COLOR_PRIMARY);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(220, 220, 220));
            btn.setForeground(COLOR_TEXT_DARK);
        }
    }
}