package com.weather.android.util;


import java.util.ArrayList;

import com.weather.android.R;

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
	
}
