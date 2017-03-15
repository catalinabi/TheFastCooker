package com.example.catalina.thefastcooker;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;


public class UserActivity extends AppCompatActivity implements FragmentHome.OnFragmentInteractionListener,FragmentComida.OnFragmentInteractionListener,FragmentDetailComida.OnFragmentInteractionListener {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    TextView nombreUsuario;
    UsuarioActivo usuarioActivo;
    ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        //esto seria la variable enviada
        int id=bundle.getInt("idUsuario");
        Log.i("INFO", "ID de usuario actual" + id);
        usuarioActivo=new UsuarioActivo(id);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //Este es nuestro menu customizado
        mNavigationView = (NavigationView) findViewById(R.id.menu_customizado) ;

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.nav_item_sent) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new SentFragment()).commit();

                }

                if (menuItem.getItemId() == R.id.nav_item_inbox) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //optional

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
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
    //Implemento metodo de la interfaz que esta en el fragment de Home
    @Override
    public void onChefSelected(int idChef){
        Log.i("INFO", "Activity User: ha clicado en el Chef con ID " + idChef);
        //Guardamos en nuestra clase usuarioActivo el chef que ha pulsado el usuario
        usuarioActivo.setChefActiveId(idChef);
        String id=String.valueOf(idChef);
        FragmentComida newFragment = new FragmentComida();
        Bundle args = new Bundle();
        args.putString(FragmentComida.ARG_PARAM1, id);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerView, newFragment,"FRAGMENT_COMIDA");
        transaction.addToBackStack(null);
        transaction.commit();


        }

    //interfaz de fragmentInteraction de Fragment Comida
    @Override
    public void onComidaSelected(int comidaId){
        Log.i("INFO", "Activity User: ha clicado en la comida con ID " + comidaId);
        //Guardamos en nuestra clase usuarioActivo el id de comida que ha pulsado el usuario
        usuarioActivo.setComidaActiveId(comidaId);
        String id=String.valueOf(comidaId);
        FragmentDetailComida newFragment = new FragmentDetailComida();
        Bundle args = new Bundle();
        args.putString(FragmentDetailComida.ARG_PARAM1, id);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerView, newFragment,"FRAGMENT_DETAIL_COMIDA");
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
