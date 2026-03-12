package com.example.Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MonederoModel {
    private static final Object FILE_LOCK = new Object();
    private final String nombreArchivo = "Monedero.txt";
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public BigDecimal obtenerSaldo(String email) {
        BigDecimal saldo = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        for (Recarga recarga : obtenerHistorial(email)) {
            saldo = saldo.add(recarga.getMonto());
        }
        return saldo;
    }

    public List<Recarga> obtenerHistorial(String email) {
        List<Recarga> historial = new ArrayList<>();
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            return historial;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 3) {
                    continue;
                }
                String emailArchivo = datos[0].trim();
                if (!emailArchivo.equalsIgnoreCase(email)) {
                    continue;
                }
                LocalDateTime fecha = LocalDateTime.parse(datos[1].trim(), formatter);
                BigDecimal monto = new BigDecimal(datos[2].trim()).setScale(2, RoundingMode.HALF_UP);
                historial.add(new Recarga(fecha, monto));
            }
        } catch (IOException | RuntimeException e) {
            System.err.println("Error al leer Monedero.txt: " + e.getMessage());
        }

        return historial;
    }

    public void registrarRecarga(String email, BigDecimal monto) throws IOException {
        BigDecimal montoNormalizado = validarMontoPositivo(monto);
        registrarMovimiento(email, montoNormalizado);
    }

    public void registrarCobro(String email, BigDecimal monto) throws IOException {
        BigDecimal montoNormalizado = validarMontoPositivo(monto);
        registrarMovimiento(email, montoNormalizado.negate());
    }

    public void registrarSaldoPana(String emailOrigen, String emailDestino, BigDecimal monto) throws IOException {
        String emailOrigenNormalizado = validarEmail(emailOrigen, "Email de origen invalido.");
        String emailDestinoNormalizado = validarEmail(emailDestino, "Email destino invalido.");

        if (emailOrigenNormalizado.equalsIgnoreCase(emailDestinoNormalizado)) {
            throw new IllegalArgumentException("No puedes transferirte saldo a ti mismo.");
        }

        BigDecimal montoNormalizado = validarMontoPositivo(monto);

        synchronized (FILE_LOCK) {
            BigDecimal saldoOrigen = obtenerSaldo(emailOrigenNormalizado);
            if (saldoOrigen.compareTo(montoNormalizado) < 0) {
                throw new IllegalStateException("Saldo insuficiente para realizar Saldo Pana.");
            }

            File archivo = new File(nombreArchivo);
            asegurarArchivoExiste(archivo);
            LocalDateTime fecha = LocalDateTime.now();

            List<String> movimientos = new ArrayList<>();
            movimientos.add(construirLineaMovimiento(emailOrigenNormalizado, fecha, montoNormalizado.negate()));
            movimientos.add(construirLineaMovimiento(emailDestinoNormalizado, fecha, montoNormalizado));

            registrarMovimientosAtomicos(archivo, movimientos);
        }
    }

    private void registrarMovimiento(String email, BigDecimal monto) throws IOException {
        String emailNormalizado = validarEmail(email, "Email invalido.");

        synchronized (FILE_LOCK) {
            File archivo = new File(nombreArchivo);
            asegurarArchivoExiste(archivo);

            LocalDateTime fecha = LocalDateTime.now();
            List<String> movimientos = new ArrayList<>();
            movimientos.add(construirLineaMovimiento(emailNormalizado, fecha, monto));

            registrarMovimientosAtomicos(archivo, movimientos);
        }
    }

    protected void registrarMovimientosAtomicos(File archivo, List<String> lineasNuevas) throws IOException {
        if (lineasNuevas == null || lineasNuevas.isEmpty()) {
            return;
        }

        Path archivoPath = archivo.toPath();
        Path parent = archivoPath.getParent();
        if (parent == null) {
            parent = new File(".").toPath().toAbsolutePath().normalize();
        }

        List<String> lineasTotales = new ArrayList<>();
        if (Files.exists(archivoPath)) {
            lineasTotales.addAll(Files.readAllLines(archivoPath, StandardCharsets.UTF_8));
        }
        lineasTotales.addAll(lineasNuevas);

        Path temporalPath = Files.createTempFile(parent, "monedero", ".tmp");
        try {
            Files.write(
                temporalPath,
                lineasTotales,
                StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING
            );
            try {
                Files.move(temporalPath, archivoPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporalPath, archivoPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporalPath);
        }
    }

    private String construirLineaMovimiento(String email, LocalDateTime fecha, BigDecimal monto) {
        return email + "," + formatter.format(fecha) + "," + monto.setScale(2, RoundingMode.HALF_UP);
    }

    private void asegurarArchivoExiste(File archivo) throws IOException {
        if (archivo.exists()) {
            return;
        }

        File parent = archivo.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta del monedero.");
        }

        archivo.createNewFile();
    }

    private String validarEmail(String email, String mensajeError) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
        return email.trim();
    }

    private BigDecimal validarMontoPositivo(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto invalido.");
        }
        return monto.setScale(2, RoundingMode.HALF_UP);
    }
}
