package com.epam.dmitriy_korobeinikov.weatherwidget.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Dmitriy_Korobeinikov on 11/27/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeather {
    @JsonProperty("weather")
    private List<Weather> weatherDescription;
    @JsonProperty("main")
    public MainData mainData;
    public Wind wind;
    @JsonProperty("dt")
    public long lastUpdateTime;
    @JsonProperty("id")
    public String cityId;
    @JsonProperty("name")
    public String cityName;
    @JsonProperty("temp")
    public Temperature temperature;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MainData {
        public String temp;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        public String speed;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Temperature {
        public double min;
        public double max;
    }

    public Weather getWeatherDescription() {
        return weatherDescription.get(0);
    }
}
