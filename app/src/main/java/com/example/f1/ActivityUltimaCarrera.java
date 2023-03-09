package com.example.f1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityUltimaCarrera extends AppCompatActivity {

    private ArrayAdapter<Rowitem> adaptador;
    private ListView listView;
    private static final String API_BASE_URL = "https://ergast.com";
    private IF1ApiService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultima_carrera);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        service = retrofit.create(IF1ApiService.class);
        llamarApi();
    }
    private List<Rowitem> getRowList(){
        List<Rowitem> lista_filas = new ArrayList<Rowitem>();
        for(int i = 0;i<5;i++){
            lista_filas.add(new Rowitem("Piloto "+ i, i));
        }
        return lista_filas;
    }
    private void llamarApi(){
        Call<JsonObject> callAsync = service.getLastRace();
        Context c = this;
        callAsync.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject respuesta = response.body();
                List<Rowitem> filas = crearRowItems(respuesta);
                //ActivityUltimaCarrera.this <- contexto
                adaptador = new RowArrayAdapter(ActivityUltimaCarrera.this,
                        R.layout.row_layout, filas);
                listView = (ListView) findViewById(R.id.listview);
                listView.setAdapter(adaptador);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("error", "error", t);
                Toast.makeText(
                        getApplicationContext(),
                        "ERROR: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private List<Rowitem> crearRowItems(JsonObject respuesta) {
        List<Rowitem> listaFilas = new ArrayList<Rowitem>();
        JsonObject carrera = respuesta.getAsJsonObject("MRData")
                .getAsJsonObject("RaceTable")
                .getAsJsonArray("Races")
                .get(0)
                .getAsJsonObject();
        JsonArray resultados = carrera.getAsJsonArray("Results");
        for(int i = 0; i < resultados.size(); i++){
            JsonObject piloto = resultados.get(i).getAsJsonObject();
            JsonObject datosPiloto = piloto.get("Driver").getAsJsonObject();
            listaFilas.add(new Rowitem(datosPiloto.get("familyName").getAsString(),
                    piloto.get("points").getAsInt()));
        }
        return listaFilas;
    }

    private void callback(){
        List<Rowitem> filas = getRowList();
        adaptador = new RowArrayAdapter(this,
                R.layout.row_layout, filas);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adaptador);
    }
}