package com.weather.android.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

public class DataBaseHelperUtil extends SQLiteOpenHelper {
	 
    //The Android's default system path of your application database.
    private static String DB_PATH;
    private static String DB_NAME = "USA_Cities.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelperUtil(Context context) {
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
        
        DB_PATH = myContext.getFilesDir().getParentFile().getPath() + "/databases/";
    }
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

    	if(!dbExist) {
    		//By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
    			copyDataBase();
    		}
        	catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
 
    	SQLiteDatabase checkDB = null;
 
    	try {
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}
    	catch(SQLiteException e) {
            Logger.i("The USA_Cities.db database doesn't exist yet!");
    	}
 
    	if(checkDB != null)
    		checkDB.close(); 

 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0) {
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }
 
    public void openDataBase() throws SQLException {

    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
 
    public void setCountryAndZipcode(Context context) {

    	String SELECT_QUERY = "SELECT * FROM CityDetails";
        Cursor cursor = myDataBase.rawQuery(SELECT_QUERY, null);
        Integer recordsNum = cursor.getCount();

        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        
        if (recordsNum > 0) {

        	cursor.moveToFirst();
            for(int i=0; i < recordsNum; i++) {
                List<Address> addresses;

            	try{
            	    addresses = geoCoder.getFromLocation(Double.valueOf(cursor.getString(4)),
                                                        Double.valueOf(cursor.getString(3)),
                                                        1);

                    ContentValues values = new ContentValues();
                    values.put("zipcode", addresses.get(0).getPostalCode());
                    values.put("statename", addresses.get(0).getAdminArea());

                    String selection = "id=?";
                    String[] selectionArgs = { cursor.getString(0) };

                    int count = myDataBase.update("CityDetails",
                                                        values,
                                                        selection,
                                                        selectionArgs);

                } catch(IOException e){
                    Logger.e(Log.getStackTraceString(e));
                }
                catch(RuntimeException rte){
                    Logger.e(Log.getStackTraceString(rte));
                }

            	cursor.moveToNext();
            }

            myDataBase.close();
        }
    }
    
    @Override
	public synchronized void close() 
    {
    	if(myDataBase != null)
    	   myDataBase.close();
 
    	super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
 
	}

	// Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
}