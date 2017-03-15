package com.example.catalina.thefastcooker;

import android.graphics.Bitmap;

/**
 * Created by dam on 13/11/2015.
 */
public class Heroes {
    public int id;
    public String name;
    public Bitmap thumbnail;
    Heroes(int id,String name, Bitmap thumbnail){
        this.id=id;
        this.name = name;
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
}
