package comedorUcv.src.main.Modelo;

public class Administrador extends Usuario{
    
    public Administrador(String email, String password, String nombre){
        super(email, password, nombre); 
    }

    @Override
    public String getRol(){ return "Administrador"; }
}
