
package com.ExceptionCatcher;

import android.util.Log;

public class NativeExceptionHandler {

    private static final String TAG = "NativeExceptionHandler";

    static {
        try {
            System.loadLibrary("nativeexceptionhandler");
            Log.i(TAG, "load library nativeexceptionhandler success");
        } catch (Throwable e) {
            Log.i(TAG, "load library nativeexceptionhandler failed");
            e.printStackTrace();
        }
    }

    // NativeExceptionHandler针对进程的，进程内任一线程中设置，所有线程生效
    public static native int init();
    // native发生crash时由native调用
    public static void crashReport() {
        Log.i(TAG, "Java crashReport was called!");
    }
}
