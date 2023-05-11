package com.example.f1;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentClasificacion extends Fragment {
    public FragmentClasificacion() {
        // Required empty public constructor
    }
    private View view;
    private ArrayAdapter<Rowitem_Clasificacion> adaptador;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_clasificacion, container, false);
        Callback<JsonObject> callback = crearCallback();
        ((PantallaInicioActivity)getActivity()).getService().getStanding("current").enqueue(callback);
        return view;
    }

    private List<Rowitem_Clasificacion> crearRowItems(JsonObject respuesta) {
        List<Rowitem_Clasificacion> listaFilas = new ArrayList<Rowitem_Clasificacion>();
        JsonArray clasificacion = respuesta.getAsJsonObject("MRData")
                .getAsJsonObject("StandingsTable")
                .getAsJsonArray("StandingsLists")
                .get(0).getAsJsonObject().getAsJsonArray("DriverStandings");
        for(int i = 0; i < clasificacion.size(); i++){
            JsonObject datosP = clasificacion.get(i).getAsJsonObject();
            listaFilas.add(new Rowitem_Clasificacion(
                    datosP.get("Driver").getAsJsonObject().get("familyName").getAsString(),
                    datosP.get("points").getAsInt(),
                    datosP.get("position").getAsInt()));
        }
        return listaFilas;
    }

    private Callback<JsonObject> crearCallback(){
        return new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                JsonObject respuesta = response.body();
                List<Rowitem_Clasificacion> filas = crearRowItems(respuesta);
                if (getActivity()!=null)
                    adaptador = new RowArrayAdapter_Clasificacion(getActivity(),
                            R.layout.row_clasificacion, filas);

                System.out.println(listView);
                listView = (ListView) view.findViewById(R.id.listviewClasificacion);
                System.out.println(listView);
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
        };
    }
}















