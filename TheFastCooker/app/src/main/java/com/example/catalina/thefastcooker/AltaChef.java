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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import static com.example.catalina.thefastcooker.DatabaseHelper.getHelper;

public class AltaChef extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;
    boolean sdDisponible = false;
    boolean sdAccesoEscritura = false;
    private TextInputLayout inputLayoutEmail,inputLayoutContra,inputLayoutNombre,inputLayoutApellidos,inputLayoutDireccion,inputLayoutPhone;
    private EditText email;
    private EditText contra;
    private EditText nombre;
    private EditText apellidos;
    private EditText direccion;
    private EditText phone;
    private ImageView imageView;
    private Button btnRegistro;
    private Bitmap bitmap;
    private Bitmap userBitmap;
    private DatabaseAdapter database;
    private Context context;
    private Chef chef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_chef);

        database = new DatabaseAdapter(this);
        database = getHelper(this);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutContra = (TextInputLayout) findViewById(R.id.input_layout_contra);
        inputLayoutNombre = (TextInputLayout) findViewById(R.id.input_layout_nombre);
        inputLayoutApellidos=(TextInputLayout) findViewById(R.id.input_layout_apellidos);
        inputLayoutDireccion=(TextInputLayout) findViewById(R.id.input_layout_direccion);
        inputLayoutPhone=(TextInputLayout) findViewById(R.id.input_layout_telefono);
        email = (EditText) findViewById(R.id.email_chef);
        contra=(EditText) findViewById(R.id.contra_chef);
        nombre=(EditText) findViewById(R.id.nombre_chef);
        apellidos=(EditText) findViewById(R.id.apellidos_chef);
        direccion=(EditText) findViewById(R.id.direccion_chef);
        phone=(EditText) findViewById(R.id.telefono_chef);
        btnRegistro=(Button) findViewById(R.id.registrarse);
        context=getApplicationContext();
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validado=validarForm(view);
                if(validado==true){
                    AddUserTask nuevaTarea = new AddUserTask(context,chef);
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
    }

    //Metodo para validar email

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public boolean validarEmail(){
        String emailChef= email.getText().toString();
        if(isValidEmail(emailChef)) {
            inputLayoutEmail.setErrorEnabled(false);
            return true;
        } else {
            inputLayoutEmail.requestFocus();
            inputLayoutEmail.setError("Error en el campo email");
            return false;
        }

    }
    public boolean validarTelefono(View view){
        String telefono=phone.getText().toString();
        boolean phoneOk=false;
        if(telefono.matches("[0-9]*")&&(telefono.length()==9)){
            inputLayoutPhone.setErrorEnabled(false);
            phoneOk=true;

        }else{
            inputLayoutPhone.requestFocus();
            inputLayoutPhone.setError("Error en el campo teléfono. Introduzca 9 digitos númericos.");
            phoneOk=false;
        }
        return phoneOk;
    }

    public boolean validarNombre(View view){
        if(nombre.getText().toString().length() == 0 ) {
            inputLayoutNombre.requestFocus();
            inputLayoutNombre.setError("Rellene el campo nombre!");
            return false;
        }else{
            inputLayoutNombre.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validarApellido(View view){
        if(apellidos.getText().toString().length() == 0 ) {
            inputLayoutApellidos.requestFocus();
            inputLayoutApellidos.setError("Rellene el campo apellidos!");
            return false;
        }else{
            inputLayoutApellidos.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validarContra(View view){
        if(contra.getText().toString().length() == 0 ) {
            inputLayoutContra.requestFocus();
            inputLayoutContra.setError("Rellene el campo contraseña!");
            return false;
        }else{
            inputLayoutContra.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validarDir(View view){
        if(direccion.getText().toString().length() ==0){
            inputLayoutDireccion.requestFocus();
            inputLayoutDireccion.setError("Debe especificar campo direccion");
            return false;
        }else{
            inputLayoutDireccion.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validarForm(View view){
        if(validarEmail()&& validarTelefono(view)&&validarContra(view)&&validarNombre(view)&&validarApellido(view)&&validarDir(view)&&getUserBitmap()!=null)
        {
            String emailChef= email.getText().toString();
            String pass=contra.getText().toString();
            String name=nombre.getText().toString();
            String lastName=apellidos.getText().toString();
            String address=direccion.getText().toString();
            int telefono=Integer.parseInt(phone.getText().toString());
            boolean existeEmail=database.existeEmailChef(emailChef);

            if(existeEmail==true) {
                Toast.makeText(this, "Ya existe un Chef con ese email dado de alta", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                chef=new Chef(emailChef, pass, name,lastName,telefono,address, "Madrid",getUserBitmap());
                return true;
            }
        }else{
            Toast.makeText(this, "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
    }//ValidarForm()

    private class AddUserTask extends AsyncTask<Chef, Void, Integer> {
        Context context;
        ProgressDialog pDialog;
        Chef chef;
        int newChefID;
        //Constructor
        public AddUserTask(Context contexto,Chef c){
            context = contexto;
            chef=c;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            Log.i("AsyncTask PreExecute", "Entra en PreExecute");
            pDialog = new ProgressDialog(AltaChef.this);

            pDialog.setMessage("Añadiendo chef en la DB "+chef.getNombre()+" "+chef.getApellido());
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(Chef... arg0) {
            // TODO Auto-generated method stub

            try{
                Thread.sleep(1000);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            boolean exito=database.addChef(chef);
            if(exito==true) {
                //guardamos el id del nuevo chef
                newChefID=database.getId(chef.getEmail(),chef.getPassword());
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
                Toast.makeText(context, "Acaba de ser dado de alta en el sistema", Toast.LENGTH_SHORT).show();
                //Le digo al intent de donde estoy a donde quiero ir
                Intent intent = new Intent(context, AltaComidaChef.class);
                Bundle id = new Bundle();
                id.putInt("idChef",newChefID);
                //Añadimos id Usuario info al intent
                intent.putExtras(id);
                startActivity(intent);
            }else{
                Toast.makeText(context, "Hubo un problema para darle de alta", Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
        }



    }//AsyncTask

    public Bitmap getUserBitmap() {
        return userBitmap;
    }

    public void setUserBitmap(Bitmap userBitmap) {
        this.userBitmap = userBitmap;
    }

    public void crearIntentGallery(){
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

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
        getMenuInflater().inflate(R.menu.menu_alta_chef, menu);
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
}
