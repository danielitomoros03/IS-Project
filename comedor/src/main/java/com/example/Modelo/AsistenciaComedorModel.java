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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AsistenciaComedorModel {
    private final String nombreArchivo = "Asistencias_Comedor.txt";
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public boolean registrarAsistencia(AsistenciaRecord record) {
        if (record == null) {
            return false;
        }

        File archivo = resolveArchivo();
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            String monto = record.getMontoCobrado().setScale(2, RoundingMode.HALF_UP).toPlainString();
            String linea = String.join(",",
                formatter.format(record.getFechaHora()),
                sanitizar(record.getServicio()),
                sanitizar(record.getEmail()),
                sanitizar(record.getCi()),
                sanitizar(record.getTipoComensal()),
                monto
            );

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
                bw.write(linea);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar Asistencias_Comedor.txt: " + e.getMessage());
            return false;
        }
    }

    public List<AsistenciaRecord> obtenerRegistros() {
        List<AsistenciaRecord> registros = new ArrayList<>();
        File archivo = resolveArchivo();
        if (!archivo.exists()) {
            return registros;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 6) {
                    continue;
                }

                try {
                    registros.add(new AsistenciaRecord(
                        LocalDateTime.parse(datos[0].trim(), formatter),
                        datos[1].trim(),
                        datos[2].trim(),
                        datos[3].trim(),
                        datos[4].trim(),
                        new BigDecimal(datos[5].trim())
                    ));
                } catch (RuntimeException ex) {
                    // Saltar lineas mal formadas para no romper el resto del reporte.
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer Asistencias_Comedor.txt: " + e.getMessage());
        }

        return registros;
    }

    public List<AsistenciaRecord> obtenerRegistrosPorServicio(String servicio) {
        List<AsistenciaRecord> todos = obtenerRegistros();
        List<AsistenciaRecord> filtrados = new ArrayList<>();
        String filtro = servicio == null ? "" : servicio.trim().toLowerCase();

        for (AsistenciaRecord registro : todos) {
            String servicioRegistro = registro.getServicio() == null ? "" : registro.getServicio().trim().toLowerCase();
            if (servicioRegistro.equals(filtro)) {
                filtrados.add(registro);
            }
        }

        return filtrados;
    }

    public Map<String, Integer> obtenerResumenPorServicio(String servicio) {
        Map<String, Integer> resumen = new LinkedHashMap<>();
        resumen.put("Estudiante Regular", 0);
        resumen.put("Estudiante Becario", 0);
        resumen.put("Estudiante Exonerado", 0);
        resumen.put("Profesor", 0);
        resumen.put("Empleado", 0);
        resumen.put("Otro", 0);

        List<AsistenciaRecord> registros = obtenerRegistrosPorServicio(servicio);
        for (AsistenciaRecord registro : registros) {
            String categoria = normalizarCategoria(registro.getTipoComensal());
            resumen.put(categoria, resumen.get(categoria) + 1);
        }

        int total = 0;
        for (Integer valor : resumen.values()) {
            total += valor;
        }
        resumen.put("Total", total);

        return resumen;
    }

    private String normalizarCategoria(String tipoComensal) {
        if (tipoComensal == null) {
            return "Otro";
        }

        String valor = tipoComensal.trim().toLowerCase();
        if (valor.contains("estudiante") && valor.contains("regular")) {
            return "Estudiante Regular";
        }
        if (valor.contains("estudiante") && valor.contains("becario")) {
            return "Estudiante Becario";
        }
        if (valor.contains("estudiante") && valor.contains("exonerado")) {
            return "Estudiante Exonerado";
        }
        if (valor.contains("profesor")) {
            return "Profesor";
        }
        if (valor.contains("empleado")) {
            return "Empleado";
        }

        return "Otro";
    }

    private String sanitizar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace(",", " ").trim();
    }

    private File resolveArchivo() {
        Path base = Paths.get("").toAbsolutePath();

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

        return Paths.get(nombreArchivo).toFile();
    }
}
