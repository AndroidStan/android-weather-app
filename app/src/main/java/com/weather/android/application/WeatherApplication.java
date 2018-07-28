package com.weather.android.application;

import com.weather.android.sqliteAsset.AssetSQLiteOpenHelperFactory;
import com.weather.android.to.WeatherTO;
import com.weather.android.util.DataBaseHelperUtil;
import com.weather.android.util.Logger;
import com.weather.android.util.room.CityDetails;
import com.weather.android.util.room.USACitiesDatabase;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

import lombok.Data;

@Data
public class WeatherApplication extends Application {
	
	private String appPackageName;
	
	private String versionCustomNumber;	// like 1.2.23
	private int versionSingleIntCode; // like 4


	private static WeatherTO weatherDetails;
	private static DataBaseHelperUtil databaseHelperUtil;
	private static USACitiesDatabase usaCitiesDatabase;

	private static List<CityDetails> citiesDetails;
	private static List<Integer> suggestedCitiesZips;
	private static List<CityDetails> citiesDetailsBySameZipCode;

	/*private void copyDatabaseIfNotExist(){
		try {
			app.getDatabaseHelperUtil().createDataBase();
		}
		catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
	}*/

    //not deleted, but just commented, in order to show
    //how I filled in the zipcode and statename columns
    //of the CityDetails table at the USA_Cities.db

	/*private void setCountryAndZipCodes(){
		WeatherApplication.getDatabaseHelperUtil().openDataBase();
		WeatherApplication.getDatabaseHelperUtil().setCountryAndZipcode(getApplicationContext());
		WeatherApplication.getDatabaseHelperUtil().close();
	}*/


	@Override
	public void onCreate()
	{
		super.onCreate();
		initFields();
		databaseHelperUtil = new DataBaseHelperUtil(getApplicationContext());

        //setCountryAndZipCodes();
        //copyDatabaseIfNotExist();
	}

	@Override
    public void onTerminate(){
        closeRoomDatabase();
	    super.onTerminate();
    }

	static final Migration MIGRATION_1_2 = new Migration(1, 2) {
		@Override
		public void migrate(SupportSQLiteDatabase database) {
		}
	};

	public static DataBaseHelperUtil getDatabaseHelperUtil()
	{
		return databaseHelperUtil;
	}

	public static USACitiesDatabase getUsaCitiesDatabase(Context context, String databaseName){
		usaCitiesDatabase = Room.databaseBuilder(context,
													USACitiesDatabase.class, databaseName)
													.openHelperFactory(new AssetSQLiteOpenHelperFactory())
													.addMigrations(MIGRATION_1_2)
													.build();

		return usaCitiesDatabase;
	}

	private static void closeRoomDatabase(){
		if(usaCitiesDatabase != null)
			usaCitiesDatabase.close();
	}

	public static void setCitiesDetails(List<CityDetails> citiesDetailsList){
	    citiesDetails = citiesDetailsList;
    }

    public static List<CityDetails> getCitiesDetails(){
	    return citiesDetails;
    }

    public static void setSuggestedCitiesZips(List<Integer> suggestedZips){
	    suggestedCitiesZips = suggestedZips;
    }

    public static List<Integer> getSuggestedCitiesZips(){
	    return suggestedCitiesZips;
    }

    //citiesDetailsBySameZipCode
    public static void setCitiesDetailsBySameZipCode(List<CityDetails> citiesDetailsBySameZipInput){
        citiesDetailsBySameZipCode = citiesDetailsBySameZipInput;
    }

    public static List<CityDetails> getCitiesDetailsBySameZipCode(){
	    return citiesDetailsBySameZipCode;
    }

    public static void clearCitiesIdsBySameZipCode(){
	    if(citiesDetailsBySameZipCode != null)
            citiesDetailsBySameZipCode.clear();
    }

    public static void clearCitiesDetails(){
	    if(citiesDetails != null)
	        citiesDetails.clear();
    }

	public static void setWeather(WeatherTO weather){
		weatherDetails = weather;
	}

	public static WeatherTO getWeatherDetails(){
		return weatherDetails;
	}

	public static void clearWeather(){
	    if(weatherDetails != null)
			weatherDetails.clearWeatherDetails();
    }

	public String getVersionCustomNumber(){
		return versionCustomNumber;
	}

	public int getVersionSingleIntCode(){
		return versionSingleIntCode;
	}
	
	private void initFields(){
				
		try{

			PackageManager manager = getPackageManager();

			appPackageName = getPackageName();
			PackageInfo info = manager.getPackageInfo(appPackageName, 0);
			
			versionSingleIntCode = info.versionCode;		//like 4
			versionCustomNumber = info.versionName; 	//like 1.0.0			
		}
		catch (Exception e){
			Logger.e(Log.getStackTraceString(e));
			appPackageName="NA";
			versionSingleIntCode=0;
			versionCustomNumber="NA";
		}
		
	}
}