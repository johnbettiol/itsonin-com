package com.itsonin.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SafeDateFormat {

	private static final ThreadLocal<Map<String, DateFormat>> threadLocal = new ThreadLocal<Map<String, DateFormat>>() {
		@Override
		protected Map<String, DateFormat> initialValue() {
			return new ConcurrentHashMap<String, DateFormat>();
		}
	};

	public static DateFormat forPattern(String pattern) {
		Map<String, DateFormat> cache = threadLocal.get();
		DateFormat df = cache.get(pattern);
		if (df == null) {
			df = new SimpleDateFormat(pattern);
			cache.put(pattern, df);
		}
		return df;
	}
}
