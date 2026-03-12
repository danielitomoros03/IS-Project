package com.example.Modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SecretariaModel {
    private final String nombreArchivo = "Fotos_Secretaria.txt";

    public String obtenerRutaFoto(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        File archivo = resolveArchivoDatos();
        if (!archivo.exists()) {
            return descubrirFotoYRegistrar(email);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",", 2);
                if (datos.length < 2) {
                    continue;
                }
                String emailArchivo = datos[0].trim();
                if (emailArchivo.equalsIgnoreCase(email)) {
                    File foto = resolveRutaFoto(datos[1].trim());
                    if (foto != null && foto.exists()) {
                        return foto.getAbsolutePath();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer Fotos_Secretaria.txt: " + e.getMessage());
        }

        return descubrirFotoYRegistrar(email);
    }

    public File obtenerArchivoFoto(String email) {
        String ruta = obtenerRutaFoto(email);
        if (ruta == null || ruta.trim().isEmpty()) {
            return null;
        }

        File foto = new File(ruta);
        return foto.exists() ? foto : null;
    }

    public File obtenerDirectorioFotos() {
        List<File> candidatos = new ArrayList<>();
        Path base = Paths.get("").toAbsolutePath();

        for (int i = 0; i < 4; i++) {
            candidatos.add(base.resolve("fotos").toFile());
            candidatos.add(base.resolve("comedor").resolve("fotos").toFile());

            Path parent = base.getParent();
            if (parent == null) {
                break;
            }
            base = parent;
        }

        for (File candidato : candidatos) {
            if (candidato.exists() && candidato.isDirectory()) {
                return candidato;
            }
        }

        return null;
    }

    private String descubrirFotoYRegistrar(String email) {
        File foto = descubrirFotoPorEmail(email);
        if (foto == null) {
            return null;
        }

        try {
            registrarRutaFoto(email, foto);
        } catch (IOException e) {
            System.err.println("Error al registrar ruta de foto en Secretaria: " + e.getMessage());
        }
        return foto.getAbsolutePath();
    }

    private File descubrirFotoPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        File directorioFotos = obtenerDirectorioFotos();
        if (directorioFotos == null) {
            return null;
        }

        File[] archivos = directorioFotos.listFiles((dir, name) -> esImagenPermitida(name));
        if (archivos == null || archivos.length == 0) {
            return null;
        }

        String localPart = email.trim();
        int at = localPart.indexOf('@');
        if (at >= 0) {
            localPart = localPart.substring(0, at);
        }

        List<String> tokens = extraerTokens(localPart);
        if (tokens.isEmpty()) {
            return null;
        }

        for (File archivo : archivos) {
            String nombreNormalizado = normalizarTexto(archivo.getName());
            for (String token : tokens) {
                if (nombreNormalizado.contains(token)) {
                    return archivo;
                }
            }
        }

        return null;
    }

    private List<String> extraerTokens(String valor) {
        List<String> tokens = new ArrayList<>();
        String[] partes = normalizarTexto(valor).split("\\s+");
        for (String parte : partes) {
            if (parte.length() >= 3) {
                tokens.add(parte);
            }
        }
        return tokens;
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return "";
        }
        return valor
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9]", " ")
            .trim();
    }

    private boolean esImagenPermitida(String name) {
        String nombre = name.toLowerCase(Locale.ROOT);
        return nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || nombre.endsWith(".png");
    }

    private File resolveRutaFoto(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            return null;
        }

        File directa = new File(ruta);
        if (directa.exists()) {
            return directa;
        }

        Path base = Paths.get("").toAbsolutePath();
        for (int i = 0; i < 4; i++) {
            File candidato = base.resolve(ruta).toFile();
            if (candidato.exists()) {
                return candidato;
            }

            File alterno = base.resolve("comedor").resolve(ruta).toFile();
            if (alterno.exists()) {
                return alterno;
            }

            Path parent = base.getParent();
            if (parent == null) {
                break;
            }
            base = parent;
        }

        return directa;
    }

    private void registrarRutaFoto(String email, File foto) throws IOException {
        if (email == null || email.trim().isEmpty() || foto == null || !foto.exists()) {
            return;
        }

        File archivo = resolveArchivoDatos();
        File parent = archivo.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        if (!archivo.exists()) {
            archivo.createNewFile();
        }

        List<String> lineas = new ArrayList<>();
        boolean actualizado = false;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",", 2);
                if (datos.length >= 1 && datos[0].trim().equalsIgnoreCase(email.trim())) {
                    lineas.add(email.trim() + "," + construirRutaRelativaFoto(foto));
                    actualizado = true;
                } else if (!linea.trim().isEmpty()) {
                    lineas.add(linea);
                }
            }
        }

        if (!actualizado) {
            lineas.add(email.trim() + "," + construirRutaRelativaFoto(foto));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
            for (String linea : lineas) {
                bw.write(linea);
                bw.newLine();
            }
        }
    }

    private String construirRutaRelativaFoto(File foto) {
        File directorioFotos = obtenerDirectorioFotos();
        if (directorioFotos != null && foto.getParentFile() != null
            && directorioFotos.getAbsolutePath().equals(foto.getParentFile().getAbsolutePath())) {
            return "fotos/" + foto.getName();
        }
        return foto.getAbsolutePath();
    }

    private File resolveArchivoDatos() {
        Path base = Paths.get("").toAbsolutePath();

        for (int i = 0; i < 4; i++) {
            File directo = base.resolve(nombreArchivo).toFile();
            if (directo.exists()) {
                return directo;
            }

            File alterno = base.resolve("comedor").resolve(nombreArchivo).toFile();
            if (alterno.exists()) {
                return alterno;
            }

            Path parent = base.getParent();
            if (parent == null) {
                break;
            }
            base = parent;
        }

        return Paths.get(nombreArchivo).toFile();
    }
}
