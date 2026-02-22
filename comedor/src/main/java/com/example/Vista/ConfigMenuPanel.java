package com.example.Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter; //La usamos para crear las tablas que aparecen para buscar menu por nombre
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.List;

import com.example.Modelo.MenuModel;
import com.example.Modelo.MenuRecord;

public class ConfigMenuPanel extends JPanel {
    
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(33, 115, 70); 
    private final Color COLOR_ACCENT = new Color(255, 255, 255);
    
    private DefaultTableModel modelo;
    private JTable tabla;
    private TableRowSorter<DefaultTableModel> sorter;

    private JButton btnCrear;
    private JButton btnEditar;
    private JButton btnEliminar;

    private final MenuModel menuModel = new MenuModel();

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

        btnEditar = new JButton("Editar Seleccionado");
        btnEditar.setBackground(Color.WHITE);
        btnEditar.setForeground(COLOR_PRIMARY);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditar.setFocusPainted(false);
        btnEditar.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY));
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnEliminar = new JButton("Eliminar Seleccionado");
        btnEliminar.setBackground(Color.WHITE);
        btnEliminar.setForeground(new Color(192, 57, 43));
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBorder(BorderFactory.createLineBorder(new Color(192, 57, 43)));
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setBackground(COLOR_BG);
        acciones.add(btnEditar);
        acciones.add(btnEliminar);
        acciones.add(btnCrear);

        headerPanel.add(titulo, BorderLayout.WEST);
        headerPanel.add(acciones, BorderLayout.EAST);

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
        registrarAcciones();
    }

    private void inicializarTabla() {
        String[] columnas = {"Id", "Fecha", "Turno", "Platos", "Estado"};

        modelo = new DefaultTableModel(columnas, 0) {
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
        tabla.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
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

        // Ocultar columna Id
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);

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

        cargarMenus();
    }

    public JButton getBtnCrear() { return btnCrear; }
    
    //Para simular la consulta desde el código o desde el test
    public void realizarConsulta(String texto) {
        if (texto.trim().length() == 0){
            sorter.setRowFilter(null);
        } 
        else{
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

    // Getter para acceder a la tabla y verificar sus filas en la prueba
    public JTable getTabla(){ 
        return tabla; 
    }

    public void abrirDialogoCrear(JFrame parent) {
        CrearMenuDialog dialogo = new CrearMenuDialog(parent);
        dialogo.getBtnSave().addActionListener(e -> {
            try {
                MenuRecord nuevo = dialogo.construirMenu();
                menuModel.guardar(nuevo);
                cargarMenus();
                dialogo.dispose();
                JOptionPane.showMessageDialog(this, "Menú creado exitosamente.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialogo, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialogo.setVisible(true);
    }

    private void registrarAcciones() {
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarSeleccionado();
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarSeleccionado();
            }
        });
    }

    private void cargarMenus() {
        modelo.setRowCount(0);
        List<MenuRecord> menus = menuModel.obtenerMenus();
        LocalDate hoy = LocalDate.now();
        for (MenuRecord m : menus) {
            modelo.addRow(new Object[] {
                m.getId(),
                m.getFecha().toString(),
                m.getTurno(),
                m.getPlatosTexto(),
                m.getEstado(hoy)
            });
        }
    }

    private String obtenerIdSeleccionado() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            return null;
        }
        int modelRow = tabla.convertRowIndexToModel(row);
        return modelo.getValueAt(modelRow, 0).toString();
    }

    private MenuRecord obtenerMenuPorId(String id) {
        List<MenuRecord> menus = menuModel.obtenerMenus();
        for (MenuRecord m : menus) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    private void editarSeleccionado() {
        String id = obtenerIdSeleccionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un menú para editar.");
            return;
        }
        MenuRecord actual = obtenerMenuPorId(id);
        if (actual == null) {
            JOptionPane.showMessageDialog(this, "No se pudo encontrar el menú seleccionado.");
            return;
        }

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        CrearMenuDialog dialogo = new CrearMenuDialog(parent, actual);
        dialogo.getBtnSave().setText("Actualizar Menú");
        dialogo.getBtnSave().addActionListener(e -> {
            try {
                MenuRecord actualizado = dialogo.construirMenuConId(actual.getId());
                menuModel.actualizar(actualizado);
                cargarMenus();
                dialogo.dispose();
                JOptionPane.showMessageDialog(this, "Menú actualizado.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialogo, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialogo.setVisible(true);
    }

    private void eliminarSeleccionado() {
        String id = obtenerIdSeleccionado();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un menú para eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar el menú seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            menuModel.eliminar(id);
            cargarMenus();
        }
    }

}