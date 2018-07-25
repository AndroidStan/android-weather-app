package com.weather.android.util;

import com.weather.android.R;
import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;



public class MenuUtil {


	public static boolean mainMenuActions(Activity activity, Context ctx, MenuItem item){
		switch (item.getItemId()) {
			case R.id.menu_about:
									//activity.startActivity(new Intent(ctx, AboutActivity.class));
									return true;

			case R.id.menu_terms:
									//sendEmail(activity);
									return true;			

			case R.id.menu_share :
									sendEmail(activity);
									return true;
			case R.id.menu_help :
									//activity.startActivity(new Intent(ctx, HelpActivity.class));
									return true;	
			}
		return false;	
	}
	
	private static void sendEmail(Activity activity){

		String subject = String.format(activity.getString(R.string.email_subject), activity.getString(R.string.app_name));
		//Logger.i("subject="+subject);
		String body = String.format(activity.getString(R.string.email_body),
									activity.getString(R.string.app_name),
									activity.getString(R.string.app_description));
		Logger.i("body="+body);
		SystemUtil.sendEmailWithLinkAndChooser(activity, body, subject,
				activity.getString(R.string.app_url),activity.getString(R.string.app_url));
	}
	

	
}
