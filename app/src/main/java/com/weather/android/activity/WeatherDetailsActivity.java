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

public class WeatherDetailsActivity extends BaseActivity {
	private TextView textViewCityName, textViewCityTemperature;
	private Button buttonBack;
	
	private class AsyncParseTask extends AsyncTask <String,Integer,ErrorMessageTO> 
	{	
		AsyncParseTask()
		{
		}
		
		protected void onPreExecute()
		{
			showProgressDialog(	getText(R.string.progress_title).toString(),
								R.string.progress_text,
								this,
								true);
		}
		
		protected ErrorMessageTO doInBackground(String... urlParams) 
		{
			ErrorMessageTO errorMessage = null;
			String 	cityId= urlParams[0];
			
			try
			{
				WeatherApplication.clearWeather();

				WeatherTO weatherDetails = DataGatherUtil.getWeather(cityId);
				WeatherApplication.setWeather(weatherDetails);
			}
			catch(Exception e)
			{
				errorMessage = new ErrorMessageTO(e,getText(R.string.server_data_failure).toString());
				Logger.e(Log.getStackTraceString(e));
			}
		
			return errorMessage;
		}
		
		protected void onProgressUpdate(Integer... progress) 
		{ 
		}
		 
		protected void onPostExecute(ErrorMessageTO errorMessage)
		{
			dismissDialog();	         

			if(errorMessage != null)
				createDialogAndHandleTechnicalExceptionAndFinishActivity(errorMessage);
			else
			{
				if(WeatherApplication.getWeather() == null)
				{
					showDialogAndFinishActivity(getText(R.string.no_weather_title).toString(),getText(R.string.no_results).toString());
					return;
				}

				populateWeatherDetails();
			}
		} 
	}


	private void populateWeatherDetails(){
		//set the fields text
		textViewCityName.setText(WeatherApplication.getWeather().getName());

		String stringCityTemperature = String.valueOf(WeatherApplication.getWeather().getMain().getTemp()) + " °F";
		textViewCityTemperature.setText(stringCityTemperature);
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {   
       	super.onCreate(savedInstanceState);
    	setContentView(R.layout.weather_details);

        textViewCityName = (TextView) findViewById(R.id.cityName);
        textViewCityTemperature = (TextView) findViewById(R.id.cityTemperature);

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