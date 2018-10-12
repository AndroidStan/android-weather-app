package com.weather.android.activity;

import com.weather.android.R;
import com.weather.android.adapter.HomeAdapter;
import com.weather.android.application.WeatherApplication;
import com.weather.android.inf.Constants;
import com.weather.android.to.ErrorMessageTO;
import com.weather.android.util.RoomUtil;
import com.weather.android.util.Logger;
import com.weather.android.util.SystemUtil;
import com.weather.android.util.room.CityDetails;

import android.content.DialogInterface;
import com.weather.android.util.recyclerView.DividerItemDecoration;

import android.support.constraint.ConstraintSet;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.support.constraint.ConstraintLayout;

import java.util.List;

import static android.support.constraint.solver.widgets.ConstraintWidget.CHAIN_PACKED;

public class HomeActivity extends BaseActivity 
{
	private RecyclerView recyclerView;
    private Button addZipButton;
    private AutoCompleteTextView autoCompleteTextView;
    //private LinearLayout zipCodeLinearLayout;
    private TextView zipCodeLabel;
    //private AutoCompleteTextView zipCodeAutoComplete;
    private ArrayAdapter<Integer> autoCompleteAdapter;

	private class AsyncParseTask extends AsyncTask <String,Integer,ErrorMessageTO> {

		AsyncParseTask() {
		}

		protected void onPreExecute() {
		}

		protected ErrorMessageTO doInBackground(String... urlParams) {

			ErrorMessageTO errorMessage = null;
			String 	asyncParamValue = urlParams[0],
                    operationType = urlParams[1];

			try {

                if(operationType.equals(Constants.GATHER_INITIAL_DATA)){
                    List<CityDetails> citiesDetails = RoomUtil.getFirstNCities(   getApplicationContext(),
                                                                                        Integer.valueOf(asyncParamValue));
                    WeatherApplication.setCitiesDetails(citiesDetails);

                    //getting all of the database zipcodes
                    List<Integer> citiesZips = RoomUtil.getAllCitiesZips(getApplicationContext());
                    //removing the zip codes which have at least one associated city already pre-populated at the main list
                    for(int i=0; i < citiesDetails.size(); i++){
                        citiesZips.remove(citiesDetails.get(i).getZipcode());
                    }

                    WeatherApplication.setSuggestedCitiesZips(citiesZips);

                    errorMessage = new ErrorMessageTO(null, Constants.GATHER_INITIAL_DATA);
                }

                if(operationType.equals(Constants.GET_CITIES_IDS_BY_SAME_ZIP)){
                    WeatherApplication.clearCitiesIdsBySameZipCode();

                    List<CityDetails> citiesDetails = RoomUtil.getCitiesDetailsBySameZipCode( getApplicationContext(),
                                                                                                Integer.valueOf(asyncParamValue));

                    WeatherApplication.setCitiesDetailsBySameZipCode(citiesDetails);

                    errorMessage = new ErrorMessageTO(null, Constants.GET_CITIES_IDS_BY_SAME_ZIP);
                }
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
			dismissProgressDialog();

			//if there is an exception
			if(errorMessage.getError() != null)
				createDialogAndHandleTechnicalExceptionAndFinishActivity(errorMessage);
			else {

			    if(errorMessage.getDisplayableMessage().equals(Constants.GATHER_INITIAL_DATA)) {

                    if (WeatherApplication.getCitiesDetails().isEmpty() ||
                            WeatherApplication.getSuggestedCitiesZips().isEmpty()) {

                        showDialogAndFinishActivity(getText(R.string.no_weather_db_title).toString(),
                                getText(R.string.no_db_results).toString());
                        return;
                    }

                    setHomeAndAutoCompleteAdapters();
                }

                if(errorMessage.getDisplayableMessage().equals(Constants.GET_CITIES_IDS_BY_SAME_ZIP)){
                    Integer zipCodeToRemove = WeatherApplication.getCitiesDetailsBySameZipCode().get(0).getZipcode();

                    WeatherApplication.getSuggestedCitiesZips().remove(zipCodeToRemove);

                    autoCompleteAdapter.remove(zipCodeToRemove);
                    autoCompleteTextView.setAdapter(autoCompleteAdapter);
                    //autoCompleteAdapter.notifyDataSetChanged();

                    //add all of the cities with the same zipcode to the home list
                    Integer numCitiesSameZipCode = WeatherApplication.getCitiesDetailsBySameZipCode().size();
                    for(int i=0; i < numCitiesSameZipCode; i++)
                        WeatherApplication.getCitiesDetails().add(WeatherApplication.getCitiesDetailsBySameZipCode().get(i));

                    recyclerView.getAdapter().notifyDataSetChanged();

                    String cityIdOfTheFirstCityWithThisZipCode = WeatherApplication.getCitiesDetailsBySameZipCode().get(0).getId().toString();

                    Intent myIntent = new Intent(getApplicationContext(), WeatherDetailsActivity.class);
                    myIntent.putExtra("cityId", cityIdOfTheFirstCityWithThisZipCode);
                    startActivity(myIntent);
                }

			}
		}
	}

