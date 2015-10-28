
package org.eurstein.test.androidsimple.utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.app.ActivityManager;
import android.content.Context;

public class ProcessUtils {

    public static String getProcessName(int pid) {
        String line = "/proc/" + pid + "/cmdline";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(line);
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);

            return EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}
