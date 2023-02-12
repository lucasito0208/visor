package com.example.apirest_retrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {

    TextView titulo, fecha;
    ImageView imagen;
    Button siguiente, anterior, atras;

    public ComicDatabaseSqLiteOpenHelper helper;

    //MainThreadExecutor mainThreadExecutor = new MainThreadExecutor();
    ExecutorService diskIOExecutor = Executors.newSingleThreadExecutor();

    ComicApi comicApi;
    Comic comic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent i = getIntent();

        titulo = findViewById(R.id.tvTitulo);
        fecha = findViewById(R.id.tvFecha);
        imagen = findViewById(R.id.imgComic);

        siguiente = findViewById(R.id.btnSiguiente);
        anterior = findViewById(R.id.btnAnterior);
        atras = findViewById(R.id.btnVisorAtras);

        String title = i.getStringExtra("titulo");
        String date = i.getStringExtra("fecha");
        String image = i.getStringExtra("imagen");

        titulo.setText(title);
        fecha.setText("Fecha de publicación: "+date);
        Picasso.get().load(image).into(imagen);

        siguiente.setOnClickListener(view -> {
            String num = comic.getNum();
            System.out.println(Integer.parseInt(num) + 1);
            //sig(num + 1);

        });

        anterior.setOnClickListener(view -> {

        });

        atras.setOnClickListener(view -> {
            Intent ii = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(ii);
        });
    }

    public void sig(String num) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        comicApi = retrofit.create(ComicApi.class);
        Call<Comic> request = comicApi.find(num);

        request.enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(@NonNull Call<Comic> call, @NonNull Response<Comic> response) {
                try{
                    if(response.isSuccessful()) {

                        Comic comic = response.body();

                        String num = comic.getNum();
                        String tituloApi = comic.getTitle();
                        String fechaApi = comic.getDay() + "/" + comic.getMonth() + "/" + comic.getYear();
                        String imagenApi = comic.getImg();

                        createComic(Integer.parseInt(num), tituloApi, fechaApi);
                        addComics(num, tituloApi, fechaApi);

                        titulo.setText(tituloApi);
                        fecha.setText("Fecha de publicación: "+fechaApi);
                        Picasso.get().load(imagenApi).into(imagen);

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

    public void createComic(int id, String titulo, String fecha) {
        helper = new ComicDatabaseSqLiteOpenHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("titulo", titulo);
            values.put("fecha", String.valueOf(fecha));
            values.put("identificador", id);
            database.insert("comic", null, values);
            database.close();
            Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            Toast.makeText(this, "Error en el registro en la bd", Toast.LENGTH_SHORT).show();
        }

    }

    public void addComics(String id, String titulo, String fecha) {
        SQLiteDatabase bd = helper.getWritableDatabase();
        if(bd!=null) {
            String sqlInsert = "insert into comic values('"+id+"', '"+titulo+"', '"+fecha+"')";
            bd.execSQL(sqlInsert);
            bd.close();
        }
    }


}