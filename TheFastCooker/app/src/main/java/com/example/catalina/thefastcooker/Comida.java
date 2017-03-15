package com.example.catalina.thefastcooker;

import android.graphics.Bitmap;

public class Comida {
    private int id_comida;
    private int idCategoria;
    private int idChef;
    private String titulo_menu;
    private String descripcion;
    private String requisitosCocina;
    private String requisitosMenaje;
    private int duracion;
    private int precio_comensal;
    private Bitmap thumbnail;

    /*Constructor1*/

    public Comida(int id_comida, int idCategoria, int idChef, String titulo_menu, String descripcion, String requisitosCocina, String requisitosMenaje, int duracion, int precio_comensal, Bitmap thumbnail) {
        this.id_comida = id_comida;
        this.idCategoria = idCategoria;
        this.idChef = idChef;
        this.titulo_menu = titulo_menu;
        this.descripcion = descripcion;
        this.requisitosCocina = requisitosCocina;
        this.requisitosMenaje = requisitosMenaje;
        this.duracion = duracion;
        this.precio_comensal = precio_comensal;
        this.thumbnail = thumbnail;
    }

    /*Constructor2 sin id comida*/

    public Comida(int idCategoria, int idChef, String titulo_menu, String descripcion, String requisitosCocina, String requisitosMenaje, int duracion, int precio_comensal, Bitmap thumbnail) {
        this.idCategoria = idCategoria;
        this.idChef = idChef;
        this.titulo_menu = titulo_menu;
        this.descripcion = descripcion;
        this.requisitosCocina = requisitosCocina;
        this.requisitosMenaje = requisitosMenaje;
        this.duracion = duracion;
        this.precio_comensal = precio_comensal;
        this.thumbnail = thumbnail;
    }
    /*Constructor para visualizar los datos mas relevantes de la comida en formato Lista*/
    public Comida(int id_comida,int idChef,String titulo_menu,String descripcion, Bitmap thumbnail){
        this.id_comida = id_comida;
        this.idChef = idChef;
        this.titulo_menu = titulo_menu;
        this.descripcion = descripcion;
        this.thumbnail = thumbnail;
    }

    public int getId_comida() {
        return id_comida;
    }

    public void setId_comida(int id_comida) {
        this.id_comida = id_comida;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdChef() {
        return idChef;
    }

    public void setIdChef(int idChef) {
        this.idChef = idChef;
    }

    public String getTitulo_menu() {
        return titulo_menu;
    }

    public void setTitulo_menu(String titulo_menu) {
        this.titulo_menu = titulo_menu;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRequisitosCocina() {
        return requisitosCocina;
    }

    public void setRequisitosCocina(String requisitosCocina) {
        this.requisitosCocina = requisitosCocina;
    }

    public String getRequisitosMenaje() {
        return requisitosMenaje;
    }

    public void setRequisitosMenaje(String requisitosMenaje) {
        this.requisitosMenaje = requisitosMenaje;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getPrecio_comensal() {
        return precio_comensal;
    }

    public void setPrecio_comensal(int precio_comensal) {
        this.precio_comensal = precio_comensal;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
