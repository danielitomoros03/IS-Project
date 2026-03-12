package com.example.Vista;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class RegistroTurnoPanelTest {

    @Test
    void calcularMontoBecarioConDescuento_aplicaDescuentoCorrectamente() {
        BigDecimal monto = RegistroTurnoPanel.calcularMontoBecarioConDescuento(
            new BigDecimal("100.00"),
            new BigDecimal("5.00")
        );

        assertEquals(new BigDecimal("95.00"), monto);
    }

    @Test
    void calcularMontoBecarioConDescuento_descuentoAlto_limitaEnCero() {
        BigDecimal monto = RegistroTurnoPanel.calcularMontoBecarioConDescuento(
            new BigDecimal("80.00"),
            new BigDecimal("120.00")
        );

        assertEquals(new BigDecimal("0.00"), monto);
    }
}
