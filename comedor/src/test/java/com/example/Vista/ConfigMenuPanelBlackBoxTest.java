package com.example.Vista;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ConfigMenuPanelBlackBoxTest {

    @TempDir
    Path tempDir;

    private String originalUserDir;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());
    }

    @AfterEach
    @SuppressWarnings("unused")
    void tearDown() {
        System.setProperty("user.dir", originalUserDir);
    }

    @Test
    void filtraMenusPorTurno() {
        ConfigMenuPanel panel = new ConfigMenuPanel();

        assertEquals(2, panel.getTabla().getRowCount());

        panel.realizarConsulta("Desayuno");

        assertEquals(1, panel.getTabla().getRowCount());
    }
}
