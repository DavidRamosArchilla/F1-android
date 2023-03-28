package com.example.f1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PantallaInicioActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private static final String API_BASE_URL = "https://ergast.com";
    private IF1ApiService service;
    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio);
        mFirebaseAuth = FirebaseAuth.getInstance();
        service = crearService();
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        toolbar.setTitle("Clasificación");

        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, new FragmentClasificacion()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch(item.getItemId()){
                    case R.id.menu_frag_clasificacion:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame1, new FragmentClasificacion()).commit();
                        toolbar.setTitle("Clasificación");
                        return true;
                    case R.id.menu_frag_ultimaCarrera:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame1, new FragmentUltimaCarrera()).commit();
                        toolbar.setTitle("Última Carrera");
                        return true;
                }
                return false;
            }
        });
    }
    public IF1ApiService getService(){
        return service;
    }
    private IF1ApiService crearService(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(IF1ApiService.class);
    }

    public void logOut(View v) {
        mFirebaseAuth.signOut();
        Toast.makeText(this, R.string.signed_out, Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
    public void abrirUltimaCarrera (View view){
        Intent myIntent = new Intent(this, ActivityUltimaCarrera.class);
        startActivity(myIntent);
    }
}