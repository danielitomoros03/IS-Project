package com.example.Modelo;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AsistenciaComedorModelTest {

    private Path asistenciasPath;
    private byte[] backup;
    private boolean hadBackup;

    @BeforeEach
    void setUp() throws IOException {
        asistenciasPath = Paths.get("").toAbsolutePath().resolve("Asistencias_Comedor.txt");
        if (Files.exists(asistenciasPath)) {
            backup = Files.readAllBytes(asistenciasPath);
            hadBackup = true;
        }
        Files.deleteIfExists(asistenciasPath);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(asistenciasPath);
        if (hadBackup) {
            Files.write(asistenciasPath, backup);
        }
    }

    @Test
    void registrarAsistencia_y_obtenerResumenPorServicio() {
        AsistenciaComedorModel model = new AsistenciaComedorModel();

        assertTrue(model.registrarAsistencia(new AsistenciaRecord(
            LocalDateTime.now(),
            "Desayuno",
            "reg@ucv.ve",
            "11111111",
            "Estudiante Regular",
            new BigDecimal("20.00")
        )));

        assertTrue(model.registrarAsistencia(new AsistenciaRecord(
            LocalDateTime.now(),
            "Desayuno",
            "bec@ucv.ve",
            "22222222",
            "Estudiante Becario",
            new BigDecimal("1.00")
        )));

        assertTrue(model.registrarAsistencia(new AsistenciaRecord(
            LocalDateTime.now(),
            "Almuerzo",
            "exo@ucv.ve",
            "33333333",
            "Estudiante Exonerado",
            new BigDecimal("0.00")
        )));

        List<AsistenciaRecord> desayuno = model.obtenerRegistrosPorServicio("Desayuno");
        assertEquals(2, desayuno.size());

        Map<String, Integer> resumen = model.obtenerResumenPorServicio("Desayuno");
        assertEquals(1, resumen.get("Estudiante Regular"));
        assertEquals(1, resumen.get("Estudiante Becario"));
        assertEquals(0, resumen.get("Estudiante Exonerado"));
        assertEquals(2, resumen.get("Total"));
    }
}
