package com.example.f1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PantallaInicioActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public void logOut(View v) {
        mFirebaseAuth.signOut();
        Toast.makeText(this, R.string.signed_out, Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
    public void abrirUltimaCarrera (View view){
        Intent myIntent = new Intent(this, ActivityUltimaCarrera.class);
        startActivity(myIntent);
    }
}