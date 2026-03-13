package com.example.Modelo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SecretariaModelTest {

    private Path fotosSecretariaPath;
    private Path fotosDir;
    private Path fotoTemporal;
    private Path fotoOrigen;
    private Path fotoGuardada;
    private byte[] backup;
    private boolean hadBackup;

    @BeforeEach
    public void setUp() throws IOException {
        fotosSecretariaPath = Paths.get("").toAbsolutePath().resolve("Fotos_Secretaria.txt");
        fotosDir = Paths.get("").toAbsolutePath().resolve("fotos");
        Files.createDirectories(fotosDir);

        if (Files.exists(fotosSecretariaPath)) {
            backup = Files.readAllBytes(fotosSecretariaPath);
            hadBackup = true;
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(fotoTemporal);
        Files.deleteIfExists(fotoOrigen);
        Files.deleteIfExists(fotoGuardada);
        Files.deleteIfExists(fotosSecretariaPath);
        if (hadBackup) {
            Files.write(fotosSecretariaPath, backup);
        }
    }

    @Test
    public void obtenerRutaFoto_resuelveRutaRegistrada() throws IOException {
        Files.writeString(fotosSecretariaPath, "daniel@ucv.ve,fotos/DanielMorosFoto.jpg\n");

        SecretariaModel model = new SecretariaModel();
        String ruta = model.obtenerRutaFoto("daniel@ucv.ve");

        assertNotNull(ruta);
        assertTrue(ruta.toLowerCase().endsWith("danielmorosfoto.jpg"));
    }

    @Test
    public void obtenerRutaFoto_sinRegistroEnSecretaria_devuelveNull() throws IOException {
        Files.writeString(fotosSecretariaPath, "");
        fotoTemporal = fotosDir.resolve("pruebausuario_face.png");
        Files.write(fotoTemporal, new byte[] {1, 2, 3});

        SecretariaModel model = new SecretariaModel();
        String ruta = model.obtenerRutaFoto("pruebausuario@ucv.ve");

        assertNull(ruta);
        String contenido = Files.readString(fotosSecretariaPath);
        assertFalse(contenido.contains("pruebausuario@ucv.ve"));
    }

    @Test
    public void guardarFotoUsuario_copiaFotoYActualizaFotosSecretaria() throws IOException {
        Files.writeString(fotosSecretariaPath, "");
        fotoOrigen = Paths.get("").toAbsolutePath().resolve("origen_foto_prueba.jpg");
        Files.write(fotoOrigen, new byte[] {9, 8, 7, 6});

        SecretariaModel model = new SecretariaModel();
        File guardada = model.guardarFotoUsuario("perfiltest@ucv.ve", fotoOrigen.toFile());
        fotoGuardada = guardada.toPath();

        assertTrue(Files.exists(fotoGuardada));
        assertTrue(fotoGuardada.getFileName().toString().equalsIgnoreCase("perfiltest_foto.jpg"));

        String contenido = Files.readString(fotosSecretariaPath);
        assertTrue(contenido.contains("perfiltest@ucv.ve"));
        assertTrue(contenido.contains("fotos/perfiltest_foto.jpg"));
    }
}