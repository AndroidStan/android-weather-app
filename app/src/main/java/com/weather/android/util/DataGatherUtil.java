package com.weather.android.util;

import android.content.Context;

import com.weather.android.application.WeatherApplication;
import com.weather.android.inf.Constants;
import com.weather.android.parser.*;
import com.weather.android.to.*;
import com.weather.android.util.room.CityDetails;

import java.util.ArrayList;
import java.util.List;

public class DataGatherUtil
{
	public static WeatherTO getWeather(String cityId){
		String getWeatherUrl = Constants.WEATHER_URL
								+ cityId
								+ "&appid="
								+ Constants.OPEN_WEATHER_MAP_KEY;

		String response = HTTPUtil.getServerResponse(getWeatherUrl);

		WeatherTO weatherDetails = DataParser.parseWeatherDetails(response);

		return weatherDetails;
	}

	public static List<CityDetails> getFirstNCities(Context context, Integer nCities){
		return WeatherApplication.getUsaCitiesDatabase(context, Constants.SQLITE_DB_NAME).cityDetailsDao().getFirstN(nCities);
	}

	public static List<Integer> getAllCitiesZips(Context context){
		return WeatherApplication.getUsaCitiesDatabase(context, Constants.SQLITE_DB_NAME).cityDetailsDao().getAllZipCodes();
	}

	public static List<CityDetails> getCitiesDetailsBySameZipCode(Context context, Integer zipcode){
		return WeatherApplication.getUsaCitiesDatabase(context, Constants.SQLITE_DB_NAME).cityDetailsDao().getCitiesDetailsBySameZipCode(zipcode);
	}
}