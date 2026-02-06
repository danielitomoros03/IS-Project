package IS-Project.comedor-ucv.src.main.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import PracticaJava.Inge.Modelo.LoginModel;
import PracticaJava.Inge.Vista.Login;
import PracticaJava.Inge.Vista.*;

public class LoginControl implements ActionListener{

    private Login vista;
    private LoginModel modelo;

    public LoginControl(){
        
        this.vista = new Login();                   //Inicializamos la vista y el modelo(instanciado)
        this.modelo = new LoginModel();        

        this.vista.addLoginListener(this);
        
        this.vista.setVisible(true);
    }

    private void handleLogin(){
        
        // Captura de datos con getters
        String email = vista.getEmail().getText();;            //Obtener datos de la vista
        
        String pass = new String(vista.getTxtPassword().getPassword()); 

        try{
            //Estado de carga

            //vista.getLblError().setVisible(false);

            vista.getBtnLogin().setEnabled(false);            //Desactivar botón mientras carga
            vista.getBtnLogin().setText("Validando...");

            String role = modelo.autenticar(email, pass);  //Llamar al modelo para autenticar

            //Esta instancia hace que al presionnar "Iniciar Sesion", cambie la vista
            //BienvenidoVista menu = new BienvenidoVista(email, role); //Se debe colocar la variable en vez de "Donander", esto es solo de prueba
            //menu.setVisible(true);
            // ABRE EL CONTROLADOR PRINCIPAL (No la vista directamente)
            new BienvenidoControl(email, role);
            vista.dispose();


            //Esto abre una ventana, se debe quitar al abrir la otra vista
            //JOptionPane.showMessageDialog(vista, "¡Bienvenido! " + role); //Exito


        } catch (Exception ex){
            // Caso error de sistema, se puede usar esto para ventanas de dialogo
            //JOptionPane.showMessageDialog(vista, "Error al conectar: " + ex.getMessage(), "Error de Login", JOptionPane.ERROR_MESSAGE);
            //ex.printStackTrace();                   // Para ver el error en consola

            //Por otro lado, que el mensaje de error lo de en la misma interfaz
            vista.showErrorMessage(ex.getMessage());

            // Si el error causó un bloqueo, deshabilitamos la vista
            if (modelo.isBloqueado()) {
                bloquearInterfaz();
            }
            
        } finally{
            
            // Limpieza final si no está bloqueado
            if (!modelo.isBloqueado()) {
                vista.getBtnLogin().setEnabled(true);
                vista.getBtnLogin().setText("Iniciar Sesión");
                vista.setTxtPassword("");
            } 

        }
    }


    // Método para gestionar el cambio visual tras el bloqueo
    private void bloquearInterfaz() {
        vista.getBtnLogin().setEnabled(false);
        vista.getBtnLogin().setText("BLOQUEADO");
        vista.getEmail().setEnabled(false);
        vista.getTxtPassword().setEnabled(false);
        vista.getLblError().setText("Usuario bloqueado por seguridad.");
    }


    // Método de la interfaz ActionListener (obligatorio por el implements)
    // Pregunta si se presiono el boton de Login para llamar a la funcion
    // que hace la autenticacion 
    @Override
    public void actionPerformed(ActionEvent e){
        // Si el objeto que generó el evento es el botón de login, no se necesita el if si hay un solo boton
        // Pero en caso de tener mas en algun momento, lo voy a dejar
        if(e.getSource() == vista.getBtnLogin()){
            System.out.println("Botón presionado!");
            handleLogin();
        }
    }

    /* Herramienta misteriosa que usaremos mas tarde
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnLogin()) {
            handleLogin();
        } else if (e.getSource() == vista.getBtnRegistro()) {
            irARegistro();
        } else if (e.getSource() == vista.getBtnSalir()) {
            System.exit(0);
        }
    } */
}
