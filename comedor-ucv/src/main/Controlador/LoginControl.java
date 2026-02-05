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
        
        this.vista = new Login();                   //Inicializamos la vista y el modelo(instanciado)
        this.modelo = new LoginModel(); 

        this.vista.setVisible(true);        

        this.vista.addLoginListener(new ActionListener(){  // Agregamos el listener al botón de login
            @Override 
            public void actionPerformed(ActionEvent e){ 
                handleLogin();
            }
        });
    }

    private void handleLogin(){
         // Nota: AccedO directo a los campos asumiendo que son public o package-private
        String email = vista.txtEmail.getText();            //Obtener datos de la vista
        
        String pass = new String(vista.txtPassword.getPassword()); 
 
        if (email.isEmpty() || pass.isEmpty()){            // Validación de campos vacíos
            JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos.");
            return;
        }

        try{
            
            vista.btnLogin.setEnabled(false);            //Desactivar botón mientras carga
            vista.btnLogin.setText("Cargando...");

            String role = modelo.autenticar(email, pass);  //Llamar al modelo para autenticar

            if (role != null && !role.isEmpty()){
                JOptionPane.showMessageDialog(vista, "¡Bienvenido! Accediendo como: " + role); //Exito

                // Abrir la siguiente ventana según el rol
                //---CODIGO SEGUN ROLLLLLLLLLLLLLLLLLLL------------------
                // MenuPrincipal menu = new MenuPrincipal(role);
                // menu.setVisible(true);
                // vista.dispose(); // Cerrar login
                
            } else {
                // Credenciales inválidas
                JOptionPane.showMessageDialog(vista, "Correo o contraseña incorrectos", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex){
            // Caso error de sistema
            JOptionPane.showMessageDialog(vista, "Error al conectar: " + ex.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();                   // Para ver el error en consola
            
        } finally{
            
            vista.btnLogin.setEnabled(true);    //Restaurar el botón
            vista.btnLogin.setText("Iniciar Sesión");
            
            vista.txtPassword.setText("");        // Limpiar campo contraseña por seguridad
        }
    }

    // Método de la interfaz ActionListener (obligatorio por el implements)
    // No lo usamos directamente porque usamos la clase anónima arriba, pero hay que tenerlo presente, que ladilloso
    @Override
    public void actionPerformed(ActionEvent e){
        // Dejar vacío o delegar o golpear 
    }
}



