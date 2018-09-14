package com.weather.android.to;

import java.util.ArrayList;

import com.weather.android.to.subclasses.Coord;
import com.weather.android.to.subclasses.Main;
import com.weather.android.to.subclasses.Rain;
import com.weather.android.to.subclasses.Sys;
import com.weather.android.to.subclasses.WeatherFields;
import com.weather.android.to.subclasses.Wind;
import com.weather.android.to.subclasses.Clouds;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class WeatherTO 
{
	private Coord coord;
	private ArrayList <WeatherFields> weather = new ArrayList <> ();
	private String base;
	private Main main;
	private Integer visibility;
	private Wind wind;
	private Rain rain;
	private Clouds clouds;
	private Long dt;

	private Sys sys;
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