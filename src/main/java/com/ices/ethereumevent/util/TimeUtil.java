package com.ices.ethereumevent.util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Random;


public class TimeUtil {
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static String getTimeFormat(Date date) {
		return simpleDateFormat.format(date);
	}
	
	public static String getNowDateFormat() {
		return simpleDateFormat.format(new Date());
	}
	
	public static Duration randomDurationFast() {
		return Duration.ofHours(1 + new Random().nextInt(1));
	}
	
	public static Duration randomDurationNormal() {
		return Duration.ofHours(24 + new Random().nextInt(4));
	}
}
