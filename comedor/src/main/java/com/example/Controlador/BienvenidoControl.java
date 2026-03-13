package com.example.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.example.Vista.BienvenidoVista;

public class BienvenidoControl implements ActionListener {
    private BienvenidoVista vista;
    private String usuarioEmail;

    public BienvenidoControl(String usuario, String rol) {
        this.vista = new BienvenidoVista(usuario, rol);
        this.usuarioEmail = usuario;

        this.vista.getBtnDashboard().addActionListener(this);
        this.vista.getBtnMenuDia().addActionListener(this);
        this.vista.getBtnRegTurno().addActionListener(this);
        this.vista.getBtnMonederoSidebar().addActionListener(this);
        this.vista.getBtnPerfil().addActionListener(this);
        this.vista.getBtnLogout().addActionListener(this);

        if (this.vista.getBtnMonedero() != null) {
            this.vista.getBtnMonedero().addActionListener(this);
        }


        // Estado inicial
        this.vista.marcarBotonActivo(this.vista.getBtnDashboard());
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == vista.getBtnDashboard()) {
            vista.changeView("DASH_VISTA");
            vista.marcarBotonActivo(vista.getBtnDashboard());
        } 
        else if (source == vista.getBtnMenuDia()) {
            vista.changeView("MENU_VISTA");
            vista.marcarBotonActivo(vista.getBtnMenuDia());
        }         
        else if (source == vista.getBtnRegTurno()) {
            vista.changeView("TURNO_VISTA");
            vista.marcarBotonActivo(vista.getBtnRegTurno());
        }
        else if (source == vista.getBtnPerfil()) {
            vista.changeView("PERFIL_VISTA");
            vista.marcarBotonActivo(vista.getBtnPerfil());
        }
        else if (source == vista.getBtnMonederoSidebar() || source == vista.getBtnMonedero()) {
            vista.marcarBotonActivo(vista.getBtnMonederoSidebar());
            new MonederoControl(vista, usuarioEmail);
        }
        // Logout
        else if (source == vista.getBtnLogout()) {
            int confirm = JOptionPane.showConfirmDialog(
                vista,
                "¿Estás seguro de cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                vista.dispose();
                // new LoginControl(); 
            }
        }
    }
}