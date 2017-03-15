package com.example.catalina.thefastcooker;

/**
 * Created by Catalina on 17/02/2016.
 */
public class Categoria {
    private int id;
    private String nombre;


    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    /*Constructor 2-> lo utilizo desde la db para cargar nombres de categorias*/
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "Categoria [id=" + getId() + ", nombre=" + nombre +"]";
    }
}
