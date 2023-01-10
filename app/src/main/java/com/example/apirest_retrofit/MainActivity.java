package com.example.apirest_retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apirest_retrofit.models.Comic;
import com.example.apirest_retrofit.models.ComicApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button buscar;
    EditText edtNum;

    ComicApi comicApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buscar = findViewById(R.id.btnBuscar);
        edtNum = findViewById(R.id.etNum);

        buscar.setOnClickListener(view -> {
            find(Integer.parseInt(edtNum.getText().toString()));
        });
    }

    //public String getImagen()

    private void find(int cod) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        comicApi = retrofit.create(ComicApi.class);
        Call<Comic> request = comicApi.getComic(cod);

        request.enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                try{
                    if(response.isSuccessful()) {
                        Comic comic = response.body();
                        String Url_img = "https://imgs.xkcd.com/comics/" + comic.getImg()+ ".jpg";
                        Intent i = new Intent(MainActivity.this, MainActivity2.class);

                        String titulo = comic.getTitle();
                        String fecha = comic.getDay();
                        String imagen = comic.getImg();

                        i.putExtra(titulo, "titu");
                        i.putExtra(fecha, "fech");
                        i.putExtra(imagen, "ima");

                        startActivity(i);


                    }
                }catch(Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comic> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error en la conexión!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}