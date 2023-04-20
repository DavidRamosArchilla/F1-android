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

public class RowArrayAdapter_listaCarreras extends ArrayAdapter<Rowitem_listaCarreras> {
    Context context;

    public RowArrayAdapter_listaCarreras(Context context, int resourceId, List<Rowitem_listaCarreras> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class RowItemHolder {
        TextView granPremio;
        TextView fecha;
        TextView hora;
        LinearLayout layout;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RowItemHolder holder = null;
        Rowitem_listaCarreras rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_lista_carreras, null);
            holder = new RowItemHolder();

            holder.granPremio = (TextView) convertView.findViewById(R.id.textViewGranPremio);
            holder.fecha = (TextView) convertView.findViewById(R.id.textViewFecha);
            holder.hora = (TextView) convertView.findViewById(R.id.textViewHora);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout_listaCarreras);
            convertView.setTag(holder);
        } else
            holder = (RowItemHolder) convertView.getTag();


        holder.granPremio.setText(rowItem.getGranPremio());
        holder.fecha.setText(rowItem.getFecha());
        holder.hora.setText(rowItem.getHora());
        holder.layout.setBackgroundColor(rowItem.getColor());

        return convertView;
    }
}