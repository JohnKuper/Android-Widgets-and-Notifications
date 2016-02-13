package com.epam.dmitriy_korobeinikov.weatherwidget.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dmitriy_Korobeinikov on 12/1/2015.
 */
public class City {
    @JsonProperty("_id")
    public String id;
    public String name;
}
