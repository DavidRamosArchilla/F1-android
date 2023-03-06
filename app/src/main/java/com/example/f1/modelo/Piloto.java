package com.example.f1.modelo;

import androidx.annotation.NonNull;

public class Piloto {
    private String nombrePiloto;
    private int putnos;

    public Piloto(String nombrePiloto, int putnos) {
        this.nombrePiloto = nombrePiloto;
        this.putnos = putnos;
    }

    public String getNombrePiloto() {
        return nombrePiloto;
    }

    public void setNombrePiloto(String nombrePiloto) {
        this.nombrePiloto = nombrePiloto;
    }

    public int getPutnos() {
        return putnos;
    }

    public void setPutnos(int putnos) {
        this.putnos = putnos;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
