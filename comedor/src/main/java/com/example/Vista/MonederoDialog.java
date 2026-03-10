package com.example.Vista;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MonederoDialog extends JDialog {
    private JLabel lblSaldo;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private JTextField txtMonto;
    private JTextField txtCiDestino;
    private JButton btnRecargar;
    private JButton btnSaldoPana;

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

        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));

        JPanel formMonto = new JPanel(new BorderLayout(10, 0));
        JLabel lblMonto = new JLabel("Monto (Bs)");
        txtMonto = new JTextField();
        formMonto.add(lblMonto, BorderLayout.WEST);
        formMonto.add(txtMonto, BorderLayout.CENTER);

        JPanel formCi = new JPanel(new BorderLayout(10, 0));
        formCi.setBorder(new EmptyBorder(8, 0, 0, 0));
        JLabel lblCi = new JLabel("CI destino (Saldo Pana)");
        txtCiDestino = new JTextField();
        formCi.add(lblCi, BorderLayout.WEST);
        formCi.add(txtCiDestino, BorderLayout.CENTER);

        formContainer.add(formMonto);
        formContainer.add(formCi);

        btnRecargar = new JButton("Recargar");
        btnRecargar.setFocusPainted(false);
        btnSaldoPana = new JButton("Saldo Pana");
        btnSaldoPana.setFocusPainted(false);

        JPanel actions = new JPanel(new GridLayout(2, 1, 0, 8));
        actions.add(btnRecargar);
        actions.add(btnSaldoPana);

        footer.add(formContainer, BorderLayout.CENTER);
        footer.add(actions, BorderLayout.EAST);

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

    public String getCiDestinoText() {
        return txtCiDestino.getText();
    }

    public void limpiarCiDestino() {
        txtCiDestino.setText("");
    }

    public JButton getBtnRecargar() {
        return btnRecargar;
    }

    public JButton getBtnSaldoPana() {
        return btnSaldoPana;
    }
}
