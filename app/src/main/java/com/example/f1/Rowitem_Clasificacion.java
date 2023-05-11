package com.example.f1;

public class Rowitem_Clasificacion {
    private String nombrePiloto;
    private int puntos;
    private int pos;

    public Rowitem_Clasificacion(String nombrePiloto, int puntos, int pos) {
        this.nombrePiloto = nombrePiloto;
        this.puntos = puntos;
        this.pos = pos;
    }

    public Rowitem_Clasificacion(String nombrePiloto, int puntos) {
        this.nombrePiloto = nombrePiloto;
        this.puntos = puntos;
    }

    public String getNombrePiloto() {
        return nombrePiloto;
    }
    public int getPuntos() {
        return puntos;
    }
    public int getPos() {
        return pos;
    }
}
