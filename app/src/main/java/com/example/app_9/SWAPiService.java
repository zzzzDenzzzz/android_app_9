package com.example.app_9;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SWAPiService {

    @GET("people/{id}")
    Call<Person> getPerson(@Path("id") String id);

    @GET("people/")
    Call<PersonResponse> getPeople();

    @GET("films/{id}")
    Call<Film> getFilms(@Path("id") String id);

    @GET("films/")
    Call<FilmResponse> getFilms();
}
