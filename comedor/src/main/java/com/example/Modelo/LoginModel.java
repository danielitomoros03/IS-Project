package com.example.Modelo;


public class LoginModel {

    private int intentosFallidos = 0;
    private final int MaxIntentos = 5;
    private boolean bloqueado = false;

    public String autenticar(String email, String password) throws Exception{

        //Verificación de bloqueo
        if(bloqueado){
            throw new Exception("Acceso denegado. Usuario bloqueado");
        }

        //Validación de campos vacíos
        if(email.isEmpty() || password.isEmpty()){
            throw new Exception("Por favor completa todos los campos");
        }

        //Validación de dominio
        if (!email.toLowerCase().endsWith("@ucv.ve")) {
            throw new Exception("Solo se permiten correos del dominio @ucv.ve");
        }

        //Verificación de credenciales. "credenciales es la password por ahora, aqui iria la variable que contiene la password correcta del usuario"
        if("cualquiera".equals(password)){
            intentosFallidos = 0; //Si es correcto, se reinicia el contador
            return email.contains("admin") ? "Adiministrador" : "Estudiante";
        } else {
            intentosFallidos++;
            if(intentosFallidos >= MaxIntentos){
                bloqueado = true;
                throw new Exception("Limite de intentos alcanzado. El susario ha sido bloqueado.");
            }

            throw new Exception("Credenciales incorrectas. Intentos: " + intentosFallidos + "/5");
        }

    }

    public boolean isBloqueado(){
        return bloqueado;
    }
    
}
