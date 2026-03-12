package com.example.Vista;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.Modelo.AsistenciaComedorModel;
import com.example.Modelo.AsistenciaRecord;

public class ReportePanelTest {

    private Path asistenciasPath;
    private byte[] backup;
    private boolean hadBackup;

    @BeforeEach
    public void setUp() throws IOException {
        asistenciasPath = Paths.get("").toAbsolutePath().resolve("Asistencias_Comedor.txt");
        if (Files.exists(asistenciasPath)) {
            backup = Files.readAllBytes(asistenciasPath);
            hadBackup = true;
        }
        Files.deleteIfExists(asistenciasPath);

        AsistenciaComedorModel model = new AsistenciaComedorModel();
        model.registrarAsistencia(new AsistenciaRecord(
            LocalDateTime.now(),
            "Desayuno",
            "regular@ucv.ve",
            "11111111",
            "Estudiante Regular",
            new BigDecimal("20.00")
        ));
        model.registrarAsistencia(new AsistenciaRecord(
            LocalDateTime.now(),
            "Desayuno",
            "becario@ucv.ve",
            "22222222",
            "Estudiante Becario",
            new BigDecimal("19.00")
        ));
        model.registrarAsistencia(new AsistenciaRecord(
            LocalDateTime.now(),
            "Almuerzo",
            "exonerado@ucv.ve",
            "33333333",
            "Estudiante Exonerado",
            new BigDecimal("0.00")
        ));
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(asistenciasPath);
        if (hadBackup) {
            Files.write(asistenciasPath, backup);
        }
    }

    @Test
    public void cargarReporteServicio_desayuno_y_almuerzo_muestraDetalleYResumen() {
        ReportePanel panel = new ReportePanel();

        assertEquals(2, panel.getDetalleModel().getRowCount());
        assertEquals(1, obtenerCantidad(panel, "Estudiante Regular"));
        assertEquals(1, obtenerCantidad(panel, "Estudiante Becario"));
        assertEquals(0, obtenerCantidad(panel, "Estudiante Exonerado"));
        assertEquals(2, obtenerCantidad(panel, "Total"));

        panel.getComboServicio().setSelectedItem("Almuerzo");

        assertEquals(1, panel.getDetalleModel().getRowCount());
        assertEquals(0, obtenerCantidad(panel, "Estudiante Regular"));
        assertEquals(0, obtenerCantidad(panel, "Estudiante Becario"));
        assertEquals(1, obtenerCantidad(panel, "Estudiante Exonerado"));
        assertEquals(1, obtenerCantidad(panel, "Total"));
    }

    private int obtenerCantidad(ReportePanel panel, String tipo) {
        for (int i = 0; i < panel.getResumenModel().getRowCount(); i++) {
            Object tipoActual = panel.getResumenModel().getValueAt(i, 0);
            if (tipo.equals(tipoActual)) {
                return Integer.parseInt(panel.getResumenModel().getValueAt(i, 1).toString());
            }
        }
        return -1;
    }
}