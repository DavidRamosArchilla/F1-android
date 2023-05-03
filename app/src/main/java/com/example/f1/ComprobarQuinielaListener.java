package com.example.f1;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComprobarQuinielaListener implements ValueEventListener {
    private final String nombreUsuario;
    private Context context;
    private JsonObject respuesta;
    private String idUsuario;
    private String fechaCarrera;

    public ComprobarQuinielaListener(Context context, JsonObject respuesta, String fechaCarrera){
        this.context = context;
        this.respuesta = respuesta;
        this.idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.nombreUsuario = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        this.fechaCarrera = fechaCarrera;
    }
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()){
            // la fecha esta ya en el fichero de quinielas para el usuario
            ArrayList<String> quiniela = (ArrayList<String>) dataSnapshot.getValue();
            int puntosQuiniela = calcularPuntosQuiniela(respuesta, quiniela);
            sumarPuntosDb(puntosQuiniela);
            borrarQuiniela(fechaCarrera);
            Toast.makeText(context, "puntos: " + puntosQuiniela, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "no hay una quiniela", Toast.LENGTH_LONG).show();
        }
    }

    private void borrarQuiniela(String fechaCarrera) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/quinielas");
        mDatabase.child(idUsuario).child(fechaCarrera).removeValue();
    }

    private void sumarPuntosDb(int puntosQuiniela) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("/puntos");
        Map<String, Object> updates = new HashMap<>();
        updates.put(idUsuario + "/puntos", ServerValue.increment(puntosQuiniela));
        updates.put(idUsuario + "/nombre", nombreUsuario);
        mDatabase.updateChildren(updates);
    }

    private int calcularPuntosQuiniela(JsonObject respuesta, List<String> quiniela) {
        List<String> resultadosCarrera = obtenerResultadosCarrera(respuesta);
        int error = 0;
        for(int i=0; i<resultadosCarrera.size(); i++){
            error += Math.abs(i - resultadosCarrera.indexOf(quiniela.get(i)));
        }
        int PUNTUACION_MAXIMA = 400;
        int PUNTUACION_MINIMA = 200;
        return PUNTUACION_MAXIMA - PUNTUACION_MINIMA - error;
    }

    private List<String> obtenerResultadosCarrera(JsonObject respuesta) {
        JsonArray resultados = respuesta.getAsJsonObject("MRData")
                .getAsJsonObject("RaceTable")
                .getAsJsonArray("Races")
                .get(0)
                .getAsJsonObject()
                .getAsJsonArray("Results");
        int tam = resultados.size();
        String[] resultadoCarrera = new String[tam];
        for(int i=0; i<tam; i++){
            String piloto = resultados.get(i)
                    .getAsJsonObject()
                    .get("Driver")
                    .getAsJsonObject()
                    .get("familyName")
                    .getAsString();
            resultadoCarrera[i] = piloto;
        }
        return Arrays.asList(resultadoCarrera);
    }


    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
