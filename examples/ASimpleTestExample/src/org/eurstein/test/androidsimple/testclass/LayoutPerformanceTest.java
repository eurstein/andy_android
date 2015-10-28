
package org.eurstein.test.androidsimple.testclass;

import org.eurstein.test.androidsimple.sourceclass.LayoutPerformanceActivity;
import org.eurstein.test.androidsimple.utils.AndyLog;

import android.content.Context;
import android.content.Intent;

public class LayoutPerformanceTest {
    private static final String TAG = "LayoutPerformanceTest";

    // 改变activity_testlayoutperformance.xml和item_testlayoutperformance.xml中的wrap_content=>fill_content,
    // 观察 AndyLog.i("p", "getview被执行");日志输出情况
    public static void test(Context context) {
        AndyLog.i(TAG, "LayoutPerformanceTest:test enter");

        Intent i = new Intent(context, LayoutPerformanceActivity.class);
        context.startActivity(i);
    }
}
