package org.eurstein.test.javaexample.testclass;

import org.eurstein.test.javaexample.utils.AndyLog;

/**
 * Created by andygzyu on 2015/4/14.
 */
public class WhileTest {

    private static final String TAG = WhileTest.class.getSimpleName();

    public static void test() {
        int i = 10;
        while (i > 0) {
            i--;
        }

        i = 10;
        while (i-- > 0) {
            ;
        }

        i = 10;
        while (i-- > 0) {
        }

        AndyLog.e(TAG, "while end");
    }
}
