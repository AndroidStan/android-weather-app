package com.weather.android.util;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import com.weather.android.R;
import com.weather.android.to.TemperatureTO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import static java.lang.Thread.sleep;

public class SystemUtil {

	public static boolean call(Context context, String number){
		boolean flag = false;
		try{
			number = PhoneNumberUtils.convertKeypadLettersToDigits(number);
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(Uri.parse("tel:" + number));
			context.startActivity(callIntent);
			flag = true;
		}
		catch (Exception e){
			Logger.e(Log.getStackTraceString(e));
		}
		return flag;
	}

	public static boolean  launchUrl(Activity activity, String url){
		boolean flag = false;
		try{
			if (url.length()>0) {
				Uri myurl = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, myurl);
				activity.startActivity(intent);	
				flag = true;
			}
		}
		catch (Exception e){
			Logger.e(Log.getStackTraceString(e));
		}
		return flag;
	}

	public static String[] getArrayListAsStringArray(ArrayList<String> list){
		String[] results = new String[list.size()];
		for (int i=0; i<list.size(); i++){
			results[i] = list.get(i);
		}
		return results;
	}


	public static boolean sendEmail(Context ctx, String body, String subject, String recipient){
		boolean flag = false;
		try {
			final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); 
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject); 
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body); 
			String[] emails = new String[1];
			emails[0] = recipient;
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emails);
			emailIntent.setType("message/rfc822");
			ctx.startActivity(emailIntent);
		} 
		catch (Throwable tr){
			Logger.e(Log.getStackTraceString(tr));				
		}

		return flag;
	}
	
	public static boolean sendEmailWithLinkAndChooser(Activity activity, String subject, String body, String linkableTextAtTheEnd, String linkUrl){
		boolean flag = false;
		try {
			SpannableString ss = new SpannableString(linkableTextAtTheEnd); 
			ss.setSpan(new URLSpan(linkUrl), 0, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE); 
			SpannableStringBuilder ssb = new SpannableStringBuilder(body).append(' ').append(ss); 
			Intent i = new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, ssb).putExtra(Intent.EXTRA_SUBJECT,subject) 
								                                        .setType("message/rfc822"); //$NON-NLS-1$ 
			activity.startActivity(Intent.createChooser(i, activity.getString(R.string.select_program)));
			flag = true;
		} 
		catch (Throwable tr){
			Logger.e(Log.getStackTraceString(tr));
			
		}
		
		return flag;
		
	}

	public static void showSoftKeyboard(Activity activity){
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}

	public static void hideSoftKeyboard(Activity activity){
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
	}

	public static TemperatureTO convertFromKelvinToFahrenheitAndCelcius(Double kelvinTemp){
		//Conversion temperature formulas following below:
		//T(°F) = T(K) × 9/5 - 459.67
		//T(°C) = T(K) - 273.15
		Double  fahrenheitTemp = (kelvinTemp*9/5) - 459.67,
				celsiusTemp = kelvinTemp - 273.15;

		DecimalFormat df = new DecimalFormat("##.#");

		TemperatureTO temperatureData = new TemperatureTO();
		temperatureData.setFormattedCelciusTemp(df.format(celsiusTemp));
		temperatureData.setFormattedFahrenheitTemp(df.format(fahrenheitTemp));

		return temperatureData;
	}

	public static String getformattedDate(Long unixSeconds){
		Date date = new Date(unixSeconds*1000L);
		// the format of your date
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		// give a timezone formatting reference
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		return sdf.format(date);
	}

	public static void sleepForMillis(long milliseconds){
		try {
			sleep(milliseconds);
		}catch(InterruptedException e){

		}
	}
}
