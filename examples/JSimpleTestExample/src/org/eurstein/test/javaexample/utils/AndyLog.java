
package org.eurstein.test.javaexample.utils;

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
			System.out.println("[" + t + "] " + m);
		}
	}

	public static void i(String t, String m) {
		if (isForDebug()) {
			if (m == null) {
				m = "............";
			}
			System.out.println("[" + t + "] " + m);
		}
	}

	public static void d(String t, String m) {
		if (isForDebug()) {
			if (m == null) {
				m = "............";
			}
			System.out.println("[" + t + "] " + m);
		}
	}

	public static void w(String t, String m) {
		if (isForDebug()) {
			if (m == null) {
				m = "............";
			}
			System.out.println("[" + t + "] " + m);
		}
	}

	public static void e(String t, String m) {
		if (isForDebug()) {
			if (m == null) {
				m = "............";
			}
			System.out.println("[" + t + "] " + m);
		}
	}

}
