package com.example.f1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCarrera#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCarrera extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String year;
    private String round;
    public FragmentCarrera() {
        // Required empty public constructor
    }

    public FragmentCarrera(String year, String round) {
        this.year=year;
        this.round=round;
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
    public static FragmentCarrera newInstance(String param1, String param2) {
        FragmentCarrera fragment = new FragmentCarrera();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private View view;
    private ArrayAdapter<Rowitem_clasificacionCarrera> adaptador;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    private List<Rowitem_clasificacionCarrera> crearRowItems(JsonObject respuesta) {
        List<Rowitem_clasificacionCarrera> listaFilas = new ArrayList<Rowitem_clasificacionCarrera>();
        JsonObject carrera = respuesta.getAsJsonObject("MRData")
                .getAsJsonObject("RaceTable")
                .getAsJsonArray("Races")
                .get(0)
                .getAsJsonObject();
        String circuit = carrera.get("raceName").getAsString();
        String date = carrera.get("date").getAsString();
        String time = carrera.get("time").getAsString();

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
            listaFilas.add(new Rowitem_clasificacionCarrera(datosPiloto.get("familyName").getAsString(),
                    piloto.get("points").getAsInt()));
        }
        return listaFilas;
    }
    private Callback<JsonObject> crearCallback(){
        return new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject respuesta = response.body();
                List<Rowitem_clasificacionCarrera> filas = crearRowItems(respuesta);
                if (getActivity()!=null)
                    adaptador = new RowArrayAdapter_clasificacionCarrera(getActivity(),
                        R.layout.row_posicion_carrera, filas);
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