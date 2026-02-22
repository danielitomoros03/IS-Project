package com.example.Modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MenuModel {
    private final String nombreArchivo = "Menus.txt";

    public List<MenuRecord> obtenerMenus() {
        List<MenuRecord> menus = new ArrayList<>();
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            menus = crearMenusIniciales();
            guardarTodos(menus);
            return menus;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                MenuRecord record = MenuRecord.fromCsv(linea);
                if (record != null) {
                    menus.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer Menus.txt: " + e.getMessage());
        }

        menus.sort(Comparator.comparing(MenuRecord::getFecha).thenComparing(MenuRecord::getTurno));
        return menus;
    }

    public boolean guardar(MenuRecord record) {
        List<MenuRecord> menus = obtenerMenus();
        menus.add(record);
        return guardarTodos(menus);
    }

    public boolean actualizar(MenuRecord actualizado) {
        List<MenuRecord> menus = obtenerMenus();
        boolean reemplazo = false;
        List<MenuRecord> nuevos = new ArrayList<>();
        for (MenuRecord m : menus) {
            if (m.getId().equals(actualizado.getId())) {
                nuevos.add(actualizado);
                reemplazo = true;
            } else {
                nuevos.add(m);
            }
        }
        if (!reemplazo) {
            nuevos.add(actualizado);
        }
        return guardarTodos(nuevos);
    }

    public boolean eliminar(String id) {
        List<MenuRecord> menus = obtenerMenus();
        List<MenuRecord> nuevos = new ArrayList<>();
        for (MenuRecord m : menus) {
            if (!m.getId().equals(id)) {
                nuevos.add(m);
            }
        }
        return guardarTodos(nuevos);
    }

    public List<MenuRecord> obtenerMenusPorFecha(LocalDate fecha) {
        List<MenuRecord> menus = obtenerMenus();
        List<MenuRecord> resultado = new ArrayList<>();
        for (MenuRecord m : menus) {
            if (m.getFecha().isEqual(fecha)) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    private boolean guardarTodos(List<MenuRecord> menus) {
        File archivo = new File(nombreArchivo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
            for (MenuRecord record : menus) {
                bw.write(record.toCsvLine());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar Menus.txt: " + e.getMessage());
            return false;
        }
    }

    private List<MenuRecord> crearMenusIniciales() {
        List<MenuRecord> menus = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        List<String> platosAlmuerzo = new ArrayList<>();
        platosAlmuerzo.add("Pollo al horno");
        platosAlmuerzo.add("Arroz blanco");
        platosAlmuerzo.add("Ensalada Cesar");

        List<String> platosDesayuno = new ArrayList<>();
        platosDesayuno.add("Arepa con queso");
        platosDesayuno.add("Jugo natural");
        platosDesayuno.add("Cafe");

        menus.add(new MenuRecord(hoy, "Almuerzo", platosAlmuerzo));
        menus.add(new MenuRecord(hoy, "Desayuno", platosDesayuno));
        return menus;
    }
}
