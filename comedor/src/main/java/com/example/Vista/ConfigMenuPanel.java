package com.example.Vista;

import javax.swing.*;
import java.awt.*;

public class ConfigMenuPanel extends JPanel {
    public ConfigMenuPanel() {
        setBackground(new Color(245, 247, 250)); // COLOR_BG
        setLayout(new BorderLayout());
        add(new JLabel("CONTENIDO: Formulario de Menú", SwingConstants.CENTER));
        // Aquí va todo el diseño de los campos de texto
    }
}