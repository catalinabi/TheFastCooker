package com.example.catalina.thefastcooker;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class FragmentHome extends Fragment implements AdapterView.OnItemClickListener{

    public static final String TAG = "Fragment Home";
    private FragmentIterationListener mCallback = null;
    private DatabaseAdapter database;
    private ArrayList<Chef> listadoChefs;
    private ArrayList<Heroes> heroes;
    private ListView listView;
    private ListAdapter adapter;
    private int idChef;
    //aqui voy a guardar la interface de main en mListener
    private OnFragmentInteractionListener mListener;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Heroes h = (Heroes) listView.getAdapter().getItem(position);
        idChef=h.getId();

        Toast.makeText(getContext(), "Has pulsado el Chef con id : " + idChef, Toast.LENGTH_LONG).show();

        //Aviso al Activity de que se ha clicado en el ChefId x
        mListener.onChefSelected(idChef);
    }

    public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }

    public static FragmentHome newInstance(Bundle arguments){
        FragmentHome f = new FragmentHome();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }
    //Constructor vacio
    public FragmentHome(){

    }

    //El fragment se ha adjuntado al Activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Le paso el interface de la activity a mi fragmentBoton
        mListener = (OnFragmentInteractionListener) context;
    }

    //El Fragment ha sido creado
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //El Fragment va a cargar su layout, el cual debemos especificar
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);
        heroes = new ArrayList<Heroes>();
        CargarListView nuevaTarea = new CargarListView(getContext());
        nuevaTarea.execute();
        listView.setOnItemClickListener(this);

        return view;
    }

    private class CargarListView extends AsyncTask<Void, Void, ListAdapter>{
        Context context;
        ProgressDialog pDialog;

        public CargarListView(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            Log.i("AsyncTask PreExecute", "Entra en PreExecute");
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Cargando Lista");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected ListAdapter doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            try{
                Thread.sleep(1000);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            database = DatabaseHelper.getHelper(context);
            listadoChefs= database.recuperarChefs();
            for (Chef c : listadoChefs){
                //String nombre=c.getId()+c.getNombre() +" " + c.getApellido();
                String nombre=c.getNombre() +" " + c.getApellido();
                Heroes hero= new Heroes(c.getId(),nombre, c.getThumbnail());
                heroes.add(hero);
            }

            for (int i= 0; i < 3; i++) {
            }
            Log.i("doInBackground", "Crea el Adaptador");
            ListAdapter adaptador = new ListAdapter(heroes,context);
            return adaptador;
        }

        @Override
        protected void onPostExecute(ListAdapter result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            listView.setAdapter(result);
            pDialog.dismiss();
        }

    }
    //La vista de layout ha sido creada y ya está disponible
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //La vista ha sido creada y cualquier configuración guardada está cargada
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    //El Activity que contiene el Fragment ha terminado su creación
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    //El Fragment ha sido quitado de su Activity y ya no está disponible
    @Override
    public void onDetach() {
        super.onDetach();
    }

    //Interface implementada por el Main
    public interface OnFragmentInteractionListener{
        void onChefSelected(int idChef);

    }


}
