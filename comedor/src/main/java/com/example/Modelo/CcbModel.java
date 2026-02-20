package com.example.Modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CcbModel {
    private final String nombreArchivo = "CCB.txt";

    public boolean guardar(CcbRecord record) {
        File archivo = new File(nombreArchivo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(record.toCsvLine());
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar CCB.txt: " + e.getMessage());
            return false;
        }
    }

    public List<CcbRecord> obtenerRegistros() {
        List<CcbRecord> registros = new ArrayList<>();
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            return registros;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                CcbRecord record = CcbRecord.fromCsv(linea);
                if (record != null) {
                    registros.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer CCB.txt: " + e.getMessage());
        }

        return registros;
    }
}
