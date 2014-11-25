package com.fahad.ornob.sust.hitthedeal.contants;

import java.util.Calendar;
import java.util.TimeZone;

public class CommonMethod {

	public static  long currentTimeFrom1970(){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.clear();
		calendar.set(2011, Calendar.OCTOBER, 1);
		long millSecondsSinceEpoch = calendar.getTimeInMillis();
		
		return  millSecondsSinceEpoch;
	}
	
	
}
