package com.example.f1;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RowArrayAdapter extends ArrayAdapter<Rowitem> {
    Context context;

    public RowArrayAdapter(Context context, int resourceId, List<Rowitem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class RowItemHolder {
        TextView piloto;
        TextView puntos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RowItemHolder holder = null;
        Rowitem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_layout, null);
            holder = new RowItemHolder();

            holder.piloto = (TextView) convertView.findViewById(R.id.textViewPiloto);
            holder.puntos = (TextView) convertView.findViewById(R.id.textViewPuntos);
            convertView.setTag(holder);
        } else
            holder = (RowItemHolder) convertView.getTag();


        holder.piloto.setText(rowItem.getNombrePiloto());
        holder.puntos.setText(String.valueOf(rowItem.getPutnos()));

        return convertView;
    }
}