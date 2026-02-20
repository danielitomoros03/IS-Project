package com.example.Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MonederoDialog extends JDialog {
    private JLabel lblSaldo;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private JTextField txtMonto;
    private JButton btnRecargar;

    public MonederoDialog(Frame owner) {
        super(owner, "Monedero Digital", true);
        setSize(520, 420);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(15, 20, 10, 20));
        JLabel titulo = new JLabel("Monedero Digital");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.add(titulo, BorderLayout.WEST);

        lblSaldo = new JLabel("Saldo: Bs 0.00");
        lblSaldo.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.add(lblSaldo, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[] {"Fecha", "Monto (Bs)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaHistorial = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.setBorder(new EmptyBorder(0, 20, 0, 20));
        add(scroll, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(10, 20, 20, 20));

        JPanel form = new JPanel(new BorderLayout(10, 0));
        JLabel lblMonto = new JLabel("Monto a recargar (Bs)");
        txtMonto = new JTextField();
        form.add(lblMonto, BorderLayout.WEST);
        form.add(txtMonto, BorderLayout.CENTER);

        btnRecargar = new JButton("Recargar");
        btnRecargar.setFocusPainted(false);

        footer.add(form, BorderLayout.CENTER);
        footer.add(btnRecargar, BorderLayout.EAST);

        add(footer, BorderLayout.SOUTH);
    }

    public void setSaldoText(String texto) {
        lblSaldo.setText(texto);
    }

    public void limpiarHistorial() {
        modeloTabla.setRowCount(0);
    }

    public void agregarFilaHistorial(Object[] fila) {
        modeloTabla.addRow(fila);
    }

    public String getMontoText() {
        return txtMonto.getText();
    }

    public void limpiarMonto() {
        txtMonto.setText("");
    }

    public JButton getBtnRecargar() {
        return btnRecargar;
    }
}
