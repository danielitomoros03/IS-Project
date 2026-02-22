package com.example.Modelo;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FaceRecognitionModel {
    private static final int HASH_SIZE = 8;

    public boolean esReconocimientoValido(File fotoIngresada, File fotoBase, int umbral) throws IOException {
        long hashIngresada = calcularDHash(fotoIngresada);
        long hashBase = calcularDHash(fotoBase);
        int distancia = hammingDistance(hashIngresada, hashBase);
        return distancia <= umbral;
    }

    private long calcularDHash(File archivo) throws IOException {
        BufferedImage original = ImageIO.read(archivo);
        if (original == null) {
            throw new IOException("Imagen invalida o no soportada.");
        }

        BufferedImage escalada = new BufferedImage(HASH_SIZE + 1, HASH_SIZE, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = escalada.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, HASH_SIZE + 1, HASH_SIZE, null);
        g.dispose();

        long hash = 0L;
        int bit = 0;
        for (int y = 0; y < HASH_SIZE; y++) {
            for (int x = 0; x < HASH_SIZE; x++) {
                int izquierda = escalada.getRGB(x, y) & 0xFF;
                int derecha = escalada.getRGB(x + 1, y) & 0xFF;
                if (izquierda > derecha) {
                    hash |= (1L << bit);
                }
                bit++;
            }
        }
        return hash;
    }

    private int hammingDistance(long a, long b) {
        return Long.bitCount(a ^ b);
    }
}
