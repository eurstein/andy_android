package org.eurstein.utils;

import android.util.Log;

public class AndyLog {

    protected static boolean mHardDebugFlag = true;

    private static final String prefix = "andygzyu-";

    public static boolean isForDebug() {
        return mHardDebugFlag;
    }

    public static void v(String t, String m) {
        if (isForDebug()) {
            if (m == null) {
                m = "............";
            }
            Log.v(prefix + t, m);
        }
    }

    public static void i(String t, String m) {
        if (isForDebug()) {
            if (m == null) {
                m = "............";
            }
            Log.i(prefix + t, m);
        }
    }

    public static void d(String t, String m) {
        if (isForDebug()) {
            if (m == null) {
                m = "............";
            }
            Log.d(prefix + t, m);
        }
    }

    public static void w(String t, String m) {
        if (isForDebug()) {
            if (m == null) {
                m = "............";
            }
            Log.w(prefix + t, m);
        }
    }

    public static void e(String t, String m) {
        if (isForDebug()) {
            if (m == null) {
                m = "............";
            }
            Log.e(prefix + t, m);
        }
    }

}
