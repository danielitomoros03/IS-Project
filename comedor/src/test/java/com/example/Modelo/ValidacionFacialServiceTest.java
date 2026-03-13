package com.example.Modelo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidacionFacialServiceTest {

    private Path fotosSecretariaPath;
    private Path fotosDir;
    private Path fotoBase;
    private Path fotoIgual;
    private Path fotoDistinta;
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

        fotoBase = fotosDir.resolve("validacion_base.png");
        fotoIgual = fotosDir.resolve("validacion_igual.png");
        fotoDistinta = fotosDir.resolve("validacion_distinta.png");

        ImageIO.write(crearPatronA(), "png", fotoBase.toFile());
        ImageIO.write(crearPatronA(), "png", fotoIgual.toFile());
        ImageIO.write(crearPatronB(), "png", fotoDistinta.toFile());

        Files.writeString(fotosSecretariaPath, "validacion@ucv.ve,fotos/validacion_base.png\n");
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(fotoBase);
        Files.deleteIfExists(fotoIgual);
        Files.deleteIfExists(fotoDistinta);
        Files.deleteIfExists(fotosSecretariaPath);
        if (hadBackup) {
            Files.write(fotosSecretariaPath, backup);
        }
    }

    @Test
    public void validarContraSecretaria_coincidente_devuelveValido() throws IOException {
        ValidacionFacialService service = new ValidacionFacialService();

        FaceRecognitionModel.ResultadoReconocimiento resultado = service.validarContraSecretaria(
            "validacion@ucv.ve",
            fotoIgual.toFile(),
            10
        );

        assertNotNull(resultado);
        assertTrue(resultado.esValido());
    }

    @Test
    public void validarContraSecretaria_distinta_devuelveInvalido() throws IOException {
        ValidacionFacialService service = new ValidacionFacialService();

        FaceRecognitionModel.ResultadoReconocimiento resultado = service.validarContraSecretaria(
            "validacion@ucv.ve",
            fotoDistinta.toFile(),
            10
        );

        assertFalse(resultado.esValido());
    }

    @Test
    public void validarContraSecretaria_sinFotoBase_lanzaError() {
        ValidacionFacialService service = new ValidacionFacialService();

        IllegalStateException ex = assertThrows(
            IllegalStateException.class,
            () -> service.validarContraSecretaria("sinfoto@ucv.ve", fotoIgual.toFile(), 10)
        );
        assertTrue(ex.getMessage().contains("No hay identificacion facial registrada en Secretaria"));
    }

    @Test
    public void validarContraSecretaria_archivoNoImagen_lanzaError() throws IOException {
        ValidacionFacialService service = new ValidacionFacialService();
        Path txt = fotosDir.resolve("no_imagen.txt");
        Files.writeString(txt, "no es una imagen");

        try {
            IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validarContraSecretaria("validacion@ucv.ve", txt.toFile(), 10)
            );
            assertTrue(ex.getMessage().contains("Solo se permiten imagenes"));
        } finally {
            Files.deleteIfExists(txt);
        }
    }

    private BufferedImage crearPatronA() {
        BufferedImage imagen = new BufferedImage(120, 120, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagen.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 120, 120);
        g.setColor(Color.BLACK);
        g.fillOval(30, 20, 60, 60);
        g.fillRect(40, 90, 40, 10);
        g.dispose();
        return imagen;
    }

    private BufferedImage crearPatronB() {
        BufferedImage imagen = new BufferedImage(120, 120, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagen.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 120, 120);
        g.setColor(Color.WHITE);
        g.fillRect(15, 15, 20, 20);
        g.fillRect(85, 15, 20, 20);
        g.fillRect(20, 85, 80, 20);
        g.dispose();
        return imagen;
    }
}
