package com.ftninformatika.termin27_webservices.Net.WebServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RESTService {
    public static final String BASE_URL = "https://www.omdbapi.com";
    public static final String API_KEY = "d3824e34";

    public static Retrofit getRetrofitInstance(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static OMDBApiEndpointInterface apiInterface(){
        OMDBApiEndpointInterface apiService = getRetrofitInstance().create(OMDBApiEndpointInterface.class);

        return apiService;
    }
}