	private void setHomeAndAutoCompleteAdapters(){

	    recyclerView.setAdapter(new HomeAdapter(WeatherApplication.getCitiesDetails(), new HomeAdapter.OnItemClickListener() {
            @Override public void onItemClick(CityDetails item) {
                String cityId = item.getId().toString();

                Intent myIntent = new Intent(getApplicationContext(), WeatherDetailsActivity.class);
                myIntent.putExtra("cityId", cityId);
                startActivity(myIntent);
            }

            @Override public void onItemLongClick(CityDetails item){
                showDialog( null,
                        getText(R.string.deleteCityWarningMessage).toString(),
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteCityListItem(item);
                            }
                        },
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        },
                        null);
            }

        }));

	    autoCompleteAdapter = new ArrayAdapter<>(   getApplicationContext(),
                                                    R.layout.single_line_dropdown,
                                                    R.id.dropdownLine,
                                                    WeatherApplication.getSuggestedCitiesZips());

        autoCompleteTextView.setAdapter(autoCompleteAdapter);
    }

    private void deleteCityListItem(CityDetails cityDetailsToRemove){
        Integer numOfCitiesSameZip = 0,
                numOfCitiesInTheList = WeatherApplication.getCitiesDetails().size();

        for(int i=0; i < numOfCitiesInTheList; i++)
            if( WeatherApplication.getCitiesDetails().get(i).getZipcode().equals(cityDetailsToRemove.getZipcode()) )
                numOfCitiesSameZip++;

        //add the zip code at the autoComplete if just the last city with this particular zip code is about to be removed
        if(numOfCitiesSameZip == 1){
            WeatherApplication.getSuggestedCitiesZips().add(cityDetailsToRemove.getZipcode());

            autoCompleteAdapter.add(cityDetailsToRemove.getZipcode());
            autoCompleteTextView.setAdapter(autoCompleteAdapter);
        }

        //remove an element from the main list each time we perform a long click
        WeatherApplication.getCitiesDetails().remove(cityDetailsToRemove);

        recyclerView.getAdapter().notifyDataSetChanged();
    }

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        WeatherApplication.setCurrentActivity(this);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.recycler_divider));

        //zipCodeLinearLayout = (LinearLayout) findViewById(R.id.zipCodeLinearLayout);

        //zipCodeLabel = (TextView) findViewById(R.id.zipCodeLabel);
        //zipCodeAutoComplete = (AutoCompleteTextView) findViewById(R.id.enterZipCode);

        /*<TextView
        android:id="@+id/zipCodeLabel"
        style="@style/Medium.Bold"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="15dp"
        android:text="@string/zipCodeLabelText"
        app:layout_constraintVertical_chainStyle="packed"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintBottom_toTopOf="@+id/list"
        app:layout_constraintTop_toBottomOf="@+id/headerLayout" />

    <AutoCompleteTextView
        android:id="@+id/enterZipCode"
        style="@style/Medium"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="18dp"
        android:layout_marginEnd="16dp"
        android:layout_marginRight="16dp"
        android:completionThreshold="1"
        android:inputType="numberDecimal"
        android:maxLength="5"
        app:layout_constraintVertical_chainStyle="packed"
        app:layout_constraintBottom_toTopOf="@+id/list"
        app:layout_constraintStart_toEndOf="@+id/zipCodeLabel"
        app:layout_constraintTop_toBottomOf="@+id/headerLayout" />
        */

        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.constraintLayout);

        ConstraintLayout.LayoutParams constraintParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        ConstraintSet constraintSet = new ConstraintSet();

        zipCodeLabel = new TextView(this);
        zipCodeLabel.setId(R.id.zipCodeLabel);
        zipCodeLabel.setTextAppearance(R.style.Medium_Bold);
        zipCodeLabel.setText(R.string.zipCodeLabelText);
        zipCodeLabel.setWidth(1000);
        zipCodeLabel.setHeight(60);
        zipCodeLabel.setVisibility(View.INVISIBLE);

        constraintParams.verticalChainStyle = CHAIN_PACKED;
        //constraintParams.topToBottom = R.id.headerLayout;
        //constraintParams.bottomToTop = R.id.list;
        //constraintParams.startToStart = R.id.list;
        //constraintParams.topMargin = 50;
        //constraintParams.bottomMargin = 50;

        zipCodeLabel.setLayoutParams(constraintParams);

        constraintLayout.addView(zipCodeLabel,1);
        constraintSet.clone(constraintLayout);
        constraintSet.connect(zipCodeLabel.getId(), ConstraintSet.LEFT, R.id.list, ConstraintSet.LEFT, 0);
        constraintSet.connect(zipCodeLabel.getId(), ConstraintSet.RIGHT, R.id.enterZipCode, ConstraintSet.LEFT, 0);
        constraintSet.connect(zipCodeLabel.getId(), ConstraintSet.TOP, R.id.headerLayout, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(zipCodeLabel.getId(), ConstraintSet.BOTTOM, R.id.list, ConstraintSet.TOP, 0);
        constraintSet.applyTo(constraintLayout);

        autoCompleteTextView = new AutoCompleteTextView(this);//(AutoCompleteTextView) findViewById(R.id.enterZipCode);
        autoCompleteTextView.setId(R.id.enterZipCode);
        autoCompleteTextView.setTextAppearance(R.style.Medium);
        //autoCompleteTextView.setWidth(300);
        //autoCompleteTextView.setHeight(60);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setInputType(InputType.TYPE_CLASS_NUMBER);

        InputFilter[] numSymbolsFilters = new InputFilter[1];
        numSymbolsFilters[0] = new InputFilter.LengthFilter(5);

        autoCompleteTextView.setFilters(numSymbolsFilters);
        autoCompleteTextView.setVisibility(View.INVISIBLE);

        constraintParams.topMargin = 15;
        constraintParams.bottomMargin = 15;
        constraintParams.leftMargin = 15;
        constraintParams.rightMargin = 15;

        constraintParams.horizontalChainStyle = CHAIN_PACKED;
        //constraintParams.topToBottom = R.id.headerLayout;
        //constraintParams.bottomToTop = R.id.list;
        //constraintParams.leftToRight = R.id.zipCodeLabel;

        autoCompleteTextView.setLayoutParams(constraintParams);

        constraintLayout.addView(autoCompleteTextView,2);
        constraintSet.clone(constraintLayout);
        constraintSet.connect(autoCompleteTextView.getId(), ConstraintSet.TOP, R.id.headerLayout, ConstraintSet.BOTTOM, 10);
        constraintSet.connect(autoCompleteTextView.getId(), ConstraintSet.BOTTOM, R.id.list, ConstraintSet.TOP, 10);
        constraintSet.connect(autoCompleteTextView.getId(), ConstraintSet.LEFT, R.id.zipCodeLabel, ConstraintSet.RIGHT, 10);
        constraintSet.applyTo(constraintLayout);

        addZipButton = (Button) findViewById(R.id.button_add_zip);

        addZipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //zipCodeLinearLayout.setVisibility(View.VISIBLE);
                zipCodeLabel.setVisibility(View.VISIBLE);
                autoCompleteTextView.setVisibility(View.VISIBLE);
                autoCompleteTextView.setFocusableInTouchMode(true);
                autoCompleteTextView.requestFocus();

                SystemUtil.showSoftKeyboard(HomeActivity.this);
            }
        });

        autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                Integer zipCode = (Integer) item;

                new AsyncParseTask().execute(   zipCode.toString(),
                                                Constants.GET_CITIES_IDS_BY_SAME_ZIP);
            }
        });

		if(WeatherApplication.getCitiesDetails() == null)
		    new AsyncParseTask().execute(   Constants.CITIES_INITIAL_ENTRIES_NUM,
                                            Constants.GATHER_INITIAL_DATA);
		else
            setHomeAndAutoCompleteAdapters();


	}
}