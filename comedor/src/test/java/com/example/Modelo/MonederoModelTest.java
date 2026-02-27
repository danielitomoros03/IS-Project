package com.example.Modelo;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MonederoModelTest {

    private Path monederoPath;
    private byte[] backup;
    private boolean hadBackup;

    @BeforeEach
    void setUp() throws IOException {
        monederoPath = Paths.get("").toAbsolutePath().resolve("Monedero.txt");
        if (Files.exists(monederoPath)) {
            backup = Files.readAllBytes(monederoPath);
            hadBackup = true;
        }
        Files.deleteIfExists(monederoPath);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(monederoPath);
        if (hadBackup) {
            Files.write(monederoPath, backup);
        }
    }

    @Test
    void obtenerHistorial_archivoInexistente_devuelveVacio() {
        MonederoModel model = new MonederoModel();
        List<Recarga> historial = model.obtenerHistorial("user@ucv.ve");
        assertEquals(0, historial.size());
    }

    @Test
    void registrarRecarga_y_obtenerSaldo() throws IOException {
        MonederoModel model = new MonederoModel();
        model.registrarRecarga("user@ucv.ve", new BigDecimal("100.00"));
        model.registrarRecarga("user@ucv.ve", new BigDecimal("25.50"));
        model.registrarRecarga("otro@ucv.ve", new BigDecimal("99.99"));

        BigDecimal saldo = model.obtenerSaldo("user@ucv.ve");
        assertEquals(new BigDecimal("125.50"), saldo);
    }

    @Test
    void registrarCobro_descuentaSaldo() throws IOException {
        MonederoModel model = new MonederoModel();
        model.registrarRecarga("user@ucv.ve", new BigDecimal("200.00"));
        model.registrarCobro("user@ucv.ve", new BigDecimal("40.25"));

        BigDecimal saldo = model.obtenerSaldo("user@ucv.ve");
        assertEquals(new BigDecimal("159.75"), saldo);
    }

    @Test
    void registrarCobro_montoNulo_lanzaError() {
        MonederoModel model = new MonederoModel();
        assertThrows(IllegalArgumentException.class, () -> model.registrarCobro("user@ucv.ve", null));
    }

    @Test
    void registrarRecarga_creaArchivo() throws IOException {
        MonederoModel model = new MonederoModel();
        model.registrarRecarga("user@ucv.ve", new BigDecimal("10.00"));
        assertEquals(true, Files.exists(monederoPath));
    }
}
