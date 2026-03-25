package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    private double speed;
    @SerializedName("deg")
    private int degree;

    public double getSpeed() { return speed; }
    public int getDegree() { return degree; }
}