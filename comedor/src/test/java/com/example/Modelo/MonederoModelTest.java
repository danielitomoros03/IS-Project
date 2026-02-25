package com.example.Modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

public class MonederoModelTest {

    @TempDir
    Path tempDir;

    private String originalUserDir;
    private MonederoModel model;

    @BeforeEach
    void setUp() {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());
        model = new MonederoModel();
    }

    @AfterEach
    void tearDown() {
        System.setProperty("user.dir", originalUserDir);
    }

    @Test
    void registrarRecargaYObtenerSaldo() throws Exception {
        model.registrarRecarga("usuario@ucv.ve", new BigDecimal("10.00"));

        BigDecimal saldo = model.obtenerSaldo("usuario@ucv.ve");

        assertEquals(new BigDecimal("10.00"), saldo);
    }

    @Test
    void registrarCobroReduceSaldo() throws Exception {
        model.registrarRecarga("usuario@ucv.ve", new BigDecimal("10.00"));
        model.registrarCobro("usuario@ucv.ve", new BigDecimal("3.50"));

        BigDecimal saldo = model.obtenerSaldo("usuario@ucv.ve");

        assertEquals(new BigDecimal("6.50"), saldo);
    }
}
