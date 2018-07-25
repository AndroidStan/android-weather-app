package com.weather.android.activity;

import com.weather.android.R;
import com.weather.android.application.WeatherApplication;
import com.weather.android.util.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.io.IOException;

public class SplashScreenActivity extends Activity {

	private static boolean 	interrupted = false,
						cancelled = false;
	

	private int splashTime = 2000; // time to display the splash screen in ms
	private TextView versionField;
	
	private SplashThread splashThread;
	private final String THREAD_RUNNING = "Splash_thread_running";
	private boolean splashThreadWasStarted;
	private WeatherApplication app;
	
	
	Handler handlerVersion = new Handler(){
		public void handleMessage (Message msg) {
			versionField.setText(getString(R.string.version)+": "+app.getVersionCustomNumber());
			versionField.setVisibility(View.VISIBLE);
		}

	};


	public void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);

		if(savedInstanceState != null){
			 splashThreadWasStarted = savedInstanceState.getBoolean(THREAD_RUNNING);		
			}
		 else {
			 splashThreadWasStarted = false;
			 cancelled = false;
			 interrupted = false;
		 }
	 		 
		 versionField = (TextView) findViewById(R.id.version);	
		 
		 //thread for getting app and version:
		Thread versionThread = new Thread() {
			final 
		        @Override
		        public void run() {
		            try {		 
		            	app = (WeatherApplication) getApplication();
		            	handlerVersion.sendEmptyMessage(0);			            	
		            	if (!splashThreadWasStarted){
		            		splashThread = new SplashThread();
	          		  		splashThread.start();
		            		}
		                }
		            catch (Throwable tr){
		            	Logger.e(Log.getStackTraceString(tr));
		            }
		        }
		};
		versionThread.start();

	}
	
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	        interrupted = true;
	    }
	    return true;
		 
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if (keyCode==KeyEvent.KEYCODE_BACK){
			cancelled = true;
		}
		return super.onKeyDown(keyCode, event);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ignore menu
	return true;

	}
	
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(THREAD_RUNNING, splashThreadWasStarted);
		super.onSaveInstanceState(outState);
	}
	
	
    protected class SplashThread extends Thread {
        @Override
        public void run() {
        	splashThreadWasStarted = true;
            try {
                int waited = 0;
       		 
                while(!interrupted && !cancelled && (waited < splashTime)) {
                    sleep(100);
                    if(!interrupted && !cancelled) {
                        waited += 100;
                    }
                }
            } catch(InterruptedException e) {
                // do nothing
            } finally {

                if (!cancelled) {
                	startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                	finish();
                }
 
            }
        }
    };
}
