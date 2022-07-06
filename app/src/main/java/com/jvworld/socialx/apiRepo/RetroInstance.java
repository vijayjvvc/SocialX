package com.jvworld.socialx.apiRepo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroInstance {
    public static String url = "https://newsapi.org/v2/everything/";

    private static Retrofit retrofit;

    private static Retrofit getClient(){

        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiServices getUserService(){
        ApiServices apiServices = getClient().create(ApiServices.class);
        return apiServices;
    }
}
