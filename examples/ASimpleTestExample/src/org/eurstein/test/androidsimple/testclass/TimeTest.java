
package org.eurstein.test.androidsimple.testclass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.eurstein.test.androidsimple.utils.AndyLog;

import android.content.ContentResolver;
import android.content.Context;
import android.text.format.Time;

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
	
	// ----------------------------- android -----------------------------
	// 利用Time获取
	public static void testTime() {
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		{
//			t.setToNow(); // 取得系统时间
			int year = t.year;
			int month = t.month;
			int day = t.monthDay;
			int hour = t.hour; // 0-23
			int minute = t.minute;
			int second = t.second;
			AndyLog.i(TAG, year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
		}

		{
			t.setToNow(); // 取得系统时间，可重复设置为当前时间
			int year = t.year;
			int month = t.month;
			int day = t.monthDay;
			int hour = t.hour; // 0-23
			int minute = t.minute;
			int second = t.second;
			AndyLog.i(TAG, year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
		}
	}

	// 如何获取Android系统时间是24小时制还是12小时制
	public static void testContentResolver(Context context) {
		ContentResolver cv = context.getContentResolver();
		String strTimeFormat = android.provider.Settings.System.getString(cv,
				android.provider.Settings.System.TIME_12_24);
		if (strTimeFormat.equals("24")) {
			AndyLog.i(TAG, "24");
		} else {
			AndyLog.i(TAG, "12");
		}
	}

}
