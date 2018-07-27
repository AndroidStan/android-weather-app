package com.weather.android.to.subclasses;

import lombok.Data;

@Data
public class Sys {
	 private Integer type;
	 private Integer id;
	 private Double message;
	 private String country;
	 private Long sunrise;
	 private Long sunset;
}
