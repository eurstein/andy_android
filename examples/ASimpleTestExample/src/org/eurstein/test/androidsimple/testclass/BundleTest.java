package org.eurstein.test.androidsimple.testclass;

import org.eurstein.test.androidsimple.utils.AndyLog;

import android.os.Bundle;

public class BundleTest {
    private static final String TAG = "BundleTest";
    
    public static void test() {
        Bundle bundle = new Bundle();
        bundle.putString("key1", "value1");
        bundle.putString(null, "nullkey");
        bundle.putString("nullvalue", null);
        bundle.putString(null, null);
        AndyLog.d(TAG, bundle.toString());
    }
}
