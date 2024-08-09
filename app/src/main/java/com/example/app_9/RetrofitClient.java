package com.example.app_9;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String BASE_URL = "https://swapi.dev/api/";
    Retrofit retrofit;
    SWAPiService swaPiService;
    static RetrofitClient instance;

    public RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        swaPiService=retrofit.create(SWAPiService.class);
    }

    public static synchronized RetrofitClient getInstance(){
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public SWAPiService getSwaPiService(){
        return swaPiService;
    }
}
