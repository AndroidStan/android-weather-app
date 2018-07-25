package com.weather.android.to.subclasses;

import lombok.Data;

@Data
public class WeatherFields {
    private Integer id;
    private String main;
    private String description;
    private String icon;
}
