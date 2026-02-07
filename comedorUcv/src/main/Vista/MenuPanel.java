package comedorUcv.src.main.Vista;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        // Título temporal
        JLabel titulo = new JLabel("Menú Completo del Comedor", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titulo, BorderLayout.CENTER);
        
        // Aquí luego se agregara la tabla de comida
        // Agreguen lo que quieran a esta interfaz.
    }
}
