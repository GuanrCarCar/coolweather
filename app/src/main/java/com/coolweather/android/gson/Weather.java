package com.coolweather.android.gson;

import android.view.textservice.SuggestionsInfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
         public String status;
    public Basic basic;

    public updata updata;
    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Daily_Forecast> forecastList;




}
