
package com.example.Modelo;

import java.io.FileWriter;
import java.io.IOException;


public class RegUsuarioModelo{

    private String nombreArchivo;

    public RegUsuarioModelo() {
        this.nombreArchivo = "Usuarios.txt";
    }

    public void registrarUsuario(String nombre, String email, String password, String rol) {
    //! si un campo esta vacio esta malo
        if (nombre == null || email == null || password == null || rol == null ||
            nombre.isEmpty() || email.isEmpty() || password.isEmpty() || rol.isEmpty()) {
            System.out.println("Error: Todos los campos son obligatorios.");
            return;
        }

    //! uno todos los datos
    String linea = nombre + "," + email + "," + password + "," + rol;

    //! sobreescribo lo que habia antes y guardo con false
        try {
            FileWriter escritor = new FileWriter(nombreArchivo, false);
        
            escritor.write(linea + "\n");
            escritor.close();
        
            System.out.println("Archivo sobrescrito con éxito.");

        } catch (IOException e) {
        System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}