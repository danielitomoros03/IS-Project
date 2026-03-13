package com.example.Modelo;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FaceRecognitionModel {
    private static final int HASH_SIZE = 8;
    private static final int COMPARISON_SIZE = 64;
    private static final int HISTOGRAM_BUCKETS = 32;
    private static final double MIN_SCORE_VALIDACION = 0.55;
    private static final double MIN_SIMILITUD_PIXELES = 0.45;

    public boolean esReconocimientoValido(File fotoIngresada, File fotoBase, int umbral) throws IOException {
        return evaluarReconocimiento(fotoIngresada, fotoBase, umbral).esValido();
    }

    public ResultadoReconocimiento evaluarReconocimiento(File fotoIngresada, File fotoBase, int umbral) throws IOException {
        BufferedImage imagenIngresada = leerImagen(fotoIngresada);
        BufferedImage imagenBase = leerImagen(fotoBase);

        long hashIngresada = calcularDHash(imagenIngresada);
        long hashBase = calcularDHash(imagenBase);
        int distancia = hammingDistance(hashIngresada, hashBase);

        BufferedImage ingresadaEscalada = escalarAGris(imagenIngresada, COMPARISON_SIZE, COMPARISON_SIZE);
        BufferedImage baseEscalada = escalarAGris(imagenBase, COMPARISON_SIZE, COMPARISON_SIZE);

        double similitudHash = 1.0 - (distancia / 64.0);
        double similitudHistograma = calcularSimilitudHistograma(ingresadaEscalada, baseEscalada);
        double similitudPixeles = calcularSimilitudPixeles(ingresadaEscalada, baseEscalada);
        double puntajeFinal = (similitudHash * 0.45) + (similitudHistograma * 0.20) + (similitudPixeles * 0.35);

        boolean valido = distancia <= umbral
            && puntajeFinal >= MIN_SCORE_VALIDACION
            && similitudPixeles >= MIN_SIMILITUD_PIXELES;
        return new ResultadoReconocimiento(valido, distancia, similitudHistograma, similitudPixeles, puntajeFinal);
    }

    private BufferedImage leerImagen(File archivo) throws IOException {
        BufferedImage original = ImageIO.read(archivo);
        if (original == null) {
            throw new IOException("Imagen invalida o no soportada.");
        }
        return original;
    }

    private long calcularDHash(BufferedImage original) {
        BufferedImage escalada = escalarAGris(original, HASH_SIZE + 1, HASH_SIZE);

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

    private BufferedImage escalarAGris(BufferedImage original, int width, int height) {
        BufferedImage escalada = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = escalada.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();
        return escalada;
    }

    private double calcularSimilitudHistograma(BufferedImage imagenA, BufferedImage imagenB) {
        int[] histA = construirHistograma(imagenA);
        int[] histB = construirHistograma(imagenB);

        int totalPixeles = imagenA.getWidth() * imagenA.getHeight();
        double diferencia = 0.0;
        for (int i = 0; i < HISTOGRAM_BUCKETS; i++) {
            diferencia += Math.abs(histA[i] - histB[i]) / (double) totalPixeles;
        }

        double similitud = 1.0 - (diferencia / 2.0);
        return limitarEntreCeroYUno(similitud);
    }

    private int[] construirHistograma(BufferedImage imagen) {
        int[] histograma = new int[HISTOGRAM_BUCKETS];
        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                int valor = imagen.getRGB(x, y) & 0xFF;
                int bucket = Math.min((valor * HISTOGRAM_BUCKETS) / 256, HISTOGRAM_BUCKETS - 1);
                histograma[bucket]++;
            }
        }
        return histograma;
    }

    private double calcularSimilitudPixeles(BufferedImage imagenA, BufferedImage imagenB) {
        double diferenciaAcumulada = 0.0;
        int total = imagenA.getWidth() * imagenA.getHeight();

        for (int y = 0; y < imagenA.getHeight(); y++) {
            for (int x = 0; x < imagenA.getWidth(); x++) {
                int pixelA = imagenA.getRGB(x, y) & 0xFF;
                int pixelB = imagenB.getRGB(x, y) & 0xFF;
                diferenciaAcumulada += Math.abs(pixelA - pixelB) / 255.0;
            }
        }

        double similitud = 1.0 - (diferenciaAcumulada / total);
        return limitarEntreCeroYUno(similitud);
    }

    private int hammingDistance(long a, long b) {
        return Long.bitCount(a ^ b);
    }

    private double limitarEntreCeroYUno(double valor) {
        return Math.max(0.0, Math.min(1.0, valor));
    }

    public static class ResultadoReconocimiento {
        private final boolean valido;
        private final int distanciaHash;
        private final double similitudHistograma;
        private final double similitudPixeles;
        private final double puntajeFinal;

        public ResultadoReconocimiento(
            boolean valido,
            int distanciaHash,
            double similitudHistograma,
            double similitudPixeles,
            double puntajeFinal
        ) {
            this.valido = valido;
            this.distanciaHash = distanciaHash;
            this.similitudHistograma = similitudHistograma;
            this.similitudPixeles = similitudPixeles;
            this.puntajeFinal = puntajeFinal;
        }

        public boolean esValido() {
            return valido;
        }

        public int getDistanciaHash() {
            return distanciaHash;
        }

        public double getSimilitudHistograma() {
            return similitudHistograma;
        }

        public double getSimilitudPixeles() {
            return similitudPixeles;
        }

        public double getPuntajeFinal() {
            return puntajeFinal;
        }
    }
}
