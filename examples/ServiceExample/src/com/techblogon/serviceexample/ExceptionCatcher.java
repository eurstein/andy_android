
package com.techblogon.serviceexample;

import java.lang.Thread.UncaughtExceptionHandler;

import org.eurstein.test.androidsimple.utils.AndyLog;

import android.content.Context;

public class ExceptionCatcher implements UncaughtExceptionHandler
{

    public static final String TAG = "CrashHandler";
    private static ExceptionCatcher instance = null;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private ExceptionCatcher() {
    }

    public static ExceptionCatcher getInstance() {
        if (instance == null)
            instance = new ExceptionCatcher();
        return instance;
    }

    public void init(Context ctx) {
        AndyLog.d(TAG, "ExceptionCatcher init, Thread: " + Thread.currentThread().getName() + "-"
                + System.identityHashCode(Thread.currentThread()) + "-" + Thread.currentThread().hashCode());
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this); // 是针对整个进程生效的
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            AndyLog.d(TAG, "uncaughtException call defaultHandler");
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            AndyLog.d(TAG, "uncaughtException kill and exit");
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    private boolean handleException(Throwable ex) {
        AndyLog.d(TAG, "handleException");
        if (ex == null) {
            return true;
        }

        AndyLog.i(TAG, "ex: " + ex.getClass().getSimpleName());

        if (ex instanceof NullPointerException) {
            return true; // 处理空指针
        } else if (ex instanceof ArrayIndexOutOfBoundsException) {
            return false; // 不处理数组越界
        }

        return true;
    }

}
