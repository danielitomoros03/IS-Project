package com.example.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import com.example.Vista.*;

public class AdminControl implements ActionListener{

    private AdminVista vista;
    
    public AdminControl(String usuario, String rol) {
        this.vista = new AdminVista(usuario, rol);

        //Para los botones de Menu
        this.vista.getBtnDasboard().addActionListener(this);
        this.vista.getBtnGestionMenu().addActionListener(this);
        this.vista.getBtnGestionInsumo().addActionListener(this);
        this.vista.getBtnReporte().addActionListener(this);
        this.vista.getBtnPerfil().addActionListener(this);
        this.vista.getBtnLogout().addActionListener(this);

        this.vista.getConfigMenuPanel().getBtnCrear().addActionListener(this);
        
        this.vista.changeView("DASH_VISTA");
        this.vista.marcarBotonActivo(this.vista.getBtnDasboard());
    
        this.vista.setExtendedState(AdminVista.MAXIMIZED_BOTH);
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 3. Obtener el texto del botón presionado
        String comando = e.getActionCommand();

        if ("Dashboard".equals(comando)) {
            vista.changeView("DASH_VISTA");
            // Cambiamos el color al botón correspondiente
            vista.marcarBotonActivo(vista.getBtnDasboard());
        } 
        else if ("Gestión de Menús".equals(comando)) {
            vista.changeView("MENU_VISTA");
            vista.marcarBotonActivo(vista.getBtnGestionMenu());
        }         
        else if ("+ Crear Nuevo Menú".equals(comando)) {
            abrirFormularioMenu();
        }
        else if ("Gestión de Insumos".equals(comando)) {
            vista.changeView("INSUMOS_VISTA");
            vista.marcarBotonActivo(vista.getBtnGestionInsumo());
        }
        else if ("Reportes".equals(comando)) {
            vista.changeView("REPORTE_VISTA");
            vista.marcarBotonActivo(vista.getBtnReporte());
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

    private void abrirFormularioMenu() {
        CrearMenuDialog dialogo = new CrearMenuDialog(vista);
    
        // Aquí se puede añadir un listener al botón de guardar del diálogo
        // para que el controlador maneje la inserción a la Base de Datos
        dialogo.getBtnSave().addActionListener(saveEvt -> {
        // Lógica para capturar datos y enviarlos al MODELO
        System.out.println("Enviando datos al modelo de base de datos.");
        dialogo.dispose();
        JOptionPane.showMessageDialog(vista, "¡Menú creado exitosamente!");
        });

        dialogo.setVisible(true);
    }

}
