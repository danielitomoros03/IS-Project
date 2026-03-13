package com.example.Modelo;

import java.io.File;
import java.io.IOException;

public class ValidacionFacialService {

    private final SecretariaModel secretariaModel;
    private final FaceRecognitionModel faceRecognitionModel;

    public ValidacionFacialService() {
        this.secretariaModel = new SecretariaModel();
        this.faceRecognitionModel = new FaceRecognitionModel();
    }

    public File obtenerFotoBase(String email) {
        return secretariaModel.obtenerArchivoFoto(email);
    }

    public File obtenerDirectorioFotos() {
        return secretariaModel.obtenerDirectorioFotos();
    }

    public boolean esImagenPermitida(File archivo) {
        if (archivo == null || !archivo.exists() || !archivo.isFile()) {
            return false;
        }

        String nombre = archivo.getName().toLowerCase();
        return nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || nombre.endsWith(".png");
    }

    public FaceRecognitionModel.ResultadoReconocimiento validarContraSecretaria(
        String email,
        File fotoIngresada,
        int umbral
    ) throws IOException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Debes indicar un correo valido.");
        }

        if (!esImagenPermitida(fotoIngresada)) {
            throw new IllegalArgumentException("Solo se permiten imagenes JPG o PNG.");
        }

        File fotoBase = obtenerFotoBase(email);
        if (fotoBase == null) {
            throw new IllegalStateException("No hay foto base registrada en Secretaria para este correo.");
        }

        return faceRecognitionModel.evaluarReconocimiento(fotoIngresada, fotoBase, umbral);
    }
}
