package com.example.f1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class RowArrayAdapter_Clasificacion extends ArrayAdapter<Rowitem_Clasificacion> {
    Context context;

    public RowArrayAdapter_Clasificacion(Context context, int resourceId, List<Rowitem_Clasificacion> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class RowItemHolder{

        TextView nombrePiloto;

        TextView puntos;

        TextView puesto;

        LinearLayout layout;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        RowItemHolder holder = null;
        Rowitem_Clasificacion rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.row_clasificacion, null);
            holder = new RowItemHolder();

            holder.nombrePiloto = (TextView) convertView.findViewById(R.id.textViewNombrePiloto);
            holder.puntos = (TextView) convertView.findViewById(R.id.textViewPuntosTotales);
            holder.puesto = (TextView) convertView.findViewById(R.id.textViewPuesto);

            convertView.setTag(holder);
        } else
            holder = (RowItemHolder) convertView.getTag();

        holder.nombrePiloto.setText(rowItem.getNombrePiloto());
        holder.puntos.setText(String.valueOf(rowItem.getPuntos()));
        holder.puesto.setText(String.valueOf(rowItem.getPos()));

        return convertView;
    }
}


