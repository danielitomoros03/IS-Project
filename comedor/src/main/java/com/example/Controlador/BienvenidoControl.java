package com.example.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import com.example.Vista.*;

public class BienvenidoControl implements ActionListener {

    private BienvenidoVista vista;

    // Recibimos los datos del usuario desde el Login
    public BienvenidoControl(String usuario, String rol) {
        this.vista = new BienvenidoVista(usuario, rol);

        this.vista.getBtnDashboard().addActionListener(this);
        this.vista.getBtnMenuDia().addActionListener(this);
        this.vista.getBtnLogout().addActionListener(this);

        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String comando = e.getActionCommand();
        //System.out.println("Comando detectado: " + comando);
        
        // Para la navegacion
        if ("Menu del Dia".equals(comando)) {
            vista.changeView("MENU_VISTA"); // Usamos el nombre clave que esta en la Vista
        }         
        else if ("Dashboard".equals(comando)) {
            vista.changeView("DASH_VISTA");
        } 
        else if (e.getSource() == vista.getBtnLogout()) {
            int confirm = JOptionPane.showConfirmDialog(vista, "¿Estás seguro de cerrar sesión?");
            if (confirm == JOptionPane.YES_OPTION) {
                vista.dispose();      // Cerramos el Dashboard
                new LoginControl();   // Reabrimos el Login
            }
        }
    }
}