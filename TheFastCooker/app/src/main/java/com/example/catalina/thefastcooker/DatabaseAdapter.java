package com.example.catalina.thefastcooker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    //instanciamos la clase MyDatabaseHelper
    MyDatabaseHelper dbHelper;
    //Constructor
    DatabaseAdapter(Context context){
        dbHelper = new MyDatabaseHelper(context);
    }
    /*------------METODOS PROPIOS---------*/
    //Metodo añadir Categorias
    public void addCategoria(Categoria c){
        Log.d("addCategoria", c.toString());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MyDatabaseHelper.ID, c.getId()); // get title
        cv.put(MyDatabaseHelper.NAME, c.getNombre()); // get author

        try{
            db.insert(MyDatabaseHelper.TABLE_CAT,//tabla
                    null,
                    cv);//valores
        }
        catch (Exception e){
            Log.i("INFO", "ERROR EN LA INSERCION");
        }
        // 4. close
        db.close();
    }
    //Metodo existeUsuario-> retorna boolean tru si existe/false si no
    public boolean existeUsuario(String email, String password){
        boolean usuario=false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String [] columns = {MyDatabaseHelper.EMAIL,MyDatabaseHelper.PASS};
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_USUARIOS, columns,  MyDatabaseHelper.EMAIL + "=?" + " and "  +
                MyDatabaseHelper.PASS + "=?",new String[] {email,password}, null, null, null);
        int numFilas=cursor.getCount();
        if(cursor != null) {
            cursor.moveToFirst();
            if(numFilas==1){
                if (cursor.moveToFirst()) {
                    do {
                        String mail = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.EMAIL));
                        String pass = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.PASS));
                        Log.i("INFO", "Nombre " + mail + "Password:" + pass);
                    } while (cursor.moveToNext());
                }
                usuario=true;
                return usuario;
            }else  if(numFilas==0){
                Log.i("INFO", "SE ENCONTRO " + numFilas + "FILAS CON ESTOS DATOS");
                return usuario=false;
            }
        }
        return usuario;
    }

    //Metodo existeChef-> En pantalla Login, le mandamos email y password y retorna boolean true si existe/false si no
    public boolean existeChef(String email, String password){
        boolean chef=false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String [] columns = {MyDatabaseHelper.EMAIL,MyDatabaseHelper.PASS};
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_CHEFS, columns,  MyDatabaseHelper.EMAIL + "=?" + " and "  +
                MyDatabaseHelper.PASS + "=?",new String[] {email,password}, null, null, null);
        int numFilas=cursor.getCount();
        if(cursor != null) {
            cursor.moveToFirst();
            if(numFilas==1){
                if (cursor.moveToFirst()) {
                    do {
                        String mail = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.EMAIL));
                        String pass = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.PASS));
                        Log.i("INFO", "Nombre " + mail + "Password:" + pass);
                    } while (cursor.moveToNext());
                }
                return chef=true;
            }else  if(numFilas==0){
                Log.i("INFO", "SE ENCONTRO " + numFilas + "FILAS CON ESTOS DATOS");
                return chef=false;
            }
        }
        return chef;
    }
    //Metodo existeEmail-> retorna boolean true si existe/false si no
    public boolean existeEmail(String email){
        boolean usuario=false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String [] columns = {MyDatabaseHelper.EMAIL};
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_USUARIOS, columns,  MyDatabaseHelper.EMAIL + "=?",new String[] {email}, null, null, null);
        int numFilas=cursor.getCount();
        if(cursor != null) {
            cursor.moveToFirst();
            if(numFilas==1){
                return usuario=true;
            }else  if(numFilas==0){
                return usuario=false;
            }
        }
        return usuario;
    }

    //Metodo existeEmail-> retorna boolean true si existe/false si no
    public boolean existeEmailChef(String email){
        boolean usuario=false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String [] columns = {MyDatabaseHelper.EMAIL};
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_CHEFS, columns,  MyDatabaseHelper.EMAIL + "=?",new String[] {email}, null, null, null);
        int numFilas=cursor.getCount();
        if(cursor != null) {
            cursor.moveToFirst();
            if(numFilas==1){
                return usuario=true;
            }else  if(numFilas==0){
                return usuario=false;
            }
        }
        return usuario;
    }
    //Metodo para recoger el id del usuario//Devuelve el id en integer
    public int getId(String email,String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {MyDatabaseHelper.ID, MyDatabaseHelper.EMAIL, MyDatabaseHelper.PASS};
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_USUARIOS, columns, MyDatabaseHelper.EMAIL + "=?" + " and " +
                MyDatabaseHelper.PASS + "=?", new String[]{email, password}, null, null, null);
        int userID = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                do {
                    userID = cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.ID));
                } while (cursor.moveToNext());
            }
        }
        return userID;
    }

    //Metodo para recoger el id del chef//Devuelve el id en integer
    public int getIdChef(String email,String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {MyDatabaseHelper.ID, MyDatabaseHelper.EMAIL, MyDatabaseHelper.PASS};
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_CHEFS, columns, MyDatabaseHelper.EMAIL + "=?" + " and " +
                MyDatabaseHelper.PASS + "=?", new String[]{email, password}, null, null, null);
        int chefID = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                do {
                    chefID = cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.ID));
                } while (cursor.moveToNext());
            }
        }
        return chefID;
    }

    //Metodo para recoger el id del chef//Devuelve el nombreCompleto del Chef
    public String getDatosChefForId(int id) {
        String idChef=String.valueOf(id);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {MyDatabaseHelper.ID, MyDatabaseHelper.NAME, MyDatabaseHelper.LASTNAME};
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_CHEFS, columns, MyDatabaseHelper.ID+ "=?", new String[]{idChef}, null, null, null);
        String chef=null;
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                do {
                    chef= cursor.getString(1) +" "+ cursor.getString(2);
                } while (cursor.moveToNext());
            }
        }
        return chef;
    }
    //Añado Chefs a la base de datos
    public boolean addChef(Chef c){
        Bitmap bitmap = c.getThumbnail();
        byte[] image=convertBitmapToBytes(bitmap);
        //Log.d("addCategoria", c.toString());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MyDatabaseHelper.EMAIL, c.getEmail());
        cv.put(MyDatabaseHelper.PASS, c.getPassword());
        cv.put(MyDatabaseHelper.NAME, c.getNombre());
        cv.put(MyDatabaseHelper.LASTNAME, c.getApellido());
        cv.put(MyDatabaseHelper.TELEFONO, c.getPhone());
        cv.put(MyDatabaseHelper.ADDRESS, c.getCalle());
        cv.put(MyDatabaseHelper.CIUDAD, c.getCiudad());
        cv.put(MyDatabaseHelper.IMAGE_BITMAP, image);

        try{
            db.insert(MyDatabaseHelper.TABLE_CHEFS, null, cv);
            return true;//valores
        }
        catch (Exception e){
            Log.i("INFO", "ERROR EN LA INSERCION DE CHEFS");
        }
        // 4. close
        db.close();
        return false;
    }
    //Añado Usuarios a la base de datos
    public boolean addUser(Usuario u){
        boolean exito=false;
        Bitmap bitmap = u.getThumbnail();
        byte[] image=convertBitmapToBytes(bitmap);
        //Log.d("addCategoria", c.toString());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
       /* cv.put(MyDatabaseHelper.ID, c.getId());*/
        cv.put(MyDatabaseHelper.EMAIL, u.getEmail());
        cv.put(MyDatabaseHelper.PASS, u.getPassword());
        cv.put(MyDatabaseHelper.NAME, u.getNombre());
        cv.put(MyDatabaseHelper.LASTNAME, u.getApellido());
        cv.put(MyDatabaseHelper.TELEFONO, u.getPhone());
        cv.put(MyDatabaseHelper.ADDRESS, u.getCalle());
        cv.put(MyDatabaseHelper.CIUDAD, u.getCiudad());
        cv.put(MyDatabaseHelper.IMAGE_BITMAP, image);

        try{
            db.insert(MyDatabaseHelper.TABLE_USUARIOS,null,cv);//valores
            Log.i("INFO", "USUARIO INSERTADO CORRECTAMENTE");
            return exito=true;

        }
        catch (Exception e){
            Log.i("INFO", "ERROR EN LA INSERCION DE USUARIO");

        }

        // 4. close
      db.close();
        return exito=false;
    }



    //Añado Comida a la base de datos
    public boolean addComida(Comida c){
        Bitmap bitmap = c.getThumbnail();
        byte[] image=convertBitmapToBytes(bitmap);
        //Log.d("addCategoria", c.toString());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MyDatabaseHelper.CAT_ID, c.getIdCategoria());
        cv.put(MyDatabaseHelper.CHEF_ID, c.getIdChef());
        cv.put(MyDatabaseHelper.NOMBRE_MENU, c.getTitulo_menu());
        cv.put(MyDatabaseHelper.DESC_MENU, c.getDescripcion());
        cv.put(MyDatabaseHelper.REQ_COCINA, c.getRequisitosCocina());
        cv.put(MyDatabaseHelper.REQ_MENAJE, c.getRequisitosMenaje());
        cv.put(MyDatabaseHelper.DURACION, c.getDuracion());
        cv.put(MyDatabaseHelper.PRECIO_COMENSAL, c.getPrecio_comensal());
        cv.put(MyDatabaseHelper.IMAGE_BITMAP, image);

        try{
            db.insert(MyDatabaseHelper.TABLE_COMIDAS, null, cv);
            Log.i("INFO", "COMIDA INSERTADA CORRECTAMENTE");
            return true;//valores
        }
        catch (Exception e){
            Log.i("INFO", "ERROR EN LA INSERCION DE COMIDAS");
        }
        // 4. close
        db.close();
        return false;
    }

    //Metodo que recibe un ID de comida y devuelve un objeto Comida con todos los detalles de la comida
    public Comida listarDetalleComida(int idComida) {
        //guardamos el id comida
        String id=String.valueOf(idComida);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Comida comida=null;
        String[] columnas = {MyDatabaseHelper.ID,MyDatabaseHelper.CAT_ID,MyDatabaseHelper.CHEF_ID,MyDatabaseHelper.NOMBRE_MENU, MyDatabaseHelper.DESC_MENU,
                MyDatabaseHelper.REQ_COCINA,MyDatabaseHelper.REQ_MENAJE,MyDatabaseHelper.DURACION,MyDatabaseHelper.PRECIO_COMENSAL, MyDatabaseHelper.IMAGE_BITMAP};
        Cursor c = db.query(MyDatabaseHelper.TABLE_COMIDAS, columnas, MyDatabaseHelper.ID+ "=?", new String[]{id}, null, null, null);
        if (c != null) {
            c.moveToFirst();
            if (c.moveToFirst()) {
                do {
                    //getBlob retorna array de bytes
                    byte[] image = c.getBlob(9);
                    Bitmap bitmap = converBytesToBitmap(image);
                    comida = new Comida(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getString(4),c.getString(5),c.getString(6),c.getInt(7),c.getInt(8),bitmap);

                } while (c.moveToNext());
            }
        }
        db.close();
        c.close();
        return comida;

    }
    //Metodo que convierte bitmap a array de bytes
    public byte[] convertBitmapToBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image=stream.toByteArray();
        return image;
    }
    //Mira si la tabla x esta vacia
    public boolean checkTableIsEmpty(String name) {
        String consulta="SELECT COUNT(*) FROM "+name;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery(consulta, null);
        if (cur != null) {
            cur.moveToFirst();
            //System.out.println("record : " + cur.getInt(0));
            if (cur.getInt(0) == 0) {
                System.out.println("Tabla "+name+" Vacia");
                cur.close();
                return true;
            }
            cur.close();
        } else {
            System.out.println("Cursor is Null");
            return true;
        }
        System.out.println("Table Not Null");
        return false;
    }

    //Retorno los Chefs de la base de datos;
    public ArrayList<Chef> recuperarChefs() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Chef> listadoChefs= new ArrayList<Chef>();
        String[] columnas = {MyDatabaseHelper.ID, MyDatabaseHelper.NAME, MyDatabaseHelper.LASTNAME, MyDatabaseHelper.IMAGE_BITMAP};
        Cursor c = db.query(MyDatabaseHelper.TABLE_CHEFS, columnas,null, null, null, null, null);
        c.moveToFirst();
        do {
            //getBlob retorna array de bytes
            byte[] image=c.getBlob(3);
            Bitmap bitmap=converBytesToBitmap(image);
            Chef chef= new Chef(c.getInt(0), c.getString(1), c.getString(2),bitmap);
            listadoChefs.add(chef);
        } while (c.moveToNext());
        db.close();
        c.close();
        return listadoChefs;
    }

    //Retorno las Comidas de la base de datos para un idChef determinado;
    public ArrayList<Comida> recuperarComidas(int idChef) {
        String id=String.valueOf(idChef);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Comida> listadoComidas= new ArrayList<Comida>();
        String[] columnas = {MyDatabaseHelper.ID,MyDatabaseHelper.CHEF_ID,MyDatabaseHelper.NOMBRE_MENU, MyDatabaseHelper.DESC_MENU, MyDatabaseHelper.IMAGE_BITMAP};
        Cursor c = db.query(MyDatabaseHelper.TABLE_COMIDAS, columnas, MyDatabaseHelper.CHEF_ID+ "=?", new String[]{id}, null, null, null);
        if (c != null) {
            c.moveToFirst();
            if (c.moveToFirst()) {
                do {
                    //getBlob retorna array de bytes
                    byte[] image = c.getBlob(4);
                    Bitmap bitmap = converBytesToBitmap(image);
                    Comida comida = new Comida(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), bitmap);
                    listadoComidas.add(comida);
                } while (c.moveToNext());
            }
        }else{
            Log.i("INFO", "No hay comidas para ese chef "+idChef);
            return null;
        }
        db.close();
        c.close();
        return listadoComidas;
    }


    //Convierte array de bytes a bitmap
    public Bitmap converBytesToBitmap(byte[] image){
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        return bitmap;
    }



    //Retorno listaod con todas las Categorias de la base de datos;
    public List<String> recuperarCategorias() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> listadoCategorias= new ArrayList<String>() ;
        String[] columnas = {MyDatabaseHelper.NAME};
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_CAT, columnas,null, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                listadoCategorias.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return listadoCategorias;
    }

    //Metodo que retorna id de categoria para un nombre de categoria
    public int getIdCatforCatName(String nameCat) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columnas = {MyDatabaseHelper.ID,MyDatabaseHelper.NAME};
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_CAT, columnas,MyDatabaseHelper.NAME + "=?", new String[]{nameCat}, null, null, null);
        int catID = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                do {
                    catID = cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.ID));
                    Log.i("INFO", "Encontrado ID "+catID+" para categoria "+nameCat);
                } while (cursor.moveToNext());
            }
        }
        return catID;
    }


    //MyDatabaseHelper-> extends de SQLiteOpenHelper----------------------------
    public class MyDatabaseHelper extends SQLiteOpenHelper {
        private static final String db_name = "database.db";
        private static final int version_db = 1;

        // aqui van las variables de los nombres de tablas y sus campos
        private static final String TABLE_USUARIOS = "USUARIOS";
        private static final String TABLE_CHEFS = "CHEFS";
        private static final String TABLE_CAT = "CATEGORIAS";
        private static final String TABLE_COMIDAS = "COMIDAS";
        private static final String TABLE_RESERVAS = "RESERVAS";
        private static final String TABLE_WHISLIST = "WISHLIST";
        private static final String ID = "id";
        private static final String COMIDA_ID = "comidaId";
        private static final String USER_ID = "userId";
        private static final String CAT_ID = "catId";
        private static final String CHEF_ID = "chefId";
        private static final String NOMBRE_MENU = "NombreMenu";
        private static final String DESC_MENU = "DescripcionMenu";
        private static final String REQ_COCINA = "RequisitosCocina";
        private static final String REQ_MENAJE = "Requisitosmenaje";
        private static final String DURACION = "Duracion";
        private static final String PRECIO_COMENSAL = "PrecioComensal";

        private static final String FECHA_IN = "FechaInicio";
        private static final String FECHA_OUT = "FechaFin";
        private static final String NUM_COMENSALES = "NumeroComensales";
        private static final String PRECIO_TOTAL = "PrecioTotal";
        private static final String PAGADO = "Pagado";
        private static final String EMAIL = "Email";
        private static final String PASS = "Password";
        private static final String NAME = "Name";
        private static final String LASTNAME = "LastName";
        private static final String TELEFONO = "Telefono";
        private static final String ADDRESS = "Direccion";
        private static final String CIUDAD = "Ciudad";
        private static final String IMAGE_BITMAP = "Image";
        //SQL TABLE USUARIOS
        String sqlCreateTableUsuarios="CREATE TABLE "
                + TABLE_USUARIOS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMAIL + " VARCHAR(255), "
                + PASS + " VARCHAR(255), "
                + NAME + " VARCHAR(255), "
                + LASTNAME+ " VARCHAR(255), "
                + TELEFONO+ " INTEGER, "
                + ADDRESS+ " VARCHAR(255), "
                + CIUDAD+ " VARCHAR(255), "
                + IMAGE_BITMAP+" TEXT);";
        //SQL TABLE CHEFS
        String sqlCreateTableChefs="CREATE TABLE "
                + TABLE_CHEFS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMAIL + " VARCHAR(255), "
                + PASS + " VARCHAR(255), "
                + NAME + " VARCHAR(255), "
                + LASTNAME+ " VARCHAR(255), "
                + TELEFONO+ " INTEGER, "
                + ADDRESS+ " VARCHAR(255), "
                + CIUDAD+ " VARCHAR(255), "
                + IMAGE_BITMAP+" TEXT);";
        //SQL TABLE CATEGORIAS
        String sqlCreateTableCategorias="CREATE TABLE "
                + TABLE_CAT + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " VARCHAR(255));";

        //SQL TABLE COMIDAS
        String sqlCreateTableComidas="CREATE TABLE "
                + TABLE_COMIDAS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CAT_ID +" INTEGER, "
                + CHEF_ID +" INTEGER, "
                + NOMBRE_MENU  + " VARCHAR(255), "
                + DESC_MENU  + " VARCHAR(255), "
                + REQ_COCINA  + " VARCHAR(255), "
                + REQ_MENAJE  + " VARCHAR(255), "
                + DURACION  + " INTEGER, "
                + PRECIO_COMENSAL + " INTEGER, "
                + IMAGE_BITMAP+" TEXT, "
                + " FOREIGN KEY ("+ CAT_ID +") REFERENCES "+TABLE_CAT+"("+ID+"),"
                + " FOREIGN KEY ("+CHEF_ID+") REFERENCES "+TABLE_CHEFS+"("+ID+")"
                +");";


        //SQL TABLE RESERVAS
        String sqlCreateTableReservas="CREATE TABLE "
                + TABLE_RESERVAS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_ID +" INTEGER, "
                + COMIDA_ID +" INTEGER, "
                + FECHA_IN  + " INTEGER, "
                + FECHA_OUT  + " INTEGER, "
                + NUM_COMENSALES  + " INTEGER, "
                + PRECIO_TOTAL  + " INTEGER, "
                + PAGADO + " BOOLEAN, "
                + " FOREIGN KEY("+USER_ID+") REFERENCES "+TABLE_USUARIOS+"("+ID+"),"
                + " FOREIGN KEY("+COMIDA_ID+") REFERENCES " + TABLE_COMIDAS + "(" + ID+")"
                +");";

        //SQL TABLE WISHLIST
        String sqlCreateTableWishlist="CREATE TABLE "
                + TABLE_WHISLIST + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_ID +" INTEGER, "
                + COMIDA_ID +" INTEGER, "
                + " FOREIGN KEY ("+COMIDA_ID +") REFERENCES "+TABLE_COMIDAS+"("+ID+"),"
                + " FOREIGN KEY ("+USER_ID+") REFERENCES "+TABLE_USUARIOS+"("+ID+")"
                +");";

        //CONSTRUCTOR
        MyDatabaseHelper(Context context) {
            super(context, db_name, null, version_db);
        }

        //Funcion OnCReate--> se ejecuta la sentencia de creacion de la tabla
        @Override
        public void onCreate(SQLiteDatabase db) {

            // Ejecutamos las sentencias Select de creacion de TABLAS
            db.execSQL(sqlCreateTableUsuarios);
            db.execSQL(sqlCreateTableChefs);
            db.execSQL(sqlCreateTableCategorias);
            db.execSQL(sqlCreateTableComidas);
            db.execSQL(sqlCreateTableReservas);
            db.execSQL(sqlCreateTableWishlist);


        }
        //onUpgrade se lanzara automaticamente cuando sea necesaria una actualizacion de la estructura de la base de datos o una conversion de los datos
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if ((oldVersion == 1) && (newVersion == 2)) {

            }
            /* db.execSQL("DROP TABLE IF EXISTS USUARIOS");
            db.execSQL(sqlCreate);*/
        }

    }
}

