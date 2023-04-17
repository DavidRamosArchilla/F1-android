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
 * Use the {@link FragmentListaCarreras#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListaCarreras extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentListaCarreras() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListaCarreras.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListaCarreras newInstance(String param1, String param2) {
        FragmentListaCarreras fragment = new FragmentListaCarreras();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private View view;
    private ArrayAdapter<Rowitem_listaCarreras> adaptador;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Callback<JsonObject> callback = crearCallback();
        ((PantallaInicioActivity)getActivity()).getService().getRacesOfYear("current").enqueue(callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lista_carreras, container, false);
        return view;
    }

    private List<Rowitem_listaCarreras> crearRowItems(JsonObject respuesta) {
        List<Rowitem_listaCarreras> listaFilas = new ArrayList<Rowitem_listaCarreras>();
        JsonArray carreras = respuesta.getAsJsonObject("MRData")
                .getAsJsonObject("RaceTable")
                .getAsJsonArray("Races");
        for(int i = 0; i < carreras.size(); i++){
            JsonObject datosGP = carreras.get(i).getAsJsonObject();
            listaFilas.add(new Rowitem_listaCarreras(datosGP.get("raceName").getAsString(),
                    datosGP.get("date").getAsString(),datosGP.get("time").getAsString(),
                    datosGP.get("season").getAsString(),datosGP.get("round").getAsString()));
        }
        return listaFilas;
    }
    private Callback<JsonObject> crearCallback(){
        return new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject respuesta = response.body();
                List<Rowitem_listaCarreras> filas = crearRowItems(respuesta);
                if (getActivity()!=null)
                    adaptador = new RowArrayAdapter_listaCarreras(getActivity(),
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
}