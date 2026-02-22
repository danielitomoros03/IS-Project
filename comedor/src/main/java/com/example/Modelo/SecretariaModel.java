package com.example.Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SecretariaModel {
    private final String nombreArchivo = "Fotos_Secretaria.txt";

    public String obtenerRutaFoto(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",", 2);
                if (datos.length < 2) {
                    continue;
                }
                String emailArchivo = datos[0].trim();
                if (emailArchivo.equalsIgnoreCase(email)) {
                    return datos[1].trim();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer Fotos_Secretaria.txt: " + e.getMessage());
        }

        return null;
    }
}
