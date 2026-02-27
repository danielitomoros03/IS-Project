package com.example.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.example.Modelo.MenuRecord;


public class CrearMenuDialog extends JDialog {

    private final Color COLOR_PRIMARY = new Color(33, 115, 70);
    private static final String ARCHIVO_PLATOS = "Platos.txt";
    private final JFrame owner;
    private JButton btnSave;
    private JLabel lblCounter;
    private int seleccionados = 0;
    private JComboBox<LocalDate> comboFecha;
    private JComboBox<String> comboTurno;
    private JTextField txtPlato;
    private JPanel gridPlatos;
    private final List<String> platosDisponibles = new ArrayList<>();
    private final Map<String, JCheckBox> platosMap = new HashMap<>();

    public CrearMenuDialog(JFrame parent) {
        super(parent, "Crear Nuevo Menú", true);
        this.owner = parent;
        construirUI();
    }

    public CrearMenuDialog(JFrame parent, MenuRecord existente) {
        super(parent, "Editar Menú", true);
        this.owner = parent;
        construirUI();
        precargar(existente);
    }

    private void construirUI() {
        setSize(500, 700); // Un poco más alto para el contador
        setLocationRelativeTo(owner);
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

        body.add(crearLabel("Fecha (hoy y siguientes 15 dias)"));
        comboFecha = new JComboBox<>(crearOpcionesFecha().toArray(new LocalDate[0]));
        body.add(comboFecha);
        body.add(Box.createVerticalStrut(15));

        body.add(crearLabel("Turno"));
        String[] turnos = {"Selecciona un turno", "Desayuno", "Almuerzo"};
        comboTurno = new JComboBox<>(turnos);
        body.add(comboTurno);
        body.add(Box.createVerticalStrut(15));

        body.add(crearLabel("Platos del menú"));

        JPanel panelEdicionPlatos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelEdicionPlatos.setBackground(Color.WHITE);
        panelEdicionPlatos.setBorder(new EmptyBorder(0, 0, 20, 0));
        JLabel lblPlato = new JLabel("Nombre del plato:");
        lblPlato.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPlato = new JTextField(18);
        txtPlato.setToolTipText("Escribe el nombre para agregar o renombrar");
        JButton btnAgregar = new JButton("Agregar");
        JButton btnRenombrar = new JButton("Renombrar");
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setToolTipText("Eliminar platos seleccionados");

        btnAgregar.addActionListener(e -> agregarPlato());
        btnRenombrar.addActionListener(e -> renombrarPlato());
        btnEliminar.addActionListener(e -> eliminarPlatosSeleccionados());

        panelEdicionPlatos.add(lblPlato);
        panelEdicionPlatos.add(txtPlato);
        panelEdicionPlatos.add(btnAgregar);
        panelEdicionPlatos.add(btnRenombrar);
        panelEdicionPlatos.add(btnEliminar);
        body.add(panelEdicionPlatos);
        body.add(Box.createVerticalStrut(10));

        gridPlatos = new JPanel(new GridLayout(0, 2, 10, 10));
        gridPlatos.setBackground(new Color(250, 250, 250));
        gridPlatos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        lblCounter = new JLabel("0 platos seleccionados");
        lblCounter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCounter.setForeground(Color.GRAY);
        lblCounter.setBorder(new EmptyBorder(10, 0, 0, 0));

        platosDisponibles.addAll(cargarPlatos());
        renderizarPlatos();

        JScrollPane scrollPlatos = new JScrollPane(gridPlatos);
        scrollPlatos.setPreferredSize(new Dimension(400, 250));
        scrollPlatos.setBorder(null);
        body.add(scrollPlatos);

        // Contador de platos 
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
        LocalDate fecha = obtenerFechaSeleccionada();
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

    private LocalDate obtenerFechaSeleccionada() {
        LocalDate fecha = (LocalDate) comboFecha.getSelectedItem();
        if (fecha == null) {
            throw new IllegalArgumentException("Selecciona una fecha valida.");
        }
        return fecha;
    }

    private List<LocalDate> crearOpcionesFecha() {
        List<LocalDate> opciones = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        for (int i = 0; i <= 15; i++) {
            opciones.add(hoy.plusDays(i));
        }
        return opciones;
    }

    private List<String> cargarPlatos() {
        Path ruta = Paths.get(ARCHIVO_PLATOS);
        if (Files.exists(ruta)) {
            try {
                List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
                List<String> filtradas = new ArrayList<>();
                for (String l : lineas) {
                    String plato = l == null ? "" : l.trim();
                    if (!plato.isEmpty() && !filtradas.contains(plato)) {
                        filtradas.add(plato);
                    }
                }
                if (!filtradas.isEmpty()) {
                    return filtradas;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo leer la lista de platos. Se usaran valores por defecto.");
            }
        }
        List<String> porDefecto = new ArrayList<>();
        porDefecto.add("Pollo al horno");
        porDefecto.add("Carne mechada");
        porDefecto.add("Pescado a la plancha");
        porDefecto.add("Pasta boloñesa");
        porDefecto.add("Arroz blanco");
        porDefecto.add("Arroz integral");
        porDefecto.add("Ensalada César");
        porDefecto.add("Ensalada verde");
        porDefecto.add("Jugo natural");
        porDefecto.add("Café");
        guardarPlatos(porDefecto);
        return porDefecto;
    }

    private void guardarPlatos(List<String> platos) {
        Path ruta = Paths.get(ARCHIVO_PLATOS);
        try {
            Files.write(ruta, platos, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar la lista de platos.");
        }
    }

    private void renderizarPlatos() {
        gridPlatos.removeAll();
        platosMap.clear();
        seleccionados = 0;
        for (String plato : platosDisponibles) {
            JCheckBox cb = new JCheckBox(plato);
            cb.setBackground(Color.WHITE);
            cb.setFocusPainted(false);
            cb.addActionListener(e -> {
                if (cb.isSelected()) {
                    seleccionados++;
                } else {
                    seleccionados--;
                }
                if (lblCounter != null) {
                    lblCounter.setText(seleccionados + " platos seleccionados");
                }
            });
            gridPlatos.add(cb);
            platosMap.put(plato, cb);
        }
        if (lblCounter != null) {
            lblCounter.setText(seleccionados + " platos seleccionados");
        }
        gridPlatos.revalidate();
        gridPlatos.repaint();
    }

    private void agregarPlato() {
        String nuevo = txtPlato.getText() == null ? "" : txtPlato.getText().trim();
        if (nuevo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el nombre del plato.");
            return;
        }
        if (platosDisponibles.contains(nuevo)) {
            JOptionPane.showMessageDialog(this, "Ese plato ya existe.");
            return;
        }
        platosDisponibles.add(nuevo);
        guardarPlatos(platosDisponibles);
        txtPlato.setText("");
        renderizarPlatos();
    }

    private void renombrarPlato() {
        List<String> seleccion = obtenerPlatosSeleccionados();
        if (seleccion.size() != 1) {
            JOptionPane.showMessageDialog(this, "Selecciona un solo plato para renombrar.");
            return;
        }
        String nuevo = txtPlato.getText() == null ? "" : txtPlato.getText().trim();
        if (nuevo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el nuevo nombre del plato.");
            return;
        }
        if (platosDisponibles.contains(nuevo)) {
            JOptionPane.showMessageDialog(this, "Ese plato ya existe.");
            return;
        }
        String actual = seleccion.get(0);
        int idx = platosDisponibles.indexOf(actual);
        if (idx >= 0) {
            platosDisponibles.set(idx, nuevo);
            guardarPlatos(platosDisponibles);
            txtPlato.setText("");
            renderizarPlatos();
        }
    }

    private void eliminarPlatosSeleccionados() {
        List<String> seleccion = obtenerPlatosSeleccionados();
        if (seleccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos un plato para eliminar.");
            return;
        }
        platosDisponibles.removeAll(seleccion);
        guardarPlatos(platosDisponibles);
        renderizarPlatos();
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
        LocalDate fechaExistente = existente.getFecha();
        boolean fechaEnLista = false;
        for (int i = 0; i < comboFecha.getItemCount(); i++) {
            if (fechaExistente.equals(comboFecha.getItemAt(i))) {
                fechaEnLista = true;
                break;
            }
        }
        if (!fechaEnLista) {
            comboFecha.addItem(fechaExistente);
        }
        comboFecha.setSelectedItem(fechaExistente);
        comboTurno.setSelectedItem(existente.getTurno());
        boolean cambio = false;
        for (String plato : existente.getPlatos()) {
            if (!platosDisponibles.contains(plato)) {
                platosDisponibles.add(plato);
                cambio = true;
            }
        }
        if (cambio) {
            guardarPlatos(platosDisponibles);
            renderizarPlatos();
        }
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