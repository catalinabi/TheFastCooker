package com.example.catalina.thefastcooker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentComida.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentComida#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentComida extends Fragment  implements AdapterView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static String ARG_PARAM1 = "param1";
    private DatabaseAdapter database;
    private ListView listView;
    private TextView tituloFragment;
    private ArrayList<Comida> listadoComidas;
    private ArrayList<ComidasList> comidas;
    private ListAdapterComidas adapter;
    private CargarListView nuevaTarea;
    private int chefActual;
    private int comidaId;
    private String nombreChef;

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment FragmentComida.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentComida newInstance(String param1) {
        FragmentComida fragment = new FragmentComida();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;


    }

    public FragmentComida() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            chefActual=Integer.parseInt(mParam1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comida, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);
        tituloFragment=(TextView) view.findViewById(R.id.textView);
        //Borro el titulo. Lo metere en el asynctask
        tituloFragment.setText("");
        comidas = new ArrayList<ComidasList>();
        Toast.makeText(getContext(), "El chef tiene ID "+chefActual, Toast.LENGTH_SHORT).show();
        nuevaTarea = new CargarListView(getContext());
        nuevaTarea.execute();
        listView.setOnItemClickListener(this);
        return view;

    }
    //AsyncTask-> carga lista con Comidas
    private class CargarListView extends AsyncTask<Void, Integer,Integer> {
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
        protected Integer doInBackground(Void... strings) {
            // TODO Auto-generated method stub

            try{
                Thread.sleep(1000);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            database = DatabaseHelper.getHelper(context);

            listadoComidas= database.recuperarComidas(chefActual);
            if(listadoComidas!=null) {
                for (Comida c : listadoComidas) {
                    int id = c.getId_comida();
                    String nombre = c.getTitulo_menu().toUpperCase();
                    String desc=c.getDescripcion();
                    ComidasList comida = new ComidasList(id, nombre,desc, c.getThumbnail());
                    comidas.add(comida);
                }
                return 1;
            }else{
                return 0;
            }



        }

        @Override
        protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result==1) {
                Log.i("onPostExecute", "Crea el Adaptador");
                ListAdapterComidas adaptador = new ListAdapterComidas(comidas,context);
                listView.setAdapter(adaptador);
                nombreChef=database.getDatosChefForId(chefActual);
                tituloFragment.setText("Menus del Chef "+ nombreChef);
            }else{
                Toast.makeText(context, "El chef "+chefActual+" No tiene comidas asociadas", Toast.LENGTH_SHORT).show();
                tituloFragment.setText("El Chef " + nombreChef + "no tiene menus asignados");
            }

            pDialog.dismiss();
        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
           // mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        //Cancelo el asyntask si hay cambio de fragment
        nuevaTarea.cancel(true);

    }
    //El Activity que contiene el Fragment ha terminado su creación
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("INFO", "FragmentComida cargado:------------------"+mParam1);
        chefActual=Integer.parseInt(mParam1);
        Log.i("INFO", "Este es el fragmento comidas para el Chef con ID " + chefActual);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ComidasList c = (ComidasList) listView.getAdapter().getItem(position);
        comidaId=c.getId();

        Toast.makeText(getContext(), "Has pulsado en la Comida con id : " + comidaId, Toast.LENGTH_LONG).show();
        mListener.onComidaSelected(comidaId);


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onComidaSelected(int comidaId);
    }

}
