package com.example.apirest_retrofit.models;

import android.text.Editable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ComicApi {


    @GET("{idComic}/info.0.json")
    Call<Comic> find(@Path("idComic") String idComic);


}
