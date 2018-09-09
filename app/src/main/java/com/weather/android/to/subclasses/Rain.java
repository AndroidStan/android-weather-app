package com.weather.android.to.subclasses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rain {

    @JsonProperty("1h")
    private Integer rainVolumeLastHour;

    //@SerializedName("3h") - GSON
    @JsonProperty("3h")
    private Integer rainVolumeLast3Hours;
}
