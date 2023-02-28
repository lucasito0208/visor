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

    TextView titulo2, fecha2;
    ImageView imagen2;
    Button siguiente, anterior, atras;

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "titulo";
    public static final String KEY_FECHA = "fecha";
    public static final String KEY_IMAGEN = "imagen";
    public static final String TABLE_COMIC = "comic";

    ArrayList<Comic> listComic;

    String num;

    ComicApi comicApi;

    Cursor cursor;
    public ComicDatabaseSqLiteOpenHelper helper;

    Comic comic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Intent i = getIntent();

        titulo2 = findViewById(R.id.tvTitulo);
        fecha2 = findViewById(R.id.tvFecha);
        imagen2 = findViewById(R.id.imgComic);

        siguiente = findViewById(R.id.btnSiguiente);
        anterior = findViewById(R.id.btnAnterior);
        atras = findViewById(R.id.btnVisorAtras);

        num = i.getStringExtra("num");
        String title = i.getStringExtra("titulo");
        String date = i.getStringExtra("fecha");
        String image = i.getStringExtra("imagen");

        titulo2.setText(title);
        fecha2.setText("Fecha de publicación: "+date);
        Picasso.get().load(image).into(imagen2);


        siguiente.setOnClickListener(view -> {
            sig(num);
        });

        anterior.setOnClickListener(view -> {

        });

        atras.setOnClickListener(view -> {
            Intent ii = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(ii);
        });
    }

    public void sig(String cod) {
        helper = new ComicDatabaseSqLiteOpenHelper(this);
        if(helper.consultarListaComics(num)) {
            cursor = new ComicDatabaseSqLiteOpenHelper(this).dameComic(cod+1);
            while(cursor.moveToNext()) {

                String titulo = cursor.getString(1);
                String fecha = cursor.getString(2);
                String imagen = cursor.getString(3);

                titulo2.setText(titulo);
                fecha2.setText(fecha);
                Picasso.get().load(imagen).into(imagen2);


                System.out.println("Se encuentra en la base de datos");

            }
        }else{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://xkcd.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            comicApi = retrofit.create(ComicApi.class);
            Call<Comic> request = comicApi.find(cod);


            request.enqueue(new Callback<Comic>() {
                @Override
                public void onResponse(@NonNull Call<Comic> call, @NonNull Response<Comic> response) {
                    try{
                        if(response.isSuccessful()) {
                            Comic comic = response.body();

                            assert comic != null;
                            String num = comic.getNum();
                            String titulo = comic.getTitle();
                            String fecha = comic.getDay() + "/" + comic.getMonth() + "/" + comic.getYear();
                            fecha2.setText("Fecha de publicación: " + fecha);
                            String imagen = comic.getImg();

                            createComic(num, titulo, fecha, imagen);
                            addComics(num, titulo, fecha, imagen);

                            Picasso.get().load(imagen).into(imagen2);

                        }
                    }catch(Exception e) {
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



    /*
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

     */

    public void createComic(String id, String titulo, String fecha, String imagen) {
        helper = new ComicDatabaseSqLiteOpenHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("titulo", titulo);
            values.put("fecha", String.valueOf(fecha));
            values.put("id", id);
            values.put("imagen", imagen);
            database.insert("comic", null, values);
            database.close();
            Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            Toast.makeText(this, "Error en el registro en la bd", Toast.LENGTH_SHORT).show();
        }

    }

    public void addComics(String id, String titulo, String fecha, String imagen) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_TITLE, titulo);
        values.put(KEY_FECHA, fecha);
        values.put(KEY_IMAGEN, imagen);

        db.insert(TABLE_COMIC, null, values);
    }



}