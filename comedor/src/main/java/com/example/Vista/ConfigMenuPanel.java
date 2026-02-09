package com.example.Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter; //La usamos para crear las tablas que aparecen para buscar menu por nombre
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ConfigMenuPanel extends JPanel {
    
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(33, 115, 70); 
    private final Color COLOR_ACCENT = new Color(255, 255, 255);
    
    private DefaultTableModel modelo;
    private JTable tabla;
    private TableRowSorter<DefaultTableModel> sorter;

    private JButton btnCrear;

    public ConfigMenuPanel() {
        setBackground(COLOR_BG);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. ENCABEZADO
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_BG);

        JPanel titulo = new JPanel(new GridLayout(2, 1, 0, 5));
        titulo.setBackground(COLOR_BG);
        
        JLabel lblTitle = new JLabel("Gestión de Menús");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        
        JLabel lblSubtitle = new JLabel("Crea, edita y gestiona los menús del comedor");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitle.setForeground(Color.GRAY);
        
        titulo.add(lblTitle);
        titulo.add(lblSubtitle);

        btnCrear = new JButton("+ Crear Nuevo Menú");
        btnCrear.setBackground(COLOR_PRIMARY);
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCrear.setFocusPainted(false);
        btnCrear.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        btnCrear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCrear.setActionCommand("+ Crear Nuevo Menú");
        
        headerPanel.add(titulo, BorderLayout.WEST);
        headerPanel.add(btnCrear, BorderLayout.EAST);

        // Barra de busqueda
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(COLOR_ACCENT);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JTextField txtSearch = new JTextField("  Buscar por plato o turno...");
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setForeground(Color.GRAY);
        
        // Evento para filtrar la tabla mientras se escribe
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = txtSearch.getText();
                if (texto.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    // Filtra en todas las columnas 
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                }
            }
        });
        
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        // Contenedor superior
        JPanel topContainer = new JPanel(new BorderLayout(0, 25));
        topContainer.setBackground(COLOR_BG);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(searchPanel, BorderLayout.CENTER);

        add(topContainer, BorderLayout.NORTH);

        inicializarTabla();
    }

    private void inicializarTabla() { //Por ahora, para visualizar algo
        String[] columnas = {"Fecha", "Turno", "Platos", "Estado", "Acciones"};
        Object[][] datos = {
            {"7 dic 2025", "Almuerzo", "Pollo al horno, Arroz blanco, Ensalada César", "Activo", "✏️  🗑️"},
            {"8 dic 2025", "Desayuno", "Arepa con queso, Jugo natural, Café", "Programado", "✏️  🗑️"}
        };

        modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter); // Esto permite que el buscador funcione
        
        // Estilos
        tabla.setRowHeight(50);
        tabla.setGridColor(new Color(240, 240, 240));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setReorderingAllowed(false);

        // Renderizador de Estado
        tabla.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                
                if ("Activo".equals(value)) {
                    label.setForeground(COLOR_PRIMARY);
                    label.setFont(label.getFont().deriveFont(Font.BOLD));
                } else {
                    label.setForeground(new Color(52, 152, 219));
                }
                return label;
            }
        });

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblSeccion = new JLabel("Menús Programados");
        lblSeccion.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSeccion.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        tableContainer.add(lblSeccion, BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scroll, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);
    }

    public JButton getBtnCrear() { return btnCrear; }
    
}