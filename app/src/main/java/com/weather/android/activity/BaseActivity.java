package com.weather.android.activity;

import com.weather.android.R;
import com.weather.android.application.WeatherApplication;
import com.weather.android.to.ErrorMessageTO;
import com.weather.android.util.Logger;
import com.weather.android.util.MenuUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class BaseActivity extends Activity{

	
	protected ProgressDialog progressDialog;
	AlertDialog alertDialog;
	
	private AsyncTask<?,?,?> currentTask;
	public WeatherApplication app;
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        app = (WeatherApplication) getApplication();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuUtil.mainMenuActions(this, this.getApplicationContext(), item);
	}
	
	public void cancelTask(AsyncTask<?,?,?> task) {
		if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
			task = null;
		}
	}

	
	public void showProgressDialog(String title, int message, AsyncTask<?,?,?> task, boolean canCancel){
		currentTask = task;		
		progressDialog = new ProgressDialog(this); //), R.style.ProgressTheme);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(canCancel);
		progressDialog.setTitle(title);
		progressDialog.setMessage(getText(message).toString());
		if (canCancel) progressDialog.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				if (currentTask!=null) cancelTask(currentTask);
				currentTask=null;

			}
		});
		
		progressDialog.show();
	}
	
	public void dismissDialog() {
		if (progressDialog != null){
			progressDialog.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cancelTask(currentTask);		
		dismissDialog();		
		currentTask = null; 
	}
	
	public void handleTechnicalException(ErrorMessageTO errorMessage){
		handleTechnicalException(errorMessage, null, null);		
	}
	
	public void handleTechnicalException(ErrorMessageTO errorMessage, 
			OnClickListener onClickListener, OnCancelListener onCancelListener){
		
		String message="";
		if (errorMessage!=null && errorMessage.getDisplayableMessage()!=null) {
					message = errorMessage.getDisplayableMessage();
		}
		showDialog(getString(R.string.technical_problem), message,onClickListener,null,onCancelListener );
	}

	public void createDialogAndHandleTechnicalExceptionAndFinishActivity(ErrorMessageTO errorMessage) {	
		handleTechnicalException(errorMessage,
			new OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
					finish();			// go back or stay on the page?		
				}				
			},
			new OnCancelListener(){
				public void onCancel(DialogInterface arg0) {
					finish();			// go back or stay on the page??		
				}	
			}	
			);	 
}

	public void showDialogAndFinishActivity(String title, String message)
	{
		showDialog(	title, 
					message,
					new OnClickListener()
					{
						public void onClick(DialogInterface arg0, int arg1) 
						{
							finish();			// go back or stay on the page?		
						}				
					}, 
					null, 
					null);
	}

	public void showDialog(String title, String message,OnClickListener positiveOnClickListener, OnClickListener negativeOnClickListener, OnCancelListener onCancelListener){
		// positive must be !=null. if negative is null, will ask OK. if not, will ask Yes/No.
		try {
			Builder builder = new AlertDialog.Builder(this); 
			builder.setTitle(title);
			builder.setMessage(message); 
			if (negativeOnClickListener!=null) {
				builder.setPositiveButton(R.string.yes, positiveOnClickListener); 
				builder.setNegativeButton(R.string.no, negativeOnClickListener);
				
			}
			else {
				builder.setPositiveButton(R.string.ok, positiveOnClickListener); 
			}
			if (onCancelListener != null){
				builder.setOnCancelListener(onCancelListener);
			}


			builder.show();	
		} catch (Exception e) {
			Logger.e(Log.getStackTraceString(e));
		}		
	}
	
	public void showDialogWithItems(String title, String[] items, DialogInterface.OnClickListener listener){
		try{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setItems(items, listener).create();
		builder.show();
		}
		catch (Exception e){
			Logger.e(Log.getStackTraceString(e));
		}
	}

	public void makeLongToast(String err) {
		try{
		Toast toast = Toast.makeText(this, err, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP, 0, 90);
		toast.show();
	}
	catch (Exception e){
		Logger.e(Log.getStackTraceString(e));
	}
	}
	
	public void makeShortToast(String err) {
		Toast toast = Toast.makeText(this, err, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 90);
		toast.show();
	}
	
	public boolean launchUrl(String url)
	{
		boolean flag = false;
		try
		{
			if (url.length()>0) 
			{
				Uri myurl = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, myurl);
				startActivity(intent);	
				flag = true;
			}
		}
		catch (Exception e)
		{
			Logger.e(Log.getStackTraceString(e));
		}
		return flag;
	}
}

