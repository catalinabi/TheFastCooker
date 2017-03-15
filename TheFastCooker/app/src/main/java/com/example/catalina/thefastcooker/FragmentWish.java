package com.example.catalina.thefastcooker;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentWish.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentWish#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentWish extends Fragment{

    public static final String TAG = "Fragment Home";
    private FragmentIterationListener mCallback = null;

    public interface FragmentIterationListener{
        public void onFragmentIteration(Bundle parameters);
    }

    public static FragmentWish newInstance(Bundle arguments){
        FragmentWish f = new FragmentWish();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }
    //Constructor vacio
    public FragmentWish(){

    }

    //El fragment se ha adjuntado al Activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish, container, false);
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

}
