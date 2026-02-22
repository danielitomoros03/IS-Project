package com.example.Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Modelo.MenuRecord;


public class CrearMenuDialog extends JDialog {

    private final Color COLOR_PRIMARY = new Color(33, 115, 70);
    private JButton btnSave;
    private JLabel lblCounter;
    private int seleccionados = 0;
    private JTextField txtFecha;
    private JComboBox<String> comboTurno;
    private final Map<String, JCheckBox> platosMap = new HashMap<>();

    public CrearMenuDialog(JFrame parent) {
        super(parent, "Crear Nuevo Menú", true);
        construirUI();
    }

    public CrearMenuDialog(JFrame parent, MenuRecord existente) {
        super(parent, "Editar Menú", true);
        construirUI();
        precargar(existente);
    }

    private void construirUI() {
        setSize(500, 700); // Un poco más alto para el contador
        setLocationRelativeTo(parent);
        setUndecorated(true); 
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        // 1. ENCABEZADO
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(20, 25, 10, 25));

        JLabel titulo = new JLabel("Crear Nuevo Menú");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        JLabel subtitulo = new JLabel("Completa la información del menú para el comedor");
        subtitulo.setForeground(Color.GRAY);
        
        header.add(titulo);
        header.add(subtitulo);

        // 2. CUERPO
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(10, 25, 10, 25));

        body.add(crearLabel("Fecha (YYYY-MM-DD)"));
        txtFecha = new JTextField("  ");
        body.add(txtFecha);
        body.add(Box.createVerticalStrut(15));

        body.add(crearLabel("Turno"));
        String[] turnos = {"Selecciona un turno", "Desayuno", "Almuerzo"};
        comboTurno = new JComboBox<>(turnos);
        body.add(comboTurno);
        body.add(Box.createVerticalStrut(15));

        body.add(crearLabel("Platos del menú"));
        
        JPanel gridPlatos = new JPanel(new GridLayout(0, 2, 10, 10));
        gridPlatos.setBackground(new Color(250, 250, 250));
        gridPlatos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        String[] platos = {"Pollo al horno", "Carne mechada", "Pescado a la plancha", 
                          "Pasta boloñesa", "Arroz blanco", "Arroz integral", 
                          "Ensalada César", "Ensalada verde", "Jugo natural", "Café"};
        
        for (String plato : platos) {
            JCheckBox cb = new JCheckBox(plato);
            cb.setBackground(Color.WHITE);
            cb.setFocusPainted(false);
            // Lógica para actualizar el contador de la imagen
            cb.addActionListener(e -> {
                if(cb.isSelected()) seleccionados++;
                else seleccionados--;
                lblCounter.setText(seleccionados + " platos seleccionados");
            });
            gridPlatos.add(cb);
            platosMap.put(plato, cb);
        }

        JScrollPane scrollPlatos = new JScrollPane(gridPlatos);
        scrollPlatos.setPreferredSize(new Dimension(400, 250));
        scrollPlatos.setBorder(null);
        body.add(scrollPlatos);

        // Contador de platos 
        lblCounter = new JLabel("0 platos seleccionados");
        lblCounter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCounter.setForeground(Color.GRAY);
        lblCounter.setBorder(new EmptyBorder(10, 0, 0, 0));
        body.add(lblCounter);

        // Pie de pagina, es opcional todavia
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footer.setBackground(Color.WHITE);
        
        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setBorderPainted(false);
        btnCancel.setContentAreaFilled(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());

        btnSave = new JButton("Crear Menú");
        btnSave.setBackground(COLOR_PRIMARY);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

        footer.add(btnCancel);
        footer.add(btnSave);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(body, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel crearLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setBorder(new EmptyBorder(5, 0, 5, 0));
        return l;
    }

    //Para el controlador
    public JButton getBtnSave() { return btnSave; }

    public MenuRecord construirMenu() {
        LocalDate fecha = parseFecha(txtFecha.getText());
        String turno = (String) comboTurno.getSelectedItem();
        if (turno == null || "Selecciona un turno".equals(turno)) {
            throw new IllegalArgumentException("Selecciona un turno valido.");
        }
        List<String> platos = obtenerPlatosSeleccionados();
        if (platos.isEmpty()) {
            throw new IllegalArgumentException("Selecciona al menos un plato.");
        }
        return new MenuRecord(fecha, turno, platos);
    }

    public MenuRecord construirMenuConId(String id) {
        MenuRecord base = construirMenu();
        return new MenuRecord(id, base.getFecha(), base.getTurno(), base.getPlatos());
    }

    private LocalDate parseFecha(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Completa la fecha del menu.");
        }
        try {
            LocalDate fecha = LocalDate.parse(texto.trim());
            if (fecha.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha del menu no puede ser anterior a hoy.");
            }
            return fecha;
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Formato de fecha invalido. Usa YYYY-MM-DD.");
        }
    }

    private List<String> obtenerPlatosSeleccionados() {
        List<String> seleccion = new ArrayList<>();
        for (Map.Entry<String, JCheckBox> entry : platosMap.entrySet()) {
            if (entry.getValue().isSelected()) {
                seleccion.add(entry.getKey());
            }
        }
        return seleccion;
    }

    private void precargar(MenuRecord existente) {
        if (existente == null) {
            return;
        }
        txtFecha.setText(existente.getFecha().toString());
        comboTurno.setSelectedItem(existente.getTurno());
        seleccionados = 0;
        for (String plato : existente.getPlatos()) {
            JCheckBox cb = platosMap.get(plato);
            if (cb != null) {
                cb.setSelected(true);
                seleccionados++;
            }
        }
        lblCounter.setText(seleccionados + " platos seleccionados");
    }
}