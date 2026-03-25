package com.example.myapplication;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastResponse {
    @SerializedName("list")
    private List<ForecastItem> list;

    public List<ForecastItem> getList() {
        return list;
    }
}