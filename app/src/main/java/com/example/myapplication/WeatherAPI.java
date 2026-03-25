package com.example.myapplication;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherAPI {
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String API_KEY = "c97a4cde1cb78418a0f27cf1cd17516e";

    public interface WeatherService {
        @GET("weather")
        Call<WeatherResponse> getCurrentWeather(
                @Query("q") String city,
                @Query("units") String units,
                @Query("lang") String lang,
                @Query("appid") String appid
        );

        @GET("forecast")
        Call<ForecastResponse> getForecast(
                @Query("q") String city,
                @Query("units") String units,
                @Query("lang") String lang,
                @Query("appid") String appid
        );
    }

    public static WeatherService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(WeatherService.class);
    }
}