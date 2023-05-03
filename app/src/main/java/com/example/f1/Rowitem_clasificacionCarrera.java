package com.example.f1;

public class Rowitem_clasificacionCarrera {
    private String nombrePiloto;
    private int puntos;

    public Rowitem_clasificacionCarrera(String nombrePiloto, int putnos) {
        this.nombrePiloto = nombrePiloto;
        this.puntos = putnos;

    }

    public String getNombrePiloto() {
        return nombrePiloto;
    }

    public void setNombrePiloto(String nombrePiloto) {
        this.nombrePiloto = nombrePiloto;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }


}
