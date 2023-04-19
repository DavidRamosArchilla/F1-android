package com.example.f1;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentClasificacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentClasificacion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentClasificacion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment clasificacion.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentClasificacion newInstance(String param1, String param2) {
        FragmentClasificacion fragment = new FragmentClasificacion();
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
        ((PantallaInicioActivity)getActivity()).getService().getStanding("current").enqueue(callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clasificacion, container, false);
    }



    private Callback<JsonObject> crearCallback(){
        return new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject respuesta = response.body();
                List<Rowitem_clasificacionCarrera> filas = crearRowItems(respuesta);
                if (getActivity()!=null)
                    adaptador = new RowArrayAdapter_clasificacionCarrera(getActivity(),
                            R.layout.row_lista_carreras, filas);

                System.out.println(listView);
                listView = (ListView) view.findViewById(R.id.listviewListaCarreras);
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

    private List<Rowitem_clasificacionCarrera> crearRowItems(JsonObject respuesta) {
        List<Rowitem_clasificacionCarrera> listaFilas = new ArrayList<Rowitem_clasificacionCarrera>();
        JsonArray clasificacion = respuesta.getAsJsonObject("MRData")
                .getAsJsonObject("StandingsTable")
                .getAsJsonArray("StandingsLists");
        for(int i = 0; i < clasificacion.size(); i++){
            JsonObject datosP = clasificacion.get(i).getAsJsonObject();
            listaFilas.add(new Rowitem_clasificacionCarrera(
                    datosP.get("DriverStandings.Driver.driverId").getAsString(),
                    datosP.get("DriverStandings.points").getAsInt()));
        }
        return listaFilas;
    }
}















