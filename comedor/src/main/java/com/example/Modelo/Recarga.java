package com.example.Modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Recarga {
    private final LocalDateTime fecha;
    private final BigDecimal monto;

    public Recarga(LocalDateTime fecha, BigDecimal monto) {
        this.fecha = fecha;
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }
}
