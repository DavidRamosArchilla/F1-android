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

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
