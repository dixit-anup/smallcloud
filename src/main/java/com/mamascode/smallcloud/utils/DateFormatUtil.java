package com.mamascode.smallcloud.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateFormatUtil {
	//////////////////////////////////////////////////////////////////////////////////////////
	// getDateFormat
	public static String getDateFormat(Date date) {
		return getDate(DateToGregorianCalendar(date));
	}
	
	public static String getDateFormat(Timestamp timestamp) {
		return getDate(TimestampToGregorianCalendar(timestamp));
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////	
	// format conversion
	public static String getDatetimeFormat(Date date) {
		return getDatetime(DateToGregorianCalendar(date));
	}
	
	public static String getDatetimeFormat(Timestamp timestamp) {
		return getDatetime(TimestampToGregorianCalendar(timestamp));
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	// 
	
	// DateToGregorianCalendar: Date format ---> GregorianCalendar
	private static GregorianCalendar DateToGregorianCalendar(Date date) {
		GregorianCalendar calendar = null;
		
		if(date != null) {
			calendar = new GregorianCalendar();
			
			if(calendar != null)
				calendar.setTimeInMillis(date.getTime());
		}
		
		return calendar;
	}
	
	// TimeStampToGregorianCalendar: Timestamp format ---> GregorianCalendar
	private static GregorianCalendar TimestampToGregorianCalendar(Timestamp timestamp) {
		GregorianCalendar calendar = null;
		
		if(timestamp != null) { 
			calendar = new GregorianCalendar();
			
			if(calendar != null)
				calendar.setTimeInMillis(timestamp.getTime());
		}
		
		return calendar;
	}
	
	// getDate
	private static String getDate(Calendar calendar) {
		 StringBuilder builder = new StringBuilder();
		 
		 if(calendar != null) {
			 builder.append(calendar.get(Calendar.YEAR))
			 .append("/").append(String.format("%02d", (calendar.get(Calendar.MONTH) + 1)))
			.append("/").append(String.format("%02d",calendar.get(Calendar.DATE)));
		 }
		 
		 return builder.toString();
	}
	
	// getDatetime
	private static String getDatetime(Calendar calendar) {
		 StringBuilder builder = new StringBuilder();
		 
		 if(calendar != null) {
			 int hour = calendar.get(Calendar.HOUR);
			 if(hour == 0) hour = 12;
			 
			 builder.append(calendar.get(Calendar.YEAR))
			 .append("/").append(String.format("%02d", (calendar.get(Calendar.MONTH) + 1)))
			 .append("/").append(String.format("%02d",calendar.get(Calendar.DATE)))
			 .append(" ").append((calendar.get(Calendar.AM_PM) == Calendar.AM) ? "am " : "pm�� ")
			 .append(String.format("%2d", hour))
			 .append(":").append(String.format("%02d", calendar.get(Calendar.MINUTE)));
		 }
		 
		 return builder.toString();
	}
}
