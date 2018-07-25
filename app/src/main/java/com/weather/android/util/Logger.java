package com.weather.android.util;

import android.util.Log;


public class Logger {
	
	private static String OFFSET = " >>> ";
	
	private static String TAG = "JRD";
	private static boolean DEBUG = true;
	
	
	public enum Flag {VERBOSE, ERROR,INFO, DEBUG, WARN};
	
	
	private static void printLine(Flag flag, String msg, String tag){
		if (DEBUG) {
					if (msg==null) msg="MESSAGE IS NULL";
					String workStr = msg;
					print(flag,workStr, tag);
		}
	}
	
	private static void print( Flag flag, String msg, String tag) {
		switch (flag) {
		case ERROR : 	Log.e(tag,msg);
						break;
		case VERBOSE : 	Log.v(tag,msg);
						break;
		case DEBUG : 	Log.d(tag,msg);
						break;
		case INFO : 	Log.i(tag,msg);
						break;
		case WARN : 	Log.w(tag,msg);
						break;
		default :		Log.i(tag, "??? > "+msg);
						break;
		}
		
	}

	


	public static void e(String msg){
		printLine(Flag.ERROR, OFFSET+msg, TAG);
	}
	
	public static void d(String msg){
		printLine(Flag.DEBUG, OFFSET+msg, TAG);
	}
	
	public static void i(String msg){
		printLine(Flag.INFO, OFFSET+msg, TAG);
	}
	
	public static void v(String msg){
		printLine(Flag.VERBOSE, OFFSET+msg, TAG);
	}
	
	public static void w(String msg){
		printLine(Flag.WARN, OFFSET+msg, TAG);
	}

}
