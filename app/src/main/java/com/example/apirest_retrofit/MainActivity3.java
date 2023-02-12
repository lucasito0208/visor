package com.example.apirest_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.apirest_retrofit.models.Comic;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity implements ComicRecyclerViewAdapter.OnComicClickListener{

    ArrayList<Comic> listComic;
    RecyclerView recyclerView;
    ComicRecyclerViewAdapter adapter;
    Comic comic;
    Cursor cursor;

    TextView num, titulo, fecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        recyclerView = findViewById(R.id.recyclerComics);

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

        //onComicClick(comic);
    }

    @Override
    public void onComicClick(Comic comicClicked) {

        TextView titulo = findViewById(R.id.txtTituloLista);
        titulo.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity3.this, MainActivity2.class);

            startActivity(i);
        });
    }
}