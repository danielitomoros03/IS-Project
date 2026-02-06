package main.Controlador;

//import main.Vista.Login;
import main.Vista.BienvenidoVista;
import main.Controlador.LoginControl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

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
        System.out.println("Comando detectado: " + comando);
        
        // Para la navegacion
        if ("Menu del Dia".equals(comando)) {
            System.out.println("Cambiando a vista: Menu del Dia");
            vista.changeView("menuDiaVista"); // Usamos el nombre clave que esta en la Vista
        } else if ("Dashboard".equals(comando)) {
            System.out.println("Cambiando a vista: Dashboard");
            vista.changeView("dashVista");
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