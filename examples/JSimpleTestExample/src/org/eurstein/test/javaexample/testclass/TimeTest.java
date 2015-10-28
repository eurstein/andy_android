
package org.eurstein.test.javaexample.testclass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.eurstein.test.javaexample.utils.AndyLog;

public class TimeTest {

	// public protected private abstract static final transient volatile
	// synchronized native strictfp

	// private final String TAG = this.getClass().getSimpleName();
	private static final String TAG = "TimeTest";

	// ---------------------------- only java ----------------------------
	// 利用Data获取
	public static void testDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		{
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String dateStr = formatter.format(curDate);
			AndyLog.i(TAG, dateStr);
		}

		{
			Date curDate = new Date();
			String dateStr = formatter.format(curDate);
			AndyLog.i(TAG, dateStr);
		}
	}
	
	// 利用Calendar获取
	public static void testCalendar() {

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);

		AndyLog.i(TAG, year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
	}
	
}
