import com.example.Vista.ConfigMenuPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigMenuPanelTest{

    private ConfigMenuPanel panelConfigMenu;

    @BeforeEach
    public void setUp() {

        //Cargar los datos estáticos
        panelConfigMenu = new ConfigMenuPanel();
    }

    @Test
    public void testConsultaMenu_PorTurno_EncuentraResultado() {
        // 1. Verificar estado inicial (Almuerzo y Desayuno)
        assertEquals(2, panelConfigMenu.getTabla().getRowCount(), "Debería cargar 2 menús inicialmente");

        // 2. Acción: Consultar/Buscar el menú de desayuno
        panelConfigMenu.realizarConsulta("Desayuno");

        // 3. Verificación: La tabla debe haberse filtrado y mostrar solo 1 fila
        assertEquals(1, panelConfigMenu.getTabla().getRowCount(), "La consulta debería retornar exactamente 1 menú para 'Desayuno'");

        // 4. Verificación del contenido: Asegurarse de que la fila visible es realmente la del Desayuno
        String turnoVisible = panelConfigMenu.getTabla().getValueAt(0, 1).toString();
        assertEquals("Desayuno", turnoVisible, "El turno en la tabla debe coincidir con la consulta");
    }

    @Test
    public void testConsultaMenu_PorPlato_EncuentraResultado() {
        // 1. Acción: Consultar un menú que contenga Pollo al horno
        panelConfigMenu.realizarConsulta("Pollo al horno");

        // 2. Verificación: Solo 1 menú tiene ese plato
        assertEquals(1, panelConfigMenu.getTabla().getRowCount(), "La consulta debería retornar 1 menú con Pollo al horno");
    }

    @Test
    public void testConsultaMenu_SinResultados() {
        // 1. Acción: Consultar un término que no existe en los menús actuales
        panelConfigMenu.realizarConsulta("Hamburguesa");

        // 2. Verificación: La tabla debe quedar vacía (0 filas visibles)
        assertEquals(0, panelConfigMenu.getTabla().getRowCount(), "La consulta no debería retornar resultados para un plato inexistente");
    }
    
    @Test
    public void testConsultaMenu_RestablecerBusqueda() {
        // 1. Filtrar
        panelConfigMenu.realizarConsulta("Desayuno");
        assertEquals(1, panelConfigMenu.getTabla().getRowCount());

        // 2. Simular consulta vacia
        panelConfigMenu.realizarConsulta("");

        // 3. Verificación: Deben volver a aparecer todos los menús
        assertEquals(2, panelConfigMenu.getTabla().getRowCount(), "Al limpiar la consulta, deben verse todos los menús nuevamente");
    }
}