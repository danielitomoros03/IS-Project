package com.example.Vista;

import javax.swing.*;
import java.awt.*;

public class HistorialPanel extends JPanel {
    public HistorialPanel() {
        setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout());
        add(new JLabel("Historial - Próximamente Tabla de Datos", SwingConstants.CENTER));
    }
}