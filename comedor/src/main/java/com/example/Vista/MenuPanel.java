package com.example.Vista;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class MenuPanel extends JPanel {
    private JPanel contenedorTarjetas;
    private final Color COLOR_BG = new Color(245, 247, 250);

    public MenuPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título de la vista
        JLabel titulo = new JLabel("Menú del Día");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        // Contenedor para las tarjetas (3 columnas, espacio de 20px)
        contenedorTarjetas = new JPanel(new GridLayout(0, 3, 20, 20));
        contenedorTarjetas.setBackground(COLOR_BG);

        // Scroll por si el menú es largo
        JScrollPane scroll = new JScrollPane(contenedorTarjetas);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // Simulación de carga de datos (Aquí podrías recibir una lista de objetos)
        cargarMenuEjemplo();
    }

    private void cargarMenuEjemplo() {
        // Usamos la función para crear tarjetas
        contenedorTarjetas.add(crearTarjetaPlato("Pollo al horno", "Pechuga de pollo al horno con hierbas y especias", "420", "45g", "35g", "12g"));
        contenedorTarjetas.add(crearTarjetaPlato("Pasta Boloñesa", "Pasta italiana con salsa de carne molida y tomate", "550", "28g", "72g", "18g"));
        contenedorTarjetas.add(crearTarjetaPlato("Ensalada César", "Lechuga romana, crotones, parmesano y aderezo", "280", "12g", "18g", "20g"));
    }

    // Funcion para crear tarjetas de los platos del menu
    private JPanel crearTarjetaPlato(String nombre, String desc, String kcal, String prot, String carb, String gras) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Nombre del plato
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        // Descripción (Uso de HTML para que el texto salte de línea)
        JLabel lblDesc = new JLabel("<html><body style='width: 150px;'>" + desc + "</body></html>");
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setBorder(new EmptyBorder(10, 0, 15, 0));

        // Panel de Macros (Iconos circulares pequeños)
        JPanel panelMacros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelMacros.setBackground(Color.WHITE);
        panelMacros.add(crearBurbujaMacro(kcal, "kcal", new Color(255, 153, 51)));
        panelMacros.add(crearBurbujaMacro(prot, "prot", new Color(255, 51, 102)));
        panelMacros.add(crearBurbujaMacro(carb, "carb", new Color(51, 153, 255)));

        card.add(lblNombre);
        card.add(lblDesc);
        card.add(panelMacros);
        
        return card;
    }

    // Función auxiliar para los circulitos nutricionales
    private JPanel crearBurbujaMacro(String valor, String unidad, Color color) {
        JPanel burbuja = new JPanel();
        burbuja.setLayout(new BoxLayout(burbuja, BoxLayout.Y_AXIS));
        burbuja.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30)); // Fondo suave
        burbuja.setBorder(new EmptyBorder(5, 8, 5, 8));

        JLabel v = new JLabel(valor);
        v.setFont(new Font("SansSerif", Font.BOLD, 11));
        v.setForeground(color);
        v.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel u = new JLabel(unidad);
        u.setFont(new Font("SansSerif", Font.PLAIN, 9));
        u.setForeground(color);
        u.setAlignmentX(Component.CENTER_ALIGNMENT);

        burbuja.add(v);
        burbuja.add(u);
        return burbuja;
    }
}