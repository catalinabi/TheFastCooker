package com.example.catalina.thefastcooker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.catalina.thefastcooker.DatabaseHelper.*;

public class MainActivity extends AppCompatActivity {
    private DatabaseAdapter database;
    private EditText email;
    private EditText contra;
    private CheckBox checkBoxChef;
    private boolean isLoginChef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //database=new DatabaseAdapter(this);
        database = getHelper(this);
        insertarDatosDB();
        email = (EditText) findViewById(R.id.email_usuario);
        contra=(EditText) findViewById(R.id.contra_usuario);
        checkBoxChef=(CheckBox)findViewById(R.id.checkbox);
        isLoginChef=false;
        checkBoxChef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox) view).isChecked();
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Desea logearse como Chef", Toast.LENGTH_SHORT).show();
                    isLoginChef=true;
                } else {
                    Toast.makeText(getApplicationContext(), "Desea logearse como usuario", Toast.LENGTH_SHORT).show();
                    isLoginChef=false;
                }
            }
        });
        //Muestra el logo de la app
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //optional

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the FragmentHome/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Metodo para validar email
    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    /*Metodos botones*/
    public void logearUsuario(View view) {
        String emailUsuario= email.getText().toString();
        String pass= contra.getText().toString();
        boolean emailValido=isValidEmail(emailUsuario);
        //Si el email  esta en el formato correcto
        if(emailValido==true) {
            //Si el checkbox de chef no esta activo
            if(isLoginChef==false) {
                boolean existeUsuario = database.existeUsuario(emailUsuario, pass);
                if (existeUsuario == true) {
                    int userID = database.getId(emailUsuario, pass);
                    //Le digo al intent de donde estoy a donde quiero ir
                    Intent intent = new Intent(this, UserActivity.class);
                    Bundle id = new Bundle();
                    id.putInt("idUsuario", userID);
                    //Añadimos id Usuario info al intent
                    intent.putExtras(id);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "El usuario introducido no existe", Toast.LENGTH_SHORT).show();
                }
            }else{
                //Si el checkbox de chef esta activo...se desea logear como chef
                boolean existeChef = database.existeChef(emailUsuario, pass);
                if (existeChef == true) {
                    int chefID = database.getIdChef(emailUsuario, pass);
                    //Le digo al intent de donde estoy a donde quiero ir
                    Intent intent = new Intent(this, AltaComidaChef.class);
                    Bundle id = new Bundle();
                    id.putInt("idChef", chefID);
                    //Añadimos id Chef info al intent
                    intent.putExtras(id);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "El chef introducido no existe", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(this, "Debe Introducir un email valido", Toast.LENGTH_SHORT).show();
        }
    }

    //Abre la actividad sin logeo
    public void crearActividadSinLogeo(View view){
        //Le digo al intent de donde estoy a donde quiero ir
        Intent intent = new Intent(this, UserActivity.class);
        Bundle id = new Bundle();
        id.putInt("idUsuario", 1);
        //Añadimos id Usuario info al intent
        intent.putExtras(id);
        startActivity(intent);
    }
    //Abre la actividad del formulario de alta del Usuario
    public void crearActividadUsuario(View view){
        Intent intent = new Intent(this,AltaUsuario.class);
        startActivity(intent);
    }
    //Abre la actividad del formulario de alta del Chef
    public void crearActividadChef(View view){
        Intent intent = new Intent(this,AltaChef.class);
        startActivity(intent);
    }

    //Abre la actividad del comida de alta del Usuario
    public void crearActividadComida(View view){
        Intent intent = new Intent(this,AltaComidaChef.class);
        startActivity(intent);
    }
    //Metodo que detecta si las tablas Categorias/Chefs/Comidas/Usuarios estan vacias y si es true-> les mete contenido
    public void insertarDatosDB(){
        boolean vacioCat=database.checkTableIsEmpty("CATEGORIAS");
        //Si la tabla categorias esta vacia la rellena
        if(vacioCat==true){
            database.addCategoria(new Categoria(1,"Asiatica"));
            database.addCategoria(new Categoria(2,"Italiana"));
            database.addCategoria(new Categoria(3,"Mediterranea"));
        }
        boolean vacioChef=database.checkTableIsEmpty("CHEFS");
        //Si la tabla chefs esta vacia la rellena
        if(vacioChef==true){

            Bitmap alvaro=BitmapFactory.decodeResource(getResources(), R.drawable.chef_alvaro);
            Bitmap eva=BitmapFactory.decodeResource(getResources(), R.drawable.chef_eva);
            Bitmap carlos=BitmapFactory.decodeResource(getResources(), R.drawable.chef_carlos);
            Bitmap antonio=BitmapFactory.decodeResource(getResources(), R.drawable.chef_antonio);
            database.addChef(new Chef("alvarito@hotmail.com", "alvarito", "Alvaro", "Bolo", 630181818, "calle Moscatelar 1K", "Madrid", alvaro));
            database.addChef(new Chef("evaperez@hotmail.com", "eva", "Eva", "Perez", 630102030, "calle Pirineos 55", "Madrid", eva));
            database.addChef(new Chef("carlosfuentes@hotmail.com", "carlitos", "Carlos", "Fuentes", 610998877, "calle Barquillo 36", "Madrid", carlos));
            database.addChef(new Chef("a@gil.com", "agil", "Antonio", "Gil", 610918877, "calle Barquillo 20", "Madrid", antonio));

        }
        boolean vacioUser=database.checkTableIsEmpty("USUARIOS");
        //Si la tabla usuarios esta vacia la rellena
        if(vacioUser==true){

            Bitmap cati=BitmapFactory.decodeResource(getResources(), R.drawable.user_cati);
            Bitmap juan=BitmapFactory.decodeResource(getResources(), R.drawable.user_juan);
            Bitmap claudia=BitmapFactory.decodeResource(getResources(), R.drawable.user_claudia);
            database.addUser(new Usuario("c@bian.com", "cb", "Catalina", "Bianchi", 630188877, "calle Barquillo, 36, planta 4", "Madrid", cati));
            database.addUser(new Usuario("juanito@hotmail.com", "juanito", "Juan", "López", 630222030, "calle Princesa 6", "Madrid", juan));
            database.addUser(new Usuario("cmartinez@hotmail.com", "claud", "Claudia", "Morales", 610998877, "calle Barco 36", "Madrid", claudia));

        }
        boolean vacioComida=database.checkTableIsEmpty("COMIDAS");
        //Si la tabla Comida esta vacia la rellena
        if(vacioComida==true){
            String descJapones="APERITIVOS \n" +
                    "Sushi con salsa de soja y wasabi\n" +
                    "ENTRANTES \n" +
                    "Empanadillas de cerdo (Gyoza)\n" +
                    "o Témpura de gambas harusame\n" +
                    "PRINCIPALES \n" +
                    "Tataki de Atún con ensalada de fideos Soba\n" +
                    "o Tataki de Buey con chips de espinacas y salsa teriyaki\n" +
                    "POSTRES \n" +
                    "Helado frito a elegir entre los sabores leche merengada, chocolate o fresa";

            String descItaliana="APERITIVOS \n" +
                    "Gnocchi al pomodoro\n" +
                    "o Antipasto de Mozzarella y Pesto\n" +
                    "ENTRANTES \n" +
                    "Pizzetas Montanara de Napoli\n" +
                    "o Burrata estilo Zafferano\n" +
                    "PRINCIPALES \n" +
                    "Paccheri ai frutti di mare\n" +
                    "o Risotto Milanesa\n" +
                    "POSTRES \n" +
                    "Helado frito a elegir entre los sabores leche merengada, chocolate o fresa";
            String descMediterranea="APERITIVOS \n" +
                    "Crujientes de boquerón y fresas con reducción\n" +
                    "ENTRANTES \n" +
                    "Ensalada de tomate confitado en especias con vinagreta de fresas y menta\n" +
                    "PRINCIPALES \n" +
                    "Conejo con gambas de la costa\n" +
                    "POSTRES \n" +
                    "Tres texturas";

            String descItaliana2="APERITIVOS \n" +
                    "Gnocchi al parmesano\n" +
                    "o Carpaccio de Buey\n" +
                    "ENTRANTES \n" +
                    "Ensalada Capresse con Mozarrella di Bufala\n" +
                    "PRINCIPALES \n" +
                    "Rissoto de hongos con aceite de trufa\n" +
                    "POSTRES \n" +
                    "Helado de Capuccino al aroma de coco";
            String reqCocina="REQUISITOS COCINA \n" +
                    "Horno y Vitroceramica\n";
            String reqMenaje="REQUISITOS MENAJE \n" +
                    "Cuberteria para todos los comensales\n";

            Bitmap japonesa=BitmapFactory.decodeResource(getResources(), R.drawable.comida_japones2);
            Bitmap italiana=BitmapFactory.decodeResource(getResources(), R.drawable.comida_italiana_pizza);
            Bitmap italiana2=BitmapFactory.decodeResource(getResources(), R.drawable.comida_italiana_rissoto);
            Bitmap mediterranea=BitmapFactory.decodeResource(getResources(), R.drawable.comida_mediter2);
            database.addComida(new Comida(1,1,"Menú Japones",descJapones,reqCocina,reqMenaje,4,45,japonesa));
            database.addComida(new Comida(2,1,"Menú Italiano",descItaliana2,reqCocina,reqMenaje,2,25,italiana2));
            database.addComida(new Comida(2,2,"Menú Italiano",descItaliana,reqCocina,reqMenaje,1,20,italiana));
            database.addComida(new Comida(3,3,"Menú de autor",descMediterranea,reqCocina,reqMenaje,2,30,mediterranea));

        }
    }
}
