package com.example.Modelo;

public class Plato {
    private String nombre;
    private String descripcion;
    private int calorias;
    private double proteinas; // en gramos
    private double grasas;    // en gramos
    private double carbohidratos; // en gramos

    public Plato(String nombre, String descripcion, int calorias, double proteinas, double grasas, double carbohidratos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.grasas = grasas;
        this.carbohidratos = carbohidratos;
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getCalorias() { return calorias; }
    public double getProteinas() { return proteinas; }
    public double getGrasas() { return grasas; }
    public double getCarbohidratos() { return carbohidratos; }
}