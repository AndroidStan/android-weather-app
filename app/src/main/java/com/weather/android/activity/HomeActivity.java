package com.weather.android.activity;

import com.weather.android.R;
import com.weather.android.adapter.HomeAdapter;
import com.weather.android.application.WeatherApplication;
import com.weather.android.inf.Constants;
import com.weather.android.to.ErrorMessageTO;
import com.weather.android.to.WeatherTO;
import com.weather.android.util.DataGatherUtil;
import com.weather.android.util.HTTPUtil;
import com.weather.android.util.Logger;
import com.weather.android.util.room.CityDetails;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import java.sql.SQLException;
import java.util.List;


public class HomeActivity extends BaseActivity 
{
	private ListView listView;
	private HomeAdapter homeAdapter;

	private class AsyncParseTask extends AsyncTask <String,Integer,ErrorMessageTO> {

		AsyncParseTask()
		{
		}

		protected void onPreExecute()
		{
		}

		protected ErrorMessageTO doInBackground(String... urlParams)
		{
			ErrorMessageTO errorMessage = null;
			String 	maxEntriesNum = urlParams[0];

			try {

				WeatherApplication.clearCitiesDetails();
                List<CityDetails> citiesDetails = DataGatherUtil.getFirstNCities(getApplicationContext(), Integer.valueOf(maxEntriesNum));
                WeatherApplication.setCitiesDetails(citiesDetails);
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
				if(WeatherApplication.getCitiesDetails().isEmpty())
				{
					showDialogAndFinishActivity(getText(R.string.no_weather_db_title).toString(),getText(R.string.no_db_results).toString());
					return;
				}

				//set the homeAdapter
				homeAdapter = new HomeAdapter(getApplicationContext(), WeatherApplication.getCitiesDetails());
				listView.setAdapter(homeAdapter);
			}
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        listView = (ListView) findViewById(R.id.list);

        listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				String cityId = WeatherApplication.getCitiesDetails().get(position).getId().toString();

				Intent myIntent = new Intent(getApplicationContext(), WeatherDetailsActivity.class);
				myIntent.putExtra("cityId", cityId);
				startActivity(myIntent);
			}

        });

            new AsyncParseTask().execute(Constants.CITIES_INITIAL_ENTRIES_NUM);
	}
}