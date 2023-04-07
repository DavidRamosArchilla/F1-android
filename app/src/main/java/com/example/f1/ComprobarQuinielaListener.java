package com.example.f1;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComprobarQuinielaListener implements ValueEventListener {
    private Context context;
    private JsonObject respuesta;
    private final int PUNTUACION_MAXIMA = 400;
    public ComprobarQuinielaListener(Context context, JsonObject respuesta){
        this.context = context;
        this.respuesta = respuesta;
    }
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()){
            // la fecha esta ya en el fichero de quinielas para el usuario
            ArrayList<String> quiniela = (ArrayList<String>) dataSnapshot.getValue();
            int puntosQuiniela = calcularPuntosQuiniela(respuesta, quiniela);
            // TODO: borrarla de la base de datos y sumar los puntos en la base de datos
            Toast.makeText(context, "puntos: " + puntosQuiniela, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "no hay una quiniela", Toast.LENGTH_LONG).show();
        }
    }

    private int calcularPuntosQuiniela(JsonObject respuesta, List<String> quiniela) {
        List<String> resultadosCarrera = obtenerResultadosCarrera(respuesta);
        int error = 0;
        for(int i=0; i<resultadosCarrera.size(); i++){
            error += Math.abs(i - resultadosCarrera.indexOf(quiniela.get(i)));
        }
        return PUNTUACION_MAXIMA - error;
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
