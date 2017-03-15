package com.example.catalina.thefastcooker;

/**
 * Created by Catalina on 09/03/2016.
 */
public class UsuarioActivo {
    private int userActiveId;
    private int chefActiveId;
    private int comidaActiveId;

    public UsuarioActivo(){

    }
    public UsuarioActivo(int usuarioActivo) {
        this.userActiveId=usuarioActivo;
    }
    public int getUserActiveId() {
        return userActiveId;
    }

    public void setUserActiveId(int userActiveId) {
        this.userActiveId = userActiveId;
    }

    public int getChefActiveId() {
        return chefActiveId;
    }

    public void setChefActiveId(int chefActiveId) {
        this.chefActiveId = chefActiveId;
    }

    public int getComidaActiveId() {
        return comidaActiveId;
    }

    public void setComidaActiveId(int comidaActiveId) {
        this.comidaActiveId = comidaActiveId;
    }
}
