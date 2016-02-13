package com.epam.dmitriy_korobeinikov.weatherwidget.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Dmitriy_Korobeinikov on 12/3/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {
    @JsonProperty("list")
    public List<CurrentWeather> dailyForecast;
}
