package com.example.Modelo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class LoginModelTest {

    @TempDir
    Path tempDir;

    private String originalUserDir;

    @BeforeEach
    void setUp() {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());
    }

    @AfterEach
    void tearDown() {
        System.setProperty("user.dir", originalUserDir);
    }

    @Test
    void autenticar_demoAdmin_retornaAdministrador() throws Exception {
        LoginModel model = new LoginModel();
        String rol = model.autenticar("admin@ucv.ve", "cualquier");
        assertEquals("Administrador", rol);
    }

    @Test
    void autenticar_camposVacios_lanzaError() {
        LoginModel model = new LoginModel();
        Exception ex = assertThrows(Exception.class, () -> model.autenticar("", ""));
        assertTrue(ex.getMessage().contains("Por favor completa"));
    }

    @Test
    void autenticar_dominioInvalido_lanzaError() {
        LoginModel model = new LoginModel();
        Exception ex = assertThrows(Exception.class, () -> model.autenticar("user@gmail.com", "123"));
        assertTrue(ex.getMessage().contains("@ucv.ve"));
    }

    @Test
    void autenticar_usuarioValido_retornaRol() throws Exception {
        writeUsuarios("Juan,user@ucv.ve,clave,Estudiante,000");

        LoginModel model = new LoginModel();
        String rol = model.autenticar("user@ucv.ve", "clave");

        assertEquals("Estudiante", rol);
    }

    @Test
    void autenticar_intentosFallidos_bloquea() throws IOException {
        writeUsuarios("Ana,ana@ucv.ve,ok,Estudiante,000");

        LoginModel model = new LoginModel();
        for (int i = 1; i <= 5; i++) {
            Exception ex = assertThrows(Exception.class, () -> model.autenticar("ana@ucv.ve", "mal"));
            assertNotNull(ex.getMessage());
        }

        assertTrue(model.isBloqueado());
        Exception ex = assertThrows(Exception.class, () -> model.autenticar("ana@ucv.ve", "ok"));
        assertTrue(ex.getMessage().contains("bloqueado"));
    }

    private void writeUsuarios(String... lineas) throws IOException {
        Path archivo = tempDir.resolve("Usuarios.txt");
        Files.write(archivo, String.join(System.lineSeparator(), lineas).getBytes());
    }
}
