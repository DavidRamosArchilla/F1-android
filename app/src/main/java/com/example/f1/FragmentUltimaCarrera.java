package com.example.f1;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUltimaCarrera#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUltimaCarrera extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentUltimaCarrera() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ultimaCarrera.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentUltimaCarrera newInstance(String param1, String param2) {
        FragmentUltimaCarrera fragment = new FragmentUltimaCarrera();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private View view;
    private ArrayAdapter<Rowitem> adaptador;
    private ListView listView;
    private static final String API_BASE_URL = "https://ergast.com";
    private IF1ApiService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        service = retrofit.create(IF1ApiService.class);
        llamarApi();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_ultima_carrera, container, false);
        return view;
    }

    private void llamarApi() {
        Call<JsonObject> callAsync = service.getLastRace();

        callAsync.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject respuesta = response.body();
                List<Rowitem> filas = crearRowItems(respuesta);
                adaptador = new RowArrayAdapter(getActivity(),
                        R.layout.row_layout, filas);
                listView = (ListView) view.findViewById(R.id.listviewfr);
                listView.setAdapter(adaptador);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("error", "error", t);
                Toast.makeText(
                        getActivity(),
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
}