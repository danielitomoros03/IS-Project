package com.example.Modelo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MenuModelTest {

    private Path menusPath;
    private byte[] backup;
    private boolean hadBackup;

    @BeforeEach
    void setUp() throws IOException {
        menusPath = Paths.get("").toAbsolutePath().resolve("Menus.txt");
        if (Files.exists(menusPath)) {
            backup = Files.readAllBytes(menusPath);
            hadBackup = true;
        }
        Files.deleteIfExists(menusPath);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(menusPath);
        if (hadBackup) {
            Files.write(menusPath, backup);
        }
    }

    @Test
    void obtenerMenus_creaIniciales_siNoExisteArchivo() {
        MenuModel model = new MenuModel();
        List<MenuRecord> menus = model.obtenerMenus();
        assertEquals(2, menus.size());
        assertTrue(Files.exists(menusPath));
    }

    @Test
    void guardar_actualizar_eliminar() throws IOException {
        MenuModel model = new MenuModel();
        MenuRecord m1 = new MenuRecord("id1", LocalDate.of(2026, 2, 26), "Almuerzo", Arrays.asList("A"));
        MenuRecord m2 = new MenuRecord("id2", LocalDate.of(2026, 2, 27), "Desayuno", Arrays.asList("B"));
        writeMenus(m1.toCsvLine(), m2.toCsvLine());

        MenuRecord m3 = new MenuRecord("id3", LocalDate.of(2026, 2, 28), "Almuerzo", Arrays.asList("C"));
        assertTrue(model.guardar(m3));
        assertEquals(3, model.obtenerMenus().size());

        MenuRecord actualizado = new MenuRecord("id2", LocalDate.of(2026, 2, 27), "Desayuno", Arrays.asList("B", "C"));
        assertTrue(model.actualizar(actualizado));
        assertEquals("B, C", model.obtenerMenus().stream().filter(m -> m.getId().equals("id2")).findFirst().get().getPlatosTexto());

        assertTrue(model.eliminar("id1"));
        assertEquals(2, model.obtenerMenus().size());
    }

    @Test
    void obtenerMenusPorFecha_filtraCorrecto() throws IOException {
        MenuModel model = new MenuModel();
        LocalDate fecha = LocalDate.of(2026, 2, 26);
        MenuRecord m1 = new MenuRecord("id1", fecha, "Almuerzo", Arrays.asList("A"));
        MenuRecord m2 = new MenuRecord("id2", fecha.plusDays(1), "Desayuno", Arrays.asList("B"));
        writeMenus(m1.toCsvLine(), m2.toCsvLine());

        List<MenuRecord> filtrados = model.obtenerMenusPorFecha(fecha);
        assertEquals(1, filtrados.size());
        assertEquals("id1", filtrados.get(0).getId());
    }

    private void writeMenus(String... lineas) throws IOException {
        Files.write(menusPath, String.join(System.lineSeparator(), lineas).getBytes());
    }
}
