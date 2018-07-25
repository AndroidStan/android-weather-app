package com.weather.android.to;

public class ErrorMessageTO {
	
	private Throwable throwable;
	private String displayableMessage;
	
	
	public ErrorMessageTO(Throwable throwable, String displayableMessage){
		super();
		this.throwable = throwable;
		this.displayableMessage=displayableMessage;
	}
	
	public Throwable getError(){
		return throwable;
	}
	
	public String getDisplayableMessage(){
		return displayableMessage;
	}
	

}
