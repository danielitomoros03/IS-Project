package com.example.Modelo;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BeneficioComensalModelTest {

    private Path beneficiosPath;
    private Path usuariosUcvPath;
    private byte[] backupBeneficios;
    private byte[] backupUsuariosUcv;
    private boolean hadBeneficiosBackup;
    private boolean hadUsuariosUcvBackup;

    @BeforeEach
    void setUp() throws IOException {
        Path base = Paths.get("").toAbsolutePath();
        beneficiosPath = base.resolve("Beneficios_Comensal.txt");
        usuariosUcvPath = base.resolve("Usuarios_UCV.txt");

        if (Files.exists(beneficiosPath)) {
            backupBeneficios = Files.readAllBytes(beneficiosPath);
            hadBeneficiosBackup = true;
        }
        if (Files.exists(usuariosUcvPath)) {
            backupUsuariosUcv = Files.readAllBytes(usuariosUcvPath);
            hadUsuariosUcvBackup = true;
        }

        Files.deleteIfExists(beneficiosPath);
        Files.deleteIfExists(usuariosUcvPath);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(beneficiosPath);
        Files.deleteIfExists(usuariosUcvPath);

        if (hadBeneficiosBackup) {
            Files.write(beneficiosPath, backupBeneficios);
        }
        if (hadUsuariosUcvBackup) {
            Files.write(usuariosUcvPath, backupUsuariosUcv);
        }
    }

    @Test
    void registrarExonerado_y_obtenerPorCi() throws IOException {
        BeneficioComensalModel model = new BeneficioComensalModel();
        model.registrarExonerado("V-12345678");

        BeneficioComensal beneficio = model.obtenerBeneficioPorCi("12345678");
        assertNotNull(beneficio);
        assertTrue(beneficio.esExonerado());
        assertEquals(new BigDecimal("0.00"), beneficio.getPorcentajeCobro());
    }

    @Test
    void registrarBecario_validaRango() {
        BeneficioComensalModel model = new BeneficioComensalModel();

        assertThrows(IllegalArgumentException.class, () -> model.registrarBecario("12345678", new BigDecimal("0")));
        assertThrows(IllegalArgumentException.class, () -> model.registrarBecario("12345678", new BigDecimal("100")));
    }

    @Test
    void registrarBecario_porcentajeNulo_lanzaError() {
        BeneficioComensalModel model = new BeneficioComensalModel();

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> model.registrarBecario("12345678", null)
        );
        assertEquals("Debes indicar el porcentaje de descuento para becario.", ex.getMessage());
    }

    @Test
    void obtenerBeneficioPorEmail_resuelvePorCi() throws IOException {
        Files.writeString(usuariosUcvPath, "ana@ucv.ve,Ana,Estudiante,Faces,Escuela,12345678\n");

        BeneficioComensalModel model = new BeneficioComensalModel();
        model.registrarBecario("12345678", new BigDecimal("5"));

        BeneficioComensal beneficio = model.obtenerBeneficioPorEmail("ana@ucv.ve");
        assertNotNull(beneficio);
        assertTrue(beneficio.esBecario());
        assertEquals(new BigDecimal("5.00"), beneficio.getPorcentajeCobro());
    }

    @Test
    void registrarBecario_actualizaPorcentajeSinDuplicarCi() throws IOException {
        BeneficioComensalModel model = new BeneficioComensalModel();

        model.registrarBecario("12345678", new BigDecimal("5"));
        model.registrarBecario("V-12.345.678", new BigDecimal("12.5"));

        BeneficioComensal beneficio = model.obtenerBeneficioPorCi("12345678");
        List<String> lineas = Files.readAllLines(beneficiosPath);

        assertNotNull(beneficio);
        assertTrue(beneficio.esBecario());
        assertEquals(new BigDecimal("12.50"), beneficio.getPorcentajeCobro());
        assertEquals(1, lineas.size());
        assertTrue(lineas.get(0).startsWith("12345678,BECARIO,12.50,"));
    }

    @Test
    void registrarExonerado_reemplazaBeneficioPrevioSinDuplicarCi() throws IOException {
        BeneficioComensalModel model = new BeneficioComensalModel();

        model.registrarBecario("12345678", new BigDecimal("8"));
        model.registrarExonerado("12345678");

        BeneficioComensal beneficio = model.obtenerBeneficioPorCi("12345678");
        List<String> lineas = Files.readAllLines(beneficiosPath);

        assertNotNull(beneficio);
        assertTrue(beneficio.esExonerado());
        assertEquals(new BigDecimal("0.00"), beneficio.getPorcentajeCobro());
        assertEquals(1, lineas.size());
        assertTrue(lineas.get(0).startsWith("12345678,EXONERADO,0.00,"));
    }

    @Test
    void obtenerBeneficioPorCi_lineasInvalidas_y_ultimoRegistroValido() throws IOException {
        Files.writeString(
            beneficiosPath,
            "12345678,BECARIO,abc,2026-03-01T10:00:00\n"
                + "12345678,EXONERADO,0.00,2026-03-01T10:10:00\n"
                + "77777777,BECARIO,20.00,2026-03-01T10:15:00\n"
                + "12345678,BECARIO,5.00,2026-03-01T10:20:00\n"
        );

        BeneficioComensalModel model = new BeneficioComensalModel();
        BeneficioComensal beneficio = model.obtenerBeneficioPorCi("V-12.345.678");

        assertNotNull(beneficio);
        assertTrue(beneficio.esBecario());
        assertEquals(new BigDecimal("5.00"), beneficio.getPorcentajeCobro());
    }

    @Test
    void obtenerBeneficioPorEmail_sinCiEnSecretaria_devuelveNull() throws IOException {
        Files.writeString(usuariosUcvPath, "ana@ucv.ve,Ana,Estudiante,Faces,Escuela\n");

        BeneficioComensalModel model = new BeneficioComensalModel();
        model.registrarExonerado("12345678");

        BeneficioComensal beneficio = model.obtenerBeneficioPorEmail("ana@ucv.ve");
        assertNull(beneficio);
    }

    @Test
    void obtenerBeneficiosVigentes_ignoraMalFormados_y_normalizaCi() throws IOException {
        Files.writeString(
            beneficiosPath,
            "linea-sin-formato\n"
                + "12345678,EXONERADO,0.00,2026-03-01T10:00:00\n"
                + "12.345.679,BECARIO,7.50,2026-03-01T10:05:00\n"
                + "12.345.679,BECARIO,no-num,2026-03-01T10:06:00\n"
        );

        BeneficioComensalModel model = new BeneficioComensalModel();
        Map<String, BeneficioComensal> vigentes = model.obtenerBeneficiosVigentes();

        assertEquals(2, vigentes.size());
        assertTrue(vigentes.containsKey("12345678"));
        assertTrue(vigentes.containsKey("12345679"));
        assertTrue(vigentes.get("12345678").esExonerado());
        assertTrue(vigentes.get("12345679").esBecario());
        assertEquals(new BigDecimal("7.50"), vigentes.get("12345679").getPorcentajeCobro());
    }

    @Test
    void obtenerBeneficioPorCi_ciVacia_devuelveNull() {
        BeneficioComensalModel model = new BeneficioComensalModel();

        BeneficioComensal beneficio = model.obtenerBeneficioPorCi("  ");
        assertNull(beneficio);
    }
}
