package com.example.Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DashAdminVista extends JPanel {
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(34, 120, 64); // Verde UCV
    private final Color COLOR_ACCENT = new Color(59, 130, 246); // Azul Insumos

    public DashAdminVista() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- TITULO Y FECHA ---
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(COLOR_BG);
        JLabel title = new JLabel("Dashboard Administrativo");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        JLabel date = new JLabel("Lunes, 7 de Diciembre 2025");
        date.setForeground(Color.GRAY);
        header.add(title);
        header.add(date);
        add(header, BorderLayout.NORTH);

        // --- CONTENIDO SCROLLABLE ---
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(COLOR_BG);

        // 1. Fila de Métricas (Las 4 tarjetas superiores)
        JPanel metricsGrid = new JPanel(new GridLayout(1, 4, 20, 0));
        metricsGrid.setBackground(COLOR_BG);
        metricsGrid.setMaximumSize(new Dimension(2000, 150));

        metricsGrid.add(crearTarjetaMetrica("Demanda Hoy", "234", "+12% vs semana pasada", Color.BLACK));
        metricsGrid.add(crearTarjetaMetrica("Ocupación", "78%", "-5% vs ayer", Color.BLACK));
        metricsGrid.add(crearTarjetaMetrica("Insumos", "42", "5 en alerta", new Color(180, 83, 9))); // Naranja alerta
        metricsGrid.add(crearTarjetaMetrica("Menús", "15", "Programados este mes", Color.BLACK));

        content.add(metricsGrid);
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        // 2. Sección Inferior: Alertas y Accesos Rápidos
        JPanel bottomSection = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomSection.setBackground(COLOR_BG);

        // Bloque Alertas de Insumos
        JPanel panelAlertas = new JPanel(new BorderLayout());
        panelAlertas.setBackground(Color.WHITE);
        panelAlertas.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));
        
        JLabel lblAlertas = new JLabel("  Alertas de Insumos");
        lblAlertas.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblAlertas.setBorder(new EmptyBorder(15, 10, 15, 10));
        panelAlertas.add(lblAlertas, BorderLayout.NORTH);

        JPanel listaAlertas = new JPanel();
        listaAlertas.setLayout(new BoxLayout(listaAlertas, BoxLayout.Y_AXIS));
        listaAlertas.setBackground(Color.WHITE);
        listaAlertas.add(crearItemAlerta("Arroz blanco", "Vence en 2 días", "15 kg", Color.ORANGE));
        listaAlertas.add(crearItemAlerta("Aceite vegetal", "Stock bajo", "3 L", Color.RED));
        panelAlertas.add(listaAlertas, BorderLayout.CENTER);

        // Bloque Accesos Rápidos
        JPanel panelAccesos = new JPanel(new GridLayout(4, 1, 0, 10));
        panelAccesos.setBackground(COLOR_BG);
        JLabel lblAccesos = new JLabel("Accesos Rápidos");
        lblAccesos.setFont(new Font("SansSerif", Font.BOLD, 16));
        panelAccesos.add(lblAccesos);
        panelAccesos.add(crearBotonAcceso("Gestionar Menús", COLOR_PRIMARY));
        panelAccesos.add(crearBotonAcceso("Gestionar Insumos", COLOR_ACCENT));
        panelAccesos.add(crearBotonAcceso("Generar Reportes", Color.WHITE));

        bottomSection.add(panelAlertas);
        bottomSection.add(panelAccesos);

        content.add(bottomSection);
        
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearTarjetaMetrica(String titulo, String valor, String sub, Color valorColor) {
        JPanel card = new JPanel(new GridLayout(3, 1, 5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel t = new JLabel(titulo);
        t.setForeground(Color.GRAY);
        JLabel v = new JLabel(valor);
        v.setFont(new Font("SansSerif", Font.BOLD, 24));
        v.setForeground(valorColor);
        JLabel s = new JLabel(sub);
        s.setFont(new Font("SansSerif", Font.PLAIN, 11));
        s.setForeground(new Color(34, 197, 94)); // Verde éxito

        card.add(t);
        card.add(v);
        card.add(s);
        return card;
    }

    private JPanel crearItemAlerta(String nombre, String estado, String cant, Color badgeColor) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Color.WHITE);
        item.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel texto = new JLabel("<html><b>" + nombre + "</b><br><font color='gray'>" + estado + "</font></html>");
        JLabel badge = new JLabel(cant);
        badge.setOpaque(true);
        badge.setBackground(new Color(badgeColor.getRed(), badgeColor.getGreen(), badgeColor.getBlue(), 40));
        badge.setForeground(badgeColor);
        badge.setBorder(new EmptyBorder(5, 10, 5, 10));

        item.add(texto, BorderLayout.WEST);
        item.add(badge, BorderLayout.EAST);
        return item;
    }

    private JButton crearBotonAcceso(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(bg == Color.WHITE ? Color.BLACK : Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(new Color(220, 220, 220)));
        btn.setPreferredSize(new Dimension(200, 45));
        return btn;
    }
}