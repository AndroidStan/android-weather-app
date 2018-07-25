package com.weather.android.to;

import java.util.ArrayList;

import com.weather.android.to.subclasses.Coord;
import com.weather.android.to.subclasses.Main;
import com.weather.android.to.subclasses.Sys;
import com.weather.android.to.subclasses.Wind;
import com.weather.android.to.subclasses.Clouds;

import lombok.Data;

@Data
public class WeatherTO 
{
	 Coord coord;
	 ArrayList < Object > weather = new ArrayList < Object > ();
	 private String base;
	 Main main;
	 private float visibility;
	 Wind wind;
     Clouds clouds;
	 private float dt;
	 Sys sys;
	 private float id;
	 private String name;
	 private float cod;

	public void clearWeatherDetails()
	{
		if(weather != null)
			weather.clear();
	}

	public void setWeather(ArrayList <Object> weatherDetails)
	{
		clearWeatherDetails();
		weather = weatherDetails;
	}

}