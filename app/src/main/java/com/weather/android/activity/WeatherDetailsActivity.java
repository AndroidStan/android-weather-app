package com.weather.android.activity;
import com.weather.android.R;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.weather.android.application.WeatherApplication;
import com.weather.android.inf.Constants;
import com.weather.android.to.*;
import com.weather.android.util.*;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;


public class WeatherDetailsActivity extends BaseActivity {
	private TextView 	textViewCityName,
						textViewCityTemperature,
						textViewWeather,
                        textViewLastUpdated,
                        textViewSunrise,
                        textViewSunset;
	private Button buttonBack;
	private String cityId, cityName;
	private Double latitude, longtitude;

	private void populateWeatherDetails(){
		cityName = WeatherApplication.getWeatherDetails().getName();
		this.latitude = WeatherApplication.getWeatherDetails().getCoord().getLat();
		this.longtitude = WeatherApplication.getWeatherDetails().getCoord().getLon();

	    //set the fields text
		String  formattedCityName = cityName,
                latitude = String.valueOf(this.latitude),
                longtitude = String.valueOf(this.longtitude);

        formattedCityName += " (" + latitude + ", " + longtitude + ")";

        textViewCityName.setText(formattedCityName);

        String lastUpdatedTime = SystemUtil.getformattedDate(WeatherApplication.getWeatherDetails().getDt());
        textViewLastUpdated.setText(lastUpdatedTime);

        String sunRiseTime = SystemUtil.getformattedDate(WeatherApplication.getWeatherDetails().getSys().getSunrise());
        textViewSunrise.setText(sunRiseTime);

        String sunSetTime = SystemUtil.getformattedDate(WeatherApplication.getWeatherDetails().getSys().getSunset());
        textViewSunset.setText(sunSetTime);

        TemperatureTO   cityTemp = new TemperatureTO(),
                        cityMinTemp = new TemperatureTO(),
                        cityMaxTemp = new TemperatureTO();

        Double  kelvinCityTemp = WeatherApplication.getWeatherDetails().getMain().getTemp(),
                kelvinCityMinTemp = WeatherApplication.getWeatherDetails().getMain().getTemp_min(),
                kelvinCityMaxTemp = WeatherApplication.getWeatherDetails().getMain().getTemp_max();

        cityTemp = SystemUtil.convertFromKelvinToFahrenheitAndCelcius(kelvinCityTemp);
        cityMinTemp = SystemUtil.convertFromKelvinToFahrenheitAndCelcius(kelvinCityMinTemp);
        cityMaxTemp = SystemUtil.convertFromKelvinToFahrenheitAndCelcius(kelvinCityMaxTemp);

        String formattedCityTemp =  cityTemp.getFormattedFahrenheitTemp()
                                    + " °F (min: " + cityMinTemp.getFormattedFahrenheitTemp() + ", "
                                    + "max: " + cityMaxTemp.getFormattedFahrenheitTemp() + ")/\r\n"
                                    + cityTemp.getFormattedCelciusTemp()
                                    + " °C (min: " + cityMinTemp.getFormattedCelciusTemp() + ", "
                                    + "max: " + cityMaxTemp.getFormattedCelciusTemp() + ")";

		textViewCityTemperature.setText(formattedCityTemp);

		String weatherDetails = WeatherApplication.getWeatherDetails().getWeather().get(0).getMain();
		weatherDetails += ": " + WeatherApplication.getWeatherDetails().getWeather().get(0).getDescription();

		Integer pressure = WeatherApplication.getWeatherDetails().getMain().getPressure(),
                humidity = WeatherApplication.getWeatherDetails().getMain().getHumidity(),
                visibility = WeatherApplication.getWeatherDetails().getVisibility(),
                cloudiness = WeatherApplication.getWeatherDetails().getClouds().getAll();

		Double windSpeed = WeatherApplication.getWeatherDetails().getWind().getSpeed();

        if(pressure != null)
            weatherDetails += "\r\nPressure: " + pressure.toString() + " hPa";

        if(humidity != null)
            weatherDetails += "\r\nHumidity: " + humidity.toString() + " %";

        if(visibility != null)
            weatherDetails += "\r\nVisibility: " + visibility.toString() + " meters";

        if(cloudiness != null)
            weatherDetails += "\r\nCloudiness: " + cloudiness.toString() + " %";

        if(windSpeed != null)
            weatherDetails +=  "\r\nWind Speed: " + windSpeed.toString() + " m/s";

		textViewWeather.setText(weatherDetails);
	}

	private void addFragment(String cityName, Double latitude, Double longtitude){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MapFragment mapFragment = new MapFragment();

        Bundle latLng = new Bundle();

        latLng.putString("cityName", cityName);
        latLng.putDouble("latitude", latitude);
        latLng.putDouble("longtitude", longtitude);

        mapFragment.setArguments(latLng);

        fragmentTransaction.add(R.id.fragmentContainer, mapFragment);
        fragmentTransaction.commit();
    }

    public void goBack() {
        WeatherApplication.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                // Code which runs on MAIN UI thread
                dismissProgressDialog();

                SystemUtil.sleepForMillis(600);

                finish();
            }
        });
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
       	super.onCreate(savedInstanceState);
    	setContentView(R.layout.weather_details);

        WeatherApplication.setCurrentActivity(this);

        textViewCityName = (TextView) findViewById(R.id.cityName);
        textViewCityTemperature = (TextView) findViewById(R.id.cityTemperature);
		textViewWeather = (TextView) findViewById(R.id.weather);
        textViewLastUpdated = (TextView) findViewById(R.id.lastUpdatedTimeTextView);
        textViewSunrise = (TextView) findViewById(R.id.sunRiseTextView);
        textViewSunset = (TextView) findViewById(R.id.sunSetTextView);

        buttonBack = (Button) findViewById(R.id.button_back);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            cityId = extras.getString("cityId");
        }

		if(cityId != null) {
            WeatherApplication.clearWeather();

            showSimpleProgressDialog("Gathering Weather Data", R.string.initializing);

            Call<WeatherTO> call = WeatherApplication.getWeatherApi().getWeather(cityId, Constants.OPEN_WEATHER_MAP_KEY);
            call.enqueue(new Callback<WeatherTO>() {
                @Override
                public void onResponse(Call<WeatherTO> call, Response<WeatherTO> response) {
                    WeatherApplication.setWeather(response.body());

                    populateWeatherDetails();
                    addFragment(cityName, latitude, longtitude);

                    dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<WeatherTO> call, Throwable t) {
                    WeatherApplication.setWeather(null);
                    Logger.e(Log.getStackTraceString(t));

                    if (t instanceof IOException)
                        makeLongToast("Internet connection error");

                    //goBack();

                    //500 ms delay at the following method below
                    //in order the progress to be visible
                    //even at the quickest get(s)
                    dismissProgressDialog();

                    //waiting 100 milliseconds more (500+100) the progress dialogue to be closed
                    SystemUtil.sleepForMillis(600);

                    //finish the activity after the progress dialog was closed in order not to leak
                    finish();
                }
            });
        }
    }
}