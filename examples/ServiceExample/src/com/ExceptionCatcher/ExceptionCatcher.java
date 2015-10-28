
package com.ExceptionCatcher;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.util.Log;

public class ExceptionCatcher implements UncaughtExceptionHandler
{

    public static final String TAG = "CrashHandler";
    private static ExceptionCatcher instance = null;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public static ExceptionCatcher getInstance() {
        if (instance == null)
            instance = new ExceptionCatcher();
        return instance;
    }

    public void init(Context ctx) {
        Log.d(TAG, "ExceptionCatcher init, Thread: " + Thread.currentThread().getName() + "-"
                + System.identityHashCode(Thread.currentThread()) + "-"
                + Thread.currentThread().hashCode());
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this); // 是针对整个进程生效的
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            Log.d(TAG, "uncaughtException call defaultHandler");
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            Log.d(TAG, "uncaughtException kill and exit");
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    private boolean handleException(Throwable ex) {
        Log.d(TAG, "handleException");
        if (ex == null) {
            return true;
        }

        ex.printStackTrace();

        // java层crash，删除坏包
        // GetApkInfoService.self().deleteProcessingApk();

        return true;
    }

}
