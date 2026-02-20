package com.example.Vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class BienvenidoVista extends JFrame {

    // Colores
    private final Color COLOR_PRIMARY = new Color(34, 120, 64);
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_GO = new Color(220, 220, 220);
    
    private CardLayout cardLayout; 
    private JPanel mainContainer; 
    private JButton btnLogout;

    // Botones del menu
    private JButton btnConsultarMenu, btnDashboard, btnRegTurno, btnHistorial, btnPerfil, btnMonedero;
    
    // Boton para admin
    private JButton btnConfigCCB; 

    private DashUserPanel dashPanel;

    public BienvenidoVista(String usuario, String rol) {
        setTitle("Sistema de Gestión - UCV");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- SIDEBAR (IZQUIERDA) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_GO);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Logo
        JLabel lblLogo = new JLabel("UCV Comedor");
        lblLogo.setForeground(Color.BLACK);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sidebar.add(lblLogo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40))); 

        // Botones del menú 
        btnDashboard = crearBotonMenu("Dashboard"); 
        sidebar.add(btnDashboard);  
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

        btnMonedero = crearBotonMenu("Monedero");
        sidebar.add(btnMonedero);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        if (rol != null && rol.equalsIgnoreCase("Administrador")) {
            btnConfigCCB = crearBotonMenu("Configuración CCB");
            sidebar.add(btnConfigCCB);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        btnPerfil = crearBotonMenu("Perfil"); 
        sidebar.add(btnPerfil);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        sidebar.add(Box.createVerticalGlue()); 
        JLabel version = new JLabel("Versión 1.1");
        version.setForeground(new Color(255,255,255,100));
        sidebar.add(version);

        add(sidebar, BorderLayout.WEST);

        // --- HEADER (ARRIBA) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230,230,230)));
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel tituloHeader = new JLabel("Sistema de Comedor");
        tituloHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        tituloHeader.setForeground(COLOR_TEXT_DARK);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(Color.WHITE);
        
        JLabel lblUser = new JLabel("<html><div style='text-align: right;'><b>"+usuario+"</b><br><span style='color:gray; font-size:9px'>"+rol+"</span></div></html>");
        
        btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(243, 244, 246));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
        btnLogout.setFocusPainted(false);                    
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
        mainContainer.setBackground(COLOR_BG);

        dashPanel = new DashUserPanel();
        MenuDiaPanel menuPanel = new MenuDiaPanel(); 
        RegistroTurnoPanel regTurnoPanel = new RegistroTurnoPanel(); 
        JPanel hist = new JPanel(); 
        JPanel perfilPanel = new JPanel();

        mainContainer.add(dashPanel, "DASH_VISTA");
        mainContainer.add(menuPanel, "MENU_VISTA");
        mainContainer.add(hist, "HIST_VISTA");
        mainContainer.add(regTurnoPanel, "TURNO_VISTA");
        mainContainer.add(perfilPanel, "PERFIL_VISTA");

        if (rol != null && rol.equalsIgnoreCase("Administrador")) {
            ConfiguracionCCBPanel configPanel = new ConfiguracionCCBPanel();
            mainContainer.add(configPanel, "CONFIG_CCB_VISTA");
        }

        add(mainContainer, BorderLayout.CENTER);
    }

    // Funciones auxiliares
    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(240, 45)); 
        btn.setBackground(COLOR_GO); 
        btn.setForeground(COLOR_TEXT_DARK);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.getBackground().equals(COLOR_GO)) {
                    btn.setBackground(new Color(200, 200, 200)); 
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(COLOR_PRIMARY)) {
                    btn.setBackground(COLOR_GO);
                }
            }
        });

        btn.setActionCommand(texto.trim());
        return btn;
    }

    public void marcarBotonActivo(JButton botonActivo) {
        JButton[] botones = {btnDashboard, btnConsultarMenu, btnRegTurno, btnHistorial, btnMonedero, btnPerfil, btnConfigCCB};

        for (JButton b : botones) {
            if (b == null) continue; // Si no es admin, btnConfigCCB es null

            if (b == botonActivo) {
                b.setBackground(COLOR_PRIMARY); 
                b.setForeground(Color.WHITE);   
            } else {
                b.setBackground(COLOR_GO);      
                b.setForeground(COLOR_TEXT_DARK); 
            }
        }
    }

    // Getters
    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnMenuDia() { return btnConsultarMenu; }
    public JButton getBtnRegTurno() { return btnRegTurno; }
    public JButton getBtnHistorial() { return btnHistorial; }
    public JButton getBtnPerfil() { return btnPerfil; }
    public JButton getBtnLogout() { return btnLogout; }
    public JButton getBtnConfigCCB() { return btnConfigCCB; }
    public JButton getBtnMonederoSidebar() { return btnMonedero; }
    public JButton getBtnMonedero() {
        return dashPanel != null ? dashPanel.getBtnMonedero() : null;
    }

    public void changeView(String nombreVista) {
        cardLayout.show(mainContainer, nombreVista);
        mainContainer.revalidate();
        mainContainer.repaint();
    }
}
