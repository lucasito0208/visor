package com.example.apirest_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apirest_retrofit.models.Comic;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity  {

    ArrayList<Comic> listComic;
    RecyclerView recyclerView;
    ComicRecyclerViewAdapter adapter;
    Comic comic;
    Cursor cursor;

    TextView num, titulo, fecha;
    ImageView img;
    public ComicDatabaseSqLiteOpenHelper helper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        recyclerView = findViewById(R.id.recyclerComics);
        titulo = findViewById(R.id.txtTituloLista);
        num = findViewById(R.id.idVista);
        fecha = findViewById(R.id.tvFecha);
        img = findViewById(R.id.imgComic);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cursor = new ComicDatabaseSqLiteOpenHelper(this).consultarDatos();

        listComic = new ArrayList<>();

        while(cursor.moveToNext()) {
            comic = new Comic(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2)
            );
            listComic.add(comic);
        }

        adapter = new ComicRecyclerViewAdapter(listComic);
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(view -> {
            /*
            Intent ii = new Intent(MainActivity3.this, MainActivity2.class);
            Toast.makeText(MainActivity3.this, "Selección: "+listComic.get(recyclerView.getChildAdapterPosition(view)).getTitle(), Toast.LENGTH_LONG).show();
            String titulo = listComic.get(recyclerView.getChildAdapterPosition(view)).getTitle();
            String fecha = listComic.get(recyclerView.getChildAdapterPosition(view)).getDay();
            String imagen = listComic.get(recyclerView.getChildAdapterPosition(view)).getImg();

            ii.putExtra("titulo", titulo);
            ii.putExtra("fecha", fecha);
            ii.putExtra("imagen", imagen);
            startActivity(ii);

             */
            cursor = new ComicDatabaseSqLiteOpenHelper(this).dameComic(listComic.get(recyclerView.getChildAdapterPosition(view)).getNum());
            while(cursor.moveToNext()) {

                Intent ii = new Intent(MainActivity3.this, MainActivity2.class);
                String titulo = cursor.getString(1);
                String fecha = cursor.getString(2);
                String imagen = cursor.getString(3);

                ii.putExtra("titulo", titulo);
                ii.putExtra("fecha", fecha);
                ii.putExtra("imagen", imagen);
                startActivity(ii);
        }});
    }



}