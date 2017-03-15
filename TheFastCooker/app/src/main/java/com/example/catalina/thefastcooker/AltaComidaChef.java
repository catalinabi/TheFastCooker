package com.example.catalina.thefastcooker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.catalina.thefastcooker.DatabaseHelper.getHelper;

public class AltaComidaChef extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;
    boolean sdDisponible = false;
    boolean sdAccesoEscritura = false;

    private Spinner comboCategorias,comboDuracion,comboPrecio;
    String categoriaSeleccionada,duracionSeleccionada,precioSeleccionado;
    private TextInputLayout inputLayoutNombre,inputLayoutAperitivo,inputLayoutEntrante,inputLayoutPrincipal,inputLayoutPostre,inputLayoutCocina,inputLayoutMenaje;
    private EditText titulo,aperitivo,entrante,principal,postre,cocina,menaje;
    private DatabaseAdapter database;
    private ImageView imageView;
    private Button btnRegistro;
    private Bitmap bitmap;
    private Bitmap userBitmap;
    private Context context;
    private Comida comida;
    private int idChef;
    private String nombreChef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_comida_chef);
        database = getHelper(this);
        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        //esto seria la variable enviada
        idChef=bundle.getInt("idChef");
        Log.i("INFO", "ID de chef actual" + idChef);
        nombreChef=database.getDatosChefForId(idChef);
        //Muestro el listado de mis categorias en el spinner o comboBox
        List<String> categorias=database.recuperarCategorias();
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,categorias);
        comboCategorias = (Spinner)findViewById(R.id.combocategoria);
        comboCategorias.setAdapter(adaptador);
        comboCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaSeleccionada = parent.getItemAtPosition(position).toString();
                Toast.makeText(AltaComidaChef.this, categoriaSeleccionada, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoriaSeleccionada = parent.getItemAtPosition(0).toString();
                Toast.makeText(AltaComidaChef.this, categoriaSeleccionada, Toast.LENGTH_SHORT).show();
            }
        });
        //Titulo menu
        inputLayoutNombre = (TextInputLayout) findViewById(R.id.input_layout_nombre);
        inputLayoutAperitivo=(TextInputLayout) findViewById(R.id.input_layout_aperitivo);
        inputLayoutEntrante=(TextInputLayout) findViewById(R.id.input_layout_entrante);
        inputLayoutPrincipal=(TextInputLayout) findViewById(R.id.input_layout_principal);
        inputLayoutPostre=(TextInputLayout) findViewById(R.id.input_layout_postre);
        inputLayoutCocina=(TextInputLayout) findViewById(R.id.input_layout_cocina);
        inputLayoutMenaje=(TextInputLayout) findViewById(R.id.input_layout_menaje);
        titulo=(EditText) findViewById(R.id.nombre_comida);
        aperitivo=(EditText) findViewById(R.id.aperitivo);
        entrante=(EditText) findViewById(R.id.entrante);
        principal=(EditText) findViewById(R.id.principal);
        postre=(EditText) findViewById(R.id.postre);
        cocina=(EditText) findViewById(R.id.cocina);
        menaje=(EditText) findViewById(R.id.menaje);

        //Select con duracion----------------------------------------
        List<String> duracion=getListadoDuracion();
        ArrayAdapter<String> adaptadorDuracion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,duracion);
        comboDuracion = (Spinner)findViewById(R.id.comboduracion);
        comboDuracion.setAdapter(adaptadorDuracion);
        comboDuracion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                duracionSeleccionada = parent.getItemAtPosition(position).toString();
                Toast.makeText(AltaComidaChef.this, duracionSeleccionada, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                duracionSeleccionada = parent.getItemAtPosition(0).toString();
                Toast.makeText(AltaComidaChef.this, duracionSeleccionada, Toast.LENGTH_SHORT).show();
            }
        });

        //Select con precios----------------------------------------
        List<String> precios=getListadoPrecios();
        ArrayAdapter<String> adaptadorPrecios = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,precios);
        comboPrecio = (Spinner)findViewById(R.id.comboprecio);
        comboPrecio.setAdapter(adaptadorPrecios);
        comboPrecio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                precioSeleccionado = parent.getItemAtPosition(position).toString();
                Toast.makeText(AltaComidaChef.this, precioSeleccionado, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                precioSeleccionado = parent.getItemAtPosition(0).toString();
                Toast.makeText(AltaComidaChef.this, precioSeleccionado, Toast.LENGTH_SHORT).show();
            }
        });
        btnRegistro=(Button) findViewById(R.id.registrarse);
        context=getApplicationContext();
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validado = validarForm(view);
                if (validado == true) {
                    AddUserTask nuevaTarea = new AddUserTask(context, comida);
                    nuevaTarea.execute();
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
    //Retorna listado de duracion en horas
    public List<String> getListadoDuracion(){
        List<String> listadoDuracion= new ArrayList<String>() ;
        for(int i=1;i<=8;i++){
            listadoDuracion.add(String.valueOf(i));
        }
        return listadoDuracion;
    }
    //Retorna listado de precios
    public List<String> getListadoPrecios(){
        List<String> listadoPrecios= new ArrayList<String>() ;
        for(int i=10;i<=90;i+=5){
            listadoPrecios.add(String.valueOf(i));
        }
        return listadoPrecios;
    }
    //Getter para recuperar la imagen cargada por el usuario
    public Bitmap getUserBitmap() {
        return userBitmap;
    }
    public void setUserBitmap(Bitmap userBitmap) {
        this.userBitmap = userBitmap;
    }

    //Cargar Imagen de Galeria
    public void cargarGaleriaImgs(View view) {
        //Comprobamos el estado de la memoria externa (tarjeta SD)
        String estado = Environment.getExternalStorageState();
        //indica que la memoria externa está disponible y podemos tanto leer como escribir en ella.
        if (estado.equals(Environment.MEDIA_MOUNTED))
        {
            System.out.println("Podemos leer y escribir");
            sdDisponible = true;
            sdAccesoEscritura = true;
            crearIntentGallery();
        }
        else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            System.out.println("Podemos SOLO leer");
            sdDisponible = true;
            sdAccesoEscritura = false;
        }
        else
        {
            System.out.println("No Podemos hacer nada");
            sdDisponible = false;
            sdAccesoEscritura = false;
        }
    }//cargarGaleriaImgs()
    public boolean validarTitulo(View view){
        if(titulo.getText().toString().length() == 0 ) {
            inputLayoutNombre.requestFocus();
            inputLayoutNombre.setError("Rellene el campo titulo del menu!");
            return false;
        }else{
            inputLayoutNombre.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validarAperitivo(View view){
        if(aperitivo.getText().toString().length() == 0 ) {
            inputLayoutAperitivo.requestFocus();
            inputLayoutAperitivo.setError("Rellene el campo aperitivo!");
            return false;
        }else{
            inputLayoutAperitivo.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validarEntrante(View view){
        if(entrante.getText().toString().length() == 0 ) {
            inputLayoutEntrante.requestFocus();
            inputLayoutEntrante.setError("Rellene el campo plato entrante!");
            return false;
        }else{
            inputLayoutEntrante.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validarPrincipal(View view){
        if(principal.getText().toString().length() == 0 ) {
            inputLayoutPrincipal.requestFocus();
            inputLayoutPrincipal.setError("Rellene el campo plato principal!");
            return false;
        }else{
            inputLayoutPrincipal.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validarPostre(View view){
        if(postre.getText().toString().length() == 0 ) {
            inputLayoutPostre.requestFocus();
            inputLayoutPostre.setError("Rellene el campo postre!");
            return false;
        }else{
            inputLayoutPostre.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validarCocina(View view){
        if(cocina.getText().toString().length() == 0 ) {
            inputLayoutCocina.requestFocus();
            inputLayoutCocina.setError("Rellene el campo requisitos de cocina!");
            return false;
        }else{
            inputLayoutCocina.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validarMenaje(View view){
        if(menaje.getText().toString().length() == 0 ) {
            inputLayoutMenaje.requestFocus();
            inputLayoutMenaje.setError("Rellene el campo requisitos de menaje!");
            return false;
        }else{
            inputLayoutMenaje.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validarForm(View view){
        if(validarTitulo(view)&& validarAperitivo(view)&&validarEntrante(view)&&validarPrincipal(view)&&validarPostre(view)&&validarCocina(view)&&validarMenaje(view)&&getUserBitmap()!=null)
        {
            String titulo_menu=titulo.getText().toString();
            String d_aperitivo="APERITIVOS \n"+aperitivo.getText().toString()+"\n";
            String d_entrante="ENTRANTES \n" +entrante.getText().toString()+"\n";
            String d_principal="PRINCIPALES \n" +principal.getText().toString()+"\n";
            String d_postre="POSTRES \n" +postre.getText().toString()+"\n";
            String descripcion=d_aperitivo+d_entrante+d_principal+d_postre;
            String reqCocina="REQUISITOS COCINA \n" +cocina.getText().toString()+"\n";
            String reqMenaje="REQUISITOS MENAJE \n" +menaje.getText().toString()+"\n";
            int idCat=database.getIdCatforCatName(categoriaSeleccionada);
            int duracion=Integer.parseInt(duracionSeleccionada);
            int precio=Integer.parseInt(precioSeleccionado);
            Log.i("INFO", "SE VA A CREAR LA COMIDA.......");
            comida=new Comida(idCat,idChef,titulo_menu,descripcion,reqCocina,reqMenaje,duracion,precio,getUserBitmap());
            System.out.println(comida.getTitulo_menu()+comida.getIdChef()+comida.getDescripcion()+comida.getRequisitosCocina()+comida.getRequisitosMenaje()+comida.getDuracion()+comida.getPrecio_comensal()+comida.getThumbnail());
            return true;

        }else{
            Toast.makeText(this, "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
    }//ValidarForm()


    private class AddUserTask extends AsyncTask<Comida, Void, Integer> {
        Context context;
        ProgressDialog pDialog;
        Comida comida;

        //Constructor
        public AddUserTask(Context contexto,Comida com){
            context = contexto;
            comida=com;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            Log.i("AsyncTask PreExecute", "Entra en PreExecute");
            pDialog = new ProgressDialog(AltaComidaChef.this);
            pDialog.setMessage("Añadiendo "+comida.getTitulo_menu()+" en la DB para el chef " +nombreChef);
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(Comida... arg0) {
            // TODO Auto-generated method stub

            try{
                Thread.sleep(1000);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            boolean exito=database.addComida(comida);
            if(exito==true) {
                return 1;
            }else{
                return 0;

            }
        }
        //cambiar la view
        @Override
        protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result==1) {
                Toast.makeText(context, "Acaba de ser dado de alta la comida en el sistema", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Hubo un problema para dar de alta el menu", Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
        }
    }//AsyncTask

    public void crearIntentGallery(){
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }//crearIntentGallery()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            Log.d("TAG", DatabaseUtils.dumpCursorToString(cursor));

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();
            try {
                userBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                imageView = (ImageView) findViewById(R.id.imgView);
                imageView.setImageBitmap(userBitmap);
                setUserBitmap(userBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alta_comida_chef, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
