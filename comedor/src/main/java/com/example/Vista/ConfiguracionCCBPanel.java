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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.example.Modelo.CcbModel;
import com.example.Modelo.CcbRecord;

public class ConfiguracionCCBPanel extends JPanel {

    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTextField txtCostosFijos;
    private JTextField txtCostosVariables;
    private JTextField txtNumBandejas;
    private JTextField txtMerma;
    private JComboBox<String> cmbTipoNb;
    private JTextField txtPctEst;
    private JTextField txtPctProf;
    private JTextField txtPctEmp;
    private JTextField txtNbEst;
    private JTextField txtNbProf;
    private JTextField txtNbEmp;
    private JTextField txtPctConcesionario;

    private JLabel lblResultadoCCB;
    private JLabel lblTarifaEst;
    private JLabel lblTarifaProf;
    private JLabel lblTarifaEmp;
    private JLabel lblIngresoTotal;
    private JLabel lblSubsidioEst;
    private JLabel lblIngresoConcesionario;
    private JLabel lblIngresoPropio;
    private JLabel lblExcedente;

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

        JLabel title = new JLabel("Configuración del Costo Cubierto de Bandeja (CCB)");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(COLOR_PRIMARY);
        JLabel subtitle = new JLabel("Periodo minimo 2 dias y maximo 31 dias (desde hoy en adelante).");
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
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del periodo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(crearLabel("Fecha inicio (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFechaInicio = crearInput();
        formPanel.add(txtFechaInicio, gbc);

        gbc.gridx = 2; gbc.gridy = row;
        formPanel.add(crearLabel("Fecha fin (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3;
        txtFechaFin = crearInput();
        formPanel.add(txtFechaFin, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(crearLabel("Costos Fijos (Bs):"), gbc);
        gbc.gridx = 1;
        txtCostosFijos = crearInput();
        formPanel.add(txtCostosFijos, gbc);

        gbc.gridx = 2; gbc.gridy = row;
        formPanel.add(crearLabel("Costos Variables (Bs):"), gbc);
        gbc.gridx = 3;
        txtCostosVariables = crearInput();
        formPanel.add(txtCostosVariables, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(crearLabel("NB (proyectadas o servidas):"), gbc);
        gbc.gridx = 1;
        txtNumBandejas = crearInput();
        formPanel.add(txtNumBandejas, gbc);

        gbc.gridx = 2; gbc.gridy = row;
        formPanel.add(crearLabel("Tipo NB:"), gbc);
        gbc.gridx = 3;
        cmbTipoNb = new JComboBox<>(new String[] {"Proyectado", "Servido"});
        formPanel.add(cmbTipoNb, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(crearLabel("Porcentaje de Merma (%):"), gbc);
        gbc.gridx = 1;
        txtMerma = crearInput();
        formPanel.add(txtMerma, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(crearLabel("% Estudiantes (20-30):"), gbc);
        gbc.gridx = 1;
        txtPctEst = crearInput();
        formPanel.add(txtPctEst, gbc);

        gbc.gridx = 2; gbc.gridy = row;
        formPanel.add(crearLabel("% Profesores (70-90):"), gbc);
        gbc.gridx = 3;
        txtPctProf = crearInput();
        formPanel.add(txtPctProf, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(crearLabel("% Empleados (90-110):"), gbc);
        gbc.gridx = 1;
        txtPctEmp = crearInput();
        formPanel.add(txtPctEmp, gbc);

        gbc.gridx = 2; gbc.gridy = row;
        formPanel.add(crearLabel("% Concesionario (25-30):"), gbc);
        gbc.gridx = 3;
        txtPctConcesionario = crearInput();
        formPanel.add(txtPctConcesionario, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(crearLabel("NB Estudiantes:"), gbc);
        gbc.gridx = 1;
        txtNbEst = crearInput();
        formPanel.add(txtNbEst, gbc);

        gbc.gridx = 2; gbc.gridy = row;
        formPanel.add(crearLabel("NB Profesores:"), gbc);
        gbc.gridx = 3;
        txtNbProf = crearInput();
        formPanel.add(txtNbProf, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(crearLabel("NB Empleados:"), gbc);
        gbc.gridx = 1;
        txtNbEmp = crearInput();
        formPanel.add(txtNbEmp, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 4;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);

        btnCalcular = new JButton("Calcular CCB");
        estilizarBoton(btnCalcular, false);

        btnGuardar = new JButton("Guardar Periodo");
        estilizarBoton(btnGuardar, true);
        btnGuardar.setEnabled(false);

        buttonPanel.add(btnCalcular);
        buttonPanel.add(btnGuardar);
        formPanel.add(buttonPanel, gbc);

        row++;
        gbc.gridy = row;
        JPanel resultados = new JPanel(new GridLayout(3, 3, 10, 8));
        resultados.setBackground(Color.WHITE);
        lblResultadoCCB = crearValor("CCB: -");
        lblTarifaEst = crearValor("Tarifa Estudiante: -");
        lblTarifaProf = crearValor("Tarifa Profesor: -");
        lblTarifaEmp = crearValor("Tarifa Empleado: -");
        lblIngresoTotal = crearValor("Ingreso Total: -");
        lblSubsidioEst = crearValor("Subsidio Estudiante: -");
        lblIngresoConcesionario = crearValor("Concesionario: -");
        lblIngresoPropio = crearValor("Ingreso Propio: -");
        lblExcedente = crearValor("Excedente: -");
        resultados.add(lblResultadoCCB);
        resultados.add(lblTarifaEst);
        resultados.add(lblTarifaProf);
        resultados.add(lblTarifaEmp);
        resultados.add(lblIngresoTotal);
        resultados.add(lblSubsidioEst);
        resultados.add(lblIngresoConcesionario);
        resultados.add(lblIngresoPropio);
        resultados.add(lblExcedente);
        formPanel.add(resultados, gbc);

        JPanel resultadosWrapper = new JPanel(new BorderLayout());
        resultadosWrapper.setBackground(Color.WHITE);
        resultadosWrapper.setBorder(BorderFactory.createTitledBorder("Resultados"));
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
        btnGuardar.addActionListener(e -> guardarPeriodo());

        cargarHistorico();
    }

    private void calcularCCB() {
        try {
            CcbRecord record = construirRegistro();
            ultimoCalculo = record;

            lblResultadoCCB.setText("CCB: Bs " + formatear(record.getCcb()));
            lblTarifaEst.setText("Tarifa Estudiante: Bs " + formatear(record.getTarifaEst()));
            lblTarifaProf.setText("Tarifa Profesor: Bs " + formatear(record.getTarifaProf()));
            lblTarifaEmp.setText("Tarifa Empleado: Bs " + formatear(record.getTarifaEmp()));
            lblIngresoTotal.setText("Ingreso Total: Bs " + formatear(record.getIngresoTotal()));
            lblSubsidioEst.setText("Subsidio Estudiante: Bs " + formatear(record.getSubsidioEst()));
            lblIngresoConcesionario.setText("Concesionario: Bs " + formatear(record.getIngresoConcesionario()));
            lblIngresoPropio.setText("Ingreso Propio: Bs " + formatear(record.getIngresoPropio()));
            lblExcedente.setText("Excedente: Bs " + formatear(record.getExcedente()));

            btnGuardar.setEnabled(true);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarPeriodo() {
        if (ultimoCalculo == null) {
            JOptionPane.showMessageDialog(this, "Primero calcula el CCB del periodo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = modelo.guardar(ultimoCalculo);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Periodo guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarHistorico();
            btnGuardar.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el periodo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarHistorico() {
        modeloTabla.setRowCount(0);
        List<CcbRecord> registros = modelo.obtenerRegistros();
        for (CcbRecord r : registros) {
            modeloTabla.addRow(new Object[] {
                r.getPeriodoTexto(),
                "Bs " + formatear(r.getCcb()),
                "Bs " + formatear(r.getTarifaEst()),
                "Bs " + formatear(r.getTarifaProf()),
                "Bs " + formatear(r.getTarifaEmp()),
                "Bs " + formatear(r.getIngresoTotal()),
                "Bs " + formatear(r.getSubsidioEst()),
                "Bs " + formatear(r.getIngresoConcesionario()),
                "Bs " + formatear(r.getIngresoPropio())
            });
        }
    }

    private JScrollPane crearTablaHistorico() {
        String[] columnas = {
            "Periodo", "CCB", "Tarifa Est", "Tarifa Prof", "Tarifa Emp",
            "Ingreso Total", "Subsidio Est", "Concesionario", "Ingreso Propio"
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
        LocalDate inicio = parseFecha(txtFechaInicio.getText(), "Fecha inicio");
        LocalDate fin = parseFecha(txtFechaFin.getText(), "Fecha fin");
        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio.");
        }

        LocalDate hoy = LocalDate.now();
        if (inicio.isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha inicio debe ser hoy o una fecha futura.");
        }

        String tipoNb = (String) cmbTipoNb.getSelectedItem();
        if ("Servido".equalsIgnoreCase(tipoNb) && !inicio.equals(hoy)) {
            throw new IllegalArgumentException("Si el periodo es Servido, la fecha inicio debe ser hoy.");
        }

        long dias = ChronoUnit.DAYS.between(inicio, fin) + 1;
        if (dias < 2 || dias > 31) {
            throw new IllegalArgumentException("El periodo debe ser entre 2 y 31 dias.");
        }

        validarSolapamiento(inicio, fin);

        BigDecimal cf = parseMonto(txtCostosFijos.getText(), "Costos Fijos");
        BigDecimal cv = parseMonto(txtCostosVariables.getText(), "Costos Variables");
        BigDecimal nb = parseMonto(txtNumBandejas.getText(), "NB");
        BigDecimal merma = parsePorcentaje(txtMerma.getText(), "Merma", new BigDecimal("0"), new BigDecimal("100"));

        BigDecimal pctEst = parsePorcentaje(txtPctEst.getText(), "% Estudiantes", new BigDecimal("20"), new BigDecimal("30"));
        BigDecimal pctProf = parsePorcentaje(txtPctProf.getText(), "% Profesores", new BigDecimal("70"), new BigDecimal("90"));
        BigDecimal pctEmp = parsePorcentaje(txtPctEmp.getText(), "% Empleados", new BigDecimal("90"), new BigDecimal("110"));
        BigDecimal pctConces = parsePorcentaje(txtPctConcesionario.getText(), "% Concesionario", new BigDecimal("25"), new BigDecimal("30"));

        BigDecimal nbEst = parseMonto(txtNbEst.getText(), "NB Estudiantes");
        BigDecimal nbProf = parseMonto(txtNbProf.getText(), "NB Profesores");
        BigDecimal nbEmp = parseMonto(txtNbEmp.getText(), "NB Empleados");

        BigDecimal suma = nbEst.add(nbProf).add(nbEmp);
        if (suma.compareTo(nb) != 0) {
            throw new IllegalArgumentException("La suma de NB por rol debe ser igual al NB total.");
        }

        BigDecimal ccb = cf.add(cv).divide(nb, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.ONE.add(merma.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)))
            .setScale(2, RoundingMode.HALF_UP);

        BigDecimal tarifaEst = ccb.multiply(pctEst).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal tarifaProf = ccb.multiply(pctProf).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal tarifaEmp = ccb.multiply(pctEmp).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        BigDecimal ingresoTotal = tarifaEst.multiply(nbEst)
            .add(tarifaProf.multiply(nbProf))
            .add(tarifaEmp.multiply(nbEmp))
            .setScale(2, RoundingMode.HALF_UP);

        BigDecimal subsidioEst = ccb.subtract(tarifaEst).max(BigDecimal.ZERO)
            .multiply(nbEst).setScale(2, RoundingMode.HALF_UP);

        BigDecimal excedente = tarifaProf.subtract(ccb).multiply(nbProf)
            .add(tarifaEmp.subtract(ccb).multiply(nbEmp))
            .setScale(2, RoundingMode.HALF_UP);

        BigDecimal ingresoConcesionario = ingresoTotal.multiply(pctConces)
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal ingresoPropio = ingresoTotal.subtract(ingresoConcesionario).setScale(2, RoundingMode.HALF_UP);

        return new CcbRecord(
            inicio, fin, cf, cv, nb, merma,
            pctEst, pctProf, pctEmp,
            nbEst, nbProf, nbEmp, pctConces,
            ccb, tarifaEst, tarifaProf, tarifaEmp,
            ingresoTotal, subsidioEst, ingresoConcesionario, ingresoPropio, excedente
        );
    }

    private LocalDate parseFecha(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Completa el campo: " + campo + ".");
        }
        try {
            return LocalDate.parse(texto.trim());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Formato invalido en " + campo + ". Usa YYYY-MM-DD.");
        }
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
        BigDecimal valor = parseMonto(texto, campo);
        if (valor.compareTo(min) < 0 || valor.compareTo(max) > 0) {
            throw new IllegalArgumentException(campo + " debe estar entre " + min + " y " + max + ".");
        }
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private void validarSolapamiento(LocalDate inicio, LocalDate fin) {
        List<CcbRecord> registros = modelo.obtenerRegistros();
        for (CcbRecord r : registros) {
            LocalDate rInicio = r.getFechaInicio();
            LocalDate rFin = r.getFechaFin();
            boolean solapa = !fin.isBefore(rInicio) && !inicio.isAfter(rFin);
            if (solapa) {
                throw new IllegalArgumentException("El periodo se solapa con uno ya registrado.");
            }
        }
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
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
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