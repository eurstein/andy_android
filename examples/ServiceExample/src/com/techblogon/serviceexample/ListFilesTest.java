
package com.techblogon.serviceexample;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eurstein.test.androidsimple.utils.AndyLog;

import android.text.TextUtils;

public class ListFilesTest {
    private static final String TAG = "ListFilesTest";

    public static void scanFile(String root)
    {
        // 优化中
        if (TextUtils.isEmpty(root))
        {
            return;
        }

        File rootDir = new File(root);
        if (rootDir != null && rootDir.exists())
        {
            AndyLog.i(TAG, "rootDir.listFiles() before");
            File[] files = rootDir.listFiles();
            AndyLog.i(TAG, "rootDir.listFiles() after");
        }
    }
}
