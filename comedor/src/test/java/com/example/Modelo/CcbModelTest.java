package com.example.Modelo;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CcbModelTest {

    @TempDir
    Path tempDir;

    private String originalUserDir;
    private CcbModel model;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());
        model = new CcbModel();
    }

    @AfterEach
    @SuppressWarnings("unused")
    void tearDown() {
        System.setProperty("user.dir", originalUserDir);
    }

    @Test
    void guardarYLeerRegistro() {
        CcbRecord record = crearRegistroEjemplo();

        boolean guardado = model.guardar(record);
        List<CcbRecord> registros = model.obtenerRegistros();

        assertTrue(guardado);
        assertEquals(1, registros.size());
        assertEquals(record.getFechaInicio(), registros.get(0).getFechaInicio());
        assertEquals(record.getCcb(), registros.get(0).getCcb());
    }

    @Test
    void obtenerRegistrosVacioCuandoNoExisteArchivo() {
        List<CcbRecord> registros = model.obtenerRegistros();

        assertEquals(0, registros.size());
    }

    private CcbRecord crearRegistroEjemplo() {
        LocalDate inicio = LocalDate.now();
        LocalDate fin = inicio.plusDays(2);
        BigDecimal cien = new BigDecimal("100.00");

        return new CcbRecord(
            inicio,
            fin,
            cien,
            cien,
            new BigDecimal("200"),
            new BigDecimal("5"),
            new BigDecimal("25"),
            new BigDecimal("80"),
            new BigDecimal("95"),
            new BigDecimal("120"),
            new BigDecimal("40"),
            new BigDecimal("40"),
            new BigDecimal("25"),
            new BigDecimal("8.50"),
            new BigDecimal("8.50"),
            new BigDecimal("15.00"),
            new BigDecimal("20.00"),
            new BigDecimal("300.00"),
            new BigDecimal("50.00"),
            new BigDecimal("70.00"),
            new BigDecimal("230.00"),
            new BigDecimal("0.00"),
            new BigDecimal("100"),
            new BigDecimal("100"),
            new BigDecimal("8.50"),
            new BigDecimal("8.50"),
            new BigDecimal("15.00"),
            new BigDecimal("15.00"),
            new BigDecimal("20.00"),
            new BigDecimal("20.00")
        );
    }
}
