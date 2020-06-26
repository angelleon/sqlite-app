package com.pandalab.sqlite;

public class Carro {
    public static final String TABLENAME = "Carro";
    public static final String SERIE = "serie";
    public static final String NOMBRE = "nombre";
    public static final String COLOR = "color";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private int serie;
    private String nombre;
    private String color;

    public Carro(int id, int serie, String nombre, String color) {
        this.id = id;
        this.serie = serie;
        this.nombre = nombre;
        this.color = color;
    }

    Carro(int serie, String nombre, String color) {
        this(-1, serie, nombre, color);
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
