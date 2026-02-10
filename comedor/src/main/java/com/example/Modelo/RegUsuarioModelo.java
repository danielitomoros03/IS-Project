package com.example.Modelo;

import java.io.FileWriter;
import java.io.IOException;


public class RegUsuarioModelo{

    private String nombreArchivo;

    public RegUsuarioModelo() {
        this.nombreArchivo = "Usuarios.txt";
    }

    public boolean registrarUsuario(String nombre, String email, String password, String rol, String telf, String facu, String carrera) {
    //Si un campo esta vacio esta malo
        if (nombre == null || password == null || rol == null || email == null || facu == null || carrera == null ||
            nombre.isEmpty() || password.isEmpty() || rol.isEmpty() || email.isEmpty() || facu.isEmpty() || carrera.isEmpty()) {
            System.out.println("Error: Todos los campos son obligatorios.");
            return false;
        }

    //Uno todos los datos
    String linea = nombre + "," + email + "," + password + "," + rol + "," + telf + "," + facu + "," + carrera;

    //Sobreescribo lo que habia antes y guardo con false
        try {
            FileWriter escritor = new FileWriter(nombreArchivo, false);
        
            escritor.write(linea + "\n");
            escritor.close();
            System.out.println("Archivo sobrescrito con éxito.");
            return true;

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
            return false;
        }
    }
}