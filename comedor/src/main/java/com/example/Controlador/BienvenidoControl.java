package com.example.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import com.example.Vista.*;

public class BienvenidoControl implements ActionListener {

    private BienvenidoVista vista;

    public BienvenidoControl(String usuario, String rol) {
        this.vista = new BienvenidoVista(usuario, rol);

        // Listeners
        this.vista.getBtnDashboard().addActionListener(this);
        this.vista.getBtnMenuDia().addActionListener(this);
        this.vista.getBtnRegTurno().addActionListener(this);
        this.vista.getBtnHistorial().addActionListener(this);
        this.vista.getBtnPerfil().addActionListener(this);
        this.vista.getBtnLogout().addActionListener(this);

        // --- IMPORTANTE: Estado inicial ---
        // Marcamos el Dashboard como activo al iniciar la ventana
        this.vista.marcarBotonActivo(this.vista.getBtnDashboard());

        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if ("Dashboard".equals(comando)) {
            vista.changeView("DASH_VISTA");
            // Cambiamos el color al botón correspondiente
            vista.marcarBotonActivo(vista.getBtnDashboard());
        } 
        else if ("Menu del Dia".equals(comando)) {
            vista.changeView("MENU_VISTA");
            // Cambiamos el color al botón correspondiente
            vista.marcarBotonActivo(vista.getBtnMenuDia());
        }         
        else if ("Registrar Turno".equals(comando)) {
            // Asegúrate de que en BienvenidoVista pusiste "TURNO_VISTA"
            vista.changeView("TURNO_VISTA");
            vista.marcarBotonActivo(vista.getBtnRegTurno());
        }
        else if ("Historial".equals(comando)) {
            // Este activará tu nuevo archivo HistorialPanel
            vista.changeView("HIST_VISTA");
            vista.marcarBotonActivo(vista.getBtnHistorial());
        }
        else if ("Perfil".equals(comando)) {
            vista.changeView("PERFIL_VISTA");
            vista.marcarBotonActivo(vista.getBtnPerfil());
        }
        else if (e.getSource() == vista.getBtnLogout()) {
            int confirm = JOptionPane.showConfirmDialog(vista, "¿Estás seguro de cerrar sesión?");
            if (confirm == JOptionPane.YES_OPTION) {
                vista.dispose();
                new LoginControl();
            }
        }
    }
}