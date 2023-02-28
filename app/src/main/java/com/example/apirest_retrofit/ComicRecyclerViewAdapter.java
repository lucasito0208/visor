package com.example.apirest_retrofit;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apirest_retrofit.models.Comic;

import java.util.ArrayList;
import java.util.List;

public class ComicRecyclerViewAdapter extends RecyclerView.Adapter<ComicRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{

    ArrayList<Comic> comics;
    Comic comic;
    private View.OnClickListener comicListener;
    public ComicRecyclerViewAdapter(ArrayList<Comic> comics) {
        this.comics = comics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comic_list_item, null, false);

        v.setOnClickListener(this);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.IdCasilla.setText(comics.get(position).getNum());
        holder.TituloCasilla.setText(comics.get(position).getTitle());
        String fecha = comics.get(position).getDay() ;
        //+ "/" + comics.get(position).getMonth() + "/" + comics.get(position).getYear()
        holder.FechaCasilla.setText(fecha);

    }



    @Override
    public int getItemCount() {
        return comics.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.comicListener = listener;
    }

    @Override
    public void onClick(View view) {
        if(comicListener != null){
            comicListener.onClick(view);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView TituloCasilla, IdCasilla, FechaCasilla;

        public ViewHolder(View view) {
            super(view);
            IdCasilla = view.findViewById(R.id.idVista);
            TituloCasilla = view.findViewById(R.id.txtTituloLista);
            FechaCasilla = view.findViewById(R.id.txtFechaLista);
        }
    }

}


