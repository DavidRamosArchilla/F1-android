package com.example.f1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rowitem_listaCarreras {
    private String granPremio;
    private String fecha;
    private String hora;
    private int color;


    public Rowitem_listaCarreras(String granPremio, String fecha, String hora) {
        this.granPremio=granPremio;
        this.fecha=fecha;
        this.hora=hora;
        this.color=calcularColor();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int calcularColor() {
        Date fechaActual=new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaGP = formato.parse(getFecha());
            if(fechaActual.compareTo(fechaGP)>0){
                return 0xFFFFFFFF;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return 0xFFD5D5D5;
    }

    public String getGranPremio() {
        return granPremio;
    }

    public void setGranPremio(String granPremio) {
        this.granPremio = granPremio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
