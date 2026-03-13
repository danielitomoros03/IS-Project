package com.example.Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class LoginModel {

    private int intentosFallidos = 0;
    private final int MaxIntentos = 5;
    private boolean bloqueado = false;
    private final String nombreArchivo = "Usuarios.txt";
    private static final String DEMO_ADMIN_EMAIL = "admin@ucv.ve";
    private final SecureRandom random = new SecureRandom();

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

        String emailNormalizado = email.trim().toLowerCase();

        if (esCredencialDemoAdmin(emailNormalizado)) {
            return "Administrador";
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

    private boolean esCredencialDemoAdmin(String emailNormalizado) {
        return DEMO_ADMIN_EMAIL.equals(emailNormalizado);
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

    public boolean existeUsuario(String email) throws IOException {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 2 && datos[1].trim().equalsIgnoreCase(email.trim())) {
                    return true;
                }
            }
        }

        return false;
    }

    public String generarCodigoRecuperacion() {
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }

    public boolean actualizarPassword(String email, String nuevaPassword) throws IOException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Correo invalido.");
        }
        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no puede estar vacia.");
        }

        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            return false;
        }

        List<String> lineas = Files.readAllLines(archivo.toPath());
        List<String> actualizadas = new ArrayList<>();
        boolean encontrado = false;

        for (String linea : lineas) {
            String[] datos = linea.split(",", -1);
            if (datos.length >= 4 && datos[1].trim().equalsIgnoreCase(email.trim())) {
                datos[2] = nuevaPassword;
                encontrado = true;
            }
            actualizadas.add(String.join(",", datos));
        }

        if (!encontrado) {
            return false;
        }

        try (FileWriter escritor = new FileWriter(archivo, false)) {
            for (String lineaActualizada : actualizadas) {
                escritor.write(lineaActualizada + System.lineSeparator());
            }
        }

        return true;
    }

    public void desbloquearDespuesDeRecuperacion() {
        intentosFallidos = 0;
        bloqueado = false;
    }
}