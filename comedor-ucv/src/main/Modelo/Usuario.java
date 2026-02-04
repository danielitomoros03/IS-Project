package PracticaJava.Inge.Modelo;

public abstract class Usuario {

    protected String email;
    protected String password;
    protected String nombre;

    public Usuario(String email, String password){
        this.email = email;
        this.password = password;
        this.nombre = nombre;
    }

    public abstract String getRol();
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    
}
