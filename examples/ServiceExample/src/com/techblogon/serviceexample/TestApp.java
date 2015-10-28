
package com.techblogon.serviceexample;

import org.eurstein.test.androidsimple.utils.AndyLog;
import org.eurstein.test.androidsimple.utils.ProcessUtils;

import android.app.Application;

public class TestApp extends Application {

    private static final String TAG = "TestApp";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        String pkgName = this.getPackageName();
        String processName = ProcessUtils.getProcessName(android.os.Process.myPid());
        String curProcessName = ProcessUtils.getCurProcessName(this);

        AndyLog.d(TAG, "pkgName: " + pkgName + " processName: " + processName + " curProcessName: "
                + curProcessName);
        AndyLog.d(TAG, "TestApp onCreate, Thread: " + Thread.currentThread().getName() + "-"
                + System.identityHashCode(Thread.currentThread()) + "-"
                + Thread.currentThread().hashCode());

        if (curProcessName.equals(pkgName + ":MyService")) { // 仅在MyService进程中启动异常捕获
            ExceptionCatcher.getInstance().init(getApplicationContext());
        }
    }

}
