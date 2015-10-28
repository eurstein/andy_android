package org.eurstein.test.androidsimple.testclass;

import org.eurstein.test.androidsimple.sourceclass.TestParcelableActivity;
import org.eurstein.test.androidsimple.sourceclass.TestParcelableObject;
import org.eurstein.test.androidsimple.utils.AndyLog;

import android.content.Context;
import android.content.Intent;

public class ParcelableObjectTest {

	private static final String TAG = "ParcelableObjectTest";
			
	public static void test(Context context) {
		AndyLog.i(TAG, "ParcelableObjectTest:test enter");
		TestParcelableObject object = new TestParcelableObject();
		object.intValue = 1;
		object.strValue1 = null;
		object.strValue2 = "str2";
		object.byteValue = 3;
		object.boolValue1 = false;
		object.boolValue2 = true;
		object.longValue = 2;
		
		Intent i = new Intent(context, TestParcelableActivity.class);
		i.putExtra("TestParcelableObject", object);
		context.startActivity(i);
	}
}
