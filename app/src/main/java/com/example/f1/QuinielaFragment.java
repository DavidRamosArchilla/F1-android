package com.example.f1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuinielaFragment extends Fragment {
    private List<Rowitem> listaPilotos;
    private RecyclerView recyclerView;
    private String idUsuario;

    public QuinielaFragment() {
        // Required empty public constructor
        listaPilotos = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiniela, container, false);
    }
    // comprobar si hay una quiniela en la base de datos con la fecha de la ultima carrera,
    // si la hay se le dan puntos (se moestra un mensaje o algo) y se borra de la base de datos
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Callback<JsonObject> callback = crearCallback();
        recyclerView = view.findViewById(R.id.recyclerView);
        ((PantallaInicioActivity)getActivity()).getService().getDrivers().enqueue(callback);
        idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FloatingActionButton btn = view.findViewById(R.id.subirQuiniela);
        comprobarUltimaCarrera();
        btn.setOnClickListener(v -> {
            // comprobar si ya se ha hecho una quiniela (SharedPreferences?)
            // a lo mejor se puede hacer que se deje acutalizar
//            mDatabase.child("id_usuario_2").child("30-04-2023").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.exists()){
//                        // la fecha esta ya en el fichero de quinielas para el usuario
//                    }
//                    else{
//
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
            crearDialog((dialog, which) -> {
                String fechaCarrera = "2023-04-02"; // TODO: hay que obtener la fecha, esto es para el ejemplo
                List<String> quiniela = obtenerQuiniela();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/quinielas");
                mDatabase.child(idUsuario).child(fechaCarrera).setValue(quiniela);

            });
        });
    }

    private void comprobarUltimaCarrera() {
        ((PantallaInicioActivity)getActivity()).getService().getLastRace().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String fechaUltimaCarrera = getFechaUltimaCarrera(response.body());
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/quinielas");
                mDatabase.child(idUsuario).child(fechaUltimaCarrera)
                        .addListenerForSingleValueEvent(new ComprobarQuinielaListener(getContext()));
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    private String getFechaUltimaCarrera(JsonObject response) {
        return response.getAsJsonObject("MRData")
                .getAsJsonObject("RaceTable")
                .getAsJsonArray("Races")
                .get(0)
                .getAsJsonObject()
                .get("date")
                .getAsString();
    }

    private List<String> obtenerQuiniela() {
        String[] arrayQuiniela = listaPilotos.stream().map(rowitem -> rowitem.getNombrePiloto()).toArray(size -> new String[size]);
        return Arrays.asList(arrayQuiniela);
    }

    private void crearDialog(DialogInterface.OnClickListener listenerAceptar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmar acción");
        builder.setMessage("¿Esats seguro de que quieres enviar esta quieniela? No podrás enviar otra hasta la siguiente carrera");
        builder.setPositiveButton("Aceptar", listenerAceptar);
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ItemTouchHelper.SimpleCallback getSimpleCallback() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                ((RecyclerAdapter.ViewHolder)viewHolder).cambiarIndice(toPosition);
                ((RecyclerAdapter.ViewHolder)target).cambiarIndice(fromPosition);
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
                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(listaPilotos);
                recyclerView.setAdapter(recyclerAdapter);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                        DividerItemDecoration.VERTICAL);
                recyclerView.addItemDecoration(dividerItemDecoration);
                ItemTouchHelper.SimpleCallback simpleCallback = getSimpleCallback();
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
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