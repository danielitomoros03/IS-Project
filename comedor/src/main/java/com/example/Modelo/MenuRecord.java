package com.example.Modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MenuRecord {
    private final String id;
    private final LocalDate fecha;
    private final String turno;
    private final List<String> platos;

    public MenuRecord(LocalDate fecha, String turno, List<String> platos) {
        this(UUID.randomUUID().toString(), fecha, turno, platos);
    }

    public MenuRecord(String id, LocalDate fecha, String turno, List<String> platos) {
        this.id = id;
        this.fecha = fecha;
        this.turno = turno;
        this.platos = new ArrayList<>(platos);
    }

    public String getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getTurno() {
        return turno;
    }

    public List<String> getPlatos() {
        return new ArrayList<>(platos);
    }

    public String getEstado(LocalDate hoy) {
        if (fecha.isBefore(hoy)) {
            return "Vencido";
        }
        if (fecha.isEqual(hoy)) {
            return "Activo";
        }
        return "Programado";
    }

    public String getPlatosTexto() {
        return String.join(", ", platos);
    }

    public String toCsvLine() {
        String platosCsv = String.join("|", platos);
        return String.join(",",
            id,
            fecha.format(DateTimeFormatter.ISO_LOCAL_DATE),
            turno,
            platosCsv
        );
    }

    public static MenuRecord fromCsv(String linea) {
        String[] d = linea.split(",", -1);
        if (d.length < 4) {
            return null;
        }
        String id = d[0].trim();
        LocalDate fecha = LocalDate.parse(d[1].trim());
        String turno = d[2].trim();
        List<String> platos = new ArrayList<>();
        String platosRaw = d[3].trim();
        if (!platosRaw.isEmpty()) {
            platos.addAll(Arrays.asList(platosRaw.split("\\|")));
        }
        return new MenuRecord(id, fecha, turno, platos);
    }
}
