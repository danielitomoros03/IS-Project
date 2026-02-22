package com.example.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.example.Modelo.MenuModel;
import com.example.Modelo.MenuRecord;

public class MenuDiaPanel extends JPanel {

    public MenuDiaPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); // Fondo gris claro

        // 1. Título de la sección
        JLabel lblTitulo = new JLabel("Menú del Día", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // 2. Panel contenedor de los platos (usamos BoxLayout vertical)
        JPanel panelPlatos = new JPanel();
        panelPlatos.setLayout(new BoxLayout(panelPlatos, BoxLayout.Y_AXIS));
        panelPlatos.setBackground(Color.WHITE);

        // --- LÓGICA DE NEGOCIO ---
        MenuModel menuModel = new MenuModel();
        List<MenuRecord> menuDelDia = menuModel.obtenerMenusPorFecha(LocalDate.now());

        // --- VALIDACIÓN DE CRITERIOS DE ACEPTACIÓN ---
        
        // ESCENARIO: No hay menú disponible
        if (menuDelDia == null || menuDelDia.isEmpty()) {
            JLabel lblNoMenu = new JLabel("No hay menú disponible para hoy", SwingConstants.CENTER);
            lblNoMenu.setFont(new Font("Arial", Font.ITALIC, 18));
            lblNoMenu.setForeground(Color.GRAY);
            lblNoMenu.setBorder(new EmptyBorder(50, 0, 0, 0));
            panelPlatos.add(lblNoMenu);
        } 
        // ESCENARIO: Consulta con éxito
        else {
            for (MenuRecord menu : menuDelDia) {
                panelPlatos.add(crearTarjetaMenu(menu));
                panelPlatos.add(Box.createRigidArea(new Dimension(0, 15))); // Espacio entre menus
            }
        }

        //  scroll por si hay muchos platos
        JScrollPane scrollPane = new JScrollPane(panelPlatos);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Método auxiliar para crear una tarjeta visual de cada menu
    private JPanel crearTarjetaMenu(MenuRecord menu) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setMaximumSize(new Dimension(600, 180)); // Tamaño fijo para uniformidad

        // Nombre del turno
        JLabel lblNombre = new JLabel("Turno: " + menu.getTurno());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 18));
        lblNombre.setForeground(new Color(50, 50, 50));

        // Listado de platos
        JLabel lblDesc = new JLabel("<html><i>" + menu.getPlatosTexto() + "</i></html>");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 14));

        // Armado de la tarjeta
        JPanel panelTexto = new JPanel(new GridLayout(2, 1));
        panelTexto.setBackground(Color.WHITE);
        panelTexto.add(lblNombre);
        panelTexto.add(lblDesc);

        tarjeta.add(panelTexto, BorderLayout.CENTER);

        return tarjeta;
    }
}
