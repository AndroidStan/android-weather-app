package com.weather.android.to.subclasses;

import lombok.Data;

@Data
public class Sys {
	 private float type;
	 private float id;
	 private float message;
	 private String country;
	 private float sunrise;
	 private float sunset;
}
