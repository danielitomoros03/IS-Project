package comedorUcv.src.main.Modelo;

public class Estudiante extends Usuario{
    //Demas atributos propios de estudiante
    
    public Estudiante(String email, String password, String nombre){
        super(email, password, nombre); 
    }

    @Override
    public String getRol(){ return "Estudiante"; }
}
