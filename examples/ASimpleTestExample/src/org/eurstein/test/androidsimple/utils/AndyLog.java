
package org.eurstein.test.androidsimple.utils;

import android.util.Log;

public class AndyLog {

	protected static boolean mHardDebugFlag = true;

	public static boolean isForDebug() {
		return mHardDebugFlag;
	}

	public static void v(String t, String m) {
		if (isForDebug()) {
			if (m == null) {
				m = "............";
			}
			Log.v(t, m);
		}
	}

	public static void i(String t, String m) {
		if (isForDebug()) {
			if (m == null) {
				m = "............";
			}
			Log.i(t, m);
		}
	}

	public static void d(String t, String m) {
		if (isForDebug()) {
			if (m == null) {
				m = "............";
			}
			Log.d(t, m);
		}
	}

	public static void w(String t, String m) {
		if (isForDebug()) {
			if (m == null) {
				m = "............";
			}
			Log.w(t, m);
		}
	}

	public static void e(String t, String m) {
		if (isForDebug()) {
			if (m == null) {
				m = "............";
			}
			Log.e(t, m);
		}
	}

}
