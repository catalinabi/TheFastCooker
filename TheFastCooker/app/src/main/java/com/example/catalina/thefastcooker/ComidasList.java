package com.example.catalina.thefastcooker;

import android.graphics.Bitmap;

public class ComidasList {
    public int id;
    public String name;
    public String description;
    public Bitmap thumbnail;
    ComidasList(int id, String name,String desc, Bitmap thumbnail){
        this.id=id;
        this.name = name;
        this.description=desc;
        this.thumbnail = thumbnail;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }
}
