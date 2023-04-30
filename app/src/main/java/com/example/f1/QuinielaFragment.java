package com.example.f1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuinielaFragment extends Fragment {
    private List<Rowitem_clasificacionCarrera> listaPilotos;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Callback<JsonObject> callback = crearCallback();
        recyclerView = view.findViewById(R.id.recyclerView);
        ((PantallaInicioActivity)getActivity()).getService().getDrivers().enqueue(callback);
        idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FloatingActionButton btn = view.findViewById(R.id.subirQuiniela);
        comprobarUltimaCarrera();
        mostrarPuntos(view);
        Button botonRanking = view.findViewById(R.id.buttonRanking);
        botonRanking.setOnClickListener(new BotonRankingListener());
        btn.setOnClickListener(v -> {
            crearDialog((dialog, which) -> {
                new Thread(() -> {
                    CompletableFuture<String> future = obtenerFechaSiguienteCarrera();
                    String fechaCarrera;
                    try {
                        // de esta forma se bloquea el thread hasta que la llamada a la api acaba
                        fechaCarrera = future.get();
                    } catch (ExecutionException | InterruptedException e) {
                        Snackbar.make(view, "No se ha podido obtener datos de la siguiente carrera", Snackbar.LENGTH_LONG).show();
                        throw new RuntimeException(e);
                    }
                    List<String> quiniela = obtenerQuiniela();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/quinielas");
                    mDatabase.child(idUsuario).child(fechaCarrera).setValue(quiniela);
                    Snackbar.make(view, "Se ha enviado correctamente para el: " + fechaCarrera, Snackbar.LENGTH_LONG).show();
                }).start();
            });
        });
    }

    private CompletableFuture<String> obtenerFechaSiguienteCarrera() {
        CompletableFuture<String> future = new CompletableFuture<>();
        ((PantallaInicioActivity)getActivity()).getService().getNextRace().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String fecha = getFechaCarrera(response.body());
                future.complete(fecha);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    private void mostrarPuntos(View view) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("puntos/" + idUsuario + "/puntos");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TextView textViewPuntos = view.findViewById(R.id.textViewPuntosUsuario);
                String puntos =  String.valueOf(snapshot.getValue());
                if (puntos.equals("null")){
                    puntos = "0";
                }
                String texto = "Puntos: " + puntos;
                textViewPuntos.setText(texto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // comprobar si hay una quiniela en la base de datos con la fecha de la ultima carrera,
    // si la hay se le dan puntos (se moestra un mensaje o algo) y se borra de la base de datos
    private void comprobarUltimaCarrera() {
        ((PantallaInicioActivity)getActivity()).getService().getLastRace().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String fechaUltimaCarrera = getFechaCarrera(response.body());
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/quinielas");
                mDatabase.child(idUsuario).child(fechaUltimaCarrera)
                        .addListenerForSingleValueEvent(new ComprobarQuinielaListener(getContext(), response.body(), fechaUltimaCarrera));
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    private String getFechaCarrera(JsonObject response) {
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
        builder.setMessage("¿Estás seguro de que quieres enviar esta quieniela? Si ya has subido una se actualizará");
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
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireActivity(),
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
    private List<Rowitem_clasificacionCarrera> crearRowItems(JsonObject respuesta) {
        List<Rowitem_clasificacionCarrera> listaFilas = new ArrayList<Rowitem_clasificacionCarrera>();
        JsonArray pilotos = respuesta.getAsJsonObject("MRData")
                .getAsJsonObject("DriverTable")
                .getAsJsonArray("Drivers");
        for(int i = 0; i < pilotos.size(); i++){
            JsonObject piloto = pilotos.get(i).getAsJsonObject();
            listaFilas.add(new Rowitem_clasificacionCarrera(piloto.get("familyName").getAsString(),
                    piloto.get("permanentNumber").getAsInt()));
        }
        return listaFilas;
    }
}