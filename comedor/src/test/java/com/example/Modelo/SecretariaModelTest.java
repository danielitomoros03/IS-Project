package com.example.Modelo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SecretariaModelTest {

    private Path fotosSecretariaPath;
    private Path fotosDir;
    private Path fotoTemporal;
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
    public void obtenerRutaFoto_descubreFotoPorEmailYLaRegistra() throws IOException {
        Files.writeString(fotosSecretariaPath, "");
        fotoTemporal = fotosDir.resolve("pruebausuario_face.png");
        Files.write(fotoTemporal, new byte[] {1, 2, 3});

        SecretariaModel model = new SecretariaModel();
        String ruta = model.obtenerRutaFoto("pruebausuario@ucv.ve");

        assertNotNull(ruta);
        assertTrue(ruta.toLowerCase().endsWith("pruebausuario_face.png"));
        String contenido = Files.readString(fotosSecretariaPath);
        assertTrue(contenido.contains("pruebausuario@ucv.ve"));
        assertTrue(contenido.contains("fotos/pruebausuario_face.png"));
    }
}