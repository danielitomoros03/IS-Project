package com.example.Vista;

import com.example.Modelo.Plato;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDiaPanel extends JPanel {

    public MenuDiaPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); // Fondo gris claro

        // 1. Título de la sección
        JLabel lblTitulo = new JLabel("Menú del Día", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // 2. Panel contenedor de los platos (usamos BoxLayout vertical)
        JPanel panelPlatos = new JPanel();
        panelPlatos.setLayout(new BoxLayout(panelPlatos, BoxLayout.Y_AXIS));
        panelPlatos.setBackground(Color.WHITE);

        // --- LÓGICA DE NEGOCIO SIMULADA ---
        List<Plato> menuDelDia = obtenerMenuBackend(); 

        // --- VALIDACIÓN DE CRITERIOS DE ACEPTACIÓN ---
        
        // ESCENARIO: No hay menú disponible
        if (menuDelDia == null || menuDelDia.isEmpty()) {
            JLabel lblNoMenu = new JLabel("No hay menú disponible para hoy", SwingConstants.CENTER);
            lblNoMenu.setFont(new Font("Arial", Font.ITALIC, 18));
            lblNoMenu.setForeground(Color.GRAY);
            lblNoMenu.setBorder(new EmptyBorder(50, 0, 0, 0));
            panelPlatos.add(lblNoMenu);
        } 
        // ESCENARIO: Consulta con éxito
        else {
            for (Plato plato : menuDelDia) {
                panelPlatos.add(crearTarjetaPlato(plato));
                panelPlatos.add(Box.createRigidArea(new Dimension(0, 15))); // Espacio entre platos
            }
        }

        //  scroll por si hay muchos platos
        JScrollPane scrollPane = new JScrollPane(panelPlatos);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Método auxiliar para crear una tarjeta visual de cada plato
    private JPanel crearTarjetaPlato(Plato plato) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setMaximumSize(new Dimension(600, 180)); // Tamaño fijo para uniformidad

        // Nombre del plato
        JLabel lblNombre = new JLabel(plato.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 18));
        lblNombre.setForeground(new Color(50, 50, 50));

        // Descripción
        JLabel lblDesc = new JLabel("<html><i>" + plato.getDescripcion() + "</i></html>");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Panel de información nutricional
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelInfo.setBackground(Color.WHITE);
        panelInfo.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Usamos HTML en JLabels para formateo rápido de negritas
        panelInfo.add(new JLabel("<html><b>Calorías:</b> " + plato.getCalorias() + " kcal</html>"));
        panelInfo.add(new JLabel("<html><b>Proteínas:</b> " + plato.getProteinas() + "g</html>"));
        panelInfo.add(new JLabel("<html><b>Grasas:</b> " + plato.getGrasas() + "g</html>"));
        panelInfo.add(new JLabel("<html><b>Carbos:</b> " + plato.getCarbohidratos() + "g</html>"));

        // Armado de la tarjeta
        JPanel panelTexto = new JPanel(new GridLayout(2, 1));
        panelTexto.setBackground(Color.WHITE);
        panelTexto.add(lblNombre);
        panelTexto.add(lblDesc);

        tarjeta.add(panelTexto, BorderLayout.NORTH);
        tarjeta.add(panelInfo, BorderLayout.CENTER);

        return tarjeta;
    }

    /**
     * Método simulado que representa la conexión a la base de datos.
     * Para probar el escenario de "No hay menú", simplemente retorna una lista vacía.
     */
    private List<Plato> obtenerMenuBackend() {
        List<Plato> lista = new ArrayList<>();
        
        // COMENTEN ESTAS LÍNEAS PARA PROBAR EL ESCENARIO DE SI NO HAY MENÚ
        lista.add(new Plato("Pollo al Horno con Papas", "Cuarto de pollo marinado en finas hierbas", 
                            450, 35.5, 12.0, 40.0));
        lista.add(new Plato("Ensalada César", "Lechuga, crutones, queso parmesano y aderezo", 
                            320, 15.0, 18.5, 10.0));
        lista.add(new Plato("Lentejas con Arroz", "Estofado casero de lentejas", 
                            500, 25.0, 8.0, 60.0));
        
        return lista;
    }
}
