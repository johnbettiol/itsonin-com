package com.itsonin.resteasy;

import java.lang.annotation.Annotation;
import java.util.Date;

import org.jboss.resteasy.spi.StringParameterUnmarshaller;
import org.jboss.resteasy.util.FindAnnotation;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author nkislitsin
 * 
 */
public class DateFormatter implements StringParameterUnmarshaller<Date> {
	private DateTimeFormatter formatter;

	public void setAnnotations(Annotation[] annotations) {
		CustomDateFormat format = FindAnnotation.findAnnotation(annotations, CustomDateFormat.class);
		formatter = DateTimeFormat.forPattern(format.value());
	}

	public Date fromString(String str) {
		return new Date(formatter.parseDateTime(str).getMillis());
	}
}
