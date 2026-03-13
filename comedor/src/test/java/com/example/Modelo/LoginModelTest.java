package com.example.Modelo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    void existeUsuario_retornaTrueSiExiste() throws IOException {
        writeUsuarios("Juan,user@ucv.ve,clave,Estudiante,000");

        LoginModel model = new LoginModel();
        assertTrue(model.existeUsuario("user@ucv.ve"));
    }

    @Test
    void existeUsuario_retornaFalseSiNoExiste() throws IOException {
        writeUsuarios("Juan,user@ucv.ve,clave,Estudiante,000");

        LoginModel model = new LoginModel();
        assertFalse(model.existeUsuario("otro@ucv.ve"));
    }

    @Test
    void generarCodigoRecuperacion_formatoValido() {
        LoginModel model = new LoginModel();
        String codigo = model.generarCodigoRecuperacion();

        assertNotNull(codigo);
        assertTrue(codigo.matches("\\d{6}"));
    }

    @Test
    void actualizarPassword_usuarioExistente_actualizaYPermiteLogin() throws Exception {
        writeUsuarios("Juan,user@ucv.ve,clave,Estudiante,000");
        LoginModel model = new LoginModel();

        assertTrue(model.actualizarPassword("user@ucv.ve", "nueva12"));

        String rol = model.autenticar("user@ucv.ve", "nueva12");
        assertEquals("Estudiante", rol);
    }

    @Test
    void actualizarPassword_usuarioNoExiste_retornaFalse() throws IOException {
        writeUsuarios("Juan,user@ucv.ve,clave,Estudiante,000");
        LoginModel model = new LoginModel();

        assertFalse(model.actualizarPassword("otro@ucv.ve", "nueva12"));
    }

    @Test
    void actualizarPassword_contrasenaVacia_lanzaError() throws IOException {
        writeUsuarios("Juan,user@ucv.ve,clave,Estudiante,000");
        LoginModel model = new LoginModel();

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> model.actualizarPassword("user@ucv.ve", "   ")
        );
        assertTrue(ex.getMessage().contains("no puede estar vacia"));
    }

    private void writeUsuarios(String... lineas) throws IOException {
        Path archivo = tempDir.resolve("Usuarios.txt");
        Files.write(archivo, String.join(System.lineSeparator(), lineas).getBytes());
    }
}
