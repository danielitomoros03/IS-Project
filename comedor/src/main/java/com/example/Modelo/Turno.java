package com.example.Modelo;

import java.time.LocalTime;

public class Turno {
    private String id;
    private String rangoHorario; // Ej: "12:00 PM - 1:00 PM"
    private int capacidadTotal;
    private int cuposOcupados;
    private LocalTime horaLimiteReserva; // Hora tope para reservar este turno

    public Turno(String id, String rangoHorario, int capacidadTotal, int cuposOcupados, String horaLimiteStr) {
        this.id = id;
        this.rangoHorario = rangoHorario;
        this.capacidadTotal = capacidadTotal;
        this.cuposOcupados = cuposOcupados;
        // Formato esperado "HH:mm" (24 horas)
        this.horaLimiteReserva = LocalTime.parse(horaLimiteStr);
    }

    public boolean estaLleno() {
        return cuposOcupados >= capacidadTotal;
    }

    // Aumentar contador (simulación de base de datos)
    public void registrarCupo() {
        this.cuposOcupados++;
    }

    // Getters
    public String getRangoHorario() { return rangoHorario; }
    public int getCapacidadTotal() { return capacidadTotal; }
    public int getCuposOcupados() { return cuposOcupados; }
    public LocalTime getHoraLimiteReserva() { return horaLimiteReserva; }
    public int getDisponibles() { return capacidadTotal - cuposOcupados; }
}