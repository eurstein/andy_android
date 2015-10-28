package org.eurstein.test.androidsimple.testclass;

import android.content.Context;
import org.eurstein.test.androidsimple.sourceclass.SignatureGet;
import org.eurstein.test.androidsimple.utils.AndyConstant;
import org.eurstein.test.androidsimple.utils.AndyLog;

/**
 * Created by andygzyu on 2015/3/22.
 */
public class SignatureGetTest {

    private static final String TAG = SignatureGetTest.class.getSimpleName();

    public static void test(Context context) {
        AndyLog.d(TAG, SignatureGet.getSelfSignInfo(context));
        AndyLog.d(TAG,
                SignatureGet.getInstalledApkSignInfo(context, AndyConstant.packageName));
        AndyLog.d(TAG, SignatureGet.getUninstallApkSigntureInfo(context, "/sdcard/local/yyb.apk"));
        AndyLog.d(TAG, SignatureGet.getUninstallApkSigntureInfo1("/sdcard/local/yyb.apk"));
    }
}
