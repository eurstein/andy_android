package org.eurstein.test.androidsimple.testclass;

import org.eurstein.test.androidsimple.sourceclass.TestPendingIntentActivity;
import org.eurstein.test.androidsimple.utils.AndyLog;

import android.content.Context;
import android.content.Intent;

public class PendingintentTest {

	private static final String TAG = "PendingintentTest";
	
	public static void test(Context context) {
		AndyLog.i(TAG, "PendingintentTest:test enter");
		
		Intent i = new Intent(context, TestPendingIntentActivity.class);
		context.startActivity(i);
	}
	
}
