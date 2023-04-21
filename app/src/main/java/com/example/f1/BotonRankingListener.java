package com.example.f1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class BotonRankingListener implements View.OnClickListener {
    public  BotonRankingListener(){

    }

    @Override
    public void onClick(View v) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("puntos/");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(v.getContext());
                builderSingle.setTitle("Ranking de puntos");
                Map<String, Map<String, Object>> puntosUsuarios = (Map<String, Map<String, Object>>)snapshot.getValue();
                int indice = 0;
                String[] ranking = new String[puntosUsuarios.size()];
                for (String idUsuario: puntosUsuarios.keySet()) {
                    Map<String, Object> datosUsuario = puntosUsuarios.get(idUsuario);
                    String nombre = String.valueOf(datosUsuario.get("nombre"));
                    String puntos = String.valueOf(datosUsuario.get("puntos"));
                    ranking[indice] = nombre + ": " + puntos;
                    Log.i("tag hashmap ", idUsuario + "     " + String.valueOf(puntosUsuarios.get(idUsuario)));
                    Log.i("user", datosUsuario.toString() + "    " + puntosUsuarios.get(idUsuario).getClass());
                    Log.i("valores: ", nombre + "  " + puntos);
                    Log.i("asd ", ranking[indice]);
                    indice++;
                }
                builderSingle.setItems(ranking, (dialog, which) -> {});
                builderSingle.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
