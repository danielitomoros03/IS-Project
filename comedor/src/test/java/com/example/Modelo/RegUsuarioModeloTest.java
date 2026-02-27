package com.example.Modelo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegUsuarioModeloTest {

    private Path usuariosPath;
    private Path usuariosUcvPath;
    private byte[] backupUsuarios;
    private byte[] backupUsuariosUcv;
    private boolean hadUsuariosBackup;
    private boolean hadUsuariosUcvBackup;

    @BeforeEach
    void setUp() throws IOException {
        Path base = Paths.get("").toAbsolutePath();
        usuariosPath = base.resolve("Usuarios.txt");
        usuariosUcvPath = base.resolve("Usuarios_UCV.txt");

        if (Files.exists(usuariosPath)) {
            backupUsuarios = Files.readAllBytes(usuariosPath);
            hadUsuariosBackup = true;
        }
        if (Files.exists(usuariosUcvPath)) {
            backupUsuariosUcv = Files.readAllBytes(usuariosUcvPath);
            hadUsuariosUcvBackup = true;
        }

        Files.deleteIfExists(usuariosPath);
        Files.deleteIfExists(usuariosUcvPath);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(usuariosPath);
        Files.deleteIfExists(usuariosUcvPath);

        if (hadUsuariosBackup) {
            Files.write(usuariosPath, backupUsuarios);
        }
        if (hadUsuariosUcvBackup) {
            Files.write(usuariosUcvPath, backupUsuariosUcv);
        }
    }

    @Test
    void registrarUsuario_camposObligatorios() {
        RegUsuarioModelo model = new RegUsuarioModelo();
        assertFalse(model.registrarUsuario("", "", "", "", ""));
        assertFalse(model.registrarUsuario(null, null, null, null, null));
    }

    @Test
    void registrarUsuario_valido_escribeArchivo() throws IOException {
        RegUsuarioModelo model = new RegUsuarioModelo();
        assertTrue(model.registrarUsuario("", "user@ucv.ve", "clave", "Estudiante", ""));

        String contenido = Files.readString(usuariosPath);
        assertTrue(contenido.contains("N/A,user@ucv.ve,clave,Estudiante,N/A"));
    }

    @Test
    void existeEnUsuarios_y_obtenerNombreYRol() throws IOException {
        Files.writeString(usuariosPath, "Ana,ana@ucv.ve,pass,Estudiante,000\n");
        Files.writeString(usuariosUcvPath, "ana@ucv.ve,Ana,Estudiante,Faces,Escuela\n");

        RegUsuarioModelo model = new RegUsuarioModelo();
        assertTrue(model.existeEnUsuarios("ana@ucv.ve"));

        String[] datos = model.obtenerNombreYRolDesdeArchivo("ana@ucv.ve");
        assertNotNull(datos);
        assertEquals("Ana", datos[0]);
        assertEquals("Estudiante", datos[1]);
    }

    @Test
    void obtenerNombreYRol_noExiste() {
        RegUsuarioModelo model = new RegUsuarioModelo();
        assertNull(model.obtenerNombreYRolDesdeArchivo("no@ucv.ve"));
    }
}
