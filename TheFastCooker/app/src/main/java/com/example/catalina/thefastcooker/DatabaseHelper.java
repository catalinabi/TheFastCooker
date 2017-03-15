package com.example.catalina.thefastcooker;

import android.content.Context;

/**
 * Created by Catalina on 28/02/2016.
 */
public class DatabaseHelper{
    private static DatabaseAdapter instance;

    public static synchronized DatabaseAdapter getHelper(Context context) {
        if (instance == null)
            instance = new DatabaseAdapter(context);

        return instance;
    }
//Other stuff...
}
