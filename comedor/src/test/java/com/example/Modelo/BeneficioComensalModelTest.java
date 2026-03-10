package com.example.Modelo;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void obtenerBeneficioPorEmail_resuelvePorCi() throws IOException {
        Files.writeString(usuariosUcvPath, "ana@ucv.ve,Ana,Estudiante,Faces,Escuela,12345678\n");

        BeneficioComensalModel model = new BeneficioComensalModel();
        model.registrarBecario("12345678", new BigDecimal("5"));

        BeneficioComensal beneficio = model.obtenerBeneficioPorEmail("ana@ucv.ve");
        assertNotNull(beneficio);
        assertTrue(beneficio.esBecario());
        assertEquals(new BigDecimal("5.00"), beneficio.getPorcentajeCobro());
    }
}
