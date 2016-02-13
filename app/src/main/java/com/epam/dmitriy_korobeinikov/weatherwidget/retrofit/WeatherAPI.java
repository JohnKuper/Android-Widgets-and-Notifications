package com.epam.dmitriy_korobeinikov.weatherwidget.retrofit;

import com.epam.dmitriy_korobeinikov.weatherwidget.model.CurrentWeather;
import com.epam.dmitriy_korobeinikov.weatherwidget.model.Forecast;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Dmitriy_Korobeinikov on 11/27/2015.
 */
public interface WeatherAPI {

    @GET("data/2.5/weather?units=metric")
    Call<CurrentWeather> getCurrentWeather(@Query("id") String cityId, @Query("APPID") String apiKey);

    @GET("data/2.5/forecast/daily?units=metric")
    Call<Forecast> getForecast(@Query("id") String cityId, @Query("cnt") String dayCount, @Query("APPID") String apiKey);
}
