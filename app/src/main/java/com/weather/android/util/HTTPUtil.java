package com.weather.android.util;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import android.graphics.drawable.Drawable;
import java.net.URLEncoder;

public class HTTPUtil
{
	static private HttpsURLConnection m_Connection;
	static private InputStream m_InputStream;
	
	public HTTPUtil()
	{
		m_Connection = null;
		m_InputStream = null;
	}
	
	private static void openHttpConnection(String doctorsSearchURL/*, final String username, final String password*/)
	{
		int resCode = -1; 
		Logger.i("Make connection to "+ doctorsSearchURL);
		try 
		{ 
			URL url = new URL(doctorsSearchURL); 
			URLConnection urlConn = null;
			
			if(url!=null)
				urlConn = url.openConnection(); 
			
			if (!(urlConn instanceof HttpsURLConnection)) {
				throw new IOException ("URL is not an Https URL"); 
			} 

			m_Connection = (HttpsURLConnection)urlConn; 
			m_Connection.setAllowUserInteraction(false);
		    m_Connection.setInstanceFollowRedirects(true); 
		    m_Connection.setRequestMethod("GET"); 
		    m_Connection.setConnectTimeout(5000);
		    
			/*Authenticator.setDefault(new Authenticator()
			{
	            protected PasswordAuthentication getPasswordAuthentication() 
	            {
	                return new PasswordAuthentication(username, password.toCharArray());
	            }
	        });*/
			    
		    m_Connection.connect(); 
		    resCode = m_Connection.getResponseCode(); 
		    Logger.i("http response code = "+resCode);   
		    
		    if (resCode == HttpsURLConnection.HTTP_OK) {
		    	Logger.i("getting input stream");   
		        m_InputStream = m_Connection.getInputStream(); 
		    } 
		   
		} 	
		catch (MalformedURLException e) 
		{ 
			Logger.e(Log.getStackTraceString(e));
		} 
		
		catch (IOException e) 
		{ 
			Logger.e(Log.getStackTraceString(e));
		}
		
	}
	
	private static String convertStreamToString()
	{
	  String line = null, retVal = null;
	  StringBuilder builder = new StringBuilder();
	  BufferedReader reader = null;
	  
	  if(m_InputStream!=null)
	  { 
		  reader = new BufferedReader(new InputStreamReader(m_InputStream));
		  m_InputStream = null;
	  }
		  
	  try
	  {
		if(builder!=null && reader!=null)
		  while ((line = reader.readLine()) != null) 
			builder.append(line);
	  }
	  catch (Exception e) 
	  {
		 Logger.e(Log.getStackTraceString(e));
	  }
		
	  if(builder!=null)
		  retVal=builder.toString();
	 
	  return retVal;
	}
	
	public static String getServerResponse(String doctorsSearchURL/*, String username, String password*/)
	{ 
		openHttpConnection(doctorsSearchURL/*, username, password*/);
		String response = convertStreamToString();
		closeHttpConnection();
		Logger.i("http response : "+response);
		return response;
	} 
	
	private static void closeHttpConnection()
	{
		if(m_Connection != null)
		{	
			m_Connection.disconnect();
			m_Connection = null;
		}
	}
	
	public static String getEncodedString(String s){
        if (s.length()>0)
        {
            try
            {
                s=URLEncoder.encode(s,"UTF-8");
            }
            catch (Exception e)
            {
                Logger.e("encoding exception : "+Log.getStackTraceString(e));
            }
        }
        return s;
    }
	
	public static Drawable LoadImageFromWebOperations(String url)
    {
		InputStream is = null;
		Drawable drawable = null;
		
        try
        {
        	if(url!=null)
        		is = (InputStream) new URL(url).getContent();
        	
        	if(is!=null)
            	drawable = Drawable.createFromStream(is, "src");
        }
        catch (IOException e) 
        {
        	Logger.e(Log.getStackTraceString(e));
        }
        
        return drawable;
    }
}