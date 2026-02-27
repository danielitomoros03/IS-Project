package com.example.Modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TurnoTest {

    @Test
    void cupos_y_disponibles() {
        Turno turno = new Turno("t1", "12:00-13:00", "Almuerzo", 2, 1, "11:30");

        assertFalse(turno.estaLleno());
        assertEquals(1, turno.getDisponibles());

        turno.registrarCupo();
        assertTrue(turno.estaLleno());
        assertEquals(0, turno.getDisponibles());
    }
}
