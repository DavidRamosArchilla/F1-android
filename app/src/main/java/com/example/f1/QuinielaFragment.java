package com.example.f1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuinielaFragment extends Fragment {

    private List<Rowitem> listaPilotos;

    public QuinielaFragment() {
        // Required empty public constructor
        listaPilotos = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Callback<JsonObject> callback = crearCallback();
        ((PantallaInicioActivity)getActivity()).getService().getDrivers().enqueue(callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiniela, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(listaPilotos);
        recyclerView.setAdapter(recyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        ItemTouchHelper.SimpleCallback simpleCallback = getSimpleCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private ItemTouchHelper.SimpleCallback getSimpleCallback() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(listaPilotos, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

    }

    private Callback<JsonObject> crearCallback(){
        return new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject respuesta = response.body();
                listaPilotos = crearRowItems(respuesta);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("error", "error", t);
                Toast.makeText(
                        getActivity(),
                        "Error al conectarse: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        };
    }
    private List<Rowitem> crearRowItems(JsonObject respuesta) {
        List<Rowitem> listaFilas = new ArrayList<Rowitem>();
        JsonArray pilotos = respuesta.getAsJsonObject("MRData")
                .getAsJsonObject("DriverTable")
                .getAsJsonArray("Drivers");
        for(int i = 0; i < pilotos.size(); i++){
            JsonObject piloto = pilotos.get(i).getAsJsonObject();
            listaFilas.add(new Rowitem(piloto.get("familyName").getAsString(),
                    piloto.get("permanentNumber").getAsInt()));
        }
        return listaFilas;
    }
}