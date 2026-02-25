package com.example.Modelo;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class UsuarioGestionTest {

    @TempDir
    Path tempDir;

    private String originalUserDir;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());
    }

    @AfterEach
    @SuppressWarnings("unused")
    void tearDown() {
        System.setProperty("user.dir", originalUserDir);
    }

    @Test
    void registrarUsuarioYAutenticar() throws Exception {
        RegUsuarioModelo reg = new RegUsuarioModelo();
        boolean ok = reg.registrarUsuario("Usuario Demo", "usuario@ucv.ve", "123456", "Estudiante", "1111");

        LoginModel login = new LoginModel();
        String rol = login.autenticar("usuario@ucv.ve", "123456");

        assertTrue(ok);
        assertEquals("Estudiante", rol);
    }

    @Test
    void obtenerRolDesdeArchivoUcv() {
        RegUsuarioModelo reg = new RegUsuarioModelo();
        reg.registrarFacultadEscuela("persona@ucv.ve", "Persona", "Empleado", "Facultad", "Escuela");

        String rol = reg.obtenerRolDesdeArchivo("persona@ucv.ve");

        assertNotNull(rol);
        assertEquals("Empleado", rol);
    }
}
