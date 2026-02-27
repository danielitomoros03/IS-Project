package com.example.Vista;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.Modelo.MenuRecord;

public class ConfigMenuPanelTest {

    private ConfigMenuPanel panelConfigMenu;
    private Path menusPath;
    private byte[] backup;
    private boolean hadBackup;

    @BeforeEach
    public void setUp() throws IOException {
        menusPath = Paths.get("").toAbsolutePath().resolve("Menus.txt");
        if (Files.exists(menusPath)) {
            backup = Files.readAllBytes(menusPath);
            hadBackup = true;
        }

        LocalDate hoy = LocalDate.now();
        MenuRecord almuerzo = new MenuRecord("id1", hoy, "Almuerzo", Arrays.asList("Pollo al horno", "Arroz blanco"));
        MenuRecord desayuno = new MenuRecord("id2", hoy, "Desayuno", Arrays.asList("Arepa con queso", "Jugo natural"));
        Files.writeString(menusPath, almuerzo.toCsvLine() + System.lineSeparator() + desayuno.toCsvLine());

        panelConfigMenu = new ConfigMenuPanel();
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(menusPath);
        if (hadBackup) {
            Files.write(menusPath, backup);
        }
    }

    @Test
    public void testConsultaMenu_PorTurno_EncuentraResultado() {
        assertEquals(2, panelConfigMenu.getTabla().getRowCount(), "Deberia cargar 2 menus inicialmente");

        panelConfigMenu.realizarConsulta("Desayuno");

        assertEquals(1, panelConfigMenu.getTabla().getRowCount(), "La consulta deberia retornar exactamente 1 menu para 'Desayuno'");

        String turnoVisible = panelConfigMenu.getTabla().getValueAt(0, 2).toString();
        assertEquals("Desayuno", turnoVisible, "El turno en la tabla debe coincidir con la consulta");
    }

    @Test
    public void testConsultaMenu_PorPlato_EncuentraResultado() {
        panelConfigMenu.realizarConsulta("Pollo al horno");

        assertEquals(1, panelConfigMenu.getTabla().getRowCount(), "La consulta deberia retornar 1 menu con Pollo al horno");
    }

    @Test
    public void testConsultaMenu_SinResultados() {
        panelConfigMenu.realizarConsulta("Hamburguesa");

        assertEquals(0, panelConfigMenu.getTabla().getRowCount(), "La consulta no deberia retornar resultados para un plato inexistente");
    }

    @Test
    public void testConsultaMenu_RestablecerBusqueda() {
        panelConfigMenu.realizarConsulta("Desayuno");
        assertEquals(1, panelConfigMenu.getTabla().getRowCount());

        panelConfigMenu.realizarConsulta("");

        assertEquals(2, panelConfigMenu.getTabla().getRowCount(), "Al limpiar la consulta, deben verse todos los menus nuevamente");
    }
}
