package com.example.rodri.exercicio2_rodrigopitubadesouza;


import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import DAO.SeriesContract;


public class LembreteAdapter extends RecyclerView.Adapter<LembreteAdapter.ViewHolder> {
    private Cursor cursor;
    private OnSerieClickListener listener;

    public LembreteAdapter(Cursor c){
        cursor = c;
    }

    public interface OnSerieClickListener {
        void onSerieClick(View view, int position);
    }

    public void setOnSerieClickListener(OnSerieClickListener listener){
        this.listener = listener;
    }

    public void setCursor(Cursor c){
        cursor = c;
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View serieView = inflater.inflate(R.layout.series_layout,parent,false);
        ViewHolder holder = new ViewHolder(serieView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int idxTitulo = cursor.getColumnIndexOrThrow(SeriesContract.Lembrete.COLUMN_NAME_TITULO);
        int idxAutor = cursor.getColumnIndexOrThrow(SeriesContract.Lembrete.COLUMN_NAME_TEMPORADA);
        int idxEp = cursor.getColumnIndexOrThrow(SeriesContract.Lembrete.COLUMN_NAME_EP);
        cursor.moveToPosition(position);
        holder.txtTitulo.setText(cursor.getString(idxTitulo));
        holder.txtTemporada.setText(cursor.getString(idxAutor));
        holder.txtEp.setText(String.valueOf(cursor.getString(idxEp)));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtTitulo;
        public TextView txtTemporada;
        public TextView txtEp;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txt_serie_nome);
            txtTemporada = itemView.findViewById(R.id.txt_serie_temporada);
            txtEp = itemView.findViewById(R.id.txt_serie_episodio);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onSerieClick(v, position);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
            {
                listener.onSerieClick(v, position);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        int idxID = cursor.getColumnIndexOrThrow(SeriesContract.Lembrete._ID);
        cursor.moveToPosition(position);
        return cursor.getLong(idxID);
    }
}
