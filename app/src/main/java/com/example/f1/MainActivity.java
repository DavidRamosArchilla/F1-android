package com.example.f1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<Rowitem> adaptador;
    private ListView listView;
    private static final String API_BASE_URL = "https://ergast.com";
    private IF1ApiService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Rowitem> filas = getRowList();
        ArrayAdapter<Rowitem> adaptador = new RowArrayAdapter(this,
                R.layout.row_layout, filas);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adaptador);
    }
    private List<Rowitem> getRowList(){
        List<Rowitem> lista_filas = new ArrayList<Rowitem>();
        for(int i = 0;i<5;i++){
            lista_filas.add(new Rowitem("Piloto "+ i, i));
        }
        llamarApi();
        return lista_filas;
    }
    private void llamarApi(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(IF1ApiService.class);

        // Calling '/api/users/2'
       Call<JsonObject> callSync = service.getLastRace();

        try {
//            Response<String> response = callSync.execute();
//            String apiResponse = response.body();
//            Log.e("llamada api: \n", apiResponse);
            Log.i("llamada", callSync.execute().body().toString());
        } catch (Exception ex) {
            Log.e("error", "error", ex);
            ex.printStackTrace();
        }
    }

}