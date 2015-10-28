package org.eurstein.test.javaexample.testclass;

import org.eurstein.test.javaexample.utils.AndyLog;

public class InstanceofTest {
    
    private static final String TAG = "InstanceofTest";
    
    public static void test() {
        String s = null;
        // 不会崩溃
        if (s instanceof String) {
            AndyLog.i(TAG, "s instanceof String = true");
        }
        AndyLog.i(TAG, "s instanceof String no crash");
    }
}
