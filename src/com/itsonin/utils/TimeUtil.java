package com.itsonin.utils;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * @author nkislitsin
 *
 */
public class TimeUtil {

	public static String prettyFormat(Date date) {

		DateTime now = new DateTime();
		Period period = new Period(new DateTime(date), now);

		PeriodFormatterBuilder fmt;
		 
		if(period.getYears() > 0) {
			 fmt = new PeriodFormatterBuilder().appendYears().appendSuffix(" year", " years");
		} else {
			if(period.getMonths() > 0) {
				fmt = new PeriodFormatterBuilder().appendMonths().appendSuffix(" month", " months");			
			} else if (period.getDays() > 0) {
				fmt = new PeriodFormatterBuilder().appendDays().appendSuffix(" day", " days");							
			} else if (period.getHours() > 0){
				fmt = new PeriodFormatterBuilder().appendHours().appendSuffix(" hour", " hours");											
			} else if (period.getMinutes() > 0) {
				fmt = new PeriodFormatterBuilder().appendMinutes().appendSuffix(" min");				
			} else if (period.getSeconds() > 0){
				fmt = new PeriodFormatterBuilder().appendSeconds().appendSuffix(" sec");
			} else {
				return "moment ago";
			}
		}
		
		return fmt.appendSuffix(" ago").toFormatter().print(period);
	}
}
