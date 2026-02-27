package com.example.Modelo;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class MenuRecordTest {

    @Test
    void getEstado_devuelveSegunFecha() {
        LocalDate hoy = LocalDate.now();
        MenuRecord pasado = new MenuRecord(hoy.minusDays(1), "Almuerzo", Arrays.asList("A"));
        MenuRecord actual = new MenuRecord(hoy, "Almuerzo", Arrays.asList("A"));
        MenuRecord futuro = new MenuRecord(hoy.plusDays(1), "Almuerzo", Arrays.asList("A"));

        assertEquals("Vencido", pasado.getEstado(hoy));
        assertEquals("Activo", actual.getEstado(hoy));
        assertEquals("Programado", futuro.getEstado(hoy));
    }

    @Test
    void csv_roundTrip_conPlatos() {
        MenuRecord record = new MenuRecord("id1", LocalDate.of(2026, 2, 26), "Desayuno", Arrays.asList("A", "B"));
        String csv = record.toCsvLine();

        MenuRecord parsed = MenuRecord.fromCsv(csv);
        assertNotNull(parsed);
        assertEquals("id1", parsed.getId());
        assertEquals(LocalDate.of(2026, 2, 26), parsed.getFecha());
        assertEquals("Desayuno", parsed.getTurno());
        assertEquals("A, B", parsed.getPlatosTexto());
    }

    @Test
    void fromCsv_invalido_devuelveNull() {
        MenuRecord parsed = MenuRecord.fromCsv("solo,dos,campos");
        assertNull(parsed);
    }
}
