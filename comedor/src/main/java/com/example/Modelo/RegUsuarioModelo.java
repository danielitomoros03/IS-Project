package com.example.Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class RegUsuarioModelo{

    private String nombreArchivo;
    private String nombreArchivoFacultad;

    public RegUsuarioModelo() {
        this.nombreArchivo = "Usuarios.txt";
        this.nombreArchivoFacultad = "Usuarios_UCV.txt";
    }

    public boolean registrarUsuario(String nombre, String email, String password, String rol, String telf) {
        if (email == null || password == null || rol == null ||
            email.isEmpty() || password.isEmpty() || rol.isEmpty()) {
            System.out.println("Error: Todos los campos son obligatorios.");
            return false; 
        }

        if (nombre == null || nombre.isEmpty()) {
            nombre = "N/A";
        }
        if (telf == null || telf.isEmpty()) {
            telf = "N/A";
        }

    //Uno todos los datos
    String linea = nombre + "," + email + "," + password + "," + rol + "," + telf;

    // Guardar en modo append para no sobrescribir usuarios existentes
        try {
            FileWriter escritor = new FileWriter(resolveArchivo(nombreArchivo), true);
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
            FileWriter escritor = new FileWriter(resolveArchivo(nombreArchivoFacultad), true);
            escritor.write(linea + System.lineSeparator());
            escritor.close();
        } catch (IOException e) {
            System.err.println("Error al escribir en Usuarios_UCV.txt: " + e.getMessage());
        }
    }

    public String[] obtenerNombreYRolDesdeArchivo(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        email = email.trim();

        File archivo = resolveArchivo(nombreArchivoFacultad);
        if (archivo == null || !archivo.exists()) {
            System.err.println("No se encontro el archivo: " + nombreArchivoFacultad);
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Formato Usuarios_UCV.txt: email,nombre,rol,facultad,escuela
                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    String emailArchivo = datos[0].trim();
                    if (emailArchivo.equalsIgnoreCase(email)) {
                        String nombre = datos[1].trim();
                        String rol = datos[2].trim();
                        return new String[] { nombre, rol };
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer Usuarios_UCV.txt: " + e.getMessage());
        }

        return null;
    }

    public boolean existeEnUsuarios(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        email = email.trim();

        File archivo = resolveArchivo(nombreArchivo);
        if (archivo == null || !archivo.exists()) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 2) {
                    String emailArchivo = datos[1].trim();
                    if (emailArchivo.equalsIgnoreCase(email)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer Usuarios.txt: " + e.getMessage());
        }

        return false;
    }

    private File resolveArchivo(String nombre) {
        Path base = Paths.get("").toAbsolutePath();

        for (int i = 0; i < 4; i++) {
            Path directo = base.resolve(nombre);
            File archivo = directo.toFile();
            if (archivo.exists()) {
                return archivo;
            }

            Path alterno = base.resolve("comedor").resolve(nombre);
            File archivoAlterno = alterno.toFile();
            if (archivoAlterno.exists()) {
                return archivoAlterno;
            }

            Path parent = base.getParent();
            if (parent == null) {
                break;
            }
            base = parent;
        }

        return Paths.get(nombre).toFile();
    }
}