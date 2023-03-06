package com.example.f1;

public class Rowitem {
    private String nombrePiloto;
    private int putnos;

    public Rowitem(String nombrePiloto, int putnos) {
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
}
