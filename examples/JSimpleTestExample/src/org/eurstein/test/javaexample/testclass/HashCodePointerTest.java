package org.eurstein.test.javaexample.testclass;

import org.eurstein.test.javaexample.utils.AndyLog;

public class HashCodePointerTest {
	
	private static final String TAG = "HashCodePointerTest";
	
	// “testObjectA.hashCode()” 和 “System.identityHashCode(testObjectA)”区别
	public static void test() {
		// 没有重载hashCode，两者 一致
		TestObjectA testObjectA = new TestObjectA(1, "1");
		AndyLog.i(TAG, "testObjectA.hashCode(): " + testObjectA.hashCode());
		AndyLog.i(TAG, "System.identityHashCode(testObjectA): " + System.identityHashCode(testObjectA));
		
		// 当重载了hashCode，两者的意义不相同
		// testObjectA.hashCode(): 返回的是重载函数的结果
		// System.identityHashCode(testObjectA): 返回的是默认hashCode方法的结果
		TestObjectB testObjectB = new TestObjectB(1, "1");
		AndyLog.i(TAG, "testObjectB.hashCode(): " + testObjectB.hashCode());
		AndyLog.i(TAG, "System.identityHashCode(testObjectB): " + System.identityHashCode(testObjectB));
		
		// 和上面的比较，不同对象的System.identityHashCode(testObjectB2)输出不同
		TestObjectB testObjectB2 = new TestObjectB(1, "1");
		AndyLog.i(TAG, "testObjectB2.hashCode(): " + testObjectB2.hashCode());
		AndyLog.i(TAG, "System.identityHashCode(testObjectB2): " + System.identityHashCode(testObjectB2));
		
		if (System.identityHashCode(testObjectB2) != System.identityHashCode(testObjectB)) {
			AndyLog.i(TAG, "the System.identityHashCode value of two objects is not same");
		} else {
			AndyLog.i(TAG, "the System.identityHashCode value of two objects is same");
		}
		
		// 两个对象的hashcode相同，但是equals不为true
		AndyLog.i(TAG, "testObjectB.toString(): " + testObjectB.toString());
		AndyLog.i(TAG, "testObjectB2.toString(): " + testObjectB2.toString());
		AndyLog.i(TAG, "testObjectB.equals(testObjectB2): " + testObjectB.equals(testObjectB2));
	}
	
	private static class TestObjectA {
		private int i;
		private String s;
		
		public TestObjectA(int i, String s) {
			this.i = i;
			this.s = s;
		}
	}
	
	private static class TestObjectB {
		private int i;
		private String s;
		
		public TestObjectB(int i, String s) {
			this.i = i;
			this.s = s;
		}
		
		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			
			return i;
		}
	}
	
	
}
