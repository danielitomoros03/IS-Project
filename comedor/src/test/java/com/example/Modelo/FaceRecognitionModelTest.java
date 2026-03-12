package com.example.Modelo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class FaceRecognitionModelTest {

    @Test
    public void evaluarReconocimiento_imagenesIdenticas_validaCorrectamente() throws IOException {
        Path dir = Files.createTempDirectory("face-model-test");
        try {
            Path base = dir.resolve("base.png");
            Path comparada = dir.resolve("comparada.png");

            guardarImagen(base, crearImagenPatronClaro());
            guardarImagen(comparada, crearImagenPatronClaro());

            FaceRecognitionModel model = new FaceRecognitionModel();
            FaceRecognitionModel.ResultadoReconocimiento resultado = model.evaluarReconocimiento(
                comparada.toFile(),
                base.toFile(),
                10
            );

            assertTrue(resultado.esValido());
            assertTrue(resultado.getPuntajeFinal() >= 0.55);
        } finally {
            borrarDirectorio(dir);
        }
    }

    @Test
    public void evaluarReconocimiento_imagenesMuyDistintas_rechazaCoincidencia() throws IOException {
        Path dir = Files.createTempDirectory("face-model-test");
        try {
            Path base = dir.resolve("base.png");
            Path comparada = dir.resolve("comparada.png");

            guardarImagen(base, crearImagenPatronClaro());
            guardarImagen(comparada, crearImagenPatronOscuro());

            FaceRecognitionModel model = new FaceRecognitionModel();
            FaceRecognitionModel.ResultadoReconocimiento resultado = model.evaluarReconocimiento(
                comparada.toFile(),
                base.toFile(),
                10
            );

            assertFalse(resultado.esValido());
        } finally {
            borrarDirectorio(dir);
        }
    }

    private void guardarImagen(Path path, BufferedImage imagen) throws IOException {
        ImageIO.write(imagen, "png", path.toFile());
    }

    private BufferedImage crearImagenPatronClaro() {
        BufferedImage imagen = new BufferedImage(120, 120, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagen.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 120, 120);
        g.setColor(Color.BLACK);
        g.fillOval(30, 20, 60, 60);
        g.fillRect(40, 85, 40, 12);
        g.dispose();
        return imagen;
    }

    private BufferedImage crearImagenPatronOscuro() {
        BufferedImage imagen = new BufferedImage(120, 120, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagen.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 120, 120);
        g.setColor(Color.WHITE);
        g.fillRect(15, 15, 25, 25);
        g.fillRect(80, 18, 25, 25);
        g.fillRect(25, 80, 70, 20);
        g.dispose();
        return imagen;
    }

    private void borrarDirectorio(Path dir) throws IOException {
        if (dir == null || Files.notExists(dir)) {
            return;
        }
        Files.walk(dir)
            .sorted((a, b) -> b.compareTo(a))
            .forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }
}