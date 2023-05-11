package com.example.f1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

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


  //  private final Color colorSecondaryVariant = com.google.android.material.R.color.design_default_color_secondary_variant;

    //private final int aux = colorSecondaryVariant.toArgb();



    public Rowitem_listaCarreras(String granPremio, String fecha, String hora, String year, String round) {
        this.granPremio=granPremio;
        this.fecha=fecha;
        this.hora=hora;
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

    public int getColor(Context context) {
        return calcularColor(context);
    }

//    public void setColor(int color) {
//        this.color = color;
//    }

    private int calcularColor(Context context) {
        Date fechaActual=new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaGP = formato.parse(getFecha());
            if(fechaActual.compareTo(fechaGP)>0){
                TypedValue typedValue = new TypedValue();
                context.getTheme().resolveAttribute(R.attr.colorSecondary, typedValue, true);
                @ColorInt int color = typedValue.data;
                return color;

            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorSecondaryVariant, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
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
