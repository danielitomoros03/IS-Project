package com.example.Modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class RegUsuarioModelo{

    private String nombreArchivo;
    private String nombreArchivoFacultad;

    public RegUsuarioModelo() {
        this.nombreArchivo = "Usuarios.txt";
        this.nombreArchivoFacultad = "Usuarios_UCV.txt";
    }

    public boolean registrarUsuario(String nombre, String email, String password, String rol, String telf) {
    //Si un campo esta vacio esta malo
        if (nombre == null || password == null || rol == null || email == null ||
            nombre.isEmpty() || password.isEmpty() || rol.isEmpty() || email.isEmpty()) {
            System.out.println("Error: Todos los campos son obligatorios.");
            return false; 
        }

    //Uno todos los datos
    String linea = nombre + "," + email + "," + password + "," + rol + "," + telf;

    // Guardar en modo append para no sobrescribir usuarios existentes
        try {
            FileWriter escritor = new FileWriter(nombreArchivo, true);
            escritor.write(linea + System.lineSeparator());
            escritor.close();
            System.out.println("Usuario agregado con éxito.");
            return true;

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
            return false;
        }
    }

    public void registrarFacultadEscuela(String email, String nombre, String rol, String facultad, String escuela) {
        // Formato Usuarios_UCV.txt: email,nombre,rol,facultad,escuela
        String linea = email + "," + nombre + "," + rol + "," + facultad + "," + escuela;

        try {
            FileWriter escritor = new FileWriter(nombreArchivoFacultad, true);
            escritor.write(linea + System.lineSeparator());
            escritor.close();
        } catch (IOException e) {
            System.err.println("Error al escribir en Usuarios_UCV.txt: " + e.getMessage());
        }
    }

    public String obtenerRolDesdeArchivo(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivoFacultad))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Formato Usuarios_UCV.txt: email,nombre,rol,facultad,escuela
                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    String emailArchivo = datos[0].trim();
                    String rolArchivo = datos[2].trim();
                    if (emailArchivo.equalsIgnoreCase(email)) {
                        return rolArchivo;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer Usuarios_UCV.txt: " + e.getMessage());
        }

        return null;
    }
}