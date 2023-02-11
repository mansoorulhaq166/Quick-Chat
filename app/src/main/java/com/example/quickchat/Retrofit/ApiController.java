package com.example.quickchat.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {

    static final String url = "http://192.168.43.231/ChatappApis/";
    private static ApiController clientObject;
    private static Retrofit retrofit;

    public ApiController() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized ApiController getInstance() {
        if (clientObject == null) {
            clientObject = new ApiController();
        }
        return clientObject;
    }

    public ApiSet getApi() {
        return retrofit.create(ApiSet.class);
    }
}