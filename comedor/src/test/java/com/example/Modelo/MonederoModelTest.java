package com.example.Modelo;

import java.io.File;
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
    public void setUp() throws IOException {
        monederoPath = Paths.get("").toAbsolutePath().resolve("Monedero.txt");
        if (Files.exists(monederoPath)) {
            backup = Files.readAllBytes(monederoPath);
            hadBackup = true;
        }
        Files.deleteIfExists(monederoPath);
    }

    @AfterEach
    public void tearDown() throws IOException {
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
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> model.registrarCobro("user@ucv.ve", null)
        );
        assertEquals("Monto invalido.", ex.getMessage());
    }

    @Test
    void registrarRecarga_creaArchivo() throws IOException {
        MonederoModel model = new MonederoModel();
        model.registrarRecarga("user@ucv.ve", new BigDecimal("10.00"));
        assertEquals(true, Files.exists(monederoPath));
    }

    @Test
    void registrarSaldoPana_transfiereSaldoEntreEstudiantes() throws IOException {
        MonederoModel model = new MonederoModel();
        model.registrarRecarga("origen@ucv.ve", new BigDecimal("300.00"));

        model.registrarSaldoPana("origen@ucv.ve", "destino@ucv.ve", new BigDecimal("50.00"));

        assertEquals(new BigDecimal("250.00"), model.obtenerSaldo("origen@ucv.ve"));
        assertEquals(new BigDecimal("50.00"), model.obtenerSaldo("destino@ucv.ve"));
    }

    @Test
    void registrarSaldoPana_saldoInsuficiente_lanzaError() throws IOException {
        MonederoModel model = new MonederoModel();
        model.registrarRecarga("origen@ucv.ve", new BigDecimal("10.00"));

        IllegalStateException ex = assertThrows(
            IllegalStateException.class,
            () -> model.registrarSaldoPana("origen@ucv.ve", "destino@ucv.ve", new BigDecimal("50.00"))
        );
        assertEquals("Saldo insuficiente para realizar Saldo Pana.", ex.getMessage());
    }

    @Test
    void registrarSaldoPana_mismoEmail_lanzaError() throws IOException {
        MonederoModel model = new MonederoModel();
        model.registrarRecarga("origen@ucv.ve", new BigDecimal("100.00"));

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> model.registrarSaldoPana("origen@ucv.ve", "origen@ucv.ve", new BigDecimal("10.00"))
        );
        assertEquals("No puedes transferirte saldo a ti mismo.", ex.getMessage());
    }

    @Test
    void registrarSaldoPana_montoNoPositivo_lanzaError() throws IOException {
        MonederoModel model = new MonederoModel();
        model.registrarRecarga("origen@ucv.ve", new BigDecimal("100.00"));

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> model.registrarSaldoPana("origen@ucv.ve", "destino@ucv.ve", BigDecimal.ZERO)
        );
        assertEquals("Monto invalido.", ex.getMessage());
    }

    @Test
    void registrarSaldoPana_montoNegativo_lanzaError() throws IOException {
        MonederoModel model = new MonederoModel();
        model.registrarRecarga("origen@ucv.ve", new BigDecimal("100.00"));

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> model.registrarSaldoPana("origen@ucv.ve", "destino@ucv.ve", new BigDecimal("-1.00"))
        );
        assertEquals("Monto invalido.", ex.getMessage());
    }

    @Test
    void registrarSaldoPana_origenInvalido_lanzaError() {
        MonederoModel model = new MonederoModel();

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> model.registrarSaldoPana("   ", "destino@ucv.ve", new BigDecimal("10.00"))
        );
        assertEquals("Email de origen invalido.", ex.getMessage());
    }

    @Test
    void registrarSaldoPana_destinoInvalido_lanzaError() {
        MonederoModel model = new MonederoModel();

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> model.registrarSaldoPana("origen@ucv.ve", "", new BigDecimal("10.00"))
        );
        assertEquals("Email destino invalido.", ex.getMessage());
    }

    @Test
    void registrarSaldoPana_mismoEmail_conMayusculasYEspacios_lanzaError() throws IOException {
        MonederoModel model = new MonederoModel();
        model.registrarRecarga("origen@ucv.ve", new BigDecimal("100.00"));

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> model.registrarSaldoPana(" ORIGEN@ucv.ve ", "origen@ucv.ve", new BigDecimal("10.00"))
        );
        assertEquals("No puedes transferirte saldo a ti mismo.", ex.getMessage());
    }

    @Test
    void registrarSaldoPana_emailesConEspacios_transfiereCorrectamente() throws IOException {
        MonederoModel model = new MonederoModel();
        model.registrarRecarga("origen@ucv.ve", new BigDecimal("100.00"));

        model.registrarSaldoPana(" origen@ucv.ve ", " destino@ucv.ve ", new BigDecimal("10.00"));

        assertEquals(new BigDecimal("90.00"), model.obtenerSaldo("ORIGEN@ucv.ve"));
        assertEquals(new BigDecimal("10.00"), model.obtenerSaldo("destino@ucv.ve"));
    }

    @Test
    void registrarRecarga_montoNoPositivo_lanzaError() {
        MonederoModel model = new MonederoModel();

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> model.registrarRecarga("origen@ucv.ve", new BigDecimal("-1.00"))
        );
        assertEquals("Monto invalido.", ex.getMessage());
    }

    @Test
    void registrarCobro_montoNoPositivo_lanzaError() {
        MonederoModel model = new MonederoModel();

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> model.registrarCobro("origen@ucv.ve", BigDecimal.ZERO)
        );
        assertEquals("Monto invalido.", ex.getMessage());
    }

    @Test
    void registrarSaldoPana_falloDeEscritura_noDejaDebitoParcial() throws IOException {
        MonederoModel base = new MonederoModel();
        base.registrarRecarga("origen@ucv.ve", new BigDecimal("300.00"));

        MonederoModel modelFallido = new MonederoModel() {
            @Override
            protected void registrarMovimientosAtomicos(File archivo, List<String> lineasNuevas) throws IOException {
                throw new IOException("Fallo simulado");
            }
        };

        IOException ex = assertThrows(
            IOException.class,
            () -> modelFallido.registrarSaldoPana("origen@ucv.ve", "destino@ucv.ve", new BigDecimal("50.00"))
        );
        assertEquals("Fallo simulado", ex.getMessage());

        assertEquals(new BigDecimal("300.00"), base.obtenerSaldo("origen@ucv.ve"));
        assertEquals(new BigDecimal("0.00"), base.obtenerSaldo("destino@ucv.ve"));
    }
}
