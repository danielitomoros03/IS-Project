package PracticaJava.Inge.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import PracticaJava.Inge.Modelo.LoginModel;
import PracticaJava.Inge.Vista.Login;


public class LoginControl implements ActionListener{

    private Login vista;
    private LoginModel modelo;

    public LoginControl(){
        this.vista = new Login();
        //this.vista.setControlador();
        this.vista.setVisible(true);

        this.vista.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                //handleLogin();
            }
        });

        /*  private void handleLogin(){
            String email = vista.txtEmail.getText();
            String pass = new String(vista.txtPassword.getPassword());

            try{
                //Se debe pedir validacion al modelo :)
                String role = modelo.autenticar(email, pass);

                vista.btnLogin.setEnabled(false);
                vista.btnLogin.setText("Cargando...");
            } catch (Exception ex){
                
            }
        } */
        

    }


}
