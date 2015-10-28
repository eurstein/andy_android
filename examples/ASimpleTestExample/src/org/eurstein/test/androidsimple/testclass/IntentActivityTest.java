package org.eurstein.test.androidsimple.testclass;

import org.eurstein.test.androidsimple.sourceclass.TestIntentActivity1;
import org.eurstein.test.androidsimple.utils.AndyLog;

import android.content.Context;
import android.content.Intent;

public class IntentActivityTest {
	
	private static final String TAG = "IntentActivityTest";
	
	public static void test(Context context) {
		AndyLog.i(TAG, "IntentActivityTest:test enter");
		
		Intent i = new Intent(context, TestIntentActivity1.class);
		context.startActivity(i);
	}
	
}
