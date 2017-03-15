package com.example.catalina.thefastcooker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentDetailComida.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentDetailComida#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDetailComida extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static String ARG_PARAM1 = "param1";
    private TextView tituloFragment,descripcion,cocina,menaje,precio,duracion,precioTotal;
    private Button btnReservar;
    private ImageView imagen;
    private DatabaseAdapter database;
    private Comida detalleComida;
    private int comidaId;
    private int chefId;
    private int pr;
    private int numCom;

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int year, monthOfYear, dayOfMonth,hourOfDay, minute;
    private Spinner comboComensales;
    int horaSeleccionada;
    String comensalesSeleccionados;
    Date dateReservaInicio;
    Date dateReservaFin;
    // TODO: Rename and change types of parameters
    private String mParam1;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment FragmentDetailComida.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDetailComida newInstance(String param1, String param2) {
        FragmentDetailComida fragment = new FragmentDetailComida();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentDetailComida() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            comidaId=Integer.parseInt(mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_detail_comida, container, false);
        database = DatabaseHelper.getHelper(getContext());
        //Aqui guardamos en la view los diferentes elementos del xml
        tituloFragment=(TextView) view.findViewById(R.id.titulo);
        imagen=(ImageView) view.findViewById(R.id.imageView);
        descripcion=(TextView) view.findViewById(R.id.descripcion);
        cocina=(TextView) view.findViewById(R.id.cocina);
        menaje=(TextView) view.findViewById(R.id.menaje);
        precio=(TextView) view.findViewById(R.id.precio);
        duracion=(TextView) view.findViewById(R.id.duracion);
        btnReservar=(Button)view.findViewById(R.id.reservar);
        btnDatePicker=(Button)view.findViewById(R.id.btn_date);
        btnTimePicker=(Button)view.findViewById(R.id.btn_time);
        txtDate=(EditText)view.findViewById(R.id.in_date);
        txtTime=(EditText)view.findViewById(R.id.in_time);
        precioTotal=(TextView)view.findViewById(R.id.preciototal);
        //Llamamos a la DB para recoger el objeto Comida
        detalleComida=database.listarDetalleComida(comidaId);
        //Llamos a la db para recoger los detalles del Chef
        chefId=detalleComida.getIdChef();
        String nombreChef=database.getDatosChefForId(chefId);
        //Cambiamos los elementos de la view
        tituloFragment.setText(detalleComida.getTitulo_menu()+" del Chef " +nombreChef);
        descripcion.setText(detalleComida.getDescripcion());
        cocina.setText(detalleComida.getRequisitosCocina());
        menaje.setText(detalleComida.getRequisitosMenaje());
        imagen.setImageBitmap(detalleComida.getThumbnail());
        precioTotal.setText("PRECIO TOTAL: " + detalleComida.getPrecio_comensal()+ "€");
        precio.setText("PRECIO: " + detalleComida.getPrecio_comensal() + "€/persona");
        duracion.setText("DURACIÓN ESTIMADA DE ELABORACIÓN: "+detalleComida.getDuracion()+" horas aproximadamente");

        btnReservar.setOnClickListener(this);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        //Select con duracion----------------------------------------
        List<String> duracion=getListadoComensales();
        ArrayAdapter<String> adaptadorComensales = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,duracion);
        comboComensales = (Spinner)view.findViewById(R.id.comboComensales);
        comboComensales.setAdapter(adaptadorComensales);
        comboComensales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                comensalesSeleccionados = parent.getItemAtPosition(position).toString();
                numCom=Integer.parseInt(comensalesSeleccionados);
                pr=detalleComida.getPrecio_comensal();
                String precioFinal=calcularPrecioTotal(pr,numCom);
                precioTotal.setText("PRECIO TOTAL: "+precioFinal+"€");
                //Toast.makeText(getContext(), comensalesSeleccionados, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                comensalesSeleccionados = parent.getItemAtPosition(0).toString();
                //Toast.makeText(getContext(), "Precio total"+comensalesSeleccionados, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    //Retorna listado de comensales
    public List<String> getListadoComensales(){
        List<String> listadoComensales= new ArrayList<String>() ;
        for(int i=1;i<=10;i++){
            listadoComensales.add(String.valueOf(i));
        }
        return listadoComensales;
    }
    //Retorna precioTotal en String
    public String calcularPrecioTotal(int p, int comensales){
        int precioTotal=p*comensales;
        String pTotal=String.valueOf(precioTotal);
        return pTotal;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
    }
    //Metodo implementado al pulsar en el boton
    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "ESTA OPCIÓN ESTARÁ DISPONIBLE EN LA VERSIÓN 2", Toast.LENGTH_LONG).show();

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = c.getTime();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {


                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);

            //Get the DatePicker instance from DatePickerDialog
            DatePicker dp = datePickerDialog.getDatePicker();
            //Evitamos que el usuario pueda elegir una fecha anterior a la fecha actual +24horas(mañana)
            dp.setMinDate(tomorrow.getTime());
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == btnReservar) {

            if(txtDate.getText().toString().length() == 0 || txtTime.getText().toString().length() == 0 ) {
                Toast.makeText(getContext(), "Debe introducir una fecha y hora valida ", Toast.LENGTH_SHORT).show();
            }else{


                String valorHoras = txtTime.getText().toString();
                boolean horaOk=validarHora(valorHoras);
                if(horaOk==true){
                    String dateReservaI = txtDate.getText().toString()+" "+txtTime.getText().toString()+":00";
                    String tiempoFinal=calcularHoraFinal();
                    String dateReservaF=txtDate.getText().toString()+" "+ tiempoFinal;


                   SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                    dateReservaInicio = null;
                    dateReservaFin =null;
                    try {
                        dateReservaInicio = sdf.parse(dateReservaI);
                        dateReservaFin = sdf.parse(dateReservaF);
                        Toast.makeText(getContext(), "Reserva Inicio" + dateReservaInicio, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Reserva Fin" + dateReservaFin, Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getContext(), "Debe seleccionar una hora entre las 10:00am y las 20.00pm. Ha escrito " + horaSeleccionada, Toast.LENGTH_SHORT).show();

                }


                /*if(horaSeleccionada>10&&horaSeleccionada<20){
                    Toast.makeText(getContext(), "Hora seleccionada" + horaSeleccionada, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "FechaReserva es " + dateReserva, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Ahora es(milisegundos) " + dateReserva.getTime(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Fecha Reserva(milisegundos) " + dateR.getTime(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Debe seleccionar una hora entre las 10:00am y las 20.00pm" + horaSeleccionada, Toast.LENGTH_SHORT).show();
                }
*/

            }
        }
    }
    public boolean validarHora(String valorHoras){
        String s[] = valorHoras.split(":");
        horaSeleccionada=Integer.parseInt(s[0]);
        if(horaSeleccionada>10&&horaSeleccionada<20) {
            return true;
        }else{
            return false;
        }
    }
    public String calcularHoraFinal(){
        //Toast.makeText(getContext(), "Hora seleccionada" + horaSeleccionada, Toast.LENGTH_SHORT).show();
        int horaFinal=horaSeleccionada+detalleComida.getDuracion();
        //Toast.makeText(getContext(), "Hora final" + horaFinal, Toast.LENGTH_SHORT).show();
        String s[]=txtTime.getText().toString().split(":");
        String tiempoFin=String.valueOf(horaFinal)+":"+s[1]+":00";
        //Toast.makeText(getContext(), "Tiempo final" + tiempoFin, Toast.LENGTH_SHORT).show();
        return tiempoFin;
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
        public void onFragmentInteraction(Uri uri);
    }

}
