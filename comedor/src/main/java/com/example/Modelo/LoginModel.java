package com.example.Modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginModel {

    private int intentosFallidos = 0;
    private final int MaxIntentos = 5;
    private boolean bloqueado = false;
    private final String nombreArchivo = "Usuarios.txt";

    public String autenticar(String email, String password) throws Exception {

        // Verificación de bloqueo
        if (bloqueado) {
            throw new Exception("Acceso denegado. Usuario bloqueado");
        }

        // Validación de campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            throw new Exception("Por favor completa todos los campos");
        }

        // Validación de dominio
        if (!email.toLowerCase().endsWith("@ucv.ve")) {
            throw new Exception("Solo se permiten correos del dominio @ucv.ve");
        }

        // Búsqueda en el "archivo"
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            boolean usuarioEncontrado = false;
            String rolEncontrado = "";

            while ((linea = br.readLine()) != null) {
                // El orden en el txt es: nombre, email, password, rol, telf, fac, car
                String[] datos = linea.split(",");

                if (datos.length >= 4) {
                    String emailArchivo = datos[1].trim();
                    String passArchivo = datos[2].trim();
                    String rolArchivo = datos[3].trim();

                    if (emailArchivo.equalsIgnoreCase(email) && passArchivo.equals(password)) {
                        usuarioEncontrado = true;
                        rolEncontrado = rolArchivo;
                        break; // Salimos del bucle si lo encontramos
                    }
                }
            }

            if (usuarioEncontrado) {
                intentosFallidos = 0; // Reiniciamos contador en caso de exito
                return rolEncontrado;
            } else {
                // Si no se encontro,  la logica de intentos fallidos
                manejarFallo();
                return null; // Nunca llega aqui por el throw de manejarFallo
            }

        } catch (IOException e) {
            // Si el archivo no existe (nadie se ha registrado aun)
            throw new Exception("No hay usuarios registrados en el sistema.");
        }
    }

    // Extraemos la lógica de fallos para que el código sea más limpio
    private void manejarFallo() throws Exception {
        intentosFallidos++;
        if (intentosFallidos >= MaxIntentos) {
            bloqueado = true;
            throw new Exception("Limite de intentos alcanzado. El usuario ha sido bloqueado.");
        }
        throw new Exception("Credenciales incorrectas. Intentos: " + intentosFallidos + "/" + MaxIntentos);
    }

    public boolean isBloqueado() {
        return bloqueado;
    }
}