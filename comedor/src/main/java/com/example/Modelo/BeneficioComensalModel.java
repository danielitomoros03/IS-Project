package com.example.Modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeneficioComensalModel {
    private static final BigDecimal PORCENTAJE_REGULAR = new BigDecimal("100.00");

    private final String nombreArchivo = "Beneficios_Comensal.txt";
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void registrarExonerado(String ci) throws IOException {
        registrarBeneficio(ci, BeneficioComensal.TIPO_EXONERADO, BigDecimal.ZERO);
    }

    public void registrarRegular(String ci) throws IOException {
        registrarBeneficio(ci, BeneficioComensal.TIPO_REGULAR, PORCENTAJE_REGULAR);
    }

    public void registrarBecario(String ci, BigDecimal porcentajeCobro) throws IOException {
        if (porcentajeCobro == null) {
            throw new IllegalArgumentException("Debes indicar el porcentaje de descuento para becario.");
        }

        BigDecimal porcentajeNormalizado = porcentajeCobro.setScale(2, RoundingMode.HALF_UP);
        if (porcentajeNormalizado.compareTo(BigDecimal.ZERO) <= 0
            || porcentajeNormalizado.compareTo(PORCENTAJE_REGULAR) >= 0) {
            throw new IllegalArgumentException("El porcentaje de descuento del becario debe ser mayor a 0 y menor a 100.");
        }

        registrarBeneficio(ci, BeneficioComensal.TIPO_BECARIO, porcentajeNormalizado);
    }

    public BeneficioComensal obtenerBeneficioPorCi(String ci) {
        String ciNormalizada = normalizarCi(ci);
        if (ciNormalizada.isEmpty()) {
            return null;
        }

        File archivo = resolveArchivo();
        if (!archivo.exists()) {
            return null;
        }

        BeneficioComensal beneficio = null;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 3) {
                    continue;
                }

                String ciArchivo = normalizarCi(datos[0]);
                if (!ciNormalizada.equals(ciArchivo)) {
                    continue;
                }

                String tipo = datos[1].trim().toUpperCase();
                BigDecimal porcentaje;
                try {
                    porcentaje = new BigDecimal(datos[2].trim());
                } catch (NumberFormatException ex) {
                    continue;
                }

                beneficio = new BeneficioComensal(ciArchivo, tipo, porcentaje);
            }
        } catch (IOException e) {
            System.err.println("Error al leer Beneficios_Comensal.txt: " + e.getMessage());
        }

        return beneficio;
    }

    public BeneficioComensal obtenerBeneficioPorEmail(String email) {
        RegUsuarioModelo regUsuarioModelo = new RegUsuarioModelo();
        String ci = regUsuarioModelo.obtenerCiPorEmailDesdeArchivo(email);
        if (ci == null || ci.trim().isEmpty()) {
            return null;
        }
        return obtenerBeneficioPorCi(ci);
    }

    public Map<String, BeneficioComensal> obtenerBeneficiosVigentes() {
        Map<String, BeneficioComensal> mapa = new HashMap<>();
        File archivo = resolveArchivo();
        if (!archivo.exists()) {
            return mapa;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 3) {
                    continue;
                }

                String ci = normalizarCi(datos[0]);
                if (ci.isEmpty()) {
                    continue;
                }

                String tipo = datos[1].trim().toUpperCase();
                BigDecimal porcentaje;
                try {
                    porcentaje = new BigDecimal(datos[2].trim());
                } catch (NumberFormatException ex) {
                    continue;
                }

                mapa.put(ci, new BeneficioComensal(ci, tipo, porcentaje));
            }
        } catch (IOException e) {
            System.err.println("Error al leer Beneficios_Comensal.txt: " + e.getMessage());
        }

        return mapa;
    }

    public String normalizarCi(String ci) {
        if (ci == null) {
            return "";
        }
        return ci.replaceAll("[^0-9]", "").trim();
    }

    private void registrarBeneficio(String ci, String tipo, BigDecimal porcentajeCobro) throws IOException {
        String ciNormalizada = normalizarCi(ci);
        if (ciNormalizada.isEmpty()) {
            throw new IllegalArgumentException("Debes indicar una CI valida.");
        }

        String tipoNormalizado = tipo == null ? BeneficioComensal.TIPO_REGULAR : tipo.trim().toUpperCase();
        BigDecimal porcentaje = porcentajeCobro == null
            ? PORCENTAJE_REGULAR
            : porcentajeCobro.setScale(2, RoundingMode.HALF_UP);

        File archivo = resolveArchivo();
        if (!archivo.exists()) {
            archivo.createNewFile();
        }

        String linea = String.join(",",
            ciNormalizada,
            tipoNormalizado,
            porcentaje.toPlainString(),
            formatter.format(LocalDateTime.now())
        );

        List<String> lineasActualizadas = new ArrayList<>();
        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String lineaActual;
                while ((lineaActual = br.readLine()) != null) {
                    if (esLineaDeLaMismaCi(lineaActual, ciNormalizada)) {
                        continue;
                    }
                    lineasActualizadas.add(lineaActual);
                }
            }
        }

        lineasActualizadas.add(linea);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
            for (String lineaActualizada : lineasActualizadas) {
                bw.write(lineaActualizada);
                bw.newLine();
            }
        }
    }

    private boolean esLineaDeLaMismaCi(String linea, String ciNormalizada) {
        if (linea == null || linea.isBlank()) {
            return false;
        }

        String[] datos = linea.split(",", 2);
        if (datos.length == 0) {
            return false;
        }

        return ciNormalizada.equals(normalizarCi(datos[0]));
    }

    private File resolveArchivo() {
        Path base = Paths.get("").toAbsolutePath();
        Path baseInicial = base;

        for (int i = 0; i < 4; i++) {
            Path directo = base.resolve(nombreArchivo);
            File archivo = directo.toFile();
            if (archivo.exists()) {
                return archivo;
            }

            Path alterno = base.resolve("comedor").resolve(nombreArchivo);
            File archivoAlterno = alterno.toFile();
            if (archivoAlterno.exists()) {
                return archivoAlterno;
            }

            Path parent = base.getParent();
            if (parent == null) {
                break;
            }
            base = parent;
        }

        File carpetaComedor = baseInicial.resolve("comedor").toFile();
        if (carpetaComedor.exists() && carpetaComedor.isDirectory()) {
            return baseInicial.resolve("comedor").resolve(nombreArchivo).toFile();
        }

        return baseInicial.resolve(nombreArchivo).toFile();
    }
}
