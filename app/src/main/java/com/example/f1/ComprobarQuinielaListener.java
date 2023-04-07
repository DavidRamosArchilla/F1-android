package com.example.f1;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ComprobarQuinielaListener implements ValueEventListener {
    private Context context;
    public ComprobarQuinielaListener(Context context){
        this.context = context;
    }
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()){
            // la fecha esta ya en el fichero de quinielas para el usuario
            // TODO: dar los puntos y mostrar un mensaje (activity nuevo, dialog..., lo que sea)
            // TODO: borrarla de la base de datos
            Toast.makeText(context, "hay una quiniela", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "no hay una quiniela", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
