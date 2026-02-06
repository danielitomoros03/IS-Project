package main.Modelo;

public class Estudiante extends Usuario{
    //Demas atributos propios de estudiante
    
    public Estudiante(String email, String password, String nombre){
        super(email, password); 
    }

    @Override
    public String getRol(){ return "Estudiante"; }
}
