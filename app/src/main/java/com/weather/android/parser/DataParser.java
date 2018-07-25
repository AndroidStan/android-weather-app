package com.weather.android.parser;

import android.util.Log;
import com.weather.android.application.WeatherApplication;
import com.weather.android.inf.Constants;
import com.weather.android.to.*;
import com.weather.android.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataParser
{
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static WeatherTO parseWeatherDetails(String response){
		WeatherTO weatherDetails = new WeatherTO();

		try{
			weatherDetails = objectMapper.readValue(response, WeatherTO.class);
		} catch (IOException e){
			Logger.e(Log.getStackTraceString(e));
        }

        return weatherDetails;
	}
}