package com.example.f1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    List<Rowitem_Clasificacion> listaPilotos;

    public RecyclerAdapter(List<Rowitem_Clasificacion> listaPilotos) {
        this.listaPilotos = listaPilotos;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_layout_quiniela, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.nombreTextView.setText(listaPilotos.get(position).getNombrePiloto());
        holder.numeroTextView.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return listaPilotos.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombreTextView, numeroTextView;
        Button moveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreTextView = itemView.findViewById(R.id.textViewPiloto);
            numeroTextView = itemView.findViewById(R.id.textViewPosicion);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
        public void cambiarIndice(int posicion){
            numeroTextView.setText(String.valueOf(posicion + 1));
        }
    }
}
