package com.example.catalina.thefastcooker;

import android.graphics.Bitmap;

/**
 * Created by Catalina on 20/03/2016.
 */
public class Usuario {
    private int id;
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private int phone;
    private String calle;
    private String ciudad;
    private Bitmap thumbnail;
    //Constructor 1 con todos los parametros
    public Usuario(int id, String email, String password, String nombre, String apellido, int phone, String calle, String ciudad, Bitmap thumbnail) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.phone = phone;
        this.calle = calle;
        this.ciudad = ciudad;
        this.thumbnail = thumbnail;
    }

    //Costructor2 sin el int
    public Usuario(String email, String password, String nombre, String apellido, int phone, String calle, String ciudad, Bitmap thumbnail) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.phone = phone;
        this.calle = calle;
        this.ciudad = ciudad;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
