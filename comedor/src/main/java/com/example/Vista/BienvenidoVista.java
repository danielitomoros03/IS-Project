package com.example.Vista;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;


public class BienvenidoVista extends JFrame {

    // Para los colores de la interfaz
    private final Color COLOR_PRIMARY = new Color(34, 120, 64); // Verde UCV
    private final Color COLOR_BG = new Color(245, 247, 250);    // Gris muy claro de fondo
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_GO = new Color(220, 220, 220); // Gris oscurito
    
    private CardLayout cardLayout; 
    private JPanel mainContainer; // Para el contenido central, aqui dentro va toda la informacion de la vista 
    private JButton btnLogout; //Boton de salir

    //Botones del menu
    private JButton btnConsultarMenu, btnDashboard, btnRegTurno, btnHistorial, btnPerfil;

    public BienvenidoVista(String usuario, String rol) {
        setTitle("Sistema de Gestión - UCV");
        setSize(1200, 750); // Un poco más ancho para que quepan las tarjetas
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Barra gris de la izquierda
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_GO);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Logo en Sidebar
        JLabel lblLogo = new JLabel("UCV Comedor");
        lblLogo.setForeground(Color.BLACK);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sidebar.add(lblLogo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40))); // Espacio

        // Botones del menú
        btnDashboard = crearBotonMenu("Dashboard"); 
        sidebar.add(btnDashboard);  //Guardar boton
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        btnConsultarMenu = crearBotonMenu("Menu del Dia"); 
        sidebar.add(btnConsultarMenu);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        btnRegTurno = crearBotonMenu("Registrar Turno"); 
        sidebar.add(btnRegTurno);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        btnHistorial = crearBotonMenu("Historial"); 
        sidebar.add(btnHistorial);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        btnPerfil = crearBotonMenu("Perfil"); 
        sidebar.add(btnPerfil);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Versión al final, en caso de querer colocarlo :)
        sidebar.add(Box.createVerticalGlue()); // Empuja lo siguiente al fondo
        JLabel version = new JLabel("Versión 1.0");
        version.setForeground(new Color(255,255,255,100));
        sidebar.add(version);

        add(sidebar, BorderLayout.WEST);


        // Para el Header de arriba blanco
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230,230,230)));
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel tituloHeader = new JLabel("Sistema de Comedor");
        tituloHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        tituloHeader.setForeground(COLOR_TEXT_DARK);
        
        // Panel de usuario (Derecha del header)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(Color.WHITE);
        
        JLabel lblUser = new JLabel("<html><div style='text-align: right;'><b>"+usuario+"</b><br><span style='color:gray; font-size:9px'>"+rol+"</span></div></html>");
        
        btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(243, 244, 246));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
        btnLogout.setFocusPainted(false);                    
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));    //La manito del cursor

        userPanel.add(lblUser);
        userPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        userPanel.add(btnLogout);

        header.add(tituloHeader, BorderLayout.WEST);
        header.add(userPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);


        // Para el contenido central, Gris claro
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(COLOR_BG);

        // Vista Dashboard
        JPanel dashboardPanel = crearPanelDashboard();
        MenuPanel menuPanel = new MenuPanel();
        // para las otras vistas, crear su interfaz en otro .java, para historialPanel en adelante
        JPanel historialPanel = new JPanel();
        historialPanel.setBackground(COLOR_BG);
        historialPanel.add(new JLabel("Vista de Historial"));

        mainContainer.add(dashboardPanel, "DASH_VISTA");
        mainContainer.add(menuPanel, "MENU_VISTA");
        mainContainer.add(historialPanel, "HIST_VISTA");


        add(mainContainer, BorderLayout.CENTER);
    }

    // Metodos auxiliares :)

    private JPanel crearPanelDashboard() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Título de bienvenida
        JLabel titulo = new JLabel("Bienvenido");
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(COLOR_TEXT_DARK);
        
        JLabel subtitulo = new JLabel("Aquí está el resumen de tu actividad en el comedor");
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitulo.setForeground(Color.GRAY);

        panel.add(titulo);
        panel.add(subtitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Contenedor de Tarjetas (Grid para ponerlas una al lado de la otra)
        JPanel cardsContainer = new JPanel(new GridLayout(1, 2, 20, 0)); // 1 fila, 2 col, espacio 20
        cardsContainer.setBackground(COLOR_BG);
        cardsContainer.setMaximumSize(new Dimension(2000, 300)); // Altura fija para las cards


        //////////////////////////////////////////////////////////////
        //Esto de las tarjetas debe variar de acuerdo con la base de datos simulada


        // Tarjeta 1, menu del dia de hoy, solo para que haya algo
        JPanel cardMenu = crearTarjetaBase("Menú de Hoy", "(Fecha de hoy)");
        agregarFilaMenu(cardMenu, "Entrada", "Ensalada César", "Plato Principal", "Pollo al horno");
        agregarFilaMenu(cardMenu, "Acompañante", "Arroz blanco", "Postre", "Fruta fresca");

        
        // Tarjeta 2, turno
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

        // Agregar las cards al contenedor
        cardsContainer.add(cardMenu);
        cardsContainer.add(cardTurno);

        panel.add(cardsContainer);
        
        // Empujar todo hacia arriba :)
        panel.add(Box.createVerticalGlue());

        return panel;
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
        
        // Añadimos al centro de la card. Si ya hay algo, Swing lo maneja o usamos un Panel intermedio.
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


    //Funcion para crear los botones de la sidebar

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto); // Espacio para simular icono
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 40)); // Ancho fijo
        btn.setBackground(COLOR_PRIMARY); // Fondo verde
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover simple (opcional) :)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(40, 140, 75)); // Un verde un poco más claro
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(COLOR_PRIMARY);
            }
        });
        
        btn.setActionCommand(texto.trim());
        return btn;
    }


    // Getter para los botones para usarlos en el controlador


    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnMenuDia() { return btnConsultarMenu; }
    public JButton getBtnLogout() { return btnLogout; }

    public void changeView(String nombreVista) {
        cardLayout.show(mainContainer, nombreVista);
        mainContainer.revalidate();
        mainContainer.repaint();
    }
}
