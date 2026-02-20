package com.example.Modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MonederoModel {
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
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            archivo.createNewFile();
        }

        LocalDateTime fecha = LocalDateTime.now();
        String linea = email + "," + formatter.format(fecha) + "," + monto.setScale(2, RoundingMode.HALF_UP);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(linea);
            bw.newLine();
        }
    }
}
