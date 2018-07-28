package com.weather.android.activity;
import com.weather.android.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.AsyncTask;

import com.weather.android.application.WeatherApplication;
import com.weather.android.to.*;
import com.weather.android.util.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class WeatherDetailsActivity extends BaseActivity {
	private TextView 	textViewCityName,
						textViewCityTemperature,
						textViewWeather,
                        textViewLastUpdated,
                        textViewSunrise,
                        textViewSunset;
	private Button buttonBack;
	
	private class AsyncParseTask extends AsyncTask <String,Integer,ErrorMessageTO> {
		AsyncParseTask() {
		}
		
		protected void onPreExecute() {
			showProgressDialog(	getText(R.string.progress_title).toString(),
								R.string.progress_text,
								this,
								true);
		}
		
		protected ErrorMessageTO doInBackground(String... urlParams) {
			ErrorMessageTO errorMessage = null;
			String 	cityId= urlParams[0];
			
			try {
				WeatherApplication.clearWeather();

				WeatherTO weatherDetails = DataGatherUtil.getWeather(cityId);
				WeatherApplication.setWeather(weatherDetails);
			}
			catch(Exception e) {
				errorMessage = new ErrorMessageTO(e,getText(R.string.server_data_failure).toString());
				Logger.e(Log.getStackTraceString(e));
			}
		
			return errorMessage;
		}
		
		protected void onProgressUpdate(Integer... progress) {
		}
		 
		protected void onPostExecute(ErrorMessageTO errorMessage) {
			dismissDialog();	         

			if(errorMessage != null)
				createDialogAndHandleTechnicalExceptionAndFinishActivity(errorMessage);
			else {
				if(WeatherApplication.getWeatherDetails() == null) {
					showDialogAndFinishActivity(getText(R.string.no_weather_title).toString(),getText(R.string.no_results).toString());
					return;
				}

				populateWeatherDetails();
			}
		} 
	}

	private void populateWeatherDetails(){
		//set the fields text
		String  formattedCityName = WeatherApplication.getWeatherDetails().getName(),
                latitude = String.valueOf(WeatherApplication.getWeatherDetails().getCoord().getLat()),
                longtitude = String.valueOf(WeatherApplication.getWeatherDetails().getCoord().getLon());

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

        weatherDetails += "\r\nPressure: " + pressure.toString() + " hPa"
                        + "\r\nHumidity: " + humidity.toString() + " %"
                        + "\r\nVisibility: " + visibility.toString() + " meters"
                        + "\r\nCloudiness: " + cloudiness.toString() + " %"
                        + "\r\nWind Speed: " + windSpeed.toString() + " m/s";

		textViewWeather.setText(weatherDetails);
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
       	super.onCreate(savedInstanceState);
    	setContentView(R.layout.weather_details);

        textViewCityName = (TextView) findViewById(R.id.cityName);
        textViewCityTemperature = (TextView) findViewById(R.id.cityTemperature);
		textViewWeather = (TextView) findViewById(R.id.weather);
        textViewLastUpdated = (TextView) findViewById(R.id.lastUpdatedTimeTextView);
        textViewSunrise = (TextView) findViewById(R.id.sunRiseTextView);
        textViewSunset = (TextView) findViewById(R.id.sunSetTextView);

        buttonBack = (Button) findViewById(R.id.button_back);

        buttonBack.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String cityId = extras.getString("cityId");
            new AsyncParseTask().execute(cityId);
        }
    }
}