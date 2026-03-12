package com.example.Controlador;

import java.awt.Frame;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JOptionPane;

import com.example.Modelo.MonederoModel;
import com.example.Modelo.Recarga;
import com.example.Modelo.RegUsuarioModelo;
import com.example.Vista.MonederoDialog;

public class MonederoControl {
    private static final BigDecimal MONTO_MIN = new BigDecimal("100.00");
    private static final BigDecimal MONTO_MAX = new BigDecimal("10000.00");
    private static final BigDecimal SALDO_PANA_MIN = new BigDecimal("1.00");

    private final MonederoModel modelo;
    private final RegUsuarioModelo regUsuarioModelo;
    private final MonederoDialog vista;
    private final String email;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public MonederoControl(Frame parent, String email) {
        this.modelo = new MonederoModel();
        this.regUsuarioModelo = new RegUsuarioModelo();
        this.vista = new MonederoDialog(parent);
        this.email = email;

        this.vista.getBtnRecargar().addActionListener(e -> procesarRecarga());
        this.vista.getBtnSaldoPana().addActionListener(e -> procesarSaldoPana());

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

    private void procesarSaldoPana() {
        String[] datosOrigen = regUsuarioModelo.obtenerNombreYRolDesdeArchivo(email);
        if (datosOrigen == null || datosOrigen.length < 2) {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudo validar tu rol en Secretaria para usar Saldo Pana.",
                "Operacion no permitida",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String rolOrigen = datosOrigen[1] == null ? "" : datosOrigen[1].toLowerCase();
        if (!rolOrigen.contains("estudiante")) {
            JOptionPane.showMessageDialog(
                vista,
                "Saldo Pana solo se permite entre estudiantes.",
                "Operacion no permitida",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String ciDestino = vista.getCiDestinoText();
        if (ciDestino == null || ciDestino.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Debes indicar la CI del estudiante destino.", "CI invalida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] datosDestino = regUsuarioModelo.obtenerDatosPorCiDesdeArchivo(ciDestino);
        if (datosDestino == null) {
            JOptionPane.showMessageDialog(vista, "La CI indicada no esta registrada en Secretaria.", "CI no encontrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rolDestino = datosDestino[2] == null ? "" : datosDestino[2].toLowerCase();
        if (!rolDestino.contains("estudiante")) {
            JOptionPane.showMessageDialog(vista, "Saldo Pana solo se permite entre estudiantes.", "Operacion no permitida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String emailDestino = datosDestino[0];
        if (emailDestino == null || emailDestino.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No se pudo determinar el correo del destino.", "Datos invalidos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal monto;
        try {
            monto = parseMonto(vista.getMontoText());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Monto invalido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (monto.compareTo(SALDO_PANA_MIN) < 0 || monto.compareTo(MONTO_MAX) > 0) {
            JOptionPane.showMessageDialog(
                vista,
                "El monto para Saldo Pana debe estar entre Bs 1.00 y Bs 10000.00",
                "Monto invalido",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            modelo.registrarSaldoPana(email, emailDestino, monto);
            vista.limpiarMonto();
            vista.limpiarCiDestino();
            cargarDatos();
            JOptionPane.showMessageDialog(vista, "Saldo Pana enviado con exito a " + emailDestino + ".");
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(vista, e.getMessage(), "Saldo insuficiente", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(vista, e.getMessage(), "Operacion invalida", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(vista, "No se pudo completar Saldo Pana.", "Error", JOptionPane.ERROR_MESSAGE);
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
