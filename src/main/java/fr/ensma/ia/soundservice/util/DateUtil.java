package fr.ensma.ia.soundservice.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
@Deprecated 
public class DateUtil {

	static SimpleDateFormat sdf;
	
	static{
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public static String setDate(Date date) {
		
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		return sdf.format(date);
		
	}
}
