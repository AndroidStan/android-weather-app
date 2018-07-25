package com.weather.android.to;

import java.util.ArrayList;

import com.weather.android.to.subclasses.Coord;
import com.weather.android.to.subclasses.Main;
import com.weather.android.to.subclasses.Sys;
import com.weather.android.to.subclasses.WeatherFields;
import com.weather.android.to.subclasses.Wind;
import com.weather.android.to.subclasses.Clouds;

import lombok.Data;

@Data
public class WeatherTO 
{
	 Coord coord;
	 ArrayList <WeatherFields> weather = new ArrayList <> ();
	 private String base;
	 Main main;
	 private Integer visibility;
	 Wind wind;
     Clouds clouds;
	 private Integer dt;
	 Sys sys;
	 private Integer id;
	 private String name;
	 private Integer cod;

	public void clearWeatherDetails()
	{
		if(weather != null)
			weather.clear();
	}

	public void setWeather(ArrayList<WeatherFields> weatherFields)
	{
		clearWeatherDetails();
		weather = weatherFields;
	}

}