package com.weather.android.activity;

import com.weather.android.R;
import com.weather.android.adapter.HomeAdapter;
import com.weather.android.application.WeatherApplication;
import com.weather.android.inf.Constants;
import com.weather.android.to.ErrorMessageTO;
import com.weather.android.util.DataGatherUtil;
import com.weather.android.util.Logger;
import com.weather.android.util.room.CityDetails;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;

public class HomeActivity extends BaseActivity 
{
	private ListView listView;
    private Button addZipButton;
    private AutoCompleteTextView autoCompleteTextView;
    private LinearLayout zipCodeLinearLayout;
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

                List<Integer> citiesZips = DataGatherUtil.getAllCitiesZips(getApplicationContext());
                WeatherApplication.setSuggestedCitiesZips(citiesZips);
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

                ArrayAdapter<Integer> adapter = new ArrayAdapter<>( getApplicationContext(),
                                                                    R.layout.single_line_dropdown,
                                                                    R.id.dropdownLine,
                                                                    WeatherApplication.getSuggestedCitiesZips());

                autoCompleteTextView.setAdapter(adapter);
			}
		}
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.enterZipCode);
        zipCodeLinearLayout = (LinearLayout) findViewById(R.id.zipCodeLinearLayout);;
        listView = (ListView) findViewById(R.id.list);
        addZipButton = (Button) findViewById(R.id.button_add_zip);

        listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				String cityId = WeatherApplication.getCitiesDetails().get(position).getId().toString();

				Intent myIntent = new Intent(getApplicationContext(), WeatherDetailsActivity.class);
				myIntent.putExtra("cityId", cityId);
				startActivity(myIntent);
			}

        });

        //remove a particular list element on a long item click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {

                WeatherApplication.getCitiesDetails().remove(pos);
                homeAdapter.notifyDataSetChanged();
                return true;
			}
		});

        addZipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                zipCodeLinearLayout.setVisibility(View.VISIBLE);
                autoCompleteTextView.setFocusableInTouchMode(true);
                autoCompleteTextView.requestFocus();

                //show the keyboard
                /*InputMethodManager imm = (InputMethodManager) HomeActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);*/

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        });

		new AsyncParseTask().execute(Constants.CITIES_INITIAL_ENTRIES_NUM);
	}
}