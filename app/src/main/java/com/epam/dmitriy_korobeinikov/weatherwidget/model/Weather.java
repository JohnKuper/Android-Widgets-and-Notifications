package com.epam.dmitriy_korobeinikov.weatherwidget.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dmitriy_Korobeinikov on 11/27/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    @JsonProperty("main")
    public String weatherDescription;
    @JsonProperty("icon")
    public String iconCode;
}
