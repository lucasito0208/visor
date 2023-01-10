package com.example.apirest_retrofit.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ComicApi {

    @GET("{idComic}/info.0.json")
    Call<Comic> getComic(@Path("idComic") int idComic);
}
