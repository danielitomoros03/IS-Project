package com.example.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.example.Vista.*;

public class AdminControl implements ActionListener{

    private AdminVista vista;
    
    public AdminControl(String usuario, String rol) {
        this.vista = new AdminVista(usuario, rol);

        this.vista.getBtnConfigurarMenu().addActionListener(this);
        this.vista.getBtnGenerarReporte().addActionListener(this);
        this.vista.getBtnConsultarConsumo().addActionListener(this);
        this.vista.getBtnLogout().addActionListener(this);

        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 3. Obtener el texto del botón presionado
        String comando = e.getActionCommand();

        if (comando.equals("Configurar Menú")) {
            vista.changeView("VISTA_CONFIG"); 
        } 
        else if (comando.equals("Generar Reporte")) {
            vista.changeView("VISTA_REPORTE");
        } 
        else if (comando.equals("Historial de Consumo")) {
            vista.changeView("VISTA_CONSUMO");
        } 
        else if (comando.equals("Cerrar Sesión")) {
            vista.dispose(); // Cierra la ventana actual
            // Aquí podrías instanciar el Login de nuevo
        }
    }


}
