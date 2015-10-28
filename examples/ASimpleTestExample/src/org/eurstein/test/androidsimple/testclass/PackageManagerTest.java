
package org.eurstein.test.androidsimple.testclass;

import java.io.File;

import org.eurstein.test.androidsimple.utils.AndyLog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

public class PackageManagerTest {

    private static final String TAG = "PackageManagerTest";

    private static final String rootPath = Environment.getExternalStorageDirectory().getPath()
            + "/local/CrashingApks";
    private static final String crashingApk1 = rootPath + "/" + "bad_compressed_size_0.apk";
    private static final String crashingApk2 = rootPath + "/" + "bad_filename_size_max.apk";
    private static final String crashingApk3 = rootPath + "/" + "bad_filename_size_max2.apk";
    private static final String crashingApk4 = rootPath + "/" + "bad_offset_wrongfile.apk";
    private static final String crashingApk5 = rootPath + "/" + "bad_uncompressed_size_max.apk";
    private static final String crashingApk6 = rootPath + "/" + "DeskClock_WinzipEncrypted.apk";
    private static final String crashingApk7 = rootPath + "/" + "file_data_corrupt.apk";
    private static final String crashingApk8 = rootPath + "/" + "MobileAssistant_1.apk";

    public static void test(Context context) {
        // getPackageArchiveInfo(context, crashingApk1);
        // getPackageArchiveInfo(context, crashingApk2);
        // getPackageArchiveInfo(context, crashingApk3);
        // getPackageArchiveInfo(context, crashingApk4);
        // getPackageArchiveInfo(context, crashingApk6);
        // getPackageArchiveInfo(context, crashingApk7);
        getPackageArchiveInfo(context, crashingApk8);
        getPackageArchiveInfo(context, crashingApk5);
    }

    // 研究pm.getPackageArchiveInfo API处理破损包时的native crash
    private static PackageInfo getPackageArchiveInfo(Context context, String filePath) {
        AndyLog.i(TAG, "filePath exist: " + new File(filePath).exists());
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(filePath, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
            AndyLog.i(TAG, "getPackageArchiveInfo crash: " + filePath);
            return null;
        }
        AndyLog.i(TAG, "getPackageArchiveInfo not crash: " + filePath);
        return pkgInfo;
    }
}
