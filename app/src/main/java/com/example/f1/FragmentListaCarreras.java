package com.example.f1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FragmentListaCarreras extends Fragment {

    public FragmentListaCarreras() {
        // Required empty public constructor
    }
    private View view;
    private ArrayAdapter<Rowitem_listaCarreras> adaptador;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                listView.setAdapter(adaptador);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Rowitem_listaCarreras carrera = (Rowitem_listaCarreras) listView.getItemAtPosition(position);
                        String date= carrera.getFecha();

                        Date fechaActual=new Date();
                        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                        Date fecha = null;
                        try {
                            fecha = formato.parse(date);
                            if(fechaActual.compareTo(fecha)>0){
                                String year= carrera.getYear();
                                String round= carrera.getRound();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame1, new FragmentCarrera(year, round)).commit();
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

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