package com.example.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class DashUserPanel extends JPanel {
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(34, 120, 64);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private JButton btnMonedero;
    //private final Color COLOR_GO = new Color(220, 220, 220); // Gris oscurito
    
    public DashUserPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Título
        JLabel titulo = new JLabel("Bienvenido");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(COLOR_TEXT_DARK);
        
        JLabel subtitulo = new JLabel("Aquí está el resumen de tu actividad en el comedor");
        subtitulo.setForeground(Color.GRAY);

        add(titulo);
        add(subtitulo);
        add(Box.createRigidArea(new Dimension(0, 30)));

        // Contenedor desTarjetas
        JPanel cardsContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsContainer.setBackground(COLOR_BG);
        cardsContainer.setMaximumSize(new Dimension(2000, 300));

        // Tarjeta Menú
        JPanel cardMenu = crearTarjetaBase("Menú de Hoy", "(Fecha de hoy)");
        agregarFilaMenu(cardMenu, "Entrada", "Ensalada César", "Plato Principal", "Pollo al horno");
        
        // Tarjeta Turno
        JPanel cardTurno = crearTarjetaBase("Tu Turno Registrado", "Hoy");
        JLabel lblTurno = new JLabel("Almuerzo");
        lblTurno.setOpaque(true);
        lblTurno.setBackground(COLOR_PRIMARY);
        lblTurno.setForeground(Color.WHITE);
        lblTurno.setBorder(new EmptyBorder(5, 10, 5, 10)); 
        
        JLabel lblHora = new JLabel("12:00 PM - 2:00 PM");
        lblHora.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        // Barra de capacidad, lo hice por curiosidad
        JProgressBar barra = new JProgressBar(0, 100);
        barra.setValue(65);
        barra.setForeground(COLOR_PRIMARY);
        barra.setString("Capacidad 65/85");
        barra.setStringPainted(true);

        // Añadir cosas a la tarjeta de turno
        JPanel contentTurno = new JPanel(new GridLayout(4, 1, 5, 5));
        contentTurno.setBackground(Color.WHITE);
        contentTurno.add(lblTurno);
        contentTurno.add(lblHora);
        contentTurno.add(Box.createRigidArea(new Dimension(0, 10)));
        contentTurno.add(barra);
        
        cardTurno.add(contentTurno, BorderLayout.CENTER);
        cardsContainer.add(cardMenu);
        cardsContainer.add(cardTurno);
        add(cardsContainer);
        add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel cardMonedero = crearTarjetaBase("Monedero Digital", "Consulta tu saldo y recargas");
        JPanel contentMonedero = new JPanel(new BorderLayout());
        contentMonedero.setBackground(Color.WHITE);

        JLabel lblInfo = new JLabel("Gestiona tu saldo desde aqui");
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setBorder(new EmptyBorder(0, 0, 10, 0));

        btnMonedero = new JButton("Ver monedero");
        btnMonedero.setBackground(COLOR_PRIMARY);
        btnMonedero.setForeground(Color.WHITE);
        btnMonedero.setFocusPainted(false);
        btnMonedero.setCursor(new Cursor(Cursor.HAND_CURSOR));

        contentMonedero.add(lblInfo, BorderLayout.NORTH);
        contentMonedero.add(btnMonedero, BorderLayout.WEST);
        cardMonedero.add(contentMonedero, BorderLayout.CENTER);

        add(cardMonedero);
        add(Box.createVerticalGlue());
    }

    // Crea el esqueleto blanco de una tarjeta
    private JPanel crearTarjetaBase(String titulo, String sub) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true), // Borde gris suave
            new EmptyBorder(20, 20, 20, 20) // Padding interno
        ));

        JPanel headerCard = new JPanel(new BorderLayout());
        headerCard.setBackground(Color.WHITE);
        
        JLabel lTitle = new JLabel("<html><b>"+titulo+"</b><br><span style='font-weight:normal; color:gray; font-size:10px'>"+sub+"</span></html>");
        lTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        headerCard.add(lTitle, BorderLayout.WEST);
        headerCard.setBorder(new EmptyBorder(0,0,15,0)); // Separación abajo
        
        card.add(headerCard, BorderLayout.NORTH);
        return card;
    }

    // Ayuda a poner los textos del menú alineados
    private void agregarFilaMenu(JPanel card, String t1, String v1, String t2, String v2) {
        JPanel row = new JPanel(new GridLayout(1, 2));
        row.setBackground(Color.WHITE);
        
        JLabel col1 = new JLabel("<html><span style='color:gray; font-size:9px'>"+t1+"</span><br><b>"+v1+"</b></html>");
        JLabel col2 = new JLabel("<html><span style='color:gray; font-size:9px'>"+t2+"</span><br><b>"+v2+"</b></html>");
        
        col1.setBorder(new EmptyBorder(0,0,10,0));
        
        row.add(col1);
        row.add(col2);
    
        // Para simplificar, asumimos que usamos un BoxLayout en el centro si son varias filas
        if(!(card.getLayout() instanceof BorderLayout)) return;
        
        Component center = ((BorderLayout)card.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        JPanel body;
        if (center == null) {
            body = new JPanel();
            body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
            body.setBackground(Color.WHITE);
            card.add(body, BorderLayout.CENTER);
        } else {
            body = (JPanel) center;
        }
        body.add(row);
    }

    public JButton getBtnMonedero() {
        return btnMonedero;
    }

}
