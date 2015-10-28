package org.eurstein.test.androidsimple.testclass;

import org.eurstein.test.androidsimple.utils.AndyLog;

import org.eurstein.test.androidsimple.utils.MD5;

public class MD5Test {

	private static final String TAG = "MD5Test";
	
	public static void test() {
		String str1 = "china";
		
		String str2 = "中国";
		
		AndyLog.i(TAG, "MD5.toMD5(\"china\"): " + MD5.toMD5(str1));
		AndyLog.i(TAG, "MD5.toMD5_new(\"china\"): " + MD5.toMD5(str1));

		AndyLog.i(TAG, "MD5.toMD5(\"中国\"): " + MD5.toMD5(str2));
		AndyLog.i(TAG, "MD5.toMD5_new(\"中国\"): " + MD5.toMD5(str2));
	}
}
