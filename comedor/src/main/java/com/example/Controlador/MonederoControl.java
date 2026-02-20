package com.example.Controlador;

import com.example.Modelo.MonederoModel;
import com.example.Modelo.Recarga;
import com.example.Vista.MonederoDialog;

import javax.swing.*;
import java.awt.Frame;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MonederoControl {
    private static final BigDecimal MONTO_MIN = new BigDecimal("100.00");
    private static final BigDecimal MONTO_MAX = new BigDecimal("10000.00");

    private final MonederoModel modelo;
    private final MonederoDialog vista;
    private final String email;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public MonederoControl(Frame parent, String email) {
        this.modelo = new MonederoModel();
        this.vista = new MonederoDialog(parent);
        this.email = email;

        this.vista.getBtnRecargar().addActionListener(e -> procesarRecarga());

        cargarDatos();
        this.vista.setVisible(true);
    }

    private void cargarDatos() {
        BigDecimal saldo = modelo.obtenerSaldo(email);
        vista.setSaldoText("Saldo: Bs " + formatearMonto(saldo));

        vista.limpiarHistorial();
        List<Recarga> historial = modelo.obtenerHistorial(email);
        for (Recarga recarga : historial) {
            vista.agregarFilaHistorial(new Object[] {
                formatter.format(recarga.getFecha()),
                formatearMonto(recarga.getMonto())
            });
        }
    }

    private void procesarRecarga() {
        String texto = vista.getMontoText();
        BigDecimal monto;

        try {
            monto = parseMonto(texto);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Monto invalido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (monto.compareTo(MONTO_MIN) < 0 || monto.compareTo(MONTO_MAX) > 0) {
            JOptionPane.showMessageDialog(
                vista,
                "El monto debe estar entre Bs 100.00 y Bs 10000.00",
                "Monto invalido",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            modelo.registrarRecarga(email, monto);
            vista.limpiarMonto();
            cargarDatos();
            JOptionPane.showMessageDialog(vista, "Recarga realizada con exito.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(vista, "No se pudo guardar la recarga.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BigDecimal parseMonto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingresa un monto para recargar.");
        }

        String normalizado = texto.trim().replace(',', '.');
        BigDecimal monto;
        try {
            monto = new BigDecimal(normalizado);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Formato de monto invalido.");
        }

        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }

        if (monto.scale() > 2) {
            throw new IllegalArgumentException("El monto solo permite hasta 2 decimales.");
        }

        return monto.setScale(2, RoundingMode.HALF_UP);
    }

    private String formatearMonto(BigDecimal monto) {
        return monto.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
