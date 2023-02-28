package com.example.apirest_retrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apirest_retrofit.models.Comic;
import com.example.apirest_retrofit.models.ComicApi;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button buscar, listar;
    EditText edtNum;
    ComicApi comicApi;

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "titulo";
    public static final String KEY_FECHA = "fecha";
    public static final String KEY_IMAGEN = "imagen";
    public static final String TABLE_COMIC = "comic";

    Cursor cursor;


    public ComicDatabaseSqLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buscar = findViewById(R.id.btnBuscar);
        listar = findViewById(R.id.btnListar);
        edtNum = findViewById(R.id.etNum);

        buscar.setOnClickListener(view -> {
            //helper = new ComicDatabaseSqLiteOpenHelper(this);
            find(edtNum.getText().toString());
            //System.out.println(helper.consultarListaComics(edtNum.getText().toString()));
        });

        listar.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, MainActivity3.class);
            startActivity(i);
        });
    }

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

    public void find(String cod) {
        helper = new ComicDatabaseSqLiteOpenHelper(this);
        if(helper.consultarListaComics(edtNum.getText().toString())) {
            cursor = new ComicDatabaseSqLiteOpenHelper(this).dameComic(cod);
            while(cursor.moveToNext()) {

                //cursor.getString();
                //cursor == helper.dameComic(cod)
                Intent ii = new Intent(MainActivity.this, MainActivity2.class);
                String titulo = cursor.getString(1);
                String fecha = cursor.getString(2);
                String imagen = cursor.getString(3);

                ii.putExtra("titulo", titulo);
                ii.putExtra("fecha", fecha);
                ii.putExtra("imagen", imagen);
                System.out.println("Se encuentra en la base de datos");
                //cursor.close();
                startActivity(ii);
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
                            Intent i = new Intent(MainActivity.this, MainActivity2.class);
                            Comic comic = response.body();

                            assert comic != null;
                            String num = comic.getNum();
                            String titulo = comic.getTitle();
                            String fecha = comic.getDay();
                            String imagen = comic.getImg();
                            System.out.println("ENTRA AQUÍ");
                            //Interacción con la base de datos
                            createComic(num, titulo, fecha, imagen);
                            System.out.println("CREA COMIC!!!");
                            addComics(num, titulo, fecha, imagen);
                            System.out.println("AÑADE COMIC");
                            //dropComic();
                            i.putExtra("titulo", titulo);
                            i.putExtra("fecha", fecha);
                            i.putExtra("imagen", imagen);
                            i.putExtra("num", num);
                            System.out.println("NO SE ENCUENTRA EN BASE DE DATOS");
                            startActivity(i);

                        }
                    }catch(Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Comic> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, "Error en la conexión!", Toast.LENGTH_SHORT).show();
                }
            });
        }



        }
}