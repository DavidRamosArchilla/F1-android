package com.example.f1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCarrera extends Fragment {
    private String year;
    private String round;
    public FragmentCarrera() {
        // Required empty public constructor
    }

    public FragmentCarrera(String year, String round) {
        this.year=year;
        this.round=round;
    }

    private View view;
    private ArrayAdapter<Rowitem_Clasificacion> adaptador;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Callback<JsonObject> callback = crearCallback();
        ((PantallaInicioActivity)getActivity()).getService().getResultOfRace(year,round).enqueue(callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_carrera, container, false);
        return view;
    }

    private List<Rowitem_Clasificacion> crearRowItems(JsonObject respuesta) {
        List<Rowitem_Clasificacion> listaFilas = new ArrayList<Rowitem_Clasificacion>();
        JsonObject carrera = respuesta.getAsJsonObject("MRData")
                .getAsJsonObject("RaceTable")
                .getAsJsonArray("Races")
                .get(0)
                .getAsJsonObject();
        String circuit = carrera.get("raceName").getAsString();
        String date = carrera.get("date").getAsString();
        String time = carrera.get("time").getAsString();

        //Cargar imagen
        String imageUri = ImagenesCircuitos.getImage(circuit);
        ImageView ivBasicImage = (ImageView) view.findViewById(R.id.circuitImage);
        Picasso.with(getActivity()).load(imageUri).into(ivBasicImage);


        TextView TVCircuit = (TextView) view.findViewById(R.id.textViewGranPremio);
        TVCircuit.setText(circuit);
        TextView TVDate = (TextView) view.findViewById(R.id.textViewFecha);
        TVDate.setText(date);
        TextView TVTime = (TextView) view.findViewById(R.id.textViewHora);
        TVTime.setText(time);
        JsonArray resultados = carrera.getAsJsonArray("Results");
        for(int i = 0; i < resultados.size(); i++){
            JsonObject piloto = resultados.get(i).getAsJsonObject();
            JsonObject datosPiloto = piloto.get("Driver").getAsJsonObject();
            listaFilas.add(new Rowitem_Clasificacion(datosPiloto.get("familyName").getAsString(),
                    piloto.get("points").getAsInt(), piloto.get("position").getAsInt()));
        }
        return listaFilas;
    }
    private Callback<JsonObject> crearCallback(){
        return new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject respuesta = response.body();
                List<Rowitem_Clasificacion> filas = crearRowItems(respuesta);
                if (getActivity()!=null)
                    adaptador = new RowArrayAdapter_Clasificacion(getActivity(),
                        R.layout.row_clasificacion, filas);
                System.out.println(listView);
                listView = (ListView) view.findViewById(R.id.listviewfr);
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