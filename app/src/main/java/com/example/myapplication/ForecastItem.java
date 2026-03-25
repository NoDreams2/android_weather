package com.example.myapplication;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastItem {
    @SerializedName("dt_txt")
    private String dateTime;

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<Weather> weather;

    public String getDateTime() {
        return dateTime;
    }

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }
}