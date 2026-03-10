package com.example.Modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class AsistenciaRecord {
    private final LocalDateTime fechaHora;
    private final String servicio;
    private final String email;
    private final String ci;
    private final String tipoComensal;
    private final BigDecimal montoCobrado;

    public AsistenciaRecord(
        LocalDateTime fechaHora,
        String servicio,
        String email,
        String ci,
        String tipoComensal,
        BigDecimal montoCobrado
    ) {
        this.fechaHora = fechaHora;
        this.servicio = servicio;
        this.email = email;
        this.ci = ci;
        this.tipoComensal = tipoComensal;
        this.montoCobrado = montoCobrado == null
            ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
            : montoCobrado.setScale(2, RoundingMode.HALF_UP);
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getServicio() {
        return servicio;
    }

    public String getEmail() {
        return email;
    }

    public String getCi() {
        return ci;
    }

    public String getTipoComensal() {
        return tipoComensal;
    }

    public BigDecimal getMontoCobrado() {
        return montoCobrado;
    }
}
