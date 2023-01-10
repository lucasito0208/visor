package com.example.apirest_retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apirest_retrofit.models.Comic;
import com.example.apirest_retrofit.models.ComicApi;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity2 extends AppCompatActivity {

    TextView titulo, fecha;
    ImageView imagen;

    //MainThreadExecutor mainThreadExecutor = new MainThreadExecutor();
    ExecutorService diskIOExecutor = Executors.newSingleThreadExecutor();

    ComicApi comicApi;
    Comic comic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        titulo = findViewById(R.id.tvTitulo);
        fecha = findViewById(R.id.tvFecha);
        imagen = findViewById(R.id.imgComic);

        Intent i = getIntent();

        String title = i.getStringExtra("titu");
        String date = i.getStringExtra("fech");
        String image = i.getStringExtra("ima");
        String url_img = "https://imgs.xkcd.com/comics/"+comic.getImg()+".jpg";
        Picasso.get().load(url_img).into(imagen);

        titulo.setText(title);
        fecha.setText(date);



    }
}