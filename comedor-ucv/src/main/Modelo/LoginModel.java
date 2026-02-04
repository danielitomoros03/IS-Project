package PracticaJava.Inge.Modelo;

import java.util.List;


public class LoginModel {

    private int intentosFallidos = 0;
    private final int MaxIntentos = 5;
    private boolean bloqueado = false;

    public String autenticar(String email, String password) throws Exception{

        if(bloqueado){
            throw new Exception("Usuario bloqueado");
        }

        if(email.isEmpty() || password.isEmpty()){
            throw new Exception("Por favor completa todos los campos");
        }

        //Solo acepta "cualquiera"
        if("cualquiera".equals(password)){
            intentosFallidos = 0; //Si es correcto, se reinicia el contador
            return email.contains("admin") ? "Adiministrador" : "Estudiante";
        } else {
            intentosFallidos++;
            if(intentosFallidos >= MaxIntentos){
                bloqueado = true;
                throw new Exception("Limite de intentos alcanzado. Acceso bloqueado.");
            }

            throw new Exception("Credenciales incorrectas.");
        }

    }

    public boolean isBloqueado(){
        return bloqueado;
    }
    
}
