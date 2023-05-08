package com.example.f1;

import android.graphics.Color;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rowitem_listaCarreras {
    private String granPremio;
    private String fecha;
    private String hora;

    private String year;

    private String round;
    private int color;

  //  private final Color colorSecondaryVariant = com.google.android.material.R.color.design_default_color_secondary_variant;

    //private final int aux = colorSecondaryVariant.toArgb();

    public Rowitem_listaCarreras(String granPremio, String fecha, String hora, String year, String round) {
        this.granPremio=granPremio;
        this.fecha=fecha;
        this.hora=hora;
        this.color=calcularColor();
        this.year=year;
        this.round=round;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public Rowitem_listaCarreras(String year, String round) {
        this.year = year;
        this.round = round;
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
                return 0xFF505050;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return 0xFF808080;
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
