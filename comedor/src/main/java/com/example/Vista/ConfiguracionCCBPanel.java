package com.example.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.example.Modelo.CcbModel;
import com.example.Modelo.CcbRecord;

public class ConfiguracionCCBPanel extends JPanel {

    private JTextField txtCostosFijos;
    private JTextField txtCostosVariables;
    private JTextField txtNumBandejas;
    private JTextField txtMerma;

    private JLabel lblResultadoCCB;

    private JButton btnCalcular;
    private JButton btnGuardar;

    private DefaultTableModel modeloTabla;

    private CcbRecord ultimoCalculo;
    private final CcbModel modelo = new CcbModel();

    private final Color COLOR_PRIMARY = new Color(34, 120, 64);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);

    public ConfiguracionCCBPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Configuracion del Costo Cubierto de Bandeja (CCB)");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(COLOR_PRIMARY);

        JLabel subtitle = new JLabel("Formula: CCB = ((CF + CV) / NB) * (1 + Merma/100)");
        subtitle.setForeground(Color.GRAY);

        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 6)));
        header.add(subtitle);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos para el calculo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(crearLabel("Costos Fijos (Bs):"), gbc);
        gbc.gridx = 1;
        txtCostosFijos = crearInput();
        formPanel.add(txtCostosFijos, gbc);

        gbc.gridx = 2;
        gbc.gridy = row;
        formPanel.add(crearLabel("Costos Variables (Bs):"), gbc);
        gbc.gridx = 3;
        txtCostosVariables = crearInput();
        formPanel.add(txtCostosVariables, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(crearLabel("NB (numero de bandejas):"), gbc);
        gbc.gridx = 1;
        txtNumBandejas = crearInput();
        formPanel.add(txtNumBandejas, gbc);

        gbc.gridx = 2;
        gbc.gridy = row;
        formPanel.add(crearLabel("Merma (%):"), gbc);
        gbc.gridx = 3;
        txtMerma = crearInput();
        formPanel.add(txtMerma, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 4;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);

        btnCalcular = new JButton("Calcular CCB");
        estilizarBoton(btnCalcular, false);

        btnGuardar = new JButton("Guardar calculo");
        estilizarBoton(btnGuardar, true);
        btnGuardar.setEnabled(false);

        buttonPanel.add(btnCalcular);
        buttonPanel.add(btnGuardar);
        formPanel.add(buttonPanel, gbc);

        row++;
        gbc.gridy = row;
        JPanel resultados = new JPanel(new GridLayout(1, 1, 10, 8));
        resultados.setBackground(Color.WHITE);
        lblResultadoCCB = crearValor("CCB: -");
        resultados.add(lblResultadoCCB);
        formPanel.add(resultados, gbc);

        JPanel resultadosWrapper = new JPanel(new BorderLayout());
        resultadosWrapper.setBackground(Color.WHITE);
        resultadosWrapper.setBorder(BorderFactory.createTitledBorder("Resultado"));
        resultadosWrapper.add(resultados, BorderLayout.CENTER);

        JPanel tablaWrapper = new JPanel(new BorderLayout());
        tablaWrapper.setBackground(Color.WHITE);
        tablaWrapper.setBorder(BorderFactory.createTitledBorder("Historico"));
        tablaWrapper.add(crearTablaHistorico(), BorderLayout.CENTER);

        content.add(formPanel);
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(resultadosWrapper);
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(tablaWrapper);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        btnCalcular.addActionListener(e -> calcularCCB());
        btnGuardar.addActionListener(e -> guardarCalculo());

        cargarHistorico();
    }

    private void calcularCCB() {
        try {
            CcbRecord record = construirRegistro();
            ultimoCalculo = record;

            lblResultadoCCB.setText("CCB: Bs " + formatear(record.getCcb()));
            btnGuardar.setEnabled(true);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarCalculo() {
        if (ultimoCalculo == null) {
            JOptionPane.showMessageDialog(this, "Primero calcula el CCB.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = modelo.guardar(ultimoCalculo);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Calculo guardado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            cargarHistorico();
            btnGuardar.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el calculo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarHistorico() {
        modeloTabla.setRowCount(0);
        List<CcbRecord> registros = modelo.obtenerRegistros();
        for (CcbRecord r : registros) {
            modeloTabla.addRow(new Object[] {
                "Bs " + formatear(r.getCostosFijos()),
                "Bs " + formatear(r.getCostosVariables()),
                formatear(r.getNbTotal()),
                formatear(r.getMerma()) + "%",
                "Bs " + formatear(r.getCcb())
            });
        }
    }

    private JScrollPane crearTablaHistorico() {
        String[] columnas = {
            "Costos Fijos", "Costos Variables", "NB", "Merma", "CCB"
        };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new EmptyBorder(10, 0, 0, 0));
        return scroll;
    }

    private CcbRecord construirRegistro() {
        BigDecimal cf = parseMonto(txtCostosFijos.getText(), "Costos Fijos");
        BigDecimal cv = parseMonto(txtCostosVariables.getText(), "Costos Variables");
        BigDecimal nb = parseMonto(txtNumBandejas.getText(), "NB");
        BigDecimal merma = parsePorcentaje(txtMerma.getText(), "Merma", BigDecimal.ZERO, new BigDecimal("100"));

        BigDecimal ccb = cf.add(cv)
            .divide(nb, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.ONE.add(merma.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)))
            .setScale(2, RoundingMode.HALF_UP);

        return new CcbRecord(cf, cv, nb, merma, ccb);
    }

    private BigDecimal parseMonto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Completa el campo: " + campo + ".");
        }

        String normalizado = texto.trim().replace(',', '.');
        BigDecimal valor;
        try {
            valor = new BigDecimal(normalizado);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Valor invalido en " + campo + ".");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(campo + " debe ser mayor a 0.");
        }
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal parsePorcentaje(String texto, String campo, BigDecimal min, BigDecimal max) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Completa el campo: " + campo + ".");
        }

        String normalizado = texto.trim().replace(',', '.');
        BigDecimal valor;
        try {
            valor = new BigDecimal(normalizado);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Valor invalido en " + campo + ".");
        }

        if (valor.compareTo(min) < 0 || valor.compareTo(max) > 0) {
            throw new IllegalArgumentException(campo + " debe estar entre " + min + " y " + max + ".");
        }
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        return lbl;
    }

    private JTextField crearInput() {
        JTextField txt = new JTextField(12);
        txt.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return txt;
    }

    private JLabel crearValor(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(COLOR_TEXT_DARK);
        return lbl;
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

    private String formatear(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}