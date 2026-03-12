package com.example.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.example.Modelo.AsistenciaComedorModel;
import com.example.Modelo.AsistenciaRecord;
import com.example.Modelo.BeneficioComensalModel;
import com.example.Modelo.RegUsuarioModelo;

public class ReportePanel extends JPanel {
    private static final BigDecimal PORCENTAJE_REGULAR = new BigDecimal("100.00");

    private final BeneficioComensalModel beneficioModel = new BeneficioComensalModel();
    private final RegUsuarioModelo regUsuarioModelo = new RegUsuarioModelo();
    private final AsistenciaComedorModel asistenciaModel = new AsistenciaComedorModel();

    private JTextField txtCi;
    private JComboBox<String> comboTipo;
    private JTextField txtPorcentaje;
    private JLabel lblEstadoBeneficio;

    private JComboBox<String> comboServicio;
    private DefaultTableModel detalleModel;
    private DefaultTableModel resumenModel;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ReportePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Reportes y Clasificacion de Comensales", SwingConstants.LEFT);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setBorder(new EmptyBorder(0, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(new Color(245, 247, 250));

        contenido.add(crearPanelBeneficios());
        contenido.add(Box.createVerticalStrut(15));
        contenido.add(crearPanelReporteServicio());

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        cargarReporteServicio();
    }

    private JPanel crearPanelBeneficios() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel subtitulo = new JLabel("Gestion de Estudiante Exonerado/Becario por CI");
        subtitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        panel.add(subtitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("CI del estudiante"), gbc);

        txtCi = new JTextField(12);
        gbc.gridx = 1;
        panel.add(txtCi, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Tipo"), gbc);

        comboTipo = new JComboBox<>(new String[] {"Exonerado", "Becario"});
        gbc.gridx = 3;
        panel.add(comboTipo, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Descuento Becario (%)"), gbc);

        txtPorcentaje = new JTextField("5", 12);
        txtPorcentaje.setEnabled(false);
        gbc.gridx = 1;
        panel.add(txtPorcentaje, gbc);

        JButton btnGuardar = new JButton("Guardar clasificacion");
        gbc.gridx = 3;
        panel.add(btnGuardar, gbc);

        lblEstadoBeneficio = new JLabel(" ");
        lblEstadoBeneficio.setForeground(new Color(34, 120, 64));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        panel.add(lblEstadoBeneficio, gbc);

        comboTipo.addActionListener(e -> {
            boolean esBecario = "Becario".equals(comboTipo.getSelectedItem());
            txtPorcentaje.setEnabled(esBecario);
            if (!esBecario) {
                txtPorcentaje.setText("5");
            }
        });

        btnGuardar.addActionListener(e -> guardarBeneficio());

        return panel;
    }

    private JPanel crearPanelReporteServicio() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel subtitulo = new JLabel("Reporte de comensales por servicio");
        subtitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.add(subtitulo, BorderLayout.WEST);

        JPanel acciones = new JPanel();
        acciones.setOpaque(false);
        comboServicio = new JComboBox<>(new String[] {"Desayuno", "Almuerzo"});
        JButton btnActualizar = new JButton("Actualizar");
        acciones.add(new JLabel("Servicio"));
        acciones.add(comboServicio);
        acciones.add(btnActualizar);
        header.add(acciones, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);

        detalleModel = new DefaultTableModel(
            new Object[] {"Fecha", "Email", "CI", "Tipo Comensal", "Monto Cobrado (Bs)"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaDetalle = new JTable(detalleModel);
        JScrollPane scrollDetalle = new JScrollPane(tablaDetalle);

        resumenModel = new DefaultTableModel(new Object[] {"Tipo", "Cantidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaResumen = new JTable(resumenModel);
        JScrollPane scrollResumen = new JScrollPane(tablaResumen);

        JPanel tablas = new JPanel(new GridLayout(2, 1, 0, 10));
        tablas.setOpaque(false);
        tablas.add(scrollDetalle);
        tablas.add(scrollResumen);

        panel.add(tablas, BorderLayout.CENTER);

        comboServicio.addActionListener(e -> cargarReporteServicio());
        btnActualizar.addActionListener(e -> cargarReporteServicio());

        return panel;
    }

    private void guardarBeneficio() {
        String ci = txtCi.getText();
        if (ci == null || ci.trim().isEmpty()) {
            mostrarError("Debes indicar la CI del estudiante.");
            return;
        }

        String[] datos = regUsuarioModelo.obtenerDatosPorCiDesdeArchivo(ci);
        if (datos == null) {
            mostrarError("No existe un estudiante con esa CI en Usuarios_UCV.txt.");
            return;
        }

        String rol = datos[2] == null ? "" : datos[2].toLowerCase();
        if (!rol.contains("estudiante")) {
            mostrarError("Solo se permite clasificar estudiantes como Exonerado o Becario.");
            return;
        }

        String tipo = (String) comboTipo.getSelectedItem();
        try {
            if ("Exonerado".equals(tipo)) {
                beneficioModel.registrarExonerado(ci);
                lblEstadoBeneficio.setForeground(new Color(34, 120, 64));
                lblEstadoBeneficio.setText("CI " + ci + " clasificada como Exonerado.");
            } else {
                BigDecimal porcentaje = parsePorcentaje(txtPorcentaje.getText());
                if (porcentaje.compareTo(PORCENTAJE_REGULAR) >= 0) {
                    mostrarError("El porcentaje de descuento del Becario debe ser menor a 100%.");
                    return;
                }
                beneficioModel.registrarBecario(ci, porcentaje);
                lblEstadoBeneficio.setForeground(new Color(34, 120, 64));
                lblEstadoBeneficio.setText(
                    "CI " + ci + " clasificada como Becario con descuento de "
                        + porcentaje.setScale(2, RoundingMode.HALF_UP).toPlainString() + "% ."
                );
            }
        } catch (IllegalArgumentException | IOException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private BigDecimal parsePorcentaje(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Debes indicar el porcentaje de descuento del Becario.");
        }

        String normalizado = valor.trim().replace(',', '.');
        BigDecimal porcentaje;
        try {
            porcentaje = new BigDecimal(normalizado);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El porcentaje de descuento del Becario es invalido.");
        }

        if (porcentaje.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El porcentaje de descuento del Becario debe ser mayor a 0.");
        }

        return porcentaje.setScale(2, RoundingMode.HALF_UP);
    }

    private void cargarReporteServicio() {
        String servicio = (String) comboServicio.getSelectedItem();
        if (servicio == null) {
            servicio = "Desayuno";
        }

        detalleModel.setRowCount(0);
        List<AsistenciaRecord> registros = asistenciaModel.obtenerRegistrosPorServicio(servicio);
        for (AsistenciaRecord registro : registros) {
            detalleModel.addRow(new Object[] {
                formatter.format(registro.getFechaHora()),
                registro.getEmail(),
                registro.getCi(),
                registro.getTipoComensal(),
                registro.getMontoCobrado().setScale(2, RoundingMode.HALF_UP).toPlainString()
            });
        }

        resumenModel.setRowCount(0);
        Map<String, Integer> resumen = asistenciaModel.obtenerResumenPorServicio(servicio);
        for (Map.Entry<String, Integer> entry : resumen.entrySet()) {
            resumenModel.addRow(new Object[] {entry.getKey(), entry.getValue()});
        }
    }

    private void mostrarError(String mensaje) {
        lblEstadoBeneficio.setForeground(Color.RED);
        lblEstadoBeneficio.setText(mensaje);
        JOptionPane.showMessageDialog(this, mensaje, "Validacion", JOptionPane.ERROR_MESSAGE);
    }

    DefaultTableModel getDetalleModel() {
        return detalleModel;
    }

    DefaultTableModel getResumenModel() {
        return resumenModel;
    }

    JComboBox<String> getComboServicio() {
        return comboServicio;
    }
}