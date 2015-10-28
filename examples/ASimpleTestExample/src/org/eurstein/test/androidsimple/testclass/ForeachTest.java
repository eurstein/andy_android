
package org.eurstein.test.androidsimple.testclass;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.eurstein.test.androidsimple.utils.AndyLog;

public class ForeachTest {

	private static final String TAG = "ForeachTest";

	public static void test() {
		test1();
//		test2();
//		test3();
//		test4();
//		test5();
	}

	private static void test1() {
		try {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("key1", "value1");
			map.put("key2", "value2");
			map.put("key3", "value3");

			for (String value : map.values()) {
				AndyLog.i(TAG, "test1, value: " + value);
				map.remove("key2");
			}
		} catch (Exception e) {
			AndyLog.e(TAG, "test1 Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void test2() {
		try {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("key1", "value1");
			map.put("key2", "value2");
			map.put("key3", "value3");

			Iterator<String> it = map.values().iterator();
			for (; it.hasNext();) {
				AndyLog.i(TAG, "test2, value: " + it.next());
				map.remove("key2");
			}
		} catch (Exception e) {
			AndyLog.e(TAG, "test2 Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void test3() {
		try {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("key1", "value1");
			map.put("key2", "value2");
			map.put("key3", "value3");

			for (String value : map.values()) {
				AndyLog.i(TAG, "test3, value: " + value);
				// map.remove("key2");
				map.put("key4", "value4");
			}
		} catch (Exception e) {
			AndyLog.e(TAG, "test3 Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void test4() {
		try {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("key1", "value1");
			map.put("key2", "value2");
			map.put("key3", "value3");

			Iterator<String> it = map.values().iterator();
			for (; it.hasNext();) {
				AndyLog.i(TAG, "test4, value: " + it.next());
				// map.remove("key2");
				map.put("key4", "value4");
			}
		} catch (Exception e) {
			AndyLog.e(TAG, "test4 Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
	private static void test5() {
		for (int i = 0; i < 100; i++) {
			map.put("key" + i, "value" + i);
			AndyLog.i(TAG, "test5 put(key" + i + " , value" + i + ")");
		}

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1);
					for (int i = 100; i < 10000; i++) {
						map.put("key" + i, "value" + i);
						AndyLog.i(TAG, "test5 put(key" + i + " , value" + i + ")");
					}
				} catch (Exception e) {
					AndyLog.e(TAG, "test5 1 Exception: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
			
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(60);
					for (String value : map.values()) {
						AndyLog.i(TAG, "test5, value: " + value);
					}
				} catch (Exception e) {
					AndyLog.e(TAG, "test5 2 Exception: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}
}
