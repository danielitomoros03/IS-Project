package com.example.Vista;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
//import javax.swing.border.LineBorder;
import java.awt.*;

public class AdminVista extends JFrame {

    // Colores corporativos UCV (Mantenidos para consistencia)
    private final Color COLOR_PRIMARY = new Color(34, 120, 64); // Verde UCV
    private final Color COLOR_BG = new Color(245, 247, 250); 
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_GO = new Color(220, 220, 220); 
    /*
    private final String VISTA_CONFIG = "CONFIG_MENU";
    private final String VISTA_REPORTE = "GENERAR_REPORTE";
    private final String VISTA_CONSUMO = "HISTORIAL_CONSUMO";
    private final String VISTA_USUARIOS = "GESTION_USUARIOS"; */

    private CardLayout cardLayout;
    private JPanel mainContainer;
    private JButton btnLogout;

    // Botones específicos para Administrador
    private JButton btnDashboard, btnReporte, btnGestionInsumo, btnGestionMenu, btnPerfil;

    public AdminVista(String usuario, String rol) {
        setTitle("Panel de Administración - UCV Comedor");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_GO);
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblLogo = new JLabel("UCV Admin");
        lblLogo.setForeground(Color.BLACK);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sidebar.add(lblLogo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // Inicialización de botones solicitados
        btnDashboard = crearBotonMenu("Dashboard");
        btnGestionMenu = crearBotonMenu("Gestión de Menús");
        btnGestionInsumo = crearBotonMenu("Gestión de Insumos");
        btnReporte = crearBotonMenu("Reportes");
        btnPerfil = crearBotonMenu("Mi Perfil");

        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnGestionMenu);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnGestionInsumo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnReporte);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnPerfil);

        sidebar.add(Box.createVerticalGlue());
        JLabel version = new JLabel("Panel Administrativo v1.0");
        version.setForeground(new Color(0,0,0,100));
        sidebar.add(version);

        add(sidebar, BorderLayout.WEST);

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel tituloHeader = new JLabel("Módulo de Administración");
        tituloHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(Color.WHITE);
        
        JLabel lblUser = new JLabel("<html><div style='text-align: right;'><b>"+usuario+"</b><br><span style='color:red; font-size:9px'>"+rol+"</span></div></html>");
        
        btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        userPanel.add(lblUser);
        userPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        userPanel.add(btnLogout);

        header.add(tituloHeader, BorderLayout.WEST);
        header.add(userPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- CONTENIDO CENTRAL ---
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // Instanciamos las clases externas
        DashAdminVista dashAdmin = new DashAdminVista();
        ConfigMenuPanel vistaConfig = new ConfigMenuPanel();
        ReportePanel vistaReporte = new ReportePanel();
        // Aqui las otras vistas

        // Las añadimos al contenedor principal
        mainContainer.add(dashAdmin, "DASH_VISTA");
        mainContainer.add(vistaConfig, "MENU_VISTA");
        mainContainer.add(vistaReporte, "REPORTE_VISTA");

        add(mainContainer, BorderLayout.CENTER);
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setBackground(COLOR_GO); 
        btn.setForeground(COLOR_TEXT_DARK);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Solo resaltamos si no está ya seleccionado (es decir, si es gris)
                if (btn.getBackground().equals(COLOR_GO)) {
                    btn.setBackground(new Color(200, 200, 200)); 
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Si no es el verde activo, vuelve al gris original
                if (!btn.getBackground().equals(COLOR_PRIMARY)) {
                    btn.setBackground(COLOR_GO);
                }
            }
        });

        btn.setActionCommand(texto.trim());
        return btn;
    }

    public void marcarBotonActivo(JButton botonActivo) {
        // Lista de todos tus botones de la sidebar
        JButton[] botones = {btnDashboard, btnGestionMenu, btnGestionInsumo, btnReporte, btnPerfil};

        for (JButton b : botones) {
            if (b == botonActivo) {
                b.setBackground(COLOR_PRIMARY); // Verde UCV
                b.setForeground(Color.WHITE);   // Texto blanco para resaltar
            } else {
                b.setBackground(COLOR_GO);      // Gris barra
                b.setForeground(COLOR_TEXT_DARK); // Texto oscuro
            }
        }
    }
    

    // Getters para el Controlador
    public JButton getBtnDasboard() { return btnDashboard; }
    public JButton getBtnGestionMenu() { return btnGestionMenu; }
    public JButton getBtnGestionInsumo() { return btnGestionInsumo; }
    public JButton getBtnReporte() { return btnReporte; }
    public JButton getBtnPerfil() { return btnPerfil; }
    public JButton getBtnLogout() { return btnLogout; }

    public void changeView(String nombreVista) {
        cardLayout.show(mainContainer, nombreVista);
        mainContainer.revalidate();
        mainContainer.repaint();

    }
}
