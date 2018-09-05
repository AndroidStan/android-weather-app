package com.weather.android.util.retrofit;
import com.weather.android.inf.Constants;
import com.weather.android.to.WeatherTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

public interface WeatherAPI {
    String BASE_URL = Constants.WEATHER_URL;

    @GET("weather")
    Call<WeatherTO> getWeather(@Query(value="id", encoded=true) String cityId,
                               @Query(value="appid", encoded=true) String appKey);
}
