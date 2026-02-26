package com.example.Vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConfigMenuPanelTest {

    private ConfigMenuPanel panelConfigMenu;

    @BeforeEach
    public void setUp() {
        panelConfigMenu = new ConfigMenuPanel();
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
