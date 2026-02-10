package com.example.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import com.example.Vista.*;

public class BienvenidoControl implements ActionListener {

    private BienvenidoVista vista;

    public BienvenidoControl(String usuario, String rol) {
        this.vista = new BienvenidoVista(usuario, rol);

        this.vista.getBtnDashboard().addActionListener(this);
        this.vista.getBtnMenuDia().addActionListener(this);
        this.vista.getBtnRegTurno().addActionListener(this);
        this.vista.getBtnHistorial().addActionListener(this);
        this.vista.getBtnPerfil().addActionListener(this);
        this.vista.getBtnLogout().addActionListener(this);

        if (this.vista.getBtnConfigCCB() != null) {
            this.vista.getBtnConfigCCB().addActionListener(this);
        }

        // Estado inicial
        this.vista.marcarBotonActivo(this.vista.getBtnDashboard());
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if ("Dashboard".equals(comando)) {
            vista.changeView("DASH_VISTA");
            vista.marcarBotonActivo(vista.getBtnDashboard());
        } 
        else if ("Menu del Dia".equals(comando)) {
            vista.changeView("MENU_VISTA");
            vista.marcarBotonActivo(vista.getBtnMenuDia());
        }         
        else if ("Registrar Turno".equals(comando)) {
            vista.changeView("TURNO_VISTA");
            vista.marcarBotonActivo(vista.getBtnRegTurno());
        }
        else if ("Historial".equals(comando)) {
            vista.changeView("HIST_VISTA");
            vista.marcarBotonActivo(vista.getBtnHistorial());
        }
        else if ("Perfil".equals(comando)) {
            vista.changeView("PERFIL_VISTA");
            vista.marcarBotonActivo(vista.getBtnPerfil());
        }
        // --- CASO NUEVO ---
        else if ("Configuración CCB".equals(comando)) {
            vista.changeView("CONFIG_CCB_VISTA");
            vista.marcarBotonActivo(vista.getBtnConfigCCB());
        }
        // Logout
        else if (e.getSource() == vista.getBtnLogout()) {
            int confirm = JOptionPane.showConfirmDialog(vista, "¿Estás seguro de cerrar sesión?");
            if (confirm == JOptionPane.YES_OPTION) {
                vista.dispose();
                // new LoginControl(); 
            }
        }
    }
}