package com.example.apirest_retrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apirest_retrofit.models.Comic;
import com.example.apirest_retrofit.models.ComicApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {

    TextView titulo, fecha2;
    ImageView imagen2;
    Button siguiente, anterior, atras;

    ArrayList<Comic> listComic;

    ComicApi comicApi;

    Cursor cursor;
    public ComicDatabaseSqLiteOpenHelper helper;

    Comic comic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Intent i = getIntent();

        titulo = findViewById(R.id.tvTitulo);
        fecha2 = findViewById(R.id.tvFecha);
        imagen2 = findViewById(R.id.imgComic);

        siguiente = findViewById(R.id.btnSiguiente);
        anterior = findViewById(R.id.btnAnterior);
        atras = findViewById(R.id.btnVisorAtras);

        String num = i.getStringExtra("num");
        String title = i.getStringExtra("titulo");
        String date = i.getStringExtra("fecha");
        String image = i.getStringExtra("imagen");

        titulo.setText(title);
        fecha2.setText("Fecha de publicación: "+date);
        Picasso.get().load(image).into(imagen2);


        siguiente.setOnClickListener(view -> {
            if(helper.consultarListaComics(num+1)) {
                cursor = new ComicDatabaseSqLiteOpenHelper(this).consultarDatos();

            }
            sig(num);
        });

        anterior.setOnClickListener(view -> {

        });

        atras.setOnClickListener(view -> {
            Intent ii = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(ii);
        });
    }


    private void sig(String cod) {



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        comicApi = retrofit.create(ComicApi.class);

                Call<Comic> request = comicApi.find(cod+1);
                request.enqueue(new Callback<Comic>() {
                    @Override
                    public void onResponse(@NonNull Call<Comic> call, @NonNull Response<Comic> response) {

                        try {
                            if (response.isSuccessful()) {
                                comic = response.body();

                                //assert comic != null;
                                String num = comic.getNum();
                                String title = comic.getTitle();
                                titulo.setText(title);
                                String fecha = comic.getDay() + "/" + comic.getMonth() + "/" + comic.getYear();
                                fecha2.setText("Fecha de publicación: " + fecha);
                                String imagen = comic.getImg();
                                Picasso.get().load(imagen).into(imagen2);

                                //createComic(num, titulo, fecha, imagen);
                                //addComics(num, titulo, fecha, imagen);

                            }
                        } catch (Exception e) {
                            Toast.makeText(MainActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Comic> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity2.this, "Error en la conexión!", Toast.LENGTH_SHORT).show();
                    }
                });



    }

}